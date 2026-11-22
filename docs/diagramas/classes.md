# Diagrama de Classes (visão simplificada do domínio)

```mermaid
classDiagram
    class Unidade {
        Long id
        String nome
        TipoUnidade tipo
        String cidade
        String estado
        boolean ativo
    }

    class Produto {
        Long id
        String nome
        BigDecimal precoBase
        boolean sazonal
        Integer mesInicioSazonal
        Integer mesFimSazonal
        disponivelNoMes(mes) boolean
    }

    class CardapioUnidade {
        Long id
        boolean disponivel
        BigDecimal precoLocal
        precoEfetivo() BigDecimal
    }

    class Estoque {
        Long id
        int quantidade
        int quantidadeMinima
        temSaldoPara(qtd) boolean
    }

    class Cliente {
        Long id
        String nome
        String email
        boolean anonimizado
        anonimizar() void
    }

    class ConsentimentoLgpd {
        Long id
        FinalidadeConsentimento finalidade
        boolean aceito
        LocalDateTime dataRevogacao
        vigente() boolean
    }

    class Pedido {
        Long id
        CanalVenda canal
        StatusPedido status
        BigDecimal valorTotal
        podeSerCancelado() boolean
    }

    class ItemPedido {
        Long id
        int quantidade
        BigDecimal precoUnitario
        subtotal() BigDecimal
    }

    class PedidoStatusHistorico {
        Long id
        StatusPedido status
        LocalDateTime criadoEm
    }

    class Pagamento {
        Long id
        StatusPagamento status
        BigDecimal valor
        String gatewayTransacaoId
    }

    class TransacaoPontos {
        Long id
        int pontos
        TipoTransacaoPontos tipo
    }

    class AuditoriaOperacao {
        Long id
        TipoOperacaoAuditoria tipo
        String entidadeTipo
        Long entidadeId
    }

    Unidade "1" --> "N" CardapioUnidade
    Produto "1" --> "N" CardapioUnidade
    Unidade "1" --> "N" Estoque
    Produto "1" --> "N" Estoque
    Unidade "1" --> "N" Pedido
    Cliente "0..1" --> "N" Pedido
    Pedido "1" --> "N" ItemPedido
    Produto "1" --> "N" ItemPedido
    Pedido "1" --> "N" PedidoStatusHistorico
    Pedido "1" --> "1" Pagamento
    Cliente "1" --> "N" ConsentimentoLgpd
    Cliente "1" --> "N" TransacaoPontos
    Pedido "0..1" --> "N" TransacaoPontos
```

**Decisão de modelagem:** `PedidoStatusHistorico` existe como tabela própria (não apenas um campo `status` em `Pedido`) para permitir reconstruir a linha do tempo do pedido e sustentar auditoria/relatórios de tempo de preparo.
