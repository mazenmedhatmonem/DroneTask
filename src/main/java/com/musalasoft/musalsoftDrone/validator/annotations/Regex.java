package com.musalasoft.musalsoftDrone.validator.annotations;

import com.musalasoft.musalsoftDrone.validator.persist.RegexValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RegexValidator.class)
@Target( {  ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Regex {

    String message() default "input does not match required pattern";
    String regex();
    boolean required() default false;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
