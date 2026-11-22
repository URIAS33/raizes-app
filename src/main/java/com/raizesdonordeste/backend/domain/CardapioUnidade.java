package com.raizesdonordeste.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cardapio_unidade", uniqueConstraints = @UniqueConstraint(columnNames = {"unidade_id", "produto_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardapioUnidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private boolean disponivel;

    @Column(name = "preco_local", precision = 10, scale = 2)
    private BigDecimal precoLocal;

    public BigDecimal precoEfetivo() {
        return precoLocal != null ? precoLocal : produto.getPrecoBase();
    }
}
