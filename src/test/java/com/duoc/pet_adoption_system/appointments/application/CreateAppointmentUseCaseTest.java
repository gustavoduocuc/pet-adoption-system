package com.duoc.pet_adoption_system.appointments.application;

import com.duoc.pet_adoption_system.appointments.domain.entities.AppointmentStatus;
import com.duoc.pet_adoption_system.appointments.domain.repositories.InMemoryAppointmentRepository;
import com.duoc.pet_adoption_system.patients.domain.entities.CareStatus;
import com.duoc.pet_adoption_system.patients.domain.entities.Patient;
import com.duoc.pet_adoption_system.patients.domain.repositories.InMemoryPatientRepository;
import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateAppointmentUseCaseTest {

	@Test
	void persistsAppointmentWhenPatientExists() {
		Id patientId = Id.generate();
		InMemoryPatientRepository patients = new InMemoryPatientRepository();
		patients.save(Patient.create(
				patientId,
				"Rex",
				"dog",
				LocalDate.now(),
				"",
				CareStatus.UNDER_CARE));
		InMemoryAppointmentRepository appointments = new InMemoryAppointmentRepository();
		var useCase = new CreateAppointmentUseCase(appointments, patients);
		LocalDateTime when = LocalDateTime.of(2026, 5, 1, 11, 0);

		var created = useCase.execute(
				patientId.value(),
				when,
				"Vaccine",
				AppointmentStatus.SCHEDULED,
				"staff");

		assertEquals(patientId, created.patientId());
		assertEquals(when, created.scheduledAt());
		assertNotNull(appointments.findById(created.id()).orElse(null));
	}

	@Test
	void rejectsWhenPatientMissing() {
		var useCase = new CreateAppointmentUseCase(
				new InMemoryAppointmentRepository(),
				new InMemoryPatientRepository());

		assertThrows(
				DomainError.class,
				() -> useCase.execute(
						Id.generate().value(),
						LocalDateTime.now(),
						"",
						AppointmentStatus.SCHEDULED,
						"staff"));
	}
}
