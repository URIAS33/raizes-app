package com.raizesdonordeste.backend.repository;

import com.raizesdonordeste.backend.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
