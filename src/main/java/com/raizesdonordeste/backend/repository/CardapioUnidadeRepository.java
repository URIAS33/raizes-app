package com.raizesdonordeste.backend.repository;

import com.raizesdonordeste.backend.domain.CardapioUnidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardapioUnidadeRepository extends JpaRepository<CardapioUnidade, Long> {

    List<CardapioUnidade> findByUnidadeIdAndDisponivelTrue(Long unidadeId);

    Optional<CardapioUnidade> findByUnidadeIdAndProdutoId(Long unidadeId, Long produtoId);
}
