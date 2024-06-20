package com.axteroid.ose.server.avatar.thread;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.service.SendOseSeverService;
import com.axteroid.ose.server.avatar.task.ShellCommand;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;

public class SendOseSeverThread implements Runnable{
	private static final Logger log = LoggerFactory.getLogger(SendOseSeverThread.class);
	private Thread t;
	private Documento tbComprobante;
	private String urlOSE;
	private String username;	
	private String password;	
	private String ipJBoss;	
	   
	public SendOseSeverThread(String url, Documento tm, String ipJBoss1, String username1, String password1) {
		tbComprobante = tm;
		urlOSE = url;	
		ipJBoss = ipJBoss1;
		username = username1;
		password = password1;
	}
	   
	public void run() {
	   log.info("run: " +  tbComprobante.getNombre() );
	   try {
		   	SendOseSeverService sendOseSeverService = new SendOseSeverService();
		   	String ret = sendOseSeverService.sendOseSever(urlOSE, tbComprobante);
		    log.info("runThread: " +  ret );
		    if((ret.trim().equals(Constantes.SUNAT_ERROR_2108))
					|| (ret.trim().equals(Constantes.SUNAT_ERROR_2236))
					|| (ret.trim().equals(Constantes.SUNAT_ERROR_2329)))
				System.exit(0); 	
			if((ret.trim().equals(Constantes.SUNAT_ERROR_0402)))
				ShellCommand.reStartJBossLinuxSshPass(ipJBoss, username, password);
			
		}catch (Exception e){
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("runException \n"+errors);
		}  	   
	}	
	
	public void start () {
	   //log.info("Starting " +  tbComprobante.getNombre() );
	   if (t == null) {
		   t = new Thread (this, tbComprobante.getNombre());
		   t.start ();
	   }
	}	
}
