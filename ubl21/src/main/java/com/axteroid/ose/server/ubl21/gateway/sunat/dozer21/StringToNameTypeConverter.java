package com.axteroid.ose.server.ubl21.gateway.sunat.dozer21;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.CityNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.CitySubdivisionNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.RegistrationNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.StreetNameType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_2_1.NameType;

import org.apache.commons.lang.StringUtils;
import org.dozer.ConfigurableCustomConverter;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToNameTypeConverter implements ConfigurableCustomConverter {
    protected String parameter;
    @SuppressWarnings("rawtypes")
	public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {
        String valueAsString = ((String) sourceFieldValue);

        if(StringUtils.isBlank(valueAsString)) return null;

        NameType nameType=null;
        if("StreetNameType".equals(parameter)){
            nameType = new StreetNameType();
        }else if("CitySubdivisionNameType".equals(parameter)){
            nameType = new CitySubdivisionNameType();
        }else if("CityNameType".equals(parameter)){
            nameType = new CityNameType();
        }else if("NameType".equals(parameter)){
            nameType = new oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.NameType();
        }else if("RegistrationNameType".equals(parameter)){
            nameType = new RegistrationNameType();
        }
        nameType.setValue(valueAsString);
        return nameType;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
