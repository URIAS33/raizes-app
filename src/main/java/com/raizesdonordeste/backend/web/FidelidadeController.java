package com.raizesdonordeste.backend.web;

import com.raizesdonordeste.backend.dto.ConsentimentoRequest;
import com.raizesdonordeste.backend.dto.ConsentimentoResponse;
import com.raizesdonordeste.backend.service.FidelidadeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fidelidade")
public class FidelidadeController {

    private final FidelidadeService fidelidadeService;

    public FidelidadeController(FidelidadeService fidelidadeService) {
        this.fidelidadeService = fidelidadeService;
    }

    @PostMapping("/consentimento")
    public ConsentimentoResponse registrarConsentimento(@Valid @RequestBody ConsentimentoRequest request) {
        return fidelidadeService.registrarConsentimento(request);
    }

    @DeleteMapping("/clientes/{clienteId}/dados")
    public void anonimizarCliente(@PathVariable Long clienteId, @RequestParam(defaultValue = "Solicitacao do titular") String motivo) {
        fidelidadeService.anonimizarCliente(clienteId, motivo);
    }
}
