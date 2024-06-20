package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import java.math.BigDecimal;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CalculationRateType;

import org.dozer.CustomConverter;

public class StringToCalculationRateTypeConverter implements CustomConverter {

	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {

		if(sourceFieldValue == null) return null;
		
		BigDecimal value = (BigDecimal) sourceFieldValue;
		
		CalculationRateType result = new CalculationRateType();
		result.setValue(value);
		
		return result;
	}

}
