package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.RoadTransportType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.LicensePlateIDType;

import org.dozer.CustomConverter;

/**
 * User: HNA
 * Date: 30/10/15
 */
public class StringToSunatCostsType implements CustomConverter {
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
        
//HNA - 30Oct2015: Se dejan comentados los otros campos en caso sunat los solicite posteriormente        
        /**
         *  StringUtil.blank(placaVehiculo) + "|" +
            StringUtil.blank(numeroConstanciaVehiculo) + "|" +
            StringUtil.blank(marcaVehiculo)
         */
        
//        String[] values = StringUtils.split(valor, "|");
        RoadTransportType result= new RoadTransportType();
        LicensePlateIDType license= new LicensePlateIDType();
        license.setValue(valor);
//        TransportAuthorizationCodeType transport=new TransportAuthorizationCodeType();
//        transport.setValue(values[1]);
//        BrandNameType brandNameType = new BrandNameType();
//        brandNameType.setValue(values[2]);
        result.setLicensePlateID(license);
//        result.setTransportAuthorizationCode(transport);
//        result.setBrandName(brandNameType);

        return result;
    }

    public static void main(String[] args) {
        System.out.println("1234".substring(0, 3));
    }
}
