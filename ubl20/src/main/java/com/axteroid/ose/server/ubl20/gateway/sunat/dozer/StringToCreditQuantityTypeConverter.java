package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CreditedQuantityType;
import un.unece.uncefact.data.specification.unqualifieddatatypesschemamodule._2.QuantityType;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToCreditQuantityTypeConverter extends StringToQuantityTypeConverter {

    @Override
    protected QuantityType getQuantityDestination(Object destination) {
        if (destination == null)
            return new CreditedQuantityType();
        else
            return (CreditedQuantityType) destination;
    }
}
