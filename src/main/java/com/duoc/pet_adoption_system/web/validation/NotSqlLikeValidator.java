package com.duoc.pet_adoption_system.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.regex.Pattern;

public class NotSqlLikeValidator implements ConstraintValidator<NotSqlLike, String> {

	private static final int FLAGS = Pattern.CASE_INSENSITIVE;

	private static final List<Pattern> DISALLOWED = List.of(
			Pattern.compile("--", FLAGS),
			Pattern.compile(";", FLAGS),
			Pattern.compile("/\\*", FLAGS),
			Pattern.compile("\\*/", FLAGS),
			Pattern.compile("\\b(and|or)\\s+\\d+\\s*=\\s*\\d+", FLAGS),
			Pattern.compile("\\bunion\\s+select\\b", FLAGS),
			Pattern.compile("\\bdrop\\s+table\\b", FLAGS),
			Pattern.compile("\\binsert\\s+into\\b", FLAGS),
			Pattern.compile("\\bdelete\\s+from\\b", FLAGS),
			Pattern.compile("\\bexec\\s*\\(", FLAGS),
			Pattern.compile("\\bxp_", FLAGS));

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isBlank()) {
			return true;
		}
		for (Pattern pattern : DISALLOWED) {
			if (pattern.matcher(value).find()) {
				return false;
			}
		}
		return true;
	}
}
