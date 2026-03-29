package com.duoc.pet_adoption_system.appointments.infrastructure.http;

import com.duoc.pet_adoption_system.appointments.domain.entities.Appointment;
import com.duoc.pet_adoption_system.appointments.domain.entities.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentResponse(
		String id,
		String patientId,
		LocalDateTime scheduledAt,
		String notes,
		AppointmentStatus status,
		String createdByUsername) {

	public static AppointmentResponse from(Appointment appointment) {
		return new AppointmentResponse(
				appointment.id().value(),
				appointment.patientId().value(),
				appointment.scheduledAt(),
				appointment.notes(),
				appointment.status(),
				appointment.createdByUsername());
	}
}
