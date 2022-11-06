package br.gov.jfrj.siga.model.dao.extended;

import static br.gov.jfrj.siga.model.ContextoPersistencia.em;

/**
 * Camada de persistência genérica adaptada para o SIGA
 * 
 * @author Michel Risucci
 */
public final class DAO {

	private DAO() {}

	public static final ExtendedPersistenceLayer getPersistenceLayer() {
		return QueryDslExtendedThreadScopedEntityManager.getScopedManager(em());
	}

}
