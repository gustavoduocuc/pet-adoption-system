package com.duoc.pet_adoption_system.appointments.domain.repositories;

import com.duoc.pet_adoption_system.appointments.domain.entities.Appointment;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {

	List<Appointment> findAllOrderByScheduledAt();

	List<Appointment> findByPatientIdOrderByScheduledAt(Id patientId);

	List<Appointment> findByCreatedByUsernameOrderByScheduledAt(String username);

	Optional<Appointment> findById(Id id);

	void save(Appointment appointment);
}
