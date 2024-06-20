package com.axteroid.ose.server.tools.validation;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User: RAC
 * Date: 15/03/12
 */
public class NotaCDSerieDocumentValidator implements ConstraintValidator<ValidateSerieNotaCD, String> {

    public void initialize(ValidateSerieNotaCD validateTypeDocumentInvoice) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(StringUtils.isBlank(value)) return true;

        String exp = "F\\w{3}-\\d{1,8}";
        if (value.matches(exp)) {
            return true;
        } else {
            exp = "B\\w{3}-\\d{1,8}";
            return value.matches(exp);
        }

    }

    public static void main(String[] args) {
//        String value="B555-545";
        String value="RA-19062012-4";
        String exp = "RA-\\d{8}-\\d{1,3}";
        System.out.println(value.matches(exp));
    }
}
