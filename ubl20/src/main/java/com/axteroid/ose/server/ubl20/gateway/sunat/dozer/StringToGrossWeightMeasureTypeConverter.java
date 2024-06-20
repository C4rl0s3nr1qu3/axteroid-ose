package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import java.math.BigDecimal;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.GrossWeightMeasureType;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

/**
 * User: HNA
 * Date: 29/12/15
 */
public class StringToGrossWeightMeasureTypeConverter implements CustomConverter {
	
//	@Override
//    protected MeasureType getMeasureDestination(Object destination) {
//        if (destination == null)
//            return new GrossWeightMeasureType();
//        else
//            return (GrossWeightMeasureType) destination;
//    }
	
	
	public Object convert(Object destinationFieldValue, Object sourceFieldValue,
            Class destinationClass, Class sourceClass) {
			String valor = (String) sourceFieldValue;

			if (valor == null) {

				return null;
			}

			String[] values = StringUtils.split(valor, "|");

			GrossWeightMeasureType result = new GrossWeightMeasureType();
			result.setUnitCode(values[0]);
			result.setValue(new BigDecimal(values[1]));
			return result;
	}
}
