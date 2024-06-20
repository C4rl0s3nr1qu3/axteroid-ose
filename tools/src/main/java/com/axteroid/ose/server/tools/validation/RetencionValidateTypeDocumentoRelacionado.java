package com.axteroid.ose.server.tools.validation;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

public class RetencionValidateTypeDocumentoRelacionado  implements ConstraintValidator<TipoDocumentoRelacionadoValidator, String>{

	@Override
	public void initialize(
			TipoDocumentoRelacionadoValidator constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(StringUtils.isBlank(value)) return true;
		 List values= Arrays.asList("01","07","08","12");		 	 
		return values.contains(value);
	}

}
