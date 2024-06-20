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
@Constraint(validatedBy = PercepcionValidateTypeDocumentoEmisor.class)
@Documented
public @interface TipoDocumentoIdentidadValidatorTypePercepcion {
	 String message() default "El documento no contiene informacion correcta en el campo \"tipoDocumentoEmisor\". Verificar el catalogo N 6 - debe ser 6 o 1";
	 Class<?>[] groups() default {};
	 Class<? extends Payload>[] payload() default {};
}
