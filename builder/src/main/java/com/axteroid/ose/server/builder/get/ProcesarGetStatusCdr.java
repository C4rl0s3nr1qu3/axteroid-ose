package com.axteroid.ose.server.builder.get;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.builder.content.ContentValidateSend;
import com.axteroid.ose.server.builder.content.ReturnResponse;
import com.axteroid.ose.server.builder.content.impl.ContentValidateSendImpl;
import com.axteroid.ose.server.builder.content.impl.ReturnResponseImpl;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesejb.rules.OseDBCRUDLocal;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.DocumentUtil;

public class ProcesarGetStatusCdr {
	private static final Logger log = LoggerFactory.getLogger(ProcesarGetStatusCdr.class);
	private ContentValidateSend contentValidateSend = new ContentValidateSendImpl();
	private ReturnResponse returnResponse = new ReturnResponseImpl();
	
	public Documento getStatusCdr(Documento documento, String user) {		
		//TbContent tbContent = new TbContent();	
		Map<String, Timestamp> mapTime = new HashMap<String, Timestamp>();
    	mapTime.put("StartGetStatusCDR", new Timestamp(System.currentTimeMillis())); 
    	documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));				
		//contentValidateSend.validarContentSend(tbContent);
		String rucEmpresa = returnResponse.getRUCbyUser(documento, user);
		if(!documento.getErrorComprobante().equals(Constantes.CONTENT_PROCESO.charAt(0))) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			contentValidateSend.buscarError(documento, documento.getErrorNumero());	
			documento.setErrorNumero(documento.getErrorNumero());
			documento.setErrorDescripcion(documento.getErrorDescripcion());
			if(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_COMUNICACION_BAJAS) ||
					documento.getTipoDocumento().trim().equals(Constantes.SUNAT_RESUMEN_DIARIO) ||
					documento.getTipoDocumento().trim().equals(Constantes.SUNAT_REVERSION))
				documento.setErrorDescripcion(documento.getErrorDescripcion()+
						Constantes.OSE_SPLIT_3+" ["+documento.getErrorLog()+"]");
			mapTime.put("EndGetStatusCDR", new Timestamp(System.currentTimeMillis())); 
			DocumentUtil.getStatisticsGetStatusCDR(mapTime);	
			return documento;
		}
		documento.setRucPseEmisor(Long.parseLong(rucEmpresa));		
		documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));
		try {				
			OseDBCRUDLocal crudOseDBLocal = 
					(OseDBCRUDLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"OseDBCRUDImpl"+Constantes.OSE_CDEJB_RULES+"OseDBCRUDLocal");			
			Documento tbc = crudOseDBLocal.buscarTbComprobanteXIDComprobante_GET(documento);				
			if(tbc!=null) {								
				if(!tbc.getUserCrea().equals(user)) {
					//log.info("2) "+tbc.getErrorComprobante()+" - "+tbc.getErrorNumero()+" - "+tbc.getErrorDescripcion());
					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
					documento.setErrorNumero(Constantes.SUNAT_ERROR_0126);
					contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
					mapTime.put("EndGetStatusCDR", new Timestamp(System.currentTimeMillis())); 
					DocumentUtil.getStatisticsGetStatusCDR(mapTime);	
					return documento;					
				}	
				//log.info("3) {} | {} | {} | {}",tbc.getErrorComprobante(),tbc.getErrorNumero(),
				//		tbc.getErrorDescripcion(), new String(tbc.getCdr()));
				if(tbc.getErrorComprobante().equals(Constantes.CONTENT_PROCESO.charAt(0))) {
					tbc.setErrorNumero(Constantes.CONTENT_PROCESO);
					tbc.setErrorDescripcion("El sistema esta procesando el archivo "+tbc.getNombre());
				}		
				if(tbc.getTipoDocumento().trim().equals(Constantes.SUNAT_COMUNICACION_BAJAS) ||
						tbc.getTipoDocumento().trim().equals(Constantes.SUNAT_RESUMEN_DIARIO) ||
						tbc.getTipoDocumento().trim().equals(Constantes.SUNAT_REVERSION)) {
					tbc.setErrorDescripcion(tbc.getErrorDescripcion()+
							Constantes.OSE_SPLIT_3+" ["+tbc.getErrorLog()+"]");
					if((tbc.getErrorNumero()!=null) && 
							(tbc.getErrorNumero().equals(Constantes.CONTENT_PROCESO)))
						tbc.setErrorDescripcion("El sistema esta procesando el archivo "+tbc.getNombre());
					
				}				
				mapTime.put("EndGetStatusCDR", new Timestamp(System.currentTimeMillis())); 
				DocumentUtil.getStatisticsGetStatusCDR(mapTime);	
				return tbc;										
			}
			
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			//log.info("documento.getErrorNumero() 4 - "+documento.getErrorNumero());
			contentValidateSend.buscarErrorComprobante(documento, documento.getErrorNumero());
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getStatusCdr Exception \n"+errors);
		}
		mapTime.put("EndGetStatusCDR", new Timestamp(System.currentTimeMillis())); 
		DocumentUtil.getStatisticsGetStatusCDR(mapTime);	
		return documento;
	}	
}
