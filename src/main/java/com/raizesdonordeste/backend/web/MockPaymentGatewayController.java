package com.raizesdonordeste.backend.web;

import com.raizesdonordeste.backend.gateway.RespostaPagamentoGateway;
import com.raizesdonordeste.backend.gateway.SolicitacaoPagamentoGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class MockPaymentGatewayController {

    @PostMapping("/mock-gateway/pagamentos")
    public RespostaPagamentoGateway solicitar(@RequestBody SolicitacaoPagamentoGateway solicitacao) {
        return new RespostaPagamentoGateway(UUID.randomUUID().toString(), "PENDENTE");
    }
}
