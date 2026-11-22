package com.raizesdonordeste.backend.dto;

import com.raizesdonordeste.backend.domain.enums.CanalVenda;
import com.raizesdonordeste.backend.domain.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        Long unidadeId,
        Long clienteId,
        CanalVenda canal,
        StatusPedido status,
        BigDecimal valorTotal,
        LocalDateTime criadoEm,
        List<ItemPedidoResponse> itens
) {
    public record ItemPedidoResponse(Long produtoId, String nomeProduto, int quantidade, BigDecimal precoUnitario) {
    }
}
