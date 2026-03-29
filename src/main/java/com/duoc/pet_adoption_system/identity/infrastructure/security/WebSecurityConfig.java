package com.duoc.pet_adoption_system.identity.infrastructure.security;

import com.duoc.pet_adoption_system.shared.infrastructure.security.AppActuatorHealthSecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(AppActuatorHealthSecurityProperties.class)
public class WebSecurityConfig {

	@Bean
	@Order(0)
	public SecurityFilterChain actuatorHealthSecurityFilterChain(
			HttpSecurity http,
			AppActuatorHealthSecurityProperties actuatorHealthSecurityProperties) throws Exception {
		http.securityMatcher("/actuator/**");
		http.csrf(AbstractHttpConfigurer::disable);
		http.formLogin(AbstractHttpConfigurer::disable);
		http.logout(AbstractHttpConfigurer::disable);
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/actuator/health", "/actuator/health/**")
				.access(actuatorHealthIpAccess(actuatorHealthSecurityProperties))
				.anyRequest().denyAll());
		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain webSecurityFilterChain(
			HttpSecurity http,
			RoleBasedAuthenticationSuccessHandler successHandler) throws Exception {
		http.securityMatcher(request -> !request.getRequestURI().startsWith("/api/")
				&& !request.getRequestURI().startsWith("/actuator/"));
		http.headers(headers -> headers
				.contentSecurityPolicy(csp -> csp.policyDirectives(
						"default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self' data:; form-action 'self'; frame-ancestors 'none'"))
				.frameOptions(frame -> frame.deny())
				.referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
				.httpStrictTransportSecurity(hsts -> hsts.disable()));
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/catalog", "/catalog/**").permitAll()
				.requestMatchers("/login", "/error", "/access-denied").permitAll()
				.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
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

	private static AuthorizationManager<RequestAuthorizationContext> actuatorHealthIpAccess(
			AppActuatorHealthSecurityProperties properties) {
		List<String> raw = properties.resolveAllowedIpAddresses();
		List<IpAddressMatcher> matchers = new ArrayList<>();
		if (raw != null) {
			for (String entry : raw) {
				if (entry == null || entry.isBlank()) {
					continue;
				}
				matchers.add(new IpAddressMatcher(entry.trim()));
			}
		}
		if (matchers.isEmpty()) {
			return (authentication, context) -> new AuthorizationDecision(false);
		}
		return (authentication, context) -> {
			for (IpAddressMatcher matcher : matchers) {
				if (matcher.matches(context.getRequest())) {
					return new AuthorizationDecision(true);
				}
			}
			return new AuthorizationDecision(false);
		};
	}
}
