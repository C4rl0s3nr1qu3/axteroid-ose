package com.axteroid.ose.server.sunat.service;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.bean.SunatBeanRequest;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.FileUtil;
import com.axteroid.ose.server.tools.util.ZipUtil;
import com.axteroid.ose.server.tools.xml.UblResponseSunat;

public class SunatFileResponseUtil {
	private static final Logger log = LoggerFactory.getLogger(SunatFileResponseUtil.class);
	
    public static SunatBeanRequest getStatusResponseBean(Documento documento) {
    	try {
    		SunatBeanRequest sendSunatBean = new SunatBeanRequest();
    		int indexPoint = documento.getNombre().lastIndexOf(".");
    		String nameZip = documento.getNombre().substring(0, indexPoint); 
    		sendSunatBean.setFilename(nameZip+Constantes.OSE_FILE_ZIP);
    		// UBL
			List<File> listFile = new ArrayList<File>();
			File fileUBL = getFileSUNAT(documento.getNombre(), documento.getUbl());			
			if(fileUBL != null)
				listFile.add(fileUBL);			
    		// CDR    		
    		//String nombreUBL = ConstantesOse.OSE_FILE_R+documento.getNombre();
			String nombreUBL = Constantes.CDR_PREFI+Constantes.GUION+documento.getNombre();
    		File fileCDR = getFileSUNAT(nombreUBL, documento.getCdr());
			if(fileCDR != null)
				listFile.add(fileCDR);    		
    		// ZIP
    		byte[] bytes = ZipUtil.zipFiles2Byte(listFile);   		
    		sendSunatBean.setContent(bytes);
			if (fileUBL.exists())
				fileUBL.delete();
			if (fileCDR.exists())
            	fileCDR.delete();
    		return sendSunatBean;    		   		
    	} catch (Exception e) {
    		documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_SERVICIO_INHABILITADO);
			documento.setErrorLogSunat(e.toString());
			documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());     
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getStatusResponseBean Exception \n"+errors);  
		}
    	return null;
    }
    
    public static SunatBeanRequest getStatusResponseBeanUbl(Documento documento) {
    	log.info("getStatusResponseBeanUbl: " + documento.toString());
    	try {
    		SunatBeanRequest sendSunatBean = new SunatBeanRequest();
    		int indexPoint = documento.getNombre().lastIndexOf(".");
    		String nameZip = documento.getNombre().substring(0, indexPoint); 
    		sendSunatBean.setFilename(nameZip+Constantes.OSE_FILE_ZIP);
    		// UBL    		
			List<File> listFile = new ArrayList<File>();
			File fileUBL = getFileSUNAT(documento.getNombre(), documento.getUbl());			
			if(fileUBL != null)
				listFile.add(fileUBL);			 		
    		// ZIP
    		byte[] bytes = ZipUtil.zipFiles2Byte(listFile);   	   		
    		sendSunatBean.setContent(bytes);
			if (fileUBL.exists())
				fileUBL.delete();
    		return sendSunatBean;    		    		
    	} catch (Exception e) {
    		documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_SERVICIO_INHABILITADO);
    		documento.setErrorLogSunat(e.toString());
    		documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());     
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getStatusResponseBeanUbl Exception \n"+errors);  
		}
    	return null;
    }	
    
	private static File getFileSUNAT(String nombreFile, byte[] byteOSE) {
    	try {
    		int indexPoint = nombreFile.lastIndexOf(".");
        	String prefix = nombreFile.substring(0, indexPoint);   
    		File file = FileUtil.writeToFilefromBytes(prefix, Constantes.OSE_FILE_XML, byteOSE);
    		return file;
    	}catch(Exception e) {
    		StringWriter errors = new StringWriter();				
    		e.printStackTrace(new PrintWriter(errors));
    		log.error("Exception \n"+errors);
    	}
        return null;
    }  
	
	public static void readRespuestaSunat(Documento documento){
		//log.info("readRespuestaSunat  ");
		UblResponseSunat ublARSunat = new UblResponseSunat();		
		String respuestaSunat = ublARSunat.readResponseCodeCDR(documento.getMensajeSunat());
		if(!(respuestaSunat!=null))
			return;
		documento.setEstado(Constantes.SUNAT_CDR_AUTORIZADO);		
		if(!respuestaSunat.equals("0")) {
			documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDBILL);
			if(documento.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS) ||
					documento.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO) ||
					documento.getTipoDocumento().equals(Constantes.SUNAT_REVERSION))
				documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDSUMMARY);
		}
		documento.setRespuestaSunat(respuestaSunat);		
		readAR(documento);	
	}
	
	public static void readAR(Documento documento) {
		UblResponseSunat ublARSunat = new UblResponseSunat();
		String mensaje = ublARSunat.readResponseCodeCDRDescription(documento.getMensajeSunat());
		String adv = "";
		Boolean bADV = false;
		if((mensaje!= null) && (mensaje.length()>0)) {
			String[] splitMensaje = mensaje.split("\\. ");			
			for(int i =0; i < splitMensaje.length; i++) {
				String repMensaje = splitMensaje[i].replace("\n", "");
				String trimMensaje = repMensaje.trim();
				if(i==0){
					documento.setErrorLogSunat(trimMensaje);
					continue;
				}
				adv = adv +" "+trimMensaje;
				bADV = true;
			}
			if(bADV)
				documento.setAdvertencia(adv);
		}
		//log.info("adv (1): "+documento.getAdvertencia());
		String cdrNote = ublARSunat.readResponseCodeCDRNote(documento.getMensajeSunat());
		//log.info("cdrNote: "+cdrNote);
		if((cdrNote!= null) && (cdrNote.length()>0) && bADV) 
			documento.setAdvertencia(adv+" | "+cdrNote);
		if((cdrNote!= null) && (cdrNote.length()>0) && !bADV) 
			documento.setAdvertencia(cdrNote);
		//log.info("adv (2): "+documento.getAdvertencia());
	}
}
