package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import java.math.BigDecimal;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TotalTransportHandlingUnitQuantityType;

import org.dozer.CustomConverter;

//package com.ebiz.ce.gateway.sunat.dozer;
//
//import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TotalTransportHandlingUnitQuantityType;
//import un.unece.uncefact.data.specification.unqualifieddatatypesschemamodule._2.QuantityType;
//
///**
// * User: HNA
// * Date: 18/11/15
// */
//public class StringToTotalTransportHandlingUnitQuantityTypeConverter extends StringToQuantityTypeConverter{
//
//    @Override
//    protected QuantityType getQuantityDestination(Object destination) {
//        if (destination == null)
//            return new TotalTransportHandlingUnitQuantityType();
//        else
//            return (TotalTransportHandlingUnitQuantityType) destination;
//    }
//}

/**
* User: HNA
* Date: 18/11/15
*/
public class StringToTotalTransportHandlingUnitQuantityTypeConverter implements CustomConverter {

	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {

		BigDecimal value = (BigDecimal) sourceFieldValue;
		if(value == null) return null;
		
		TotalTransportHandlingUnitQuantityType result = new TotalTransportHandlingUnitQuantityType();
		result.setValue(new BigDecimal(value.intValue()));
		
		return result;
	}

	public static void main(String[] args) {
		BigDecimal bigDecimal = new BigDecimal(5000);	
		System.out.println(bigDecimal);
		System.out.println(new BigDecimal(bigDecimal.intValue()));
	}
	
}