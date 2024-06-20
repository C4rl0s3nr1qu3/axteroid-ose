package com.axteroid.ose.server.tools.xml;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.axteroid.ose.server.tools.ubltype.TypeParty;

public class XmlUtil {
	//private static final Logger log = Logger.getLogger(XMLUtil.class);
	private static final Logger log = LoggerFactory.getLogger(XmlUtil.class);
	
	public static Document getString2Document(String xmlRecords) {
		//log.info("getString2Document ");
		try {
			//log.info("xmlRecords \n"+xmlRecords);
			InputSource is = new InputSource();
			StringReader reader = new StringReader(xmlRecords);
			is.setCharacterStream(reader);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();	
			return dBuilder.parse(is);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getString2Document Exception \n"+errors);
	    }
        return null;
	}
	
	public static Document getByteArray2Document(byte[] bytes) {
		String doc = bytes.toString();
		log.info("getByteArray2Document doc: {}",doc);
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
	
	public static String getContent2Filename(byte[] content) {
		String responseCode = "";
		if(content.length == 0)
			return responseCode;
		String ruc = "";
		String td = "";
		String id = "";
		String serie = "";
		try {
			Document document = XmlUtil.getByteArray2Document(content);
			document.getDocumentElement().normalize();
	        NodeList nl_0 = document.getElementsByTagName("cbc:ID");	  
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_2 = nl_1.item(b);		
					//log.info(a+"-"+b+" cbc:ID : " +node_2.getNodeValue());	
					id = node_2.getNodeValue();
					serie = id.substring(0, 1);
					if((serie.equals("V")) || (serie.equals("T")))
						a = nl_0.getLength()+10;							
					//log.info("eDocumento.getIssueTime(): "+eDocumento.getIssueTime());
				}				
	        }
	        //log.info("Root element :" + document.getDocumentElement().getNodeName());
	        if((serie.equals("V")) || (serie.equals("T"))) {
		        nl_0 = document.getElementsByTagName("cbc:DespatchAdviceTypeCode");	  
		        for(int a=0; a < nl_0.getLength(); a++){
					Node node_0 = nl_0.item(a);			
					NodeList nl_1 = node_0.getChildNodes();
					for(int b=0; b < nl_1.getLength(); b++){
						Node node_2 = nl_1.item(b);		
						//log.info(a+"-"+b+" cbc:DespatchAdviceTypeCode : " +node_2.getNodeValue());					
						td = node_2.getNodeValue();
						//log.info("{}-{}) eDocumento.getTipoDocumento() {}",a,b,eDocumento.getTipoDocumento());
					}				
		        }    
		        nl_0 = document.getElementsByTagName("cac:DespatchSupplierParty");	          
		        for(int a=0; a < nl_0.getLength(); a++){
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
												ruc = node_4.getNodeValue();
												//log.info("eDocumento.getNumeroDocumentoEmisor(): "+eDocumento.getNumeroDocumentoEmisor());
											}
										}	
									}
								}
															
							}				
						}	
					}	
		        }
	        }
	        responseCode = ruc+"-"+td+"-"+id+".XML";
	        //log.info("responseCode: {}",responseCode);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getContent2Filename \n"+errors);
		}
		return responseCode.trim();
	}	
	
    private static boolean checkIfNodeExists(Document document, String xpathExpression) throws Exception{
        boolean matches = false;         
        // Create XPathFactory object
        XPathFactory xpathFactory = XPathFactory.newInstance(); 
        // Create XPath object
        XPath xpath = xpathFactory.newXPath(); 
        try {
            // Create XPathExpression object
            XPathExpression expr = xpath.compile(xpathExpression); 
            // Evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);             
            if(nodes != null  && nodes.getLength() > 0) {
                matches = true;
            } 
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return matches;
    }	
    
    public static void listAllAttributes(Element element) {       
    	//log.info("listAllAttributes for node: " + element.getNodeName());        
        // get a map containing the attributes of this node 
        NamedNodeMap attributes = element.getAttributes(); 
        // get the number of nodes in this map
        int numAttrs = attributes.getLength(); 
        for (int i = 0; i < numAttrs; i++) {
            Attr attr = (Attr) attributes.item(i);            
            String attrName = attr.getNodeName();
            String attrValue = attr.getNodeValue();            
//            log.info("Found attribute: " + attrName + " with value: " + attrValue);             
        }
    }    
    
    public static String getAttributesValue(Element element, String nodeName) {       
    	//log.info("listAllAttributes for node: " + element.getNodeName());        
        NamedNodeMap attributes = element.getAttributes(); 
        int numAttrs = attributes.getLength(); 
        for (int i = 0; i < numAttrs; i++) {
            Attr attr = (Attr) attributes.item(i);   
            //log.info("attribute name: " + attr.getNodeName() + " - value: " + attr.getNodeValue());
            if(attr.getNodeName().equals(nodeName))
            	return attr.getNodeValue();                                  
        }
        return "";
    }    
}
