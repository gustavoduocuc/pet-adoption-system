package com.duoc.pet_adoption_system.pets.infrastructure.persistence;

import com.duoc.pet_adoption_system.pets.domain.entities.PetSpecies;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class PetLegacySpeciesDataMigration {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void normalizeLegacySpeciesValues() {
		for (PetSpecies species : PetSpecies.values()) {
			if (species == PetSpecies.OTHER) {
				continue;
			}
			String name = species.name();
			entityManager.createNativeQuery(
							"UPDATE pets SET species = :target WHERE UPPER(TRIM(species)) = :upper "
									+ "OR LOWER(TRIM(species)) = :lower")
					.setParameter("target", name)
					.setParameter("upper", name)
					.setParameter("lower", name.toLowerCase(Locale.ROOT))
					.executeUpdate();
		}
		entityManager.createNativeQuery(
						"UPDATE pets SET species = 'OTHER' WHERE species NOT IN "
								+ "('DOG','CAT','BIRD','RABBIT','REPTILE','OTHER')")
				.executeUpdate();
	}
}
