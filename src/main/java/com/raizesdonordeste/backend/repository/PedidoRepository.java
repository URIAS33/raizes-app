package com.raizesdonordeste.backend.repository;

import com.raizesdonordeste.backend.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUnidadeIdAndCriadoEmBetween(Long unidadeId, LocalDateTime inicio, LocalDateTime fim);

    List<Pedido> findByCriadoEmBetween(LocalDateTime inicio, LocalDateTime fim);
}
