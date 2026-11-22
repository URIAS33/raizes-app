package com.raizesdonordeste.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPedidoRequest(

        @NotNull
        Long produtoId,

        @Min(1)
        int quantidade
) {
}
