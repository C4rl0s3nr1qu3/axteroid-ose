package com.axteroid.ose.server.tools.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * User: RAC
 * Date: 15/03/12
 */
public class TicketTypeValidator implements ConstraintValidator<ValidateTypeRCTicket, String> {

    public void initialize(ValidateTypeRCTicket validateTypeDocumentInvoice) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        List values = Arrays.asList("RC");
        return values.contains(value);
    }
}
