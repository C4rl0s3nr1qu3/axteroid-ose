package com.axteroid.ose.server.avatar.worker;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.bean.GroupBean;
import com.axteroid.ose.server.avatar.dao.DocumentoPGDao;
import com.axteroid.ose.server.avatar.dao.impl.DocumentoPGDaoImpl;
import com.axteroid.ose.server.avatar.jdbc.RsetPostGresJDBC;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoAvatarPropertiesEnum;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.DirUtil;

public class ActualizarEstadosWorker {
    static Logger log = LoggerFactory.getLogger(ActualizarEstadosWorker.class);
    private int hora = 0;
    
    public void worker(){    	
    	Calendar rightNow = Calendar.getInstance();			
		hora = rightNow.get(Calendar.HOUR_OF_DAY);
		Map<String, String> mapa = DirUtil.getMapRobotProperties();
		if((mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_ESTADOS_GENERAL.getCodigo()) != null) &&
				(mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_ESTADOS_GENERAL.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
			log.info("ACTUALIZAR_ESTADOS_GENERAL: " + mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_ESTADOS_GENERAL.getCodigo()));
			int horaIni = Integer.parseInt(mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_ESTADOS_GENERAL_HORAINI.getCodigo()));
			int horaFin = Integer.parseInt(mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_ESTADOS_GENERAL_HORAFIN.getCodigo()));			
			this.actualiza_ESTADOS_GENERAL(horaIni, horaFin); 
		}		
    }
    
    private void actualiza_ESTADOS_GENERAL(int horaIni, int horaFin){   	
    	DocumentoPGDao documentoPGDao = new DocumentoPGDaoImpl();
    	try {  
    		//documentoPGDao.updateDocumentoAjusteGeneral();
    		documentoPGDao.updateDocumentoEstado30();
//    		if((hora >= horaIni) && (hora <= horaFin))
//    			documentoPGDao.updateTbComprobanteAjusteGeneral();
//    		else
//    			documentoPGDao.updateTbComprobanteAjusteEstado();   
    		
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("actualiza_ESTADOS_GENERAL Exception \n"+errors);
		}    	
    }    
    
    private void actualiza_ESTADOS_GENERAL__(int horaIni, int horaFin){   	
    	DocumentoPGDao documentoPGDao = new DocumentoPGDaoImpl();
    	try {   		
    		if((hora >= horaIni) && (hora <= horaFin))
    			documentoPGDao.updateTbComprobanteAjusteGeneral();
    		else
    			documentoPGDao.updateTbComprobanteAjusteEstado();
    		Date ayer = DateUtil.substractDaysToDate(new Date(), 7);
    		String sAyer = DateUtil.dateFormat(ayer, DateUtil.FMT_DAY());
    		RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
    		List<GroupBean> listGroupBean = rsetPostGresJDBC.countGrupoEstado();
    		boolean b71 = true, b72 = true, b73 = true, b74 = true, b75 = true, b76 = true, b77 = true;
    		Integer n = 500;
    		Integer n1 = 2000;
    		for(GroupBean gb : listGroupBean) {
    			log.info(gb.getEstado()+" | "+gb.getIntCount());
    			if(gb.getEstado().trim().equals("71")) {
    				if(gb.getIntCount() > n1)
    					b71 = false;    
    			}
    			if(gb.getEstado().trim().equals("72")) {
    				if(gb.getIntCount() > n)
    					b72 = false;
    			}
    			if(gb.getEstado().trim().equals("73")) {
    				if(gb.getIntCount() > n)
    					b73 = false;
    			}
    			if(gb.getEstado().trim().equals("74")) {
    				if(gb.getIntCount() > n)
    					b74 = false;
    			}
    			if(gb.getEstado().trim().equals("75")) {
    				if(gb.getIntCount() > n)
    					b75 = false;
    			}
    			if(gb.getEstado().trim().equals("76")) {
    				if(gb.getIntCount() > n)
    					b76 = false;
    			}	
    			if(gb.getEstado().trim().equals("77")) {
    				if(gb.getIntCount() > n)
    					b77 = false;
    			}	
    		}
    		if(b71)
    			//documentoPGDao.updateTbComprobanteNoRC();
    			documentoPGDao.updateTbComprobanteNoRARCRR();
    		if(b72)
    			//documentoPGDao.updateTbComprobanteRARR();
    			documentoPGDao.updateTbComprobanteRARCRR();
//    		if(b73)
//    			documentoPGDao.updateTbComprobanteRCLast(sAyer);
//    		if(b74)
//    			documentoPGDao.updateTbComprobanteRCFirst(sAyer);
    		if(b75)
    			documentoPGDao.updateTbComprobanteResumenEELast(sAyer);  
    		if(b76)
    			documentoPGDao.updateTbComprobanteResumenEEFirst(sAyer);  
    		if(b77)
    			documentoPGDao.updateTbComprobanteResumenEERUC();    		   		
    		
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("actualiza_ESTADOS_GENERAL Exception \n"+errors);
		}    	
    }      
    
}
