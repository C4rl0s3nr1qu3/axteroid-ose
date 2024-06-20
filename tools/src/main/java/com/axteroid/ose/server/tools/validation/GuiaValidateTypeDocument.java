package com.axteroid.ose.server.tools.validation;

import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;

public class GuiaValidateTypeDocument implements ConstraintValidator<TipoDocumentoGuiaValidate, String>{

	@Override
	public void initialize(TipoDocumentoGuiaValidate constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(StringUtils.isBlank(value)) return true;
		 List values= Arrays.asList("09");		 	 
		return values.contains(value);
	}

}
