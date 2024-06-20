package com.axteroid.ose.server.avatar.thread;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.axteroid.ose.server.avatar.model.TmCeDocumento;
import com.axteroid.ose.server.avatar.service.SendOseSeverService;

public class GetSatustThread implements Runnable{
	private static final Logger log = LoggerFactory.getLogger(GetSatustThread.class);
	private Thread t;
	private TmCeDocumento tmCeDocumento;
	private int status;
	private String urlSunat;
   
   public GetSatustThread(String url,TmCeDocumento tm, int st) {
	   urlSunat = url;
	   tmCeDocumento = tm;
	   status = st;	   
	   log.info("Creating " +  tmCeDocumento.getNombrearchivo() );
   }
   
   public void run() {
	   MDC.put("FILE", tmCeDocumento.getNombrearchivo());
	   log.info("Running " +  tmCeDocumento.getNombrearchivo() );
	   try {
			String ticket = "";
		   	switch(status) {
		   		case 1:
		   			ticket = "SEND_CDR @ "+tmCeDocumento.getId().getId();
		   			break;
		   		case 2:
		   			ticket = "GET_CDR @ "+tmCeDocumento.getId().getId();
		   			break;
		   		case 3:
		   			ticket = "UPDATE_CPE @ "+tmCeDocumento.getId().getId();
		   			break;    
		   		case 4:
		   			ticket = "UPDATE_CPE @ "+tmCeDocumento.getId().getId();
		   			break;		
		   	} 	   	
		   	log.info("ticket "+ticket);		   
		   	SendOseSeverService sendOseServerService = new SendOseSeverService();
		   	sendOseServerService.getSatus_ROBOT(ticket, urlSunat);	
		}catch (Exception e){
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);
		}  	   
	   	log.info("Thread " +  tmCeDocumento.getNombrearchivo() + " exiting.");
   	}
   
   
   public void start () {
	   log.info("Starting " +  tmCeDocumento.getNombrearchivo() );
      if (t == null) {
         t = new Thread (this, tmCeDocumento.getNombrearchivo());
         t.start ();
      }
   }
	
}
