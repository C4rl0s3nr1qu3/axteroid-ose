package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.StatusType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ConditionCodeType;

import org.apache.commons.lang.StringUtils;
import org.dozer.ConfigurableCustomConverter;

/**
 * User: HNA
 * Date: 07/10/15
 */
public class StringToStatusTypeConverter implements ConfigurableCustomConverter {


    private String parameter;

    public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {
        String value = (String)sourceFieldValue;
        if(StringUtils.isBlank(value)) return null;

        String[] values = StringUtils.split(value,"|");
        
        ConditionCodeType conditionCodeType = new ConditionCodeType();
        conditionCodeType.setValue(values[0]);
        
        StatusType statusType = new StatusType();
        statusType.setConditionCode(conditionCodeType);
        
		return statusType;
    }

    public void setParameter(String parameter) {
        this.parameter=parameter;
    }
}
