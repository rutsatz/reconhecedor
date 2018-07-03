package com.reconhecedor.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

/**
 * Representa os dados digitados pelo usuário no Form.
 * 
 * @author Rafael
 *
 */
@Component
public class EntradaUsuario {

	/**
	 * Símbolos não terminais informados pelo usuário.
	 */
	@NotEmpty(message = "Símbolo não terminal deve ser informado!")
	private String naoTerminais;

	/**
	 * Símbolos terminais informados pelo usuário.
	 */
	@NotEmpty(message = "Símbolo terminal deve ser informado!")
	private String terminais;

	/**
	 * Símbolo de inicio de produção.
	 */
	@NotEmpty(message = "Símbolo de início de produção deve ser informado!")
	private String inicioProducao;

	/**
	 * Lista com as Regras de Produção da gramática.
	 */
	@Valid
	@NotEmpty(message = "Regras de produção devem ser informadas!")
	private List<RegraProducao> regrasProducao;

	/**
	 * Lista com os símbolos não terminais.
	 */
	private String[] listNT;

	/**
	 * Lista com os símbolos terminais.
	 */
	private String[] listT;

	/**
	 * Símbolos não terminais limpos, formatados para usar em REGEX ou outras
	 * expressões.
	 */
	private String formatedNT;

	/**
	 * Símbolos terminais formatados para usar em REGEX.
	 */
	private String formatedT;

	/**
	 * Construtor default para inicializar as regras de producao como LazyList.
	 */
	@SuppressWarnings("unchecked")
	public EntradaUsuario() {

		regrasProducao = LazyList.decorate(new ArrayList<RegraProducao>(),
				FactoryUtils.instantiateFactory(RegraProducao.class));

		// para testes.

		// setNaoTerminais("Z, Y, X");
		// setTerminais("a, c, d");
		// setInicioProducao("Z");
		// regrasProducao.add(new RegraProducao("Z", "XYZ, d"));
		// regrasProducao.add(new RegraProducao("Y", "c, &"));
		// regrasProducao.add(new RegraProducao("X", "Y, a"));

		// regrasProducao.add(new RegraProducao("Z", "Y, d"));
		// regrasProducao.add(new RegraProducao("Y", "Xc, &"));
		// regrasProducao.add(new RegraProducao("X", "aZ, a"));

		// setNaoTerminais("S, A, B");
		// setTerminais("(, ), a, b, :");
		// setInicioProducao("S");
		// regrasProducao.add(new RegraProducao("S", "(A), b"));
		// regrasProducao.add(new RegraProducao("A", "B:A, B"));
		// regrasProducao.add(new RegraProducao("B", "a, &"));
		//

		// setNaoTerminais("D, E, F, T, G");
		// setTerminais("v, ;, :, i, a");
		// setInicioProducao("D");
		// regrasProducao.add(new RegraProducao("D", "vE"));
		// regrasProducao.add(new RegraProducao("E", "F;E, &"));
		// regrasProducao.add(new RegraProducao("F", ":T"));
		// regrasProducao.add(new RegraProducao("T", "i"));
		// regrasProducao.add(new RegraProducao("G", "a"));

		// GR
		// setNaoTerminais("S, A, B, C, D");
		// setTerminais("a, b, c, d");
		// setInicioProducao("S");
		// regrasProducao.add(new RegraProducao("S", "aA"));
		// regrasProducao.add(new RegraProducao("A", "a, bB"));
		// regrasProducao.add(new RegraProducao("B", "b, &"));
		// regrasProducao.add(new RegraProducao("C", "cD, c"));
		// regrasProducao.add(new RegraProducao("D", "d"));

		// GLC
		// setNaoTerminais("A, B");
		// setTerminais("a,b");
		// setInicioProducao("S");
		// regrasProducao.add(new RegraProducao("S", "b , B"));

		// GSC
		// setNaoTerminais("A, B");
		// setTerminais("a,b");
		// setInicioProducao("S");
		// regrasProducao.add(new RegraProducao("aB", "bb , BB"));

		// GI
		// setNaoTerminais("A, B");
		// setTerminais("a,b");
		// setInicioProducao("S");
		// regrasProducao.add(new RegraProducao("AA", "b , &"));

		// Análise Ascendente
		setNaoTerminais("E, T, F, P");
		setTerminais("+, *, ^, (, ), i");
		setInicioProducao("E");
		regrasProducao.add(new RegraProducao("E", "E+T, T"));
		regrasProducao.add(new RegraProducao("T", "T*F, F"));
		regrasProducao.add(new RegraProducao("F", "P^F, P"));
		regrasProducao.add(new RegraProducao("P", "(E), i"));

	}

	/**
	 * Verifica se o símbolo informado é um terminal.
	 * 
	 * @param String
	 * @return true se for um Terminal.
	 */
	public boolean isTerminal(String simb) {
		for (String string : listT) {
			if (string.equals(simb))
				return true;
		}
		return false;
	}

	/**
	 * Verifica se o símbolo informado é um NT.
	 * 
	 * @param String
	 * @return true se for um Não Terminal.
	 */
	public boolean isNT(String simb) {
		for (String string : listNT) {
			if (string.equals(simb))
				return true;
		}
		return false;
	}

	/**
	 * Verifica se o símbolo é uma sentença vazia
	 * 
	 * @param simb
	 * @return true se o símbolo for sentença vazia.
	 */
	public boolean isSentencaVazia(String simb) {
		return simb.equals("&");
	}

	public String getNaoTerminais() {
		return naoTerminais;
	}

	/**
	 * Ao setar os NT, já gera a lista com os símbolos e formata para REGEX.
	 * 
	 * @param naoTerminais
	 */
	public void setNaoTerminais(String naoTerminais) {

		// Usa vírgula como separador padrão.
		this.naoTerminais = naoTerminais.replaceAll("\\|", ",");

		// inicio de produção entra como NT.
		if (inicioProducao != null && !this.naoTerminais.contains(inicioProducao))
			this.naoTerminais += "," + inicioProducao;

		// Gera a lista.
		listNT = this.naoTerminais.split(",");

		// remove espaços.
		for (int i = 0; i < listNT.length; i++) {
			listNT[i] = listNT[i].trim();
		}

		// remover vírgulas e espaços.
		String remover = ", ";
		String aux = naoTerminais;

		for (int i = 0; i < remover.length(); i++) {
			// Remove os caracteres indesejados.
			aux = aux.replace(String.valueOf(remover.charAt(i)), "");
		}

		this.formatedNT = aux;
	}

	public String getTerminais() {
		return terminais;
	}

	/**
	 * Ao setar os terminais, já gera a lista com os símbolos e formata para REGEX.
	 * 
	 * @param terminais
	 */
	public void setTerminais(String terminais) {

		// Usa vírgula como separador padrão.
		this.terminais = terminais.replaceAll("\\|", ",");

		// Gera a lista.
		listT = this.terminais.split(",");

		// remove espaços.
		for (int i = 0; i < listT.length; i++) {
			listT[i] = listT[i].trim();
		}

		// remover vírgulas e espaços.
		String remover = ", ";
		String aux = terminais;

		for (int i = 0; i < remover.length(); i++) {
			// Remove os caracteres indesejados.
			aux = aux.replace(String.valueOf(remover.charAt(i)), "");
		}
		this.formatedT = aux;
	}

	public String getInicioProducao() {
		return inicioProducao;
	}

	public void setInicioProducao(String inicioProducao) {
		this.inicioProducao = inicioProducao;
	}

	public List<RegraProducao> getRegrasProducao() {
		return regrasProducao;
	}

	public void setRegrasProducao(List<RegraProducao> regrasProducao) {
		this.regrasProducao = regrasProducao;
	}

	public String[] getListNT() {
		return listNT;
	}

	public String[] getListT() {
		return listT;
	}

	public String getFormatedNT() {
		return formatedNT;
	}

	public String getFormatedT() {
		return formatedT;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EntradaUsuario [naoTerminais=");
		builder.append(naoTerminais);
		builder.append(", terminais=");
		builder.append(terminais);
		builder.append(", inicioProducao=");
		builder.append(inicioProducao);
		builder.append(", regrasProducao=");
		builder.append(regrasProducao);
		builder.append("]");
		return builder.toString();
	}

}
