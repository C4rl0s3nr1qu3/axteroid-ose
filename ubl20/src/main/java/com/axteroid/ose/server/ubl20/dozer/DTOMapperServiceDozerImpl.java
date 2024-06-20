package com.axteroid.ose.server.ubl20.dozer;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


/**
 * User: RAC
 * Date: 06/03/12
 */
//@Component
public class DTOMapperServiceDozerImpl implements DTOMapperService {
	
	private static final Logger log = LoggerFactory.getLogger(DTOMapperServiceDozerImpl.class);
    private	Mapper mapper ;
    private ClassLoader webClassLoader;
    
    public DTOMapperServiceDozerImpl (){
    	//log.info("DTOMapperServiceDozerImpl -- DTOMapperServiceDozerImpl ");
    	 init();
    }

     public <T> T map(Object o, Class<T> tClass) {
    	 //log.info("DTOMapperServiceDozerImpl -- map ");
    	 return mapper.map(o, tClass);
    }

	public ClassLoader getWebClassLoader() {
		return webClassLoader;
	}

	public void init() {	   		   
	   	ClassLoader classLoader = getClass().getClassLoader();
	   	//log.info("init classLoader.getResource(): "+classLoader.getResource(""));	   
        mapper = new DozerBeanMapper(Arrays.asList(       
        		//UblConstantes.DIR_DOZER + File.separator+"dozer-peru.xml",
        		//UblConstantes.DIR_DOZER + File.separator+"dozer.xml",        		
        		classLoader.getResource("dozer20/dozer-credit-note.xml").toString(),
        		classLoader.getResource("dozer20/dozer-debit-note.xml").toString(),
        		classLoader.getResource("dozer20/dozer-despatch.xml").toString(),
        		classLoader.getResource("dozer20/dozer-invoice.xml").toString(),
        		classLoader.getResource("dozer20/dozer-perception.xml").toString(),        		
        		classLoader.getResource("dozer20/dozer-retention.xml").toString(),
        		classLoader.getResource("dozer20/dozer-reversion.xml").toString(),
        		classLoader.getResource("dozer20/dozer-summary.xml").toString(),          		
        		classLoader.getResource("dozer20/dozer-void.xml").toString()));          

	}

}
