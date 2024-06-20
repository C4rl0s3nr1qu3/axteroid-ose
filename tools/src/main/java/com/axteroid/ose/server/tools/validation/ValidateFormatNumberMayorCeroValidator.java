package com.axteroid.ose.server.tools.validation;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidateFormatNumberMayorCeroValidator implements ConstraintValidator<ValidateFormatNumberMayorCero, BigDecimal>  {

	private int precision;
    private int scale;
    
	@Override
	public void initialize(ValidateFormatNumberMayorCero constraintAnnotation) {
		//precision = constraintAnnotation.presicion();
        //scale = constraintAnnotation.scale();		
	}

	@Override
	public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
		if(value.compareTo(BigDecimal.ZERO) == 0){
			return false;
		}			
		else{
			return true;
		}
	}

}
