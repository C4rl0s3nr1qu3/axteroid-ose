package com.axteroid.ose.server.repository.rules.impl;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.repository.dao.OseValiConsQryDAO;
import com.axteroid.ose.server.repository.dao.impl.OseValiConsQryDAOImpl;
import com.axteroid.ose.server.repository.model.OseValiConsBean;
import com.axteroid.ose.server.repository.rules.OseValiConsQryLocal;
import com.mongodb.MongoSecurityException;

public class OseValiConsQryImpl implements OseValiConsQryLocal{
	//private final static Logger log =  Logger.getLogger(OseValiConsQryImpl.class);
	private static final Logger log = LoggerFactory.getLogger(OseResponseQryImpl.class);
	public String getOseResponseBean(OseValiConsBean oseValiConsBean){ 
		//log.info("getOseValiConsBean ---> "+oseValiConsBean.getRucEmisor());
		try {			
			OseValiConsQryDAO oseValiConsQryDAO = new OseValiConsQryDAOImpl();
			return oseValiConsQryDAO.getOseResponseBean(oseValiConsBean);
		}catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getOseResponseBean MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getOseResponseBean Exception \n"+errors);
        }  
		return null;
	}
	
	public String getOseCpeBean(OseValiConsBean oseValiConsBean){ 
		//log.info("getOseValiConsBean ---> "+oseValiConsBean.getRucEmisor());
		try {			
			OseValiConsQryDAO oseValiConsQryDAO = new OseValiConsQryDAOImpl();
			return oseValiConsQryDAO.getOseCpeBean(oseValiConsBean);
		}catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getOseCpeBean MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getOseCpeBean Exception \n"+errors);
        }  
		return null;
	}
}
