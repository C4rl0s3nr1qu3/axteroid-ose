package com.axteroid.ose.server.ubl21.validation;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;

/**
 * User: RAC
 * Date: 15/03/12
 */
public class ValidateFormatNumberValidator implements ConstraintValidator<ValidateFormatNumber, BigDecimal> {

    private int precision;
    private int scale;

    public void initialize(ValidateFormatNumber constraintAnnotation) {
        precision = constraintAnnotation.presicion();
        scale = constraintAnnotation.scale();
    }

    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null)
            return true;
        String format = "";
        for (int i = 0; i < 30; i++) {
            format += "#";
        }
        format += ".";
        for (int i = 0; i < 30; i++) {
            format += "#";
        }
        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
        unusualSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat(format, unusualSymbols );
        String result = df.format(value);

        int totalPrecision = 0;
        int totalScale = 0;
        if (StringUtils.contains(result, ".")) {
            totalPrecision = result.substring(0, result.indexOf(".")).length();
            totalScale = result.substring(result.indexOf(".") + 1, result.length()).length();
        } else {
            totalPrecision = result.length();
        }

        return totalPrecision <= precision && totalScale <= scale;
    }

    public static void main(String[] ags) {
        String result = "1234567890";
        int totalPrecision = 0;
        int totalScale = 0;
        if (StringUtils.contains(result, ".")) {
            totalPrecision = result.substring(0, result.indexOf(".")).length();
            totalScale = result.substring(result.indexOf(".") + 1, result.length()).length();
        } else {
            totalPrecision = result.length();
        }
        System.out.println(totalPrecision);
        System.out.println(totalScale);

    }
}
