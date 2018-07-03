package com.reconhecedor.web.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrecedenciaDeOperadores {

	/**
	 * Guarda as strings digitadas pelo usuário.
	 */
	@Autowired
	private EntradaUsuario entradaUsuario;

	@Autowired
	private Gramatica gramatica;

	/**
	 * Lista de primeiros Terminais.
	 */
	HashMap<String, List<String>> mapPrimeirosTerminais = new HashMap<>();

	/**
	 * Lista de últimos Terminais.
	 */
	HashMap<String, List<String>> mapUltimosTerminais = new HashMap<>();

	public void buscarPrimeirosTerminais() {

		System.out.println("Método Primeiros Terminais");

		//
		for (RegraProducao rp : entradaUsuario.getRegrasProducao()) {

			String LE = rp.getLE(); // Guarda o NT que está sendo analisado.

			List<String> rpPrimeiros = expandirPrimeiros(rp);

			// Se todos os first forem terminais, adiciona na lista.
			// if (rpSize == totTerminais) {
			// mapFirst.put(rp.getLE(), rpFirst);
			// }

			mapPrimeirosTerminais.put(rp.getLE(), rpPrimeiros);
		}

		System.out.println(mapPrimeirosTerminais);

	}

	private List<String> expandirPrimeiros(RegraProducao rp) {

		int rpSize = rp.getListLD().length; // Tamanho de cada LD.
		// int totTerminais = 0; // Acumulador auxiliar
		ArrayList<String> rpPrimeiros = new ArrayList<>(); // Guarda os simbolos first.
		// Avalia cada corpo do LD.
		for (int i = 0; i < rpSize; i++) {
			String corpo = rp.getListLD()[i];
			// Se tiver mais simbolos, analiso.
			if (corpo.length() >= 2) {

				String NT = corpo.substring(0, 1); // Extrai o NT da esquerda.
				String operador = corpo.substring(1, 2); // Extrai o operador.
				// Se o operador avaliado for terminal.
				if (entradaUsuario.isTerminal(operador)) {
					// Verifica se é o NT que estamos analisando.
					if (NT.equals(rp.getLE())) {
						if (!rpPrimeiros.contains(operador)) {
							// Adiciona na lista de primeiros
							rpPrimeiros.add(operador);
						}
					}
					// totTerminais++;
				} else {

					// se o simbolo da esquerda for parenteses, não precisa expandir.
					if (NT.equals("(") || NT.equals(")")) {
						rpPrimeiros.add(NT);
					} else {

						// se for um NT, preciso expandir.

						// Busca a próxima RP que ele deriva.
						RegraProducao nextRP;
						try {
							// Nesse caso o operador é na verdade um NT, entao preciso expandir.
							String naoTerminal = operador;
							nextRP = gramatica.findRP(naoTerminal);

//							System.out.println("Buscando nextRP: " + nextRP);
							List<String> newFirst = expandirPrimeiros(nextRP);
							// System.out.println("newFirst: " + newFirst);
							rpPrimeiros.addAll(newFirst);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			} else {
				// Se for um NT isolado, precisamos expandir.
				if (entradaUsuario.isNT(corpo)) {
					// Busca a próxima RP que ele deriva.
					RegraProducao nextRP;

					// Nesse caso o corpo é na verdade um NT, entao preciso expandir.
					String naoTerminal = corpo;
					try {
						nextRP = gramatica.findRP(naoTerminal);

//						System.out.println("Buscando nextRP: " + nextRP);
						List<String> newFirst = expandirPrimeiros(nextRP);
						// System.out.println("newFirst: " + newFirst);
						rpPrimeiros.addAll(newFirst);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}
		return rpPrimeiros;
	}

	public void buscarUltimosTerminais() {

		System.out.println("Método Últimos Terminais");

	}
}
