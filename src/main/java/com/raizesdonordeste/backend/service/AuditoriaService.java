package com.raizesdonordeste.backend.service;

import com.raizesdonordeste.backend.domain.AuditoriaOperacao;
import com.raizesdonordeste.backend.domain.Estoque;
import com.raizesdonordeste.backend.domain.Pedido;
import com.raizesdonordeste.backend.domain.enums.TipoOperacaoAuditoria;
import com.raizesdonordeste.backend.dto.AuditoriaResponse;
import com.raizesdonordeste.backend.repository.AuditoriaOperacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditoriaService {

    private final AuditoriaOperacaoRepository auditoriaOperacaoRepository;

    public AuditoriaService(AuditoriaOperacaoRepository auditoriaOperacaoRepository) {
        this.auditoriaOperacaoRepository = auditoriaOperacaoRepository;
    }

    public void registrarCancelamento(Pedido pedido, String motivo) {
        registrar(TipoOperacaoAuditoria.CANCELAMENTO, "PEDIDO", pedido.getId(), motivo);
    }

    public void registrarAjusteEstoque(Estoque estoque, String motivo) {
        registrar(TipoOperacaoAuditoria.AJUSTE_ESTOQUE, "ESTOQUE", estoque.getId(), motivo);
    }

    public void registrarAcessoDadosPessoais(Long clienteId, String motivo) {
        registrar(TipoOperacaoAuditoria.ACESSO_DADOS_PESSOAIS, "CLIENTE", clienteId, motivo);
    }

    private void registrar(TipoOperacaoAuditoria tipo, String entidadeTipo, Long entidadeId, String descricao) {
        AuditoriaOperacao operacao = AuditoriaOperacao.builder()
                .tipo(tipo)
                .entidadeTipo(entidadeTipo)
                .entidadeId(entidadeId)
                .descricao(descricao)
                .criadoEm(LocalDateTime.now())
                .build();
        auditoriaOperacaoRepository.save(operacao);
    }

    public List<AuditoriaResponse> listarPorTipo(TipoOperacaoAuditoria tipo) {
        return auditoriaOperacaoRepository.findByTipo(tipo).stream()
                .map(op -> new AuditoriaResponse(
                        op.getId(),
                        op.getTipo(),
                        op.getEntidadeTipo(),
                        op.getEntidadeId(),
                        op.getFuncionario() != null ? op.getFuncionario().getId() : null,
                        op.getDescricao(),
                        op.getCriadoEm()))
                .toList();
    }
}
