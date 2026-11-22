package com.raizesdonordeste.backend.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class HttpPaymentGatewayClient implements PaymentGatewayClient {

    private final RestClient restClient;

    public HttpPaymentGatewayClient(@Value("${gateway-pagamento.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public RespostaPagamentoGateway solicitarPagamento(SolicitacaoPagamentoGateway solicitacao) {
        ResponseEntity<RespostaPagamentoGateway> resposta = restClient.post()
                .uri("/pagamentos")
                .body(solicitacao)
                .retrieve()
                .toEntity(RespostaPagamentoGateway.class);

        return resposta.getBody();
    }
}
