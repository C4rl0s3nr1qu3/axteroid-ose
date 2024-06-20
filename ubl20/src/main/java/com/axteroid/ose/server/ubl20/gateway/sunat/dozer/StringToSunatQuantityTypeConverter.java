package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import java.math.BigDecimal;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.QuantityType;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

/**
 * User: HNA
 * Date: 30/10/15
 */
public class StringToSunatQuantityTypeConverter implements CustomConverter {
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
        QuantityType result = new QuantityType();
        result.setUnitCode(values[0]);
        result.setValue(new BigDecimal(values[1]));

        return result;
    }
}
