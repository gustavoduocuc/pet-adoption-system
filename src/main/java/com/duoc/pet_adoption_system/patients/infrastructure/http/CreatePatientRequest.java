package com.duoc.pet_adoption_system.patients.infrastructure.http;

import com.duoc.pet_adoption_system.patients.domain.entities.CareStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreatePatientRequest(
		@NotBlank String name,
		@NotBlank String species,
		@NotNull LocalDate intakeDate,
		String treatmentNotes,
		@NotNull CareStatus careStatus) {
}
