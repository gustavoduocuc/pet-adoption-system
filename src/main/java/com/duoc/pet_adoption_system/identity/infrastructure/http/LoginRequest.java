package com.duoc.pet_adoption_system.identity.infrastructure.http;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
		@NotBlank String username,
		@NotBlank String password) {
}
