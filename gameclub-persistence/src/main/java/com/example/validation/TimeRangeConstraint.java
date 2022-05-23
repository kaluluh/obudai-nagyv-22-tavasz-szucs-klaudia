package com.example.validation;

import com.example.entity.Limits;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;

@Constraint(validatedBy = TimeRangeValidator.class)
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeRangeConstraint {
    String message() default
            "Maximum must be greater than minimum";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
