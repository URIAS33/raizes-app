# Diagrama de Sequência — Pagamento Desacoplado

Mostra como o sistema principal nunca processa o pagamento diretamente: ele solicita, e a confirmação chega depois, de forma assíncrona, via webhook.

```mermaid
sequenceDiagram
    actor Cliente
    participant App as App/Totem
    participant API as Backend Raizes do Nordeste
    participant GW as Gateway de Pagamento (externo)

    Cliente->>App: Confirma pedido
    App->>API: POST /pedidos
    API-->>App: 201 Created (status=CRIADO)

    App->>API: POST /pagamentos/{pedidoId}/solicitar
    API->>GW: POST /pagamentos (valor, pedidoId)
    GW-->>API: transacaoId + status PENDENTE
    API-->>App: status=AGUARDANDO_PAGAMENTO

    Note over GW: processamento real do pagamento<br/>acontece do lado do gateway

    GW->>API: POST /pagamentos/webhook (transacaoId, CONFIRMADO)
    API->>API: Atualiza Pagamento e Pedido (status=PAGO)
    API-->>GW: 200 OK

    App->>API: GET /pedidos/{id}/status
    API-->>App: status=PAGO
```

**Tratamento de falha:** se o webhook nunca chegar, o pedido permanece em `AGUARDANDO_PAGAMENTO`; um job de conciliação (fora do escopo implementado) consultaria o gateway periodicamente para pedidos pendentes há mais de X minutos. O endpoint de webhook é idempotente (reenvios com o mesmo `gatewayTransacaoId` não duplicam o efeito).
