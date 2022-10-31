package com.musalasoft.musalsoftDrone.validator.persist;

import com.musalasoft.musalsoftDrone.validator.annotations.Exists;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class ExistsValidator extends OccurenceValidator implements ConstraintValidator<Exists, Object> {

    protected Exists constraintAnnotation;

    public ExistsValidator(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void initialize(Exists constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (value != null) {
            String attributeName = constraintAnnotation.field();
            Long count = constraintAnnotation.count();
            Long result;
            if (!StringUtils.hasText(attributeName)) {
                result = count(value, (ConstraintValidatorContextImpl) context, constraintAnnotation.entity());
            } else {
                result = count(value, attributeName, constraintAnnotation.entity());
            }
            return Objects.equals(result, count);

        }

        return !constraintAnnotation.required();
    }
}
