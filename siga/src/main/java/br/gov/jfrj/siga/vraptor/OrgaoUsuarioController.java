package br.gov.jfrj.siga.vraptor;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;
import br.gov.jfrj.siga.dp.CpContrato;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpOrgaoUsuarioDaoFiltro;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

@Controller
public class OrgaoUsuarioController extends SigaSelecionavelControllerSupport<CpOrgaoUsuario, DaoFiltroSelecionavel>{

	/**
	 * @deprecated CDI eyes only
	 */
	public OrgaoUsuarioController() {
		super();
	}

	@Inject
	public OrgaoUsuarioController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
	}

	@Override
	protected DaoFiltroSelecionavel createDaoFiltro() {
		final CpOrgaoUsuarioDaoFiltro flt = new CpOrgaoUsuarioDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		return flt;
	}
	
	@Get("app/orgaoUsuario/listar")
	public void lista(Integer paramoffset, Integer quantidadePagina, String nome) throws Exception {

		assertAcesso(CpServico.VERIFICADOR_ACESSO_CADASTRO_ORGAO_USUARIO);;
		
		if (paramoffset == null) {
			paramoffset = 0;
		}
		if (quantidadePagina == null) {
			quantidadePagina = 30;
		}

		CpOrgaoUsuarioDaoFiltro filtro = new CpOrgaoUsuarioDaoFiltro();
		filtro.setAtivo(null); // Buscar ativos e inativos
		filtro.setNome(nome);

		setItens(CpDao.getInstance().consultarPorFiltroComContrato(filtro, paramoffset, quantidadePagina));
		result.include("itens", getItens());
		result.include("tamanho", dao().consultarQuantidade(filtro));
		result.include("nome", nome);

		final String siglaOrgaoTitular = getTitular().getOrgaoUsuario().getSigla();
		final boolean administrador = CpConfiguracaoBL.SIGLAS_ORGAOS_ADMINISTRADORES.contains(siglaOrgaoTitular);

		result.include("usuarioPodeAlterar", administrador);
		result.include("orgaoUsuarioSiglaLogado", siglaOrgaoTitular);
		result.include("administrador", CpConfiguracaoBL.SIGLA_ORGAO_ROOT.equalsIgnoreCase(siglaOrgaoTitular));

		setItemPagina(quantidadePagina);
		result.include("currentPageNumber", calculaPaginaAtual(paramoffset));
	}
	
	@Get("/app/orgaoUsuario/editar")
	public void edita(final Long id){
		
		assertAcesso(CpServico.VERIFICADOR_ACESSO_CADASTRO_ORGAO_USUARIO);;
		
		if (id != null) {
			CpContrato contrato = daoContrato(id);
			CpOrgaoUsuario orgaoUsuario = daoOrgaoUsuario(id);
			result.include("nmOrgaoUsuario", orgaoUsuario.getDescricao());
			result.include("siglaOrgaoUsuario", orgaoUsuario.getSigla());
			result.include("siglaOrgaoUsuarioCompleta", orgaoUsuario.getSiglaOrgaoUsuarioCompleta());
			result.include("isExternoOrgaoUsu", orgaoUsuario.getIsExternoOrgaoUsu());
			result.include("codOrgUsu", orgaoUsuario.getCodOrgaoUsu());
			try {
				result.include("dtContrato",contrato.getDtContratoDDMMYYYY());
			} catch (final Exception e) {
				result.include("dtContrato","");
			}
		}
		
		List<DpPessoa> listaPessoa = CpDao.getInstance().consultarPorMatriculaEOrgao(null,id,Boolean.FALSE,Boolean.FALSE);
		
		if(listaPessoa.size() == 0) {
			result.include("podeAlterarSigla", Boolean.TRUE);
		}
		result.include("request", getRequest());
		result.include("id", id);
	}
	
	private void atualizarContrato(Long id, Date dataContrato) {
		CpContrato contrato = daoContrato(id);

		if ((contrato == null) && (dataContrato == null)) {
			// Faz nada
		} else if ((contrato == null) && (dataContrato != null)) {
			// Insere
			contrato = new CpContrato();
			contrato.setIdOrgaoUsu(id);
			contrato.setDtContrato(dataContrato);
			dao().gravar(contrato);
		} else if ((contrato != null) && (dataContrato == null)) {
			dao.excluir(contrato);
		} else if (contrato.getDtContrato().compareTo(dataContrato) != 0) {
			// Atualiza se a data foi alterada.
			contrato.setDtContrato(dataContrato);
			dao().gravar(contrato);
		}
	}

	@Transacional
	@Post("/app/orgaoUsuario/gravar")
	public void editarGravar(final Long id, 
							 final Long codOrgUsu,
							 final String nmOrgaoUsuario,
							 final String siglaOrgaoUsuario,
							 final String siglaOrgaoUsuarioCompleta,
							 final String dtContrato,
							 final Boolean isExternoOrgaoUsu
	) throws Exception {
		
		assertAcesso(CpServico.VERIFICADOR_ACESSO_CADASTRO_ORGAO_USUARIO);;

		if (codOrgUsu == null) {
			throw new AplicacaoException("Código Interno do Órgão não informado");
		}

		if (isBlank(nmOrgaoUsuario)) {
			throw new AplicacaoException("Nome do órgão usuário não informado");
		}
		
		if (isBlank(siglaOrgaoUsuario)) {
			throw new AplicacaoException("Sigla abreviada do órgão usuário não informada");
		}
		
		if(!siglaOrgaoUsuario.matches("[a-zA-Z]{1,10}")) {
			throw new AplicacaoException("Sigla do órgão inválida");
		}
		
		if(isBlank(siglaOrgaoUsuarioCompleta)) {
			throw new AplicacaoException("Sigla Oficial do órgão usuário não informada");
		}
		
		if(!siglaOrgaoUsuarioCompleta.matches("[a-zA-Z]{1,10}")) {
			throw new AplicacaoException("Sigla Oficial do órgão inválida");
		}

		if(dtContrato != null && !dtContrato.matches("(0[1-9]|[12][0-9]|3[01])\\/(0[1-9]|1[012])\\/(19|20)\\d{2,2}"))
			throw new AplicacaoException("Data do contrato inválida");

		Date dataContrato = null;
		if(dtContrato != null) {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			dataContrato = formatter.parse(dtContrato);
		}

		final String siglaAcronimo = Texto.removerEspacosExtra(siglaOrgaoUsuario.toUpperCase().trim());
		final String siglaCompleta = Texto.removerEspacosExtra(siglaOrgaoUsuarioCompleta.toUpperCase()).trim();
		CpOrgaoUsuario orgaoAnterior = dao().consultarOrgaoUsuarioAtivoPorCodigoDeIntegracaoOuSigla(codOrgUsu, siglaAcronimo);

		if (orgaoAnterior != null) {
			if (!orgaoAnterior.getId().equals(id)) {
				throw new AplicacaoException("Acrônimo ou sigla já cadastrada para outro órgão com código ativo: ["
						+ orgaoAnterior.getCodOrgaoUsu() + "] ["
						+ orgaoAnterior.getSiglaOrgaoUsuarioCompleta() + "] - "
						+ orgaoAnterior.getNmOrgaoUsu());
			}
		}

		CpOrgaoUsuario orgaoUsuario;
		if (orgaoAnterior == null) {
			orgaoUsuario = new CpOrgaoUsuario();
			orgaoUsuario.setAcronimoOrgaoUsu(siglaAcronimo);
			orgaoUsuario.setSigla(siglaAcronimo);
		} else {
			orgaoUsuario = orgaoAnterior;
		}
		orgaoUsuario.setCodOrgaoUsu(codOrgUsu);

		final String nomeOrgaoUsuario = Texto.removerEspacosExtra(nmOrgaoUsuario).trim().replaceAll("[\"'\u201c\u201d]", "\"");
		orgaoUsuario.setNmOrgaoUsu(nomeOrgaoUsuario);
		orgaoUsuario.setSiglaOrgaoUsuarioCompleta(siglaCompleta);
		orgaoUsuario.setHisAtivo(1);

		boolean isOrgaoExterno = BooleanUtils.toBoolean(isExternoOrgaoUsu);
		orgaoUsuario.setIsExternoOrgaoUsu(BooleanUtils.toInteger(isOrgaoExterno));

		try {
			if (orgaoUsuario.getId() == null) {
				orgaoUsuario = dao().gravar(orgaoUsuario);
				orgaoUsuario.setHisIdIni(orgaoUsuario.getId());
			}
			orgaoUsuario = dao().atualizar(orgaoUsuario);
			atualizarContrato(orgaoUsuario.getId(), dataContrato);

			this.result.include("mensagem", "Operação realizada com sucesso!");
			this.result.redirectTo(this).lista(null, null, null);
		} catch (final Exception e) {
			throw new AplicacaoException("Erro na gravação", 0, e);
		}

	}

	@Transacional
	@Post("/app/orgaoUsuario/ativar")
	public void ativar(final Long id) throws Exception {
		
		assertAcesso(CpServico.VERIFICADOR_ACESSO_CADASTRO_ORGAO_USUARIO);;
		
		CpOrgaoUsuario orgaoUsuario = dao().consultar(id, CpOrgaoUsuario.class, false);
		CpOrgaoUsuario orgaoAnteriorAtivo = dao().consultarOrgaoUsuarioAtivoPorCodigoDeIntegracaoOuSigla(orgaoUsuario.getCodOrgaoUsu(), EMPTY);
		if (orgaoAnteriorAtivo != null && !Objects.equals(orgaoUsuario.getId(), orgaoAnteriorAtivo.getId())) {
			throw new AplicacaoException("Não foi possível ativar porque o Órgão ["
					+ orgaoAnteriorAtivo.getCodOrgaoUsu() + "] ["
					+ orgaoAnteriorAtivo.getSiglaOrgaoUsuarioCompleta() + "] - "
					+ orgaoAnteriorAtivo.getNmOrgaoUsu() + " já está ativo com o mesmo código de integração");
		}
		orgaoUsuario.setHisAtivo(1);
		dao().gravar(orgaoUsuario);
		result.redirectTo(this).lista(null, null, null);
	}

	@Transacional
	@Post("/app/orgaoUsuario/desativar")
	public void desativar(final Long codigoDeIntegracao) throws Exception {
		
		assertAcesso(CpServico.VERIFICADOR_ACESSO_CADASTRO_ORGAO_USUARIO);;
		
		CpOrgaoUsuario orgaoUsuario = dao().consultarOrgaoUsuarioAtivoPorCodigoDeIntegracaoOuSigla(codigoDeIntegracao, EMPTY);
		orgaoUsuario.setHisAtivo(0);
		dao().gravar(orgaoUsuario);
		result.redirectTo(this).lista(null, null, null);
	}

	private CpOrgaoUsuario daoOrgaoUsuario(long id) {
		return dao().consultar(id, CpOrgaoUsuario.class, false);
	}	
	
	private CpContrato daoContrato(long idOrgaoUsu) {
		return dao().consultar(idOrgaoUsu, CpContrato.class, false);
	}	
}
