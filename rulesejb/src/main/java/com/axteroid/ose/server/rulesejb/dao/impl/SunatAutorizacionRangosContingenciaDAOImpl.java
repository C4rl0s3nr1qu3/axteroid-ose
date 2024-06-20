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
import com.axteroid.ose.server.jpa.model.SunatAutorizacionRangosContingencia;
import com.axteroid.ose.server.rulesejb.dao.SunatAutorizacionRangosContingenciaDAOLocal;
import com.axteroid.ose.server.rulesejb.emf.impl.DAOComImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;
import com.axteroid.ose.server.tools.util.StringUtil;

@Stateless
@Local(SunatAutorizacionRangosContingenciaDAOLocal.class)
public class SunatAutorizacionRangosContingenciaDAOImpl extends DAOComImpl<SunatAutorizacionRangosContingencia> 
	implements SunatAutorizacionRangosContingenciaDAOLocal {
	private static final Logger log = LoggerFactory.getLogger(SunatAutorizacionRangosContingenciaDAOImpl.class);
	
    public SunatAutorizacionRangosContingenciaDAOImpl() { }
    
    public void getAutorizacionRangosContingencia_RC(Documento documento, EResumenDocumentoItem rdi) {
    	try {    		 
    		List<SunatAutorizacionRangosContingencia> results = this.buscarAutorizacionRangosContingencia(documento, 
    				rdi.getDocumentTypeCode(), rdi.getId());	
   		 	if(results != null && results.size()>0)  
   		 		return;
//    		documento.setErrorComprobante(OseConstantes.CONTENT_FALSE.charAt(0));
//    		documento.setErrorNumero(OseConstantes.SUNAT_ERROR_3207);  
    		String obsv = "";
  			if(documento.getObservaNumero()!=null && !(documento.getObservaNumero().isEmpty()))
  				obsv = documento.getObservaNumero()+Constantes.OSE_SPLIT_3;
  			documento.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4204);
     		String cpe = "";
   			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
   				cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
   			cpe = cpe+"["+rdi.getDocumentTypeCode()+"-"+rdi.getId()+"]";
   			documento.setErrorLog(cpe);
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getAutorizacionRangosContingencia_RC Exception \n"+errors);
		}
    }  

    public void getAutorizacionRangosContingencia_RP(Documento documento) {
    	try {    		 
    		List<SunatAutorizacionRangosContingencia> results = this.buscarAutorizacionRangosContingencia(documento, 
    				documento.getTipoDocumento(), documento.getSerie()+"-"+documento.getNumeroCorrelativo());	
   		 	if(results != null && results.size()>0)  
   		 		return;
    		documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_3207);  
//    		String obsv = "";
//  			if(documento.getObservaNumero()!=null && !(documento.getObservaNumero().isEmpty()))
//  				obsv = documento.getObservaNumero()+Constantes.OSE_SPLIT_3;
//  			documento.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4204);
//     		String cpe = "";
//   			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
//   				cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
//   			cpe = cpe+"["+rdi.getDocumentTypeCode()+"-"+rdi.getId()+"]";
//   			documento.setErrorLog(cpe);
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getAutorizacionRangosContingencia_RP Exception \n"+errors);
		}
    }  
    
    public void getAutorizacionRangosContingencia(Documento documento) {
    	try {    		 
    		List<SunatAutorizacionRangosContingencia> results = this.buscarAutorizacionRangosContingencia(documento);	
   		 	if(results != null && results.size()>0)  
   		 		return;
    		String obsv = "";
  			if(documento.getObservaNumero()!=null && !(documento.getObservaNumero().isEmpty()))
  				obsv = documento.getObservaNumero()+Constantes.OSE_SPLIT_3;
  			documento.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4204);
     		String cpe = "";
   			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
   				cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
   			cpe = cpe+"["+documento.getTipoDocumento()+"-"+documento.getSerie()+"-"+documento.getNumeroCorrelativo()+"]";
   			documento.setErrorLog(cpe);
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getAutorizacionRangosContingencia Exception \n"+errors);
		}
    }  
    
    @SuppressWarnings("unused")
	private List<SunatAutorizacionRangosContingencia> buscarAutorizacionRangosContingencia(Documento documento) {
    	log.info("buscarAutorizacionRangosContingencia (1): "+documento.getRucEmisor()+" - "+ documento.getTipoDocumento()+" - "+
     			documento.getSerie()+" - "+documento.getNumeroCorrelativo());
     	try{
     		String consulta = "SELECT d FROM SunatAutorizacionRangosContingencia d "
  				+" where d.sunatAutorizacionRangosContingenciaPK.numRuc = :numRuc "
  				+" and d.sunatAutorizacionRangosContingenciaPK.codCpe = :codCpe "
  				+" and d.sunatAutorizacionRangosContingenciaPK.numSerieCpe = :numSerieCpe "
  				+" and :numCpe between d.sunatAutorizacionRangosContingenciaPK.numIniCpe and d.numFinCpe ";
     		Map<String, Object> parameterValues = new HashMap<String, Object>();
     		parameterValues.put("numRuc", documento.getRucEmisor());
     		parameterValues.put("codCpe", documento.getTipoDocumento());
     		parameterValues.put("numSerieCpe", documento.getSerie());
     		int numCpe = 0;		
    		if(StringUtil.hasString(documento.getNumeroCorrelativo()))
    			numCpe = Integer.parseInt(documento.getNumeroCorrelativo());
    		parameterValues.put("numCpe", numCpe);     		
     		List<SunatAutorizacionRangosContingencia> results = this.query(consulta, parameterValues);   
     		if(results != null && results.size()>0)
     			return results;
     	} catch (Exception e) {
     		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
     		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
     		documento.setErrorLog(e.toString());
 			StringWriter errors = new StringWriter();				
 			e.printStackTrace(new PrintWriter(errors));
 			log.error("buscarAutorizacionRangosContingencia Exception \n"+errors);
 		}
         return null;      	
     }    
    
    private List<SunatAutorizacionRangosContingencia> buscarAutorizacionRangosContingencia(Documento documento, 
    		String tipoDocRefPri, String nroDocRefPri) {
    	try{
    		String [] docRefPri = nroDocRefPri.split("-");
    		String consulta = "SELECT d FROM SunatAutorizacionRangosContingencia d "
 				+" where d.sunatAutorizacionRangosContingenciaPK.numRuc = :numRuc "
 				+" and d.sunatAutorizacionRangosContingenciaPK.codCpe = :codCpe "
 				+" and d.sunatAutorizacionRangosContingenciaPK.numSerieCpe = :numSerieCpe "
 				+" and :numCpe between d.sunatAutorizacionRangosContingenciaPK.numIniCpe and d.numFinCpe ";
    		Map<String, Object> parameterValues = new HashMap<String, Object>();
    		parameterValues.put("numRuc", documento.getRucEmisor());
    		parameterValues.put("codCpe", tipoDocRefPri);
    		parameterValues.put("numSerieCpe", docRefPri[0]);
    		int numCpe = 0;			
			if(StringUtil.hasString(docRefPri[1]))
				numCpe = Integer.parseInt(docRefPri[1]);
    		parameterValues.put("numCpe", numCpe);    
    		log.info("buscarAutorizacionRangosContingencia (4): "+documento.getRucEmisor()+"-"+ tipoDocRefPri+"-"+docRefPri[0]+"-"+numCpe);
    		List<SunatAutorizacionRangosContingencia> results = this.query(consulta, parameterValues);    	
    		if(results != null && results.size()>0)
    			return results;
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarAutorizacionRangosContingencia Exception \n"+errors);
		}
        return null;     	
     }
}
