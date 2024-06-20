package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;


import org.apache.commons.net.util.Base64;
import org.dozer.CustomConverter;


public class ByteToStringTypeConverter implements CustomConverter {

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {

        if(source==null) return null;

        return Base64.encodeBase64String((byte[]) source);
    }
}
