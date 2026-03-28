package com.duoc.pet_adoption_system.web.dto;

import com.duoc.pet_adoption_system.patients.domain.entities.CareStatus;
import com.duoc.pet_adoption_system.patients.domain.entities.Patient;

import java.time.LocalDate;

public record PatientView(
		String id,
		String name,
		String species,
		LocalDate intakeDate,
		String treatmentNotes,
		CareStatus careStatus) {

	public static PatientView from(Patient patient) {
		return new PatientView(
				patient.id().value(),
				patient.name(),
				patient.species(),
				patient.intakeDate(),
				patient.treatmentNotes(),
				patient.careStatus());
	}
}
