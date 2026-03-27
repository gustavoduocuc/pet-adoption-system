package com.duoc.pet_adoption_system.pets.application;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.Pet;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.repositories.PetRepository;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

public class CreatePetUseCase {

	private final PetRepository petRepository;

	public CreatePetUseCase(PetRepository petRepository) {
		this.petRepository = petRepository;
	}

	public Pet execute(
			String name,
			String species,
			String breed,
			int age,
			String location,
			PetGender gender,
			AdoptionStatus adoptionStatus) {
		Pet pet = Pet.create(Id.generate(), name, species, breed, age, location, gender, adoptionStatus);
		petRepository.save(pet);
		return pet;
	}
}
