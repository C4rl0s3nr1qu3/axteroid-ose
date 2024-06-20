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
@Constraint(validatedBy = RetencionValidateTypeDocumentoRelacionado.class)
@Documented
public @interface TipoDocumentoRelacionadoValidator {
	String message() default "El documento no contiene informacion correcta en el campo \"tipoDocumentoRelacionado\". Verificar el catalogo N 1, debe ser (01,07,08 o 12)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
