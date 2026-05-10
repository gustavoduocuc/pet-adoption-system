package com.duoc.pet_adoption_system.identity.infrastructure.security;

import com.duoc.pet_adoption_system.shared.infrastructure.http.JsonAccessDeniedHandler;
import com.duoc.pet_adoption_system.shared.infrastructure.http.JsonAuthenticationEntryPoint;
import com.duoc.pet_adoption_system.shared.infrastructure.security.AppCorsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.HstsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
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
@EnableConfigurationProperties(AppCorsProperties.class)
public class ApiSecurityConfig {

	private static final String API_PETS_WILDCARD_PATH = "/api/pets/**";
	private static final String ADMIN_ROLE = "ADMIN";
	private static final String STAFF_ROLE = "STAFF";

	@Bean
	@Order(1)
	public SecurityFilterChain apiSecurityFilterChain(
			HttpSecurity http,
			JwtAuthenticationFilter jwtAuthenticationFilter,
			JsonAuthenticationEntryPoint authenticationEntryPoint,
			JsonAccessDeniedHandler accessDeniedHandler,
			AppCorsProperties corsProperties) {
		http.securityMatcher("/api/**");
		http.csrf(AbstractHttpConfigurer::disable);
		http.anonymous(anonymous -> anonymous.disable());
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.cors(cors -> cors.configurationSource(corsConfigurationSource(corsProperties)));
		http.headers(headers -> headers
				.contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'none'; frame-ancestors 'none'"))
				.frameOptions(frame -> frame.deny())
				.referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
				.httpStrictTransportSecurity(HstsConfig::disable));
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/pets", API_PETS_WILDCARD_PATH).permitAll()
				.requestMatchers(HttpMethod.POST, "/api/pets").hasAnyRole(ADMIN_ROLE, STAFF_ROLE)
				.requestMatchers(HttpMethod.PUT, API_PETS_WILDCARD_PATH).hasAnyRole(ADMIN_ROLE, STAFF_ROLE)
				.requestMatchers(HttpMethod.DELETE, API_PETS_WILDCARD_PATH).hasAnyRole(ADMIN_ROLE, STAFF_ROLE)
				.requestMatchers(HttpMethod.GET, "/api/appointments/my").authenticated()
				.requestMatchers(HttpMethod.POST, "/api/appointments/new/**").hasAnyRole(ADMIN_ROLE, STAFF_ROLE)
				.requestMatchers(HttpMethod.PUT, "/api/appointments/update/**").hasAnyRole(ADMIN_ROLE, STAFF_ROLE)
				.requestMatchers(HttpMethod.GET, "/api/appointments").hasAnyRole(ADMIN_ROLE, STAFF_ROLE)
				.requestMatchers(HttpMethod.GET, "/api/appointments/*").hasAnyRole(ADMIN_ROLE, STAFF_ROLE)
				.requestMatchers("/api/patients/**").hasAnyRole(ADMIN_ROLE, STAFF_ROLE)
				.anyRequest().denyAll());
		http.exceptionHandling(ex -> ex
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler));
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		try {
			return http.build();
		}
		catch (Exception cause) {
			throw new SecurityFilterChainConfigurationException("Failed to build API security filter chain", cause);
		}
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(JwtProperties jwtProperties) {
		return new JwtAuthenticationFilter(jwtProperties);
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
}
