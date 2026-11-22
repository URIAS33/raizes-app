package com.raizesdonordeste.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(name = "preco_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoBase;

    @Column(nullable = false)
    private boolean sazonal;

    @Column(name = "mes_inicio_sazonal")
    private Integer mesInicioSazonal;

    @Column(name = "mes_fim_sazonal")
    private Integer mesFimSazonal;

    public boolean disponivelNoMes(int mesAtual) {
        if (!sazonal) {
            return true;
        }
        if (mesInicioSazonal == null || mesFimSazonal == null) {
            return true;
        }
        if (mesInicioSazonal <= mesFimSazonal) {
            return mesAtual >= mesInicioSazonal && mesAtual <= mesFimSazonal;
        }
        return mesAtual >= mesInicioSazonal || mesAtual <= mesFimSazonal;
    }
}
