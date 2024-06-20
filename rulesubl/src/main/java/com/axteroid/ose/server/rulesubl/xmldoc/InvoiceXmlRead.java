package com.axteroid.ose.server.rulesubl.xmldoc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EDocumentoItem;
import com.axteroid.ose.server.tools.ubltype.TypeAddress;
import com.axteroid.ose.server.tools.ubltype.TypeCommodityClassification;
import com.axteroid.ose.server.tools.ubltype.TypeConsignment;
import com.axteroid.ose.server.tools.ubltype.TypeDelivery;
import com.axteroid.ose.server.tools.ubltype.TypeDespatch;
import com.axteroid.ose.server.tools.ubltype.TypeItem;
import com.axteroid.ose.server.tools.ubltype.TypeItemProperty;
import com.axteroid.ose.server.tools.ubltype.TypeLocation;
import com.axteroid.ose.server.tools.ubltype.TypeParty;
import com.axteroid.ose.server.tools.ubltype.TypePartyIdentification;
import com.axteroid.ose.server.tools.ubltype.TypeShipment;
import com.axteroid.ose.server.tools.ubltype.TypeTransportEvent;
import com.axteroid.ose.server.tools.xml.XmlUtil;

public class InvoiceXmlRead {
	private static final Logger log = LoggerFactory.getLogger(InvoiceXmlRead.class);
	
	public void getInvoiceDocuments(Document document, EDocumento eDocumento) {
		//log.info("getInvoiceDocuments");
		try {				
	        NodeList nl_0 = document.getElementsByTagName("cac:Delivery");	
	        List<TypeDelivery> delivery = new ArrayList<TypeDelivery>();
	        for(int a=0; a < nl_0.getLength(); a++){	
				Node node_0 = nl_0.item(a);		
				//log.info(a+" node: " +node_0.getNodeName()+" - "+nl_0.getLength());		
				if(node_0.getNodeName().equals("cac:Delivery")) {
					TypeDelivery typeDelivery = new TypeDelivery();
					NodeList nl_1 = node_0.getChildNodes();					
					for(int b=0; b < nl_1.getLength(); b++){
						Node node_1 = nl_1.item(b);								
						//log.info(a+"-"+b+" node: " +node_1.getNodeName() +" - "+nl_1.getLength());							 
						if(node_1.getNodeName().equals("cac:DeliveryLocation")) {							
							TypeLocation deliveryLocation = new TypeLocation();
							NodeList nl_2 = node_1.getChildNodes();							
							for(int c=0; c < nl_2.getLength(); c++){							
								Node node_2 = nl_2.item(c);	
								//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeName());	
								if(node_2.getNodeName().equals("cac:Address")) {	
									TypeAddress address = new TypeAddress();
									NodeList nl_3 = node_2.getChildNodes();
									for(int d=0; d < nl_3.getLength(); d++){										
										Node node_3 = nl_3.item(d);	
										//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName());
										if(node_3.getNodeName().equals("cbc:ID")) {
											NodeList nl_4 = node_3.getChildNodes();										
											for(int e=0; e < nl_4.getLength(); e++){	
												Node node_4 = nl_4.item(e);	
												//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" node : " +node_4.getNodeValue());	
												address.setId(node_4.getNodeValue());
												//log.info(" typeAddress.getId(): "+address.getId());
												deliveryLocation.setAddress(address);
											}
										}										
									}									
								}
							}
							if((deliveryLocation!=null) && (deliveryLocation.getAddress()!=null)) {
								typeDelivery.setDeliveryLocation(deliveryLocation);
								//log.info(" deliveryLocation.getAddress().getId(): "+deliveryLocation.getAddress().getId());
								delivery.add(typeDelivery);
							}
						}
						if(node_1.getNodeName().equals("cac:Shipment")) {							
							TypeShipment shipment = new TypeShipment();
							NodeList nl_2 = node_1.getChildNodes();							
							for(int c=0; c < nl_2.getLength(); c++){							
								Node node_2 = nl_2.item(c);	
								//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeName());	
								if(node_2.getNodeName().equals("cac:Delivery")) {	
									TypeDelivery shipDelivery = new TypeDelivery();
									NodeList nl_3 = node_2.getChildNodes();
									for(int d=0; d < nl_3.getLength(); d++){										
										Node node_3 = nl_3.item(d);	
										//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName());
										if(node_3.getNodeName().equals("cac:DeliveryAddress")) {
											TypeAddress deliveryAddress = new TypeAddress();
											NodeList nl_4 = node_3.getChildNodes();						
											for(int e=0; e < nl_4.getLength(); e++){
												Node node_4 = nl_4.item(e);
												//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_4.getNodeName()+" - "+node_4.getTextContent());
												if(node_4.getNodeName().equals("cbc:ID")) {										
													NodeList nl_5 = node_4.getChildNodes();						
													for(int f=0; f < nl_5.getLength(); f++){
														Node node_5 = nl_5.item(f);
														//log.info(a+"-"+b+"-"+c+"-"+e+"-"+f+" node: " +node_5.getNodeValue()+" - "+node_5.getTextContent());											
														deliveryAddress.setId(node_5.getNodeValue());
														//log.info(" deliveryAddress.getId(): "+deliveryAddress.getId());
													}
												}
											}
											if(deliveryAddress.getId()!=null) {
												shipDelivery.setDeliveryAddress(deliveryAddress);
												//log.info(" shipDelivery.getDeliveryAddress().getId(): "+shipDelivery.getDeliveryAddress().getId());
											}
										}										
									}																			
									if(shipDelivery.getDeliveryAddress()!=null && shipDelivery.getDeliveryAddress().getId()!=null) {
										shipment.setDelivery(shipDelivery);
										//log.info(" shipment.getDelivery().getDeliveryAddress().getId(): "+shipment.getDelivery().getDeliveryAddress().getId());
									}
								}
								if(node_2.getNodeName().equals("cac:OriginAddress")) {	
									TypeAddress originAddress = new TypeAddress();
									NodeList nl_3 = node_2.getChildNodes();
									for(int d=0; d < nl_3.getLength(); d++){										
										Node node_3 = nl_3.item(d);	
										//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName());
										if(node_3.getNodeName().equals("cbc:ID")) {
											NodeList nl_4 = node_3.getChildNodes();										
											for(int e=0; e < nl_4.getLength(); e++){	
												Node node_4 = nl_4.item(e);	
												//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" node : " +node_4.getNodeValue());	
												originAddress.setId(node_4.getNodeValue());
												//log.info(" originAddress.getId(): "+originAddress.getId());
												shipment.setOriginAddress(originAddress);
											}
										}										
									}																			
								}																
							}
							if((shipment!=null) && ((shipment.getDelivery()!=null) ||
									(shipment.getOriginAddress()!=null))){
								typeDelivery.setShipment(shipment);
								//log.info(" typeDelivery.getShipment().getDelivery().getDeliveryAddress().getId(): "+typeDelivery.getShipment().getDelivery()!=null ? typeDelivery.getShipment().getDelivery().getDeliveryAddress().getId():0);
								delivery.add(typeDelivery);
							}
						}						
					}
				}					
	        }
	        if((delivery!=null) && (delivery.size()>0)) {
	        	eDocumento.setDelivery(delivery);
	        	//log.info(" eDocumento.getDelivery().size(): "+eDocumento.getDelivery().size());
	        }
	        this.getInvoiceDocumentsSAP(document, eDocumento);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getInvoiceDocuments Exception \n"+errors);	
	    }	        
	}
    
	public void getInvoiceDocumentsLine(Document document, List<EDocumentoItem> invoiceLine, String tagName) {
		//log.info("getInvoceDocumentsLine items "+items.size());
		try {			
	        //log.info("Root element :" + document.getDocumentElement().getNodeName());
	        //NodeList nl_0 = document.getElementsByTagName("cac:InvoiceLine");	  
	        NodeList nl_0 = document.getElementsByTagName(tagName);	
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);		
				//log.info("{}) node: {} | {} ",a,node_0.getNodeName(),node_0.getNodeValue());		
				NodeList nl_1 = node_0.getChildNodes();
				int idItem = 0;
				List<TypeDelivery> delivery = new ArrayList<TypeDelivery>();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					//log.info(node_0.getNodeName()+" | {}-{}) node: {} | {}",a,b,node_1.getNodeName(),node_1.getNodeValue());	
					if(node_1.getNodeName().equals("cbc:ID")) {								
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID: {} - {}",node_2.getNodeValue(),node_2.getTextContent());
							idItem = this.getIDItemEDI(invoiceLine, node_2.getNodeValue());	
						}
					}	
					if(node_1.getNodeName().equals("cbc:InvoicedQuantity")) {								
						if(nl_1.item(b).getNodeType() == Node.ELEMENT_NODE){
							Element element_4 = (Element) nl_1.item(b);
							String val = XmlUtil.getAttributesValue(element_4,"unitCode");
							invoiceLine.get(idItem).setUnidadMedidaComercial(val);	
							//log.info(node_1.getNodeName()+" | invoiceLine.get({}).getUnidadMedidaComercial(): ",idItem,invoiceLine.get(idItem).getUnidadMedidaComercial());
						}	
					}	
					if(node_1.getNodeName().equals("cbc:CreditedQuantity")) {								
						if(nl_1.item(b).getNodeType() == Node.ELEMENT_NODE){
							Element element_4 = (Element) nl_1.item(b);
							String val = XmlUtil.getAttributesValue(element_4,"unitCode");
							invoiceLine.get(idItem).setUnidadMedidaComercial(val);	
							//log.info(node_1.getNodeName()+" | invoiceLine.get({}).getUnidadMedidaComercial(): ",idItem,invoiceLine.get(idItem).getUnidadMedidaComercial());
						}	
					}		
					if(node_1.getNodeName().equals("cbc:DebitedQuantity")) {								
						if(nl_1.item(b).getNodeType() == Node.ELEMENT_NODE){
							Element element_4 = (Element) nl_1.item(b);
							String val = XmlUtil.getAttributesValue(element_4,"unitCode");
							invoiceLine.get(idItem).setUnidadMedidaComercial(val);	
							//log.info(node_1.getNodeName()+" | invoiceLine.get({}).getUnidadMedidaComercial(): ",idItem,invoiceLine.get(idItem).getUnidadMedidaComercial());
						}	
					}							
					//log.info("items.get(idItem).getNumeroOrdenItem() : " + items.get(idItem).getNumeroOrdenItem()+" idItem "+idItem);					 
					if(node_1.getNodeName().equals("cac:Item")) {
						NodeList nl_2 = node_1.getChildNodes();		
						//log.info(a+"-"+b+" node: " +node_1.getNodeName());
						TypeItem item = new TypeItem();							
						List<TypeItemProperty> additionalItemProperty = new ArrayList<TypeItemProperty>();
						for(int c=0; c < nl_2.getLength(); c++){							
							Node node_2 = nl_2.item(c);	
							//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeName());	
							List<TypeCommodityClassification> listTypeCommodityClassification = new ArrayList<TypeCommodityClassification>();
							if(node_2.getNodeName().equals("cac:CommodityClassification")) {																
								NodeList nl_3 = node_2.getChildNodes();
								for(int d=0; d < nl_3.getLength(); d++){										
									Node node_3 = nl_3.item(d);	
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName());
									if(node_3.getNodeName().equals("cbc:ItemClassificationCode")) {
										NodeList nl_4 = node_3.getChildNodes();										
										for(int e=0; e < nl_4.getLength(); e++){	
											Node node_4 = nl_4.item(e);	
											//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" cbc:ItemClassificationCode : " +node_4.getNodeValue());	
											TypeCommodityClassification typeCommodityClassification = new TypeCommodityClassification();
											typeCommodityClassification.setItemClassificationCode(node_4.getNodeValue());
											//log.info(" typeCommodityClassification.getItemClassificationCode() -  "+typeCommodityClassification.getItemClassificationCode());
											listTypeCommodityClassification.add(typeCommodityClassification);
										}
									}										
								}									
							}
							//log.info(" listTypeCommodityClassification.size() -  "+listTypeCommodityClassification.size());
							if(listTypeCommodityClassification.size() > 0){
								item.setCommodityClassification(listTypeCommodityClassification);
								//log.info(" item.getCommodityClassification().size() -  "+item.getCommodityClassification().size());
							}
														
							if(node_2.getNodeName().equals("cac:AdditionalItemProperty")) {																
								NodeList nl_3 = node_2.getChildNodes();
								TypeItemProperty typeItemProperty = new TypeItemProperty();
								for(int d=0; d < nl_3.getLength(); d++){										
									Node node_3 = nl_3.item(d);	
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName());									
									if(node_3.getNodeName().equals("cbc:Value")) {
										NodeList nl_4 = node_3.getChildNodes();										
										for(int e=0; e < nl_4.getLength(); e++){	
											Node node_4 = nl_4.item(e);	
											//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" node : " +node_4.getNodeValue());												
											typeItemProperty.setValue(node_4.getNodeValue());
											//log.info("typeItemProperty.getValue() -  "+typeItemProperty.getValue());
										}
									}	
									if(node_3.getNodeName().equals("cbc:NameCode")) {
										NodeList nl_4 = node_3.getChildNodes();										
										for(int e=0; e < nl_4.getLength(); e++){	
											Node node_4 = nl_4.item(e);	
											//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" node : " +node_4.getNodeValue());	
											typeItemProperty.setNameCode(node_4.getNodeValue());
											//log.info("typeItemProperty.getName() -  "+typeItemProperty.getNameCode());											
										}
									}
								}	
								if((typeItemProperty.getValue()!= null) || (typeItemProperty.getNameCode()!= null))
									additionalItemProperty.add(typeItemProperty);
							}							
							if(additionalItemProperty.size() > 0){
								item.setAdditionalItemProperty(additionalItemProperty);
								//log.info(" item.getAdditionalItemProperty() -  "+item.getAdditionalItemProperty().size());
							}								
			            }					
						if(((item.getCommodityClassification()!= null) &&  (item.getCommodityClassification().size() > 0))
								|| ((item.getAdditionalItemProperty()!=null) &&  (item.getAdditionalItemProperty().size() > 0))) {
							invoiceLine.get(idItem).setItem(item);								
//							if((item.getCommodityClassification()!= null) &&  (item.getCommodityClassification().size() > 0))
//								log.info(" invoiceLine.get("+idItem+").getItem().getCommodityClassification().size() -  "+invoiceLine.get(idItem).getItem().getCommodityClassification()!= null ? invoiceLine.get(idItem).getItem().getCommodityClassification().size():0);
//							if(((item.getAdditionalItemProperty()!=null) &&  (item.getCommodityClassification().size() > 0)))
//								log.info(" invoiceLine.get("+idItem+").getItem().getAdditionalItemProperty().size() -  "+invoiceLine.get(idItem).getItem().getAdditionalItemProperty()!= null ? invoiceLine.get(idItem).getItem().getAdditionalItemProperty().size():0);
						}
					}	
					
					if(node_1.getNodeName().equals("cac:OriginatorParty")) {							
						TypeParty originatorParty = new TypeParty();
						NodeList nl_2 = node_1.getChildNodes();		
						List<TypePartyIdentification> partyIdentification = new ArrayList<TypePartyIdentification>();
						for(int c=0; c < nl_2.getLength(); c++){							
							Node node_2 = nl_2.item(c);	
							//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeName());								
							if(node_2.getNodeName().equals("cac:PartyIdentification")) {	
								TypePartyIdentification typePartyIdentification = new TypePartyIdentification();
								NodeList nl_3 = node_2.getChildNodes();
								for(int d=0; d < nl_3.getLength(); d++){										
									Node node_3 = nl_3.item(d);	
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName());
									if(node_3.getNodeName().equals("cbc:ID")) {
										NodeList nl_4 = node_3.getChildNodes();										
										for(int e=0; e < nl_4.getLength(); e++){	
											Node node_4 = nl_4.item(e);	
											//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" node : " +node_4.getNodeValue());	
											typePartyIdentification.setId(node_4.getNodeValue());
											//log.info(" typeAddress.getId(): "+address.getId());
											
										}
									}
									if(nl_3.item(d).getNodeType() == Node.ELEMENT_NODE){
										Element element_4 = (Element) nl_3.item(d);
										String val = XmlUtil.getAttributesValue(element_4,"schemeID");
										typePartyIdentification.setSchemeID(val);
										//log.info("eDocumento.getTipoDocumentoEmisor(): "+eDocumento.getTipoDocumentoEmisor());
									}									
								}
								if(typePartyIdentification.getSchemeID()!=null)
									partyIdentification.add(typePartyIdentification);
							}
						}
						if(partyIdentification.size() > 0){
							originatorParty.setPartyIdentification(partyIdentification);
							//log.info("originatorParty.getPartyIdentification():  "+originatorParty.getPartyIdentification().size());
						}	
						if((originatorParty!=null) && (originatorParty.getPartyIdentification()!=null)) {
							invoiceLine.get(idItem).setOriginatorParty(originatorParty);
						}
					}					
										
					if(node_1.getNodeName().equals("cac:Delivery")) {
						TypeDelivery typeDelivery = new TypeDelivery();
						NodeList nl_2 = node_1.getChildNodes();					
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);								
							//log.info(b+"-"+c+" node: " +node_2.getNodeName() +" - "+nl_2.getLength());							 
							if(node_2.getNodeName().equals("cac:DeliveryLocation")) {							
								TypeLocation deliveryLocation = new TypeLocation();
								NodeList nl_3 = node_2.getChildNodes();							
								for(int d=0; d < nl_3.getLength(); d++){							
									Node node_3 = nl_3.item(d);	
									//log.info(b+"-"+c+"-"+d+" node: " +node_3.getNodeName());	
									if(node_3.getNodeName().equals("cac:Address")) {	
										TypeAddress address = new TypeAddress();
										NodeList nl_4 = node_3.getChildNodes();
										for(int e=0; e < nl_4.getLength(); e++){										
											Node node_4 = nl_4.item(e);	
											//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_4.getNodeName());
											if(node_4.getNodeName().equals("cbc:ID")) {
												NodeList nl_5 = node_4.getChildNodes();										
												for(int f=0; f < nl_5.getLength(); f++){	
													Node node_5 = nl_5.item(f);	
													//log.info(b+"-"+c+"-"+d+"-"+e+"-"+f+" node : " +node_5.getNodeValue());	
													address.setId(node_5.getNodeValue());
													//log.info(" typeAddress.getId(): "+address.getId());
													deliveryLocation.setAddress(address);
												}
											}										
										}									
									}
								}
								if((deliveryLocation!=null) && (deliveryLocation.getAddress()!=null)) {
									typeDelivery.setDeliveryLocation(deliveryLocation);
									//log.info(" typeDelivery.getDeliveryLocation().getAddress().getId(): "+typeDelivery.getDeliveryLocation().getAddress().getId());
								}
							}
							if(node_2.getNodeName().equals("cac:Despatch")) {							
								TypeDespatch despatch = new TypeDespatch();
								NodeList nl_3 = node_2.getChildNodes();							
								for(int d=0; d < nl_3.getLength(); d++){							
									Node node_3 = nl_3.item(d);	
									//log.info(b+"-"+c+"-"+d+" node: " +node_3.getNodeName());	
									if(node_3.getNodeName().equals("cac:DespatchAddress")) {	
										TypeAddress despatchAddress = new TypeAddress();
										NodeList nl_4 = node_3.getChildNodes();
										for(int e=0; e < nl_4.getLength(); e++){										
											Node node_4 = nl_4.item(e);	
											//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_4.getNodeName());
											if(node_4.getNodeName().equals("cbc:ID")) {
												NodeList nl_5 = node_4.getChildNodes();										
												for(int f=0; f < nl_5.getLength(); f++){	
													Node node_5 = nl_5.item(f);	
													//log.info(b+"-"+c+"-"+d+"-"+e+"-"+f+" node : " +node_5.getNodeValue());	
													despatchAddress.setId(node_5.getNodeValue());
													//log.info(" despatchAddress.getId(): "+despatchAddress.getId());
													despatch.setDespatchAddress(despatchAddress);
												}
											}										
										}									
									}
								}
								if((despatch!=null) && (despatch.getDespatchAddress()!=null)) {
									typeDelivery.setDespatch(despatch);
									//log.info(" typeDelivery.getDespatch().getDespatchAddress().getId(): "+typeDelivery.getDespatch().getDespatchAddress().getId());
								}
							}							
							if(node_2.getNodeName().equals("cac:Shipment")) {							
								TypeShipment shipment = new TypeShipment();
								List<TypeConsignment> consignment = new ArrayList<TypeConsignment>();
								NodeList nl_3 = node_2.getChildNodes();							
								for(int d=0; d < nl_3.getLength(); d++){							
									Node node_4 = nl_3.item(d);	
									//log.info(b+"-"+c+"-"+d+" node: " +node_4.getNodeName());	
									if(node_4.getNodeName().equals("cac:Consignment")) {	
										TypeConsignment typeConsignment = new TypeConsignment();
										NodeList nl_5 = node_4.getChildNodes();
										for(int e=0; e < nl_5.getLength(); e++){										
											Node node_5 = nl_5.item(e);	
											//log.info(b+"-"+c+"-"+d+"-"+e+" node: " +node_5.getNodeName());
											if(node_5.getNodeName().equals("cac:PlannedPickupTransportEvent")) {
												TypeTransportEvent plannedDeliveryTransportEvent = new TypeTransportEvent();
												NodeList nl_6 = node_5.getChildNodes();						
												for(int f=0; f < nl_6.getLength(); f++){
													Node node_6 = nl_6.item(f);
													//log.info(b+"-"+c+"-"+d+"-"+e+"-"+f+" node: " +node_6.getNodeName()+" - "+node_6.getTextContent());
													if(node_6.getNodeName().equals("cac:Location")) {
														TypeLocation location = new TypeLocation();
														NodeList nl_7 = node_6.getChildNodes();						
														for(int g=0; g < nl_7.getLength(); g++){
															Node node_7 = nl_7.item(g);
															//log.info(b+"-"+c+"-"+d+"-"+e+"-"+f+"-"+g+" node: " +node_7.getNodeName()+" - "+node_7.getTextContent());
															if(node_7.getNodeName().equals("cbc:ID")) {										
																NodeList nl_8 = node_7.getChildNodes();						
																for(int h=0; h < nl_7.getLength(); h++){
																	Node node_8 = nl_8.item(h);
																	//log.info(b+"-"+c+"-"+d+"-"+e+"-"+f+"-"+g+"-"+h+" node: " +node_8.getNodeValue()+" - "+node_8.getTextContent());											
																	location.setId(node_8.getNodeValue());
																	//log.info(" deliveryAddress.getId(): "+location.getId());																	
																}
															}
														}
														if(location.getId()!=null)
															plannedDeliveryTransportEvent.setLocation(location);
													}
												}
												if((plannedDeliveryTransportEvent.getLocation()!=null))
													typeConsignment.setPlannedDeliveryTransportEvent(plannedDeliveryTransportEvent);
											}										
										}
										if(typeConsignment.getPlannedDeliveryTransportEvent()!= null)
											consignment.add(typeConsignment);
									}										
								}
								if(consignment.size()>0){
									shipment.setConsignment(consignment);
									//log.info(" shipment.getConsignment().get(0).getPlannedDeliveryTransportEvent().getLocation().getId(): "+shipment.getConsignment().get(0).getPlannedDeliveryTransportEvent().getLocation().getId()!=null ? shipment.getConsignment().get(0).getPlannedDeliveryTransportEvent().getLocation().getId():0);
									typeDelivery.setShipment(shipment);
								}
							}
							if(((typeDelivery.getShipment()!= null && typeDelivery.getShipment().getConsignment()!=null) && 
									(typeDelivery.getShipment().getConsignment().size()>0)) || 
									((typeDelivery.getDespatch()!=null) && (typeDelivery.getDespatch().getDespatchAddress()!=null)) ||
									((typeDelivery.getDeliveryLocation()!=null) && (typeDelivery.getDeliveryLocation().getAddress()!=null))){								
								delivery.add(typeDelivery);
								//log.info(" delivery.get(0): ");
							}						
						}
					}
				}
				if((delivery!= null) && (delivery.size()>0)) {
					invoiceLine.get(idItem).setDelivery(delivery);
					//log.info(" invoiceLine.get("+idItem+").getDelivery().size() -  "+invoiceLine.get(idItem).getDelivery()!= null ? invoiceLine.get(idItem).getDelivery().size():0);
				}
			}
	        this.getInvoiceDocumentsLineSAP(document, invoiceLine, tagName);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getInvoiceDocumentsLine Exception \n"+errors);	
	    }
	} 
		
	private Integer getIDItemEDI(List<EDocumentoItem> items, String id) {
		Integer idItem =0;
		for(EDocumentoItem eDocumentoItem : items){
			if(eDocumentoItem.getNumeroOrdenItem().equals(id))
				return idItem;
			idItem++;	
		}		
		return null;
	}	
	

	public void getInvoiceDocumentsSAP(Document document, EDocumento eDocumento) {
		//log.info("getInvoiceDocuments");
		try {				
	        NodeList nl_0 = document.getElementsByTagName("cac:Delivery");	
	        List<TypeDelivery> delivery = new ArrayList<TypeDelivery>();
	        for(int a=0; a < nl_0.getLength(); a++){	
				Node node_0 = nl_0.item(a);		
				//log.info(a+" node: " +node_0.getNodeName()+" - "+nl_0.getLength());		
				if(node_0.getNodeName().equals("cac:Delivery")) {
					TypeDelivery typeDelivery = new TypeDelivery();
					NodeList nl_1 = node_0.getChildNodes();					
					for(int b=0; b < nl_1.getLength(); b++){
						Node node_1 = nl_1.item(b);								
						//log.info(a+"-"+b+" node: " +node_1.getNodeName() +" - "+nl_1.getLength());							 
						if(node_1.getNodeName().equals("cac:DeliveryLocation")) {							
							TypeLocation deliveryLocation = new TypeLocation();
							NodeList nl_2 = node_1.getChildNodes();							
							for(int c=0; c < nl_2.getLength(); c++){							
								Node node_2 = nl_2.item(c);	
								//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeName());	
								if(node_2.getNodeName().equals("cac:Address")) {	
									TypeAddress address = new TypeAddress();
									NodeList nl_3 = node_2.getChildNodes();
									for(int d=0; d < nl_3.getLength(); d++){										
										Node node_3 = nl_3.item(d);	
										//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName());
										if(node_3.getNodeName().equals("cbc:ID")) {
											NodeList nl_4 = node_3.getChildNodes();										
											for(int e=0; e < nl_4.getLength(); e++){	
												Node node_4 = nl_4.item(e);	
												//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" node : " +node_4.getNodeValue());	
												address.setId(node_4.getNodeValue());
												//log.info(" typeAddress.getId(): "+address.getId());
												deliveryLocation.setAddress(address);
											}
										}										
									}									
								}
							}
							if((deliveryLocation!=null) && (deliveryLocation.getAddress()!=null)) {
								typeDelivery.setDeliveryLocation(deliveryLocation);
								//log.info(" deliveryLocation.getAddress().getId(): "+deliveryLocation.getAddress().getId());
								delivery.add(typeDelivery);
							}
						}
						if(node_1.getNodeName().equals("cac:Shipment")) {							
							TypeShipment shipment = new TypeShipment();
							NodeList nl_2 = node_1.getChildNodes();							
							for(int c=0; c < nl_2.getLength(); c++){							
								Node node_2 = nl_2.item(c);	
								//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeName());	
								if(node_2.getNodeName().equals("cac:Delivery")) {	
									TypeDelivery shipDelivery = new TypeDelivery();
									NodeList nl_3 = node_2.getChildNodes();
									for(int d=0; d < nl_3.getLength(); d++){										
										Node node_3 = nl_3.item(d);	
										//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName());
										if(node_3.getNodeName().equals("cac:DeliveryAddress")) {
											TypeAddress deliveryAddress = new TypeAddress();
											NodeList nl_4 = node_3.getChildNodes();						
											for(int e=0; e < nl_4.getLength(); e++){
												Node node_4 = nl_4.item(e);
												//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_4.getNodeName()+" - "+node_4.getTextContent());
												if(node_4.getNodeName().equals("cbc:ID")) {										
													NodeList nl_5 = node_4.getChildNodes();						
													for(int f=0; f < nl_5.getLength(); f++){
														Node node_5 = nl_5.item(f);
														//log.info(a+"-"+b+"-"+c+"-"+e+"-"+f+" node: " +node_5.getNodeValue()+" - "+node_5.getTextContent());											
														deliveryAddress.setId(node_5.getNodeValue());
														//log.info(" deliveryAddress.getId(): "+deliveryAddress.getId());
													}
												}
											}
											if(deliveryAddress.getId()!=null) {
												shipDelivery.setDeliveryAddress(deliveryAddress);
												//log.info(" shipDelivery.getDeliveryAddress().getId(): "+shipDelivery.getDeliveryAddress().getId());
											}
										}										
									}																			
									if(shipDelivery.getDeliveryAddress()!=null && shipDelivery.getDeliveryAddress().getId()!=null) {
										shipment.setDelivery(shipDelivery);
										//log.info(" shipment.getDelivery().getDeliveryAddress().getId(): "+shipment.getDelivery().getDeliveryAddress().getId());
									}
								}
								if(node_2.getNodeName().equals("cac:OriginAddress")) {	
									TypeAddress originAddress = new TypeAddress();
									NodeList nl_3 = node_2.getChildNodes();
									for(int d=0; d < nl_3.getLength(); d++){										
										Node node_3 = nl_3.item(d);	
										//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName());
										if(node_3.getNodeName().equals("cbc:ID")) {
											NodeList nl_4 = node_3.getChildNodes();										
											for(int e=0; e < nl_4.getLength(); e++){	
												Node node_4 = nl_4.item(e);	
												//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" node : " +node_4.getNodeValue());	
												originAddress.setId(node_4.getNodeValue());
												//log.info(" originAddress.getId(): "+originAddress.getId());
												shipment.setOriginAddress(originAddress);
											}
										}										
									}																			
								}																
							}
							if((shipment!=null) && ((shipment.getDelivery()!=null) ||
									(shipment.getOriginAddress()!=null))){
								typeDelivery.setShipment(shipment);
								//log.info(" typeDelivery.getShipment().getDelivery().getDeliveryAddress().getId(): "+typeDelivery.getShipment().getDelivery()!=null ? typeDelivery.getShipment().getDelivery().getDeliveryAddress().getId():0);
								delivery.add(typeDelivery);
							}
						}						
					}
				}					
	        }
	        if((delivery!=null) && (delivery.size()>0)) {
	        	eDocumento.setDelivery(delivery);
	        	//log.info(" eDocumento.getDelivery().size(): "+eDocumento.getDelivery().size());
	        }
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getInvoiceDocuments Exception \n"+errors);	
	    }	        
	}
    
	public void getInvoiceDocumentsLineSAP(Document document, List<EDocumentoItem> invoiceLine, String tagName) {
		//log.info("getInvoceDocumentsLine items "+items.size());
		try {			
	        //log.info("Root element :" + document.getDocumentElement().getNodeName());
	        //NodeList nl_0 = document.getElementsByTagName("cac:InvoiceLine");	  
	        NodeList nl_0 = document.getElementsByTagName(tagName);	
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);		
				//log.info(a+" node: " +node_0.getNodeName()+" - "+nl_0.getLength());		
				NodeList nl_1 = node_0.getChildNodes();
				int idItem = 0;
				List<TypeDelivery> delivery = new ArrayList<TypeDelivery>();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" node: " +node_1.getNodeName() +" - "+nl_1.getLength());	
					if(node_1.getNodeName().equals("n2:ID")) {								
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getTextContent());
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							idItem = this.getIDItemEDI(invoiceLine, node_2.getNodeValue());	
							//items.get(idItem).setLineID(node_2.getNodeValue());
						}
					}	
					//log.info("items.get(idItem).getNumeroOrdenItem() : " + items.get(idItem).getNumeroOrdenItem()+" idItem "+idItem);					 
					if(node_1.getNodeName().equals("n1:Item")) {
						NodeList nl_2 = node_1.getChildNodes();		
						//log.info(a+"-"+b+" node: " +node_1.getNodeName());
						TypeItem item = new TypeItem();							
						List<TypeItemProperty> additionalItemProperty = new ArrayList<TypeItemProperty>();
						for(int c=0; c < nl_2.getLength(); c++){							
							Node node_2 = nl_2.item(c);	
							//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeName());	
							List<TypeCommodityClassification> listTypeCommodityClassification = new ArrayList<TypeCommodityClassification>();
							if(node_2.getNodeName().equals("n1:CommodityClassification")) {																
								NodeList nl_3 = node_2.getChildNodes();
								for(int d=0; d < nl_3.getLength(); d++){										
									Node node_3 = nl_3.item(d);	
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName());
									if(node_3.getNodeName().equals("n2:ItemClassificationCode")) {
										NodeList nl_4 = node_3.getChildNodes();										
										for(int e=0; e < nl_4.getLength(); e++){	
											Node node_4 = nl_4.item(e);	
											//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" cbc:ItemClassificationCode : " +node_4.getNodeValue());	
											TypeCommodityClassification typeCommodityClassification = new TypeCommodityClassification();
											typeCommodityClassification.setItemClassificationCode(node_4.getNodeValue());
											//log.info(" typeCommodityClassification.getItemClassificationCode() -  "+typeCommodityClassification.getItemClassificationCode());
											listTypeCommodityClassification.add(typeCommodityClassification);
										}
									}										
								}									
							}
							//log.info(" listTypeCommodityClassification.size() -  "+listTypeCommodityClassification.size());
							if(listTypeCommodityClassification.size() > 0){
								item.setCommodityClassification(listTypeCommodityClassification);
								//log.info(" item.getCommodityClassification().size() -  "+item.getCommodityClassification().size());
							}
														
							if(node_2.getNodeName().equals("cac:AdditionalItemProperty")) {																
								NodeList nl_3 = node_2.getChildNodes();
								TypeItemProperty typeItemProperty = new TypeItemProperty();
								for(int d=0; d < nl_3.getLength(); d++){										
									Node node_3 = nl_3.item(d);	
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName());									
									if(node_3.getNodeName().equals("cbc:Value")) {
										NodeList nl_4 = node_3.getChildNodes();										
										for(int e=0; e < nl_4.getLength(); e++){	
											Node node_4 = nl_4.item(e);	
											//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" node : " +node_4.getNodeValue());												
											typeItemProperty.setValue(node_4.getNodeValue());
											//log.info("typeItemProperty.getValue() -  "+typeItemProperty.getValue());
										}
									}	
									if(node_3.getNodeName().equals("cbc:NameCode")) {
										NodeList nl_4 = node_3.getChildNodes();										
										for(int e=0; e < nl_4.getLength(); e++){	
											Node node_4 = nl_4.item(e);	
											//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" node : " +node_4.getNodeValue());	
											typeItemProperty.setNameCode(node_4.getNodeValue());
											//log.info("typeItemProperty.getName() -  "+typeItemProperty.getNameCode());											
										}
									}
								}	
								if((typeItemProperty.getValue()!= null) || (typeItemProperty.getNameCode()!= null))
									additionalItemProperty.add(typeItemProperty);
							}							
							if(additionalItemProperty.size() > 0){
								item.setAdditionalItemProperty(additionalItemProperty);
								//log.info(" item.getAdditionalItemProperty() -  "+item.getAdditionalItemProperty().size());
							}								
			            }					
						if(((item.getCommodityClassification()!= null) &&  (item.getCommodityClassification().size() > 0))
								|| ((item.getAdditionalItemProperty()!=null) &&  (item.getAdditionalItemProperty().size() > 0))) {
							invoiceLine.get(idItem).setItem(item);								
//							if((item.getCommodityClassification()!= null) &&  (item.getCommodityClassification().size() > 0))
//								log.info(" invoiceLine.get("+idItem+").getItem().getCommodityClassification().size() -  "+invoiceLine.get(idItem).getItem().getCommodityClassification()!= null ? invoiceLine.get(idItem).getItem().getCommodityClassification().size():0);
//							if(((item.getAdditionalItemProperty()!=null) &&  (item.getCommodityClassification().size() > 0)))
//								log.info(" invoiceLine.get("+idItem+").getItem().getAdditionalItemProperty().size() -  "+invoiceLine.get(idItem).getItem().getAdditionalItemProperty()!= null ? invoiceLine.get(idItem).getItem().getAdditionalItemProperty().size():0);
						}
					}	
					
					if(node_1.getNodeName().equals("cac:OriginatorParty")) {							
						TypeParty originatorParty = new TypeParty();
						NodeList nl_2 = node_1.getChildNodes();		
						List<TypePartyIdentification> partyIdentification = new ArrayList<TypePartyIdentification>();
						for(int c=0; c < nl_2.getLength(); c++){							
							Node node_2 = nl_2.item(c);	
							//log.info(a+"-"+b+"-"+c+" node: " +node_2.getNodeName());								
							if(node_2.getNodeName().equals("cac:PartyIdentification")) {	
								TypePartyIdentification typePartyIdentification = new TypePartyIdentification();
								NodeList nl_3 = node_2.getChildNodes();
								for(int d=0; d < nl_3.getLength(); d++){										
									Node node_3 = nl_3.item(d);	
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName());
									if(node_3.getNodeName().equals("cbc:ID")) {
										NodeList nl_4 = node_3.getChildNodes();										
										for(int e=0; e < nl_4.getLength(); e++){	
											Node node_4 = nl_4.item(e);	
											//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" node : " +node_4.getNodeValue());	
											typePartyIdentification.setId(node_4.getNodeValue());
											//log.info(" typeAddress.getId(): "+address.getId());
											
										}
									}
									if(nl_3.item(d).getNodeType() == Node.ELEMENT_NODE){
										Element element_4 = (Element) nl_3.item(d);
										String val = XmlUtil.getAttributesValue(element_4,"schemeID");
										typePartyIdentification.setSchemeID(val);
										//log.info("eDocumento.getTipoDocumentoEmisor(): "+eDocumento.getTipoDocumentoEmisor());
									}									
								}
								if(typePartyIdentification.getSchemeID()!=null)
									partyIdentification.add(typePartyIdentification);
							}
						}
						if(partyIdentification.size() > 0){
							originatorParty.setPartyIdentification(partyIdentification);
							//log.info("originatorParty.getPartyIdentification():  "+originatorParty.getPartyIdentification().size());
						}	
						if((originatorParty!=null) && (originatorParty.getPartyIdentification()!=null)) {
							invoiceLine.get(idItem).setOriginatorParty(originatorParty);
						}
					}					
										
					if(node_1.getNodeName().equals("cac:Delivery")) {
						TypeDelivery typeDelivery = new TypeDelivery();
						NodeList nl_2 = node_1.getChildNodes();					
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);								
							//log.info(b+"-"+c+" node: " +node_2.getNodeName() +" - "+nl_2.getLength());							 
							if(node_2.getNodeName().equals("cac:DeliveryLocation")) {							
								TypeLocation deliveryLocation = new TypeLocation();
								NodeList nl_3 = node_2.getChildNodes();							
								for(int d=0; d < nl_3.getLength(); d++){							
									Node node_3 = nl_3.item(d);	
									//log.info(b+"-"+c+"-"+d+" node: " +node_3.getNodeName());	
									if(node_3.getNodeName().equals("cac:Address")) {	
										TypeAddress address = new TypeAddress();
										NodeList nl_4 = node_3.getChildNodes();
										for(int e=0; e < nl_4.getLength(); e++){										
											Node node_4 = nl_4.item(e);	
											//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_4.getNodeName());
											if(node_4.getNodeName().equals("cbc:ID")) {
												NodeList nl_5 = node_4.getChildNodes();										
												for(int f=0; f < nl_5.getLength(); f++){	
													Node node_5 = nl_5.item(f);	
													//log.info(b+"-"+c+"-"+d+"-"+e+"-"+f+" node : " +node_5.getNodeValue());	
													address.setId(node_5.getNodeValue());
													//log.info(" typeAddress.getId(): "+address.getId());
													deliveryLocation.setAddress(address);
												}
											}										
										}									
									}
								}
								if((deliveryLocation!=null) && (deliveryLocation.getAddress()!=null)) {
									typeDelivery.setDeliveryLocation(deliveryLocation);
									//log.info(" typeDelivery.getDeliveryLocation().getAddress().getId(): "+typeDelivery.getDeliveryLocation().getAddress().getId());
								}
							}
							if(node_2.getNodeName().equals("cac:Despatch")) {							
								TypeDespatch despatch = new TypeDespatch();
								NodeList nl_3 = node_2.getChildNodes();							
								for(int d=0; d < nl_3.getLength(); d++){							
									Node node_3 = nl_3.item(d);	
									//log.info(b+"-"+c+"-"+d+" node: " +node_3.getNodeName());	
									if(node_3.getNodeName().equals("cac:DespatchAddress")) {	
										TypeAddress despatchAddress = new TypeAddress();
										NodeList nl_4 = node_3.getChildNodes();
										for(int e=0; e < nl_4.getLength(); e++){										
											Node node_4 = nl_4.item(e);	
											//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_4.getNodeName());
											if(node_4.getNodeName().equals("cbc:ID")) {
												NodeList nl_5 = node_4.getChildNodes();										
												for(int f=0; f < nl_5.getLength(); f++){	
													Node node_5 = nl_5.item(f);	
													//log.info(b+"-"+c+"-"+d+"-"+e+"-"+f+" node : " +node_5.getNodeValue());	
													despatchAddress.setId(node_5.getNodeValue());
													//log.info(" despatchAddress.getId(): "+despatchAddress.getId());
													despatch.setDespatchAddress(despatchAddress);
												}
											}										
										}									
									}
								}
								if((despatch!=null) && (despatch.getDespatchAddress()!=null)) {
									typeDelivery.setDespatch(despatch);
									//log.info(" typeDelivery.getDespatch().getDespatchAddress().getId(): "+typeDelivery.getDespatch().getDespatchAddress().getId());
								}
							}							
							if(node_2.getNodeName().equals("cac:Shipment")) {							
								TypeShipment shipment = new TypeShipment();
								List<TypeConsignment> consignment = new ArrayList<TypeConsignment>();
								NodeList nl_3 = node_2.getChildNodes();							
								for(int d=0; d < nl_3.getLength(); d++){							
									Node node_4 = nl_3.item(d);	
									//log.info(b+"-"+c+"-"+d+" node: " +node_4.getNodeName());	
									if(node_4.getNodeName().equals("cac:Consignment")) {	
										TypeConsignment typeConsignment = new TypeConsignment();
										NodeList nl_5 = node_4.getChildNodes();
										for(int e=0; e < nl_5.getLength(); e++){										
											Node node_5 = nl_5.item(e);	
											//log.info(b+"-"+c+"-"+d+"-"+e+" node: " +node_5.getNodeName());
											if(node_5.getNodeName().equals("cac:PlannedPickupTransportEvent")) {
												TypeTransportEvent plannedDeliveryTransportEvent = new TypeTransportEvent();
												NodeList nl_6 = node_5.getChildNodes();						
												for(int f=0; f < nl_6.getLength(); f++){
													Node node_6 = nl_6.item(f);
													//log.info(b+"-"+c+"-"+d+"-"+e+"-"+f+" node: " +node_6.getNodeName()+" - "+node_6.getTextContent());
													if(node_6.getNodeName().equals("cac:Location")) {
														TypeLocation location = new TypeLocation();
														NodeList nl_7 = node_6.getChildNodes();						
														for(int g=0; g < nl_7.getLength(); g++){
															Node node_7 = nl_7.item(g);
															//log.info(b+"-"+c+"-"+d+"-"+e+"-"+f+"-"+g+" node: " +node_7.getNodeName()+" - "+node_7.getTextContent());
															if(node_7.getNodeName().equals("cbc:ID")) {										
																NodeList nl_8 = node_7.getChildNodes();						
																for(int h=0; h < nl_7.getLength(); h++){
																	Node node_8 = nl_8.item(h);
																	//log.info(b+"-"+c+"-"+d+"-"+e+"-"+f+"-"+g+"-"+h+" node: " +node_8.getNodeValue()+" - "+node_8.getTextContent());											
																	location.setId(node_8.getNodeValue());
																	//log.info(" deliveryAddress.getId(): "+location.getId());																	
																}
															}
														}
														if(location.getId()!=null)
															plannedDeliveryTransportEvent.setLocation(location);
													}
												}
												if((plannedDeliveryTransportEvent.getLocation()!=null))
													typeConsignment.setPlannedDeliveryTransportEvent(plannedDeliveryTransportEvent);
											}										
										}
										if(typeConsignment.getPlannedDeliveryTransportEvent()!= null)
											consignment.add(typeConsignment);
									}										
								}
								if(consignment.size()>0){
									shipment.setConsignment(consignment);
									//log.info(" shipment.getConsignment().get(0).getPlannedDeliveryTransportEvent().getLocation().getId(): "+shipment.getConsignment().get(0).getPlannedDeliveryTransportEvent().getLocation().getId()!=null ? shipment.getConsignment().get(0).getPlannedDeliveryTransportEvent().getLocation().getId():0);
									typeDelivery.setShipment(shipment);
								}
							}
							if(((typeDelivery.getShipment()!= null && typeDelivery.getShipment().getConsignment()!=null) && 
									(typeDelivery.getShipment().getConsignment().size()>0)) || 
									((typeDelivery.getDespatch()!=null) && (typeDelivery.getDespatch().getDespatchAddress()!=null)) ||
									((typeDelivery.getDeliveryLocation()!=null) && (typeDelivery.getDeliveryLocation().getAddress()!=null))){								
								delivery.add(typeDelivery);
								//log.info(" delivery.get(0): ");
							}						
						}
					}
				}
				if((delivery!= null) && (delivery.size()>0)) {
					invoiceLine.get(idItem).setDelivery(delivery);
					//log.info(" invoiceLine.get("+idItem+").getDelivery().size() -  "+invoiceLine.get(idItem).getDelivery()!= null ? invoiceLine.get(idItem).getDelivery().size():0);
				}
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getInvoiceDocumentsLine Exception \n"+errors);	
	    }
	} 
		
	
}
