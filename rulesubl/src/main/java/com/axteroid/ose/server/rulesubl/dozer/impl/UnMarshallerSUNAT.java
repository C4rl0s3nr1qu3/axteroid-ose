package com.axteroid.ose.server.rulesubl.dozer.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.tools.ubltype.TypeAddress;
import com.axteroid.ose.server.tools.ubltype.TypeOrderReference;
import com.axteroid.ose.server.tools.ubltype.TypeParty;
import com.axteroid.ose.server.tools.ubltype.TypePayment;
import com.axteroid.ose.server.tools.ubltype.TypeTaxCategory;
import com.axteroid.ose.server.tools.ubltype.TypeTaxSubtotal;
import com.axteroid.ose.server.tools.util.StringUtil;
import com.axteroid.ose.server.ubl20.dozer.DTOMapperServiceDozerImpl;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import oasis.names.specification.ubl.schema.xsd.IResumenSunat;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.SunatPerceptionDocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.SunatRetentionDocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxCategoryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxSchemeType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxSubtotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DespatchLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.OrderReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PaymentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.ExtensionContentType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.UBLExtensionType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.UBLExtensionsType;
import oasis.names.specification.ubl.schema.xsd.despatchadvice_2.DespatchAdviceType;
import sunat.names.specification.ubl.peru.schema.xsd.perception_1.PerceptionType;
import sunat.names.specification.ubl.peru.schema.xsd.retention_1.RetentionType;
import sunat.names.specification.ubl.peru.schema.xsd.summarydocuments_1.SummaryDocumentsType;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.AdditionalInformationType;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.AdditionalPropertyType;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.SummaryDocumentsLineType;

public class UnMarshallerSUNAT {	
	private static final Logger log = LoggerFactory.getLogger(UnMarshallerSUNAT.class);
	DTOMapperServiceDozerImpl dtoMapperService = new DTOMapperServiceDozerImpl();
	
	public Object unmarshalResumenSunat(Object resumenSunat) {
		try{
			List<EResumenDocumentoItem> itemsResumen=null;
			EResumenDocumento result = dtoMapperService.map(resumenSunat, EResumenDocumento.class);                
			EReversionDocumento result2=null; 
			String resulttipoDocumento = "";
			if(result.getResumenId()!=null && result.getResumenId().length()>1)				
				resulttipoDocumento=result.getResumenId().toString().substring(0,2);
			if(resulttipoDocumento != null && resulttipoDocumento.equals(Constantes.SUNAT_REVERSION)){
				result2=dtoMapperService.map(resumenSunat, EReversionDocumento.class); 
			}else
				itemsResumen = result.getItems();       
			
			if (resumenSunat instanceof SummaryDocumentsType) {
				// RC
				SummaryDocumentsType summaryDocumentsType = (SummaryDocumentsType) resumenSunat;
				List<SummaryDocumentsLineType> items = summaryDocumentsType.getSummaryDocumentsLine();
				if (items != null) {
					for (final SummaryDocumentsLineType lineType : items) {
						Collection itemList = Collections2.filter(itemsResumen, new Predicate<EResumenDocumentoItem>() {
							public boolean apply(@NotNull EResumenDocumentoItem eResumenDocumentoItem) {
								return lineType.getLineID().getValue().equals(eResumenDocumentoItem.getNumeroFila() + "");
							}
						});
						EResumenDocumentoItem itemResumen = (EResumenDocumentoItem) itemList.toArray()[0];
						if(lineType.getLineID() != null)
							itemResumen.setLineID(lineType.getLineID().getValue());
						if(lineType.getDocumentTypeCode() != null)
							itemResumen.setDocumentTypeCode(lineType.getDocumentTypeCode().getValue());										
						List<TypePayment> listTypePayment = new ArrayList<TypePayment>();
						List<PaymentType> billingPayment = lineType.getBillingPayment();
						for (PaymentType paymentType : billingPayment) {
							if(paymentType.getInstructionID()!=null) {					
								if ("01".equals(paymentType.getInstructionID().getValue())) {
									itemResumen.setTotalValorVentaOpGravadasConIgv(paymentType.getPaidAmount().getValue());
									itemResumen.setTipoMoneda(paymentType.getPaidAmount().getCurrencyID().value());
								} else if ("02".equals(paymentType.getInstructionID().getValue())) {
									itemResumen.setTotalValorVentaOpExoneradasIgv(paymentType.getPaidAmount().getValue());
								} else if ("03".equals(paymentType.getInstructionID().getValue())) {
									itemResumen.setTotalValorVentaOpInafectasIgv(paymentType.getPaidAmount().getValue());
								} else if ("04".equals(paymentType.getInstructionID().getValue())) {
									itemResumen.setTotalValorVentaExportacion(paymentType.getPaidAmount().getValue());
								} else if ("05".equals(paymentType.getInstructionID().getValue())) {
									itemResumen.setTotalValorVentaOpGratuitas(paymentType.getPaidAmount().getValue());
								}
							}
		                    TypePayment typePayment = new TypePayment();
		                    if(paymentType.getPaidAmount()!=null)
		                    	typePayment.setPaidAmount(paymentType.getPaidAmount().getValue());
		                    if(paymentType.getInstructionID()!=null)
		                    	typePayment.setInstructionID(paymentType.getInstructionID().getValue());
		                    listTypePayment.add(typePayment);
		                }
		                itemResumen.setListTypePayment(listTypePayment);
		                
		                List<TypeTaxSubtotal> listTypeTaxSubtotal = new ArrayList<TypeTaxSubtotal>();
		                List<TaxTotalType> taxtTotalList = lineType.getTaxTotal();
		                for (TaxTotalType taxtTotalItem : taxtTotalList) {
		                	if(taxtTotalItem.getTaxSubtotal().get(0).getTaxCategory().getTaxScheme().getID()!=null) {
		                		String codigoTributo = taxtTotalItem.getTaxSubtotal().get(0).getTaxCategory().getTaxScheme().getID().getValue();
		                		if (Constantes.SUNAT_CODIGO_TRIBUTO_IGV.equals(codigoTributo)) {
		                			itemResumen.setTotalIgv(taxtTotalItem.getTaxSubtotal().get(0).getTaxAmount().getValue());
		                		} else if (Constantes.SUNAT_CODIGO_TRIBUTO_ISC.equals(codigoTributo)) {
		                			itemResumen.setTotalIsc(taxtTotalItem.getTaxSubtotal().get(0).getTaxAmount().getValue());
		                		} else if (Constantes.SUNAT_CODIGO_OTROS_CODIGO.equals(codigoTributo)) {
		                			itemResumen.setTotalOtrosTributos(taxtTotalItem.getTaxSubtotal().get(0).getTaxAmount().getValue());
		                		}
		                	}
		                    for (TaxSubtotalType taxSubtotalItem : taxtTotalItem.getTaxSubtotal()){
		                    	TypeTaxSubtotal typeTaxSubtotal = new TypeTaxSubtotal();
		                    	if(taxSubtotalItem.getTaxAmount()!=null)
		                    		typeTaxSubtotal.setTaxAmount(taxSubtotalItem.getTaxAmount().getValue());
		                    	TypeTaxCategory typeTaxCategory = new TypeTaxCategory();
		                    	TaxCategoryType taxCategory = taxSubtotalItem.getTaxCategory();
		                    	if(taxCategory.getTaxExemptionReasonCode()!=null)
		                    		typeTaxCategory.setTaxExemptionReasonCode(taxCategory.getTaxExemptionReasonCode().getValue());
		                    	if(taxCategory.getTierRange()!=null)
		                    		typeTaxCategory.setTierRange(taxCategory.getTierRange().getValue());
		                    	TaxSchemeType taxScheme = taxCategory.getTaxScheme();
		                    	if(taxScheme.getID()!=null)
		                    		typeTaxCategory.setTaxSchemeId(taxScheme.getID().getValue());
		                    	if(taxScheme.getTaxTypeCode()!=null)
		                    		typeTaxCategory.setTaxSchemeTaxTypeCode(taxScheme.getTaxTypeCode().getValue());
		                    	typeTaxSubtotal.setTypeTaxCategory(typeTaxCategory);
		                    	listTypeTaxSubtotal.add(typeTaxSubtotal);
		                    }
		                }
		                itemResumen.setListTypeTaxSubtotal(listTypeTaxSubtotal);
					}  
	            }
	        }
	        if(resulttipoDocumento.equals(Constantes.SUNAT_REVERSION)){
	        	completarResumenReversionCabeceraInformacionAdicional((IResumenSunat) resumenSunat, result2);
	        	result2.setReversionTipo(resulttipoDocumento);
	        	return (EReversionDocumento)result2;
	        }else{
	        	 completarResumenCabeceraInformacionAdicional((IResumenSunat) resumenSunat, result);
	        	 result.setResumenTipo(resulttipoDocumento);
	             return (EResumenDocumento)result;
	        }
        } catch (Exception e) {
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("unmarshalResumenSunat Exception \n"+errors);
        } 
		return null;
    }
	
	private void completarResumenReversionCabeceraInformacionAdicional(IResumenSunat documentoSunat, EReversionDocumento documento) {
	    try {    
	    	UBLExtensionsType ublExtensionsType = documentoSunat.getUBLExtensions();
	        if (ublExtensionsType == null) return;
	        List<UBLExtensionType> ublList = ublExtensionsType.getUBLExtension();
	        if (ublList == null) return;
	        BeanWrapper beanWrapper = new BeanWrapperImpl(documento);
	        for (UBLExtensionType ublExtensionType : ublList) {
	            ExtensionContentType extensionContentType = ublExtensionType.getExtensionContent();
	            if (!(extensionContentType.getAny() instanceof AdditionalInformationType)) continue;
	            AdditionalInformationType additionalInformationType = (AdditionalInformationType) extensionContentType.getAny();
	            if (ublExtensionType.getID().getValue().equals("EBIZ")) {
	                completarPropiedadesAdicionalesResumen(additionalInformationType, beanWrapper);
	            }
	        }
        } catch (Exception e) {
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("completarResumenReversionCabeceraInformacionAdicional Exception \n"+errors);
        } 
	}
    
	private void completarResumenCabeceraInformacionAdicional(IResumenSunat documentoSunat, EResumenDocumento documento) {
        try {
        	UBLExtensionsType ublExtensionsType = documentoSunat.getUBLExtensions();
        	if (ublExtensionsType == null) return;        
        	List<UBLExtensionType> ublList = ublExtensionsType.getUBLExtension();
        	if (ublList == null) return; 
        	BeanWrapper beanWrapper = new BeanWrapperImpl(documento);
        	for (UBLExtensionType ublExtensionType : ublList) {
        		ExtensionContentType extensionContentType = ublExtensionType.getExtensionContent();
        		if (extensionContentType == null) continue; 
        		if (extensionContentType.getAny() == null) continue;
        		if (!(extensionContentType.getAny() instanceof AdditionalInformationType)) continue;
        		AdditionalInformationType additionalInformationType = (AdditionalInformationType) extensionContentType.getAny();
        		if (additionalInformationType == null) continue; 
        		if (ublExtensionType.getID() == null) continue;
        		if (ublExtensionType.getID().getValue() == null) continue;
        		if (ublExtensionType.getID().getValue().equals("EBIZ")) {
        			completarPropiedadesAdicionalesResumen(additionalInformationType, beanWrapper);
        		}
        	}
        } catch (Exception e) {
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("completarResumenCabeceraInformacionAdicional Exception \n"+errors);
        } 
    }

    private void completarPropiedadesAdicionalesResumen(AdditionalInformationType additionalInformationType, BeanWrapper beanWrapper) {
        try {
        	String correoEmisor = "vacioEmisor@example.com"; // 9616
        	List<AdditionalPropertyType> propertyList = additionalInformationType.getAdditionalProperty();
        	if (propertyList == null) return;
        	for (AdditionalPropertyType propertyType : propertyList) {
        		try {
        			if ("9616".equals(propertyType.getID().getValue())
        					&& StringUtils.isNotBlank(propertyType.getValue().getValue())
        					&& StringUtil.isEmail(propertyType.getValue().getValue())) {
        				correoEmisor = propertyType.getValue().getValue();
        			}
        		} catch (Exception e) {
        			log.error("error al validar email ", e);
        		}
        	}
        	beanWrapper.setPropertyValue("correoEmisor", correoEmisor);
        } catch (Exception e) {
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("completarPropiedadesAdicionalesResumen Exception \n"+errors);
        } 
    }
    
    public ERetencionDocumento unmarshalRetencionSunat(Object retencionSunat) {
    	try{
    		RetentionType retentionType = (RetentionType) retencionSunat;
    		ERetencionDocumento documento = dtoMapperService.map(retentionType, ERetencionDocumento.class);   	
    		String correoAdquiriente = "vacioAdquiriente@example.com";
    		String correoEmisor = "vacioEmisor@example.com";	    	
    		documento.setTipoDocumento(Constantes.SUNAT_RETENCION);
    		documento.setCorreoEmisor(correoEmisor);
    		documento.setCorreoAdquiriente(correoAdquiriente);
    		List<SunatRetentionDocumentReferenceType> items = retentionType.getSunatRetentionDocumentReference();
    		List<ERetencionDocumentoItem> itemList = new ArrayList<ERetencionDocumentoItem>();   		
    		if (items != null) { 
    			int numeroOrden = 0;
	    		for (SunatRetentionDocumentReferenceType ir : items) {
	    			numeroOrden++;
	    			ERetencionDocumentoItem item = dtoMapperService.map(ir, ERetencionDocumentoItem.class);
	    			item.setNumeroOrdenItem(String.valueOf(numeroOrden));    		
	    			itemList.add(item);
	    		}   	
	    		documento.setItems(itemList);    	
    		}
    		PartyType agentType = retentionType.getAgentParty();
    		AddressType addressType_A = agentType.getPostalAddress();
    		TypeParty typeAgent = new TypeParty();
    		TypeAddress typeAddress_A = new TypeAddress();
    		if(addressType_A!=null){    			    			
    			if(addressType_A.getID()!=null)
    				typeAddress_A.setId(addressType_A.getID().getValue());    						
    		}
    		typeAgent.setPostalAddress(typeAddress_A);    
    		documento.setAgentParty(typeAgent);
    		
    		PartyType receiverType = retentionType.getReceiverParty();
    		AddressType addressType_R = receiverType.getPostalAddress();
			TypeParty typeReceiver = new TypeParty();
			TypeAddress typeAddress_R = new TypeAddress();
    		if(addressType_R!=null){
    			if(addressType_R.getID()!=null)
    				typeAddress_R.setId(addressType_R.getID().getValue());
    		}
			typeReceiver.setPostalAddress(typeAddress_R);
			documento.setReceiverParty(typeReceiver); 			
    		return documento;
        } catch (Exception e) {
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("unmarshalRetencionSunat Exception \n"+errors);
        } 
    	return null;
    }
    
    public EPercepcionDocumento unmarshalPercepcionSunat(Object percepcionSunat) {
    	try {
    		PerceptionType perceptionType = (PerceptionType) percepcionSunat;
    		EPercepcionDocumento documento = dtoMapperService.map(perceptionType, EPercepcionDocumento.class);   	
    		String correoAdquiriente = "vacioAdquiriente@example.com";
    		String correoEmisor = "vacioEmisor@example.com";    	
    		documento.setTipoDocumento(Constantes.SUNAT_PERCEPCION);
    		documento.setCorreoEmisor(correoEmisor);
    		documento.setCorreoAdquiriente(correoAdquiriente);
    		List<SunatPerceptionDocumentReferenceType> items = perceptionType.getSunatPerceptionDocumentReference();
    		List<EPercepcionDocumentoItem> itemList = new ArrayList<EPercepcionDocumentoItem>();
    		if (items != null) {
	    		int numeroOrden = 0;
	    		for (SunatPerceptionDocumentReferenceType ir : items) {
	    			numeroOrden++;
	    			EPercepcionDocumentoItem item = dtoMapperService.map(ir, EPercepcionDocumentoItem.class);
	    			item.setTipoDocumentoRelacionado(ir.getId().getSchemeID());
	    			item.setNumeroDocumentoRelacionado(ir.getId().getValue());
	    			item.setFechaEmisionDocumentoRelacionado(ir.getIssueDate().getValue());
	    			item.setNumeroOrdenItem(String.valueOf(numeroOrden));    			
	    			item.setImporteTotalDocumentoRelacionado(new BigDecimal(0));
	    			if(ir.getTotalInvoiceAmount()!=null)    			
	    				item.setImporteTotalDocumentoRelacionado(ir.getTotalInvoiceAmount().getValue());
	    			itemList.add(item);
	    		}   	
	    		documento.setItems(itemList);    
    		}
    		PartyType agentType = perceptionType.getAgentParty();
    		AddressType addressType_A = agentType.getPostalAddress();
    		TypeParty typeAgent = new TypeParty();
    		TypeAddress typeAddress_A = new TypeAddress();
    		if(addressType_A!=null){    			    			
    			if(addressType_A.getID()!=null)
    				typeAddress_A.setId(addressType_A.getID().getValue());    						
    		}
    		typeAgent.setPostalAddress(typeAddress_A);    
    		documento.setAgentParty(typeAgent);
    		
    		PartyType receiverType = perceptionType.getReceiverParty();
    		AddressType addressType_R = receiverType.getPostalAddress();
			TypeParty typeReceiver = new TypeParty();
			TypeAddress typeAddress_R = new TypeAddress();
    		if(addressType_R!=null){
    			if(addressType_R.getID()!=null)
    				typeAddress_R.setId(addressType_R.getID().getValue());
    		}
			typeReceiver.setPostalAddress(typeAddress_R);
			documento.setReceiverParty(typeReceiver); 
			
    		return documento;
        } catch (Exception e) {
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("unmarshalPercepcionSunat Exception \n"+errors);
        } 
    	return null;
    }
    
    public EGuiaDocumento unmarshalDespatchAdviceType (Object guiaSunat){
    	try {
    		DespatchAdviceType despatchAdviceType = (DespatchAdviceType) guiaSunat;
    		EGuiaDocumento documento = dtoMapperService.map(despatchAdviceType, EGuiaDocumento.class);   	
    		String correoAdquiriente = "vacioAdquiriente@example.com";
    		String correoEmisor = "vacioEmisor@example.com";    	
    		documento.setTipoDocumentoGuia(Constantes.SUNAT_GUIA_REMISION_REMITENTE);
    		documento.setCorreoEmisor(correoEmisor);
    		documento.setCorreoAdquiriente(correoAdquiriente);
    		OrderReferenceType orderReferenceType = despatchAdviceType.getOrderReference();
    		TypeOrderReference typeOrderReference = new TypeOrderReference();
    		if(orderReferenceType!= null && orderReferenceType.getOrderTypeCode()!=null){
    			typeOrderReference.setOrderTypeCode(orderReferenceType.getOrderTypeCode().getValue());
    			documento.setTypeOrderReference(typeOrderReference);
    		}
    		List<DespatchLineType> items = despatchAdviceType.getDespatchLine();
    		List<EGuiaDocumentoItem> itemList = new ArrayList<EGuiaDocumentoItem>();
    		if (items != null) {
	    		int numeroOrden = 0;
	    		for (DespatchLineType ir : items) {
	    			numeroOrden++;
	    			EGuiaDocumentoItem item = dtoMapperService.map(ir, EGuiaDocumentoItem.class);
	    			item.setNumeroOrdenItem(String.valueOf(numeroOrden));    		
	    			itemList.add(item);
	    		}   	
	    		documento.setItems(itemList);   
    		}
			if(despatchAdviceType.getIssueTime()!=null)
	        	documento.setIssueTime(despatchAdviceType.getIssueTime().getValue().toString());

    		return documento;
        } catch (Exception e) {
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("unmarshalDespatchAdviceType Exception \n"+errors);
        } 
    	return null;
    } 
}
