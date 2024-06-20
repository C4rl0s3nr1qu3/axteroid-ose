package com.axteroid.ose.server.rulesejb.dao.impl;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatContribuyenteAsociadoEmisor;
import com.axteroid.ose.server.rulesejb.dao.SunatContribuyenteAsociadoEmisorDAOLocal;
import com.axteroid.ose.server.rulesejb.dao.SunatParametroDAOLocal;
import com.axteroid.ose.server.rulesejb.emf.impl.DAOComImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoParametroEnum;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Local(SunatContribuyenteAsociadoEmisorDAOLocal.class)
public class SunatContribuyenteAsociadoEmisorDAOImpl extends DAOComImpl<SunatContribuyenteAsociadoEmisor> 
		implements SunatContribuyenteAsociadoEmisorDAOLocal {
	private static final Logger log = LoggerFactory.getLogger(SunatContribuyenteAsociadoEmisorDAOImpl.class);
	
    public SunatContribuyenteAsociadoEmisorDAOImpl() {}
    
    public void getContribuyenteAsociado(Documento tbComprobante, Date fechaRecepcion) {
    	try {
    		List<SunatContribuyenteAsociadoEmisor> results = this.buscarContribuyenteAsociadoEmisor(tbComprobante, fechaRecepcion, Constantes.SUNAT_IndTipAsoc_OSE);	 
    		if(results != null && results.size()>0) {    	    			
    			boolean b = true;
				SunatParametroDAOLocal tsParametroDAOLocal = 
						(SunatParametroDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatParametroDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatParametroDAOLocal");						
    			
    			String cli = tsParametroDAOLocal.bucarParametro(TipoParametroEnum.CLI.getCodigo(),tbComprobante.getUserCrea());    			
    			if((cli != null) && 
    					(cli.equals(String.valueOf(Constantes.SUNAT_IndTipAsoc_EMI))))
    				return;
    			List<SunatContribuyenteAsociadoEmisor> resultsFecha = this.buscarPSEEmisor(tbComprobante, fechaRecepcion, Constantes.SUNAT_IndTipAsoc_PSE);	 
    			if((resultsFecha != null) && (resultsFecha.size()>0))  
    				b = false; 			
    			if(b) {	
					//documento.setErrorComprobante(OseConstantes.CONTENT_FALSE.charAt(0));    				
					//documento.setErrorNumero(OseConstantes.SUNAT_ERROR_2873);
		    		String obsv = "";
		  			if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
		  				obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
		  			tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_2873);
    			}    			
    			return;   		
    		}    		
//    		documento.setErrorComprobante(OseConstantes.CONTENT_FALSE.charAt(0));
//    		documento.setErrorNumero(OseConstantes.SUNAT_ERROR_2874);  
    		String obsv = "";
  			if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
  				obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
  			tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_2874);
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getContribuyenteAsociado Exception \n"+errors);
		}
    }
    
	private List<SunatContribuyenteAsociadoEmisor> buscarContribuyenteAsociadoEmisor(Documento documento, 
    		Date fechaEmisionComprobante, int indicador) {
    	//log.info("buscarContribuyenteAsociadoEmisor (4) "+tbContent.getRucPseEmisor()+" - "+fechaEmisionComprobante);
    	try {
    		String consulta = "SELECT d FROM SunatContribuyenteAsociadoEmisor d "
				+" where d.sunatContribuyenteAsociadoEmisorPK.numRuc = :numRuc "
				+" and d.sunatContribuyenteAsociadoEmisorPK.numRucAsociado = :numRucAsociado "
				+" and d.sunatContribuyenteAsociadoEmisorPK.indTipAsociacion =:indicador "	
				+" and :fecini between d.fecInicio and (CASE WHEN d.fecFin = '0001-01-01 00:00:00.0' " 
				+" THEN '3001-01-01 00:00:00.0' ELSE d.fecFin END) "
				+" order by d.fecInicio desc";
    		Map<String, Object> parameterValues = new HashMap<String, Object>();
    		parameterValues.put("numRuc", documento.getRucEmisor());
    		Short ind = (short) indicador;
    		parameterValues.put("indicador", ind);	
    		Long ruc_OseServer = Long.parseLong(Constantes.CDR_RUC_OSE);
    		parameterValues.put("numRucAsociado", ruc_OseServer); 
    		parameterValues.put("fecini", fechaEmisionComprobante);    	    	 
    		List<SunatContribuyenteAsociadoEmisor> results = this.query(consulta, parameterValues);
    		if(results != null && results.size()>0)
    			return results;  
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarContribuyenteAsociadoEmisor Exception \n"+errors);
		}
    	return null;
    }
    
    public void getContribuyentePSEEmisor(Documento documento, Date fechaEmisionComprobante) {
    	try {
    		List<SunatContribuyenteAsociadoEmisor> results = this.buscarPSEEmisor(documento);	
    		if(results != null && results.size()>0)
    			return;
    		documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0154);
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getContribuyentePSEEmisor Exception \n"+errors);
		}
    }
    
	private List<SunatContribuyenteAsociadoEmisor> buscarPSEEmisor(Documento documento,  
			Date fechaEmisionComprobante, int indicador) {
    	log.info("buscarContribuyenteAsociadoEmisor PSEEmisor (4) RUC "+documento.getRucEmisor()+ " - PSE/EMISOR - "+documento.getRucPseEmisor()+"- indicador -"+indicador+"- fechaEmisionComprobante -"+fechaEmisionComprobante);
    	try {
    		String consulta = "SELECT d FROM SunatContribuyenteAsociadoEmisor d "
				+" where d.sunatContribuyenteAsociadoEmisorPK.numRuc = :numRuc "
				+" and d.sunatContribuyenteAsociadoEmisorPK.numRucAsociado = :numRucAsociado "
				+" and d.sunatContribuyenteAsociadoEmisorPK.indTipAsociacion =:indicador "
				+" and :fecini between d.fecInicio and (CASE WHEN d.fecFin = '0001-01-01 00:00:00.0' " 
				+" THEN '3001-01-01 00:00:00.0' ELSE d.fecFin END) "
				+" order by d.fecInicio desc";
    		Map<String, Object> parameterValues = new HashMap<String, Object>();
    		parameterValues.put("numRuc", documento.getRucEmisor());
    		Short ind = (short) indicador;
    		parameterValues.put("indicador", ind);	
    		parameterValues.put("numRucAsociado", documento.getRucPseEmisor()); 
    		parameterValues.put("fecini", fechaEmisionComprobante);
    		List<SunatContribuyenteAsociadoEmisor> results = this.query(consulta, parameterValues);
    		if(results != null && results.size()>0)
    			return results;  
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarPSEEmisor Exception \n"+errors);
		}
    	return null;
    }
    
    private List<SunatContribuyenteAsociadoEmisor> buscarPSEEmisor(Documento documento) {
    	//log.info("buscarContribuyenteAsociadoEmisor RucEmisor "+documento.getRucEmisor()+" - RucPseEmisor - "+tbContent.getRucPseEmisor());    	
    	try {
    		String consulta = "SELECT d FROM SunatContribuyenteAsociadoEmisor d "
				+" where d.sunatContribuyenteAsociadoEmisorPK.numRuc = :numRuc "
				+" and d.sunatContribuyenteAsociadoEmisorPK.numRucAsociado = :numRucAsociado ";
    		Map<String, Object> parameterValues = new HashMap<String, Object>();
    		parameterValues.put("numRuc", documento.getRucEmisor());	  
    		Long ruc = Long.valueOf(Constantes.CDR_RUC_OSE);
    		parameterValues.put("numRucAsociado", ruc);
    		List<SunatContribuyenteAsociadoEmisor> results = this.query(consulta, parameterValues);
    		if(results != null && results.size()>0)
    			return results;  
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarPSEEmisor Exception \n"+errors);
		}
    	return null;
    }  
 
    public List<SunatContribuyenteAsociadoEmisor> getJoinPSEEmisor(Documento documento, int indicador, Long rucPSE) {
    	log.info("getJoinPSEEmisor: {} | {} | {} ",documento.toString(), indicador, rucPSE);
    	try {
    		List<SunatContribuyenteAsociadoEmisor> results = this.buscarPSEEmisor(documento, 
    				documento.getFechaCrea(), indicador, rucPSE);	
    		if(results != null && results.size()>0)
    			return results;
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getContribuyentePSEEmisor Exception \n"+errors);
		}
    	return null;
    }
    
	private List<SunatContribuyenteAsociadoEmisor> buscarPSEEmisor(Documento documento,  
			Date fechaEmisionComprobante, int indicador, Long rucPSE) {
    	log.info("buscarPSEEmisor (5) RUC-EMISOR: {} | RUC-PSE: {} | Indicador: {} ",
    			documento.getRucEmisor(), rucPSE, indicador);
    	try {
    		String consulta = "SELECT d FROM SunatContribuyenteAsociadoEmisor d "
				+" where d.sunatContribuyenteAsociadoEmisorPK.numRuc = :numRuc "
				+" and d.sunatContribuyenteAsociadoEmisorPK.numRucAsociado = :numRucAsociado "
				+" and d.sunatContribuyenteAsociadoEmisorPK.indTipAsociacion =:indicador "
				//+" and :fecini between d.fecInicio and (CASE WHEN d.fecFin = '0001-01-01 00:00:00.0' " 
				//+" THEN '3001-01-01 00:00:00.0' ELSE d.fecFin END) "
				+" order by d.fecInicio desc";
    		Map<String, Object> parameterValues = new HashMap<String, Object>();
    		parameterValues.put("numRuc", documento.getRucEmisor());
    		Short ind = (short) indicador;
    		parameterValues.put("indicador", ind);	
    		parameterValues.put("numRucAsociado", rucPSE); 
    		//parameterValues.put("fecini", fechaEmisionComprobante);
    		List<SunatContribuyenteAsociadoEmisor> results = this.query(consulta, parameterValues);
    		if(results != null && results.size()>0)
    			return results;  
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarPSEEmisor Exception \n"+errors);
		}
    	return null;
    }
}
