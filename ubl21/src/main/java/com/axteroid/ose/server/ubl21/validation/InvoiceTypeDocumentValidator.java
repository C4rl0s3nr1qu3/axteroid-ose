package com.axteroid.ose.server.ubl21.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * User: RAC
 * Date: 15/03/12
 */
public class InvoiceTypeDocumentValidator implements ConstraintValidator<ValidateTypeDocumentInvoice, String> {

    public void initialize(ValidateTypeDocumentInvoice validateTypeDocumentInvoice) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        List values = Arrays.asList("01", "03");
        return values.contains(value);
    }
}
