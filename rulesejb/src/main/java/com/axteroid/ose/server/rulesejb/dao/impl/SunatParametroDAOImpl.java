package com.axteroid.ose.server.rulesejb.dao.impl;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatParametro;
import com.axteroid.ose.server.rulesejb.dao.SunatParametroDAOLocal;
import com.axteroid.ose.server.rulesejb.emf.impl.DAOComImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoParametroEnum;
import com.axteroid.ose.server.tools.util.DateUtil;

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

@Stateless
@Local(SunatParametroDAOLocal.class)
public class SunatParametroDAOImpl extends DAOComImpl<SunatParametro> implements SunatParametroDAOLocal {
	private static final Logger log = LoggerFactory.getLogger(SunatParametroDAOImpl.class);
	
    public SunatParametroDAOImpl() {}

    public String getParametro(Documento tbComprobante, String codParametro, String codArgumento) {	
    	String desArgumento = "";
    	try {
    		List<SunatParametro> results = this.buscarParametro(tbComprobante, codParametro, codArgumento);
            if(results != null && results.size()>0) {
            	if((results.get(0) != null) && (results.get(0).getDesArgumento() != null))
            		desArgumento = results.get(0).getDesArgumento();   
            }
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getParametro Exception \n"+errors);
		 }
		 return desArgumento;
	}	 

	public boolean getParametroErrorFechas(String dias, Date fechaRecep, Date fechaEmision) {		
		log.info("dias "+dias+ " - fechaRecep - "+fechaRecep+ " - fechaEmision - " +fechaEmision);	
		long lDiasParam = Long.parseLong(dias);				
		long lDias = DateUtil.deltaDays(fechaRecep, fechaEmision);
		//log.info("ParametroError2108 codParametro "+codParametro+ " - key - " +key+" - value - "+dias+" - DifDias - "+lDias);	
		if(lDias>lDiasParam) 
			return true;
		return false;
	}
	
	public boolean getParametroErrorFechaAmplSunat(Date currentdate, Date cbcIssueDate) {	
		log.info("currentdate: "+currentdate+" - cbcIssueDate: "+cbcIssueDate);
		 Date d1 = DateUtil.newDate(2020, 3, 9);
		 Date d2 = DateUtil.newDate(2020, 6, 30);
		 Date d3 = DateUtil.newDate(2020, 7, 10);
		 long t2 = DateUtil.deltaDays(currentdate, cbcIssueDate);		 
		 long t4 = DateUtil.deltaDays(cbcIssueDate, d1);
		 long t6 = DateUtil.deltaDays(cbcIssueDate, d2);
		 long t8 = DateUtil.deltaDays(currentdate, d1);
		 long t10 = DateUtil.deltaDays(currentdate, d3);
		 log.info("t2: "+t2+" | t4: "+t4+" | t6: "+t6+" | t8: "+t8+" | t10: "+t10);
		 long lDiasParam = Long.parseLong(Constantes.SUNAT_DIAS_7);
		 if(t10<=0 && t8>=0) {
			 log.info("t10: "+t10+" | t8: "+t8);
			 if(t4<0 || t6>0) {
				log.info("t4: "+t4+" | t6: "+t6);
				 if(t2>lDiasParam) {
					log.info("1");
				 	return true;
				 }
			 }
			 log.info("t2: "+t2);
		 }else if(t2>lDiasParam) {
			 log.info("2");
			 return true;
		 }
		 log.info("3");
		 return false;
	}			
	
	public boolean getParametroNoExiste(Documento tbComprobante, String codParametro, String codArgumento) {
		try {
			if(codArgumento!=null){
				String desArgumento = this.getParametro(tbComprobante, codParametro, codArgumento);			
				if(desArgumento!= null && !desArgumento.isEmpty()) 
					return false;			
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getParametroNoExiste Exception \n"+errors);
		}
	    return true;
	}
	
	public String getParametroString(Documento tbComprobante, String codParametro, String codArgumento) {		
		String desArgumento = "";
		try {
			if(codArgumento!=null)
				desArgumento = this.getParametro(tbComprobante, codParametro, codArgumento);							
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarLoginParametro Exception \n"+errors);
		}
        return desArgumento;
	}
    
	public List<SunatParametro> listarTsParametro() {		
		try {    		    		
            List<SunatParametro> results = this.findAll();
            if(results != null && results.size()>0)
            	return results;   
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("listarTsParametro Exception \n"+errors);
		}
        return null;
	}
		
	
    private List<SunatParametro> buscarParametro(Documento tbComprobante, 
    		String codParametro, String codArgumento) {
    	//log.info("buscarParametro "+codParametro+" - "+codArgumento);
    	try {
    		String consulta = "SELECT d FROM SunatParametro d "
				+" where d.sunatParametroPK.codParametro = :codParametro "
				+" and d.sunatParametroPK.codArgumento = :codArgumento "		
				+" order by desArgumento desc";
    		Map<String, Object> parameterValues = new HashMap<String, Object>();
    		parameterValues.put("codParametro", codParametro);
    		parameterValues.put("codArgumento", codArgumento);    	
    		List<SunatParametro> results = this.query(consulta, parameterValues);
    		if(results != null && results.size()>0)
    			return results;  
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarParametro Exception \n"+errors);
		}
    	return null;
    }	
    
	public String bucarParametro(String codParametro, String codArgumento) {		
		String desArgumento = "";
		try {    	
			if(codArgumento!=null){
				List<SunatParametro> results = this.buscarParametro(codParametro, codArgumento);
				if(results != null && results.size()>0) {
					if((results.get(0) != null) && (results.get(0).getDesArgumento() != null))
						desArgumento = results.get(0).getDesArgumento();   
				}
			}
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarLoginParametro Exception \n"+errors);
		}
        return desArgumento;
	}
	
	public String bucarLoginParametro(String codArgumento) {		
		String desArgumento = "";
		try {    	
			if(codArgumento!=null){
				List<SunatParametro> results = this.buscarParametro(TipoParametroEnum.SEC.getCodigo(), codArgumento);
				if(results != null && results.size()>0) {
					if((results.get(0) != null) && (results.get(0).getDesArgumento() != null))
						desArgumento = results.get(0).getDesArgumento();   
				}
			}
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarLoginParametro Exception \n"+errors);
		}
        return desArgumento;
	}
	
    private List<SunatParametro> buscarParametro(String codParametro, String codArgumento) {
    	//log.info("buscarParametro "+codParametro+" - "+codArgumento);
    	try {
    		String consulta = "SELECT d FROM SunatParametro d "
				+" where d.sunatParametroPK.codParametro = :codParametro "
				+" and d.sunatParametroPK.codArgumento = :codArgumento "		
				+" order by desArgumento desc";
    		Map<String, Object> parameterValues = new HashMap<String, Object>();
    		parameterValues.put("codParametro", codParametro);
    		parameterValues.put("codArgumento", codArgumento);    	
    		List<SunatParametro> results = this.query(consulta, parameterValues);
    		if(results != null && results.size()>0)
    			return results;  
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarParametro Exception \n"+errors);
		}
    	return null;
    }	
    
    public List<SunatParametro> buscarParametroCodArgumento(String codParametro, String desArgumento) {
    	//log.info("buscarParametro "+codParametro+" - "+desArgumento);
    	try {
    		String consulta = "SELECT d FROM SunatParametro d "
				+" where d.sunatParametroPK.codParametro = :codParametro "
				+" and d.desArgumento = :desArgumento "		
				+" order by d.sunatParametroPK.codArgumento desc";
    		Map<String, Object> parameterValues = new HashMap<String, Object>();
    		parameterValues.put("codParametro", codParametro);
    		parameterValues.put("desArgumento", desArgumento);    	
    		List<SunatParametro> results = this.query(consulta, parameterValues);
    		if(results != null && results.size()>0)
    			return results;  
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarParametro Exception \n"+errors);
		}
    	return null;
    }	   
}
