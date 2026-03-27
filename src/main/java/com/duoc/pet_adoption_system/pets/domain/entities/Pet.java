package com.duoc.pet_adoption_system.pets.domain.entities;

import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

public final class Pet {

	private final Id id;
	private String name;
	private String species;
	private String breed;
	private int age;
	private String location;
	private PetGender gender;
	private AdoptionStatus adoptionStatus;

	private Pet(
			Id id,
			String name,
			String species,
			String breed,
			int age,
			String location,
			PetGender gender,
			AdoptionStatus adoptionStatus) {
		this.id = id;
		this.name = name;
		this.species = species;
		this.breed = breed;
		this.age = age;
		this.location = location;
		this.gender = gender;
		this.adoptionStatus = adoptionStatus;
	}

	public static Pet create(
			Id id,
			String name,
			String species,
			String breed,
			int age,
			String location,
			PetGender gender,
			AdoptionStatus adoptionStatus) {
		validateCore(name, species, age, location, gender, adoptionStatus);
		return new Pet(id, name.trim(), species.trim(), breed == null ? "" : breed.trim(), age, location.trim(), gender,
				adoptionStatus);
	}

	public static Pet restore(
			Id id,
			String name,
			String species,
			String breed,
			int age,
			String location,
			PetGender gender,
			AdoptionStatus adoptionStatus) {
		return new Pet(id, name, species, breed, age, location, gender, adoptionStatus);
	}

	private static void validateCore(
			String name,
			String species,
			int age,
			String location,
			PetGender gender,
			AdoptionStatus adoptionStatus) {
		if (name == null || name.isBlank()) {
			throw DomainError.validation("Pet name must not be blank");
		}
		if (species == null || species.isBlank()) {
			throw DomainError.validation("Species must not be blank");
		}
		if (age < 0) {
			throw DomainError.validation("Age must not be negative");
		}
		if (location == null || location.isBlank()) {
			throw DomainError.validation("Location must not be blank");
		}
		if (gender == null) {
			throw DomainError.validation("Gender is required");
		}
		if (adoptionStatus == null) {
			throw DomainError.validation("Adoption status is required");
		}
	}

	public void updateDetails(
			String name,
			String species,
			String breed,
			int age,
			String location,
			PetGender gender,
			AdoptionStatus adoptionStatus) {
		validateCore(name, species, age, location, gender, adoptionStatus);
		this.name = name.trim();
		this.species = species.trim();
		this.breed = breed == null ? "" : breed.trim();
		this.age = age;
		this.location = location.trim();
		this.gender = gender;
		this.adoptionStatus = adoptionStatus;
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

	public String breed() {
		return breed;
	}

	public int age() {
		return age;
	}

	public String location() {
		return location;
	}

	public PetGender gender() {
		return gender;
	}

	public AdoptionStatus adoptionStatus() {
		return adoptionStatus;
	}
}
