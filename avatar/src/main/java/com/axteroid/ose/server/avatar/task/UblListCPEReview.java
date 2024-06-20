package com.axteroid.ose.server.avatar.task;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesubl.builder.ParseXML2Document;
import com.axteroid.ose.server.rulesubl.builder.impl.ParseXml2DocumentImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.tools.util.StringUtil;
import com.axteroid.ose.server.tools.xml.UblResponseSunat;

public class UblListCPEReview {
	private static final Logger log = LoggerFactory.getLogger(UblListCPEReview.class);

	public void validateComprobantesReview(Documento tbComprobante) {
		log.info("validateComprobantesReview tbComprobante: "+tbComprobante.getNombre());
		switch(tbComprobante.getTipoDocumento()){
			case Constantes.SUNAT_NOTA_CREDITO:
				this.revisarRespuesta2NCNDSunatList(tbComprobante);
				break;
			case Constantes.SUNAT_NOTA_DEBITO:
				this.revisarRespuesta2NCNDSunatList(tbComprobante);
				break;
			case Constantes.SUNAT_COMUNICACION_BAJAS:
				this.revisarRespuesta2RASunatList(tbComprobante);
				break;
			case Constantes.SUNAT_RESUMEN_DIARIO:
				this.revisarRespuesta2RCSunatList(tbComprobante);
				break;
			case Constantes.SUNAT_REVERSION:
				this.revisarRespuesta2RRSunatList(tbComprobante);
				break;
		}
	}	
	
	public void revisarRespuesta2RASunatList(Documento tbComprobante) {
		log.info("revisarRespuesta2RASunatList: "+tbComprobante.getId()+" - "+tbComprobante.getNombre());
		//log.info("tbComprobante.getErrorLogSunat() a) "+tbComprobante.getErrorLogSunat());	
		//String errorLogSunat = tbComprobante.getErrorLogSunat();
		//List<String> listSunatError = this.getComprobantesResumenSUNAT(errorLogSunat);
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		EResumenDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryResumen(tbComprobante);
		tbComprobante.setErrorLogSunat("");	
		UBLListDataValidate ublListDataValidate = new UBLListDataValidate();
		ublListDataValidate.revisarRespuesta2RARCSunatList(tbComprobante, eDocumento);	
	}
	
	public void revisarRespuesta2RCSunatList(Documento tbComprobante) {
		log.info("revisarRespuesta2RCSunatList: "+tbComprobante.getId()+" - "+tbComprobante.getNombre());
		//String errorLogSunat = tbComprobante.getErrorLogSunat();
		//List<String> listSunatError = this.getComprobantesResumenSUNAT(errorLogSunat);
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		EResumenDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryResumen(tbComprobante);
		tbComprobante.setErrorLogSunat("");		
		UBLListDataValidate ublListDataValidate = new UBLListDataValidate();
		ublListDataValidate.revisarRespuesta2RARCSunatList(tbComprobante, eDocumento);
	}
		
	public void revisarRespuesta2RRSunatList(Documento tbComprobante) {
		log.info("revisarRespuesta2RRSunatList: "+tbComprobante.getId()+" - "+tbComprobante.getNombre());
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		EReversionDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryReversion(tbComprobante);
		tbComprobante.setErrorLogSunat("");
		UBLListDataValidate ublListDataValidate = new UBLListDataValidate();
		ublListDataValidate.revisarRespuesta2RRSunatList(tbComprobante, eDocumento);		
		log.info("tbComprobante.getErrorLogSunat(): "+tbComprobante.getErrorLogSunat());
	}	
	
	public void revisarRespuesta2NCNDSunatList(Documento tbComprobante) {
		log.info("revisarRespuesta2NCNDSunatList: "+tbComprobante.getId()+" - "+tbComprobante.getNombre());
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		EDocumento eDocumento = parseXML2Document.parseUBLSendBill(tbComprobante);
		tbComprobante.setErrorLogSunat("");	
		UBLListDataValidate ublListDataValidate = new UBLListDataValidate();
		ublListDataValidate.revisarRespuesta2NCNDSunatList(tbComprobante, eDocumento);					
	}
		
	public List<String> getComprobantesResumenSUNAT(String s0) {
		//log.info("sunat-s0: "+s0);
		List<String> s5 = new ArrayList<String>();
		String separador = Pattern.quote("[[");
		String separador_2989 = Pattern.quote("Boleta relacionada");
		String[] s1 = s0.split(separador);
		String[] s_1 = s0.split(separador_2989);
		log.info("s1.length: "+s1.length+" | s_1.length: "+s_1.length);
		if((s1.length<2) && (s_1.length<2))
			return s5;
		if(s1.length>1) {
			String[] s2 = s1[1].split(",");
			log.info("s2.length: "+s2.length);		
			for(String s : s2) {
				//log.info("s: "+s);		
				String s4=s.replaceAll("[\\[\\]\\']", "");
				//log.info("sunat-s4: "+s4);
				s5.add(s4);
			}
		}
		if(s_1.length>1) {			
			String[] s_2 = s_1[1].split(" ");
			log.info("s_2.length: "+s_2.length);	
			for(String s : s_2) {
				//log.info("s: "+s);		
				if(s.trim().isEmpty())
					continue;
				String s_4=s.replaceAll(" ", "");
				if(StringUtil.hasString(s_4.substring(0,1)))
					s5.add(s_4);
			}
		}
		return s5;
	}	

	public List<String> getComprobantesResumen(String s0) {
		//log.info("sunat-s0: "+s0);
		List<String> s5 = new ArrayList<String>();
//		String separador = Pattern.quote("[[");
//		String separador_2989 = Pattern.quote("Boleta relacionada");
//		String[] s1 = s0.split(separador);
//		String[] s_1 = s0.split(separador_2989);
//		log.info("s1.length: "+s1.length+" | s_1.length: "+s_1.length);
//		if((s1.length<2) && (s_1.length<2))
//			return s5;
//		if(s1.length>1) {
			//String[] s2 = s1[1].split(";");
			String[] s2 = s0.split(";");
			log.info("s2.length: "+s2.length);		
			for(String s : s2) {
				//log.info("s: "+s);		
				String s4=s.replaceAll("[\\[\\]\\']", "");
				//log.info("sunat-s4: "+s4);
				s5.add(s4);
			}
//		}
//		if(s_1.length>1) {			
//			String[] s_2 = s_1[1].split(" ");
//			log.info("s_2.length: "+s_2.length);	
//			for(String s : s_2) {
//				//log.info("s: "+s);		
//				if(s.trim().isEmpty())
//					continue;
//				String s_4=s.replaceAll(" ", "");
//				if(StringUtil.hasString(s_4.substring(0,1)))
//					s5.add(s_4);
//			}
//		}
		return s5;
	}	
	public void readRespuestaSunat(Documento tbComprobante){
		log.info("readRespuestaSunat  ");
		byte[] resp = tbComprobante.getMensajeSunat();
		if((resp!=null) && (resp.length>0)){ 
			String stResp = new String(resp);
			UblResponseSunat cdrSunatXmlReader = new UblResponseSunat();
			String respuestaSunat = cdrSunatXmlReader.readResponseCodeCDR(tbComprobante.getMensajeSunat());
			if(!(respuestaSunat!=null))
				return;		
			tbComprobante.setRespuestaSunat(respuestaSunat);
			//tbComprobante.setErrorLogSunat(xmlReader.readResponseCodeCDRNote(tbComprobante.getMensajeSunat()));
			tbComprobante.setErrorLogSunat(cdrSunatXmlReader.readResponseCodeCDRDescription(tbComprobante.getMensajeSunat()));
		}
	}

}
