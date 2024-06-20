package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import java.math.BigDecimal;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PercentType;

import org.dozer.ConfigurableCustomConverter;

import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.SunatPerceptionPercentType;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.SunatRetentionPercentType;

/**
 * User: HNA
 * Date: 02/12/15
 */
public class StringToPercentTypeConverter implements ConfigurableCustomConverter {

    private String parameter;

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        
    	BigDecimal value = (BigDecimal) source;
    	
        if(value == null) return null;

        PercentType result = null;


        if ("SunatRetentionPercentType".equals(parameter)) {
            result = new SunatRetentionPercentType();
        } else if("SunatPerceptionPercentType".equals(parameter)) {
        	result = new SunatPerceptionPercentType();
        }
        result.setValue(value);

        return result;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
