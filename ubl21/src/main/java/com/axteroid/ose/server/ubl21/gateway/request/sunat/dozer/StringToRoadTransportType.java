package com.axteroid.ose.server.ubl21.gateway.request.sunat.dozer;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.RoadTransportType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.BrandNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.LicensePlateIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.TransportAuthorizationCodeType;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToRoadTransportType implements CustomConverter {
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
        RoadTransportType result= new RoadTransportType();
        if(values.length == 1) {
        	//HNA - Para el caso Guias de Remision, solo se envia la placa
            LicensePlateIDType license= new LicensePlateIDType();
            license.setValue(values[0]);
            result.setLicensePlateID(license);
        } else {
        	//HNA - Para el caso de Facturas Guia, se envia la placa, constancia y marca
            LicensePlateIDType license= new LicensePlateIDType();
            license.setValue(values[0]);
            TransportAuthorizationCodeType transport=new TransportAuthorizationCodeType();
            transport.setValue(values[1]);
            BrandNameType brandNameType = new BrandNameType();
            brandNameType.setValue(values[2]);
            result.setLicensePlateID(license);
            //result.setTransportAuthorizationCode(transport);
            //result.setBrandName(brandNameType);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("1234".substring(0, 3));
    }
}
