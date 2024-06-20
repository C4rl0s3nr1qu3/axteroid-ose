package com.axteroid.ose.server.tools.validation;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class RetencionValidateTypeDocumentoEmisor implements ConstraintValidator< TipoDocumentoIdentidadValidatorTypeRetencion, String> {

	@Override
	public void initialize(
			TipoDocumentoIdentidadValidatorTypeRetencion constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		 if(StringUtils.isBlank(value)) return true;
		 List values= Arrays.asList("6");		 	 
		return values.contains(value);
	}

}
