package com.axteroid.ose.server.apirest.list;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.bean.CertificadoEmisor;
import com.axteroid.ose.server.tools.bean.ComprobantePago;
import com.axteroid.ose.server.tools.bean.ComprobanteRango;
import com.axteroid.ose.server.tools.bean.Contribuyente;
import com.axteroid.ose.server.tools.bean.ContribuyenteAsociadoEmisor;
import com.axteroid.ose.server.tools.bean.PadronContribuyente;
import com.axteroid.ose.server.tools.bean.Parametro;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoJBossOsePropertiesEnum;
import com.axteroid.ose.server.tools.exception.ConsumeExceptionRest;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ListSunatRest {
	private static Logger log = LoggerFactory.getLogger(ListSunatRest.class);
	private static String urlSunatListRest = DirUtil.getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_LIST_CONSULTA_URL.getCodigo());
	private static final Gson gson = new GsonBuilder().create();	
	private ListSunatRest() {}
	
	public static Optional<Contribuyente> buscaContribuyente(String RUC) {
		try {		
			HttpGet httpGet = new HttpGet(urlSunatListRest + "/contribuyente/" + RUC);
			Optional<String> resp = consumeRequest(httpGet,2000);				
			if((resp!=null) && (resp.isPresent()))
				return Optional.ofNullable(gson.fromJson(resp.get(), Contribuyente.class));	
		} catch (ConsumeExceptionRest e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));			
			log.error("buscaContribuyente RestConsumeException \n"+errors);
		}catch (Exception e) {

			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));			
			log.error("buscaContribuyente Exception \n"+errors);
		}			
		return Optional.ofNullable(null);
	}
	
	public static Optional<ComprobantePago> buscaComprobantePagoElectronico(String RUC, String tipo, String serie, Long sec){		
		log.info(urlSunatListRest+ "/cpe/" + RUC+"-"+tipo+"-"+serie+"-"+sec);
		//System.out.println(OseConstantes.OSE_CONSULTAS_SUNAT_LISTADOS_URL+ "/cpe/" + RUC+"-"+tipo+"-"+serie+"-"+sec);
		try {
			HttpGet httpGet = new HttpGet(urlSunatListRest + "/cpe/" + RUC+"/"+tipo+"/"+serie+"/"+sec);		
			Optional<String> resp = consumeRequest(httpGet,1000);	
			if((resp!=null) && (resp.isPresent())) {
				//log.info("resp.get(): {}",resp.get());				
				return Optional.ofNullable(gson.fromJson(resp.get(), ComprobantePago.class));
			}
		} catch (ConsumeExceptionRest e) {	
			log.error("buscaComprobantePagoElectronico RestConsumeException: "+e.getMessage());
		}catch (Exception e) {		
			log.error("buscaComprobantePagoElectronico Exception: "+e.getMessage());
		}			
		return Optional.ofNullable(null);	
	}	

	public static Optional<Parametro> buscaParametro(String codigoParametro, String codigoArgumento) throws ConsumeExceptionRest{		
		HttpGet httpGet = new HttpGet(urlSunatListRest + "/parametro/" + codigoParametro+"/"+codigoArgumento);		
		Optional<String> resp = consumeRequest(httpGet,2000);		
		if((resp!=null) && (resp.isPresent()))
			return Optional.ofNullable(gson.fromJson(resp.get(), Parametro.class));
		return Optional.ofNullable(null);	
	}
	
	public static Optional<ComprobanteRango> buscaComprobantePagoFisico(String RUC, String tipo, String serie, String sec) throws ConsumeExceptionRest{
		HttpGet httpGet = new HttpGet(urlSunatListRest + "/cpf/" + RUC+"/"+tipo+"/"+serie+"/"+sec);		
		Optional<String> resp = consumeRequest(httpGet,2000);		
		if(resp.isPresent()) {
			return Optional.ofNullable(gson.fromJson(resp.get(), ComprobanteRango.class));
		}else {
			return Optional.ofNullable(null);
		}		
	}
	
	public static Optional<ComprobanteRango> buscaComprobanteContingencia(String RUC, String tipo, String serie, String sec) throws ConsumeExceptionRest{
		HttpGet httpGet = new HttpGet(urlSunatListRest + "/contingencia/" + RUC+"/"+tipo+"/"+serie+"/"+sec);		
		Optional<String> resp = consumeRequest(httpGet,2000);		
		if(resp.isPresent()) {
			return Optional.ofNullable(gson.fromJson(resp.get(), ComprobanteRango.class));
		}else {
			return Optional.ofNullable(null);
		}		
	}		

	public static Optional<CertificadoEmisor> buscaCertificadoEmisor(String RUC,  String caId, String serieId) throws ConsumeExceptionRest{
		HttpGet httpGet = new HttpGet(urlSunatListRest + "/cerfificado/" + RUC+"/"+caId+"/"+serieId);		
		Optional<String> resp = consumeRequest(httpGet,2000);		
		if(resp.isPresent()) {
			return Optional.ofNullable(gson.fromJson(resp.get(), CertificadoEmisor.class));
		}else {
			return Optional.ofNullable(null);
		}		
	}	
	
	public static Optional<ContribuyenteAsociadoEmisor> buscaContribuyenteAsociado(String RUC,  String RUCASOCIADO, String tipo) throws ConsumeExceptionRest{
		HttpGet httpGet = new HttpGet(urlSunatListRest + "/contribuyenteAsociado/" + RUC+"/"+RUCASOCIADO+"/"+tipo);		
		Optional<String> resp = consumeRequest(httpGet,2000);		
		if(resp.isPresent()) {
			return Optional.ofNullable(gson.fromJson(resp.get(), ContribuyenteAsociadoEmisor.class));
		}else {
			return Optional.ofNullable(null);
		}		
	}	
	
	public static Optional<PadronContribuyente> buscaEmisorPadron(String RUC,  String padron) throws ConsumeExceptionRest{
		HttpGet httpGet = new HttpGet(urlSunatListRest + "/padronEmisor/" + RUC+"/"+padron);		
		Optional<String> resp = consumeRequest(httpGet,2000);		
		if(resp.isPresent()) {
			return Optional.ofNullable(gson.fromJson(resp.get(), PadronContribuyente.class));
		}else {
			return Optional.ofNullable(null);
		}		
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
				StringWriter errors = new StringWriter();				
				e.printStackTrace(new PrintWriter(errors));
				log.error("consumeRequest (a) Exception \n"+errors);				
		    } 	    	
	    } catch ( Exception e) {			
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("consumeRequest (b) Exception \n"+errors);
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
				//log.info("wri.getBuffer().toString(): {}",wri.getBuffer().toString());
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
			//log.error("consumeResponse: "+e.getMessage());			
			//System.out.println("CloseableHttpResponse: "+e.getMessage());		
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("consumeResponse Exception \n"+errors);
	    } 	
		return Optional.ofNullable(Constantes.SUNAT_RESPUESTA_0098);
	}
}
