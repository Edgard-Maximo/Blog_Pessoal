package br.org.generation.blogpessoal.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table(name = "tb_usuario")
@Data
public class Usuario {
	
	public Usuario() {
			super();
	}


	public Usuario(long id, String nome, String usuario, String senha, LocalDate dataNascimento) {
		this.id = id;
		this.nome = nome;
		this.usuario = usuario;
		this.senha = senha;
		this.dataNascimento = dataNascimento;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull(message = "O atributo tipotrabalho é obrigatório!")
	@Size(min = 1, max = 250, message = "O atributo descrição deve conter no mínimo 1 carecter e no máximo 250")
	private String nome;

	@NotNull(message = "O atributo tipotrabalho é obrigatório!")
	@Size(min = 1, max = 250, message = "O atributo descrição deve conter no mínimo 1 carecter e no máximo 250")
	private String usuario;

	@NotNull(message = "O atributo tipotrabalho é obrigatório!")
	@Size(min = 1, max = 250, message = "O atributo descrição deve conter no mínimo 1 carecter e no máximo 250")
	private String senha;

	@Column(name = "dt_nascimento")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataNascimento; // Atributo adicional

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties("usuario")
	private List<Postagem> postagem;

	/**
	 * Para efetuar os testes, precisamos criar dois métodos construtores
	 * 
	 * 1) Método construtor com todos os atributos, exceto o atributo postagem
	 * 
	 * 2) Método construtor vazio sem nenhum atributo
	 */

}
