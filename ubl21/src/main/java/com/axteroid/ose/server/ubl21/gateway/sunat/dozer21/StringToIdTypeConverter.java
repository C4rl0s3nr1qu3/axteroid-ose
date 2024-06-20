package com.axteroid.ose.server.ubl21.gateway.sunat.dozer21;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.dozer.CustomConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.IDType;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToIdTypeConverter implements CustomConverter {
	//private static final Logger log = Logger.getLogger(StringToIdTypeConverter.class);
	private static final Logger log = LoggerFactory.getLogger(StringToIdTypeConverter.class);
    @SuppressWarnings("rawtypes")
	public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        String valueAsString = ((String) source);
        //log.info("21 StringToIdentifierTypeConverter --> "+valueAsString);
        if(StringUtils.isBlank(valueAsString)) return null;
        IDType idType = new IDType();
        idType.setValue(valueAsString);
        return idType;
    }
}
