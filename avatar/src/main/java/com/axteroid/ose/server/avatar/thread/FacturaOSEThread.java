package com.axteroid.ose.server.avatar.thread;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.build.FacturaOseBuild;
import com.axteroid.ose.server.jpa.model.Documento;

public class FacturaOSEThread implements Runnable{
	private static final Logger log = LoggerFactory.getLogger(FacturaOSEThread.class);
	private Thread t;
	private Documento tbComprobante;
	   
	public FacturaOSEThread(Documento tm) {
		tbComprobante = tm;   
		//log.info("Creating " +  tbComprobante.getNombre() );
	}
	   
	public void run() {
	   //log.info("Running " +  tbComprobante.getNombre() );
	   try {
		   FacturaOseBuild facturaOseBuild = new FacturaOseBuild();
		   facturaOseBuild.sendDocumento(tbComprobante); 	
		}catch (Exception e){
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);
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
