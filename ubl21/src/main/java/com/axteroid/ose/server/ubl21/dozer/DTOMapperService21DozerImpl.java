package com.axteroid.ose.server.ubl21.dozer;

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
public class DTOMapperService21DozerImpl implements DTOMapperService21 {
	
	private static final Logger log = LoggerFactory.getLogger(DTOMapperService21DozerImpl.class);
    private	Mapper mapper ;
    private ClassLoader webClassLoader;
    
    public DTOMapperService21DozerImpl (){
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
	   	//log.info("DTOMapperServiceDozerImpl -- init ");	   	   
	   	ClassLoader classLoader = getClass().getClassLoader();	
	   	
        mapper = new DozerBeanMapper(Arrays.asList( 
        		classLoader.getResource("dozer21/dozer-despatch.xml").toString()));   

	}

}
