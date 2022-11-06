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
package br.gov.jfrj.siga.dp;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import br.gov.jfrj.siga.cp.CpConvertableEntity;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

@Entity
@Table(name = "corporativo.cp_orgao_usuario")
@Cacheable
@Cache(region = CpDao.CACHE_CORPORATIVO, usage = CacheConcurrencyStrategy.READ_ONLY)
public class CpOrgaoUsuario extends AbstractCpOrgaoUsuario implements Selecionavel, CpConvertableEntity {

	private static final long serialVersionUID = -373979421641186860L;

	@Formula(value = "REMOVE_ACENTO(NM_ORGAO_USU)")
	private String nmOrgaoAI;

	@Column(name = "HIS_DT_INI")
	@Desconsiderar
	private Date hisDtIni;

	@Column(name = "HIS_DT_FIM")
	@Desconsiderar
	private Date hisDtFim;

	@Desconsiderar
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HIS_IDC_INI")
	private CpIdentidade hisIdcIni;

	@Desconsiderar
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HIS_IDC_FIM")
	private CpIdentidade hisIdcFim;

	@Column(name = "HIS_ATIVO")
	private Integer hisAtivo;

	@Desconsiderar
	@Column(name = "HIS_ID_INI", nullable = false)
	private Long idInicial;

	public CpOrgaoUsuario() {
		super();
	}

	public String iniciais(String s) {
		final StringBuilder sb = new StringBuilder(10);
		boolean f = true;

		s = s.replace(" E ", " ");
		s = s.replace(" DA ", " ");
		s = s.replace(" DE ", " ");
		s = s.replace(" DO ", " ");

		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (f) {
				sb.append(c);
				f = false;
			}
			if (c == ' ') {
				f = true;
			}
		}
		return sb.toString();
	}

	public String getIniciais() {
		return iniciais(getNmOrgaoUsu());
	}

	public Long getId() {
		return getIdOrgaoUsu();
	}
	
	public String getSigla() {
		return getSiglaOrgaoUsu();
	}

	public void setSigla(final String sigla) {
		setSiglaOrgaoUsu(sigla);
	}

	public String getDescricao() {
		return getNmOrgaoUsu();
	}

	public String getDescricaoMaiusculas() {
		return getNmOrgaoUsu().toUpperCase();
	}

	public String getNmOrgaoAI() {
		return nmOrgaoAI;
	}

	@Override
	public Date getHisDtIni() {
		return hisDtIni;
	}

	@Override
	public void setHisDtIni(Date hisDtIni) {
		this.hisDtIni = hisDtIni;
	}

	@Override
	public Date getHisDtFim() {
		return hisDtFim;
	}

	@Override
	public void setHisDtFim(Date hisDtFim) {
		this.hisDtFim = hisDtFim;
	}

	public CpIdentidade getHisIdcIni() {
		return hisIdcIni;
	}

	public CpIdentidade getHisIdcFim() {
		return hisIdcFim;
	}

	@Override
	public Integer getHisAtivo() {
		return hisAtivo;
	}

	@Override
	public void setHisAtivo(Integer hisAtivo) {
		this.hisAtivo = hisAtivo;
	}

	@Override
	public Long getIdInicial() {
		return this.idInicial;
	}

	public void setIdInicial(Long idInicial) {
		this.idInicial = idInicial;
	}

	@Override
	public void setId(Long id) {
		this.setIdOrgaoUsu(id);
	}

	@Override
	public Long getHisIdIni() {
		return this.getIdInicial();
	}

	@Override
	public void setHisIdIni(Long hisIdIni) {
		this.setIdInicial(hisIdIni);
	}

}
