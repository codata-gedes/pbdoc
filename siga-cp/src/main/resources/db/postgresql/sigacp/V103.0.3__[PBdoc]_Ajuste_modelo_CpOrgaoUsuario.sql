UPDATE cp_orgao_usuario SET cod_orgao_usu = id_orgao_usu;
ALTER TABLE cp_orgao_usuario ALTER COLUMN cod_orgao_usu SET NOT NULL;
SELECT setval('cp_orgao_usuario_id_orgao_usu_seq', 10000000000);
ALTER TABLE cp_orgao_usuario ALTER COLUMN id_orgao_usu SET DEFAULT nextval('cp_orgao_usuario_id_orgao_usu_seq');
ALTER TABLE cp_orgao_usuario ADD COLUMN his_id_ini BIGINT;
UPDATE cp_orgao_usuario SET his_id_ini = id_orgao_usu;
CREATE UNIQUE INDEX cp_orgao_usuario_cod_orgao_usu_his_ativo_uq ON cp_orgao_usuario(cod_orgao_usu) WHERE (his_ativo = 1);
