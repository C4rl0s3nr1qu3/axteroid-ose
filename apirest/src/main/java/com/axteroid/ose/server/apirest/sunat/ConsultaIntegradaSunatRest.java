package com.axteroid.ose.server.apirest.sunat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.bean.SunatRequest;
import com.axteroid.ose.server.tools.bean.SunatTokenResponse;
import com.axteroid.ose.server.tools.bean.SunatBeanResponse;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.exception.ConsumeExceptionRest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConsultaIntegradaSunatRest {
	private static Logger log = LoggerFactory.getLogger(ConsultaIntegradaSunatRest.class);
	private static final Gson gson = new GsonBuilder().create();	
	private ConsultaIntegradaSunatRest() {}
	
	public static Optional<SunatBeanResponse> validarComprobante(SunatRequest sunatConsulta){
		SunatTokenResponse sunatToken =  buscaTokenCpe();
		if((sunatToken!=null) && (sunatToken.getAccess_token()!=null)) {
			log.info("getExpires_in "+sunatToken.getExpires_in());
			Optional<SunatBeanResponse> respVal = buscaCpeValidez(sunatConsulta, sunatToken);
			if((respVal!=null) && (respVal.isPresent()))
				return Optional.ofNullable(respVal.get());
		}
		return Optional.ofNullable(null);
	}
	
	public static SunatTokenResponse buscaTokenCpe() {
		SunatTokenResponse sunatToken = new SunatTokenResponse();
		try {			
			HttpPost httpPost = new HttpPost(Constantes.OSE_CONSULTAS_SUNAT_CONSULTA_INTEGRADA_TOCKEN);
			httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("grant_type","client_credentials"));
			params.add(new BasicNameValuePair("scope","https://api.sunat.gob.pe/v1/contribuyente/contribuyentes"));
			params.add(new BasicNameValuePair("client_id","e66be698-d865-43b8-a933-48f37165a2ee"));
			params.add(new BasicNameValuePair("client_secret","uESu1CqDWD/cmluxstKS5Q=="));
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			Optional<String> resp = consumeRequest(httpPost,1000);	
			if((resp!=null) &&(resp.isPresent()))  	
				sunatToken = gson.fromJson(resp.get(), SunatTokenResponse.class);
		} catch (ConsumeExceptionRest e) {		
			log.error("buscaTokenCpe RestConsumeException: "+e.getMessage());
		}catch (Exception e) {		
			log.error("buscaTokenCpe Exception "+e.getMessage());
		}			
		return sunatToken;
	}
	
	public static Optional<SunatBeanResponse> buscaCpeValidez(SunatRequest sunatConsulta, SunatTokenResponse sunatToken) {
		log.info("\n"+Constantes.OSE_CONSULTAS_SUNAT_CONSULTA_INTEGRADA_VALIDEZ+ sunatConsulta.getNumRuc()+"/validarcomprobante/"
				+ sunatConsulta.getNumRuc()+"-"+sunatConsulta.getCodComp()+"-"+sunatConsulta.getNumeroSerie()+"-"
				+sunatConsulta.getNumero()+"|"+sunatConsulta.getFechaEmision()+"|"+sunatConsulta.getMonto());

		try {
			HttpPost httpPost = new HttpPost(Constantes.OSE_CONSULTAS_SUNAT_CONSULTA_INTEGRADA_VALIDEZ + sunatConsulta.getNumRuc()+"/validarcomprobante");
			httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
			httpPost.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + sunatToken.getAccess_token());
			httpPost.addHeader("Accept","application/json");
			JSONObject payload = new JSONObject();
			payload.put("numRuc",sunatConsulta.getNumRuc());
			payload.put("codComp",sunatConsulta.getCodComp());
			payload.put("numeroSerie",sunatConsulta.getNumeroSerie());
			payload.put("numero",sunatConsulta.getNumero());
			payload.put("fechaEmision",sunatConsulta.getFechaEmision());
			payload.put("monto",sunatConsulta.getMonto());
			httpPost.setEntity(new StringEntity(payload.toString(), ContentType.APPLICATION_JSON));				
			Optional<String> resp = consumeRequest(httpPost,1000);		
			if((resp!=null) &&(resp.isPresent())) {  	
				SunatBeanResponse sunatValidar = gson.fromJson(resp.get(), SunatBeanResponse.class);		
				return Optional.ofNullable(sunatValidar);	
			}	
		} catch (ConsumeExceptionRest e) {		
			log.error("buscaCpeValidez RestConsumeException: "+e.getMessage());
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));			
			log.error("buscaCpeValidez Exception: "+e.getMessage());
		}			
		return Optional.ofNullable(null);
	}	
	
	public static Optional<SunatBeanResponse> buscaCpeConsultaIntegrada(SunatRequest sunatConsulta) {
		log.info(Constantes.OSE_CONSULTAS_SUNAT_CONSULTA_INTEGRADA+ sunatConsulta.getNumRuc()+"/"+sunatConsulta.getCodComp()
			+"/"+sunatConsulta.getNumeroSerie()+"/"+sunatConsulta.getNumero()+"/"+sunatConsulta.getFechaEmision()+"/"+sunatConsulta.getMonto());
		try {
			HttpPost httpPost = new HttpPost(Constantes.OSE_CONSULTAS_SUNAT_CONSULTA_INTEGRADA);
			httpPost.setHeader(HttpHeaders.CONTENT_TYPE,"application/json");
			JSONObject payload = new JSONObject();
			payload.put("numRuc",sunatConsulta.getNumRuc());
			payload.put("codComp",sunatConsulta.getCodComp());
			payload.put("numeroSerie",sunatConsulta.getNumeroSerie());
			payload.put("numero",sunatConsulta.getNumero());
			payload.put("fechaEmision",sunatConsulta.getFechaEmision());
			payload.put("monto",sunatConsulta.getMonto());
			httpPost.setEntity(new StringEntity(payload.toString(), ContentType.APPLICATION_JSON));			
			Optional<String> resp = consumeRequest(httpPost,1000);
			log.info("buscaCpeConsultaIntegrada resp.isPresent(): "+resp.isPresent());
			if(resp.isPresent()) { 	
				SunatBeanResponse sunatValidar = gson.fromJson(resp.get(), SunatBeanResponse.class);
				System.out.println("sunatValidar: "+sunatValidar.getSuccess());			
				return Optional.ofNullable(sunatValidar);
			}
		} catch (ConsumeExceptionRest e) {		
			log.error("buscaCpeConsultaIntegrada RestConsumeException: "+e.getMessage());
		}catch (Exception e) {		
			log.error("buscaCpeConsultaIntegrada Exception: "+e.getMessage());
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
}
