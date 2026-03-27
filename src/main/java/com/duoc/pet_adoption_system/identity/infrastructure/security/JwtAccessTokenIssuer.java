package com.duoc.pet_adoption_system.identity.infrastructure.security;

import com.duoc.pet_adoption_system.identity.application.ports.AccessTokenIssuer;
import com.duoc.pet_adoption_system.identity.domain.entities.UserRole;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtAccessTokenIssuer implements AccessTokenIssuer {

	private final JwtProperties properties;
	private final SecretKey secretKey;

	public JwtAccessTokenIssuer(JwtProperties properties) {
		this.properties = properties;
		this.secretKey = JwtSigningKey.fromConfiguredSecret(properties.getSecret());
	}

	@Override
	public String issue(String username, Set<UserRole> roles) {
		Instant now = Instant.now();
		Instant exp = now.plusSeconds(properties.getExpirationMinutes() * 60);
		var roleNames = roles.stream().map(Enum::name).collect(Collectors.toList());
		return Jwts.builder()
				.subject(username)
				.claim("roles", roleNames)
				.issuedAt(Date.from(now))
				.expiration(Date.from(exp))
				.signWith(secretKey)
				.compact();
	}
}
