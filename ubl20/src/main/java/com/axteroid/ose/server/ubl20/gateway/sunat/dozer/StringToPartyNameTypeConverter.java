package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NameType;

import org.dozer.CustomConverter;

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