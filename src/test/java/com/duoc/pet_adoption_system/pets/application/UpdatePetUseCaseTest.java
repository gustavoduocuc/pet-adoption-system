package com.duoc.pet_adoption_system.pets.application;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.Pet;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.entities.PetSpecies;
import com.duoc.pet_adoption_system.pets.domain.repositories.InMemoryPetRepository;
import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UpdatePetUseCaseTest {

	@Test
	void updatesWhenPetExists() {
		InMemoryPetRepository repository = new InMemoryPetRepository();
		Pet pet = Pet.create(
				Id.generate(),
				"Rex",
				PetSpecies.DOG,
				"mix",
				3,
				"Santiago",
				PetGender.MALE,
				AdoptionStatus.AVAILABLE);
		repository.save(pet);
		var useCase = new UpdatePetUseCase(repository);

		Pet updated = useCase.execute(
				pet.id().value(),
				"Max",
				PetSpecies.CAT,
				"siames",
				5,
				"Valparaiso",
				PetGender.FEMALE,
				AdoptionStatus.ADOPTED);

		assertEquals("Max", updated.name());
		assertEquals(PetSpecies.CAT, updated.species());
		assertEquals(5, updated.age());
	}

	@Test
	void throwsNotFoundWhenPetMissing() {
		InMemoryPetRepository repository = new InMemoryPetRepository();
		var useCase = new UpdatePetUseCase(repository);
		String nonExistentId = Id.generate().value();

		assertThrows(
				DomainError.class,
				() -> useCase.execute(
						nonExistentId,
						"Max",
						PetSpecies.CAT,
						"siames",
						5,
						"Valparaiso",
						PetGender.FEMALE,
						AdoptionStatus.ADOPTED));
	}
}
