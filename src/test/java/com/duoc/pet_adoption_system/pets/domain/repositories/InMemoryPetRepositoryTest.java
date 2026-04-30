package com.duoc.pet_adoption_system.pets.domain.repositories;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.Pet;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.entities.PetSpecies;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryPetRepositoryTest {

	@Test
	void searchWithAllFilters() {
		InMemoryPetRepository repository = new InMemoryPetRepository();
		Pet dog1 = createPet("Rex", PetSpecies.DOG, 3, "Santiago", PetGender.MALE, AdoptionStatus.AVAILABLE);
		Pet dog2 = createPet("Luna", PetSpecies.DOG, 2, "Valparaiso", PetGender.FEMALE, AdoptionStatus.AVAILABLE);
		repository.save(dog1);
		repository.save(dog2);

		List<Pet> result = repository.search(PetSpecies.DOG, 3, "santiago", PetGender.MALE);

		assertEquals(1, result.size());
		assertEquals("Rex", result.get(0).name());
	}

	@Test
	void searchWithOnlySpecies() {
		InMemoryPetRepository repository = new InMemoryPetRepository();
		Pet dog = createPet("Rex", PetSpecies.DOG, 3, "Santiago", PetGender.MALE, AdoptionStatus.AVAILABLE);
		Pet cat = createPet("Mimi", PetSpecies.CAT, 2, "Santiago", PetGender.FEMALE, AdoptionStatus.AVAILABLE);
		repository.save(dog);
		repository.save(cat);

		List<Pet> result = repository.search(PetSpecies.DOG, null, null, null);

		assertEquals(1, result.size());
		assertEquals("Rex", result.get(0).name());
	}

	@Test
	void searchWithOnlyAge() {
		InMemoryPetRepository repository = new InMemoryPetRepository();
		Pet young = createPet("Puppy", PetSpecies.DOG, 1, "Santiago", PetGender.MALE, AdoptionStatus.AVAILABLE);
		Pet old = createPet("Senior", PetSpecies.DOG, 10, "Santiago", PetGender.FEMALE, AdoptionStatus.AVAILABLE);
		repository.save(young);
		repository.save(old);

		List<Pet> result = repository.search(null, 1, null, null);

		assertEquals(1, result.size());
		assertEquals("Puppy", result.get(0).name());
	}

	@Test
	void searchWithOnlyLocation() {
		InMemoryPetRepository repository = new InMemoryPetRepository();
		Pet santiago = createPet("Santi", PetSpecies.DOG, 3, "Santiago Centro", PetGender.MALE, AdoptionStatus.AVAILABLE);
		Pet valpo = createPet("Valpo", PetSpecies.CAT, 2, "Valparaiso", PetGender.FEMALE, AdoptionStatus.AVAILABLE);
		repository.save(santiago);
		repository.save(valpo);

		List<Pet> result = repository.search(null, null, "centro", null);

		assertEquals(1, result.size());
		assertEquals("Santi", result.get(0).name());
	}

	@Test
	void searchWithOnlyGender() {
		InMemoryPetRepository repository = new InMemoryPetRepository();
		Pet male = createPet("Max", PetSpecies.DOG, 3, "Santiago", PetGender.MALE, AdoptionStatus.AVAILABLE);
		Pet female = createPet("Bella", PetSpecies.DOG, 2, "Santiago", PetGender.FEMALE, AdoptionStatus.AVAILABLE);
		repository.save(male);
		repository.save(female);

		List<Pet> result = repository.search(null, null, null, PetGender.FEMALE);

		assertEquals(1, result.size());
		assertEquals("Bella", result.get(0).name());
	}

	@Test
	void searchWithNoFilters() {
		InMemoryPetRepository repository = new InMemoryPetRepository();
		Pet dog = createPet("Rex", PetSpecies.DOG, 3, "Santiago", PetGender.MALE, AdoptionStatus.AVAILABLE);
		Pet cat = createPet("Mimi", PetSpecies.CAT, 2, "Valparaiso", PetGender.FEMALE, AdoptionStatus.AVAILABLE);
		repository.save(dog);
		repository.save(cat);

		List<Pet> result = repository.search(null, null, null, null);

		assertEquals(2, result.size());
	}

	@Test
	void searchWithBlankLocationIgnored() {
		InMemoryPetRepository repository = new InMemoryPetRepository();
		Pet dog = createPet("Rex", PetSpecies.DOG, 3, "Santiago", PetGender.MALE, AdoptionStatus.AVAILABLE);
		repository.save(dog);

		List<Pet> result = repository.search(PetSpecies.DOG, null, "   ", null);

		assertEquals(1, result.size());
	}

	@Test
	void searchWithSpeciesAndAge() {
		InMemoryPetRepository repository = new InMemoryPetRepository();
		Pet dogYoung = createPet("Puppy", PetSpecies.DOG, 1, "Santiago", PetGender.MALE, AdoptionStatus.AVAILABLE);
		Pet dogOld = createPet("Senior", PetSpecies.DOG, 10, "Santiago", PetGender.FEMALE, AdoptionStatus.AVAILABLE);
		Pet catYoung = createPet("Kitty", PetSpecies.CAT, 1, "Santiago", PetGender.FEMALE, AdoptionStatus.AVAILABLE);
		repository.save(dogYoung);
		repository.save(dogOld);
		repository.save(catYoung);

		List<Pet> result = repository.search(PetSpecies.DOG, 1, null, null);

		assertEquals(1, result.size());
		assertEquals("Puppy", result.get(0).name());
	}

	@Test
	void findByAdoptionStatusReturnsMatchingPets() {
		InMemoryPetRepository repository = new InMemoryPetRepository();
		Pet available = createPet("Rex", PetSpecies.DOG, 3, "Santiago", PetGender.MALE, AdoptionStatus.AVAILABLE);
		Pet adopted = createPet("Mimi", PetSpecies.CAT, 2, "Santiago", PetGender.FEMALE, AdoptionStatus.ADOPTED);
		repository.save(available);
		repository.save(adopted);

		List<Pet> result = repository.findByAdoptionStatus(AdoptionStatus.AVAILABLE);

		assertEquals(1, result.size());
		assertEquals("Rex", result.get(0).name());
	}

	@Test
	void deleteByIdRemovesPet() {
		InMemoryPetRepository repository = new InMemoryPetRepository();
		Pet dog = createPet("Rex", PetSpecies.DOG, 3, "Santiago", PetGender.MALE, AdoptionStatus.AVAILABLE);
		repository.save(dog);

		repository.deleteById(dog.id());

		assertTrue(repository.findById(dog.id()).isEmpty());
	}

	private Pet createPet(String name, PetSpecies species, int age, String location, PetGender gender, AdoptionStatus status) {
		return Pet.create(Id.generate(), name, species, "mix", age, location, gender, status);
	}
}
