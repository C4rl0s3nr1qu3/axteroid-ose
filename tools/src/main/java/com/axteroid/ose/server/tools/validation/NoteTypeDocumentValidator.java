package com.axteroid.ose.server.tools.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * User: RAC
 * Date: 15/03/12
 */
public class NoteTypeDocumentValidator implements ConstraintValidator<ValidateTypeDocumentNote, String> {

    public void initialize(ValidateTypeDocumentNote validateTypeDocumentInvoice) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        List values = Arrays.asList("07", "08");
        return values.contains(value);
    }
}
