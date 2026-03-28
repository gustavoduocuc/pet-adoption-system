package com.duoc.pet_adoption_system.identity.infrastructure.security;

import com.duoc.pet_adoption_system.identity.domain.entities.User;
import com.duoc.pet_adoption_system.identity.domain.entities.UserRole;
import com.duoc.pet_adoption_system.identity.domain.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public JpaUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		var authorities = user.roles().stream()
				.map(UserRole::name)
				.map(r -> new SimpleGrantedAuthority("ROLE_" + r))
				.collect(Collectors.toList());
		return org.springframework.security.core.userdetails.User
				.withUsername(user.username())
				.password(user.passwordHash())
				.authorities(authorities)
				.build();
	}
}
