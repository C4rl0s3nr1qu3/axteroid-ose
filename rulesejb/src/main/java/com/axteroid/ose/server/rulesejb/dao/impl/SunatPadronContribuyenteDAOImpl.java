package com.axteroid.ose.server.rulesejb.dao.impl;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatPadronContribuyente;
import com.axteroid.ose.server.rulesejb.dao.SunatPadronContribuyenteDAOLocal;
import com.axteroid.ose.server.rulesejb.emf.impl.DAOComImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;
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
@Local(SunatPadronContribuyenteDAOLocal.class)
public class SunatPadronContribuyenteDAOImpl extends DAOComImpl<SunatPadronContribuyente> 
		implements SunatPadronContribuyenteDAOLocal {
	private static final Logger log = LoggerFactory.getLogger(SunatPadronContribuyenteDAOImpl.class);
    public SunatPadronContribuyenteDAOImpl() {}

    public void getPadronContribuyente_RP(Documento documento) {
   	 	try {  
   	 		List<SunatPadronContribuyente> results = this.buscarTsPadronContribuyente(documento, documento.getRucEmisor());	   	 		
   	 		if(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_RETENCION)) {
   	 			if(results != null && results.size()>0) {
		   	 		for(SunatPadronContribuyente pc : results){
		   	 			if(pc.getSunatPadronContribuyentePK().getIndPadron().equals(Constantes.SUNAT_Indicador_03))
			 				return;   	 			
		   	 		}
   	 			}
	   	 		documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	   	 		documento.setErrorNumero(Constantes.SUNAT_ERROR_2617);
	   	 		return;
   	 		}
   	 		if(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_PERCEPCION)) {
   	 			if(results != null && results.size()>0) {
		   	 		for(SunatPadronContribuyente pc : results){
		   	 			if((pc.getSunatPadronContribuyentePK().getIndPadron().equals(Constantes.SUNAT_Indicador_01)) ||
		   	 				(pc.getSunatPadronContribuyentePK().getIndPadron().equals(Constantes.SUNAT_Indicador_02)))
			 				return;   	 			
		   	 		}
   	 			}
//	   	 		documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
//	   	 		documento.setErrorNumero(Constantes.SUNAT_ERROR_4285);
	    		String obsv = "";
	  			if(documento.getObservaNumero()!=null && !(documento.getObservaNumero().isEmpty()))
	  				obsv = documento.getObservaNumero()+Constantes.OSE_SPLIT_3;
	  			documento.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4204);
	     		String cpe = "";
	   			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
	   				cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
	   			cpe = cpe+"["+documento.getTipoDocumento()+"-"+documento.getSerie()+"-"+documento.getNumeroCorrelativo()+"]";
	   			documento.setErrorLog(cpe);	   	 		
	   	 		return;
   	 		}   	 		
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getPadronContribuyente_RP Exception \n"+errors);
		}
   	 	return;
    }
    
 	private List<SunatPadronContribuyente> buscarTsPadronContribuyente(Documento documento, Long documentoContribuyente) {
 		//log.info("buscarTsPadronContribuyente --> "+documentoContribuyente);
		try {    
			String consulta = "SELECT d FROM SunatPadronContribuyente d "
	  				+" where d.sunatPadronContribuyentePK.numRuc = :numRuc ";
	     	Map<String, Object> parameterValues = new HashMap<String, Object>();
	     	parameterValues.put("numRuc", documentoContribuyente);
            List<SunatPadronContribuyente> results = this.query(consulta, parameterValues);
            if(results != null && results.size()>0)
            	return results;   
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTsPadronContribuyente Exception \n"+errors);
		}
        return null;
	}
 	
    public void getPadronContribuyente_Proveedor(Documento documento, Long documentoProveedor) {
   	 	try {  
   	 		List<SunatPadronContribuyente> results = this.buscarTsPadronContribuyente(documento, documentoProveedor);	
   	 		if(results != null && results.size()>0) {
	   	 		for(SunatPadronContribuyente pc : results){ 	
	   	 			String indicador = pc.getSunatPadronContribuyentePK().getIndPadron();
	   	 			//log.info("indicador "+indicador);
	   	 			if(indicador.equals(Constantes.SUNAT_Indicador_01) ||
	   	 				indicador.equals(Constantes.SUNAT_Indicador_02) ||
	   	 				indicador.equals(Constantes.SUNAT_Indicador_03) ||
	   	 				indicador.equals(Constantes.SUNAT_Indicador_10)) {  	 
	   	 				String obsv = "";
	   	 				if(documento.getObservaNumero()!=null && !(documento.getObservaNumero().isEmpty()))
	   	 					obsv = documento.getObservaNumero()+Constantes.OSE_SPLIT_3;
	   	 				documento.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4091);
	   	 			}  	 			
	   	 		}  
   	 		}
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getPadronContribuyente_Proveedor Exception \n"+errors);
		}
   	 	return;
    }
    
    public void getPadronContribuyente_Cliente(Documento documento, Long documentoCliente) {
   	 	try {  
   	 		List<SunatPadronContribuyente> results = this.buscarTsPadronContribuyente(documento, documentoCliente);	
   	 		if(results != null && results.size()>0) {
	   	 		for(SunatPadronContribuyente pc : results){ 	
	   	 			String indicador = pc.getSunatPadronContribuyentePK().getIndPadron();
	   	 			//log.info("indicador "+indicador);
	   	 			String obsv = "";
	   	 			if(documento.getObservaNumero()!=null && !(documento.getObservaNumero().isEmpty()))
	   	 				obsv = documento.getObservaNumero()+Constantes.OSE_SPLIT_3;
	   	 			if(indicador.equals(Constantes.SUNAT_Indicador_03))
	   	 				documento.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4089);
	   	 			
	   	 			if(indicador.equals(Constantes.SUNAT_Indicador_04))
	   	 				documento.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4090); 							 
	   	 				
	   	 		}
   	 		}
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getPadronContribuyente_Cliente Exception \n"+errors);
		}
   	 	return;
    }
    
    public void getPadronContribuyente_Cliente_Percepcion(Documento documento, Long documentoCliente) {
   	 	try {  
  	 		List<SunatPadronContribuyente> resulEmisor = this.buscarTsPadronContribuyente(documento, documento.getRucEmisor());	
  	 		boolean indEmisor02 = false;
  	 		if(resulEmisor != null && resulEmisor.size()>0) {
		 		for(SunatPadronContribuyente pc : resulEmisor){
		 			if(pc.getSunatPadronContribuyentePK().getIndPadron().equals(Constantes.SUNAT_Indicador_02))
		 				indEmisor02 = true;
		 		}
  	 		}
	 		if(!indEmisor02) return;
	 		boolean indCliente02 = false;
   	 		List<SunatPadronContribuyente> resulCliente = this.buscarTsPadronContribuyente(documento, documentoCliente);	
   	 		if(resulCliente != null && resulCliente.size()>0) {
	   	 		for(SunatPadronContribuyente pc : resulCliente){  	
	   	 			if(pc.getSunatPadronContribuyentePK().getIndPadron().equals(Constantes.SUNAT_Indicador_02))
	   	 				indCliente02 = true; 	 							    	 				
	   	 		}
   	 		}
   	 		if(indCliente02) {
   	 			String obsv = "";
	 			if(documento.getObservaNumero()!=null && !(documento.getObservaNumero().isEmpty()))
	 				obsv = documento.getObservaNumero()+Constantes.OSE_SPLIT_3;
	 			documento.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4086);  
   	 		}
	 		
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getPadronContribuyente_Cliente_Percepcion Exception \n"+errors);
		}
   	 	return;
    }
    
    public void getPadronContribuyente_RC_Cliente(Documento documento, Long documentoCliente) {
   	 	try {  
   	 		List<SunatPadronContribuyente> results = this.buscarTsPadronContribuyente(documento, documentoCliente);
   	 		String obsv = "";
   	 		if(results != null && results.size()>0) {
	   	 		for(SunatPadronContribuyente pc : results){ 	
	   	 			String indicador = pc.getSunatPadronContribuyentePK().getIndPadron();
	   	 			log.info("indicador "+indicador);   	 			
	   	 			if(documento.getObservaNumero()!=null && !(documento.getObservaNumero().isEmpty()))
	   	 				obsv = documento.getObservaNumero()+Constantes.OSE_SPLIT_3;
	   	 			if(indicador.equals(Constantes.SUNAT_Indicador_03))
	   	 				documento.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4089);
	   	 			
	   	 			if(indicador.equals(Constantes.SUNAT_Indicador_04))
	   	 				documento.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4090); 							 
	   	 			
	   	 			if(indicador.equals(Constantes.SUNAT_Indicador_02))
		 				documento.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4086);   	   	 		   	   	 		
	   	 			return;
	   	 		}
   	 		}
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getPadronContribuyente_RC_Cliente Exception \n"+errors);
		}
   	 	return;
    }
    
    public void getPadronContribuyente_FBNCND(Documento documento) {
   	 	try {  
   	 		List<SunatPadronContribuyente> results = this.buscarTsPadronContribuyente(documento, documento.getRucEmisor());	   	 		
   	 		if(results != null && results.size()>0) {
	   	 		for(SunatPadronContribuyente pc : results){
	   	 			if(pc.getSunatPadronContribuyentePK().getIndPadron().equals(Constantes.SUNAT_Indicador_13)) {
	   	 			    documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	   	 			    documento.setErrorNumero(Constantes.SUNAT_ERROR_1080); 
	   	 			    return; 
	   	 			}	   	 			
	   	 		}
   	 		}
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getPadronContribuyente_BFNCND Exception \n"+errors);
		}
   	 	return;
    }   
    
    public void getPadronContribuyente_F(Documento documento) {
   	 	try {  
   	 		List<SunatPadronContribuyente> results = this.buscarTsPadronContribuyente(documento, documento.getRucEmisor());	   	 		
   	 		if(results != null && results.size()>0) {
   	 			boolean b = true;
	   	 		for(SunatPadronContribuyente pc : results){
	   	 			if(pc.getSunatPadronContribuyentePK().getIndPadron().equals(Constantes.SUNAT_Indicador_14)) 
	   	 				b = false;	
	   	 		}
	   	 		if(b) {
   	 			    documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
   	 			    documento.setErrorNumero(Constantes.SUNAT_ERROR_3281); 
	   	 		}	   	 			
   	 		}
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getPadronContribuyente_BFNCND Exception \n"+errors);
		}
   	 	return;
    }    

    public void getPadronContribuyente_FB(Documento documento) {
   	 	try {  
   	 		List<SunatPadronContribuyente> results = this.buscarTsPadronContribuyente(documento, documento.getRucEmisor());	   	 		
   	 		if(results != null && results.size()>0) {
   	 			boolean b = true;
	   	 		for(SunatPadronContribuyente pc : results){
	   	 			if(pc.getSunatPadronContribuyentePK().getIndPadron().equals(Constantes.SUNAT_Indicador_05)) 
	   	 				b = false;	
	   	 		}
	   	 		if(b) {
   	 			    documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
   	 			    documento.setErrorNumero(Constantes.SUNAT_ERROR_3097); 
	   	 		}	   	 			
   	 		}
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getPadronContribuyente_BFNCND Exception \n"+errors);
		}
   	 	return;
    }    
    
    public void getPadronContribuyenteCustomer(Documento documento, String rucCustomer ) {
   	 	try {  
   	 		List<SunatPadronContribuyente> results = this.buscarTsPadronContribuyente(documento, Long.parseLong(rucCustomer));	   	 		
 			boolean ruc = true;
   	 		if(results != null && results.size()>0) {
	   	 		for(SunatPadronContribuyente pc : results){
	   	 			if(pc.getSunatPadronContribuyentePK().getIndPadron().equals(Constantes.SUNAT_Indicador_03))
	   	 				ruc = false; 			   	 			
	   	 		}
 			}
   	 		if(ruc) {
   	 			Date hoy = new Date();
   	 			Date limite = DateUtil.newDate(2021, 12, 31);
   	 			long ldias = DateUtil.deltaDays(hoy,limite);	
   	 			//log.info("ldias: "+ldias);
   	 			if(ldias>0) {
   	 				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
   	 				documento.setErrorNumero(Constantes.SUNAT_ERROR_3262);
   	 				return;
   	 			}else {
   	 				String obsv = "";
   	 				if(documento.getObservaNumero()!=null && !(documento.getObservaNumero().isEmpty()))
	 					obsv = documento.getObservaNumero()+Constantes.OSE_SPLIT_3;
	 				documento.setObservaNumero(obsv+Constantes.SUNAT_ERROR_3262);
   	 			}
   	 		}
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getPadronContribuyenteCustomer Exception \n"+errors);
		}
   	 	return;
    }
    
    public void getPadronContribuyenteSupplier(Documento documento, String rucSupplier ) {
   	 	try {  
   	 		List<SunatPadronContribuyente> results = this.buscarTsPadronContribuyente(documento, Long.parseLong(rucSupplier));	   	 		
 			if(results != null && results.size()>0) {
	   	 		for(SunatPadronContribuyente pc : results){
	   	 			if(pc.getSunatPadronContribuyentePK().getIndPadron().equals(Constantes.SUNAT_Indicador_03)){ 
	   	   	 			Date hoy = new Date();
	   	   	 			Date limite = DateUtil.newDate(2021, 12, 31);
	   	   	 			long ldias = DateUtil.deltaDays(hoy,limite);	
	   	   	 			//log.info("ldias: "+ldias);
	   	   	 			if(ldias>0) {
	   	   	 				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	   	   	 				documento.setErrorNumero(Constantes.SUNAT_ERROR_3269);
	   	   	 				return;	 		
	   	   	 			}else {
	   	   	 				String obsv = "";
	   	   	 				if(documento.getObservaNumero()!=null && !(documento.getObservaNumero().isEmpty()))
	   		 					obsv = documento.getObservaNumero()+Constantes.OSE_SPLIT_3;
	   		 				documento.setObservaNumero(obsv+Constantes.SUNAT_ERROR_3269);
	   	   	 			}	   	 				
	   	 			}
	   	 		}
 			}	   	 			 		
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getPadronContribuyenteSupplier Exception \n"+errors);
		}
   	 	return;
    }
    
    
}
