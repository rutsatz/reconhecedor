package com.reconhecedor.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reconhecedor.web.model.Sentenca;
import com.reconhecedor.web.service.GramaticaService;

@RestController
@RequestMapping("/analise")
public class AnalisePreditivaTabularController {

	@Autowired
	private GramaticaService gramaticaService;
	
	@PutMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> analisePreditivaTabular() {
		try {
			gramaticaService.conjuntoFirst();
			gramaticaService.conjuntoFollow();
			String sentenca = gramaticaService.analisePreditivaTabular();
			
			return new ResponseEntity<>( sentenca, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
