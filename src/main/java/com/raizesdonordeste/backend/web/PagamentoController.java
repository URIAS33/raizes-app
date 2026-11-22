package com.raizesdonordeste.backend.web;

import com.raizesdonordeste.backend.dto.PagamentoResponse;
import com.raizesdonordeste.backend.dto.PagamentoWebhookRequest;
import com.raizesdonordeste.backend.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/{pedidoId}/solicitar")
    public ResponseEntity<PagamentoResponse> solicitar(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pagamentoService.solicitarPagamento(pedidoId));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> receberWebhook(@Valid @RequestBody PagamentoWebhookRequest request) {
        pagamentoService.processarWebhook(request.pedidoId(), request.gatewayTransacaoId(), request.status());
        return ResponseEntity.ok().build();
    }
}
