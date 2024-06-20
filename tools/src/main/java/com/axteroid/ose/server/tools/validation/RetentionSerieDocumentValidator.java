package com.axteroid.ose.server.tools.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User: HNA
 * Date: 10/05/16
 */
public class RetentionSerieDocumentValidator implements ConstraintValidator<ValidateSerieRetention, String> {

    public void initialize(ValidateSerieRetention validateTypeDocumentInvoice) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        String exp = "R\\w{3}-\\d{1,8}";
        return value.matches(exp);
    }
}
