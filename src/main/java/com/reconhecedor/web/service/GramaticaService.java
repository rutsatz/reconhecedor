package com.reconhecedor.web.service;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reconhecedor.web.model.EntradaUsuario;
import com.reconhecedor.web.model.Gramatica;
import com.reconhecedor.web.model.Sentenca;
import com.reconhecedor.web.model.TipoGramatica;

/**
 * Serviços oferecidos para a gramática.
 * 
 * @author Rafael
 *
 */
@Service
public class GramaticaService {

	@Autowired
	private Gramatica gramatica;

	/**
	 * Serviço de reconhecer uma gramática.
	 * 
	 * @param entradaUsuario
	 * @return Tipo da gramática reconhecida.
	 */
	public TipoGramatica reconhecer(EntradaUsuario entradaUsuario) {

		TipoGramatica tipoGramatica = gramatica.reconhecer(entradaUsuario);

		return tipoGramatica;
	}

	/**
	 * Serviço de gerar sentença.
	 * 
	 * @return A sentença gerada.
	 * @throws Exception 
	 */
	public Sentenca gerarSentenca() throws Exception {
		return gramatica.gerarSentenca();
	}

	public Gramatica getGramatica() {
		return gramatica;
	}

	public void setGramatica(Gramatica gramatica) {
		this.gramatica = gramatica;
	}

}
