package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DescriptionType;
import org.dozer.CustomConverter;

import java.util.Iterator;
import java.util.List;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class ListToStringTypeConverter implements CustomConverter {

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {

        if (!(source instanceof List)) return null;

        @SuppressWarnings("unchecked")
		List<DescriptionType> descripcionList = (List<DescriptionType>) source;
        StringBuilder sb = new StringBuilder();
        for (DescriptionType descriptionType : descripcionList) {
            sb.append(descriptionType.getValue());
        }

        return sb.toString();
    }
}
