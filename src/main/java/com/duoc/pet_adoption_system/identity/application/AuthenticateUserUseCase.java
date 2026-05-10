package com.duoc.pet_adoption_system.identity.application;

import com.duoc.pet_adoption_system.identity.application.ports.AccessTokenIssuer;
import com.duoc.pet_adoption_system.identity.application.ports.EncodedPasswordVerifier;
import com.duoc.pet_adoption_system.identity.domain.entities.User;
import com.duoc.pet_adoption_system.identity.domain.repositories.UserRepository;
import com.duoc.pet_adoption_system.shared.domain.DomainError;

public class AuthenticateUserUseCase {

	private static final String INVALID_USERNAME_OR_PASSWORD_MESSAGE = "Invalid username or password";

	private final UserRepository userRepository;
	private final EncodedPasswordVerifier encodedPasswordVerifier;
	private final AccessTokenIssuer accessTokenIssuer;

	public AuthenticateUserUseCase(
			UserRepository userRepository,
			EncodedPasswordVerifier encodedPasswordVerifier,
			AccessTokenIssuer accessTokenIssuer) {
		this.userRepository = userRepository;
		this.encodedPasswordVerifier = encodedPasswordVerifier;
		this.accessTokenIssuer = accessTokenIssuer;
	}

	public LoginResult execute(String username, String rawPassword) {
		if (rawPassword == null) {
			throw DomainError.other(INVALID_USERNAME_OR_PASSWORD_MESSAGE);
		}
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> DomainError.other(INVALID_USERNAME_OR_PASSWORD_MESSAGE));
		if (!encodedPasswordVerifier.matches(rawPassword, user.passwordHash())) {
			throw DomainError.other(INVALID_USERNAME_OR_PASSWORD_MESSAGE);
		}
		String token = accessTokenIssuer.issue(user.username(), user.roles());
		return new LoginResult(token, "Bearer");
	}
}
