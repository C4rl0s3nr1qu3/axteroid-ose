package com.axteroid.ose.server.rulesubl.xmldoc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;
import com.axteroid.ose.server.tools.xml.XmlUtil;

public class SummaryXmlRead {
	//private static final Logger log = Logger.getLogger(SummaryDocumentsXmlRead.class);
	private static final Logger log = LoggerFactory.getLogger(SummaryXmlRead.class);
	
	public void getSummaryDocumentsLine(Documento tbComprobante, List<EResumenDocumentoItem> items) {
		//log.info("getSummaryDocumentsLine items "+items.size());
		try {			
			Document document = XmlUtil.getByteArray2Document(tbComprobante.getUbl());
	        document.getDocumentElement().normalize();
	        //log.info("Root element :" + document.getDocumentElement().getNodeName());
	        NodeList nl_0 = document.getElementsByTagName("sac:SummaryDocumentsLine");	          
	        for(int a=0; a < nl_0.getLength(); a++){
	        	int j = 0;
				Node node_0 = nl_0.item(a);		
				//log.info(a+" node: " +node_0.getNodeName());		
				NodeList nl_1 = node_0.getChildNodes();
				int idItem = 0;
				for(int b=0; b < nl_1.getLength(); b++){
					if(j>5)
						break;
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" node: " +node_1.getNodeName());	
					if(node_1.getNodeName().equals("cbc:LineID")) {								
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:LineID : " +node_2.getTextContent());
							//log.info(a+"-"+b+"-"+c+" cbc:LineID : " +node_2.getNodeValue());
							idItem = this.getIDItem(items, node_2.getNodeValue());
						}
					}
					if(node_1.getNodeName().equals("cbc:ID")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							items.get(idItem).setId(node_2.getNodeValue());
							j++;
						}
					}
					if(node_1.getNodeName().equals("cac:BillingReference")) {
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){								
							Node node_2 = nl_2.item(c);	
							if(node_2.getNodeName().equals("cac:InvoiceDocumentReference")) {
								NodeList nl_3 = node_2.getChildNodes();
								for(int d=0; d < nl_3.getLength(); d++){	
									Node node_3 = nl_3.item(d);	
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName());
									if(node_3.getNodeName().equals("cbc:ID")) {
										NodeList nl_4 = node_3.getChildNodes();
										for(int e=0; e < nl_4.getLength(); e++){	
											Node node_4 = nl_4.item(e);	
											//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" cac:InvoiceDocumentReference/cbc:ID : " +node_4.getNodeValue());
											items.get(idItem).setBrID(node_4.getNodeValue());
											j++;
										}
									}	
									if(node_3.getNodeName().equals("cbc:DocumentTypeCode")) {
										NodeList nl_4 = node_3.getChildNodes();
										for(int e=0; e < nl_4.getLength(); e++){	
											Node node_4 = nl_4.item(e);	
											//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" cac:InvoiceDocumentReference/cbc:DocumentTypeCode : " +node_4.getNodeValue());
											items.get(idItem).setBrDocumentTypeCode(node_4.getNodeValue());
											j++;
										}
									}	
								}
							}
						}
					}
					if(node_1.getNodeName().equals("cac:Status")) {
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){								
							Node node_2 = nl_2.item(c);	
							if(node_2.getNodeName().equals("cbc:ConditionCode")) {
								NodeList nl_3 = node_2.getChildNodes();
								for(int d=0; d < nl_3.getLength(); d++){	
									Node node_3 = nl_3.item(d);	
									//log.info(a+"-"+b+"-"+c+"-"+d+" cac:Status/cbc:ConditionCode : " +node_3.getNodeValue());
									items.get(idItem).setStatus(Integer.parseInt(node_3.getNodeValue()));
									j++;
								}
							}
						}
					}
					if(node_1.getNodeName().equals("cac:AccountingCustomerParty")) {
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){								
							Node node_2 = nl_2.item(c);	
							if(node_2.getNodeName().equals("cbc:CustomerAssignedAccountID")) {
								NodeList nl_3 = node_2.getChildNodes();
								for(int d=0; d < nl_3.getLength(); d++){	
									Node node_3 = nl_3.item(d);	
									//log.info(a+"-"+b+"-"+c+"-"+d+" cac:AccountingCustomerParty/cbc:CustomerAssignedAccountID : " +node_3.getNodeValue());
									items.get(idItem).setAcpCustomerAssignedAccountID(node_3.getNodeValue());
									j++;
								}
							}
							if(node_2.getNodeName().equals("cbc:AdditionalAccountID")) {
								NodeList nl_3 = node_2.getChildNodes();
								for(int d=0; d < nl_3.getLength(); d++){	
									Node node_3 = nl_3.item(d);	
									//log.info(a+"-"+b+"-"+c+"-"+d+" cac:AccountingCustomerParty/cbc:AdditionalAccountID : " +node_3.getNodeValue());
									items.get(idItem).setAcpAdditionalAccountID(node_3.getNodeValue());
									j++;
								}
							}
						}
					}
					if(node_1.getNodeName().equals("sac:SUNATPerceptionSummaryDocumentReference")) {
						//log.info(a+"-"+b+" sac:SUNATPerceptionSummaryDocumentReference ");
						items.get(idItem).setSunatPerceptionSummaryDocumentReference(true); 
						j++;
					}
				}
			}
	        this.getSummaryDocumentsLineSAP(tbComprobante, items);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getSummaryDocumentsLine Exception \n"+errors);	
	    }
	} 
	
	public void getSummaryDocumentsLineSAP(Documento tbComprobante, List<EResumenDocumentoItem> items) {
		//log.info("getSummaryDocumentsLine items "+items.size());
		try {			
			Document document = XmlUtil.getByteArray2Document(tbComprobante.getUbl());
	        document.getDocumentElement().normalize();
	        //log.info("Root element :" + document.getDocumentElement().getNodeName());
	        NodeList nl_0 = document.getElementsByTagName("n4:SummaryDocumentsLine");	          
	        for(int a=0; a < nl_0.getLength(); a++){
	        	int j = 0;
				Node node_0 = nl_0.item(a);		
				//log.info(a+" node: " +node_0.getNodeName());		
				NodeList nl_1 = node_0.getChildNodes();
				int idItem = 0;
				for(int b=0; b < nl_1.getLength(); b++){
					if(j>5)
						break;
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" node: " +node_1.getNodeName());	
					if(node_1.getNodeName().equals("n2:LineID")) {								
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:LineID : " +node_2.getTextContent());
							//log.info(a+"-"+b+"-"+c+" cbc:LineID : " +node_2.getNodeValue());
							idItem = this.getIDItem(items, node_2.getNodeValue());
						}
					}
					if(node_1.getNodeName().equals("n2:ID")) {							
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeValue());
							items.get(idItem).setId(node_2.getNodeValue());
							j++;
						}
					}
					if(node_1.getNodeName().equals("n1:BillingReference")) {
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){								
							Node node_2 = nl_2.item(c);	
							if(node_2.getNodeName().equals("n1:InvoiceDocumentReference")) {
								NodeList nl_3 = node_2.getChildNodes();
								for(int d=0; d < nl_3.getLength(); d++){	
									Node node_3 = nl_3.item(d);	
									//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName());
									if(node_3.getNodeName().equals("n2:ID")) {
										NodeList nl_4 = node_3.getChildNodes();
										for(int e=0; e < nl_4.getLength(); e++){	
											Node node_4 = nl_4.item(e);	
											//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" cac:InvoiceDocumentReference/cbc:ID : " +node_4.getNodeValue());
											items.get(idItem).setBrID(node_4.getNodeValue());
											j++;
										}
									}	
									if(node_3.getNodeName().equals("n2:DocumentTypeCode")) {
										NodeList nl_4 = node_3.getChildNodes();
										for(int e=0; e < nl_4.getLength(); e++){	
											Node node_4 = nl_4.item(e);	
											//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" cac:InvoiceDocumentReference/cbc:DocumentTypeCode : " +node_4.getNodeValue());
											items.get(idItem).setBrDocumentTypeCode(node_4.getNodeValue());
											j++;
										}
									}	
								}
							}
						}
					}
					if(node_1.getNodeName().equals("n1:Status")) {
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){								
							Node node_2 = nl_2.item(c);	
							if(node_2.getNodeName().equals("n2:ConditionCode")) {
								NodeList nl_3 = node_2.getChildNodes();
								for(int d=0; d < nl_3.getLength(); d++){	
									Node node_3 = nl_3.item(d);	
									//log.info(a+"-"+b+"-"+c+"-"+d+" cac:Status/cbc:ConditionCode : " +node_3.getNodeValue());
									items.get(idItem).setStatus(Integer.parseInt(node_3.getNodeValue()));
									j++;
								}
							}
						}
					}
					if(node_1.getNodeName().equals("n1:AccountingCustomerParty")) {
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){								
							Node node_2 = nl_2.item(c);	
							if(node_2.getNodeName().equals("n2:CustomerAssignedAccountID")) {
								NodeList nl_3 = node_2.getChildNodes();
								for(int d=0; d < nl_3.getLength(); d++){	
									Node node_3 = nl_3.item(d);	
									//log.info(a+"-"+b+"-"+c+"-"+d+" cac:AccountingCustomerParty/cbc:CustomerAssignedAccountID : " +node_3.getNodeValue());
									items.get(idItem).setAcpCustomerAssignedAccountID(node_3.getNodeValue());
									j++;
								}
							}
							if(node_2.getNodeName().equals("n2:AdditionalAccountID")) {
								NodeList nl_3 = node_2.getChildNodes();
								for(int d=0; d < nl_3.getLength(); d++){	
									Node node_3 = nl_3.item(d);	
									//log.info(a+"-"+b+"-"+c+"-"+d+" cac:AccountingCustomerParty/cbc:AdditionalAccountID : " +node_3.getNodeValue());
									items.get(idItem).setAcpAdditionalAccountID(node_3.getNodeValue());
									j++;
								}
							}
						}
					}
					if(node_1.getNodeName().equals("n4:SUNATPerceptionSummaryDocumentReference")) {
						//log.info(a+"-"+b+" sac:SUNATPerceptionSummaryDocumentReference ");
						items.get(idItem).setSunatPerceptionSummaryDocumentReference(true); 
						j++;
					}
				}
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getSummaryDocumentsLineSAP Exception \n"+errors);	
	    }		
	}
	
	private Integer getIDItem(List<EResumenDocumentoItem> items, String id) {
		Integer idItem =0;
		for(EResumenDocumentoItem eResumenDocumentoItem : items){
			if(eResumenDocumentoItem.getLineID().equals(id))
				return idItem;
			idItem++;	
		}		
		return null;
	}
    
	
}
