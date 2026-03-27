package com.duoc.pet_adoption_system.pets.infrastructure;

import com.duoc.pet_adoption_system.pets.application.CreatePetUseCase;
import com.duoc.pet_adoption_system.pets.application.DeletePetUseCase;
import com.duoc.pet_adoption_system.pets.application.GetPetByIdUseCase;
import com.duoc.pet_adoption_system.pets.application.ListAllPetsUseCase;
import com.duoc.pet_adoption_system.pets.application.ListAvailablePetsUseCase;
import com.duoc.pet_adoption_system.pets.application.SearchPetsUseCase;
import com.duoc.pet_adoption_system.pets.application.UpdatePetUseCase;
import com.duoc.pet_adoption_system.pets.domain.repositories.PetRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PetsBeansConfiguration {

	@Bean
	public CreatePetUseCase createPetUseCase(PetRepository petRepository) {
		return new CreatePetUseCase(petRepository);
	}

	@Bean
	public GetPetByIdUseCase getPetByIdUseCase(PetRepository petRepository) {
		return new GetPetByIdUseCase(petRepository);
	}

	@Bean
	public ListAllPetsUseCase listAllPetsUseCase(PetRepository petRepository) {
		return new ListAllPetsUseCase(petRepository);
	}

	@Bean
	public ListAvailablePetsUseCase listAvailablePetsUseCase(PetRepository petRepository) {
		return new ListAvailablePetsUseCase(petRepository);
	}

	@Bean
	public SearchPetsUseCase searchPetsUseCase(PetRepository petRepository) {
		return new SearchPetsUseCase(petRepository);
	}

	@Bean
	public UpdatePetUseCase updatePetUseCase(PetRepository petRepository) {
		return new UpdatePetUseCase(petRepository);
	}

	@Bean
	public DeletePetUseCase deletePetUseCase(PetRepository petRepository) {
		return new DeletePetUseCase(petRepository);
	}
}
