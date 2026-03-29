package com.duoc.pet_adoption_system.web.forms;

import com.duoc.pet_adoption_system.patients.domain.entities.CareStatus;
import com.duoc.pet_adoption_system.web.validation.NotSqlLike;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class PatientForm {

	@NotBlank
	@Size(max = 120)
	@Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Solo letras, números y espacios")
	private String name = "";

	@NotBlank
	@Size(max = 120)
	@Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Solo letras, números y espacios")
	private String species = "";

	@NotNull
	private LocalDate intakeDate;

	@Size(max = 4000)
	@NotSqlLike
	private String treatmentNotes = "";

	@NotNull
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
