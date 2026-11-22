package com.raizesdonordeste.backend.service;

import com.raizesdonordeste.backend.domain.ItemPedido;
import com.raizesdonordeste.backend.domain.Pedido;
import com.raizesdonordeste.backend.domain.enums.StatusPedido;
import com.raizesdonordeste.backend.dto.RelatorioVendasResponse;
import com.raizesdonordeste.backend.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    private final PedidoRepository pedidoRepository;

    public RelatorioService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public RelatorioVendasResponse gerarRelatorioVendas(LocalDateTime inicio, LocalDateTime fim) {
        List<Pedido> pedidos = pedidoRepository.findByCriadoEmBetween(inicio, fim).stream()
                .filter(p -> p.getStatus() != StatusPedido.CANCELADO)
                .toList();

        Map<Long, List<Pedido>> porUnidade = pedidos.stream()
                .collect(Collectors.groupingBy(p -> p.getUnidade().getId()));

        List<RelatorioVendasResponse.VendaPorUnidade> vendasPorUnidade = porUnidade.entrySet().stream()
                .map(entry -> {
                    List<Pedido> pedidosDaUnidade = entry.getValue();
                    BigDecimal total = pedidosDaUnidade.stream()
                            .map(Pedido::getValorTotal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    String nomeUnidade = pedidosDaUnidade.get(0).getUnidade().getNome();
                    return new RelatorioVendasResponse.VendaPorUnidade(entry.getKey(), nomeUnidade, pedidosDaUnidade.size(), total);
                })
                .sorted(Comparator.comparing(RelatorioVendasResponse.VendaPorUnidade::totalVendido).reversed())
                .toList();

        Map<Long, List<ItemPedido>> itensPorProduto = pedidos.stream()
                .flatMap(p -> p.getItens().stream())
                .collect(Collectors.groupingBy(i -> i.getProduto().getId()));

        List<RelatorioVendasResponse.ProdutoMaisVendido> produtosMaisVendidos = itensPorProduto.entrySet().stream()
                .map(entry -> {
                    long quantidade = entry.getValue().stream().mapToLong(ItemPedido::getQuantidade).sum();
                    String nomeProduto = entry.getValue().get(0).getProduto().getNome();
                    return new RelatorioVendasResponse.ProdutoMaisVendido(entry.getKey(), nomeProduto, quantidade);
                })
                .sorted(Comparator.comparing(RelatorioVendasResponse.ProdutoMaisVendido::quantidadeVendida).reversed())
                .toList();

        BigDecimal faturamentoTotal = vendasPorUnidade.stream()
                .map(RelatorioVendasResponse.VendaPorUnidade::totalVendido)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RelatorioVendasResponse(vendasPorUnidade, produtosMaisVendidos, faturamentoTotal);
    }
}
