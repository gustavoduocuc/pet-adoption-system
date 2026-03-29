package com.duoc.pet_adoption_system.appointments.infrastructure.persistence;

import com.duoc.pet_adoption_system.appointments.domain.entities.AppointmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class AppointmentJpaEntity {

	@Id
	@Column(length = 36)
	private String id;

	@Column(name = "patient_id", nullable = false, length = 36)
	private String patientId;

	@Column(name = "scheduled_at", nullable = false)
	private LocalDateTime scheduledAt;

	@Column(length = 2000)
	private String notes;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private AppointmentStatus status;

	@Column(name = "created_by_username", nullable = false, length = 100)
	private String createdByUsername;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public LocalDateTime getScheduledAt() {
		return scheduledAt;
	}

	public void setScheduledAt(LocalDateTime scheduledAt) {
		this.scheduledAt = scheduledAt;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public AppointmentStatus getStatus() {
		return status;
	}

	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}

	public String getCreatedByUsername() {
		return createdByUsername;
	}

	public void setCreatedByUsername(String createdByUsername) {
		this.createdByUsername = createdByUsername;
	}
}
