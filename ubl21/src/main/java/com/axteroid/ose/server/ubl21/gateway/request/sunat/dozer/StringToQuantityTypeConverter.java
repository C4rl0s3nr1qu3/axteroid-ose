package com.axteroid.ose.server.ubl21.gateway.request.sunat.dozer;

import org.dozer.ConfigurableCustomConverter;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.QuantityType;

import java.math.BigDecimal;

/**
 * User: RAC
 * Date: 16/02/12
 */
public abstract class StringToQuantityTypeConverter implements ConfigurableCustomConverter {

    private String parameter;

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {

        QuantityType idType = getQuantityDestination(destination);

        if ("value".equals(parameter)) {
            idType.setValue(((BigDecimal) source));
        } else if ("id".equals(parameter)) {
            idType.setUnitCode((String) source);
        }
        return idType;
    }

    protected abstract QuantityType getQuantityDestination(Object destination) ;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
