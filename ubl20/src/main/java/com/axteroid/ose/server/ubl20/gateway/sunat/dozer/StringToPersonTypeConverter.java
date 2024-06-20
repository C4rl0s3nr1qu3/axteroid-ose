package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PersonType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

/**
 * User: HNA
 * Date: 17/11/15
 */
public class StringToPersonTypeConverter implements CustomConverter {

	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		
		String value = (String) sourceFieldValue;
		if(value == null) return null;
		
		String[] values = StringUtils.split(value, "|"); 
		
		PersonType personType = new PersonType();
		
		IDType idType = new IDType();
		idType.setValue(values[0]);
		idType.setSchemeID(values[1]);
		
		personType.setId(idType);

		return personType;
	}

}
