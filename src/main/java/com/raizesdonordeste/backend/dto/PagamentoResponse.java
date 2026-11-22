package com.raizesdonordeste.backend.dto;

import com.raizesdonordeste.backend.domain.enums.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoResponse(
        Long id,
        Long pedidoId,
        StatusPagamento status,
        BigDecimal valor,
        String gatewayTransacaoId,
        LocalDateTime solicitadoEm,
        LocalDateTime confirmadoEm
) {
}
