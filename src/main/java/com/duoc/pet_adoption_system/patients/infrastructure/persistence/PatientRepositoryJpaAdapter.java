package com.duoc.pet_adoption_system.patients.infrastructure.persistence;

import com.duoc.pet_adoption_system.patients.domain.entities.Patient;
import com.duoc.pet_adoption_system.patients.domain.repositories.PatientRepository;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepositoryJpaAdapter implements PatientRepository {

	private final SpringDataPatientJpaRepository springData;

	public PatientRepositoryJpaAdapter(SpringDataPatientJpaRepository springData) {
		this.springData = springData;
	}

	@Override
	public List<Patient> findAll() {
		return springData.findAll(Sort.by(Sort.Order.asc("name"))).stream()
				.map(this::toDomain)
				.toList();
	}

	@Override
	public Optional<Patient> findById(Id id) {
		return springData.findById(id.value()).map(this::toDomain);
	}

	@Override
	public void save(Patient patient) {
		springData.save(toEntity(patient));
	}

	private Patient toDomain(PatientJpaEntity entity) {
		return Patient.restore(
				Id.of(entity.getId()),
				entity.getName(),
				entity.getSpecies(),
				entity.getIntakeDate(),
				entity.getTreatmentNotes() == null ? "" : entity.getTreatmentNotes(),
				entity.getCareStatus());
	}

	private PatientJpaEntity toEntity(Patient patient) {
		PatientJpaEntity entity = new PatientJpaEntity();
		entity.setId(patient.id().value());
		entity.setName(patient.name());
		entity.setSpecies(patient.species());
		entity.setIntakeDate(patient.intakeDate());
		entity.setTreatmentNotes(patient.treatmentNotes());
		entity.setCareStatus(patient.careStatus());
		return entity;
	}
}
