package com.reconhecedor.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

/**
 * Representa os dados digitados pelo usuário no Form.
 * 
 * @author Rafael
 *
 */
@Component
public class EntradaUsuario {

	@NotEmpty(message = "Símbolo não terminal deve ser informado!")
	private String naoTerminais;

	@NotEmpty(message = "Símbolo terminal deve ser informado!")
	private String terminais;

	@NotEmpty(message = "Símbolo de início de produção deve ser informado!")
	private String inicioProducao;

	@Valid
	@NotEmpty(message = "Regras de produção devem ser informadas!")
	private List<RegraProducao> regrasProducao;

	/**
	 * Construtor default para inicializar as regras de producao como LazyList.
	 */
	@SuppressWarnings("unchecked")
	public EntradaUsuario() {

		regrasProducao = LazyList.decorate(new ArrayList<RegraProducao>(),
				FactoryUtils.instantiateFactory(RegraProducao.class));
		
		// para testes.
		// GR
		 setNaoTerminais("A, B");
		 setTerminais("a,b");
		 setInicioProducao("S");
		 regrasProducao.add(new RegraProducao("S", "aA , bB , a , b"));
		 
		// GLC
		// setNaoTerminais("A, B");
		// setTerminais("a,b");
		// setInicioProducao("S");
		// regrasProducao.add(new RegraProducao("B", "b , B"));

		// GSC
		// setNaoTerminais("A, B");
		// setTerminais("a,b");
		// setInicioProducao("S");
		// regrasProducao.add(new RegraProducao("aB", "bb , BB"));

		// GI
		// setNaoTerminais("A, B");
		// setTerminais("a,b");
		// setInicioProducao("S");
		// regrasProducao.add(new RegraProducao("AA", "b , &"));

	}

	public String getNaoTerminais() {
		return naoTerminais;
	}

	public void setNaoTerminais(String naoTerminais) {
		this.naoTerminais = naoTerminais;
	}

	public String getTerminais() {
		return terminais;
	}

	public void setTerminais(String terminais) {
		this.terminais = terminais;
	}

	public String getInicioProducao() {
		return inicioProducao;
	}

	public void setInicioProducao(String inicioProducao) {
		this.inicioProducao = inicioProducao;
	}

	public List<RegraProducao> getRegrasProducao() {
		return regrasProducao;
	}

	public void setRegrasProducao(List<RegraProducao> regrasProducao) {
		this.regrasProducao = regrasProducao;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EntradaUsuario [naoTerminais=");
		builder.append(naoTerminais);
		builder.append(", terminais=");
		builder.append(terminais);
		builder.append(", inicioProducao=");
		builder.append(inicioProducao);
		builder.append(", regrasProducao=");
		builder.append(regrasProducao);
		builder.append("]");
		return builder.toString();
	}

}
