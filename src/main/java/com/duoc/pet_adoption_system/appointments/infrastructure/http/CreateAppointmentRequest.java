package com.duoc.pet_adoption_system.appointments.infrastructure.http;

import com.duoc.pet_adoption_system.appointments.domain.entities.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
		@NotNull LocalDateTime scheduledAt,
		String notes,
		AppointmentStatus status) {
}
