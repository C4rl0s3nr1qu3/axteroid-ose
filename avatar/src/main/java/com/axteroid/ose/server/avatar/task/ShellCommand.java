package com.axteroid.ose.server.avatar.task;

import java.awt.AWTException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.LogDateUtil;

public class ShellCommand {
	private static final Logger log = LoggerFactory.getLogger(ShellCommand.class);
	
	public static void main(String[] args) throws InterruptedException, IOException, AWTException {
		//ShellCommand shellCommand = new ShellCommand();
		//shellRemote.ejecutarCambioFecha("sudo curl http://172.18.138.44:6080/ol-ti-itcpe/billService?wsdl", "172.18.138.44", "OseServer", "OseServer2018,");
		reStartJBossLinuxSshPass("172.18.138.44", "OseServer", "OseServer2018,");
		System.exit(0);
	}	
	public static boolean validarFechaEmision(String fechaEmision, Date fecJBossAct) {
		Date fecEmision = DateUtil.stringToDateYYYY_MM_DD(fechaEmision);			
		Date fecJBossActRest = DateUtil.substractDaysToDate(fecJBossAct, 7);
		Date fecJBossActAdd = DateUtil.addDays(fecJBossAct, 2);
		log.info("fecJBossActRest: "+fecJBossActRest+" - fecEmision: "+fecEmision+" - fecJBossAct: "+fecJBossAct+" - fecJBossActAdd: "+fecJBossActAdd);	
//		if((LogDateUtil.validarFechaEnRango(fecEmision, fecJBossActRest, fecJBossAct))
//				|| (LogDateUtil.validarFechaEnRango(fecEmision, fecJBossAct, fecJBossActAdd))) 
		if((LogDateUtil.validarFechaEnRango(fecEmision, fecJBossActRest, fecJBossAct)))
			return true;	
		return false;
	}
	    
	public static boolean validarFechaGeneracionRC(String fechaGeneracion, Date fecJBossAct) {
		Date fecGeneracion = DateUtil.stringToDateYYYY_MM_DD(fechaGeneracion);			
		Date fecJBossActRest = DateUtil.substractDaysToDate(fecJBossAct, 730);
		Date fecJBossActAdd = DateUtil.addDays(fecJBossAct, 2);
		log.info("fecJBossActRest: "+fecJBossActRest+" - fechaGeneracion: "+fechaGeneracion+" - fecJBossAct: "+fecJBossAct+" - fecJBossActAdd: "+fecJBossActAdd);	
		if((LogDateUtil.validarFechaEnRango(fecGeneracion, fecJBossActRest, fecJBossAct))) 
			return true;	
		return false;
	}
	
	public static void cambiarFechaJBoss(Date fecJBossAct, String ipJBoss, String username, String password) {
    	String strFecJBossAct = DateUtil.dateToStringYYYY_MM_DD(fecJBossAct);
		String [] dato = strFecJBossAct.split("-");
		String cadena = "sudo date "+dato[1]+""+dato[2]+"1200"+dato[0];			
		ShellRemote shellRemote = new ShellRemote();   			
		shellRemote.ejecutarCambioFecha(cadena, ipJBoss, username, password);
    }
	
	public static void cambiarFechaJBossLinux(Date fecJBossAct, String ipJBoss, String username, String password) {
    	String strFecJBossAct = DateUtil.dateToStringYYYY_MM_DD(fecJBossAct);
		String [] dato = strFecJBossAct.split("-");
		String cadena = "sudo date "+dato[1]+""+dato[2]+"1200"+dato[0];			
		String sshCadena = "sshpass -p "+password+" ssh -o StrictHostKeyChecking=no "+username+"@"+ipJBoss+" "+cadena;
		String respuestaCadena = ShellRemote.executeCommand(sshCadena, 120);
		log.info("respuestaCadena: "+respuestaCadena);
    }	
	
	public static void reStartJBossLinuxSshPass(String ipJBoss, String username, String password) {
		try {
			String cadenaStop = "sudo systemctl stop  jbosseap_73.service ";			
			String sshCadenaStop = "sshpass -p "+password+" ssh -o StrictHostKeyChecking=no "+username+"@"+ipJBoss+" "+cadenaStop;
			String respuestaCadenaStop = ShellRemote.executeCommand(sshCadenaStop, 120);
			log.info("respuestaCadenaStop: "+respuestaCadenaStop);		
			//Thread.sleep(120000);		
			String cadenaStart = "sudo systemctl start  jbosseap_73.service ";			
			String sshCadenaStart = "sshpass -p "+password+" ssh -o StrictHostKeyChecking=no "+username+"@"+ipJBoss+" "+cadenaStart;
			String respuestaCadenaStart = ShellRemote.executeCommand(sshCadenaStart, 180);
			log.info("respuestaCadenaStart: "+respuestaCadenaStart);
			//Thread.sleep(180000);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);
		}
    }	

	public static boolean curlJBossLinux(String ipJBoss, String username, String password, String shell) {
		boolean curl = true;
		try {
			//String cadena = "sudo curl http://172.18.138.44:6080/ol-ti-itcpe/billService?wsdl -k ";			
			//String sshCadena = "sshpass -p "+password+" ssh -o StrictHostKeyChecking=no "+username+"@"+ipJBoss+" "+shell;
			String respuestaCadena = ShellRemote.executeCommand(shell,120);
			//log.info("respuestaCadena: "+respuestaCadena);	
			log.info("respuestaCadena.length(): "+respuestaCadena.length());			
			//Thread.sleep(120000);	
			if(respuestaCadena.length() >= 10369)
			 	curl = false;
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);
		}
		return curl;
    }	
	
	public static void reStartJBossLinux(String ipJBoss, String username, String password) {
		try {
			String cadenaStop = "sudo systemctl stop  jbosseap_73.service ";			
			//String sshCadenaStop = "sshpass -p "+password+" ssh -o StrictHostKeyChecking=no "+username+"@"+ipJBoss+" "+cadenaStop;
			String respuestaCadenaStop = ShellRemote.executeCommand(cadenaStop, 120);
			log.info("respuestaCadenaStop: "+respuestaCadenaStop);		
			//Thread.sleep(120000);		
			String cadenaStart = "sudo systemctl start  jbosseap_73.service ";			
			//String sshCadenaStart = "sshpass -p "+password+" ssh -o StrictHostKeyChecking=no "+username+"@"+ipJBoss+" "+cadenaStart;
			String respuestaCadenaStart = ShellRemote.executeCommand(cadenaStart,180);
			log.info("respuestaCadenaStart: "+respuestaCadenaStart);
			//Thread.sleep(180000);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);
		}
    }	
		
}
