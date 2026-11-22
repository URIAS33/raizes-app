package com.raizesdonordeste.backend.dto;

import com.raizesdonordeste.backend.domain.enums.TipoOperacaoAuditoria;

import java.time.LocalDateTime;

public record AuditoriaResponse(
        Long id,
        TipoOperacaoAuditoria tipo,
        String entidadeTipo,
        Long entidadeId,
        Long funcionarioId,
        String descricao,
        LocalDateTime criadoEm
) {
}
