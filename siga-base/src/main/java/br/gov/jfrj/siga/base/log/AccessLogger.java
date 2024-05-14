package br.gov.jfrj.siga.base.log;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import br.gov.jfrj.siga.model.ContextoPersistencia;

public class AccessLogger {
	
	private static final Logger logger = Logger.getLogger(AccessLogger.class.getName());
	
	private static final ArrayList<String> atributosOcultos = new ArrayList<String>() {
		{
		add("meusDelegados");
		add("meusTitulares");
		add("identidadeCadastrante");
		}
	};

	
	public static void logAcesso(HttpServletRequest httpReq, String sigla) {
		
		StringBuffer caminho = httpReq.getRequestURL();
		String parametros = httpReq.getQueryString() == null ? "" : "?" + httpReq.getQueryString();
		caminho.append(parametros);

		StringBuffer requestInfo = new StringBuffer();

		requestInfo.append("\n\n ----- Detalhes do Acesso -----\n");
		
		requestInfo.append("\nURL: ");
		requestInfo.append(caminho);
		requestInfo.append("\n");

		requestInfo.append("Método: ");
		requestInfo.append(httpReq.getMethod());
		requestInfo.append("\n");

		requestInfo.append("Session ID: ");
		requestInfo.append(httpReq.getRequestedSessionId() == null ? "indefinido" : httpReq.getRequestedSessionId());
		requestInfo.append("\n");

		requestInfo.append("User Principal: ");
		requestInfo.append(ContextoPersistencia.getUserPrincipal() == null ? "indefinido": ContextoPersistencia.getUserPrincipal());
		requestInfo.append("\n");

		requestInfo.append("Thread ID: ");
		requestInfo.append(Thread.currentThread().getId());
		requestInfo.append("\n");

		requestInfo.append("Thread Name: ");
		requestInfo.append(Thread.currentThread().getName());
		requestInfo.append("\n");

		requestInfo.append("Remote Host: ");
		requestInfo.append(httpReq.getRemoteHost());
		requestInfo.append("\n");
		
		requestInfo.append("Sigla: ");
		requestInfo.append(sigla);
		requestInfo.append("\n");

		requestInfo.append("Atributos: \n");
		Enumeration<String> attrs = httpReq.getAttributeNames();
		while (attrs.hasMoreElements()) {
			String name = attrs.nextElement();
			if (name.startsWith("org.jboss.weld") || atributosOcultos.contains(name)) 
				continue;
			requestInfo.append("\t");
			requestInfo.append(name);
			requestInfo.append(" : ");
			try {
				String a = httpReq.getAttribute(name).toString();
				if (a != null && a.length() > 200)
					a = "[atributo encurtado] " + a.substring(0, 200) + "...";
				requestInfo.append(a);
			} catch (Exception e) {
				requestInfo.append("não foi possível determinar: ");
				requestInfo.append(e.getMessage());
			}
			requestInfo.append("\n");
		}
		
		logger.info(requestInfo.toString());
	}
	
}