package com.raizesdonordeste.backend.repository;

import com.raizesdonordeste.backend.domain.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    Optional<Pagamento> findTopByPedidoIdOrderBySolicitadoEmDesc(Long pedidoId);
}
