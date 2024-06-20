package com.axteroid.ose.server.avatar.job;

import java.sql.Timestamp;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.worker.ActualizarEstadosWorker;
import com.axteroid.ose.server.avatar.worker.ClienteWorker;
import com.axteroid.ose.server.avatar.worker.DescargarSunatWorker;
import com.axteroid.ose.server.avatar.worker.DetalleDocumentosWorker;
import com.axteroid.ose.server.avatar.worker.EnviarSunatWorker;
import com.axteroid.ose.server.avatar.worker.GestionarJBossWorker;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.job.Programacion;
import com.axteroid.ose.server.tools.util.DocumentUtil;

public class GestorOseJobProg  implements Job{
	static Logger log = LoggerFactory.getLogger(GestorOseJobProg.class);
    public GestorOseJobProg(){}

    public void execute(JobExecutionContext arg0) throws JobExecutionException{
		Programacion.pause();		
		log.info("============================================");
		log.info("  AX-AVATAR "+Constantes.AVATAR_VERSION+": " + new Date());
		log.info("============================================");			
		log.info(" ");
		Timestamp timestampOSEStart = new Timestamp(System.currentTimeMillis());
		log.info("   Inicio: "+ timestampOSEStart);
		ActualizarEstadosWorker actualizarEstadosWorker = new ActualizarEstadosWorker();				
		actualizarEstadosWorker.worker();
		GestionarJBossWorker gestionarJBossWorker = new GestionarJBossWorker();
		gestionarJBossWorker.worker();
		ClienteWorker clienteWorker = new ClienteWorker();				
		clienteWorker.worker();
		EnviarSunatWorker enviarSunatWorker = new EnviarSunatWorker();				
		enviarSunatWorker.worker();
		DescargarSunatWorker descargarSunatWorker = new DescargarSunatWorker();				
		descargarSunatWorker.worker();
		DetalleDocumentosWorker detalleDocumentosWorker = new DetalleDocumentosWorker();
		detalleDocumentosWorker.worker();
		Timestamp timestampOSEEnd = new Timestamp(System.currentTimeMillis());
		log.info("   Fin. "+ timestampOSEEnd);
		log.info("   Duracion: "+DocumentUtil.compareTwoTimeStamps(timestampOSEEnd, timestampOSEStart));		
		Programacion.resume();	
		System.exit(0);
    }
}
