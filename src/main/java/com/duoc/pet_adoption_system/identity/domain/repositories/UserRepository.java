package com.duoc.pet_adoption_system.identity.domain.repositories;

import com.duoc.pet_adoption_system.identity.domain.entities.User;

import java.util.Optional;

public interface UserRepository {

	Optional<User> findByUsername(String username);

	void save(User user);
}
