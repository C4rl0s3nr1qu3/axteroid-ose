package com.axteroid.ose.server.avatar.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.worker.DescargarSunatWorker;
import com.axteroid.ose.server.tools.job.Programacion;

public class DescargarSunatJobProg implements Job{
	static Logger log = LoggerFactory.getLogger(DescargarSunatJobProg.class);
    public DescargarSunatJobProg(){}

    public void execute(JobExecutionContext arg0) throws JobExecutionException{
		Programacion.pause();		
		DescargarSunatWorker descargarSunatWorker = new DescargarSunatWorker();				
		descargarSunatWorker.worker();
		Programacion.resume();					
    }
}
