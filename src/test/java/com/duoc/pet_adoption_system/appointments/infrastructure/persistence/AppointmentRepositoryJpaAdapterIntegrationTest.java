package com.duoc.pet_adoption_system.appointments.infrastructure.persistence;

import com.duoc.pet_adoption_system.appointments.domain.entities.Appointment;
import com.duoc.pet_adoption_system.appointments.domain.entities.AppointmentStatus;
import com.duoc.pet_adoption_system.appointments.domain.repositories.AppointmentRepository;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(AppointmentRepositoryJpaAdapter.class)
@ActiveProfiles("test")
class AppointmentRepositoryJpaAdapterIntegrationTest {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Test
	void savesAndFindsByPatientId() {
		Id patientId = Id.generate();
		Id appointmentId = Id.generate();
		Appointment appointment = Appointment.create(
				appointmentId,
				patientId,
				LocalDateTime.of(2026, 7, 1, 10, 0),
				"Follow-up",
				AppointmentStatus.SCHEDULED,
				"staff");

		appointmentRepository.save(appointment);

		var found = appointmentRepository.findByPatientIdOrderByScheduledAt(patientId);
		assertEquals(1, found.size());
		assertEquals(appointmentId, found.get(0).id());
		assertTrue(appointmentRepository.findById(appointmentId).isPresent());
	}
}
