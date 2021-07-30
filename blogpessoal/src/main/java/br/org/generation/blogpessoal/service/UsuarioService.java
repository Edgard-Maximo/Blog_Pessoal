package br.org.generation.blogpessoal.service;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.org.generation.blogpessoal.model.UserLogin;
import br.org.generation.blogpessoal.model.Usuario;
import br.org.generation.blogpessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	public Usuario cadastrarUsuario(Usuario usuario) {

		// Verifica se o usuário (email) existe
		if (repository.findByUsuario(usuario.getUsuario()).isPresent())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe, Cadastre outro e-mail.",
					null);

		// Verifica se o usuário é maior de idade
		int idade = Period.between(usuario.getDataNascimento(), LocalDate.now()).getYears();

		if (idade < 18)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Usuário menor de 18 anos. Infelizmente não será concluir seu cadastro.", null);

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		String senhaEncoder = encoder.encode(usuario.getSenha());
		usuario.setSenha(senhaEncoder);

		return repository.save(usuario);
	}

	public Optional<Usuario> atualizarUsuario(Usuario usuario) {

		if (repository.findById(usuario.getId()).isPresent()) {

			int idade = Period.between(usuario.getDataNascimento(), LocalDate.now()).getYears();

			if (idade < 18)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Você é menor de 18 anos. Infelizmente, não será possível atualizar o seu cadastro.", null);

			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

			String senhaEncoder = encoder.encode(usuario.getSenha());
			usuario.setSenha(senhaEncoder);

			return Optional.of(repository.save(usuario));

		} else {

			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!", null);

		}

	}

	public Optional<UserLogin> Logar(Optional<UserLogin> user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Optional<Usuario> usuario = repository.findByUsuario(user.get().getUsuario());

		if (usuario.isPresent()) {

			if (encoder.matches(user.get().getSenha(), usuario.get().getSenha())) {

				String auth = user.get().getUsuario() + ":" + user.get().getSenha();
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);

				user.get().setToken(authHeader);
				user.get().setId(usuario.get().getId());
				user.get().setNome(usuario.get().getNome());
				user.get().setSenha(usuario.get().getSenha());
				user.get().setFoto(usuario.get().getFoto());
				user.get().setTipo(usuario.get().getTipo());
				

				return user;

			}

		}

		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos!", null);
	}
}
