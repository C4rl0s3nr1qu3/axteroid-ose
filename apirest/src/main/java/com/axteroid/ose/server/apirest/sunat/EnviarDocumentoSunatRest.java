package com.axteroid.ose.server.apirest.sunat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.Emisor;
import com.axteroid.ose.server.tools.bean.Archivo;
import com.axteroid.ose.server.tools.bean.SunatRequest;
import com.axteroid.ose.server.tools.bean.SunatTokenResponse;
import com.axteroid.ose.server.tools.bean.SunatBeanResponseGRE;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoJBossOsePropertiesEnum;
import com.axteroid.ose.server.tools.exception.ConsumeExceptionRest;
import com.axteroid.ose.server.tools.exception.JavaParserExceptions;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.axteroid.ose.server.tools.util.ZipUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EnviarDocumentoSunatRest {
	private static Logger log = LoggerFactory.getLogger(EnviarDocumentoSunatRest.class);
	private static final Gson gson = new GsonBuilder().create();
	private static String envioComprobante = DirUtil.getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_RES_ENVIO_COMPROBANTE.getCodigo());
	private static String apiTockenEnvioComprobante = DirUtil.getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_RES_ENVIO_COMPROBANTE_TOKEN_API.getCodigo());
	private static String apiEnviarComprobante = DirUtil.getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_RES_ENVIO_COMPROBANTE_ENVIAR_API.getCodigo());
	private static String apiConsultarComprobante = DirUtil.getJBossPropertiesValue(TipoJBossOsePropertiesEnum.SUNAT_RES_ENVIO_COMPROBANTE_CONSULTAR_API.getCodigo());
	
	public static void enviarDocumentoSunat(Documento documento, SunatRequest sunatConsulta, Emisor emisor){
		log.info("enviarDocumentoSunat sunatConsulta: {} ",sunatConsulta.toString());
		if(envioComprobante.trim().equals(Constantes.CONTENT_FALSE))
			return;
		SunatTokenResponse sunatToken = buscaTokenEnvioComprobante(sunatConsulta, emisor);
		if((sunatToken!=null) && (sunatToken.getAccess_token()!=null)) {
			log.info("sunatToken: {}",sunatToken.toString());
			Optional<SunatBeanResponseGRE> optEnvioComprobante = envioComprobante(sunatConsulta, sunatToken);
			if(optEnvioComprobante!=null) {
				SunatBeanResponseGRE sunatResponseGRE = optEnvioComprobante.get();
				log.info("1)EnvioComprobante sunatResponseGRE.toString(): {}",sunatResponseGRE.toString());
				if(sunatResponseGRE.getNumTicket()!=null) {
					documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDBILL);
					documento.setErrorLog(sunatResponseGRE.getNumTicket());			
					documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_98); 	
					documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());
					if(sunatResponseGRE.getCod()!= null) {							
						documento.setErrorNumero(sunatResponseGRE.getCod());
						documento.setErrorDescripcion(sunatResponseGRE.getMsg());
						documento.setErrorLogSunat(sunatResponseGRE.getCod()+" | "+sunatResponseGRE.getMsg());
					}					
					if(sunatResponseGRE.getError()!= null) {
						documento.setErrorLogSunat(sunatResponseGRE.getError().getNumError()+" | "+sunatResponseGRE.getError().getDesError());
					}
				}else if(sunatResponseGRE.getCodRespuesta().equals(Constantes.SUNAT_RESPUESTA_99)){	
					documento.setEstado(Constantes.SUNAT_CDR_ERROR_AVATAR);	
					if(sunatResponseGRE.getCod()!= null) {							
						documento.setErrorNumero(sunatResponseGRE.getCod());
						documento.setErrorDescripcion(sunatResponseGRE.getMsg());
						documento.setRespuestaSunat(sunatResponseGRE.getCod());
						documento.setErrorLogSunat(sunatResponseGRE.getMsg());
					}	
					if(sunatResponseGRE.getError()!= null) {
						documento.setRespuestaSunat(sunatResponseGRE.getError().getNumError()); 						
						documento.setErrorLogSunat(sunatResponseGRE.getError().getDesError());		
					}
				}else {
					documento.setEstado(Constantes.SUNAT_CDR_ERROR_AVATAR);	
					if(sunatResponseGRE.getCod()!= null) {							
						documento.setErrorNumero(sunatResponseGRE.getCod());
						documento.setErrorDescripcion(sunatResponseGRE.getMsg());
						documento.setRespuestaSunat(sunatResponseGRE.getCod());
						documento.setErrorLogSunat(sunatResponseGRE.getMsg());
					}	
					if(sunatResponseGRE.getError()!= null) {
						documento.setRespuestaSunat(sunatResponseGRE.getError().getNumError()); 						
						documento.setErrorLogSunat(sunatResponseGRE.getError().getDesError());		
					}
					documento.setAdvertencia(sunatResponseGRE.getExc());
					documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());
				}				  
			}else {
				documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_SERVICIO_INHABILITADO);
				documento.setEstado(Constantes.SUNAT_CDR_ERROR_AVATAR);	
				documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());
				//documento.setCdr(new byte[0]);
			}
		}
	}
	
	public static void recuperarDocumentoSunat(Documento documento, SunatRequest sunatRequest, Emisor emisor){
		log.info("recuperarDocumentoSunat a) sunatRequest: {} ",sunatRequest.toString());
		if(envioComprobante.trim().equals(Constantes.CONTENT_FALSE))
			return;
		SunatTokenResponse sunatToken = buscaTokenEnvioComprobante(sunatRequest, emisor);
		if((sunatToken!=null) && (sunatToken.getAccess_token()!=null)) {
//			log.info("sunatToken: {}",sunatToken.toString());
			log.info("recuperarDocumentoSunat b) ticket: {} | TipoDocumento(): {} | Estado: {} | RespuestaSunat: {} | logSunat: {} | ErrorNumero: {} ",
					documento.getErrorLog(), documento.getTipoDocumento(), documento.getEstado(), 
					documento.getRespuestaSunat(), documento.getErrorLogSunat(), documento.getErrorNumero());	
			if((documento.getEstado().trim().equals(Constantes.SUNAT_CDR_ERROR_SENDBILL)) &&
					(documento.getErrorLog() != null)) {
				Optional<SunatBeanResponseGRE> optConsultarTicket = consultarTicket(documento, sunatToken);
				readDocument(documento, optConsultarTicket);	
				log.info("recuperarDocumentoSunat c) ticket: {} | TipoDocumento(): {} | Estado: {} | RespuestaSunat: {} | logSunat: {} | ErrorNumero: {} ",
						documento.getErrorLog(), documento.getTipoDocumento(), documento.getEstado(), 
						documento.getRespuestaSunat(), documento.getErrorLogSunat(), documento.getErrorNumero());					
			}
		}
	}	
	
	public static void readDocument(Documento documento, Optional<SunatBeanResponseGRE> optConsultarTicket) {
		if(optConsultarTicket!=null) {
			log.info("readDocument A) ticket: {} | TipoDocumento(): {} | Estado: {} | RespuestaSunat: {} | logSunat: {} | ErrorNumero: {} ",
					documento.getErrorLog(), documento.getTipoDocumento(), documento.getEstado(), 
					documento.getRespuestaSunat(), documento.getErrorLogSunat(), documento.getErrorNumero());	
			SunatBeanResponseGRE sunatResponseGRE = optConsultarTicket.get();
			log.info("1.A) sunatResponseGRE.toString(): {}",sunatResponseGRE.toStringRes());
			if(sunatResponseGRE.getIndCdrGenerado() != null) {
				if(sunatResponseGRE.getIndCdrGenerado().equals(Constantes.SUNAT_ESTADOCP_ACEPTADO)) {					
					byte[] arcCdr = decodeString2Byte(sunatResponseGRE.getArcCdr());
					if(arcCdr.length > 0) {
						byte[] bytes = ZipUtil.descomprimirArchivoMejorado(arcCdr);
						documento.setCdr(bytes);
						documento.setMensajeSunat(bytes); 
						documento.setRespuestaSunat(Constantes.CONTENT_TRUE);
						documento.setEstado(Constantes.SUNAT_CDR_AUTORIZADO);
					}
				}else
					documento.setRespuestaSunat(sunatResponseGRE.getCodRespuesta());
			}
										
			log.info("readDocument B) ticket: {} | TipoDocumento(): {} | Estado: {} | RespuestaSunat: {} | logSunat: {} | ErrorNumero: {} ",
					documento.getErrorLog(), documento.getTipoDocumento(), documento.getEstado(), 
					documento.getRespuestaSunat(), documento.getErrorLogSunat(), documento.getErrorNumero());		
			log.info("2.A) sunatResponseGRE.toString(): {}",sunatResponseGRE.toStringRes());
			if(documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_98)) {
				if(sunatResponseGRE.getCod()!= null) {	
					//log.info("3.A) sunatResponseGRE.getError().toString(): {}",sunatResponseGRE.getError().toString());					
					documento.setErrorNumero(sunatResponseGRE.getCod());
					documento.setErrorDescripcion(sunatResponseGRE.getMsg());
					documento.setRespuestaSunat(sunatResponseGRE.getCod());
					documento.setErrorLogSunat(sunatResponseGRE.getCod()+" "+sunatResponseGRE.getMsg());
				}
				if(sunatResponseGRE.getError()!= null) {
					//log.info("3.B) sunatResponseGRE.getError().toString(): {}",sunatResponseGRE.getError().toString());
//					documento.setErrorNumero(sunatResponseGRE.getError().getNumError());
//					documento.setErrorDescripcion(sunatResponseGRE.getError().getDesError());
					documento.setRespuestaSunat(sunatResponseGRE.getError().getNumError()); 						
					documento.setErrorLogSunat(sunatResponseGRE.getError().getDesError());	
				}
				if((sunatResponseGRE.getErrors()!= null) && (sunatResponseGRE.getErrors().size() > 0)) {
					//log.info("3.C) sunatResponseGRE.getErrors().get(0).toString(): {}",sunatResponseGRE.getErrors().get(0).toString());
//					documento.setErrorNumero(sunatResponseGRE.getErrors().get(0).getNumError());
//					documento.setErrorDescripcion(sunatResponseGRE.getErrors().get(0).getDesError());
					documento.setRespuestaSunat(sunatResponseGRE.getErrors().get(0).getNumError()); 						
					documento.setErrorLogSunat(sunatResponseGRE.getErrors().get(0).getDesError());	
				}				
//				if((sunatResponseGRE.getErrors()!= null) && (sunatResponseGRE.getErrors().size() > 0)) {	
//										documento.setObservaDescripcion(sunatResponseGRE.getErrors().get(0).getNumError()
//							+ " | "+sunatResponseGRE.getErrors().get(0).getDesError());
//				}	
				
			}else if(documento.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_99)) {
				log.info("4.A) sunatResponseGRE.toStringRes(): {}",sunatResponseGRE.toStringRes());
				if(sunatResponseGRE.getCod()!= null) {					
					documento.setErrorNumero(sunatResponseGRE.getCod());
					documento.setErrorDescripcion(sunatResponseGRE.getMsg());
					documento.setRespuestaSunat(sunatResponseGRE.getCod());
					documento.setErrorLogSunat(sunatResponseGRE.getCod()+" "+sunatResponseGRE.getMsg());
				}			
				if(sunatResponseGRE.getError()!= null) {
					log.info("4.B) sunatResponseGRE.toStringRes(): {}",sunatResponseGRE.toStringRes());
//					documento.setErrorNumero(sunatResponseGRE.getError().getNumError());
//					documento.setErrorDescripcion(sunatResponseGRE.getError().getDesError());
					documento.setRespuestaSunat(sunatResponseGRE.getError().getNumError()); 						
					documento.setErrorLogSunat(sunatResponseGRE.getError().getDesError());	
				}
				if((sunatResponseGRE.getErrors()!= null) && (sunatResponseGRE.getErrors().size() > 0)) {
					log.info("4.C) sunatResponseGRE.toStringRes(): {}",sunatResponseGRE.toStringRes());
//					documento.setErrorNumero(sunatResponseGRE.getErrors().get(0).getNumError());
//					documento.setErrorDescripcion(sunatResponseGRE.getErrors().get(0).getDesError());
					documento.setRespuestaSunat(sunatResponseGRE.getErrors().get(0).getNumError()); 						
					documento.setErrorLogSunat(sunatResponseGRE.getErrors().get(0).getDesError());	
				}
				//documento.setCdr(new byte[0]);
//				if(sunatResponseGRE.getIndCdrGenerado().equals(Constantes.SUNAT_ESTADOCP_ACEPTADO)) {
//					byte[] cdr = decodeString2Byte(sunatResponseGRE.getArcCdr());
//					documento.setCdr(cdr);
//				}
			}else {						
				documento.setRespuestaSunat(Constantes.SUNAT_ERROR_0137); 					
				if(sunatResponseGRE.getCod()!= null) {
					log.info("5.A) sunatResponseGRE.toString(): {}",sunatResponseGRE.toStringRes());
					documento.setErrorNumero(sunatResponseGRE.getCod());
					documento.setErrorDescripcion(sunatResponseGRE.getMsg());
					documento.setErrorLogSunat(sunatResponseGRE.getCod()+" "+sunatResponseGRE.getMsg());
				}
				if(sunatResponseGRE.getError()!= null) {					
					log.info("5.B) sunatResponseGRE.toString(): {}",sunatResponseGRE.toStringRes());
//					documento.setErrorNumero(sunatResponseGRE.getError().getNumError());
//					documento.setErrorDescripcion(sunatResponseGRE.getError().getDesError());
					//documento.setEstado(Constantes.SUNAT_CDR_ERROR_AVATAR);	
					documento.setEstado(Constantes.SUNAT_CDR_ERROR_PROCESO);
					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));					
					documento.setErrorNumero(sunatResponseGRE.getError().getNumError());
					documento.setErrorDescripcion(sunatResponseGRE.getError().getDesError());
					documento.setRespuestaSunat(sunatResponseGRE.getError().getNumError()); 						
					documento.setErrorLogSunat(sunatResponseGRE.getError().getDesError());	
				}
				if((sunatResponseGRE.getErrors()!= null) && (sunatResponseGRE.getErrors().size() > 0)) {
					log.info("5.C) sunatResponseGRE.toString(): {}",sunatResponseGRE.toStringRes());
//					documento.setErrorNumero(sunatResponseGRE.getErrors().get(0).getNumError());
//					documento.setErrorDescripcion(sunatResponseGRE.getErrors().get(0).getDesError());
					//documento.setEstado(Constantes.SUNAT_CDR_ERROR_AVATAR);
					documento.setRespuestaSunat(sunatResponseGRE.getErrors().get(0).getNumError()); 						
					documento.setErrorLogSunat(sunatResponseGRE.getErrors().get(0).getDesError());	
				}				
//				else {
//					if((sunatResponseGRE.getErrors()!= null) && (sunatResponseGRE.getErrors().size() > 0)) {
//												documento.setErrorNumero(sunatResponseGRE.getErrors().get(0).getNumError());
//						documento.setErrorDescripcion(sunatResponseGRE.getErrors().get(0).getDesError());
//						documento.setErrorLogSunat(sunatResponseGRE.getErrors().get(0).getNumError()
//							+ " | "+sunatResponseGRE.getErrors().get(0).getDesError());	
//					}
//				}					
			}
			documento.setAdvertencia(sunatResponseGRE.getExc());
			documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());
			log.info("readDocument C) ticket: {} | TipoDocumento(): {} | Estado: {} | RespuestaSunat: {} | logSunat: {} | ErrorNumero: {} ",
					documento.getErrorLog(), documento.getTipoDocumento(), documento.getEstado(), 
					documento.getRespuestaSunat(), documento.getErrorLogSunat(), documento.getErrorNumero());	
		}		
	}
	
	public static SunatTokenResponse buscaTokenEnvioComprobante(SunatRequest sunatConsulta, Emisor emisor) {
		SunatTokenResponse sunatToken = new SunatTokenResponse();
		//String apiToken = apiTockenEnvioComprobante+"/"+idTockenEnvioComprobante+"/oauth2/token/";
		String apiToken = apiTockenEnvioComprobante+"/"+emisor.getIdTokenGre()+"/oauth2/token/";
		log.info("buscaTokenEnvioComprobante : {}",apiToken);
		try {			
			//log.info("sunatConsulta {}",sunatConsulta.toString() );
			HttpPost httpPost = new HttpPost(apiToken);
			httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("grant_type","password"));
			params.add(new BasicNameValuePair("scope","https://api-cpe.sunat.gob.pe"));
//			params.add(new BasicNameValuePair("client_id",idTockenEnvioComprobante));
//			params.add(new BasicNameValuePair("client_secret",claveTockenEnvioComprobante));
//			params.add(new BasicNameValuePair("username",sunatConsulta.getNumRuc()+usuariosol.toUpperCase()));
//			params.add(new BasicNameValuePair("password",clavesol));

			params.add(new BasicNameValuePair("client_id",emisor.getIdTokenGre()));
			params.add(new BasicNameValuePair("client_secret",emisor.getClaveTokenGre()));
			params.add(new BasicNameValuePair("username",sunatConsulta.getNumRuc()+emisor.getUsuariosol().toUpperCase()));
			params.add(new BasicNameValuePair("password",emisor.getClavesol()));			
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));			
			Optional<String> resp = consumeRequest(httpPost,10000);	
			//log.info("buscaTokenEnvioComprobante resp: {}",resp!=null ? resp.get() : "");
			if((resp!=null) && (resp.isPresent())) 
				sunatToken = gson.fromJson(resp.get(), SunatTokenResponse.class);
		} catch (ConsumeExceptionRest e) {	
			JavaParserExceptions.getParseException(e, "buscaTokenEnvioComprobante ConsumeExceptionRest");
			//log.error("buscaTokenEnvioComprobante RestConsumeException: "+e.getMessage());
		}catch (Exception e) {		
			JavaParserExceptions.getParseException(e, "buscaTokenEnvioComprobante");
			//log.error("buscaTokenEnvioComprobante Exception "+e.getMessage());
		}			
		return sunatToken;
	}
	
	public static Optional<SunatBeanResponseGRE> envioComprobante(SunatRequest sunatConsulta, SunatTokenResponse sunatToken) {		
		String urlEnvio = apiEnviarComprobante+"/"+sunatConsulta.getNumRuc()+"-"+sunatConsulta.getCodComp()+"-"+sunatConsulta.getNumeroSerie()+"-"
				+sunatConsulta.getNumero();
		log.info("envioComprobante : {}",urlEnvio);
		try {
			HttpPost httpPost = new HttpPost(urlEnvio);
			httpPost.setHeader(HttpHeaders.CONTENT_TYPE,"application/json");
			httpPost.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + sunatToken.getAccess_token());	
			Archivo archivo = new Archivo();
			archivo.setNomArchivo(sunatConsulta.getFilename());
			archivo.setArcGreZip(sunatConsulta.getContentString());
			archivo.setHashZip(sunatConsulta.getHash());		    
		    ObjectMapper ObjMapperArchivo = new ObjectMapper();
		    String jsonMapperArchivo = ObjMapperArchivo.writeValueAsString(archivo);
		    String jsonEntity = "{\"archivo\":"+jsonMapperArchivo+"}";
		    //log.info("jsonEntity: {}",jsonEntity);			
			httpPost.setEntity(new StringEntity(jsonEntity, ContentType.APPLICATION_JSON));				
			Optional<String> resp = consumeRequest(httpPost,50000);		
			//log.info("envioComprobante resp: {}",resp!=null ? resp.get() : "");
			if((resp!=null)) {  	
				SunatBeanResponseGRE sunatResponseGRE = gson.fromJson(resp.get(), SunatBeanResponseGRE.class);		
				return Optional.ofNullable(sunatResponseGRE);	
			}	
		} catch (ConsumeExceptionRest e) {	
			JavaParserExceptions.getParseException(e, "envioComprobante ConsumeExceptionRest");
			log.error("envioComprobante RestConsumeException: "+e.getMessage());
		}catch (Exception e) {
			JavaParserExceptions.getParseException(e, "envioComprobante");
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));			
//			log.error("envioComprobante Exception: "+e.getMessage());
		}			
		return Optional.ofNullable(null);
	}	
	
	public static Optional<SunatBeanResponseGRE> consultarTicket(Documento documento, SunatTokenResponse sunatToken) {		
		String urlEnvio = apiConsultarComprobante+"/"+documento.getErrorLog();
		log.info("consultarTicket : {}",urlEnvio);
		try {
			HttpGet httpGet = new HttpGet(urlEnvio);
			httpGet.setHeader(HttpHeaders.CONTENT_TYPE,"application/json");
			httpGet.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + sunatToken.getAccess_token());	
			//httpPost.setEntity(new StringEntity(jsonEntity, ContentType.APPLICATION_JSON));				
			Optional<String> resp = consumeRequest(httpGet,50000);		
			//log.info("consultarTicket resp: {}",resp!=null ? resp.get() : "");
			if((resp!=null) &&(resp.isPresent())) {  					
				SunatBeanResponseGRE sunatResponseGRE = gson.fromJson(resp.get(), SunatBeanResponseGRE.class);	
				return Optional.ofNullable(sunatResponseGRE);	
			}	
		} catch (ConsumeExceptionRest e) {	
			JavaParserExceptions.getParseException(e, "consultarTicket ConsumeExceptionRest");
//			log.error("consultarTicket RestConsumeException: "+e.getMessage());
		}catch (Exception e) {
			JavaParserExceptions.getParseException(e, "consultarTicket");
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
	    				resp = consumeResponseDos(httpClient, req);
	    		return resp;
		    } catch ( Exception e) {	
		    	JavaParserExceptions.getParseException(e, "consumeRequest");
//				log.error("consumeRequest: {} ",e.getMessage());
		    } 	    	
	    } catch ( Exception e) {	
	    	JavaParserExceptions.getParseException(e, "consumeRequest");
//			log.error("consumeRequest CloseableHttpClient: {}",e.getMessage());
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
				throw new ConsumeExceptionRest("consumeResponse 400: "+new String(bout.toByteArray(), StandardCharsets.UTF_8));
			} else {
				String responseSunatError = getResponseSunatError(resp);
				throw new ConsumeExceptionRest("consumeResponse respondio statusCode="+resp.getStatusLine().getStatusCode()
						+" | ReasonPhrase: "+resp.getStatusLine().getReasonPhrase()
						+" | "+responseSunatError);
			}
	    } catch ( Exception e) {		
	    	JavaParserExceptions.getParseException(e, "consumeResponse");
			//log.error("consumeResponse CloseableHttpResponse: {}",e.getMessage());	
	    } 	
		return Optional.ofNullable(Constantes.SUNAT_RESPUESTA_0098);
	}	
	
	private static Optional<String> consumeResponseDos(CloseableHttpClient httpClient, HttpUriRequest req) 
			throws ConsumeExceptionRest {
		String responseSunatError = "";
		try (CloseableHttpResponse resp = httpClient.execute(req)){ 
			if (resp.getStatusLine().getStatusCode() == 200) {
				InputStream is = resp.getEntity().getContent();
				StringWriter wri = new StringWriter();
				IOUtils.copy(is, wri, StandardCharsets.UTF_8);
				is.close();
				wri.close();
				return Optional.of(wri.getBuffer().toString());
			} else {				
				responseSunatError = getResponseSunatError(resp);				
				throw new ConsumeExceptionRest("consumeResponseDos respondio statusCode="+resp.getStatusLine().getStatusCode()
						+" | ReasonPhrase: "+resp.getStatusLine().getReasonPhrase()
						+" | "+responseSunatError);
			}
	    } catch ( Exception e) {
	    	JavaParserExceptions.getParseException(e, "consumeResponseDos");
//			log.error("consumeResponse CloseableHttpResponse: {}",e.getMessage());	
	    } 	
		return Optional.ofNullable(responseSunatError);
	}
	
	private static String getResponseSunatError(CloseableHttpResponse resp) {
		String responseSunatError = "";
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			resp.getEntity().writeTo(bout);						
			bout.close();	
			responseSunatError = new String(bout.toByteArray(), StandardCharsets.UTF_8);
			//sunatResponseGRE = gson.fromJson(responseSunatError, SunatResponseGRE.class);
		}catch(Exception e) {
			JavaParserExceptions.getParseException(e, "getResponseSunatError");
//			log.error("getResponseSunatError: {}",e.getMessage());
		}
		return responseSunatError;
	}
	
	private static byte[] decodeString2Byte(String contentFile) {
		//log.info("decodeString2Byte contentFile: {}",contentFile);
		String rContentFile = contentFile.replaceAll(" ", "+");
		//log.info("decodeString2Byte contentByte: {}",rContentFile);
		byte[] contentByte = Base64.getDecoder().decode(rContentFile);
		return contentByte;		
	}	
				
}
