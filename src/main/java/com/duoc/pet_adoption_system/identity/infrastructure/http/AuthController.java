package com.duoc.pet_adoption_system.identity.infrastructure.http;

import com.duoc.pet_adoption_system.identity.application.AuthenticateUserUseCase;
import com.duoc.pet_adoption_system.identity.application.LoginResult;
import com.duoc.pet_adoption_system.shared.domain.DomainError;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthenticateUserUseCase authenticateUserUseCase;

	public AuthController(AuthenticateUserUseCase authenticateUserUseCase) {
		this.authenticateUserUseCase = authenticateUserUseCase;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
		try {
			LoginResult result = authenticateUserUseCase.execute(request.username(), request.password());
			return ResponseEntity.ok(new LoginResponse(result.accessToken(), result.tokenType()));
		}
		catch (DomainError error) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", "Unauthorized", "message", "Invalid username or password"));
		}
	}
}
