package com.axteroid.ose.server.ubl21.gateway.request.sunat.dozer;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.CustomerAssignedAccountIDType;

/**
 * User: HNA
 * Date: 16/11/15
 * Para implementacion de guias, el valor llega en formato numeroDocumentoRemitente|tipoDocumentoRemitente.
 */
public class StringToCustomerAssignedAccountIDType implements CustomConverter{

	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {

		String value = (String) sourceFieldValue;	
		if(StringUtils.isBlank(value)) return null;	
		
        String[] values = StringUtils.split(value, "|");
        
        CustomerAssignedAccountIDType result = new CustomerAssignedAccountIDType();
        result.setValue(values[0]);
        result.setSchemeID(values[1]);
		
		return result;
	}

}
