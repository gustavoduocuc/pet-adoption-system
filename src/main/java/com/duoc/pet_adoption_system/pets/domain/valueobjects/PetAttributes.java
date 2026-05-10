package com.duoc.pet_adoption_system.pets.domain.valueobjects;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.entities.PetSpecies;

public record PetAttributes(
		String name,
		PetSpecies species,
		String breed,
		int age,
		String location,
		PetGender gender,
		AdoptionStatus adoptionStatus) {
}
