package com.axteroid.ose.server.tools.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RRSerieDocumentValidator implements ConstraintValidator<ValidateSerieRR, String> {

    public void initialize(ValidateSerieRR validateTypeDocumentInvoice) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        String exp = "RR-\\d{8}-\\d{1,5}";
        return value.matches(exp);
    }
}