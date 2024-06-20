package com.axteroid.ose.server.avatar.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.security.wss4j.WSS4JStaxOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.FileUtil;
import com.axteroid.ose.server.tools.util.ZipUtil;

import pe.gob.sunat.service.AxteroidOseServerInterface;
import pe.gob.sunat.service.AxteroidOseServerService;
import pe.gob.sunat.service.StatusResponse;

public class SendOseSeverService {	
	private static final Logger log = LoggerFactory.getLogger(SendOseSeverService.class);
	static String directorioZip = Constantes.DIR_AVATAR+Constantes.DIR_AVATAR_CDR;
	 
	public String sendOseSever(String urlSunat, Documento t) throws SOAPException {
		String td = t.getNombre().substring(12, 14);
		String dato = String.valueOf(t.getNombre());
		log.info(" Tipo documento: "+td+ " - Filename: "+ dato+" - ID: "+t.getId());	   	
		if(td.equals(Constantes.SUNAT_COMUNICACION_BAJAS) ||
				td.equals(Constantes.SUNAT_RESUMEN_DIARIO) ||
				td.equals(Constantes.SUNAT_REVERSION)) 
			return this.sendSummary_OSE(urlSunat, t);
		else
			return this.sendBill_OSE(urlSunat, t);
	}	
	 
	private String sendBill_OSE(String urlSunat, Documento t) {    	        	
    	try {    
    		AxteroidOseServerInterface port = this.connect_Service(urlSunat, t.getUserCrea());
        	if(port!=null) ;    		
        	else return "";  
//        	Client client = ClientProxy.getClient(port);    
//        	this.getClientWSS4J(client, t.getUserCrea());
        	String nameFile = t.getNombre().substring(0, t.getNombre().length()-4);					        
	        String nameZip =(new StringBuilder(nameFile)).append(".").append("zip").toString();
	        DataHandler dh = this.getUBLZip(nameFile, t);
    		byte[] retorno = port.sendBill(nameZip,dh);       
    		byte[] mejorado = ZipUtil.descomprimirArchivoMejorado(retorno);
			return new String(mejorado);
    	}catch(SOAPFaultException e) {   
    		//log.info("e.getMessage() --> "+e.getMessage());
    		SOAPFault soapFault = e.getFault();
    		Detail detail = soapFault.getDetail();  
    		if(detail !=null) {
    			Iterator<DetailEntry> detailEntries= detail.getDetailEntries();   
    			while (detailEntries.hasNext()) {
    				SOAPBodyElement sbe = (SOAPBodyElement) detailEntries.next();
    				log.info("sendBill_OSE SOAPFaultException --> "+soapFault.getFaultString()+" : "+sbe.getValue());
    			}    
    	    }else
    	    	log.info("sendBill_OSE SOAPFaultException --> "+soapFault.getFaultString());
    	    return soapFault.getFaultString();
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("sendBill_OSE Exception \n"+errors);
    	}
    	return "";
	}	 
	    
    private String sendSummary_OSE(String urlSunat, Documento t) throws SOAPException{   
    	try {
    		AxteroidOseServerInterface port = this.connect_Service(urlSunat,t.getUserCrea());
        	if(port!=null) ;    		
        	else return"";
//    		Client client = ClientProxy.getClient(port);
//    		this.getClientWSS4J(client,t.getUserCrea());
        	
        	String nameFile = t.getNombre().substring(0, t.getNombre().length()-4);					        
	        String nameZip =(new StringBuilder(nameFile)).append(".").append("zip").toString();
	        DataHandler dh = this.getUBLZip(nameFile, t);  	        
    		String retorno = port.sendSummary(nameZip, dh);        
    		//log.info("sendSummary_OSE.retorno " + retorno);     
    		return "ticket: "+retorno;
    	}catch(SOAPFaultException e) {        		
    		//log.info("e.getMessage() --> "+e.getMessage());
    		SOAPFault soapFault = e.getFault();
    		Detail detail = soapFault.getDetail();   
    		if(detail !=null) {
    			Iterator<DetailEntry> detailEntries= detail.getDetailEntries();   
    			while (detailEntries.hasNext()) {
    				SOAPBodyElement sbe = (SOAPBodyElement) detailEntries.next();
    				log.info("sendSummary_OSE SOAPFaultException --> "+soapFault.getFaultString()+" : "+sbe.getValue());        	      
    			}    
    	    }else
    	    	log.info("sendSummary_OSE SOAPFaultException --> "+soapFault.getFaultString());
    		return soapFault.getFaultString();    		
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("sendSummary_OSE Exception \n"+errors);
    	}	  
    	return "";
    }	    
	
    public byte[] getSatus_ROBOT(String ticket, String urlSunat) throws SOAPException{
    	log.info("getSatus_ROBOT: "+ticket+"| "+ urlSunat);
    	AxteroidOseServerInterface port = this.connect_Service_Robot(urlSunat,Constantes.AVATAR_USER);
    	if(port!=null);
    	else { 
    		log.info("getSatus_ROBOT: No se realizo la Connexion");
    		return null;    	
    	}
    	try {
    		StatusResponse retorno = port.getStatus(ticket);   
    		log.info("getSatus_ROBOT retorno: "+retorno.getContent().length);
    		if(retorno.getContent().length>0) {
    			byte[] mejorado = ZipUtil.descomprimirArchivoMejorado(retorno.getContent());        			
    			return mejorado;   			
    			//String sendString = new String(Base64.getDecoder().decode(_getStatus__return.getContent())) ;
    		}   		
    	}catch(SOAPFaultException e) {        		
    		//log.info("e.getMessage() --> "+e.getMessage());
    		SOAPFault soapFault = e.getFault();
    		Detail detail = soapFault.getDetail(); 
    		if(detail.getDetailEntries()!=null) {
				Iterator<DetailEntry> detailEntries= detail.getDetailEntries();   
			    while (detailEntries.hasNext()) {
			    	SOAPBodyElement sbe = (SOAPBodyElement) detailEntries.next();
			    	log.info("SOAPFaultException --> "+soapFault.getFaultString()+" : "+sbe.getValue());
			      
			    }    
    		}
    	}
	     return null;
    }  
    
    private AxteroidOseServerInterface connect_Service(String urlSunat, String user) {
    	log.info("connect_Service "+urlSunat+" | "+user);
    	try{
	        URL wsdlURL = new URL(urlSunat);
	        AxteroidOseServerService ss = new AxteroidOseServerService(wsdlURL);
	        AxteroidOseServerInterface port = ss.getOseServerPort(); 
	        if(port!=null){
	        	WSS4JStaxOutInterceptor wss4jout = this.getClientWSS4J(user);
	        	Client client = ClientProxy.getClient(port);       
	        	Endpoint ep =client.getEndpoint();        
	        	ep.getOutInterceptors().add(wss4jout);                    	
	        	HTTPConduit http = (HTTPConduit) client.getConduit();
	        	HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
	        	httpClientPolicy.setConnectionTimeout(20000);
	          	httpClientPolicy.setReceiveTimeout(20000);
	          	http.setClient( httpClientPolicy );
    		}
	        return port;
    	} catch (Exception e){
    		e.printStackTrace();
        }  
    	return null; 
    }

    private WSS4JStaxOutInterceptor getClientWSS4J(String user) {  
    	log.info("getClientWSS4J: "+user);
    	Map<String, Object> outProps = new HashMap<String, Object>();
        outProps.put(WSHandlerConstants.ACTION,  WSHandlerConstants.USERNAME_TOKEN);
        outProps.put(WSHandlerConstants.USER, user);
        outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, PasswordCallback.class.getName());
        WSS4JStaxOutInterceptor wss4jout = new WSS4JStaxOutInterceptor(outProps);
        return wss4jout;
    } 
    
    private AxteroidOseServerInterface connect_Service_Robot(String urlSunat, String user) {
    	log.info("connect_Service "+urlSunat+" | "+user);
    	try{
	        URL wsdlURL = new URL(urlSunat);
	        AxteroidOseServerService ss = new AxteroidOseServerService(wsdlURL);
	        AxteroidOseServerInterface port = ss.getOseServerPort(); 
	        if(port!=null){
	        	WSS4JStaxOutInterceptor wss4jout = this.getClientWSS4J_Robot(user);
	        	Client client = ClientProxy.getClient(port);       
	        	Endpoint ep =client.getEndpoint();        
	        	ep.getOutInterceptors().add(wss4jout);                    	
	        	HTTPConduit http = (HTTPConduit) client.getConduit();
	        	HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
	        	httpClientPolicy.setConnectionTimeout(20000);
	          	httpClientPolicy.setReceiveTimeout(20000);
	          	http.setClient( httpClientPolicy );
    		}
	        return port;
    	} catch (Exception e){
    		e.printStackTrace();
        }  
    	return null; 
    }

    private WSS4JStaxOutInterceptor getClientWSS4J_Robot(String user) {  
    	log.info("getClientWSS4J: "+user);
    	Map<String, Object> outProps = new HashMap<String, Object>();
        outProps.put(WSHandlerConstants.ACTION,  WSHandlerConstants.USERNAME_TOKEN);
        outProps.put(WSHandlerConstants.USER, user);
        outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, ROBOT_PRODPasswordCallback.class.getName());
        WSS4JStaxOutInterceptor wss4jout = new WSS4JStaxOutInterceptor(outProps);
        return wss4jout;
    }              
    
    private static Document convertStringToXMLDocument(String xmlString) {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();        
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();             
            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }catch (Exception e){
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("sendBill_OSE \n"+errors);
        }
        return null;
    }
    
    private  boolean GuardarArchivoRemoto(Document doc, String nombreOriginal) {    	
    	try {
    		TransformerFactory transformerFactory = TransformerFactory.newInstance();
    		Transformer transformer = transformerFactory.newTransformer();    		
    		DOMSource source = new DOMSource(doc);
    		File archivoCDR = new File(directorioZip +"R-"+ nombreOriginal + ".XML");
    		FileWriter writer = new FileWriter(archivoCDR);   		
    		StreamResult result = new StreamResult(writer);    		
    		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
    		transformer.transform(source, result);    		
    		writer.close();    		    			
    		return true;    			    					
    	} catch (TransformerConfigurationException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("sendBill_OSE \n"+errors);
    		return false;
    	}catch(TransformerException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("sendBill_OSE \n"+errors);
    		return false;
    	}catch(IOException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("sendBill_OSE \n"+errors);
    		return false;
    	}
    }     
    
    private DataHandler getUBLZip(String nameFile, Documento t) {        
		try {
			List<File> listFile = new ArrayList<File>();
			File fileUBL = FileUtil.writeToFilefromBytes( nameFile, ".XML", t.getUbl());
			listFile.add(fileUBL);
	        byte[] bytesZip = ZipUtil.zipFiles2Byte(listFile); 
	        ByteArrayDataSource ds = new ByteArrayDataSource(bytesZip, "");        		        		
	    	//DataHandler dh = new DataHandler(ds);
	    	return new DataHandler(ds);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getUBLZip \n"+errors);
		}
		return null;       		        		
    }
}
