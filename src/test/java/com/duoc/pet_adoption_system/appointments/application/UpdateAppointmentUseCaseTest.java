package com.duoc.pet_adoption_system.appointments.application;

import com.duoc.pet_adoption_system.appointments.domain.entities.Appointment;
import com.duoc.pet_adoption_system.appointments.domain.entities.AppointmentStatus;
import com.duoc.pet_adoption_system.appointments.domain.repositories.InMemoryAppointmentRepository;
import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UpdateAppointmentUseCaseTest {

	@Test
	void updatesExistingAppointment() {
		Id appointmentId = Id.generate();
		Appointment existing = Appointment.create(
				appointmentId,
				Id.generate(),
				LocalDateTime.of(2026, 6, 1, 9, 0),
				"Old",
				AppointmentStatus.SCHEDULED,
				"staff");
		InMemoryAppointmentRepository appointments = new InMemoryAppointmentRepository();
		appointments.save(existing);
		var useCase = new UpdateAppointmentUseCase(appointments);
		LocalDateTime newWhen = LocalDateTime.of(2026, 6, 2, 14, 0);

		var updated = useCase.execute(appointmentId.value(), newWhen, "New notes", AppointmentStatus.COMPLETED);

		assertEquals(AppointmentStatus.COMPLETED, updated.status());
		assertEquals("New notes", updated.notes());
		assertEquals(newWhen, updated.scheduledAt());
	}

	@Test
	void rejectsWhenAppointmentMissing() {
		var useCase = new UpdateAppointmentUseCase(new InMemoryAppointmentRepository());

		assertThrows(
				DomainError.class,
				() -> useCase.execute(
						Id.generate().value(),
						LocalDateTime.now(),
						"",
						AppointmentStatus.CANCELLED));
	}
}
