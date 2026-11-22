package com.raizesdonordeste.backend.repository;

import com.raizesdonordeste.backend.domain.AuditoriaOperacao;
import com.raizesdonordeste.backend.domain.enums.TipoOperacaoAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaOperacaoRepository extends JpaRepository<AuditoriaOperacao, Long> {

    List<AuditoriaOperacao> findByTipo(TipoOperacaoAuditoria tipo);
}
