package com.reconhecedor.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reconhecedor.web.model.EntradaUsuario;
import com.reconhecedor.web.model.TipoGramatica;
import com.reconhecedor.web.service.GramaticaService;

@Controller
@RequestMapping("/reconhecer")
public class ReconhecedorController {

	/**
	 * View principal do sistema.
	 */
	private static final String RECONHECER = "Reconhecer";

	@Autowired
	private GramaticaService gramaticaService;

	/**
	 * Boas vindas.
	 * 
	 * @return ModelAndView
	 */
	@GetMapping
	public ModelAndView inicio() {

		ModelAndView mv = new ModelAndView(RECONHECER);
		mv.addObject(new EntradaUsuario());

		return mv;
	}

	/**
	 * Ação reconhecer.
	 * 
	 * @param entradaUsuario
	 * @return String
	 * @throws Exception
	 */
	@PostMapping
	public ModelAndView reconhecer(@Validated EntradaUsuario entradaUsuario, Errors errors) throws Exception {

		ModelAndView mv = new ModelAndView(RECONHECER);

		TipoGramatica tipoGramatica = null;

		if (errors.hasErrors()) {
			return mv;
		}
		try {
			tipoGramatica = gramaticaService.reconhecer(entradaUsuario);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erro ao reconhecer a gramática.");
		}

		String msg = "";

		if (tipoGramatica == null) {
			msg = "Gramática não reconhecida.";

		} else {
			msg = "Gramática reconhecida: " + tipoGramatica.getDescricao();
			mv.addObject("reconheceu", true);
		}

		mv.addObject("mensagem", msg);

		return mv;
	}
}