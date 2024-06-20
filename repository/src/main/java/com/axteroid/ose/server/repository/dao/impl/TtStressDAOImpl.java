package com.axteroid.ose.server.repository.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Map;

//import org.apache.log4j.Logger;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.repository.conn.DaoMongoDB;
import com.axteroid.ose.server.repository.dao.TtStressDAO;
import com.axteroid.ose.server.tools.util.LogDateUtil;
import com.mongodb.MongoSecurityException;
import com.mongodb.client.MongoCollection;

public class TtStressDAOImpl extends DaoMongoDB implements TtStressDAO{
	//private final static Logger log =  Logger.getLogger(TtStressDAOImpl.class);
	private static final Logger log = LoggerFactory.getLogger(TtStressDAOImpl.class);
	public TtStressDAOImpl() {
		super();		
		conexionMongoClientOSE();
		conexionMongoDatabaseOSE();
	}
	
    public void grabarTsStress(Documento tbComprobante, Map<String, Timestamp> mapTime){     
    	//log.info("grabarTsStress ---> "+tbComprobante.getNombre());
        try {
            MongoCollection<Document> collection = mongoDatabaseOSE.getCollection("TT_STRESS");
            Document  document = new Document();
            document.append("ID", tbComprobante.getId());
            //document.append("ID_CONTENT", tbComprobante.getIdContent().getId());
            document.append("ID_COMPROBANTE", tbComprobante.getIdComprobante());
            document.append("RUC_EMISOR", tbComprobante.getRucEmisor());
            document.append("TIPO_COMPROBANTE", tbComprobante.getTipoDocumento());
            document.append("SERIE", tbComprobante.getSerie());
            document.append("NUMERO_CORRELATIVO", tbComprobante.getNumeroCorrelativo());
            document.append("ERROR_COMPROBANTE", tbComprobante.getErrorComprobante());
            document.append("ERROR_NUMERO", tbComprobante.getErrorNumero());
            document.append("OBSERVA_NUMERO", tbComprobante.getObservaNumero());
            document.append("START_BUILD", (mapTime.get("StartBuild").getTime() - mapTime.get("Start").getTime()));
            document.append("RULES_VALIDATE", (mapTime.get("RulesValidate").getTime() - mapTime.get("StartBuild").getTime()));
            document.append("PARSE_DOCUMENT", (mapTime.get("ParseDocument").getTime() - mapTime.get("RulesValidate").getTime()));
            document.append("UBL_PARAMETERS", (mapTime.get("UBLParameters").getTime() - mapTime.get("ParseDocument").getTime()));
            document.append("LIST_VALIDATE", (mapTime.get("ListValidate").getTime() - mapTime.get("UBLParameters").getTime()));
            document.append("CDR_DOCUMENT_BUILD", (mapTime.get("CDRDocumentBuild").getTime() - mapTime.get("ListValidate").getTime()));
            document.append("CRUD", (mapTime.get("CRUD").getTime() - mapTime.get("CDRDocumentBuild").getTime()));
            document.append("SEND_SUNAT", (mapTime.get("End").getTime() - mapTime.get("CRUD").getTime()));
            document.append("TOTAL", (mapTime.get("End").getTime() - mapTime.get("Start").getTime()));
            document.append("USER_CREA", tbComprobante.getUserCrea());
            document.append("FECHA_CREA", LogDateUtil.dateToStringDD_MM_YYYY(tbComprobante.getFechaCrea()));
            collection.insertOne(document);
            mongoClientOSE.close();  
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabarTsStress Exception \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabarTsStress Exception \n"+errors);
		}
    }
   
}
