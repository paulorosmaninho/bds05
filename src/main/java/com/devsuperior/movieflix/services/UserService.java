package com.devsuperior.movieflix.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.UserDTO;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthService authService;


	@Transactional(readOnly = true)
	public UserDTO getDataFromLoggedUser() {

		User user = authService.authenticated();

		user = userRepository.findByEmail(user.getEmail());

		return new UserDTO(user);

	}

	// Implementa método obrigatório para pesquisar se usuário existe através do
	// e-mail
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(username);

		if (user == null) {
			logger.error("Usuário não encontrado para o e-mail: " + username);
			throw new UsernameNotFoundException("Usuário não encontrado para o e-mail: " + username);
		}

		logger.info("Usuário encontrado para o e-mail: " + username);

		return user;
	}

}
