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
package br.gov.jfrj.siga.ex.vo;

import static java.util.Optional.ofNullable;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.VO;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExVO extends VO {

	private static final String ID = "id";
	private static final String MOV_ID_DOC = "movDocId";
	private static final String SIGLA = "sigla";

	public void addAcao(
			String icone,
			String nome,
			String nameSpace,
			String action,
			boolean pode,
			String msgConfirmacao,
			String parametros,
			String pre,
			String pos,
			String classe) {
		addAcao(icone, nome, nameSpace, action, pode, null, msgConfirmacao, parametros, pre, pos, classe, null);
	}

	@Override
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

		Long movIdDoc = null;
		final Map<String, Object> params = new LinkedHashMap<>();
		if (this instanceof ExMovimentacaoVO) {
			final ExMovimentacaoVO vo = (ExMovimentacaoVO) this;
			ofNullable(vo)
					.map(ExMovimentacaoVO::getIdMov)
					.map(Long::valueOf)
					.ifPresent(value -> params.put(ID, value));
			ofNullable(vo)
					.map(ExMovimentacaoVO::getMobilVO)
					.map(ExMobilVO::getSigla)
					.ifPresent(value -> params.put(SIGLA, value));
			movIdDoc = ofNullable(vo)
					.map(ExMovimentacaoVO::getMov)
					.map(ExMovimentacao::getExDocumento)
					.map(ExDocumento::getId)
					.orElse(null);
		}

		if (this instanceof ExMobilVO) {
			final ExMobilVO vo = (ExMobilVO) this;
			ofNullable(vo)
					.map(ExMobilVO::getSigla)
					.ifPresent(id -> params.put(SIGLA, id));
		}

		if (this instanceof ExDocumentoVO) {
			final ExDocumentoVO vo = (ExDocumentoVO) this;
			ofNullable(vo)
					.map(ExDocumentoVO::getSigla)
					.ifPresent(id -> params.put(SIGLA, id));
			params.put(SIGLA, ((ExDocumentoVO) this).getSigla());
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
			if (movIdDoc != null) {
				params.put(MOV_ID_DOC, movIdDoc);
			}

			String hintEscapado = StringUtils.replace(nome, "_", "");
			AcaoVO acao = new AcaoVO(icone, nome, nameSpace, action, pode, msgConfirmacao, params, pre, pos, classe, modal, hintEscapado);
			getAcoes().add(acao);
		}
	}

}
