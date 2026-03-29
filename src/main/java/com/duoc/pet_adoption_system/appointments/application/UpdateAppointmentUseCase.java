package com.duoc.pet_adoption_system.appointments.application;

import com.duoc.pet_adoption_system.appointments.domain.entities.Appointment;
import com.duoc.pet_adoption_system.appointments.domain.entities.AppointmentStatus;
import com.duoc.pet_adoption_system.appointments.domain.repositories.AppointmentRepository;
import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

import java.time.LocalDateTime;

public class UpdateAppointmentUseCase {

	private final AppointmentRepository appointmentRepository;

	public UpdateAppointmentUseCase(AppointmentRepository appointmentRepository) {
		this.appointmentRepository = appointmentRepository;
	}

	public Appointment execute(String appointmentId, LocalDateTime scheduledAt, String notes, AppointmentStatus status) {
		Id aid = Id.of(appointmentId);
		Appointment appointment = appointmentRepository.findById(aid)
				.orElseThrow(() -> DomainError.notFound("Appointment " + appointmentId + " not found"));
		appointment.update(scheduledAt, notes, status);
		appointmentRepository.save(appointment);
		return appointment;
	}
}
