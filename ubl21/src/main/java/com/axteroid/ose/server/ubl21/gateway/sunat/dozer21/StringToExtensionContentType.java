package com.axteroid.ose.server.ubl21.gateway.sunat.dozer21;

import java.io.PrintWriter;
import java.io.StringWriter;

//import org.apache.log4j.Logger;
import org.dozer.CustomConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2_1.ExtensionContentType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2_1.UBLExtensionType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2_1.UBLExtensionsType;

public class StringToExtensionContentType implements CustomConverter {
	//private static final Logger log = Logger.getLogger(StringToExtensionContentType.class);
	private static final Logger log = LoggerFactory.getLogger(StringToExtensionContentType.class);
    @SuppressWarnings("rawtypes")
	public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {
        
    	String value = (String) sourceFieldValue;       
        try {
            
            ExtensionContentType extensionContent = new ExtensionContentType();
            
            UBLExtensionType ublExtension = new UBLExtensionType();
            ublExtension.setExtensionContent(extensionContent);
            
            UBLExtensionsType ublExtensions = new UBLExtensionsType();
            //ublExtensions.getUBLExtension().add(ublExtension);
            
            return ublExtensions;
            
    	}catch(Exception e) {
    		StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));			
			log.error("Exception \n"+errors);  
			
    	}
        return null;

    }
}
