package com.axteroid.ose.server.rulesubl.xmldoc;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;

public class PerceptionXmlRead {
private static final Logger log = LoggerFactory.getLogger(InvoiceXmlRead.class);
	
	public void getPerceptionDocuments(Document document, EPercepcionDocumento eDocumento) {
		try {				
	        NodeList nl_0 = document.getElementsByTagName("sac:ExceptionalIndicator");	  
	        for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);			
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_2 = nl_1.item(b);		
					log.info(a+"-"+b+" sac:ExceptionalIndicator : " +node_2.getNodeValue());					
					eDocumento.setIndicadorEmisiónExcepcional(node_2.getNodeValue());
					//log.info("eDocumento.getIssueTime(): "+eDocumento.getIssueTime());
				}				
	        }
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getPerceptionDocuments Exception \n"+errors);	
	    }	        
	}
}
