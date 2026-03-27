package com.duoc.pet_adoption_system.identity.application.ports;

import com.duoc.pet_adoption_system.identity.domain.entities.UserRole;

import java.util.Set;

public interface AccessTokenIssuer {

	String issue(String username, Set<UserRole> roles);
}
