-- CADASTRO DE SERVICO (SIGA-WEB)
-- INCLUSÃO DE COLUNA UNIDADE RECEPTORA (SIGA-CP)

alter table corporativo.dp_lotacao add "unidade_receptora" bool default false not null;