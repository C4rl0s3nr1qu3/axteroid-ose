package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.RegistrationNameType;
import un.unece.uncefact.data.specification.unqualifieddatatypesschemamodule._2.NameType;

import java.util.Arrays;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToListTypeConverter extends StringToNameTypeConverter {
    public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {

        NameType result = (NameType) super.convert(destinationFieldValue,
                sourceFieldValue,
                destinationClass,
                sourceClass);

        if(result==null) return null;

        if ("RegistrationNameType".equals(parameter)) {
            PartyLegalEntityType partyLegalEntityType = new PartyLegalEntityType();
            partyLegalEntityType.setRegistrationName((RegistrationNameType) result);
            return Arrays.asList(partyLegalEntityType);
        }  else {
            PartyNameType partyNameType = new PartyNameType();
            partyNameType.setName((oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NameType) result);
            return Arrays.asList(partyNameType);
        }

    }
}
