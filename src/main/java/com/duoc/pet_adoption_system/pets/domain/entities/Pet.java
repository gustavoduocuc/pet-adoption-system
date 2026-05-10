package com.duoc.pet_adoption_system.pets.domain.entities;

import com.duoc.pet_adoption_system.pets.domain.valueobjects.PetAttributes;
import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

public final class Pet {

	private final Id id;
	private String name;
	private PetSpecies species;
	private String breed;
	private int age;
	private String location;
	private PetGender gender;
	private AdoptionStatus adoptionStatus;

	private Pet(Id id, PetAttributes attributes) {
		this.id = id;
		this.name = attributes.name();
		this.species = attributes.species();
		this.breed = attributes.breed();
		this.age = attributes.age();
		this.location = attributes.location();
		this.gender = attributes.gender();
		this.adoptionStatus = attributes.adoptionStatus();
	}

	public static Pet create(Id id, PetAttributes rawAttributes) {
		validateCore(rawAttributes);
		PetAttributes normalized = new PetAttributes(
				rawAttributes.name().trim(),
				rawAttributes.species(),
				rawAttributes.breed() == null ? "" : rawAttributes.breed().trim(),
				rawAttributes.age(),
				rawAttributes.location().trim(),
				rawAttributes.gender(),
				rawAttributes.adoptionStatus());
		return new Pet(id, normalized);
	}

	public static Pet restore(Id id, PetAttributes attributes) {
		return new Pet(id, attributes);
	}

	private static void validateCore(PetAttributes attributes) {
		if (attributes.name() == null || attributes.name().isBlank()) {
			throw DomainError.validation("Pet name must not be blank");
		}
		if (attributes.species() == null) {
			throw DomainError.validation("Species is required");
		}
		if (attributes.age() < 0) {
			throw DomainError.validation("Age must not be negative");
		}
		if (attributes.location() == null || attributes.location().isBlank()) {
			throw DomainError.validation("Location must not be blank");
		}
		if (attributes.gender() == null) {
			throw DomainError.validation("Gender is required");
		}
		if (attributes.adoptionStatus() == null) {
			throw DomainError.validation("Adoption status is required");
		}
	}

	public void updateDetails(PetAttributes rawAttributes) {
		validateCore(rawAttributes);
		this.name = rawAttributes.name().trim();
		this.species = rawAttributes.species();
		this.breed = rawAttributes.breed() == null ? "" : rawAttributes.breed().trim();
		this.age = rawAttributes.age();
		this.location = rawAttributes.location().trim();
		this.gender = rawAttributes.gender();
		this.adoptionStatus = rawAttributes.adoptionStatus();
	}

	public Id id() {
		return id;
	}

	public String name() {
		return name;
	}

	public PetSpecies species() {
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
