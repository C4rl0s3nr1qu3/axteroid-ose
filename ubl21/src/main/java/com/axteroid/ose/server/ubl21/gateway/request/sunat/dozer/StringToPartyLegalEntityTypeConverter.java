package com.axteroid.ose.server.ubl21.gateway.request.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.RegistrationNameType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_2_1.NameType;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToPartyLegalEntityTypeConverter extends StringToNameTypeConverter {
    public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {

        NameType nameType = (NameType) super.convert(destinationFieldValue, sourceFieldValue, destinationClass, sourceClass);      
        if(nameType==null) return null;
        PartyLegalEntityType partyLegalEntityType = new PartyLegalEntityType();
        partyLegalEntityType.setRegistrationName((RegistrationNameType) nameType);
        return partyLegalEntityType;
    }

}
