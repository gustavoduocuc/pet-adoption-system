package com.duoc.pet_adoption_system.patients.infrastructure.http;

import com.duoc.pet_adoption_system.patients.application.CreatePatientUseCase;
import com.duoc.pet_adoption_system.patients.application.GetPatientByIdUseCase;
import com.duoc.pet_adoption_system.patients.application.ListPatientsUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

	private final ListPatientsUseCase listPatientsUseCase;
	private final GetPatientByIdUseCase getPatientByIdUseCase;
	private final CreatePatientUseCase createPatientUseCase;

	public PatientController(
			ListPatientsUseCase listPatientsUseCase,
			GetPatientByIdUseCase getPatientByIdUseCase,
			CreatePatientUseCase createPatientUseCase) {
		this.listPatientsUseCase = listPatientsUseCase;
		this.getPatientByIdUseCase = getPatientByIdUseCase;
		this.createPatientUseCase = createPatientUseCase;
	}

	@GetMapping
	public List<PatientResponse> list() {
		return listPatientsUseCase.execute().stream().map(PatientResponse::from).toList();
	}

	@GetMapping("/{id}")
	public PatientResponse getById(@PathVariable String id) {
		return PatientResponse.from(getPatientByIdUseCase.execute(id));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PatientResponse create(@Valid @RequestBody CreatePatientRequest request) {
		var patient = createPatientUseCase.execute(
				request.name(),
				request.species(),
				request.intakeDate(),
				request.treatmentNotes(),
				request.careStatus());
		return PatientResponse.from(patient);
	}
}
