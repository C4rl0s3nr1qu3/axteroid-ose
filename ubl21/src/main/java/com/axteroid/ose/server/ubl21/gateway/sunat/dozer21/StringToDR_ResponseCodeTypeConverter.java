package com.axteroid.ose.server.ubl21.gateway.sunat.dozer21;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.dozer.CustomConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.DocumentResponseType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.ResponseType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.StatusType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.DescriptionType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.ResponseCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.StatusReasonCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.StatusReasonType;

public class StringToDR_ResponseCodeTypeConverter implements CustomConverter {
	//private static final Logger log = Logger.getLogger(StringToDR_ResponseCodeTypeConverter.class);
	private static final Logger log = LoggerFactory.getLogger(StringToDR_ResponseCodeTypeConverter.class);
    @SuppressWarnings("rawtypes")
	public Object convert(Object destinationFieldValue, Object sourceFieldValue, 
    		Class destinationClass, Class sourceClass) {

		String valor = (String) sourceFieldValue;		
		if(StringUtils.isBlank(valor)) return null;
		
		//log.info("21 StringToDR_ResponseCodeTypeConverter -- 1 "+valor);
		  
    	String[] values = StringUtils.split(valor, "|"); 
    	ResponseCodeType responseCodeType = new ResponseCodeType();
    	if(!StringUtils.isBlank(values[0])){
    		responseCodeType.setValue(values[0]);
    		//log.info("values[0] "+values[0]);
    	}
    	if(values[1]!= null){
    		responseCodeType.setListAgencyName(values[1]);     	
    		//log.info("values[1] "+values[1]);
    	}
    	
    	DescriptionType descriptionType = new DescriptionType();
    	if(values[2]!= null){
        	descriptionType.setValue(values[2]);
        	//log.info("values[2] "+values[2]);
    	}
    	
    	ResponseType responseType = new ResponseType();
    	responseType.setResponseCode(responseCodeType); 
    	responseType.getDescription().add(descriptionType); 
    	
    	
        StatusReasonCodeType statusReasonCodeType = new StatusReasonCodeType();
        if(values[3]!= null){
        	statusReasonCodeType.setValue(values[3]);
        	//log.info("values[3] "+values[3]);
        }
        if(values[4]!= null){
        	statusReasonCodeType.setListURI(values[4]);  
        	//log.info("values[4] "+values[4]);
        }
        StatusReasonType statusReasonType = new StatusReasonType();
        if(values[5]!= null){
        	statusReasonType.setValue(values[5]);      
        	//log.info("values[5] "+values[5]);
        }
        StatusType statusType = new StatusType();
        statusType.setStatusReasonCode(statusReasonCodeType);    	
        statusType.getStatusReason().add(statusReasonType);
        //statusType.setStatusReason(Arrays.asList(statusReasonType));
        
        responseType.getStatus().add(statusType);
        
    	DocumentResponseType documentResponseType = new DocumentResponseType();
    	documentResponseType.setResponse(responseType);
    	/*return Arrays.asList(documentResponseType);*/
    	
    	return documentResponseType;
    }

}
