package com.duoc.pet_adoption_system.identity.application.ports;

public interface EncodedPasswordVerifier {

	boolean matches(String rawPassword, String encodedPassword);
}
