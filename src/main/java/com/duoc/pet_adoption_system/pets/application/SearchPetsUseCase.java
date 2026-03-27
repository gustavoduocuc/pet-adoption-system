package com.duoc.pet_adoption_system.pets.application;

import com.duoc.pet_adoption_system.pets.domain.entities.Pet;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.repositories.PetRepository;

import java.util.List;

public class SearchPetsUseCase {

	private final PetRepository petRepository;

	public SearchPetsUseCase(PetRepository petRepository) {
		this.petRepository = petRepository;
	}

	public List<Pet> execute(String species, Integer age, String location, PetGender gender) {
		return petRepository.search(species, age, location, gender);
	}
}
