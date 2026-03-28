package com.duoc.pet_adoption_system.pets.domain.repositories;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.Pet;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.entities.PetSpecies;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

import java.util.List;
import java.util.Optional;

public interface PetRepository {

	List<Pet> findAll();

	Optional<Pet> findById(Id id);

	void save(Pet pet);

	void deleteById(Id id);

	List<Pet> findByAdoptionStatus(AdoptionStatus status);

	List<Pet> search(PetSpecies species, Integer age, String location, PetGender gender);
}
