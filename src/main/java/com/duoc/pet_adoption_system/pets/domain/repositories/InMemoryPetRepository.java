package com.duoc.pet_adoption_system.pets.domain.repositories;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.Pet;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.entities.PetSpecies;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class InMemoryPetRepository implements PetRepository {

	private final Map<String, Pet> pets = new HashMap<>();

	@Override
	public List<Pet> findAll() {
		return pets.values().stream()
				.sorted(Comparator.comparing(p -> p.name().toLowerCase(Locale.ROOT)))
				.toList();
	}

	@Override
	public Optional<Pet> findById(Id id) {
		return Optional.ofNullable(pets.get(id.value()));
	}

	@Override
	public void save(Pet pet) {
		pets.put(pet.id().value(), pet);
	}

	@Override
	public void deleteById(Id id) {
		pets.remove(id.value());
	}

	@Override
	public List<Pet> findByAdoptionStatus(AdoptionStatus status) {
		return pets.values().stream()
				.filter(p -> p.adoptionStatus() == status)
				.sorted(Comparator.comparing(p -> p.name().toLowerCase(Locale.ROOT)))
				.toList();
	}

	@Override
	public List<Pet> search(PetSpecies species, Integer age, String location, PetGender gender) {
		Stream<Pet> stream = pets.values().stream();
		if (species != null) {
			stream = stream.filter(p -> p.species() == species);
		}
		if (age != null) {
			stream = stream.filter(p -> p.age() == age);
		}
		if (location != null && !location.isBlank()) {
			String needle = location.trim().toLowerCase(Locale.ROOT);
			stream = stream.filter(p -> p.location().toLowerCase(Locale.ROOT).contains(needle));
		}
		if (gender != null) {
			stream = stream.filter(p -> p.gender() == gender);
		}
		return stream.sorted(Comparator.comparing(p -> p.name().toLowerCase(Locale.ROOT))).toList();
	}
}
