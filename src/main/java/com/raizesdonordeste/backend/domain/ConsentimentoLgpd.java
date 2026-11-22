package com.raizesdonordeste.backend.domain;

import com.raizesdonordeste.backend.domain.enums.FinalidadeConsentimento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "consentimento_lgpd")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsentimentoLgpd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 80)
    private FinalidadeConsentimento finalidade;

    @Column(nullable = false)
    private boolean aceito;

    @Column(name = "data_consentimento", nullable = false)
    private LocalDateTime dataConsentimento;

    @Column(name = "data_revogacao")
    private LocalDateTime dataRevogacao;

    public boolean vigente() {
        return aceito && dataRevogacao == null;
    }
}
