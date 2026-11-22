package com.raizesdonordeste.backend.repository;

import com.raizesdonordeste.backend.domain.ConsentimentoLgpd;
import com.raizesdonordeste.backend.domain.enums.FinalidadeConsentimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsentimentoLgpdRepository extends JpaRepository<ConsentimentoLgpd, Long> {

    Optional<ConsentimentoLgpd> findTopByClienteIdAndFinalidadeOrderByDataConsentimentoDesc(
            Long clienteId, FinalidadeConsentimento finalidade);
}
