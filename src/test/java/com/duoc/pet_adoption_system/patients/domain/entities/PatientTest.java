package com.duoc.pet_adoption_system.patients.domain.entities;

import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PatientTest {

	@Test
	void createTrimsAndAcceptsAlphanumericNameAndSpecies() {
		Patient patient = Patient.create(
				Id.generate(),
				"  Rex 1 ",
				" dog ",
				LocalDate.of(2026, 1, 15),
				"",
				CareStatus.UNDER_CARE);

		assertEquals("Rex 1", patient.name());
		assertEquals("dog", patient.species());
	}

	@Test
	void createRejectsNameWithSpecialCharacters() {
		Id id = Id.generate();
		LocalDate intakeDate = LocalDate.of(2026, 1, 15);

		DomainError error = assertThrows(DomainError.class, () -> Patient.create(
				id,
				"'; DROP TABLE patients;--",
				"dog",
				intakeDate,
				"",
				CareStatus.UNDER_CARE));

		assertEquals(DomainError.Type.VALIDATION, error.getType());
	}

	@Test
	void createRejectsNameLongerThanAllowed() {
		String longName = "a".repeat(121);
		Id id = Id.generate();
		LocalDate intakeDate = LocalDate.of(2026, 1, 15);

		assertThrows(DomainError.class, () -> Patient.create(
				id,
				longName,
				"dog",
				intakeDate,
				"",
				CareStatus.UNDER_CARE));
	}
}
