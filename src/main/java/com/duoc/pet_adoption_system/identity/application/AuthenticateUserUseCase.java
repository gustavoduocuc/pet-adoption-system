package com.duoc.pet_adoption_system.identity.application;

import com.duoc.pet_adoption_system.identity.application.ports.AccessTokenIssuer;
import com.duoc.pet_adoption_system.identity.application.ports.EncodedPasswordVerifier;
import com.duoc.pet_adoption_system.identity.domain.entities.User;
import com.duoc.pet_adoption_system.identity.domain.repositories.UserRepository;
import com.duoc.pet_adoption_system.shared.domain.DomainError;

public class AuthenticateUserUseCase {

	private static final String invalidUsernameOrPasswordMessage = "Invalid username or password";

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
			throw DomainError.other(invalidUsernameOrPasswordMessage);
		}
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> DomainError.other(invalidUsernameOrPasswordMessage));
		if (!encodedPasswordVerifier.matches(rawPassword, user.passwordHash())) {
			throw DomainError.other(invalidUsernameOrPasswordMessage);
		}
		String token = accessTokenIssuer.issue(user.username(), user.roles());
		return new LoginResult(token, "Bearer");
	}
}
