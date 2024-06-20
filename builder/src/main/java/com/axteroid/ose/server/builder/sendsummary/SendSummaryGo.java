package com.axteroid.ose.server.builder.sendsummary;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.builder.content.ReturnResponse;
import com.axteroid.ose.server.builder.content.impl.ContentValidateSendImpl;
import com.axteroid.ose.server.builder.content.impl.ReturnResponseImpl;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.DateUtil;

public class SendSummaryGo {
	private static final Logger log = LoggerFactory.getLogger(SendSummaryGo.class);
	private ContentValidateSendImpl contentValidateSend = new ContentValidateSendImpl();
	private ReturnResponse returnResponse = new ReturnResponseImpl();
	
	private String destinationName = "queue/SendSummaryQueue";	
	@JMSConnectionFactory("jms/remoteOSE")
	private JMSContext context;
	@Resource(lookup = "java:jboss/exported/jms/queue/SendSummaryQueue")
	private Queue queue;	
	@Resource(mappedName = "java:/JmsXA")
	private ConnectionFactory cf;		
	
	public String validarComprobante(String fileName, byte[] contentFile, String user){		
		log.info("validarComprobante | "+ user);	
		Documento documento = new Documento();		
		documento.setNombre(fileName);
		documento.setUbl(contentFile);
		documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));		
		documento.setFecRecepcion(DateUtil.getCurrentTimestamp());
		documento.setUserCrea(user);
		
		if(!(user!=null) || (user.isEmpty())) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			contentValidateSend.buscarError(documento, Constantes.SUNAT_ERROR_0106);
			String sError = documento.getErrorNumero()+Constantes.OSE_SPLIT+documento.getErrorDescripcion();		
			return sError;
		}		
		
		contentValidateSend.validarContentSendSummary(documento);
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return returnResponse.buildByteReturnError(documento, contentValidateSend);
		
//		if(documento.getRucEmisor() == ConstantesOse.SUNAT_RUC){
//			documento.setErrorComprobante(ConstantesOse.CONTENT_FALSE.charAt(0));
//			documento.setErrorNumero(ConstantesOse.SUNAT_ERROR_0000);
//			documento.setErrorDescripcion(ConstantesOse.SUNAT_ERROR_0000_CD+" - recibido: "+documento.getFecRecepcion()); 
//			return  returnResponse.buildByteReturnError(documento, contentValidateSend);	
//		}		
		
		String activoEmpresa = returnResponse.getEstadobyRUC(documento);
		log.info("activoEmpresa ... "+activoEmpresa);
		if(!documento.getErrorComprobante().equals(Constantes.CONTENT_PROCESO.charAt(0))) 
			return  returnResponse.buildByteReturnError(documento, contentValidateSend);		
		
		String rucEmpresa = returnResponse.getRUCbyUser(documento, user);
		if(!documento.getErrorComprobante().equals(Constantes.CONTENT_PROCESO.charAt(0))) 
			return  returnResponse.buildByteReturnError(documento, contentValidateSend);
		
		documento.setEstado(Constantes.SUNAT_CDR_EN_PROCESO);
		documento.setRucPseEmisor(Long.parseLong(rucEmpresa));
		documento.setFecIniProc(DateUtil.getCurrentTimestamp());
		returnResponse.grabarDocumento(documento);
		//returnResponse.updateTbComprobante_Send(documento);
		if(!documento.getErrorComprobante().equals(Constantes.CONTENT_PROCESO.charAt(0))) 
			return  returnResponse.buildByteReturnError(documento, contentValidateSend);	
		
		//Documento documento = tbContent.getTbComprobanteList().get(0);
		//documento.setIdContent(tbContent);
		//documento.setCorrelativo(tbContent.getNumCpe());
		//documento.setErrorComprobante(ConstantesOse.CONTENT_PROCESO.charAt(0));
		//documento.setRucEmisor(tbContent.getRucEmisor());		
		//documento.setUserCrea(user);			
//		returnResponse.grabarComprobante_SendPack(documento, tbContent, contentValidateSend);
//		if(documento.getErrorComprobante().equals(ConstantesOse.CONTENT_FALSE.charAt(0)))
//			return  returnResponse.buildByteReturnError(tbContent, contentValidateSend);
		
		SendSummaryProcess sendSummaryProcess = new SendSummaryProcess();
		String mensaje = sendSummaryProcess.procesarComprobante(documento);	
		
//		sendMessage(tbContent.getId().toString());
//		String mensaje = returnResponse.buildByteReturnTicket(documento);
					
		return mensaje;			
	}
	
	void sendMessage(String fileName) {	
		Context context;
		ConnectionFactory connectionFactory;
		Connection connection = null;		
		MessageProducer publisher = null;
		Session session = null;
		try {
			context = new InitialContext();
			connectionFactory = (ConnectionFactory)context.lookup("/ConnectionFactory");      
			Destination queue = (Destination)context.lookup(destinationName);
		    connection = connectionFactory.createConnection();
		    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);		        
		    publisher = session.createProducer(queue);
		    publisher.setDeliveryMode(DeliveryMode.PERSISTENT);		      
		    connection.start();	        
		    TextMessage message = session.createTextMessage(fileName);
		    publisher.send(message);
		    publisher.close();	      
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("sendMessage Exception \n"+errors);
		}finally {         			   
			if (publisher != null) try { publisher.close(); } catch (Exception ignore) { }
			if (session != null) try { session.close(); } catch (Exception ignore) { }
			if (connection != null) try { connection.close(); } catch (Exception ignore) { }
		}        
	}	
	
}
