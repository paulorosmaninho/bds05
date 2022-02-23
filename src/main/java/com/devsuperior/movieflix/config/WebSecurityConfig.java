package com.devsuperior.movieflix.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	// Refatorado em 11/02/2022

	// 11/02/2022 - Injetado Bean BCryptPasswordEncoder
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	// 11/02/2022 - Injetado UserDetailsService
	@Autowired
	private UserDetailsService userDetailsService;

	// @Override
	// public void configure(WebSecurity web) throws Exception {
	// web.ignoring().antMatchers("/**");
	// }

	// 11/02/2022 - Inclusão da biblioteca actuator utilizada pelo OAuth
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/actuator/**");
	}

	// Inclusão do método AuthenticationManagerBuilder - Source Override
	// Configurar objetos userDetails e criptografia.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		// Agora o Spring Security vai saber obter o usuário por e-mail e analisar a
		// senha criptografada.
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

	}

	@Override
	@Bean /*Incluída a anotação @Bean*/
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	

}