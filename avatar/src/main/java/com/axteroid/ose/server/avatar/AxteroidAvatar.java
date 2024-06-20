package com.axteroid.ose.server.avatar;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.job.GestorOseJobProg;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.job.Programacion;

public class AxteroidAvatar {

	private static final Logger log = LoggerFactory.getLogger(AxteroidAvatar.class);

	public static void main(String[] args) throws InterruptedException {
		AxteroidAvatar axteroidAvatar = new AxteroidAvatar();
		axteroidAvatar.readAuto();
	}
	
	private void readAuto() {
		//String time = "0/1 * * * * ?" // cada segundo
		String time = "0 0/1 * * * ?"; // cada minuto		
		//String time = "0 1 0 ? * * *"; // cada minuto
		//String time = "0 0 1 ? * * *"; // cada 1 hora
		try {
			log.info("*********************************************************");
			log.info("  INICIO AUTO: AX-AVATAR "+Constantes.AVATAR_VERSION+": " + new Date());
			log.info("*********************************************************");
			log.info("  ");
			Programacion.start(time, GestorOseJobProg.class, "? PRODUCCION AX-AVATAR ");		
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			log.error("GestorOseJobProg Exception \n" + errors);
		}
	}		
	
}
