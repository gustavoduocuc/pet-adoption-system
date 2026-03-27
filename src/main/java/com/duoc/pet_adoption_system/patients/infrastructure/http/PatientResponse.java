package com.duoc.pet_adoption_system.patients.infrastructure.http;

import com.duoc.pet_adoption_system.patients.domain.entities.CareStatus;
import com.duoc.pet_adoption_system.patients.domain.entities.Patient;

import java.time.LocalDate;

public record PatientResponse(
		String id,
		String name,
		String species,
		LocalDate intakeDate,
		String treatmentNotes,
		CareStatus careStatus) {

	public static PatientResponse from(Patient patient) {
		return new PatientResponse(
				patient.id().value(),
				patient.name(),
				patient.species(),
				patient.intakeDate(),
				patient.treatmentNotes(),
				patient.careStatus());
	}
}
