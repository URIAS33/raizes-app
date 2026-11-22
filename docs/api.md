# Documentação dos principais endpoints

Base URL local: `http://localhost:8080`

## Cardápio

### `GET /unidades/{id}/cardapio`
Retorna os produtos disponíveis na unidade, já considerando sazonalidade (ex.: itens juninos fora do período não aparecem).

**Resposta 200**
```json
[
  { "produtoId": 1, "nome": "Tapioca de queijo coalho", "descricao": "...", "preco": 15.00 }
]
```

## Pedidos

### `POST /pedidos`
```json
{
  "unidadeId": 1,
  "clienteId": null,
  "canal": "TOTEM",
  "itens": [ { "produtoId": 1, "quantidade": 2 } ]
}
```
- `clienteId` é opcional (pedido de balcão sem cadastro).
- Valida disponibilidade no cardápio da unidade e saldo de estoque antes de confirmar.
- Resposta `201 Created` com o pedido e `Location: /pedidos/{id}`.
- Erros: `404` (unidade/cliente inexistente), `422` (produto indisponível ou estoque insuficiente).

### `GET /pedidos/{id}/status`
Retorna o status atual e o histórico completo de transições.

### `PATCH /pedidos/{id}/cancelar`
```json
{ "motivo": "Cliente desistiu" }
```
Só permitido enquanto o pedido não estiver `EM_PREPARO` ou além. Gera registro de auditoria.

## Pagamento (desacoplado)

### `POST /pagamentos/{pedidoId}/solicitar`
Dispara a solicitação ao gateway externo (simulado localmente em `/mock-gateway/pagamentos`). Pedido passa para `AGUARDANDO_PAGAMENTO`.

### `POST /pagamentos/webhook`
Callback do gateway externo (não deve ser chamado pelo cliente final):
```json
{ "pedidoId": 10, "gatewayTransacaoId": "abc-123", "status": "CONFIRMADO" }
```
Idempotente por `gatewayTransacaoId`.

## Fidelidade / LGPD

### `POST /fidelidade/consentimento`
```json
{ "clienteId": 1, "finalidade": "FIDELIZACAO", "aceito": true }
```

### `DELETE /fidelidade/clientes/{clienteId}/dados?motivo=...`
Anonimiza os dados pessoais do cliente (direito do titular), preservando o histórico de pedidos para fins estatísticos/fiscais. Gera registro de auditoria (`ACESSO_DADOS_PESSOAIS`).

## Relatórios (matriz)

### `GET /relatorios/vendas?inicio=2026-11-01T00:00:00&fim=2026-11-30T23:59:59`
Retorna vendas por unidade, produtos mais vendidos e faturamento total do período (pedidos cancelados são excluídos).

## Auditoria

### `GET /auditoria/operacoes?tipo=CANCELAMENTO`
Tipos possíveis: `CANCELAMENTO`, `DESCONTO`, `AJUSTE_ESTOQUE`, `ACESSO_DADOS_PESSOAIS`.

---

Uma coleção Postman/Insomnia com esses exemplos pode ser exportada e anexada ao PDF final como evidência de execução (ver Etapa 6 do roteiro de implementação).
