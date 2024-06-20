package com.axteroid.ose.server.apirest.sunat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoJBossOsePropertiesEnum;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.axteroid.ose.server.tools.xml.UblResponseSunat;

public class ConsultaSunat {
	private static Logger log = LoggerFactory.getLogger(ConsultaSunat.class);
	private static final HttpClient client = HttpClients.createDefault();
	private static String consulta = DirUtil.getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_WS_OLITWSCONSCPEGEM_CONSULTA.getCodigo());
	private static String urlConsultaSunat = DirUtil.getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_WS_OLITWSCONSCPEGEM_CONSULTA_URL.getCodigo());
	
	public static Map<String, String> lookupCDR(String ruc, String tipo, String serie, String secuencial) {
		Map<String,String> mapResult = new HashMap<String, String>();
		if(consulta.equals(Constantes.CONTENT_FALSE))
    		return mapResult;
		if(!(tipo.equals(Constantes.SUNAT_FACTURA) ||
				tipo.equals(Constantes.SUNAT_NOTA_CREDITO)	||
				tipo.equals(Constantes.SUNAT_NOTA_DEBITO))) 
			return mapResult;
		//log.info("serie.substring(0,1): "+serie.substring(0,1));
		if(!serie.substring(0,1).equals(Constantes.SUNAT_SERIE_F))
			return mapResult;
						
		HttpPost post = new HttpPost(urlConsultaSunat);
		String entityModel;
		try {
			//entityModel = IOUtils.resourceToString("/req/request.txt", Charset.forName("UTF-8"));
			entityModel = IOUtils.resourceToString("/req/request.txt", StandardCharsets.UTF_8);
			entityModel = entityModel.replace("RUC", ruc).replace("TIPO", tipo).replace("SERIE", serie).replace("SECUENCIAL", secuencial);				
			HttpEntity ent = new StringEntity(entityModel, ContentType.TEXT_XML);		
			post.setEntity(ent);
			//System.out.println("post.getURI(): "+post.getURI());			
			int espera = 1000;
		    RequestConfig requestConfig = RequestConfig.custom()
		    	      .setConnectionRequestTimeout(espera)
		    	      .setConnectTimeout(espera)
		    	      .setSocketTimeout(espera)
		    	      .build();
		    try (CloseableHttpClient httpClient = HttpClients.custom()
		    	      .setDefaultRequestConfig(requestConfig)
		    	      .build()) { 
		    	try {			
	    		
					HttpResponse resp = httpClient.execute(post);
					Document doc = null;
					//log.info("resp.getStatusLine(): "+resp.getStatusLine());
					//System.out.println("resp.getStatusLine(): "+resp.getStatusLine());
					if(resp.getStatusLine().getStatusCode()==200 || resp.getStatusLine().getStatusCode()==500) {				
						InputStream is =  resp.getEntity().getContent();				
						DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
						dbf.setNamespaceAware(true);				
						DocumentBuilder db =  dbf.newDocumentBuilder();				
						doc = db.parse(is);							
					}
					if(doc!=null) {
						//log.info("doc.getTextContent(): "+doc.getTextContent() );
						String prefix = doc.lookupPrefix("http://schemas.xmlsoap.org/soap/envelope/");
						NodeList nl =  doc.getElementsByTagName(prefix+":Body");
						if(nl.getLength()>0) {
							Node n = nl.item(0);
							Node f = n.getFirstChild();
							//log.info("f.getNodeName(): "+f.getNodeName()+" f.getNodeValue(): - "+f.getNodeValue() );
							//System.out.println("f.getNodeName(): "+f.getNodeName()+" f.getNodeValue(): - "+f.getNodeValue() );
							if(f.getNodeName().endsWith("Fault")) {
								NodeList hijos = f.getChildNodes();					
								for(int i=0;i<hijos.getLength();i++) {
									Node hijo = hijos.item(i);
									mapResult.put(hijo.getNodeName(),hijo.getTextContent());						
								}					
							}else if(f.getNodeName().endsWith("getStatusCdrResponse")) {
								Node statusCdr = f.getFirstChild();
								if(statusCdr.getNodeName().endsWith("statusCdr")) {
									NodeList hijos = statusCdr.getChildNodes();
									for(int i=0;i<hijos.getLength();i++) {
										Node hijo = hijos.item(i);
										mapResult.put(hijo.getNodeName(),hijo.getTextContent());							
									}
								}else {
									log.info("Error no tiene statusCdr");
									return mapResult;
								}						
							}
						}
					}			
			
			    } catch ( Exception e) {		
					log.error("lookupCDR: "+e.getMessage());
					//System.out.println("consumeResponse: "+e.getMessage());
			    } 	    	
		    } catch ( Exception e) {		
				log.error("CloseableHttpClient: "+e.getMessage());
				//System.out.println("CloseableHttpClient: "+e.getMessage());
		    } 			
			
			Map<String,String> status = null;
			boolean cdr = false;
			for(Entry<String,String> entry : mapResult.entrySet()) {
				//log.info("entry.getKey(): "+entry.getKey()+": "+entry.getValue());
				//System.out.println("entry.getKey(): "+entry.getKey()+" - "+entry.getValue());
				if(entry.getKey().equals("content")) {
					if(!entry.getValue().isEmpty()) {
						status = UblResponseSunat.readStatusFromCDR(entry.getValue());	
						cdr = true;
					}
				}
			}
			if(cdr)
				mapResult.putAll(status);		
		} catch (IOException e) {			
			log.info("lookupCDR IOException: "+e.getMessage());
		} catch (UnsupportedOperationException e) {			
			log.error("lookupCDR Exception: "+e.getMessage());
		} 
		return mapResult;
	}

	public static Map<String, String> lookupCDR_1(String ruc, String tipo, String serie, String secuencial) {
		Map<String,String> mapResult = new HashMap<String, String>();
		if(consulta.equals(Constantes.CONTENT_FALSE))
    		return mapResult;
		HttpPost post = new HttpPost(urlConsultaSunat);
		String entityModel;
		try {
			entityModel = IOUtils.resourceToString("/req/request.txt", Charset.forName("UTF-8"));
			entityModel = entityModel.replace("RUC", ruc).replace("TIPO", tipo).replace("SERIE", serie).replace("SECUENCIAL", secuencial);				
			HttpEntity ent = new StringEntity(entityModel, ContentType.TEXT_XML);		
			post.setEntity(ent);
			//System.out.println("post.getURI(): "+post.getURI());
			HttpResponse resp = client.execute(post);
			Document doc = null;
			//log.info("resp.getStatusLine(): "+resp.getStatusLine());
			//System.out.println("resp.getStatusLine(): "+resp.getStatusLine());
			if(resp.getStatusLine().getStatusCode()==200 || resp.getStatusLine().getStatusCode()==500) {				
				InputStream is =  resp.getEntity().getContent();				
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setNamespaceAware(true);				
				DocumentBuilder db =  dbf.newDocumentBuilder();				
				doc = db.parse(is);							
			}
			if(doc!=null) {
				//log.info("doc.getTextContent(): "+doc.getTextContent() );
				String prefix = doc.lookupPrefix("http://schemas.xmlsoap.org/soap/envelope/");
				NodeList nl =  doc.getElementsByTagName(prefix+":Body");
				if(nl.getLength()>0) {
					Node n = nl.item(0);
					Node f = n.getFirstChild();
					//log.info("f.getNodeName(): "+f.getNodeName()+" f.getNodeValue(): - "+f.getNodeValue() );
					//System.out.println("f.getNodeName(): "+f.getNodeName()+" f.getNodeValue(): - "+f.getNodeValue() );
					if(f.getNodeName().endsWith("Fault")) {
						NodeList hijos = f.getChildNodes();					
						for(int i=0;i<hijos.getLength();i++) {
							Node hijo = hijos.item(i);
							mapResult.put(hijo.getNodeName(),hijo.getTextContent());						
						}					
					}else if(f.getNodeName().endsWith("getStatusCdrResponse")) {
						Node statusCdr = f.getFirstChild();
						if(statusCdr.getNodeName().endsWith("statusCdr")) {
							NodeList hijos = statusCdr.getChildNodes();
							for(int i=0;i<hijos.getLength();i++) {
								Node hijo = hijos.item(i);
								mapResult.put(hijo.getNodeName(),hijo.getTextContent());							
							}
						}else {
							log.info("Error no tiene statusCdr");
							return mapResult;
						}						
					}
				}
			}
			Map<String,String> status = null;
			boolean cdr = false;
			for(Entry<String,String> entry : mapResult.entrySet()) {
				//log.info("entry.getKey(): "+entry.getKey()+": "+entry.getValue());
				//System.out.println("entry.getKey(): "+entry.getKey()+" - "+entry.getValue());
				if(entry.getKey().equals("content")) {
					if(!entry.getValue().isEmpty()) {
						status = UblResponseSunat.readStatusFromCDR(entry.getValue());	
						cdr = true;
					}
				}
			}
			if(cdr)
				mapResult.putAll(status);		
		} catch (IOException e) {			
			log.info("lookupCDR IOException: "+e.getMessage());
		} catch (UnsupportedOperationException e) {			
			log.error("lookupCDR Exception: "+e.getMessage());
		} catch (ParserConfigurationException e) {			
			log.error("lookupCDR Exception: "+e.getMessage());
		} catch (SAXException e) {			
			log.error("lookupCDR Exception: "+e.getMessage());
		}
		return mapResult;
	}	
	
    public static boolean getOlitwsconscpegem(Long longRuc, String tipoDocRefPri, String nroDocRefPri) {
    	boolean cpe = false;
		if(consulta.equals(Constantes.CONTENT_FALSE))
    		return cpe;
//		String ambiente = DirUtil.getAmbiente();     
//		log.info("ambiente: {}",ambiente);
//    	if((ambiente.equals(OseConstantes.DIR_AMB_POST)) || 
//    			(ambiente.equals(OseConstantes.DIR_AMB_TEST)) || 
//    			(ambiente.equals(OseConstantes.DIR_AMB_DESA)))
//    		return true;   	
    	try { 		
			String [] docRefPri = nroDocRefPri.split("-");
			String ruc = String.valueOf(longRuc);
			Map<String, String> getStatus = ConsultaSunat.lookupCDR(ruc,tipoDocRefPri,docRefPri[0],docRefPri[1]);			
			if((getStatus!=null) && (getStatus.get("responseCode")!= null) && 
					(getStatus.get("responseCode").equals(Constantes.SUNAT_RESPUESTA_0)))
				cpe = true;
			//for(Entry<String,String> entry : getStatus.entrySet()) {
				//log.info("key: "+entry.getKey()+" value: "+entry.getValue());
				//System.out.println("key: "+entry.getKey()+" value: "+entry.getValue());
			//}
			log.info("getOlitwsconscpegem: "+longRuc+"-"+ tipoDocRefPri+"-"+docRefPri[0]+"-"+docRefPri[1]+" | "+cpe);
		} catch (Exception e) {
			log.error("getOlitwsconscpegem Exception: "+e.getMessage());
		}
    	return cpe;
	}
  
}
