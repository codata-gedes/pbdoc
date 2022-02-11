package br.gov.jfrj.siga.base;

import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;

import java.util.Arrays;

public enum Ambiente {

	PRODUCAO("prod"),
	HOMOLOGACAO("homolo"),
	TREINAMENTO("treina"),
	DESENVOLVIMENTO("desenv"),
	;

	private final String valor;

	private Ambiente(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}

	public static Ambiente porValor(final String valor) {
		return Arrays.stream(values())
				.filter(ambiente -> equalsIgnoreCase(ambiente.getValor(), valor))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
