package com.axteroid.ose.server.tools.validation;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

public class PercepcionValidateTypeDocument implements ConstraintValidator<TipoDocumentoPercepcionValidate, String>{

	@Override
	public void initialize(TipoDocumentoPercepcionValidate constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(StringUtils.isBlank(value)) return true;
		 List values= Arrays.asList("40");		 	 
		return values.contains(value);
	}

}
