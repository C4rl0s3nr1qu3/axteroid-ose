package com.axteroid.ose.server.repository.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.repository.conn.DaoMongoDB;
import com.axteroid.ose.server.repository.dao.TTOseServerDao;
import com.axteroid.ose.server.tools.constantes.TipoCollectionEnum;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoSecurityException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class TTOseServerDaoImpl extends DaoMongoDB implements TTOseServerDao{
	private static final Logger log = LoggerFactory.getLogger(TTOseServerDaoImpl.class);
	
	public TTOseServerDaoImpl() {
		super();		
		conexionMongoClientOSE();
		conexionMongoDatabaseOSE();
	}
	
	public boolean findTTOseServerID(Document document){
		log.info("findTTOseServerID ---> "+document.get("ID"));
		//System.out.println("findOseResponse ---> "+document.get("ID"));
		BasicDBObject query = new BasicDBObject();
		query.put("ID", document.get("ID")); 
		String nameCollection = this.validarTipoDocumento("01");
		
		boolean existe = this.getTTOseServer(query, nameCollection);	
		mongoClientOSE.close();
		return existe;		
	}	
	
//	private boolean getTTOseServerID(BasicDBObject query){    
//		log.info("getTTOseServerID ---> "+query.toJson());
//        try {        	            
//            MongoCollection<Document> collection = mongoDatabaseOSE.getCollection("TT_OSE_INVOICE_RESPONSE");              
//            FindIterable<Document> findDocument = collection.find(query);
//            MongoCursor<Document> cursorDocument = findDocument.iterator();
//            while(cursorDocument.hasNext())          
//                return true;   
//            log.info("getTTOseServerID ---> NO DATA ");
//            cursorDocument.close();
//        }catch(MongoSecurityException e) {
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));
//			log.error("getOseResponseBean MongoSecurityException \n"+errors);		
//		}catch(Exception e) {
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));
//			log.error("getOseResponseBean Exception \n"+errors);
//        }       
//		return false;
//    }	
	
	public boolean findTTOseServerDoc(Document document){
		log.info("findTTOseServerID ---> "+document.get("NUM_RUC"));
		BasicDBObject query = new BasicDBObject();
		query.put("NUM_RUC", document.get("NUM_RUC")); 
		query.put("COD_CPE", document.get("COD_CPE")); 
		query.put("NUM_SERIE_CPE", document.get("NUM_SERIE_CPE")); 
		query.put("NUM_CPE", document.get("NUM_CPE")); 
		String codCpe = (String) document.get("COD_CPE");
		
		String nameCollection = this.validarTipoDocumento(codCpe);
		boolean existe = this.getTTOseServer(query,nameCollection);	
		mongoClientOSE.close();
		return existe;		
	}
	
	private boolean getTTOseServer(BasicDBObject query,String nameCollection){    
		log.info("getTTOseServerID ---> "+query.toJson());
        try {        	            
            MongoCollection<Document> collection = mongoDatabaseOSE.getCollection(nameCollection);              
            FindIterable<Document> findDocument = collection.find(query);
            MongoCursor<Document> cursorDocument = findDocument.iterator();
            while(cursorDocument.hasNext())          
                return true;   
            log.info("getTTOseServerID ---> NO DATA ");
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

	private String validarTipoDocumento(String documentType){
		String nameCollection = "";
		String enumTipoDocumento = "TD"+documentType;		
//		List<String> listTipoDocumento = Stream.of(TipoCollectionEnum.values())
//                .map(TipoCollectionEnum::getCodigo(), e -> e).collect(Collectors.toList());		
		Map<String, String> mapTipoCollectionEnum = Stream.of( TipoCollectionEnum.values() ).collect( Collectors.toMap( k -> k.getCodigo(), v -> v.getDescripcion() ) );

		//String sTipoDoc = mapTipoCollectionEnum.stream().filter(x ->  enumTipoDocumento.equals(x)).findAny().orElse("");
		//nameCollection = mapTipoCollectionEnum.get(enumTipoDocumento);
		nameCollection = mapTipoCollectionEnum.get(documentType);
		log.info("nameCollection: {}",nameCollection);
	
		return nameCollection;
	}
	
	private String getNameCollection(String documentType) {		
		switch(documentType) {			
		case "01":
			return "TT_OSE_INVOICE_RESPONSE";		
		case "03":
			return "TT_OSE_VOUCHER_RESPONSE";			
		case "07":
			return "TT_OSE_CREDIT_NOTE_RESPONSE";			
		case "08":
			return "TT_OSE_DEBIT_NOTE_RESPONSE";			
		case "09":
			return "TT_OSE_DESPATCH_ADVICE_RESPONSE";			
		case "20":
			return "TT_OSE_RETENTION_RESPONSE";			
		case "40":
			return "TT_OSE_PERCEPTION_RESPONSE";			
		case "RA":
			return "TT_OSE_CANCEL_SUMMARY_RESPONSE";			
		case "RC":
			return "TT_OSE_VOUCHER_SUMMARY_RESPONSE";			
		case "RR":
			return "TT_OSE_REVERTION_SUMMARY_RESPONSE";		
		default:
			return "TT_ERROR";	
		}
	}
}
