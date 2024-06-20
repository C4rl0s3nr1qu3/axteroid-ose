package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToIntegerTypeConverter implements CustomConverter {

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        String valueAsString = ((String) source);
        if (StringUtils.isBlank(valueAsString)) return null;
        try {
            return new Integer(valueAsString.trim());
        } catch (Exception ex) {
            return null;
        }

    }
}
