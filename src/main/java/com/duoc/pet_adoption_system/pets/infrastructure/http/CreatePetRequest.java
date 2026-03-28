package com.duoc.pet_adoption_system.pets.infrastructure.http;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.entities.PetSpecies;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreatePetRequest(
		@NotBlank @Size(max = 200) String name,
		@NotNull PetSpecies species,
		@Size(max = 200) String breed,
		@Min(0) int age,
		@NotBlank @Size(max = 300) String location,
		@NotNull PetGender gender,
		@NotNull AdoptionStatus adoptionStatus) {
}
