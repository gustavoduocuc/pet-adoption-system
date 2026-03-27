package com.duoc.pet_adoption_system.pets.application;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.Pet;
import com.duoc.pet_adoption_system.pets.domain.repositories.PetRepository;

import java.util.List;

public class ListAvailablePetsUseCase {

	private final PetRepository petRepository;

	public ListAvailablePetsUseCase(PetRepository petRepository) {
		this.petRepository = petRepository;
	}

	public List<Pet> execute() {
		return petRepository.findByAdoptionStatus(AdoptionStatus.AVAILABLE);
	}
}
