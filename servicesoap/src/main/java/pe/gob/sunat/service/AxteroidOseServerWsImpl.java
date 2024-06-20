package pe.gob.sunat.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.soap.Detail;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.axteroid.ose.server.builder.get.ProcesarGetStatus;
import com.axteroid.ose.server.builder.get.ProcesarGetStatusCdr;
import com.axteroid.ose.server.builder.sendbill.ProcesarSendBill;
import com.axteroid.ose.server.builder.sendsummary.SendSummaryGo;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.exception.OseServerServiceFault;
import com.axteroid.ose.server.tools.util.FileUtil;
import com.axteroid.ose.server.tools.util.ZipUtil;

@WebService(
        serviceName = "axteroidOseServer",
        portName = "AxteroidOseServerWsPort",
        targetNamespace = "http://service.sunat.gob.pe/",
        endpointInterface = "pe.gob.sunat.service.AxteroidOseServer")

public class AxteroidOseServerWsImpl implements AxteroidOseServerWs{
	private static final Logger log = LoggerFactory.getLogger(AxteroidOseServerWsImpl.class);
	@Resource
    private WebServiceContext webServiceContext;
	
	@WebMethod
    public byte[] sendBill(String fileName, DataHandler contentFile) throws SOAPException{  	
		MDC.put("FILE", fileName);
		try {
			log.info("Executing sendBill: {} | {}",fileName,contentFile);
	       	OseServerServiceFault fault = new OseServerServiceFault();	    
			byte[] contentByte = this.getDataHander2Byte(contentFile);					
			ProcesarSendBill procesarSendBill = new ProcesarSendBill();
			String retorno = procesarSendBill.procesarComprobante(fileName, contentByte, this.getUser());
			String [] retSplit = retorno.split(Constantes.OSE_SPLIT);
			if(retSplit.length == 1) {
				byte[] ret = this.getCDR2Zip(retorno, fileName);
				if(ret.length > 0) 
					return ret;				
				retorno = "";
			}        
			fault.setCode(Constantes.SUNAT_ERROR_0100_C);
			fault.setMessage(Constantes.SUNAT_ERROR_0100_D);        
			if(!retorno.isEmpty()) {
				fault.setCode(retSplit[0]);
				fault.setMessage(retSplit[1]);
			}						
			SOAPFault soapFault = this.getSOAPFault(fault);
			throw new SOAPFaultException(soapFault);
		} finally {
			MDC.remove("FILE");
		}		
    }
	
	@WebMethod
    public String sendSummary(String fileName, DataHandler contentFile) throws SOAPException{ 
		MDC.put("FILE", fileName);
		try {
			log.info("Executing sendSummary: {} | {}",fileName,contentFile);
			OseServerServiceFault fault = new OseServerServiceFault();
			byte[] contentByte = this.getDataHander2Byte(contentFile);
			SendSummaryGo sendSummaryGo = new SendSummaryGo();
			String retorno = sendSummaryGo.validarComprobante(fileName, contentByte, this.getUser());	
			String [] retSplit = retorno.split(Constantes.OSE_SPLIT);			
			if(retSplit.length == 1) 
				return retorno;
			fault.setCode(Constantes.SUNAT_ERROR_0100_C);
			fault.setMessage(Constantes.SUNAT_ERROR_0100_D);        
			if(!retorno.isEmpty()) {
				fault.setCode(retSplit[0]);
				fault.setMessage("");
				if(retSplit[1] != null)
					fault.setMessage(retSplit[1]);				
			}			
			SOAPFault soapFault = this.getSOAPFault(fault);
			throw new SOAPFaultException(soapFault);
		} finally {
			MDC.remove("FILE");
		}		
    }
	
	@WebMethod
    public StatusResponse getStatus(String ticket) throws SOAPException{ 
		MDC.put("FILE", ticket);
		try {
			log.info("Executing getStatus ticket: {}", ticket);
			StatusResponse statusResponse = new StatusResponse();  
			OseServerServiceFault oseServerServiceFault = new OseServerServiceFault();
			ProcesarGetStatus procesarGetStatus  = new ProcesarGetStatus();		      
			Documento retorno =  procesarGetStatus.getStatus(ticket, this.getUser()); 	
			log.info("retorno.getErrorContent() - "+retorno.getErrorComprobante());
			if((retorno != null) && 
					(retorno.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0)))) {
				log.info("retorno.getStatus - "+retorno.getCdr());
				statusResponse.setContent(retorno.getCdr());
				statusResponse.setStatusCode(retorno.getErrorNumero());
				return statusResponse;
			}		
			oseServerServiceFault.setCode(Constantes.SUNAT_ERROR_0100_C);
			oseServerServiceFault.setMessage(Constantes.SUNAT_ERROR_0100_D);        
			if(!retorno.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0))) {
				oseServerServiceFault.setCode(retorno.getErrorNumero());
				oseServerServiceFault.setMessage(retorno.getErrorDescripcion());
			} 
			SOAPFault soapFault = this.getSOAPFault(oseServerServiceFault);
			throw new SOAPFaultException(soapFault);
		} finally {
			MDC.remove("FILE");
		}		
    }
	
	@WebMethod
	public byte[] getStatusCdr(StatusCdr statusCdr) throws SOAPException{ 		
		MDC.put("FILE", statusCdr.getRucComprobante()+"-"+statusCdr.getTipoComprobante()+"-"+statusCdr.getSerieComprobante()+"-"+statusCdr.getNumeroComprobante());
		try {
			OseServerServiceFault fault = new OseServerServiceFault();
			ProcesarGetStatusCdr procesarGetStatusCdr = new ProcesarGetStatusCdr();
			Documento tbComprobante = new Documento();
			this.getComprobante(tbComprobante,statusCdr);        
			log.info("Executing operation getStatusCdr: {}-{}-{}-{}",tbComprobante.getRucEmisor(),tbComprobante.getTipoDocumento(),tbComprobante.getSerie(),tbComprobante.getNumeroCorrelativo());
			Documento retorno = procesarGetStatusCdr.getStatusCdr(tbComprobante, this.getUser());        
			if(retorno != null && retorno.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0))) {
				if(retorno.getCdr()!=null && retorno.getCdr().length>0) {
					String strRetorno = new String(retorno.getCdr());
					byte[] ret = this.getCDR2Zip(strRetorno, retorno.getNombre());
					if(ret.length > 0) 
						return ret;							
				}
			}    
			fault.setCode(Constantes.SUNAT_ERROR_0100_C);
			fault.setMessage(Constantes.SUNAT_ERROR_0100_D);        
			if(!retorno.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0))) {
				fault.setCode(retorno.getErrorNumero());
				fault.setMessage(retorno.getErrorDescripcion());
			}
			SOAPFault soapFault = this.getSOAPFault(fault);
			throw new SOAPFaultException(soapFault);
		} finally {
			MDC.remove("FILE");
		}		
	}
	
	@WebMethod
    public String sendPack(String fileName, DataHandler contentFile) throws SOAPException{ 
		MDC.put("FILE", fileName);
		try {	
			log.info("Executing operation sendPack | "+fileName+" | "+contentFile);
			OseServerServiceFault fault = new OseServerServiceFault();
//			byte[] contentByte = this.getDataHander2Byte(contentFile);
//			SendPackGo sendPackGo = new SendPackGo();
//			String retorno = sendPackGo.validarComprobante(fileName, contentByte, this.getUser());
//			String [] retSplit = retorno.split(ConstantesOse.OSE_SPLIT);
//			if(retSplit.length == 1)    		
//				return retorno;        		
//			fault.setCode(ConstantesOse.SUNAT_ERROR_0100_C);
//			fault.setMessage(ConstantesOse.SUNAT_ERROR_0100_D);        
//			if(!retorno.isEmpty()) {
//				fault.setCode(retSplit[0]);
//				fault.setMessage(retSplit[1]);
//			}
			SOAPFault soapFault = this.getSOAPFault(fault);
			throw new SOAPFaultException(soapFault);     
		} finally {
			MDC.remove("FILE");
		}		
    }	
	
	private void getComprobante(Documento tbComprobante, StatusCdr statusCdr) {
		tbComprobante.setRucEmisor(Long.parseLong(statusCdr.getRucComprobante()));
		tbComprobante.setTipoDocumento(statusCdr.getTipoComprobante());
		tbComprobante.setSerie(statusCdr.getSerieComprobante());
		tbComprobante.setNumeroCorrelativo(statusCdr.getNumeroComprobante());
		if(tbComprobante.getNumeroCorrelativo() != null) {
			long corr = Long.parseLong(tbComprobante.getNumeroCorrelativo());
			tbComprobante.setCorrelativo(corr);
		}
	}
	
	private String getUser() {
		MessageContext messageContext = (MessageContext) webServiceContext.getMessageContext();
		AuthorizationPolicy authorizationPolicy = (AuthorizationPolicy)messageContext.get("org.apache.cxf.configuration.security.AuthorizationPolicy");
		String user = "";
		if (authorizationPolicy != null)  
			user = authorizationPolicy.getUserName();	      
		if((user!=null) && !(user.isEmpty()))
			return user;
		user = getWSUser();
		//log.info("WS user: "+user);
		return user;
	}
		
	private String getWSUser() {
		String user = "";
		Principal p = webServiceContext.getUserPrincipal();
		if(p!=null)
			user = p.getName();
		return user;
	}
	
	private SOAPFault getSOAPFault(OseServerServiceFault fault) throws SOAPException {
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage soapMessage = mf.createMessage(); //create SOAP message object
	    SOAPPart soapPart = soapMessage.getSOAPPart();//start to drill down to body
	    SOAPEnvelope soapEnvelope = soapPart.getEnvelope();	    
        SOAPFactory fac = SOAPFactory.newInstance();
		//SOAPFault soapFault = fac.createFault(code, new QName("", "SOAP-ENV:Server"));
        SOAPFault soapFault = fac.createFault();       
        //soapFault.setFaultCode("SOAP-ENV:Server");
        Name name = fac.createName("Server", null, null);
        soapFault.setFaultCode(name);
        soapFault.setFaultString(fault.getCode());        
        Detail detail = soapFault.addDetail();
        detail.addDetailEntry(soapEnvelope.createName("message")).addTextNode(fault.getMessage());   
        return soapFault;
	}
	
	private byte[] getDataHander2Byte(DataHandler contentFile) {		
		try {
			InputStream in = contentFile.getInputStream();
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		    byte[] buf = new byte[Constantes.BUFFER_CONTENTFILE];
		    for (int len = in.read(buf); len != -1; len = in.read(buf))
		        bytes.write(buf, 0, len);
		    in.close();	
		    return bytes.toByteArray();
		} catch (IOException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDataHander2Byte Exception \n"+errors);	
		}
		return new byte[] {};
	}
	
	private byte[] getCDR2Zip(String retorno, String fileName) {
		byte[] byteCDR = new byte[] {};
		try {
			List<File> listFile = new ArrayList<File>();
			String nombreFile = Constantes.CDR_PREFI+Constantes.GUION+fileName;
			int indexPoint = nombreFile.lastIndexOf(".");
			String prefix = nombreFile.substring(0, indexPoint);			
			byte [] byteOSE = retorno.getBytes();			
			File fileCDR = FileUtil.writeToFilefromBytes(prefix, Constantes.OSE_FILE_XML, byteOSE);
			log.info("fileCDR.getName(): "+fileCDR.getName());
			if(fileCDR != null)
				listFile.add(fileCDR);			
			byteCDR = ZipUtil.zipFiles2Byte(listFile);													
            if (fileCDR.exists())
            	fileCDR.delete();    					
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCDR2Zip Exception \n"+errors);	
		}
		return byteCDR;
	}
}
