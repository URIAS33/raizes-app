package com.raizesdonordeste.backend.dto;

import com.raizesdonordeste.backend.domain.enums.CanalVenda;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoRequest(

        @NotNull
        Long unidadeId,

        Long clienteId,

        @NotNull
        CanalVenda canal,

        @NotEmpty
        @Valid
        List<ItemPedidoRequest> itens
) {
}
