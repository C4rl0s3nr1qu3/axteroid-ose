package com.axteroid.ose.server.avatar.worker;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.build.SendOseSeverBuild;
import com.axteroid.ose.server.avatar.build.UblFactoryBuild;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoAvatarPropertiesEnum;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.DirUtil;

public class ClienteWorker {
    static Logger log = LoggerFactory.getLogger(ClienteWorker.class);

    public void worker(){    	
		Map<String, String> mapa = DirUtil.getMapRobotProperties();			
		if((mapa.get(TipoAvatarPropertiesEnum.CLIENTE_ERROR_PFF.getCodigo())!=null) &&
			(mapa.get(TipoAvatarPropertiesEnum.CLIENTE_ERROR_PFF.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
			log.info("CLIENTE_ERROR_PFF: " + mapa.get(TipoAvatarPropertiesEnum.CLIENTE_ERROR_PFF.getCodigo()));
			String urlOSE = mapa.get(TipoAvatarPropertiesEnum.CLIENTE_ERROR_PFF_URL.getCodigo());
			String ip = mapa.get(TipoAvatarPropertiesEnum.CLIENTE_ERROR_PFF_IP.getCodigo());
			String usuario = mapa.get(TipoAvatarPropertiesEnum.CLIENTE_ERROR_PFF_USUARIO.getCodigo());
			String clave = mapa.get(TipoAvatarPropertiesEnum.CLIENTE_ERROR_PFF_CLAVE.getCodigo());
			String contar = mapa.get(TipoAvatarPropertiesEnum.CLIENTE_ERROR_PFF_CONTAR.getCodigo());			
			String error_numero = mapa.get(TipoAvatarPropertiesEnum.CLIENTE_ERROR_PFF_ERROR_NUMERO.getCodigo());
			String fechaIniJBoss = mapa.get(TipoAvatarPropertiesEnum.CLIENTE_ERROR_PFF_FECHA_INI_JBOSS.getCodigo());
			String fechaFinJBoss = mapa.get(TipoAvatarPropertiesEnum.CLIENTE_ERROR_PFF_FECHA_FIN_JBOSS.getCodigo());
			String fechaIniDB = mapa.get(TipoAvatarPropertiesEnum.CLIENTE_ERROR_PFF_FECHA_INI_DB.getCodigo());
			String fechaFinDB = mapa.get(TipoAvatarPropertiesEnum.CLIENTE_ERROR_PFF_FECHA_FIN_DB.getCodigo());
			Date fecJBossInicio = DateUtil.stringToDateYYYY_MM_DD(fechaIniJBoss);
			Date fecJBossFin = DateUtil.stringToDateYYYY_MM_DD(fechaFinJBoss); 
			Date fecDBInicio = DateUtil.stringToDateYYYY_MM_DD(fechaIniDB); 
			Date fecDBFin = DateUtil.stringToDateYYYY_MM_DD(fechaFinDB);
			//log.info("1) "+fecJBossInicio+" 2) "+fecJBossFin+" 3) "+fecDBInicio+" 4) "+fecDBFin);
			String ruc = mapa.get(TipoAvatarPropertiesEnum.CLIENTE_ERROR_PFF_RUC.getCodigo());
			int proceso = Integer.parseInt(mapa.get(TipoAvatarPropertiesEnum.CLIENTE_ERROR_PFF_PROCESO.getCodigo()));
			
			this.send2OseSever(urlOSE, ip, usuario, clave, contar, error_numero, proceso, ruc, 
					fecJBossInicio, fecJBossFin, fecDBInicio, fecDBFin);
		}
    } 
      
    private void send2OseSever(String urlOSE, String ip, String usuario, String clave, String contar, String error_numero, 
    		int proceso, String ruc, Date fecJBossInicio, Date fecJBossFin, Date fecDBInicio, Date fecDBFin) {
    	log.info("proceso: {} | fecJBossInicio: {} - fecJBossFin: {}  | fecDBInicio: {} - fecDBFin: {} ",
    			proceso, fecJBossInicio, fecJBossFin,fecDBInicio,fecDBFin);
    	try {       	
    		SendOseSeverBuild sendOseServerBuild = new SendOseSeverBuild(); 
        	String estado = "20";           	
    		if(proceso==20){
    			sendOseServerBuild.sendOseSever(urlOSE, ip, usuario, 
    					clave, contar, error_numero, estado, proceso);
    			error_numero = ruc;
    			sendOseServerBuild.sendOseSeverFecha(urlOSE, ip, usuario, 
    					clave, contar, error_numero, estado, proceso);    			
    			estado = "10";
    			error_numero = "";
    			sendOseServerBuild.sendOseSever(urlOSE, ip, usuario, 
    					clave, contar, error_numero, estado, proceso);    			
    		}		    		
    		if((proceso==50) || (proceso==51) || (proceso==60) || (proceso==61) || (proceso==70) || (proceso==71)){
    			String script = "";
    			if((proceso==50) || (proceso==51)|| (proceso==60) || (proceso==61))
    				ruc = null;
    			estado = "79"; 
    			log.info("error_numero: "+error_numero);
    			if(error_numero.equals("in ('9997')")) {
    				estado = "78";
    				ruc = null;
    			}
    			UblFactoryBuild ublFactoryBuild = new UblFactoryBuild();
    			ublFactoryBuild.repairSummeryUbl(urlOSE, ip, usuario, clave, contar, error_numero, estado, 
    				proceso, fecJBossInicio, fecJBossFin, fecDBInicio, fecDBFin, script, ruc); 
      		}    	    		
    		
    		if((proceso==80) || (proceso==81)|| (proceso==90) || (proceso==91) || (proceso==100) || (proceso==101)) {
    			String script = " (error_log is null or LENGTH(error_log) != 10) ";
    			if((proceso==80) || (proceso==81)|| (proceso==90) || (proceso==91))
    				ruc = null;
    			
    			sendOseServerBuild.sendOseSeverDiaELBD(urlOSE, ip, usuario, clave, contar, error_numero, estado, 
    				proceso, fecJBossInicio, fecJBossFin, fecDBInicio, fecDBFin, script, ruc); 
    			int procesoNuevo = proceso;
    			if((proceso==80) || (proceso==90) || (proceso==100)) procesoNuevo = 21080;
    			if((proceso==81) || (proceso==91) || (proceso==101)) procesoNuevo = 21081;
    			script = "(error_log is not null and LENGTH(error_log) = 10)";
    			sendOseServerBuild.sendOseSeverDiaJoinEL(urlOSE, ip, usuario, clave, contar, error_numero, estado, 
    				procesoNuevo, fecJBossInicio, fecJBossFin, fecDBInicio, fecDBFin, script, ruc);
    		}    		
	    } catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("send2OseSevere Exception \n"+errors);
		} 
    }
}
