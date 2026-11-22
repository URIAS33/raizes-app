package com.raizesdonordeste.backend.service;

import com.raizesdonordeste.backend.domain.*;
import com.raizesdonordeste.backend.domain.enums.StatusPedido;
import com.raizesdonordeste.backend.dto.ItemPedidoRequest;
import com.raizesdonordeste.backend.dto.PedidoRequest;
import com.raizesdonordeste.backend.dto.PedidoResponse;
import com.raizesdonordeste.backend.dto.StatusPedidoResponse;
import com.raizesdonordeste.backend.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.backend.exception.RegraDeNegocioException;
import com.raizesdonordeste.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoStatusHistoricoRepository historicoRepository;
    private final UnidadeRepository unidadeRepository;
    private final ClienteRepository clienteRepository;
    private final CardapioUnidadeRepository cardapioUnidadeRepository;
    private final EstoqueService estoqueService;
    private final AuditoriaService auditoriaService;

    public PedidoService(PedidoRepository pedidoRepository,
                          PedidoStatusHistoricoRepository historicoRepository,
                          UnidadeRepository unidadeRepository,
                          ClienteRepository clienteRepository,
                          CardapioUnidadeRepository cardapioUnidadeRepository,
                          EstoqueService estoqueService,
                          AuditoriaService auditoriaService) {
        this.pedidoRepository = pedidoRepository;
        this.historicoRepository = historicoRepository;
        this.unidadeRepository = unidadeRepository;
        this.clienteRepository = clienteRepository;
        this.cardapioUnidadeRepository = cardapioUnidadeRepository;
        this.estoqueService = estoqueService;
        this.auditoriaService = auditoriaService;
    }

    @Transactional
    public PedidoResponse criar(PedidoRequest request) {
        Unidade unidade = unidadeRepository.findById(request.unidadeId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade nao encontrada: " + request.unidadeId()));

        Cliente cliente = null;
        if (request.clienteId() != null) {
            cliente = clienteRepository.findById(request.clienteId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente nao encontrado: " + request.clienteId()));
        }

        LocalDateTime agora = LocalDateTime.now();
        Pedido pedido = Pedido.builder()
                .unidade(unidade)
                .cliente(cliente)
                .canal(request.canal())
                .status(StatusPedido.CRIADO)
                .valorTotal(BigDecimal.ZERO)
                .criadoEm(agora)
                .atualizadoEm(agora)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedidoRequest itemRequest : request.itens()) {
            CardapioUnidade cardapioUnidade = cardapioUnidadeRepository
                    .findByUnidadeIdAndProdutoId(request.unidadeId(), itemRequest.produtoId())
                    .filter(CardapioUnidade::isDisponivel)
                    .orElseThrow(() -> new RegraDeNegocioException(
                            "Produto " + itemRequest.produtoId() + " nao esta disponivel na unidade " + request.unidadeId()));

            estoqueService.validarDisponibilidade(request.unidadeId(), itemRequest.produtoId(), itemRequest.quantidade());

            ItemPedido item = ItemPedido.builder()
                    .produto(cardapioUnidade.getProduto())
                    .quantidade(itemRequest.quantidade())
                    .precoUnitario(cardapioUnidade.precoEfetivo())
                    .build();
            pedido.adicionarItem(item);
            total = total.add(item.subtotal());
        }
        pedido.setValorTotal(total);

        Pedido salvo = pedidoRepository.save(pedido);
        registrarHistorico(salvo, StatusPedido.CRIADO, "Pedido criado via " + request.canal());

        for (ItemPedido item : salvo.getItens()) {
            estoqueService.baixarEstoque(request.unidadeId(), item.getProduto().getId(), item.getQuantidade());
        }

        return toResponse(salvo);
    }

    @Transactional
    public void cancelar(Long pedidoId, String motivo) {
        Pedido pedido = buscarOuErro(pedidoId);
        if (!pedido.podeSerCancelado()) {
            throw new RegraDeNegocioException("Pedido " + pedidoId + " nao pode mais ser cancelado (status atual: " + pedido.getStatus() + ")");
        }
        pedido.setStatus(StatusPedido.CANCELADO);
        pedido.setAtualizadoEm(LocalDateTime.now());
        pedidoRepository.save(pedido);
        registrarHistorico(pedido, StatusPedido.CANCELADO, motivo);
        auditoriaService.registrarCancelamento(pedido, motivo);
    }

    @Transactional
    public void atualizarStatus(Long pedidoId, StatusPedido novoStatus, String observacao) {
        Pedido pedido = buscarOuErro(pedidoId);
        pedido.setStatus(novoStatus);
        pedido.setAtualizadoEm(LocalDateTime.now());
        pedidoRepository.save(pedido);
        registrarHistorico(pedido, novoStatus, observacao);
    }

    public StatusPedidoResponse consultarStatus(Long pedidoId) {
        Pedido pedido = buscarOuErro(pedidoId);
        List<StatusPedidoResponse.Evento> eventos = historicoRepository.findByPedidoIdOrderByCriadoEmAsc(pedidoId).stream()
                .map(h -> new StatusPedidoResponse.Evento(h.getStatus(), h.getCriadoEm(), h.getObservacao()))
                .toList();
        return new StatusPedidoResponse(pedido.getId(), pedido.getStatus(), eventos);
    }

    public Pedido buscarOuErro(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido nao encontrado: " + pedidoId));
    }

    private void registrarHistorico(Pedido pedido, StatusPedido status, String observacao) {
        historicoRepository.save(PedidoStatusHistorico.builder()
                .pedido(pedido)
                .status(status)
                .observacao(observacao)
                .criadoEm(LocalDateTime.now())
                .build());
    }

    private PedidoResponse toResponse(Pedido pedido) {
        List<PedidoResponse.ItemPedidoResponse> itens = pedido.getItens().stream()
                .map(i -> new PedidoResponse.ItemPedidoResponse(
                        i.getProduto().getId(), i.getProduto().getNome(), i.getQuantidade(), i.getPrecoUnitario()))
                .toList();

        return new PedidoResponse(
                pedido.getId(),
                pedido.getUnidade().getId(),
                pedido.getCliente() != null ? pedido.getCliente().getId() : null,
                pedido.getCanal(),
                pedido.getStatus(),
                pedido.getValorTotal(),
                pedido.getCriadoEm(),
                itens
        );
    }
}
