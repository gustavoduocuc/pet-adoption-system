package com.duoc.pet_adoption_system.patients.infrastructure.http;

import com.duoc.pet_adoption_system.patients.domain.entities.CareStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreatePatientRequest(
		@NotBlank @Size(max = 200) String name,
		@NotBlank @Size(max = 100) String species,
		@NotNull LocalDate intakeDate,
		@Size(max = 2000) String treatmentNotes,
		@NotNull CareStatus careStatus) {
}
