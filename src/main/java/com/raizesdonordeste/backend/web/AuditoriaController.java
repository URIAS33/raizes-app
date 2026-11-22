package com.raizesdonordeste.backend.web;

import com.raizesdonordeste.backend.domain.enums.TipoOperacaoAuditoria;
import com.raizesdonordeste.backend.dto.AuditoriaResponse;
import com.raizesdonordeste.backend.service.AuditoriaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auditoria")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping("/operacoes")
    public List<AuditoriaResponse> listar(@RequestParam TipoOperacaoAuditoria tipo) {
        return auditoriaService.listarPorTipo(tipo);
    }
}
