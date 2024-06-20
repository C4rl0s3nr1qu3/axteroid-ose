package com.axteroid.ose.server.builder.sendsunat;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
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

import pe.gob.sunat.service.AxteroidOseClient;
import pe.gob.sunat.service.AxteroidOseClientService;
import pe.gob.sunat.service.StatusResponse;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.axteroid.ose.server.tools.util.ZipUtil;

public class SendOseClient {
	private static final Logger log = LoggerFactory.getLogger(SendOseClient.class);			
    public void sendOse(Documento t) throws SOAPException {
		try {
    		log.info("Filename: "+ t.getNombre()+" | TipoComprobante: "+t.getTipoDocumento());
    	   	switch(t.getTipoDocumento()) {
    	   		case "RA":
    	   			this.sendSummary_OSE(t);
    	   			break;
    	   		case "RC":
    	   			this.sendSummary_OSE(t);
    	   			break;
    	   		case "RR":
    	   			this.sendSummary_OSE(t);
    	   			break;
    	   		default:
    	   			this.sendBill_OSE(t);
    	   			break;	    	   				
    	   	} 	
		} catch (Exception e) {
			e.printStackTrace();
    	}
    }
    
    private AxteroidOseClient connecting(Documento tbComprobante, String user) {     	
    	List<String> listUrlSunat = new ArrayList<String>();    
    	listUrlSunat.add(DirUtil.getLinkOseEnvioUblProper(Constantes.CONTENT_TRUE));
    	listUrlSunat.add(DirUtil.getLinkOseEnvioUblProper(Constantes.CONTENT_FALSE));  	
    	for(String link : listUrlSunat) { 	
    		log.info("link; "+link);
    		try {
    			AxteroidOseClient port = this.connectServiceOse(link, user);
	    		if(port!=null)
	    			return port;   		    		
    		}catch (Exception e){
    			StringWriter errors = new StringWriter();				
    			e.printStackTrace(new PrintWriter(errors));
    			log.error("connecting Exception \n"+errors);
    	    }    
    	}
    	return null;
    }   
         
    void sendBill_OSE(Documento t) throws SOAPException{
    	log.info("1) Filename: "+ t.getNombre());
    	try {
    		AxteroidOseClient port = this.connecting(t, t.getUserCrea());
    		if(port!=null) ;    		
    	   	else return;     	        	
    		ByteArrayDataSource ds = new ByteArrayDataSource(t.getUbl(), "");        		        		
        	DataHandler dh = new DataHandler(ds); 
    		byte[] retorno = port.sendBill(t.getNombre(),dh);       
    		byte[] mejorado = ZipUtil.descomprimirArchivoMejorado(retorno);
    		String sendString = new String(mejorado);
			log.info("sendBill.result = " + sendString);  			    			
    	}catch(SOAPFaultException e) {   
    		//log.info("e.getMessage() --> "+e.getMessage());
    		SOAPFault soapFault = e.getFault();
    		Detail detail = soapFault.getDetail();  
    		if(detail !=null) {
    			Iterator<DetailEntry> detailEntries= detail.getDetailEntries();   
    			while (detailEntries.hasNext()) {
    				SOAPBodyElement sbe = (SOAPBodyElement) detailEntries.next();
    				log.info("SOAPFaultException --> "+soapFault.getFaultString()+" : "+sbe.getValue());
    			}    
    	    }else
    	    	log.info("SOAPFaultException --> "+soapFault.getFaultString());
    	    
    	} catch (Exception e) {
    		StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("sendBill Exception \n"+errors);  
    	}	       
    }
    
    void sendSummary_OSE(Documento t) throws SOAPException{   
    	try {
    		AxteroidOseClient port = this.connecting(t, t.getUserCrea());
    		if(port!=null) ;    		
    		else return;      	
    		ByteArrayDataSource ds = new ByteArrayDataSource(t.getUbl(), "");        		        		
        	DataHandler dh = new DataHandler(ds);
    		String retorno = port.sendSummary(t.getNombre(), dh);        
    		log.info("sendSummary.result " + retorno);           		
        }catch(SOAPFaultException e) {        		
    		//log.info("e.getMessage() --> "+e.getMessage());
    		SOAPFault soapFault = e.getFault();
    		Detail detail = soapFault.getDetail();   
    		if(detail !=null) {
    			Iterator<DetailEntry> detailEntries= detail.getDetailEntries();   
    			while (detailEntries.hasNext()) {
    				SOAPBodyElement sbe = (SOAPBodyElement) detailEntries.next();
    				log.info("SOAPFaultException --> "+soapFault.getFaultString()+" : "+sbe.getValue());        	      
    			}    
    		}else
    	    	log.info("SOAPFaultException ticket --> "+soapFault.getFaultString());
        }        	
    }
    
    public byte[] getStatusROBOT(String ticket, Documento t) throws SOAPException{
    	//log.info("Filename: "+ t.getNombre());
    	try {
    		AxteroidOseClient port = this.connecting(t, Constantes.AVATAR_USER); 	
    		if(port!=null) ;    		
    		else return null;    	
    		StatusResponse retorno = port.getStatus(ticket);   
    		if((retorno!=null) && (retorno.getContent().length>0)) {
    			log.info("getSatus.result "+retorno.getStatusCode());
    			byte[] mejorado = ZipUtil.descomprimirArchivoMejorado(retorno.getContent());        			
    			return mejorado;   			
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
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getStatusROBOT Exception \n"+errors);
		}
	    return new byte[0];
    }    
    
    private WSS4JStaxOutInterceptor getClientWSS4J(String userOSE) {  
    	log.info("getClientWSS4J: "+userOSE);
    	Map<String, Object> outProps = new HashMap<String, Object>();
        outProps.put(WSHandlerConstants.ACTION,  WSHandlerConstants.USERNAME_TOKEN);
        outProps.put(WSHandlerConstants.USER, userOSE);
        outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, PasswordCallback.class.getName());
        WSS4JStaxOutInterceptor wss4jout = new WSS4JStaxOutInterceptor(outProps);
        return wss4jout;
    }
    
    private AxteroidOseClient connectServiceOse(String urlSunat, String userOSE) {
    	try{
	        URL wsdlURL = new URL(urlSunat);
	        AxteroidOseClientService ss = new AxteroidOseClientService(wsdlURL);        
	        AxteroidOseClient port = ss.getAxteroidOseServerPort();   
    		if(port!=null){
	        	WSS4JStaxOutInterceptor wss4jout = this.getClientWSS4J(userOSE);
	        	Client client = ClientProxy.getClient(port);       
	        	Endpoint ep =client.getEndpoint();        
	        	ep.getOutInterceptors().add(wss4jout);                    	
	        	HTTPConduit http = (HTTPConduit) client.getConduit();
	        	HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
	        	httpClientPolicy.setConnectionTimeout(60000);
	          	httpClientPolicy.setReceiveTimeout(6000000);
	          	http.setClient( httpClientPolicy );
	    		return port;
    		}
    	} catch (Exception e){
    		StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getConnectSunat Exception \n"+errors);
        }  
    	return null; 
    }    

}
