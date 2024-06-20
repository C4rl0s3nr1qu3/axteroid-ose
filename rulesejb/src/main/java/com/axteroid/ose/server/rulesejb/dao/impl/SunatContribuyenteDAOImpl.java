package com.axteroid.ose.server.rulesejb.dao.impl;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatContribuyente;
import com.axteroid.ose.server.rulesejb.dao.SunatContribuyenteDAOLocal;
import com.axteroid.ose.server.rulesejb.emf.impl.DAOComImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Local(SunatContribuyenteDAOLocal.class)
public class SunatContribuyenteDAOImpl extends DAOComImpl<SunatContribuyente> 
		implements SunatContribuyenteDAOLocal {
	private static final Logger log = LoggerFactory.getLogger(SunatContribuyenteDAOImpl.class);
     public SunatContribuyenteDAOImpl() {}
     
     public void getContribuyente(Documento tbComprobante) {
    	 try {  
    		 List<SunatContribuyente> results = this.buscarTsContribuyente(tbComprobante, tbComprobante.getRucEmisor());	    		 
    		 if(results != null && results.size()>0) {
	    		 for(SunatContribuyente tc : results){  		
	    			 if(!tc.getIndEstado().equals(Constantes.SUNAT_Indicador_00)) {
	    				 tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    				 tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_2010);
	    				 return;
	    			 }
	    			 if(tc.getIndCondicion().equals(Constantes.SUNAT_Indicador_12)){
	    				 tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    				 tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_2011);
	    				 return;
	    			 }     	
	    			if((tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO)) ||
	    						(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_NOTA_DEBITO)) ) {
		    			 String pSerie = tbComprobante.getSerie().substring(0, 1);
		    			 if(pSerie.equals(Constantes.SUNAT_SERIE_S)) {
		    				 tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
		    				 tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_1080);
		    				 return;
		    			 }
	    			}
	    			return;
	    		 }
    		 }
 		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getContribuyente Exception \n"+errors);
		}
    	return;
     }
 	
    public void getBFAdquiriente(Documento tbComprobante, Long documentoAdquiriente) {
    	//log.info("getBFAdquiriente --> "+tbComprobante.getIdComprobante());
    	try {      
    		List<SunatContribuyente> results = this.buscarTsContribuyente(tbComprobante, documentoAdquiriente);	
    		String obsv = "";
    		if(results != null && results.size()>0) {
	    		for(SunatContribuyente tc : results){     		
	    			if(!tc.getIndEstado().equals(Constantes.SUNAT_Indicador_00)) {    				
	    	    		 if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
	    	    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
	    	    		tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4013); 
	    			}else if(tc.getIndCondicion().equals(Constantes.SUNAT_Indicador_12)){
	   	    		 	if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
	   	    		 		obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
	   	    		 	tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4014); 
	    			}     
	    			//log.info("tc.getIndEstado() "+tc.getIndEstado()+" - tc.getIndCondicion() --> "+tc.getIndCondicion());
	    			return;
	    		}
    		}
			tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			//tbComprobante.setErrorNumero(OseConstantes.SUNAT_ERROR_3202);
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_1083);
    		
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getBFAdquiriente Exception \n"+errors);
		}    	
     }
    
    public void getBFEmisorAnticipo(Documento tbComprobante, Long documentoEmisorAnticipo) {
   	 	try {  
   	 		List<SunatContribuyente> results = this.buscarTsContribuyente(tbComprobante, documentoEmisorAnticipo);
   	 		if(results != null && results.size()>0)   		   	
   	 			return;   		 
   	 		tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
   	 		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_2529);
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getBFEmisorAnticipo Exception \n"+errors);
		}
   	 	return;
    }    
    
    public void getContribuyente_RP(Documento tbComprobante) {
   	 	try {  
   	 		List<SunatContribuyente> results = this.buscarTsContribuyente(tbComprobante, tbComprobante.getRucEmisor());
   	 		if(results != null && results.size()>0)   		   	
   	 			return;   		 
   	 		tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
   	 		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_2104);
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getContribuyente_RP Exception \n"+errors);
		}
   	 	return;
    }
    
    public void getRetencionProveedor(Documento tbComprobante, Long documentoProveedor) {    	
    	try {      
    		List<SunatContribuyente> results = this.buscarTsContribuyente(tbComprobante, documentoProveedor);
    		if(results != null && results.size()>0)     		  	
    			return;    		
    		tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
   	 		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_2621);
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getRetencionProveedor Exception \n"+errors);
		}    	
     }
    
    public void getPercepcionCliente(Documento tbComprobante, Long documentoCliente) {    	
    	try {      
    		List<SunatContribuyente> results = this.buscarTsContribuyente(tbComprobante, documentoCliente);
    		if(results != null && results.size()>0)     		  	
    			return;    		
    		tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
   	 		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_2605);
     		String cpe = "";
   			if(tbComprobante.getErrorLog()!=null && !(tbComprobante.getErrorLog().isEmpty()))
   				cpe = tbComprobante.getErrorLog()+Constantes.OSE_SPLIT_3;
   			cpe = cpe+"["+String.valueOf(documentoCliente)+"]";
   	 		tbComprobante.setErrorLog(cpe);
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getPercepcionCliente Exception \n"+errors);
		}    	
     }
    
    public void getGuiaRemitente(Documento tbComprobante, Long documentoRemitente) {    	
    	try {      
    		List<SunatContribuyente> results = this.buscarTsContribuyente(tbComprobante, documentoRemitente);
    		if(results != null && results.size()>0)     		  	
    			return;    		
    		tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
   	 		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_2104);
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getGuiaRemitente Exception \n"+errors);
		}    	
     }    
    
    public void getGuiaProveedor(Documento tbComprobante, Long documentoProveedor) {    	
    	try {      
    		List<SunatContribuyente> results = this.buscarTsContribuyente(tbComprobante, documentoProveedor);
    		if(results != null && results.size()>0) {
	    		for(SunatContribuyente tc : results){
	    			if(!tc.getIndEstado().equals(Constantes.SUNAT_Indicador_00)) {
	    				tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    				tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_4051);
	    				return;
	    			}else if(!tc.getIndCondicion().equals(Constantes.SUNAT_Indicador_00)){
	    				tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    				tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_4052);
	    				return;
	    			}     	
	    			return;    		
	    		}    			    		
    		}
    		tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
   	 		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_4050);
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getGuiaProveedor Exception \n"+errors);
		}    	
    }  
    
    public void getDAEParticipe(Documento tbComprobante, Long rucParticipe) {
    	try {  
   		 	String obsv = "";
   		 	List<SunatContribuyente> results = this.buscarTsContribuyente(tbComprobante, rucParticipe);	
   		 	if(results != null && results.size()>0) {
	   		 	for(SunatContribuyente tc : results){  		
		   			 if(!tc.getIndEstado().equals(Constantes.SUNAT_Indicador_00)) {
		   				 if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
		   					 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
		   				 tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4351); 
		   			 }
		   			 if(tc.getIndCondicion().equals(Constantes.SUNAT_Indicador_12)){
		   				 if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
		   					 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
		   	    		 tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4352); 
		   			 }     	
		   			 return;
	   		 	}
   		 	}
   		 	tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
   			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_2605);
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDAEParticipe Exception \n"+errors);
		}
   	 	return;
    }    
    
    public void getContribuyenteComunicaBajas(Documento tbComprobante) {
   	 	try {  
	   		 List<SunatContribuyente> results = this.buscarTsContribuyente(tbComprobante, tbComprobante.getRucEmisor());	    		 
	   		 if(results != null && results.size()>0) {
	    		 for(SunatContribuyente tc : results){  		
	    			 if(tc.getIndCondicion().equals(Constantes.SUNAT_Indicador_12)){
	    				 tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    				 tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_2011);
	    				 return;
	    			 }     	
	    		 }
	   		 }
   	 	}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getContribuyenteComunicaBajas Exception \n"+errors);
		}
   	 	return;
    }    
     	
 	private List<SunatContribuyente> buscarTsContribuyente(Documento tbComprobante, Long documento) {
 		log.info("buscarTsContribuyente --> "+documento);
		try {    
            List<SunatContribuyente> results = this.findByColumnName("numRuc", documento);
            if(results != null && results.size()>0)
            	return results;             
    	} catch (Exception e) {
    		tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTsContribuyente Exception \n"+errors);
		}
        return null;
	}    
}
