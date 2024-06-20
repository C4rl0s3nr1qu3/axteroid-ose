package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.MaritimeTransportType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.VesselIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.VesselNameType;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

/**
 * User: HNA
 * Date: 30/10/15
 */
public class StringToMaritimeTransportType implements CustomConverter {

    public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {
        String valueAsString = ((String) sourceFieldValue);

        if(StringUtils.isBlank(valueAsString)) return null;
        
        String[] values = StringUtils.split(valueAsString, "|");

        MaritimeTransportType maritimeTransport=new MaritimeTransportType(); 
        
        if(!StringUtils.isBlank(values[0])){
        	VesselIDType vesselIDType = new VesselIDType();
        	vesselIDType.setValue(values[0]);
            maritimeTransport.setVesselID(vesselIDType);
        } 
        	
        if(!StringUtils.isBlank(values[1])){
        	VesselNameType vesselNameType = new VesselNameType();
        	vesselNameType.setValue(values[1]);
        	maritimeTransport.setVesselName(vesselNameType);
        }

        return maritimeTransport;
    }

}
