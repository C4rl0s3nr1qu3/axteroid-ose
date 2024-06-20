package com.axteroid.ose.server.avatar.job;

import com.axteroid.ose.server.avatar.worker.EnviarSunatWorker;
import com.axteroid.ose.server.tools.job.Programacion;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnviarSunatOSEJobProg implements Job{

	static Logger log = LoggerFactory.getLogger(EnviarSunatOSEJobProg.class);
    public EnviarSunatOSEJobProg(){}

    public void execute(JobExecutionContext arg0) throws JobExecutionException{
		Programacion.pause();				
		EnviarSunatWorker enviarSunatWorker = new EnviarSunatWorker();				
		enviarSunatWorker.worker();
		Programacion.resume();					
    }

}

