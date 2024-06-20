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
@Constraint(validatedBy = RetencionValidateTypeDocumentoEmisor.class)
@Documented
public @interface TipoDocumentoIdentidadValidatorTypeRetencion {
    String message() default "El documento no contiene el campo \"Tipo de documento de identidad del emisor \". Verificar el catalogo N 6, debe ser 06";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

