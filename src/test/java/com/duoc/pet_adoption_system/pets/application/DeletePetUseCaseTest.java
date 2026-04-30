package com.duoc.pet_adoption_system.pets.application;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.Pet;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.entities.PetSpecies;
import com.duoc.pet_adoption_system.pets.domain.repositories.InMemoryPetRepository;
import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeletePetUseCaseTest {

	@Test
	void deletesWhenPetExists() {
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
		var useCase = new DeletePetUseCase(repository);

		useCase.execute(pet.id().value());

		assertTrue(repository.findById(pet.id()).isEmpty());
	}

	@Test
	void throwsNotFoundWhenPetMissing() {
		InMemoryPetRepository repository = new InMemoryPetRepository();
		var useCase = new DeletePetUseCase(repository);
		String nonExistentId = Id.generate().value();

		assertThrows(
				DomainError.class,
				() -> useCase.execute(nonExistentId));
	}
}
