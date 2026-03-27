package com.duoc.pet_adoption_system.pets.application;

import com.duoc.pet_adoption_system.pets.domain.entities.Pet;
import com.duoc.pet_adoption_system.pets.domain.repositories.PetRepository;
import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

public class GetPetByIdUseCase {

	private final PetRepository petRepository;

	public GetPetByIdUseCase(PetRepository petRepository) {
		this.petRepository = petRepository;
	}

	public Pet execute(String id) {
		Id petId = Id.of(id);
		return petRepository.findById(petId)
				.orElseThrow(() -> DomainError.notFound("Pet " + id + " not found"));
	}
}
