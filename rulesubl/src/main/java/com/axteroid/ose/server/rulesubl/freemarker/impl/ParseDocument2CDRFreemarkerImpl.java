package com.axteroid.ose.server.rulesubl.freemarker.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesubl.freemarker.ParseDocument2CDRFreemarker;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EDocumentoCDR;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.ubl21.builder.CdrDozer21Build;

public class ParseDocument2CDRFreemarkerImpl implements ParseDocument2CDRFreemarker{
	private static final Logger log = LoggerFactory.getLogger(ParseDocument2CDRFreemarkerImpl.class);
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSS");
	
	public void generarEDocumentCDR(Documento tbComprobante, EDocumentoCDR eDocumentoCDR, 
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
			eDocumentoCDR.setCodigo_observacion(tbComprobante.getObservaNumero()+"|"+Constantes.CDR_SUNAT_CR);
			eDocumentoCDR.setDescripcion_observacion(tbComprobante.getObservaDescripcion());
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
			
			//CdrDozer21Build cdrDozer21Build = new CdrDozer21Build();
			//byte[] cdr = cdrDozer21Build.generaCDR21(eDocumentoCDR); 	
			
			byte[] cdr = new byte[1];
			if(cdr!=null)
				tbComprobante.setCdr(cdr);
			else {
				tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0137);
				tbComprobante.setErrorLog("Problemaa para generar el CDR2.1");
			}
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			tbComprobante.setErrorLog(errors.toString());
			log.error("Exception \n"+errors);
		}		
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
