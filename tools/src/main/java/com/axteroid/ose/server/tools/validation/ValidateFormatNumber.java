package com.axteroid.ose.server.tools.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * User: RAC
 * Date: 15/03/12
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidateFormatNumberValidator.class)
@Documented
public @interface ValidateFormatNumber {
    String message() default "";
    int presicion() default 0;
    int scale() default 0;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

