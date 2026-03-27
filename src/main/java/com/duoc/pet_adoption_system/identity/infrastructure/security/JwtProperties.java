package com.duoc.pet_adoption_system.identity.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtProperties {

	private String secret = "dev-only-change-me-use-at-least-256-bits-for-hs256-please-change-in-prod";
	private long expirationMinutes = 60;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public long getExpirationMinutes() {
		return expirationMinutes;
	}

	public void setExpirationMinutes(long expirationMinutes) {
		this.expirationMinutes = expirationMinutes;
	}
}
