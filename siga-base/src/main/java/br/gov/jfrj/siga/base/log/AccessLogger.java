package br.gov.jfrj.siga.base.log;

import java.util.logging.Logger;

public class AccessLogger {
	
	private static final Logger logger = Logger.getLogger(AccessLogger.class.getName());
	
	public void accessUser() {
		String usuario = "joao123";
		String cpf = "123.456.789-01";
        String matricula = "M12345";
        String dataAcesso = "2024-05-13 10:30:00";
	
        logger.info(String.format("O Usuário %s acessou: CPF=%s, Matrícula=%s, Data=%s", usuario, maskCpf(cpf), matricula, dataAcesso));
	}

	private Object maskCpf(String cpf) {
		 return "***" + cpf.substring(9);
	}
}