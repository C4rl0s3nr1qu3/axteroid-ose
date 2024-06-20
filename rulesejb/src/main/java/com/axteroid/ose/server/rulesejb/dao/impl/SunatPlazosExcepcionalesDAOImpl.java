package com.axteroid.ose.server.rulesejb.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatPlazosExcepcionales;
import com.axteroid.ose.server.rulesejb.dao.SunatPlazosExcepcionalesDAOLocal;
import com.axteroid.ose.server.rulesejb.emf.impl.DAOComImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;

@Stateless
@Local(SunatPlazosExcepcionalesDAOLocal.class)
public class SunatPlazosExcepcionalesDAOImpl extends DAOComImpl<SunatPlazosExcepcionales> 
	implements SunatPlazosExcepcionalesDAOLocal {
	private static final Logger log = LoggerFactory.getLogger(SunatPlazosExcepcionalesDAOImpl.class);
    public SunatPlazosExcepcionalesDAOImpl() {}
    
    public void getPlazoExcepcional(Documento tbComprobante, Date fechaEmisionComprobante) {
    	try {
//    		if(parametrosReader.getParametroError2108(OseConstantes.SUNAT_PARAMETRO_004, tbComprobante.getTipoComprobante(), 
//   				 tbContent.getFecRecepcion(), fechaEmisionComprobante)) { 
//    			
//    			if(parametrosReader.getParametroError2108_Ampl(fechaEmisionComprobante)) {		    			
//	    			List<TsPlazosExcepcionales> results = this.buscarPlazosExcepcionales(tbComprobante, tbContent.getFecRecepcion(), fechaEmisionComprobante);	
//	    			if(results != null && results.size()>0)
//	    				return;   	
//	    			if(tbComprobante.getTipoComprobante().equals(OseConstantes.SUNAT_GUIA_REMISION)) {
//	    				tbComprobante.setErrorComprobante(OseConstantes.CONTENT_FALSE.charAt(0));
//	    				tbComprobante.setErrorNumero(OseConstantes.SUNAT_ERROR_2108); 
//	    			}
//	    			if(tbComprobante.getTipoComprobante().equals(OseConstantes.SUNAT_RETENCION) 
//	    					|| tbComprobante.getTipoComprobante().equals(OseConstantes.SUNAT_PERCEPCION)) {
//	    				tbComprobante.setErrorComprobante(OseConstantes.CONTENT_FALSE.charAt(0));
//	    				tbComprobante.setErrorNumero(OseConstantes.SUNAT_ERROR_2600); 
//	    			}   
//    			}
//    		}
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getPlazoExcepcional Exception \n"+errors);
		}
    }
        
}
