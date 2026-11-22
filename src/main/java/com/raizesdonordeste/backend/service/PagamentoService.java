package com.raizesdonordeste.backend.service;

import com.raizesdonordeste.backend.domain.Pagamento;
import com.raizesdonordeste.backend.domain.Pedido;
import com.raizesdonordeste.backend.domain.enums.StatusPagamento;
import com.raizesdonordeste.backend.domain.enums.StatusPedido;
import com.raizesdonordeste.backend.dto.PagamentoResponse;
import com.raizesdonordeste.backend.exception.RegraDeNegocioException;
import com.raizesdonordeste.backend.gateway.PaymentGatewayClient;
import com.raizesdonordeste.backend.gateway.RespostaPagamentoGateway;
import com.raizesdonordeste.backend.gateway.SolicitacaoPagamentoGateway;
import com.raizesdonordeste.backend.repository.PagamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final PaymentGatewayClient paymentGatewayClient;
    private final PedidoService pedidoService;
    private final FidelidadeService fidelidadeService;

    public PagamentoService(PagamentoRepository pagamentoRepository,
                             PaymentGatewayClient paymentGatewayClient,
                             PedidoService pedidoService,
                             FidelidadeService fidelidadeService) {
        this.pagamentoRepository = pagamentoRepository;
        this.paymentGatewayClient = paymentGatewayClient;
        this.pedidoService = pedidoService;
        this.fidelidadeService = fidelidadeService;
    }

    @Transactional
    public PagamentoResponse solicitarPagamento(Long pedidoId) {
        Pedido pedido = pedidoService.buscarOuErro(pedidoId);

        RespostaPagamentoGateway resposta = paymentGatewayClient.solicitarPagamento(
                new SolicitacaoPagamentoGateway(pedido.getId(), pedido.getValorTotal()));

        Pagamento pagamento = Pagamento.builder()
                .pedido(pedido)
                .status(StatusPagamento.PENDENTE)
                .valor(pedido.getValorTotal())
                .gatewayTransacaoId(resposta.gatewayTransacaoId())
                .solicitadoEm(LocalDateTime.now())
                .build();

        pagamentoRepository.save(pagamento);
        pedidoService.atualizarStatus(pedidoId, StatusPedido.AGUARDANDO_PAGAMENTO, "Pagamento solicitado ao gateway externo");

        return toResponse(pagamento);
    }

    @Transactional
    public void processarWebhook(Long pedidoId, String gatewayTransacaoId, StatusPagamento statusRecebido) {
        Pagamento pagamento = pagamentoRepository.findTopByPedidoIdOrderBySolicitadoEmDesc(pedidoId)
                .orElseThrow(() -> new RegraDeNegocioException("Nenhuma solicitacao de pagamento encontrada para o pedido " + pedidoId));

        if (!pagamento.getGatewayTransacaoId().equals(gatewayTransacaoId)) {
            throw new RegraDeNegocioException("Transacao do gateway nao corresponde a solicitacao registrada");
        }

        if (pagamento.getStatus() != StatusPagamento.PENDENTE) {
            return;
        }

        pagamento.setStatus(statusRecebido);
        pagamento.setConfirmadoEm(LocalDateTime.now());
        pagamentoRepository.save(pagamento);

        if (statusRecebido == StatusPagamento.CONFIRMADO) {
            pedidoService.atualizarStatus(pedidoId, StatusPedido.PAGO, "Pagamento confirmado pelo gateway externo");
            if (pagamento.getPedido().getCliente() != null) {
                fidelidadeService.acumularPontos(pagamento.getPedido().getCliente(), pagamento.getPedido());
            }
        } else {
            pedidoService.atualizarStatus(pedidoId, StatusPedido.CANCELADO, "Pagamento negado pelo gateway externo");
        }
    }

    private PagamentoResponse toResponse(Pagamento pagamento) {
        return new PagamentoResponse(
                pagamento.getId(),
                pagamento.getPedido().getId(),
                pagamento.getStatus(),
                pagamento.getValor(),
                pagamento.getGatewayTransacaoId(),
                pagamento.getSolicitadoEm(),
                pagamento.getConfirmadoEm()
        );
    }
}
