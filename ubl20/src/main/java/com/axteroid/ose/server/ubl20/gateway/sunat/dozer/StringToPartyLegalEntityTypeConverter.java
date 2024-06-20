package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import org.apache.commons.lang.StringUtils;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CompanyIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.RegistrationNameType;
import un.unece.uncefact.data.specification.unqualifieddatatypesschemamodule._2.NameType;

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
