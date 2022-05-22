package com.example.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EventDateValidator implements ConstraintValidator<DateConstraint, String> {
    @Override
    public boolean isValid(String dateStr, ConstraintValidatorContext context) {
        LocalDateTime date = LocalDateTime.of(LocalDate.parse(dateStr), LocalTime.MIN);
        if (dateStr != null && (date).isAfter(LocalDateTime.now()) ) {
            return true;
        }
        else {
            return false;
        }
    }
}
