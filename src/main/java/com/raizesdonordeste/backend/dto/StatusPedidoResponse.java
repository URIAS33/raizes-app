package com.raizesdonordeste.backend.dto;

import com.raizesdonordeste.backend.domain.enums.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;

public record StatusPedidoResponse(
        Long pedidoId,
        StatusPedido statusAtual,
        List<Evento> historico
) {
    public record Evento(StatusPedido status, LocalDateTime criadoEm, String observacao) {
    }
}
