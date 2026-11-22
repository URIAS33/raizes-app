# Diagrama de Casos de Uso

Atores:
- **Cliente** — consome o serviço pelos canais app, totem, balcão (via atendente) ou pickup.
- **Atendente** — opera o totem/balcão, registra pedidos manualmente quando necessário.
- **Gerente de Unidade** — acompanha estoque e indicadores da própria unidade.
- **Administrador (Matriz)** — consulta relatórios consolidados e auditoria.
- **Gateway de Pagamento** (ator externo/sistema) — confirma ou nega pagamentos.

```mermaid
flowchart LR
    Cliente((Cliente))
    Atendente((Atendente))
    Gerente((Gerente de Unidade))
    Admin((Administrador da Matriz))
    Gateway((Gateway de Pagamento))

    subgraph Sistema Raizes do Nordeste
        UC1[Visualizar cardápio da unidade]
        UC2[Realizar pedido]
        UC3[Acompanhar status do pedido]
        UC4[Cancelar pedido]
        UC5[Gerenciar estoque da unidade]
        UC6[Consultar relatórios consolidados]
        UC7[Auditar operações sensíveis]
        UC8[Registrar consentimento LGPD]
        UC9[Solicitar anonimização de dados]
        UC10[Processar confirmação de pagamento]
    end

    Cliente --> UC1
    Cliente --> UC2
    Cliente --> UC3
    Cliente --> UC9
    Atendente --> UC2
    Atendente --> UC4
    Gerente --> UC5
    Admin --> UC6
    Admin --> UC7
    Cliente --> UC8
    Gateway --> UC10
    UC2 -.inclui.-> UC8
    UC4 -.gera.-> UC7
```

**Observação:** `Realizar pedido` inclui a verificação de disponibilidade no cardápio da unidade e a checagem de estoque antes de confirmar. `Cancelar pedido` sempre gera um registro de auditoria.
