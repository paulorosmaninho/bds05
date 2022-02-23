package com.devsuperior.movieflix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.UserRepository;
import com.devsuperior.movieflix.services.exceptions.ForbiddenException;
import com.devsuperior.movieflix.services.exceptions.UnauthorizedException;

//Classe para obter o usuário logado

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Transactional(readOnly = true)
	public User authenticated() {

		String username = null;

		try {

			// SecurityContextHolder obtém os dados do usuário logado
			username = SecurityContextHolder.getContext().getAuthentication().getName();
			return userRepository.findByEmail(username);

		} catch (Exception e) {
			// Qualquer exceção será direcionada para UnauthorizedException

			throw new UnauthorizedException("Usuário: " + username + " inválido");

		}

	}

	// Valida se o usuário logado é ele mesmo (self) ou Admin
	// Se sim, permite continuar, se não emite erro.
	public void validateSelfOrAdmin(Long userId) {

		User user = authenticated();

		// Se não for o próprio usuário ou admin
		if (!user.getId().equals(userId) && !user.hasRole("ROLE_ADMIN")) {

			throw new ForbiddenException(
					"Acesso negado. Usuário autenticado está tentando acessar dados não permitidos para seu perfil");
		}

	}

}
