package com.axteroid.ose.server.tools.job;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Programacion {
	static Logger log = LoggerFactory.getLogger(Programacion.class);
	
    private static Scheduler horario = null;
    
    @SuppressWarnings("deprecation")
	public static void start(String date, Class<? extends Job> tarea, String name) {
    	log.info(" Iniciar   "+name);
        try {
            // Definimos la tarea (nombre de la tarea, nombre del grupo de tareas, Clase que implementa la tarea)
			@SuppressWarnings("serial")
			JobDetail jobDetail = new JobDetailImpl(name, Scheduler.DEFAULT_GROUP, tarea){};

            // Configuramos el Trigger que avisar� al planificador de cuando debe ejecutar la tarea, en este caso cada 5 segundos.
			CronTrigger trigger = new CronTriggerImpl(name, Scheduler.DEFAULT_GROUP, date);

            // Obtenemos el planificador
            horario = StdSchedulerFactory.getDefaultScheduler();

            // La tarea definida en JobDetail ser� ejecutada en los instantes especificados por el Trigger.
            horario.scheduleJob(jobDetail, trigger);

            // Iniciamos las tareas planificadas en el Sheduler
            horario.start();

        } catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);
        }
    }

    public static void pause() {
        try {
            horario.pauseAll();
        } catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);
        }
    }

    public static void resume() {
        try {
            horario.resumeAll();
        } catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);
        }
    }

    public static void stop() {
        try {
            horario.shutdown();
        } catch (Exception e) {
        	log.info(e.toString());
        }
    }
}
