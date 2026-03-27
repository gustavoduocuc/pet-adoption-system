package com.duoc.pet_adoption_system.pets.infrastructure.persistence;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SpringDataPetJpaRepository extends JpaRepository<PetJpaEntity, String>, JpaSpecificationExecutor<PetJpaEntity> {

	List<PetJpaEntity> findByAdoptionStatusOrderByNameAsc(AdoptionStatus adoptionStatus);
}
