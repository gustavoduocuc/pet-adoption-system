package com.duoc.pet_adoption_system.appointments.application;

import com.duoc.pet_adoption_system.appointments.domain.entities.Appointment;
import com.duoc.pet_adoption_system.appointments.domain.repositories.AppointmentRepository;

import java.util.List;

public class ListMyAppointmentsUseCase {

	private final AppointmentRepository appointmentRepository;

	public ListMyAppointmentsUseCase(AppointmentRepository appointmentRepository) {
		this.appointmentRepository = appointmentRepository;
	}

	public List<Appointment> execute(String username) {
		return appointmentRepository.findByCreatedByUsernameOrderByScheduledAt(username);
	}
}
