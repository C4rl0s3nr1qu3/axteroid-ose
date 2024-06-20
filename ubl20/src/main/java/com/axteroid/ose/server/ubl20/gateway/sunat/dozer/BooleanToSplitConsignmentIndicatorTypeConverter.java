package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.SplitConsignmentIndicatorType;

import org.dozer.CustomConverter;

/**
 * User: HNA
 * Date: 16/11/15
 */
public class BooleanToSplitConsignmentIndicatorTypeConverter implements CustomConverter {

	@Override
	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {

		if(sourceFieldValue == null) {
			return null;
		}
		
        Boolean valor = (Boolean) sourceFieldValue;
        
        SplitConsignmentIndicatorType result = new SplitConsignmentIndicatorType();
        result.setValue(valor);
		
		return result;
	}

}
