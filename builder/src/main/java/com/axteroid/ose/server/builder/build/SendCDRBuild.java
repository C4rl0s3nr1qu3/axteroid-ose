package com.axteroid.ose.server.builder.build;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.builder.review.DBDocumentCpeReview;
import com.axteroid.ose.server.builder.review.SunatListResponseReview;
import com.axteroid.ose.server.builder.review.SunatWsResponseReview;
import com.axteroid.ose.server.builder.sendsunat.SunatDocumentSend;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesejb.rules.OseDBCRUDLocal;
import com.axteroid.ose.server.rulesejb.rules.impl.OseDBCRUDImpl;
import com.axteroid.ose.server.rulesubl.builder.ParseXML2Document;
import com.axteroid.ose.server.rulesubl.builder.impl.ParseXml2DocumentImpl;
import com.axteroid.ose.server.sunat.service.SendSunatClientCxf;
import com.axteroid.ose.server.sunat.service.SunatFileResponseUtil;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.StringUtil;

public class SendCDRBuild {
	private static final Logger log = LoggerFactory.getLogger(SendCDRBuild.class);
	
	public void getBuilding(String id) {
		try {
			Documento tb = new Documento();
			tb.setId(Long.parseLong(id));
			OseDBCRUDLocal oseDBCRUDLocal = new OseDBCRUDImpl();
			Documento documento = oseDBCRUDLocal.buscarTbComprobanteXID(tb);	
			if((documento != null) && (documento.getId()!=null))
				this.buildSendCDR(documento);
			return;
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getBuilding \n"+errors);
		}
	}		
	
	public void buildSendCDR(Documento documento) {		
		log.info("buildComprobante: "+documento.toString()+" | Estado: "+documento.getEstado()+" | RespuestaSunat: "+documento.getRespuestaSunat());
		try {
			if(documento.getUbl()!= null) {  
				if(documento.getUbl().length == 0)	return;
			}else return;
			
			if(!((documento.getTipoDocumento().trim().equals(Constantes.SUNAT_GUIA_REMISION_REMITENTE)) ||
					(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA)))) { 
				if(documento.getCdr()!= null) {  
					if(documento.getCdr().length == 0)	return;
				}else return;	
			}			
			if(documento.getEstado()!= null) { 
				if((documento.getEstado().trim().equals(Constantes.SUNAT_CDR_EN_PROCESO)) ||
						(documento.getEstado().trim().equals(Constantes.SUNAT_CDR_ERROR_PROCESO))||
						(documento.getEstado().trim().equals(Constantes.SUNAT_CDR_AUTORIZADO)))
					return;
								
				if (documento.getEstado().trim().equals(Constantes.SUNAT_CDR_GENERADO)){									
//					if((documento.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO)) ||
//							(documento.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS) ||
//									documento.getTipoDocumento().equals(Constantes.SUNAT_REVERSION))) {								
//						if(!(DateUtil.deltaHoras(new Date(), documento.getFechaCrea()) > 0)){	
//							boolean updateState = false;
//							OseDBCRUDLocal oseDBCRUDLocal = new OseDBCRUDImpl();
//							if(documento.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO)) {
//								Integer countState = oseDBCRUDLocal.countComprobantePagoIDState(documento, Constantes.SUNAT_IndEstCpe_Anula);						
//								if(countState > 0) 
//									updateState = true;
//							}else
//								updateState = true;
//							if(updateState) {
//								documento.setEstado(Constantes.SUNAT_CDR_DETENIDO);
//								oseDBCRUDLocal.updateTbComprobante_Send(documento);
//								return;
//							}
//						}						
//					}
//					DBDocumentCpeReview dbDocumentCpeReview = new DBDocumentCpeReview();					
//					if(dbDocumentCpeReview.documentReview(documento)) { 
						SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
						sunatDocumentSend.getSendSunat(documento);		
//					}
					return;
				}	
				if(documento.getEstado().trim().equals(Constantes.SUNAT_CDR_DETENIDO)) {	
					SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
					DBDocumentCpeReview dbDocumentCpeReview = new DBDocumentCpeReview();	
					if(DateUtil.deltaHoras(new Date(), documento.getFechaCrea()) > 0){
						sunatDocumentSend.getSendSunat(documento);				
						return;
					}					
					if(dbDocumentCpeReview.documentReview(documento)) 
						sunatDocumentSend.getSendSunat(documento);		
					return;
				}								
			}else return;
			
			OseDBCRUDLocal oseDBCRUDLocal = new OseDBCRUDImpl();
			SendSunatClientCxf oseSUNAT_Client = new SendSunatClientCxf();
			SunatListResponseReview sunatListResponseReview = new SunatListResponseReview();
			SunatWsResponseReview sunatResponseReview = new SunatWsResponseReview();
			String respuestaSunat = "";
			if(documento.getRespuestaSunat()!=null) 
				respuestaSunat = documento.getRespuestaSunat();		
			log.info("Estado 0.a: {} | respuestaSunat: {} | getRespuestaSunat: {}",
					documento.getEstado(), respuestaSunat, documento.getRespuestaSunat());
			if((documento.getMensajeSunat()!= null) && (documento.getMensajeSunat().length>0)){
				SunatFileResponseUtil.readRespuestaSunat(documento);
				log.info("Estado 0.b: {} | respuestaSunat: {} | getRespuestaSunat: {}",
						documento.getEstado(), respuestaSunat, documento.getRespuestaSunat());
				if((documento.getRespuestaSunat()!=null) && 
						(documento.getRespuestaSunat().equals(Constantes.SUNAT_RESPUESTA_0))) {
					documento.setUserModi(Constantes.AVATAR_USER);		
					oseDBCRUDLocal.updateTbComprobanteCDR(documento);
					return;
				}		
			}	
			log.info("Estado 0.c: {} | respuestaSunat: {} | getRespuestaSunat: {}",
					documento.getEstado(), respuestaSunat, documento.getRespuestaSunat());
			if(documento.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS) ||
				documento.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO) ||
				documento.getTipoDocumento().equals(Constantes.SUNAT_REVERSION)) {		
				if(!(respuestaSunat.equals(Constantes.SUNAT_RESPUESTA_0098))
						&& ((documento.getRespuestaSunat()!=null) && 
								!(documento.getRespuestaSunat().trim().isEmpty()))){
					log.info("Estado 1.a: {} | respuestaSunat: {} | getRespuestaSunat: {}",
							documento.getEstado(), respuestaSunat, documento.getRespuestaSunat());				
					if((documento.getFechaRespuestaSunat()!=null) &&
						(!(DateUtil.deltaDays(new Date(), documento.getFechaRespuestaSunat())>0)))
						return;				
				}	
				log.info("Estado 1.b: {} | respuestaSunat: {} | getRespuestaSunat: {} | Ticket: {} | ErrorLogSunat: {}",
						documento.getEstado(), respuestaSunat, documento.getRespuestaSunat(), documento.getErrorLog(), documento.getErrorLogSunat());
				if((documento.getErrorLog()!= null) && (StringUtil.hasString(documento.getErrorLog()))) { 							
					oseSUNAT_Client.getStatusOnly(documento);						
					log.info("Estado 1.c: {} | respuestaSunat: {} | getRespuestaSunat: {} | Ticket: {} | ErrorLogSunat: {}",
							documento.getEstado(), respuestaSunat, documento.getRespuestaSunat(), documento.getErrorLog(), documento.getErrorLogSunat());
					this.reviewRespuestaSunat(documento, respuestaSunat);
					log.info("Estado 1.d: {} | respuestaSunat: {} | getRespuestaSunat: {} | Ticket: {} | ErrorLogSunat: {}",
							documento.getEstado(), respuestaSunat, documento.getRespuestaSunat(), documento.getErrorLog(), documento.getErrorLogSunat());
					if((documento.getRespuestaSunat()!=null) 
							&& (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_0))){
						documento.setUserModi(Constantes.AVATAR_USER);		
						oseDBCRUDLocal.updateTbComprobanteCDR(documento);
						if(respuestaSunat.equals(Constantes.SUNAT_RESPUESTA_0098))
							return;		
					}
				}
				log.info("Estado 2: {} | respuestaSunat: {} | getRespuestaSunat: {} | Ticket: {} | ErrorLogSunat: {}",
						documento.getEstado(), respuestaSunat, documento.getRespuestaSunat(), documento.getErrorLog(), documento.getErrorLogSunat());
				if((documento.getRespuestaSunat()!=null) 
						&& (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_0)))
					return;		
				log.info("Estado 3: {} | respuestaSunat: {} | getRespuestaSunat: {} | Ticket: {} | ErrorLogSunat: {}",
						documento.getEstado(), respuestaSunat, documento.getRespuestaSunat(), documento.getErrorLog(), documento.getErrorLogSunat());
				if(documento.getRespuestaSunat()!=null) {						
					sunatListResponseReview.validateSunatListComprobantesReview(documento);
					oseDBCRUDLocal.updateTbComprobanteCDRID(documento);		
					log.info("Estado 4: {} | respuestaSunat: {} | getRespuestaSunat: {} | Ticket: {} | ErrorLogSunat: {}",
							documento.getEstado(), respuestaSunat, documento.getRespuestaSunat(), documento.getErrorLog(), documento.getErrorLogSunat());
				}
				return;
			}				
			sunatResponseReview.getRequestSunat(documento);	
			log.info("Estado 5a: {} | respuestaSunat: {} | getRespuestaSunat: {}",
					documento.getEstado(), respuestaSunat, documento.getRespuestaSunat());
			if(documento.getRespuestaSunat()!=null) {
				if(documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_0)){
					oseDBCRUDLocal.updateTbComprobanteCDRID(documento);
					return;
				}
				
				if(documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_1033)){
					if(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_ROL_ADQUIRIENTE_SISTEMAS_PAGO) ||
							documento.getTipoDocumento().trim().equals(Constantes.SUNAT_OPERADOR) ||
							documento.getTipoDocumento().trim().equals(Constantes.SUNAT_ADQUIRIENTE_SISTEMAS_PAGO)) {
						documento.setEstado(Constantes.SUNAT_CDR_AUTORIZADO);
						documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_0);
						oseDBCRUDLocal.updateTbComprobanteCDRID(documento);
						return;						
					}
					oseDBCRUDLocal.updateTbComprobanteCDRID(documento);
					return;
				}
				
				if(respuestaSunat.equals(Constantes.SUNAT_ERROR_1033)) {
					documento.setRespuestaSunat(Constantes.SUNAT_ERROR_1033);
					oseDBCRUDLocal.updateTbComprobanteCDRID(documento);
					return;					
				}
//				log.info("1) TipoDocumento(): {} | Estado: {} | RespuestaSunat: {} | ticket: {}",
//						documento.getTipoDocumento(), documento.getEstado(), 
//						documento.getRespuestaSunat(), documento.getErrorLog());	
				if((documento.getTipoDocumento().trim().equals(Constantes.SUNAT_GUIA_REMISION_REMITENTE)) ||
						(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA))) {
//					log.info("2) TipoDocumento(): {} | Estado: {} | RespuestaSunat: {} | ticket: {}",
//							documento.getTipoDocumento(), documento.getEstado(), 
//							documento.getRespuestaSunat(), documento.getErrorLog());	
					SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
					sunatDocumentSend.getSendSunat(documento);	
//					log.info("3) TipoDocumento(): {} | Estado: {} | RespuestaSunat: {} | ticket: {}",
//							documento.getTipoDocumento(), documento.getEstado(), 
//							documento.getRespuestaSunat(), documento.getErrorLog());	
					log.info("Estado 5b: {} | respuestaSunat: {} | getRespuestaSunat: {}",
							documento.getEstado(), respuestaSunat, documento.getRespuestaSunat());
					return;
				}
			}									
			sunatListResponseReview.buscarComprobanteSunatList(documento);
			log.info("Estado 6: {} | respuestaSunat: {} | getRespuestaSunat: {}",
					documento.getEstado(), respuestaSunat, documento.getRespuestaSunat());
			if((documento.getRespuestaSunat()!=null) 
					&& (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_0))){				
				oseDBCRUDLocal.updateTbComprobanteCDRID(documento);
				return;
			}			
			sunatListResponseReview.validateSunatListComprobantesReview(documento);		
			log.info("Estado 7: {} | respuestaSunat: {} | getRespuestaSunat: {}",
					documento.getEstado(), respuestaSunat, documento.getRespuestaSunat());
			if((documento.getRespuestaSunat()!=null) 
					&& (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_0))){				
				oseDBCRUDLocal.updateTbComprobanteCDRID(documento);
				return;
			}
			documento.setLongitudNombre(1);
			if((documento.getRespuestaSunat()!=null) 
					&& ((documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_0005)) 
					|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_0011))
					|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0113))
					|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0127))
					|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_SD))
					|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0127))				
					|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_1033))
					)){
				if(respuestaSunat.equals(Constantes.SUNAT_ERROR_1033))
					documento.setRespuestaSunat(Constantes.SUNAT_ERROR_1033);
				SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
				sunatDocumentSend.getSendSunat(documento);
				log.info("Estado 8: {} | respuestaSunat: {} | getRespuestaSunat: {}",
						documento.getEstado(), respuestaSunat, documento.getRespuestaSunat());
				return;
			}
			if((documento.getRespuestaSunat()!=null) 
					&& ((documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_OK))
					|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_NO_FOUND))
					|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_Not_Acceptable))
					|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_SERVICIO_INHABILITADO))
					|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0100_C))
					|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0130))
					|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0132))
					|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0200))
					)){
				if((!documento.getTipoDocumento().trim().equals(Constantes.SUNAT_GUIA_REMISION_REMITENTE)) ||
						(!documento.getTipoDocumento().trim().equals(Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA)))
					return;
				if(respuestaSunat.equals(Constantes.SUNAT_ERROR_1033))
					documento.setRespuestaSunat(Constantes.SUNAT_ERROR_1033);
				SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
				sunatDocumentSend.getSendSunat(documento);
				log.info("Estado 9: {} | respuestaSunat: {} | getRespuestaSunat: {}",
						documento.getEstado(), respuestaSunat, documento.getRespuestaSunat());
				return;
			}			
			if((documento.getRespuestaSunat()!=null) 
					&& ((documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_1032)))
					|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_1033))){
				sunatResponseReview.getRequestSunat(documento);
				log.info("Estado 10: {} | respuestaSunat: {} | getRespuestaSunat: {}",
						documento.getEstado(), respuestaSunat, documento.getRespuestaSunat());
				if((documento.getRespuestaSunat()!=null) 
						&& (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_0))){				
					oseDBCRUDLocal.updateTbComprobanteCDRID(documento);
					return;
				}
			}		
			oseDBCRUDLocal.updateTbComprobanteCDRID(documento);
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buildSendCDR Exception \n"+errors);
		}
	}		
	
	public Integer countComprobantePagoIDState(Documento documento, Integer state) {
		log.info("countComprobantePagoIDState: "+documento.getId()+" - "+documento.getNombre());
		Integer countState = 0;
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		EResumenDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryResumen(documento);
		for(EResumenDocumentoItem rdi : eDocumento.getItems()) {
			if(documento.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO)) {	
	    		if(rdi.getStatus()==Constantes.SUNAT_IndEstCpe_Anula)
	    			countState++;	    		
			}
		}
		return countState;
	}
	
	public void reviewRespuestaSunat(Documento documento, String respuestaSunat) {
		if((documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_OK))
				|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_Bad_Request))
				|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_Payment_Required))
				|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_NO_AUTORIZADO))
				|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_NO_FOUND))
				|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_Not_Acceptable))
				|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_REQUEST_TIMEOUT))
				|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_SERVICIO_INHABILITADO))
				|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0100_C))
				|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0130))
				|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0132))
				|| (documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0200))
				)
			documento.setRespuestaSunat(respuestaSunat);
	}
	
	public void getBuildingQA(String id) {
		try {
			Documento tb = new Documento();
			tb.setId(Long.parseLong(id));
			OseDBCRUDLocal oseDBCRUDLocal = new OseDBCRUDImpl();
			Documento documento = oseDBCRUDLocal.buscarTbComprobanteXID(tb);	
			if((documento != null) && (documento.getId()!=null))
				this.buildSendCDR(documento);
			return;
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getBuildingQA \n"+errors);
		}
	}	

	public void buildSendCDRQA(Documento documento) {		
		log.info("buildSendCDRQA: "+documento.toString()+" | Estado: "+documento.getEstado()+" | RespuestaSunat: "+documento.getRespuestaSunat());
		try {

		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buildSendCDRQA Exception \n"+errors);
		}
	}			
}
