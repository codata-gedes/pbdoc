package br.gov.jfrj.siga.cp.model.enm;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.List;

public enum CpMarcadorEnum {

	EM_ELABORACAO(1, "Em Elaboração", "fas fa-lightbulb", EMPTY, CpMarcadorGrupoEnum.EM_ELABORACAO),
	//
	EM_ANDAMENTO(2, "Aguardando Andamento", "fas fa-clock", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	A_RECEBER(3, "A Receber", "fas fa-inbox", EMPTY, CpMarcadorGrupoEnum.CAIXA_DE_ENTRADA),
	//
	EXTRAVIADO(4, "Extraviado", "fas fa-inbox", EMPTY, CpMarcadorGrupoEnum.ALERTA),
	//
	A_ARQUIVAR(5, "A Arquivar", "fas fa-inbox", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	ARQUIVADO_CORRENTE(6, "Arquivado Corrente", "fas fa-inbox", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	A_ELIMINAR(7, "A Eliminar", "fas fa-inbox", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ACAO_DE_TEMPORALIDADE),
	//
	ELIMINADO(8, "Eliminado", "fas fa-power-off", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	JUNTADO(9, "Juntado", "fas fa-lock", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	JUNTADO_EXTERNO(16, "Juntado Externo", "fas fa-lock", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	CANCELADO(10, "Cancelado", "fas fa-ban", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	TRANSFERIDO_A_ORGAO_EXTERNO(11, "Tranferido a Órgão Externo", "fas fa-paper-plane", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	ARQUIVADO_INTERMEDIARIO(12, "Arquivado Intermediário", "fas fa-inbox", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	CAIXA_DE_ENTRADA(14, "A Receber", "fas fa-inbox", EMPTY, CpMarcadorGrupoEnum.CAIXA_DE_ENTRADA),
	//
	ARQUIVADO_PERMANENTE(13, "Arquivado Permanente", "fas fa-inbox", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	PENDENTE_DE_ASSINATURA(15, "Pendente de Assinatura", "fas fa-key", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	JUNTADO_A_DOCUMENTO_EXTERNO(16, "Juntado a Documento Externo", "fas fa-inbox", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	A_REMETER_PARA_PUBLICACAO(17, "A Remeter para Publicação", "fas fa-scroll", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	REMETIDO_PARA_PUBLICACAO(18, "Remetido para Publicação", "fas fa-scroll", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	A_REMETER_MANUALMENTE(19, "A Remeter Manualmente", "fas fa-scroll", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	PUBLICADO(20, "Publicado", "fas fa-scroll", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	PUBLICACAO_SOLICITADA(21, "Publicação Solicitada", "fas fa-scroll", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	DISPONIBILIZADO(22, "Disponibilizado", "fas fa-scroll", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	EM_TRANSITO(23, "Em Trâmite Físico", "fas fa-truck", EMPTY, CpMarcadorGrupoEnum.CAIXA_DE_SAIDA),
	//
	EM_TRANSITO_ELETRONICO(24, "Em Trâmite", "fas fa-shipping-fast", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	COMO_SUBSCRITOR(25, "Como Subscritor", "fas fa-key", EMPTY, CpMarcadorGrupoEnum.A_ASSINAR),
	//
	APENSADO(26, "Apensado", "fas fa-compress-arrows-alt", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	COMO_GESTOR(27, "Gestor", "fas fa-tag", EMPTY, CpMarcadorGrupoEnum.ACOMPANHANDO),
	//
	COMO_INTERESSADO(28, "Interessado", "fas fa-tag", EMPTY, CpMarcadorGrupoEnum.ACOMPANHANDO),
	//
	DESPACHO_PENDENTE_DE_ASSINATURA(29, "Despacho Pendente de Assinatura", "fas fa-key", EMPTY, CpMarcadorGrupoEnum.ALERTA),
	//
	ANEXO_PENDENTE_DE_ASSINATURA(30, "Anexo Pendente de Assinatura", "fas fa-key", EMPTY, CpMarcadorGrupoEnum.ALERTA),
	//
	SOBRESTADO(31, "Sobrestado", "fas fa-hourglass-start", EMPTY, CpMarcadorGrupoEnum.ACOMPANHANDO),
	//
	SEM_EFEITO(32, "Sem Efeito", "fas fa-power-off", EMPTY, CpMarcadorGrupoEnum.NENHUM),
	//
	ATIVO(36, "Ativo", "inbox", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	NOVO(37, "Novo", "inbox", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	POPULAR(38, "Popular", "inbox", EMPTY, CpMarcadorGrupoEnum.ALERTA),
	//
	REVISAR(39, "A Revisar", "fas fa-glasses", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	TOMAR_CIENCIA(40, "Tomar Ciência", "inbox", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	SOLICITACAO_A_RECEBER(41, "A Receber", "inbox", EMPTY, CpMarcadorGrupoEnum.CAIXA_DE_ENTRADA),
	//
	SOLICITACAO_EM_ANDAMENTO(42, "Em Andamento", "inbox", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	SOLICITACAO_FECHADO(43, "Fechado", "inbox", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	SOLICITACAO_PENDENTE(44, "Pendente", "inbox", EMPTY, CpMarcadorGrupoEnum.OUTROS),
	//
	SOLICITACAO_CANCELADO(45, "Cancelado", "inbox", EMPTY, CpMarcadorGrupoEnum.NENHUM),
	//
	SOLICITACAO_PRE_ATENDIMENTO(46, "Pré-atendimento", "inbox", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	SOLICITACAO_POS_ATENDIMENTO(47, "Pós-atendimento", "inbox", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	SOLICITACAO_COMO_CADASTRANTE(48, "Cadastrante", "inbox", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	SOLICITACAO_COMO_SOLICITANTE(49, "Solicitante", "inbox", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	RECOLHER_PARA_ARQUIVO_PERMANENTE(50, "Recolher Arq. Permante", "fas fa-inbox", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ACAO_DE_TEMPORALIDADE),
	//
	TRANSFERIR_PARA_ARQUIVO_INTERMEDIARIO(51, "Transferir Arq. Intermediário", "fas fa-inbox", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ACAO_DE_TEMPORALIDADE),
	//
	EM_EDITAL_DE_ELIMINACAO(52, "Em Edital de Eliminação", "fas fa-inbox", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	SOLICITACAO_FECHADO_PARCIAL(53, "Fechado Parcial", "inbox", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	SOLICITACAO_EM_CONTROLE_QUALIDADE(54, "Em Controle de Qualidade", "inbox", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	A_DEVOLVER(56, "A Devolver", "fas fa-exchange-alt", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	AGUARDANDO(57, "Aguardando", "fas fa-clock", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	A_DEVOLVER_FORA_DO_PRAZO(58, "A Devolver Fora do Prazo", "fas fa-exchange-alt", EMPTY, CpMarcadorGrupoEnum.ALERTA),
	//
	AGUARDANDO_DEVOLUCAO_FORA_DO_PRAZO(59, "Aguardando Devolução Fora Do Prazo", "fas fa-exchange-alt", EMPTY, CpMarcadorGrupoEnum.ALERTA),
	//
	PENDENTE_DE_ANEXACAO(60, "Pendente de Anexação", "fas fa-arrow-alt-circle-up", EMPTY, CpMarcadorGrupoEnum.ALERTA),
	//
	SOLICITACAO_EM_ELABORACAO(61, "Em Elaboração", "inbox", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	DOCUMENTO_ASSINADO_COM_SENHA(62, "Assinado com Senha", "fas fa-key", EMPTY, CpMarcadorGrupoEnum.NENHUM),
	//
	MOVIMENTACAO_ASSINADA_COM_SENHA(63, "Movimentação Ass. com Senha", "fas fa-key", EMPTY, CpMarcadorGrupoEnum.NENHUM),
	//
	MOVIMENTACAO_CONFERIDA_COM_SENHA(64, "Movimentação Autenticada com Senha", "fas fa-key", EMPTY, CpMarcadorGrupoEnum.NENHUM),
	//
	SOLICITACAO_FORA_DO_PRAZO(65, "Fora do Prazo", "inbox", EMPTY, CpMarcadorGrupoEnum.ALERTA),
	//
	SOLICITACAO_ATIVO(66, "Ativo", "inbox", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	PENDENTE_DE_COLABORACAO(67, "Pendente de Colaboração", "fas fa-users-cog", EMPTY, CpMarcadorGrupoEnum.CAIXA_DE_ENTRADA),
	//
	FINALIZAR_DOCUMENTO_COLABORATIVO(68, "Finalizar Documento Colaborativo", "fas fa-users-cog", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	SOLICITACAO_NECESSITA_PROVIDENCIA(69, "Necessita Providência", "inbox", EMPTY, CpMarcadorGrupoEnum.ALERTA),
	//
	COMO_EXECUTOR(70, "Executor", "inbox", EMPTY, CpMarcadorGrupoEnum.ACOMPANHANDO),
	//
	PRONTO_PARA_ASSINAR(71, "Pronto para Assinar", "fas fa-check-circle", EMPTY, CpMarcadorGrupoEnum.PRONTO_PARA_ASSINAR),
	//
	COMO_REVISOR(72, "Como Revisor", "fas fa-glasses", EMPTY, CpMarcadorGrupoEnum.A_REVISAR),
	//
	PORTAL_TRANSPARENCIA(73, "Portal da Transparência", "fas fa-globe", EMPTY, CpMarcadorGrupoEnum.NENHUM),
	//
	URGENTE(1000, "Urgente", "fas fa-bomb", EMPTY, CpMarcadorGrupoEnum.ALERTA),
	//
	IDOSO(1001, "Idoso", "fas fa-user-tag", EMPTY, CpMarcadorGrupoEnum.ALERTA),
	//
	RETENCAO_INSS(1002, "Retenção de INSS", "fas fa-tag", EMPTY, CpMarcadorGrupoEnum.ALERTA),
	//
	PRIORITARIO(1003, "Prioritário", "fas fa-star", EMPTY, CpMarcadorGrupoEnum.ALERTA),
	//
	RESTRICAO_ACESSO(1004, "Restrição de Acesso", "fas fa-user-secret", EMPTY, CpMarcadorGrupoEnum.ALERTA),
	//
	DOCUMENTO_ANALISADO(1005, "Documento Analisado", "fas fa-book-reader", EMPTY, CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	COVID_19(1006, "COVID-19", "fas fa-tag", EMPTY, CpMarcadorGrupoEnum.NENHUM),
	//
	NOTA_EMPENHO(1007, "Nota de Empenho", "fas fa-tag", EMPTY, CpMarcadorGrupoEnum.NENHUM),
	//
	DEMANDA_JUDICIAL_BAIXA(1008, "Demanda Judicial Prioridade Baixa", "fas fa-tag", EMPTY, CpMarcadorGrupoEnum.ALERTA),
	//
	DEMANDA_JUDICIAL_MEDIA(1009, "Demanda Judicial Prioridade Média", "fas fa-tag", EMPTY, CpMarcadorGrupoEnum.ALERTA),
	//
	DEMANDA_JUDICIAL_ALTA(1010, "Demanda Judicial Prioridade Alta", "fas fa-tag", EMPTY, CpMarcadorGrupoEnum.ALERTA);

	private CpMarcadorEnum(int id, String nome, String icone, String descricao, CpMarcadorGrupoEnum grupo) {
		this.id = id;
		this.nome = nome;
		this.icone = icone;
		this.descricao = descricao;
		this.grupo = grupo;
	}

	public static CpMarcadorEnum getById(Number id) {
		if (id == null) {
			return null;
		}

		for (CpMarcadorEnum i : CpMarcadorEnum.values()) {
			if (i.id == id.longValue()) {
				return i;
			}
		}
		return null;
	}

	public static CpMarcadorEnum getByNome(String nome) {
		if (isBlank(nome)) {
			return null;
		}

		for (CpMarcadorEnum i : CpMarcadorEnum.values()) {
			if (i.nome.equals(nome)) {
				return i;
			}
		}
		return null;
	}

	public static List<Long> getListIdByGrupo(String nomegrupo) {
		List<Long> listMar = new ArrayList<>();
		for (CpMarcadorEnum mar : CpMarcadorEnum.values()) {
			if (mar.getGrupo().getNome().equals(nomegrupo)) {
				listMar.add(mar.id);
			}
		}
		return listMar;
	}

	public String getIcone() {
		return icone;
	}

	public long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public CpMarcadorGrupoEnum getGrupo() {
		return grupo;
	}

	private final long id;
	private final String nome;
	private final String icone;
	private final String descricao;
	private final CpMarcadorGrupoEnum grupo;

}