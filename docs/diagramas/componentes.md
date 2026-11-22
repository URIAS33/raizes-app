# Diagrama de Componentes / Arquitetura

```mermaid
flowchart TB
    subgraph Canais
        AppMobile[App Oficial]
        Totem[Totem de Auto-atendimento]
        Balcao[Atendimento de Balcão]
    end

    subgraph Backend["Backend Raizes do Nordeste (Spring Boot)"]
        WebLayer[Controllers REST]
        ServiceLayer[Camada de Serviço\nPedido / Pagamento / Estoque / Fidelidade / Auditoria / Relatório]
        RepoLayer[Spring Data JPA]
        GatewayPort[PaymentGatewayClient\n(porta de integração)]
    end

    DB[(PostgreSQL)]
    GatewayExterno[[Gateway de Pagamento Externo]]

    AppMobile --> WebLayer
    Totem --> WebLayer
    Balcao --> WebLayer

    WebLayer --> ServiceLayer
    ServiceLayer --> RepoLayer
    RepoLayer --> DB
    ServiceLayer --> GatewayPort
    GatewayPort --> GatewayExterno
    GatewayExterno -. webhook de confirmação .-> WebLayer
```

## Justificativa da arquitetura

- **Monólito modular em camadas** (controller → service → repository), não microsserviços: a equipe é pequena/acadêmica e o domínio ainda cabe em um único deployable; separar em microsserviços agora adicionaria complexidade operacional (service discovery, tracing distribuído) sem benefício real neste estágio. Evolução natural seria extrair "Pagamentos" e "Relatórios" como serviços independentes quando o volume justificar.
- **Pagamento desacoplado via interface (`PaymentGatewayClient`)**: a regra de negócio nunca conhece o provedor concreto, só a porta. Troca de provedor (ex.: migrar de um gateway para outro) não exige alterar `PedidoService`/`PagamentoService`.
- **Confirmação assíncrona via webhook**: evita bloquear a requisição do cliente esperando o processamento do pagamento, que pode demorar segundos no mundo real.
- **Alta disponibilidade**: `open-in-view: false` e transações curtas reduzem contenção; o uso de banco relacional com índices nas colunas de busca mais frequentes (unidade, cliente, pedido) sustenta picos de tráfego. Para crescimento futuro: réplicas de leitura para relatórios e cache de cardápio (Redis) evitariam que consultas de leitura pesada concorram com o fluxo de escrita de pedidos.
- **Auditoria como tabela própria**, nunca sobrescrita: cancelamentos, descontos e ajustes de estoque ficam rastreáveis, atendendo à exigência de rastreabilidade da franqueadora.
