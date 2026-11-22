package com.raizesdonordeste.backend.dto;

import com.raizesdonordeste.backend.domain.enums.FinalidadeConsentimento;

import java.time.LocalDateTime;

public record ConsentimentoResponse(
        Long id,
        Long clienteId,
        FinalidadeConsentimento finalidade,
        boolean aceito,
        LocalDateTime dataConsentimento
) {
}
