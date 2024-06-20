package com.axteroid.ose.server.tools.xml;

import java.io.ByteArrayInputStream;
import java.io.File;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.axteroid.ose.server.tools.util.DateUtil;

public class XmlSign {
	
	private final static Logger log = LoggerFactory.getLogger(XmlSign.class);
	
	public String readResponseCodeCDR(byte[] bMensaje) {
		log.info("readResponseCodeCDR : ");
		String responseCode = "";
		try {
			Document document = XmlSign.getByteArray2Document(bMensaje);
			document.getDocumentElement().normalize();
	        NodeList nl_0 = document.getElementsByTagName("cac:DocumentResponse");	  
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				//log.info(a+" cac:DocumentResponse : " +node_0.getNodeName()+" - "+node_0.getNodeValue());
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" cac:Response : " +node_1.getNodeName()+" - "+node_1.getNodeValue());					
					if(node_1.getNodeName().equals("cac:Response")) {	
						NodeList nl_2 = node_1.getChildNodes();
						//log.info("nl_2 : "+nl_2.toString());
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							//log.info(a+"-"+b+"-"+c+" cbc:ResponseCode 1 " +node_2.getNodeName()+" - "+node_2.getTextContent());
							//log.info(a+"-"+b+"-"+c+" cbc:ResponseCode 2 " +node_2.getTextContent()+" - "+node_2.toString());
							if(node_2.getNodeName().equals("cbc:ResponseCode")) {		
								log.info(a+"-"+b+"-"+c+" cbc:ResponseCode ------->>> " +node_2.getNodeName());
								return node_2.getNodeName();
								
							}
						}						
					}
				}
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseCode;
	}
	
	public void updateUBL(byte[] bytes, String fechaUBL, String ruc, String rutaCPE, 
			String tipoDoc, String fecha, String rucOLD) {
		try {
			String formatDate =  "yyyyMMdd";
			Date fecha_Test = new Date() ;
			
			boolean ubl = false;
			Document document = XmlSign.getByteArray2Document(bytes);
			document.getDocumentElement().normalize();
	        NodeList nl_0 = document.getElementsByTagName("cbc:UBLVersionID");	  
	        String versionUBL = "";
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){					
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" b) cbc:UBLVersionID : "+ node_1.getNodeName()+" "+node_1.getNodeValue());
					versionUBL = node_1.getNodeValue();
					ubl = true;
					continue;
				}
				if(ubl) continue;
	        }
	        
	        nl_0 = document.getElementsByTagName("cbc:IssueDate");	        
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){					
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" b) cbc:IssueDate : "+ node_1.getNodeName()+" "+node_1.getNodeValue());						
					//System.out.println("fecha_UBL "+ fechaUBL);
					String fechaUBL_2 = fechaUBL.replaceAll("-", "");
					Date fecha_UBL = DateUtil.parseDate(fechaUBL_2, formatDate);
					//System.out.println("fecha_UBL "+ fecha_UBL);
					String[] fecha_Year = node_1.getNodeValue().split("-");
					if(!fecha_Year[0].equals("2020")) {
						String fecha_XML_2 = node_1.getNodeValue().replaceAll("-", "");
						Date fecha_XML = DateUtil.parseDate(fecha_XML_2, formatDate);
						//System.out.println("fecha_XML "+ fecha_XML);
						if(versionUBL.equals("2.0"))
							fecha_Test = DateUtil.parseDate("20180907", formatDate);
						if(versionUBL.equals("2.1"))
							fecha_Test = DateUtil.parseDate("20181004", formatDate);
						if(tipoDoc.equals("09"))
							fecha_Test = DateUtil.parseDate("20180907", formatDate);
						
						//System.out.println("fecha_Test "+ fecha_Test);
						long diffInMillies_Test  = fecha_XML.getTime() - fecha_Test.getTime();
						long diff_Test = TimeUnit.DAYS.convert(diffInMillies_Test, TimeUnit.MILLISECONDS);
						String str_diff = String.valueOf(diff_Test);
						//System.out.println("str_diff "+ str_diff);
						Date date_dif = DateUtil.addDays(fecha_UBL,Integer.parseInt(str_diff));
						//System.out.println("date_dif "+ date_dif);
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
						String fechaUBL_3 = DateUtil.dateFormat(date_dif, simpleDateFormat);					
						//System.out.println("fechaUBL_3 "+ fechaUBL_3);		
						String fechaUBL_14 = fechaUBL;
						fechaUBL = (new StringBuilder(fechaUBL_3.substring(0,4))).append("-").append(fechaUBL_3.substring(4,6)).append("-").append(fechaUBL_3.substring(6,8)).toString();
						node_1.setNodeValue(fechaUBL);					
						//System.out.println("fechaUBL "+ fechaUBL);
						if(tipoDoc.equals("14")) 
							node_1.setNodeValue(fechaUBL_14);
					}else {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
						String fechaUBL_3 = DateUtil.dateFormat(fecha_UBL, simpleDateFormat);	;
						//System.out.println("fechaUBL_3 "+ fechaUBL_3);		
						String fechaUBL_14 = fechaUBL;
						fechaUBL = (new StringBuilder(fechaUBL_3.substring(0,4))).append("-").append(fechaUBL_3.substring(4,6)).append("-").append(fechaUBL_3.substring(6,8)).toString();
						node_1.setNodeValue(fechaUBL);					
						//System.out.println("fechaUBL "+ fechaUBL);
						if(tipoDoc.equals("14")) 
							node_1.setNodeValue(fechaUBL_14);						
					}
					ubl = true;
					continue;
				}
				if(ubl) continue;
	        }
	        
	        if(tipoDoc.equals("30") || tipoDoc.equals("42")) {
		        nl_0 = document.getElementsByTagName("cbc:InvoiceTypeCode");	     
		        for(int a=0; a < nl_0.getLength(); a++){
					Node node_0 = nl_0.item(a);			
					NodeList nl_1 = node_0.getChildNodes();
					for(int b=0; b < nl_1.getLength(); b++){					
						Node node_1 = nl_1.item(b);	
						node_1.setNodeValue(tipoDoc);	
					}
		        }
	        }
	        ubl = false;	
	        if(tipoDoc.equals("09")) 
	        	nl_0 = document.getElementsByTagName("cac:DespatchSupplierParty");	        	
	        else
	        	nl_0 = document.getElementsByTagName("cac:AccountingSupplierParty");	 
	        
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				//log.info(a+" " +node_0.getNodeName()+" - "+node_0.getNodeValue());
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
										
					if((versionUBL.equals("2.0")) || (tipoDoc.equals("09"))){	
						//log.info(a+"-"+b+" cbc:CustomerAssignedAccountID : " +node_1.getNodeName()+" - "+node_1.getNodeValue());
						if(node_1.getNodeName().equals("cbc:CustomerAssignedAccountID")) {
							NodeList nl_2 = node_1.getChildNodes();						
							for(int c=0; c < nl_2.getLength(); c++){
								Node node_2 = nl_2.item(c);
								//log.info(a+"-"+b+"-"+c+" c) cbc:CustomerAssignedAccountID : " +node_2.getNodeValue());								
								//if(node_2.getNodeValue().equals("20176512345"))
								if(node_2.getNodeValue().equals(rucOLD))
									node_2.setNodeValue(ruc);
								ubl = true;
								continue;
							}
						}
					}
					if(versionUBL.equals("2.1")){						
						//log.info(a+"-"+b+" cac:Party : " +node_1.getNodeName()+" - "+node_1.getNodeValue());
						if(node_1.getNodeName().equals("cac:Party")) {
							NodeList nl_2 = node_1.getChildNodes();						
							for(int c=0; c < nl_2.getLength(); c++){
								Node node_2 = nl_2.item(c);
								//log.info(a+"-"+b+"-"+c+" cac:PartyIdentification : " +node_2.getNodeName()+" - "+node_2.getNodeValue());
								if(node_2.getNodeName().equals("cac:PartyIdentification")) {
									NodeList nl_3 = node_2.getChildNodes();						
									for(int d=0; d < nl_3.getLength(); d++){
										Node node_3 = nl_3.item(d);		
										//log.info(a+"-"+b+"-"+c+"-"+d+" cbc:ID : " +node_3.getNodeName()+" - "+node_3.getTextContent());
										if(node_3.getNodeName().equals("cbc:ID")) {										
											NodeList nl_4 = node_3.getChildNodes();						
											for(int e=0; e < nl_4.getLength(); e++){
												Node node_4 = nl_4.item(e);
												//log.info(a+"-"+b+"-"+c+"-"+e+" cbc:ID : " +node_4.getNodeValue()+" - "+node_4.getTextContent());
												//if(node_4.getNodeValue().equals("20176512345"))
												if(node_4.getNodeValue().equals(rucOLD))
													node_4.setNodeValue(ruc);
												ubl = true;
												continue;
											}
										}
									}
								}
							}
						}
					}
				}				
	        }
	        	        		        
	        ubl = false;
	        nl_0 = document.getElementsByTagName("cac:Signature");	        
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				//log.info(a+" cac:Signature : " +node_0.getNodeName()+" - "+node_0.getNodeValue());
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" cac:SignatoryParty : " +node_1.getNodeName()+" - "+node_1.getNodeValue());
					if(node_1.getNodeName().equals("cac:SignatoryParty")) {
						NodeList nl_2 = node_1.getChildNodes();						
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							//log.info(a+"-"+b+"-"+c+" cac:PartyIdentification : " +node_2.getNodeName()+" - "+node_2.getNodeValue());
							if(node_2.getNodeName().equals("cac:PartyIdentification")) {
								NodeList nl_3 = node_2.getChildNodes();						
								for(int d=0; d < nl_3.getLength(); d++){
									Node node_3 = nl_3.item(d);		
									//log.info(a+"-"+b+"-"+c+"-"+d+" cbc:ID : " +node_3.getNodeName()+" - "+node_3.getTextContent());
									if(node_3.getNodeName().equals("cbc:ID")) {										
										NodeList nl_4 = node_3.getChildNodes();						
										for(int e=0; e < nl_4.getLength(); e++){
											Node node_4 = nl_4.item(e);
											//log.info(a+"-"+b+"-"+c+"-"+e+" cbc:ID : " +node_4.getNodeValue()+" - "+node_4.getTextContent());
											//if(node_4.getNodeValue().equals("20176512345"))
											if(node_4.getNodeValue().equals(rucOLD))	
												node_4.setNodeValue(ruc);
											ubl = true;
											continue;
										}
									}
								}
							}
							if(ubl) continue;
						}
					}
					if(ubl) continue;
				}
				if(ubl) continue;
	        }
	        
	        ubl = false;
	        nl_0 = document.getElementsByTagName("cac:AdditionalDocumentReference");	        
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				//log.info(a+" cac:Signature : " +node_0.getNodeName()+" - "+node_0.getNodeValue());
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" cac:SignatoryParty : " +node_1.getNodeName()+" - "+node_1.getNodeValue());
					if(node_1.getNodeName().equals("cac:IssuerParty")) {
						NodeList nl_2 = node_1.getChildNodes();						
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							//log.info(a+"-"+b+"-"+c+" cac:PartyIdentification : " +node_2.getNodeName()+" - "+node_2.getNodeValue());
							if(node_2.getNodeName().equals("cac:PartyIdentification")) {
								NodeList nl_3 = node_2.getChildNodes();						
								for(int d=0; d < nl_3.getLength(); d++){
									Node node_3 = nl_3.item(d);		
									//log.info(a+"-"+b+"-"+c+"-"+d+" cbc:ID : " +node_3.getNodeName()+" - "+node_3.getTextContent());
									if(node_3.getNodeName().equals("cbc:ID")) {										
										NodeList nl_4 = node_3.getChildNodes();						
										for(int e=0; e < nl_4.getLength(); e++){
											Node node_4 = nl_4.item(e);
											//log.info(a+"-"+b+"-"+c+"-"+e+" cbc:ID : " +node_4.getNodeValue()+" - "+node_4.getTextContent());
											//if(node_4.getNodeValue().equals("20176512345"))
											if(node_4.getNodeValue().equals(rucOLD))
												node_4.setNodeValue(ruc);
											ubl = true;
											continue;
										}
									}
								}
							}
							if(ubl) continue;
						}
					}
					if(ubl) continue;
				}
				if(ubl) continue;
	        }
	        
	        ubl = false;	
	        if(tipoDoc.equals("20")) 
	        	nl_0 = document.getElementsByTagName("sac:SUNATRetentionDocumentReference");	        	
	        else if(tipoDoc.equals("40")) 
	        	nl_0 = document.getElementsByTagName("sac:SUNATPerceptionDocumentReference");
	        
	        if(tipoDoc.equals("20") || tipoDoc.equals("40")) {
		        for(int a=0; a < nl_0.getLength(); a++){
					Node node_0 = nl_0.item(a);			
					NodeList nl_1 = node_0.getChildNodes();
					//log.info(a+"  " +node_0.getNodeName()+" - "+node_0.getNodeValue());
					for(int b=0; b < nl_1.getLength(); b++){
						Node node_1 = nl_1.item(b);		
						//log.info(a+"-"+b+" cac:Payment : " +node_1.getNodeName()+" - "+node_1.getNodeValue());
						if(node_1.getNodeName().equals("cac:Payment")) {
							NodeList nl_2 = node_1.getChildNodes();						
							for(int c=0; c < nl_2.getLength(); c++){
								Node node_2 = nl_2.item(c);
								//log.info(a+"-"+b+"-"+c+" cbc:PaidDate : " +node_2.getNodeName()+" - "+node_2.getNodeValue());
								if(node_2.getNodeName().equals("cbc:PaidDate")) {
									NodeList nl_3 = node_2.getChildNodes();						
									for(int d=0; d < nl_3.getLength(); d++){
										Node node_3 = nl_3.item(d);		
										//log.info(a+"-"+b+"-"+c+"-"+d+" cbc:PaidDate : " +node_3.getNodeName()+" - "+node_3.getTextContent());
										node_3.setNodeValue(fechaUBL);
										ubl = true;
										continue;
									}
								}
								if(ubl) continue;
							}
						}
						
						//log.info(a+"-"+b+" sac:SUNATRetentionInformation : " +node_1.getNodeName()+" - "+node_1.getNodeValue());
						if(node_1.getNodeName().equals("sac:SUNATRetentionInformation")) {
							NodeList nl_2 = node_1.getChildNodes();						
							for(int c=0; c < nl_2.getLength(); c++){
								Node node_2 = nl_2.item(c);
								//log.info(a+"-"+b+"-"+c+" sac:SUNATRetentionDate : " +node_2.getNodeName()+" - "+node_2.getNodeValue());
								if(node_2.getNodeName().equals("sac:SUNATRetentionDate")) {
									NodeList nl_3 = node_2.getChildNodes();						
									for(int d=0; d < nl_3.getLength(); d++){
										Node node_3 = nl_3.item(d);		
										//log.info(a+"-"+b+"-"+c+"-"+d+" sac:SUNATRetentionDate : " +node_3.getNodeName()+" - "+node_3.getTextContent());
										node_3.setNodeValue(fechaUBL);
										ubl = true;
										continue;
									}
								}
								if(ubl) continue;
							}
						}
						
						//log.info(a+"-"+b+" sac:SUNATPerceptionInformation : " +node_1.getNodeName()+" - "+node_1.getNodeValue());
						if(node_1.getNodeName().equals("sac:SUNATPerceptionInformation")) {
							NodeList nl_2 = node_1.getChildNodes();						
							for(int c=0; c < nl_2.getLength(); c++){
								Node node_2 = nl_2.item(c);
								//log.info(a+"-"+b+"-"+c+" sac:SUNATPerceptionDate : " +node_2.getNodeName()+" - "+node_2.getNodeValue());
								if(node_2.getNodeName().equals("sac:SUNATPerceptionDate")) {
									NodeList nl_3 = node_2.getChildNodes();						
									for(int d=0; d < nl_3.getLength(); d++){
										Node node_3 = nl_3.item(d);		
										//log.info(a+"-"+b+"-"+c+"-"+d+" sac:SUNATPerceptionDate : " +node_3.getNodeName()+" - "+node_3.getTextContent());
										node_3.setNodeValue(fechaUBL);
										ubl = true;
										continue;
									}
								}
								if(ubl) continue;
							}
						}
					}
		        }
		        
		        ubl = false;
		        nl_0 = document.getElementsByTagName("cac:AgentParty");	
		        for(int a=0; a < nl_0.getLength(); a++){
					Node node_0 = nl_0.item(a);			
					NodeList nl_1 = node_0.getChildNodes();
					//log.info(a+"  " +node_0.getNodeName()+" - "+node_0.getNodeValue());
					for(int b=0; b < nl_1.getLength(); b++){
						Node node_1 = nl_1.item(b);		
						//log.info(a+"-"+b+" cac:PartyIdentification : " +node_1.getNodeName()+" - "+node_1.getNodeValue());
						if(node_1.getNodeName().equals("cac:PartyIdentification")) {
							NodeList nl_2 = node_1.getChildNodes();						
							for(int c=0; c < nl_2.getLength(); c++){
								Node node_2 = nl_2.item(c);
								//log.info(a+"-"+b+"-"+c+" cbc:ID : " +node_2.getNodeName()+" - "+node_2.getNodeValue());
								if(node_2.getNodeName().equals("cbc:ID")) {
									NodeList nl_3 = node_2.getChildNodes();						
									for(int d=0; d < nl_3.getLength(); d++){
										Node node_3 = nl_3.item(d);		
										//log.info(a+"-"+b+"-"+c+"-"+d+" cbc:ID : " +node_3.getNodeName()+" - "+node_3.getTextContent());
										//if(node_3.getNodeValue().equals("20176512345"))
										if(node_3.getNodeValue().equals(rucOLD))
											node_3.setNodeValue(ruc);
										ubl = true;
										continue;
									}
								}
								if(ubl) continue;
							}
						}
						if(ubl) continue;
					}
					if(ubl) continue;
		        }
	        }
	        	 
	        ubl = false;	
	        if(tipoDoc.equals("RC") || tipoDoc.equals("RA") || tipoDoc.equals("RR")) { 
	        	nl_0 = document.getElementsByTagName("cbc:ID");	        
	        	for(int a=0; a < nl_0.getLength(); a++){
	        		Node node_0 = nl_0.item(a);			
	        		NodeList nl_1 = node_0.getChildNodes();
	        		for(int b=0; b < nl_1.getLength(); b++){					
	        			Node node_1 = nl_1.item(b);		
	        			//log.info(a+"-"+b+" b) cbc:ID : "+ node_1.getNodeName()+" "+node_1.getNodeValue());
	        			String[] rr = node_1.getNodeValue().split("-");
	        			String rrNuevo = "";
	        			int i = 0;
                        for(String s : rr ) {             
                        	if(i==0)
                        		rrNuevo = (new StringBuilder(s)).toString();
                        	else if(i==1)
                        		rrNuevo = (new StringBuilder(rrNuevo)).append("-").append(fecha).toString();                        	
                        	else if(i==2)
                        		rrNuevo = (new StringBuilder(rrNuevo)).append("-").append(s).toString();                        		
                        	i++;
                        }
	        			//og.info("rrNuevo --> "+rrNuevo);
	        			if(rr.length>2)
	        				node_1.setNodeValue(rrNuevo);
	        			ubl = true;
	        			continue;
	        		}
	        		if(ubl) continue;
	        	}
	        }
	        
	        if(tipoDoc.equals("RA")) {
		        nl_0 = document.getElementsByTagName("cbc:ReferenceDate");	        
		        for(int a=0; a < nl_0.getLength(); a++){
					Node node_0 = nl_0.item(a);			
					NodeList nl_1 = node_0.getChildNodes();
					for(int b=0; b < nl_1.getLength(); b++){					
						Node node_1 = nl_1.item(b);		
						//log.info(a+"-"+b+" b) cbc:ReferenceDate> : "+ node_1.getNodeName()+" "+node_1.getNodeValue());
						node_1.setNodeValue(fechaUBL);
						ubl = true;
						continue;
					}
					if(ubl) continue;
		        }	        	
	        }
	        
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource domSource = new DOMSource(document);
	        StreamResult streamResult = new StreamResult(new File(rutaCPE));
	        transformer.transform(domSource, streamResult);	        	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public static Document getByteArray2Document(byte[] bytes) {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();	
			return dBuilder.parse(new ByteArrayInputStream(bytes));
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getByteArray2Document Exception \n"+errors);
	    }
        return null;
	}	
	
	public void updateUBLDeleteSign(byte[] bytes, String rutaCPE) {
		System.out.println("updateUBLDeleteSign ... " +rutaCPE);
		try {
			boolean ubl = false;
			Document document = XmlSign.getByteArray2Document(bytes);
			document.getDocumentElement().normalize();
	        NodeList nl_0 = document.getElementsByTagName("ext:UBLExtensions");	  
	        String versionUBL = "";
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes(); 
				//log.info(a+"  " +node_0.getNodeName()+" - "+node_0.getNodeValue());
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" cac:Payment : " +node_1.getNodeName()+" - "+node_1.getNodeValue());
					if(node_1.getNodeName().equals("ext:UBLExtension")) {
						NodeList nl_2 = node_1.getChildNodes();						
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							//log.info(a+"-"+b+"-"+c+" cbc:PaidDate : " +node_2.getNodeName()+" - "+node_2.getNodeValue());
							if(node_2.getNodeName().equals("ext:ExtensionContent")) {
								NodeList nl_3 = node_2.getChildNodes();						
								for(int d=0; d < nl_3.getLength(); d++){
									Node node_3 = nl_3.item(d);		
									//log.info(a+"-"+b+"-"+c+"-"+d+" ds:Signature : " +node_3.getNodeName()+" - "+node_3.getTextContent());
									if(node_3.getNodeName().equals("ds:Signature")) {										
										NodeList nl_4 = node_3.getChildNodes();						
										ubl = true;
										continue;
									}
									if(node_3.getNodeName().equals("Signature")) {										
										NodeList nl_4 = node_3.getChildNodes();						
										ubl = true;
										continue;
									}
								}
							}
							if(ubl) continue;
						}
					}
					if(ubl) {
						//log.info("ubl: "+ubl);
						node_1.getParentNode().removeChild(node_1);
						continue;
					}
				}	
				if(ubl) continue;
	        }  
	        	        
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource domSource = new DOMSource(document);
	        StreamResult streamResult = new StreamResult(new File(rutaCPE));
	        transformer.transform(domSource, streamResult);	        	        
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("updateUBLDeleteSign Exception \n"+errors);
		}
	}	
	
	public void updateUBL_3226(byte[] bytes, String rutaCPE) {
		System.out.println("  updateUBL_3226 ... " +rutaCPE);
		try {
			boolean ubl = false;
			Document document = XmlSign.getByteArray2Document(bytes);
			document.getDocumentElement().normalize();
	        NodeList nl_0 = document.getElementsByTagName("cac:AllowanceCharge");	 	        
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				//log.info(a+" " +node_0.getNodeName()+" - "+node_0.getNodeValue());
				Double base = 0.0;
				Double amount = 0.0;
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);												
					//log.info(a+"-"+b+" cbc:CustomerAssignedAccountID : " +node_1.getNodeName()+" - "+node_1.getNodeValue());					
					if(node_1.getNodeName().equals("cbc:BaseAmount")) {
						NodeList nl_2 = node_1.getChildNodes();						
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							log.info(a+"-"+b+"-"+c+" c) cbc:BaseAmount : " +node_2.getNodeValue());
							base = Double.parseDouble(node_2.getNodeValue());
						}
					}						
					if(node_1.getNodeName().equals("cbc:Amount")) {
						NodeList nl_2 = node_1.getChildNodes();						
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							log.info(a+"-"+b+"-"+c+" c) cbc:Amount : " +node_2.getNodeValue());
							amount = Double.parseDouble(node_2.getNodeValue());
						}
					}
				}
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);	
					Double factor = 0.0;										
					if(node_1.getNodeName().equals("cbc:MultiplierFactorNumeric")) {
						NodeList nl_2 = node_1.getChildNodes();						
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							log.info(a+"-"+b+"-"+c+" c) cbc:MultiplierFactorNumeric : " +node_2.getNodeValue());							
							factor = (amount / base);	
							//String res = df2.format(factor);							
							//String res = String.format("%.2f", factor);
							String res = String.format ("%,.3f", factor);
							String resFinal = res.replace(",", ".");
							log.info(a+"-"+b+"-"+c+" c) factor : " +factor+ " - "+res+ " - "+resFinal);
							node_2.setNodeValue(resFinal);							
						}
					}					
				}
	        }
	        
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource domSource = new DOMSource(document);
	        StreamResult streamResult = new StreamResult(new File(rutaCPE));
	        transformer.transform(domSource, streamResult);	        	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			
	public boolean updateUBLResumen(byte[] bytes, String rutaCPE, String documentTypeCode, 
			String id, String documentLine) {
			boolean bCpeUbl = false;		
		try {
			log.info("updateUBLResumen : "+documentTypeCode+"-"+id);
			Document document = XmlSign.getByteArray2Document(bytes);
			document.getDocumentElement().normalize();
	        NodeList nl_0 = document.getElementsByTagName(documentLine);	
	        String[] arrID = id.split("-");
	        for(int a=0; a < nl_0.getLength(); a++){	        		        	
	        	boolean bDocumentTypeCode = false;
	        	boolean bSerial = false;
	        	boolean bNumber = false;
	        	boolean bID = false;
	        	boolean bDocumentTypeCodeBilling = false;
	        	boolean bIDBilling = false;
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				String typeCode = "";				
				String serial = "";
				String number = "";
				String serieNum = "";
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" " +node_1.getNodeName());
					if(node_1.getNodeName().equals("cbc:DocumentTypeCode")) {
						NodeList nl_2 = node_1.getChildNodes();						
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							typeCode = node_2.getNodeValue();							
							if(typeCode.trim().equals(documentTypeCode.trim())) 																	
								bDocumentTypeCode = true;	
							//log.info(" 1) cbc:DocumentTypeCode : " +documentTypeCode+" | "+typeCode+" | bDocumentTypeCode: "+bDocumentTypeCode);
						}
					}
					if(node_1.getNodeName().equals("sac:DocumentSerialID")) {
						NodeList nl_2 = node_1.getChildNodes();						
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							serial = node_2.getNodeValue();							
							if(serial.trim().equals(arrID[0])) 																	
								bSerial = true;	
							log.info(" 2) sac:DocumentSerialID : " +arrID[0]+" | "+serial+" | bSerial: "+bSerial);
						}
					}
					if(node_1.getNodeName().equals("sac:DocumentNumberID")) {
						NodeList nl_2 = node_1.getChildNodes();						
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							number = node_2.getNodeValue();							
							if(number.trim().equals(arrID[1])) 																	
								bNumber = true;	
							log.info(" 3) sac:DocumentNumberID : " +arrID[1]+" | "+number+" | bNumber: "+bNumber);
						}
					}
					if(node_1.getNodeName().equals("cbc:ID")) {
						NodeList nl_2 = node_1.getChildNodes();						
						for(int c=0; c < nl_2.getLength(); c++){							
							Node node_2 = nl_2.item(c);
							serieNum = node_2.getNodeValue();
							Boolean booSerie = false;
							Boolean booNum = false;							
							String[] arrSerieNum = serieNum.split("-"); 
							
							//log.info( a+"-"+b+"-"+c+" cbc:ID: " +serieNum+" - "+id+" | arrSerieNum: "+arrSerieNum.length+" | arrID; "+arrID.length);
							if(arrSerieNum.length != arrID.length)
								continue;
							if(arrSerieNum.length == 2){
								if(arrSerieNum[0].equals(arrID[0]))
									booSerie = true;								
								int intNum = Integer.parseInt(arrSerieNum[1]);
								int intNumID = Integer.parseInt(arrID[1]);
								if(intNum == intNumID)
									booNum = true;			
								log.info(" 4) cbc:ID | SERIE: " +arrSerieNum[0]+" - "+arrID[0]+" | NUMERO: "+intNum+" - "+intNumID+" | bID "+ bID+" S - N: "+booSerie+" - "+booNum);
							}
							if((booSerie) && (booNum) ) 																	
								bID = true;	
							log.info(" 5) cbc:ID | SERIE: " +arrSerieNum[0]+" - "+arrID[0]+" | NUMERO: "+arrSerieNum[1]+" - "+arrID[1]+" | bID "+ bID+" S - N: "+booSerie+" - "+booNum);
						}
						
					}			
					
					if(node_1.getNodeName().equals("cac:BillingReference")) {
						NodeList nl_2 = node_1.getChildNodes();						
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							//log.info(a+"-"+b+"-"+c+" cac:InvoiceDocumentReference : " +node_2.getNodeName()+" - "+node_2.getNodeValue());
							if(node_2.getNodeName().equals("cac:InvoiceDocumentReference")) {
								NodeList nl_3 = node_2.getChildNodes();						
								for(int d=0; d < nl_3.getLength(); d++){
									Node node_3 = nl_3.item(d);		
									//log.info(a+"-"+b+"-"+c+"-"+d+" cbc:ID : " +node_3.getNodeName()+" - "+node_3.getTextContent());
									if(node_3.getNodeName().equals("cbc:ID")) {										
										NodeList nl_4 = node_3.getChildNodes();						
										for(int e=0; e < nl_4.getLength(); e++){
											Node node_4 = nl_4.item(e);
											//log.info(a+"-"+b+"-"+c+"-"+e+" cbc:ID : " +node_4.getNodeValue()+" - "+node_4.getTextContent());
											serieNum = node_4.getNodeValue();
											Boolean booSerie = false;
											Boolean booNum = false;							
											String[] arrSerieNum = serieNum.split("-"); 
											
											//log.info( a+"-"+b+"-"+c+" cbc:ID: " +serieNum+" - "+id+" | arrSerieNum: "+arrSerieNum.length+" | arrID; "+arrID.length);
											if(arrSerieNum.length != arrID.length)
												continue;
											if(arrSerieNum.length == 2){
												if(arrSerieNum[0].equals(arrID[0]))
													booSerie = true;								
												int intNum = Integer.parseInt(arrSerieNum[1]);
												int intNumID = Integer.parseInt(arrID[1]);
												if(intNum == intNumID)
													booNum = true;			
												//log.info(" 6) cac:BillingReference | SERIE: " +arrSerieNum[0]+" - "+arrID[0]+" | NUMERO: "+intNum+" - "+intNumID+" | bID "+ bID+" S - N: "+booSerie+" - "+booNum);
											}
											if((booSerie) && (booNum) ) 																	
												bIDBilling = true;	
											log.info(" 7) cac:BillingReference | SERIE: " +arrSerieNum[0]+" - "+arrID[0]+" | NUMERO: "+arrSerieNum[1]+" - "+arrID[1]+" | bID "+ bID+" S - N: "+booSerie+" - "+booNum);

										}
									}
									log.info(a+"-"+b+"-"+c+" cac:BillingReference cbc:DocumentTypeCode : " +node_2.getNodeName()+" - "+node_2.getNodeValue());
									if(node_3.getNodeName().equals("cbc:DocumentTypeCode")) {
										NodeList nl_4 = node_3.getChildNodes();						
										for(int e=0; e < nl_4.getLength(); e++){
											Node node_5 = nl_4.item(e);
											typeCode = node_5.getNodeValue();	
											log.info("typeCode "+typeCode);
											if(typeCode.trim().equals(documentTypeCode.trim())) 																	
												bDocumentTypeCodeBilling = true;	
											log.info(" 8) cac:BillingReference cbc:DocumentTypeCode : " +documentTypeCode+" | "+typeCode+" | bDocumentTypeCodeBilling: "+bDocumentTypeCodeBilling);
										}								
									}									
								}
							}

						}
					}
					if(bDocumentTypeCode)
						log.info(a+" 9) >>>>>>>>>>> "+ documentTypeCode + "-"+id+" | "+ typeCode + "-"+serieNum+" | boolean: "+bDocumentTypeCode+" - "+bID);
					if(bDocumentTypeCodeBilling)
						log.info(a+" 10) >>>>>>>>>>> cac:BillingReference: "+ documentTypeCode + "-"+id+" | "+ typeCode + "-"+serieNum+" | boolean: "+bDocumentTypeCodeBilling+" - "+bIDBilling);
					if((bDocumentTypeCode) && (bID)) 
						continue;
					if((bDocumentTypeCode) && (bSerial) && (bNumber)) 
						continue;
					if((bDocumentTypeCodeBilling) && (bIDBilling)) 
						continue;
				}					
				if((bDocumentTypeCode) && (bID)) {
					node_0.getParentNode().removeChild(node_0);
					bCpeUbl = true;
					continue;
				}
				if((bDocumentTypeCode) && (bSerial) && (bNumber)) {
					node_0.getParentNode().removeChild(node_0);
					bCpeUbl = true;
					continue;
				}
				if((bDocumentTypeCodeBilling) && (bIDBilling)) {
					node_0.getParentNode().removeChild(node_0);
					bCpeUbl = true;
					continue;
				}
				if(bDocumentTypeCode)
					log.info(a+" 11) >>>>>>>>>>> "+ documentTypeCode + "-"+id+" | "+ typeCode + "-"+serieNum+" | boolean: "+bDocumentTypeCode+" - "+bID);
				if(bDocumentTypeCodeBilling)
					log.info(a+" 12) >>>>>>>>>>> cac:BillingReference:  "+ documentTypeCode + "-"+id+" | "+ typeCode + "-"+serieNum+" | boolean: "+bDocumentTypeCodeBilling+" - "+bIDBilling);
	        } 	        
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource domSource = new DOMSource(document);
	        StreamResult streamResult = new StreamResult(new File(rutaCPE));
	        transformer.transform(domSource, streamResult);	        	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bCpeUbl;
	}
	
	public boolean countCpeUBLResumen(byte[] bytes, String documentTypeCode, String id, String documentLine) {
		boolean bCpeUbl = false;
		try {
			log.info("countCpeUBLResumen : "+documentTypeCode+"-"+id);
			Document document = XmlSign.getByteArray2Document(bytes);
			document.getDocumentElement().normalize();			
	        NodeList nl_0 = document.getElementsByTagName(documentLine);	 	        
	        for(int a=0; a < nl_0.getLength(); a++){
	        	boolean bDocumentTypeCode = false;
	        	boolean bID = false;
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				String typeCode = "";
				String serieNum = "";
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					//log.info(a+"-"+b+" cbc:DocumentTypeCode : " +node_1.getNodeName());
					if(node_1.getNodeName().equals("cbc:DocumentTypeCode")) {
						NodeList nl_2 = node_1.getChildNodes();						
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);
							typeCode = node_2.getNodeValue();
							//log.info(a+"-"+b+"-"+c+" cbc:DocumentTypeCode : " +typeCode);
							if(typeCode.equals(documentTypeCode)) 																	
								bDocumentTypeCode = true;								
						}
					}
					if(node_1.getNodeName().equals("cbc:ID")) {
						NodeList nl_2 = node_1.getChildNodes();						
						for(int c=0; c < nl_2.getLength(); c++){							
							Node node_2 = nl_2.item(c);
							serieNum = node_2.getNodeValue();
							Boolean booSerie = false;
							Boolean booNum = false;							
							String[] arrSerieNum = serieNum.split("-"); 
							String[] arrID = id.split("-");
							//log.info(a+"-"+b+"-"+c+" cbc:ID: " +serieNum+" - "+id+" | arrSerieNum: "+arrSerieNum.length+" | arrID; "+arrID.length);
							if(arrSerieNum.length != arrID.length)
								continue;
							if(arrSerieNum.length == 2){
								if(arrSerieNum[0].equals(arrID[0]))
									booSerie = true;								
								int intNum = Integer.parseInt(arrSerieNum[1]);
								int intNumID = Integer.parseInt(arrID[1]);
								if(intNum == intNumID)
									booNum = true;			
								//log.info(a+"-"+b+"-"+c+") SERIE: " +arrSerieNum[0]+" - "+arrID[0]+" | NUMERO: "+intNum+" - "+intNumID);
							}
							if((booSerie) && (booNum) ) 																	
								bID = true;	
						}
					}	
					if((bDocumentTypeCode) && (bID)) 
						continue;
				}	
				if((bDocumentTypeCode) && (bID)) {										
					log.info(" CPE: " +documentTypeCode+"-"+id);
					bCpeUbl = true;
					continue;
				}
	        } 	        	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bCpeUbl;
	}
	
	public static String ObtenerFechaEmisionFromUBL(byte[] doc) {
    	try {
    		Document document = XmlUtil.getByteArray2Document(doc);
            document.getDocumentElement().normalize();
            String date = document.getElementsByTagName("cbc:IssueDate").item(0).getTextContent();
            log.info("ISSUE DATE: " + date);
            return date;
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("ObtenerFechaEmisionFromUBL Exception \n"+errors);
		} 
    	return "";
    }	
	
	public static String ObtenerFechaEmisionFromUBLResumen(byte[] doc) {
    	try {
    		Document document = XmlUtil.getByteArray2Document(doc);
            document.getDocumentElement().normalize();
            String date = document.getElementsByTagName("cbc:ReferenceDate").item(0).getTextContent();
            log.info("REFERENCE DATE: " + date);
            return date;
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("ObtenerFechaEmisionFromUBLResumen Exception \n"+errors);
		} 
    	return "";
    }	
}
