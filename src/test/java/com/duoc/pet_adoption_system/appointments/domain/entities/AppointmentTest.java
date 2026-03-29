package com.duoc.pet_adoption_system.appointments.domain.entities;

import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppointmentTest {

	@Test
	void createsAppointmentWithScheduledTimeAndCreator() {
		Id appointmentId = Id.generate();
		Id patientId = Id.generate();
		LocalDateTime when = LocalDateTime.of(2026, 4, 1, 10, 0);

		Appointment appointment = Appointment.create(
				appointmentId,
				patientId,
				when,
				"Check-up",
				AppointmentStatus.SCHEDULED,
				"staff");

		assertEquals(appointmentId, appointment.id());
		assertEquals(patientId, appointment.patientId());
		assertEquals(when, appointment.scheduledAt());
		assertEquals("Check-up", appointment.notes());
		assertEquals(AppointmentStatus.SCHEDULED, appointment.status());
		assertEquals("staff", appointment.createdByUsername());
	}

	@Test
	void rejectsBlankCreatedByUsername() {
		assertThrows(DomainError.class, () -> Appointment.create(
				Id.generate(),
				Id.generate(),
				LocalDateTime.now(),
				"",
				AppointmentStatus.SCHEDULED,
				"   "));
	}

	@Test
	void updateChangesScheduleNotesAndStatus() {
		Appointment appointment = Appointment.create(
				Id.generate(),
				Id.generate(),
				LocalDateTime.of(2026, 4, 1, 10, 0),
				"Old",
				AppointmentStatus.SCHEDULED,
				"admin");
		LocalDateTime newWhen = LocalDateTime.of(2026, 4, 2, 15, 30);

		appointment.update(newWhen, "Done", AppointmentStatus.COMPLETED);

		assertEquals(newWhen, appointment.scheduledAt());
		assertEquals("Done", appointment.notes());
		assertEquals(AppointmentStatus.COMPLETED, appointment.status());
	}
}
