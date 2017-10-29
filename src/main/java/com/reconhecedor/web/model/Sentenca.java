package com.reconhecedor.web.model;

/**
 * Representa uma parte da senteca que está sendo gerada.
 * 
 * @author Rafael
 *
 */
public class Sentenca implements Cloneable {

	/**
	 * Literal que representa a sentenca.
	 */
	private String sentenca;

	/**
	 * Simbolo usado na derivacao, quando houver.
	 */
	private String simboloDerivacao;

	/**
	 * Indice do simbolo usado na derivação, pois podem haver mais de um.
	 */
	private int indexSimboloDerivacao;

	/**
	 * Log com o histórico das derivações.
	 */
	private String log;
	
	/**
	 * Construtor.
	 * 
	 * @param sentenca
	 */
	public Sentenca(String sentenca) {
		setSentenca(sentenca);
	}

	/**
	 * Verifica se a sentença é terminal.
	 * 
	 * @return true se for terminal.
	 */
	public boolean isTerminal() {
		return (simboloDerivacao == null ? true : false);
	}

	public String getSentenca() {
		return sentenca;
	}

	public void setSentenca(String sentenca) {
		this.sentenca = sentenca;
	}

	public String getSimboloDerivacao() {
		return simboloDerivacao;
	}

	public void setSimboloDerivacao(String simboloDerivacao) {
		this.simboloDerivacao = simboloDerivacao;
	}

	public int getIndexSimboloDerivacao() {
		return indexSimboloDerivacao;
	}

	public void setIndexSimboloDerivacao(int indexSimboloDerivacao) {
		this.indexSimboloDerivacao = indexSimboloDerivacao;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	@Override
	public Sentenca clone() throws CloneNotSupportedException {
		return (Sentenca) super.clone();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Sentenca [sentenca=");
		builder.append(sentenca);
		builder.append(", simboloDerivacao=");
		builder.append(simboloDerivacao);
		builder.append(", indexSimboloDerivacao=");
		builder.append(indexSimboloDerivacao);
		builder.append(", isTerminal()=");
		builder.append(isTerminal());
		builder.append("]\n");
		return builder.toString();
	}
}