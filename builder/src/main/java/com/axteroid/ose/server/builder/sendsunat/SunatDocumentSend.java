package com.axteroid.ose.server.builder.sendsunat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import javax.naming.InitialContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.apirest.chile.ApiChile;
import com.axteroid.ose.server.apirest.sunat.ConsultaIntegradaSunatRest;
import com.axteroid.ose.server.apirest.sunat.EnviarDocumentoSunatRest;
import com.axteroid.ose.server.builder.content.impl.ReturnResponseImpl;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.Emisor;
import com.axteroid.ose.server.rulesejb.rules.OseDBCRUDLocal;
import com.axteroid.ose.server.sunat.core.SendSunatClientWS;
import com.axteroid.ose.server.sunat.service.SendSunatClientCxf;
import com.axteroid.ose.server.sunat.service.SunatFileResponseUtil;
import com.axteroid.ose.server.tools.bean.SunatRequest;
import com.axteroid.ose.server.tools.bean.ApiChileResponse;
import com.axteroid.ose.server.tools.bean.SunatBeanRequest;
import com.axteroid.ose.server.tools.bean.SunatBeanResponse;
import com.axteroid.ose.server.tools.bean.SunatBeanResponseGRE;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.exception.JavaParserExceptions;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.DirUtil;

public class SunatDocumentSend {
	private static final Logger log = LoggerFactory.getLogger(SunatDocumentSend.class);	
	private ReturnResponseImpl returnValidateRules = new ReturnResponseImpl();
	private String ambiente = DirUtil.getAmbienteSunat();
	private String sendSunatWS = DirUtil.getSendSunatWs();
	
	public void getSendSunat(Documento documento) {		
		log.error("getSendSunat "+documento.toString());
		try {					
			if((documento.getRespuestaSunat()!=null) && 
				(documento.getRespuestaSunat().equals(Constantes.SUNAT_ERROR_1033))) 
				return;
			boolean bSunat = false;
			boolean bSendSunatWS = false;
			if(ambiente.equals(Constantes.DIR_AMB_PROD)) 
				bSunat = true;
			if(ambiente.equals(Constantes.DIR_AMB_BETA)) 
				bSunat = true;
			if(sendSunatWS.equals(Constantes.CONTENT_TRUE)) 
				bSendSunatWS = true;			
			if((bSunat) && 
					!(Constantes.SUNAT_GUIA_REMISION_REMITENTE.equals(documento.getTipoDocumento()) || 
		            		Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA.equals(documento.getTipoDocumento()))) {
				if(documento.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS) ||
						documento.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO) ||
						documento.getTipoDocumento().equals(Constantes.SUNAT_REVERSION)) {		
					if(bSendSunatWS){						
						try {
							SendSunatClientWS client = new SendSunatClientWS();
							client.sendSummary(documento);
						} catch (Throwable e) {}
					}else {
						SendSunatClientCxf sendSunatClient = new SendSunatClientCxf();
						sendSunatClient.sendSummary(documento);
					}
				}else {	
					if(bSendSunatWS){
						try {
							SendSunatClientWS client = new SendSunatClientWS();
							client.sendBill(documento);
						} catch (Throwable e) {}
					}else {
						SendSunatClientCxf sendSunatClient = new SendSunatClientCxf();
						sendSunatClient.sendBill(documento);	
					}
				}
			}
			if (Constantes.SUNAT_GUIA_REMISION_REMITENTE.equals(documento.getTipoDocumento()) || 
            		Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA.equals(documento.getTipoDocumento())) {	
				SunatRequest sunatRequest = this.setSunatRequest(documento);
				Emisor emisor = this.buscarCredenciales(documento);
				log.info("A) ticket: {} | TipoDocumento(): {} | Estado: {} | RespuestaSunat: {} | logSunat: {} | ErrorNumero: {} ",
						documento.getErrorLog(), documento.getTipoDocumento(), documento.getEstado(), 
						documento.getRespuestaSunat(), documento.getErrorLogSunat(), documento.getErrorNumero());	
				if((documento.getEstado().trim().equals(Constantes.SUNAT_CDR_GENERADO)))
					EnviarDocumentoSunatRest.enviarDocumentoSunat(documento, sunatRequest, emisor);
				else 
					EnviarDocumentoSunatRest.recuperarDocumentoSunat(documento, sunatRequest, emisor);	
				if((documento.getEstado().trim().equals(Constantes.SUNAT_CDR_AUTORIZADO))) {
					//SendSunatClientCxf sendSunatClient = new SendSunatClientCxf();
					SunatFileResponseUtil.readRespuestaSunat(documento);
				}						
				log.info("B) ticket: {} | TipoDocumento(): {} | Estado: {} | RespuestaSunat: {} | logSunat: {} | ErrorNumero: {} ",
						documento.getErrorLog(), documento.getTipoDocumento(), documento.getEstado(), 
						documento.getRespuestaSunat(), documento.getErrorLogSunat(), documento.getErrorNumero());	
				if(((documento.getEstado().trim().equals(Constantes.SUNAT_CDR_AUTORIZADO)) &&
						(documento.getRespuestaSunat().trim().equals(Constantes.CONTENT_TRUE))) ||
						((documento.getEstado().trim().equals(Constantes.SUNAT_CDR_ERROR_SENDBILL)) &&
						(documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_1033)))) {
					Long minutos = DateUtil.deltaMinutos(new Date(), documento.getFechaCrea());
					if(minutos > 9) {						
						Optional<ApiChileResponse> opt = ApiChile.updateStatusApiChile(documento);
				    	if((opt!=null) && opt.isPresent()){  
				    		ApiChileResponse apiChileResponse = opt.get();
				    		apiChileResponse.toString();
				    	}
					}
				}									
			}
			returnValidateRules.updateComprobante_Send(documento);	
			returnValidateRules.grabarTsAcuseReciboSunat(documento);
		}catch(Exception e) {
			JavaParserExceptions.getParseException(e, "getSendSunat");
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));
//			log.error("getSendSunat Exception \n"+errors);
		}
	}
	
	public void getStatusAR(Documento documento) {		
		log.info("getStatusAR ");
		try {			
			if (Constantes.SUNAT_GUIA_REMISION_REMITENTE.equals(documento.getTipoDocumento()) || 
            		Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA.equals(documento.getTipoDocumento()))  
				return;
			SendSunatClientCxf sendSunatClient = new SendSunatClientCxf();			
			sendSunatClient.getStatusAR(documento);			
			returnValidateRules.updateComprobante_Send(documento);	
			returnValidateRules.grabarTsAcuseReciboSunat(documento);
		}catch(Exception e) {
			JavaParserExceptions.getParseException(e, "getStatusAR");
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));
//			log.error("getStatusAR Exception \n {}",errors);
		}
	}	
	
	private SunatRequest setSunatRequest(Documento documento) {
		SunatRequest sunatRequest = new SunatRequest();
		sunatRequest.setNumRuc(String.valueOf(documento.getRucEmisor()));
		sunatRequest.setCodComp(documento.getTipoDocumento());
		sunatRequest.setNumeroSerie(documento.getSerie());
		sunatRequest.setNumero(documento.getNumeroCorrelativo());
//    	sunatConsulta.setFilename(sunatConsulta.getNumRuc()+"-"+sunatConsulta.getCodComp()+"-"+sunatConsulta.getNumeroSerie()+"-"
//				+sunatConsulta.getNumero()+".zip");
    	//SendSunatClient sendSunatClient = new SendSunatClient();
    	SunatBeanRequest SunatSendBean = SunatFileResponseUtil.getStatusResponseBeanUbl(documento);
    	sunatRequest.setFilename(SunatSendBean.getFilename());
    	sunatRequest.setContent(SunatSendBean.getContent());
    	String contentString = Base64.getEncoder().encodeToString(sunatRequest.getContent());   	
    	sunatRequest.setContentString(contentString);
    	   	
    	MessageDigest md = null;
    	try {
    		md = MessageDigest.getInstance("SHA-256");
    		byte[] hashMessageDigest = md.digest(sunatRequest.getContent());
    		StringBuffer sbhashMessageDigest = new StringBuffer();   	    
    		for(byte b : hashMessageDigest) {        
    			sbhashMessageDigest.append(String.format("%02x", b));
    		}    	   
    		sunatRequest.setHash(sbhashMessageDigest.toString());
    		log.info("sbhashMessageDigest: {}",sbhashMessageDigest.toString());
    	} 
    	catch (NoSuchAlgorithmException e) {	
    		JavaParserExceptions.getParseException(e, "setSunatRequest");
//    		e.printStackTrace();
    	}    	    
    	
    	log.info("sunatConsulta: {}",sunatRequest.toString());
    	return sunatRequest;
	}	
	
	public Emisor buscarCredenciales(Documento documento){
		log.info("buscarCredenciales "+documento.getNombre()+" - documento.getErrorComprobante(): "+documento.getErrorComprobante());
		try {
			OseDBCRUDLocal crudOseDBLocal = 
				(OseDBCRUDLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"OseDBCRUDImpl"+Constantes.OSE_CDEJB_RULES+"OseDBCRUDLocal");		
			Emisor emisor = crudOseDBLocal.buscarEmisor(documento);
			return emisor;
//			idTockenEnvioComprobante = emisor.getIdTockenGre();
//			claveTockenEnvioComprobante = emisor.getClaveTockenGre();	
//			usuariosol = emisor.getUsuariosol();
//			clavesol = emisor.getClavesol();
		}catch(Exception e) {			
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			JavaParserExceptions.getParseException(e, "buscarCredenciales");
		}		
		return null;
	}	
}
