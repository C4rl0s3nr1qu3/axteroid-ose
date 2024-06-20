package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.SunatCarrierPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.SunatShipmentStageType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.RegistrationNameType;
import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.SunatTransactionType;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToSunatTransactionType implements CustomConverter {
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

        SunatTransactionType result = new SunatTransactionType();

        IDType idType=new IDType();
        idType.setValue(valor);
        result.setIdType(idType);
        return result;
    }
}
