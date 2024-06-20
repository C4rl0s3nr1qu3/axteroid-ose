package com.axteroid.ose.server.servicerest.port;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.axteroid.ose.server.builder.get.ProcesarGetStatus;
import com.axteroid.ose.server.builder.get.ProcesarGetStatusCdr;
import com.axteroid.ose.server.builder.sendbill.ProcesarSendBill;
import com.axteroid.ose.server.builder.sendsummary.SendSummaryGo;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.servicerest.sec.AxteroidSecurityRest;
import com.axteroid.ose.server.tools.bean.OseResponse;
import com.axteroid.ose.server.tools.bean.SunatBeanRequest;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.ZipUtil;

@Path("/billService")
public class AxteroidOseServerService {
	private static final Logger log = LoggerFactory.getLogger(AxteroidOseServerService.class);
	private String userOse = "";
	
	@POST
	@Path("/sendBill")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)    
    public Response sendBill(SunatBeanRequest SunatBeanRequest, @Context HttpHeaders httpHeaders) {		
		AxteroidSecurityRest axteroidSecurityRest = new AxteroidSecurityRest();
		OseResponse oseResponse = new OseResponse();
		if(!axteroidSecurityRest.getHeader(httpHeaders)) {
			oseResponse.setSuccess(Constantes.CONTENT_FALSE);
			oseResponse.setErrorCode(Constantes.SUNAT_ERROR_0102_C);
			oseResponse.setMessage(Constantes.SUNAT_ERROR_0102_D); 
			return Response.ok().entity(oseResponse).build();
		}
		axteroidSecurityRest.getSunatSendBean(SunatBeanRequest);
		MDC.put("FILE", SunatBeanRequest.getFilename());
		log.info("Executing sendBill fileName: {} ",SunatBeanRequest.getFilename());
		try {
			//OseServerServiceFault oseServerServiceFault = new OseServerServiceFault();							
			try {
				userOse = axteroidSecurityRest.getUserOse();
				ProcesarSendBill procesarSendBill = new ProcesarSendBill();				
				String retorno = procesarSendBill.procesarComprobante(SunatBeanRequest.getFilename(), SunatBeanRequest.getContent(), userOse);
				//log.info("Executing sendBill retorno: {} ",retorno);
				String [] retSplit = retorno.split(Constantes.OSE_SPLIT);				
				if(retSplit.length == 1) {
					if(retorno.trim().isEmpty()) {
						oseResponse.setSuccess(Constantes.CONTENT_FALSE);
						oseResponse.setErrorCode(Constantes.SUNAT_ERROR_98_C);
						oseResponse.setMessage(Constantes.SUNAT_ERROR_98_D);						
						return Response.ok().entity(oseResponse).build();
					}
					byte[] ret = axteroidSecurityRest.getCDR2Zip(retorno, SunatBeanRequest.getFilename());					
					if(ret.length > 0) { 					
						oseResponse.setCdr(ret);
						axteroidSecurityRest.readAR(oseResponse);
						if(oseResponse.getData()!=null) {
							oseResponse.setSuccess(Constantes.CONTENT_TRUE);
							oseResponse.setMessage(Constantes.CONTENT_CDR_RESPUESTA);
						}else {
							oseResponse.setSuccess(Constantes.CONTENT_FALSE);
							oseResponse.setMessage(SunatBeanRequest.getFilename());
						}
						return Response.ok().entity(oseResponse).build();
					}
				}     
				oseResponse.setSuccess(Constantes.CONTENT_FALSE);
				oseResponse.setErrorCode(Constantes.SUNAT_ERROR_0100_C);
				oseResponse.setMessage(Constantes.SUNAT_ERROR_0100_D);        
				if(!retorno.isEmpty()) {
					oseResponse.setErrorCode(retSplit[0]);
					oseResponse.setMessage(retSplit[1]);			
				}					
			}catch (Exception e) {
				StringWriter errors = new StringWriter();				
				e.printStackTrace(new PrintWriter(errors));
				log.error("sendBill Exception \n"+errors);
			}						
			return Response.ok().entity(oseResponse).build();			
		} finally {			
			MDC.remove("FILE");
		}			
    }			
	
	@POST
	@Path("/sendSummary")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)    
    public Response sendSummary(SunatBeanRequest sunatSendBean, @Context HttpHeaders httpHeaders) {
		MDC.put("FILE", sunatSendBean.getFilename());
		AxteroidSecurityRest axteroidSecurityRest = new AxteroidSecurityRest();
		OseResponse oseResponse = new OseResponse();
		if(!axteroidSecurityRest.getHeader(httpHeaders)) {
			oseResponse.setSuccess(Constantes.CONTENT_FALSE);
			oseResponse.setErrorCode(Constantes.SUNAT_ERROR_0102_C);
			oseResponse.setMessage(Constantes.SUNAT_ERROR_0102_D); 
			return Response.ok().entity(oseResponse).build();
		}
		try {
			axteroidSecurityRest.getSunatSendBean(sunatSendBean);			
			log.info("Executing sendSummary fileName: {} ",sunatSendBean.getFilename());
			userOse = axteroidSecurityRest.getUserOse();
			//OseServerServiceFault oseServerServiceFault = new OseServerServiceFault();
			SendSummaryGo sendSummaryGo = new SendSummaryGo();
			String retorno = sendSummaryGo.validarComprobante(sunatSendBean.getFilename(), sunatSendBean.getContent(), userOse);	
			String [] retSplit = retorno.split(Constantes.OSE_SPLIT);			
			if(retSplit.length == 1) { 
				oseResponse.setSuccess(Constantes.CONTENT_TRUE);
				oseResponse.setTicket(retorno);
				return Response.ok().entity(oseResponse).build();
			}
			oseResponse.setSuccess(Constantes.CONTENT_FALSE);
			oseResponse.setErrorCode(Constantes.SUNAT_ERROR_0100_C);
			oseResponse.setMessage(Constantes.SUNAT_ERROR_0100_D);        
			if(!retorno.isEmpty()) {
				oseResponse.setErrorCode(retSplit[0]);
				oseResponse.setMessage("");
				if(retSplit[1] != null)
					oseResponse.setMessage(retSplit[1]);				
			}		
			return Response.ok().entity(oseResponse).build();
		} finally {
			MDC.remove("FILE");
		}			
    }		
		
	@GET
	@Path("/getStatus")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)    
    public Response getStatus(@QueryParam("ticket") final String ticket, @Context HttpHeaders httpHeaders) {
		MDC.put("FILE", ticket);
		AxteroidSecurityRest axteroidSecurityRest = new AxteroidSecurityRest();
		OseResponse oseResponse = new OseResponse();
		if(!axteroidSecurityRest.getHeader(httpHeaders)) {
			oseResponse.setSuccess(Constantes.CONTENT_FALSE);
			oseResponse.setErrorCode(Constantes.SUNAT_ERROR_0102_C);
			oseResponse.setMessage(Constantes.SUNAT_ERROR_0102_D); 
			return Response.ok().entity(oseResponse).build();
		}
		try {
			log.info("Executing getStatus ticket: {}", ticket);	
			userOse = axteroidSecurityRest.getUserOse();
			//OseServerServiceFault oseServerServiceFault = new OseServerServiceFault();
			ProcesarGetStatus procesarGetStatus  = new ProcesarGetStatus();		      
			Documento documento =  procesarGetStatus.getStatus(ticket, userOse);
			if((documento != null) && 
					(documento.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0)))) {	
				oseResponse.setSuccess(Constantes.CONTENT_FALSE);
				if((documento.getCdr()!=null) && (documento.getCdr().length > 0)) {
					byte[] cdr = ZipUtil.descomprimirArchivoMejorado(documento.getCdr());
					oseResponse.setCdr(cdr);
					axteroidSecurityRest.readCdrSunat(oseResponse);
					//oseResponse.setErrorCode(documento.getErrorNumero());		
					return Response.ok().entity(oseResponse).build();
				}else {
					oseResponse.setErrorCode(Constantes.SUNAT_ERROR_98_C);
					String mensaje = documento.getErrorNumero()+" | "+documento.getErrorDescripcion();
					if((documento.getRespuestaSunat()!=null) && !(documento.getRespuestaSunat().isEmpty()))
						mensaje = documento.getRespuestaSunat()+" | "+documento.getErrorLogSunat();
//					oseResponse.setMessage(Constantes.SUNAT_ERROR_98_D); 
					oseResponse.setMessage(mensaje);
					return Response.ok().entity(oseResponse).build();
				}
			}	
			if(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_GUIA_REMISION_REMITENTE) ||
					documento.getTipoDocumento().trim().equals(Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA)) {
				if(documento.getCdr()!=null && documento.getCdr().length>0) 
					oseResponse.setCdr(documento.getCdr());
			}
			oseResponse.setErrorCode(Constantes.SUNAT_ERROR_0100_C);
			oseResponse.setMessage(Constantes.SUNAT_ERROR_0100_D);        
			if(!documento.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0))) {
				oseResponse.setErrorCode(documento.getErrorNumero());
				oseResponse.setMessage(documento.getErrorDescripcion());
			}
			return Response.ok().entity(oseResponse).build();
		} finally {
			MDC.remove("FILE");
		}			
    }	
	
	@GET
	@Path("/getStatusCdr")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)    
    public Response getStatusCdr(@QueryParam("ruc") String ruc, @QueryParam("tipoComprobante") String tipoComprobante,
    		@QueryParam("serie") String serie, @QueryParam("correlativo") String correlativo, 
    		@Context HttpHeaders httpHeaders) {
		MDC.put("FILE", ruc+"-"+tipoComprobante+"-"+serie+"-"+correlativo);
		AxteroidSecurityRest axteroidSecurityRest = new AxteroidSecurityRest();
		OseResponse oseResponse = new OseResponse();
		if(!axteroidSecurityRest.getHeader(httpHeaders)) {
			oseResponse.setSuccess(Constantes.CONTENT_FALSE);
			oseResponse.setErrorCode(Constantes.SUNAT_ERROR_0102_C);
			oseResponse.setMessage(Constantes.SUNAT_ERROR_0102_D); 
			return Response.ok().entity(oseResponse).build();
		}
		try {					
			log.info("Executing getStatusCdr 1): {}", ruc+"-"+tipoComprobante+"-"+serie+"-"+correlativo);	
			userOse = axteroidSecurityRest.getUserOse();
			//OseServerServiceFault oseServerServiceFault = new OseServerServiceFault();
			ProcesarGetStatusCdr procesarGetStatusCdr = new ProcesarGetStatusCdr();
			Documento documento = new Documento();
			axteroidSecurityRest.getComprobante(documento,ruc,tipoComprobante,serie,correlativo);        
			log.info("Executing getStatusCdr 2): {}-{}-{}-{}",documento.getRucEmisor(),documento.getTipoDocumento(),documento.getSerie(),documento.getNumeroCorrelativo());
			Documento doc = procesarGetStatusCdr.getStatusCdr(documento, userOse);        
			//log.info("Executing getStatusCdr 3): {} ", new String(doc.getCdr()));
			if((doc != null) && 
					(doc.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0)))) {					
				oseResponse.setSuccess(Constantes.CONTENT_FALSE);
				if(doc.getCdr()!=null && doc.getCdr().length>0) {					
					oseResponse.setCdr(doc.getCdr());
					axteroidSecurityRest.readCdrSunat(oseResponse);
					//oseResponse.setErrorCode(doc.getErrorNumero());		
					return Response.ok().entity(oseResponse).build();
				}else {
					oseResponse.setErrorCode(Constantes.SUNAT_ERROR_98_C);
					String mensaje = "";
					if((doc.getErrorNumero()!=null) && !(doc.getErrorNumero().isEmpty())) {						
						oseResponse.setErrorCode(doc.getErrorNumero());
						mensaje = doc.getErrorNumero()+" | "+doc.getErrorDescripcion();
					}
					if((doc.getRespuestaSunat()!=null) && !(doc.getRespuestaSunat().isEmpty())) { 
						oseResponse.setErrorCode(doc.getRespuestaSunat());					
						mensaje = doc.getRespuestaSunat()+" | "+doc.getErrorLogSunat();
					}
					oseResponse.setMessage(mensaje);
					return Response.ok().entity(oseResponse).build();
				}
			}  			
			if(doc.getTipoDocumento().trim().equals(Constantes.SUNAT_GUIA_REMISION_REMITENTE) ||
					doc.getTipoDocumento().trim().equals(Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA)) {
				if(doc.getCdr()!=null && doc.getCdr().length>0) 
					oseResponse.setCdr(doc.getCdr());
			}
			//log.info("Executing getStatusCdr 4): {} ", new String(oseResponse.getCdr()));
			oseResponse.setSuccess(Constantes.CONTENT_FALSE);
			oseResponse.setErrorCode(Constantes.SUNAT_ERROR_0100_C);
			oseResponse.setMessage(Constantes.SUNAT_ERROR_0100_D);        
			if(!doc.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0))) {
				oseResponse.setErrorCode(doc.getErrorNumero());
				oseResponse.setMessage(doc.getErrorDescripcion());
			}
			return Response.ok().entity(oseResponse).build();
		} finally {
			MDC.remove("FILE");
		}					
    }
	
}
