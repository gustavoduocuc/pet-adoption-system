package com.duoc.pet_adoption_system.patients.infrastructure.http;

import com.duoc.pet_adoption_system.patients.domain.entities.CareStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreatePatientRequest(
		@NotBlank
		@Size(max = 120)
		@Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Solo letras, números y espacios")
		String name,
		@NotBlank
		@Size(max = 120)
		@Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Solo letras, números y espacios")
		String species,
		@NotNull LocalDate intakeDate,
		@Size(max = 2000) String treatmentNotes,
		@NotNull CareStatus careStatus) {
}
