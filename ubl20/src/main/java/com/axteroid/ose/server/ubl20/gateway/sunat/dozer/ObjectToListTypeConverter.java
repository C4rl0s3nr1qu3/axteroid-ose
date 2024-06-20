package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import org.dozer.CustomConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class ObjectToListTypeConverter implements CustomConverter {

    @SuppressWarnings("unchecked")
	public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        List result = new ArrayList();
        if (source == null) return result;
        try {
            result.add(source);
            return result;
        } catch (Exception ex) {
            return null;
        }

    }
}
