package com.raizesdonordeste.backend.repository;

import com.raizesdonordeste.backend.domain.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    Optional<Estoque> findByUnidadeIdAndProdutoId(Long unidadeId, Long produtoId);

    @Modifying
    @Query("UPDATE Estoque e SET e.quantidade = e.quantidade - :quantidade " +
            "WHERE e.unidade.id = :unidadeId AND e.produto.id = :produtoId AND e.quantidade >= :quantidade")
    int decrementarSeHouverSaldo(@Param("unidadeId") Long unidadeId,
                                  @Param("produtoId") Long produtoId,
                                  @Param("quantidade") int quantidade);
}
