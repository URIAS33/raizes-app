package com.raizesdonordeste.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 160, unique = true)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(nullable = false)
    private boolean anonimizado;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    public void anonimizar() {
        this.nome = "CLIENTE ANONIMIZADO";
        this.email = "anonimizado-" + this.id + "@raizesdonordeste.invalid";
        this.telefone = null;
        this.dataNascimento = null;
        this.anonimizado = true;
    }
}
