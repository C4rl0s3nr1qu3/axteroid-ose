package com.axteroid.ose.server.ubl21.gateway.request.sunat.dozer;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.OrderTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_2_1.CodeType;

/**
 * User: HNA
 * Date: 18/11/15
 */
public class StringToOrderTypeCodeType implements CustomConverter {
	private static final Logger LOG = LoggerFactory.getLogger(StringToOrderTypeCodeType.class);

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        String valueAsString = ((String) source);

        if(StringUtils.isBlank(valueAsString)) return null;
        
        String[] values = StringUtils.split(valueAsString, "|");

        CodeType result = null;

       	result = new OrderTypeCodeType();
       	result.setValue(values[0]);
        	
        if(!values[1].equals("null")) {
        	result.setName(values[1]);
        }

        return result;
    }

}
