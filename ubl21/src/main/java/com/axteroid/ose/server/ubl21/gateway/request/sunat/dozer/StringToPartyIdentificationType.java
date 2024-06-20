package com.axteroid.ose.server.ubl21.gateway.request.sunat.dozer;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.IDType;

/**
 * User: HNA
 * Date: 17/11/15
 */
public class StringToPartyIdentificationType implements CustomConverter {

	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {

		String valor = (String) sourceFieldValue;

        if (valor == null) {

            return null;
        }
        
        PartyType partyType= new PartyType();
        
        String[] values = StringUtils.split(valor, "|");
        IDType idType = new IDType();
        idType.setValue(values[0]);
        idType.setSchemeID(values[1]);
        
        PartyIdentificationType partyIdentificationType= new PartyIdentificationType();
        partyIdentificationType.setID(idType);
        partyType.getPartyIdentification().add(partyIdentificationType);
		
		return partyType;
	}

}
