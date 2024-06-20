package com.axteroid.ose.server.tools.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User: RAC
 * Date: 15/03/12
 */
public class RCSerieDocumentValidator implements ConstraintValidator<ValidateSerieRC, String> {

    public void initialize(ValidateSerieRC validateTypeDocumentInvoice) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        String exp = "RC-\\d{8}-\\d{1,3}";
        return value.matches(exp);
    }
}
