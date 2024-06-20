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
import com.axteroid.ose.server.avatar.task.ShellCommand;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoAvatarPropertiesEnum;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.DirUtil;

public class GestionarJBossWorker {
    static Logger log = LoggerFactory.getLogger(GestionarJBossWorker.class);
    
    public void worker(){    		
		Map<String, String> mapa = DirUtil.getMapRobotProperties();
		if((mapa.get(TipoAvatarPropertiesEnum.GESTIONAR_JBOSS.getCodigo()) != null) &&
				(mapa.get(TipoAvatarPropertiesEnum.GESTIONAR_JBOSS.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
			log.info("GESTIONAR_JBOSS: " + mapa.get(TipoAvatarPropertiesEnum.GESTIONAR_JBOSS.getCodigo()));
			String ip = mapa.get(TipoAvatarPropertiesEnum.GESTIONAR_JBOSS_IP.getCodigo());
			String usuario = mapa.get(TipoAvatarPropertiesEnum.GESTIONAR_JBOSS_USUARIO.getCodigo());		
			String clave = mapa.get(TipoAvatarPropertiesEnum.GESTIONAR_JBOSS_CLAVE.getCodigo());
			String reiniciar = mapa.get(TipoAvatarPropertiesEnum.GESTIONAR_JBOSS_REINICIAR.getCodigo());				
			String shell = mapa.get(TipoAvatarPropertiesEnum.GESTIONAR_JBOSS_REINICIAR_SHELL.getCodigo());	
			if(reiniciar.trim().equals(Constantes.CONTENT_TRUE))
				this.reStartJBossLinux(ip, usuario, clave, shell);
		}		
    }
    
    private void reStartJBossLinux(String ip, String usuario, String clave, String shell){   	    	
    	try {   		
    		if(ShellCommand.curlJBossLinux(ip, usuario, clave, shell))
    			ShellCommand.reStartJBossLinux(ip, clave, shell);
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reStartJBossLinux Exception \n"+errors);
		}    	
    }      
}
