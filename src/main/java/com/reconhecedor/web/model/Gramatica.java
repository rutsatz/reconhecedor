package com.reconhecedor.web.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Gramatica {

	private TipoGramatica gramatica;

	/**
	 * Guarda as strings digitadas pelo usuário.
	 */
	@Autowired
	private EntradaUsuario entradaUsuario;

	public TipoGramatica reconhecer(EntradaUsuario entradaUsuario) {

		this.entradaUsuario = entradaUsuario;

		TipoGramatica tipoGramatica;

		tipoGramatica = identificarEntrada();

		return tipoGramatica;
	}

	/**
	 * Verifica todas as gramáticas possíveis para tentar identificar.
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
			String lE = rp.getLE();
			String lD = rp.getLD();

			// formatar LE para ter somente a virgula. @@

			// Valida LE.
			String[] listLE = lE.split(",");

			for (String lEStr : listLE) {
				lEStr = lEStr.trim();
				tipoGramatica.setLE(lEStr);
				if (!lEStr.matches(tipoGramatica.getLERegexPattern())) {
					System.out.println(tipoGramatica.getDescricao() + " (LE): " + lEStr + " - " + tipoGramatica.getLERegexPattern());
					return false;
				}				
				
				// Se o LE tem símbolo de início de produção.
				if(lEStr.matches(inicioProducao)){
					temInicioProducao = true;
				}
				
				// Valida LD.
				String[] listLD = lD.split(",");

				for (String lDStr : listLD) {
					lDStr = lDStr.trim();
					tipoGramatica.setLD(lDStr);

					if (!lDStr.matches(tipoGramatica.getLDRegexPattern())) {
						System.out.println(tipoGramatica.getDescricao() + " (LD): " + lDStr + " - " + tipoGramatica.getLDRegexPattern());
						return false;
					}					
					
					// Se for GSC
					if (tipoGramatica == TipoGramatica.GSC) {

						// Verifica se LE <= LD
						if (lEStr.length() > lDStr.length()) {
							System.out.println(tipoGramatica.getDescricao() + " (LD): " + lEStr + " > " + lDStr);
							return false;
						}
					}
				}
			}
		}
		
		// Se LE não tem nenhum início de produção.
		if(!temInicioProducao){
			return false;
		}
		
		return true;
	}

	public TipoGramatica getGramatica() {
		return gramatica;
	}

	public void setGramatica(TipoGramatica gramatica) {
		this.gramatica = gramatica;
	}

	public EntradaUsuario getEntradaUsuario() {
		return entradaUsuario;
	}

	public void setEntradaUsuario(EntradaUsuario entradaUsuario) {
		this.entradaUsuario = entradaUsuario;
	}
}
