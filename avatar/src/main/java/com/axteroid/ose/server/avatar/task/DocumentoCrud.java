package com.axteroid.ose.server.avatar.task;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.apirest.list.ListSunatRest;
import com.axteroid.ose.server.apirest.sunat.ConsultaIntegradaSunatRest;
import com.axteroid.ose.server.avatar.dao.DocumentoPGDao;
import com.axteroid.ose.server.avatar.dao.impl.DocumentoPGDaoImpl;
import com.axteroid.ose.server.avatar.jdbc.RsetPostGresJDBC;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatComprobantesPagoElectronicos;
import com.axteroid.ose.server.jpa.model.SunatComprobantesPagoElectronicosPK;
import com.axteroid.ose.server.tools.bean.ComprobantePago;
import com.axteroid.ose.server.tools.bean.SunatRequest;
import com.axteroid.ose.server.tools.bean.SunatTokenResponse;
import com.axteroid.ose.server.tools.bean.SunatBeanResponse;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoAvatarPropertiesEnum;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EReversionDocumentoItem;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.axteroid.ose.server.tools.util.FileUtil;
import com.axteroid.ose.server.tools.util.StringUtil;

public class DocumentoCrud {
	
	private static final Logger log = LoggerFactory.getLogger(DocumentoCrud.class);
	private SunatTokenResponse sunatToken = new SunatTokenResponse();
	
	public void upLoadCDRSUNAT(long idComprobante, String dirFile) {		
		log.info("upLoadCDRSUNAT --> "+idComprobante); 
		try {
			Documento tbComprobante = new Documento();
			tbComprobante.setId(idComprobante);
			File f = new File(dirFile);
			RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
			rsetPostGresJDBC.listComprobantes(tbComprobante);		
			byte[] mensaje = FileUtil.toByte(f);
			tbComprobante.setMensajeSunat(mensaje);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))) 
				return;		
			DocumentoPGDao tbComprobantePGDao = new DocumentoPGDaoImpl();
			tbComprobantePGDao.updateUblCdrMensajeSunat(tbComprobante);
			
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("upLoadCDRSUNAT Exception \n"+errors);
		}
	}
	
	public void upLoadCDROseServer(long idComprobante, String dirCDR) {		
		log.info("upLoadCDROseServer --> "+idComprobante);
		try {
			Documento tbComprobante = new Documento();
			tbComprobante.setId(idComprobante);
			File f = new File(dirCDR);
			RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
			rsetPostGresJDBC.listComprobantes(tbComprobante);			
			byte[] mensaje = FileUtil.toByte(f);
			tbComprobante.setCdr(mensaje);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))) 
				return;
			DocumentoPGDao tbComprobantePGDao = new DocumentoPGDaoImpl();
			tbComprobantePGDao.updateFileCDROseServer(tbComprobante);
			
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("upLoadCDROseServer Exception \n"+errors);
		}
	}
	
	public void upLoadUBLOseServer(long idComprobante, String dirUBL) {		
		log.info("upLoadUBLOseServer --> "+idComprobante);
		try {
			Documento tbComprobante = new Documento();
			tbComprobante.setId(idComprobante);
			File f = new File(dirUBL);
			RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
			rsetPostGresJDBC.listComprobantes(tbComprobante);			
			byte[] mensaje = FileUtil.toByte(f);
			tbComprobante.setUbl(mensaje);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))) 
				return;
			DocumentoPGDao tbComprobantePGDao = new DocumentoPGDaoImpl();
			tbComprobantePGDao.updateFileUBLOseServer(tbComprobante);
			
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("upLoadUBLOseServer Exception \n"+errors);
		}
	}		
	
	public void updateComprobanteError(long idComprobante) {		
		log.info("updateComprobanteError --> "+idComprobante);
		try {
			Documento tbComprobante = new Documento();
			tbComprobante.setId(idComprobante);
			RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
			rsetPostGresJDBC.listComprobantes(tbComprobante);
			tbComprobante.setErrorComprobante("0".charAt(0));
			tbComprobante.setErrorNumero(null);
			tbComprobante.setErrorDescripcion(null);
			tbComprobante.setErrorLog(null);
			tbComprobante.setRespuestaSunat("0160");	
			DocumentoPGDao tbComprobantePGDao = new DocumentoPGDaoImpl();
			tbComprobantePGDao.updateError(tbComprobante);			
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateComprobanteError Exception \n"+errors);
		}
	}
	
	
	public void updateComprobanteEstado(long idComprobante) {		
		log.info("updateComprobanteEstado --> "+idComprobante);
		try {
			Documento tbComprobante = new Documento();
			tbComprobante.setId(idComprobante);
			RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
			rsetPostGresJDBC.listComprobantes(tbComprobante);
			tbComprobante.setErrorComprobante("0".charAt(0));
			tbComprobante.setEstado("30"); 	
			DocumentoPGDao tbComprobantePGDao = new DocumentoPGDaoImpl();
			tbComprobantePGDao.updateEstado(tbComprobante);			
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateComprobante Exception \n"+errors);
		}	
	}
	
	public void updateRespuestaSunat(long idComprobante) {		
		log.info("updateRespuestaSunat --> "+idComprobante);
		try {
			Documento tbComprobante = new Documento();
			tbComprobante.setId(idComprobante);
			RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
			rsetPostGresJDBC.listComprobantes(tbComprobante);
			tbComprobante.setEstado("90");
			tbComprobante.setRespuestaSunat("1033");
			DocumentoPGDao tbComprobantePGDao = new DocumentoPGDaoImpl();
			tbComprobantePGDao.updateRespuestaSunat(tbComprobante);			
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateRespuesta Exception \n"+errors);
		}	
	}
	
	public  List<Documento> getComprobantes_Estado_CDR_OSE(Documento tbComprobante) {		
		log.info("getComprobantes_Estado_CDR_OSE --> "+tbComprobante.getEstado()+ " - "+tbComprobante.getErrorComprobante());
		List<Documento> listTbComprobante = new ArrayList<Documento>(); 
		try {					
			RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
			listTbComprobante = rsetPostGresJDBC.listComprobantes(tbComprobante);			 
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("getComprobantes_Estado_CDR_OSE Exception \n"+errors);
		}	
		return listTbComprobante;
	}	
	
	public  List<Documento> selectListComprobantes_Estado_Respuesta_OSE(Documento tbComprobante) {		
		log.info("selectListComprobantes_Estado_Respuesta_OSE --> "+tbComprobante.getEstado()+ " - "+tbComprobante.getErrorComprobante()+ " - "+tbComprobante.getRespuestaSunat());
		List<Documento> listTbComprobante = new ArrayList<Documento>(); 
		try {		
			RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
			listTbComprobante = rsetPostGresJDBC.listComprobantes(tbComprobante);			 
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("selectListComprobantes_Estado_Respuesta_OSE Exception \n"+errors);
		}	
		return listTbComprobante;
	}
	
	public  List<Documento> getComprobantes_Blob(int year, int mes, int dia) {		
		//log.info("getComprobantes_Blob --> ");
		List<Documento> listTbComprobante = new ArrayList<Documento>(); 
		try {							
			//listTbComprobante = rsetPostGresJDBC.selectComprobante_Blob(year, mes, dia);
			 
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("getComprobantes_Blob Exception \n"+errors);
		}	
		return listTbComprobante;
	}		
	
	public  List<Documento> getComprobantes_PG_UBL() {		
		log.info("getComprobantes_PG_UBL --> ");
		List<Documento> listTbComprobante = new ArrayList<Documento>(); 
		try {						
			//listTbComprobante = tbComprobantePGDao.selectComprobante_PG_UBL(tipBD);			 
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("getComprobantes_PG_UBL Exception \n"+errors);
		}	
		return listTbComprobante;
	}
	
	   public void revisarRespuesta2RCItemSunatList(Documento tbComprobante, EResumenDocumentoItem rdi, 
	    		String fechaEmision, SunatTokenResponse sunatToken) {
	    	try {        		
	    		log.info("revisarRespuesta2RCItemSunatList: "+tbComprobante.getRucEmisor()+"-"+rdi.getDocumentTypeCode()+"-"+rdi.getId()+" | Status: "+rdi.getStatus());    		   		
	    		List<SunatComprobantesPagoElectronicos> results = this.revisarCompobantePagoGetSunatList(
	    				tbComprobante, rdi.getDocumentTypeCode(), rdi.getId());
	    		String cpe = "";
	    		boolean rev = false;
	    		boolean bSunatList = false;
	    		boolean bCpeSunatList = false;
	    		boolean bCpeConsSunat = false;
	    		boolean bCpeReference = false;
	    		for(SunatComprobantesPagoElectronicos cp : results) {       			
	    			bSunatList = true;
	    			log.info("rdi.getStatus(): "+rdi.getStatus()+"| cp.getIndEstadoCpe(): "+cp.getIndEstadoCpe());
	    			if((rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Adicionar) && 
	    					  ((cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Recha)
	    					|| (cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Acept) 
	    					|| (cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Anula))) 
	    				bCpeSunatList = true;
	    			if((rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Modificar) && 
	    					((cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Recha)
	    					|| (cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Anula))) 
	    				bCpeSunatList = true;
	    			if((rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Anulado) && 
	    					((cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Recha)
	    					|| (cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Anula))) 
	    				bCpeSunatList = true;
	    			if(bCpeSunatList) {
	    				rev = true;
	    				//log.info("1) >>>>>>>>>>> DATA: NO-OK ");
	    				Integer OseServerNoOK = Integer.parseInt(tbComprobante.getDireccion())+1;
	    				tbComprobante.setDireccion(String.valueOf(OseServerNoOK));
	    			} 	    			
	    		}		
	    		 
	    		if(!bSunatList){
	    			//log.info("1) ConsultaIntegrada ");
	    			if((sunatToken!=null) && (sunatToken.getAccess_token()!=null)) {
		    			this.sunatToken = sunatToken;
			    		String monto = String.valueOf(rdi.getTotalVenta());
	    				log.info("2) ConsultaIntegrada: "+tbComprobante.getRucEmisor()+"-"+rdi.getDocumentTypeCode()+"-"+rdi.getId()+" | "+fechaEmision+" | "+monto);
			    		
			    		List<SunatComprobantesPagoElectronicos> resultsCIS =  this.revisarCompobantePago2ConsultaIntegrada(
			    				tbComprobante, rdi.getDocumentTypeCode(), rdi.getId(), fechaEmision, monto);
			    		
			    		for(SunatComprobantesPagoElectronicos sunatValidar : resultsCIS) { 
			    			//log.info("rdi.getStatus(): "+rdi.getStatus()+"| sunatValidar.getIndEstadoCpe(): "+sunatValidar.getIndEstadoCpe());   					
			    			if((sunatValidar.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Acept)
			        			|| (sunatValidar.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Anula)){			
								if((rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Adicionar) && 
										((sunatValidar.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Acept)
										|| (sunatValidar.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Anula))) 
									bCpeConsSunat = true;
								if((rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Modificar) && 
										(sunatValidar.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Anula)) 
									bCpeConsSunat = true;
								if((rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Anulado) && 
										(sunatValidar.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Anula))
									bCpeConsSunat = true; 
								if(bCpeConsSunat) {
					 				rev = true;
				    				//log.info("2) >>>>>>>>>>> DATA: NO-OK ");
				    				Integer OseServerNoOK = Integer.parseInt(tbComprobante.getDireccion())+1;
				    				tbComprobante.setDireccion(String.valueOf(OseServerNoOK));				 				
								}
			        		}
			    		}   
	    			}
	    		}
	   		
	    		if(rdi.getBrDocumentTypeCode()!=null && rdi.getBrDocumentTypeCode().equals(Constantes.SUNAT_BOLETA)) {
	  				if(rdi.getBrID().substring(0,1).equals(Constantes.SUNAT_SERIE_B)) {
	  					if(!rev) {
							//boolean b = false;
				  			List<SunatComprobantesPagoElectronicos> resultsRB = this.revisarCompobantePagoGetSunatList(
								tbComprobante, rdi.getBrDocumentTypeCode(), rdi.getBrID());
				  			if(!(resultsRB!=null))
				  				bCpeReference = true;
				  			if((resultsRB!=null) && (resultsRB.size()==0))
				  				bCpeReference = true;
				  			//log.info("InvoiceDocumentReference: "+tbComprobante.getRucEmisor()+"-"+rdi.getBrDocumentTypeCode()+"-"+rdi.getBrID());
				  			if(bCpeReference) {
								rev = true;
								//log.info("3) >>>>>>>>>>> DATA: NO-OK ");
								Integer OseServerNoOK = Integer.parseInt(tbComprobante.getDireccion())+1;
			    				tbComprobante.setDireccion(String.valueOf(OseServerNoOK));
				  			}
	  					}
	  				}
	    		}    		
	    		
	    		if((!bCpeSunatList) && (!bCpeConsSunat) && (!bCpeReference)) {
	    			//log.info("4) >>>>>>>>>>> DATA: OK "+bCpeConsSunat+" | "+bCpeSunatList+" | "+bCpeReference);
	    			tbComprobante.setLongitudNombre(tbComprobante.getLongitudNombre()+1);
	    			return;	   
	    		}   		
	    		
	    		if(rev){
	    			if(tbComprobante.getErrorLogSunat()!=null && !(tbComprobante.getErrorLogSunat().isEmpty()))
	    				cpe = tbComprobante.getErrorLogSunat()+Constantes.OSE_SPLIT_3;
	    			cpe = cpe+"["+rdi.getDocumentTypeCode()+"-"+rdi.getId()+"]";
	    			tbComprobante.setErrorLogSunat(cpe);   
	    			log.info("5) >>>>>>>>>>> DATA: NO-OK ");
	    			return;
	    		}  
				tbComprobante.setLongitudNombre(tbComprobante.getLongitudNombre()+1);
	    	} catch (Exception e) {
	    		tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
	    		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
	    		tbComprobante.setErrorLog(e.toString());	
	    		StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				log.error("revisarRespuesta2RCItemSunatList Exception \n"+errors);
			}
	    }   

	    public void revisarRespuesta2RAItemSunatList(Documento tbComprobante, EResumenDocumentoItem rdi) {
	    	try {        	
	    		String serie = rdi.getSerieDocumentoBaja()+"-"+rdi.getNumeroDocumentoBaja();
	    		//log.info("revisarRespuesta2RAItemSunatList "+rdi.getTipoDocumento()+"-"+serie);
	    		String cpe = "";
	    		List<SunatComprobantesPagoElectronicos> results = this.revisarCompobantePagoGetSunatList(tbComprobante, 
	    				rdi.getTipoDocumento(), serie);  		
	    		for(SunatComprobantesPagoElectronicos cp : results){
	    			if(cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Acept) {
	    				tbComprobante.setLongitudNombre(tbComprobante.getLongitudNombre()+1);
	    			}    
	    			if(cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Anula) {
	    				if(tbComprobante.getErrorLogSunat()!=null && !(tbComprobante.getErrorLogSunat().isEmpty()))
	    					cpe = tbComprobante.getErrorLogSunat()+Constantes.OSE_SPLIT_3;
	    				cpe = cpe+"["+rdi.getTipoDocumento()+"-"+serie+"]";
	    				tbComprobante.setErrorLogSunat(cpe);
	    				Integer OseServerNoOK = Integer.parseInt(tbComprobante.getDireccion()+1);
	    				tbComprobante.setDireccion(String.valueOf(OseServerNoOK));
	    			}    			  
	    		}			   		
	    	} catch (Exception e) {
	    		tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
	    		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
	    		tbComprobante.setErrorLog(e.toString());
				StringWriter errors = new StringWriter();				
				e.printStackTrace(new PrintWriter(errors));
				log.error("revisarRespuesta2RAItemSunatList Exception \n"+errors);
			}
	    }  
	    
	    public void revisarRespuesta2RRItemSunatList(Documento tbComprobante, EReversionDocumentoItem rdi) {
	    	try {       
	    		String serie = rdi.getSerieDocumentoRevertido()+"-"+rdi.getCorrelativoDocRevertido();	
	    		//log.info("revisarRespuesta2RRItemSunatList "+rdi.getTipoDocumentoRevertido()+"-"+serie);
	    		String cpe = "";
	    		List<SunatComprobantesPagoElectronicos> results = this.revisarCompobantePagoGetSunatList(tbComprobante, 
	    				rdi.getTipoDocumentoRevertido(), serie);  		
	    		for(SunatComprobantesPagoElectronicos cp : results) {	
	    			if(cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Acept) {
	    				if(tbComprobante.getErrorLogSunat()!=null && !(tbComprobante.getErrorLogSunat().isEmpty()))
	    					cpe = tbComprobante.getErrorLogSunat()+Constantes.OSE_SPLIT_3;
	    				cpe = cpe+"["+rdi.getTipoDocumentoRevertido()+"-"+serie+"]";
	    				tbComprobante.setErrorLogSunat(cpe);
	    				tbComprobante.setLongitudNombre(tbComprobante.getLongitudNombre()+1);
	    			}
	    		}    		
	    	} catch (Exception e) {
	    		tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
	    		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
	    		tbComprobante.setErrorLog(e.toString());
				StringWriter errors = new StringWriter();				
				e.printStackTrace(new PrintWriter(errors));
				log.error("revisarRespuesta2RRItemSunatList Exception \n"+errors);
			}
	    }           
	    
	    public void revisarRespuesta2NCNDItemSunatList(Documento tbComprobante, String tipoDocumento, String serie) {
	    	try {       	
	    		//log.info("revisarRespuesta2NCNDItemSunatList "+tipoDocumento+"-"+serie);
	    		String cpe = "";
	    		List<SunatComprobantesPagoElectronicos> results = this.revisarCompobantePagoGetSunatList(tbComprobante, 
	    				tipoDocumento, serie);  		
	    		for(SunatComprobantesPagoElectronicos cp : results) {	
	    			if(cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Acept) {
	    				if(tbComprobante.getErrorLogSunat()!=null && !(tbComprobante.getErrorLogSunat().isEmpty()))
	    					cpe = tbComprobante.getErrorLogSunat()+Constantes.OSE_SPLIT_3;
	    				cpe = cpe+"["+tipoDocumento+"-"+serie+"]";
	    				tbComprobante.setErrorLogSunat(cpe);
	    				tbComprobante.setLongitudNombre(tbComprobante.getLongitudNombre()+1);
	    			}
	    		}    		
	    	} catch (Exception e) {
	    		tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
	    		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
	    		tbComprobante.setErrorLog(e.toString());
				StringWriter errors = new StringWriter();				
				e.printStackTrace(new PrintWriter(errors));
				log.error("revisarRespuesta2NCNDItemSunatList Exception \n"+errors);
			}
	    } 

	    private List<SunatComprobantesPagoElectronicos> revisarCompobantePagoGetSunatList(Documento tbComprobante, 
	    		String tipoDocRefPri, String nroDocRefPri){
	    	String [] docRefPri = nroDocRefPri.split("-");
	    	//log.info("revisarCompobantePagoGetSunatList: "+tbComprobante.getRucEmisor()+" - "+ tipoDocRefPri+" - "+docRefPri[0]+" - "+docRefPri[1]);
	    	String sNumRUC = String.valueOf(tbComprobante.getRucEmisor());
	    	String tipo = tipoDocRefPri;
	    	String serie = docRefPri[0];
			Long iNumCor = 0l;
			if(StringUtil.hasString(docRefPri[1]))
				iNumCor = Long.valueOf(docRefPri[1]);
			//String sNumCor = String.valueOf(iNumCor);   
			List<SunatComprobantesPagoElectronicos> results = this.buscarListGetSunatList(sNumRUC, tipo, serie, iNumCor);
			if((results!= null) && (results.size()>0))
				return results;
			return new ArrayList<SunatComprobantesPagoElectronicos>();
	    }         
	    
    private List<SunatComprobantesPagoElectronicos> revisarCompobantePago2ConsultaIntegrada(Documento tbComprobante, 
    		String tipoDocRefPri, String nroDocRefPri, String fechaEmision, String monto){
    	String [] docRefPri = nroDocRefPri.split("-");
    	//log.info("revisarCompobantePago2ConsultaIntegrada: "+tbComprobante.getRucEmisor()+"-"+ tipoDocRefPri+"-"+docRefPri[0]+"-"+docRefPri[1]+" | "+fechaEmision+" | "+monto);
    	String sNumRUC = String.valueOf(tbComprobante.getRucEmisor());
    	String tipo = tipoDocRefPri;
    	String serie = docRefPri[0];
		String sNumCor = docRefPri[1];    	 		   	
		List<SunatComprobantesPagoElectronicos> results =  this.buscarConsultaIntegradaSunat(sNumRUC, tipo, serie, sNumCor, fechaEmision, monto);
		if((results!= null) && (results.size()>0))
			return results;
		return new ArrayList<SunatComprobantesPagoElectronicos>();
    } 
    
    private List<SunatComprobantesPagoElectronicos> buscarListGetSunatList(String ruc, String tipo, String serie, Long numero ){
    	List<SunatComprobantesPagoElectronicos> results = new ArrayList <SunatComprobantesPagoElectronicos>(); 
    	try {
    		String sunatList = DirUtil.getAvatarPropertiesValue(TipoAvatarPropertiesEnum.SUNAT_LIST_CONSULTA.getCodigo());
    		if(sunatList.equals(Constantes.CONTENT_FALSE))
        		return results;
        				   		
			Optional<ComprobantePago> opt = ListSunatRest.buscaComprobantePagoElectronico(ruc, tipo, serie, numero);
			if(opt.isPresent()){    			
				ComprobantePago cp = opt.get();
				SunatComprobantesPagoElectronicos sunatComprobantesPagoElectronicos = new SunatComprobantesPagoElectronicos();
				SunatComprobantesPagoElectronicosPK sunatComprobantesPagoElectronicosPK = new SunatComprobantesPagoElectronicosPK();
				Long numRuc = Long.parseLong(cp.getRuc());
				sunatComprobantesPagoElectronicosPK.setNumRuc(numRuc);
				sunatComprobantesPagoElectronicosPK.setCodCpe(cp.getTipo());
				sunatComprobantesPagoElectronicosPK.setNumSerieCpe(cp.getSerie());
				//String numCpe = String.valueOf(cp.getSecuencial());
				//sunatComprobantesPagoElectronicosPK.setNumCpe(numCpe);   
				sunatComprobantesPagoElectronicosPK.setNumCpe(cp.getSecuencial()); 
				sunatComprobantesPagoElectronicos.setSunatComprobantesPagoElectronicosPK(sunatComprobantesPagoElectronicosPK);
				Short indEstadoCpe = Short.valueOf(cp.getEstado().toString());
				sunatComprobantesPagoElectronicos.setIndEstadoCpe(indEstadoCpe);
				sunatComprobantesPagoElectronicos.setFecEmisionCpe(cp.getFechaEmision());
				BigDecimal mtoImporteCpe = BigDecimal.valueOf(cp.getImporte());
				sunatComprobantesPagoElectronicos.setMtoImporteCpe(mtoImporteCpe);
				sunatComprobantesPagoElectronicos.setCodMonedaCpe(cp.getMoneda());
				Short codMotTraslado = Short.valueOf(cp.getMotivoTraslado().toString());
				sunatComprobantesPagoElectronicos.setCodMotTraslado(codMotTraslado);
				Short codModTraslado = Short.valueOf(cp.getModTraslado().toString());
				sunatComprobantesPagoElectronicos.setCodModTraslado(codModTraslado);
				Short indTransbordo = Short.valueOf(cp.getIndicadorTransbordo().toString());
				sunatComprobantesPagoElectronicos.setIndTransbordo(indTransbordo);				
				sunatComprobantesPagoElectronicos.setFecIniTraslado(cp.getFechaInicioTraslado());	
				
//					log.info("cp.getRuc(): "+cp.getRuc()+" - "+tsComprobantesPagoElectronicos.getTsComprobantesPagoElectronicosPK().getNumRuc());
//					log.info("cp.getTipo(): "+cp.getTipo()+" - "+tsComprobantesPagoElectronicos.getTsComprobantesPagoElectronicosPK().getCodCpe());
//					log.info("cp.getSerie(): "+cp.getSerie()+" - "+tsComprobantesPagoElectronicos.getTsComprobantesPagoElectronicosPK().getNumSerieCpe());
//					log.info("cp.getSecuencial(): "+cp.getSecuencial()+" - "+tsComprobantesPagoElectronicos.getTsComprobantesPagoElectronicosPK().getNumCpe());
//					log.info("cp.getEstado(): "+cp.getEstado()+" - "+tsComprobantesPagoElectronicos.getIndEstadoCpe());
//					log.info("cp.getFechaEmision(): "+cp.getFechaEmision()+" - "+tsComprobantesPagoElectronicos.getFecEmisionCpe());
//					log.info("cp.getImporte(): "+cp.getImporte()+" - "+tsComprobantesPagoElectronicos.getMtoImporteCpe());
//					log.info("cp.getMoneda(): "+cp.getMoneda()+" - "+tsComprobantesPagoElectronicos.getCodMonedaCpe());
//					log.info("cp.getMotivoTraslado(): "+cp.getMotivoTraslado()+" - "+tsComprobantesPagoElectronicos.getCodMotTraslado());
//					log.info("cp.getModTraslado(): "+cp.getModTraslado()+" - "+tsComprobantesPagoElectronicos.getCodModTraslado());
//					log.info("cp.getIndicadorTransbordo(): "+cp.getIndicadorTransbordo()+" - "+tsComprobantesPagoElectronicos.getIndTransbordo());
//					log.info("cp.getFechaInicioTraslado(): "+cp.getFechaInicioTraslado()+" - "+tsComprobantesPagoElectronicos.getFecIniTraslado());
				
				results.add(sunatComprobantesPagoElectronicos);
			}
			log.info("Optional<ComprobantePago> opt: "+opt.isPresent());
			//return results;
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarListGetSunatList Exception \n"+errors);
		}
	    return results;
    }
    
    private List<SunatComprobantesPagoElectronicos> buscarConsultaIntegradaSunat(String ruc, String tipo, 
    		String serie, String numero, String fechaEmision, String monto){
    	List<SunatComprobantesPagoElectronicos> results = new ArrayList <SunatComprobantesPagoElectronicos>(); 
    	try {
    		String sunatFiles = DirUtil.getAvatarPropertiesValue(TipoAvatarPropertiesEnum.SUNAT_WS_OLITWSCONSCPEGEM_CONSULTA.getCodigo());
    		if(sunatFiles.equals(Constantes.CONTENT_FALSE))
        		return results;
    		
	    	if((sunatToken!=null) && (sunatToken.getAccess_token()!=null)) 
	    		return results;
	    	if(tipo.equals(Constantes.SUNAT_FACTURA) ||
	    			tipo.equals(Constantes.SUNAT_BOLETA) ||
	    			tipo.equals(Constantes.SUNAT_NOTA_CREDITO)||
	    			tipo.equals(Constantes.SUNAT_NOTA_DEBITO)) { 
		    	SunatRequest sunatConsulta = new SunatRequest();
		    	sunatConsulta.setNumRuc(ruc);
		    	sunatConsulta.setCodComp(tipo);
		    	sunatConsulta.setNumeroSerie(serie);
		    	sunatConsulta.setNumero(numero);
		    	sunatConsulta.setFechaEmision(fechaEmision);
		    	sunatConsulta.setMonto(monto);  
		    	  	
		    	Optional<SunatBeanResponse> opt = ConsultaIntegradaSunatRest.buscaCpeValidez(sunatConsulta,sunatToken);
		    	if((opt!=null) && opt.isPresent()){  
		    		SunatBeanResponse sunatValidar = opt.get();
		    		if((sunatValidar.getData().getEstadoCp()!= null) &&
		    				(sunatValidar.getData().getEstadoCp().equals(Constantes.SUNAT_ESTADOCP_ACEPTADO) || 
		    				sunatValidar.getData().getEstadoCp().equals(Constantes.SUNAT_ESTADOCP_ANULADO))){	
		    			SunatComprobantesPagoElectronicos sunatComprobantesPagoElectronicos = new SunatComprobantesPagoElectronicos();
		    			SunatComprobantesPagoElectronicosPK sunatComprobantesPagoElectronicosPK = new SunatComprobantesPagoElectronicosPK();
						Long numRuc = Long.parseLong(ruc);
						sunatComprobantesPagoElectronicosPK.setNumRuc(numRuc);
						sunatComprobantesPagoElectronicosPK.setCodCpe(tipo);
						sunatComprobantesPagoElectronicosPK.setNumSerieCpe(serie);						
						//sunatComprobantesPagoElectronicosPK.setNumCpe(numero);  
						Long numcpe = Long.parseLong(numero);
						sunatComprobantesPagoElectronicosPK.setNumCpe(numcpe);
						sunatComprobantesPagoElectronicos.setSunatComprobantesPagoElectronicosPK(sunatComprobantesPagoElectronicosPK);
						Short indEstadoCpe = Short.valueOf(sunatValidar.getData().getEstadoCp());
						sunatComprobantesPagoElectronicos.setIndEstadoCpe(indEstadoCpe);
						
						sunatComprobantesPagoElectronicos.setFecEmisionCpe(DateUtil.parseDate(fechaEmision,"dd/mm/yyyy"));
						BigDecimal mtoImporteCpe = BigDecimal.valueOf(Double.valueOf(monto));
						sunatComprobantesPagoElectronicos.setMtoImporteCpe(mtoImporteCpe);
						sunatComprobantesPagoElectronicos.setCodMonedaCpe("");
						Short codMotTraslado = Short.valueOf("0");
						sunatComprobantesPagoElectronicos.setCodMotTraslado(codMotTraslado);
						Short codModTraslado = Short.valueOf("0");
						sunatComprobantesPagoElectronicos.setCodModTraslado(codModTraslado);
						Short indTransbordo = Short.valueOf("0");
						sunatComprobantesPagoElectronicos.setIndTransbordo(indTransbordo);				
						sunatComprobantesPagoElectronicos.setFecIniTraslado(null);		    			
		    			
//							log.info("sunatValidar.getSuccess(): "+sunatValidar.getSuccess());	
//							log.info("sunatValidar.getMessage(): "+sunatValidar.getMessage());
//							log.info("sunatValidar.getErrorCode(): "+sunatValidar.getErrorCode());
//							log.info("sunatValidar.getData().getEstadoCp(): "+sunatValidar.getData().getEstadoCp());
//							log.info("sunatValidar.getData().getEstadoRuc(): "+sunatValidar.getData().getEstadoRuc());
//							log.info("sunatValidar.getData().getCondDomiRuc(): "+sunatValidar.getData().getCondDomiRuc());
//							log.info("sunatValidar.getData().getObservaciones(): "+sunatValidar.getData().getObservaciones());

		    			log.info("Optional<SunatValidar> opt: "+opt.isPresent());
		    			results.add(sunatComprobantesPagoElectronicos);
						return results;
		    		}
	    		}
				log.info("Optional<SunatValidar> opt: "+false);
				return results;
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarConsultaIntegradaSunat Exception \n"+errors);
		}
	    return results;
    }    	 
     
}
