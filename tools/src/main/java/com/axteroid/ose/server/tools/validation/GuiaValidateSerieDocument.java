package com.axteroid.ose.server.tools.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GuiaValidateSerieDocument implements ConstraintValidator<SerieGuiaValidate, String>{

	@Override
	public void initialize(SerieGuiaValidate constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		String exp = "T\\w{3}-\\d{1,8}";
		String exp2 = "EG01-\\d{1,8}";
		if(value.matches(exp) || value.matches(exp2)){
			return true;		
		}else{
			return false;
		}
	}

}
