package com.axteroid.ose.server.avatar.thread;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.dao.impl.InsertList2SunatPGDaoImpl;

public class ListSunatThread implements Runnable{
	private static final Logger log = LoggerFactory.getLogger(ListSunatThread.class);
	private Thread t;
	private String linea;
	public ListSunatThread(String tm) {
		linea = tm;   
		//log.info("Creating " +  tbComprobante.getNombre() );
	}
	   
	public void run() {
	   //log.info("Running " +  tbComprobante.getNombre() );
	   try {
		   InsertList2SunatPGDaoImpl insertList2TSPGDaoImpl = new InsertList2SunatPGDaoImpl();
		   insertList2TSPGDaoImpl.insertSunatCuadreDiarioDetalle(linea);
		}catch (Exception e){
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);
		}  	   
	}	
	
	public void start () {
	   //log.info("Starting " +  tbComprobante.getNombre() );
	   if (t == null) {
		   //t = new Thread (this, tsCuadreDiarioDetalle.toString());
		   t = new Thread (this, linea.toString());
		   t.start ();
	   }
	}	
}
