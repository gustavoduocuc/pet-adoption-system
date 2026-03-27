package com.duoc.pet_adoption_system.patients.application;

import com.duoc.pet_adoption_system.patients.domain.entities.CareStatus;
import com.duoc.pet_adoption_system.patients.domain.entities.Patient;
import com.duoc.pet_adoption_system.patients.domain.repositories.PatientRepository;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

import java.time.LocalDate;

public class CreatePatientUseCase {

	private final PatientRepository patientRepository;

	public CreatePatientUseCase(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	public Patient execute(
			String name,
			String species,
			LocalDate intakeDate,
			String treatmentNotes,
			CareStatus careStatus) {
		Patient patient = Patient.create(Id.generate(), name, species, intakeDate, treatmentNotes, careStatus);
		patientRepository.save(patient);
		return patient;
	}
}
