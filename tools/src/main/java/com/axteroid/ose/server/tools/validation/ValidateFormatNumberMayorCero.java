package com.axteroid.ose.server.tools.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidateFormatNumberMayorCeroValidator.class)
@Documented
public @interface ValidateFormatNumberMayorCero {
    String message() default "";
    int presicion() default 0;
    int scale() default 0;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
