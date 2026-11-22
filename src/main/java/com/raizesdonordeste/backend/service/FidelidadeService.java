package com.raizesdonordeste.backend.service;

import com.raizesdonordeste.backend.domain.Cliente;
import com.raizesdonordeste.backend.domain.ConsentimentoLgpd;
import com.raizesdonordeste.backend.domain.Pedido;
import com.raizesdonordeste.backend.domain.TransacaoPontos;
import com.raizesdonordeste.backend.domain.enums.FinalidadeConsentimento;
import com.raizesdonordeste.backend.domain.enums.TipoTransacaoPontos;
import com.raizesdonordeste.backend.dto.ConsentimentoRequest;
import com.raizesdonordeste.backend.dto.ConsentimentoResponse;
import com.raizesdonordeste.backend.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.backend.exception.RegraDeNegocioException;
import com.raizesdonordeste.backend.repository.ClienteRepository;
import com.raizesdonordeste.backend.repository.ConsentimentoLgpdRepository;
import com.raizesdonordeste.backend.repository.TransacaoPontosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class FidelidadeService {

    private static final BigDecimal REAIS_POR_PONTO = BigDecimal.valueOf(10);

    private final ClienteRepository clienteRepository;
    private final ConsentimentoLgpdRepository consentimentoLgpdRepository;
    private final TransacaoPontosRepository transacaoPontosRepository;
    private final AuditoriaService auditoriaService;

    public FidelidadeService(ClienteRepository clienteRepository,
                              ConsentimentoLgpdRepository consentimentoLgpdRepository,
                              TransacaoPontosRepository transacaoPontosRepository,
                              AuditoriaService auditoriaService) {
        this.clienteRepository = clienteRepository;
        this.consentimentoLgpdRepository = consentimentoLgpdRepository;
        this.transacaoPontosRepository = transacaoPontosRepository;
        this.auditoriaService = auditoriaService;
    }

    @Transactional
    public ConsentimentoResponse registrarConsentimento(ConsentimentoRequest request) {
        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente nao encontrado: " + request.clienteId()));

        ConsentimentoLgpd consentimento = ConsentimentoLgpd.builder()
                .cliente(cliente)
                .finalidade(request.finalidade())
                .aceito(request.aceito())
                .dataConsentimento(LocalDateTime.now())
                .build();

        ConsentimentoLgpd salvo = consentimentoLgpdRepository.save(consentimento);
        return new ConsentimentoResponse(salvo.getId(), cliente.getId(), salvo.getFinalidade(), salvo.isAceito(), salvo.getDataConsentimento());
    }

    @Transactional
    public void acumularPontos(Cliente cliente, Pedido pedido) {
        boolean podeFidelizar = consentimentoLgpdRepository
                .findTopByClienteIdAndFinalidadeOrderByDataConsentimentoDesc(cliente.getId(), FinalidadeConsentimento.FIDELIZACAO)
                .map(ConsentimentoLgpd::vigente)
                .orElse(false);

        if (!podeFidelizar) {
            return;
        }

        int pontos = pedido.getValorTotal().divide(REAIS_POR_PONTO, 0, RoundingMode.DOWN).intValue();
        if (pontos <= 0) {
            return;
        }

        transacaoPontosRepository.save(TransacaoPontos.builder()
                .cliente(cliente)
                .pedido(pedido)
                .pontos(pontos)
                .tipo(TipoTransacaoPontos.ACUMULO)
                .criadoEm(LocalDateTime.now())
                .build());
    }

    @Transactional
    public void anonimizarCliente(Long clienteId, String motivoSolicitacao) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente nao encontrado: " + clienteId));

        if (cliente.isAnonimizado()) {
            throw new RegraDeNegocioException("Cliente " + clienteId + " ja foi anonimizado anteriormente");
        }

        cliente.anonimizar();
        clienteRepository.save(cliente);
        auditoriaService.registrarAcessoDadosPessoais(clienteId, "Anonimizacao solicitada: " + motivoSolicitacao);
    }
}
