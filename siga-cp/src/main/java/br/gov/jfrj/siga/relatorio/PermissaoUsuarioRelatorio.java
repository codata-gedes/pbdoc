package br.gov.jfrj.siga.relatorio;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jboss.logging.Logger;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import ar.com.fdvs.dj.domain.constants.Page;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.acesso.ConfiguracaoAcesso;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
/**
 * Relatório indicando todas as permissões de um determinado usuário
 * @author aym
 *
 */
public class PermissaoUsuarioRelatorio extends RelatorioTemplate{

	private static final Logger log = Logger.getLogger(PermissaoUsuarioRelatorio.class);

	private DpPessoa dpPessoa;
	public PermissaoUsuarioRelatorio(Map<String, String> parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("idPessoa") == null){
			throw new DJBuilderException("Parâmetro idPessoa não informado!");
		}
		try {
			Long t_lngIdPessoa = Long.parseLong((String) parametros.get("idPessoa"));
			setDpPessoa(dao().consultar(t_lngIdPessoa, DpPessoa.class, false));
		} catch (Exception e) {
			throw new DJBuilderException("Parâmetro idPessoa inválido!");
		}

		this.configurarParametrosPadraoSistema(parametros);
		parametros.put("secaoUsuario", EMPTY);
	}
	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException {
		//this.setTemplateFile(null);
		this.setTitle("Permissão de " 
				        + getDescricaoTipoConfiguracao() 
						+ ": (" 
						+ dpPessoa.getSesbPessoa() +  dpPessoa.getMatricula()
						+ ") " 
						+ dpPessoa.getNomePessoa()  
						); 
		this.addColuna("Serviço",45,RelatorioRapido.ESQUERDA,false, false);
		this.addColuna("Situação",7,RelatorioRapido.ESQUERDA,false, false);
		this.addColuna("Origem", 7,RelatorioRapido.ESQUERDA,false, false);
		this.addColuna("Desde", 9,RelatorioRapido.ESQUERDA,false, false);  
		this.addColuna("Cadastrante", 9,RelatorioRapido.ESQUERDA,false, false);
		this.setPageSizeAndOrientation( Page.Page_A4_Landscape());
		return this;
	}
	/**
	 * Processa as configurações ativas para os vários ids da pessoa 
	 * (mesmo id inicial que a pessoa selecionada)
	 */
	@Override
	public Collection<String> processarDados() {
		final List<String> dados = new ArrayList<>();
		HashMap<CpServico, ConfiguracaoAcesso> achm = new HashMap<CpServico, ConfiguracaoAcesso>();
		List<CpServico> l = dao().listarServicos();
		try {
			for (CpServico srv : l) {
							ConfiguracaoAcesso ac = ConfiguracaoAcesso.gerar(null, dpPessoa,
									null, null, srv, null);
	
							achm.put(ac.getServico(), ac);
			}
		} catch(Exception e) {
			log.warn("Não foi possível processar permissões", e);
			return emptyList();
		}
		SortedSet<ConfiguracaoAcesso> acs = new TreeSet<ConfiguracaoAcesso>();
		for (ConfiguracaoAcesso ac : achm.values()) {
			if (ac.getServico().getCpServicoPai() == null) {
				acs.add(ac);
			} else {
				achm.get(ac.getServico().getCpServicoPai()).getSubitens()
						.add(ac);
			}
		}
		for (ConfiguracaoAcesso cfga : acs) {
			varrerConfiguracao ( cfga,  dados );
		}
		return dados;
	}
	/**
	 * Varre a árvore de configurações (ConfiguracaoAcesso) - organizada  
	 * @param cfga - Configuração acesso
	 * @param dados - coleção de linhas do relatório
	 */
	private void varrerConfiguracao (ConfiguracaoAcesso cfga, List<String> dados ) {
		processarConfiguracaoAcesso(cfga, dados);
		for ( ConfiguracaoAcesso cfgaSub : cfga.getSubitens()) {
			varrerConfiguracao ( cfgaSub,  dados);
		}
	}
	/**
	 * Preenche os dados com as informações da configuração já formatados
	 * @param cfga - Configuração acesso
	 * @param dados - coleção de linhas do relatório
	 */
	private void processarConfiguracaoAcesso(ConfiguracaoAcesso cfga, List<String> dados ) {
		try {dados.add(printSeparadorNivel(cfga.getServico().getNivelHierarquico() ) + printServico(cfga.getServico()));}  catch (Exception e) { dados.add("");}
		try {dados.add(cfga.getSituacao().getDscSitConfiguracao());}  catch (Exception e) { dados.add("");}
		try {dados.add(printOrigem(cfga));}  catch (Exception e) { dados.add("");}
		try {dados.add(printDate(cfga.getInicio()));}  catch (Exception e) { dados.add("");}
		try {dados.add(String.valueOf(cfga.getCadastrante().getSesbPessoa() + cfga.getCadastrante().getMatricula()));}  catch (Exception e) { dados.add("");}
	}
	private String printDate( Date dte) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return df.format(dte);
	}
	private String printServico(CpServico srv) {
		return srv.getSiglaSemPartePai() + " - " + srv.getDescricao();
	}
	/**
	 * 
	 * @param nivel o nível do serviço
	 * @return uma string para a indentação da descrição do serviço
	 */
	private String printSeparadorNivel(int nivel) {
		StringBuffer str = new StringBuffer();
		for (int cta = 0; cta < nivel; cta++) {
			if (cta == (nivel -1) ) {
				str.append(" . ");
			} else {
				str.append("   ");
			}
		}
		return str.toString();
	}
	/**
	 *  @param	configuração acesso
	 *  @return Uma String representativa da origem
	 */
	private String printOrigem(ConfiguracaoAcesso cfga) {
		return cfga.printOrigemCurta();
	}
	private Long getIdTipoConfiguracao() {
		return CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO;
	}
	private CpTipoConfiguracao getTipoConfiguracao() {
		return dao().consultar(getIdTipoConfiguracao(), CpTipoConfiguracao.class, false);
	}
	private String getDescricaoTipoConfiguracao() {
		return getTipoConfiguracao().getDscTpConfiguracao();
	}
	private CpDao dao() {
		return CpDao.getInstance();
	}
	/**
	 * @return the cpServico
	 */
	public DpPessoa getDpPessoa() {
		return dpPessoa;
	}
	/**
	 * @param cpServico the cpServico to set
	 */
	public void setDpPessoa(DpPessoa dpPessoa) {
		this.dpPessoa = dpPessoa;
	}
	public static void main (String args[]) throws Exception{
	}
}
