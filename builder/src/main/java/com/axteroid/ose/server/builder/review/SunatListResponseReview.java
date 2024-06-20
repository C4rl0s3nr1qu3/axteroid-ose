package com.axteroid.ose.server.builder.review;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.builder.content.impl.ReturnResponseImpl;
import com.axteroid.ose.server.builder.sendsunat.SunatDocumentSend;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.ComprobantesPagoElectronicos;
import com.axteroid.ose.server.rulesejb.dao.ComprobantesPagoElectronicosDAOLocal;
import com.axteroid.ose.server.rulesejb.rules.UBLListValidateLocal;
import com.axteroid.ose.server.rulesejb.rules.impl.UBLListValidateImpl;
import com.axteroid.ose.server.rulesubl.builder.ParseXML2Document;
import com.axteroid.ose.server.rulesubl.builder.impl.ParseXml2DocumentImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.tools.util.StringUtil;
import com.axteroid.ose.server.tools.xml.XmlSign;

public class SunatListResponseReview {
	private static final Logger log = LoggerFactory.getLogger(SunatListResponseReview.class);
	private ReturnResponseImpl returnValidateRules = new ReturnResponseImpl();

	public void validateSunatListComprobantesReview(Documento tbComprobante) {
		log.info("validateSunatListComprobantesReview tbComprobante: "+tbComprobante.getNombre());
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
		String errorLogSunat = tbComprobante.getErrorLogSunat();
		List<String> listSunatError = this.getComprobantesResumenSUNAT(errorLogSunat);
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		EResumenDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryResumen(tbComprobante);
		tbComprobante.setErrorLogSunat(String.valueOf(Constantes.SUNAT_RESPUESTA_SD));	
		String customizaVersion = tbComprobante.getCustomizaVersion();
		tbComprobante.setCustomizaVersion(String.valueOf(Constantes.SUNAT_IndEstCpe_SD));
		UBLListValidateLocal ublListValidateLocal = new UBLListValidateImpl();
		ublListValidateLocal.revisarRespuesta2RARCSunatList(tbComprobante, eDocumento);	
		int oseServerOK = tbComprobante.getLongitudNombre();	
		int oseServerNoOK = Integer.parseInt(tbComprobante.getDireccion());
		int oseServerError = this.getCountItemsError(tbComprobante.getErrorLogSunat());	
		int sunatError = listSunatError.size();	
		int itemsResumen = eDocumento.getItems().size();	
		int status = Integer.parseInt(tbComprobante.getCustomizaVersion()!=null ? tbComprobante.getCustomizaVersion() : "9");
		tbComprobante.setCustomizaVersion(customizaVersion);
		this.revisarReglasRespuesta(tbComprobante, oseServerOK, oseServerNoOK, oseServerError, 
				sunatError, itemsResumen, errorLogSunat, listSunatError, status);
	}
	
	public void revisarRespuesta2RCSunatList(Documento tbComprobante) {
		log.info("revisarRespuesta2RCSunatList: "+tbComprobante.getId()+" - "+tbComprobante.getNombre()+" | ErrorLogSunat(): "+tbComprobante.getErrorLogSunat());	
		String errorLogSunat = tbComprobante.getErrorLogSunat();
		//log.info("RespuestaSunat(0.a): "+tbComprobante.getRespuestaSunat()+" | ErrorLogSunat(): "+tbComprobante.getErrorLogSunat());							
		List<String> listSunatError = this.getComprobantesResumenSUNAT(errorLogSunat);
		//log.info("RespuestaSunat(0.b): "+tbComprobante.getRespuestaSunat()+" | ErrorLogSunat(): "+tbComprobante.getErrorLogSunat());							
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		//log.info("RespuestaSunat(0.c): "+tbComprobante.getRespuestaSunat()+" | ErrorLogSunat(): "+tbComprobante.getErrorLogSunat());							
		EResumenDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryResumen(tbComprobante);
		//log.info("RespuestaSunat(0.d): "+tbComprobante.getRespuestaSunat()+" | ErrorLogSunat(): "+tbComprobante.getErrorLogSunat());							
		tbComprobante.setErrorLogSunat(String.valueOf(Constantes.SUNAT_RESPUESTA_SD));		
		String customizaVersion = tbComprobante.getCustomizaVersion();
		tbComprobante.setCustomizaVersion(String.valueOf(Constantes.SUNAT_IndEstCpe_SD));
		UBLListValidateLocal ublListValidateLocal = new UBLListValidateImpl();
		ublListValidateLocal.revisarRespuesta2RARCSunatList(tbComprobante, eDocumento);
		int oseServerOK = tbComprobante.getLongitudNombre();	
		int oseServerNoOK = Integer.parseInt(tbComprobante.getDireccion());
		int oseServerError = this.getCountItemsError(tbComprobante.getErrorLogSunat());		
		int sunatError = listSunatError.size();
		int itemsResumen = eDocumento.getItems().size();	
		
		int status = Integer.parseInt(tbComprobante.getCustomizaVersion()!=null ? tbComprobante.getCustomizaVersion() : "9");
		tbComprobante.setCustomizaVersion(customizaVersion);
		this.revisarReglasRespuesta(tbComprobante, oseServerOK, oseServerNoOK, oseServerError, 
				sunatError, itemsResumen, errorLogSunat, listSunatError, status);
	}
	
	public void revisarReglasRespuesta(Documento tbComprobante, int oseServerOK, int oseServerNoOK, int oseServerError, 
			int sunatError, int itemsResumen, String errorLogSunat, List<String> listSunatError, int status) {
		log.info("revisarReglasRespuesta: "+tbComprobante.getId()+" - "+tbComprobante.getNombre()+" | ErrorLogSunat(): "+tbComprobante.getErrorLogSunat());	
		tbComprobante.setDireccion(String.valueOf(itemsResumen));
		tbComprobante.setLongitudNombre(oseServerOK);
		int ajusteSunatError = 0;
		if(sunatError > 134)
			ajusteSunatError = oseServerError - sunatError;
		log.info("oseServer OK: "+oseServerOK+" | itemsResumen: "+itemsResumen+" | oseServerNoOK: "+oseServerNoOK+" | oseServerError: "+oseServerError+" | sunatError: "+sunatError+" | ajusteSunatError: "+ajusteSunatError);
		if((oseServerOK == itemsResumen) && (sunatError == 0)) {
			log.info("RespuestaSunat(1): "+tbComprobante.getRespuestaSunat()+" | ErrorLogSunat(): "+tbComprobante.getErrorLogSunat());								
//			if(itemsResumen == 1) {
//				tbComprobante.setEstado(OseConstantes.SUNAT_CDR_AUTORIZADO);
//				tbComprobante.setRespuestaSunat(OseConstantes.CONTENT_TRUE);
//				tbComprobante.setFechaRespuestaSunat(new Date());
//				returnValidateRules.updateComprobante_Send(tbComprobante);						
//				return;
//			}	
			if((tbComprobante.getRespuestaSunat().equals(Constantes.SUNAT_ERROR_2987))) {
				if(tbComprobante.getErrorLogSunat().isEmpty()) {
					tbComprobante.setErrorLogSunat(errorLogSunat);
					tbComprobante.setFechaRespuestaSunat(new Date());
					returnValidateRules.updateComprobante_Send(tbComprobante);			
					return;
				}
			}
			tbComprobante.setMensajeSunat(null);
			SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
			sunatDocumentSend.getSendSunat(tbComprobante);		
			return;
		}
		if((oseServerOK == itemsResumen) && (sunatError > 0)) {
			log.info("RespuestaSunat(2): "+tbComprobante.getRespuestaSunat()+" | ErrorLogSunat(): "+tbComprobante.getErrorLogSunat());					
			XmlSign xmlSign = new XmlSign();
			int countCPE = 0;
			String documentLine = "";
			if((tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS)) ||
					(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_REVERSION)))
				documentLine = "sac:VoidedDocumentsLine";
			if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO))
				documentLine = "sac:SummaryDocumentsLine";
			String listError = "";
			for(String error : listSunatError){
				log.info("listSunatError: {} | error: {}",listSunatError.toString(),error);
				String[] cpe = error.split("-");
				String documentTypeCode = cpe[0].replace("[", "");
				String id1 = cpe[2].replace("]", "");
				String id = cpe[1]+"-"+id1;
				boolean bCpeUbl = xmlSign.countCpeUBLResumen(tbComprobante.getUbl(), documentTypeCode, id, documentLine);
				if(bCpeUbl)
					countCPE++;
				listError = listError+error;
			}
			if(countCPE == 0){
				tbComprobante.setMensajeSunat(null);
				SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
				sunatDocumentSend.getSendSunat(tbComprobante);		
				return;
			}			
			log.info("countCPE: {} | listError: {}",countCPE,listError);
			if((countCPE > 0) && (countCPE == itemsResumen)){
				if((tbComprobante.getRespuestaSunat().equals(Constantes.SUNAT_ERROR_2663))) {
					tbComprobante.setMensajeSunat(null);
					SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
					sunatDocumentSend.getSendSunat(tbComprobante);		
					return;
				}					
				tbComprobante.setEstado(Constantes.SUNAT_CDR_AUTORIZADO);
				tbComprobante.setRespuestaSunat(Constantes.CONTENT_TRUE);
				tbComprobante.setErrorLogSunat(listError);
				tbComprobante.setFechaRespuestaSunat(new Date());
				returnValidateRules.updateComprobante_Send(tbComprobante);			
				return;
			}
		}
		if((oseServerNoOK == itemsResumen) && 
				!(tbComprobante.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_2989))) {
			log.info("RespuestaSunat(3): "+tbComprobante.getRespuestaSunat()+" | ErrorLogSunat(): "+tbComprobante.getErrorLogSunat());					
			tbComprobante.setEstado(Constantes.SUNAT_CDR_AUTORIZADO);
			tbComprobante.setRespuestaSunat(Constantes.CONTENT_TRUE);
			tbComprobante.setErrorLogSunat(errorLogSunat);
			tbComprobante.setFechaRespuestaSunat(new Date());
			returnValidateRules.updateComprobante_Send(tbComprobante);			
			return;
		}	
		if(sunatError == itemsResumen) {			
			log.info("RespuestaSunat(4): "+tbComprobante.getRespuestaSunat()+" | ErrorLogSunat(): "+tbComprobante.getErrorLogSunat());
			if((tbComprobante.getRespuestaSunat().equals(Constantes.SUNAT_ERROR_2663))) {
				tbComprobante.setMensajeSunat(null);
				SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
				sunatDocumentSend.getSendSunat(tbComprobante);		
				return;
			}			
			tbComprobante.setEstado(Constantes.SUNAT_CDR_AUTORIZADO);
			tbComprobante.setRespuestaSunat(Constantes.CONTENT_TRUE);
			tbComprobante.setErrorLogSunat(errorLogSunat);
			tbComprobante.setFechaRespuestaSunat(new Date());
			returnValidateRules.updateComprobante_Send(tbComprobante);			
			return;
		}				
		if((itemsResumen > oseServerOK) && (
				(tbComprobante.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_Continue)) ||
				(tbComprobante.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_OK)) ||
				(tbComprobante.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_NO_FOUND)) ||
				(tbComprobante.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_Not_Acceptable)) ||
				(tbComprobante.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_SERVICIO_INHABILITADO)) ||
				(tbComprobante.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_SD)) ||
				(tbComprobante.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_133)) ||
				(tbComprobante.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_306)) ||
				(tbComprobante.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0000)) ||
				(tbComprobante.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0100_C)) ||
				(tbComprobante.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0109)) ||
				(tbComprobante.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0130)) ||
				(tbComprobante.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0132)) ||
				(tbComprobante.getRespuestaSunat().trim().equals(Constantes.SUNAT_ERROR_0200)))){
			log.info("tbComprobante.getRespuestaSunat(5): "+tbComprobante.getRespuestaSunat()
			+" | tbComprobante.getErrorLogSunat(): "+tbComprobante.getErrorLogSunat()
			+" | tbComprobante.getAdvertencia(): "+tbComprobante.getAdvertencia()
			+" | status: "+status);					
			tbComprobante.setFechaRespuestaSunat(new Date());	
			tbComprobante.setEstado(Constantes.SUNAT_CDR_ERROR_SENDSUMMARY);
			if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO)) {				
				if(status == Constantes.SUNAT_CodigoOperacion_Adicionar)  
					tbComprobante.setRespuestaSunat(Constantes.SUNAT_ERROR_2282);		
				if(status == Constantes.SUNAT_CodigoOperacion_Modificar)  
					tbComprobante.setRespuestaSunat(Constantes.SUNAT_ERROR_2987);		
				if(status == Constantes.SUNAT_CodigoOperacion_Anulado)  
					tbComprobante.setRespuestaSunat(Constantes.SUNAT_ERROR_2987);		
			}
			if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS)) {
				if(status == Constantes.SUNAT_IndEstCpe_Recha)  
					tbComprobante.setRespuestaSunat(Constantes.SUNAT_ERROR_2398);		
				if(status == Constantes.SUNAT_IndEstCpe_Anula)  
					tbComprobante.setRespuestaSunat(Constantes.SUNAT_ERROR_2323);
				if(status == Constantes.SUNAT_IndEstCpe_SD)  
					tbComprobante.setRespuestaSunat(Constantes.SUNAT_ERROR_2105);
			}
			returnValidateRules.updateComprobante_Send(tbComprobante);			
			return;
		}		
		if((tbComprobante.getRespuestaSunat()!=null) && (tbComprobante.getRespuestaSunat().length()==0)){
			log.info("RespuestaSunat(6): "+tbComprobante.getRespuestaSunat()+" | ErrorLogSunat(): "+tbComprobante.getErrorLogSunat());							
			if (oseServerNoOK>0 || oseServerError > 0 || sunatError > 0 )
				tbComprobante.setRespuestaSunat(Constantes.SUNAT_ERROR_0000);	
			if(itemsResumen > 0 && oseServerOK == 0 && oseServerNoOK == 0 && oseServerError == 0 && sunatError == 0)
				tbComprobante.setRespuestaSunat(Constantes.SUNAT_ERROR_0000);	
		}
		tbComprobante.setEstado(Constantes.SUNAT_CDR_ERROR_SENDSUMMARY);
		log.info("RespuestaSunat(7): "+tbComprobante.getRespuestaSunat()+" | ErrorLogSunat(): "+tbComprobante.getErrorLogSunat());		
		tbComprobante.setFechaRespuestaSunat(new Date());
		returnValidateRules.updateComprobante_Send(tbComprobante);						
	}	
		
	public void revisarRespuesta2RRSunatList(Documento tbComprobante) {
		log.info("revisarRespuesta2RRSunatList: "+tbComprobante.getId()+" - "+tbComprobante.getNombre()+" | ErrorLogSunat(): "+tbComprobante.getErrorLogSunat());	
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		EReversionDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryReversion(tbComprobante);
		tbComprobante.setErrorLogSunat("");
		UBLListValidateLocal ublListValidateLocal = new UBLListValidateImpl();
		ublListValidateLocal.revisarRespuesta2RRSunatList(tbComprobante, eDocumento);		
		log.info("ErrorLogSunat(): "+tbComprobante.getErrorLogSunat());
		if(tbComprobante.getErrorLogSunat().trim().isEmpty()) {
			tbComprobante.setEstado(Constantes.SUNAT_CDR_AUTORIZADO);
			tbComprobante.setRespuestaSunat(Constantes.CONTENT_TRUE);	
			returnValidateRules.updateComprobante_Send(tbComprobante);
			return;
		}
		log.info("LongitudNombre(): "+tbComprobante.getLongitudNombre()+" eDocumento.getItems().size(): "+eDocumento.getItems().size());
		if(tbComprobante.getLongitudNombre()==eDocumento.getItems().size()) {
			tbComprobante.setMensajeSunat(null);
			SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
			sunatDocumentSend.getSendSunat(tbComprobante);
		}else
			returnValidateRules.updateComprobante_Send(tbComprobante);
	}	
	
	public void revisarRespuesta2NCNDSunatList(Documento tbComprobante) {
		log.info("revisarRespuesta2NCNDSunatList: "+tbComprobante.getId()+" - "+tbComprobante.getNombre()+" | ErrorLogSunat(): "+tbComprobante.getErrorLogSunat());	
		//log.info("tbComprobante.getErrorLogSunat() a) "+tbComprobante.getErrorLogSunat());	
		String errorLogSunat = tbComprobante.getErrorLogSunat();
		List<String> listSunat = this.getComprobantesNCNDSUNAT(errorLogSunat);
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		EDocumento eDocumento = parseXML2Document.parseUBLSendBill(tbComprobante);
		tbComprobante.setErrorLogSunat("");	
		UBLListValidateLocal ublListValidateLocal = new UBLListValidateImpl();
		ublListValidateLocal.revisarRespuesta2NCNDSunatList(tbComprobante, eDocumento);				
		int iListSunat = 0;
		if(listSunat.size()>134)
			iListSunat = tbComprobante.getLongitudNombre()-listSunat.size();
		log.info("LongitudNombre(): "+tbComprobante.getLongitudNombre()
			+" | eDocumento.getBillingReference().size(): "+eDocumento.getBillingReference().size()
			+" | eDocumento.getItems().size(): "+eDocumento.getItems().size()
			+" | listSunat.size(): "+listSunat.size()
			+" | iListSunat: "+iListSunat);
//		if((tbComprobante.getLongitudNombre()==eDocumento.getBillingReference().size()) 
//				&& listSunat.size() == 0 ) {	
//			tbComprobante.setMensajeSunat(null);
//			SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
//			sunatDocumentSend.getSendSunat(tbComprobante);
//			return;
//		}	
		if((tbComprobante.getLongitudNombre()==listSunat.size()) 
				&& (tbComprobante.getLongitudNombre()==eDocumento.getItems().size())) {	
			tbComprobante.setEstado(Constantes.SUNAT_CDR_AUTORIZADO);
			tbComprobante.setRespuestaSunat(Constantes.CONTENT_TRUE);
			tbComprobante.setErrorLogSunat(errorLogSunat);
			returnValidateRules.updateComprobante_Send(tbComprobante);
			return;
		}
		if(tbComprobante.getLongitudNombre()==listSunat.size() || iListSunat>0 ) {	
			tbComprobante.setMensajeSunat(null);
			SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
			sunatDocumentSend.getSendSunat(tbComprobante);
			return;
		}	
		if(tbComprobante.getLongitudNombre()==eDocumento.getItems().size()) {
			tbComprobante.setMensajeSunat(null);
			SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
			sunatDocumentSend.getSendSunat(tbComprobante);
			return;
		}
		if(tbComprobante.getRespuestaSunat().equals(Constantes.SUNAT_RESPUESTA_OK)) {	
			tbComprobante.setMensajeSunat(null);
			SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
			sunatDocumentSend.getSendSunat(tbComprobante);
			return;
		}	
		tbComprobante.setErrorLogSunat(errorLogSunat);
		returnValidateRules.updateComprobante_Send(tbComprobante);				
	}
		
	private List<String> getComprobantesResumenSUNAT(String s0) {
		//log.info("getComprobantesResumenSUNAT s0: "+s0);
		List<String> s5 = new ArrayList<String>();
		if((s0==null) || (s0.isEmpty()))
			return s5;
		String separador = Pattern.quote("[[");
		String separador_2989 = Pattern.quote("Boleta relacionada");
		String[] s1 = s0.split(separador);
		String[] s_1 = s0.split(separador_2989);
		//log.info("s1.length: "+s1.length+" | s_1.length: "+s_1.length);
		if((s1.length<2) && (s_1.length<2))
			return s5;
		if(s1.length>1) {
			String[] s2 = s1[1].split(",");
			//log.info("s2.length: "+s2.length);		
			for(String s : s2) {
				//log.info("s: "+s);		
				String s4=s.replaceAll("[\\[\\]\\']", "");
				//log.info("sunat-s4: "+s4);
				s5.add(s4);
			}
		}
		if(s_1.length>1) {			
			String[] s_2 = s_1[1].split(" ");
			//log.info("s_2.length: "+s_2.length);	
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

	private List<String> getComprobantesNCNDSUNAT(String s0) {
		//log.info("sunat-s0: "+s0);
		List<String> s5 = new ArrayList<String>();
		if((s0==null) || (s0.isEmpty()))
			return s5;
		String separador = Pattern.quote("El comprobante");
		String[] s1 = s0.split(separador);
		//log.info("sunat-s1.length: "+s1.length);
		if(s1.length<2)
			return s5;
		String[] s2 = s1[1].trim().split(" ");
		//log.info("sunat-s2.length: "+s2.length);		
		for(String s : s2) {
			//log.info("sunat-s: "+s);		
			String s4=s.replaceAll("[\\[\\]\\']", "");
			//log.info("sunat-s4: "+s4);
			s5.add(s4);
		}
		return s5;
	}	
	
	private int getCountItemsError(String revisarItemsError) {
		int revisarItems = 0;
		if((revisarItemsError!=null) && (!revisarItemsError.isEmpty())) {
			String[] revisar = revisarItemsError.split(";");
			revisarItems = revisar.length;
		}
		return revisarItems;
	}
	
	public void buscarComprobanteSunatList(Documento tbComprobante) {
		try {  		
			String sNumRUC = String.valueOf(tbComprobante.getRucEmisor());
			Long iNumCor = 0l;
			if(StringUtil.hasString(tbComprobante.getNumeroCorrelativo()))
				iNumCor = Long.valueOf(tbComprobante.getNumeroCorrelativo());
			//String sNumCor = String.valueOf(iNumCor);
      		ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
      		List<ComprobantesPagoElectronicos> results = 
      				tsComprobantesPagoElectronicosDAOLocal.buscarListGetSunatList(sNumRUC, tbComprobante.getTipoDocumento(), tbComprobante.getSerie(), iNumCor);
      		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results) {
					if(cp.getIndEstadoCpe() == Constantes.SUNAT_IndEstCpe_Acept) {
						tbComprobante.setEstado(Constantes.SUNAT_CDR_AUTORIZADO);
						tbComprobante.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_0);
						return;
					}
					if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_FACTURA) ||
							tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_BOLETA) ||
							tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO) ||
							tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_NOTA_DEBITO) ||
							tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_GUIA_REMISION_REMITENTE)||
							tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA)) {
						if((cp.getIndEstadoCpe() == Constantes.SUNAT_IndEstCpe_Recha) ||
								(cp.getIndEstadoCpe() == Constantes.SUNAT_IndEstCpe_Anula)) {
							tbComprobante.setEstado(Constantes.SUNAT_CDR_AUTORIZADO);
							tbComprobante.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_0);
						}
					}
	    		}								
			}			
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarComprobanteSunatList Exception \n"+errors);
		}
	}
}
