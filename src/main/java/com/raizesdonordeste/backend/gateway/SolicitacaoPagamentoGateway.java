package com.raizesdonordeste.backend.gateway;

import java.math.BigDecimal;

public record SolicitacaoPagamentoGateway(Long pedidoId, BigDecimal valor) {
}
