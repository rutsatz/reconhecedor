package com.reconhecedor.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reconhecedor.web.model.EntradaUsuario;
import com.reconhecedor.web.model.Gramatica;
import com.reconhecedor.web.model.TipoGramatica;

@Service
public class GramaticaService {

	@Autowired
	private Gramatica gramatica;

	public TipoGramatica reconhecer(EntradaUsuario entradaUsuario) {

		TipoGramatica tipoGramatica = gramatica.reconhecer(entradaUsuario);

		return tipoGramatica;
	}

	public Gramatica getGramatica() {
		return gramatica;
	}

	public void setGramatica(Gramatica gramatica) {
		this.gramatica = gramatica;
	}

}
