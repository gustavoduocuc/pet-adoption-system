package com.duoc.pet_adoption_system.identity.infrastructure.security;

import com.duoc.pet_adoption_system.identity.application.ports.EncodedPasswordVerifier;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BcryptEncodedPasswordVerifier implements EncodedPasswordVerifier {

	private final PasswordEncoder passwordEncoder;

	public BcryptEncodedPasswordVerifier(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public boolean matches(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
}
