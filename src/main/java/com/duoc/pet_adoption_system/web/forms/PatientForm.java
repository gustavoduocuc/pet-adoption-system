package com.duoc.pet_adoption_system.web.forms;

import com.duoc.pet_adoption_system.patients.domain.entities.CareStatus;

import java.time.LocalDate;

public class PatientForm {

	private String name = "";
	private String species = "";
	private LocalDate intakeDate;
	private String treatmentNotes = "";
	private CareStatus careStatus;

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
