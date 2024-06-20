package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DriverPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.RoadTransportType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.BrandNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.LicensePlateIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TransportAuthorizationCodeType;
import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToDriverPartyType implements CustomConverter {
    /**
     * Invoice/ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/sac:AdditionalInformation/sac:AdditionalMonetaryTotal/cbc:ID (Código de tipo de monto -  Católogo No. 14)
     * Invoice/ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/sac:AdditionalInformation/sac:AdditionalMonetaryTotal/cbc:PayableAmount (Monto)
     *
     * @param destinationFieldValue
     * @param sourceFieldValue
     * @param destinationClass
     * @param sourceClass
     * @return
     */
    public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {
        String valor = (String) sourceFieldValue;

        if (valor == null) {

            return null;
        }

        PartyType partyType= new PartyType();

        IDType idType = new IDType();
        idType.setValue(valor);
        PartyIdentificationType partyIdentificationType= new PartyIdentificationType();
        partyIdentificationType.setID(idType);
        partyType.getPartyIdentification().add(partyIdentificationType);

        DriverPartyType result= new DriverPartyType();
        result.setParty(partyType);
        return result;
    }

    public static void main(String[] args) {
        System.out.println("1234".substring(0, 3));
    }
}
