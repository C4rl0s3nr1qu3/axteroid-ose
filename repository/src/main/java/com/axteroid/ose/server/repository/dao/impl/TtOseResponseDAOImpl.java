package com.axteroid.ose.server.repository.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;

//import org.apache.log4j.Logger;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.repository.conn.DaoMongoDB;
import com.axteroid.ose.server.repository.dao.TtOseResponseDAO;
import com.mongodb.MongoSecurityException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

public class TtOseResponseDAOImpl extends DaoMongoDB implements  TtOseResponseDAO{
	//private final static Logger log =  Logger.getLogger(TtOseResponseDAOImpl.class);
	private static final Logger log = LoggerFactory.getLogger(TtOseResponseDAOImpl.class);
	public TtOseResponseDAOImpl() {
		super();		
		conexionMongoClientOSE();
		conexionMongoDatabaseOSE();
	}
	
	public void grabarOseResponse(Document document){
		//log.info("grabarOseResponse ---> "+document.get("ID_COMPROBANTE"));
		if(this.getOseResponseBean(document))
			this.deleteOSE_RESPONSE(document);
		this.insertOSE_RESPONSE(document);		
		mongoClientOSE.close();
	}
	
	public boolean findOseResponse(Document document){
		//log.info("findOseResponse ---> "+document.get("ID"));
		//System.out.println("findOseResponse ---> "+document.get("ID"));
		boolean existe = this.getOseResponseBeanID(document);	
		mongoClientOSE.close();
		return existe;		
	}	
	
	private boolean getOseResponseBean(Document document){    
		//log.info("getOseResponseBean ---> "+document.get("ID_COMPROBANTE"));
		//System.out.println("getOseResponseBean ---> "+document.get("ID_COMPROBANTE"));
        try {        	            
            MongoCollection<Document> collection = mongoDatabaseOSE.getCollection("TT_OSE_RESPONSE");              
            FindIterable<Document> findDocument = collection.find(Filters.eq("ID_COMPROBANTE", document.get("ID_COMPROBANTE")));
            MongoCursor<Document> cursorDocument = findDocument.iterator();
            while(cursorDocument.hasNext()) {
            	Document doc = cursorDocument.next();
            	return true;   
            }      
            //log.info("getOseResponseBean ---> NO DATA ");
            cursorDocument.close();
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getOseResponseBean MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getOseResponseBean Exception \n"+errors);
        }       
		return false;
    }     
	
	private boolean getOseResponseBeanID(Document document){    
		log.info("getOseResponseBeanID ---> "+document.get("ID"));
		System.out.println("getOseResponseBeanID ---> "+document.get("ID"));
        try {        	            
            MongoCollection<Document> collection = mongoDatabaseOSE.getCollection("TT_OSE_RESPONSE");              
            FindIterable<Document> findDocument = collection.find(Filters.eq("ID", document.get("ID")));
            MongoCursor<Document> cursorDocument = findDocument.iterator();
            while(cursorDocument.hasNext())          
                return true;   
            //log.info("getOseResponseBeanID ---> NO DATA ");
            //System.out.println("getOseResponseBeanID ---> NO DATA ");
            cursorDocument.close();
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getOseResponseBean MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getOseResponseBean Exception \n"+errors);
        }       
		return false;
    }   
	
    private void insertOSE_RESPONSE(Document document){     
    	log.info("insertOSE_RESPONSE ---> "+document.get("ID_COMPROBANTE"));
        try {               
            MongoCollection<Document> collection = mongoDatabaseOSE.getCollection("TT_OSE_RESPONSE");                    
            collection.insertOne(document);
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertOSE_RESPONSE MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertOSE_RESPONSE Exception \n"+errors);
		}
    }            
    
    private void deleteOSE_RESPONSE(Document document){
    	log.info("deleteOSE_RESPONSE ---> "+document.get("ID_COMPROBANTE"));   	
        try {                  
            MongoCollection<Document> collection = mongoDatabaseOSE.getCollection("TT_OSE_RESPONSE");
            collection.deleteOne(Filters.eq("ID_COMPROBANTE", document.get("ID_COMPROBANTE"))) ;
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("updateOSE_RESPONSE MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("updateOSE_RESPONSE Exception \n"+errors);
		}    	
    }	            
}
