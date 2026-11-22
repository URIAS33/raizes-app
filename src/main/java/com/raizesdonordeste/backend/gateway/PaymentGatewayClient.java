package com.raizesdonordeste.backend.gateway;

public interface PaymentGatewayClient {

    RespostaPagamentoGateway solicitarPagamento(SolicitacaoPagamentoGateway solicitacao);
}
