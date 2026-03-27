package com.duoc.pet_adoption_system.patients.domain.repositories;

import com.duoc.pet_adoption_system.patients.domain.entities.Patient;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class InMemoryPatientRepository implements PatientRepository {

	private final Map<String, Patient> patients = new HashMap<>();

	@Override
	public List<Patient> findAll() {
		return patients.values().stream()
				.sorted(Comparator.comparing(p -> p.name().toLowerCase(Locale.ROOT)))
				.toList();
	}

	@Override
	public Optional<Patient> findById(Id id) {
		return Optional.ofNullable(patients.get(id.value()));
	}

	@Override
	public void save(Patient patient) {
		patients.put(patient.id().value(), patient);
	}
}
