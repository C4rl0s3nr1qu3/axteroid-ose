package com.axteroid.ose.server.sunat.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.security.wss4j.WSS4JStaxOutInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.bean.SunatBeanRequest;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.axteroid.ose.server.tools.util.DocumentUtil;
import com.axteroid.ose.server.tools.util.ZipUtil;

import pe.gob.sunat.service.BillServiceSunatInterface;
import pe.gob.sunat.service.BillServiceSunat;
import pe.gob.sunat.service.StatusResponse;
import pe.gob.sunat.service.StatusResponseAR;

public class SendSunatClientCxf {	
	private static final Logger log = LoggerFactory.getLogger(SendSunatClientCxf.class);
	
    public SendSunatClientCxf() {}
    
    private BillServiceSunatInterface connecting(Documento documento) {     	
    	List<String> listUrlSunat = DirUtil.getLinkEnvioCDRSunatProper();    
    	for(String link : listUrlSunat) { 	
    		try {
    			BillServiceSunatInterface port = this.getConnectSunat(link);
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
    
    private BillServiceSunatInterface connectingAR(Documento documento) {     	
    	List<String> listUrlSunat = DirUtil.getLinkEnvioCDRSunatProper();
    	for(String link : listUrlSunat) { 	
    		try {
    			BillServiceSunatInterface port = this.getConnectSunatAR(link);
	    		if(port!=null)
	    			return port;   		    		
    		}catch (Exception e){
    			StringWriter errors = new StringWriter();				
    			e.printStackTrace(new PrintWriter(errors));
    			log.error("connectingAR Exception \n"+errors);
    	    }    
    	}
    	return null;
    }  
    
    public void sendBill(Documento documento) {    
    	try{    
        	Timestamp timestampStartSunat = new Timestamp(System.currentTimeMillis());
        	log.info("sendBill  SUNAT");
            org.apache.xml.security.Init.init();
            documento.setFechaEnvioSunat(DateUtil.getCurrentTimestamp());            
            BillServiceSunatInterface port = this.connecting(documento);              
            if(port!=null) {                    	
            	SunatBeanRequest statusResponseBean = SunatFileResponseUtil.getStatusResponseBean(documento);            	   
            	ByteArrayDataSource ds = new ByteArrayDataSource(statusResponseBean.getContent(), "");        		        		
            	DataHandler dh = new DataHandler(ds);   
            	log.info("sendBill SUNAT Enviando: " + statusResponseBean.getFilename()); 
            	byte[] _sendBill__return = port.sendBill(statusResponseBean.getFilename(), dh, "");   
            	documento.setEstado(Constantes.SUNAT_CDR_DETENIDO); 
            	//this.getFileSUNATZip("SUNAT-"+documento.getNombre(), _sendBill__return);           	
            	byte[] bytes = ZipUtil.descomprimirArchivoMejorado(_sendBill__return);
            	documento.setMensajeSunat(bytes); 
            	documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());             	
            	documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDBILL);  
            	
            	if((documento.getMensajeSunat()!=null) && (documento.getMensajeSunat().length > 0))
            		SunatFileResponseUtil.readRespuestaSunat(documento);
            	else
            		documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_NO_FOUND);
            	
            	Timestamp timestampEndSunat = new Timestamp(System.currentTimeMillis());
            	log.info("sendBill timestampSendSUNAT: "+
            			DocumentUtil.compareTwoTimeStamps(timestampEndSunat, timestampStartSunat));            	            	          	  	
            }else {
            	documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_SERVICIO_INHABILITADO); 
            	documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());     
            	documento.setEstado(Constantes.SUNAT_CDR_SERVICIO_INHABILITADO);
            }            	
    	}catch(SOAPFaultException e) {   
			StringWriter errors1 = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors1));
			log.error("sendBill Exception1 \n"+errors1); 
    		SOAPFault soapFault = e.getFault();
    		Detail detail = soapFault.getDetail();   
    		if(detail !=null) {
    			Iterator<DetailEntry> detailEntries= detail.getDetailEntries();   
    			while (detailEntries.hasNext()) {
    				SOAPBodyElement sbe = (SOAPBodyElement) detailEntries.next();
    				documento.setErrorLogSunat(sbe.getValue());
    				log.error("sendBill  SOAPFaultException soapFault: "+soapFault.getFaultString()+" - "+sbe.getValue());        	      
    			}        			
    	    }   		
    		documento.setRespuestaSunat(soapFault.getFaultString());
    		documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDBILL);
    		if(!(documento.getRespuestaSunat()!=null) || (documento.getRespuestaSunat().length()>4)) {    			
    			documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_Not_Acceptable);
    			if((documento.getErrorLogSunat()==null) || (documento.getErrorLogSunat().isEmpty()))
    				documento.setErrorLogSunat(e.toString());
    			documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDBILL);
    		}    	
    		documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp()); 
        }catch (Exception e) {
        	documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_NO_FOUND);
			documento.setErrorLogSunat(e.toString());
			documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());    
			documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDBILL);
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("sendBill Exception \n"+errors);       
        }	
    }
    
    public void sendSummary(Documento documento) {   
        try{
            Timestamp timestampStartSunat = new Timestamp(System.currentTimeMillis());
            log.info("sendSummary  SUNAT");
            org.apache.xml.security.Init.init();
            documento.setFechaEnvioSunat(DateUtil.getCurrentTimestamp());
            BillServiceSunatInterface port = this.connecting(documento);  
            if(port!=null) {
            	SunatBeanRequest statusResponseBean = SunatFileResponseUtil.getStatusResponseBean(documento);            	           	
            	ByteArrayDataSource ds = new ByteArrayDataSource(statusResponseBean.getContent(), "");        		        		
            	DataHandler dh = new DataHandler(ds);   
            	log.info("sendSummary SUNAT Enviando: " + statusResponseBean.getFilename());  
            	String sendSummaryReturn = port.sendSummary(statusResponseBean.getFilename(), dh, "");              	
            	documento.setEstado(Constantes.SUNAT_CDR_DETENIDO);            	
            	documento.setErrorLog(sendSummaryReturn);
            	documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());               	  
            	getStatus(documento);
            	if(!(documento.getMensajeSunat()!=null) || (documento.getMensajeSunat().length == 0))
            		this.getStatus(documento);
            	            	
            	documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDSUMMARY);   
            	if((documento.getMensajeSunat()!=null) && (documento.getMensajeSunat().length > 0))
            		SunatFileResponseUtil.readRespuestaSunat(documento);  
            	
            	Timestamp timestampEndSunat = new Timestamp(System.currentTimeMillis());
            	log.info("sendSummary timestampSendSUNAT: "+DocumentUtil.compareTwoTimeStamps(timestampEndSunat, timestampStartSunat));                	
            }else {
            	documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_SERVICIO_INHABILITADO); 
            	documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());  
            	documento.setEstado(Constantes.SUNAT_CDR_SERVICIO_INHABILITADO);
            }        
    	}catch(SOAPFaultException e) {   
    		log.error("sendSummary SOAPFaultException: "+e.getMessage()+" - "+e.getCause());       		
    		SOAPFault soapFault = e.getFault();
    		Detail detail = soapFault.getDetail();   
    		if(detail !=null) {
    			Iterator<DetailEntry> detailEntries= detail.getDetailEntries();   
    			while (detailEntries.hasNext()) {
    				SOAPBodyElement sbe = (SOAPBodyElement) detailEntries.next();
    				documento.setErrorLogSunat(sbe.getValue());
    				log.error("sendSummary SOAPFaultException soapFault: "+soapFault.getFaultString()+" - "+sbe.getValue());        	      
    			}        			
    	    }   
    		log.error("sendSummary SOAPFaultException soapFault.getFaultString(): "+soapFault.getFaultString());  
    		documento.setRespuestaSunat(soapFault.getFaultString());
    		documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDSUMMARY);
    		if(!(documento.getRespuestaSunat()!=null) || (documento.getRespuestaSunat().length()>4)) {    			
    			documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_Not_Acceptable);
    			if((documento.getErrorLogSunat()==null) || (documento.getErrorLogSunat().isEmpty()))
    				documento.setErrorLogSunat(e.toString());
    			documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDSUMMARY);
    		}    						
    		documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());    
        } catch (Exception e) {
        	documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_NO_FOUND);
			documento.setErrorLogSunat(e.toString());
			documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());  
			documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDSUMMARY);
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("sendSummary Exception \n"+errors);       
        }	
    }   
    
    public void getStatus(Documento documento) {
    	//log.info("getStatus "+ documento.toString()+" | " +documento.getErrorLog()); 
    	if(!(documento.getMensajeSunat()!=null) || (documento.getMensajeSunat().length == 0)) 
    		this.getStatusOnly(documento);
    }

    public void getStatusOnly(Documento documento) {
    	log.info("getStatusOnly: {} | {}", documento.toString(),documento.getErrorLog());  
    	Timestamp timestampStart = new Timestamp(System.currentTimeMillis()); 
		try {       	   		   
    		org.apache.xml.security.Init.init();
    		BillServiceSunatInterface port = this.connecting(documento);   
    		if(port!=null) {           	
        		StatusResponse statusResponse = port.getStatus(documento.getErrorLog());  
        		documento.setRespuestaSunat(statusResponse.getStatusCode());     
        		byte[] bytes = ZipUtil.descomprimirArchivoMejorado(statusResponse.getContent());
            	documento.setMensajeSunat(bytes);            	
        		documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp()); 
        		SunatFileResponseUtil.readRespuestaSunat(documento);
    			log.info("getStatusOnly: {} | {}", documento.getEstado(),documento.getRespuestaSunat());        		
        	}else {
        		documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_SERVICIO_INHABILITADO); 
        		documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());     
        	}      		
		}catch(SOAPFaultException e) {   
			log.error("getStatusOnly SOAPFaultException: "+e.getMessage()+" - "+e.getCause());           		
			SOAPFault soapFault = e.getFault();
			Detail detail = soapFault.getDetail();   
			if(detail !=null) {
				Iterator<DetailEntry> detailEntries= detail.getDetailEntries();   
				while (detailEntries.hasNext()) {
					SOAPBodyElement sbe = (SOAPBodyElement) detailEntries.next();
					documento.setErrorLogSunat(sbe.getValue());
					log.error("getStatusOnly SOAPFaultException soapFault --> "+soapFault.getFaultString()+" : "+sbe.getValue());        	      
				}        			
			}     
			documento.setRespuestaSunat(soapFault.getFaultString());
			if(!(documento.getRespuestaSunat()!=null) || (documento.getRespuestaSunat().length()>4)) {     			
				documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_Not_Acceptable);
				if((documento.getErrorLogSunat()==null) || (documento.getErrorLogSunat().isEmpty()))
					documento.setErrorLogSunat(e.toString());
			}    						
			documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());
		} catch (Exception e) {
			documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_NO_FOUND);
			documento.setErrorLogSunat(e.toString());
			documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());   
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getStatusOnly Exception \n"+errors);  
		}
		Timestamp timestampEnd = new Timestamp(System.currentTimeMillis());
		log.info("getStatusOnly timestampSendSUNAT: "+DocumentUtil.compareTwoTimeStamps(timestampEnd, timestampStart));
    }
    
    public void getStatusAR(Documento documento) {
    	Timestamp timestampStart = new Timestamp(System.currentTimeMillis());  
		try {       	  		
    		String ruc = String.valueOf(documento.getRucEmisor());
    		log.info("getStatusAR: {}",documento.toString());        
    		org.apache.xml.security.Init.init();
    		BillServiceSunatInterface port = this.connectingAR(documento);   
    		if(port!=null) {           	   			
        		StatusResponseAR statusResponseAR = port.getStatusAR(ruc, documento.getTipoDocumento(), 
        				documento.getSerie(), documento.getNumeroCorrelativo());  
        		if((statusResponseAR.getStatusCode()!=null) && 
        				(statusResponseAR.getStatusCode().equals(Constantes.SUNAT_ERROR_0000))) {  
        			byte[] bytes = ZipUtil.descomprimirArchivoMejorado(statusResponseAR.getContent());        			
        			documento.setMensajeSunat(bytes);
        			documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());  
        			SunatFileResponseUtil.readRespuestaSunat(documento); 
        		}
    			log.info("getStatusAR: {} | RespuestaSunat: {}", documento.toString(), documento.getRespuestaSunat());        		
         	}else {
        		documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_SERVICIO_INHABILITADO); 
        		documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());     
        	}      	
		}catch(SOAPFaultException e) {   
			log.error("getStatusAR SOAPFaultException: {} | {}",e.getMessage(),e.getCause());    
			documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDBILL);
			if(documento.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS) ||
					documento.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO) ||
					documento.getTipoDocumento().equals(Constantes.SUNAT_REVERSION))
				documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDSUMMARY);
			SOAPFault soapFault = e.getFault();
			Detail detail = soapFault.getDetail();   
			if(detail !=null) {
				Iterator<DetailEntry> detailEntries= detail.getDetailEntries();   
				while (detailEntries.hasNext()) {
					SOAPBodyElement sbe = (SOAPBodyElement) detailEntries.next();
					documento.setErrorLogSunat(sbe.getValue());
					log.error("getStatusAR SOAPFaultException soapFault{} ",soapFault.getFaultString()+" : "+sbe.getValue());        	      
				}        			
			}     
			documento.setRespuestaSunat(soapFault.getFaultString());
			if(!(documento.getRespuestaSunat()!=null) || (documento.getRespuestaSunat().length()>4)) {     			
				documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_Not_Acceptable);
				if((documento.getErrorLogSunat()==null) || (documento.getErrorLogSunat().isEmpty()))
					documento.setErrorLogSunat(e.toString());
			}    						
			documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());
		} catch (Exception e) {
			documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_NO_FOUND);
			documento.setErrorLogSunat(e.toString());
			documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());   
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getStatusAR Exception \n {}",errors);  
		}
		Timestamp timestampEnd = new Timestamp(System.currentTimeMillis());
    	log.info("getStatusAR timestamp: {}",DocumentUtil.compareTwoTimeStamps(timestampEnd, timestampStart));		
    }
    
    private WSS4JStaxOutInterceptor getWSS4JStaxOutInterceptor() {
    	String cdrRucOse = DirUtil.getCertificateRUC();
    	String sigPropFile = DirUtil.getCertificateSigPropFile();
    	
        Map<String, Object> outprops = new HashMap<String,Object>();         
        outprops.put(WSHandlerConstants.ACTION, 
        	    WSHandlerConstants.TIMESTAMP + " " + WSHandlerConstants.SIGNATURE);
        outprops.put(WSHandlerConstants.SIG_KEY_ID, "DirectReference");//OK        
        outprops.put(WSHandlerConstants.USER, cdrRucOse);
        outprops.put(WSHandlerConstants.PW_CALLBACK_CLASS, "com.axteroid.ose.server.sunat.service.ClientPasswordCallback"); 
        outprops.put(WSHandlerConstants.SIG_PROP_FILE, sigPropFile);           
        WSS4JStaxOutInterceptor wss4jout = new WSS4JStaxOutInterceptor(outprops);
        log.info("getWSS4JStaxOutInterceptor:  {} | {} ", cdrRucOse, sigPropFile);
    	return wss4jout;	    
	}    
	
	private BillServiceSunatInterface getConnectSunat(String urlSunat) {
    	try{ 
    		URL wsdlURL = new URL(urlSunat);
    		log.info("getConnectSunat  "+wsdlURL);
    		BillServiceSunat billServiceSunat = new BillServiceSunat(wsdlURL);     		    		
    		BillServiceSunatInterface port = billServiceSunat.getBillServicePort();  
    		if(port!=null){
	        	WSS4JStaxOutInterceptor wss4jout = this.getWSS4JStaxOutInterceptor();
	        	Client client = ClientProxy.getClient(port);       
	        	Endpoint ep =client.getEndpoint();        
	        	ep.getOutInterceptors().add(wss4jout);                    	
	        	HTTPConduit http = (HTTPConduit) client.getConduit();
	        	HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
	        	httpClientPolicy.setConnectionTimeout(30000);
	          	httpClientPolicy.setReceiveTimeout(180000);
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
	
	private BillServiceSunatInterface getConnectSunatAR(String urlSunat) {
    	try{ 
    		URL wsdlURL = new URL(urlSunat);
    		log.info("getConnectSunat  "+wsdlURL);
    		BillServiceSunat billServiceSunat = new BillServiceSunat(wsdlURL);     		    		
    		BillServiceSunatInterface port = billServiceSunat.getBillServicePort();  
    		if(port!=null){
	        	WSS4JStaxOutInterceptor wss4jout = this.getWSS4JStaxOutInterceptor();
	        	Client client = ClientProxy.getClient(port);       
	        	Endpoint ep =client.getEndpoint();        
	        	ep.getOutInterceptors().add(wss4jout);                    	
	        	HTTPConduit http = (HTTPConduit) client.getConduit();
	        	HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
	        	httpClientPolicy.setConnectionTimeout(2000);
	          	httpClientPolicy.setReceiveTimeout(2000);
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
