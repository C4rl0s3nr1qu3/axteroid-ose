package com.axteroid.ose.server.rulesejb.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatPadronContribuyente;
import com.axteroid.ose.server.jpa.model.SunatPadronVigencia;
import com.axteroid.ose.server.rulesejb.dao.SunatPadronVigenciaDAOLocal;
import com.axteroid.ose.server.rulesejb.emf.impl.DAOComImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.LogDateUtil;

/**
 * Session Bean implementation class SunatPadronVigenciaDAOImpl
 */
@Stateless
@Local(SunatPadronVigenciaDAOLocal.class)
public class SunatPadronVigenciaDAOImpl extends DAOComImpl<SunatPadronVigencia> 
implements SunatPadronVigenciaDAOLocal{

	private static final Logger log = LoggerFactory.getLogger(SunatPadronVigenciaDAOImpl.class);
    public SunatPadronVigenciaDAOImpl() {
        // TODO Auto-generated constructor stub
    }

    public void getPadronAutorizadoIgv(Documento documento) {
    	documento.setLongitudNombre(18);
   	 	try {  
   	 		List<SunatPadronVigencia> results = this.buscarTsPadronVigencia(documento, documento.getRucEmisor());	   	 		
   	 		if(results != null && results.size()>0) {
   	 			Date emisiondate = documento.getFecFinProc();
	   	 		for(SunatPadronVigencia pc : results){	   	 			
	   	 			boolean bFecInivig = LogDateUtil.greaterThanOrEqualTo(emisiondate, pc.getSunatPadronVigenciaPK().getFec_inivig()); 
	   	 			boolean bFecFinvig = LogDateUtil.lessThanOrEqualTo(emisiondate, pc.getFec_finvig()); 
	   	 			if((bFecInivig) && (bFecFinvig)) {
	   	 				if(pc.getSunatPadronVigenciaPK().getIndPadron().equals(Constantes.SUNAT_Indicador_01)) 
	   	 					documento.setLongitudNombre(10); 
	   	 			}
	   	 			log.info("emisiondate: {} | Fec_inivig: {} | bFecInivig: {} | Fec_finvig: {} | bFecFinvig: {} | IGV: {}",emisiondate,pc.getSunatPadronVigenciaPK().getFec_inivig(),bFecInivig,pc.getFec_finvig(),bFecFinvig, documento.getLongitudNombre());
	   	 			
	   	 		}
   	 		}
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getPadronAutorizadoIgv Exception \n"+errors);
		}
    }   
    
 	private List<SunatPadronVigencia> buscarTsPadronVigencia(Documento documento, Long documentoContribuyente) {
 		//log.info("buscarTsPadronContribuyente --> "+documentoContribuyente);
		try {    
			String consulta = "SELECT d FROM SunatPadronVigencia d "
	  				+" where d.sunatPadronVigenciaPK.numRuc = :numRuc ";
	     	Map<String, Object> parameterValues = new HashMap<String, Object>();
	     	parameterValues.put("numRuc", documentoContribuyente);
            List<SunatPadronVigencia> results = this.query(consulta, parameterValues);
            if(results != null && results.size()>0)
            	return results;   
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTsPadronVigencia Exception \n"+errors);
		}
        return null;
	}   
    
}
