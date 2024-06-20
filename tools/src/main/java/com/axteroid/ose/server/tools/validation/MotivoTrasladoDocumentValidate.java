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
@Constraint(validatedBy = GuiaValidateMotivoTraslado.class)
@Documented
public @interface MotivoTrasladoDocumentValidate {
	String message() default "El documento no contiene informacion correcta en el campo \"motivoTraslado\". Verificar el catalogo N 20, debe ser (01,14,02,04,18,08,09,19 o 13)";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
