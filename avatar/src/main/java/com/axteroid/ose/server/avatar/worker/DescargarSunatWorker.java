package com.axteroid.ose.server.avatar.worker;

import java.util.Calendar;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.task.SunatListDownload;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoAvatarPropertiesEnum;
import com.axteroid.ose.server.tools.util.DirUtil;

public class DescargarSunatWorker {
    static Logger log = LoggerFactory.getLogger(DescargarSunatWorker.class);
    private SunatListDownload sunatListDownload = new SunatListDownload();
    private int hora = 0;
    
    public void worker(){    			
		Calendar rightNow = Calendar.getInstance();			
		hora = rightNow.get(Calendar.HOUR_OF_DAY);

		Map<String, String> mapa = DirUtil.getMapRobotProperties();											
		if((mapa.get(TipoAvatarPropertiesEnum.DESCARGA_SUNAT.getCodigo()) !=null) &&
				(mapa.get(TipoAvatarPropertiesEnum.DESCARGA_SUNAT.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
			log.info("DESCARGA_SUNAT: " + mapa.get(TipoAvatarPropertiesEnum.DESCARGA_SUNAT.getCodigo()));
			int descargarHora = Integer.parseInt(mapa.get(TipoAvatarPropertiesEnum.DESCARGA_SUNAT_HORA.getCodigo()));
			this.descargaSunatOSE(descargarHora);
		}
		
		if((mapa.get(TipoAvatarPropertiesEnum.MIGRA_DB2_TM_TS.getCodigo())!= null) &&
				mapa.get(TipoAvatarPropertiesEnum.MIGRA_DB2_TM_TS.getCodigo()).trim().equals(Constantes.CONTENT_TRUE)) {
			log.info("MIGRA_DB2_TM_TS " + mapa.get(TipoAvatarPropertiesEnum.MIGRA_DB2_TM_TS.getCodigo()));
			int horaIni = Integer.parseInt(mapa.get(TipoAvatarPropertiesEnum.MIGRA_DB2_TM_TS_HORAINI.getCodigo()));
			int horaFin = Integer.parseInt(mapa.get(TipoAvatarPropertiesEnum.MIGRA_DB2_TM_TS_HORAFIN.getCodigo()));
			this.migrarDB2_TM2TS(horaIni, horaFin);
		}				
    }
	
    private void descargaSunatOSE(int horaDes) {    	
		if(hora == horaDes) {	    	
	    	sunatListDownload.actualizarListasSunat("");
		}
    }
   
    private void migrarDB2_TM2TS(int horaIni, int horaFin) {    	
		if((hora >= horaIni) && (hora <=horaFin)) {

		}
    }    
}
