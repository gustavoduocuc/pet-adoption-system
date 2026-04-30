package com.duoc.pet_adoption_system.pets.application;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.Pet;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.entities.PetSpecies;
import com.duoc.pet_adoption_system.pets.domain.repositories.InMemoryPetRepository;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListAvailablePetsUseCaseTest {

	@Test
	void executesAndReturnsAvailablePets() {
		InMemoryPetRepository repository = new InMemoryPetRepository();
		Pet available = Pet.create(
				Id.generate(),
				"Rex",
				PetSpecies.DOG,
				"mix",
				3,
				"Santiago",
				PetGender.MALE,
				AdoptionStatus.AVAILABLE);
		Pet adopted = Pet.create(
				Id.generate(),
				"Mimi",
				PetSpecies.CAT,
				"siames",
				2,
				"Valparaiso",
				PetGender.FEMALE,
				AdoptionStatus.ADOPTED);
		repository.save(available);
		repository.save(adopted);
		var useCase = new ListAvailablePetsUseCase(repository);

		List<Pet> result = useCase.execute();

		assertEquals(1, result.size());
		assertEquals("Rex", result.get(0).name());
	}
}
