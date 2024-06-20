package com.axteroid.ose.server.avatar.build;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.xml.soap.SOAPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.axteroid.ose.server.avatar.model.TmCeDocumento;
import com.axteroid.ose.server.avatar.thread.FacturaOSEThread;
import com.axteroid.ose.server.avatar.thread.GetSatustThread;
import com.axteroid.ose.server.jpa.model.Documento;

public class ThreadBuild {
	private static final Logger log = LoggerFactory.getLogger(ThreadBuild.class);

    public void getStatusThread(String urlSunat,List<TmCeDocumento> list, int status, int nRun, long nEspera) throws SOAPException {    	
    	int total = list.size();    
    	long espera = nEspera;
    	for(int i=0; i< total; i++){     		
    		try {
    			int j = 1;
    			if(j <= nRun) {
    				TmCeDocumento t1 = list.get(i);
    				MDC.put("FILE", t1.getNombrearchivo());
    				log.info(i+" of "+total+" documento: "+t1.getNombrearchivo());
    				GetSatustThread R1 = new GetSatustThread(urlSunat, t1, status);
    				R1.start();
    				//Thread.sleep(espera);
    			}
			    j++;			    
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
				    TmCeDocumento t2 = list.get(i);
				    MDC.put("FILE", t2.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t2.getNombrearchivo());
				    GetSatustThread R2 = new GetSatustThread(urlSunat, t2, status);
				    R2.start();
				    //Thread.sleep(espera);
			    }			    
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
				    TmCeDocumento t3 = list.get(i);
				    MDC.put("FILE", t3.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t3.getNombrearchivo());
				    GetSatustThread R3 = new GetSatustThread(urlSunat, t3, status);
				    R3.start();
				    //Thread.sleep(espera);
			    }			    
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
	    			TmCeDocumento t4 = list.get(i);
	    			MDC.put("FILE", t4.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t4.getNombrearchivo());
					GetSatustThread R4 = new GetSatustThread(urlSunat, t4, status);
				    R4.start();
				    //Thread.sleep(espera);
			    }			    
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
				    TmCeDocumento t5 = list.get(i);
				    MDC.put("FILE", t5.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t5.getNombrearchivo());
				    GetSatustThread R5 = new GetSatustThread(urlSunat, t5, status);
				    R5.start();
				    Thread.sleep(espera);
			    }
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
				    TmCeDocumento t6 = list.get(i);
				    MDC.put("FILE", t6.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t6.getNombrearchivo());
				    GetSatustThread R6 = new GetSatustThread(urlSunat, t6, status);
				    R6.start();
				    //Thread.sleep(espera);
			    }
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
				    TmCeDocumento t7 = list.get(i);
				    MDC.put("FILE", t7.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t7.getNombrearchivo());
				    GetSatustThread R7 = new GetSatustThread(urlSunat, t7, status);
				    R7.start();
				    //Thread.sleep(espera);
			    }	
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
	    			TmCeDocumento t8 = list.get(i);
	    			MDC.put("FILE", t8.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t8.getNombrearchivo());
					GetSatustThread R8 = new GetSatustThread(urlSunat, t8, status);
				    R8.start();
				    //Thread.sleep(espera);
			    }
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
				    TmCeDocumento t9 = list.get(i);
				    MDC.put("FILE", t9.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t9.getNombrearchivo());
				    GetSatustThread R9 = new GetSatustThread(urlSunat, t9, status);
				    R9.start();
				    //Thread.sleep(espera);
			    }
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
				    TmCeDocumento t10 = list.get(i);
				    MDC.put("FILE", t10.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t10.getNombrearchivo());
				    GetSatustThread R10 = new GetSatustThread(urlSunat, t10, status);
				    R10.start();
				    //Thread.sleep(espera);
			    }
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
	    			TmCeDocumento t11 = list.get(i);
	    			MDC.put("FILE", t11.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t11.getNombrearchivo());
					GetSatustThread R11 = new GetSatustThread(urlSunat, t11, status);
				    R11.start();
				    //Thread.sleep(espera);
			    }
			    j++;
			    if(j <= nRun) {
			    i++;
				    if(i>=total) return;
				    TmCeDocumento t12 = list.get(i);
				    MDC.put("FILE", t12.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t12.getNombrearchivo());
				    GetSatustThread R12 = new GetSatustThread(urlSunat, t12, status);
				    R12.start();
				    //Thread.sleep(espera);
			    }
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
				    TmCeDocumento t13 = list.get(i);
				    MDC.put("FILE", t13.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t13.getNombrearchivo());
				    GetSatustThread R13 = new GetSatustThread(urlSunat, t13, status);
				    R13.start();
				    //Thread.sleep(espera);
			    }
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
	    			TmCeDocumento t14 = list.get(i);
	    			MDC.put("FILE", t14.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t14.getNombrearchivo());
					GetSatustThread R14 = new GetSatustThread(urlSunat, t14, status);
				    R14.start();
				    //Thread.sleep(espera);
			    }
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
				    TmCeDocumento t15 = list.get(i);
				    MDC.put("FILE", t15.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t15.getNombrearchivo());
				    GetSatustThread R15 = new GetSatustThread(urlSunat, t15, status);
				    R15.start();
				    //Thread.sleep(espera);
			    }
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
				    TmCeDocumento t16 = list.get(i);
				    MDC.put("FILE", t16.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t16.getNombrearchivo());
				    GetSatustThread R16 = new GetSatustThread(urlSunat, t16, status);
				    R16.start();
				    //Thread.sleep(espera);
			    }
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
				    TmCeDocumento t17 = list.get(i);
				    MDC.put("FILE", t17.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t17.getNombrearchivo());
				    GetSatustThread R17 = new GetSatustThread(urlSunat, t17, status);
				    R17.start();
				    //Thread.sleep(espera);
			    }
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
	    			TmCeDocumento t18 = list.get(i);
	    			MDC.put("FILE", t18.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t18.getNombrearchivo());
					GetSatustThread R18 = new GetSatustThread(urlSunat, t18, status);
				    R18.start();
				    //Thread.sleep(espera);
			    }
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
				    TmCeDocumento t19 = list.get(i);
				    MDC.put("FILE", t19.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t19.getNombrearchivo());
				    GetSatustThread R19 = new GetSatustThread(urlSunat, t19, status);
				    R19.start();
				    //Thread.sleep(espera);
			    }
			    j++;
			    if(j <= nRun) {
				    i++;
				    if(i>=total) return;
				    TmCeDocumento t20 = list.get(i);
				    MDC.put("FILE", t20.getNombrearchivo());
					log.info(i+" of "+total+" documento: "+t20.getNombrearchivo());
				    GetSatustThread R20 = new GetSatustThread(urlSunat, t20, status);
				    R20.start();	
				    //Thread.sleep(espera);
			    }
			    Thread.sleep(espera);
        	} catch (Exception e){
    			StringWriter errors = new StringWriter();				
    			e.printStackTrace(new PrintWriter(errors));
    			log.error("Exception \n"+errors);
            }  	   		
    	}
    }    
    public void getStatusThread_Hard(String urlSunat,List<TmCeDocumento> list, int status) throws SOAPException {    	
    	int total = list.size();
    	for(int i=0; i< total; i++){ 
    		try {
    			TmCeDocumento t1 = list.get(i);
				log.info(i+" of "+total+" documento: "+t1.getNombrearchivo());
				GetSatustThread R1 = new GetSatustThread(urlSunat, t1, status);
			    R1.start();
			    
			    i++;
			    if(i>=total) return;
			    TmCeDocumento t2 = list.get(i);
				log.info(i+" of "+total+" documento: "+t2.getNombrearchivo());
			    GetSatustThread R2 = new GetSatustThread(urlSunat, t2, status);
			    R2.start();
			    
			    i++;
			    if(i>=total) return;
			    TmCeDocumento t3 = list.get(i);
				log.info(i+" of "+total+" documento: "+t3.getNombrearchivo());
			    GetSatustThread R3 = new GetSatustThread(urlSunat, t3, status);
			    R3.start();
			    	
			    i++;
			    if(i>=total) return;
    			TmCeDocumento t4 = list.get(i);
				log.info(i+" of "+total+" documento: "+t4.getNombrearchivo());
				GetSatustThread R4 = new GetSatustThread(urlSunat, t4, status);
			    R4.start();
			    
			    i++;
			    if(i>=total) return;
			    TmCeDocumento t5 = list.get(i);
				log.info(i+" of "+total+" documento: "+t5.getNombrearchivo());
			    GetSatustThread R5 = new GetSatustThread(urlSunat, t5, status);
			    R5.start();
			    
			    i++;
			    if(i>=total) return;
			    TmCeDocumento t6 = list.get(i);
				log.info(i+" of "+total+" documento: "+t6.getNombrearchivo());
			    GetSatustThread R6 = new GetSatustThread(urlSunat, t6, status);
			    R6.start();
			    
			    i++;
			    if(i>=total) return;
			    TmCeDocumento t7 = list.get(i);
				log.info(i+" of "+total+" documento: "+t7.getNombrearchivo());
			    GetSatustThread R7 = new GetSatustThread(urlSunat, t7, status);
			    R7.start();
			    	
			    i++;
			    if(i>=total) return;
    			TmCeDocumento t8 = list.get(i);
				log.info(i+" of "+total+" documento: "+t8.getNombrearchivo());
				GetSatustThread R8 = new GetSatustThread(urlSunat, t8, status);
			    R8.start();
			    
			    i++;
			    if(i>=total) return;
			    TmCeDocumento t9 = list.get(i);
				log.info(i+" of "+total+" documento: "+t9.getNombrearchivo());
			    GetSatustThread R9 = new GetSatustThread(urlSunat, t9, status);
			    R9.start();
			    
			    i++;
			    if(i>=total) return;
			    TmCeDocumento t10 = list.get(i);
				log.info(i+" of "+total+" documento: "+t10.getNombrearchivo());
			    GetSatustThread R10 = new GetSatustThread(urlSunat, t10, status);
			    R10.start();

			    i++;
			    if(i>=total) return;
    			TmCeDocumento t11 = list.get(i);
				log.info(i+" of "+total+" documento: "+t11.getNombrearchivo());
				GetSatustThread R11 = new GetSatustThread(urlSunat, t11, status);
			    R11.start();
			    
			    i++;
			    if(i>=total) return;
			    TmCeDocumento t12 = list.get(i);
				log.info(i+" of "+total+" documento: "+t12.getNombrearchivo());
			    GetSatustThread R12 = new GetSatustThread(urlSunat, t12, status);
			    R12.start();
			    
			    i++;
			    if(i>=total) return;
			    TmCeDocumento t13 = list.get(i);
				log.info(i+" of "+total+" documento: "+t13.getNombrearchivo());
			    GetSatustThread R13 = new GetSatustThread(urlSunat, t13, status);
			    R13.start();
			    	
			    i++;
			    if(i>=total) return;
    			TmCeDocumento t14 = list.get(i);
				log.info(i+" of "+total+" documento: "+t14.getNombrearchivo());
				GetSatustThread R14 = new GetSatustThread(urlSunat, t14, status);
			    R14.start();
			    
			    i++;
			    if(i>=total) return;
			    TmCeDocumento t15 = list.get(i);
				log.info(i+" of "+total+" documento: "+t15.getNombrearchivo());
			    GetSatustThread R15 = new GetSatustThread(urlSunat, t15, status);
			    R15.start();
			    
			    i++;
			    if(i>=total) return;
			    TmCeDocumento t16 = list.get(i);
				log.info(i+" of "+total+" documento: "+t16.getNombrearchivo());
			    GetSatustThread R16 = new GetSatustThread(urlSunat, t16, status);
			    R16.start();
			    
			    i++;
			    if(i>=total) return;
			    TmCeDocumento t17 = list.get(i);
				log.info(i+" of "+total+" documento: "+t17.getNombrearchivo());
			    GetSatustThread R17 = new GetSatustThread(urlSunat, t17, status);
			    R17.start();
			    	
			    i++;
			    if(i>=total) return;
    			TmCeDocumento t18 = list.get(i);
				log.info(i+" of "+total+" documento: "+t18.getNombrearchivo());
				GetSatustThread R18 = new GetSatustThread(urlSunat, t18, status);
			    R18.start();
			    
			    i++;
			    if(i>=total) return;
			    TmCeDocumento t19 = list.get(i);
				log.info(i+" of "+total+" documento: "+t19.getNombrearchivo());
			    GetSatustThread R19 = new GetSatustThread(urlSunat, t19, status);
			    R19.start();
			    
			    i++;
			    if(i>=total) return;
			    TmCeDocumento t20 = list.get(i);
				log.info(i+" of "+total+" documento: "+t20.getNombrearchivo());
			    GetSatustThread R20 = new GetSatustThread(urlSunat, t20, status);
			    R20.start();	
			    
        	} catch (Exception e){
    			StringWriter errors = new StringWriter();				
    			e.printStackTrace(new PrintWriter(errors));
    			log.error("Exception \n"+errors);
            }  	   		
    	}
    }    
      
    
    public void updateFacturaOse(List<Documento> list) throws SOAPException {    	   	
    	int total = list.size();
    	for(int i=0; i< total; i++){   		
    		try {
    			Documento t1 = list.get(i);
				log.info(i+" of "+total+" documento: "+t1.getNombre());
				FacturaOSEThread R1 = new FacturaOSEThread(t1);
			    R1.start();
			    
			    i++;
			    if(i>=total) return;
			    Documento t2 = list.get(i);
				log.info(i+" of "+total+" documento: "+t2.getNombre());
				FacturaOSEThread R2 = new FacturaOSEThread(t2);
			    R2.start();
			    
			    i++;
			    if(i>=total) return;
    			Documento t3 = list.get(i);
				log.info(i+" of "+total+" documento: "+t3.getNombre());
				FacturaOSEThread R3 = new FacturaOSEThread(t3);
			    R3.start();
			    
			    i++;
			    if(i>=total) return;
			    Documento t4 = list.get(i);
				log.info(i+" of "+total+" documento: "+t4.getNombre());
				FacturaOSEThread R4 = new FacturaOSEThread(t4);
			    R4.start();
			    
			    i++;
			    if(i>=total) return;
    			Documento t5 = list.get(i);
				log.info(i+" of "+total+" documento: "+t5.getNombre());
				FacturaOSEThread R5 = new FacturaOSEThread(t5);
			    R5.start();
			    
			    i++;
			    if(i>=total) return;
			    Documento t6 = list.get(i);
				log.info(i+" of "+total+" documento: "+t6.getNombre());
				FacturaOSEThread R6 = new FacturaOSEThread(t6);
			    R6.start();
			    
			    i++;
			    if(i>=total) return;
    			Documento t7 = list.get(i);
				log.info(i+" of "+total+" documento: "+t7.getNombre());
				FacturaOSEThread R7 = new FacturaOSEThread(t7);
			    R7.start();
			    
			    i++;
			    if(i>=total) return;
			    Documento t8 = list.get(i);
				log.info(i+" of "+total+" documento: "+t8.getNombre());
				FacturaOSEThread R8 = new FacturaOSEThread(t8);
			    R8.start();
			    
			    i++;
			    if(i>=total) return;
    			Documento t9 = list.get(i);
				log.info(i+" of "+total+" documento: "+t9.getNombre());
				FacturaOSEThread R9 = new FacturaOSEThread(t9);
			    R9.start();
			    
			    i++;
			    if(i>=total) return;
			    Documento t10 = list.get(i);
				log.info(i+" of "+total+" documento: "+t10.getNombre());
				FacturaOSEThread R10 = new FacturaOSEThread(t10);
			    R10.start();
			    Thread.sleep(10000);

        	} catch (Exception e){
    			StringWriter errors = new StringWriter();				
    			e.printStackTrace(new PrintWriter(errors));
    			log.error("Exception \n"+errors);
            }  	   		
    	}
    }    
}
