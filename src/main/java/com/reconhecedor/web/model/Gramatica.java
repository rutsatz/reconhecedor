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

	/**
	 * Tipo de gramatica conforme a Hierarquia de Chomsky.
	 */
	private TipoGramatica tipoGramatica;

	/**
	 * Lista de First da gramatica.
	 */
	HashMap<String, List<String>> mapFirst = new HashMap<>();

	/**
	 * Lista de Follow da gramatica.
	 */
	HashMap<String, List<String>> mapFollow = new HashMap<>();

	/**
	 * Guarda as strings digitadas pelo usuário.
	 */
	@Autowired
	private EntradaUsuario entradaUsuario;

	private static int qtdIteracoes = 0;

	/**
	 * Calcula o conjunto First da gramatica.
	 * 
	 * @return Lista do conjunto First.
	 */
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
	 * Realiza a busca do follow do NT informado pelo parâmetro.
	 * 
	 * @return Lista de simbolos Follow.
	 * @throws Exception
	 */
	private List<String> buscaFollow(final String simb) throws Exception {
		// System.out.println("buscaFollow de " + simb);
		List<RegraProducao> regrasProducao = entradaUsuario.getRegrasProducao();

		// Verifica se o follow que estou procurando já não está na lista.
		if (mapFollow.get(simb) != null) {
			return mapFollow.get(simb);
		}

		// Guarda os simbolos follow temporariamente.
		ArrayList<String> followList = new ArrayList<>();

		// Percorre todas as regras de produção.
		for (RegraProducao rp : regrasProducao) {

			int rpSize = rp.getListLD().length; // Tamanho de cada LD.

			// Avalia todos os corpos do LD.
			for (int i = 0; i < rpSize; i++) {

				String corpo = rp.getListLD()[i];
				int tamCorpo = corpo.length();

				// Se o LD possui NT, então precisa avaliar.
				if (hasNT(corpo)) {

					// Procura todos os NT dentro daquele corpo.
					for (int x = 0; x < tamCorpo; x++) {
						// Extrai cada simbolo.
						String str = corpo.substring(x, x + 1);
						// System.out.println("str: " + str);
						// Se o simbolo for um NT
						if (entradaUsuario.isNT(str)) {

							// System.out.println("corpo: " + corpo);
							// System.out.println("NT: " + str);
							// Se não tem o follow na lista.
							if (mapFollow.get(str) == null) {

								// System.out.println("mapFollow.get(" + str + ") == null");

								// procura o follow

								// Se o terminal é o último elemento, então aplicamos
								// a regra aX, em que os follow de X são os follow do LE
								// da produção.
								if (x + 1 == tamCorpo) {
									System.out.println("Procurando follow de " + rp.getLE());
									this.qtdIteracoes++;

									if (this.qtdIteracoes == 500) {
										throw new Exception(
												"Recursão detectada para o símbolo " + str + " ou " + rp.getLE());
									}

									// Procura o follow dele.

									// Se estou procurando o follow dele mesmo,
									// no caso do follow eu ignoro.
									if (simb.equals(rp.getLE())) {
										continue;
									}

									List<String> newFollow = buscaFollow(rp.getLE());
									// System.out.println("newFollow de " + str + ": " + newFollow);
									// Adiciona na lista de follows
									// followList.addAll(newFollow);
									return newFollow;

								} else {
									// Não atingiu o tamanho, então ainda está no
									// meio da expressão, então avalia o caracter ao lado direito.
									String sideSimb = corpo.substring(x + 1, x + 2);
									// System.out.println("sideSimb: " + sideSimb);
									// Se o simb ao lado direito for terminal, aí é caixa.
									if (entradaUsuario.isTerminal(sideSimb)) {
										followList.add(sideSimb);
									} else if (entradaUsuario.isNT(sideSimb)) {
										// Se for NT, então aplicamos a regra
										// aXB, em que o Follow(X) é o First(B).
										List<String> listFirst = mapFirst.get(sideSimb);

										// System.out.println("mapFirst.get("+sideSimb+")"+mapFirst.get(sideSimb));

										// System.out.println("listFirst: " + listFirst);
										// System.out.println("followList: " + followList);
										// Como já temos o first calculado, basta buscar
										// o first na lista.
										// followList.addAll(listFirst);
										followList = (ArrayList<String>) juntaListas(listFirst, followList);
										// return followList;
										// System.out.println("followList: " + followList);
									}
								}
							} else {
								// Já encontrou o follow. (Já está na lista)
								// rpFirst.add(first);
							}

						}
					}
				}

				// String first = simbolo.substring(0, 1); // Extrai o primeiro simbolo.
				// // Se o simbolo avaliado for terminal.
				// if (entradaUsuario.isTerminal(first) ||
				// entradaUsuario.isSentencaVazia(first)) {
				// rpFirst.add(first);
				// totTerminais++;
				// }
			}
			// Se for o inicio de producao, adiciona o marcador.
			if (rp.getLE().equals(entradaUsuario.getInicioProducao())) {
				if (!followList.contains("$")) {
					followList.add("$");
				}
			}
		}
		// System.out.println("return " + followList);

		return followList;
		// throw new Exception("Nenhuma Regra de Produção localizada para o símbolo " +
		// simb);
	}

	private List<String> juntaListas(List<String> orig, List<String> dest) {
		ArrayList<String> novaLista = new ArrayList<>();

		orig.forEach((e) -> {
			if (!novaLista.contains(e)) {
				novaLista.add(e);
			}
		});

		dest.forEach((e) -> {
			if (!novaLista.contains(e)) {
				novaLista.add(e);
			}
		});
		return novaLista;
	}

	/**
	 * Busca a lista de First da RP informada no parâmetro.
	 * 
	 * @param Regra
	 *            de Producao desejada.
	 * @return Lista com os simbolos First da RP.
	 */
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

	/**
	 * Monta o conjunto follow da gramatica.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String conjuntoFollow() throws Exception {
		System.out.println("Método follow");

		// Percorre todas as regras de produção.
		for (RegraProducao rp : entradaUsuario.getRegrasProducao()) {

			this.qtdIteracoes = 0;

			// Busca a lista de follow para cada NT.
			List<String> listFollow = new ArrayList<>();

			if (rp.getLE().equals(entradaUsuario.getInicioProducao())) {

				listFollow.add("$");
			} else {
				listFollow = buscaFollow(rp.getLE());
			}
			// Adiciona a lista dos follows no map.
			mapFollow.put(rp.getLE(), listFollow);
		}

		System.out.println(mapFollow);

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
	RegraProducao findRP(String simb) throws Exception {

		List<RegraProducao> regrasProducao = entradaUsuario.getRegrasProducao();

		// Percorre todas as regras de produção.
		for (RegraProducao rp : regrasProducao) {
			// Percorre as regras do LE.
			for (String lEStr : rp.getListLE()) {
				// Se for parenteses, deve adicionar o caracter de escape para a
				// expressão regular funcionar corretamente.
				if (simb.equals("(") || simb.equals(")")) {
					// Se encontrou o inicio de produção
					if (lEStr.matches("\\" + simb)) {
						return rp;
					}
				} else {
					// Se encontrou o inicio de produção
					if (lEStr.matches(simb)) {
						return rp;
					}
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
