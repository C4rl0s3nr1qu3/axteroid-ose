package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DescriptionType;

import org.dozer.CustomConverter;

/**
 * User: HNA
 * Date: 30/10/15
 */
	
public class StringToDescriptionTypeConverter implements CustomConverter{
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
	        
        DescriptionType result = new DescriptionType();
        result.setValue(valor);
        return result;

    }

	public static void main(String[] args) {
	       System.out.println("1234".substring(0, 3));
	}
}

