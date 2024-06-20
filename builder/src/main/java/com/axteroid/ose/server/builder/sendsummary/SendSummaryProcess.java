package com.axteroid.ose.server.builder.sendsummary;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.builder.content.ContentValidateSend;
import com.axteroid.ose.server.builder.content.ReturnResponse;
import com.axteroid.ose.server.builder.content.impl.ContentValidateSendImpl;
import com.axteroid.ose.server.builder.content.impl.ParseDocument;
import com.axteroid.ose.server.builder.content.impl.ReturnResponseImpl;
//import com.axteroid.ose.server.builder.sendsunat.SunatDocumentSend;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesejb.rules.OseDBCRUDLocal;
import com.axteroid.ose.server.rulesejb.rules.UBLListParametroLocal;
import com.axteroid.ose.server.rulesejb.rules.UBLListValidateLocal;
import com.axteroid.ose.server.rulesejb.rules.impl.OseDBCRUDImpl;
import com.axteroid.ose.server.rulesejb.rules.impl.UBLListParametroImpl;
import com.axteroid.ose.server.rulesejb.rules.impl.UBLListValidateImpl;
import com.axteroid.ose.server.rulesubl.builder.CdrBuild;
import com.axteroid.ose.server.rulesubl.builder.ParseXML2Document;
import com.axteroid.ose.server.rulesubl.builder.UblRulesValidate;
import com.axteroid.ose.server.rulesubl.builder.impl.CdrBuildImpl;
import com.axteroid.ose.server.rulesubl.builder.impl.ParseXml2DocumentImpl;
import com.axteroid.ose.server.rulesubl.builder.impl.UblRulesValidateImpl;
import com.axteroid.ose.server.rulesubl.dozer.ParseDocument2CDRDozer;
import com.axteroid.ose.server.rulesubl.dozer.impl.ParseDocument2CDRDozerImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.DocumentUtil;

public class SendSummaryProcess {	
	private static final Logger log = LoggerFactory.getLogger(SendSummaryProcess.class);
	private ContentValidateSend contentValidateSend = new ContentValidateSendImpl();
	private ReturnResponse returnResponse = new ReturnResponseImpl();
	private Map<String, Timestamp> mapTime = new HashMap<String, Timestamp>();
	
	public String procesarComprobante(Documento documento){		
		log.info("procesarComprobante {}: "+documento.toString());
		mapTime.put("Start", new Timestamp(System.currentTimeMillis()));

		if(documento.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0)))
			return returnResponse.buildByteReturnTicket(documento);
		documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));			
		documento.setFecIniProc(DateUtil.getCurrentTimestamp());

		if(documento.getTipoDocumento().equals(Constantes.SUNAT_REVERSION)) 
			this.buildCDR_SummaryReversion(documento);	
		else 
			this.buildCDR_SummaryResumen(documento);
		//log.info("2 documento.getUserCrea(): "+documento.getUserCrea());
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
				documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))) {	
			if(documento.getErrorNumero().equals(Constantes.SUNAT_ERROR_0138)
					|| documento.getErrorNumero().equals(Constantes.SUNAT_ERROR_0402)) {
				OseDBCRUDLocal oseDBCRUDLocal = new OseDBCRUDImpl();
				Documento tbc = oseDBCRUDLocal.buscarTbComprobanteXIDComprobante(documento);				
				if(tbc!=null) {
					String rptaFinal = returnResponse.buildByteReturn(tbc);	
					return rptaFinal;
				}
			}			
			String respuesta =  returnResponse.buildByteReturnErrorComprobante(documento, contentValidateSend);
			return respuesta;
		}
		String bbrec = returnResponse.buildByteReturnErrorComprobante(documento, contentValidateSend);
		if(!documento.getErrorComprobante().equals(Constantes.CONTENT_PROCESO.charAt(0))){		
			return bbrec;				
		}
		documento.setErrorComprobante(Constantes.CONTENT_TRUE.charAt(0));
		returnResponse.updateComprobante(documento, contentValidateSend);		
		mapTime.put("RecordDocument", new Timestamp(System.currentTimeMillis()));
		String rptaFinal = returnResponse.buildByteReturnTicket(documento);	
		
		if((documento.getErrorComprobante()!=null && documento.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0))) && 
				(documento.getEstado()!= null  && documento.getEstado().equals(Constantes.SUNAT_CDR_GENERADO))) {
			//SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
			//sunatDocumentSend.getSendSunat(documento);
		}		
							
		mapTime.put("End", new Timestamp(System.currentTimeMillis()));			
		DocumentUtil.getStatistics(mapTime);
		return rptaFinal;
	}
	
	private void buildCDR_SummaryResumen(Documento documento) {	
		log.info("buildCDR_SummaryResumen - RA - RC ");
		mapTime.put("Security", new Timestamp(System.currentTimeMillis()));
		OseDBCRUDLocal oseDBCRUDLocal = new OseDBCRUDImpl();
		oseDBCRUDLocal.getPadronAutorizadoIgv(documento);
		
		UblRulesValidate ublRulesValidate = new UblRulesValidateImpl();	
		ublRulesValidate.service(documento);	
		ParseDocument.parseUBLResumen(documento);
				
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}
		mapTime.put("RulesUbl", new Timestamp(System.currentTimeMillis()));
		
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();		
		EResumenDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryResumen(documento);
		ParseDocument2CDRDozer parseDocument2DataCDR = new ParseDocument2CDRDozerImpl();
		parseDocument2DataCDR.document2TbComprobante_SummaryResumen(documento, eDocumento);
		this.getSignedEResumenDocumento(documento, eDocumento);
		mapTime.put("GetDatUbl", new Timestamp(System.currentTimeMillis()));
		
		if(documento.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO)) {
			//UBLParameters ublParameters = new UBLParametersImpl();
			UBLListParametroLocal ublListParametroLocal = new UBLListParametroImpl();
			ublListParametroLocal.reglasValidarParametrosResumenDiario(documento, eDocumento);
			
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
				contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
				return;
			}
		}
		mapTime.put("RulesParameters", new Timestamp(System.currentTimeMillis()));
		
		UBLListValidateLocal ublListValidateLocal = new UBLListValidateImpl();
		if(documento.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS)) {
			ublListValidateLocal.reglasValidarDatos_SummaryComunicaBajas(documento, eDocumento); 	
			ParseDocument.parseUBLResumenDatos(documento);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))) {
				contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
				return;
			}
		}
				
		if(documento.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO)) {
			ublListValidateLocal.reglasValidarDatos_SummaryResumenDiario(documento, eDocumento); 
			ParseDocument.parseUBLResumenDatos(documento);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))) {
				contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
				return;
			}
		}
		mapTime.put("RulesList", new Timestamp(System.currentTimeMillis()));
		
		if(documento.getObservaNumero()!=null && !documento.getObservaNumero().isEmpty())
			contentValidateSend.buscarObservaciones(documento);	
		CdrBuild cdrDocumentBuild = new CdrBuildImpl();				
		cdrDocumentBuild.generarEDocumentCDR_SummaryResumen(documento, eDocumento);
		mapTime.put("BuildCDR", new Timestamp(System.currentTimeMillis()));
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}else
			returnResponse.updateTbComprobante_Send(documento);
		mapTime.put("RecordCDR", new Timestamp(System.currentTimeMillis()));	
				
		ublListValidateLocal.modificarEstadoComprobantesPagoElectronicos_RA(documento, eDocumento);		
		ublListValidateLocal.grabaTbComprobantesPagoElectronicos_RC(documento, eDocumento);		;		
	}
	
	private void buildCDR_SummaryReversion(Documento documento) {
		log.info("buildCDR_SummaryReversion RR ");
		mapTime.put("Security", new Timestamp(System.currentTimeMillis()));
		OseDBCRUDLocal oseDBCRUDLocal = new OseDBCRUDImpl();
		oseDBCRUDLocal.getPadronAutorizadoIgv(documento);
		
		UblRulesValidate ublRulesValidate = new UblRulesValidateImpl();	
		ublRulesValidate.service(documento);
		ParseDocument.parseUBLResumen(documento);		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}
		mapTime.put("RulesUbl", new Timestamp(System.currentTimeMillis()));
		
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		EReversionDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryReversion(documento);				
		ParseDocument2CDRDozer parseDocument2DataCDR = new ParseDocument2CDRDozerImpl();
		parseDocument2DataCDR.document2TbComprobante_SummaryReversion(documento, eDocumento);
		this.getSignedEReversionDocumento(documento, eDocumento);
		mapTime.put("GetDatUbl", new Timestamp(System.currentTimeMillis()));
		
		mapTime.put("RulesParameters", new Timestamp(System.currentTimeMillis()));
		
		UBLListValidateLocal ublListValidateLocal = new UBLListValidateImpl();
		ublListValidateLocal.reglasValidarDatos_SummaryReversion(documento, eDocumento); 	
		ParseDocument.parseUBLResumenDatos(documento);
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
				documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}		
		mapTime.put("RulesList", new Timestamp(System.currentTimeMillis()));
		
		if(documento.getObservaNumero()!=null && !documento.getObservaNumero().isEmpty())
			contentValidateSend.buscarObservaciones(documento);			
		CdrBuild cdrDocumentBuild = new CdrBuildImpl();				
		cdrDocumentBuild.generarEDocumentCDR_SummaryReversion(documento, eDocumento);
		mapTime.put("BuildCDR", new Timestamp(System.currentTimeMillis()));
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}else
			returnResponse.updateTbComprobante_Send(documento);
		mapTime.put("RecordCDR", new Timestamp(System.currentTimeMillis()));	
				
		ublListValidateLocal.modificarEstadoComprobantesPagoElectronicos_RR(documento, eDocumento);		
	}
	
	private void getSignedEResumenDocumento(Documento documento, EResumenDocumento eDocumento) {
		//log.info(" documento.getErrorLogSunat() = "+documento.getErrorLogSunat());
		try {
			if(documento.getErrorLogSunat()!=null) {		
				String[] val = documento.getErrorLogSunat().split(Constantes.OSE_SPLIT);
				if(val[0]!=null) 
					eDocumento.setNumIdCd(val[0]);
				if(val[1]!=null) 
					eDocumento.setHashCode(val[1]);
				if(val[2]!=null) 
					eDocumento.setDategetNotBefore(DateUtil.parseDate(val[2], "yyyy-MM-dd HH:mm:ss"));		
				if(val[3]!=null) 
					eDocumento.setDateNotAfter(DateUtil.parseDate(val[3], "yyyy-MM-dd HH:mm:ss"));
			}
			documento.setErrorLogSunat("");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getSignedEResumenDocumento Exception \n"+errors);
		}
	}
	
	private void getSignedEReversionDocumento(Documento documento, EReversionDocumento eDocumento) {		
		//log.info(" documento.getErrorLogSunat() = "+documento.getErrorLogSunat());
		try {			
			if(documento.getErrorLogSunat()!=null) {		
				String[] val = documento.getErrorLogSunat().split(Constantes.OSE_SPLIT);
				if(val[0]!=null) 
					eDocumento.setNumIdCd(val[0]);
				if(val[1]!=null) 
					eDocumento.setHashCode(val[1]);
				if(val[2]!=null) 
					eDocumento.setDategetNotBefore(DateUtil.parseDate(val[2], "yyyy-MM-dd HH:mm:ss"));		
				if(val[3]!=null) 
					eDocumento.setDateNotAfter(DateUtil.parseDate(val[3], "yyyy-MM-dd HH:mm:ss"));
			}
			documento.setErrorLogSunat("");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getSignedEReversionDocumento Exception \n"+errors);
		}
	}
		
}
