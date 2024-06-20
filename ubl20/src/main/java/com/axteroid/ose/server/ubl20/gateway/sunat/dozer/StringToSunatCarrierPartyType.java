package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.RoadTransportType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.SunatCarrierPartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.*;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToSunatCarrierPartyType implements CustomConverter {
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
        /**
         *  StringUtil.blank(placaVehiculo) + "|" +
            StringUtil.blank(numeroConstanciaVehiculo) + "|" +
            StringUtil.blank(marcaVehiculo)
         */
        String[] values = StringUtils.split(valor, "|");

        SunatCarrierPartyType result= new SunatCarrierPartyType();
        CustomerAssignedAccountIDType customerAssignedAccountIDType= new CustomerAssignedAccountIDType();
        customerAssignedAccountIDType.setValue(values[0]);
        result.setCustomerAssignedAccountID(customerAssignedAccountIDType);

        AdditionalAccountIDType additionalAccountIDType= new AdditionalAccountIDType();
        additionalAccountIDType.setValue(values[1]);
        result.setAdditionalAccountID(additionalAccountIDType);
        
        //HNA - 01Oct2015: La razon social transportista pasa al grupo SUNATCarrierParty en vez de SUNATShipmentStage
        PartyType partyType=new PartyType();
        RegistrationNameType registrationNameType= new RegistrationNameType();
        registrationNameType.setValue(values[2]);
        PartyLegalEntityType partyLegalEntityType=new PartyLegalEntityType();
        partyLegalEntityType.setRegistrationName(registrationNameType);
        partyType.getPartyLegalEntity().add(partyLegalEntityType);
        result.setParty(partyType);
        
        return result;
    }

    public static void main(String[] args) {
        System.out.println("1234".substring(0, 3));
    }
}
