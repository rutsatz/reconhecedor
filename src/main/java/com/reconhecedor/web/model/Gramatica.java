package com.reconhecedor.web.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Classe que representa uma gramática.
 * 
 * @author Rafael
 *
 */
@Component
public class Gramatica {

	private TipoGramatica tipoGramatica;

	HashMap<String, List<String>> mapFirst = new HashMap<>();

	/**
	 * Guarda as strings digitadas pelo usuário.
	 */
	@Autowired
	private EntradaUsuario entradaUsuario;

	public HashMap<String, List<String>> conjuntoFirst() {

		System.out.println("Método first");

		// Procura primeiro o first de entradas simples.
		// Quando os first forem compostos de terminais.
		for (RegraProducao rp : entradaUsuario.getRegrasProducao()) {
			int rpSize = rp.getListLD().length; // Tamanho de cada LD.
			int totTerminais = 0; // Acumulador auxiliar
			ArrayList<String> rpFirst = new ArrayList<>(); // Guarda os simbolos first.
			// Avalia cada corpo do LD.
			for (int i = 0; i < rpSize; i++) {
				String simbolo = rp.getListLD()[i];
				String first = simbolo.substring(0, 1); // Extrai o primeiro simbolo.
				// Se o simbolo avaliado for terminal.
				if (entradaUsuario.isTerminal(first) || entradaUsuario.isSentencaVazia(first)) {
					rpFirst.add(first);
					totTerminais++;
				}
			}

			// Se todos os first forem terminais, adiciona na lista.
			if (rpSize == totTerminais) {
				mapFirst.put(rp.getLE(), rpFirst);
			}
		}

		// Depois de encontrar os terminais, busca pelo restante (NT)
		for (RegraProducao rp : entradaUsuario.getRegrasProducao()) {
			ArrayList<String> rpFirst = new ArrayList<>(); // Guarda os simbolos first.

			String LE = rp.getLE();

			findFirstFromSymbol(rp);

		}

		System.out.println(mapFirst);

		return mapFirst;
	}

	/**
	 * Busca as Regras de Produção e os corpos de derivações que contem o simbolodo
	 * LD.
	 * 
	 * @return Lista de Regras de produção com os seus corpos de derivação que
	 *         contem o símbolo no LD.
	 * @throws Exception
	 */
	private List<RegraProducao> findAllRPcontaing(String simb) throws Exception {

		List<RegraProducao> regrasProducao = entradaUsuario.getRegrasProducao();

		// Percorre todas as regras de produção.
		for (RegraProducao rp : regrasProducao) {

//			ArrayList<String> listComLD = new ArrayList<>(); // Guarda os corpos que contem NT.

			int rpSize = rp.getListLD().length; // Tamanho de cada LD.

			// Avalia todos os corpos do LD.
			for (int i = 0; i < rpSize; i++) {
				
				String corpo = rp.getListLD()[i];
				
				// Se o LD possui NT, então precisa avaliar.
				if(hasNT(corpo)) {
//					listComLD.add(corpo); // Adiciona na lista
					
					String str = corpo.substring(i, ++i);
					
					// Se o simbolo
					if(entradaUsuario.isNT(str)) {
//						System.out.println();
					}
					
				}
				
//				String first = simbolo.substring(0, 1); // Extrai o primeiro simbolo.
//				// Se o simbolo avaliado for terminal.
//				if (entradaUsuario.isTerminal(first) || entradaUsuario.isSentencaVazia(first)) {
//					rpFirst.add(first);
//					totTerminais++;
//				}
			}

		}
		return null;
//		throw new Exception("Nenhuma Regra de Produção localizada para o símbolo " + simb);
	}

	private List<String> findFirstFromSymbol(RegraProducao rp) {

		// System.out.println("findFirstFromSymbol receiving rp: " + rp);

		ArrayList<String> rpFirst = new ArrayList<>(); // Guarda os simbolos first.

		String LE = rp.getLE();

		// Se ainda não encontro o first do LE.
		if (mapFirst.get(LE) == null) {
			int rpSize = rp.getListLD().length; // Tamanho de cada LD.
			// Avalia cada corpo do LD.
			for (int i = 0; i < rpSize; i++) {
				String simbolo = rp.getListLD()[i];
				String first = simbolo.substring(0, 1); // Extrai o primeiro simbolo.
				// Se o simbolo avaliado for NT.
				if (entradaUsuario.isNT(first)) {

					try {
						// Busca a próxima RP que ele deriva.
						RegraProducao nextRP = findRP(first);
						// System.out.println("Buscando first: " + first);
						// while (entradaUsuario.isNT(nextRP.getLE())) {
						// nextRP = findRP(nextRP.getLE());
						List<String> newFirst = findFirstFromSymbol(nextRP);
						// System.out.println("newFirst: " + newFirst);
						rpFirst.addAll(newFirst);
						// }

					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					// Então é terminal e adiciona.
					rpFirst.add(first);
				}
			}

			// Adiciona na lista.
			mapFirst.put(rp.getLE(), rpFirst);

		} else {
			return mapFirst.get(LE);
		}

		return rpFirst;
	}

	public String conjuntoFollow() throws Exception {
		System.out.println("Método follow");

		// Percorre todas as regras de produção.
		for (RegraProducao rp : entradaUsuario.getRegrasProducao()) {

			// Busca a lista de RPs e os LD que tem o simbolo.
			List<RegraProducao> listLD = findAllRPcontaing(rp.getLE());

		}

		return null;
	}

	public String analisePreditivaTabular() {
		System.out.println("Tabela preditiva tabular");
		return null;
	}

	/**
	 * Reconhece a gramatica informada pelo usuário.
	 * 
	 * @param entradaUsuario
	 * @return O Tipo de gramática reconhecido.
	 */
	public TipoGramatica reconhecer(EntradaUsuario entradaUsuario) {

		this.entradaUsuario = entradaUsuario;

		this.tipoGramatica = identificarEntrada();

		return tipoGramatica;
	}

	/**
	 * Verifica todas as gramáticas possíveis para tentar identificar.
	 * 
	 * @return O TipoGramatica se reconhecido.
	 */
	private TipoGramatica identificarEntrada() {

		// Avaliação Bottom-Up na Hierarquia de Chomsky.
		if (matchUserEntry(entradaUsuario, TipoGramatica.GR)) {
			return TipoGramatica.GR;
		} else if (matchUserEntry(entradaUsuario, TipoGramatica.GLC)) {
			return TipoGramatica.GLC;
		} else if (matchUserEntry(entradaUsuario, TipoGramatica.GSC)) {
			return TipoGramatica.GSC;
		} else if (matchUserEntry(entradaUsuario, TipoGramatica.GI)) {
			return TipoGramatica.GI;
		}
		return null;
	}

	/**
	 * Verifica a gramática selecionada com a entrada do usuário.
	 * 
	 * @param entradaUsuario
	 * @param tipoGramatica
	 * @return true se conferir com o tipoGramatica
	 */
	private boolean matchUserEntry(EntradaUsuario entradaUsuario, TipoGramatica tipoGramatica) {

		// Indica se o LE possui ao menos um símbolo de início de produção.
		boolean temInicioProducao = false;

		String inicioProducao = entradaUsuario.getInicioProducao();
		String naoTerminais = entradaUsuario.getNaoTerminais();
		String terminais = entradaUsuario.getTerminais();

		tipoGramatica.setInicioProducao(inicioProducao);
		tipoGramatica.setNaoTerminais(naoTerminais);
		tipoGramatica.setTerminais(terminais);

		// Verifica todas as regras de produção informadas.
		for (RegraProducao rp : entradaUsuario.getRegrasProducao()) {
			// Valida LE.
			String[] listLE = rp.getListLE();

			for (String lEStr : listLE) {
				tipoGramatica.setLE(lEStr);

				if (!lEStr.matches(tipoGramatica.getLERegexPattern())) {
					// System.out.println(tipoGramatica.getDescricao() + " (LE): " + lEStr + " - "
					// + tipoGramatica.getLERegexPattern());
					return false;
				}

				// Se o LE tem símbolo de início de produção.
				if (lEStr.matches(inicioProducao)) {

					// Se tem mais de um inicio de produção, retorna false.
					if (temInicioProducao)
						return false;

					temInicioProducao = true;
				}

				// Valida LD.
				String[] listLD = rp.getListLD();

				for (String lDStr : listLD) {

					tipoGramatica.setLD(lDStr);

					if (!lDStr.matches(tipoGramatica.getLDRegexPattern())) {
						// System.out.println(tipoGramatica.getDescricao() + " (LD): " + lDStr + " - " +
						// tipoGramatica.getLDRegexPattern());
						return false;
					}

					// Se for GSC
					if (tipoGramatica == TipoGramatica.GSC) {

						// Verifica se LE <= LD
						if (lEStr.length() > lDStr.length()) {
							// System.out.println(tipoGramatica.getDescricao() + " (LD): " + lEStr + " > " +
							// lDStr);
							return false;
						}
					}
				}
			}
		}

		// Se LE não tem nenhum início de produção.
		if (!temInicioProducao) {
			return false;
		}

		return true;
	}

	/**
	 * Gerar uma sentenca baseado na gramática reconhecida.
	 * 
	 * @return Sentença gerada.
	 * @throws Exception
	 */
	public Sentenca gerarSentenca() throws Exception {

		// Pilha usada para organizar as sentenças.
		LinkedList<Sentenca> listSentencas = new LinkedList<>();

		// Limite máximo de iterações.
		int nMax = 10000;

		// Contador de iterações atual.
		int x = 1;

		// Próxima regra a ser pesquisada.
		String nextStrRP = "";

		RegraProducao rp;
		RegraProducao nextRP;

		// LD extraído.
		String extractedLD;

		String inicioProducao = entradaUsuario.getInicioProducao();

		Sentenca sentenca = null;

		// Log com os passos da derivação.
		String sentencaStr = "<strong>" + inicioProducao + "</strong>";

		// Começa procurando pela Regra de Produção inicial.
		nextStrRP = inicioProducao;

		// Extrai os símbolos de produção e adiciona na lista de
		// sentenças.
		do {

			// Busca próxima Regra de Produção
			nextRP = findRP(nextStrRP);

			// extrai um LD aleatório da RP sorteada.
			extractedLD = nextRP.randomLD();

			// Tratar para GR e GLC extrair somente o NT.
			if (tipoGramatica == TipoGramatica.GR || tipoGramatica == TipoGramatica.GLC) {

				// Se tem Não Terminal, extrai.
				if (hasNT(extractedLD)) {
					sentenca = extractRandomNT(extractedLD);

					// Se tem mais caracteres.
					// if (!sentenca.getSentenca().equals(sentenca.getSimboloDerivacao())) {

					// Adiciona o símbolo na lista.
					listSentencas.push(sentenca);

					// Seta a próxima RP a ser extraída.
					nextStrRP = sentenca.getSimboloDerivacao();

					// Log.
					sentencaStr += " -> <strong>" + sentenca.getSentenca() + "</strong> <small>("
							+ sentenca.getSimboloDerivacao() + "-" + (sentenca.getIndexSimboloDerivacao() + 1)
							+ ")</small>";

					// }

				} else {
					sentenca = new Sentenca(extractedLD);

					// Enquanto forem terminais, desempilha.
					while (!hasNT(sentenca.getSentenca()) && !listSentencas.isEmpty()) {

						// Desempilha o último.
						Sentenca sentencaAnterior = listSentencas.pop();

						String novaSentenca = sentencaAnterior.getSentenca();
						int indiceSubstituir = sentencaAnterior.getIndexSimboloDerivacao();

						// Remove o NT, colocando a sentença que derivou em seu lugar.
						novaSentenca = cutAndReplace(novaSentenca, sentenca.getSentenca(), indiceSubstituir,
								indiceSubstituir + 1);

						// Log.
						sentencaStr += " <- <del>" + sentencaAnterior.getSimboloDerivacao() + "</del> = <strong>"
								+ sentenca.getSentenca() + "</strong> <small>(" + novaSentenca + ")</small>";

						// Atualiza o elemento retirado da lista com a nova sentença.
						sentencaAnterior.setSentenca(novaSentenca);

						// Cria a nova sentença com os símbolos substituídos.
						sentenca = new Sentenca(novaSentenca);

					}

					// Se ainda restaram NT, adiciona na lista novamente, para ser extraído na
					// próxima iteração.
					if (hasNT(sentenca.getSentenca())) {
						Sentenca temp = extractRandomNT(sentenca.getSentenca());

						// Extrai um próximo LD aleatório.
						nextStrRP = temp.getSimboloDerivacao();
						// Adiciona a nova Sentenca a lista.
						listSentencas.push(temp);
					}
				}

			} else {
				// Demais gramáticas não precisa extrair NT.

				// Busca a RP correspondente.
				rp = findRP(extractedLD);

				// Adiciona o símbolo na lista.
				listSentencas.push(new Sentenca(extractedLD));
			}

			// Enquanto não atingiu o limite de iterações
			// e possui Não Terminais para extrair
		} while (x++ < nMax && !listSentencas.isEmpty());

		if (x > nMax) {
			throw new Exception("Não foi possível gerar a sentença (Atingiu o limite máximo de iterações).");
		}

		// listSentencas.push(sentenca);
		sentenca.setLog(sentencaStr);
		return sentenca;
	}

	// Transformação em GLC

	public Sentenca transformacaoGLC() throws Exception {
		StringBuilder builder = new StringBuilder();

		// Passo 1
		List<RegraProducao> newList = removerSimbolosInuteis();
		entradaUsuario.setRegrasProducao(newList);

		builder.append("Passo 1 - Eliminar símbolos inúteis.<br>");
		builder.append("P':{");
		for (RegraProducao rp : newList) {
			builder.append("<br>" + rp.getLE() + " -> " + rp.getLD());
		}
		builder.append("}");

		// Passo 2
		newList = removerSimbolosInalcansaveis();
		entradaUsuario.setRegrasProducao(newList);

		builder.append("<br><br>");
		builder.append("Passo 2 - Eliminar símbolos Inalcansáveis.<br>");
		builder.append("P':{");
		for (RegraProducao rp : newList) {
			builder.append("<br>" + rp.getLE() + " -> " + rp.getLD());
		}
		builder.append("}");

		return new Sentenca(builder.toString());
	}

	/**
	 * Eliminar símbolos inalcansáveis. (2) 2
	 * 
	 * @return
	 * @throws Exception
	 */
	private List<RegraProducao> removerSimbolosInalcansaveis() throws Exception {

		List<RegraProducao> regrasProducao = entradaUsuario.getRegrasProducao();
		List<RegraProducao> newList = new LinkedList<>();

		RegraProducao rp;

		// Lista de símbolos inalcansáveis.
		List<String> inalcansaveis = new LinkedList<>();

		// Lista de símbolos derivados no LD.
		List<String> derivacoes = new LinkedList<>();

		// Percorre todas as regras de produção.
		for (int i = 0; i < regrasProducao.size(); i++) {
			rp = regrasProducao.get(i);
			// Percorre todo o LD da produção.
			for (int j = 0; j < rp.getListLD().length; j++) {
				String strLD = rp.getListLD()[j];

				// Se tem T e NT ou somente NT
				if (hasNT(strLD)) {
					// Percorre cada letra da derivação
					for (int k = 0; k < strLD.length(); k++) {
						String simbLD = strLD.substring(k, k + 1);

						if (entradaUsuario.isNT(simbLD)) {

							// Busca a regra gerada.
							RegraProducao derivado = findRP(simbLD);

							// Se for um NT derivado e ainda não estiver na lista.
							if (entradaUsuario.isNT(simbLD) && !derivacoes.contains(simbLD)) {
								derivacoes.add(simbLD);
							}
						}
					}
				}
			}
		}

		System.out.println("Derivações:");
		System.out.println(derivacoes);

		// Percorre todas as regras de produção.
		for (int i = 0; i < derivacoes.size(); i++) {
			String str = derivacoes.get(i);
			rp = findRP(str);
			newList.add(rp);
		}

		return newList;
	}

	// @@ Eliminar simbolos inúteis.
	/**
	 * Eliminar símbolos inúteis. (1) 1
	 */
	private List<RegraProducao> removerSimbolosInuteis() throws Exception {

		List<RegraProducao> regrasProducao = entradaUsuario.getRegrasProducao();
		List<RegraProducao> newList = new LinkedList<>();

		RegraProducao rp;

		// Lista de símbolos ferteis.
		List<String> ferteis = new LinkedList<>();

		// Percorre todas as regras de produção.
		for (int i = 0; i < regrasProducao.size(); i++) {
			rp = regrasProducao.get(i);

			// Percorre todo o LD da produção.
			for (int j = 0; j < rp.getListLD().length; j++) {
				String strLD = rp.getListLD()[j];

				// Se tem T e NT ou somente NT
				if (hasNT(strLD)) {
					// Percorre cada letra da derivação
					for (int k = 0; k < strLD.length(); k++) {
						String simbLD = strLD.substring(k, k + 1);

						// Se é um NT e não é o próprio símbolo.
						if (hasNT(simbLD) && !rp.getLE().equals(simbLD)) {
							// Verifica se o NT é de alguma
							// produção.
							for (int k2 = 0; k2 < regrasProducao.size(); k2++) {
								RegraProducao rpAux = regrasProducao.get(k2);
								// System.out.println("simbLD: " + simbLD);
								// System.out.println("rpAux.getLE(): " + rpAux.getLE());
								// System.out.println("rp.getLE(): " + rp.getLE());
								// System.out.println();
								// Se for uma produção e não for a própria produção.
								if (rp.getLE().equals(simbLD)

								) {

									// if se LD é válido.
									// aí adiciona.
									// pra ver se é válido, acho que é só ver se
									// o LD TEM um NT diferente do próprio LE.

									// System.out.println(">" + rpAux.getLE() + "<");
									// System.out.println(">>>" + simbLD + "<<<");
									if (!ferteis.contains(simbLD)) {
										// System.out.println("*** add "+simbLD+"****");
										ferteis.add(simbLD);

									}
								}
							}
						}
					}
					// Tem terminal, então não é um símbolo estéril.
				} else {
					if (!ferteis.contains(rp.getLE())) {
						// System.out.println("### add else: rp.getLE()=" +rp.getLE() + "");
						ferteis.add(rp.getLE());
					}
				}
			}
		}

		// Percorre todas as regras, deixando somente as férteis.
		for (int i = 0; i < regrasProducao.size(); i++) {
			rp = regrasProducao.get(i);

			// Percorre os simbolos férteis
			for (int j = 0; j < ferteis.size(); j++) {
				String simbFer = ferteis.get(j);

				boolean achou = false;

				// Se for fértil, adiciona na nova lista.
				if (rp.getLE().equals(simbFer)) {
					newList.add(rp);
				}

				// Lista dos NT da sentença do LD.
				List<String> somenteNT = rp.getListLD_NT(ferteis);

				// Percorre todos LD e se não for fértil, remove do LD.
				for (int k = 0; k < rp.getListLD().length; k++) {
					String strLD = rp.getListLD()[k];
					if (strLD.contains(simbFer)) {
						achou = true;
					}
				}

				if (!achou) {
					System.out.println("Não achou >>> " + simbFer);
				}
			}

		}

		System.out.println("Férteis: " + ferteis);
		return newList;
	}

	/**
	 * Recebe a primeira String e recorta os caracteres não desejados nas posições
	 * informadas nos indices iniciais e finais. Após, adiciona a segunda String no
	 * local em que os caracteres dos indices foram removidos.
	 * 
	 * @param String
	 *            com o texto original.
	 * @param String
	 *            a ser injetada nas posições dos indices.
	 * @param int
	 *            com a posição inicial a ser recortada.
	 * @param int
	 *            com a posição final a ser recortada.
	 * @return Nova string ajustada.
	 */
	private String cutAndReplace(String str, String strSubs, int inicio, int fim) {
		String newStr = "";
		int pos = 0;
		// Recorta o inicio da String.
		for (int i = 0; i < inicio; i++) {
			newStr += String.valueOf(str.charAt(i));
		}
		// Injeta nova String no meio.
		for (int i = inicio; i < strSubs.length() + inicio; i++) {
			newStr += String.valueOf(strSubs.charAt(pos));
			pos++;
		}
		// Recorta o final.
		for (int i = fim; i < str.length(); i++) {
			newStr += String.valueOf(str.charAt(i));
		}
		return newStr;
	}

	/**
	 * Extrai um NT randômico.
	 * 
	 * @param extractedLD
	 * @return
	 */
	private Sentenca extractRandomNT(String extractedLD) {

		String naoTerminais = "";

		// Guarda as posições em que achou os NT.
		ArrayList<Integer> pos = new ArrayList<>();

		// Marca os NT existentes para serem sorteados.
		for (int i = 0; i < extractedLD.length(); i++) {
			String str = extractedLD.substring(i, i + 1);
			for (int j = 0; j < entradaUsuario.getListNT().length; j++) {
				String NT = entradaUsuario.getListNT()[j];
				if (str.contains(NT)) {
					naoTerminais += str;
					pos.add(i);
					continue;
				}
			}
		}

		// Qtd de NT q podem ser escolhidos.
		int tam = pos.size();
		// Escolhe algum.
		int x = new Random().nextInt(tam);
		// Pega o indice do escolhido.
		int index = pos.get(x);
		// Extrai não terminal.
		String sentencaStr = extractedLD.substring(index, index + 1);

		Sentenca sentenca = new Sentenca(extractedLD);
		sentenca.setIndexSimboloDerivacao(index);
		sentenca.setSimboloDerivacao(sentencaStr);

		return sentenca;
	}

	/**
	 * Verifica se a sentenca possui Não Terminais.
	 * 
	 * @param sentenca
	 * @return Retorna true se tiver Não Terminais.
	 */
	private boolean hasNT(String sentenca) {
		for (String str : entradaUsuario.getListNT()) {
			if (sentenca.contains(str))
				return true;
		}
		return false;
	}

	/**
	 * Busca a Regra de produção para o símbolo informado.
	 * 
	 * @return RegraProducao que contém o símbolo informado.
	 * @throws Exception
	 */
	private RegraProducao findRP(String simb) throws Exception {

		List<RegraProducao> regrasProducao = entradaUsuario.getRegrasProducao();

		// Percorre todas as regras de produção.
		for (RegraProducao rp : regrasProducao) {
			// Percorre as regras do LE.
			for (String lEStr : rp.getListLE()) {
				// Se encontrou o inicio de produção
				if (lEStr.matches(simb)) {
					return rp;
				}
			}
		}
		throw new Exception("Nenhuma Regra de Produção localizada para o símbolo " + simb);
	}

	public TipoGramatica getTipoGramatica() {
		return tipoGramatica;
	}

	public void setTipoGramatica(TipoGramatica tipoGramatica) {
		this.tipoGramatica = tipoGramatica;
	}

	public EntradaUsuario getEntradaUsuario() {
		return entradaUsuario;
	}

	public void setEntradaUsuario(EntradaUsuario entradaUsuario) {
		this.entradaUsuario = entradaUsuario;
	}

}
