package com.raizesdonordeste.backend.dto;

import java.math.BigDecimal;

public record CardapioItemResponse(
        Long produtoId,
        String nome,
        String descricao,
        BigDecimal preco
) {
}
