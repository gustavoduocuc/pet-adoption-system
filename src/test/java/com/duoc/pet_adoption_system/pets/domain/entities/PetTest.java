package com.duoc.pet_adoption_system.pets.domain.entities;

import com.duoc.pet_adoption_system.pets.domain.valueobjects.PetAttributes;
import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PetTest {

	@Test
	void createPersistsSpecies() {
		Pet pet = Pet.create(
				Id.generate(),
				new PetAttributes(
						"Luna",
						PetSpecies.DOG,
						"mix",
						2,
						"Santiago",
						PetGender.FEMALE,
						AdoptionStatus.AVAILABLE));

		assertEquals(PetSpecies.DOG, pet.species());
	}

	@Test
	void createRejectsNullSpecies() {
		Id id = Id.generate();
		PetAttributes attributes = new PetAttributes(
				"Luna",
				null,
				"mix",
				2,
				"Santiago",
				PetGender.FEMALE,
				AdoptionStatus.AVAILABLE);

		assertThrows(DomainError.class, () -> Pet.create(id, attributes));
	}

	@Test
	void updateDetailsChangesSpecies() {
		Id id = Id.generate();
		Pet pet = Pet.create(
				id,
				new PetAttributes(
						"Luna",
						PetSpecies.DOG,
						"mix",
						2,
						"Santiago",
						PetGender.FEMALE,
						AdoptionStatus.AVAILABLE));

		pet.updateDetails(new PetAttributes(
				"Luna",
				PetSpecies.CAT,
				"siames",
				3,
				"Valparaíso",
				PetGender.FEMALE,
				AdoptionStatus.ADOPTED));

		assertEquals(PetSpecies.CAT, pet.species());
		assertEquals(3, pet.age());
	}

	@Test
	void createRejectsBlankName() {
		Id id = Id.generate();
		PetAttributes attributes = new PetAttributes(
				"  ",
				PetSpecies.DOG,
				"mix",
				2,
				"Santiago",
				PetGender.FEMALE,
				AdoptionStatus.AVAILABLE);

		assertThrows(DomainError.class, () -> Pet.create(id, attributes));
	}

	@Test
	void createRejectsNegativeAge() {
		Id id = Id.generate();
		PetAttributes attributes = new PetAttributes(
				"Luna",
				PetSpecies.DOG,
				"mix",
				-1,
				"Santiago",
				PetGender.FEMALE,
				AdoptionStatus.AVAILABLE);

		assertThrows(DomainError.class, () -> Pet.create(id, attributes));
	}

	@Test
	void createRejectsBlankLocation() {
		Id id = Id.generate();
		PetAttributes attributes = new PetAttributes(
				"Luna",
				PetSpecies.DOG,
				"mix",
				2,
				"  ",
				PetGender.FEMALE,
				AdoptionStatus.AVAILABLE);

		assertThrows(DomainError.class, () -> Pet.create(id, attributes));
	}

	@Test
	void createRejectsNullGender() {
		Id id = Id.generate();
		PetAttributes attributes = new PetAttributes(
				"Luna",
				PetSpecies.DOG,
				"mix",
				2,
				"Santiago",
				null,
				AdoptionStatus.AVAILABLE);

		assertThrows(DomainError.class, () -> Pet.create(id, attributes));
	}

	@Test
	void createRejectsNullAdoptionStatus() {
		Id id = Id.generate();
		PetAttributes attributes = new PetAttributes(
				"Luna",
				PetSpecies.DOG,
				"mix",
				2,
				"Santiago",
				PetGender.FEMALE,
				null);

		assertThrows(DomainError.class, () -> Pet.create(id, attributes));
	}

	@Test
	void updateRejectsInvalidValues() {
		Pet pet = Pet.create(
				Id.generate(),
				new PetAttributes(
						"Luna",
						PetSpecies.DOG,
						"mix",
						2,
						"Santiago",
						PetGender.FEMALE,
						AdoptionStatus.AVAILABLE));

		PetAttributes invalidUpdate = new PetAttributes(
				"  ",
				PetSpecies.CAT,
				"siames",
				3,
				"Valparaiso",
				PetGender.FEMALE,
				AdoptionStatus.ADOPTED);

		assertThrows(DomainError.class, () -> pet.updateDetails(invalidUpdate));
	}
}
