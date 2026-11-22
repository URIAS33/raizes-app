package com.raizesdonordeste.backend.service;

import com.raizesdonordeste.backend.domain.Estoque;
import com.raizesdonordeste.backend.exception.RegraDeNegocioException;
import com.raizesdonordeste.backend.repository.EstoqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final AuditoriaService auditoriaService;

    public EstoqueService(EstoqueRepository estoqueRepository, AuditoriaService auditoriaService) {
        this.estoqueRepository = estoqueRepository;
        this.auditoriaService = auditoriaService;
    }

    public void validarDisponibilidade(Long unidadeId, Long produtoId, int quantidade) {
        Estoque estoque = buscarOuErro(unidadeId, produtoId);
        if (!estoque.temSaldoPara(quantidade)) {
            throw new RegraDeNegocioException(
                    "Estoque insuficiente para o produto " + produtoId + " na unidade " + unidadeId);
        }
    }

    @Transactional
    public void baixarEstoque(Long unidadeId, Long produtoId, int quantidade) {
        int linhasAfetadas = estoqueRepository.decrementarSeHouverSaldo(unidadeId, produtoId, quantidade);
        if (linhasAfetadas == 0) {
            throw new RegraDeNegocioException(
                    "Estoque insuficiente para o produto " + produtoId + " na unidade " + unidadeId
                            + " (saldo foi consumido por outro pedido concorrente)");
        }

        Estoque estoque = buscarOuErro(unidadeId, produtoId);
        if (estoque.getQuantidade() <= estoque.getQuantidadeMinima()) {
            auditoriaService.registrarAjusteEstoque(estoque, "Estoque atingiu o nivel minimo apos baixa de pedido");
        }
    }

    private Estoque buscarOuErro(Long unidadeId, Long produtoId) {
        return estoqueRepository.findByUnidadeIdAndProdutoId(unidadeId, produtoId)
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Produto " + produtoId + " nao possui controle de estoque na unidade " + unidadeId));
    }
}
