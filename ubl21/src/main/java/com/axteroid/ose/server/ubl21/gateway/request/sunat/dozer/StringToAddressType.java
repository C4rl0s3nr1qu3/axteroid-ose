package com.axteroid.ose.server.ubl21.gateway.request.sunat.dozer;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.AddressTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.CityNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.CitySubdivisionNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.CountrySubentityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.DistrictType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.IdentificationCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.StreetNameType;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToAddressType implements CustomConverter {
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
        /*      StringUtil.blank(ubigeoDireccionPtoPartida) + "|" +
                StringUtil.blank(direccionCompletaPtoPartida) + "|" +
                StringUtil.blank(urbanizacionPtoPartida) + "|" +
                StringUtil.blank(provinciaPtoPartida) + "|" +
                StringUtil.blank(departamentoPtoPartida) + "|" +
                StringUtil.blank(distritoPtoPartida) + "|" +
                StringUtil.blank(paisPtoPartida)*/
        String[] values = StringUtils.split(valor, "|");
        
        AddressType result = new AddressType();
        
        if(values.length == 2) {
            //HNA - Para el caso Guias de Remision, solo se envian dos valores en el grupo 
            IDType addressId = new IDType();
            addressId.setValue(values[0]);

            StreetNameType addressStreet = new StreetNameType();
            addressStreet.setValue(values[1]);
            
            
            result.setID(addressId);
            result.setStreetName(addressStreet);
        } else if (values.length == 7) {
        	if(!values[0].equals("SV")) {
                IDType addressId = new IDType();
                addressId.setValue(values[0]);
                result.setID(addressId);
        	}

        	if(!values[1].equals("SV")) {
                StreetNameType addressStreet = new StreetNameType();
                addressStreet.setValue(values[1]); 
                result.setStreetName(addressStreet);
        	}
        	
        	if(!values[2].equals("SV")) {
                CitySubdivisionNameType addressCitySubdivision = new CitySubdivisionNameType();
                addressCitySubdivision.setValue(values[2]);   
                result.setCitySubdivisionName(addressCitySubdivision);
        	}
        	
        	if(!values[3].equals("SV")) {
                CityNameType addressCity = new CityNameType();
                addressCity.setValue(values[3]);
                result.setCityName(addressCity);
        	}
        	
        	if(!values[4].equals("SV")) {
                CountrySubentityType addressCountrySubentity = new CountrySubentityType();
                addressCountrySubentity.setValue(values[4]);
                result.setCountrySubentity(addressCountrySubentity);
        	}
        	
        	if(!values[5].equals("SV")) {
                DistrictType addressDistrict = new DistrictType();
                addressDistrict.setValue(values[5]);
                result.setDistrict(addressDistrict);
        	}
        	
        	if(!values[6].equals("SV")) {        		
                CountryType addressCountry = new CountryType();
                IdentificationCodeType addressIdentificationCode = new IdentificationCodeType();
                addressIdentificationCode.setValue(values[6]);
                addressCountry.setIdentificationCode(addressIdentificationCode);
                result.setCountry(addressCountry);                	
        	}
        } else {
        	//HNA - Para el caso Factura Guia - direccion del lugar donde se realiza la operacion,
//          se agrega en algunos casos el tag addressTypeCode al grupo - 255-2015        	
            IDType addressId = new IDType();
            addressId.setValue(values[0]);

            StreetNameType addressStreet = new StreetNameType();
            addressStreet.setValue(values[1]);

            CitySubdivisionNameType addressCitySubdivision = new CitySubdivisionNameType();
            addressCitySubdivision.setValue(values[2]);

            CityNameType addressCity = new CityNameType();
            addressCity.setValue(values[3]);

            CountrySubentityType addressCountrySubentity = new CountrySubentityType();
            addressCountrySubentity.setValue(values[4]);

            DistrictType addressDistrict = new DistrictType();
            addressDistrict.setValue(values[5]);

            CountryType addressCountry = new CountryType();
            IdentificationCodeType addressIdentificationCode = new IdentificationCodeType();
            addressIdentificationCode.setValue(values[6]);
            addressCountry.setIdentificationCode(addressIdentificationCode);
            
            AddressTypeCodeType addressTypeCode = new AddressTypeCodeType();
            addressTypeCode.setValue(values[7]);

            result.setID(addressId);
            result.setStreetName(addressStreet);
            result.setCitySubdivisionName(addressCitySubdivision);
            result.setCityName(addressCity);
            result.setCountrySubentity(addressCountrySubentity);
            result.setCountry(addressCountry);
            result.setDistrict(addressDistrict);
            result.setAddressTypeCode(addressTypeCode);
        }
        
        return result;
    }

    public static void main(String[] args) {
        System.out.println("1234".substring(0, 3));
    }
}
