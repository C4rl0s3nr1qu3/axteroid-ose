package com.axteroid.ose.server.ubl21.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User: RAC
 * Date: 15/03/12
 */
public class BoletaSerieDocumentValidator implements ConstraintValidator<ValidateSerieBoleta, String> {

    public void initialize(ValidateSerieBoleta validateTypeDocumentInvoice) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        String exp = "B\\w{3}-\\d{1,8}";
        return value.matches(exp);
    }
}
