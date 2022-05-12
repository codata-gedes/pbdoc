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
package br.gov.jfrj.siga.hibernate.ext;

import java.util.Date;

public interface IExMobilDaoFiltro {

	void setSigla(final String nome);

	Long getAnoEmissao();

	Long getClassificacaoSelId();

	String getDescrDocumento();

	Long getDestinatarioSelId();

	Long getIdFormaDoc();

	Long getIdTpDoc();

	Long getLotacaoDestinatarioSelId();

	String getNmDestinatario();

	String getNmSubscritorExt();

	Long getNumExpediente();

	String getNumExtDoc();

	Long getOrgaoExternoDestinatarioSelId();

	Long getSubscritorSelId();

	Long getUltMovIdEstadoDoc();

	Long getUltMovLotaRespSelId();

	Long getUltMovRespSelId();

	void setAnoEmissao(final Long anoEmissao);

	void setClassificacaoSelId(final Long classificacaoSelId);

	void setDescrDocumento(final String descrDocumento);

	String getFullText();

	void setFullText(String fullText);

	void setDestinatarioSelId(final Long destinatarioSelId);

	void setIdFormaDoc(final Long idFormaDoc);

	void setIdTpDoc(final Long idTpDoc);

	void setLotacaoDestinatarioSelId(
			final Long lotacaoDestinatarioSelId);

	void setNmDestinatario(final String nmDestinatario);

	void setNmSubscritorExt(final String nmSubscritorExt);

	void setNumExpediente(final Long numExpediente);

	void setNumExtDoc(final String numExtDoc);

	void setOrgaoExternoDestinatarioSelId(
			final Long orgaoExternoDestinatarioSelId);

	void setSubscritorSelId(final Long subscritorSelId);

	void setUltMovIdEstadoDoc(final Long ultMovIdEstadoDoc);

	void setUltMovLotaRespSelId(final Long ultMovLotaRespSelId);

	void setUltMovRespSelId(final Long ultMovRespSelId);

	Long getId();

	Integer getNumSequencia();

	void setNumSequencia(final Integer numSequencia);

	Long getIdDoc();

	void setIdDoc(final Long idDoc);

	Long getOrgaoExternoSelId();

	void setOrgaoExternoSelId(final Long orgaoExternoSelId);

	Date getDtDoc();

	void setDtDoc(final Date dtDoc);

	String getNumAntigoDoc();

	void setNumAntigoDoc(String numAntigoDoc);

	Long getLotacaoCadastranteAtualId();

	void setLotacaoCadastranteAtualId(
			Long lotacaoCadastranteAtualId);

	Long getIdOrgaoUsu();

	void setIdOrgaoUsu(Long idOrgaoUsu);

	Long getCadastranteSelId();

	void setCadastranteSelId(Long cadastranteSelId);

	Long getLotaCadastranteSelId();

	void setLotaCadastranteSelId(Long lotaCadastranteSelId);

	Date getDtDocFinal();

	void setDtDocFinal(Date dtDocFinal);

	Long getIdTipoMobil();

	void setIdTipoMobil(Long idTipoMobil);

	Long getIdTipoFormaDoc();

	void setIdTipoFormaDoc(Long idTipoFormaDoc);

	Long getIdMod();

	void setIdMod(Long idMod);

	void setOrdem(Integer ordem);

	Integer getOrdem();

	boolean isApenasDocumentosAssinados();

	boolean buscarPorCamposMarca();

	boolean buscarPorCamposDoc();

}
