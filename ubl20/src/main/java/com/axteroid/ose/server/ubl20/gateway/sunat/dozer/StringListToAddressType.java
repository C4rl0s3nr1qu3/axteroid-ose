package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import java.util.ArrayList;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CityNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CitySubdivisionNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CountrySubentityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DistrictType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IdentificationCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.StreetNameType;

import org.dozer.CustomConverter;

/**
 * User: HNA
 * Date: 21/06/16
 */
public class StringListToAddressType implements CustomConverter {
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
        @SuppressWarnings("unchecked")
		ArrayList<String> valor = (ArrayList<String>) sourceFieldValue;

        if (valor == null) {

            return null;
        }
        
        AddressType result = new AddressType();
        
        if(valor.get(0)!=null) {
                IDType addressId = new IDType();
                addressId.setValue(valor.get(0));
                result.setID(addressId);
        }

        if(valor.get(1)!=null) {
                StreetNameType addressStreet = new StreetNameType();
                addressStreet.setValue(valor.get(1)); 
                result.setStreetName(addressStreet);
        }
        	
        if(valor.get(2)!=null) {
                CitySubdivisionNameType addressCitySubdivision = new CitySubdivisionNameType();
                addressCitySubdivision.setValue(valor.get(2));   
                result.setCitySubdivisionName(addressCitySubdivision);
        }
        	
        if(valor.get(3)!=null) {
                CityNameType addressCity = new CityNameType();
                addressCity.setValue(valor.get(3));
                result.setCityName(addressCity);
        }
        
        if(valor.get(4)!=null) {
                CountrySubentityType addressCountrySubentity = new CountrySubentityType();
                addressCountrySubentity.setValue(valor.get(4));
                result.setCountrySubentity(addressCountrySubentity);
        }
        	
        if(valor.get(5)!=null) {
                DistrictType addressDistrict = new DistrictType();
                addressDistrict.setValue(valor.get(5));
                result.setDistrict(addressDistrict);
        }
        	
        if(valor.get(6)!=null) {        		
                CountryType addressCountry = new CountryType();
                IdentificationCodeType addressIdentificationCode = new IdentificationCodeType();
                addressIdentificationCode.setValue(valor.get(6));
                addressCountry.setIdentificationCode(addressIdentificationCode);
                result.setCountry(addressCountry);                	
        }
        
        return result;
    }

    public static void main(String[] args) {
        System.out.println("1234".substring(0, 3));
    }
}
