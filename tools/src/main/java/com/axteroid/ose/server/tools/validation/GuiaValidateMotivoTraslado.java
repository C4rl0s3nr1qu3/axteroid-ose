package com.axteroid.ose.server.tools.validation;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

public class GuiaValidateMotivoTraslado implements ConstraintValidator<MotivoTrasladoDocumentValidate, String> {

	@Override
	public void initialize(MotivoTrasladoDocumentValidate constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(StringUtils.isBlank(value)) return true;
		 List values= Arrays.asList("01","14","02","04","18","08","09","19","13");		 	 
		return values.contains(value);
	}

}
