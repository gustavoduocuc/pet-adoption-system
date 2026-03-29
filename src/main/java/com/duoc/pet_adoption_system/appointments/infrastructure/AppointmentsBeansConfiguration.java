package com.duoc.pet_adoption_system.appointments.infrastructure;

import com.duoc.pet_adoption_system.appointments.application.CreateAppointmentUseCase;
import com.duoc.pet_adoption_system.appointments.application.ListAllAppointmentsUseCase;
import com.duoc.pet_adoption_system.appointments.application.ListAppointmentsByPatientUseCase;
import com.duoc.pet_adoption_system.appointments.application.ListMyAppointmentsUseCase;
import com.duoc.pet_adoption_system.appointments.application.UpdateAppointmentUseCase;
import com.duoc.pet_adoption_system.appointments.domain.repositories.AppointmentRepository;
import com.duoc.pet_adoption_system.patients.domain.repositories.PatientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppointmentsBeansConfiguration {

	@Bean
	public ListAllAppointmentsUseCase listAllAppointmentsUseCase(AppointmentRepository appointmentRepository) {
		return new ListAllAppointmentsUseCase(appointmentRepository);
	}

	@Bean
	public ListAppointmentsByPatientUseCase listAppointmentsByPatientUseCase(
			AppointmentRepository appointmentRepository,
			PatientRepository patientRepository) {
		return new ListAppointmentsByPatientUseCase(appointmentRepository, patientRepository);
	}

	@Bean
	public ListMyAppointmentsUseCase listMyAppointmentsUseCase(AppointmentRepository appointmentRepository) {
		return new ListMyAppointmentsUseCase(appointmentRepository);
	}

	@Bean
	public CreateAppointmentUseCase createAppointmentUseCase(
			AppointmentRepository appointmentRepository,
			PatientRepository patientRepository) {
		return new CreateAppointmentUseCase(appointmentRepository, patientRepository);
	}

	@Bean
	public UpdateAppointmentUseCase updateAppointmentUseCase(AppointmentRepository appointmentRepository) {
		return new UpdateAppointmentUseCase(appointmentRepository);
	}
}
