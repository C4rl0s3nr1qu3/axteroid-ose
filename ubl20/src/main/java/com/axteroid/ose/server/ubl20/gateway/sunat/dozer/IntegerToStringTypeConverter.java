package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import org.dozer.CustomConverter;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class IntegerToStringTypeConverter implements CustomConverter {

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        return source.toString();
    }
}
