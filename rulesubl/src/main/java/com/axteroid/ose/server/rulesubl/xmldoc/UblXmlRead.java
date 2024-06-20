package com.axteroid.ose.server.rulesubl.xmldoc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.ubltype.TypeAddress;
import com.axteroid.ose.server.tools.ubltype.TypeCustomerParty;
import com.axteroid.ose.server.tools.ubltype.TypeDocumentReference;
import com.axteroid.ose.server.tools.ubltype.TypeMonetaryTotal;
import com.axteroid.ose.server.tools.ubltype.TypeParty;
import com.axteroid.ose.server.tools.ubltype.TypePartyIdentification;
import com.axteroid.ose.server.tools.ubltype.TypePartyLegalEntity;
import com.axteroid.ose.server.tools.ubltype.TypePaymentTerms;
import com.axteroid.ose.server.tools.ubltype.TypeResponse;
import com.axteroid.ose.server.tools.ubltype.TypeSupplierParty;
import com.axteroid.ose.server.tools.xml.XmlUtil;

public class UblXmlRead {

	private static final Logger log = LoggerFactory.getLogger(UblXmlRead.class);
	
	public void getDatosDocuments(Document document, EDocumento eDocumento, Documento tbComprobante) {
		//log.info("getDatosDocuments");
		try {				
	        NodeList nl_0 = document.getElementsByTagName("cbc:IssueTime");	  
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_2 = nl_1.item(b);		
					//log.info(a+"-"+b+" cbc:UBLVersionID : " +node_2.getNodeValue());					
					eDocumento.setIssueTime(node_2.getNodeValue());
					//log.info("eDocumento.getIssueTime(): "+eDocumento.getIssueTime());
				}				
	        }
	        //log.info("Root element :" + document.getDocumentElement().getNodeName());
	        nl_0 = document.getElementsByTagName("cbc:InvoiceTypeCode");	  
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_2 = nl_1.item(b);		
					//log.info(a+"-"+b+" cbc:UBLVersionID : " +node_2.getNodeValue());					
					eDocumento.setTipoDocumento(node_2.getNodeValue());
					//log.info("{}-{}) eDocumento.getTipoDocumento() {}",a,b,eDocumento.getTipoDocumento());
				}	
				if(nl_0.item(a).getNodeType() == Node.ELEMENT_NODE){
					Element element_4 = (Element) nl_0.item(a);
					String val = XmlUtil.getAttributesValue(element_4,"listID");
					eDocumento.setTipoOperacionFactura(val);
					//log.info("{}) eDocumento.getTipoOperacionFactura() {}",a,eDocumento.getTipoOperacionFactura());
				}				
	        }       
			if((tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO)) ||
					(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_NOTA_DEBITO))) {
				eDocumento.setTipoDocumento(tbComprobante.getTipoDocumento());				
				//log.info("eDocumento.getTipoDocumento() 1) "+eDocumento.getTipoDocumento());
		        nl_0 = document.getElementsByTagName("cac:DiscrepancyResponse");	
		        List<TypeResponse> listDiscrepancyResponse = new ArrayList<TypeResponse>();
		        for(int a=0; a < nl_0.getLength(); a++){
		        	TypeResponse discrepancyResponseType = new TypeResponse();
					Node node_0 = nl_0.item(a);			
					NodeList nl_1 = node_0.getChildNodes();
					for(int b=0; b < nl_1.getLength(); b++){
						Node node_1 = nl_1.item(b);		
						//log.info(a+"-"+b+" node: " +node_1.getNodeName());	
						if(node_1.getNodeName().equals("cbc:ResponseCode")) {							
							NodeList nl_2 = node_1.getChildNodes();
							for(int c=0; c < nl_2.getLength(); c++){
								Node node_2 = nl_2.item(c);		
								//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
								//BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
								//typeLegalMonetaryTotal.setLineExtensionAmount(bigDecimal);
								discrepancyResponseType.setResponseCode(node_2.getNodeValue());
								//log.info("typeLegalMonetaryTotal.getLineExtensionAmount(): "+typeLegalMonetaryTotal.getLineExtensionAmount());
								listDiscrepancyResponse.add(discrepancyResponseType);
							}
						}
					}					
		        }
		        eDocumento.setDiscrepancyResponse(listDiscrepancyResponse);
			}
			if((tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_FACTURA)) ||
					(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_BOLETA)) ||
					(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO))){
				//eDocumento.setTipoDocumento(tbComprobante.getTipoComprobante());				
				//log.info("eDocumento.getTipoDocumento() 1) "+eDocumento.getTipoDocumento());
		        nl_0 = document.getElementsByTagName("cac:PaymentTerms");	
		        List<TypePaymentTerms> listTypePaymentTerms = new ArrayList<TypePaymentTerms>();
		        for(int a=0; a < nl_0.getLength(); a++){
		        	TypePaymentTerms typePaymentTerms = new TypePaymentTerms();
					Node node_0 = nl_0.item(a);			
					NodeList nl_1 = node_0.getChildNodes();
					boolean bList = false;
					for(int b=0; b < nl_1.getLength(); b++){
						Node node_1 = nl_1.item(b);		
						//log.info(a+"-"+b+" node: " +node_1.getNodeName());	
						if(node_1.getNodeName().equals("cbc:ID")) {							
							NodeList nl_2 = node_1.getChildNodes();
							for(int c=0; c < nl_2.getLength(); c++){
								Node node_2 = nl_2.item(c);		
								//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
								typePaymentTerms.setId(node_2.getNodeValue());
								bList = true;								
							}
						}
						if(node_1.getNodeName().equals("cbc:PaymentMeansID")) {							
							NodeList nl_2 = node_1.getChildNodes();
							for(int c=0; c < nl_2.getLength(); c++){
								Node node_2 = nl_2.item(c);		
								//log.info(a+"-"+b+"-"+c+" cbc:PaymentMeansID : " +node_2.getNodeValue());
								typePaymentTerms.setPaymentMeansID(node_2.getNodeValue());
								bList = true;
							}
						}		
						if(node_1.getNodeName().equals("cbc:PaymentDueDate")) {							
							NodeList nl_2 = node_1.getChildNodes();
							for(int c=0; c < nl_2.getLength(); c++){
								Node node_2 = nl_2.item(c);		
								//log.info(a+"-"+b+"-"+c+" cbc:PaymentDueDate : " +node_2.getNodeValue());
								typePaymentTerms.setPaymentDueDate(node_2.getNodeValue());
								bList = true;
							}
						}	
					}
					if(bList) 
						listTypePaymentTerms.add(typePaymentTerms);						
					//log.info("bList: {} | size: {} | typePaymentTerms: {}",bList,listTypePaymentTerms.size(),typePaymentTerms.getId());
		        }
		        eDocumento.setListTypePaymentTerms(listTypePaymentTerms);
			}
	        this.getDatosDocumentsSAP(document, eDocumento, tbComprobante);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDatosDocuments Exception \n"+errors);	
	    }	        
	}	
	
	public void getAccountingSupplierParty(Document document, EDocumento eDocumento) {
		//log.info("getAccountingSupplierParty");
		try {				
	        //log.info("Root element :" + doc.getDocumentElement().getNodeName());
	        NodeList nl_0 = document.getElementsByTagName("cac:AccountingSupplierParty");	          
	        for(int a=0; a < nl_0.getLength(); a++){
	        	TypeSupplierParty accountingSupplierParty = new TypeSupplierParty();
				Node node_0 = nl_0.item(a);		
				//log.info(a+" node: " +node_0.getNodeName());		
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" node: " +node_1.getNodeName());	
					if(node_1.getNodeName().equals("cac:Party")) {
						TypeParty party = new TypeParty();
						NodeList nl_2 = node_1.getChildNodes();		
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeName()+" - "+node_2.getNodeValue());
							if(node_2.getNodeName().equals("cac:PartyIdentification")) {
								NodeList nl_3 = node_2.getChildNodes();						
								for(int d=0; d < nl_3.getLength(); d++){
									Node node_3 = nl_3.item(d);		
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName()+" - "+node_3.getTextContent());
									if(node_3.getNodeName().equals("cbc:ID")) {										
										NodeList nl_4 = node_3.getChildNodes();						
										for(int e=0; e < nl_4.getLength(); e++){
											Node node_4 = nl_4.item(e);
											//log.info(a+"-"+b+"-"+c+"-"+e+" node: " +node_4.getNodeValue()+" - "+node_4.getTextContent());													
											eDocumento.setNumeroDocumentoEmisor(node_4.getNodeValue());
											//log.info("eDocumento.getNumeroDocumentoEmisor(): "+eDocumento.getNumeroDocumentoEmisor());
										}
									}
									if(nl_3.item(d).getNodeType() == Node.ELEMENT_NODE){
										Element element_4 = (Element) nl_3.item(d);
										String val = XmlUtil.getAttributesValue(element_4,"schemeID");
										eDocumento.setTipoDocumentoEmisor(val);
										//log.info("eDocumento.getTipoDocumentoEmisor(): "+eDocumento.getTipoDocumentoEmisor());
									}	
								}
							}
							
							if(node_2.getNodeName().equals("cac:PartyLegalEntity")) {
								List<TypePartyLegalEntity> partyLegalEntity = new ArrayList<TypePartyLegalEntity>();
								NodeList nl_3 = node_2.getChildNodes();						
								for(int d=0; d < nl_3.getLength(); d++){
									Node node_3 = nl_3.item(d);		
									TypePartyLegalEntity typePartyLegalEntity = new TypePartyLegalEntity();
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName()+" - "+node_3.getTextContent());
									if(node_3.getNodeName().equals("cac:RegistrationAddress")) {	
										TypeAddress registrationAddress = new TypeAddress();
										NodeList nl_4 = node_3.getChildNodes();						
										for(int e=0; e < nl_4.getLength(); e++){
											Node node_4 = nl_4.item(e);
											//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_4.getNodeName()+" - "+node_4.getTextContent());
											if(node_4.getNodeName().equals("cbc:ID")) {										
												NodeList nl_5 = node_4.getChildNodes();						
												for(int f=0; f < nl_5.getLength(); f++){
													Node node_5 = nl_5.item(f);
													//log.info(a+"-"+b+"-"+c+"-"+e+"-"+f+" node: " +node_5.getNodeValue()+" - "+node_5.getTextContent());											
													registrationAddress.setId(node_5.getNodeValue());
												}
											}
											if(node_4.getNodeName().equals("cbc:AddressTypeCode")) {										
												NodeList nl_5 = node_4.getChildNodes();						
												for(int f=0; f < nl_5.getLength(); f++){
													Node node_5 = nl_5.item(f);
													//log.info(a+"-"+b+"-"+c+"-"+e+"-"+f+" node: " +node_5.getNodeValue()+" - "+node_5.getTextContent());											
													registrationAddress.setAddressTypeCode(node_5.getNodeValue());
													//log.info("registrationAddress.getAddressTypeCode():  "+registrationAddress.getAddressTypeCode() );
												}
											}
										}
										if((registrationAddress.getId()!=null) || (registrationAddress.getAddressTypeCode()!=null) ) {
											typePartyLegalEntity.setRegistrationAddress(registrationAddress);
											partyLegalEntity.add(typePartyLegalEntity);
											//log.info("partyLegalEntity.size():  "+partyLegalEntity.size());
										}
									}
								}		
								if(partyLegalEntity.size() > 0){
									party.setPartyLegalEntity(partyLegalEntity);
									//log.info(" party.getListPartyLegalEntity().size(): "+party.getPartyLegalEntity().size());
								}
							}							
						}				
						if((party.getPartyLegalEntity()!= null) &&  (party.getPartyLegalEntity().size() > 0)){
							accountingSupplierParty.setParty(party);
							//log.info(" accountingSupplierParty.getParty().getListPartyLegalEntity().size():  "+accountingSupplierParty.getParty().getPartyLegalEntity().size());
						}
					}	
				}	
				if(accountingSupplierParty.getParty()!= null){
					eDocumento.setAccountingSupplierParty(accountingSupplierParty);
					//log.info(" eDocumento.getAccountingSupplierParty().getParty().getListPartyLegalEntity().size():  "+eDocumento.getAccountingSupplierParty().getParty().getPartyLegalEntity().size());
				}
	        }	    
	        this.getAccountingSupplierPartySAP(document, eDocumento);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getAccountingCustomerParty Exception \n"+errors);	
		}
	}	
	
	public void getAccountingCustomerParty(Document document, EDocumento eDocumento) {
		//log.info("getAccountingCustomerParty");
		try {			
	        //log.info("Root element :" + doc.getDocumentElement().getNodeName());
	        NodeList nl_0 = document.getElementsByTagName("cac:AccountingCustomerParty");	          
	        for(int a=0; a < nl_0.getLength(); a++){
	        	TypeCustomerParty accountingCustomerParty = new TypeCustomerParty();
				Node node_0 = nl_0.item(a);		
				//log.info(a+" node: " +node_0.getNodeName());		
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" node: " +node_1.getNodeName());	
					if(node_1.getNodeName().equals("cac:Party")) {
						TypeParty party = new TypeParty();
						NodeList nl_2 = node_1.getChildNodes();		
						//List<String> listPartyIdentification = new ArrayList<String>();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeName()+" - "+node_2.getNodeValue());
							if(node_2.getNodeName().equals("cac:PartyIdentification")) {
								NodeList nl_3 = node_2.getChildNodes();						
								for(int d=0; d < nl_3.getLength(); d++){
									Node node_3 = nl_3.item(d);		
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName()+" - "+node_3.getTextContent());
									if(node_3.getNodeName().equals("cbc:ID")) {										
										NodeList nl_4 = node_3.getChildNodes();						
										for(int e=0; e < nl_4.getLength(); e++){
											Node node_4 = nl_4.item(e);
											//log.info(a+"-"+b+"-"+c+"-"+e+" cac:PartyIdentification/cbc:ID : " +node_4.getNodeValue()+" - "+node_4.getTextContent());												
											eDocumento.setNumeroDocumentoAdquiriente(node_4.getNodeValue());
											//log.info("eDocumento.getNumeroDocumentoAdquiriente():"+eDocumento.getNumeroDocumentoAdquiriente());
										}
									}
									if(nl_3.item(d).getNodeType() == Node.ELEMENT_NODE){
										Element element_4 = (Element) nl_3.item(d);
										String val = XmlUtil.getAttributesValue(element_4,"schemeID");
										eDocumento.setTipoDocumentoAdquiriente(val);
										//log.info("eDocumento.getTipoDocumentoAdquiriente():"+eDocumento.getTipoDocumentoAdquiriente());
									}									
								}
							}
							if(node_2.getNodeName().equals("cac:PartyLegalEntity")) {
								List<TypePartyLegalEntity> partyLegalEntity = new ArrayList<TypePartyLegalEntity>();
								NodeList nl_3 = node_2.getChildNodes();						
								for(int d=0; d < nl_3.getLength(); d++){
									Node node_3 = nl_3.item(d);		
									TypePartyLegalEntity typePartyLegalEntity = new TypePartyLegalEntity();
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName()+" - "+node_3.getTextContent());
									if(node_3.getNodeName().equals("cac:RegistrationAddress")) {	
										TypeAddress registrationAddress = new TypeAddress();
										NodeList nl_4 = node_3.getChildNodes();						
										for(int e=0; e < nl_4.getLength(); e++){
											Node node_4 = nl_4.item(e);
											//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_4.getNodeName()+" - "+node_4.getTextContent());
											if(node_4.getNodeName().equals("cbc:ID")) {										
												NodeList nl_5 = node_4.getChildNodes();						
												for(int f=0; f < nl_5.getLength(); f++){
													Node node_5 = nl_5.item(f);
													//log.info(a+"-"+b+"-"+c+"-"+e+"-"+f+" cac:RegistrationAddress/cbc:ID : " +node_5.getNodeValue()+" - "+node_5.getTextContent());											
													registrationAddress.setId(node_5.getNodeValue());
												}
											}
										}
										if(registrationAddress.getId()!=null) {
											typePartyLegalEntity.setRegistrationAddress(registrationAddress);
											partyLegalEntity.add(typePartyLegalEntity);
											//log.info("partyLegalEntity.size():  "+partyLegalEntity.size());
										}
									}
								}		
								if(partyLegalEntity.size() > 0){
									party.setPartyLegalEntity(partyLegalEntity);
									//log.info(" party.getListPartyLegalEntity().size(): "+party.getPartyLegalEntity().size());
								}
							}															
						}						
					}										
				}	
				if(accountingCustomerParty.getParty()!= null){
					eDocumento.setAccountingCustomerParty(accountingCustomerParty);
					//log.info(" eDocumento.getAccountingCustomerParty().getParty().getListPartyLegalEntity().size():  "+eDocumento.getAccountingCustomerParty().getParty().getPartyLegalEntity().size());
				}
	        }
	        this.getAccountingCustomerPartySAP(document, eDocumento);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getAccountingCustomerParty Exception \n"+errors);	
		}
	}
				
	public void getAdditionalDocumentReference(Document document, EDocumento eDocumento) {
		//log.info("getAdditionalDocumentReference");
		try {				 
			List<TypeDocumentReference> listTypeDocumentReference = new ArrayList<TypeDocumentReference>();
	        //log.info("Root element :" + doc.getDocumentElement().getNodeName());
	        NodeList nl_0 = document.getElementsByTagName("cac:AdditionalDocumentReference");	          
	        for(int a=0; a < nl_0.getLength(); a++){
	        	TypeDocumentReference typeDocumentReference = new TypeDocumentReference();
				Node node_0 = nl_0.item(a);		
				//log.info(a+" node: " +node_0.getNodeName());		
				NodeList nl_1 = node_0.getChildNodes();
				boolean dato = false;
				for(int b=0; b < nl_1.getLength(); b++){
					if(dato)
						return;
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" node: " +node_1.getNodeName());	
					if(node_1.getNodeName().equals("cbc:ID")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							typeDocumentReference.setId(node_2.getNodeValue());
						}
					}
					if(node_1.getNodeName().equals("cbc:DocumentTypeCode")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeValue());
							typeDocumentReference.setDocumentTypeCode(node_2.getNodeValue());
						}
					}
					if(node_1.getNodeName().equals("cbc:DocumentStatusCode")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeValue());
							typeDocumentReference.setDocumentStatusCode(node_2.getNodeValue());
						}
					}
					
					if(node_1.getNodeName().equals("cac:IssuerParty")) {
						TypeParty issuerParty = new TypeParty();
						NodeList nl_2 = node_1.getChildNodes();		
						List<TypePartyIdentification> partyIdentification = new ArrayList<TypePartyIdentification>();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeName()+" - "+node_2.getNodeValue());
							if(node_2.getNodeName().equals("cac:PartyIdentification")) {
								NodeList nl_3 = node_2.getChildNodes();		
								TypePartyIdentification typePartyIdentification = new TypePartyIdentification();
								for(int d=0; d < nl_3.getLength(); d++){
									Node node_3 = nl_3.item(d);		
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName()+" - "+node_3.getTextContent());
									if(node_3.getNodeName().equals("cbc:ID")) {										
										NodeList nl_4 = node_3.getChildNodes();						
										for(int e=0; e < nl_4.getLength(); e++){
											Node node_4 = nl_4.item(e);
											//log.info(a+"-"+b+"-"+c+"-"+e+" cac:PartyIdentification/cbc:ID : " +node_4.getNodeValue()+" - "+node_4.getTextContent());											
											typePartyIdentification.setId(node_4.getNodeValue());
											partyIdentification.add(typePartyIdentification);											
										}
										
									}
								}
							}
						}						
						if(partyIdentification.size()>0) {
							//log.info("listPartyIdentification.size()  "+listPartyIdentification.size());
							issuerParty.setPartyIdentification(partyIdentification);
							typeDocumentReference.setIssuerParty(issuerParty);
							//log.info("listPartyIdentification.size()  "+typeDocumentReference.getIssuerParty().getPartyIdentification());
						}							
					}
				}	
				listTypeDocumentReference.add(typeDocumentReference);				
	        }
	        eDocumento.setListTypeAdditionalDocumentReference(listTypeDocumentReference);
	        this.getAdditionalDocumentReferenceSAP(document, eDocumento);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getAdditionalDocumentReference Exception \n"+errors);	
		}
	}

	public void getLegalMonetaryTotal(Document document, EDocumento eDocumento) {
		//log.info("getLegalMonetaryTotal");
		try {				
	        NodeList nl_0 = document.getElementsByTagName("cac:LegalMonetaryTotal");	  
	        for(int a=0; a < nl_0.getLength(); a++){
	        	TypeMonetaryTotal typeLegalMonetaryTotal = new TypeMonetaryTotal();
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" node: " +node_1.getNodeName());	
					if(node_1.getNodeName().equals("cbc:LineExtensionAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeLegalMonetaryTotal.setLineExtensionAmount(bigDecimal);
							//log.info("typeLegalMonetaryTotal.getLineExtensionAmount(): "+typeLegalMonetaryTotal.getLineExtensionAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:TaxInclusiveAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeLegalMonetaryTotal.setTaxInclusiveAmount(bigDecimal);
							//log.info("typeLegalMonetaryTotal.getTaxInclusiveAmount(): "+typeLegalMonetaryTotal.getTaxInclusiveAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:AllowanceTotalAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeLegalMonetaryTotal.setAllowanceTotalAmount(bigDecimal);
							//log.info("typeLegalMonetaryTotal.getAllowanceTotalAmount(): "+typeLegalMonetaryTotal.getAllowanceTotalAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:ChargeTotalAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeLegalMonetaryTotal.setChargeTotalAmount(bigDecimal);
							//log.info("typeLegalMonetaryTotal.getChargeTotalAmount(): "+typeLegalMonetaryTotal.getChargeTotalAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:PrepaidAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeLegalMonetaryTotal.setPrepaidAmount(bigDecimal);
							//log.info("typeLegalMonetaryTotal.getPrepaidAmount(): "+typeLegalMonetaryTotal.getPrepaidAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:PayableRoundingAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeLegalMonetaryTotal.setPayableRoundingAmount(bigDecimal);
							//log.info("typeLegalMonetaryTotal.getPayableRoundingAmount(): "+typeLegalMonetaryTotal.getPayableRoundingAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:PayableAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeLegalMonetaryTotal.setPayableAmount(bigDecimal);
							//log.info("typeLegalMonetaryTotal.getPayableAmount(): "+typeLegalMonetaryTotal.getPayableAmount());
						}
					}										
				}	
				eDocumento.setTypeLegalMonetaryTotal(typeLegalMonetaryTotal);
	        }	
	        this.getLegalMonetaryTotalSAP(document, eDocumento);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getLegalMonetaryTotal Exception \n"+errors);	
		}
	}

	public void getRequestedMonetaryTotal(Document document, EDocumento eDocumento) {
		//log.info("getRequestedMonetaryTotal");
		try {				
	        NodeList nl_0 = document.getElementsByTagName("cac:RequestedMonetaryTotal");	  
	        for(int a=0; a < nl_0.getLength(); a++){
	        	TypeMonetaryTotal typeRequestedMonetaryTotal = new TypeMonetaryTotal();
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" node: " +node_1.getNodeName());	
					if(node_1.getNodeName().equals("cbc:LineExtensionAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeRequestedMonetaryTotal.setLineExtensionAmount(bigDecimal);
							//log.info("typeRequestedMonetaryTotal.getLineExtensionAmount(): "+typeRequestedMonetaryTotal.getLineExtensionAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:TaxInclusiveAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeRequestedMonetaryTotal.setTaxInclusiveAmount(bigDecimal);
							//log.info("typeRequestedMonetaryTotal.getTaxInclusiveAmount(): "+typeRequestedMonetaryTotal.getTaxInclusiveAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:AllowanceTotalAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeRequestedMonetaryTotal.setAllowanceTotalAmount(bigDecimal);
							//log.info("typeRequestedMonetaryTotal.getAllowanceTotalAmount(): "+typeRequestedMonetaryTotal.getAllowanceTotalAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:ChargeTotalAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeRequestedMonetaryTotal.setChargeTotalAmount(bigDecimal);
							//log.info("typeRequestedMonetaryTotal.getChargeTotalAmount(): "+typeRequestedMonetaryTotal.getChargeTotalAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:PrepaidAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeRequestedMonetaryTotal.setPrepaidAmount(bigDecimal);
							//log.info("typeRequestedMonetaryTotal.getPrepaidAmount(): "+typeRequestedMonetaryTotal.getPrepaidAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:PayableRoundingAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeRequestedMonetaryTotal.setPayableRoundingAmount(bigDecimal);
							//log.info("typeLegalMonetaryTotal.getPayableRoundingAmount(): "+typeRequestedMonetaryTotal.getPayableRoundingAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:PayableAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeRequestedMonetaryTotal.setPayableAmount(bigDecimal);
							//log.info("typeRequestedMonetaryTotal.getPayableAmount(): "+typeRequestedMonetaryTotal.getPayableAmount());
						}
					}										
				}	
				eDocumento.setTypeRequestedMonetaryTotal(typeRequestedMonetaryTotal);				
	        }	
	        this.getRequestedMonetaryTotalSAP(document, eDocumento);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getRequestedMonetaryTotal Exception \n"+errors);	
		}
	}	
	
	public void getVersionUBL(Documento tbComprobante) {
		try {
			Document document = XmlUtil.getByteArray2Document(tbComprobante.getUbl());
			document.getDocumentElement().normalize();
	        NodeList nl_0 = document.getElementsByTagName("cbc:UBLVersionID");	  
	        tbComprobante.setUblVersion("2.0");
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_2 = nl_1.item(b);		
					//log.info(a+"-"+b+" cbc:UBLVersionID : " +node_2.getNodeValue());					
					tbComprobante.setUblVersion(node_2.getNodeValue());
				}
	        }
	        
	        nl_0 = document.getElementsByTagName("n2:UBLVersionID");	
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_2 = nl_1.item(b);		
					//log.info(a+"-"+b+" cbc:UBLVersionID : " +node_2.getNodeValue());					
					tbComprobante.setUblVersion(node_2.getNodeValue());
				}
	        }
	        
	        nl_0 = document.getElementsByTagName("cbc:CustomizationID");	 
	        tbComprobante.setCustomizaVersion("1.0");
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_2 = nl_1.item(b);		
					//log.info(a+"-"+b+" cbc:CustomizationID : " +node_2.getNodeValue());
					tbComprobante.setCustomizaVersion(node_2.getNodeValue());
				}
	        }
	        
	        nl_0 = document.getElementsByTagName("n2:CustomizationID");	 
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_2 = nl_1.item(b);		
					//log.info(a+"-"+b+" cbc:CustomizationID : " +node_2.getNodeValue());
					tbComprobante.setCustomizaVersion(node_2.getNodeValue());
				}
	        }
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getVersionUBL Exception \n"+errors);
		}
	}	
	
	/// GET SAP DOCUMENT
	public void getDatosDocumentsSAP(Document document, EDocumento eDocumento, Documento tbComprobante) {
		//log.info("getDatosDocuments");
		try {				
	        NodeList nl_0 = document.getElementsByTagName("n2:IssueTime");	  
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_2 = nl_1.item(b);		
					//log.info(a+"-"+b+" cbc:UBLVersionID : " +node_2.getNodeValue());					
					eDocumento.setIssueTime(node_2.getNodeValue());
					//log.info("eDocumento.getIssueTime(): "+eDocumento.getIssueTime());
				}				
	        }
	        //log.info("Root element :" + document.getDocumentElement().getNodeName());
	        nl_0 = document.getElementsByTagName("n2:InvoiceTypeCode");	  
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_2 = nl_1.item(b);		
					//log.info(a+"-"+b+" cbc:UBLVersionID : " +node_2.getNodeValue());					
					eDocumento.setTipoDocumento(node_2.getNodeValue());
					//log.info("{}-{}) eDocumento.getTipoDocumento() {}",a,b,eDocumento.getTipoDocumento());
				}	
				if(nl_0.item(a).getNodeType() == Node.ELEMENT_NODE){
					Element element_4 = (Element) nl_0.item(a);
					String val = XmlUtil.getAttributesValue(element_4,"listID");
					//log.info("val {}",val);
					eDocumento.setTipoOperacionFactura(val);
					//log.info("{}) eDocumento.getTipoOperacionFactura() {}",a,eDocumento.getTipoOperacionFactura());
				}				
	        }       
			if((tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO)) ||
					(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_NOTA_DEBITO))) {
				eDocumento.setTipoDocumento(tbComprobante.getTipoDocumento());
				//log.info("eDocumento.getTipoDocumento() 1) "+eDocumento.getTipoDocumento());
			}
			if((tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_FACTURA)) ||
					(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_BOLETA)) ||
					(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO))){				//eDocumento.setTipoDocumento(tbComprobante.getTipoComprobante());				
				//log.info("eDocumento.getTipoDocumento() 1) "+eDocumento.getTipoDocumento());
		        nl_0 = document.getElementsByTagName("n1:PaymentTerms");	
		        List<TypePaymentTerms> listTypePaymentTerms = new ArrayList<TypePaymentTerms>();
		        for(int a=0; a < nl_0.getLength(); a++){
		        	TypePaymentTerms typePaymentTerms = new TypePaymentTerms();
					Node node_0 = nl_0.item(a);			
					NodeList nl_1 = node_0.getChildNodes();
					boolean bList = false;
					for(int b=0; b < nl_1.getLength(); b++){
						Node node_1 = nl_1.item(b);		
						//log.info(a+"-"+b+" node: " +node_1.getNodeName());	
						if(node_1.getNodeName().equals("n2:ID")) {							
							NodeList nl_2 = node_1.getChildNodes();
							for(int c=0; c < nl_2.getLength(); c++){
								Node node_2 = nl_2.item(c);		
								//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
								typePaymentTerms.setId(node_2.getNodeValue());
								bList = true;								
							}
						}
						if(node_1.getNodeName().equals("n2:PaymentMeansID")) {							
							NodeList nl_2 = node_1.getChildNodes();
							for(int c=0; c < nl_2.getLength(); c++){
								Node node_2 = nl_2.item(c);		
								//log.info(a+"-"+b+"-"+c+" cbc:PaymentMeansID : " +node_2.getNodeValue());
								typePaymentTerms.setPaymentMeansID(node_2.getNodeValue());
								bList = true;
							}
						}		
						if(node_1.getNodeName().equals("n2:PaymentDueDate")) {							
							NodeList nl_2 = node_1.getChildNodes();
							for(int c=0; c < nl_2.getLength(); c++){
								Node node_2 = nl_2.item(c);		
								//log.info(a+"-"+b+"-"+c+" cbc:PaymentDueDate : " +node_2.getNodeValue());
								typePaymentTerms.setPaymentDueDate(node_2.getNodeValue());
								bList = true;
							}
						}							
					}
					if(bList) listTypePaymentTerms.add(typePaymentTerms);	
		        }
		        if(listTypePaymentTerms.size()>0)
		        	eDocumento.setListTypePaymentTerms(listTypePaymentTerms);
			}	        
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDatosDocuments Exception \n"+errors);	
	    }	        
	}		
	
	public void getAccountingSupplierPartySAP(Document document, EDocumento eDocumento) {
		//log.info("getAccountingSupplierParty");
		try {				
	        //log.info("Root element :" + doc.getDocumentElement().getNodeName());
	        NodeList nl_0 = document.getElementsByTagName("cac:AccountingSupplierParty");	          
	        for(int a=0; a < nl_0.getLength(); a++){
	        	TypeSupplierParty accountingSupplierParty = new TypeSupplierParty();
				Node node_0 = nl_0.item(a);		
				//log.info(a+" node: " +node_0.getNodeName());		
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" node: " +node_1.getNodeName());	
					if(node_1.getNodeName().equals("n1:Party")) {
						TypeParty party = new TypeParty();
						NodeList nl_2 = node_1.getChildNodes();		
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeName()+" - "+node_2.getNodeValue());
							if(node_2.getNodeName().equals("n1:PartyIdentification")) {
								NodeList nl_3 = node_2.getChildNodes();						
								for(int d=0; d < nl_3.getLength(); d++){
									Node node_3 = nl_3.item(d);		
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName()+" - "+node_3.getTextContent());
									if(node_3.getNodeName().equals("n2:ID")) {										
										NodeList nl_4 = node_3.getChildNodes();						
										for(int e=0; e < nl_4.getLength(); e++){
											Node node_4 = nl_4.item(e);
											//log.info(a+"-"+b+"-"+c+"-"+e+" node: " +node_4.getNodeValue()+" - "+node_4.getTextContent());													
											eDocumento.setNumeroDocumentoEmisor(node_4.getNodeValue());
											//log.info("eDocumento.getNumeroDocumentoEmisor(): "+eDocumento.getNumeroDocumentoEmisor());
										}
									}
									if(nl_3.item(d).getNodeType() == Node.ELEMENT_NODE){
										Element element_4 = (Element) nl_3.item(d);
										String val = XmlUtil.getAttributesValue(element_4,"schemeID");
										eDocumento.setTipoDocumentoEmisor(val);
										//log.info("eDocumento.getTipoDocumentoEmisor(): "+eDocumento.getTipoDocumentoEmisor());
									}	
								}
							}
							
							if(node_2.getNodeName().equals("n1:PartyLegalEntity")) {
								List<TypePartyLegalEntity> partyLegalEntity = new ArrayList<TypePartyLegalEntity>();
								NodeList nl_3 = node_2.getChildNodes();						
								for(int d=0; d < nl_3.getLength(); d++){
									Node node_3 = nl_3.item(d);		
									TypePartyLegalEntity typePartyLegalEntity = new TypePartyLegalEntity();
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName()+" - "+node_3.getTextContent());
									if(node_3.getNodeName().equals("n1:RegistrationAddress")) {	
										TypeAddress registrationAddress = new TypeAddress();
										NodeList nl_4 = node_3.getChildNodes();						
										for(int e=0; e < nl_4.getLength(); e++){
											Node node_4 = nl_4.item(e);
											//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_4.getNodeName()+" - "+node_4.getTextContent());
											if(node_4.getNodeName().equals("n2:ID")) {										
												NodeList nl_5 = node_4.getChildNodes();						
												for(int f=0; f < nl_5.getLength(); f++){
													Node node_5 = nl_5.item(f);
													//log.info(a+"-"+b+"-"+c+"-"+e+"-"+f+" node: " +node_5.getNodeValue()+" - "+node_5.getTextContent());											
													registrationAddress.setId(node_5.getNodeValue());
												}
											}
											if(node_4.getNodeName().equals("n2:AddressTypeCode")) {										
												NodeList nl_5 = node_4.getChildNodes();						
												for(int f=0; f < nl_5.getLength(); f++){
													Node node_5 = nl_5.item(f);
													//log.info(a+"-"+b+"-"+c+"-"+e+"-"+f+" node: " +node_5.getNodeValue()+" - "+node_5.getTextContent());											
													registrationAddress.setAddressTypeCode(node_5.getNodeValue());
												}
											}
										}
										if((registrationAddress.getId()!=null) || (registrationAddress.getAddressTypeCode()!=null)) {
											typePartyLegalEntity.setRegistrationAddress(registrationAddress);
											partyLegalEntity.add(typePartyLegalEntity);
											//log.info("partyLegalEntity.size():  "+partyLegalEntity.size());
										}
									}
								}		
								if(partyLegalEntity.size() > 0){
									party.setPartyLegalEntity(partyLegalEntity);
									//log.info(" party.getListPartyLegalEntity().size(): "+party.getPartyLegalEntity().size());
								}
							}							
						}				
						if((party.getPartyLegalEntity()!= null) &&  (party.getPartyLegalEntity().size() > 0)){
							accountingSupplierParty.setParty(party);
							//log.info(" accountingSupplierParty.getParty().getListPartyLegalEntity().size():  "+accountingSupplierParty.getParty().getPartyLegalEntity().size());
						}
					}	
				}	
				if(accountingSupplierParty.getParty()!= null){
					eDocumento.setAccountingSupplierParty(accountingSupplierParty);
					//log.info(" eDocumento.getAccountingSupplierParty().getParty().getListPartyLegalEntity().size():  "+eDocumento.getAccountingSupplierParty().getParty().getPartyLegalEntity().size());
				}
	        }	        
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getAccountingCustomerParty Exception \n"+errors);	
		}
	}	
	
	public void getAccountingCustomerPartySAP(Document document, EDocumento eDocumento) {
		//log.info("getAccountingCustomerParty");
		try {			
	        //log.info("Root element :" + doc.getDocumentElement().getNodeName());
	        NodeList nl_0 = document.getElementsByTagName("n1:AccountingCustomerParty");	          
	        for(int a=0; a < nl_0.getLength(); a++){
	        	TypeCustomerParty accountingCustomerParty = new TypeCustomerParty();
				Node node_0 = nl_0.item(a);		
				//log.info(a+" node: " +node_0.getNodeName());		
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" node: " +node_1.getNodeName());	
					if(node_1.getNodeName().equals("n1:Party")) {
						TypeParty party = new TypeParty();
						NodeList nl_2 = node_1.getChildNodes();		
						//List<String> listPartyIdentification = new ArrayList<String>();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeName()+" - "+node_2.getNodeValue());
							if(node_2.getNodeName().equals("n1:PartyIdentification")) {
								NodeList nl_3 = node_2.getChildNodes();						
								for(int d=0; d < nl_3.getLength(); d++){
									Node node_3 = nl_3.item(d);		
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName()+" - "+node_3.getTextContent());
									if(node_3.getNodeName().equals("n2:ID")) {										
										NodeList nl_4 = node_3.getChildNodes();						
										for(int e=0; e < nl_4.getLength(); e++){
											Node node_4 = nl_4.item(e);
											//log.info(a+"-"+b+"-"+c+"-"+e+" cac:PartyIdentification/cbc:ID : " +node_4.getNodeValue()+" - "+node_4.getTextContent());												
											eDocumento.setNumeroDocumentoAdquiriente(node_4.getNodeValue());
											//log.info("eDocumento.getNumeroDocumentoAdquiriente():"+eDocumento.getNumeroDocumentoAdquiriente());
										}
									}
									if(nl_3.item(d).getNodeType() == Node.ELEMENT_NODE){
										Element element_4 = (Element) nl_3.item(d);
										String val = XmlUtil.getAttributesValue(element_4,"schemeID");
										eDocumento.setTipoDocumentoAdquiriente(val);
										//log.info("eDocumento.getTipoDocumentoAdquiriente():"+eDocumento.getTipoDocumentoAdquiriente());
									}									
								}
							}
							if(node_2.getNodeName().equals("n1:PartyLegalEntity")) {
								List<TypePartyLegalEntity> partyLegalEntity = new ArrayList<TypePartyLegalEntity>();
								NodeList nl_3 = node_2.getChildNodes();						
								for(int d=0; d < nl_3.getLength(); d++){
									Node node_3 = nl_3.item(d);		
									TypePartyLegalEntity typePartyLegalEntity = new TypePartyLegalEntity();
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName()+" - "+node_3.getTextContent());
									if(node_3.getNodeName().equals("n2:RegistrationAddress")) {	
										TypeAddress registrationAddress = new TypeAddress();
										NodeList nl_4 = node_3.getChildNodes();						
										for(int e=0; e < nl_4.getLength(); e++){
											Node node_4 = nl_4.item(e);
											//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_4.getNodeName()+" - "+node_4.getTextContent());
											if(node_4.getNodeName().equals("n2:ID")) {										
												NodeList nl_5 = node_4.getChildNodes();						
												for(int f=0; f < nl_5.getLength(); f++){
													Node node_5 = nl_5.item(f);
													//log.info(a+"-"+b+"-"+c+"-"+e+"-"+f+" cac:RegistrationAddress/cbc:ID : " +node_5.getNodeValue()+" - "+node_5.getTextContent());											
													registrationAddress.setId(node_5.getNodeValue());
												}
											}
										}
										if(registrationAddress.getId()!=null) {
											typePartyLegalEntity.setRegistrationAddress(registrationAddress);
											partyLegalEntity.add(typePartyLegalEntity);
											//log.info("partyLegalEntity.size():  "+partyLegalEntity.size());
										}
									}
								}		
								if(partyLegalEntity.size() > 0){
									party.setPartyLegalEntity(partyLegalEntity);
									//log.info(" party.getListPartyLegalEntity().size(): "+party.getPartyLegalEntity().size());
								}
							}															
						}						
					}										
				}	
				if(accountingCustomerParty.getParty()!= null){
					eDocumento.setAccountingCustomerParty(accountingCustomerParty);
					//log.info(" eDocumento.getAccountingCustomerParty().getParty().getListPartyLegalEntity().size():  "+eDocumento.getAccountingCustomerParty().getParty().getPartyLegalEntity().size());
				}
	        }
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getAccountingCustomerParty Exception \n"+errors);	
		}
	}
				
	public void getAdditionalDocumentReferenceSAP(Document document, EDocumento eDocumento) {
		//log.info("getAdditionalDocumentReference");
		try {				 
			List<TypeDocumentReference> listTypeDocumentReference = new ArrayList<TypeDocumentReference>();
	        //log.info("Root element :" + doc.getDocumentElement().getNodeName());
	        NodeList nl_0 = document.getElementsByTagName("cac:AdditionalDocumentReference");	          
	        for(int a=0; a < nl_0.getLength(); a++){
	        	TypeDocumentReference typeDocumentReference = new TypeDocumentReference();
				Node node_0 = nl_0.item(a);		
				//log.info(a+" node: " +node_0.getNodeName());		
				NodeList nl_1 = node_0.getChildNodes();
				boolean dato = false;
				for(int b=0; b < nl_1.getLength(); b++){
					if(dato)
						return;
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" node: " +node_1.getNodeName());	
					if(node_1.getNodeName().equals("cbc:ID")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							typeDocumentReference.setId(node_2.getNodeValue());
						}
					}
					if(node_1.getNodeName().equals("cbc:DocumentTypeCode")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeValue());
							typeDocumentReference.setDocumentTypeCode(node_2.getNodeValue());
						}
					}
					if(node_1.getNodeName().equals("cbc:DocumentStatusCode")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeValue());
							typeDocumentReference.setDocumentStatusCode(node_2.getNodeValue());
						}
					}
					
					if(node_1.getNodeName().equals("cac:IssuerParty")) {
						TypeParty issuerParty = new TypeParty();
						NodeList nl_2 = node_1.getChildNodes();		
						List<TypePartyIdentification> partyIdentification = new ArrayList<TypePartyIdentification>();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeName()+" - "+node_2.getNodeValue());
							if(node_2.getNodeName().equals("cac:PartyIdentification")) {
								NodeList nl_3 = node_2.getChildNodes();		
								TypePartyIdentification typePartyIdentification = new TypePartyIdentification();
								for(int d=0; d < nl_3.getLength(); d++){
									Node node_3 = nl_3.item(d);		
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName()+" - "+node_3.getTextContent());
									if(node_3.getNodeName().equals("cbc:ID")) {										
										NodeList nl_4 = node_3.getChildNodes();						
										for(int e=0; e < nl_4.getLength(); e++){
											Node node_4 = nl_4.item(e);
											//log.info(a+"-"+b+"-"+c+"-"+e+" cac:PartyIdentification/cbc:ID : " +node_4.getNodeValue()+" - "+node_4.getTextContent());											
											typePartyIdentification.setId(node_4.getNodeValue());
											partyIdentification.add(typePartyIdentification);											
										}
										
									}
								}
							}
						}						
						if(partyIdentification.size()>0) {
							//log.info("listPartyIdentification.size()  "+listPartyIdentification.size());
							issuerParty.setPartyIdentification(partyIdentification);
							typeDocumentReference.setIssuerParty(issuerParty);
							//log.info("listPartyIdentification.size()  "+typeDocumentReference.getIssuerParty().getPartyIdentification());
						}							
					}
				}	
				listTypeDocumentReference.add(typeDocumentReference);				
	        }
	        eDocumento.setListTypeAdditionalDocumentReference(listTypeDocumentReference);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getAdditionalDocumentReference Exception \n"+errors);	
		}
	}

	public void getLegalMonetaryTotalSAP(Document document, EDocumento eDocumento) {
		//log.info("getLegalMonetaryTotal");
		try {				
	        NodeList nl_0 = document.getElementsByTagName("n1:LegalMonetaryTotal");	  
	        for(int a=0; a < nl_0.getLength(); a++){
	        	TypeMonetaryTotal typeLegalMonetaryTotal = new TypeMonetaryTotal();
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" node: " +node_1.getNodeName());	
					if(node_1.getNodeName().equals("n2:LineExtensionAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeLegalMonetaryTotal.setLineExtensionAmount(bigDecimal);
							//log.info("typeLegalMonetaryTotal.getLineExtensionAmount(): "+typeLegalMonetaryTotal.getLineExtensionAmount());
						}
					}
					if(node_1.getNodeName().equals("n2:TaxInclusiveAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeLegalMonetaryTotal.setTaxInclusiveAmount(bigDecimal);
							//log.info("typeLegalMonetaryTotal.getTaxInclusiveAmount(): "+typeLegalMonetaryTotal.getTaxInclusiveAmount());
						}
					}
					if(node_1.getNodeName().equals("n2:AllowanceTotalAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeLegalMonetaryTotal.setAllowanceTotalAmount(bigDecimal);
							//log.info("typeLegalMonetaryTotal.getAllowanceTotalAmount(): "+typeLegalMonetaryTotal.getAllowanceTotalAmount());
						}
					}
					if(node_1.getNodeName().equals("n2:ChargeTotalAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeLegalMonetaryTotal.setChargeTotalAmount(bigDecimal);
							//log.info("typeLegalMonetaryTotal.getChargeTotalAmount(): "+typeLegalMonetaryTotal.getChargeTotalAmount());
						}
					}
					if(node_1.getNodeName().equals("n2:PrepaidAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeLegalMonetaryTotal.setPrepaidAmount(bigDecimal);
							//log.info("typeLegalMonetaryTotal.getPrepaidAmount(): "+typeLegalMonetaryTotal.getPrepaidAmount());
						}
					}
					if(node_1.getNodeName().equals("n2:PayableRoundingAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeLegalMonetaryTotal.setPayableRoundingAmount(bigDecimal);
							//log.info("typeLegalMonetaryTotal.getPayableRoundingAmount(): "+typeLegalMonetaryTotal.getPayableRoundingAmount());
						}
					}
					if(node_1.getNodeName().equals("n2:PayableAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeLegalMonetaryTotal.setPayableAmount(bigDecimal);
							//log.info("typeLegalMonetaryTotal.getPayableAmount(): "+typeLegalMonetaryTotal.getPayableAmount());
						}
					}										
				}	
				eDocumento.setTypeLegalMonetaryTotal(typeLegalMonetaryTotal);
	        }			
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getLegalMonetaryTotal Exception \n"+errors);	
		}
	}

	public void getRequestedMonetaryTotalSAP(Document document, EDocumento eDocumento) {
		//log.info("getRequestedMonetaryTotal");
		try {				
	        NodeList nl_0 = document.getElementsByTagName("cac:RequestedMonetaryTotal");	  
	        for(int a=0; a < nl_0.getLength(); a++){
	        	TypeMonetaryTotal typeRequestedMonetaryTotal = new TypeMonetaryTotal();
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" node: " +node_1.getNodeName());	
					if(node_1.getNodeName().equals("cbc:LineExtensionAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeRequestedMonetaryTotal.setLineExtensionAmount(bigDecimal);
							//log.info("typeRequestedMonetaryTotal.getLineExtensionAmount(): "+typeRequestedMonetaryTotal.getLineExtensionAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:TaxInclusiveAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeRequestedMonetaryTotal.setTaxInclusiveAmount(bigDecimal);
							//log.info("typeRequestedMonetaryTotal.getTaxInclusiveAmount(): "+typeRequestedMonetaryTotal.getTaxInclusiveAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:AllowanceTotalAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeRequestedMonetaryTotal.setAllowanceTotalAmount(bigDecimal);
							//log.info("typeRequestedMonetaryTotal.getAllowanceTotalAmount(): "+typeRequestedMonetaryTotal.getAllowanceTotalAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:ChargeTotalAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeRequestedMonetaryTotal.setChargeTotalAmount(bigDecimal);
							log.info("typeRequestedMonetaryTotal.getChargeTotalAmount(): "+typeRequestedMonetaryTotal.getChargeTotalAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:PrepaidAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeRequestedMonetaryTotal.setPrepaidAmount(bigDecimal);
							//log.info("typeRequestedMonetaryTotal.getPrepaidAmount(): "+typeRequestedMonetaryTotal.getPrepaidAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:PayableRoundingAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeRequestedMonetaryTotal.setPayableRoundingAmount(bigDecimal);
							//log.info("typeLegalMonetaryTotal.getPayableRoundingAmount(): "+typeRequestedMonetaryTotal.getPayableRoundingAmount());
						}
					}
					if(node_1.getNodeName().equals("cbc:PayableAmount")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							BigDecimal bigDecimal = new BigDecimal(node_2.getNodeValue());
							typeRequestedMonetaryTotal.setPayableAmount(bigDecimal);
							//log.info("typeRequestedMonetaryTotal.getPayableAmount(): "+typeRequestedMonetaryTotal.getPayableAmount());
						}
					}										
				}	
				eDocumento.setTypeRequestedMonetaryTotal(typeRequestedMonetaryTotal);				
	        }			
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getRequestedMonetaryTotal Exception \n"+errors);	
		}
	}	
		
	
}
