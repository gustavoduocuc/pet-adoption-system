package com.duoc.pet_adoption_system.pets.infrastructure.persistence;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.entities.PetSpecies;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class PetSpecificationsTest {

	@Autowired
	private SpringDataPetJpaRepository repository;

	@Test
	void withAllFilters() {
		PetJpaEntity dog = createEntity("1", "Rex", PetSpecies.DOG, 3, "Santiago Centro", PetGender.MALE, AdoptionStatus.AVAILABLE);
		PetJpaEntity cat = createEntity("2", "Mimi", PetSpecies.CAT, 2, "Valparaiso", PetGender.FEMALE, AdoptionStatus.AVAILABLE);
		repository.save(dog);
		repository.save(cat);

		Specification<PetJpaEntity> spec = PetSpecifications.withOptionalFilters(PetSpecies.DOG, 3, "centro", PetGender.MALE);
		List<PetJpaEntity> result = repository.findAll(spec);

		assertEquals(1, result.size());
		assertEquals("Rex", result.get(0).getName());
	}

	@Test
	void withOnlySpecies() {
		PetJpaEntity dog = createEntity("1", "Rex", PetSpecies.DOG, 3, "Santiago", PetGender.MALE, AdoptionStatus.AVAILABLE);
		PetJpaEntity cat = createEntity("2", "Mimi", PetSpecies.CAT, 2, "Santiago", PetGender.FEMALE, AdoptionStatus.AVAILABLE);
		repository.save(dog);
		repository.save(cat);

		Specification<PetJpaEntity> spec = PetSpecifications.withOptionalFilters(PetSpecies.DOG, null, null, null);
		List<PetJpaEntity> result = repository.findAll(spec);

		assertEquals(1, result.size());
		assertEquals("Rex", result.get(0).getName());
	}

	@Test
	void withOnlyAge() {
		PetJpaEntity young = createEntity("1", "Puppy", PetSpecies.DOG, 1, "Santiago", PetGender.MALE, AdoptionStatus.AVAILABLE);
		PetJpaEntity old = createEntity("2", "Senior", PetSpecies.DOG, 10, "Santiago", PetGender.FEMALE, AdoptionStatus.AVAILABLE);
		repository.save(young);
		repository.save(old);

		Specification<PetJpaEntity> spec = PetSpecifications.withOptionalFilters(null, 1, null, null);
		List<PetJpaEntity> result = repository.findAll(spec);

		assertEquals(1, result.size());
		assertEquals("Puppy", result.get(0).getName());
	}

	@Test
	void withOnlyLocation() {
		PetJpaEntity santiago = createEntity("1", "Santi", PetSpecies.DOG, 3, "Santiago Centro", PetGender.MALE, AdoptionStatus.AVAILABLE);
		PetJpaEntity valpo = createEntity("2", "Valpo", PetSpecies.CAT, 2, "Valparaiso", PetGender.FEMALE, AdoptionStatus.AVAILABLE);
		repository.save(santiago);
		repository.save(valpo);

		Specification<PetJpaEntity> spec = PetSpecifications.withOptionalFilters(null, null, "centro", null);
		List<PetJpaEntity> result = repository.findAll(spec);

		assertEquals(1, result.size());
		assertEquals("Santi", result.get(0).getName());
	}

	@Test
	void withOnlyGender() {
		PetJpaEntity male = createEntity("1", "Max", PetSpecies.DOG, 3, "Santiago", PetGender.MALE, AdoptionStatus.AVAILABLE);
		PetJpaEntity female = createEntity("2", "Bella", PetSpecies.DOG, 2, "Santiago", PetGender.FEMALE, AdoptionStatus.AVAILABLE);
		repository.save(male);
		repository.save(female);

		Specification<PetJpaEntity> spec = PetSpecifications.withOptionalFilters(null, null, null, PetGender.FEMALE);
		List<PetJpaEntity> result = repository.findAll(spec);

		assertEquals(1, result.size());
		assertEquals("Bella", result.get(0).getName());
	}

	@Test
	void withNoFilters() {
		PetJpaEntity dog = createEntity("1", "Rex", PetSpecies.DOG, 3, "Santiago", PetGender.MALE, AdoptionStatus.AVAILABLE);
		PetJpaEntity cat = createEntity("2", "Mimi", PetSpecies.CAT, 2, "Valparaiso", PetGender.FEMALE, AdoptionStatus.AVAILABLE);
		repository.save(dog);
		repository.save(cat);

		Specification<PetJpaEntity> spec = PetSpecifications.withOptionalFilters(null, null, null, null);
		List<PetJpaEntity> result = repository.findAll(spec);

		assertEquals(2, result.size());
	}

	@Test
	void withBlankLocationIgnored() {
		PetJpaEntity dog = createEntity("1", "Rex", PetSpecies.DOG, 3, "Santiago", PetGender.MALE, AdoptionStatus.AVAILABLE);
		repository.save(dog);

		Specification<PetJpaEntity> spec = PetSpecifications.withOptionalFilters(PetSpecies.DOG, null, "   ", null);
		List<PetJpaEntity> result = repository.findAll(spec);

		assertEquals(1, result.size());
	}

	private PetJpaEntity createEntity(String id, String name, PetSpecies species, int age, String location, PetGender gender, AdoptionStatus status) {
		PetJpaEntity entity = new PetJpaEntity();
		entity.setId(id);
		entity.setName(name);
		entity.setSpecies(species);
		entity.setBreed("mix");
		entity.setAge(age);
		entity.setLocation(location);
		entity.setGender(gender);
		entity.setAdoptionStatus(status);
		return entity;
	}
}
