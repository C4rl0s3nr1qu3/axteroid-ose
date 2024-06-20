package com.axteroid.ose.server.rulesubl.dozer.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesubl.dozer.ParseDocument2CDRDozer;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EDocumentoCDR;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.ubl21.builder.CdrDozer21Build;

public class ParseDocument2CDRDozerImpl implements ParseDocument2CDRDozer{
	private static final Logger log = LoggerFactory.getLogger(ParseDocument2CDRDozerImpl.class);
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSS");
	
	public void generarEDocumentCDR(Documento documento, EDocumentoCDR eDocumentoCDR, 
			EDocumento eDocumento) {
		log.info("generarEDocumentCDR ");
		try{			
			eDocumentoCDR.setAutorización_Comprobante(UUID.randomUUID().toString());
			eDocumentoCDR.setFecha_comprobacion_comprobante(new Date());
			eDocumentoCDR.setHora_comprobacion_comprobante(eDocumentoCDR.getFecha_comprobacion_comprobante());
			eDocumentoCDR.setRuc_emisor_pse(eDocumentoCDR.getRuc_emisor_pse()+Constantes.CDR_RUC_VF);
			eDocumentoCDR.setRuc_ose(Constantes.CDR_RUC_OSE+Constantes.CDR_RUC_VF);		
			eDocumentoCDR.setCodigo_respuesta("0|"+Constantes.CDR_SUNAT_PE);
			eDocumentoCDR.setDescripcion_respuesta("El comprobante numero "+eDocumento.getIdDocumento()+", ha sido aceptada");
			eDocumentoCDR.setCodigo_observacion(documento.getObservaNumero()+"|"+Constantes.CDR_SUNAT_CR);
			eDocumentoCDR.setDescripcion_observacion(documento.getObservaDescripcion());
			eDocumentoCDR.setSerie_numero_comprobante(eDocumento.getIdDocumento());			
			eDocumentoCDR.setFecha_emisión_comprobante(eDocumento.getFechaDocumento());
			eDocumentoCDR.setHora_emisión_comprobante(eDocumento.getFechaDocumento());
			eDocumentoCDR.setTipo_comprobante(eDocumento.getTipoDocumento());
			eDocumentoCDR.setHash_comprobante(eDocumento.getHashCode());			
			eDocumentoCDR.setNumero_documento_emisor(eDocumento.getNumeroDocumentoEmisor());
			eDocumentoCDR.setTipo_documento_emisor(eDocumento.getTipoDocumentoEmisor());
			eDocumentoCDR.setNumero_documento_receptor(eDocumento.getNumeroDocumentoAdquiriente());
			//log.info("eDocumento.getNumeroDocumentoAdquiriente  "+eDocumento.getNumeroDocumentoAdquiriente());
			eDocumentoCDR.setTipo_documento_receptor(eDocumento.getTipoDocumentoAdquiriente());	
			//log.info("eDocumento.getIssueTime()  "+eDocumento.getIssueTime());
			this.changeFecha_emisión_comprobante(eDocumentoCDR, eDocumento.getIssueTime());
			//log.info("eDocumentoCDR.getFecha_emisión_comprobante()  "+eDocumentoCDR.getFecha_emisión_comprobante());
			//if(eDocumentoCDR.getCodigo_observacion().isEmpty())
				
			String eDocumentResponse = eDocumentoCDR.getCodigo_respuesta()+"|"
				+eDocumentoCDR.getDescripcion_respuesta()+"|"
				+eDocumentoCDR.getCodigo_observacion()+"|"
				+eDocumentoCDR.getDescripcion_observacion()+"|"
				+eDocumentoCDR.getSerie_numero_comprobante()+"|"
				+sdf.format(eDocumentoCDR.getFecha_emisión_comprobante())+"|"
				+eDocumentoCDR.getTipo_comprobante()+"|"
				+eDocumentoCDR.getHash_comprobante()+"|"
				+eDocumentoCDR.getNumero_documento_emisor()+"|"
				+eDocumentoCDR.getTipo_documento_emisor()+"|"
				+eDocumentoCDR.getNumero_documento_receptor()+"|"
				+eDocumentoCDR.getTipo_documento_receptor();
			eDocumentoCDR.setDocument_response(eDocumentResponse);
			
			CdrDozer21Build cdrDozer21Build = new CdrDozer21Build();
			byte[] cdr = cdrDozer21Build.generaCDR21(eDocumentoCDR); 		
			
			if(cdr!=null)
				documento.setCdr(cdr);
			else {
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);
				documento.setErrorLog("Problemaa para generar el CDR2.1");
			}
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			documento.setErrorLog(errors.toString());
			log.error("Exception \n"+errors);
		}		
	}
	
	public void document2TbComprobante(Documento documento, EDocumento eDocumento) {
		try{
			String[] values = StringUtils.split(eDocumento.getIdDocumento(), "-");   
	        String val0 = "";
	        String val1 = "";
	        if(values!=null && values.length>0) {
	        	val0 = values[0];
	        	if(values.length>1)
	        		val1 = values[1];
	        }
			documento.setIdComprobante(eDocumento.getNumeroDocumentoEmisor()+eDocumento.getTipoDocumento()+val0+val1);	
			if(eDocumento.getNumeroDocumentoEmisor() == null)
				eDocumento.setNumeroDocumentoEmisor("0");
			documento.setRucEmisor(Long.parseLong(eDocumento.getNumeroDocumentoEmisor()));
			documento.setTipoDocumento(eDocumento.getTipoDocumento());
			documento.setSerie(val0);
			documento.setNumeroCorrelativo(val1);
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			documento.setErrorLog(errors.toString());
			log.error("Exception \n"+errors);
		}
	}

	public void generarEDocumentCDR_Retencion(Documento documento, EDocumentoCDR eDocumentoCDR, 
			ERetencionDocumento eDocumento) {
		log.info("generarEDocumentCDR_Retencion");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		try{
			eDocumentoCDR.setAutorización_Comprobante(UUID.randomUUID().toString());
			eDocumentoCDR.setFecha_comprobacion_comprobante(new Date());
			eDocumentoCDR.setHora_comprobacion_comprobante(eDocumentoCDR.getFecha_comprobacion_comprobante());
			eDocumentoCDR.setRuc_emisor_pse(eDocumentoCDR.getRuc_emisor_pse()+Constantes.CDR_RUC_VF);
			eDocumentoCDR.setRuc_ose(Constantes.CDR_RUC_OSE+Constantes.CDR_RUC_VF);		
			eDocumentoCDR.setCodigo_respuesta("0|"+Constantes.CDR_SUNAT_PE);
			eDocumentoCDR.setDescripcion_respuesta("El comprobante numero "+eDocumento.getIdDocumento()+", ha sido aceptada");
			eDocumentoCDR.setCodigo_observacion(documento.getObservaNumero()+"|"+Constantes.CDR_SUNAT_CR);
			eDocumentoCDR.setDescripcion_observacion(documento.getObservaDescripcion());
			eDocumentoCDR.setSerie_numero_comprobante(eDocumento.getIdDocumento());
			eDocumentoCDR.setFecha_emisión_comprobante(eDocumento.getFechaDocumento());
			eDocumentoCDR.setHora_emisión_comprobante(eDocumento.getFechaDocumento());
			eDocumentoCDR.setTipo_comprobante(eDocumento.getTipoDocumento());
			eDocumentoCDR.setHash_comprobante(eDocumento.getHashCode());
			eDocumentoCDR.setNumero_documento_emisor(eDocumento.getNumeroDocumentoEmisor());
			eDocumentoCDR.setTipo_documento_emisor(eDocumento.getTipoDocumentoEmisor());
			eDocumentoCDR.setNumero_documento_receptor(eDocumento.getNumeroDocumentoAdquiriente());
			eDocumentoCDR.setTipo_documento_receptor(eDocumento.getTipoDocumentoAdquiriente());
			String eDocumentResponse = eDocumentoCDR.getCodigo_respuesta()+"|"
				+eDocumentoCDR.getDescripcion_respuesta()+"|"
				+eDocumentoCDR.getCodigo_observacion()+"|"
				+eDocumentoCDR.getDescripcion_observacion()+"|"
				+eDocumentoCDR.getSerie_numero_comprobante()+"|"
				+sdf.format(eDocumentoCDR.getFecha_emisión_comprobante())+"|"
				+eDocumentoCDR.getTipo_comprobante()+"|"
				+eDocumentoCDR.getHash_comprobante()+"|"
				+eDocumentoCDR.getNumero_documento_emisor()+"|"
				+eDocumentoCDR.getTipo_documento_emisor()+"|"
				+eDocumentoCDR.getNumero_documento_receptor()+"|"
				+eDocumentoCDR.getTipo_documento_receptor();
			eDocumentoCDR.setDocument_response(eDocumentResponse);
			
			CdrDozer21Build cdrDozer21Build = new CdrDozer21Build();
			byte[] cdr = cdrDozer21Build.generaCDR21(eDocumentoCDR); 		
			
			if(cdr!=null)
				documento.setCdr(cdr);
			else {
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);
				documento.setErrorLog("Problemaa para generar el CDR2.1");
			}
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			documento.setErrorLog(errors.toString());
			log.error("Exception \n"+errors);
		}		
	}
	
	public void document2TbComprobante_Retencion(Documento documento, ERetencionDocumento eDocumento) {
        String[] values = StringUtils.split(eDocumento.getIdDocumento(), "-");  
        String val0 = "";
        String val1 = "";
        if(values!=null && values.length>0) {
        	val0 = values[0];
        	if(values.length>1)
        		val1 = values[1];
        }
        documento.setIdComprobante(eDocumento.getNumeroDocumentoEmisor()+eDocumento.getTipoDocumento()+val0+val1);
		documento.setRucEmisor(Long.parseLong(eDocumento.getNumeroDocumentoEmisor()));
		documento.setTipoDocumento(eDocumento.getTipoDocumento());
		documento.setSerie(val0);
		documento.setNumeroCorrelativo(val1);
	}	
	
	public void generarEDocumentCDR_Percepcion(Documento documento, EDocumentoCDR eDocumentoCDR, 
			EPercepcionDocumento eDocumento) {
		log.info("generarEDocumentCDR_Percepcion ");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		try{
			eDocumentoCDR.setAutorización_Comprobante(UUID.randomUUID().toString());
			eDocumentoCDR.setFecha_comprobacion_comprobante(new Date());
			eDocumentoCDR.setHora_comprobacion_comprobante(eDocumentoCDR.getFecha_comprobacion_comprobante());
			eDocumentoCDR.setRuc_emisor_pse(eDocumentoCDR.getRuc_emisor_pse()+Constantes.CDR_RUC_VF);
			eDocumentoCDR.setRuc_ose(Constantes.CDR_RUC_OSE+Constantes.CDR_RUC_VF);		
			eDocumentoCDR.setCodigo_respuesta("0|"+Constantes.CDR_SUNAT_PE);
			eDocumentoCDR.setDescripcion_respuesta("El comprobante numero "+eDocumento.getIdDocumento()+", ha sido aceptada");
			eDocumentoCDR.setCodigo_observacion(documento.getObservaNumero()+"|"+Constantes.CDR_SUNAT_CR);
			eDocumentoCDR.setDescripcion_observacion(documento.getObservaDescripcion());
			eDocumentoCDR.setSerie_numero_comprobante(eDocumento.getIdDocumento());
			eDocumentoCDR.setFecha_emisión_comprobante(eDocumento.getFechaDocumento());
			eDocumentoCDR.setHora_emisión_comprobante(eDocumento.getFechaDocumento());
			eDocumentoCDR.setTipo_comprobante(eDocumento.getTipoDocumento());
			eDocumentoCDR.setHash_comprobante(eDocumento.getHashCode());
			eDocumentoCDR.setNumero_documento_emisor(eDocumento.getNumeroDocumentoEmisor());
			eDocumentoCDR.setTipo_documento_emisor(eDocumento.getTipoDocumentoEmisor());
			eDocumentoCDR.setNumero_documento_receptor(eDocumento.getNumeroDocumentoAdquiriente());
			eDocumentoCDR.setTipo_documento_receptor(eDocumento.getTipoDocumentoAdquiriente());
		
			String eDocumentResponse = eDocumentoCDR.getCodigo_respuesta()+"|"
				+eDocumentoCDR.getDescripcion_respuesta()+"|"
				+eDocumentoCDR.getCodigo_observacion()+"|"
				+eDocumentoCDR.getDescripcion_observacion()+"|"
				+eDocumentoCDR.getSerie_numero_comprobante()+"|"
				+sdf.format(eDocumentoCDR.getFecha_emisión_comprobante())+"|"
				+eDocumentoCDR.getTipo_comprobante()+"|"
				+eDocumentoCDR.getHash_comprobante()+"|"
				+eDocumentoCDR.getNumero_documento_emisor()+"|"
				+eDocumentoCDR.getTipo_documento_emisor()+"|"
				+eDocumentoCDR.getNumero_documento_receptor()+"|"
				+eDocumentoCDR.getTipo_documento_receptor();
			eDocumentoCDR.setDocument_response(eDocumentResponse);
			
			CdrDozer21Build cdrDozer21Build = new CdrDozer21Build();
			byte[] cdr = cdrDozer21Build.generaCDR21(eDocumentoCDR); 		
			
			if(cdr!=null)
				documento.setCdr(cdr);
			else {
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);
				documento.setErrorLog("Problemaa para generar el CDR2.1");
			}
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			documento.setErrorLog(errors.toString());
			log.error("Exception \n"+errors);
		}		
	}
	
	public void document2TbComprobante_Percepcion(Documento documento, EPercepcionDocumento eDocumento) {
        String[] values = StringUtils.split(eDocumento.getIdDocumento(), "-");  
        String val0 = "";
        String val1 = "";
        if(values!=null && values.length>0) {
        	val0 = values[0];
        	if(values.length>1)
        		val1 = values[1];      
        }
        documento.setIdComprobante(eDocumento.getNumeroDocumentoEmisor()+eDocumento.getTipoDocumento()+val0+val1);         
		documento.setRucEmisor(Long.parseLong(eDocumento.getNumeroDocumentoEmisor()));
		documento.setTipoDocumento(eDocumento.getTipoDocumento());
		documento.setSerie(val0);
		documento.setNumeroCorrelativo(val1);
	}	
	
	public void generarEDocumentCDR_Guia(Documento documento, EDocumentoCDR eDocumentoCDR, 
			EGuiaDocumento eDocumento) {
		log.info("generarEDocumentCDR_Guia ");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		try{
			eDocumentoCDR.setAutorización_Comprobante(UUID.randomUUID().toString());
			eDocumentoCDR.setFecha_comprobacion_comprobante(new Date());
			eDocumentoCDR.setHora_comprobacion_comprobante(eDocumentoCDR.getFecha_comprobacion_comprobante());
			eDocumentoCDR.setRuc_emisor_pse(eDocumentoCDR.getRuc_emisor_pse()+Constantes.CDR_RUC_VF);
			eDocumentoCDR.setRuc_ose(Constantes.CDR_RUC_OSE+Constantes.CDR_RUC_VF);		
			eDocumentoCDR.setCodigo_respuesta("0|"+Constantes.CDR_SUNAT_PE);
			eDocumentoCDR.setDescripcion_respuesta("El comprobante numero "+eDocumento.getIdDocumento()+", ha sido aceptada");
			eDocumentoCDR.setCodigo_observacion(documento.getObservaNumero()+"|"+Constantes.CDR_SUNAT_CR);
			eDocumentoCDR.setDescripcion_observacion(documento.getObservaDescripcion());
			eDocumentoCDR.setSerie_numero_comprobante(eDocumento.getIdDocumento());
			eDocumentoCDR.setFecha_emisión_comprobante(eDocumento.getFechaDocumento());
			eDocumentoCDR.setHora_emisión_comprobante(eDocumento.getFechaDocumento());
			eDocumentoCDR.setTipo_comprobante(eDocumento.getTipoDocumento());
			eDocumentoCDR.setHash_comprobante(eDocumento.getHashCode());
			eDocumentoCDR.setNumero_documento_emisor(eDocumento.getNumeroDocumentoEmisor());
			eDocumentoCDR.setTipo_documento_emisor(eDocumento.getTipoDocumentoEmisor());
			eDocumentoCDR.setNumero_documento_receptor(eDocumento.getNumeroDocumentoAdquiriente());
			eDocumentoCDR.setTipo_documento_receptor(eDocumento.getTipoDocumentoAdquiriente());
			this.changeFecha_emisión_comprobante(eDocumentoCDR, eDocumento.getIssueTime());
			
			String eDocumentResponse = eDocumentoCDR.getCodigo_respuesta()+"|"
				+eDocumentoCDR.getDescripcion_respuesta()+"|"
				+eDocumentoCDR.getCodigo_observacion()+"|"
				+eDocumentoCDR.getDescripcion_observacion()+"|"
				+eDocumentoCDR.getSerie_numero_comprobante()+"|"
				+sdf.format(eDocumentoCDR.getFecha_emisión_comprobante())+"|"
				+eDocumentoCDR.getTipo_comprobante()+"|"
				+eDocumentoCDR.getHash_comprobante()+"|"
				+eDocumentoCDR.getNumero_documento_emisor()+"|"
				+eDocumentoCDR.getTipo_documento_emisor()+"|"
				+eDocumentoCDR.getNumero_documento_receptor()+"|"
				+eDocumentoCDR.getTipo_documento_receptor();
			eDocumentoCDR.setDocument_response(eDocumentResponse);
			
			CdrDozer21Build cdrDozer21Build = new CdrDozer21Build();
			byte[] cdr = cdrDozer21Build.generaCDR21(eDocumentoCDR); 		
			
			if(cdr!=null)
				documento.setCdr(cdr);
			else {
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);
				documento.setErrorLog("Problemaa para generar el CDR2.1");
			}
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			documento.setErrorLog(errors.toString());
			log.error("Exception \n"+errors);
		}		
	}
	
	public void document2TbComprobante_Guia(Documento documento, EGuiaDocumento eDocumento) {
		log.info("eDocumento: {} ",eDocumento.toString());

        String[] values = StringUtils.split(eDocumento.getIdDocumento(), "-");   
        String val0 = "";
        String val1 = "";
        if(values!=null && values.length>0) {
        	val0 = values[0];
        	if(values.length>1)
        		val1 = values[1];
        }
        log.info("Documento: {} | {} | {}",documento.getIdComprobante(), documento.toString());           	
// 		log.info("eDocumento: {} | {} | {}",eDocumento.getIdDocumento(), eDocumento.getNumeroDocumento(),eDocumento.getTipoDocumento());           	
//    	log.info("eDocumento Remitente: {} | {} | {}",documento.getIdComprobante(),eDocumento.getNumeroDocumentoRemitente(), eDocumento.getTipoDocumentoRemitente());
//    	log.info("eDocumento Emisor: {} | {} | {}",documento.getIdComprobante(),eDocumento.getNumeroDocumentoEmisor(), eDocumento.getTipoDocumentoEmisor());
//    	documento.setIdComprobante(eDocumento.getRucRazonSocialRemitente()+eDocumento.getTipoDocumento()+val0+val1);
//		documento.setTipoDocumento(eDocumento.getTipoDocumento());
//		documento.setSerie(val0);
//		documento.setNumeroCorrelativo(val1);		
	}	
	
	public void generarEDocumentCDR_SummaryResumen(Documento documento, EDocumentoCDR eDocumentoCDR, 
			EResumenDocumento eDocumento) {
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		try {
			eDocumentoCDR.setAutorización_Comprobante(UUID.randomUUID().toString());
			eDocumentoCDR.setFecha_comprobacion_comprobante(new Date());
			eDocumentoCDR.setHora_comprobacion_comprobante(eDocumentoCDR.getFecha_comprobacion_comprobante());
			eDocumentoCDR.setRuc_emisor_pse(eDocumentoCDR.getRuc_emisor_pse()+Constantes.CDR_RUC_VF);
			eDocumentoCDR.setRuc_ose(Constantes.CDR_RUC_OSE+Constantes.CDR_RUC_VF);		
			eDocumentoCDR.setCodigo_respuesta("0|"+Constantes.CDR_SUNAT_PE);
			eDocumentoCDR.setDescripcion_respuesta("El comprobante numero "+eDocumento.getIdDocumento()+", ha sido aceptada");
			eDocumentoCDR.setCodigo_observacion(documento.getObservaNumero()+"|"+Constantes.CDR_SUNAT_CR);
			eDocumentoCDR.setDescripcion_observacion(documento.getObservaDescripcion());
			eDocumentoCDR.setSerie_numero_comprobante(eDocumento.getIdDocumento());
			//eDocumentoCDR.setFecha_emisión_comprobante(eDocumento.getFechaEmisionComprobante());
			//eDocumentoCDR.setHora_emisión_comprobante(eDocumento.getFechaEmisionComprobante());
			eDocumentoCDR.setFecha_emisión_comprobante(eDocumento.getFechaGeneracionResumen());
			eDocumentoCDR.setHora_emisión_comprobante(eDocumento.getFechaGeneracionResumen());
//			if(documento.getTipoComprobante().equals(OseConstantes.SUNAT_COMUNICACION_BAJAS)) {
//				eDocumentoCDR.setFecha_emisión_comprobante(eDocumento.getFechaGeneracionResumen());
//				eDocumentoCDR.setHora_emisión_comprobante(eDocumento.getFechaGeneracionResumen());
//			}
			eDocumentoCDR.setTipo_comprobante(eDocumento.getTipoDocumento());
			eDocumentoCDR.setHash_comprobante(eDocumento.getHashCode());
			eDocumentoCDR.setNumero_documento_emisor(eDocumento.getNumeroDocumentoEmisor());
			eDocumentoCDR.setTipo_documento_emisor(eDocumento.getTipoDocumentoEmisor());
			eDocumentoCDR.setNumero_documento_receptor(eDocumento.getNumeroDocumentoAdquiriente());
			eDocumentoCDR.setTipo_documento_receptor(eDocumento.getTipoDocumentoAdquiriente());
			//log.info("eDocumento.getFechaDocumento() "+eDocumento.getFechaDocumento());
			//log.info("eDocumento.getFechaEmisionComprobante() "+eDocumento.getFechaEmisionComprobante());
			String eDocumentResponse = eDocumentoCDR.getCodigo_respuesta()+"|"
				+eDocumentoCDR.getDescripcion_respuesta()+"|"
				+eDocumentoCDR.getCodigo_observacion()+"|"
				+eDocumentoCDR.getDescripcion_observacion()+"|"
				+eDocumentoCDR.getSerie_numero_comprobante()+"|"
				+sdf.format(eDocumentoCDR.getFecha_emisión_comprobante())+"|"
				+eDocumentoCDR.getTipo_comprobante()+"|"
				+eDocumentoCDR.getHash_comprobante()+"|"
				+eDocumentoCDR.getNumero_documento_emisor()+"|"
				+eDocumentoCDR.getTipo_documento_emisor()+"|"
				+eDocumentoCDR.getNumero_documento_receptor()+"|"
				+eDocumentoCDR.getTipo_documento_receptor();
			eDocumentoCDR.setDocument_response(eDocumentResponse);		
			CdrDozer21Build cdrDozer21Build = new CdrDozer21Build();
			byte[] cdr = cdrDozer21Build.generaCDR21_Resumen(eDocumentoCDR); 
			if(cdr!=null)
				documento.setCdr(cdr);
			else {
				documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);
				documento.setErrorLog("Problemaa para generar el CDR2.1");
			}
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);	
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			documento.setErrorLog(errors.toString());
			log.error("Exception \n"+errors);
		}	
	}
	
	public void document2TbComprobante_SummaryResumen(Documento documento, EResumenDocumento eDocumento) {
        String[] values = StringUtils.split(eDocumento.getIdDocumento(), "-");
        String val0 = "";
        String val1 = "";
        String val2 = "";
        if(values!=null && values.length>0) {
        	val0 = values[0];
        	if(values.length>1)
        		val1 = values[1];
        	if(values.length>2)
        		val2 = values[2];
        }
		documento.setIdComprobante(eDocumento.getNumeroDocumentoEmisor()+eDocumento.getTipoDocumento()+val1+val2);
		//log.info("eDocumento.getNumeroDocumentoEmisor() "+eDocumento.getNumeroDocumentoEmisor());		
		documento.setRucEmisor(Long.parseLong(eDocumento.getNumeroDocumentoEmisor()));		
		documento.setTipoDocumento(eDocumento.getTipoDocumento());
		documento.setSerie(val1);
		documento.setNumeroCorrelativo(val2);
	}
	
	public void generarEDocumentCDR_SummaryReversion(Documento documento, EDocumentoCDR eDocumentoCDR, 
			EReversionDocumento eDocumento) {
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		try {
			eDocumentoCDR.setAutorización_Comprobante(UUID.randomUUID().toString());
			eDocumentoCDR.setFecha_comprobacion_comprobante(new Date());
			eDocumentoCDR.setHora_comprobacion_comprobante(eDocumentoCDR.getFecha_comprobacion_comprobante());
			eDocumentoCDR.setRuc_emisor_pse(eDocumentoCDR.getRuc_emisor_pse()+Constantes.CDR_RUC_VF);
			eDocumentoCDR.setRuc_ose(Constantes.CDR_RUC_OSE+Constantes.CDR_RUC_VF);		
			eDocumentoCDR.setCodigo_respuesta("0|"+Constantes.CDR_SUNAT_PE);
			eDocumentoCDR.setDescripcion_respuesta("El comprobante numero "+eDocumento.getIdDocumento()+", ha sido aceptada");
			eDocumentoCDR.setCodigo_observacion(documento.getObservaNumero()+"|"+Constantes.CDR_SUNAT_CR);
			eDocumentoCDR.setDescripcion_observacion(documento.getObservaDescripcion());
			eDocumentoCDR.setSerie_numero_comprobante(eDocumento.getIdDocumento());
			eDocumentoCDR.setFecha_emisión_comprobante(eDocumento.getFechaDocumento());
			eDocumentoCDR.setHora_emisión_comprobante(eDocumento.getFechaDocumento());
			//eDocumentoCDR.setFecha_emisión_comprobante(eDocumento.getFechaEmisionreversion());
			//eDocumentoCDR.setHora_emisión_comprobante(eDocumento.getFechaEmisionreversion());
			eDocumentoCDR.setTipo_comprobante(eDocumento.getTipoDocumento());
			eDocumentoCDR.setHash_comprobante(eDocumento.getHashCode());
			eDocumentoCDR.setNumero_documento_emisor(eDocumento.getNumeroDocumentoEmisor());
			eDocumentoCDR.setTipo_documento_emisor(eDocumento.getTipoDocumentoEmisor());
			eDocumentoCDR.setNumero_documento_receptor(eDocumento.getNumeroDocumentoAdquiriente());
			eDocumentoCDR.setTipo_documento_receptor(eDocumento.getTipoDocumentoAdquiriente());
		
			String eDocumentResponse = eDocumentoCDR.getCodigo_respuesta()+"|"
				+eDocumentoCDR.getDescripcion_respuesta()+"|"
				+eDocumentoCDR.getCodigo_observacion()+"|"
				+eDocumentoCDR.getDescripcion_observacion()+"|"
				+eDocumentoCDR.getSerie_numero_comprobante()+"|"
				+sdf.format(eDocumentoCDR.getFecha_emisión_comprobante())+"|"
				+eDocumentoCDR.getTipo_comprobante()+"|"
				+eDocumentoCDR.getHash_comprobante()+"|"
				+eDocumentoCDR.getNumero_documento_emisor()+"|"
				+eDocumentoCDR.getTipo_documento_emisor()+"|"
				+eDocumentoCDR.getNumero_documento_receptor()+"|"
				+eDocumentoCDR.getTipo_documento_receptor();
			eDocumentoCDR.setDocument_response(eDocumentResponse);
		
			CdrDozer21Build cdrDozer21Build = new CdrDozer21Build();
			byte[] cdr = cdrDozer21Build.generaCDR21_Resumen(eDocumentoCDR); 
			if(cdr!=null)
				documento.setCdr(cdr);
			else {
				documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);
				documento.setErrorLog("Problemaa para generar el CDR2.1");
			}
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);	
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			documento.setErrorLog(errors.toString());
			log.error("Exception \n"+errors);
		}	
	}

	public void document2TbComprobante_SummaryReversion(Documento documento, 
			EReversionDocumento eDocumento) {
        String[] values = StringUtils.split(eDocumento.getIdDocumento(), "-");    
        String val0 = "";
        String val1 = "";
        String val2 = "";
        if(values!=null && values.length>0) {
        	val0 = values[0];
        	if(values.length>1)
        		val1 = values[1];
        	if(values.length>2)
        		val2 = values[2];
        }
		documento.setIdComprobante(eDocumento.getNumeroDocumentoEmisor()+eDocumento.getTipoDocumento()+val1+val2);			
		documento.setRucEmisor(Long.parseLong(eDocumento.getNumeroDocumentoEmisor()));
		documento.setTipoDocumento(eDocumento.getTipoDocumento());
		documento.setSerie(val1);
		documento.setNumeroCorrelativo(val2);
	}
	
	private void changeFecha_emisión_comprobante(EDocumentoCDR eDocumentoCDR, String sTime) {
		try {
			if(sTime!= null && !sTime.isEmpty()) {
				if(sTime.length()<9)
					return;
				XMLGregorianCalendar xgc = DateUtil.convertDateToXMLGC(eDocumentoCDR.getFecha_emisión_comprobante());
				//log.info("sTime "+sTime);
				String shora = sTime.replace(".", ":");
				//log.info("shora "+shora);
				String fecha = xgc.toString()+" "+shora;	
				//log.info("fecha "+fecha);				
				Date date1 = sdf1.parse(fecha);
				eDocumentoCDR.setFecha_emisión_comprobante(date1);				
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);
		}
	}
}
