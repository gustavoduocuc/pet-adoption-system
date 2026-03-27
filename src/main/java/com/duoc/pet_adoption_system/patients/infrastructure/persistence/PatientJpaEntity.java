package com.duoc.pet_adoption_system.patients.infrastructure.persistence;

import com.duoc.pet_adoption_system.patients.domain.entities.CareStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class PatientJpaEntity {

	@Id
	@Column(length = 36)
	private String id;

	@Column(nullable = false, length = 200)
	private String name;

	@Column(nullable = false, length = 100)
	private String species;

	@Column(name = "intake_date", nullable = false)
	private LocalDate intakeDate;

	@Column(name = "treatment_notes", length = 2000)
	private String treatmentNotes;

	@Enumerated(EnumType.STRING)
	@Column(name = "care_status", nullable = false, length = 30)
	private CareStatus careStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public LocalDate getIntakeDate() {
		return intakeDate;
	}

	public void setIntakeDate(LocalDate intakeDate) {
		this.intakeDate = intakeDate;
	}

	public String getTreatmentNotes() {
		return treatmentNotes;
	}

	public void setTreatmentNotes(String treatmentNotes) {
		this.treatmentNotes = treatmentNotes;
	}

	public CareStatus getCareStatus() {
		return careStatus;
	}

	public void setCareStatus(CareStatus careStatus) {
		this.careStatus = careStatus;
	}
}
