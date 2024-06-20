package com.axteroid.ose.server.tools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.axteroid.ose.server.tools.bean.CertificadoEmisor;
import com.axteroid.ose.server.tools.constantes.Constantes;

public class ReadYaml {
	private final static Logger log = LoggerFactory.getLogger(ReadYaml.class);
	
	public static CertificadoEmisor getCertificadoEmisorYaml(String idCertificadoEmisor, Date fechaEmision){
		CertificadoEmisor certificadoEmisor = new CertificadoEmisor();
		try{
			String rutaYaml = (new StringBuilder(Constantes.DIR_AVATAR))
					.append(Constantes.DIR_AVATAR_CONFIG)
					.append(Constantes.YAML_CERTICADO_EMISOR).toString();	
						
			Map<String, Object> certificadoEmisorMap =  getCertificadoEmisor(rutaYaml,"certificado","emisor");
			for (Map.Entry<String, Object> entry : certificadoEmisorMap.entrySet()) {	
				String sEntry = entry.toString();		
				log.info(" entry: " + sEntry);
			}	
			
			for (Map.Entry<String, Object> entry : certificadoEmisorMap.entrySet()) {	
				String sEntry = entry.toString();		
				log.info(" entry: " + sEntry);
				String [] arEntry = sEntry.split("=");
				if(arEntry.length!=2) 
					continue;				
				if(!idCertificadoEmisor.equals(arEntry[0]))
					continue;			
				String certificadoEmisorYaml = arEntry[1];
				String [] cey = certificadoEmisorYaml.split(";");
				if(cey.length!=6) 
					continue;			
				Date fechaInicio = DateUtil.stringToDateYYYY_MM_DD(cey[4]);
				Date fechaFin = DateUtil.stringToDateYYYY_MM_DD(cey[5]);
				log.info(" Key: " + arEntry[0] + " | Value: " + arEntry[1]+" | "+fechaInicio+" - "+fechaInicio);
				if(LogDateUtil.validarFechaEnRango(fechaEmision,fechaInicio,fechaFin)){	
					certificadoEmisor.setKeyPassword(cey[0]);
					certificadoEmisor.setPrivateKeyAlias(cey[1]);
					certificadoEmisor.setKeyStorePassword(cey[2]);
					certificadoEmisor.setCertificate(cey[3]);				
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
	
	@SuppressWarnings("unchecked")
	private static Map<String, Object> getCertificadoEmisor(String rutaYaml, String tab1Yaml, String tab2Yaml) {
		ReadYaml readYaml = new ReadYaml();
		Map<String, Object> yamlMap = readYaml.readYaml(rutaYaml);
		Map<String, Object> tab1YamlMap = (Map<String, Object>)yamlMap.get(tab1Yaml);
		Map<String, Object> tab2YamlMap = (Map<String, Object>)tab1YamlMap.get(tab2Yaml);	
		return tab2YamlMap;
	}	
	
	private static String getCertificadoEmisor(String idCertificadoEmisor) {
		String rutaCertificadoEmisorYaml = (new StringBuilder(Constantes.DIR_AXTEROID_OSE))
				.append(Constantes.DIR_AVATAR_CONFIG)
				.append(Constantes.YAML_CERTICADO_EMISOR).toString();	
		ReadYaml readYaml = new ReadYaml();
		Map<String, Object> yamlMap = readYaml.readYaml(rutaCertificadoEmisorYaml);
		Map<String, Object> certificadoMap = (Map<String, Object>)yamlMap.get("certificado");
		Map<String, Object> emisorMap = (Map<String, Object>)certificadoMap.get("emisor");		
		return (String) emisorMap.get(idCertificadoEmisor);
	}	
	
	private Map<String, Object> readYaml(String rutaYaml){
		//log.info("readYaml  ");		
		try {	
			Yaml yaml = new Yaml();
			File origen = new File(rutaYaml);
			InputStream inputStream = new FileInputStream(origen);
			Map<String, Object> yamlMap = yaml.load(inputStream);
			inputStream.close();
			return yamlMap;
	    } catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("readYaml Exception \n"+errors);
		} 		
		return new HashMap<String, Object>();
	}
	
	
}
