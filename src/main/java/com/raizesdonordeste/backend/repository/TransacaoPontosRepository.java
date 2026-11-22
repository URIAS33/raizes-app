package com.raizesdonordeste.backend.repository;

import com.raizesdonordeste.backend.domain.TransacaoPontos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoPontosRepository extends JpaRepository<TransacaoPontos, Long> {

    List<TransacaoPontos> findByClienteId(Long clienteId);
}
