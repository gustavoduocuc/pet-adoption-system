package com.duoc.pet_adoption_system.appointments.application;

import com.duoc.pet_adoption_system.appointments.domain.entities.Appointment;
import com.duoc.pet_adoption_system.appointments.domain.repositories.AppointmentRepository;
import com.duoc.pet_adoption_system.patients.domain.repositories.PatientRepository;
import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

import java.util.List;

public class ListAppointmentsByPatientUseCase {

	private final AppointmentRepository appointmentRepository;
	private final PatientRepository patientRepository;

	public ListAppointmentsByPatientUseCase(
			AppointmentRepository appointmentRepository,
			PatientRepository patientRepository) {
		this.appointmentRepository = appointmentRepository;
		this.patientRepository = patientRepository;
	}

	public List<Appointment> execute(String patientId) {
		Id pid = Id.of(patientId);
		if (patientRepository.findById(pid).isEmpty()) {
			throw DomainError.notFound("Patient " + patientId + " not found");
		}
		return appointmentRepository.findByPatientIdOrderByScheduledAt(pid);
	}
}
