package com.duoc.pet_adoption_system.identity.domain.entities;

import com.duoc.pet_adoption_system.shared.domain.DomainError;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public final class User {

	private final Id id;
	private final String username;
	private final String passwordHash;
	private final Set<UserRole> roles;

	private User(Id id, String username, String passwordHash, Set<UserRole> roles) {
		this.id = id;
		this.username = username;
		this.passwordHash = passwordHash;
		this.roles = EnumSet.copyOf(roles);
	}

	public static User create(Id id, String username, String passwordHash, Set<UserRole> roles) {
		if (username == null || username.isBlank()) {
			throw DomainError.validation("Username must not be blank");
		}
		if (passwordHash == null || passwordHash.isBlank()) {
			throw DomainError.validation("Password hash must not be blank");
		}
		if (roles == null || roles.isEmpty()) {
			throw DomainError.validation("User must have at least one role");
		}
		return new User(id, username.trim(), passwordHash, roles);
	}

	public Id id() {
		return id;
	}

	public String username() {
		return username;
	}

	public String passwordHash() {
		return passwordHash;
	}

	public Set<UserRole> roles() {
		return Collections.unmodifiableSet(roles);
	}
}
