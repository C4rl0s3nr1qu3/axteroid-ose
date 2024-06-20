package com.axteroid.ose.server.avatar.build;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.soap.SOAPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.dao.DocumentoPGDao;
import com.axteroid.ose.server.avatar.dao.impl.DocumentoPGDaoImpl;
import com.axteroid.ose.server.avatar.jdbc.RsetPostGresJDBC;
import com.axteroid.ose.server.avatar.service.SendOseSeverService;
import com.axteroid.ose.server.avatar.task.ShellCommand;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.LogDateUtil;
import com.axteroid.ose.server.tools.xml.UblResponseSunat;
import com.axteroid.ose.server.tools.xml.XmlSign;

public class SendOseSeverBuild {
	private static final Logger log = LoggerFactory.getLogger(SendOseSeverBuild.class);
	static String username;	
	static String password;	
	static String ipJBoss;	
	static String url;
	Date fechaAnt;

    public void sendOseSever(String urlOSE, String ip, String usuario, String clave, String contar, String error_numero, 
    		String estado, int proceso) throws SOAPException {    
    	log.info("proceso: {} | estado {} | error_numero: {} ",proceso,estado,error_numero);
    	ipJBoss = ip;
    	url = urlOSE;
    	int k=0;  		    			    	
		List<Documento> list = 
				this.getListComprobantesEstadoErrorNumero(contar, error_numero, estado);
		int total = list.size(); 
    	if(total<=0)
    		return;	    	
		this.procesarSendOseSeverProg(list, total, k);
		k++;						    	
    }  	
    
    public void sendOseSeverFecha(String urlOSE, String ip, String usuario, String clave, String contar, String error_numero, 
    		String estado, int proceso) throws SOAPException {  
    	log.info("proceso: {} | estado {} | error_numero: {} ",proceso,estado,error_numero);
    	ipJBoss = ip;
    	url = urlOSE;
    	int k=0;  		    			    	
		List<Documento> list = this.getListComprobantesEstadoErrorNumeroFecha(contar, 
				error_numero, estado);
		int total = list.size(); 
    	if(total<=0)
    		return;
    	this.procesarSendOseSeverProg(list, total, k);
		k++;			  	
    } 
    
    public void sendOseSeverDia(String urlOSE, String ip, String usuario, String clave, String contar, String error_numero, 
    		String estado, int proceso, Date fecDBInicio, Date fecDBFin) throws SOAPException {    	
    	ipJBoss = ip;
    	url = urlOSE;
    	int k=0;  		    		
		Date fecDBAct = fecDBInicio;
		while(LogDateUtil.esFechaMayor(fecDBFin,fecDBAct)) {
			String strFecDBAct = DateUtil.dateToStringYYYY_MM_DD(fecDBAct);
	    	String [] fecDB = strFecDBAct.split("-");
	    	int year = Integer.parseInt(fecDB[0]);
	    	int mes = Integer.parseInt(fecDB[1]);
	    	int dia = Integer.parseInt(fecDB[2]);	    	
			List<Documento> list = this.getListComprobantesEstadoErrorNumeroContarDia(contar, 
					error_numero, estado, year, mes, dia);
			int total = list.size(); 
			//log.info("Itera: "+k+" - fecDBFin: "+fecDBFin+" - fecDBInicio: "+fecDBInicio+" - fecDBAct: "+fecDBAct);
			log.info("Itera: "+k+" - sendOseSevereDia: "+year +"/"+mes+"/"+dia+" - total: "+total);
			fecDBAct = DateUtil.addDays(fecDBAct, 1);
	    	if(total<=0)
	    		continue;
			switch(proceso) {
				case 0: this.procesarSendOseSeverProg(list, total, k); 
					break;
				case 1: this.procesarSendOseSeverProg(list, total, k);
					break;
			}
			k++;			
		}	    	
    }  		      
    
    public void sendOseSeverDiaELBD(String urlOSE, String ip, String usuario, String clave, String contar, String error_numero, 
    		String estado, int proceso, Date fecJBossInicio, Date fecJBossFin, Date fecDBInicio, Date fecDBFin, String error_log, String ruc) {    	
    	username = usuario;
    	password = clave;
    	ipJBoss = ip;
    	int k=0;       	
    	fecDBFin = DateUtil.addDays(fecDBFin, 1);
		Date fecDBAct = fecDBInicio;
		while(LogDateUtil.esFechaMayor(fecDBFin,fecDBAct)) {
			k++;			
			String strFecDBAct = DateUtil.dateToStringYYYY_MM_DD(fecDBAct);
	    	String [] fecDB = strFecDBAct.split("-");
	    	int year = Integer.parseInt(fecDB[0]);
	    	int mes = Integer.parseInt(fecDB[1]);
	    	int dia = Integer.parseInt(fecDB[2]);  
	    	List<Documento> list = new ArrayList<Documento>();
	    	list = this.getListComprobantesEstadoErrorNumeroContarDiaELRUC(contar, error_numero, estado, year, mes, dia, error_log, ruc);
			int total = list.size(); 
			log.info("Itera: "+k+" - sendOseSeverDiaELBD: "+year +"/"+mes+"/"+dia+" - total: "+total);
			fecDBAct = DateUtil.addDays(fecDBAct, 1);
	    	if(total<=0)
	    		continue;
			for(Documento documento : list){
				log.info("documento: "+documento.getId()+" | "+documento.getNombre());
				String fechaEmision = XmlSign.ObtenerFechaEmisionFromUBL(documento.getUbl());	
//				if((proceso==90) || (proceso==91)) {	
//					boolean file1 = false, file2 = false;
//					SunatListRead sunatListRead = new SunatListRead();
//					if(!sunatListRead.updateErrorContent(documento, fechaEmision)) 
//						continue;
//						file1 = true;
//					if(!ConsultaSunat.getOlitwsconscpegem(documento.getRucEmisor(), 
//    						documento.getTipoComprobante(), documento.getSerie()+"-"+documento.getNumeroCorrelativo()))
//						file2 = true;	
//					if(file1 && file2 ) continue;
//				}				
				documento.setErrorLog(fechaEmision);				
				try {		
					DocumentoPGDao documentoPGDao = new DocumentoPGDaoImpl();
					documentoPGDao.updateErrorLog(documento);
				} catch (SQLException e) {
					StringWriter errors = new StringWriter();				
					e.printStackTrace(new PrintWriter(errors));
					log.info("sendOseSeverErrorLogDia SQLException \n"+errors);
				}
			}
		}	
    }       
    
    public void sendOseSeverDiaJoinEL(String urlOSE, String ip, String usuario, String clave, String contar, 
    		String error_numero, String estado, int proceso, Date fecJBossInicio, Date fecJBossFin, Date fecDBInicio,
    		Date fecDBFin, String error_log, String ruc) throws SOAPException {    	
    	username = usuario;
    	password = clave;
    	ipJBoss = ip;
    	url = urlOSE;
    	Date fecJBossFinVal = fecJBossFin;
    	fecJBossFin = DateUtil.addDays(fecJBossFin, 1);    	
    	Date fecJBossAct = fecJBossInicio;
    	int k=0;
    	int diasFactura = 3;
    	boolean actual = true;
    	while(LogDateUtil.esFechaMayor(fecJBossFin,fecJBossAct)) {
    		k++;
    		if(Constantes.DIR_AXTEROID_OSE.trim().equals(Constantes.DIR_AXTEROID_OSE))
    			ShellCommand.cambiarFechaJBossLinux(fecJBossAct, ipJBoss, username, password);
    		else
    			ShellCommand.cambiarFechaJBoss(fecJBossAct, ipJBoss, username, password);       		    		
    		Date fecDBAct = DateUtil.substractDaysToDate(fecJBossAct, diasFactura);
    		fecDBFin = fecJBossAct;
    		fecDBFin = DateUtil.addDays(fecDBFin, 1);
    		int m = 0;
    		while(LogDateUtil.esFechaMayor(fecDBFin,fecDBAct)) {
    			m++;
    			String strFecDBAct = DateUtil.dateToStringYYYY_MM_DD(fecDBAct);
    	    	String [] fecDB = strFecDBAct.split("-");
    	    	int year = Integer.parseInt(fecDB[0]);
    	    	int mes = Integer.parseInt(fecDB[1]);
    	    	int dia = Integer.parseInt(fecDB[2]); 
    	    	List<Documento> list = new ArrayList<Documento>();
//    	    	if(proceso==60 || proceso == 61)
//    	    		list = this.getListComprobantesEstadoErrorNumeroContarDiaEL(contar, 
//						error_numero, estado, year, mes, dia, error_log);   	    	
//    	    	else if(proceso==21080 || proceso == 21081 || proceso==70 || proceso == 71)
    	    	list = this.getListComprobantesEstadoErrorNumeroContarDiaELFecha(contar, 
						error_numero, estado, year, mes, dia, error_log, ruc);
				int total = list.size(); 
				log.info("Itera: ("+k+"."+m+") sendOseSeverDiaJoinEL: "+year +"/"+mes+"/"+dia+" - total: "+total);
				fecDBAct = DateUtil.addDays(fecDBAct, 1);
		    	if(total<=0)
		    		continue;
		    	if(proceso == 21080) 
		    		this.procesarSendOseSeverProg(list, total, fecJBossAct, k); 
		    	if(proceso == 21081)
					this.procesarSendOseSeverThread(list, total, fecJBossAct, k);		    	
    		}
    		log.info(k+".a) fecJBossAct: "+fecJBossAct+" - fecJBossFin: "+fecJBossFin+ " - actual: "+actual);
	    	fecJBossAct = DateUtil.addDays(fecJBossAct, diasFactura);
	    	log.info(k+".b) fecJBossAct: "+fecJBossAct+" - fecJBossFin: "+fecJBossFin+ " - actual: "+actual);
	    	if(LogDateUtil.esFechaMayor(fecJBossAct, fecJBossFinVal) && actual) {
	    		fecJBossAct = new Date();
	    		actual = false;
	    	}	
	    	log.info(k+".c) fecJBossAct: "+fecJBossAct+" - fecJBossFin: "+fecJBossFin+ " - actual: "+actual);
    	}	
    }    

    private void procesarSendOseSeverProg(List<Documento> list, int total, int k) throws SOAPException { 
    	int i=0;
    	for(Documento documento : list){   
    		i++;
    		try {
    			log.info(k+"-"+i+" of "+total+" documento: "+documento.getNombre());   
    			SendOseSeverService sendOseSeverService = new SendOseSeverService();
				String ret = sendOseSeverService.sendOseSever(url, documento);	
				log.info("runProg: " +  ret );
				if(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_RESUMEN_DIARIO)) 
					Thread.sleep(10000);
				else
					Thread.sleep(1000);
        	} catch (Exception e){
    			StringWriter errors = new StringWriter();				
    			e.printStackTrace(new PrintWriter(errors));
    			log.error("procesarSendOseSeverFecha Exception \n"+errors);
            }  	   		
    	}
    }    
    
    private void procesarSendOseSeverProg(List<Documento> list, int total, Date fecJBossAct, int k) throws SOAPException { 
    	int i=0;
    	for(Documento documento : list){   
    		i++;
    		try {
    			log.info(k+"-"+i+" of "+total+" documento: "+documento.getNombre());   			
    			boolean boolFecha = false;
    			if(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_RESUMEN_DIARIO)) {
    				String fechaGeneracion = UblResponseSunat.ObtenerFechaEmisionFromUBL(documento.getUbl());
    				if(ShellCommand.validarFechaGeneracionRC(fechaGeneracion, fecJBossAct)) 
    					boolFecha = true;
    			}else {
    				String fechaEmision = UblResponseSunat.ObtenerFechaEmisionFromUBL(documento.getUbl());	 		
	    			if(ShellCommand.validarFechaEmision(fechaEmision, fecJBossAct)) 
	    				boolFecha = true;
    			}
    			if(boolFecha) {		
    				SendOseSeverService sendOseSeverService = new SendOseSeverService();
					String ret = sendOseSeverService.sendOseSever(url, documento);	
					log.info("runProg: " +  ret );
					if((ret.trim().equals(Constantes.SUNAT_ERROR_2108))
							|| (ret.trim().equals(Constantes.SUNAT_ERROR_2236))
							|| (ret.trim().equals(Constantes.SUNAT_ERROR_2329)))
						System.exit(0);
					if((documento.getErrorNumero()!=null) &&
							!documento.getErrorNumero().equals(Constantes.SUNAT_ERROR_1033)) {
						if((ret.trim().equals(Constantes.SUNAT_ERROR_1033))){
							ShellCommand.reStartJBossLinuxSshPass(ipJBoss, username, password);
							ret = sendOseSeverService.sendOseSever(url, documento);	
						}
					}
					if(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_RESUMEN_DIARIO)) 
						Thread.sleep(10000);
					else
						Thread.sleep(1000);
	    		}
        	} catch (Exception e){
    			StringWriter errors = new StringWriter();				
    			e.printStackTrace(new PrintWriter(errors));
    			log.error("procesarSendOseSeverFecha Exception \n"+errors);
            }  	   		
    	}
    }
    
    private void procesarSendOseSeverThread(List<Documento> list,  int total, Date fecJBossAct, int k) throws SOAPException { 
    	//int i=0;
    	for(int p=0; p< total; p++){ 
    		try {
    			Object lock = new Object();
    			Documento t1 = list.get(p);
    			log.info(k+"-"+p+" of "+total+" documento: "+t1.getNombre());
    			Thread A = new Thread(new Runnable() {
    	            @Override
    	            public void run() {
    	            	synchronized (lock) {
	    	            	try {	    	            				    	    			
		    	    			boolean boolFecha1 = false;
		    	    			if(t1.getTipoDocumento().trim().equals(Constantes.SUNAT_RESUMEN_DIARIO)) {
		    	    				String fechaGeneracion = UblResponseSunat.ObtenerFechaEmisionFromUBL(t1.getUbl());
		    	    				if(ShellCommand.validarFechaGeneracionRC(fechaGeneracion, fecJBossAct)) 
		    	    					boolFecha1 = true;
		    	    			}else {
		    	    				String fechaEmision = UblResponseSunat.ObtenerFechaEmisionFromUBL(t1.getUbl());	 		
		    		    			if(ShellCommand.validarFechaEmision(fechaEmision, fecJBossAct)) 
		    		    				boolFecha1 = true;
		    	    			}
		    	    			if(boolFecha1) {
		    	    				SendOseSeverService sendOseSeverService = new SendOseSeverService();
		    						String ret = sendOseSeverService.sendOseSever(url, t1);	
		    						log.info("runProgThread_A1: " +  ret );
		    						if((ret.trim().equals(Constantes.SUNAT_ERROR_2108))
		    								|| (ret.trim().equals(Constantes.SUNAT_ERROR_2236))
		    								|| (ret.trim().equals(Constantes.SUNAT_ERROR_2329)))
		    							System.exit(0);
		    						if((ret.trim().equals(Constantes.SUNAT_ERROR_0402))){
		    							lock.wait();
		    							ret = sendOseSeverService.sendOseSever(url, t1);	
			    						log.info("runProgThread_A2: " +  ret );
		    						}
		    	    			}
	    	            	} catch (Exception e){
	    	        			StringWriter errors = new StringWriter();				
	    	        			e.printStackTrace(new PrintWriter(errors));
	    	        			log.error("procesarSendOseSeverThreadException \n"+errors);
	    	                }  
	    	            }
    	            }
    	        });		
    			
    			p++;
    			if(p>=total) {
    				A.start();
    				return;
    			}
    			Documento t2 = list.get(p);
    			log.info(k+"-"+p+" of "+total+" documento: "+t2.getNombre());
    			Thread B = new Thread(new Runnable() {
    	            @Override
    	            public void run() {
    	            	synchronized (lock) {
	    	            	try {
		    	    			boolean boolFecha1 = false;
		    	    			if(t2.getTipoDocumento().trim().equals(Constantes.SUNAT_RESUMEN_DIARIO)) {
		    	    				String fechaGeneracion = UblResponseSunat.ObtenerFechaEmisionFromUBL(t2.getUbl());
		    	    				if(ShellCommand.validarFechaGeneracionRC(fechaGeneracion, fecJBossAct)) 
		    	    					boolFecha1 = true;
		    	    			}else {
		    	    				String fechaEmision = UblResponseSunat.ObtenerFechaEmisionFromUBL(t2.getUbl());	 		
		    		    			if(ShellCommand.validarFechaEmision(fechaEmision, fecJBossAct)) 
		    		    				boolFecha1 = true;
		    	    			}
		    	    			if(boolFecha1) {
		    	    				SendOseSeverService sendOseSeverService = new SendOseSeverService();
		    						String ret = sendOseSeverService.sendOseSever(url, t2);	
		    						log.info("runProgThread_B1: " +  ret );
		    						if((ret.trim().equals(Constantes.SUNAT_ERROR_2108))
		    								|| (ret.trim().equals(Constantes.SUNAT_ERROR_2236))
		    								|| (ret.trim().equals(Constantes.SUNAT_ERROR_2329)))
		    							System.exit(0);
		    						if((ret.trim().equals(Constantes.SUNAT_ERROR_0402))){
		    							ShellCommand.reStartJBossLinuxSshPass(ipJBoss, username, password);	
		    							ret = sendOseSeverService.sendOseSever(url, t2);	
			    						log.info("runProgThread_B2: " +  ret );
		    							lock.notify();
		    						}
		    	    			}
	    	            	} catch (Exception e){
	    	        			StringWriter errors = new StringWriter();				
	    	        			e.printStackTrace(new PrintWriter(errors));
	    	        			log.error("procesarSendOseSeverThreadException \n"+errors);
	    	                }  
	    	            }
    	            }
    	        });				
    			A.start();
    			B.start();
    			Thread.sleep(2000);
        	} catch (Exception e){
    			StringWriter errors = new StringWriter();				
    			e.printStackTrace(new PrintWriter(errors));
    			log.error("procesarSendOseSeverThreadException \n"+errors);
            }  	   		
    	}
    }    
 
    private List<Documento> getListComprobantesEstadoErrorNumero(String contar, 
    		String error_numero, String estado) {
        //log.info("getListComprobantes_Estado_ErrorNumero_Fecha_Contar ...");            
    	Documento tbcConsulta = new Documento();
    	tbcConsulta.setLongitudNombre(Integer.valueOf(contar));
    	tbcConsulta.setErrorNumero(error_numero);
    	String sEstado = "";
    	if((estado!=null) && !(estado.trim().isEmpty()))
    		sEstado = " in ('"+estado+"') ";
    	tbcConsulta.setEstado(sEstado);	   	
    	tbcConsulta.setUserCrea("0");
    	RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
    	List<Documento> list = rsetPostGresJDBC.listComprobantes(tbcConsulta);
    	return list;
    } 
    
    private List<Documento> getListComprobantesEstadoErrorNumeroFecha(String contar, 
    		String error_numero, String estado) {
        //log.info("getListComprobantes_Estado_ErrorNumero_Fecha_Contar ...");            
    	Documento tbcConsulta = new Documento();
    	tbcConsulta.setLongitudNombre(Integer.valueOf(contar));
    	tbcConsulta.setErrorNumero(error_numero);
    	String sEstado = "";
    	if((estado!=null) && !(estado.trim().isEmpty()))
    		sEstado = " in ('"+estado+"') ";
    	tbcConsulta.setEstado(sEstado);	 	   	
    	tbcConsulta.setUserCrea("2");
    	RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
    	List<Documento> list = rsetPostGresJDBC.listComprobantes(tbcConsulta);
    	return list;
    } 
    
    private List<Documento> getListComprobantesEstadoErrorNumeroContarDia(String contar, 
    		String error_numero, String estado, int year, int mes, int dia) {
        //log.info("getListComprobantes_Estado_ErrorNumero_Fecha_Contar ...");            
    	Documento tbcConsulta = new Documento();
    	tbcConsulta.setLongitudNombre(Integer.valueOf(contar));
    	tbcConsulta.setErrorNumero(error_numero);
    	//tbcConsulta.setObservaNumero(estado);
    	String sEstado = "";
    	if((estado!=null) && !(estado.trim().isEmpty()))
    		sEstado = " in ('"+estado+"') ";
    	tbcConsulta.setEstado(sEstado);	 
    	tbcConsulta.setUblVersion(" = "+year+" ");
    	tbcConsulta.setCustomizaVersion(" = "+mes+" ");
    	tbcConsulta.setErrorLogSunat(" = "+dia+" ");
    	tbcConsulta.setUserCrea("0");
    	RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
    	List<Documento> list = rsetPostGresJDBC.listComprobantes(tbcConsulta);
    	return list;
    } 

    private List<Documento> getListComprobantesEstadoErrorNumeroContarDiaELRUC(String contar, 
    		String error_numero, String estado, int year, int mes, int dia, String error_log, String ruc) {
        log.info("getListComprobantesEstadoErrorNumeroContarDiaELRUC ... 1");        
    	Documento tbcConsulta = new Documento();
    	tbcConsulta.setLongitudNombre(Integer.valueOf(contar));
    	tbcConsulta.setErrorNumero(error_numero);
    	//tbcConsulta.setObservaNumero(estado);
    	String sEstado = "";
    	if((estado!=null) && !(estado.trim().isEmpty()))
    		sEstado = " in ('"+estado+"') ";
    	tbcConsulta.setEstado(sEstado);	 
    	tbcConsulta.setUblVersion(" = "+year+" ");
    	tbcConsulta.setCustomizaVersion(" = "+mes+" ");
    	tbcConsulta.setErrorLogSunat(" = "+dia+" ");
    	tbcConsulta.setErrorLog(error_log);
    	tbcConsulta.setObservaDescripcion(ruc);  
    	tbcConsulta.setUserCrea("0");
    	RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
    	List<Documento> list = rsetPostGresJDBC.listComprobantes(tbcConsulta);                
    	return list;
    } 
    
    private List<Documento> getListComprobantesEstadoErrorNumeroContarDiaELFecha(String contar, 
    		String error_numero, String estado, int year, int mes, int dia, String error_log, String ruc) {
        log.info("getListComprobantesEstadoErrorNumeroContarDiaELFecha ...");     
       	Documento tbcConsulta = new Documento();
    	tbcConsulta.setLongitudNombre(Integer.valueOf(contar));
    	tbcConsulta.setErrorNumero(error_numero);
    	//tbcConsulta.setObservaNumero(estado);	 
    	String sEstado = "";
    	if((estado!=null) && !(estado.trim().isEmpty()))
    		sEstado = " in ('"+estado+"') ";
    	tbcConsulta.setEstado(sEstado);	 
    	tbcConsulta.setUblVersion(" = "+year+" ");
    	tbcConsulta.setCustomizaVersion(" = "+mes+" ");
    	tbcConsulta.setErrorLogSunat(" = "+dia+" ");
    	tbcConsulta.setErrorLog(error_log);
    	tbcConsulta.setObservaDescripcion(ruc); 
    	tbcConsulta.setUserCrea("1");
    	RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
    	List<Documento> list = rsetPostGresJDBC.listComprobantes(tbcConsulta);         
    	return list;
    } 
   
    
}
