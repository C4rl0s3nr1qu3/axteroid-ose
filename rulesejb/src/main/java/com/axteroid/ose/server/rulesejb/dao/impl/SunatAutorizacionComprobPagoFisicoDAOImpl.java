package com.axteroid.ose.server.rulesejb.dao.impl;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatAutorizacionComprobPagoFisico;
import com.axteroid.ose.server.rulesejb.dao.SunatAutorizacionComprobPagoFisicoDAOLocal;
import com.axteroid.ose.server.rulesejb.emf.impl.DAOComImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoDocumentoSUNATEnum;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;
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
@Local(SunatAutorizacionComprobPagoFisicoDAOLocal.class)
public class SunatAutorizacionComprobPagoFisicoDAOImpl extends DAOComImpl<SunatAutorizacionComprobPagoFisico> 
		implements SunatAutorizacionComprobPagoFisicoDAOLocal {
	private static final Logger log = LoggerFactory.getLogger(SunatAutorizacionComprobPagoFisicoDAOImpl.class);
	
    public SunatAutorizacionComprobPagoFisicoDAOImpl() {}
     
     public void getAutorizacionComprobPagoFisico(Documento tbComprobante) {
    	 try {
    		 List<SunatAutorizacionComprobPagoFisico> results = this.buscarAutorizacionComprobPagoFisico(tbComprobante);	
    		 if(results != null && results.size()>0)    		
    			 return; 
    		tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
    		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_2404);
 		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getAutorizacionComprobPagoFisico Exception \n"+errors);
		}
     }
       
     public void getAutorizacionComprobPagoFisico_FB(Documento tbComprobante) {
    	 try {
    		 List<SunatAutorizacionComprobPagoFisico> results = this.buscarAutorizacionComprobPagoFisico(tbComprobante);	
    		 if(results != null && results.size()>0)    		
    			 return;    		
    		tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
    		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_3207);
 		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getAutorizacionComprobPagoFisico Exception \n"+errors);
		}
     }
     
     private List<SunatAutorizacionComprobPagoFisico> buscarAutorizacionComprobPagoFisico(Documento tbComprobante) {
    	log.info("buscarAutorizacionComprobPagoFisico (1): "+tbComprobante.getRucEmisor()+" - "+ tbComprobante.getTipoDocumento()+" - "+tbComprobante.getSerie()+" - "+tbComprobante.getNumeroCorrelativo()); 
     	try{
     		String consulta = "SELECT d FROM SunatAutorizacionComprobPagoFisico d "
  				+" where d.sunatAutorizacionComprobPagoFisicoPK.numRuc = :numRuc "
  				+" and d.sunatAutorizacionComprobPagoFisicoPK.codCpe = :codCpe "
  				+" and d.sunatAutorizacionComprobPagoFisicoPK.numSerieCpe = :numSerieCpe "
  				+" and :numCpe between d.sunatAutorizacionComprobPagoFisicoPK.numIniCpe and d.numFinCpe ";
     		Map<String, Object> parameterValues = new HashMap<String, Object>();
     		parameterValues.put("numRuc", tbComprobante.getRucEmisor());
     		parameterValues.put("codCpe", tbComprobante.getTipoDocumento());
     		parameterValues.put("numSerieCpe", tbComprobante.getSerie());
     		int numCpe = 0;	
    		if(StringUtil.hasString(tbComprobante.getNumeroCorrelativo()))
    			numCpe = Integer.parseInt(tbComprobante.getNumeroCorrelativo());
    		parameterValues.put("numCpe", numCpe);
     		List<SunatAutorizacionComprobPagoFisico> results = this.query(consulta, parameterValues); 
     		if(results != null && results.size()>0)
     			return results;
     	} catch (Exception e) {
     		tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
     		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
     		tbComprobante.setErrorLog(e.toString());
 			StringWriter errors = new StringWriter();				
 			e.printStackTrace(new PrintWriter(errors));
 			log.error("buscarAutorizacionComprobPagoFisico Exception \n"+errors);
 		}
         return null;      	
     }     
     
     public void getAutorizacionComprobPagoFisico(Documento tbComprobante, String tipoDocRefPri, 
    		 String nroDocRefPri) {
		 //log.info(tbComprobante.getUblVersion() + "-" + tbComprobante.getTipoComprobante());
		 log.info("getAutorizacionComprobPagoFisico (4) ");
    	 
    	 try {
    		 List<SunatAutorizacionComprobPagoFisico> results = this.buscarAutorizacionComprobPagoFisico(tbComprobante, 
    				 tipoDocRefPri, nroDocRefPri, tbComprobante.getRucEmisor());	
    		 if(results != null && results.size()>0)    		
    			 return;    		
    		 if(tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.FACTURA.getCodigo()) || 
    				 tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.BOLETA.getCodigo())) { 
    			 tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
    			 tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_2404);
    			 return;
    		 }   
    		
    		 if((tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.NOTA_CREDITO.getCodigo())
   				 || tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.NOTA_DEBITO.getCodigo()))) { 
    			 String obsv = "";
   	  			if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
   	  				obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
   	  			tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_2404);
   	  			return;
    		 }  
   		    		
 		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getAutorizacionComprobPagoFisico Exception \n"+errors);
		}
     }     
     
     public void getAutorizacionComprobPagoFisico_RP(Documento tbComprobante, String tipoDocRefPri, 
    		 String nroDocRefPri, Long numRUC) {
    	 try {    		 
    		 List<SunatAutorizacionComprobPagoFisico> results = this.buscarAutorizacionComprobPagoFisico(tbComprobante, 
    				 tipoDocRefPri, nroDocRefPri, numRUC);	
    		 if(results != null && results.size()>0)  
    			 return;  
 		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getAutorizacionComprobPagoFisico_RP Exception \n"+errors);
		}
     }     
     
     public void getAutorizacionComprobPagoFisico_RC(Documento tbComprobante, EResumenDocumentoItem rdi) {
    	 try {    		 
    		 List<SunatAutorizacionComprobPagoFisico> results = this.buscarAutorizacionComprobPagoFisico(tbComprobante, 
    				 rdi.getDocumentTypeCode(), rdi.getId(), tbComprobante.getRucEmisor());	
    		 if(results != null && results.size()>0)  
    			 return;
//    		 String obsv = "";
//    		 if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
//    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
//    		 tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4204); 		 
     		tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
     		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_3207);  
//     		String cpe = "";
//   			if(tbComprobante.getErrorLog()!=null && !(tbComprobante.getErrorLog().isEmpty()))
//   				cpe = tbComprobante.getErrorLog()+OseConstantes.OSE_SPLIT_3;
//   			cpe = cpe+"["+rdi.getDocumentTypeCode()+"-"+rdi.getId()+"]";  
//    		 tbComprobante.setErrorLog(cpe);
 		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getAutorizacionComprobPagoFisico_RC Exception \n"+errors);
		}
     }  
     
     public void getAutorizacionComprobPagoFisico_RP(Documento tbComprobante) {
    	 try {    		 
    		 List<SunatAutorizacionComprobPagoFisico> results = this.buscarAutorizacionComprobPagoFisico(tbComprobante, 
    				 tbComprobante.getTipoDocumento(), tbComprobante.getSerie()+"-"+tbComprobante.getNumeroCorrelativo(), 
    				 tbComprobante.getRucEmisor());	
    		 if(results != null && results.size()>0)  
    			 return;	 
     		tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
     		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_3207);  
     		
//    		 String obsv = "";
//    		 if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
//    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
//    		 tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4204); 	     		
//     		String cpe = "";
//   			if(tbComprobante.getErrorLog()!=null && !(tbComprobante.getErrorLog().isEmpty()))
//   				cpe = tbComprobante.getErrorLog()+OseConstantes.OSE_SPLIT_3;
//   			cpe = cpe+"["+rdi.getDocumentTypeCode()+"-"+rdi.getId()+"]";  
//    		 tbComprobante.setErrorLog(cpe);
 		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getAutorizacionComprobPagoFisico_RC Exception \n"+errors);
		}
     }      
     
     public void getAutorizacionComprobPagoFisico_RC_BR(Documento tbComprobante, String tipoDocRefPri, 
    		 String nroDocRefPri, Long numRUC) {
    	 try {    		 
    		 List<SunatAutorizacionComprobPagoFisico> results = this.buscarAutorizacionComprobPagoFisico(tbComprobante, 
    				 tipoDocRefPri, nroDocRefPri, numRUC);	
    		 if(results != null && results.size()>0)  
    			 return;
    		 String obsv = "";
    		 if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
    		 tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_2988);
//     		tbComprobante.setErrorComprobante(OseConstantes.CONTENT_FALSE.charAt(0));
//     		tbComprobante.setErrorNumero(OseConstantes.SUNAT_ERROR_2988);  
//     		String cpe = "";
//   			if(tbComprobante.getErrorLog()!=null && !(tbComprobante.getErrorLog().isEmpty()))
//   					cpe = tbComprobante.getErrorLog()+OseConstantes.OSE_SPLIT_3;
//   			cpe = cpe+"["+tipoDocRefPri+"-"+nroDocRefPri+"]";
//   			tbComprobante.setErrorLog(cpe);
 		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getAutorizacionComprobPagoFisico_RC_BR Exception \n"+errors);
		}
     }   
     
     private List<SunatAutorizacionComprobPagoFisico> buscarAutorizacionComprobPagoFisico(Documento tbComprobante, 
    		 String tipoDocRefPri, String nroDocRefPri, Long numRUC) {
     	try{
     		String [] docRefPri = nroDocRefPri.split("-");
     		String consulta = "SELECT d FROM SunatAutorizacionComprobPagoFisico d "
  				+" where d.sunatAutorizacionComprobPagoFisicoPK.numRuc = :numRuc "
  				+" and d.sunatAutorizacionComprobPagoFisicoPK.codCpe = :codCpe "
  				+" and d.sunatAutorizacionComprobPagoFisicoPK.numSerieCpe = :numSerieCpe "
  				+" and :numCpe between d.sunatAutorizacionComprobPagoFisicoPK.numIniCpe and d.numFinCpe ";
     		Map<String, Object> parameterValues = new HashMap<String, Object>();
     		parameterValues.put("numRuc", numRUC);
     		parameterValues.put("codCpe", tipoDocRefPri);
     		parameterValues.put("numSerieCpe", docRefPri[0]);
     		int numCpe = 0;			
			if(StringUtil.hasString(docRefPri[1]))
				numCpe = Integer.parseInt(docRefPri[1]);
     		parameterValues.put("numCpe", numCpe);    
     		log.info("buscarAutorizacionComprobPagoFisico (4) --> "+numRUC+"-"+ tipoDocRefPri+"-"+docRefPri[0]+"-"+numCpe);
     		List<SunatAutorizacionComprobPagoFisico> results = this.query(consulta, parameterValues);    	
     		if(results != null && results.size()>0)
     			return results;
     	} catch (Exception e) {
     		tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
     		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
     		tbComprobante.setErrorLog(e.toString());
 			StringWriter errors = new StringWriter();				
 			e.printStackTrace(new PrintWriter(errors));
 			log.error("buscarAutorizacionComprobPagoFisico Exception \n"+errors);
 		}
         return null;
      	
      }
     
     public void getAutorizacionComprobPagoFisico_Anticipo(Documento tbComprobante, Long numDocumento, 
     		String tipoDocRefPri, String nroDocRefPri) {
    	 log.info("getAutorizacionComprobPagoFisico_Anticipo "+numDocumento+" - "+tipoDocRefPri+" - "+nroDocRefPri);
     	try {        		     		
     		if(tipoDocRefPri.equals("02"))
    			tipoDocRefPri = Constantes.SUNAT_FACTURA;
     		List<SunatAutorizacionComprobPagoFisico> results = this.buscarAutorizacionComprobPagoFisico(tbComprobante, 
     				tipoDocRefPri, nroDocRefPri, numDocumento);	
    	 	if(results != null && results.size()>0)   		   	
    	 		return;  
    	 	if(tbComprobante.getTipoDocumento().trim().equals(Constantes.SUNAT_FACTURA)) {
    	 		tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
    	 		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_3219);   
    	 	}
    	 	if(tbComprobante.getTipoDocumento().trim().equals(Constantes.SUNAT_BOLETA)) {
	     		String obsv = "";
	 			if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
	 				obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
	 			tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4441);
    	 	}
     	} catch (Exception e) {
     		tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
     		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
     		tbComprobante.setErrorLog(e.toString());
 			StringWriter errors = new StringWriter();				
 			e.printStackTrace(new PrintWriter(errors));
 			log.error("getAutorizacionComprobPagoFisico_Anticipo Exception \n"+errors);
 		}
     }       
}
