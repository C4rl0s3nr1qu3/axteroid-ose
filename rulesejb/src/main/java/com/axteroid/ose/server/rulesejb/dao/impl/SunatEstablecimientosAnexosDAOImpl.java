package com.axteroid.ose.server.rulesejb.dao.impl;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatEstablecimientosAnexos;
import com.axteroid.ose.server.rulesejb.dao.SunatEstablecimientosAnexosDAOLocal;
import com.axteroid.ose.server.rulesejb.emf.impl.DAOComImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.StringUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Local(SunatEstablecimientosAnexosDAOLocal.class)
public class SunatEstablecimientosAnexosDAOImpl extends DAOComImpl<SunatEstablecimientosAnexos> 
	implements SunatEstablecimientosAnexosDAOLocal {
	private static final Logger log = LoggerFactory.getLogger(SunatEstablecimientosAnexosDAOImpl.class);
	
    public SunatEstablecimientosAnexosDAOImpl() {}
    
    public void getEstablecimientosAnexos(Documento tbComprobante, String codEstab, 
    		String tipoDocumentoMdifica) {
    	log.info("getEstablecimientosAnexos --> "+tbComprobante.toString()+" | "+codEstab+" | "+tipoDocumentoMdifica);
    	try {
    		
    		List<SunatEstablecimientosAnexos> results = this.buscarTsEstablecimientosAnexos(tbComprobante, codEstab);
    		if(results != null && results.size()>0)
    			return;
	
    		String inicioSerie = tbComprobante.getSerie().substring(0,1);   	
    		if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_FACTURA)){
    			if(!StringUtil.hasString(inicioSerie)) {
    				tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
					tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_3239);
					return;
    			}
    	   		String obsv = "";
      			if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
      				obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
      			tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4199);
    			return;
    		}
    		if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_BOLETA)){
    	   		String obsv = "";
      			if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
      				obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
      			tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4199);
    			return;
    		}
    		if((tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO)) || 
    				(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_NOTA_DEBITO))){
    			if((inicioSerie.equals(Constantes.SUNAT_SERIE_F)) && 
    					(tipoDocumentoMdifica.equals(Constantes.SUNAT_Indicador_01))) {    				
    				tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
					tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_3239);
					return;
    			}
    	   		String obsv = "";
      			if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
      				obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
      			tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4199);
    			return;
    		}
    		
    	} catch (Exception e) {
    		tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getEstablecimientosAnexos Exception \n"+errors);
		}
    }
    
    private List<SunatEstablecimientosAnexos> buscarTsEstablecimientosAnexos(Documento tbComprobante, 
 			String codEstab) {
 		//log.info("buscarTsEstablecimientosAnexos --> "+tbComprobante.toString());
		try {    
			String consulta = "SELECT d FROM SunatEstablecimientosAnexos d "
	  				+" where d.sunatEstablecimientosAnexosPK.numRuc = :numRuc "
					+" and d.sunatEstablecimientosAnexosPK.codEstab = :codEstab ";
	     	Map<String, Object> parameterValues = new HashMap<String, Object>();
	     	String numruc = String.valueOf(tbComprobante.getRucEmisor());
	     	parameterValues.put("numRuc", numruc);
	     	parameterValues.put("codEstab", codEstab);
            List<SunatEstablecimientosAnexos> results = this.query(consulta, parameterValues);
            if(results != null && results.size()>0)
            	return results;   
    	} catch (Exception e) {
    		tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTsEstablecimientosAnexos Exception \n"+errors);
		}
        return null;
	}   

}
