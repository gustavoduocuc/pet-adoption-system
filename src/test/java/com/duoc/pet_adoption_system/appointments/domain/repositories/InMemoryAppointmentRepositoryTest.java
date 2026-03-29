package com.duoc.pet_adoption_system.appointments.domain.repositories;

import com.duoc.pet_adoption_system.appointments.domain.entities.Appointment;
import com.duoc.pet_adoption_system.appointments.domain.entities.AppointmentStatus;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryAppointmentRepositoryTest {

	@Test
	void findsByPatientOrderedByScheduledAt() {
		Id patientId = Id.generate();
		Appointment earlier = Appointment.create(
				Id.generate(),
				patientId,
				LocalDateTime.of(2026, 4, 1, 9, 0),
				"",
				AppointmentStatus.SCHEDULED,
				"a");
		Appointment later = Appointment.create(
				Id.generate(),
				patientId,
				LocalDateTime.of(2026, 4, 2, 9, 0),
				"",
				AppointmentStatus.SCHEDULED,
				"b");
		InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();
		repository.save(later);
		repository.save(earlier);

		var found = repository.findByPatientIdOrderByScheduledAt(patientId);

		assertEquals(2, found.size());
		assertEquals(earlier.id(), found.get(0).id());
		assertEquals(later.id(), found.get(1).id());
	}

	@Test
	void findsByCreatorUsernameCaseInsensitive() {
		Appointment one = Appointment.create(
				Id.generate(),
				Id.generate(),
				LocalDateTime.now(),
				"",
				AppointmentStatus.SCHEDULED,
				"Staff");
		InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();
		repository.save(one);

		var found = repository.findByCreatedByUsernameOrderByScheduledAt("staff");

		assertEquals(1, found.size());
		assertTrue(found.get(0).createdByUsername().equalsIgnoreCase("staff"));
	}
}
