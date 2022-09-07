/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * Criado em 23/11/2005
 */

package br.gov.jfrj.siga.vraptor;

import static java.util.Collections.singletonList;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.CpAcesso;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpVisualizacao;
import br.gov.jfrj.siga.ex.bl.AcessoConsulta;
import br.gov.jfrj.siga.ex.bl.Mesa2;
import br.gov.jfrj.siga.ex.bl.Mesa2.SelGrupo;
import br.gov.jfrj.siga.hibernate.ExDao;

@Controller
public class ExMesa2Controller extends ExController {

	private static final Logger log = Logger.getLogger(ExMesa2Controller.class);

	private static final TypeReference<Map<String, SelGrupo>> RESPONSE_SERIALIZATION_VALUE_TYPE_REF = new TypeReference<Map<String, Mesa2.SelGrupo>>() {};

	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	private static final List<Long> MARCAS_IGNORAR_PADRAO = singletonList(
			CpMarcadorEnum.CANCELADO.getId()
	);
	private static final List<Long> MARCAS_IGNORAR_SIGA_SP = Arrays.asList(
			CpMarcadorEnum.CANCELADO.getId(),
			CpMarcadorEnum.ARQUIVADO_CORRENTE.getId(),
			CpMarcadorEnum.ARQUIVADO_INTERMEDIARIO.getId(),
			CpMarcadorEnum.ARQUIVADO_PERMANENTE.getId()
	);

	/**
	 * @deprecated CDI eyes only
	 */
	public ExMesa2Controller() {
		super();
	}

	@Inject
	public ExMesa2Controller(HttpServletRequest request, HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	@Get("app/mesa2")
	public void lista(Boolean exibirAcessoAnterior, Long idVisualizacao, String msg) throws Exception {
		result.include("ehPublicoExterno", AcessoConsulta.ehPublicoExterno(getTitular()));
		result.include("podeNovoDocumento",
				Cp.getInstance().getConf().podePorConfiguracao(getTitular(), getTitular().getLotacao(),
				CpTipoConfiguracao.TIPO_CONFIG_CRIAR_NOVO_EXTERNO)
		);

		if (exibirAcessoAnterior != null && exibirAcessoAnterior) {
			final CpAcesso acessoAnterior = dao.consultarAcessoAnterior(so.getCadastrante());
			if (acessoAnterior == null) {
				result.include("idVisualizacao", 0);
				return;
			}
			final String acessoAnteriorData = Data.formatDDMMYY_AS_HHMMSS(acessoAnterior.getDtInicio());
			final String acessoAnteriorMaquina = acessoAnterior.getAuditIP();
			result.include("acessoAnteriorData", acessoAnteriorData);
			result.include("acessoAnteriorMaquina", acessoAnteriorMaquina);
		}

		if(idVisualizacao != null) {
			DpVisualizacao vis = dao().consultar(idVisualizacao, DpVisualizacao.class, false);
			if(vis != null && vis.getDelegado().equals(getTitular())) {
				result.include("idVisualizacao", idVisualizacao);
				result.include("visualizacao", vis);
			} else {
				result.include("idVisualizacao", 0);
			}
		} else {
			result.include("idVisualizacao", 0);
		}

		if (msg != null) {
			result.include("mensagemCabec", msg);
			result.include("msgCabecClass", "alert-info fade-close");
		}
	}

	@Post("app/mesa2.json")
	public void json(Long idVisualizacao, boolean exibeLotacao, boolean trazerAnotacoes, boolean trazerArquivados, 
			boolean trazerComposto, boolean trazerCancelados, boolean ordemCrescenteData, 
			boolean usuarioPosse, String parms) throws Exception {

		final Instant inicio = Instant.now();

		try {
			final boolean publicoExterno = AcessoConsulta.ehPublicoExterno(getTitular()); 
			result.include("ehPublicoExterno", publicoExterno);
			final boolean deveCarregarLotacao = Prop.getBool("/siga.mesa.carrega.lotacao");

			if (exibeLotacao && (publicoExterno || !deveCarregarLotacao)) {
				result.use(Results.http())
						.addHeader(CONTENT_TYPE, TEXT_PLAIN)
						.body("Não é permitido exibir dados da sua " + SigaMessages.getMessage("usuario.lotacao"))
						.setStatusCode(HttpServletResponse.SC_OK);

				final Duration tempo = Duration.between(inicio, Instant.now());
				log.debugv("Carregamento de mesa: {0}ms", tempo.toMillis());
				return;
			}

			final Map<String, Mesa2.SelGrupo> selGrupos = isNotBlank(parms)
					? JSON_MAPPER.readValue(parms, RESPONSE_SERIALIZATION_VALUE_TYPE_REF)
					: null;

			final List<Long> marcasIgnorar = getMarcasIgnorar(trazerCancelados);
			final DpPessoa titular = getTitular(idVisualizacao);

			final List<Mesa2.GrupoItem> mesa = Mesa2.getContadores(dao(), titular, titular.getLotacao(), selGrupos, exibeLotacao, marcasIgnorar);
			Mesa2.popularDocumentos(dao(), titular, titular.getLotacao(), selGrupos, mesa, exibeLotacao, trazerAnotacoes, trazerComposto, ordemCrescenteData, usuarioPosse, marcasIgnorar);

			result.use(Results.http())
					.addHeader(CONTENT_TYPE, APPLICATION_JSON)
					.body(ExAssinadorExternoController.gson.toJson(mesa))
					.setStatusCode(HttpServletResponse.SC_OK);

			final Duration tempo = Duration.between(inicio, Instant.now());
			log.debugv("Carregamento de mesa: {0}ms", tempo.toMillis());
		} catch (Exception e) {
			throw e;
		} 
	}

	private List<Long> getMarcasIgnorar(final boolean trazerCancelados) {
		if (trazerCancelados) {
			return Collections.emptyList();
		}

		return SigaMessages.isSigaSP() ? MARCAS_IGNORAR_SIGA_SP : MARCAS_IGNORAR_PADRAO;
	}

	private DpPessoa getTitular(final Long idVisualizacao) throws Exception {
		if (idVisualizacao == null || idVisualizacao.longValue() <= 0L) {
			return this.getTitular();
		}

		final DpPessoa cadastrante = this.getCadastrante();
		boolean cadastrantePodeDelegarVisualizacao = Cp.getInstance().getConf().podePorConfiguracao(
				cadastrante,
				cadastrante.getLotacao(),
				CpTipoConfiguracao.TIPO_CONFIG_DELEGAR_VISUALIZACAO
		);

		if (!cadastrantePodeDelegarVisualizacao) {
			return this.getTitular();
		}

		final DpVisualizacao vis = dao().consultar(idVisualizacao, DpVisualizacao.class, false);
		return vis.getTitular();
	}

}
