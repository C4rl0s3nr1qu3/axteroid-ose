package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.*;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.dozer.ConfigurableCustomConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import un.unece.uncefact.data.specification.unqualifieddatatypesschemamodule._2.IdentifierType;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToIdentifierTypeConverter implements ConfigurableCustomConverter {
	//private static final Logger log = Logger.getLogger(StringToIdentifierTypeConverter.class);
	private static final Logger log = LoggerFactory.getLogger(StringToIdentifierTypeConverter.class);
    protected String parameter;

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {

        if(source==null) return null;

        String valueAsString = source.toString();

        IdentifierType result = null;

        if ("AdditionalAccountIDType".equals(parameter)) {
            result = new AdditionalAccountIDType();
            result.setValue(valueAsString);
        } else if ("CustomerAssignedAccountIDType".equals(parameter)) {
            result = new CustomerAssignedAccountIDType();
            result.setValue(valueAsString);
        } else if ("LineIDType".equals(parameter)) {
            result = new LineIDType();
            result.setValue(valueAsString);
        }else if ("IdentifierType".equals(parameter)) {
            result = new IdentifierType();
            result.setValue(valueAsString);
        }else if ("CustomizationIDType".equals(parameter)) {
            result = new CustomizationIDType();
            result.setValue(valueAsString);
        }else if ("UBLVersionIDType".equals(parameter)) {
            result = new UBLVersionIDType();
            result.setValue(valueAsString);
        } else if ("SunatRetentionDocumentReferenceType".equals(parameter) || "SunatPerceptionDocumentReferenceType".equals(parameter)) {
        	String[] values = StringUtils.split(valueAsString, "|");
        	result = new IDType();        	
        	result.setValue(values[0]);
        	result.setSchemeID(values[1]);
        } else if("RegistrationNationalityIDType".equals(parameter)) {
        	result = new RegistrationNationalityIDType();
        	result.setValue(valueAsString);
        } else if("LicensePlateIDType".equals(parameter)) {
        	result = new LicensePlateIDType();
        	result.setValue(valueAsString);
        } 
        return result;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
