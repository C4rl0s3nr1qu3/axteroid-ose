package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import org.dozer.ConfigurableCustomConverter;
import un.unece.uncefact.data.specification.unqualifieddatatypesschemamodule._2.QuantityType;

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
