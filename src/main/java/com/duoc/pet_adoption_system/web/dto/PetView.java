package com.duoc.pet_adoption_system.web.dto;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.Pet;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.entities.PetSpecies;

public record PetView(
		String id,
		String name,
		PetSpecies species,
		String breed,
		int age,
		String location,
		PetGender gender,
		AdoptionStatus adoptionStatus) {

	public static PetView from(Pet pet) {
		return new PetView(
				pet.id().value(),
				pet.name(),
				pet.species(),
				pet.breed(),
				pet.age(),
				pet.location(),
				pet.gender(),
				pet.adoptionStatus());
	}
}
