package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IdentificationCodeType;
import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToCountryTypeConverter implements CustomConverter {

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        String valueAsString = ((String) source);

        if(StringUtils.isBlank(valueAsString)) return null;
        CountryType countryType = new CountryType();
        IdentificationCodeType identificationCodeType = new IdentificationCodeType();
        identificationCodeType.setValue(valueAsString);
        countryType.setIdentificationCode(identificationCodeType);
        return countryType;
    }
}
