package com.axteroid.ose.server.servicerest.sec;

import java.io.File;
//import java.io.PrintWriter;
//import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesejb.rules.UBLListParametroLocal;
import com.axteroid.ose.server.rulesejb.rules.impl.UBLListParametroImpl;
import com.axteroid.ose.server.tools.algoritmo.NcCrypt;
import com.axteroid.ose.server.tools.bean.CdrSunatResponse;
import com.axteroid.ose.server.tools.bean.OseResponse;
import com.axteroid.ose.server.tools.bean.SunatBeanRequest;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.exception.JavaParserExceptions;
import com.axteroid.ose.server.tools.util.FileUtil;
import com.axteroid.ose.server.tools.util.ZipUtil;
import com.axteroid.ose.server.tools.xml.UblResponseSunat;
import com.axteroid.ose.server.tools.xml.XmlUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AxteroidSecurityRest {

	private static final Logger log = LoggerFactory.getLogger(AxteroidSecurityRest.class);
	private String userOse = "";
	
	public AxteroidSecurityRest() {
		log.info("AxteroidSecurity REST: {} ",Constantes.OSE_VERSION);
	}
		
	public void getComprobante(Documento documento, String ruc, String tipoComprobante, String serie, String correlativo) {
		documento.setRucEmisor(Long.parseLong(ruc));
		documento.setTipoDocumento(tipoComprobante);
		documento.setSerie(serie);
		documento.setNumeroCorrelativo(correlativo);
		Long corr = Long.parseLong(correlativo);
		documento.setCorrelativo(corr); 
	}
	
	public byte[] getCDR2Zip(String retorno, String fileName) {
		byte[] byteCDR = new byte[] {};
		try {
			List<File> listFile = new ArrayList<File>();
			String nombreFile = Constantes.CDR_PREFI+Constantes.GUION+fileName;
			int indexPoint = nombreFile.lastIndexOf(".");
			String prefix = nombreFile.substring(0, indexPoint);			
			byte [] byteOSE = retorno.getBytes();			
			File fileCDR = FileUtil.writeToFilefromBytes(prefix, Constantes.OSE_FILE_XML, byteOSE);
			log.info("fileCDR.getName(): "+fileCDR.getName());
			if(fileCDR != null)
				listFile.add(fileCDR);			
			byteCDR = ZipUtil.zipFiles2Byte(listFile);													
            if (fileCDR.exists())
            	fileCDR.delete();    					
		}catch(Exception e) {
			JavaParserExceptions.getParseException(e, "getCDR2Zip");
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));
//			log.error("getCDR2Zip Exception \n"+errors);	
		}
		return byteCDR;
	}
		
	public boolean getHeader(HttpHeaders httpHeaders) {
		boolean auth = false;
    	MultivaluedMap<String, String> requestHeaders = httpHeaders.getRequestHeaders();
        List<String> listToken = requestHeaders.get("Authorization");
        if((listToken != null) && (listToken.size() > 0)) {
        	String token_1 = listToken.get(0);
        	String token_2 = token_1.replace("Token ", "");
        	String token_3 = token_2;
        	byte[] base64Creds = Base64.getDecoder().decode(token_3);
        	String token = new String (base64Creds);
        	//log.info("token_1: {} | token_2: {} | token_3: {} | token {}",token_1,token_2,token_3,token);
        	String[] clave = token.split(":");
        	userOse = clave[0];
        	//log.info("userOse: {} | clave[1]: {} ",userOse,clave[1]);       	
        	String pass = this.bucarLoginParametro();
        	//log.info("clave[1]: {} | pass: {} ",clave[1],pass); 
        	if(clave[1].equals(pass)) 
        		auth = true;
        }                
        return auth;
	}
	
	public String bucarLoginParametro(){		
		log.info("bucarLoginParametro {} ",userOse);
		try {			
			UBLListParametroLocal ublListParametroLocal = new UBLListParametroImpl();
			String clave = ublListParametroLocal.bucarLoginParametro(userOse);
			log.info("bucarLoginParametro clave: {} ",clave);
			if(clave != null && clave.length()>0) {
    			String password = NcCrypt.desencriptarPassword(clave);
    			log.info("bucarLoginParametro "+userOse+" - "+password);
    			return NcCrypt.desencriptarPassword(clave);
			}
		}catch(Exception e) {
			JavaParserExceptions.getParseException(e, "bucarLoginParametro");
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));
//			log.error("bucarLoginParametro Exception \n {}",errors);
		}
		return "";
	}

	public void getSunatSendBean(SunatBeanRequest sunatSendBean) {
    	//log.info("getSunatSendBean: {}" + new String(sunatSendBean.getContent()));
    	try {    		
    		this.getFilename(sunatSendBean);
    		String fileName = sunatSendBean.getFilename();
    		//log.info("fileName: {}",fileName);
    		int indexPoint = fileName.lastIndexOf(".");
    		String nameZip = fileName.substring(0, indexPoint); 
    		sunatSendBean.setFilename(nameZip+Constantes.OSE_FILE_ZIP);
    		// UBL    		
			List<File> listFile = new ArrayList<File>();
			File fileUBL = ZipUtil.getFileSUNAT(fileName, sunatSendBean.getContent());			
			if(fileUBL != null)
				listFile.add(fileUBL);			 		
    		// ZIP
    		byte[] bytes = ZipUtil.zipFiles2Byte(listFile);   	
    		
    		sunatSendBean.setContent(bytes);
			if (fileUBL.exists())
				fileUBL.delete(); 		
    		
    	} catch (Exception e) {
    		JavaParserExceptions.getParseException(e, "getSunatSendBean");
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));
//			log.error("getSunatSendBean Exception \n"+errors);  
		}
    }		
    
	public void getFilename(SunatBeanRequest sunatSendBean) {  
    	String filename = "";
    	if((sunatSendBean.getFilename() == null) || (sunatSendBean.getFilename().isEmpty())) {   		
    		filename = XmlUtil.getContent2Filename(sunatSendBean.getContent());    	
    		sunatSendBean.setFilename(filename);
    	}
    }	
	
	public void readAR(OseResponse oseResponse) {
		log.info("readAR ");
		try {
			//String cdr = new String(oseResponse.getCdr());
			//log.info("readAR cdr: {}",cdr);
			CdrSunatResponse cdrSunatResponse = UblResponseSunat.readStatus2CdrSunat(oseResponse.getCdr());
			ObjectMapper ObjMapperArchivo = new ObjectMapper();
			String jsonMapperArchivo = ObjMapperArchivo.writeValueAsString(cdrSunatResponse);	
			oseResponse.setErrorCode(cdrSunatResponse.getResponseCode());
			oseResponse.setMessage(cdrSunatResponse.getDescription());
			oseResponse.setData(jsonMapperArchivo);
			log.info("jsonMapperArchivo: {}",jsonMapperArchivo);
		}catch (Exception e) {
			JavaParserExceptions.getParseException(e, "readAR");
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));			
//			log.error("readAR Exception: "+e.getMessage());
		}	    
	}
	
	public void readCdrSunat(OseResponse oseResponse) {
		log.info("readCdrSunat ");
		try {
//			String cdr = new String(oseResponse.getCdr());
//			log.info("readCdrSunat cdr: {}",cdr);
			CdrSunatResponse cdrSunatResponse = UblResponseSunat.readStatus2CdrSunat(oseResponse.getCdr());
			if(cdrSunatResponse.getResponseCode().trim().equals(Constantes.CONTENT_TRUE)) {
				oseResponse.setSuccess(Constantes.CONTENT_TRUE);
				oseResponse.setErrorCode(cdrSunatResponse.getResponseCode());
				oseResponse.setMessage(cdrSunatResponse.getDescription());
			}else {				
				oseResponse.setErrorCode(Constantes.SUNAT_ERROR_98_C);
				if((cdrSunatResponse.getResponseCode()!=null) && !(cdrSunatResponse.getResponseCode().isEmpty())) 
					oseResponse.setErrorCode(cdrSunatResponse.getResponseCode());
				oseResponse.setMessage(cdrSunatResponse.getResponseCode()+" | "+cdrSunatResponse.getDescription());
			}
			ObjectMapper ObjMapperArchivo = new ObjectMapper();
			String jsonMapperArchivo = ObjMapperArchivo.writeValueAsString(cdrSunatResponse);				
			oseResponse.setData(jsonMapperArchivo);
			log.info("jsonMapperArchivo: {}",jsonMapperArchivo);
		}catch (Exception e) {
			JavaParserExceptions.getParseException(e, "readCdrSunat");
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));			
//			log.error("readCdrSunat Exception: "+e.getMessage());
		}	    
	}
	
	private void getUriInfo(UriInfo uriInfo) {
    	log.info("sendBill uriInfo.getAbsolutePath(): {}",uriInfo.getAbsolutePath());
    	log.info("sendBill uriInfo.getAbsolutePathBuilder(): {}",uriInfo.getAbsolutePathBuilder());
    	log.info("sendBill uriInfo.getBaseUri(): {}",uriInfo.getBaseUri());
    	log.info("sendBill uriInfo.getBaseUriBuilder(): {}",uriInfo.getBaseUriBuilder());
    	log.info("sendBill uriInfo.getPath(): {}",uriInfo.getPath());
    	log.info("sendBill uriInfo.getPathParameters(): {}",uriInfo.getPathParameters());
    	log.info("sendBill uriInfo.getPathSegments(): {}",uriInfo.getPathSegments());
    	log.info("sendBill uriInfo.getQueryParameters(): {}",uriInfo.getQueryParameters());
    	log.info("sendBill uriInfo.getRequestUri(): {}",uriInfo.getRequestUri());
    	log.info("sendBill uriInfo.getRequestUriBuilder(): {}",uriInfo.getRequestUriBuilder());
    	MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		HashMap<String, Object> obj = new HashMap<String, Object>(); 
		for(String strQP : queryParameters.keySet()){
			log.info("getUriInfo strQP queryParameters.keySet(): {}",strQP);
			//obj.put(str, queryParameters.getFirst(str));
		}
		
        String strGQP = queryParameters.entrySet()
                .stream()
                .map(e -> e.getKey() + " = " + e.getValue())
                .collect(Collectors.joining("<br/>"));
        
        log.info("getUriInfo uriInfo.queryParameters(): {}",strGQP);
	}
	
	private void getHttpHeaders(HttpHeaders httpHeaders) {
    	MultivaluedMap<String, String> requestHeaders = httpHeaders.getRequestHeaders();
    	String strRH = requestHeaders.entrySet()
                     .stream()
                     .map(e -> e.getKey() + " = " + e.getValue())
                     .collect(Collectors.joining("<br/>"));
    	log.info("getHeader strRH httpHeaders.getRequestHeaders(): {}",strRH);
    	for(String strQP : requestHeaders.keySet()){        	
			log.info("requestHeaders.keySet(): {} | {}",strQP,requestHeaders.get(strQP));			
			}
    	log.info("getHeader Authorization: {}",requestHeaders.get("Authorization"));
	}
		
	private byte[] getReplaceContentFile(String contentFile) {
		//log.info("getReplaceContentFile contentFile: {}",contentFile);
		String rContentFile = contentFile.replaceAll(" ", "+");
		//log.info("getReplaceContentFile contentByte: {}",rContentFile);
		byte[] contentByte = Base64.getDecoder().decode(rContentFile);
		return contentByte;		
	}

	public String getUserOse() {
		return userOse;
	}
}
