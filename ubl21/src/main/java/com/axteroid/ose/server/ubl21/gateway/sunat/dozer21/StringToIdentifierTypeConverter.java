package com.axteroid.ose.server.ubl21.gateway.sunat.dozer21;

//import org.apache.log4j.Logger;
import org.dozer.ConfigurableCustomConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.CustomizationIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.UBLVersionIDType;

public class StringToIdentifierTypeConverter implements ConfigurableCustomConverter {
	//private static final Logger log = Logger.getLogger(StringToIdentifierTypeConverter.class);
	private static final Logger log = LoggerFactory.getLogger(StringToIdentifierTypeConverter.class);
	protected String parameter;

    @SuppressWarnings("rawtypes")
	public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {

        if(source==null) return null;

        String valueAsString = source.toString();
        //log.info("21 valueAsString --> "+valueAsString);
        //log.info("21 parameter --> "+parameter);

        if ("CustomizationIDType".equals(parameter)) {
        	CustomizationIDType result = new CustomizationIDType();
            result.setValue(valueAsString);
            return result;
        }else if ("UBLVersionIDType".equals(parameter)) {
        	UBLVersionIDType result = new UBLVersionIDType();
            result.setValue(valueAsString);
            return result;
        } 
        return null;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
