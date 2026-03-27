package com.duoc.pet_adoption_system.patients.application;

import com.duoc.pet_adoption_system.patients.domain.entities.Patient;
import com.duoc.pet_adoption_system.patients.domain.repositories.PatientRepository;

import java.util.List;

public class ListPatientsUseCase {

	private final PatientRepository patientRepository;

	public ListPatientsUseCase(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	public List<Patient> execute() {
		return patientRepository.findAll();
	}
}
