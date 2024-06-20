package com.axteroid.ose.server.ubl21.gateway.request.sunat.dozer;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.ItemIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.IDType;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToItemIdentificationTypeConverter implements CustomConverter {

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        String valueAsString = ((String) source);
        if(StringUtils.isBlank(valueAsString)) return null;
        ItemIdentificationType itemIdentificationType= new ItemIdentificationType();
        IDType idType = new IDType();
        idType.setValue(valueAsString);
        itemIdentificationType.setID(idType);
        return itemIdentificationType;
    }
}
