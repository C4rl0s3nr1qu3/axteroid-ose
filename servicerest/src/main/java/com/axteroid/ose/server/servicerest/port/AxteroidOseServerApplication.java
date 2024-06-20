package com.axteroid.ose.server.servicerest.port;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.constantes.Constantes;


@ApplicationPath("/rest")
public class AxteroidOseServerApplication extends Application{
	private static final Logger log = LoggerFactory.getLogger(AxteroidOseServerApplication.class);
	
	private Set<Object> singletons = new HashSet<Object>();

    public AxteroidOseServerApplication() {
    	log.info("AxteroidOseServer REST {}",Constantes.OSE_VERSION);
        singletons.add(new AxteroidOseServerService());
    }

    @Override
    public Set<Object> getSingletons() {
    	//log.info("getSingletons");
        return singletons;
    }	
	
}
