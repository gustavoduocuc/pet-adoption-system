package com.duoc.pet_adoption_system.identity.infrastructure.persistence;

import com.duoc.pet_adoption_system.identity.domain.entities.User;
import com.duoc.pet_adoption_system.identity.domain.entities.UserRole;
import com.duoc.pet_adoption_system.identity.domain.repositories.UserRepository;
import com.duoc.pet_adoption_system.shared.domain.valueobjects.Id;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;

@Repository
public class UserRepositoryJpaAdapter implements UserRepository {

	private final SpringDataUserJpaRepository springData;

	public UserRepositoryJpaAdapter(SpringDataUserJpaRepository springData) {
		this.springData = springData;
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return springData.findByUsernameIgnoreCase(username).map(this::toDomain);
	}

	@Override
	public void save(User user) {
		UserJpaEntity entity = new UserJpaEntity();
		entity.setId(user.id().value());
		entity.setUsername(user.username());
		entity.setPasswordHash(user.passwordHash());
		entity.setRoles(new HashSet<>(user.roles()));
		springData.save(entity);
	}

	private User toDomain(UserJpaEntity entity) {
		return User.create(
				Id.of(entity.getId()),
				entity.getUsername(),
				entity.getPasswordHash(),
				entity.getRoles().isEmpty()
						? java.util.Set.of(UserRole.USER)
						: java.util.EnumSet.copyOf(entity.getRoles()));
	}
}
