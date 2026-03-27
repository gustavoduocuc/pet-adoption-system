package com.duoc.pet_adoption_system.pets.infrastructure.persistence;

import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import org.springframework.data.jpa.domain.Specification;

public final class PetSpecifications {

	private PetSpecifications() {
	}

	public static Specification<PetJpaEntity> withOptionalFilters(
			String species,
			Integer age,
			String location,
			PetGender gender) {
		Specification<PetJpaEntity> spec = (root, query, cb) -> cb.conjunction();
		if (species != null && !species.isBlank()) {
			String pattern = "%" + species.trim().toLowerCase() + "%";
			spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("species")), pattern));
		}
		if (age != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("age"), age));
		}
		if (location != null && !location.isBlank()) {
			String pattern = "%" + location.trim().toLowerCase() + "%";
			spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("location")), pattern));
		}
		if (gender != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("gender"), gender));
		}
		return spec;
	}
}
