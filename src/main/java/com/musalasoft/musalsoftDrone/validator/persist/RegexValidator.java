package com.musalasoft.musalsoftDrone.validator.persist;

import com.musalasoft.musalsoftDrone.validator.annotations.Regex;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RegexValidator implements ConstraintValidator<Regex, String> {

    private Regex constraintAnnotation;

    @Override
    public void initialize(Regex constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return (value != null && value.matches(constraintAnnotation.regex())) || ((!constraintAnnotation.required() && value == null));
    }
}
