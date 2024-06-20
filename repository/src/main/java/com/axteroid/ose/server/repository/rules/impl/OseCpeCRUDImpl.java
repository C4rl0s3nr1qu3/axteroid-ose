package com.axteroid.ose.server.repository.rules.impl;

import java.io.PrintWriter;
import java.io.StringWriter;

//import org.apache.log4j.Logger;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.repository.dao.TtOseCpeDAO;
import com.axteroid.ose.server.repository.dao.impl.TtOseCpeDAOImpl;
import com.axteroid.ose.server.repository.rules.OseCpeCRUDLocal;
import com.mongodb.MongoSecurityException;

public class OseCpeCRUDImpl implements OseCpeCRUDLocal{
	//private final static Logger log =  Logger.getLogger(OseCpeCRUDImpl.class);
	private static final Logger log = LoggerFactory.getLogger(OseCpeALLCRUDImpl.class);
	public void grabarOseCPE(Document document){
		//log.info("grabarOseCPE ");
		try {		
			TtOseCpeDAO ttOseCpeDAO = new TtOseCpeDAOImpl();
			ttOseCpeDAO.grabarOseCPE(document);
    	}catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabarOseCPE MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabarOseCPE Exception \n"+errors);
        }
	}		
}
