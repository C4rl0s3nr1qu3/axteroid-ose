package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DeliveredQuantityType;
import un.unece.uncefact.data.specification.unqualifieddatatypesschemamodule._2.QuantityType;

/**
 * User: HNA
 * Date: 13/11/15
 */
public class StringToDespatchQuantityTypeConverter extends StringToQuantityTypeConverter{

    @Override
    protected QuantityType getQuantityDestination(Object destination) {
        if (destination == null)
            return new DeliveredQuantityType();
        else
            return (DeliveredQuantityType) destination;
    }
}
