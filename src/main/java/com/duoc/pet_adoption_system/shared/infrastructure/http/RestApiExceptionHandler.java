package com.duoc.pet_adoption_system.shared.infrastructure.http;

import com.duoc.pet_adoption_system.shared.domain.DomainError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestApiExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(RestApiExceptionHandler.class);

	@ExceptionHandler(DomainError.class)
	public ResponseEntity<Map<String, Object>> handleDomainError(DomainError error) {
		HttpStatus status = switch (error.getType()) {
			case NOT_FOUND -> HttpStatus.NOT_FOUND;
			case VALIDATION -> HttpStatus.BAD_REQUEST;
			case OTHER -> HttpStatus.BAD_REQUEST;
		};
		return ResponseEntity.status(status)
				.body(Map.of("error", status.getReasonPhrase(), "message", error.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
				.collect(Collectors.joining("; "));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("error", "Bad Request", "message", message));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleUnexpected(Exception ex) {
		log.error("Unhandled exception", ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("error", "Internal Server Error", "message", "An unexpected error occurred"));
	}
}
