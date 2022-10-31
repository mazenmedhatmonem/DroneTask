package com.musalasoft.musalsoftDrone.validator.annotations;

import com.musalasoft.musalsoftDrone.entity.Base;
import com.musalasoft.musalsoftDrone.validator.persist.UniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {UniqueValidator.class})
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {

    String message() default "input value is not unique";

    String field();

    Class<? extends Base> entity();

    boolean required() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
