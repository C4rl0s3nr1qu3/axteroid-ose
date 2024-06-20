package com.axteroid.ose.server.tools.job;

import java.io.PrintWriter;
import java.io.StringWriter;

//import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CronTriggerGestorOSEJob {
	//static Logger log = Logger.getLogger(CronTriggerGestorOSEJob.class);
	private static Logger log = LoggerFactory.getLogger(CronTriggerGestorOSEJob.class);
	public static void triggerGestorOSEJob(Class<? extends Job> tarea) {
		try {
			JobKey jobKeyA = new JobKey("jobA", "group1");
	    	JobDetail jobA = JobBuilder.newJob(tarea)
			.withIdentity(jobKeyA).build();   
	    	
	    	Trigger trigger1 = TriggerBuilder
	    			.newTrigger()
	    			.withIdentity("triggerGestorOSE_A", "group1")
	    			.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
	    			.build();
	    	
	    	Scheduler scheduler;		
			scheduler = new StdSchedulerFactory().getScheduler();
	    	scheduler.start();
	    	scheduler.scheduleJob(jobA, trigger1);
		} catch (SchedulerException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);
		}

	}
	
	public static void triggerGestorOSEJobFive(Class<? extends Job> tarea) {
		try {			
			JobKey jobKeyA = new JobKey("GestorOSE-A", "group1");
			JobDetail jobA = JobBuilder.newJob(tarea)
			.withIdentity(jobKeyA).build(); 
			
			JobKey jobKeyB = new JobKey("GestorOSE-B", "group1");
			JobDetail jobB = JobBuilder.newJob(tarea)
			.withIdentity(jobKeyB).build(); 
			
			JobKey jobKeyC = new JobKey("GestorOSE-C", "group1");
			JobDetail jobC = JobBuilder.newJob(tarea)
			.withIdentity(jobKeyC).build(); 
			
			JobKey jobKeyD = new JobKey("GestorOSE-D", "group1");
			JobDetail jobD = JobBuilder.newJob(tarea)
			.withIdentity(jobKeyD).build(); 
			
			JobKey jobKeyE = new JobKey("GestorOSE-E", "group1");
			JobDetail jobE = JobBuilder.newJob(tarea)
			.withIdentity(jobKeyE).build();    
			
			Trigger trigger1 = TriggerBuilder
					.newTrigger()
					.withIdentity("triggerGestorOSE_1", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
					.build();
			
			Trigger trigger2 = TriggerBuilder
					.newTrigger()
					.withIdentity("triggerGestorOSE_2", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
					.build();
			Trigger trigger3 = TriggerBuilder
					.newTrigger()
					.withIdentity("triggerGestorOSE_3", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
					.build();
			Trigger trigger4 = TriggerBuilder
					.newTrigger()
					.withIdentity("triggerGestorOSE_4", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
					.build();
			Trigger trigger5 = TriggerBuilder
					.newTrigger()
					.withIdentity("triggerGestorOSE_5", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
					.build();

			Scheduler scheduler;		
		
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.scheduleJob(jobA, trigger1);
			scheduler.scheduleJob(jobB, trigger2);
			scheduler.scheduleJob(jobC, trigger3);
			scheduler.scheduleJob(jobD, trigger4);
			scheduler.scheduleJob(jobE, trigger5);
			scheduler.start();
		} catch (SchedulerException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);
		}   	
	}
	
	public static void triggerGestorOSEJobTen(Class<? extends Job> tarea) {
		try {
			JobKey jobKeyA = new JobKey("GestorOSE-A", "group1");
	    	JobDetail jobA = JobBuilder.newJob(tarea)
			.withIdentity(jobKeyA).build(); 
	    	
			JobKey jobKeyB = new JobKey("GestorOSE-B", "group1");
	    	JobDetail jobB = JobBuilder.newJob(tarea)
			.withIdentity(jobKeyB).build(); 
	    	
			JobKey jobKeyC = new JobKey("GestorOSE-C", "group1");
	    	JobDetail jobC = JobBuilder.newJob(tarea)
			.withIdentity(jobKeyC).build(); 
	    	
			JobKey jobKeyD = new JobKey("GestorOSE-D", "group1");
	    	JobDetail jobD = JobBuilder.newJob(tarea)
			.withIdentity(jobKeyD).build(); 
	    	
			JobKey jobKeyE = new JobKey("GestorOSE-E", "group1");
	    	JobDetail jobE = JobBuilder.newJob(tarea)
			.withIdentity(jobKeyE).build();    
	    	
			JobKey jobKeyF = new JobKey("GestorOSE-F", "group1");
	    	JobDetail jobF = JobBuilder.newJob(tarea)
			.withIdentity(jobKeyF).build(); 
	    	
			JobKey jobKeyG = new JobKey("GestorOSE-G", "group1");
	    	JobDetail jobG = JobBuilder.newJob(tarea)
			.withIdentity(jobKeyG).build(); 
	    	
			JobKey jobKeyH = new JobKey("GestorOSE-H", "group1");
	    	JobDetail jobH = JobBuilder.newJob(tarea)
			.withIdentity(jobKeyH).build(); 
	    	
			JobKey jobKeyI = new JobKey("GestorOSE-I", "group1");
	    	JobDetail jobI = JobBuilder.newJob(tarea)
			.withIdentity(jobKeyI).build(); 
	    	
			JobKey jobKeyK = new JobKey("GestorOSE-K", "group1");
	    	JobDetail jobK = JobBuilder.newJob(tarea)
			.withIdentity(jobKeyK).build();        	
	    	
			Trigger trigger1 = TriggerBuilder
					.newTrigger()
					.withIdentity("triggerGestorOSE_1", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
					.build();			
			Trigger trigger2 = TriggerBuilder
					.newTrigger()
					.withIdentity("triggerGestorOSE_2", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
					.build();
			Trigger trigger3 = TriggerBuilder
					.newTrigger()
					.withIdentity("triggerGestorOSE_3", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
					.build();
			Trigger trigger4 = TriggerBuilder
					.newTrigger()
					.withIdentity("triggerGestorOSE_4", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
					.build();
			Trigger trigger5 = TriggerBuilder
					.newTrigger()
					.withIdentity("triggerGestorOSE_5", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
					.build();
			Trigger trigger6 = TriggerBuilder
					.newTrigger()
					.withIdentity("triggerGestorOSE_6", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
					.build();
			Trigger trigger7 = TriggerBuilder
					.newTrigger()
					.withIdentity("triggerGestorOSE_7", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
					.build();
			Trigger trigger8 = TriggerBuilder
					.newTrigger()
					.withIdentity("triggerGestorOSE_8", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
					.build();
			Trigger trigger9 = TriggerBuilder
					.newTrigger()
					.withIdentity("triggerGestorOSE_9", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
					.build();
			Trigger trigger10 = TriggerBuilder
					.newTrigger()
					.withIdentity("triggerGestorOSE_10", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
					.build();	
			Scheduler scheduler;		
		
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.scheduleJob(jobA, trigger1);
			scheduler.scheduleJob(jobB, trigger2);
			scheduler.scheduleJob(jobC, trigger3);
			scheduler.scheduleJob(jobD, trigger4);
			scheduler.scheduleJob(jobE, trigger5);
			scheduler.scheduleJob(jobF, trigger6);
			scheduler.scheduleJob(jobG, trigger7);
			scheduler.scheduleJob(jobH, trigger8);
			scheduler.scheduleJob(jobI, trigger9);
			scheduler.scheduleJob(jobK, trigger10);
			scheduler.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   	
	}	
}
