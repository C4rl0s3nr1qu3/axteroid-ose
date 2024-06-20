package com.axteroid.ose.server.rulesejb.emf.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class DAOEntityManagerCom {
	private static final Logger log = LoggerFactory.getLogger(DAOEntityManagerCom.class);
	protected static final String PERSISTENCE_UNIT_NAME = "axteroidPersistence";

	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    protected EntityManager em;
	
	protected EntityManagerFactory emf;
    
	public DAOEntityManagerCom() {
		super();
		getDAOEntityManagerCom();
	}
	
	private void getDAOEntityManagerCom(){
        if (emf != null){                	
        	if(!emf.isOpen())
        		conexionEntityManagerFactory();        	
        }else 
        	conexionEntityManagerFactory();
    }
	
	private void conexionEntityManagerFactory() {
		//log.info("conexionEntityManagerFactory ");
        Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY, InitialContextFactory.class.getName());
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        props.put(Context.PROVIDER_URL, "localhost:4447");	
		try {
			emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME,props);
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("conexionEntityManagerFactory Exception \n"+errors);
		}
	}

	public EntityManager getEntityManager() {
		return em;
	}	

	public void closeEntityManager() {				
		if (em != null && em.isOpen())			
			em.close();
	}
}
