package com.axteroid.ose.server.builder.review;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.apirest.sunat.ConsultaSunat;
import com.axteroid.ose.server.builder.sendsunat.SunatDocumentSend;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.axteroid.ose.server.tools.util.DocumentUtil;
import com.axteroid.ose.server.tools.xml.UblResponseSunat;

public class SunatWsResponseReview {
	private static final Logger log = LoggerFactory.getLogger(SunatWsResponseReview.class);
	
	public void getRequestSunat(Documento tbComprobante) {
		SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
		sunatDocumentSend.getStatusAR(tbComprobante);	
		if(!tbComprobante.getRespuestaSunat().equals(Constantes.SUNAT_RESPUESTA_0)) {			
			this.getConsultaSunat(tbComprobante);
		}
		log.info("getRequestSunat: "+tbComprobante.toString()+" | "+tbComprobante.getEstado()+" | "+tbComprobante.getRespuestaSunat());
	}
	
    public void getConsultaSunat(Documento tbComprobante) {
				String ambiente = DirUtil.getAmbiente();   
    	if((ambiente.equals(Constantes.DIR_AMB_BETA)))
    		return;
		
    	Timestamp timestampStart = new Timestamp(System.currentTimeMillis()); 
    	log.info("getConsultaSunat: "+tbComprobante.getRucEmisor()+"-"+ tbComprobante.getTipoDocumento()+"-"+tbComprobante.getSerie()+"-"+tbComprobante.getNumeroCorrelativo());    	
		String ruc = String.valueOf(tbComprobante.getRucEmisor());			
		Map<String, String> respuestaConsulta = ConsultaSunat.lookupCDR(ruc,tbComprobante.getTipoDocumento(),tbComprobante.getSerie(),tbComprobante.getNumeroCorrelativo());			
		if(respuestaConsulta.get("description")!= null)
			tbComprobante.setErrorLogSunat(respuestaConsulta.get("description"));		
		if((respuestaConsulta.get("responseCode")!= null) && 
				(respuestaConsulta.get("responseCode").equals(Constantes.SUNAT_RESPUESTA_0))){
			tbComprobante.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_0);
			String base64CDR = respuestaConsulta.get("content");
			byte[] cdr = base64CDR.getBytes();
			byte[] zip = Base64.decodeBase64(cdr);
			byte[] xml = UblResponseSunat.extractXMLFromCDR(zip);
			tbComprobante.setMensajeSunat(xml);
			tbComprobante.setEstado(Constantes.SUNAT_CDR_AUTORIZADO);
			return;
		}
		if(respuestaConsulta.get("responseCode")!= null)
			tbComprobante.setRespuestaSunat(respuestaConsulta.get("responseCode"));
		if(respuestaConsulta.get("content")!= null) {
			String base64CDR = respuestaConsulta.get("content");
			byte[] cdr = base64CDR.getBytes();
			byte[] zip = Base64.decodeBase64(cdr);
			byte[] xml = UblResponseSunat.extractXMLFromCDR(zip);
			tbComprobante.setMensajeSunat(xml);
			return;
		}
		
		if((respuestaConsulta.get("responseCode")== null)
				&& (respuestaConsulta.get("statusCode")!= null)) 
			tbComprobante.setRespuestaSunat(respuestaConsulta.get("statusCode"));
		if(respuestaConsulta.get("statusMessage")!= null)
				tbComprobante.setErrorLogSunat(respuestaConsulta.get("statusMessage"));		
		
		for(Entry<String, String> entry : respuestaConsulta.entrySet()) {
			log.info("key: "+entry.getKey()+" value: "+entry.getValue());
		}
		Timestamp timestampEnd = new Timestamp(System.currentTimeMillis());
    	log.info("getConsultaSunat timestamp: "+DocumentUtil.compareTwoTimeStamps(timestampEnd, timestampStart));		

	}	
}
