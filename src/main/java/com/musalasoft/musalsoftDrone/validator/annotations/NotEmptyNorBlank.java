package com.musalasoft.musalsoftDrone.validator.annotations;

import com.musalasoft.musalsoftDrone.validator.persist.NotEmptyNorBlankValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotEmptyNorBlankValidator.class)
@Target( {  ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyNorBlank {
    String message() default "input must not be empty or blank";
    boolean required() default false;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
