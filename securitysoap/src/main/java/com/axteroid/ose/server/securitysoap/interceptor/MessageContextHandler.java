package com.axteroid.ose.server.securitysoap.interceptor;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageContextHandler implements Handler<MessageContext> {
	private static final Logger log = LoggerFactory.getLogger(MessageContextHandler.class);
	
	@Override
	public boolean handleMessage(MessageContext context) {
		log.info("MessageContextHandler : handleMessage Called....");
		return true;
	}

//	@Override
//	public boolean handleFault(MessageContext context) {
//		log.info("MessageContextHandler : handleFault Called....");
//		Integer rc = (Integer) context.get(MessageContext.HTTP_RESPONSE_CODE);		
//		//log.info("MessageContextHandler : handleFault rc-1: {}", rc);
//		context.put(MessageContext.HTTP_RESPONSE_CODE, 200);
//		rc = (Integer) context.get(MessageContext.HTTP_RESPONSE_CODE);
//		log.info("MessageContextHandler : handleFault rc-2: {}", rc);
//		return true;
//	}

	@Override
	public boolean handleFault(MessageContext context) {
		log.info("MessageContextHandler : handleFault Called....");
		Object oResponseCode = context.get(MessageContext.HTTP_RESPONSE_CODE);		
		if (oResponseCode instanceof Integer) {
			Integer iResponseCode = (Integer) oResponseCode;
			log.info("MessageContextHandler : handleFault iResponseCode-1: {}", iResponseCode);
			if(iResponseCode == 500)
				context.put(MessageContext.HTTP_RESPONSE_CODE, 200);
			iResponseCode = (Integer) context.get(MessageContext.HTTP_RESPONSE_CODE);
			log.info("MessageContextHandler : handleFault iResponseCode-2: {}", iResponseCode);
		}				
		return true;
	}
	
	@Override
	public void close(MessageContext context) {
		log.info("MessageContextHandler : close Called....");		
	}

}
