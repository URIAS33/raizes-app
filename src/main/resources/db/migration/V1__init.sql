CREATE TABLE unidade (
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(120) NOT NULL,
    tipo            VARCHAR(20)  NOT NULL,
    cidade          VARCHAR(80)  NOT NULL,
    estado          VARCHAR(2)   NOT NULL,
    ativo           BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE produto (
    id                  BIGSERIAL PRIMARY KEY,
    nome                VARCHAR(120)   NOT NULL,
    descricao           VARCHAR(500),
    preco_base          NUMERIC(10,2)  NOT NULL,
    sazonal             BOOLEAN        NOT NULL DEFAULT FALSE,
    mes_inicio_sazonal  INTEGER,
    mes_fim_sazonal     INTEGER
);

CREATE TABLE cardapio_unidade (
    id           BIGSERIAL PRIMARY KEY,
    unidade_id   BIGINT NOT NULL REFERENCES unidade(id),
    produto_id   BIGINT NOT NULL REFERENCES produto(id),
    disponivel   BOOLEAN NOT NULL DEFAULT TRUE,
    preco_local  NUMERIC(10,2),
    UNIQUE (unidade_id, produto_id)
);

CREATE TABLE estoque (
    id                 BIGSERIAL PRIMARY KEY,
    unidade_id         BIGINT NOT NULL REFERENCES unidade(id),
    produto_id         BIGINT NOT NULL REFERENCES produto(id),
    quantidade         INTEGER NOT NULL DEFAULT 0,
    quantidade_minima  INTEGER NOT NULL DEFAULT 0,
    UNIQUE (unidade_id, produto_id)
);

CREATE TABLE funcionario (
    id          BIGSERIAL PRIMARY KEY,
    unidade_id  BIGINT NOT NULL REFERENCES unidade(id),
    nome        VARCHAR(120) NOT NULL,
    cargo       VARCHAR(40)  NOT NULL,
    email       VARCHAR(160) NOT NULL UNIQUE
);

CREATE TABLE cliente (
    id               BIGSERIAL PRIMARY KEY,
    nome             VARCHAR(120) NOT NULL,
    email            VARCHAR(160) NOT NULL UNIQUE,
    telefone         VARCHAR(20),
    data_nascimento  DATE,
    anonimizado      BOOLEAN NOT NULL DEFAULT FALSE,
    criado_em        TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE consentimento_lgpd (
    id                   BIGSERIAL PRIMARY KEY,
    cliente_id           BIGINT NOT NULL REFERENCES cliente(id),
    finalidade           VARCHAR(80)  NOT NULL,
    aceito               BOOLEAN      NOT NULL,
    data_consentimento   TIMESTAMP    NOT NULL DEFAULT now(),
    data_revogacao       TIMESTAMP
);

CREATE TABLE pedido (
    id             BIGSERIAL PRIMARY KEY,
    unidade_id     BIGINT NOT NULL REFERENCES unidade(id),
    cliente_id     BIGINT REFERENCES cliente(id),
    canal          VARCHAR(20)   NOT NULL,
    status         VARCHAR(30)   NOT NULL,
    valor_total    NUMERIC(10,2) NOT NULL,
    criado_em      TIMESTAMP     NOT NULL DEFAULT now(),
    atualizado_em  TIMESTAMP     NOT NULL DEFAULT now()
);

CREATE TABLE item_pedido (
    id              BIGSERIAL PRIMARY KEY,
    pedido_id       BIGINT NOT NULL REFERENCES pedido(id),
    produto_id      BIGINT NOT NULL REFERENCES produto(id),
    quantidade      INTEGER NOT NULL,
    preco_unitario  NUMERIC(10,2) NOT NULL
);

CREATE TABLE pedido_status_historico (
    id           BIGSERIAL PRIMARY KEY,
    pedido_id    BIGINT NOT NULL REFERENCES pedido(id),
    status       VARCHAR(30) NOT NULL,
    observacao   VARCHAR(255),
    criado_em    TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE pagamento (
    id                   BIGSERIAL PRIMARY KEY,
    pedido_id            BIGINT NOT NULL REFERENCES pedido(id),
    status               VARCHAR(20) NOT NULL,
    valor                NUMERIC(10,2) NOT NULL,
    gateway_transacao_id VARCHAR(80),
    solicitado_em        TIMESTAMP NOT NULL DEFAULT now(),
    confirmado_em        TIMESTAMP
);

CREATE TABLE transacao_pontos (
    id          BIGSERIAL PRIMARY KEY,
    cliente_id  BIGINT NOT NULL REFERENCES cliente(id),
    pedido_id   BIGINT REFERENCES pedido(id),
    pontos      INTEGER NOT NULL,
    tipo        VARCHAR(20) NOT NULL,
    criado_em   TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE auditoria_operacao (
    id             BIGSERIAL PRIMARY KEY,
    tipo           VARCHAR(30)  NOT NULL,
    entidade_tipo  VARCHAR(40)  NOT NULL,
    entidade_id    BIGINT       NOT NULL,
    funcionario_id BIGINT REFERENCES funcionario(id),
    descricao      VARCHAR(500),
    criado_em      TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_pedido_unidade ON pedido(unidade_id);
CREATE INDEX idx_pedido_cliente ON pedido(cliente_id);
CREATE INDEX idx_item_pedido_pedido ON item_pedido(pedido_id);
CREATE INDEX idx_estoque_unidade_produto ON estoque(unidade_id, produto_id);
