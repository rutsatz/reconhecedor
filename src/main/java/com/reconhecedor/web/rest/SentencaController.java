package com.reconhecedor.web.rest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reconhecedor.web.model.Gramatica;
import com.reconhecedor.web.model.Sentenca;
import com.reconhecedor.web.service.GramaticaService;

@RestController
@RequestMapping("/sentenca")
public class SentencaController {

	@Autowired
	private GramaticaService gramaticaService;

	@PutMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Sentenca gerarSentenca(HttpServletResponse response) {

		// Gramatica gramatica = gramaticaService.getGramatica();

//		ArrayList<Sentenca> sentencas = new ArrayList<>();

//		sentencas.add(gramaticaService.gerarSentenca());

		try {
			Sentenca sentenca = gramaticaService.gerarSentenca();
			response.setStatus(HttpServletResponse.SC_OK);
			return sentenca;
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

}
