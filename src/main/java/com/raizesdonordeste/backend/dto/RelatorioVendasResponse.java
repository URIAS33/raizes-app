package com.raizesdonordeste.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public record RelatorioVendasResponse(
        List<VendaPorUnidade> vendasPorUnidade,
        List<ProdutoMaisVendido> produtosMaisVendidos,
        BigDecimal faturamentoTotal
) {
    public record VendaPorUnidade(Long unidadeId, String nomeUnidade, long quantidadePedidos, BigDecimal totalVendido) {
    }

    public record ProdutoMaisVendido(Long produtoId, String nomeProduto, long quantidadeVendida) {
    }
}
