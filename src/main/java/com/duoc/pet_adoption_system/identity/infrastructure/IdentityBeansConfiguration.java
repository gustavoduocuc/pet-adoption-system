package com.duoc.pet_adoption_system.identity.infrastructure;

import com.duoc.pet_adoption_system.identity.application.AuthenticateUserUseCase;
import com.duoc.pet_adoption_system.identity.application.ports.AccessTokenIssuer;
import com.duoc.pet_adoption_system.identity.application.ports.EncodedPasswordVerifier;
import com.duoc.pet_adoption_system.identity.domain.repositories.UserRepository;
import com.duoc.pet_adoption_system.identity.infrastructure.security.BcryptEncodedPasswordVerifier;
import com.duoc.pet_adoption_system.identity.infrastructure.security.JwtAccessTokenIssuer;
import com.duoc.pet_adoption_system.identity.infrastructure.security.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class IdentityBeansConfiguration {

	@Bean
	public EncodedPasswordVerifier encodedPasswordVerifier(PasswordEncoder passwordEncoder) {
		return new BcryptEncodedPasswordVerifier(passwordEncoder);
	}

	@Bean
	public AccessTokenIssuer accessTokenIssuer(JwtProperties jwtProperties) {
		return new JwtAccessTokenIssuer(jwtProperties);
	}

	@Bean
	public AuthenticateUserUseCase authenticateUserUseCase(
			UserRepository userRepository,
			EncodedPasswordVerifier encodedPasswordVerifier,
			AccessTokenIssuer accessTokenIssuer) {
		return new AuthenticateUserUseCase(userRepository, encodedPasswordVerifier, accessTokenIssuer);
	}
}
