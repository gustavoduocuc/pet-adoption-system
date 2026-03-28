package com.duoc.pet_adoption_system.pets.infrastructure.persistence;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.Pet;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.entities.PetSpecies;
import com.duoc.pet_adoption_system.pets.domain.repositories.PetRepository;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PetRepositoryJpaAdapter implements PetRepository {

	private final SpringDataPetJpaRepository springData;

	public PetRepositoryJpaAdapter(SpringDataPetJpaRepository springData) {
		this.springData = springData;
	}

	@Override
	public List<Pet> findAll() {
		return springData.findAll(Sort.by(Sort.Order.asc("name"))).stream()
				.map(this::toDomain)
				.toList();
	}

	@Override
	public Optional<Pet> findById(Id id) {
		return springData.findById(id.value()).map(this::toDomain);
	}

	@Override
	public void save(Pet pet) {
		springData.save(toEntity(pet));
	}

	@Override
	public void deleteById(Id id) {
		springData.deleteById(id.value());
	}

	@Override
	public List<Pet> findByAdoptionStatus(AdoptionStatus status) {
		return springData.findByAdoptionStatusOrderByNameAsc(status).stream()
				.map(this::toDomain)
				.toList();
	}

	@Override
	public List<Pet> search(PetSpecies species, Integer age, String location, PetGender gender) {
		Specification<PetJpaEntity> spec = PetSpecifications.withOptionalFilters(species, age, location, gender);
		return springData.findAll(spec, Sort.by(Sort.Order.asc("name"))).stream()
				.map(this::toDomain)
				.toList();
	}

	private Pet toDomain(PetJpaEntity entity) {
		return Pet.restore(
				Id.of(entity.getId()),
				entity.getName(),
				entity.getSpecies(),
				entity.getBreed() == null ? "" : entity.getBreed(),
				entity.getAge(),
				entity.getLocation(),
				entity.getGender(),
				entity.getAdoptionStatus());
	}

	private PetJpaEntity toEntity(Pet pet) {
		PetJpaEntity entity = new PetJpaEntity();
		entity.setId(pet.id().value());
		entity.setName(pet.name());
		entity.setSpecies(pet.species());
		entity.setBreed(pet.breed());
		entity.setAge(pet.age());
		entity.setLocation(pet.location());
		entity.setGender(pet.gender());
		entity.setAdoptionStatus(pet.adoptionStatus());
		return entity;
	}
}
