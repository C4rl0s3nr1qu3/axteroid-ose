package com.axteroid.ose.server.ubl21.gateway.request.sunat.dozer;

import org.dozer.CustomConverter;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.LocationType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.IDType;

/**
 * User: HNA
 * Date: 17/11/15
 */
public class StringToLocationTypeConverter implements CustomConverter {

	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {

		String value = (String) sourceFieldValue;
		if(value == null) return null;
		
		IDType idType = new IDType();
		idType.setValue(value);

		LocationType locationType = new LocationType();
		locationType.setID(idType);
		
		return locationType;
	}

}
