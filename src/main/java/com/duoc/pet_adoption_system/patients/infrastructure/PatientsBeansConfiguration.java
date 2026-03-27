package com.duoc.pet_adoption_system.patients.infrastructure;

import com.duoc.pet_adoption_system.patients.application.CreatePatientUseCase;
import com.duoc.pet_adoption_system.patients.application.GetPatientByIdUseCase;
import com.duoc.pet_adoption_system.patients.application.ListPatientsUseCase;
import com.duoc.pet_adoption_system.patients.domain.repositories.PatientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PatientsBeansConfiguration {

	@Bean
	public ListPatientsUseCase listPatientsUseCase(PatientRepository patientRepository) {
		return new ListPatientsUseCase(patientRepository);
	}

	@Bean
	public GetPatientByIdUseCase getPatientByIdUseCase(PatientRepository patientRepository) {
		return new GetPatientByIdUseCase(patientRepository);
	}

	@Bean
	public CreatePatientUseCase createPatientUseCase(PatientRepository patientRepository) {
		return new CreatePatientUseCase(patientRepository);
	}
}
