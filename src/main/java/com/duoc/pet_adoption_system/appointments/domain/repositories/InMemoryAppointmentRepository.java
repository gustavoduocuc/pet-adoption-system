package com.duoc.pet_adoption_system.appointments.domain.repositories;

import com.duoc.pet_adoption_system.appointments.domain.entities.Appointment;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class InMemoryAppointmentRepository implements AppointmentRepository {

	private final Map<String, Appointment> byId = new HashMap<>();

	@Override
	public List<Appointment> findAllOrderByScheduledAt() {
		return byId.values().stream()
				.sorted(Comparator.comparing(Appointment::scheduledAt))
				.toList();
	}

	@Override
	public List<Appointment> findByPatientIdOrderByScheduledAt(Id patientId) {
		return byId.values().stream()
				.filter(a -> a.patientId().equals(patientId))
				.sorted(Comparator.comparing(Appointment::scheduledAt))
				.toList();
	}

	@Override
	public List<Appointment> findByCreatedByUsernameOrderByScheduledAt(String username) {
		if (username == null) {
			return List.of();
		}
		String key = username.trim().toLowerCase(Locale.ROOT);
		return byId.values().stream()
				.filter(a -> a.createdByUsername().toLowerCase(Locale.ROOT).equals(key))
				.sorted(Comparator.comparing(Appointment::scheduledAt))
				.toList();
	}

	@Override
	public Optional<Appointment> findById(Id id) {
		return Optional.ofNullable(byId.get(id.value()));
	}

	@Override
	public void save(Appointment appointment) {
		byId.put(appointment.id().value(), appointment);
	}
}
