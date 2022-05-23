package com.example.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class EventDateValidator implements ConstraintValidator<DateConstraint, String> {
    @Override
    public boolean isValid(String dateStr, ConstraintValidatorContext context) {
        if (dateStr.isEmpty()) {
            return false;
        }   else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withResolverStyle(ResolverStyle.SMART);
            LocalDate date = LocalDate.parse(dateStr, formatter);
            if ((date).isAfter(LocalDate.now()) ) {
                return true;
            }
            else {
                return false;
            }
        }
    }
}
