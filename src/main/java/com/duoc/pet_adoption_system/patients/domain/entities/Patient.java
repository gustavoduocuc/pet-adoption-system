package com.duoc.pet_adoption_system.patients.domain.entities;

import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

import java.time.LocalDate;
import java.util.regex.Pattern;

public final class Patient {

	private static final int maxNameLength = 120;
	private static final int maxSpeciesLength = 120;
	private static final Pattern lettersNumbersAndSpacesOnly = Pattern.compile("^[A-Za-z0-9 ]+$");

	private final Id id;
	private String name;
	private String species;
	private LocalDate intakeDate;
	private String treatmentNotes;
	private CareStatus careStatus;

	private Patient(
			Id id,
			String name,
			String species,
			LocalDate intakeDate,
			String treatmentNotes,
			CareStatus careStatus) {
		this.id = id;
		this.name = name;
		this.species = species;
		this.intakeDate = intakeDate;
		this.treatmentNotes = treatmentNotes;
		this.careStatus = careStatus;
	}

	public static Patient create(
			Id id,
			String name,
			String species,
			LocalDate intakeDate,
			String treatmentNotes,
			CareStatus careStatus) {
		validate(name, species, intakeDate, careStatus);
		return new Patient(
				id,
				name.trim(),
				species.trim(),
				intakeDate,
				treatmentNotes == null ? "" : treatmentNotes.trim(),
				careStatus);
	}

	public static Patient restore(
			Id id,
			String name,
			String species,
			LocalDate intakeDate,
			String treatmentNotes,
			CareStatus careStatus) {
		return new Patient(id, name, species, intakeDate, treatmentNotes, careStatus);
	}

	private static void validate(String name, String species, LocalDate intakeDate, CareStatus careStatus) {
		String trimmedName = name == null ? "" : name.trim();
		String trimmedSpecies = species == null ? "" : species.trim();
		if (trimmedName.isEmpty()) {
			throw DomainError.validation("Patient name must not be blank");
		}
		if (trimmedName.length() > maxNameLength) {
			throw DomainError.validation("Patient name must be at most " + maxNameLength + " characters");
		}
		if (!lettersNumbersAndSpacesOnly.matcher(trimmedName).matches()) {
			throw DomainError.validation("Patient name must contain only letters, numbers and spaces");
		}
		if (trimmedSpecies.isEmpty()) {
			throw DomainError.validation("Species must not be blank");
		}
		if (trimmedSpecies.length() > maxSpeciesLength) {
			throw DomainError.validation("Species must be at most " + maxSpeciesLength + " characters");
		}
		if (!lettersNumbersAndSpacesOnly.matcher(trimmedSpecies).matches()) {
			throw DomainError.validation("Species must contain only letters, numbers and spaces");
		}
		if (intakeDate == null) {
			throw DomainError.validation("Intake date is required");
		}
		if (careStatus == null) {
			throw DomainError.validation("Care status is required");
		}
	}

	public void update(
			String name,
			String species,
			LocalDate intakeDate,
			String treatmentNotes,
			CareStatus careStatus) {
		validate(name, species, intakeDate, careStatus);
		this.name = name.trim();
		this.species = species.trim();
		this.intakeDate = intakeDate;
		this.treatmentNotes = treatmentNotes == null ? "" : treatmentNotes.trim();
		this.careStatus = careStatus;
	}

	public Id id() {
		return id;
	}

	public String name() {
		return name;
	}

	public String species() {
		return species;
	}

	public LocalDate intakeDate() {
		return intakeDate;
	}

	public String treatmentNotes() {
		return treatmentNotes;
	}

	public CareStatus careStatus() {
		return careStatus;
	}
}
