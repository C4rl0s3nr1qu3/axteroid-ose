package com.axteroid.ose.server.builder.content.impl;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.builder.content.ContentValidateSend;
import com.axteroid.ose.server.builder.content.ReturnResponse;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatAcuseReciboSunat;
import com.axteroid.ose.server.jpa.model.SunatContribuyenteAsociadoEmisor;
import com.axteroid.ose.server.rulesejb.rules.OseDBCRUDLocal;
import com.axteroid.ose.server.rulesejb.rules.UBLListParametroLocal;
import com.axteroid.ose.server.rulesejb.rules.impl.UBLListParametroImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.FileTypeEnum;
import com.axteroid.ose.server.tools.constantes.TipoParametroEnum;

public class ReturnResponseImpl implements ReturnResponse{
	private static final Logger log = LoggerFactory.getLogger(ReturnResponseImpl.class);
	
	public String buildByteReturnErrorComprobante(Documento documento,
			ContentValidateSend contentValidateSend){
		log.info("buildByteReturnErrorComprobante "+documento.getNombre()+" - documento.getErrorComprobante(): "+documento.getErrorComprobante());
		try {
			OseDBCRUDLocal crudOseDBLocal = 
				(OseDBCRUDLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"OseDBCRUDImpl"+Constantes.OSE_CDEJB_RULES+"OseDBCRUDLocal");
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))) {
				crudOseDBLocal.grabarDocumento(documento);
			}
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
		}

		if(documento.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0)))
			contentValidateSend.buscarErrorComprobante(documento, Constantes.SUNAT_ERROR_0402);
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_REPETIDO.charAt(0)))
			contentValidateSend.buscarErrorComprobante(documento, Constantes.SUNAT_ERROR_0402);
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))			
			contentValidateSend.buscarErrorComprobante(documento, Constantes.SUNAT_ERROR_0402);
		String sError = documento.getErrorNumero()+Constantes.OSE_SPLIT+documento.getErrorDescripcion();
		if(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_COMUNICACION_BAJAS) ||
				documento.getTipoDocumento().trim().equals(Constantes.SUNAT_RESUMEN_DIARIO) ||
				documento.getTipoDocumento().trim().equals(Constantes.SUNAT_REVERSION))
			sError = sError+Constantes.OSE_SPLIT_3+" ["+documento.getErrorLog()+"]";
		return sError;
	}
	
	public String buildByteReturnErrorArchivoString(Documento documento, ContentValidateSend contentValidateSend) {		
		String sError = documento.getErrorNumero()+Constantes.OSE_SPLIT+documento.getErrorDescripcion();
		return sError;
	}		
	
	public String buildByteReturn(Documento documento){	
		String sCDR = "";
		if ((documento.getTipoDocumento().equals(Constantes.SUNAT_GUIA_REMISION_REMITENTE)) ||
				(documento.getTipoDocumento().equals(Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA))) {			
			if((documento.getRespuestaSunat() != null) && 
					(documento.getRespuestaSunat().equals(Constantes.CONTENT_TRUE)))
				sCDR = new String(documento.getCdr());
			//else if(documento.getRespuestaSunat().equals(Constantes.SUNAT_RESPUESTA_99)) {
			//	sCDR = new String(documento.getCdr());
			//}
		}else {		
			sCDR = new String(documento.getCdr());
		}
		return sCDR;
	}
	
	public String buildByteReturnTicket(Documento documento){	
		String sCDR = String.valueOf(documento.getId());	
		return sCDR;
	}
	
	public void grabarDocumento(Documento documento) {
		//log.info("grabarComprobante(3) "+documento.getNombre());
		try {
			OseDBCRUDLocal oseDBCRUDLocal = 
				(OseDBCRUDLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"OseDBCRUDImpl"+Constantes.OSE_CDEJB_RULES+"OseDBCRUDLocal");
			oseDBCRUDLocal.grabarDocumento(documento);
			//updateContent(documento, documento, contentValidateSend);
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
		}
	}
	
	public void updateTbComprobante_Send(Documento documento) {
		//log.info("updateTbComprobante_Send(1) ");
		try {
			OseDBCRUDLocal oseDBCRUDLocal = 
				(OseDBCRUDLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"OseDBCRUDImpl"+Constantes.OSE_CDEJB_RULES+"OseDBCRUDLocal");
			oseDBCRUDLocal.updateTbComprobante_Send(documento);

		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
		}
	}
	
	public void updateComprobante(Documento documento, ContentValidateSend contentValidateSend) {
		//log.info("updateComprobante");
		try {
			OseDBCRUDLocal crudOseDBLocal = 
				(OseDBCRUDLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"OseDBCRUDImpl"+Constantes.OSE_CDEJB_RULES+"OseDBCRUDLocal");
			crudOseDBLocal.updateTbComprobante(documento);
			//updateContent(documento, documento, contentValidateSend);
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
		}
	}
	
	public void updateComprobante_Send(Documento documento) {
		//log.info("updateComprobante_Send ");
		//log.info("documento.getUserCrea(): "+documento.getUserCrea());
		try {
			OseDBCRUDLocal oseDBCRUDLocal = 
				(OseDBCRUDLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"OseDBCRUDImpl"+Constantes.OSE_CDEJB_RULES+"OseDBCRUDLocal");
			oseDBCRUDLocal.updateTbComprobante_Send(documento);
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
		}
	}
		
	public String getRUCbyUser(Documento documento, String user) {
		try {
			if(documento.getRucEmisor() == 20100144680L)
				return "20298058490";	
			if(documento.getRucEmisor() == 20255950925L)
				return "20298058490";				
			UBLListParametroLocal ublListParametroLocal = new UBLListParametroImpl();
			String ruc = ublListParametroLocal.bucarParametro(TipoParametroEnum.RUC.getCodigo(), user);
			//log.info("getRUCbyUser - RUC: {} - {} | User: {} - {} ", documento.getRucEmisor(),ruc,documento.getUserCrea(),user);
			if(ruc != null && ruc.length()>0){
				Long rucPSE = Long.valueOf(ruc);
				if(rucPSE!=documento.getRucEmisor()) {
					OseDBCRUDLocal oseDBCRUDLocal = 
							(OseDBCRUDLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"OseDBCRUDImpl"+Constantes.OSE_CDEJB_RULES+"OseDBCRUDLocal");
					List<SunatContribuyenteAsociadoEmisor> results =
							oseDBCRUDLocal.getJoinPSEEmisor(documento,Constantes.SUNAT_IndTipAsoc_PSE,rucPSE);					
		    		if(results != null && results.size()>0)
		    			return ruc;
		    		else {
		    			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
		    			documento.setErrorNumero(Constantes.SUNAT_ERROR_0154);
		    		}
				}				
				return ruc;
			}							
    		documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0109);	
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
		}
		return null;
	}
	
	public String getEstadobyRUC(Documento documento) {
		try {
			UBLListParametroLocal ublListParametroLocal = new UBLListParametroImpl();
			String user = String.valueOf(documento.getRucEmisor());
			String estado = ublListParametroLocal.bucarParametro(TipoParametroEnum.EST.getCodigo(), user);
			if(estado != null && estado.equals(Constantes.CONTENT_FALSE)) {
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0105);
				return Constantes.CONTENT_FALSE;
			}
			String desafiliado = ublListParametroLocal.bucarParametro(TipoParametroEnum.AFI.getCodigo(), user);
			//log.info("desafiliado: {} |  UserCrea: {} | user: {}", desafiliado, documento.getUserCrea(), user);
			if(desafiliado != null && desafiliado.trim().equals(documento.getUserCrea().trim())) {
				//log.info("2) desafiliado: {} |  UserCrea: {} | user: {}", desafiliado, documento.getUserCrea(), user);
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0105);
				return Constantes.CONTENT_FALSE;
			}			
			return Constantes.CONTENT_TRUE;	
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
		}
		return null;
	}		
	
	public String getCLIbyUser(Documento documento, String user) {
		try {
			UBLListParametroLocal ublListParametroLocal = new UBLListParametroImpl();
			String ruc = ublListParametroLocal.bucarParametro(TipoParametroEnum.CLI.getCodigo(), user);
			if(ruc != null && ruc.length()>0)
				return ruc;
    		documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0109);	
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
		}
		return null;
	}
		
	public void getCompobantePago(Documento documento){
		//log.info("getCompobantePago");
		try {
			OseDBCRUDLocal crudOseDBLocal = 
				(OseDBCRUDLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"OseDBCRUDImpl"+Constantes.OSE_CDEJB_RULES+"OseDBCRUDLocal");
			crudOseDBLocal.getCompobantePago(documento); 	
			if(documento.getErrorNumero()==null && documento.getErrorNumero().isEmpty())
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0402);
		}catch(Exception e) {
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
		}
	}	
	
	public void grabarTsAcuseReciboSunat(Documento documento)  {
		log.info("grabarTsAcuseReciboSunat ");
		try { 					
			SunatAcuseReciboSunat tsAcuseReciboSunat = new SunatAcuseReciboSunat();
			tsAcuseReciboSunat.setId(documento.getId());
			tsAcuseReciboSunat.setRucEmisor(documento.getRucEmisor());
			tsAcuseReciboSunat.setTipoComprobante(documento.getTipoDocumento());
			tsAcuseReciboSunat.setSerie(documento.getSerie());
			tsAcuseReciboSunat.setNumeroCorrelativo(documento.getNumeroCorrelativo()); 
			tsAcuseReciboSunat.setEstado(documento.getEstado());			
			tsAcuseReciboSunat.setObservaNumero(documento.getObservaNumero());			
			tsAcuseReciboSunat.setObservaDescripcion(documento.getAdvertencia());			
			tsAcuseReciboSunat.setFechaEnvioSunat(documento.getFechaEnvioSunat());			
			tsAcuseReciboSunat.setRespuestaSunat(documento.getRespuestaSunat());
			String RUC = String.valueOf(documento.getRucEmisor());	
			String nombreFile = Constantes.AR_PREFI+Constantes.GUION
					+RUC+Constantes.GUION
					+documento.getTipoDocumento()+Constantes.GUION
					+documento.getSerie()+Constantes.GUION
					+documento.getNumeroCorrelativo();
			String filePath = documento.getTipoDocumento().concat("/")
					.concat(RUC).concat("/")
					.concat(FileTypeEnum.SUNAT.val).concat("/")
					.concat(nombreFile);
			
			tsAcuseReciboSunat.setMensajeSunat(filePath+Constantes.OSE_SPLIT+"AR");			
			tsAcuseReciboSunat.setFechaRespuestaSunat(documento.getFechaRespuestaSunat());
			tsAcuseReciboSunat.setErrorLogSunat(documento.getErrorLogSunat());
			tsAcuseReciboSunat.setUserCrea(documento.getUserModi());
			tsAcuseReciboSunat.setFechaCrea(documento.getFechaModi());
			if((documento.getErrorLog()!= null) && (documento.getErrorLog().length()<45))
				tsAcuseReciboSunat.setTicket(documento.getErrorLog());						
			OseDBCRUDLocal crudOseDBLocal = 
				(OseDBCRUDLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"OseDBCRUDImpl"+Constantes.OSE_CDEJB_RULES+"OseDBCRUDLocal");
			 crudOseDBLocal.grabarTsAcuseReciboSunat(tsAcuseReciboSunat);	
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabarTsAcuseReciboSunat Exception \n"+errors);
		}
	}	
	
	public String buildByteReturn(Documento documento, ContentValidateSend contentValidateSend){
		log.info("buildByteReturn - ErrorComprobante: {} | ErrorNumero: {} | ErrorDescripcion: {}", 
				documento.getErrorComprobante(), documento.getErrorNumero(), documento.getErrorDescripcion());
		try {
//			if(documento.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0))){
//				switch(documento.getTipoDocumento()){			
//					case Constantes.SUNAT_COMUNICACION_BAJAS:
//						contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_2324);
//						break;
//					case Constantes.SUNAT_RESUMEN_DIARIO:
//						contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_2223);
//						break;
//					case Constantes.SUNAT_REVERSION:
//						contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_2223);
//						break;
//					case Constantes.SUNAT_GUIA_REMISION_REMITENTE:
//						contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_4000);
//						break;
//					case Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA:
//						contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_4000);
//						break;
//					case Constantes.SUNAT_RETENCION:
//						contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_1033);
//						break;
//					case Constantes.SUNAT_PERCEPCION:
//						contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_1033);
//						break;
//					default:	
//						contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_1033);
//						break;
//				}				
//			}
			OseDBCRUDLocal oseDBCRUDLocal = 
					(OseDBCRUDLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"OseDBCRUDImpl"+Constantes.OSE_CDEJB_RULES+"OseDBCRUDLocal");
			oseDBCRUDLocal.getEstadoCompobantePagoElectronicos(documento);
			
//			log.info("buildByteReturn - ErrorComprobante: {} | ErrorNumero: {} | ErrorDescripcion: {}", 
//					documento.getErrorComprobante(), documento.getErrorNumero(), documento.getErrorDescripcion());	
			contentValidateSend.buscarError(documento, documento.getErrorNumero());
//			log.info("buildByteReturn - ErrorComprobante: {} | ErrorNumero: {} | ErrorDescripcion: {}", 
//					documento.getErrorComprobante(), documento.getErrorNumero(), documento.getErrorDescripcion());
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_REPETIDO.charAt(0)))
				contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_0402);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))			
				contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_0402);
			log.info("buildByteReturn - ErrorComprobante: {} | ErrorNumero: {} | ErrorDescripcion: {}", 
					documento.getErrorComprobante(), documento.getErrorNumero(), documento.getErrorDescripcion());		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buildByteReturn Exception \n"+errors);
		}
		String sError = documento.getErrorNumero()+Constantes.OSE_SPLIT+documento.getErrorDescripcion();
		return sError;
	}
		
	public String buildByteReturnError(Documento documento, ContentValidateSend contentValidateSend){
		log.info("buildByteReturnError");
//		try {
//			OseDBCRUDLocal crudOseDBLocal = 
//				(OseDBCRUDLocal) InitialContext.doLookup(ConstantesOse.OSE_doLookup+"OseDBCRUDImpl"+ConstantesOse.OSE_CDEJB_RULES+"OseDBCRUDLocal");
//			if(documento.getErrorContent().equals(ConstantesOse.CONTENT_FALSE.charAt(0)))
//				crudOseDBLocal.grabarContent(documento);
//			
//		}catch(Exception e) {
//			documento.setErrorComprobante(ConstantesOse.CONTENT_FALSE.charAt(0));
//			documento.setErrorNumero(ConstantesOse.SUNAT_ERROR_0137);	
//			documento.setErrorLog(e.getMessage());
//		}
		//log.info("buildByteReturnError - ErrorContent: "+documento.getErrorComprobante()+" - ErrorNumero: "+documento.getErrorNumero());
		log.info("buildByteReturnError - ErrorComprobante: {} | ErrorNumero: {} | ErrorDescripcion: {}", 
				documento.getErrorComprobante(), documento.getErrorNumero(), documento.getErrorDescripcion());		

		if(documento.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0))){
			switch(documento.getTipoDocumento()){			
				case Constantes.SUNAT_COMUNICACION_BAJAS:
					String eRA = documento.getRucEmisor()+"-"+documento.getTipoDocumento()+"-"+documento.getNumeroCorrelativo();
					//log.info("eRA: {} ",eRA);
					if(eRA.trim().equals("20298058490-RA-0100"))
						contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_2957);	
					else
						contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_2324);
					break;
				case Constantes.SUNAT_RESUMEN_DIARIO:
					String eRC = documento.getRucEmisor()+"-"+documento.getTipoDocumento()+"-"+documento.getNumeroCorrelativo();
					//log.info("eRC: {} ",eRC);
					if(eRC.trim().equals("20298058490-RC-518"))
						contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_2236);	
					else
						contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_2223);
					break;
				case Constantes.SUNAT_REVERSION:
					contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_2223);
					break;
				case Constantes.SUNAT_GUIA_REMISION_REMITENTE:
					contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_4000);
					break;
				case Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA:
					contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_4000);
					break;
				case Constantes.SUNAT_RETENCION:
					contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_1033);
					break;
				case Constantes.SUNAT_PERCEPCION:
					contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_1033);
					break;
				default:	
					this.getCompobantePago(documento);
					contentValidateSend.buscarError(documento, documento.getErrorNumero());
			}				
		}else
			contentValidateSend.buscarError(documento, documento.getErrorNumero());
			
//		log.info("buildByteReturnError - ErrorComprobante: {} | ErrorNumero: {} | ErrorDescripcion: {}", 
//				documento.getErrorComprobante(), documento.getErrorNumero(), documento.getErrorDescripcion());				
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_REPETIDO.charAt(0)))
			contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_0402);
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))			
			contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_0402);
		log.info("buildByteReturnError - ErrorComprobante: {} | ErrorNumero: {} | ErrorDescripcion: {}", 
				documento.getErrorComprobante(), documento.getErrorNumero(), documento.getErrorDescripcion());		
		
		String sError = documento.getErrorNumero()+Constantes.OSE_SPLIT+documento.getErrorDescripcion();		
		return sError;
	}	
	
	public void updateTbComprobante(Documento documento) {
		//log.info("updateContent_Pack ");
		try {
			OseDBCRUDLocal oseDBCRUDLocal = 
				(OseDBCRUDLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"OseDBCRUDImpl"+Constantes.OSE_CDEJB_RULES+"OseDBCRUDLocal");	
			oseDBCRUDLocal.updateTbComprobante(documento);	
			//oseDBCRUDLocal.buscarTbContentInErrorContent(documento);
			
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
		}
	}	
		
}
