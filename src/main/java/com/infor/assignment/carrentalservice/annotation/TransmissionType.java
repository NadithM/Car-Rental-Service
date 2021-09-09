package com.infor.assignment.carrentalservice.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = TransmissionTypeValidator.class)
public @interface TransmissionType {

    String message() default "Invalid Transmission Type found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}