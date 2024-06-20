package com.axteroid.ose.server.repository.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;

//import org.apache.log4j.Logger;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.repository.conn.DaoMongoDB;
import com.axteroid.ose.server.repository.dao.TtOseCpeDAO;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoSecurityException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class TtOseCpeDAOImpl  extends DaoMongoDB implements TtOseCpeDAO{
	//private final static Logger log =  Logger.getLogger(TtOseCpeDAOImpl.class);
	private static final Logger log = LoggerFactory.getLogger(TtOseCpeDAOImpl.class);
	public TtOseCpeDAOImpl() {
		super();		
		conexionMongoClientOSE();
		conexionMongoDatabaseOSE();
	}
	
	public void grabarOseCPE(Document document){
		//log.info("grabarOseCPE ---> "+document.toJson());
		BasicDBObject query = new BasicDBObject();
		query.put("NUM_RUC", document.get("NUM_RUC")); 
		query.put("COD_CPE", document.get("COD_CPE")); 
		query.put("NUM_SERIE_CPE", document.get("NUM_SERIE_CPE")); 
		query.put("NUM_CPE", document.get("NUM_CPE")); 
		if(this.getOse_CPE(document, query))
			this.deleteOSE_CPE(document, query);
		this.insertOSE_CPE(document);		
		mongoClientOSE.close();
	}
	
	private boolean getOse_CPE(Document document, BasicDBObject query){    
		//log.info("getOse_CPE ---> "+query.toJson());
        try {        	            
            MongoCollection<Document> collection = mongoDatabaseOSE.getCollection("TT_OSE_CPE");              
            FindIterable<Document> findDocument = collection.find(query);
            MongoCursor<Document> cursorDocument = findDocument.iterator();
            while(cursorDocument.hasNext())          
                return true;   
            cursorDocument.close();
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getOse_CPE MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getOse_CPE Exception \n"+errors);
        }       
		return false;
    }     
	
    private void insertOSE_CPE(Document document){     
    	//log.info("insertOSE_CPE --> "+document.toJson());
        try {               
            MongoCollection<Document> collection = mongoDatabaseOSE.getCollection("TT_OSE_CPE");                    
            collection.insertOne(document);
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertOSE_CPE MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertOSE_CPE Exception \n"+errors);
		}
    }            
    
    private void deleteOSE_CPE(Document document, BasicDBObject query){
    	//log.info("deleteOSE_CPE ---> "+query.toJson());		
        try {                  
            MongoCollection<Document> collection = mongoDatabaseOSE.getCollection("TT_OSE_CPE");
            collection.deleteOne(query) ;
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("deleteOSE_CPE MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("deleteOSE_CPE Exception \n"+errors);
		}    	
    }	
}
