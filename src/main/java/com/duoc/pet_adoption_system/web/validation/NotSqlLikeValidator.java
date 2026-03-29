package com.duoc.pet_adoption_system.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NotSqlLikeValidator implements ConstraintValidator<NotSqlLike, String> {

	private static final Pattern disallowedPattern = Pattern.compile(
			"(--)|(;)|(/\\*)|(\\*/)|(\\b(and|or)\\s+\\d+\\s*=\\s*\\d+)|(\\bunion\\s+select\\b)|(\\bdrop\\s+table\\b)|(\\binsert\\s+into\\b)|(\\bdelete\\s+from\\b)|(\\bexec\\s*\\()|(\\bxp_)",
			Pattern.CASE_INSENSITIVE);

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isBlank()) {
			return true;
		}
		return !disallowedPattern.matcher(value).find();
	}
}
