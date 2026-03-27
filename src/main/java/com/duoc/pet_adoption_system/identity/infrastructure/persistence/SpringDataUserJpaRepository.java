package com.duoc.pet_adoption_system.identity.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataUserJpaRepository extends JpaRepository<UserJpaEntity, String> {

	Optional<UserJpaEntity> findByUsernameIgnoreCase(String username);
}
