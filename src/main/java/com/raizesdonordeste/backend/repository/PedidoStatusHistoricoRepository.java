package com.raizesdonordeste.backend.repository;

import com.raizesdonordeste.backend.domain.PedidoStatusHistorico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoStatusHistoricoRepository extends JpaRepository<PedidoStatusHistorico, Long> {

    List<PedidoStatusHistorico> findByPedidoIdOrderByCriadoEmAsc(Long pedidoId);
}
