package com.duoc.pet_adoption_system.shared.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public final class Id {

	private final String value;

	private Id(String value) {
		this.value = Objects.requireNonNull(value);
	}

	public static Id generate() {
		return new Id(UUID.randomUUID().toString());
	}

	public static Id of(String value) {
		if (value == null || value.isBlank()) {
			throw com.duoc.pet_adoption_system.shared.domain.DomainError.validation("Id must not be blank");
		}
		return new Id(value);
	}

	public String value() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Id id = (Id) o;
		return value.equals(id.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
