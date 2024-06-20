package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import org.dozer.ConfigurableCustomConverter;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class FileNameConverter implements ConfigurableCustomConverter {
    private String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {

        return null;
    }

}
