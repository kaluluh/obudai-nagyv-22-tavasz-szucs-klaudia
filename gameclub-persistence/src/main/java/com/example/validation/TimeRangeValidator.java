package com.example.validation;

import com.example.dto.GameFormDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimeRangeValidator implements ConstraintValidator<TimeRangeConstraint, GameFormDTO> {

    @Override
    public boolean isValid(GameFormDTO value, ConstraintValidatorContext context) {
        if (value.getNoOfPlayersMin() == null
                || value.getNoOfPlayersMax() == null
                || value.getPlayTimeMin() == null
                || value.getPlayTimeMax() == null) {
            return false;
        } else {
            if (value.getNoOfPlayersMin() < value.getNoOfPlayersMax()
                    && value.getPlayTimeMin() < value.getPlayTimeMax()){
                return true;
            } else {
                return false;
            }
        }
    }
}
