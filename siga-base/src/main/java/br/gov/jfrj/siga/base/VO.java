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
package br.gov.jfrj.siga.base;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import br.gov.jfrj.siga.base.util.Utils;

public class VO {
	List<AcaoVO> acoes = new ArrayList<>();

	private class NomeAcaoVOComparator implements Comparator<AcaoVO> {

		public int compare(AcaoVO o1, AcaoVO o2) {
			return o1.getNome().replace("_", "").compareTo(o2.getNome().replace("_", ""));
		}
	}

	public List<AcaoVO> getAcoes() {
		return acoes;
	}

	public List<AcaoVO> getAcoesOrdenadasPorNome() {
		return AcaoVO.ordena(getAcoes(), new NomeAcaoVOComparator());
	}

	public void setAcoes(List<AcaoVO> acoes) {
		this.acoes = acoes;
	}
	
	public void addAcao(AcaoVO acao) {
		acoes.add(acao);
	}

	public void addAcao(
			String icone,
			String nome,
			String nameSpace,
			String action,
			boolean pode) {
		addAcao(icone, nome, nameSpace, action, pode, null, null, null, null, null, null, null);
	}

	public void addAcao(
			String icone,
			String nome,
			String nameSpace,
			String action,
			boolean pode,
			String tooltip, 
			String msgConfirmacao,
			String parametros,
			String pre,
			String pos,
			String classe,
			String modal) {

		Map<String, Object> params = new LinkedHashMap<>();
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
			String hintEscapado = StringUtils.replace(nome, "_", "");
			AcaoVO acao = new AcaoVO(icone, nome, nameSpace, action, pode, msgConfirmacao, params, pre, pos, classe, modal, hintEscapado);
			acoes.add(acao);
		}
	}
}