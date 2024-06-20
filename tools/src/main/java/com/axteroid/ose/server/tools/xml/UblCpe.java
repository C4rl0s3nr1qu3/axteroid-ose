package com.axteroid.ose.server.tools.xml;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.axteroid.ose.server.tools.util.DateUtil;


public class UblCpe {
	private final static Logger log = LoggerFactory.getLogger(XmlSign.class);
	
	public static Date readIssueDateFromUbl(byte[] cpe) {
		log.info("readIssueDateFromUbl : ");
		String formatDate =  "yyyyMMdd";
		Date emisiondate = new Date();
		try {						
			Document document = XmlSign.getByteArray2Document(cpe);
			document.getDocumentElement().normalize();
	        NodeList nl_0 = document.getElementsByTagName("cbc:IssueDate");	  
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){					
					Node node_1 = nl_1.item(b);		
					String fecha_XML = node_1.getNodeValue().replaceAll("-", "");
					//log.info(a+"-"+b+" b) cbc:UBLVersionID : "+ node_1.getNodeName()+" "+node_1.getNodeValue());
					emisiondate = DateUtil.parseDate(fecha_XML, formatDate);
				}
	        }			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return emisiondate;			
	}
	
	public static Date readReferenceDateFromUbl(byte[] cpe) {
		log.info("readReferenceDateFromUbl : ");
		String formatDate =  "yyyyMMdd";
		Date emisiondate = new Date();
		try {						
			Document document = XmlSign.getByteArray2Document(cpe);
			document.getDocumentElement().normalize();
	        NodeList nl_0 = document.getElementsByTagName("cbc:ReferenceDate");	  
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){					
					Node node_1 = nl_1.item(b);		
					String fecha_XML = node_1.getNodeValue().replaceAll("-", "");
					//log.info(a+"-"+b+" b) cbc:UBLVersionID : "+ node_1.getNodeName()+" "+node_1.getNodeValue());
					emisiondate = DateUtil.parseDate(fecha_XML, formatDate);
				}
	        }			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return emisiondate;			
	}
}
