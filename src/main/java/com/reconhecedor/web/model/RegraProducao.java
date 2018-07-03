package com.reconhecedor.web.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
	@Pattern(regexp = "[\\w+-/*//&| ():;^]{1,}", message = "Lado Direito da produção somente permite letras, números ou os sinais de +, -, *, /, |, _, &, (, ), :, ; e ^")
	private String LD;

	// Vetor com a lista de todos operadores.
	private String[] listLE;
	private String[] listLD;

	public RegraProducao() {
		super();
	}

	public RegraProducao(String lE, String lD) {
		super();
		setLE(lE);
		setLD(lD);
	}

	/**
	 * Extrai um operador aleatório.
	 * 
	 * @return Operador LD
	 */
	public String randomLD() {
		int tam = listLD.length;
		int x = new Random().nextInt(tam);
		return listLD[x];
	}

	public String getLE() {
		return LE;
	}

	public void setLE(String lE) {
		// Usa vírgula como separador padrão.
		LE = lE.replaceAll("\\|", ",");

		// Gera a lista.
		listLE = LE.split(",");

		// remove espaços.
		for (int i = 0; i < listLE.length; i++) {
			listLE[i] = listLE[i].trim();
		}
	}

	public String getLD() {
		return LD;
	}

	public void setLD(String lD) {
		// Usa vírgula como separador padrão.
		LD = lD.replaceAll("\\|", ",");

		listLD = LD.split(",");

		// remove espaços.
		for (int i = 0; i < listLD.length; i++) {
			listLD[i] = listLD[i].trim();
		}
	}

	/**
	 * Recebe os NT válidos como parametro e retorna a lista com somente os NT do
	 * LD.
	 * 
	 * @param listNT
	 * @return
	 */
	public List<String> getListLD_NT(List<String> ferteis) {

		LinkedList<String> list = new LinkedList<>();

		// Percorre a lista do parâmetro
		for (int i = 0; i < ferteis.size(); i++) {
			// Percorre a lista de NT.
			for (int j = 0; j < listLD.length; j++) {
				String strLD = listLD[j];
				// Percorre cada símbolo da sentença.
				for (int k = 0; k < strLD.length(); k++) {
					String strSimb = strLD.substring(k, k + 1);
					// Verifica se existe na lista de NT válidos.
					if (ferteis.get(i).equals(strLD)) {
						list.add(ferteis.get(i));
					}
				}
			}
		}

		System.out.println(list);
		return list;
	}

	public String[] getListLE() {
		return listLE;
	}

	public String[] getListLD() {
		return listLD;
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
