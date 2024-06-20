package com.axteroid.ose.server.tools.validation;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

public class PercepcionValidateTypeDocumentoEmisor implements ConstraintValidator< TipoDocumentoIdentidadValidatorTypePercepcion, String> {

	@Override
	public void initialize(
			TipoDocumentoIdentidadValidatorTypePercepcion constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(StringUtils.isBlank(value)) return true;
		 List values= Arrays.asList("6","1");		 	 
		return values.contains(value);
	}

}
