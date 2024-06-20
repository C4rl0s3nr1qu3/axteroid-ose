package com.axteroid.ose.server.ubl21.gateway.request.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.DeliveredQuantityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.QuantityType;



/**
 * User: HNA
 * Date: 13/11/15
 */
public class StringToDespatchQuantityTypeConverter extends StringToQuantityTypeConverter{

    @Override
    protected QuantityType getQuantityDestination(Object destination) {
      /*  if (destination == null)
            return new DeliveredQuantityType();
        else
            return (DeliveredQuantityType) destination;*/
    	return null;
    }
}
