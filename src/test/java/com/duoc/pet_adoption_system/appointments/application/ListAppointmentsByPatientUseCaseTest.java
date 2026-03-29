package com.duoc.pet_adoption_system.appointments.application;

import com.duoc.pet_adoption_system.appointments.domain.entities.Appointment;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

class ListAppointmentsByPatientUseCaseTest {

	@Test
	void returnsAppointmentsForExistingPatient() {
		Id patientId = Id.generate();
		InMemoryPatientRepository patients = new InMemoryPatientRepository();
		patients.save(Patient.create(
				patientId,
				"Miau",
				"cat",
				LocalDate.now(),
				"",
				CareStatus.UNDER_CARE));
		InMemoryAppointmentRepository appointments = new InMemoryAppointmentRepository();
		appointments.save(Appointment.create(
				Id.generate(),
				patientId,
				LocalDateTime.now(),
				"",
				AppointmentStatus.SCHEDULED,
				"staff"));
		var useCase = new ListAppointmentsByPatientUseCase(appointments, patients);

		var list = useCase.execute(patientId.value());

		assertEquals(1, list.size());
	}

	@Test
	void rejectsWhenPatientMissing() {
		var useCase = new ListAppointmentsByPatientUseCase(
				new InMemoryAppointmentRepository(),
				new InMemoryPatientRepository());

		assertThrows(DomainError.class, () -> useCase.execute(Id.generate().value()));
	}
}
