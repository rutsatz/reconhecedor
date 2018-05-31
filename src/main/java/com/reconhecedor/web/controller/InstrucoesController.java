package com.reconhecedor.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InstrucoesController {

	@GetMapping("/instrucoes")
	public String instrucoes() {
		return "Instrucoes";
	}
	
}
