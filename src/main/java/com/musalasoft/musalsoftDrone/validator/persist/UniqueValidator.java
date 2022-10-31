package com.musalasoft.musalsoftDrone.validator.persist;

import com.musalasoft.musalsoftDrone.validator.annotations.Unique;

import javax.persistence.EntityManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class UniqueValidator extends OccurenceValidator implements ConstraintValidator<Unique, Object> {

    protected Unique constraintAnnotation;

    public UniqueValidator(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (value != null) {
            String attributeName = constraintAnnotation.field();
            Long result = count(value, attributeName, constraintAnnotation.entity());
            return Objects.equals(result, 0L);

        }
        return !constraintAnnotation.required();
    }
}
