package com.axteroid.ose.server.avatar.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.worker.ClienteWorker;
import com.axteroid.ose.server.tools.job.Programacion;

public class ClienteOseServerJobProg implements Job{
	static Logger log = LoggerFactory.getLogger(ClienteOseServerJobProg.class);
    public ClienteOseServerJobProg(){}

    public void execute(JobExecutionContext arg0) throws JobExecutionException{
		Programacion.pause();		
		ClienteWorker clienteWorker = new ClienteWorker();				
		clienteWorker.worker();
		Programacion.resume();					
    }
}
