package com.axteroid.ose.server.tools.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User: HNA
 * Date: 10/05/16
 */
public class DespatchSerieDocumentValidator implements ConstraintValidator<ValidateSerieDespatch, String> {

    public void initialize(ValidateSerieDespatch validateTypeDocumentInvoice) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        String exp = "T\\w{3}-\\d{1,8}";
        return value.matches(exp);
    }
}
