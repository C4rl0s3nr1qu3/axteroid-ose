package com.axteroid.ose.server.repository.rules.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Map;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.repository.build.BuildDocument;
import com.axteroid.ose.server.repository.dao.TtOseResponseDAO;
import com.axteroid.ose.server.repository.dao.TtStressDAO;
import com.axteroid.ose.server.repository.dao.impl.TtOseResponseDAOImpl;
import com.axteroid.ose.server.repository.dao.impl.TtStressDAOImpl;
import com.axteroid.ose.server.repository.rules.OseResponseCRUDLocal;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.mongodb.MongoSecurityException;

public class OseResponseCRUDImpl implements OseResponseCRUDLocal{
	private final static Logger log =  LoggerFactory.getLogger(OseResponseCRUDImpl.class);
	
	public void grabarOseResponse(Document document){
		//log.info("grabarOseResponse ---> "+document.get("ID_COMPROBANTE"));
		try {		
			TtOseResponseDAO ttOseResponseDAO = new TtOseResponseDAOImpl();
			ttOseResponseDAO.grabarOseResponse(document);
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabarOseResponse Exception \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabarOseResponse Exception \n"+errors);
		}
	}
	
	public void grabarTsStress(Documento tbComprobante, Map<String, Timestamp> mapTime){
		//log.info("grabarTsStress ---> "+tbComprobante.getNombre());
		try {			
			TtStressDAO ttStressDAO = new TtStressDAOImpl();
			ttStressDAO.grabarTsStress(tbComprobante, mapTime);
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
	
	public void getDocument_FBNCND(Documento tbComprobante, EDocumento eDocumento, Document document){
		//log.info("getDocument_FBNCND ---> "+tbComprobante.getNombre());
		try {		
			BuildDocument buildDocument = new BuildDocument();
			buildDocument.appendDocument_FBNCND(tbComprobante, eDocumento, document);
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDocument_FBNCND Exception \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDocument_FBNCND Exception \n"+errors);
		}
	}
	
	public void getDocument_Retencion(Documento tbComprobante, ERetencionDocumento eDocumento, Document document){
		//log.info("getDocument_Retencion ---> "+tbComprobante.getNombre());
		try {		
			BuildDocument buildDocument = new BuildDocument();
			buildDocument.appendDocument_Retencion(tbComprobante, eDocumento, document);
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDocument_Retencion Exception \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDocument_Retencion Exception \n"+errors);
		}
	}
	
	public void getDocument_Percepcion(Documento tbComprobante, EPercepcionDocumento eDocumento, Document document){
		//log.info("getDocument_Percepcion ---> "+tbComprobante.getNombre());
		try {		
			BuildDocument buildDocument = new BuildDocument();
			buildDocument.appendDocument_Percepcion(tbComprobante, eDocumento, document);
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDocument_Percepcion Exception \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDocument_Percepcion Exception \n"+errors);
		}
	}
	
	public void getDocument_Guia(Documento tbComprobante, EGuiaDocumento eDocumento, Document document){
		//log.info("getDocument_Guia ---> "+tbComprobante.getNombre());
		try {		
			BuildDocument buildDocument = new BuildDocument();
			buildDocument.appendDocument_Guia(tbComprobante, eDocumento, document);
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDocument_Guia Exception \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDocument_Guia Exception \n"+errors);
		}
	}
	
	public void getDocument_SummaryResumen(Documento tbComprobante, EResumenDocumento eDocumento, Document document){
		log.info("getDocument_SummaryResumen ---> "+tbComprobante.getNombre());
		try {		
			BuildDocument buildDocument = new BuildDocument();
			buildDocument.appendDocument_SummaryResumen(tbComprobante, eDocumento, document);
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDocument_SummaryResumen Exception \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDocument_SummaryResumen Exception \n"+errors);
		}
	}	
	
	public void getDocument_SummaryReversion(Documento tbComprobante, EReversionDocumento eDocumento, Document document){
		log.info("getDocument_SummaryReversion ---> "+tbComprobante.getNombre());		
		try {
			BuildDocument buildDocument = new BuildDocument();
			buildDocument.appendDocument_SummaryReversion(tbComprobante, eDocumento, document);
        }catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDocument_SummaryReversion Exception \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDocument_SummaryReversion Exception \n"+errors);
		}
	}		
	
}
