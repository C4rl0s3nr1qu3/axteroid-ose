package com.axteroid.ose.server.ubl21.validation;

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
@Constraint(validatedBy = InvoiceTypeDocumentValidator.class)
@Documented
public @interface ValidateTypeDocumentInvoice {
    String message() default "El tipo de documento es incorrecto. Debe ser Factura:01 ó Boleta:03)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

