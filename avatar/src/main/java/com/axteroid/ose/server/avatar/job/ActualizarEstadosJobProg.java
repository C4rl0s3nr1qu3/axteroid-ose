package com.axteroid.ose.server.avatar.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.worker.ActualizarEstadosWorker;
import com.axteroid.ose.server.tools.job.Programacion;

public class ActualizarEstadosJobProg  implements Job{
	static Logger log = LoggerFactory.getLogger(ActualizarEstadosJobProg.class);
    public ActualizarEstadosJobProg(){}

    public void execute(JobExecutionContext arg0) throws JobExecutionException{
		Programacion.pause();		
		ActualizarEstadosWorker actualizarEstadosWorker = new ActualizarEstadosWorker();				
		actualizarEstadosWorker.worker();
		Programacion.resume();					
    }
}
