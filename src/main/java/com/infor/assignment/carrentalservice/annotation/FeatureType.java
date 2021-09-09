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
@Constraint(validatedBy = FeatureTypeValidator.class)
public @interface FeatureType {

    String message() default "Invalid Feature Type found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}