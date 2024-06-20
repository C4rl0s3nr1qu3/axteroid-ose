package com.axteroid.ose.server.avatar.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.worker.EnviarSunatWorker;

public class EnviarSunatJob implements Job{

    static Logger log = LoggerFactory.getLogger(EnviarSunatJob.class);
    
    public EnviarSunatJob(){}

    public void execute(JobExecutionContext arg0) throws JobExecutionException{	
		EnviarSunatWorker sunatWorker = new EnviarSunatWorker();				
		sunatWorker.worker();	
    }
}

