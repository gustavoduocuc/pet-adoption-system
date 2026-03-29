package com.duoc.pet_adoption_system.appointments.application;

import com.duoc.pet_adoption_system.appointments.domain.entities.Appointment;
import com.duoc.pet_adoption_system.appointments.domain.entities.AppointmentStatus;
import com.duoc.pet_adoption_system.appointments.domain.repositories.AppointmentRepository;
import com.duoc.pet_adoption_system.patients.domain.repositories.PatientRepository;
import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

import java.time.LocalDateTime;

public class CreateAppointmentUseCase {

	private final AppointmentRepository appointmentRepository;
	private final PatientRepository patientRepository;

	public CreateAppointmentUseCase(
			AppointmentRepository appointmentRepository,
			PatientRepository patientRepository) {
		this.appointmentRepository = appointmentRepository;
		this.patientRepository = patientRepository;
	}

	public Appointment execute(
			String patientId,
			LocalDateTime scheduledAt,
			String notes,
			AppointmentStatus status,
			String createdByUsername) {
		Id pid = Id.of(patientId);
		if (patientRepository.findById(pid).isEmpty()) {
			throw DomainError.notFound("Patient " + patientId + " not found");
		}
		AppointmentStatus effectiveStatus = status != null ? status : AppointmentStatus.SCHEDULED;
		Appointment appointment = Appointment.create(
				Id.generate(),
				pid,
				scheduledAt,
				notes,
				effectiveStatus,
				createdByUsername);
		appointmentRepository.save(appointment);
		return appointment;
	}
}
