package com.axteroid.ose.server.repository.rules.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

//import org.apache.log4j.Logger;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.repository.dao.OseResponseQryDAO;
import com.axteroid.ose.server.repository.dao.TtOseResponseDAO;
import com.axteroid.ose.server.repository.dao.impl.OseResponseQryDAOImpl;
import com.axteroid.ose.server.repository.dao.impl.TtOseResponseDAOImpl;
import com.axteroid.ose.server.repository.model.OseQueryBean;
import com.axteroid.ose.server.repository.model.OseResponseBean;
import com.axteroid.ose.server.repository.rules.OseResponseQryLocal;
import com.mongodb.MongoSecurityException;

public class OseResponseQryImpl implements OseResponseQryLocal{
	//private final static Logger log =  Logger.getLogger(OseResponseQryImpl.class);
	private static final Logger log = LoggerFactory.getLogger(OseResponseQryImpl.class);
	public List<OseResponseBean> getListOseResponseBean(OseQueryBean oseQueryBean){ 
		//log.info("getListOseResponseBean ---> "+oseQueryBean.getListRucEmisor());
		try {
			OseResponseQryDAO qryOseResponseDAO = new OseResponseQryDAOImpl();
			return qryOseResponseDAO.getListOseResponseBean(oseQueryBean);
		}catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getListOseResponseBean MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getListOseResponseBean Exception \n"+errors);
        }  
		return null;
	}
	
	public boolean findOseResponseBean(String id){ 
		log.info("findOseResponseBean ---> "+id);
		System.out.println("findOseResponseBean ---> "+id);
		try {
			Document document = new Document();
			document.append("ID", id);
			TtOseResponseDAO ttOseResponseDAO = new TtOseResponseDAOImpl();
			return ttOseResponseDAO.findOseResponse(document);
		}catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("findOseResponseBean MongoSecurityException \n"+errors);	
			System.out.println("findOseResponseBean MongoSecurityException \n"+errors);	
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("findOseResponseBean Exception \n"+errors);
			System.out.println("findOseResponseBean Exception \n"+errors);
        }  
		return false;
	}
	
}
