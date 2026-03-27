package com.duoc.pet_adoption_system.patients.domain.repositories;

import com.duoc.pet_adoption_system.patients.domain.entities.Patient;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

import java.util.List;
import java.util.Optional;

public interface PatientRepository {

	List<Patient> findAll();

	Optional<Patient> findById(Id id);

	void save(Patient patient);
}
