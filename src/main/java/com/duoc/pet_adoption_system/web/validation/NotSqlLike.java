package com.duoc.pet_adoption_system.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = NotSqlLikeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotSqlLike {

	String message() default "El texto contiene caracteres o patrones no permitidos";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
