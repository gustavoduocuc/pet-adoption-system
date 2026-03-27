package com.duoc.pet_adoption_system.patients.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPatientJpaRepository extends JpaRepository<PatientJpaEntity, String> {
}
