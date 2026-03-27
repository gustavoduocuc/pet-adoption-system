package com.duoc.pet_adoption_system.identity.infrastructure.bootstrap;

import com.duoc.pet_adoption_system.identity.domain.entities.User;
import com.duoc.pet_adoption_system.identity.domain.entities.UserRole;
import com.duoc.pet_adoption_system.identity.domain.repositories.UserRepository;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class DevUsersBootstrap implements ApplicationRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public DevUsersBootstrap(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(ApplicationArguments args) {
		seedIfMissing("admin", "admin123", EnumSet.of(UserRole.ADMIN));
		seedIfMissing("staff", "staff123", EnumSet.of(UserRole.STAFF));
		seedIfMissing("user", "user123", EnumSet.of(UserRole.USER));
	}

	private void seedIfMissing(String username, String rawPassword, EnumSet<UserRole> roles) {
		if (userRepository.findByUsername(username).isPresent()) {
			return;
		}
		User user = User.create(
				Id.generate(),
				username,
				passwordEncoder.encode(rawPassword),
				roles);
		userRepository.save(user);
	}
}
