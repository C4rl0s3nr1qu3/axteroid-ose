package com.axteroid.ose.server.repository.rules.impl;

import java.io.PrintWriter;
import java.io.StringWriter;

//import org.apache.log4j.Logger;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.repository.dao.impl.TtOseCpeALLDAOImpl;
import com.axteroid.ose.server.repository.rules.OseCpeALLCRUDLocal;
import com.mongodb.MongoSecurityException;

public class OseCpeALLCRUDImpl implements OseCpeALLCRUDLocal{
	//private final static Logger log =  Logger.getLogger(OseCpeALLCRUDImpl.class);
	private static final Logger log = LoggerFactory.getLogger(OseCpeALLCRUDImpl.class);
	public void grabarOseCPEALL(Document document){
		//log.info("grabarOseCPE ");
		try {		
			TtOseCpeALLDAOImpl TtOseCpeALLDAOImpl = new TtOseCpeALLDAOImpl();
			TtOseCpeALLDAOImpl.grabarOseCPEALL(document);
    	}catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabarOseCPEALL MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabarOseCPEALL Exception \n"+errors);
        }
	}	
}
