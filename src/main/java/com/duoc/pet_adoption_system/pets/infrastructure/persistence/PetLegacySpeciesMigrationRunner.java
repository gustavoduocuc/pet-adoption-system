package com.duoc.pet_adoption_system.pets.infrastructure.persistence;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(5)
public class PetLegacySpeciesMigrationRunner implements ApplicationRunner {

	private final PetLegacySpeciesDataMigration migration;

	public PetLegacySpeciesMigrationRunner(PetLegacySpeciesDataMigration migration) {
		this.migration = migration;
	}

	@Override
	public void run(ApplicationArguments args) {
		migration.normalizeLegacySpeciesValues();
	}
}
