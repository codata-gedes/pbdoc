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
package br.gov.jfrj.sigale.ex.vo;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.gov.jfrj.siga.base.util.Utils;

public class ExApiVO {
	List<ExAcaoApiVO> acoes = new ArrayList<ExAcaoApiVO>();

	private class NomeExAcaoApiVOComparator implements Comparator<ExAcaoApiVO> {

		public int compare(ExAcaoApiVO o1, ExAcaoApiVO o2) {
			return o1.getNome().replace("_", "")
					.compareTo(o2.getNome().replace("_", ""));
		}
	}

	public List<ExAcaoApiVO> getAcoes() {
		return acoes;
	}

	public List<ExAcaoApiVO> getAcoesOrdenadasPorNome() {
		return ExAcaoApiVO.ordena(getAcoes(), new NomeExAcaoApiVOComparator());
	}

	public void setAcoes(List<ExAcaoApiVO> acoes) {
		this.acoes = acoes;
	}

	protected void addAcao(String icone, String nome, String nameSpace,
			String action, boolean pode) {
		addAcao(icone, nome, nameSpace, action, pode, null, null, null, null,
				null);
	}

	protected void addAcao(String icone, String nome, String nameSpace,
			String action, boolean pode, String msgConfirmacao,
			String parametros, String pre, String pos, String classe) {

		Map<String, Object> params = new TreeMap<>();

		if (this instanceof ExMovimentacaoApiVO) {
			params.put("id",
					Long.toString(((ExMovimentacaoApiVO) this).getIdMov()));
			// params.put("sigla", ((ExMovimentacaoVO)
			// this).getMobilVO().getSigla());
		} else if (this instanceof ExMobilApiVO) {
			params.put("sigla", ((ExMobilApiVO) this).getSigla());
		} else if (this instanceof ExDocumentoApiVO) {
			params.put("sigla", ((ExDocumentoApiVO) this).getSigla());
		}

		if (parametros != null) {
			if (parametros.startsWith("&")) {
				parametros = parametros.substring(1);
			}
			else {
				params.clear();
			}
			Utils.mapFromUrlEncodedForm(params, parametros.getBytes(StandardCharsets.ISO_8859_1));
		}

		if (pode) {
			ExAcaoApiVO acao = new ExAcaoApiVO(icone, nome, nameSpace, action, pode, msgConfirmacao, params, pre, pos, classe, null);
			acoes.add(acao);
		}
	}
}
