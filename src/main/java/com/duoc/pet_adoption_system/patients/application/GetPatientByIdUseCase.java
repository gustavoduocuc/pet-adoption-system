package com.duoc.pet_adoption_system.patients.application;

import com.duoc.pet_adoption_system.patients.domain.entities.Patient;
import com.duoc.pet_adoption_system.patients.domain.repositories.PatientRepository;
import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

public class GetPatientByIdUseCase {

	private final PatientRepository patientRepository;

	public GetPatientByIdUseCase(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	public Patient execute(String id) {
		Id patientId = Id.of(id);
		return patientRepository.findById(patientId)
				.orElseThrow(() -> DomainError.notFound("Patient " + id + " not found"));
	}
}
