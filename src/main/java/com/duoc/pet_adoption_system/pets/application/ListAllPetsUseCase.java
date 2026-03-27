package com.duoc.pet_adoption_system.pets.application;

import com.duoc.pet_adoption_system.pets.domain.entities.Pet;
import com.duoc.pet_adoption_system.pets.domain.repositories.PetRepository;

import java.util.List;

public class ListAllPetsUseCase {

	private final PetRepository petRepository;

	public ListAllPetsUseCase(PetRepository petRepository) {
		this.petRepository = petRepository;
	}

	public List<Pet> execute() {
		return petRepository.findAll();
	}
}
