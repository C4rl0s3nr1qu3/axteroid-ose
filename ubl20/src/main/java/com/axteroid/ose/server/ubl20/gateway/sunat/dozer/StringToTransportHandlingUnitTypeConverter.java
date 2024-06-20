package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TransportEquipmentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TransportHandlingUnitType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;

import org.dozer.CustomConverter;

public class StringToTransportHandlingUnitTypeConverter implements CustomConverter {

	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		
		String value = (String) sourceFieldValue;
		if(value == null) return null;
		
		IDType idType = new IDType();
		idType.setValue(value);
		
		TransportEquipmentType transportEquipmentType = new TransportEquipmentType();
		transportEquipmentType.setID(idType);
		
		TransportHandlingUnitType transportHandlingUnitType = new TransportHandlingUnitType();
		transportHandlingUnitType.getTransportEquipment().add(transportEquipmentType);
		
		return transportHandlingUnitType;
	}

}
