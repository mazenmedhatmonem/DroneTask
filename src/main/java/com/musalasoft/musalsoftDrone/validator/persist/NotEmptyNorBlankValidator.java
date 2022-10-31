package com.musalasoft.musalsoftDrone.validator.persist;

import com.musalasoft.musalsoftDrone.validator.annotations.NotEmptyNorBlank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEmptyNorBlankValidator implements ConstraintValidator<NotEmptyNorBlank, String> {


    private NotEmptyNorBlank constraintAnnotation;


    @Override
    public void initialize(NotEmptyNorBlank constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {


        return (value != null && !value.trim().isEmpty()) || ((!constraintAnnotation.required() && value == null));
    }
}
