package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ValueType;
import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.AdditionalPropertyType;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToAdditionalPropertyTypeConverter implements CustomConverter {
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
        String[] values = StringUtils.split(valor, "|");

        AdditionalPropertyType additionalPropertyType = new AdditionalPropertyType();
        IDType idType = new IDType();

        idType.setValue(values[0]);
        additionalPropertyType.setID(idType);
        ValueType valueType = new ValueType();
        if ("1000".equals(values[0]) && values[1].length() > 100) {
            valueType.setValue(values[1].substring(0, 100));
        } else {
            valueType.setValue(values[1]);
        }

        additionalPropertyType.setValue(valueType);
        if (values.length == 3) {
            NameType nameType = new NameType();
            nameType.setValue(values[2]);
            additionalPropertyType.setName(nameType);
        }
        return additionalPropertyType;
    }

    public static void main(String[] args) {
        System.out.println("1234".substring(0,3));
    }
}
