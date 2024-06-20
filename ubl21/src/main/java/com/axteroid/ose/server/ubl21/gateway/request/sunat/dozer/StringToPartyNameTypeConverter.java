package com.axteroid.ose.server.ubl21.gateway.request.sunat.dozer;

import org.dozer.CustomConverter;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.NameType;

/**
 * User: BSANCHEZ <br/>
 * Date: 18/04/16 <br/>
 * FIX:  02
 */
//HNA - 20Jun2016: Metodo modificado
public class StringToPartyNameTypeConverter implements CustomConverter {

	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {

		String valor = (String) sourceFieldValue;

       if (valor == null) {
            return null;
        }

       NameType nameType = new NameType();
       nameType.setValue(valor);
       
       PartyNameType partyNameType= new PartyNameType();
       partyNameType.setName(nameType);
        
       return partyNameType;
	}
}