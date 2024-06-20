package com.axteroid.ose.server.tools.util;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.bean.CertificadoEmisor;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoJBossOsePropertiesEnum;

public class DirUtil {
	private final static Logger log = LoggerFactory.getLogger(DirUtil.class);
    
    public static String getRutaRobotProperties(){
    	String rutaRobotProperties = (new StringBuilder(Constantes.DIR_AVATAR))
				.append(Constantes.DIR_AVATAR_CONFIG)
				.append(Constantes.FILE_AVATAR_PROPER).toString(); 
    	return rutaRobotProperties;
    }
    
    public static Map<String, String> getMapRobotProperties(){   	
    	Map<String, String> mapRobotProperties = PropertiesUtil.devolverMapProper(getRutaRobotProperties());
    	return mapRobotProperties;
    }    
    
    public static String getAvatarPropertiesValue(String key) {
		Map<String, String> mapa = getMapRobotProperties();
		return mapa.get(key);
    }
    
    public static String getRutaJBossProperties(){
		String rutaOseProperties = (new StringBuilder(Constantes.DIR_AXTEROID_OSE))
				.append(Constantes.DIR_CONFIG)
				.append(Constantes.FILE_JBOSS_PROPER).toString(); 		
    	return rutaOseProperties;
    }
    
    public static Map<String, String> getMapJBossProperties(){   	
    	Map<String, String> mapOseProperties = PropertiesUtil.devolverMapProper(getRutaJBossProperties()); 
    	return mapOseProperties;
    }    
    
    public static String getJBossPropertiesValue(String key) {
		Map<String, String> mapa = getMapJBossProperties();
		return mapa.get(key);
    }
          
    public static String getAmbienteDOS() {
        String ruta = (new StringBuilder(Constantes.DIR_AXTEROID_OSE)).toString();     
        if(FileUtil.tobeDirectory(ruta+Constantes.DIR_AMB_PROD))
        	return Constantes.DIR_AMB_PROD;
        return Constantes.DIR_AMB_BETA;
    }    
    
    public static String getAmbiente() {
        if(getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_PROD.getCodigo())
        		.equals(Constantes.CONTENT_TRUE))
        	return Constantes.DIR_AMB_PROD;     	
        return Constantes.DIR_AMB_BETA;    	
    }     
    
    public static String getAmbienteSunat() {
        if(getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_PROD.getCodigo())
        		.equals(Constantes.CONTENT_TRUE))
        	return Constantes.DIR_AMB_PROD;   
        if(getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_BETA.getCodigo())
        		.equals(Constantes.CONTENT_TRUE))
        	return Constantes.DIR_AMB_BETA; 
        return Constantes.DIR_AMB_ALFA;    	
    }   
    
    public static String getSendSunatWs() {
        if(getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_PROD.getCodigo())
        		.equals(Constantes.CONTENT_TRUE))
        	return getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_PROD_WS.getCodigo());   
        if(getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_BETA.getCodigo())
        		.equals(Constantes.CONTENT_TRUE))
        	return getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_BETA_WS.getCodigo());  
        return Constantes.DIR_AMB_ALFA;    	
    }       
    
    public static String getSunatPropeties() {
			return Constantes.FILE_SUNAT_SECURITY_PROPER;
    }
    
    public static String getSunatPropetiesRobot() {
		return (new StringBuilder(Constantes.DIR_AXTEROID_OSE))
				.append(Constantes.DIR_AVATAR_CONFIG)
				.append(Constantes.FILE_SUNAT_SECURITY_PROPER).toString();
    }
    

    public static String getLinkEnvioCDRSunatProper(String cdr) {
		Map<String, String> mapa = getMapJBossProperties();
		if(getAmbiente().equals(Constantes.DIR_AMB_PROD)) {
		    if(cdr.trim().equals(Constantes.CONTENT_TRUE)){
				//log.info("SUNAT_ENVIO_CDR_PROD_UNO: " + mapa.get(TipoFileOseProperEnum.SUNAT_ENVIO_CDR_PROD_UNO.getCodigo()));
				return mapa.get(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_PROD_UNO.getCodigo());
		    }
		    //log.info("SUNAT_ENVIO_CDR_PROD_DOS: " + mapa.get(TipoFileOseProperEnum.SUNAT_ENVIO_CDR_PROD_DOS.getCodigo()));
		    return mapa.get(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_PROD_DOS.getCodigo());
		}
		if(getAmbiente().equals(Constantes.DIR_AMB_BETA)) {
		    if(cdr.trim().equals(Constantes.CONTENT_TRUE)){
				//log.info("SUNAT_ENVIO_CDR_PROD_UNO: " + mapa.get(TipoFileOseProperEnum.SUNAT_ENVIO_CDR_BETA_DOS.getCodigo()));
				return mapa.get(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_BETA_DOS.getCodigo());
		    }
		    //log.info("SUNAT_ENVIO_CDR_PROD_DOS: " + mapa.get(TipoFileOseProperEnum.SUNAT_ENVIO_CDR_BETA_DOS.getCodigo()));
		    return mapa.get(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_PROD_DOS.getCodigo());
		}
		return "";
    }
    
    public static List<String> getLinkEnvioCDRSunatProper() {
    	List<String> listUrlSunat = new ArrayList<String>();
		Map<String, String> mapa = getMapJBossProperties();
		if(getAmbiente().equals(Constantes.DIR_AMB_PROD)) {
			if(getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_PROD.getCodigo())
	        		.equals(Constantes.CONTENT_TRUE)) {
				//log.info("SUNAT_ENVIO_CDR_PROD_UNO: " + mapa.get(TipoFileOseProperEnum.SUNAT_ENVIO_CDR_PROD_UNO.getCodigo()));
				listUrlSunat.add(mapa.get(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_PROD_UNO.getCodigo()));
				listUrlSunat.add(mapa.get(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_PROD_DOS.getCodigo()));
				return listUrlSunat;
		    }
		    //log.info("SUNAT_ENVIO_CDR_PROD_DOS: " + mapa.get(TipoFileOseProperEnum.SUNAT_ENVIO_CDR_PROD_DOS.getCodigo()));
		}
		if(getAmbiente().equals(Constantes.DIR_AMB_BETA)) {
			if(getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_BETA.getCodigo())
	        		.equals(Constantes.CONTENT_TRUE)) {
				//log.info("SUNAT_ENVIO_CDR_PROD_UNO: " + mapa.get(TipoFileOseProperEnum.SUNAT_ENVIO_CDR_PROD_UNO.getCodigo()));
				listUrlSunat.add(mapa.get(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_BETA_UNO.getCodigo()));
				listUrlSunat.add(mapa.get(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_BETA_DOS.getCodigo()));
				return listUrlSunat;
		    }
		}
		return listUrlSunat;
    }
 
    public static String getLinkOseEnvioUblProper(String wsc) {
		Map<String, String> mapa = getMapJBossProperties();
	    if(wsc.trim().equals(Constantes.CONTENT_TRUE)){
			//log.info("OSE_ENVIO_UBL_PROD_UNO: " + mapa.get(TipoFileOseProperEnum.OSE_ENVIO_UBL_PROD_UNO.getCodigo()));
			return mapa.get(TipoJBossOsePropertiesEnum.AXTEROID_OSE_ENVIO_UBL_PROD_UNO.getCodigo());
	    }
	    //log.info("OSE_ENVIO_UBL_PROD_DOS: " + mapa.get(TipoFileOseProperEnum.OSE_ENVIO_UBL_PROD_DOS.getCodigo()));
	    return mapa.get(TipoJBossOsePropertiesEnum.AXTEROID_OSE_ENVIO_UBL_PROD_DOS.getCodigo());
    }    
    
    public static int getTipoBD() {
    	String ambiente = DirUtil.getAmbiente();
    	int tipBD = 6; // POSTGRESQL - PRODUCCION 	
		if(ambiente.equals(Constantes.DIR_AMB_BETA))
			tipBD = 7; // POSTGRESQL - BETA
		return tipBD;
    }
    
    public static String getRutaCertificadosProperties(){
		String rutaOseProperties = (new StringBuilder(Constantes.DIR_AXTEROID_OSE))
				.append(Constantes.DIR_AVATAR_CONFIG)
				.append(Constantes.FILE_CERTICADO_EMISOR).toString(); 		
    	return rutaOseProperties;
    }
    
    public static List<String> getListCertificadosProperties(){   	
    	log.info("getRutaCertificadosProperties() "+getRutaCertificadosProperties());
    	List<String> mapa = PropertiesUtil.devolverListProper(getRutaCertificadosProperties());
    	return mapa;
    }    
    
	public static CertificadoEmisor getCertificadoEmisor(String idCertificadoEmisor, Date fechaEmision){
		//log.info("idCertificadoEmisor "+idCertificadoEmisor+" | fechaEmision "+fechaEmision);
		CertificadoEmisor certificadoEmisor = new CertificadoEmisor();
		try{						
			List<String> certificadoEmisorMap =  getListCertificadosProperties();
			for (String entry : certificadoEmisorMap) {	
				String sEntry = entry.toString();	
				//log.info(" sEntry: " + sEntry);
				String [] arEntry = sEntry.split(":=");
				if(arEntry.length!=2) 
					continue;		
				if(!idCertificadoEmisor.trim().equals(arEntry[0].trim()))
					continue;			
				String certificadoEmisorYaml = arEntry[1];
				String [] cey = certificadoEmisorYaml.split(";");
				//log.info(" cey.length: " + cey.length);
				if(cey.length!=6) 
					continue;	
				Date fechaInicio = DateUtil.stringToDateYYYY_MM_DD(cey[4]);
				Date fechaFin = DateUtil.stringToDateYYYY_MM_DD(cey[5]);
				log.info(" Key: " + arEntry[0] + " | Value: " + arEntry[1]+" | "+fechaEmision+" - "+fechaInicio+" - "+fechaFin);
				if(LogDateUtil.validarFechaEnRango(fechaEmision,fechaInicio,fechaFin)){	
					certificadoEmisor.setKeyPassword(cey[0].trim());
					certificadoEmisor.setPrivateKeyAlias(cey[1].trim());
					certificadoEmisor.setKeyStorePassword(cey[2].trim());
					certificadoEmisor.setCertificate(cey[3].trim());				
					certificadoEmisor.setFechaInicio(fechaInicio);				
					certificadoEmisor.setFechaFin(fechaFin);					
					return certificadoEmisor;
				}
		    }
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCertificadoEmisorYaml Exception \n"+errors);	
		}		
		return certificadoEmisor;
	}   

    public static String getRutaMongoDBProperHost() {  	
		Map<String, String> mapa = getMapJBossProperties();
		return mapa.get(TipoJBossOsePropertiesEnum.DB_MONGODB_OSE_HOST.getCodigo());
    }	
	
    public static int getRutaMongoDBProperPort() {  	
		Map<String, String> mapa = getMapJBossProperties();
		return Integer.parseInt(mapa.get(TipoJBossOsePropertiesEnum.DB_MONGODB_OSE_PORT.getCodigo()));
    }
    
    public static String getRutaMongoDBProperDBSchema() {  	
		Map<String, String> mapa = getMapJBossProperties();
		return mapa.get(TipoJBossOsePropertiesEnum.DB_MONGODB_OSE_DBSCHEMA.getCodigo());
    }	
    
    public static String getRutaMongoDBProperUser() {  	
		Map<String, String> mapa = getMapJBossProperties();
		return mapa.get(TipoJBossOsePropertiesEnum.DB_MONGODB_OSE_USER.getCodigo());
    }	
    
    public static String getRutaMongoDBProperPassword() {  	
		Map<String, String> mapa = getMapJBossProperties();
		return mapa.get(TipoJBossOsePropertiesEnum.DB_MONGODB_OSE_PASSWORD.getCodigo());
    }	   
    
    
    public static String getCertificateRUC() {  	
		Map<String, String> mapa = getMapJBossProperties();
		return mapa.get(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_CERTIFICATE_RUC.getCodigo());
    }	
	
    public static String getCertificateName() {  	
		Map<String, String> mapa = getMapJBossProperties();
		return mapa.get(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_CERTIFICATE_NAME.getCodigo());
    }	
    public static String getCertificateStorePassword() {  	
		Map<String, String> mapa = getMapJBossProperties();
		return mapa.get(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_CERTIFICATE_STORE_PASSWORD.getCodigo());
    }	
    public static String getCertificateKeyPassword() {  	
		Map<String, String> mapa = getMapJBossProperties();
		return mapa.get(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_CERTIFICATE_KEY_PASSWORD.getCodigo());
    }	
    public static String getCertificatePrivateKeyAlias() {  	
		Map<String, String> mapa = getMapJBossProperties();
		return mapa.get(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_CERTIFICATE_PRIVATE_KEY_ALIAS.getCodigo());
    }	
    public static String getCertificateType() {  	
		Map<String, String> mapa = getMapJBossProperties();
		return mapa.get(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_CERTIFICATE_TYPE.getCodigo());
    }	
    public static String getCertificateSigPropFile() {  	
		Map<String, String> mapa = getMapJBossProperties();
		return mapa.get(TipoJBossOsePropertiesEnum.SUNAT_SEND_CDR_CERTIFICATE_SIG_PROP_FILE.getCodigo());
    }	
}
