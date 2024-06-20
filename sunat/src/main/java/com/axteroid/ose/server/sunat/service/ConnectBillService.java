package com.axteroid.ose.server.sunat.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

//import org.apache.cxf.configuration.jsse.TLSClientParameters;
//import org.apache.cxf.configuration.security.FiltersType;
//import org.apache.cxf.endpoint.Client;
//import org.apache.cxf.frontend.ClientProxy;
//import org.apache.cxf.interceptor.LoggingInInterceptor;
//import org.apache.cxf.interceptor.LoggingOutInterceptor;
//import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
//import org.apache.cxf.transport.http.HTTPConduit;
//import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
//import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.exception.JavaParserExceptions;
import com.axteroid.ose.server.tools.exception.WebserviceConfigurationException;

import pe.gob.sunat.service.BillServiceSunat;

public class ConnectBillService {
	private static final Logger log = LoggerFactory.getLogger(ConnectBillService.class);
	private String curentURLService;
	private String usuario;
	private StringWriter errors = new StringWriter();
	private String rutaAlmCert = "";
	private String rutaALMC = Constantes.DIR_AXTEROID_OSE+Constantes.DIR_JKS;
	
	public ConnectBillService(String usuarioSol, String curentURLService, String rutaALMC){
		this.curentURLService = curentURLService;
		this.usuario = usuarioSol;
		this.rutaALMC = rutaALMC;
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
	}
		
//	public BillServiceSunat initWebServiceSunat(int ind) {
//  		log.info("initWebService ind: {} | {} ",ind, curentURLService);
//  		try{
//	  		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean(); 		
//	  		Map<String, Object> props = new HashMap<String, Object>();
//	  		props.put("mtom-enabled", Boolean.FALSE);
//	  		factory.setProperties(props);    
//	  		factory.setAddress(curentURLService);
//	  		factory.getInInterceptors().add(new LoggingInInterceptor());
//	  		factory.getOutInterceptors().add(new LoggingOutInterceptor());    
//	  		factory.setServiceClass(BillServiceSunat.class);    
//	  		BillServiceSunat billService = (BillServiceSunat)factory.create();   
//	  		FiltersType filter = new FiltersType();
//	  		filter.getInclude().add(".*_EXPORT_.*");
//	  		filter.getInclude().add(".*_EXPORT1024_.*");
//	  		filter.getInclude().add(".*_WITH_DES_.*");
//	  		filter.getInclude().add(".*_WITH_NULL_.*");
//	  		filter.getExclude().add(".*_DH_anon_.*");      		
//  			TLSClientParameters tlsParams = new TLSClientParameters();
//  			tlsParams.setDisableCNCheck(true);
//  			if (ind == 1) {
//  				tlsParams.setCipherSuites(Collections.singletonList("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256"));
//  			} else {
//  				tlsParams.setSecureSocketProtocol("TLSv1.2");
//  			}     
//  			tlsParams.setTrustManagers(this.getTrustManagers());
//  			tlsParams.setCipherSuitesFilter(filter);     
//  			this.configureSSLOnTheClient(billService, usuario, tlsParams);
//  			return billService;
//  		} catch (WebserviceConfigurationException e) {		
//  			e.printStackTrace(new PrintWriter(errors));
//  			log.error("initWebServiceSunat WebserviceConfigurationException \n"+errors);
//  			throw new RuntimeException(e);
//  		} catch (Exception e) {		
//  			JavaParserExceptions.getParseException(e, "initWebServiceSunat Exception");  		
//		}   
//  		return null;
//	}
	
	private TrustManager[] getTrustManagers() throws WebserviceConfigurationException {
		try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            String trustpass = "changeit";
            rutaAlmCert = this.getRutaAlmCert();
            //log.info("rutaAlmCert: "+rutaAlmCert);
            InputStream inputStream = new FileInputStream(rutaAlmCert);
			keyStore.load(inputStream, trustpass.toCharArray());			
			TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustFactory.init(keyStore);
			TrustManager tm[] = trustFactory.getTrustManagers();
			inputStream.close();
			return tm;
        } catch(KeyStoreException kse) {
            System.out.println((new StringBuilder()).append("Security configuration failed with the following: ").append(kse.getCause()).toString());
            throw new WebserviceConfigurationException("KeyStoreException: ", kse);
        } catch(NoSuchAlgorithmException nsa) {
            System.out.println((new StringBuilder()).append("Security configuration failed with the following: ").append(nsa.getCause()).toString());
            throw new WebserviceConfigurationException("NoSuchAlgorithmException: ", nsa);
        } catch(FileNotFoundException fnfe) {
            System.out.println((new StringBuilder()).append("Security configuration failed with the following: ").append(fnfe.getCause()).toString());
            throw new WebserviceConfigurationException("FileNotFoundException: ", fnfe);
        } catch(CertificateException ce) {
            System.out.println((new StringBuilder()).append("Security configuration failed with the following: ").append(ce.getCause()).toString());
            throw new WebserviceConfigurationException("CertificateException: ", ce);
        } catch(IOException ioe) {
            System.out.println((new StringBuilder()).append("Security configuration failed with the following: ").append(ioe.getCause()).toString());
            throw new WebserviceConfigurationException("IOException: ", ioe);
        } catch(Exception e) {
            if(e instanceof WebserviceConfigurationException) {
                throw (WebserviceConfigurationException)e;
            } else {
                System.out.println((new StringBuilder()).append("Security configuration failed with the following: ").append(e.getCause()).toString());
                throw new WebserviceConfigurationException("Exception: ", e);
            }
        }
    }	
	
//	  private void configureSSLOnTheClient(Object c, String usuario, TLSClientParameters tlsParams) throws WebserviceConfigurationException {
//		    Client client = ClientProxy.getClient(c);
//		    HTTPConduit httpConduit = (HTTPConduit)client.getConduit();    
//		    HTTPClientPolicy policy = new HTTPClientPolicy();
//		    policy.setConnectionTimeout(30000L);
//		    policy.setReceiveTimeout(180000L);
//		    policy.setAllowChunking(false);
//		    httpConduit.setClient(policy);    
//		    httpConduit.setTlsClientParameters(tlsParams);
//		    WSS4JOutInterceptor wsOut = UserNamePasswordInterceptor(usuario);
//		    client.getEndpoint().getOutInterceptors().add(wsOut);
//	  }
//	  
//	  private WSS4JOutInterceptor UserNamePasswordInterceptor(String usuario)  {
//		    Map<String, Object> outProps = new HashMap<String, Object>();
//		    outProps.put("action", "UsernameToken");    
//		    outProps.put("user", usuario);    
//		    outProps.put("passwordType", "PasswordText");    
//		    outProps.put("passwordCallbackClass", ClientPasswordCallback.class.getName());    
//		    WSS4JOutInterceptor wsOut = new WSS4JOutInterceptor(outProps);
//		    return wsOut;
//	  }
//	  
	  private String getRutaAlmCert() {
		  return (new StringBuilder()).append(rutaALMC).append("truststore.jks").toString();	   
	  }
		
	  public static class TrustAllX509TrustManager implements X509TrustManager {
		  private static final X509Certificate[] acceptedIssuers = new X509Certificate[0];
		  public TrustAllX509TrustManager() {}
		  public void checkClientTrusted(X509Certificate[] chain, String authType) {}
		  public void checkServerTrusted(X509Certificate[] chain, String authType) {}
		  public X509Certificate[] getAcceptedIssuers() {
			  return acceptedIssuers;
		  }
	  }

	  public static class FActuraElectronicaTrustManager  implements X509TrustManager  {
		  private static final X509Certificate[] acceptedIssuers = new X509Certificate[0];
		  public FActuraElectronicaTrustManager() {}
		  public void checkClientTrusted(X509Certificate[] chain, String authType) {}
		  public void checkServerTrusted(X509Certificate[] chain, String authType) {}
		  public X509Certificate[] getAcceptedIssuers(){
			  return acceptedIssuers;
		  }
	  }  		
}
