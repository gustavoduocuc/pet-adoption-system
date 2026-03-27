package com.duoc.pet_adoption_system.identity.application;

import com.duoc.pet_adoption_system.identity.application.ports.AccessTokenIssuer;
import com.duoc.pet_adoption_system.identity.application.ports.EncodedPasswordVerifier;
import com.duoc.pet_adoption_system.identity.domain.entities.User;
import com.duoc.pet_adoption_system.identity.domain.entities.UserRole;
import com.duoc.pet_adoption_system.identity.domain.repositories.InMemoryUserRepository;
import com.duoc.pet_adoption_system.identity.domain.repositories.UserRepository;
import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.EnumSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthenticateUserUseCaseTest {

	@Test
	void issuesTokenWhenCredentialsMatch() {
		UserRepository users = new InMemoryUserRepository();
		var encoder = new BCryptPasswordEncoder(4);
		User user = User.create(
				Id.generate(),
				"alice",
				encoder.encode("secret"),
				EnumSet.of(UserRole.STAFF));
		users.save(user);
		EncodedPasswordVerifier verifier = new BcryptLikeVerifier(encoder);
		AccessTokenIssuer issuer = new FixedAccessTokenIssuer();
		var useCase = new AuthenticateUserUseCase(users, verifier, issuer);

		LoginResult result = useCase.execute("alice", "secret");

		assertEquals("fixed-token", result.accessToken());
		assertEquals("Bearer", result.tokenType());
	}

	@Test
	void rejectsWhenPasswordWrong() {
		UserRepository users = new InMemoryUserRepository();
		var encoder = new BCryptPasswordEncoder(4);
		users.save(User.create(
				Id.generate(),
				"bob",
				encoder.encode("right"),
				EnumSet.of(UserRole.USER)));
		var useCase = new AuthenticateUserUseCase(
				users,
				new BcryptLikeVerifier(encoder),
				new FixedAccessTokenIssuer());

		assertThrows(DomainError.class, () -> useCase.execute("bob", "wrong"));
	}

	private static final class BcryptLikeVerifier implements EncodedPasswordVerifier {

		private final BCryptPasswordEncoder encoder;

		private BcryptLikeVerifier(BCryptPasswordEncoder encoder) {
			this.encoder = encoder;
		}

		@Override
		public boolean matches(String rawPassword, String encodedPassword) {
			return encoder.matches(rawPassword, encodedPassword);
		}
	}

	private static final class FixedAccessTokenIssuer implements AccessTokenIssuer {

		@Override
		public String issue(String username, Set<UserRole> roles) {
			return "fixed-token";
		}
	}
}
