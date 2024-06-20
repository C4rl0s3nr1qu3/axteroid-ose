package com.axteroid.ose.server.apirest.chile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.bean.ApiChileResponse;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoJBossOsePropertiesEnum;
import com.axteroid.ose.server.tools.exception.ConsumeExceptionRest;
import com.axteroid.ose.server.tools.exception.JavaParserExceptions;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ApiChile {
	private static Logger log = LoggerFactory.getLogger(ApiChile.class);
	private static final Gson gson = new GsonBuilder().create();	
	
	//private static String updateStatusApiChile = "0";
	//private static String urlUpdateStatusApiChile = "https://api.axteroid.com/irs/sunat/update_status";
	//private static String urlUpdateStatusApiChile = "https://api-test.axteroid.com/irs/sunat/update_status";
	private static String updateStatusApiChile = DirUtil.getJBossPropertiesValue(TipoJBossOsePropertiesEnum.AXTEROID_OSE_APICHILE_UPDATESTATUS.getCodigo());
	private static String urlUpdateStatusApiChile = DirUtil.getJBossPropertiesValue(TipoJBossOsePropertiesEnum.AXTEROID_OSE_APICHILE_UPDATESTATUS_URL.getCodigo());
	
	private ApiChile() {}
	
	public static Optional<ApiChileResponse> updateStatusApiChile(Documento documento) {		
		String urlEnvio = urlUpdateStatusApiChile+"/?tax_id="+documento.getRucEmisor()+"&prefix="+documento.getSerie()+"&number="+documento.getNumeroCorrelativo();
		if(updateStatusApiChile.equals(Constantes.CONTENT_FALSE))
    		return null;
		if(!(documento.getTipoDocumento().equals(Constantes.SUNAT_GUIA_REMISION_REMITENTE) ||
				documento.getTipoDocumento().equals(Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA))) 
			return null;
		log.info("updateStatusApiChile : {}",urlEnvio);
		try {
			HttpGet httpGet = new HttpGet(urlEnvio);
			httpGet.setHeader(HttpHeaders.CONTENT_TYPE,"application/json");
			//httpGet.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + sunatToken.getAccess_token());	
			//httpPost.setEntity(new StringEntity(jsonEntity, ContentType.APPLICATION_JSON));				
			Optional<String> resp = consumeRequest(httpGet,50000);		
			log.info("updateStatusApiChile resp: {}",resp!=null ? resp.get() : "");
			if((resp!=null) &&(resp.isPresent())) {  					
				ApiChileResponse apiChileResponse = gson.fromJson(resp.get(), ApiChileResponse.class);	
				return Optional.ofNullable(apiChileResponse);	
			}	
		} catch (ConsumeExceptionRest e) {	
			JavaParserExceptions.getParseException(e, "updateStatusApiChile ConsumeExceptionRest");
//			log.error("consultarTicket RestConsumeException: "+e.getMessage());
		}catch (Exception e) {
			JavaParserExceptions.getParseException(e, "updateStatusApiChile");
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));			
//			log.error("consultarTicket Exception: "+e.getMessage());
		}			
		return Optional.ofNullable(null);
	}			

	private static Optional<String> consumeRequest(HttpUriRequest req, int espera) throws ConsumeExceptionRest {
	    RequestConfig requestConfig = RequestConfig.custom()
	    	      .setConnectionRequestTimeout(espera)
	    	      .setConnectTimeout(espera)
	    	      .setSocketTimeout(espera)
	    	      .build();
	    try (CloseableHttpClient httpClient = HttpClients.custom()
	    	      .setDefaultRequestConfig(requestConfig)
	    	      .build()) { 
	    	try {
	    		Optional<String> resp = consumeResponse(httpClient, req);
	    		if(resp.isPresent())
	    			if(resp.get().equals(Constantes.SUNAT_RESPUESTA_0098))
	    				resp = consumeResponse(httpClient, req);
	    		return resp;
		    } catch ( Exception e) {		
				log.error("consumeResponse: "+e.getMessage());
		    } 	    	
	    } catch ( Exception e) {		
			log.error("CloseableHttpClient: "+e.getMessage());
	    } 
	    return Optional.ofNullable(null);
	}	
	
	private static Optional<String> consumeResponse(CloseableHttpClient httpClient, HttpUriRequest req) 
			throws ConsumeExceptionRest {
		try (CloseableHttpResponse resp = httpClient.execute(req)){ 
			if (resp.getStatusLine().getStatusCode() == 200) {
				InputStream is = resp.getEntity().getContent();
				StringWriter wri = new StringWriter();
				IOUtils.copy(is, wri, StandardCharsets.UTF_8);
				is.close();
				wri.close();
				return Optional.of(wri.getBuffer().toString());
			} else if (resp.getStatusLine().getStatusCode() == 404) {
				return Optional.ofNullable(null);
			} else if (resp.getStatusLine().getStatusCode() == 400) {
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				resp.getEntity().writeTo(bout);
				bout.close();
				throw new ConsumeExceptionRest(new String(bout.toByteArray(), StandardCharsets.UTF_8));
			} else {
				throw new ConsumeExceptionRest("Se respondio statusCode="+resp.getStatusLine().getStatusCode());
			}
	    } catch ( Exception e) {				
			log.error("CloseableHttpResponse: "+e.getMessage());	
	    } 	
		return Optional.ofNullable(Constantes.SUNAT_RESPUESTA_0098);
	}
	
    public static void main( String[] args ){
    	//ApiChile apiChile = new ApiChile();
    	
    	Documento documento = new Documento();
    	// ByS
    	documento.setRucEmisor(20384891943L);
    	documento.setTipoDocumento("09");
    	documento.setSerie("T973");
    	documento.setNumeroCorrelativo("9");
    	// DECO
    	//documento.setRucEmisor(20100061474L);
    	//documento.setTipoDocumento("09");
    	//documento.setSerie("T003");
    	//documento.setNumeroCorrelativo("89213");
    	//documento.setCorrelativo(0);
    	ApiChile.updateStatusApiChile(documento);
    	
    	
    } 
}
