package com.axteroid.ose.server.rulesubl.xmldoc;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.xml.XmlUtil;

public class CdrOseXmlRead {
	private static final Logger log = LoggerFactory.getLogger(CdrOseXmlRead.class);
	
	public void upDateCDR(Documento documento, String sUpDate, String type) {
		//log.info("upDateCDR");
		try {			      
			Document document = XmlUtil.getByteArray2Document(documento.getCdr());
			document.getDocumentElement().normalize();
	        log.info("Root element :" + document.getDocumentElement().getNodeName());
	        NodeList nl_0 = document.getElementsByTagName("cac:DocumentResponse");	 	        
	        boolean activo = false;
	        for(int a=0; a < nl_0.getLength(); a++){
	        	if(activo)
	        		continue;
				Node node_0 = nl_0.item(a);		
				//log.info(a+" node: " +node_0.getNodeName());		
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					if(activo)
		        		continue;
					Node node_1 = nl_1.item(b);	
					//log.info(a+"-"+b+"- node: " +node_1.getNodeName());
					switch(type) {
						case "IssueTime":
							if(node_1.getNodeName().equals("cac:DocumentReference")) {
								NodeList nl_2 = node_1.getChildNodes();
								for(int c=0; c < nl_2.getLength(); c++){	
									if(activo)
						        		continue;
									Node node_2 = nl_2.item(c);	
									//log.info(a+"-"+b+"-"+c+"- node: " +node_2.getNodeName());
									if(node_2.getNodeName().equals("cbc:IssueTime")) {
										NodeList nl_3 = node_2.getChildNodes();
										for(int d=0; d < nl_3.getLength(); d++){	
											Node node_3 = nl_3.item(d);	
											//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName());
											//log.info("sUpDate --> " +sUpDate);
											node_3.setNodeValue(sUpDate);	
											//log.info(a+"-"+b+"- cbc:IssueTime : " +node_3.getNodeValue());	
											activo = true;
										}
									}
								}
							}
							break;
						case "CompanyID":							
							if(node_1.getNodeName().equals("cac:RecipientParty")) {
								NodeList nl_2 = node_1.getChildNodes();
								for(int c=0; c < nl_2.getLength(); c++){
									if(activo)
						        		continue;
									Node node_2 = nl_2.item(c);	
									//log.info(a+"-"+b+"-"+c+"- node: " +node_2.getNodeName());
									if(node_2.getNodeName().equals("cac:PartyLegalEntity")) {
										NodeList nl_3 = node_2.getChildNodes();
										for(int d=0; d < nl_3.getLength(); d++){	
											if(activo)
								        		continue;
											Node node_3 = nl_3.item(d);	
											//log.info(a+"-"+b+"-"+c+"-"+d+" node: " +node_3.getNodeName());
											if(node_3.getNodeName().equals("cbc:CompanyID")) {
												NodeList nl_4 = node_3.getChildNodes();
												for(int e=0; e < nl_4.getLength(); e++){	
													Node node_4 = nl_4.item(e);	
													//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" cbc:CompanyID : " +node_4.getNodeValue());
													//log.info("sUpDate -->  "+sUpDate);
													String[] nodo = sUpDate.split("\\t");
													if(nodo.length==1)
														return;
													String newNodo = "";
													boolean bNodo = true;
													for(String s : nodo) {
														//log.info(" node: " +s);
														if(bNodo) {
															newNodo=s;
															bNodo = false;
															continue;
														}	
														newNodo = newNodo+"\t"+s;									
													}
													node_4.setNodeValue(newNodo);		
													//node_4.setNodeValue(nodo[0]+"	"+nodo[1]);	
													//log.info(a+"-"+b+"-"+c+"-"+d+"-"+e+" cbc:CompanyID : " +node_4.getNodeValue());
													activo = true;
												}
											}	
										}
									}
								}
							}
							break;					
					}
				}	
	        }
	        document.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            //log.info("result.toString() \n"+writer.toString());
            byte [] bt = writer.toString().getBytes();
            documento.setCdr(bt);                        
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("upDateCDR Exception \n"+errors);				
		}
	}	
	
}
