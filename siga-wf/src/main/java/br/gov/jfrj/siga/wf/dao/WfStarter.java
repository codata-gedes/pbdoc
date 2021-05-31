package br.gov.jfrj.siga.wf.dao;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.gov.jfrj.siga.cp.util.SigaFlyway;

@Startup
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
public class WfStarter {

	public static EntityManagerFactory emf;

	@PostConstruct
	public void init() {
		SigaFlyway.migrate("java:/jboss/datasources/SigaWfDS", "sigawf", "sigawf");
		emf = Persistence.createEntityManagerFactory("default");
	}

}