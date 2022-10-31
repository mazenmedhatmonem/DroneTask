package com.musalasoft.musalsoftDrone.validator.annotations;

import com.musalasoft.musalsoftDrone.entity.Base;
import com.musalasoft.musalsoftDrone.validator.persist.ExistsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistsValidator.class)
@Target( {  ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Exists {

    String message() default "input value is does not exist";
    String field() default "";
    long count() default 1;
    Class<? extends Base> entity();
    boolean required() default false;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
