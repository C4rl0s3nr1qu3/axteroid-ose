package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import un.unece.uncefact.data.specification.unqualifieddatatypesschemamodule._2.IdentifierType;

import java.util.Arrays;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToListIdentifierTypeConverter extends StringToIdentifierTypeConverter {
    public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {

        IdentifierType result = (IdentifierType) super.convert(destinationFieldValue,
                sourceFieldValue,
                destinationClass,
                sourceClass);

        if(result==null) return null;

        if ("AdditionalAccountIDType".equals(parameter)) {
            return Arrays.asList(result);
        }else if ("CustomerAssignedAccountIDType".equals(parameter)) {
            return Arrays.asList(result);
        }
        return null;
    }
}
