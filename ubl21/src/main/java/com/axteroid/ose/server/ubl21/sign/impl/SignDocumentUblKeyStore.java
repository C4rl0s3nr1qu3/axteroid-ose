package com.axteroid.ose.server.ubl21.sign.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import com.axteroid.ose.server.tools.constantes.Constantes;

public class SignDocumentUblKeyStore {

	public static KeyStore loadStore(String keyStorePassword, String certicate) throws KeyStoreException,  IOException, GeneralSecurityException {
		KeyStore ks = KeyStore.getInstance(Constantes.OSE_JKS_TYPE);
		InputStream fis = null; 
        try {            
            byte[] bVal3 = readBytesFromFile(certicate);
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
	
	
    private static byte[] readBytesFromFile(String certicate) {
        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;
        try {        
        	//String rutaDir = DirUtil.getRutaDirectory();            
            //String rutaFile = (new StringBuilder(rutaDir)).append(OseConstantes.DIR_JKS_UBL).append(certicate).toString();           
        	String rutaFile = (new StringBuilder(Constantes.DIR_AVATAR))
    				.append(Constantes.DIR_AVATAR_CONFIG_JKSUBL)
    				.append(certicate).toString();
        	File file = new File(rutaFile);
            bytesArray = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);
        } catch (IOException e) {
            e.printStackTrace();
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
