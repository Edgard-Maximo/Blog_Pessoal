package br.org.generation.blogpessoal.model;

import lombok.Data;

@Data
public class UserLogin {
	
	private long id;

	private String nome;

	private String usuario;

	private String senha;

	private String token;
	
	private String foto;
	
	private String tipo;

	


}
