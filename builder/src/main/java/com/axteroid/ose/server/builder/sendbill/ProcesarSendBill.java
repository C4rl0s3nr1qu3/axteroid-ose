package com.axteroid.ose.server.builder.sendbill;

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
import com.axteroid.ose.server.tools.constantes.TipoJBossOsePropertiesEnum;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.axteroid.ose.server.tools.util.DocumentUtil;
import com.axteroid.ose.server.tools.util.StringUtil;

public class ProcesarSendBill {
	private static final Logger log = LoggerFactory.getLogger(ProcesarSendBill.class);
	private ContentValidateSend contentValidateSend = new ContentValidateSendImpl();
	private ReturnResponse returnResponse = new ReturnResponseImpl();
	private Map<String, Timestamp> mapTime = new HashMap<String, Timestamp>();
	
	public String procesarComprobante(String fileName, byte[] contentFile, String user) {		
		log.info("procesarComprobante: {} | {}",user,fileName);
		mapTime.put("Start", new Timestamp(System.currentTimeMillis()));
		Documento documento = new Documento();
		documento.setNombre(fileName);
		documento.setUbl(contentFile);
		documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));		
		documento.setFecRecepcion(DateUtil.getCurrentTimestamp());
		documento.setUserCrea(user);
//		log.info("M-1) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);

		if(!(user !=null) || (user.isEmpty())) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_0106);
			String sError = documento.getErrorNumero()+Constantes.OSE_SPLIT+documento.getErrorDescripcion();		
			return sError;
		}
//		log.info("M-2) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
		
		contentValidateSend.validarContentSendBill(documento);
//		log.info("M-3) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);

		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return  returnResponse.buildByteReturnError(documento, contentValidateSend);
				
//		log.info("M-5) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
		
		if(documento.getNumeroCorrelativo().length()>8) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_1001);
		}
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return  returnResponse.buildByteReturnError(documento, contentValidateSend);
			
		
		returnResponse.getEstadobyRUC(documento);
		if(!documento.getErrorComprobante().equals(Constantes.CONTENT_PROCESO.charAt(0))) 
			return  returnResponse.buildByteReturnError(documento, contentValidateSend);			
//		log.info("M-6) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);

		String rucEmpresa = returnResponse.getRUCbyUser(documento, user);	
		//String rucEmpresa = String.valueOf(documento.getRucEmisor());	
		if(!documento.getErrorComprobante().equals(Constantes.CONTENT_PROCESO.charAt(0))) 
			return  returnResponse.buildByteReturnError(documento, contentValidateSend);
		
//		log.info("M-7) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
		
		documento.setEstado(Constantes.SUNAT_CDR_EN_PROCESO);
		documento.setRucPseEmisor(Long.parseLong(rucEmpresa));
		documento.setFecIniProc(DateUtil.getCurrentTimestamp());
		returnResponse.grabarDocumento(documento);
				
//		log.info("M-8) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);

		boolean esArchivoString = true;
		if(!documento.getErrorComprobante().equals(Constantes.CONTENT_PROCESO.charAt(0))){		
			log.info("M-9) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
					documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
			if(StringUtil.hasString(documento.getSerie())) {
//				log.info("M-10) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//						documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
				if((documento.getTipoDocumento().equals(Constantes.SUNAT_FACTURA)) || 
					(documento.getTipoDocumento().equals(Constantes.SUNAT_BOLETA)) ||
					(documento.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO)) ||
					(documento.getTipoDocumento().equals(Constantes.SUNAT_NOTA_DEBITO)) ||
					(documento.getTipoDocumento().equals(Constantes.SUNAT_RETENCION)) ||
					(documento.getTipoDocumento().equals(Constantes.SUNAT_PERCEPCION))){
					documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));
					returnResponse.updateTbComprobante(documento);					
					esArchivoString = false;
				}
			}
			if(esArchivoString) 
				return  returnResponse.buildByteReturn(documento, contentValidateSend);
		}
		
//		log.info("P-1) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
		
		if(documento.getTipoDocumento().equals(Constantes.SUNAT_RETENCION)) 
			this.buildCDR_Retencion(documento);	
		else if(documento.getTipoDocumento().equals(Constantes.SUNAT_PERCEPCION)) 
			this.buildCDR_Percepcion(documento);	
		else if((documento.getTipoDocumento().equals(Constantes.SUNAT_GUIA_REMISION_REMITENTE)) ||
				(documento.getTipoDocumento().equals(Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA)))
			this.buildCDR_Guia(documento);	
		else 
			this.buildCDR(documento);
		
//		log.info("P-2) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
				documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))) {
//			log.info("P-3) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//					documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
			
			if(documento.getErrorNumero().equals(Constantes.SUNAT_ERROR_1033)
					|| documento.getErrorNumero().equals(Constantes.SUNAT_ERROR_0138)
					|| documento.getErrorNumero().equals(Constantes.SUNAT_ERROR_0402)) {
//				log.info("P-4) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//						documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);

				OseDBCRUDLocal oseDBCRUDLocal = new OseDBCRUDImpl();
				Documento tbc = oseDBCRUDLocal.buscarTbComprobanteXIDComprobante(documento);	
				if(tbc!=null) {
//					log.info("P-5) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//							documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
					String rptaFinal = returnResponse.buildByteReturn(tbc);	
					return rptaFinal;
				}
//				log.info("P-6) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//						documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
			}		
//			log.info("P-7) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//					documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);

			if(!esArchivoString){	
//				log.info("P-8) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//						documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
				String respuesta = returnResponse.buildByteReturnErrorArchivoString(documento, contentValidateSend);
				return respuesta;
			}
//			log.info("P-9) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//					documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);

			String respuesta = returnResponse.buildByteReturnErrorComprobante(documento, contentValidateSend);
			return respuesta;			
		}	
//		log.info("P-10) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);

		String bbrec = returnResponse.buildByteReturnErrorComprobante(documento, contentValidateSend);
		if(!documento.getErrorComprobante().equals(Constantes.CONTENT_PROCESO.charAt(0))){
			return bbrec;					
		}
//		log.info("P-11) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);

		documento.setErrorComprobante(Constantes.CONTENT_TRUE.charAt(0));
		returnResponse.updateComprobante(documento, contentValidateSend);
//		log.info("P-12) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
		
		mapTime.put("RecordDocument", new Timestamp(System.currentTimeMillis()));        
		
		if((documento.getErrorComprobante()!=null && documento.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0))) && 
				(documento.getEstado()!= null  && documento.getEstado().equals(Constantes.SUNAT_CDR_GENERADO))) {
			//SunatDocumentSend sunatDocumentSend = new SunatDocumentSend();
			//sunatDocumentSend.getSendSunat(documento);
		}
		
		String rptaFinal = returnResponse.buildByteReturn(documento);				
		mapTime.put("End", new Timestamp(System.currentTimeMillis()));
		DocumentUtil.getStatistics(mapTime);		
		return rptaFinal;		
	}

	public void buildCDR(Documento documento) {		
		log.info("buildCDR");
		mapTime.put("Security", new Timestamp(System.currentTimeMillis()));		
		OseDBCRUDLocal oseDBCRUDLocal = new OseDBCRUDImpl();
		oseDBCRUDLocal.getPadronAutorizadoIgv(documento);
		
		UblRulesValidate ublRulesValidate = new UblRulesValidateImpl();	
		ublRulesValidate.service(documento);
//		log.info("1) documento: "+documento.toString()+" | "+ documento.getErrorComprobante()
//			+" UBL: "+documento.getUbl().length);		
		ParseDocument.parseUBL(documento);
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {			
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}
		mapTime.put("RulesUbl", new Timestamp(System.currentTimeMillis()));
		
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();		
		EDocumento eDocumento = parseXML2Document.parseUBLSendBill(documento);	
//		log.info("2) documento: "+documento.toString()+" | "+ documento.getErrorComprobante()
//			+" UBL: "+new String(documento.getUbl()));
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}			
		ParseDocument2CDRDozer parseDocument2DataCDR = new ParseDocument2CDRDozerImpl();
		parseDocument2DataCDR.document2TbComprobante(documento, eDocumento);		
		this.getSignedEDocumento(documento, eDocumento);	
//		log.info("3a) documento: "+documento.getRucEmisor()+" - "+ documento.getTipoComprobante()+" - "+
//				documento.getSerie()+" - "+documento.getNumeroCorrelativo());
//		log.info("3b) eDocumento: "+eDocumento.getNumeroDocumentoEmisor()+" - "+ eDocumento.getTipoDocumento()+" - "+
//				eDocumento.getIdDocumento());
		
		mapTime.put("GetDatUbl", new Timestamp(System.currentTimeMillis()));
		
		UBLListParametroLocal ublListParametroLocal = new UBLListParametroImpl();
		ublListParametroLocal.reglasValidarParametrosComprobante(documento, eDocumento);
//		log.info("4) documento: "+documento.getRucEmisor()+" - "+ documento.getTipoComprobante()+" - "+
//				documento.getSerie()+" - "+documento.getNumeroCorrelativo());
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}
		mapTime.put("RulesParameters", new Timestamp(System.currentTimeMillis()));		
		
		UBLListValidateLocal ublListValidateLocal = new UBLListValidateImpl();
		ublListValidateLocal.reglasValidarDatosComprobante(documento, eDocumento); 	
//		log.info("5) documento: "+documento.getRucEmisor()+" - "+ documento.getTipoComprobante()+" - "+
//				documento.getSerie()+" - "+documento.getNumeroCorrelativo()+" | "+documento.getErrorComprobante());
		ParseDocument.parseUBLDatos(documento);
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
				documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}
		mapTime.put("RulesList", new Timestamp(System.currentTimeMillis()));
				
		if(documento.getObservaNumero()!=null && !documento.getObservaNumero().isEmpty())
			contentValidateSend.buscarObservaciones(documento);	
		
		CdrBuild cdrDocumentBuild = new CdrBuildImpl();
		cdrDocumentBuild.generarEDocumentCDR(documento, eDocumento);
//		log.info("6) documento: "+documento.getRucEmisor()+" - "+ documento.getTipoComprobante()+" - "+
//				documento.getSerie()+" - "+documento.getNumeroCorrelativo()+" | "+documento.getErrorComprobante());
				
		mapTime.put("BuildCDR", new Timestamp(System.currentTimeMillis()));	
		//log.info("INICIO RecordCDR");
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}else 
			returnResponse.updateTbComprobante_Send(documento);	

		//log.info("FIN RecordCDR");
		mapTime.put("RecordCDR", new Timestamp(System.currentTimeMillis()));				
				
		ublListValidateLocal.grabaTbComprobantesPagoElectronicosFBNCND(documento, eDocumento);
//		log.info("7) documento: "+documento.getRucEmisor()+" - "+ documento.getTipoComprobante()+" - "+
//				documento.getSerie()+" - "+documento.getNumeroCorrelativo()+" | "+documento.getErrorComprobante());
	}
	
	public void buildCDR_Retencion(Documento documento) {
		log.info("buildCDR_Retencion");
		mapTime.put("Security", new Timestamp(System.currentTimeMillis()));
		OseDBCRUDLocal oseDBCRUDLocal = new OseDBCRUDImpl();
		oseDBCRUDLocal.getPadronAutorizadoIgv(documento);
		
		UblRulesValidate ublRulesValidate = new UblRulesValidateImpl();	
		ublRulesValidate.service(documento);			
		ParseDocument.parseUBL(documento);
				
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}
		mapTime.put("RulesUbl", new Timestamp(System.currentTimeMillis()));
		
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();		
		ERetencionDocumento eDocumento = parseXML2Document.parseUBLSendBill_Retencion(documento);				
		ParseDocument2CDRDozer parseDocument2DataCDR = new ParseDocument2CDRDozerImpl();
		parseDocument2DataCDR.document2TbComprobante_Retencion(documento, eDocumento);
		this.getSignedERetencionDocumento(documento, eDocumento);
		mapTime.put("GetDatUbl", new Timestamp(System.currentTimeMillis()));
		
		UBLListParametroLocal ublListParametroLocal = new UBLListParametroImpl();
		ublListParametroLocal.reglasValidarParametrosRetencion(documento, eDocumento);
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}		
		mapTime.put("RulesParameters", new Timestamp(System.currentTimeMillis()));
		
		UBLListValidateLocal ublListValidateLocal = new UBLListValidateImpl();
		ublListValidateLocal.reglasValidarDatosComprobante_Retencion(documento, eDocumento); 
		ParseDocument.parseUBLDatos(documento);
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
				documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}
		mapTime.put("RulesList", new Timestamp(System.currentTimeMillis()));
		
		if(documento.getObservaNumero()!=null && !documento.getObservaNumero().isEmpty())
			contentValidateSend.buscarObservaciones(documento);				
		CdrBuild cdrDocumentBuild = new CdrBuildImpl();				
		cdrDocumentBuild.generarEDocumentCDR_Retencion(documento, eDocumento);
		mapTime.put("BuildCDR", new Timestamp(System.currentTimeMillis()));
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}else
			returnResponse.updateTbComprobante_Send(documento);
		mapTime.put("RecordCDR", new Timestamp(System.currentTimeMillis()));	
						
		ublListValidateLocal.grabaTbComprobantesPagoElectronicosRetencion(documento, eDocumento);
	}

	public void buildCDR_Percepcion(Documento documento) {
		log.info("buildCDR_Percepcion");
		mapTime.put("Security", new Timestamp(System.currentTimeMillis()));
		OseDBCRUDLocal oseDBCRUDLocal = new OseDBCRUDImpl();
		oseDBCRUDLocal.getPadronAutorizadoIgv(documento);
		
		UblRulesValidate ublRulesValidate = new UblRulesValidateImpl();	
		ublRulesValidate.service(documento);	
		ParseDocument.parseUBL(documento);
				
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}
		mapTime.put("RulesUbl", new Timestamp(System.currentTimeMillis()));
		
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();		
		EPercepcionDocumento eDocumento = parseXML2Document.parseUBLSendBill_Percepcion(documento);		
		ParseDocument2CDRDozer parseDocument2DataCDR = new ParseDocument2CDRDozerImpl();
		parseDocument2DataCDR.document2TbComprobante_Percepcion(documento, eDocumento);
		this.getSignedPercepcionEDocumento(documento, eDocumento);
		mapTime.put("GetDatUbl", new Timestamp(System.currentTimeMillis()));
		
		UBLListParametroLocal ublListParametroLocal = new UBLListParametroImpl();
		ublListParametroLocal.reglasValidarParametrosPercepcion(documento, eDocumento);
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}
		mapTime.put("RulesParameters", new Timestamp(System.currentTimeMillis()));
		
		UBLListValidateLocal ublListValidateLocal = new UBLListValidateImpl();
		ublListValidateLocal.reglasValidarDatosComprobante_Percepcion(documento, eDocumento); 
		ParseDocument.parseUBLDatos(documento);
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
				documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}
		mapTime.put("RulesList", new Timestamp(System.currentTimeMillis()));
		
		if(documento.getObservaNumero()!=null && !documento.getObservaNumero().isEmpty())
			contentValidateSend.buscarObservaciones(documento);
		CdrBuild cdrDocumentBuild = new CdrBuildImpl();				
		cdrDocumentBuild.generarEDocumentCDR_Percepcion(documento, eDocumento);
		mapTime.put("BuildCDR", new Timestamp(System.currentTimeMillis()));
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}else
			returnResponse.updateTbComprobante_Send(documento);
		mapTime.put("RecordCDR", new Timestamp(System.currentTimeMillis()));	
				
		ublListValidateLocal.grabaTbComprobantesPagoElectronicosPercepcion(documento, eDocumento);
	}
	
	public void buildCDR_Guia(Documento documento) {		
		String envioComprobante = DirUtil.getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_RES_ENVIO_COMPROBANTE.getCodigo());
		log.info("buildCDR_Guia");
		mapTime.put("Security", new Timestamp(System.currentTimeMillis()));
		ParseDocument.parseUBL(documento);
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}
		
		if(envioComprobante.trim().equals(Constantes.CONTENT_NO_APLICA)){
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_1085);
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}
		
		UblRulesValidate ublRulesValidate = new UblRulesValidateImpl();	
		ublRulesValidate.service(documento);	
		log.info("Guia-1) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
		
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}
		mapTime.put("RulesUbl", new Timestamp(System.currentTimeMillis()));
				
		EGuiaDocumento eDocumento = parseXML2Document.parseUBLSendBill_Guia(documento);				
//		ParseDocument2CDRDozer parseDocument2DataCDR = new ParseDocument2CDRDozerImpl();
//		parseDocument2DataCDR.document2TbComprobante_Guia(documento, eDocumento);
		log.info("Guia-2) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
		
//		this.getSignedEGuiaDocumento(documento, eDocumento);
		mapTime.put("GetDatUbl", new Timestamp(System.currentTimeMillis()));
		
//		UBLListParametroLocal ublListParametroLocal = new UBLListParametroImpl();
//		ublListParametroLocal.reglasValidarParametrosGuia(documento, eDocumento);
		log.info("Guia-3) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}
		mapTime.put("RulesParameters", new Timestamp(System.currentTimeMillis()));
		
		UBLListValidateLocal ublListValidateLocal = new UBLListValidateImpl();
//		ublListValidateLocal.reglasValidarDatosComprobante_Guia(documento, eDocumento); 	
//		log.info("Guia-4) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
//		
//		if(documento.getErrorComprobante().equals(ConstantesOse.CONTENT_FALSE.charAt(0)) || 
//				documento.getErrorComprobante().equals(ConstantesOse.CONTENT_ERROR_DB.charAt(0))) {
//			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
//			return;
//		}
		mapTime.put("RulesList", new Timestamp(System.currentTimeMillis()));
		
//		if(documento.getObservaNumero()!=null && !documento.getObservaNumero().isEmpty())
//			contentValidateSend.buscarObservaciones(documento);
//		CDRDocumentBuild cdrDocumentBuild = new CDRDocumentBuildImpl();				
//		cdrDocumentBuild.generarEDocumentCDR_Guia(documento, eDocumento);
//		log.info("Guia-5) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
//				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
		
		mapTime.put("BuildCDR", new Timestamp(System.currentTimeMillis()));
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) {
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
			return;
		}else {
			documento.setEstado(Constantes.SUNAT_CDR_GENERADO);
			returnResponse.updateTbComprobante_Send(documento);	
		}
		log.info("Guia-6) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);

		mapTime.put("RecordCDR", new Timestamp(System.currentTimeMillis()));	
				
		ublListValidateLocal.grabaTbComprobantesPagoElectronicosGuia(documento, eDocumento);
		log.info("Guia-7) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
				documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);

	}

	private void getSignedEDocumento(Documento documento, EDocumento eDocumento) {
		try {
			//log.info(" documento.getErrorLogSunat() = "+documento.getErrorLogSunat());
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
			log.error("getSignedEDocumento Exception \n"+errors);
		}
	}
	
	private void getSignedERetencionDocumento(Documento documento, ERetencionDocumento eDocumento) {	
		try {
			//log.info(" documento.getErrorLogSunat() = "+documento.getErrorLogSunat());
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
			log.error("getSignedERetencionDocumento Exception \n"+errors);
		}
	}
	
	private void getSignedPercepcionEDocumento(Documento documento, EPercepcionDocumento eDocumento) {			
		try {
			//log.info(" documento.getErrorLogSunat() = "+documento.getErrorLogSunat());
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
			log.error("getSignedPercepcionEDocumento Exception \n"+errors);
		}
	}
	
	private void getSignedEGuiaDocumento(Documento documento, EGuiaDocumento eDocumento) {
		try {
			//log.info(" documento.getErrorLogSunat() = "+documento.getErrorLogSunat());
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
			log.error("getSignedEGuiaDocumento Exception \n"+errors);
		}
	}

	public Map<String, Timestamp> getMapTime() {
		return mapTime;
	}

	public void setMapTime(Map<String, Timestamp> mapTime) {
		this.mapTime = mapTime;
	}

}
