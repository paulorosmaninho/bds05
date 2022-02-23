package com.devsuperior.movieflix.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	// Injetar o Bean JwtTokenStore
	@Autowired
	private JwtTokenStore jwtTokenStore;
	
	//Injetar a classe Environment para liberar o h2-console
	@Autowired
	private Environment env;
	
	private static final String[] PUBLIC = { "/oauth/token", "/h2-console/**" };

//	private static final String[] OPERATOR_OR_ADMIN = { "/products/**", "/categories/**" };
//
//	private static final String[] ADMIN = { "/users/**" };


	// Esse método é responsável por decodificar o token e analisar
	// se as informações da origem, secret, expiration, etc. são válidas.
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

		resources.tokenStore(jwtTokenStore);

	}

	// Esse método é responsável por configurar quais rotas terão acesso livre
	// e quais terão acesso restrito.
	// Navegação no catálogo de produtos é livre
	// CRUDS são restritos para operadores do sistema

	@Override
	public void configure(HttpSecurity http) throws Exception {

		// Obtém os profiles ativos para liberar o h2-console
		// Se contém "test" desabilita os frames para o h2-console executar
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.authorizeRequests()
			.antMatchers(PUBLIC).permitAll()
//			.antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).permitAll()
//			.antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")
//			.antMatchers(ADMIN).hasRole("ADMIN")
			.anyRequest().authenticated();

	}

}
