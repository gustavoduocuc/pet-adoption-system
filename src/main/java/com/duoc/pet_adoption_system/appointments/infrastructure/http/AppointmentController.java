package com.duoc.pet_adoption_system.appointments.infrastructure.http;

import com.duoc.pet_adoption_system.appointments.application.CreateAppointmentUseCase;
import com.duoc.pet_adoption_system.appointments.application.ListAllAppointmentsUseCase;
import com.duoc.pet_adoption_system.appointments.application.ListAppointmentsByPatientUseCase;
import com.duoc.pet_adoption_system.appointments.application.ListMyAppointmentsUseCase;
import com.duoc.pet_adoption_system.appointments.application.UpdateAppointmentUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@Validated
public class AppointmentController {

	private final ListAllAppointmentsUseCase listAllAppointmentsUseCase;
	private final ListMyAppointmentsUseCase listMyAppointmentsUseCase;
	private final ListAppointmentsByPatientUseCase listAppointmentsByPatientUseCase;
	private final CreateAppointmentUseCase createAppointmentUseCase;
	private final UpdateAppointmentUseCase updateAppointmentUseCase;

	public AppointmentController(
			ListAllAppointmentsUseCase listAllAppointmentsUseCase,
			ListMyAppointmentsUseCase listMyAppointmentsUseCase,
			ListAppointmentsByPatientUseCase listAppointmentsByPatientUseCase,
			CreateAppointmentUseCase createAppointmentUseCase,
			UpdateAppointmentUseCase updateAppointmentUseCase) {
		this.listAllAppointmentsUseCase = listAllAppointmentsUseCase;
		this.listMyAppointmentsUseCase = listMyAppointmentsUseCase;
		this.listAppointmentsByPatientUseCase = listAppointmentsByPatientUseCase;
		this.createAppointmentUseCase = createAppointmentUseCase;
		this.updateAppointmentUseCase = updateAppointmentUseCase;
	}

	@GetMapping("/my")
	public List<AppointmentResponse> my(@AuthenticationPrincipal String username) {
		return listMyAppointmentsUseCase.execute(username).stream()
				.map(AppointmentResponse::from)
				.toList();
	}

	@GetMapping
	public List<AppointmentResponse> listAll() {
		return listAllAppointmentsUseCase.execute().stream()
				.map(AppointmentResponse::from)
				.toList();
	}

	@GetMapping("/{patientId}")
	public List<AppointmentResponse> listByPatient(@PathVariable @Size(max = 36) String patientId) {
		return listAppointmentsByPatientUseCase.execute(patientId).stream()
				.map(AppointmentResponse::from)
				.toList();
	}

	@PostMapping("/new/{patientId}")
	@ResponseStatus(HttpStatus.CREATED)
	public AppointmentResponse create(
			@PathVariable @Size(max = 36) String patientId,
			@Valid @RequestBody CreateAppointmentRequest request,
			@AuthenticationPrincipal String username) {
		var appointment = createAppointmentUseCase.execute(
				patientId,
				request.scheduledAt(),
				request.notes(),
				request.status(),
				username);
		return AppointmentResponse.from(appointment);
	}

	@PutMapping("/update/{appointmentId}")
	public AppointmentResponse update(
			@PathVariable @Size(max = 36) String appointmentId,
			@Valid @RequestBody UpdateAppointmentRequest request) {
		var appointment = updateAppointmentUseCase.execute(
				appointmentId,
				request.scheduledAt(),
				request.notes(),
				request.status());
		return AppointmentResponse.from(appointment);
	}
}
