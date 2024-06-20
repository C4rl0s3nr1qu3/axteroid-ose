package com.axteroid.ose.server.repository.rules.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.repository.dao.TTOseServerDao;
import com.axteroid.ose.server.repository.dao.impl.TTOseServerDaoImpl;
import com.axteroid.ose.server.repository.rules.TTOseServerQry;
import com.mongodb.MongoSecurityException;

public class TTOseServerQryImpl implements TTOseServerQry{
	private static final Logger log = LoggerFactory.getLogger(TTOseServerQryImpl.class);
	
	public List<Documento> findTTOseServer(Documento tbComprobante){ 
		log.info("findTTOseServer ---> "+tbComprobante.getId());
		List<Documento> results = new ArrayList<Documento>();
		boolean tipoID = false;
		try {
			if((tbComprobante.getId()!=null)
					&&  (tbComprobante.getId() > 0)) 
				tipoID = true;
			if(tipoID) {
				Document document = new Document();
				document.append("ID", tbComprobante.getId());
				TTOseServerDao ttOseServerDao = new TTOseServerDaoImpl();
				ttOseServerDao.findTTOseServerID(document);
			}
			
		}catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("findTTOseServer MongoSecurityException \n"+errors);	
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("findTTOseServer Exception \n"+errors);
        }  
		return results;
	}
}
