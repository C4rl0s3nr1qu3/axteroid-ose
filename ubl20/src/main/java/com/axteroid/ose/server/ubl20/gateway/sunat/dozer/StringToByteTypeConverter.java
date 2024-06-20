package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;


import org.apache.commons.net.util.Base64;
import org.dozer.CustomConverter;


public class StringToByteTypeConverter implements CustomConverter {

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {

        if(source==null) return null;

        return Base64.decodeBase64((String) source);
    }
}
