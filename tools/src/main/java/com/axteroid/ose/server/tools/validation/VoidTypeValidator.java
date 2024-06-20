package com.axteroid.ose.server.tools.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * User: RAC
 * Date: 15/03/12
 */
public class VoidTypeValidator implements ConstraintValidator<ValidateTypeRAVoid, String> {

    public void initialize(ValidateTypeRAVoid validateTypeDocumentInvoice) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        List values = Arrays.asList("RA");
        return values.contains(value);
    }
}
