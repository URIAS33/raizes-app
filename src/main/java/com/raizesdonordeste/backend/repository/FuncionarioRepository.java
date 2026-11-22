package com.raizesdonordeste.backend.repository;

import com.raizesdonordeste.backend.domain.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}
