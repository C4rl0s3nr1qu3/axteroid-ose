package com.axteroid.ose.server.rulesubl.dozer.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EDocumentoAnticipo;
import com.axteroid.ose.server.tools.edocu.EDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EDocumentoReferencia;
import com.axteroid.ose.server.tools.ubltype.TypeAllowanceCharge;
import com.axteroid.ose.server.tools.ubltype.TypeBillingReference;
import com.axteroid.ose.server.tools.ubltype.TypeDocumentReference;
import com.axteroid.ose.server.tools.ubltype.TypePricingReference;
import com.axteroid.ose.server.tools.ubltype.TypeResponse;
import com.axteroid.ose.server.tools.ubltype.TypeTaxCategory;
import com.axteroid.ose.server.tools.ubltype.TypeTaxSubtotal;
import com.axteroid.ose.server.ubl20.dozer.DTOMapperServiceDozerImpl;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import oasis.names.specification.ubl.schema.xsd.IDocumentoItemSunat;
import oasis.names.specification.ubl.schema.xsd.IDocumentoSunat;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AllowanceChargeType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.BillingReferenceType;
//import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CreditNoteLineType;
//import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DebitNoteLineType;
//import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PaymentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PriceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ResponseType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxCategoryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxSchemeType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxSubtotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.creditnote_2.CreditNoteType;
import oasis.names.specification.ubl.schema.xsd.debitnote_2.DebitNoteType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;

public class UnMarshallerOPE {	
	//private static final Logger log = Logger.getLogger(UnMarshallerOPE.class);
	private static final Logger log = LoggerFactory.getLogger(UnMarshallerOPE.class);
	
	DTOMapperServiceDozerImpl dtoMapperService = new DTOMapperServiceDozerImpl();
	
    public EDocumento unmarshalDocumento(IDocumentoSunat documentoSunat) {   
    	//log.info("unmarshalDocumento");
        try {
        	EDocumento documento = dtoMapperService.map(documentoSunat, EDocumento.class);
        	if (documentoSunat instanceof CreditNoteType) {
        		this.completarDocumentoReferencia(documentoSunat, documento);
        	} else if (documentoSunat instanceof DebitNoteType) {
        		this.completarDocumentoReferencia(documentoSunat, documento);
        	} else if (documentoSunat instanceof InvoiceType) {               		
        		if(((InvoiceType) documentoSunat).getInvoiceTypeCode()!=null) {
        			this.completarItemInformacionCabecera(documentoSunat, documento);
        		}
        	}
        	this.completarItemInformacionItem(documentoSunat, documento);
        	return documento;
        } catch (Exception e) {
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("unmarshalDocumento Exception \n"+errors);
        } 
        return null;
    }
    
    private void completarDocumentoReferencia(IDocumentoSunat documentoSunat, EDocumento documento) {
    	try {    	
	    	List<BillingReferenceType> billingReferenceTypes = new ArrayList<BillingReferenceType>();
	        List<ResponseType> discrepancyResponse = new ArrayList<ResponseType>();
	        if (documentoSunat instanceof CreditNoteType) {        	
	            billingReferenceTypes = ((CreditNoteType) documentoSunat).getBillingReference();
	            discrepancyResponse = ((CreditNoteType) documentoSunat).getDiscrepancyResponse();
	        }else if (documentoSunat instanceof DebitNoteType) {
	            billingReferenceTypes = ((DebitNoteType) documentoSunat).getBillingReference();
	            discrepancyResponse = ((DebitNoteType) documentoSunat).getDiscrepancyResponse();
	        }
	        List<TypeBillingReference> billingReference = new ArrayList<TypeBillingReference>();
	        for (int i = 0; i < billingReferenceTypes.size(); i++) {	        	
	        	TypeBillingReference typeBillingReference = new TypeBillingReference();
	        	TypeDocumentReference typeDocumentReference = new TypeDocumentReference();
	            if(billingReferenceTypes.get(i).getInvoiceDocumentReference()!=null) {
	            	typeDocumentReference.setId(billingReferenceTypes.get(i).getInvoiceDocumentReference().getID().getValue());
	            	if(billingReferenceTypes.get(i).getInvoiceDocumentReference().getDocumentTypeCode()!=null)
	            		typeDocumentReference.setDocumentTypeCode(billingReferenceTypes.get(i).getInvoiceDocumentReference().getDocumentTypeCode().getValue());	            
	            }
	            typeBillingReference.setInvoiceDocumentReference(typeDocumentReference);
	            billingReference.add(typeBillingReference);
	        }	        
	        documento.setBillingReference(billingReference);
	        
	        List<TypeResponse> listTypeResponse = new ArrayList<TypeResponse>();
	        for (int i = 0; i < discrepancyResponse.size(); i++) {
	            TypeResponse typeResponse = new TypeResponse();
	            if(discrepancyResponse.get(i).getReferenceID() != null)
	            	typeResponse.setReferenceID(discrepancyResponse.get(i).getReferenceID().getValue());
	            if(discrepancyResponse.get(i).getResponseCode() != null)
	            	typeResponse.setResponseCode(discrepancyResponse.get(i).getResponseCode().getValue());
	            listTypeResponse.add(typeResponse);
	        }
	        documento.setListTypeResponse(listTypeResponse);
	        
	        List<EDocumentoReferencia> documentoReferencias = new ArrayList<EDocumentoReferencia>();
	        for (int i = 0; i < billingReferenceTypes.size(); i++) {
	            if (i == 0) continue;
	            EDocumentoReferencia documentoReferencia = new EDocumentoReferencia();
	            documentoReferencia.setNumeroDocumentoReferencia(billingReferenceTypes.get(i).getInvoiceDocumentReference().getID().getValue());
	            documentoReferencia.setTipoDocumentoReferencia(billingReferenceTypes.get(i).getInvoiceDocumentReference().getDocumentTypeCode().getValue());
	            documentoReferencias.add(documentoReferencia);
	        }
	        documento.setReferencias(documentoReferencias);	        	        
    	} catch (Exception e) {
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("completarDocumentoReferencia Exception \n"+errors);
        } 
    }
    
    private void completarItemInformacionCabecera(IDocumentoSunat documentoSunat, EDocumento documento) {
    	try {	    
	        InvoiceType invoiceType = (InvoiceType) documentoSunat;
	        List<PaymentType> prepaidList = invoiceType.getPrepaidPayment();
	        List<EDocumentoAnticipo> itemList = new ArrayList<EDocumentoAnticipo>();
	        for (PaymentType paymentType : prepaidList) {
	            EDocumentoAnticipo anticipo=new EDocumentoAnticipo();
	            if(paymentType.getPaidAmount()!=null){
	                anticipo.setTotalPrepagadoAnticipo(paymentType.getPaidAmount().getValue());
	            }
	            if(paymentType.getID()!=null){
	                anticipo.setTipoDocumentoAnticipo(paymentType.getID().getSchemeID());
	                anticipo.setSerieNumeroDocumentoAnticipo(paymentType.getID().getValue());
	            }
	            if(paymentType.getInstructionID()!=null){
	                anticipo.setTipoDocumentoEmisorAnticipo(paymentType.getInstructionID().getSchemeID());
	                anticipo.setNumeroDocumentoEmisorAnticipo(paymentType.getInstructionID().getValue());
	            }
	            itemList.add(anticipo);
	        }
	        documento.setAnticipos(itemList);
	        List<AllowanceChargeType> allowanceChargeList = invoiceType.getAllowanceCharge();
	        List<TypeAllowanceCharge> listTypeAllowanceCharge = new ArrayList<TypeAllowanceCharge>();
	        for (AllowanceChargeType allowanceChargeType : allowanceChargeList) {
	        	TypeAllowanceCharge typeAllowanceCharge = new TypeAllowanceCharge();
	        	if(allowanceChargeType.getAllowanceChargeReasonCode()!=null)
	        		typeAllowanceCharge.setAllowanceChargeReasonCode(allowanceChargeType.getAllowanceChargeReasonCode().getValue());
	        	listTypeAllowanceCharge.add(typeAllowanceCharge);
	        }
	        documento.setListTypeAllowanceCharge(listTypeAllowanceCharge);	
	            
    	} catch (Exception e) {
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("completarItemInformacionCabecera Exception \n"+errors);
        } 
    }
		
    private void completarItemInformacionItem(IDocumentoSunat documentoSunat, EDocumento documento) {        
    	try {    	
    		List<? extends IDocumentoItemSunat> itemSunatList = documentoSunat.getLineItem();
	        List<EDocumentoItem> itemList = documento.getItems();
	        for (IDocumentoItemSunat itemSunat : itemSunatList) {
	            final String nuOrden = itemSunat.getIdValue();
	            Collection result = Collections2.filter(itemList, new Predicate<EDocumentoItem>() {
	                public boolean apply(@NotNull EDocumentoItem eDocumentoItem) {
	                    return nuOrden.equals(eDocumentoItem.getNumeroOrdenItem());
	                }
	            });
	            EDocumentoItem itemDocumento = (EDocumentoItem) result.toArray()[0];
	            itemDocumento.setTipoMoneda(documento.getTipoMoneda());
	            List<AllowanceChargeType> allowanceChargeTypeList = itemSunat.getAllowanceCharge();
	            if (allowanceChargeTypeList != null) {
	                for (AllowanceChargeType allowanceChargeType : allowanceChargeTypeList) {
	                	if(allowanceChargeType.getAmount().getCurrencyID()!=null)
	                		itemDocumento.setTipoMoneda(allowanceChargeType.getAmount().getCurrencyID().value());
	                    if (allowanceChargeType.getChargeIndicator().isValue()) {
	                        itemDocumento.setImporteCargo(allowanceChargeType.getAmount().getValue());
	                    } else {
	                        itemDocumento.setImporteDescuento(allowanceChargeType.getAmount().getValue());
	                    }
	                }
	            }
	            
	            List<TypeTaxSubtotal> listTypeTaxSubtotal = new ArrayList<TypeTaxSubtotal>();
	            List<TaxTotalType> taxtTotalList = itemSunat.getTaxTotal();
	            for (TaxTotalType taxtTotalItem : taxtTotalList) {
	                String codigoTributo = taxtTotalItem.getTaxSubtotal().get(0).getTaxCategory().getTaxScheme().getID().getValue();
	                if (Constantes.SUNAT_CODIGO_TRIBUTO_IGV.equals(codigoTributo)) {
	                    itemDocumento.setImporteIgv(taxtTotalItem.getTaxSubtotal().get(0).getTaxAmount().getValue());	                    
	                    if(taxtTotalItem.getTaxSubtotal().get(0).getTaxCategory().getTaxExemptionReasonCode()!=null)
	                    	itemDocumento.setCodigoRazonExoneracion(taxtTotalItem.getTaxSubtotal().get(0).getTaxCategory().getTaxExemptionReasonCode().getValue());
	                } else if (Constantes.SUNAT_CODIGO_TRIBUTO_ISC.equals(codigoTributo)) {
	                    itemDocumento.setImporteIsc(taxtTotalItem.getTaxSubtotal().get(0).getTaxAmount().getValue());
	                    if(taxtTotalItem.getTaxSubtotal().get(0).getTaxCategory().getTierRange()!=null)
	                    	itemDocumento.setTipoSistemaImpuestoISC(taxtTotalItem.getTaxSubtotal().get(0).getTaxCategory().getTierRange().getValue());
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
	            itemDocumento.setListTypeTaxSubtotal(listTypeTaxSubtotal);	            
	            if((itemSunat.getPricingReference()!=null) && 
	            		(itemSunat.getPricingReference().getAlternativeConditionPrice()!=null)) {
		            List<PriceType> priceTypeList = itemSunat.getPricingReference().getAlternativeConditionPrice();
		            if (priceTypeList.size() == 1) {
		                itemDocumento.setImporteUnitarioConImpuesto(priceTypeList.get(0).getPriceAmount().getValue());
		                itemDocumento.setCodigoImporteUnitarioConImpuesto(priceTypeList.get(0).getPriceTypeCode().getValue());
		            } else if (priceTypeList.size() == 2) {
		                for (PriceType priceTypeItem : priceTypeList) {
		                    if ("02".equals(priceTypeItem.getPriceTypeCode().getValue())) {
		                        itemDocumento.setImporteReferencial(priceTypeItem.getPriceAmount().getValue());
		                        itemDocumento.setCodigoImporteReferencial(priceTypeItem.getPriceTypeCode().getValue());
		                    } else {
		                        itemDocumento.setImporteUnitarioConImpuesto(priceTypeItem.getPriceAmount().getValue());
		                        itemDocumento.setCodigoImporteUnitarioConImpuesto(priceTypeItem.getPriceTypeCode().getValue());
		                    }                            
		                }
		            }	
		            List<TypePricingReference> listTypePricingReference = new ArrayList<TypePricingReference>();
		            for (PriceType priceTypeItem : priceTypeList) {
		            	 TypePricingReference typePricingReference = new TypePricingReference();
		            	 typePricingReference.setPriceAmount(priceTypeItem.getPriceAmount().getValue());
		            	 typePricingReference.setPriceTypeCode(priceTypeItem.getPriceTypeCode().getValue());
		            	 listTypePricingReference.add(typePricingReference);
		            }
		            itemDocumento.setListTypePricingReference(listTypePricingReference);	           	            	            
//		            if(documento.getTipoDocumento()!=null && (documento.getTipoDocumento().equals(OseConstantes.SUNAT_FACTURA) || 
//			        		documento.getTipoDocumento().equals(OseConstantes.SUNAT_BOLETA))) {
//		            	InvoiceLineType invoiceLineType = (InvoiceLineType) itemSunat;
//		            	if(invoiceLineType.getInvoicedQuantity()!=null)
//		            		itemDocumento.setUnidadMedidaComercial(invoiceLineType.getInvoicedQuantity().getUnitCode());
//		            }
//		            if(documento.getTipoDocumento()!=null && documento.getTipoDocumento().equals(OseConstantes.SUNAT_NOTA_CREDITO)) {
//		            	CreditNoteLineType creditNoteLineType = (CreditNoteLineType) itemSunat;
//		            	if(creditNoteLineType.getCreditedQuantity()!=null)
//		            		itemDocumento.setUnidadMedidaComercial(creditNoteLineType.getCreditedQuantity().getUnitCode());
//		            }
//		            if(documento.getTipoDocumento()!=null && documento.getTipoDocumento().equals(OseConstantes.SUNAT_NOTA_DEBITO)) {
//		            	DebitNoteLineType creditNoteLineType = (DebitNoteLineType) itemSunat;
//		            	if(creditNoteLineType.getDebitedQuantity()!=null)
//		            		itemDocumento.setUnidadMedidaComercial(creditNoteLineType.getDebitedQuantity().getUnitCode());
//		            }
//		            log.info("itemDocumento.getUnidadMedidaComercial(): |"+itemDocumento.getUnidadMedidaComercial()+"|");
	            }
	        }
    	} catch (Exception e) {
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("completarItemInformacionItem Exception \n"+errors);
        } 
    }    
    
}
