package com.duoc.pet_adoption_system.identity.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	@Order(2)
	public SecurityFilterChain webSecurityFilterChain(
			HttpSecurity http,
			RoleBasedAuthenticationSuccessHandler successHandler) throws Exception {
		http.securityMatcher(request -> !request.getRequestURI().startsWith("/api/"));
		http.headers(headers -> headers
				.contentSecurityPolicy(csp -> csp.policyDirectives(
						"default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'; img-src 'self' data: https:; frame-ancestors 'none'"))
				.frameOptions(frame -> frame.deny())
				.referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
				.httpStrictTransportSecurity(hsts -> hsts.disable()));
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/catalog", "/catalog/**").permitAll()
				.requestMatchers("/login", "/error", "/access-denied").permitAll()
				.requestMatchers("/css/**", "/images/**").permitAll()
				.requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
				.requestMatchers("/app/**").hasAnyRole("ADMIN", "STAFF")
				.anyRequest().authenticated());
		http.formLogin(form -> form
				.loginPage("/login")
				.permitAll()
				.successHandler(successHandler));
		http.logout(logout -> logout
				.logoutSuccessUrl("/catalog?logout")
				.permitAll());
		http.exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"));
		return http.build();
	}
}
