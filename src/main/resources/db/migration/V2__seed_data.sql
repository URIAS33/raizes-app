INSERT INTO unidade (nome, tipo, cidade, estado, ativo) VALUES
    ('Raizes do Nordeste - Boa Viagem', 'COMPLETA', 'Recife', 'PE', true),
    ('Raizes do Nordeste - Shopping Iguatemi (Quiosque)', 'REDUZIDA', 'Fortaleza', 'CE', true);

INSERT INTO produto (nome, descricao, preco_base, sazonal, mes_inicio_sazonal, mes_fim_sazonal) VALUES
    ('Tapioca de queijo coalho', 'Tapioca recheada com queijo coalho', 15.00, false, null, null),
    ('Cuscuz recheado', 'Cuscuz com queijo, ovo e manteiga de garrafa', 18.00, false, null, null),
    ('Bolo de macaxeira', 'Fatia de bolo de macaxeira', 9.00, false, null, null),
    ('Canjica', 'Canjica doce, item de festa junina', 12.00, true, 5, 7),
    ('Suco de caju natural', 'Suco de caju 500ml', 8.00, false, null, null);

INSERT INTO cardapio_unidade (unidade_id, produto_id, disponivel, preco_local)
SELECT 1, id, true, null FROM produto;

INSERT INTO cardapio_unidade (unidade_id, produto_id, disponivel, preco_local)
SELECT 2, id, (nome <> 'Cuscuz recheado'), null FROM produto;

INSERT INTO estoque (unidade_id, produto_id, quantidade, quantidade_minima)
SELECT u.id, p.id, 100, 10
FROM unidade u CROSS JOIN produto p;

INSERT INTO cliente (nome, email, telefone, data_nascimento, criado_em) VALUES
    ('Maria Souza', 'maria.souza@example.com', '81999990000', '1990-04-12', now());
