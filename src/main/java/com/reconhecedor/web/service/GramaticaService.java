package com.reconhecedor.web.service;

import java.util.HashMap;
import java.util.List;

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

	/**
	 * Serviço de transformação em GLC.
	 * 
	 * @return
	 * @throws Exception
	 */
	public Sentenca transformacaoGLC() throws Exception {
		return gramatica.transformacaoGLC();
	}

	/**
	 * Gera Conjunto First.
	 * 
	 * @return Os dados da tabela.
	 * @throws Exception
	 */
	public HashMap<String, List<String>> conjuntoFirst() throws Exception {
		return gramatica.conjuntoFirst();
	}

	/**
	 * Gera Conjunto Follow.
	 * 
	 * @return Os dados da tabela.
	 * @throws Exception
	 */
	public String conjuntoFollow() throws Exception {
		return gramatica.conjuntoFollow();
	}

	/**
	 * Serviço de Análise Preditiva Tabular.
	 * 
	 * @return Os dados da tabela.
	 * @throws Exception
	 */
	public String analisePreditivaTabular() throws Exception {
		return gramatica.analisePreditivaTabular();
	}

	public Gramatica getGramatica() {
		return gramatica;
	}

	public void setGramatica(Gramatica gramatica) {
		this.gramatica = gramatica;
	}
}