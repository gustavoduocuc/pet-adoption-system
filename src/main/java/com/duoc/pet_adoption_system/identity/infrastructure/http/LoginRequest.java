package com.duoc.pet_adoption_system.identity.infrastructure.http;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
		@NotBlank @Size(max = 100) String username,
		@NotBlank @Size(max = 128) String password) {
}
