package com.duoc.pet_adoption_system.pets.application;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.Pet;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.repositories.PetRepository;
import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

public class UpdatePetUseCase {

	private final PetRepository petRepository;

	public UpdatePetUseCase(PetRepository petRepository) {
		this.petRepository = petRepository;
	}

	public Pet execute(
			String id,
			String name,
			String species,
			String breed,
			int age,
			String location,
			PetGender gender,
			AdoptionStatus adoptionStatus) {
		Id petId = Id.of(id);
		Pet pet = petRepository.findById(petId)
				.orElseThrow(() -> DomainError.notFound("Pet " + id + " not found"));
		pet.updateDetails(name, species, breed, age, location, gender, adoptionStatus);
		petRepository.save(pet);
		return pet;
	}
}
