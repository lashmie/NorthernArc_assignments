package org.northernarc.loan.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtFilter jwtFilter;
	private final CustomUserDetailsService customUserDetailsService;

	public SecurityConfig(JwtFilter jwtFilter, CustomUserDetailsService customUserDetailsService) {
		this.jwtFilter = jwtFilter;
		this.customUserDetailsService = customUserDetailsService;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login", "/api/auth/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/loans/approve").hasAnyRole("ADMIN", "MANAGER")
						.requestMatchers(HttpMethod.POST, "/api/loans/approve").hasAnyRole("ADMIN", "MANAGER")
						.requestMatchers(HttpMethod.PATCH, "/loans/interest-rate").hasAnyRole("ADMIN", "MANAGER")
						.requestMatchers(HttpMethod.PATCH, "/api/loans/interest-rate").hasAnyRole("ADMIN", "MANAGER")
						.requestMatchers(HttpMethod.PUT, "/api/loans/*/interest").hasAnyRole("ADMIN", "MANAGER")
						.requestMatchers(HttpMethod.DELETE, "/api/loans/*").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/loans/dashboard").hasAnyRole("ADMIN", "MANAGER")
						.requestMatchers(HttpMethod.GET, "/api/dashboard").hasAnyRole("ADMIN", "MANAGER")
						.requestMatchers(HttpMethod.GET, "/api/dashboard/admin").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/loans/emi/pay").hasAnyRole("ADMIN", "MANAGER", "USER")
						.requestMatchers(HttpMethod.POST, "/api/loans/emi/pay").hasAnyRole("ADMIN", "MANAGER", "USER")
						.requestMatchers("/api/customers/**").authenticated()
						.requestMatchers("/api/loans/**").hasAnyRole("ADMIN", "MANAGER", "USER")
						.requestMatchers("/loans/**").hasAnyRole("ADMIN", "MANAGER", "USER")
						.anyRequest().authenticated())
				.exceptionHandling(exception -> exception
						.authenticationEntryPoint((request, response, authException) -> {
							response.setStatus(HttpStatus.UNAUTHORIZED.value());
							response.setContentType(MediaType.APPLICATION_JSON_VALUE);
							new ObjectMapper().writeValue(response.getWriter(), Map.of("message", "Unauthorized"));
						})
						.accessDeniedHandler((request, response, accessDeniedException) -> {
							response.setStatus(HttpStatus.FORBIDDEN.value());
							response.setContentType(MediaType.APPLICATION_JSON_VALUE);
							new ObjectMapper().writeValue(response.getWriter(), Map.of("message", "Forbidden"));
						}))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
