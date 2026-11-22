package com.raizesdonordeste.backend.dto;

import com.raizesdonordeste.backend.domain.enums.FinalidadeConsentimento;
import jakarta.validation.constraints.NotNull;

public record ConsentimentoRequest(

        @NotNull
        Long clienteId,

        @NotNull
        FinalidadeConsentimento finalidade,

        @NotNull
        boolean aceito
) {
}
