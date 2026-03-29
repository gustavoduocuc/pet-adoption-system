package com.duoc.pet_adoption_system.appointments.application;

import com.duoc.pet_adoption_system.appointments.domain.entities.Appointment;
import com.duoc.pet_adoption_system.appointments.domain.repositories.AppointmentRepository;

import java.util.List;

public class ListAllAppointmentsUseCase {

	private final AppointmentRepository appointmentRepository;

	public ListAllAppointmentsUseCase(AppointmentRepository appointmentRepository) {
		this.appointmentRepository = appointmentRepository;
	}

	public List<Appointment> execute() {
		return appointmentRepository.findAllOrderByScheduledAt();
	}
}
