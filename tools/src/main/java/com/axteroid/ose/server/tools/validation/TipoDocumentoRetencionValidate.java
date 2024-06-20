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
@Constraint(validatedBy = RetencionValidateTypeDocument.class)
@Documented
public @interface TipoDocumentoRetencionValidate {
	String message() default "El documento no contiene informacion correcta en el campo  \"tipodocumento\", debe ser 20";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
