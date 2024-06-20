package com.axteroid.ose.server.avatar.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.security.KeyStore;
import java.util.Date;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.TrustManagerUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.ubl21.sign.impl.SignDocumentCdrKeyStore;

public class DownloadUtil {
	private static final Logger log = LoggerFactory.getLogger(DownloadUtil.class);
	
	public static void descargaHTTPS(String rutaSUNAT, String rutaOseServer){
		File f = new File(rutaOseServer);
	    try{
	    	log.info("descargaHTTPS --> "+rutaOseServer);
	    	URL kitap = new URL(rutaSUNAT);
	    	org.apache.commons.io.FileUtils.copyURLToFile(kitap, f);   
	    }catch(Exception e){
	    	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("descargaHTTPS Exception \n"+errors);  
	    }
	}
	
	public static void descargaHTTPS_SSL(String rutaSUNAT, String rutaOseServer){
	    try{
	    	log.info("descargaHTTPS_SSL --> "+rutaSUNAT+ " \n rutaOseServer --> "+rutaOseServer);			
	    	String privateKeyPassword = Constantes.OSE_JKS_KeyPassword; 	
	    	KeyStore ks = SignDocumentCdrKeyStore.loadStore();
	    	SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(ks, privateKeyPassword.toCharArray()).build();    		
	    	HttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).build();
	    	HttpResponse response = httpClient.execute(new HttpGet(rutaSUNAT));
	    	InputStream is = response.getEntity().getContent();
	    	FileOutputStream fos = new FileOutputStream(new File(rutaOseServer));
	    	int read = 0;
	    	byte[] buffer = new byte[32768];
	    	while( (read = is.read(buffer)) > 0) {
	    	  fos.write(buffer, 0, read);
	    	}
	    	fos.close();
	    	is.close();	    		    		    	
	    }catch(Exception e){
	    	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("descargaHTTPS_SSL Exception \n"+errors);  
	    }
	}	
		
	public static void descargaFTPSUNAT(String rutaSUNAT){		
		try { 
			log.info("descargaFTPSUNAT iniciada ... "+String.valueOf(new Date()));
		    KeyStore ks = SignDocumentCdrKeyStore.loadStore();
		    X509TrustManager defaultTrustManager = TrustManagerUtils.getDefaultTrustManager(ks);
		    String protocol = "SSL";
		    FTPSClient client = new FTPSClient(protocol, true);
		    client.setTrustManager(defaultTrustManager);
		    client.addProtocolCommandListener(new PrintCommandListener(new  PrintWriter(System.out)));		    
		    log.info("**** Connect to host ****"+rutaSUNAT);		    
		    client.connect(rutaSUNAT);//Here I get an Exception			
			log.info("descargaFTPSUNAT finalizada ... "+String.valueOf(new Date()));
		} catch (Exception e){
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("descargaFTPSUNAT Exception \n"+errors);  
		}
	}
}
