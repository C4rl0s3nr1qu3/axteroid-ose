package com.axteroid.ose.server.ubl21.sign.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.axteroid.ose.server.tools.util.LogDateUtil;

public class SignDocumentCdrKeyStore {
	//private static final Logger log = Logger.getLogger(SignDocumentCDRKeyStore.class);
	private static final Logger log = LoggerFactory.getLogger(SignDocumentCdrKeyStore.class);
	public static KeyStore loadStore() throws KeyStoreException,  IOException, GeneralSecurityException {
		String keyStorePassword = Constantes.OSE_JKS_StorePassword;
		KeyStore ks = KeyStore.getInstance(Constantes.OSE_JKS_TYPE);
		InputStream fis = null; 
        try {            
            byte[] bVal3 = readBytesFromFile();
            fis = new ByteArrayInputStream(bVal3);   		   		
    		ks.load(fis, keyStorePassword.toCharArray());    		
        } finally {
        	 if (fis != null) {
                 try {
                	 fis.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
        }
        return ks;
	}
	
    private static byte[] readBytesFromFile() {
        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;
        try {        
//        	Date fecha2020 = new Date();
//        	Date fecha2019 = LogDateUtil.stringToDate("2020/04/04",LogDateUtil.FORMATO_FECHA_YYYY_MM_DD);
        	String nombreJDKCertificado = Constantes.OSE_JKS_CERTIFICADO;
//        	if(LogDateUtil.esFechaMayor(fecha2019,fecha2020))
//        		nombreJDKCertificado = Constantes.OSE_JKS_CERTIFICADO_2019;
        	String rutaDir = Constantes.DIR_AXTEROID_OSE;            
            //String rutaFile = (new StringBuilder(rutaDir)).append(OseConstantes.DIR_JKS).append(OseConstantes.OSE_JKS_CERTIFICADO).toString();  
            String rutaFile = (new StringBuilder(rutaDir)).append(Constantes.DIR_JKS).append(nombreJDKCertificado).toString();           
            log.info("rutaFile: {}",rutaFile);
            File file = new File(rutaFile);
            bytesArray = new byte[(int) file.length()];
            String certificado = new String(bytesArray);
            //log.info("certificado: {}",certificado);
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);
        } catch (IOException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("readBytesFromFile Exception \n"+errors);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytesArray;
    }	
}
