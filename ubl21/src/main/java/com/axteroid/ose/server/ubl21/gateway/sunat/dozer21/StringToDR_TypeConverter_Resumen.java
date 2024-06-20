package com.axteroid.ose.server.ubl21.gateway.sunat.dozer21;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.AttachmentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.DocumentResponseType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.ExternalReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.ResponseType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.StatusType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.CompanyIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.DescriptionType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.DocumentHashType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.DocumentTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.IssueTimeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.ResponseCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.StatusReasonCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.StatusReasonType;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.rulesejb.rules.UBLListParametroLocal;
import com.axteroid.ose.server.rulesejb.rules.impl.UBLListParametroImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToDR_TypeConverter_Resumen implements CustomConverter {
	private static final Logger log = LoggerFactory.getLogger(StringToDR_TypeConverter_Resumen.class);
    @SuppressWarnings("rawtypes")
	public Object convert(Object destinationFieldValue, Object sourceFieldValue, 
    		Class destinationClass, Class sourceClass) {

		String valor = (String) sourceFieldValue;		
		if(StringUtils.isBlank(valor)) return null;
		  
		log.info("convert ---> "+valor);
		
    	String[] values = StringUtils.split(valor, "|"); 
    	ResponseCodeType responseCodeType = new ResponseCodeType();
    	if(!StringUtils.isBlank(values[0]))
    		responseCodeType.setValue(values[0]);

    	if(values[1]!= null)
    		responseCodeType.setListAgencyName(values[1]);     	

    	DescriptionType descriptionType = new DescriptionType();
    	if(values[2]!= null)
        	descriptionType.setValue(values[2]);

        ResponseType responseType = new ResponseType();
    	responseType.setResponseCode(responseCodeType); 
        responseType.getDescription().add(descriptionType);    
        
        //log.info("1 values[3] = "+values[3]+" - values[5] = "+values[5]);        
        if(!values[3].equals("null") && !values[5].equals("null")) {
        	//log.info("2 values[3] = "+values[3]+" - values[5] = "+values[5]);
        	String[] listReasonCode =  values[3].split(";");        	        	
        	//String[] listReason =  values[5].split(";");
        	//log.info("leng "+listReasonCode.length);
        	int i = 0;
        	UBLListParametroLocal ublListParametroLocal = new UBLListParametroImpl();
        	for(String reasonCode : listReasonCode) {
        		String reason = ublListParametroLocal.bucarParametro(Constantes.SUNAT_PARAMETRO_CODIGO_RETORNO, reasonCode);
        		responseType.getStatus().add(this.updateResponseType(reasonCode, values[4], reason));
        		i++;
        	}
        }
               	               	        	
        DocumentReferenceType documentReferenceType = new DocumentReferenceType();
        if(values[6]!= null){
        	IDType idType = new IDType();
            idType.setValue(values[6]);
        	documentReferenceType.setID(idType);
        }
        DateToXmlGregorianCalendarConverter dgcc = new DateToXmlGregorianCalendarConverter();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");  	
        if(values[7]!= null){        		
        	try {
        		String sfecha = values[7].replace(".", ":");
        		//log.info("sfecha ---> "+sfecha);
        		Date date = dateFormat.parse(sfecha); 
				IssueDateType issueDateType = new IssueDateType();
				issueDateType.setValue(dgcc.convertDateToXMLGC(date));
				documentReferenceType.setIssueDate(issueDateType);
				
	            IssueTimeType issueTimeType = new IssueTimeType();
	            issueTimeType.setValue(dgcc.convertTimeToXMLGC(date));
	            documentReferenceType.setIssueTime(issueTimeType);
	            
        	} catch (Exception e) {
        		StringWriter errors = new StringWriter();	
        		e.printStackTrace(new PrintWriter(errors));
        		log.error("Exception \n"+errors);
        	}
        }
		DocumentTypeCodeType documentTypeCodeType = new DocumentTypeCodeType();
		if(values[8]!= null){
			documentTypeCodeType.setValue(values[8]);
			documentReferenceType.setDocumentTypeCode(documentTypeCodeType);
		}
		DocumentHashType documentHashType = new DocumentHashType();
		if(values[9]!= null)
			documentHashType.setValue(values[9]);

		ExternalReferenceType externalReferenceType = new ExternalReferenceType();
		externalReferenceType.setDocumentHash(documentHashType);
		AttachmentType attachmentType = new AttachmentType();
		attachmentType.setExternalReference(externalReferenceType);			
		documentReferenceType.setAttachment(attachmentType);
						
		CompanyIDType companyIDType1 = new CompanyIDType();
		if(values[10]!= null)
			companyIDType1.setValue(values[10]);

		if(values[11]!= null)
			companyIDType1.setSchemeID(values[11]);

		PartyLegalEntityType partyLegalEntityType1 = new PartyLegalEntityType();
		partyLegalEntityType1.setCompanyID(companyIDType1);
		PartyType partyType1 = new PartyType();
		partyType1.getPartyLegalEntity().add(partyLegalEntityType1);
			
//		CompanyIDType companyIDType2 = new CompanyIDType();
//		if(values[12]!= null) 
//			companyIDType2.setValue(values[12]);
//
//		if(values[13]!= null)
//			companyIDType2.setSchemeID(values[13]);

//		PartyLegalEntityType partyLegalEntityType2 = new PartyLegalEntityType();
//		partyLegalEntityType2.setCompanyID(companyIDType2);
//		PartyType partyType2 = new PartyType();
//		partyType2.getPartyLegalEntity().add(partyLegalEntityType2);
			
    	DocumentResponseType documentResponseType = new DocumentResponseType();
    	documentResponseType.setResponse(responseType);    		  		
    	documentResponseType.getDocumentReference().add(documentReferenceType);
    	documentResponseType.setIssuerParty(partyType1);
//    	documentResponseType.setRecipientParty(partyType2);
  		
    	return documentResponseType;
    }   
    
    private StatusType updateResponseType(String reasonCode, String listURI, String reason) {
		StatusReasonCodeType statusReasonCodeType = new StatusReasonCodeType();
		statusReasonCodeType.setValue(reasonCode);
		statusReasonCodeType.setListURI(listURI);  
		StatusReasonType statusReasonType = new StatusReasonType();
		statusReasonType.setValue(reason);
		StatusType statusType = new StatusType();
		statusType.setStatusReasonCode(statusReasonCodeType);     	
		statusType.getStatusReason().add(statusReasonType);
		return statusType;
    }
    
}
