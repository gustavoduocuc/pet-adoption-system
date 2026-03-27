package com.duoc.pet_adoption_system.identity.infrastructure.security;

import com.duoc.pet_adoption_system.shared.infrastructure.http.JsonAccessDeniedHandler;
import com.duoc.pet_adoption_system.shared.infrastructure.http.JsonAuthenticationEntryPoint;
import com.duoc.pet_adoption_system.shared.infrastructure.security.AppCorsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(AppCorsProperties.class)
public class ApiSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			JwtAuthenticationFilter jwtAuthenticationFilter,
			JsonAuthenticationEntryPoint authenticationEntryPoint,
			JsonAccessDeniedHandler accessDeniedHandler,
			AppCorsProperties corsProperties) throws Exception {
		http.csrf(csrf -> csrf.disable());
		http.anonymous(anonymous -> anonymous.disable());
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.cors(cors -> cors.configurationSource(corsConfigurationSource(corsProperties)));
		http.headers(headers -> headers
				.contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'none'; frame-ancestors 'none'"))
				.frameOptions(frame -> frame.deny())
				.referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
				.httpStrictTransportSecurity(hsts -> hsts.disable()));
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/pets", "/api/pets/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/pets").hasAnyRole("ADMIN", "STAFF")
				.requestMatchers(HttpMethod.PUT, "/api/pets/**").hasAnyRole("ADMIN", "STAFF")
				.requestMatchers(HttpMethod.DELETE, "/api/pets/**").hasAnyRole("ADMIN", "STAFF")
				.requestMatchers("/api/patients/**").hasAnyRole("ADMIN", "STAFF")
				.requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
				.anyRequest().permitAll());
		http.exceptionHandling(ex -> ex
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler));
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource(AppCorsProperties corsProperties) {
		CorsConfiguration configuration = new CorsConfiguration();
		if (!corsProperties.getAllowedOrigins().isEmpty()) {
			configuration.setAllowedOrigins(corsProperties.getAllowedOrigins());
		}
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		configuration.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/**", configuration);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService noopUserDetailsService() {
		return username -> {
			throw new UsernameNotFoundException("Authentication uses JWT only");
		};
	}
}
