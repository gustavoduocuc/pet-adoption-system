package com.duoc.pet_adoption_system.web.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotSqlLikeValidatorTest {

	private final NotSqlLikeValidator validator = new NotSqlLikeValidator();

	@Test
	void acceptsNullBlankOrNormalText() {
		assertTrue(validator.isValid(null, null));
		assertTrue(validator.isValid("", null));
		assertTrue(validator.isValid("   ", null));
		assertTrue(validator.isValid("Control de pulgas aplicado", null));
	}

	@Test
	void rejectsUnionSelectPattern() {
		assertFalse(validator.isValid("union select id from users", null));
	}

	@Test
	void rejectsCommentDashDash() {
		assertFalse(validator.isValid("nota -- fin", null));
	}

	@Test
	void rejectsSemicolon() {
		assertFalse(validator.isValid("a; b", null));
	}
}
