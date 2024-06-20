package com.axteroid.ose.server.ubl21.validation;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * User: RAC
 * Date: 15/03/12
 */
public class TipoDocumentoValidatorTypeValidator implements ConstraintValidator<TipoDocumentoValidatorType, String> {

    public void initialize(TipoDocumentoValidatorType validatorType) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) return true;
        //HNA: Se completa los valores
        List values = Arrays.asList("01", "03", "07", "08", "09", "12", "13", "18", "31", "04", "05", "99");
        return values.contains(value);
    }
}
