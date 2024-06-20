package com.axteroid.ose.server.ubl21.gateway.request.sunat.dozer;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.dozer.ConfigurableCustomConverter;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.GrossWeightMeasureType;
//import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.MeasureType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.NetWeightMeasureType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_2_1.MeasureType;

/**
 * User: HNA
 * Date: 03/11/15
 */
public class StringToMeasureTypeConverter implements ConfigurableCustomConverter {
    protected String parameter;
    public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {
        String valueAsString = ((String) sourceFieldValue);

        if(StringUtils.isBlank(valueAsString)) return null;
        
        String[] values = StringUtils.split(valueAsString, "|");
        
        MeasureType result = null;
       
        if("GrossWeightMeasureType".equals(parameter)){
        	result = new GrossWeightMeasureType();
			result.setUnitCode(values[0]);
			result.setValue(new BigDecimal(values[1]));
        } else if("NetWeightMeasureType".equals(parameter)) {	
        	result = new NetWeightMeasureType();
			result.setUnitCode(values[0]);
			result.setValue(new BigDecimal(values[1]));
        } else {
            result = new MeasureType();
			result.setUnitCode(values[0]);
			result.setValue(new BigDecimal(values[1]));
        }
		return result;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
