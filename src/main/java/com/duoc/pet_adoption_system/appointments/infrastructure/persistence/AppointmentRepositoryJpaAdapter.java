package com.duoc.pet_adoption_system.appointments.infrastructure.persistence;

import com.duoc.pet_adoption_system.appointments.domain.entities.Appointment;
import com.duoc.pet_adoption_system.appointments.domain.entities.AppointmentStatus;
import com.duoc.pet_adoption_system.appointments.domain.repositories.AppointmentRepository;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentRepositoryJpaAdapter implements AppointmentRepository {

	private final SpringDataAppointmentJpaRepository springData;

	public AppointmentRepositoryJpaAdapter(SpringDataAppointmentJpaRepository springData) {
		this.springData = springData;
	}

	@Override
	public List<Appointment> findAllOrderByScheduledAt() {
		return springData.findAllByOrderByScheduledAtAsc().stream()
				.map(this::toDomain)
				.toList();
	}

	@Override
	public List<Appointment> findByPatientIdOrderByScheduledAt(Id patientId) {
		return springData.findByPatientIdOrderByScheduledAtAsc(patientId.value()).stream()
				.map(this::toDomain)
				.toList();
	}

	@Override
	public List<Appointment> findByCreatedByUsernameOrderByScheduledAt(String username) {
		return springData.findByCreatedByUsernameIgnoreCaseOrderByScheduledAtAsc(username).stream()
				.map(this::toDomain)
				.toList();
	}

	@Override
	public Optional<Appointment> findById(Id id) {
		return springData.findById(id.value()).map(this::toDomain);
	}

	@Override
	public void save(Appointment appointment) {
		springData.save(toEntity(appointment));
	}

	private Appointment toDomain(AppointmentJpaEntity entity) {
		return Appointment.restore(
				Id.of(entity.getId()),
				Id.of(entity.getPatientId()),
				entity.getScheduledAt(),
				entity.getNotes() == null ? "" : entity.getNotes(),
				entity.getStatus(),
				entity.getCreatedByUsername());
	}

	private AppointmentJpaEntity toEntity(Appointment appointment) {
		AppointmentJpaEntity entity = new AppointmentJpaEntity();
		entity.setId(appointment.id().value());
		entity.setPatientId(appointment.patientId().value());
		entity.setScheduledAt(appointment.scheduledAt());
		entity.setNotes(appointment.notes());
		entity.setStatus(appointment.status());
		entity.setCreatedByUsername(appointment.createdByUsername());
		return entity;
	}
}
