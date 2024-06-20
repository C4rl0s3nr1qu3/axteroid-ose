package com.axteroid.ose.server.tools.validation;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * User: RAC
 * Date: 15/03/12
 */
public class TipoDocumentoIdentidadTypeValidator implements ConstraintValidator<TipoDocumentoIdentidadValidatorType, String> {

    public void initialize(TipoDocumentoIdentidadValidatorType validatorType) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(StringUtils.isBlank(value)) return true;
        List values = Arrays.asList("0", "1", "4", "6", "7", "A");
        return values.contains(value);
    }
}
