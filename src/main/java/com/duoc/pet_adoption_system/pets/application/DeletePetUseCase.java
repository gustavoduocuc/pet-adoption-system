package com.duoc.pet_adoption_system.pets.application;

import com.duoc.pet_adoption_system.pets.domain.repositories.PetRepository;
import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

public class DeletePetUseCase {

	private final PetRepository petRepository;

	public DeletePetUseCase(PetRepository petRepository) {
		this.petRepository = petRepository;
	}

	public void execute(String id) {
		Id petId = Id.of(id);
		if (petRepository.findById(petId).isEmpty()) {
			throw DomainError.notFound("Pet " + id + " not found");
		}
		petRepository.deleteById(petId);
	}
}
