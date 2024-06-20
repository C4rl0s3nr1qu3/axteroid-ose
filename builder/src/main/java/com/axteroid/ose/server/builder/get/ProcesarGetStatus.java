package com.axteroid.ose.server.builder.get;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.builder.build.DocumentReviewBuild;
import com.axteroid.ose.server.builder.content.ContentValidateSend;
import com.axteroid.ose.server.builder.content.ReturnResponse;
import com.axteroid.ose.server.builder.content.impl.ContentValidateSendImpl;
import com.axteroid.ose.server.builder.content.impl.ReturnResponseImpl;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesejb.rules.OseDBCRUDLocal;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.DocumentUtil;
import com.axteroid.ose.server.tools.util.FileUtil;
import com.axteroid.ose.server.tools.util.StringUtil;
import com.axteroid.ose.server.tools.util.ZipUtil;

public class ProcesarGetStatus {
	private static final Logger log = LoggerFactory.getLogger(ProcesarGetStatus.class);
	
	private ContentValidateSend contentValidateSend = new ContentValidateSendImpl();
	private ReturnResponse returnResponse = new ReturnResponseImpl();
	
	public Documento getStatus(String ticket, String user) {						
		//TbContent documento = new TbContent();		
		Documento documento = new Documento();
		Map<String, Timestamp> mapTime = new HashMap<String, Timestamp>();
    	mapTime.put("StartGetStatus", new Timestamp(System.currentTimeMillis())); 
		if(user.equals(Constantes.AVATAR_USER)) {
			DocumentReviewBuild documentReviewBuild = new DocumentReviewBuild();
			documentReviewBuild.getBuilding(ticket);
			documento.setErrorComprobante(Constantes.CONTENT_TRUE.charAt(0));
			documento.setErrorNumero(Constantes.CONTENT_TRUE);
			String doc = Constantes.SUNAT_ERROR_ROBOT+" "+ticket;
			documento.setCdr(doc.getBytes());
			return documento;
		}
		
		if(!StringUtil.hasString(ticket)) {		
			this.getTbContent(documento, Constantes.SUNAT_ERROR_0127);
			return documento;
		}
		
		documento.setId(Long.parseLong(ticket));
		documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));
		//contentValidateSend.validarContentSend(documento);
		try {						
			//returnResponse.getRUCbyUser(documento, user);
			if(!documento.getErrorComprobante().equals(Constantes.CONTENT_PROCESO.charAt(0))) {
				contentValidateSend.buscarError(documento, documento.getErrorNumero());	
				return documento;
			}
			//log.info("2) "+documento.getErrorContent()+" - "+documento.getErrorNumero()+" - "+documento.getErrorDescripcion());			
			OseDBCRUDLocal crudOseDBLocal = 
				(OseDBCRUDLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"OseDBCRUDImpl"+Constantes.OSE_CDEJB_RULES+"OseDBCRUDLocal");						
			List<Documento> listTBC = crudOseDBLocal.buscarTbComprobanteXContentID_GET(documento);
			if(listTBC!=null && listTBC.size()>0) {
				//log.info("3) "+documento.getErrorContent()+" - "+documento.getErrorNumero()+" - "+documento.getErrorDescripcion());
				if(!(listTBC.get(0).getUserCrea().equals(user))) {
					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
					documento.setErrorNumero(Constantes.SUNAT_ERROR_0126);
					contentValidateSend.buscarError(documento, documento.getErrorNumero());
					mapTime.put("EndGetStatus", new Timestamp(System.currentTimeMillis())); 
					DocumentUtil.getStatisticsGetStatus(mapTime);	
					return documento;					
				}				
				//log.info("4) "+documento.getErrorContent()+" - "+documento.getErrorNumero()+" - "+documento.getErrorDescripcion());
				if((listTBC.get(0).getCdr()!=null) && (listTBC.get(0).getCdr().length>0)){		
					List<File> listFile = this.getListFile(listTBC);	        	
		        	byte[] bytes = ZipUtil.zipFiles2Byte(listFile);	
		        	this.delListFile(listFile);
					documento.setErrorComprobante(Constantes.CONTENT_TRUE.charAt(0));
					documento.setErrorNumero(Constantes.CONTENT_PROCESO);
					documento.setCdr(bytes);
					if(bytes.length>0) 
						documento.setErrorNumero(Constantes.CONTENT_TRUE);
					mapTime.put("EndGetStatus", new Timestamp(System.currentTimeMillis())); 
					DocumentUtil.getStatisticsGetStatus(mapTime);	
					return documento;
				}
				//log.info("6) "+documento.getErrorContent()+" - "+documento.getErrorNumero()+" - "+documento.getErrorDescripcion());
				//log.info("6.a) "+listTBC.get(0).getErrorComprobante()+" - "+listTBC.get(0).getErrorNumero()+" - "+listTBC.get(0).getErrorDescripcion());
				if(listTBC.get(0).getErrorComprobante().equals(Constantes.CONTENT_PROCESO.charAt(0)))
					documento.setErrorNumero(Constantes.CONTENT_PROCESO);
				else 					
					documento.setErrorNumero(listTBC.get(0).getErrorNumero());
				
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorDescripcion(listTBC.get(0).getErrorDescripcion());
				//log.info("7) "+documento.getErrorContent()+" - "+documento.getErrorNumero()+" - "+documento.getErrorDescripcion());
				if(listTBC.get(0).getTipoDocumento().trim().equals(Constantes.SUNAT_COMUNICACION_BAJAS) ||
						listTBC.get(0).getTipoDocumento().trim().equals(Constantes.SUNAT_RESUMEN_DIARIO) ||
						listTBC.get(0).getTipoDocumento().trim().equals(Constantes.SUNAT_REVERSION)) {
					documento.setErrorDescripcion(listTBC.get(0).getErrorDescripcion()+
							Constantes.OSE_SPLIT_3+" ["+listTBC.get(0).getErrorLog()+"]");
					if(documento.getErrorNumero().equals(Constantes.CONTENT_PROCESO))
						documento.setErrorDescripcion("El sistema esta procesando el archivo "+listTBC.get(0).getNombre());
				}
				//log.info("8) "+documento.getErrorContent()+" - "+documento.getErrorNumero()+" - "+documento.getErrorDescripcion());
				mapTime.put("EndGetStatus", new Timestamp(System.currentTimeMillis())); 
				DocumentUtil.getStatisticsGetStatus(mapTime);	
				return documento;
			}			
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			contentValidateSend.buscarError(documento, documento.getErrorNumero());			
			//log.info("9) "+documento.getErrorContent()+" - "+documento.getErrorNumero()+" - "+documento.getErrorDescripcion());			
			mapTime.put("EndGetStatus", new Timestamp(System.currentTimeMillis())); 
			DocumentUtil.getStatisticsGetStatus(mapTime);	
			return documento;	

		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getStatus Exception \n"+errors);
		}
		mapTime.put("EndGetStatus", new Timestamp(System.currentTimeMillis())); 
		DocumentUtil.getStatisticsGetStatus(mapTime);		
		return null;
	}
		
	private List<File> getListFile(List<Documento> list) {
    	List<File> listFile = new ArrayList<File>();
    	for(Documento t : list){
    		try {
    			//String nombreFile = OseConstantes.OSE_FILE_R+t.getNombre();
    			String nombreFile = Constantes.CDR_PREFI+Constantes.GUION+t.getNombre();
    			int indexPoint = nombreFile.lastIndexOf(".");
        		String prefix = nombreFile.substring(0, indexPoint);           		
    			File fileCDR = FileUtil.writeToFilefromBytes(prefix, Constantes.OSE_FILE_XML, t.getCdr());    			
    			if(fileCDR != null){
    				listFile.add(fileCDR);    			    			    			
    				log.info("CDR: "+fileCDR.getName());
    			}
    		}catch(Exception e) {
    			StringWriter errors = new StringWriter();				
    			e.printStackTrace(new PrintWriter(errors));
    			log.error("getListFile Exception \n"+errors);
    		}
    	}    	
        return listFile;
    }
	private void delListFile(List<File> listFile) {
		for(File fileCDR : listFile){
			if (fileCDR.exists())
            	fileCDR.delete();
		}
	}
			
	private void getTbContent(Documento documento, String error){
		documento.setId(0l);
		documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));
		//contentValidateSend.validarContentSend(documento);
		documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
		documento.setErrorNumero(error);		
		contentValidateSend.buscarError(documento, documento.getErrorNumero());	
		
	}
}
