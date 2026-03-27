package com.duoc.pet_adoption_system.pets.infrastructure.http;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePetRequest(
		@NotBlank String name,
		@NotBlank String species,
		String breed,
		@Min(0) int age,
		@NotBlank String location,
		@NotNull PetGender gender,
		@NotNull AdoptionStatus adoptionStatus) {
}
