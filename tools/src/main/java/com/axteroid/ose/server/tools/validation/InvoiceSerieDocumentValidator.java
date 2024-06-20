package com.axteroid.ose.server.tools.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User: RAC
 * Date: 15/03/12
 */
public class InvoiceSerieDocumentValidator implements ConstraintValidator<ValidateSerieInvoice, String> {

    public void initialize(ValidateSerieInvoice validateTypeDocumentInvoice) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        String exp = "F\\w{3}-\\d{1,8}";
        return value.matches(exp);
    }
}
