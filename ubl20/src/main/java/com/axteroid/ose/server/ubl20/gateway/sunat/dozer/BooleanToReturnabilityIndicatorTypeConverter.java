package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ReturnabilityIndicatorType;

import org.dozer.CustomConverter;

/**
 * User: HNA
 * Date: 16/11/15
 */
public class BooleanToReturnabilityIndicatorTypeConverter implements CustomConverter {

	@Override
	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		
		if(sourceFieldValue == null) {
			return null;
		}
		
        Boolean valor = Boolean.valueOf((String)sourceFieldValue);
        
        ReturnabilityIndicatorType result = new ReturnabilityIndicatorType();
        result.setValue(valor);

		return result;
	}
}
