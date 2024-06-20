package com.axteroid.ose.server.builder.sendsummary;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MessageDriven(name = "SendSummaryMDBServer", 
	activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/SendSummaryQueue"),
		@ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "NonDurable")
})
public class SendSummaryMDBServer implements MessageListener {	
	private static final Logger log = LoggerFactory.getLogger(SendSummaryMDBServer.class);
    public SendSummaryMDBServer() {}
	
    public void onMessage(Message inMessage) {
    	TextMessage message = (TextMessage)inMessage;
        try {
        	log.info(String.format("Content ID = %s", message.getText()));        	
        	SendSummaryContent sendSummaryContent = new SendSummaryContent();
        	sendSummaryContent.getContentSendSummary(message.getText());

        } catch (Exception e) {
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("Exception \n"+errors);
        }
        
    }
}
