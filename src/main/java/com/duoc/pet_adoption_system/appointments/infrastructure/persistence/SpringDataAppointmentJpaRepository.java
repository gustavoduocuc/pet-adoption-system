package com.duoc.pet_adoption_system.appointments.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataAppointmentJpaRepository extends JpaRepository<AppointmentJpaEntity, String> {

	List<AppointmentJpaEntity> findAllByOrderByScheduledAtAsc();

	List<AppointmentJpaEntity> findByPatientIdOrderByScheduledAtAsc(String patientId);

	List<AppointmentJpaEntity> findByCreatedByUsernameIgnoreCaseOrderByScheduledAtAsc(String username);
}
