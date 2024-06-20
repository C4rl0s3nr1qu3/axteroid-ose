package com.axteroid.ose.server.repository.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.repository.conn.DaoMongoDB;
import com.axteroid.ose.server.repository.dao.OseResponseQryDAO;
import com.axteroid.ose.server.repository.model.OseQueryBean;
import com.axteroid.ose.server.repository.model.OseResponseBean;
import com.axteroid.ose.server.tools.util.LogDateUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoSecurityException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class OseResponseQryDAOImpl extends DaoMongoDB implements OseResponseQryDAO{
	//private final static Logger log =  Logger.getLogger(OseResponseQryDAOImpl.class);
	private static final Logger log = LoggerFactory.getLogger(OseResponseQryDAOImpl.class);
	public OseResponseQryDAOImpl() {
		super();		
		conexionMongoClientOSE();
		conexionMongoDatabaseOSE();
	}
	
	public List<OseResponseBean> getListOseResponseBean(OseQueryBean oseQueryBean){ 
    	//log.info("getListOseResponseBean ---> "+oseQueryBean.getListRucEmisor());
    	try { 
	    	List<OseResponseBean> listOseResponseBean = new ArrayList<OseResponseBean>();
	    	List<Long> listBean = new ArrayList<Long>(); 
	    	for(String s : oseQueryBean.getListRucEmisor()) {
	    		Long l = Long.parseLong(s);
	    		listBean.add(l);
	    	}	    			    		
	    	BasicDBObject query = new BasicDBObject();
	    	query.put("RUC_EMISOR", new BasicDBObject("$in", listBean));	    	
    		query.put("NUMERO_DOCUMENTO_CLIENTE", oseQueryBean.getNumeroDocumentoCliente());
    		if(oseQueryBean.getTipoComprobante()!=null)
    			query.put("TIPO_COMPROBANTE", oseQueryBean.getTipoComprobante());
    		if(oseQueryBean.getEstadoOse()!=null) 
			query.put("ESTADO", oseQueryBean.getEstadoOse()); 	
    		
    		String [] snd = oseQueryBean.getSerieNumeroDesde().split("-");
    		if((oseQueryBean.getSerieNumeroDesde()!=null) && (snd.length == 2)) {
    			query.put("SERIE", snd[0]);
    			query.put("NUMERO_CORRELATIVO", snd[1]);
    		}
    		String [] snh = oseQueryBean.getSerieNumeroHasta().split("-");
       		if((oseQueryBean.getSerieNumeroHasta()!=null) && (snh.length == 2))
    			query.put("NUMERO_CORRELATIVO", new BasicDBObject("$gte", snd[1]).append("$lte", snh[1]));
    		
       		if((oseQueryBean.getFechaEmisionDesde() !=null) && (oseQueryBean.getFechaEmisionHasta() != null)) {
       			Date fechaDesde = LogDateUtil.dateToDateYYYYMMDD(oseQueryBean.getFechaEmisionDesde());
       			Date fechaHasta = LogDateUtil.dateToDateYYYYMMDD(oseQueryBean.getFechaEmisionHasta());
    			query.put("FECHA_EMISION", new BasicDBObject("$gte", fechaDesde).append("$lte", fechaHasta));
    		}else if((oseQueryBean.getFechaEmisionDesde() !=null)) {
    			Date fechaDesde = LogDateUtil.dateToDateYYYYMMDD(oseQueryBean.getFechaEmisionDesde());
    			query.put("FECHA_EMISION", fechaDesde);
    		}
	
    		this.getListOseResponseBean(oseQueryBean, query, listOseResponseBean);
			return listOseResponseBean;
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
	
    private void getListOseResponseBean(OseQueryBean oseQueryBean, BasicDBObject query,
    		List<OseResponseBean> listOseResponseBean){     
    	//log.info("getListOseResponseBean ---> "+query.toJson());
        try {        	
            MongoCollection<Document> collection = mongoDatabaseOSE.getCollection("TT_OSE_RESPONSE");
            FindIterable<Document> findDocument = collection.find(query);
            MongoCursor<Document> cursorDocument = findDocument.iterator();
            while(cursorDocument.hasNext()) {            
            	OseResponseBean oseResponseBean = new OseResponseBean();
                Document doc = cursorDocument.next();
                //log.info(doc.toJson());
                oseResponseBean.setId((Long)doc.get("ID"));
                oseResponseBean.setIdComprobante((String)doc.get("ID_COMPROBANTE"));                
                oseResponseBean.setRucEmisor((Long)doc.get("RUC_EMISOR"));
                oseResponseBean.setTipoComprobante((String)doc.get("TIPO_COMPROBANTE"));
                oseResponseBean.setEstado((String)doc.get("ESTADO"));
                oseResponseBean.setSerie((String)doc.get("SERIE"));
                oseResponseBean.setNumeroCorrelativo((String)doc.get("NUMERO_CORRELATIVO"));
                Date dateFechaEmision = (Date)doc.get("FECHA_EMISION");
                String strFechaEmision = LogDateUtil.dateToStringDD_MM_YYYY(dateFechaEmision);
                oseResponseBean.setFechaEmision(strFechaEmision);
                oseResponseBean.setTipoDocumentoEmisor((String)doc.get("TIPO_DOCUMENTO_EMISOR"));
                oseResponseBean.setRazonSocialEmisor((String)doc.get("RAZON_SOCIAL_EMISOR"));
                oseResponseBean.setDireccionEmisor((String)doc.get("DIRECCION_EMISOR"));                                   
                oseResponseBean.setNumeroDocumentoCliente((String)doc.get("NUMERO_DOCUMENTO_CLIENTE"));                
                oseResponseBean.setTipoDocumentoCliente((String)doc.get("TIPO_DOCUMENTO_CLIENTE"));
                oseResponseBean.setRazonSocialCliente((String)doc.get("RAZON_SOCIAL_CLIENTE"));
                oseResponseBean.setDireccionCliente((String)doc.get("DIRECCION_CLIENTE"));
                oseResponseBean.setTipoMoneda((String)doc.get("TIPO_MONEDA"));
                Date dateFechaVencimiento = (Date)doc.get("FECHA_VENCIMIENTO");
                String strFechaVencimiento = LogDateUtil.dateToStringDD_MM_YYYY(dateFechaVencimiento);
                oseResponseBean.setFechaVencimiento(strFechaVencimiento);
                oseResponseBean.setOpGravada((String)doc.get("OP_GRAVADA"));
                oseResponseBean.setIgv((String)doc.get("IGV"));
                oseResponseBean.setOpInafecta((String)doc.get("OP_INAFECTA"));                
                oseResponseBean.setOpExonerada((String)doc.get("OP_EXONERADA"));
                oseResponseBean.setOpExportacion((String)doc.get("OP_EXPORTACION"));
                oseResponseBean.setIsc((String)doc.get("ISC"));
                oseResponseBean.setDescuentosGlobalesAfectos((String)doc.get("DESCUENTOS_GLOBALES_AFECTOS"));
                oseResponseBean.setCargosGlobalesAfectos((String)doc.get("CARGOS_GLOBALES_AFECTOS"));
                oseResponseBean.setImporteTotal((String)doc.get("IMPORTE_TOTAL"));
                oseResponseBean.setDescuentosGlobalesNoAfectos((String)doc.get("DESCUENTOS_GLOBALES_NO_AFECTOS"));
                oseResponseBean.setCargosGlobalesNoAfectos((String)doc.get("CARGOS_GLOBALES_NO_AFECTOS"));                     
                
                Binary binaryPDF = doc.get("PDF", Binary.class); 
                byte[] bytePDF = new byte[]{};
                if(binaryPDF!=null)
                	bytePDF = binaryPDF.getData();                
                oseResponseBean.setPdf(bytePDF);

                Binary binaryUBL = doc.get("UBL", Binary.class); 
                byte[] byteUBL = new byte[]{};
                if(binaryUBL!=null)
                	byteUBL = binaryUBL.getData();
                oseResponseBean.setUbl(byteUBL);                
                
                Binary binaryCDR = doc.get("CDR", Binary.class); 
                byte[] byteCDR = new byte[]{};
                if(binaryCDR!=null)
                	byteCDR = binaryCDR.getData();                
                oseResponseBean.setCdr(byteCDR);                
                listOseResponseBean.add(oseResponseBean);
            }     
            cursorDocument.close();
            mongoClientOSE.close();  
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getListOseResponseBean MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getListOseResponseBean Exception \n"+errors);
        }       
    }           
}
