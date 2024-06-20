package com.axteroid.ose.server.tools.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.axteroid.ose.server.tools.bean.ArResponse;
import com.axteroid.ose.server.tools.bean.BeanResponse;
import com.axteroid.ose.server.tools.bean.CdrSunatResponse;

public class UblResponseSunat {
	private static final Logger log = LoggerFactory.getLogger(UblResponseSunat.class);
	//private static final Logger log = Logger.getLogger(CdrSunatXmlReader.class);
	//private static final Logger log = LoggerFactory.getLogger(CdrSunatXmlReader.class);
	
	public String readResponseCodeCDR(byte[] bMensaje) {
		//log.info("readResponseCodeCDR ");
		String responseCode = "";
		if(bMensaje.length == 0)
			return responseCode;
		try {
			Document document = XmlUtil.getByteArray2Document(bMensaje);
			document.getDocumentElement().normalize();
	        NodeList nl_0 = document.getElementsByTagName("cac:DocumentResponse");	  
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					if(node_1.getNodeName().equals("cac:Response")) {	
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							if(node_2.getNodeName().equals("cbc:ResponseCode")) {		
								log.info(a+"-"+b+"-"+c+" cbc:ResponseCode: " +node_2.getTextContent());
								return node_2.getTextContent();
								
							}
						}						
					}
				}
	        }
	        
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("readResponseCodeCDR \n"+errors);
		}
		return responseCode;
	}
	
	public String readResponseCodeCDRNote(byte[] bMensaje) {
		//String m = new String(bMensaje);
		//log.info("readResponseCodeCDRNote \n "+m);
		String responseCode = "";
		if(bMensaje.length == 0)
			return responseCode;
		try {
			Document document = XmlUtil.getByteArray2Document(bMensaje);
			document.getDocumentElement().normalize();
	        NodeList nl_0 = document.getElementsByTagName("cbc:Note");	        
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){					
					Node node_1 = nl_1.item(b);		
					log.info(a+"-"+b+" cbc:Note : "+ node_1.getNodeName()+" "+node_1.getNodeValue());					
					if(responseCode.isEmpty())
						responseCode = node_1.getNodeValue();
					else
						responseCode = responseCode+" | "+node_1.getNodeValue();
				}
	        }	        
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("readResponseCodeCDRNote \n"+errors);
		}
		return responseCode;
	}
	
	public String readResponseCodeCDRDescription(byte[] bMensaje) {
		//String m = new String(bMensaje);
		//log.info("readResponseCodeCDRDescription \n "+m);
		String responseCode = "";
		if(bMensaje.length == 0)
			return responseCode;
		try {
			Document document = XmlUtil.getByteArray2Document(bMensaje);
			document.getDocumentElement().normalize();
	        NodeList nl_0 = document.getElementsByTagName("cbc:Description");	        
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){					
					Node node_1 = nl_1.item(b);		
					log.info(a+"-"+b+" cbc:Description : "+ node_1.getNodeValue());					
					if(responseCode.isEmpty())
						responseCode = node_1.getNodeValue();
					else
						responseCode = responseCode+"; "+node_1.getNodeValue();
				}
	        }	        
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("readResponseCodeCDRDescription \n"+errors);
		}
		return responseCode;
	}
	
	public static String readValueFromXML(String xml, String field) {
		Pattern pattern = Pattern.compile(field+">(.*?)<\\/");
		Matcher ma = pattern.matcher(xml);
		if(ma.find(1)) {
			return ma.group(1);
		}
		else return null;		
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
			log.error("Exception \n"+errors);
		} 
    	return "";
    }	
	
	public static String ObtenerFechaEmisionFromUBL_RC(byte[] doc) {
    	try {
    		Document document = XmlUtil.getByteArray2Document(doc);
            document.getDocumentElement().normalize();
            String date = document.getElementsByTagName("cbc:ReferenceDate").item(0).getTextContent();
            log.info("REFERENCE DATE: " + date);
            return date;
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);
		} 
    	return "";
    }	
	   
	public static byte[] extractXMLFromCDR(byte[] zip) {
		byte[] xml;
		ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zip));
		ZipEntry zipEntry = null;
		try {
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				if (!zipEntry.isDirectory()) {
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					IOUtils.copy(zipInputStream, byteArrayOutputStream);
					byteArrayOutputStream.close();					
					zipInputStream.close();
					xml = byteArrayOutputStream.toByteArray();
					return xml;
				}				
			}
		} catch (IOException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("extractXMLFromCDR Exception \n"+errors);
			return null;
		}
		return null;
	} 	
	
	public static Map<String,String> readStatusFromCDR(String base64CDR) {		
		byte[] cdr = base64CDR.getBytes();
		byte[] zip = Base64.decodeBase64(cdr);
		byte[] xml = extractXMLFromCDR(zip);		
		String xmlString = new String(xml,Charset.forName("UTF-8"));
		//System.out.println("xmlString: \n"+xmlString);
		Map<String,String> fields = new HashMap<String, String>();
		String respCode = UblResponseSunat.readValueFromXML(xmlString, "ResponseCode");
		if(respCode!=null) fields.put("responseCode", respCode);
		String desc = UblResponseSunat.readValueFromXML(xmlString, "Description");
		if(desc!=null) fields.put("description", desc);		
		return fields;
	}	
	
	public static ArResponse readStatus2AR(byte[] ar) {	
		//log.info("readStatusFromAR ");
		ArResponse arResponse = new ArResponse();
		try {
			//byte[] zip = Base64.decodeBase64(ar);
			//byte[] xml = extractXMLFromCDR(zip);		
			String xmlString = new String(ar,Charset.forName("UTF-8"));
			//log.info("readStatusFromAR {}",xmlString);
			//System.out.println("xmlString: \n"+xmlString);
			//Map<String,String> fields = new HashMap<String, String>();			
			String respCode = UblResponseSunat.readValueFromXML(xmlString, "ResponseCode");
			if(respCode!=null) 
				arResponse.setResponseCode(respCode);
				//fields.put("responseCode", respCode);
			String desc = UblResponseSunat.readValueFromXML(xmlString, "Description");
			if(desc!=null) 
				arResponse.setDescription(desc);
				//fields.put("description", desc);	
			List<String> note = readResponseCodeARNote(ar);
			arResponse.setNote(note);
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));			
			log.error("readStatusFromAR Exception: "+e.getMessage());
		}
		return arResponse;
	}			
	
	public static CdrSunatResponse readStatus2CdrSunat(byte[] ar) {	
		//log.info("readStatus2CdrSunat ");
		CdrSunatResponse cdrSunatResponse = new CdrSunatResponse();
		try {
			//byte[] zip = Base64.decodeBase64(ar);
			//byte[] xml = extractXMLFromCDR(zip);		
			String xmlString = new String(ar,Charset.forName("UTF-8"));
			//log.info("readStatusFromAR {}",xmlString);
			//System.out.println("xmlString: \n"+xmlString);
			//Map<String,String> fields = new HashMap<String, String>();			
			String respCode = UblResponseSunat.readValueFromXML(xmlString, "ResponseCode");
			if(respCode!=null) 
				cdrSunatResponse.setResponseCode(respCode);
				//fields.put("responseCode", respCode);
			String desc = UblResponseSunat.readValueFromXML(xmlString, "Description");
			if(desc!=null) 
				cdrSunatResponse.setDescription(desc);
				//fields.put("description", desc);	
			List<String> status = readResponseCodeARNote(ar);
			cdrSunatResponse.setStatus(status);
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));			
			log.error("readStatus2CdrSunat Exception: "+e.getMessage());
		}
		return cdrSunatResponse;
	}	
	
	public static List<String> readResponseCodeARNote(byte[] xml) {
		//log.info("readResponseCodeCDRNote ");
		String m = new String(xml);
		//log.info("readResponseCodeCDRNote \n "+m);
		List<String> note = new ArrayList<String>();;
		if(xml.length == 0)
			return note;
		try {			
			Document document = XmlUtil.getByteArray2Document(xml);
			document.getDocumentElement().normalize();
	        NodeList nl_0 = document.getElementsByTagName("cbc:Note");	        
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){					
					Node node_1 = nl_1.item(b);		
					log.info(a+"-"+b+" cbc:Note : "+ node_1.getNodeName()+" "+node_1.getNodeValue());	
					note.add(node_1.getNodeValue());
//					if(responseCode.isEmpty())
//						responseCode = node_1.getNodeValue();
//					else
//						responseCode = responseCode+" | "+node_1.getNodeValue();
				}
	        }	        
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("readResponseCodeARNote \n"+errors);
		}
		return note;
	}	
	
	public static ArResponse readResponseWS(byte[] bMensaje) {
		//log.info("readResponseCodeCDR ");
		ArResponse arResponse = new ArResponse();
		if(bMensaje.length == 0)
			return arResponse;
		try {
			Document document = XmlUtil.getByteArray2Document(bMensaje);
			document.getDocumentElement().normalize();
	        NodeList nl_0 = document.getElementsByTagName("soap-env:Fault");	  
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);					
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);		
					if(node_1.getNodeName().equals("detail")) {	
						NodeList nl_2 = node_1.getChildNodes();
						for(int c=0; c < nl_2.getLength(); c++){
							Node node_2 = nl_2.item(c);		
							if(node_2.getNodeName().equals("message")) {		
								log.info(a+"-"+b+"-"+c+" message: " +node_2.getTextContent());
								arResponse.setDescription(node_2.getTextContent());
								
							}
						}						
					}
					if(node_1.getNodeName().equals("faultstring")) {	
						NodeList nl_2 = node_1.getChildNodes();
						log.info(a+"-"+b+" message: " +node_1.getTextContent());
						arResponse.setResponseCode(node_1.getTextContent());			
					}
				}
	        }
	        
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("readResponseWS \n"+errors);
		}
		return arResponse;
	}
}
