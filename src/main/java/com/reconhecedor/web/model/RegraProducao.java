package com.reconhecedor.web.model;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Representa as entradas em string fornecidas pelo usuário para o Lado Esquerdo
 * e Lado Direito.
 * 
 * @author Rafael
 *
 */
public class RegraProducao {

	@NotEmpty(message = "Lado Esquerdo da produção deve ser preenchido!")
	@Pattern(regexp = "[\\w+-/*// ]{1,}", message = "Lado Esquerdo da produção somente permite letras, números ou os sinais de +, -, *, / e _")
	private String LE;

	@NotEmpty(message = "Lado direito da produção deve ser informado!")
	@Pattern(regexp = "[\\w+-/*//& ]{1,}", message = "Lado Direito da produção somente permite letras, números ou os sinais de +, -, *, /, _ e &")
	private String LD;

	public RegraProducao() {
		super();
	}

	public RegraProducao(String lE, String lD) {
		super();
		LE = lE;
		LD = lD;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegraProducaoString [LE=");
		builder.append(LE);
		builder.append(", LD=");
		builder.append(LD);
		builder.append("]");
		return builder.toString();
	}
}
