package com.raizesdonordeste.backend.dto;

import com.raizesdonordeste.backend.domain.enums.StatusPagamento;
import jakarta.validation.constraints.NotNull;

public record PagamentoWebhookRequest(

        @NotNull
        Long pedidoId,

        @NotNull
        String gatewayTransacaoId,

        @NotNull
        StatusPagamento status
) {
}
