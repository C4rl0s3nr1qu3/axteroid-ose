package com.axteroid.ose.server.sunat.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.CertificateEncodingException;
import java.util.Iterator;
import java.util.List;

import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.sunat.service.SunatFileResponseUtil;
import com.axteroid.ose.server.tools.bean.ArResponse;
import com.axteroid.ose.server.tools.bean.SunatBeanRequest;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.axteroid.ose.server.tools.xml.UblResponseSunat;

public class SendSunatClientWS {
	private static final Logger log = LoggerFactory.getLogger(SendSunatClientWS.class);
	
	//private static boolean enableLogSoap = true; 
	//private String certPath = Constantes.DIR_AXTEROID_OSE+Constantes.DIR_JKS+"/"+Constantes.OSE_JKS_CERTIFICADO;
	//private String certPwd = Constantes.OSE_JKS_KeyPassword;
	private String certPath = Constantes.DIR_AXTEROID_OSE+Constantes.DIR_JKS+DirUtil.getCertificateName();
	private String certPwd = DirUtil.getCertificateKeyPassword();
	private String urlWs;
	private String ticket;
	private Certificado certificate;	
	private StringBuffer sbSoapResponse;	
	private File zipFile;
	//private String idFirma = "id-axteroid-ose";
	private String idFirma = "id-6279";
	private SOAPMessage soapMessageRequest;
	private SOAPMessage soapMessageResponse;
			
	public SendSunatClientWS() {}
	
	public void sendBill(Documento documento) throws CertificateEncodingException, IOException, Throwable {
		this.getDataSend(documento);
		send(getSendBillMessage(), "urn:sendBill", documento);
		this.readResponseWS(documento);
	}

	public void sendSummary(Documento documento) throws CertificateEncodingException, IOException, Throwable {
		this.getDataSend(documento);
		send(getSummaryMessage(), "urn:sendSummary", documento);
		this.readResponseWS(documento);
	}
	
	public void getDataSend(Documento documento) {
		log.info("getDataSend {} ",documento.toString());
		try {
			this.sbSoapResponse = new StringBuffer();
			this.certificate = CertificateLoader.getCertificado(certPath, certPwd);
			ReadCertificate readCertificate = new ReadCertificate();
			readCertificate.validarCertificate(this.certificate.getX509Certificate());
			SunatBeanRequest statusResponseBean = SunatFileResponseUtil.getStatusResponseBean(documento);  
			String pathZip = Constantes.DIR_AXTEROID_OSE+Constantes.DIR_FILE+"/"+statusResponseBean.getFilename();
	    	FileUtils.writeByteArrayToFile(new File(pathZip), statusResponseBean.getContent());
			List<String> listUrlSunat = DirUtil.getLinkEnvioCDRSunatProper();  
			this.urlWs = listUrlSunat.get(0);
			this.zipFile = new File(pathZip);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDataSend Exception \n"+errors);  
		}
	}
	
	private void send(StringBuffer message, String soapAction, Documento documento) {
		log.info("send {} ",documento.toString());
		SOAPConnection soapConnection = null;	
		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			soapConnection = soapConnectionFactory.createConnection();	
			MessageFactory messageFactory = MessageFactory.newInstance();
			this.soapMessageRequest = messageFactory.createMessage();
			SOAPPart soapPart = this.soapMessageRequest.getSOAPPart();			
			SOAPSigner soapSigner = new SOAPSigner(certificate);
			String xmlSigned = soapSigner.firmar(message.toString(), "Body", "Security");
			ByteArrayInputStream bais = new ByteArrayInputStream(xmlSigned.getBytes());			
			StreamSource prepMsg = new StreamSource(bais);
			soapPart.setContent(prepMsg);
			try {bais.close();} catch (Throwable e) {};
			bais = null;			
			MimeHeaders headers = this.soapMessageRequest.getMimeHeaders();
			headers.addHeader("SOAPAction",soapAction);
			this.soapMessageRequest.saveChanges();		
			byte[] byteRequest = soapToString(this.soapMessageRequest);
			log.info("SOAP-REQUEST: {} ", new String(byteRequest));			
			sendSoapMsg(soapConnection, documento);									
		} catch (UnsupportedOperationException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("send UnsupportedOperationException \n {}",errors);  
		} catch (SOAPException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("send SOAPException \n {}",errors);  
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("send Exception \n {}",errors);  
		} finally {			
			if (soapConnection != null) {
				try {
					soapConnection.close();
				} catch (Throwable e) {
				}
			}			
		}			
	}
	
	private void sendSoapMsg(SOAPConnection soapConnection, Documento documento) {		
		log.info("sendSoapMsg {} ",documento.toString());
		try {
			soapMessageResponse = soapConnection.call(this.soapMessageRequest, urlWs);
    	}catch(SOAPFaultException e) {   
			StringWriter errors1 = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors1));
			log.error("sendSoapMsg Exception1 \n"+errors1); 
    		SOAPFault soapFault = e.getFault();
    		Detail detail = soapFault.getDetail();   
    		if(detail !=null) {
    			Iterator<DetailEntry> detailEntries= detail.getDetailEntries();   
    			while (detailEntries.hasNext()) {
    				SOAPBodyElement sbe = (SOAPBodyElement) detailEntries.next();
    				documento.setErrorLogSunat(sbe.getValue());
    				log.error("sendSoapMsg  SOAPFaultException soapFault: "+soapFault.getFaultString()+" - "+sbe.getValue());        	      
    			}        			
    	    }   		
    		documento.setRespuestaSunat(soapFault.getFaultString());
    		documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDBILL);
    		if((documento.getTipoDocumento().trim().equals(Constantes.SUNAT_COMUNICACION_BAJAS)) ||
    				(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_RESUMEN_DIARIO)) ||
    				(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_REVERSION)))
				documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDSUMMARY);
    		if(!(documento.getRespuestaSunat()!=null) || (documento.getRespuestaSunat().length()>4)) {    			
    			documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_Not_Acceptable);
    			if((documento.getErrorLogSunat()==null) || (documento.getErrorLogSunat().isEmpty()))
    				documento.setErrorLogSunat(e.toString());    			
    			documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDBILL);
        		if((documento.getTipoDocumento().trim().equals(Constantes.SUNAT_COMUNICACION_BAJAS)) ||
        				(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_RESUMEN_DIARIO)) ||
        				(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_REVERSION)))
    				documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDSUMMARY);   			
    		}    	
    		documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp()); 
        }catch (Exception e) {
        	documento.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_NO_FOUND);
			documento.setErrorLogSunat(e.toString());
			documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());    
			documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDBILL);
    		if((documento.getTipoDocumento().trim().equals(Constantes.SUNAT_COMUNICACION_BAJAS)) ||
    				(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_RESUMEN_DIARIO)) ||
    				(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_REVERSION)))
				documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDSUMMARY);
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("sendSoapMsg Exception \n"+errors);       
        }
	}		
	
	private void readResponseWS(Documento documento) {
		log.info("readResponseWS {} ",documento.toString());
		byte[] byteResponse = soapToString(this.soapMessageResponse);	
		if(byteResponse.length == 0)
			return;
		log.info("SOAP RESPONSE: {} ", new String(byteResponse));
		documento.setMensajeSunat(byteResponse);
		ArResponse arResponse = UblResponseSunat.readResponseWS(byteResponse);
		if((arResponse.getResponseCode() !=null) && !(arResponse.getResponseCode().isEmpty())) 
			documento.setRespuestaSunat(arResponse.getResponseCode());
		if((arResponse.getDescription() !=null) && !(arResponse.getDescription().isEmpty())) 
			documento.setErrorLogSunat(arResponse.getDescription());
		documento.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());    
		documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDBILL);
		if((documento.getTipoDocumento().trim().equals(Constantes.SUNAT_COMUNICACION_BAJAS)) ||
				(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_RESUMEN_DIARIO)) ||
				(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_REVERSION)))
			documento.setEstado(Constantes.SUNAT_CDR_ERROR_SENDSUMMARY);			
	}
	
	private byte[] soapToString(SOAPMessage soapMessage) {
		log.info("soapToString ");
		byte[] byteResponse = new byte[0];
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			Source sourceContent = soapMessage.getSOAPPart().getContent();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(baos); 
			transformer.transform(sourceContent, result);
			byteResponse = baos.toByteArray();
			//response = new String(baos.toByteArray());
			//return new String(baos.toByteArray());
		} catch (Exception e) {  
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("soapToString Exception \n {}",errors);  
		}
		return byteResponse;
	}	
	
	private StringBuffer getSendBillMessage() {		
		log.info("getSendBillMessage ");
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>");
			buffer.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:math=\"http://exslt.org/math\">");
			buffer.append("<SOAP-ENV:Header>");
			buffer.append("<wsse:Security xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss- wssecurity-utility-1.0.xsd\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis- 200401-wss-wssecurity-secext-1.0.xsd\" SOAP-ENV:mustUnderstand=\"1\"");
			buffer.append("<wsse:BinarySecurityToken EncodingType=\"http://docs.oasis- open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token- profile-1.0#X509v3\" wsu:Id=\"X509-C46279C6030251FE7D1485355223627975\">");
			buffer.append(base64(certificate.getX509Certificate().getEncoded()));
			buffer.append("</wsse:BinarySecurityToken>");
			buffer.append("</wsse:Security>");
			buffer.append("</SOAP-ENV:Header>");
			buffer.append("<SOAP-ENV:Body xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"" + idFirma + "\">");
			buffer.append("<ns2:sendBill xmlns:ns2=\"http://service.sunat.gob.pe\">");
			buffer.append("<fileName>" + zipFile.getName() + "</fileName>");
			buffer.append("<contentFile>" + base64(readBytes(zipFile)) + "</contentFile>");
			buffer.append("</ns2:sendBill>");
			buffer.append("</SOAP-ENV:Body>");
			buffer.append("</SOAP-ENV:Envelope>");
		} catch (CertificateEncodingException e) {  
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getSendBillMessage CertificateEncodingException \n {}",errors);  
		} catch (IOException e) {  
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getSendBillMessage IOException \n {}",errors);  
		}
		return buffer;
	}		
	
	private StringBuffer getSendBillMessage_01() {		
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>");
			buffer.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:math=\"http://exslt.org/math\">");
			buffer.append("<SOAP-ENV:Header>");
			buffer.append("<wsse:Security xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" SOAP-ENV:mustUnderstand=\"1\">");
			buffer.append("<wsse:BinarySecurityToken EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" wsu:Id=\"X509-C46279C6030251FE7D148535522378575\">");
			buffer.append(base64(certificate.getX509Certificate().getEncoded()));
			buffer.append("</wsse:BinarySecurityToken>");
			buffer.append("</wsse:Security>");
			buffer.append("</SOAP-ENV:Header>");
			buffer.append("<SOAP-ENV:Body xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"" + idFirma + "\">");
			buffer.append("<ns2:sendBill xmlns:ns2=\"http://service.sunat.gob.pe\">");
			buffer.append("<fileName>" + zipFile.getName() + "</fileName>");
			buffer.append("<contentFile>" + base64(readBytes(zipFile)) + "</contentFile>");
			buffer.append("</ns2:sendBill>");
			buffer.append("</SOAP-ENV:Body>");
			buffer.append("</SOAP-ENV:Envelope>");
		} catch (CertificateEncodingException e) {  
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getSendBillMessage CertificateEncodingException \n {}",errors);  
		} catch (IOException e) {  
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getSendBillMessage IOException \n {}",errors);  
		}
		return buffer;
	}		

	private StringBuffer getSummaryMessage() {		
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>");
			buffer.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:math=\"http://exslt.org/math\">");
			buffer.append("<SOAP-ENV:Header>");
			buffer.append("<wsse:Security xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" SOAP-ENV:mustUnderstand=\"1\">");
			buffer.append("<wsse:BinarySecurityToken EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" wsu:Id=\"X509-C46279C6030251FE7D148535522378575\">");
			buffer.append(base64(certificate.getX509Certificate().getEncoded()));
			buffer.append("</wsse:BinarySecurityToken>");
			buffer.append("</wsse:Security>");
			buffer.append("</SOAP-ENV:Header>");
			buffer.append("<SOAP-ENV:Body xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"" + idFirma + "\">");
			buffer.append("<ns2:sendSummary xmlns:ns2=\"http://service.sunat.gob.pe\">");
			buffer.append("<fileName>" + zipFile.getName() + "</fileName>");
			buffer.append("<contentFile>" + base64(readBytes(zipFile)) + "</contentFile>");
			buffer.append("</ns2:sendSummary>");
			buffer.append("</SOAP-ENV:Body>");
			buffer.append("</SOAP-ENV:Envelope>");
		} catch (CertificateEncodingException e) {  
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getSummaryMessage CertificateEncodingException \n {}",errors);  
		} catch (IOException e) {  
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getSummaryMessage IOException \n {}",errors);  
		}
		return buffer;
	}			
	
//	private String addZero(String tipoComprobante){
//		return tipoComprobante.length()> 1 ? tipoComprobante : "0"+tipoComprobante;
//	}
	
	public String getSoapResponse() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.getProperty("line.separator") + sbSoapResponse.toString();
	}

	private String base64(byte[] text) {
		 return Base64.encodeBase64String(text);
	}
	
	private byte[] readBytes(File file) throws IOException {
		Path path = Paths.get(file.getPath());
		return Files.readAllBytes(path);
	}
	
	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
	public void setZipFile(File archivoZip) {
		this.zipFile = archivoZip;
	}
}
