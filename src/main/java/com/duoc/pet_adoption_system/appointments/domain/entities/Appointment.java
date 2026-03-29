package com.duoc.pet_adoption_system.appointments.domain.entities;

import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

import java.time.LocalDateTime;

public final class Appointment {

	private final Id id;
	private final Id patientId;
	private LocalDateTime scheduledAt;
	private String notes;
	private AppointmentStatus status;
	private final String createdByUsername;

	private Appointment(
			Id id,
			Id patientId,
			LocalDateTime scheduledAt,
			String notes,
			AppointmentStatus status,
			String createdByUsername) {
		this.id = id;
		this.patientId = patientId;
		this.scheduledAt = scheduledAt;
		this.notes = notes;
		this.status = status;
		this.createdByUsername = createdByUsername;
	}

	public static Appointment create(
			Id id,
			Id patientId,
			LocalDateTime scheduledAt,
			String notes,
			AppointmentStatus status,
			String createdByUsername) {
		validateForCreate(patientId, scheduledAt, status, createdByUsername);
		String safeNotes = notes == null ? "" : notes.trim();
		return new Appointment(id, patientId, scheduledAt, safeNotes, status, createdByUsername.trim());
	}

	public static Appointment restore(
			Id id,
			Id patientId,
			LocalDateTime scheduledAt,
			String notes,
			AppointmentStatus status,
			String createdByUsername) {
		return new Appointment(id, patientId, scheduledAt, notes, status, createdByUsername);
	}

	private static void validateForCreate(
			Id patientId,
			LocalDateTime scheduledAt,
			AppointmentStatus status,
			String createdByUsername) {
		if (patientId == null) {
			throw DomainError.validation("Patient id is required");
		}
		if (scheduledAt == null) {
			throw DomainError.validation("Scheduled time is required");
		}
		if (status == null) {
			throw DomainError.validation("Appointment status is required");
		}
		if (createdByUsername == null || createdByUsername.isBlank()) {
			throw DomainError.validation("Created-by username must not be blank");
		}
	}

	public void update(LocalDateTime scheduledAt, String notes, AppointmentStatus status) {
		if (scheduledAt == null) {
			throw DomainError.validation("Scheduled time is required");
		}
		if (status == null) {
			throw DomainError.validation("Appointment status is required");
		}
		this.scheduledAt = scheduledAt;
		this.notes = notes == null ? "" : notes.trim();
		this.status = status;
	}

	public Id id() {
		return id;
	}

	public Id patientId() {
		return patientId;
	}

	public LocalDateTime scheduledAt() {
		return scheduledAt;
	}

	public String notes() {
		return notes;
	}

	public AppointmentStatus status() {
		return status;
	}

	public String createdByUsername() {
		return createdByUsername;
	}
}
