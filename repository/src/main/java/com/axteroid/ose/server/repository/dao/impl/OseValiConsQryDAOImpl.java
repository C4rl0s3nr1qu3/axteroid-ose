package com.axteroid.ose.server.repository.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
//import org.apache.log4j.Logger;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.repository.conn.DaoMongoDB;
import com.axteroid.ose.server.repository.dao.OseValiConsQryDAO;
import com.axteroid.ose.server.repository.model.OseValiConsBean;
import com.axteroid.ose.server.tools.constantes.TipoEstadoCDREnum;
import com.axteroid.ose.server.tools.util.LogDateUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoSecurityException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class OseValiConsQryDAOImpl extends DaoMongoDB implements OseValiConsQryDAO{
	//private final static Logger log =  Logger.getLogger(OseValiConsQryDAOImpl.class);
	private static final Logger log = LoggerFactory.getLogger(OseResponseQryDAOImpl.class);
	public OseValiConsQryDAOImpl(){
		super();		
		conexionMongoClientOSE();
		conexionMongoDatabaseOSE();
	}
	
	public String getOseResponseBean(OseValiConsBean oseValiConsBean){ 
    	//log.info("getOseValiConsBean ");
    	String numeroCPE = (new StringBuilder("El comprobante ").append(oseValiConsBean.getRucEmisor()).append("-").append(oseValiConsBean.getTipoComprobante()).append("-").append(oseValiConsBean.getSerie()).append("-").append(oseValiConsBean.getNumeroCorrelativo())).toString();
    	String resultado = (new StringBuilder(numeroCPE).append(" No ha sido informado a la OSE.")).toString();    	
    	try { 
    		BasicDBObject query = new BasicDBObject();
	    	query.put("RUC_EMISOR", oseValiConsBean.getRucEmisor());	
    		query.put("TIPO_COMPROBANTE_OSE", oseValiConsBean.getTipoComprobante());
    		
    		if(oseValiConsBean.getTipoDocumentoReceptor()!=null && !oseValiConsBean.getTipoDocumentoReceptor().isEmpty())
    			query.put("TIPO_DOCUMENTO_CLIENTE", oseValiConsBean.getTipoDocumentoReceptor());
    		if(oseValiConsBean.getNumeroDocumentoReceptor()!=null && !oseValiConsBean.getNumeroDocumentoReceptor().isEmpty())
    			query.put("NUMERO_DOCUMENTO_CLIENTE", oseValiConsBean.getNumeroDocumentoReceptor());    		
    		query.put("SERIE", oseValiConsBean.getSerie());
    		query.put("NUMERO_CORRELATIVO", oseValiConsBean.getNumeroCorrelativo());    
    		query.put("IMPORTE_TOTAL", oseValiConsBean.getImporteTotal()); 
       		Date fechaEmision = LogDateUtil.dateToDateYYYYMMDD(oseValiConsBean.getFechaEmision());
       		query.put("FECHA_EMISION", fechaEmision);       		   		
			return this.getStringOseResponseBean(numeroCPE, query);
    	}catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getListOseResponseBean MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getListOseResponseBean Exception \n"+errors);
        }  
    	return resultado;
	}
	
    private String getStringOseResponseBean(String numeroCPE, BasicDBObject query){     
    	log.info("getStringOseValiConsBean ---> "+query.toJson());
    	String resultado = (new StringBuilder(numeroCPE).append(" No ha sido informado a la OSE.")).toString();   
        try {        	
            MongoCollection<Document> collection = mongoDatabaseOSE.getCollection("TT_OSE_RESPONSE");
            FindIterable<Document> findDocument = collection.find(query);
            MongoCursor<Document> cursorDocument = findDocument.iterator();
            while(cursorDocument.hasNext()) {            
                Document doc = cursorDocument.next();
                String estado = (String)doc.get("ESTADO");
                if(estado.trim().equals(TipoEstadoCDREnum.CDR_PROCESO.getCodigo()) ||
                		estado.trim().equals(TipoEstadoCDREnum.CDR_ERRADO.getCodigo()))
					resultado = (new StringBuilder(numeroCPE).append(" no es válido, cuenta con comunicación de inconsistencia.")).toString();					
				if(estado.trim().equals(TipoEstadoCDREnum.CDR_GENERADO.getCodigo()) ||
						estado.trim().equals(TipoEstadoCDREnum.CDR_ENVIADO.getCodigo()) ||
						estado.trim().equals(TipoEstadoCDREnum.CDR_SERVICIO_INHABILITADO.getCodigo()) ||
						estado.trim().equals(TipoEstadoCDREnum.CDR_ERROR.getCodigo()) ||
						estado.trim().equals(TipoEstadoCDREnum.CDR_SIN_RETORNO.getCodigo()) ||
						estado.trim().equals(TipoEstadoCDREnum.CDR_RECHAZADO.getCodigo()) || 
						estado.trim().equals(TipoEstadoCDREnum.CDR_AUTORIZADO.getCodigo()))					
					resultado = (new StringBuilder(numeroCPE).append(" es válido, cuenta con CDR.")).toString();	
                return resultado;
            }     
            cursorDocument.close();
            mongoClientOSE.close();  
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getStringOseValiConsBean MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getStringOseValiConsBean Exception \n"+errors);
        }      
        return resultado;
    }       
    
	public String getOseCpeBean(OseValiConsBean oseValiConsBean){ 
    	//log.info("getOseCpeBean ");
    	String numeroCPE = (new StringBuilder("El comprobante ").append(oseValiConsBean.getRucEmisor()).append("-").append(oseValiConsBean.getTipoComprobante()).append("-").append(oseValiConsBean.getSerie()).append("-").append(oseValiConsBean.getNumeroCorrelativo())).toString();
    	String resultado = (new StringBuilder(numeroCPE).append(" No ha sido informado a la OSE.")).toString();    	
    	try { 
    		BasicDBObject query = new BasicDBObject();
	    	query.put("NUM_RUC", oseValiConsBean.getRucEmisor());	
    		query.put("COD_CPE", oseValiConsBean.getTipoComprobante());    			
    		query.put("NUM_SERIE_CPE", oseValiConsBean.getSerie());
    		query.put("NUM_CPE", oseValiConsBean.getNumeroCorrelativo());    		
    		query.put("MTO_IMPORTE_CPE", oseValiConsBean.getImporteTotal()); 
       		Date fechaEmision = LogDateUtil.dateToDateYYYYMMDD(oseValiConsBean.getFechaEmision());
       		query.put("FEC_EMISION_CPE", fechaEmision);             		   		
			return this.getStringOseCpeBean(numeroCPE, query);
    	}catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getOseCpeBean MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getOseCpeBean Exception \n"+errors);
        }  
    	return resultado;
	}
	
    private String getStringOseCpeBean(String numeroCPE, BasicDBObject query){     
    	log.info("getStringOseCpeBean ---> "+query.toJson());
    	String resultado = (new StringBuilder(numeroCPE).append(" No ha sido informado a la OSE.")).toString();   
        try {        	
            MongoCollection<Document> collection = mongoDatabaseOSE.getCollection("TT_OSE_CPE");
            FindIterable<Document> findDocument = collection.find(query);
            MongoCursor<Document> cursorDocument = findDocument.iterator();
            while(cursorDocument.hasNext()) {            
                Document doc = cursorDocument.next();
                String estado = (String)doc.get("IND_ESTADO_CPE");
                if(estado.trim().equals(TipoEstadoCDREnum.CDR_PROCESO.getCodigo()) ||
                		estado.trim().equals(TipoEstadoCDREnum.CDR_ERRADO.getCodigo()))
					resultado = (new StringBuilder(numeroCPE).append(" no es válido, cuenta con comunicación de inconsistencia.")).toString();					
				if(estado.trim().equals(TipoEstadoCDREnum.CDR_GENERADO.getCodigo()) ||
						estado.trim().equals(TipoEstadoCDREnum.CDR_ENVIADO.getCodigo()) ||
						estado.trim().equals(TipoEstadoCDREnum.CDR_SERVICIO_INHABILITADO.getCodigo()) ||
						estado.trim().equals(TipoEstadoCDREnum.CDR_ERROR.getCodigo()) ||
						estado.trim().equals(TipoEstadoCDREnum.CDR_SIN_RETORNO.getCodigo()) ||
						estado.trim().equals(TipoEstadoCDREnum.CDR_RECHAZADO.getCodigo()) || 
						estado.trim().equals(TipoEstadoCDREnum.CDR_AUTORIZADO.getCodigo()))					
					resultado = (new StringBuilder(numeroCPE).append(" es válido, cuenta con CDR.")).toString();	
                return resultado;
            }     
            cursorDocument.close();
            mongoClientOSE.close();  
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getStringOseCpeBean MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getStringOseCpeBean Exception \n"+errors);
        }      
        return resultado;
    }        
}
