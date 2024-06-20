package com.axteroid.ose.server.sunat.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.util.DirUtil;

public class CertificateLoader {
	private static final Logger log = LoggerFactory.getLogger(CertificateLoader.class);
	private static Map<String, Certificado> mapCerts = new HashMap<>();
	private static String typeCertificate = DirUtil.getCertificateType();
	
	public static Certificado getCertificado(String pathArchivo, String clave) {
		log.info("getCertificado pathArchivo: {} | clave: {}",pathArchivo,clave);
		
		if (!mapCerts.containsKey(pathArchivo)) {
			mapCerts.put(pathArchivo, load(pathArchivo, clave));
		}		
		return mapCerts.get(pathArchivo);
	}
	
	private static Certificado load(String pathArchivo, String clave) {
		log.info("load pathArchivo: {} | clave: {}",pathArchivo,clave);
		Certificado certificado = new Certificado();		
		File archivo = new File(pathArchivo);        
        /*Obtenemos la instancia de KeyStore para el tipo especificado*/
        KeyStore ks = null;
        InputStream is = null;        
        try {
        	ks = KeyStore.getInstance(typeCertificate);
			is = new FileInputStream(archivo);
			ks.load(is, clave.toCharArray() );
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is!=null) {
					is.close();
				}
			} catch (IOException e) {}
		}		
		try {
			Enumeration<?> aliasesEnum = ks.aliases();

			while (aliasesEnum.hasMoreElements()) {
				String alias = (String)aliasesEnum.nextElement();
				if (ks.isKeyEntry(alias)) {
					Certificate[] certs = ks.getCertificateChain(alias);
					certificado.setX509Certificate( (X509Certificate) certs[0] );
					certificado.setPrivateKey( (PrivateKey)ks.getKey(alias, clave.toCharArray()) );
					certificado.setPublicKey( certificado.getX509Certificate().getPublicKey() );
				}
			}			
		}
		catch(Throwable e) { 
			e.printStackTrace();
		}		       
        return certificado;
    }
	
    public static void main(String[] args) {
        try {
            String filename = "c:/home/axteroid/AXTEROID20610051406.pfx";
            String password = "0710";
            
            Certificado certificado = getCertificado(filename, password);
            System.out.println(certificado.toString());
            
        } catch (Exception e) {
        	e.printStackTrace();
        } 
    }
	
}
