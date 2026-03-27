package com.duoc.pet_adoption_system.identity.domain.repositories;

import com.duoc.pet_adoption_system.identity.domain.entities.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

	private final Map<String, User> byUsername = new HashMap<>();

	@Override
	public Optional<User> findByUsername(String username) {
		if (username == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(byUsername.get(username.trim().toLowerCase()));
	}

	@Override
	public void save(User user) {
		byUsername.put(user.username().toLowerCase(), user);
	}
}
