package com.reconhecedor.web.model;

/**
 * Tipos de gramática.
 * 
 * @author Rafael
 *
 */
public enum TipoGramatica {

	GI("Gramática Irrestrita"), GSC("Gramática Sensível ao Contexto"), GLC("Gramática Livre de Contexto"), GR(
			"Gramática Regular");

	/**
	 * Descrição da gramática.
	 */
	private String descricao;

	/**
	 * Símbolos Não Terminais.
	 */
	private String naoTerminais;

	/**
	 * Símbolos Terminais.
	 */
	private String terminais;

	/**
	 * Simbolo de início de produção.
	 */
	private String inicioProducao;

	/**
	 * Lado Esquerdo da expressão.
	 */
	private String LE;

	/**
	 * Lado Direito da expressão.
	 */
	private String LD;

	private TipoGramatica(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Expressão Regular para o LE.
	 * 
	 * @return A expressão regular válida para a gramática (LE).
	 */
	public String getLERegexPattern() {

		String pattern = null;

		if (this == GR) {

			// String final da expressão regular.
			String str = formatLE(inicioProducao + naoTerminais);

			// Obrigatóriamente uma ocorrência de um dos símbolos do grupo
			pattern = "[" + str + "]{1}";

		} else if (this == GLC) {

			// String final da expressão regular.
			String str = formatLE(inicioProducao + naoTerminais);

			// Obrigatóriamente uma ocorrência de 1 NT.
			pattern = "[" + str + "]{1}";

		} else if (this == GSC) {

			// String final da expressão regular.
			String str = formatLE(inicioProducao + naoTerminais + terminais);

			// Valida se contém somente T ou NT.
			// Exemplo: [SABab]{1,}
			pattern = "[" + str + "]{1,}";
		} else if (this == GI) {

			// String final da expressão regular.
			String str = formatLE(inicioProducao + naoTerminais + terminais);

			// Valida se contém somente T ou NT.
			// Exemplo: [SABab]{1,}
			pattern = "[" + str + "]{1,}";
		}

		return pattern;
	}

	/**
	 * Expressão regular para o LD.
	 * 
	 * @return
	 */
	public String getLDRegexPattern() {
		String pattern = null;

		// Remove caracteres indesejados.
		String naoTerminais = formatLE(this.naoTerminais);

		String terminais = formatLD(this.terminais);

		if (this == GR) {

			// Somente 1T ou Símbolo Vazio ou 1 T/NT.
			// Exemplo: [ab]{1}|([ab]{1}[ABS]{1})|&
			pattern = "[" + terminais + "]{1}|([" + terminais + "]{1}[" + naoTerminais + inicioProducao + "]{1})|&";

		} else if (this == GLC) {

			// Não pode ter &.
			// Exemplo: [abABS]{1,}
			pattern = "[" + terminais + naoTerminais + inicioProducao + "]{1,}";

		} else if (this == GSC) {

			// Não pode ter &.
			// Exemplo: [abABS]{1,}
			pattern = "[" + terminais + naoTerminais + inicioProducao + "]{1,}";

		} else if (this == GI) {

			// Valida se contém somente T e NT ou somente &.
			// Exemplo: [abABS]{1,}|&
			pattern = "[" + terminais + naoTerminais + inicioProducao + "&]{1,}";

		}
		return pattern;
	}

	/**
	 * Formata a string (LE) para a expressão regular.
	 * 
	 * @param String
	 *            para formatar
	 * @return String formatada
	 */
	private String formatLE(String str) {
		// remover vírgulas e espaços.
		String remover = ", ";

		for (int i = 0; i < remover.length(); i++) {
			// Remove os caracteres indesejados.
			str = str.replace(String.valueOf(remover.charAt(i)), "");
		}

		// String final da expressão regular.
		return str;
	}

	/**
	 * Formata a string (LD) para a expressão regular.
	 * 
	 * @param String
	 *            para formatar
	 * @return String formatada
	 */
	private String formatLD(String str) {
		// remover espaços vírgulas e espaços.
		String remover = ", ";

		for (int i = 0; i < remover.length(); i++) {
			// Remove os caracteres indesejados.
			str = str.replace(String.valueOf(remover.charAt(i)), "");
		}

		// String final da expressão regular.
		return str;
	}

	public String getNaoTerminais() {
		return naoTerminais;
	}

	public void setNaoTerminais(String naoTerminais) {
		this.naoTerminais = naoTerminais;
	}

	public String getTerminais() {
		return terminais;
	}

	public void setTerminais(String terminais) {
		this.terminais = terminais;
	}

	public String getInicioProducao() {
		return inicioProducao;
	}

	public void setInicioProducao(String inicioProducao) {
		this.inicioProducao = inicioProducao;
	}

	public String getLE() {
		return LE;
	}

	public void setLE(String lE) {
		LE = lE;
	}

	public String getLD() {
		return LD;
	}

	public void setLD(String lD) {
		LD = lD;
	}

}
