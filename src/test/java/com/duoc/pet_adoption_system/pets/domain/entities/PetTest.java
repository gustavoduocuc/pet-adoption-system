package com.duoc.pet_adoption_system.pets.domain.entities;

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
				"Luna",
				PetSpecies.DOG,
				"mix",
				2,
				"Santiago",
				PetGender.FEMALE,
				AdoptionStatus.AVAILABLE);

		assertEquals(PetSpecies.DOG, pet.species());
	}

	@Test
	void createRejectsNullSpecies() {
		assertThrows(DomainError.class, () -> Pet.create(
				Id.generate(),
				"Luna",
				null,
				"mix",
				2,
				"Santiago",
				PetGender.FEMALE,
				AdoptionStatus.AVAILABLE));
	}

	@Test
	void updateDetailsChangesSpecies() {
		Id id = Id.generate();
		Pet pet = Pet.create(
				id,
				"Luna",
				PetSpecies.DOG,
				"mix",
				2,
				"Santiago",
				PetGender.FEMALE,
				AdoptionStatus.AVAILABLE);

		pet.updateDetails(
				"Luna",
				PetSpecies.CAT,
				"siames",
				3,
				"Valparaíso",
				PetGender.FEMALE,
				AdoptionStatus.ADOPTED);

		assertEquals(PetSpecies.CAT, pet.species());
		assertEquals(3, pet.age());
	}
}
