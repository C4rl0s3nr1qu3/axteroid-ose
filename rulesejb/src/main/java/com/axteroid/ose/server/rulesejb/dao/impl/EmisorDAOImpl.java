package com.axteroid.ose.server.rulesejb.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.Emisor;
import com.axteroid.ose.server.rulesejb.dao.EmisorDAOLocal;
import com.axteroid.ose.server.rulesejb.emf.impl.DAOComImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;

@Stateless
@Local(EmisorDAOLocal.class)
public class EmisorDAOImpl extends DAOComImpl<Emisor> implements EmisorDAOLocal{
	private static final Logger log = LoggerFactory.getLogger(EmisorDAOImpl.class);
	
	public Emisor buscarEmisor(Documento documento) {
		log.info("buscarEmisor "+documento.getId());
		try {    		    		
            List<Emisor> results = this.buscarEmisorRuc(documento);
            if(results != null && results.size()>0)	
            	return results.get(0);	   
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarEmisor Exception \n"+errors);
		}
        return null;		
	}
	
	
	public List<Emisor> buscarEmisorIdRuc(Documento documento) {
		log.info("buscarEmisorRuc "+documento.toString());
		try {    		    		
            List<Emisor> results = this.findByColumnName("rucEmisor", documento.getRucEmisor());
            if(results != null && results.size()>0)	
            	return results;	   
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarEmisorRuc Exception \n"+errors);
		}
        return null;
	}	
	
	public List<Emisor> buscarEmisorRuc(Documento documento) {		
		log.info("buscarEmisorRuc "+documento.toString());
		try {    		    		
			String consulta = "SELECT d FROM Emisor d where d.rucEmisor =:rucEmisor ";
	    	Map<String, Object> parameterValues = new HashMap<String, Object>();
			parameterValues.put("rucEmisor", documento.getRucEmisor());    	
			List<Emisor> results = this.query(consulta, parameterValues);
			if(results != null && results.size()>0)	
            	return results;		
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarEmisorRuc Exception \n"+errors);
		}
        return null;
	}	
}
