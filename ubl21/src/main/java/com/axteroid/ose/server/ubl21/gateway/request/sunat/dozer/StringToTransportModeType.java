package com.axteroid.ose.server.ubl21.gateway.request.sunat.dozer;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.TransportModeCodeType;

/**
 * User: HNA
 * Date: 29/12/15
 */
public class StringToTransportModeType implements CustomConverter {

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        String valueAsString = ((String) source);
        if(StringUtils.isBlank(valueAsString)) return null;
        TransportModeCodeType transportModeCodeType = new TransportModeCodeType();
        transportModeCodeType.setValue(valueAsString);
        return transportModeCodeType;
    }
}
