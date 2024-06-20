package com.axteroid.ose.server.rulesejb.dao.impl;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.apirest.list.ListSunatRest;
import com.axteroid.ose.server.apirest.sunat.ConsultaIntegradaSunatRest;
import com.axteroid.ose.server.apirest.sunat.ConsultaSunat;
import com.axteroid.ose.server.jpa.model.ComprobantesPagoElectronicos;
import com.axteroid.ose.server.jpa.model.ComprobantesPagoElectronicosPK;
import com.axteroid.ose.server.rulesejb.dao.DocumentoDAOLocal;
import com.axteroid.ose.server.rulesejb.dao.ComprobantesPagoElectronicosDAOLocal;
import com.axteroid.ose.server.rulesejb.dao.SunatParametroDAOLocal;
import com.axteroid.ose.server.rulesejb.emf.impl.DAOComImpl;
import com.axteroid.ose.server.rulesjdbc.dao.SunatComprobantesPagoElectronicosDAO;
import com.axteroid.ose.server.rulesjdbc.dao.impl.SunatComprobantesPagoElectronicosDAOImpl;
import com.axteroid.ose.server.tools.bean.ComprobantePago;
import com.axteroid.ose.server.tools.bean.ComprobantePagoList;
import com.axteroid.ose.server.tools.bean.ComprobantePagoListID;
import com.axteroid.ose.server.tools.bean.SunatRequest;
import com.axteroid.ose.server.tools.bean.SunatTokenResponse;
import com.axteroid.ose.server.tools.bean.SunatBeanResponse;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoJBossOsePropertiesEnum;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EReversionDocumentoItem;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.axteroid.ose.server.tools.util.DocumentUtil;
import com.axteroid.ose.server.tools.util.StringUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Local(ComprobantesPagoElectronicosDAOLocal.class)
public class ComprobantesPagoElectronicosDAOImpl extends DAOComImpl<ComprobantesPagoElectronicos> 
		implements ComprobantesPagoElectronicosDAOLocal {
	private static final Logger log = LoggerFactory.getLogger(ComprobantesPagoElectronicosDAOImpl.class);
	private SunatTokenResponse sunatToken = new SunatTokenResponse();
	static Map<String, String> mapa = DirUtil.getMapJBossProperties();
	
    public ComprobantesPagoElectronicosDAOImpl() {}
    
    public void getCompobantePago(Documento documento) {
    	try {
    		String serieNumero = documento.getSerie()+"-"+documento.getNumeroCorrelativo();
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePago(documento,
    				documento.getRucEmisor(), documento.getTipoDocumento(), serieNumero);	    		
    		//log.info("serieNumero: "+serieNumero+" - results.size(): "+results.size());
			//log.info("(a) documento: "+documento.getId()+" | "+documento.getNombre()+" | "+documento.getErrorComprobante()+" | "+documento.getEstado());
			if(results != null && results.size()>0){
				for(ComprobantesPagoElectronicos cp : results){   
	    			log.info("1) cp: "+cp.toString());
	    			if(cp.getIndEstadoCpe() == Constantes.SUNAT_IndEstCpe_Acept) {   
	    				//log.info("2) cp: "+cp.toString());  
	    				if(StringUtil.hasString(cp.getComprobantesPagoElectronicosPK().getNumSerieCpe())) {
	    					//log.info("3) cp: "+cp.toString());   					
	    					if((documento.getTipoDocumento().equals(Constantes.SUNAT_FACTURA)) || 
	    						(documento.getTipoDocumento().equals(Constantes.SUNAT_BOLETA)) ||
	    						(documento.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO)) ||
	    						(documento.getTipoDocumento().equals(Constantes.SUNAT_NOTA_DEBITO)))	    						    					
	    							documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));
	    					//log.info("4) cp: "+cp.toString()); 
	    					this.updateTbComprobanteErrorComprobante(documento);
	    					return;
	    				} 					
	    				// DESHABILITAR PARA GENERAR CDR CUANDO DOCUMENTO ESTA REGISTRADO EN SUNAT  
	    				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    				documento.setErrorNumero(Constantes.SUNAT_ERROR_1033); 
	    				return;   				
	    			}else if(cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Recha  || 
	        				cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Anula){	
	    				//log.info("4) cp: "+cp.toString());  
	    				if(StringUtil.hasString(cp.getComprobantesPagoElectronicosPK().getNumSerieCpe())) { 
	    					if((cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Anula) &&
	    						((documento.getTipoDocumento().equals(Constantes.SUNAT_FACTURA)) || 
	    						(documento.getTipoDocumento().equals(Constantes.SUNAT_BOLETA)) ||
	    						(documento.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO)) ||
	    						(documento.getTipoDocumento().equals(Constantes.SUNAT_NOTA_DEBITO)))){ 					
	    							documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    							documento.setErrorNumero(Constantes.SUNAT_ERROR_1032);
	    					}
	        				return;
	    				}
	    				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    				documento.setErrorNumero(Constantes.SUNAT_ERROR_1032);
	    				return;
	    			}
	    		}    	
			}
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePago Exception \n"+errors);
		}
    }
    
    public void getCompobantePagoGuia(Documento documento, String tipoDocRefPri, String nroDocRefPri) {
    	try {    
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePago(documento, 
    				documento.getRucEmisor(), tipoDocRefPri, nroDocRefPri);	
    		if(results != null && results.size()>0) { 		
    			// DESHABILITAR PARA GENERAR CDR CUANDO DOCUMENTO ESTA REGISTRADO EN SUNAT
    			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
        		documento.setErrorNumero(Constantes.SUNAT_ERROR_4000);
        		return;
    		}
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePagoGuia Exception \n"+errors);
		}
    }    
    
    public void getCompobantePago_RP(Documento documento) {
    	try {
    		//log.info("documento: {}",documento.toString());
    		String serieNumero = documento.getSerie()+"-"+documento.getNumeroCorrelativo();
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePago(documento,
    				documento.getRucEmisor(), documento.getTipoDocumento(), serieNumero);	    		
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results){       
	    			log.info("cp: {}",cp.toString());
					if(StringUtil.hasString(cp.getComprobantesPagoElectronicosPK().getNumSerieCpe())) { 
						if((cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Anula)){ 					
								documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
								documento.setErrorNumero(Constantes.SUNAT_ERROR_1032);
						}
	    				return;   			
					}
	    			// DESHABILITAR PARA GENERAR CDR CUANDO DOCUMENTO ESTA REGISTRADO EN SUNAT
					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
					documento.setErrorNumero(Constantes.SUNAT_ERROR_1033); 
					return;    			
	    		}   
    		}
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePago_RP Exception \n"+errors);
		}
    } 
    
    public void getCompobantePagoNotaDebito(Documento documento, String tipoDocRefPri, 
    		String nroDocRefPri, String codMoneda, String codigoTipoNota) {    	
    	try {   
    		//log.info("documento.getNombre(): "+documento.getNombre()+" | "+codMoneda +" | "+tipoDocRefPri+"-"+nroDocRefPri);
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePago(documento, 
    				documento.getRucEmisor(), tipoDocRefPri, nroDocRefPri);	
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results){   
	    			//log.info("documento.getNombre(): "+documento.getNombre()+" | "+codMoneda+" | "+cp.getCodMonedaCpe());
	    			if(cp.getIndEstadoCpe() == Constantes.SUNAT_IndEstCpe_Recha) {
	    				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    				documento.setErrorNumero(Constantes.SUNAT_ERROR_2208);
	    				return;
	    			}else if(cp.getIndEstadoCpe() == Constantes.SUNAT_IndEstCpe_Anula){
	    				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    				documento.setErrorNumero(Constantes.SUNAT_ERROR_2207);
	    				return;
	    			}
	    			if(tipoDocRefPri.equals(Constantes.SUNAT_BOLETA) &&
	    					codigoTipoNota.equals("03")){
	    				if(!cp.getCodMonedaCpe().toUpperCase().equals(codMoneda)){
	    					String obsv = "";
	    					if(documento.getObservaNumero()!=null && !(documento.getObservaNumero().isEmpty()))
	    						obsv = documento.getObservaNumero()+Constantes.OSE_SPLIT_3;
	    					documento.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4368);
	    				}
	    			}else {
	    				if((tipoDocRefPri.equals(Constantes.SUNAT_FACTURA)) ||
	    						(tipoDocRefPri.equals(Constantes.SUNAT_ROL_ADQUIRIENTE_SISTEMAS_PAGO)) ||
	    						(tipoDocRefPri.equals(Constantes.SUNAT_OPERADOR)) ||
	    						(tipoDocRefPri.equals(Constantes.SUNAT_PARTICIPE)) || 
	    						(tipoDocRefPri.equals(Constantes.SUNAT_ADQUIRIENTE_SISTEMAS_PAGO))){
		    				if(!cp.getCodMonedaCpe().toUpperCase().equals(codMoneda)){
		    					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
		    					documento.setErrorNumero(Constantes.SUNAT_ERROR_3209);
		    					return;
		    				}	
	    				}
	    			}
	    			return;
	    		}
    		}
    		if(ConsultaSunat.getOlitwsconscpegem(documento.getRucEmisor(), tipoDocRefPri, nroDocRefPri))
				return;
    		documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_2209);
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePagoNotaDebito Exception \n"+errors);
		}
    }

    public void getCompobantePagoNotaCredito(Documento documento, String tipoDocRefPri, 
    		String nroDocRefPri, String codMoneda, String codigoTipoNota, List<Date> listDateCuota) {
    	try {    
    		log.info("documento: {} | {}-{}",documento.toString(),tipoDocRefPri,nroDocRefPri);
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePago(documento, 
    				documento.getRucEmisor(), tipoDocRefPri, nroDocRefPri);	
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results){   
	    			//log.info("cp.getIndEstadoCpe(): "+cp.getIndEstadoCpe()+" | codigoTipoNota: "+codigoTipoNota);
	    			if(cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Recha) {    				
	    				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    				documento.setErrorNumero(Constantes.SUNAT_ERROR_2121);
	    				return;
	    			}
	    			if(cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Anula){
	    				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    				documento.setErrorNumero(Constantes.SUNAT_ERROR_2120);
	    				return;
	    			}
	    			//log.info("cp.getCodMonedaCpe: "+cp.getCodMonedaCpe()+" | codMoneda: "+codMoneda);
    				boolean errorMoneda = true;
	    			if((cp.getCodMonedaCpe()!=null) && 
	    					(cp.getCodMonedaCpe().toUpperCase().equals(codMoneda))){		    				
	    				errorMoneda = false;
	    			}		    			
	    			if(errorMoneda) {
	    				String cpeRuc = String.valueOf(cp.getComprobantesPagoElectronicosPK().getNumRuc());
	    				List<ComprobantesPagoElectronicos> resultsSunatList = this.buscarListGetSunatList(cpeRuc, 
	    						cp.getComprobantesPagoElectronicosPK().getCodCpe(),
	    						cp.getComprobantesPagoElectronicosPK().getNumSerieCpe(),
	    						cp.getComprobantesPagoElectronicosPK().getNumCpe());	
	    				if(resultsSunatList != null && resultsSunatList.size()>0){
	    					ComprobantesPagoElectronicos cpSunatList = resultsSunatList.get(0);
	    					//log.info("cpSunatList.getCodMonedaCpe: "+cpSunatList.getCodMonedaCpe()+" | codMoneda: "+codMoneda);
	    					if((cpSunatList.getCodMonedaCpe()!=null) && 
			    					(cpSunatList.getCodMonedaCpe().toUpperCase().equals(codMoneda))){	
	    						errorMoneda = false;
		    				}		    					
	    				}
	    			}	    			
	    			if(tipoDocRefPri.equals(Constantes.SUNAT_BOLETA) &&
	    					codigoTipoNota.equals("03")){
	    				if(errorMoneda){
	    					String obsv = "";
	    					if(documento.getObservaNumero()!=null && !(documento.getObservaNumero().isEmpty()))
	    						obsv = documento.getObservaNumero()+Constantes.OSE_SPLIT_3;
	    					documento.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4368);
	    				}
	    			}else {	 
	    				if((tipoDocRefPri.equals(Constantes.SUNAT_FACTURA)) ||
	    						(tipoDocRefPri.equals(Constantes.SUNAT_ROL_ADQUIRIENTE_SISTEMAS_PAGO)) ||
	    						(tipoDocRefPri.equals(Constantes.SUNAT_OPERADOR)) ||
	    						(tipoDocRefPri.equals(Constantes.SUNAT_PARTICIPE)) || 
	    						(tipoDocRefPri.equals(Constantes.SUNAT_ADQUIRIENTE_SISTEMAS_PAGO))){
			    			if(errorMoneda) {
		    					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
		    					documento.setErrorNumero(Constantes.SUNAT_ERROR_3209);
		    					return;
			    			}
	    				}
	    			}
	    			if(tipoDocRefPri.equals(Constantes.SUNAT_FACTURA)){
	    				if(listDateCuota.size()>0) {
	    					for(Date d : listDateCuota) {
	    						long ldias = DateUtil.deltaDays(cp.getFecEmisionCpe(),d);	
	    						log.info("cp: {} | cuota: {} | ldias: {}",cp.getFecEmisionCpe(),d,ldias);
	    						if(ldias>=0) {
	    				    		documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    				    		documento.setErrorNumero(Constantes.SUNAT_ERROR_3321);
	    				    		return;
	    						}	    							
	    					}
	    				}
	    			}
	    			return;	    			
	    		}	
    		}
    		if(ConsultaSunat.getOlitwsconscpegem(documento.getRucEmisor(), tipoDocRefPri, nroDocRefPri))
				return;
    		documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_2119);
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePagoNotaCredito Exception \n"+errors);
		}
    }
    
    public void getCompobantePagoNCND(Documento documento, String tipoDocRefPri, 
    		String nroDocRefPri, Date fechaEmision) {    	
    	try {   
    		//log.info("documento.getNombre(): "+documento.getNombre());
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePago(documento, 
    				documento.getRucEmisor(), tipoDocRefPri, nroDocRefPri);	
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results){   		
					long ldias = DateUtil.deltaDays(cp.getFecEmisionCpe(),fechaEmision);	
					//log.info("FecEmisionCpe: "+cp.getFecEmisionCpe()+" | FecEmision: "+fechaEmision +" | dias: "+ldias);
					if(ldias>0) {
						documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
						documento.setErrorNumero(Constantes.SUNAT_ERROR_2885);
						return;
					}
	    		}
    		}
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePagoNCND Exception \n"+errors);
		}
    }
    
    public void getCompobantePagoNCDocRefPri(Documento documento, String tipoDocRefPri, 
    		String nroDocRefPri) {    	
    	try {   
    		//log.info("documento.getNombre(): "+documento.getNombre());
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePago(documento, 
    				documento.getRucEmisor(), tipoDocRefPri, nroDocRefPri);	
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results){   		
					log.info("cp: "+cp.toString());
					if(cp.getIndForPpag() == 0) {
						documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
						documento.setErrorNumero(Constantes.SUNAT_ERROR_3260);
						return;
					}
	    		}
    		}
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePagoNCND Exception \n"+errors);
		}
    }
    
    public void getCompobantePagoRP(Documento documento, String tipoDocRefPri, String nroDocRefPri) {
    	try {    
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePago(documento, 
    				documento.getRucEmisor(), tipoDocRefPri, nroDocRefPri);	
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results){   		
	    			if(cp.getIndEstadoCpe() == Constantes.SUNAT_IndEstCpe_Recha) {
	    				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    				documento.setErrorNumero(Constantes.SUNAT_ERROR_2121);
	    				return;
	    			}else if(cp.getIndEstadoCpe() == Constantes.SUNAT_IndEstCpe_Anula){
	    				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    				documento.setErrorNumero(Constantes.SUNAT_ERROR_2120);
	    				return;
	    			}
	    			return;
	    		}    		
    		}
    		if(ConsultaSunat.getOlitwsconscpegem(documento.getRucEmisor(), tipoDocRefPri, nroDocRefPri))
    			return;
    		documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_2119);
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePagoRP Exception \n"+errors);
		}
    }
    
    public void getCompobantePagoPercepcionItem(Documento documento, 
    		EPercepcionDocumentoItem rdi, String indicadorEmisiónExcepcional) {
    	try {        		
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePago(documento, 
    				documento.getRucEmisor(), rdi.getTipoDocumentoRelacionado(), rdi.getNumeroDocumentoRelacionado());	
    		String [] docRefPri = rdi.getNumeroDocumentoRelacionado().split("-"); 
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results){ 	
	    			log.info("cp: {}",cp.toString());
	    			if(docRefPri[0].equals(Constantes.SUNAT_SERIE_E001) ||
	    					docRefPri[0].substring(0, 1).equals(Constantes.SUNAT_SERIE_F) ||
	    					docRefPri[0].substring(0, 1).equals(Constantes.SUNAT_SERIE_B)) {
	    				if(!DateUtil.compareDate(rdi.getFechaEmisionDocumentoRelacionado(), cp.getFecEmisionCpe())){
	   						documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	   						documento.setErrorNumero(Constantes.SUNAT_ERROR_2610);
	   						return;
	   					}			
	    			}
	    			String serie = cp.getComprobantesPagoElectronicosPK().getNumSerieCpe().substring(0,1);
	    			//log.info("cp.getTsComprobantesPagoElectronicosPK().getNumSerieCpe(): {} | indicadorEmisiónExcepcional",cp.getTsComprobantesPagoElectronicosPK().getNumSerieCpe(),indicadorEmisiónExcepcional);
	    			if((indicadorEmisiónExcepcional!=null) && 
	    					(indicadorEmisiónExcepcional.equals(Constantes.SUNAT_FACTURA))) {
	    				if(cp.getComprobantesPagoElectronicosPK().getCodCpe().equals(Constantes.SUNAT_FACTURA)){
	    					if(!StringUtil.hasString(serie)){
		    					if(cp.getIndForPpag()==1) {
			   						documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			   						documento.setErrorNumero(Constantes.SUNAT_ERROR_3325);
			   						return;		    						
		    					}		    					
		    				}	    					
	    				}
	    			}
	    			if((indicadorEmisiónExcepcional==null) || 
	    					(indicadorEmisiónExcepcional.isEmpty())) {
	    				//log.info("serie: {} | getCodCpe {}",serie,cp.getTsComprobantesPagoElectronicosPK().getCodCpe());
	    				if(cp.getComprobantesPagoElectronicosPK().getCodCpe().equals(Constantes.SUNAT_FACTURA)){
	    					if(!StringUtil.hasString(serie)){
		    					if((cp.getIndPercepcion()!=null) && (cp.getIndPercepcion()==1)) {
			   						documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			   						documento.setErrorNumero(Constantes.SUNAT_ERROR_3312);
			   						return;		    						
		    					}
		    					if((cp.getIndPercepcion()!=null) && (cp.getIndForPpag()==0)) {
			   						documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			   						documento.setErrorNumero(Constantes.SUNAT_ERROR_3329);
			   						return;		    						
		    					}		
		    				}	    					
	    				}
	    			}
	    			if(cp.getComprobantesPagoElectronicosPK().getCodCpe().equals(Constantes.SUNAT_BOLETA)) {
	    				if(!StringUtil.hasString(serie)){
	    					if((cp.getIndPercepcion()!=null) && (cp.getIndPercepcion()==1)) {
		   						documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
		   						documento.setErrorNumero(Constantes.SUNAT_ERROR_3328);
		   						return;		    						
	    					}		    					
	    				}
	    			}
	    			return;
	    		}
    		}
    		if(ConsultaSunat.getOlitwsconscpegem(documento.getRucEmisor(), rdi.getTipoDocumentoRelacionado(), rdi.getNumeroDocumentoRelacionado()))
    			return;
    		if((rdi.getTipoDocumentoRelacionado().equals(Constantes.SUNAT_FACTURA)) ||
    				(rdi.getTipoDocumentoRelacionado().equals(Constantes.SUNAT_BOLETA)) ||
    				(rdi.getTipoDocumentoRelacionado().equals(Constantes.SUNAT_NOTA_CREDITO))||
    				(rdi.getTipoDocumentoRelacionado().equals(Constantes.SUNAT_NOTA_DEBITO))) {
    			Boolean bDoc = false;
    			if((docRefPri[0].equals(Constantes.SUNAT_SERIE_E001)) ||
    				(docRefPri[0].equals(Constantes.SUNAT_SERIE_EB01)))
    				bDoc = true;
    			if(docRefPri[0].substring(0, 1).equals(Constantes.SUNAT_SERIE_F))
    				bDoc = true;
    			if(bDoc) {
    				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
    				documento.setErrorNumero(Constantes.SUNAT_ERROR_2609);
    			}
    		}
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePagoPercepcionItemSerie Exception \n"+errors);
		}
    }
       
    public void getCompobantePagoComunicaBajasItem(Documento documento, EResumenDocumentoItem rdi,
    		Date fechaEmisionComprobante, Date fechaRecep) {
    	try {        		
    		String serie = rdi.getSerieDocumentoBaja()+"-"+rdi.getNumeroDocumentoBaja();
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePago(documento, 
    				documento.getRucEmisor(), rdi.getTipoDocumento(), serie);	
    		//log.info("documento: "+documento.getRucEmisor()+"-"+rdi.getTipoDocumento()+"-"+serie);
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results){
	    			//log.info("cp.getIndEstadoCpe(): "+cp.getIndEstadoCpe()+"| fechaEmisionComprobante: "+fechaEmisionComprobante+" | cp.getFecEmisionCpe(); "+cp.getFecEmisionCpe());
	    			if(cp.getIndEstadoCpe()==0){
	   					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	   					documento.setErrorNumero(Constantes.SUNAT_ERROR_2398);
	   		     		String cpe = "";
	   		   			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
	   		   				cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
	   		   			cpe = cpe+"["+rdi.getTipoDocumento()+"-"+serie+"]"; 
	   		   			documento.setErrorLog(cpe);
	   		   			return;
	   				}    				
	    			if(cp.getIndEstadoCpe()==2){
	   					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	   					documento.setErrorNumero(Constantes.SUNAT_ERROR_2323);
	   		     		String cpe = "";
	   		   			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
	   		   				cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
	   		   			cpe = cpe+"["+rdi.getTipoDocumento()+"-"+serie+"]"; 
	   		   			documento.setErrorLog(cpe);
	   					return;
	   				}
	    			if((serie.substring(0, 1).equals(Constantes.SUNAT_SERIE_F) || StringUtil.hasString(serie.substring(0, 1)) && 
	    					(rdi.getTipoDocumento().equals(Constantes.SUNAT_FACTURA) || 
	    					rdi.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO) || 
	    					rdi.getTipoDocumento().equals(Constantes.SUNAT_NOTA_DEBITO)))) {
	    				
	    				if(!DateUtil.compareDate(fechaEmisionComprobante, cp.getFecEmisionCpe())){
	    					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    					documento.setErrorNumero(Constantes.SUNAT_ERROR_2375);
	       		     		String cpe = "";
	       		   			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
	       		   				cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
	       		   			cpe = cpe+"["+rdi.getTipoDocumento()+"-"+serie+"]"; 
	       		   			documento.setErrorLog(cpe);
	    					return;
	    				}    			
	    			}
	    			if(serie.substring(0, 1).equals(Constantes.SUNAT_SERIE_S) && 
	    					(rdi.getTipoDocumento().equals(Constantes.SUNAT_RECIBO_SERV_PUBL) || 
	    					rdi.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO) || 
	    					rdi.getTipoDocumento().equals(Constantes.SUNAT_NOTA_DEBITO))) {
	    				
	    				if(!DateUtil.compareDate(fechaEmisionComprobante, cp.getFecEmisionCpe())){
	    					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	    					documento.setErrorNumero(Constantes.SUNAT_ERROR_2375);
	       		     		String cpe = "";
	       		   			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
	       		   				cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
	       		   			cpe = cpe+"["+rdi.getTipoDocumento()+"-"+serie+"]"; 
	       		   		    documento.setErrorLog(cpe);
	    					return;
	    				}    			
	    			} 		
	        		if(!StringUtil.hasString(serie.substring(0, 1))) {
        				SunatParametroDAOLocal tsParametroDAOLocal = 
        						(SunatParametroDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatParametroDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatParametroDAOLocal");						
	        			if(tsParametroDAOLocal.getParametroErrorFechas(Constantes.SUNAT_DIAS_7,
	        					fechaRecep, cp.getFecEmisionCpe())) {	
        					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
        					documento.setErrorNumero(Constantes.SUNAT_ERROR_2957);			
        					String cpe = "";
        	    			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
        	    					cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
        	    			cpe = cpe+"["+rdi.getDocumentTypeCode()+"-"+rdi.getId()+"]";
        	    			documento.setErrorLog(cpe);
        	    			documento.setLongitudNombre(0);
        					return;
	    	    		}
	        		}	        		  	   	    			
	    			return;
	    		}
    		}
    		if((serie.substring(0, 1).equals(Constantes.SUNAT_SERIE_F) || StringUtil.hasString(serie.substring(0, 1)) && 
					(rdi.getTipoDocumento().equals(Constantes.SUNAT_FACTURA) || 
					rdi.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO) || 
					rdi.getTipoDocumento().equals(Constantes.SUNAT_NOTA_DEBITO)))) {
    			if((ConsultaSunat.getOlitwsconscpegem(documento.getRucEmisor(), rdi.getTipoDocumento(), serie)))
					return;    			
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_2105);
				String cpe = "";
	   			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
	   				cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
	   			cpe = cpe+"["+rdi.getTipoDocumento()+"-"+serie+"]"; 	
	   			documento.setErrorLog(cpe);
    			return;
    		}
    		if((serie.substring(0, 1).equals(Constantes.SUNAT_SERIE_F) && 
					(rdi.getTipoDocumento().equals(Constantes.SUNAT_ROL_ADQUIRIENTE_SISTEMAS_PAGO) || 
					rdi.getTipoDocumento().equals(Constantes.SUNAT_OPERADOR) || 
					rdi.getTipoDocumento().equals(Constantes.SUNAT_ADQUIRIENTE_SISTEMAS_PAGO)))) {
    			if((ConsultaSunat.getOlitwsconscpegem(documento.getRucEmisor(), rdi.getTipoDocumento(), serie)))
					return;
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_2105);
				String cpe = "";
	   			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
	   				cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
	   			cpe = cpe+"["+rdi.getTipoDocumento()+"-"+serie+"]"; 	
	   			documento.setErrorLog(cpe);
    			return;
    		}    		
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePagoComunicaBajasItem Exception \n"+errors);
		}
    }    
    
    public void getCompobantePagoReversionItem(Documento documento, EReversionDocumentoItem rdi) {
    	try {        		
    		String serie = rdi.getSerieDocumentoRevertido()+"-"+rdi.getCorrelativoDocRevertido();
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePago(documento, 
    				documento.getRucEmisor(), rdi.getTipoDocumentoRevertido(), serie);	
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results){
	    			if(cp.getIndEstadoCpe()==2){
	   					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	   					documento.setErrorNumero(Constantes.SUNAT_ERROR_2751);
	   		     		String cpe = "";
	   		   			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
	   		   				cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
	   		   			cpe = cpe+"["+rdi.getTipoDocumentoRevertido()+"-"+serie+"]"; 
	   		   			documento.setErrorLog(cpe);
	   		   			return;
	   				}			
	    			return;
	    		}
    		}
    		if(ConsultaSunat.getOlitwsconscpegem(documento.getRucEmisor(), rdi.getTipoDocumentoRevertido(), serie))
				return;
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_2750);
     		String cpe = "";
   			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
   				cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
   			cpe = cpe+"["+rdi.getTipoDocumentoRevertido()+"-"+serie+"]"; 
   			documento.setErrorLog(cpe);
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePagoReversionItem Exception \n"+errors);
		}
    }    
    
	public void grabaTbComprobantesPagoElectronicos(Documento documento, Date fechaEmisionComprobante, 
			BigDecimal mtoImporteCpe, Short indEstCPE, String moneda, Short Ind_percepcion, Short ind_for_pag){				
		try {
			//log.info("grabaTbComprobantesPagoElectronicos "+documento.getRucEmisor()+" - "+ documento.getTipoComprobante()+" - "+
	    	//		documento.getSerie()+" - "+documento.getNumeroCorrelativo());
			List<ComprobantesPagoElectronicos> results = this.buscarCompobantePagoDB(documento, 
					documento.getRucEmisor(), documento.getTipoDocumento(),
			    			documento.getSerie()+"-"+documento.getNumeroCorrelativo());						
			if(results != null && results.size()>0) 
				return;
			ComprobantesPagoElectronicos cpe = new ComprobantesPagoElectronicos();
			ComprobantesPagoElectronicosPK cpePK = new ComprobantesPagoElectronicosPK();
			cpePK.setNumRuc(documento.getRucEmisor());
			cpePK.setCodCpe(documento.getTipoDocumento());
			cpePK.setNumSerieCpe(documento.getSerie());				
			cpePK.setNumCpe(Long.valueOf(documento.getNumeroCorrelativo()));
			cpe.setComprobantesPagoElectronicosPK(cpePK);
			cpe.setFecEmisionCpe(fechaEmisionComprobante);
			cpe.setMtoImporteCpe(mtoImporteCpe);
			cpe.setIndEstadoCpe(indEstCPE);
			cpe.setCodMonedaCpe(moneda);
			cpe.setUserCrea(documento.getUserCrea());
			cpe.setFechaCrea(documento.getFechaCrea());
			cpe.setId(documento.getId());
			//cpe.setNumeroCpe(documento.getNumCpe());
			cpe.setIndPercepcion(Ind_percepcion);
			cpe.setIndForPpag(ind_for_pag);
			this.add(cpe);
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabaTbComprobantesPagoElectronicos Exception \n"+errors);
		}
	}
	
	public void modificarEstadoComprobantesPagoElectronicos_RA(Documento documento, EResumenDocumentoItem rdi,
			Short indEstCPE){				
		try {
			//log.info("modificarEstadoComprobantesPagoElectronicos_RA "+documento.getRucEmisor());
			String serie = rdi.getSerieDocumentoBaja()+"-"+rdi.getNumeroDocumentoBaja();
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePagoDB(documento, 
    				documento.getRucEmisor(), rdi.getTipoDocumento(), serie);		
    		//log.info("0) results.size(): "+results.size()!= null ? results.size() : "NULL");
    		if(results != null && results.size()>0){
    			for(ComprobantesPagoElectronicos cpe : results){
    				cpe.setIndEstadoCpe(indEstCPE);
    				cpe.setUserModi(documento.getUserCrea());
    				cpe.setFechaModi(documento.getFechaCrea());
    				this.update(cpe);
    			}			
    		}else{
    			String [] docRefPri = serie.split("-");
    	    	String sNumRUC = String.valueOf(documento.getRucEmisor());
    			Long iNumCor = 0l;
    			if(StringUtil.hasString(docRefPri[1]))
    				iNumCor = Long.valueOf(docRefPri[1]);
    			//String sNumCor = String.valueOf(iNumCor); 
    			results = this.buscarListGetSunatList(sNumRUC, rdi.getTipoDocumento(), docRefPri[0], iNumCor);
    			//log.info("2) results.size(): "+results.size()!= null ? results.size() : "NULL");
    			if(results != null && results.size()>0){	    			
	    			for(ComprobantesPagoElectronicos cpe : results){
	    				cpe.setIndEstadoCpe(indEstCPE);
	    				cpe.setUserModi(documento.getUserCrea());
	    				cpe.setFechaModi(documento.getFechaCrea());
	    				this.add(cpe);
	    			}
    			}
    		}    		
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("modificarEstadoComprobantesPagoElectronicos_RA Exception \n"+errors);
		}
	}	
	
	public void modificarEstadoComprobantesPagoElectronicos_RR(Documento documento, EReversionDocumentoItem rdi,
			Short indEstCPE){				
		try {
			//log.info("modificarEstadoComprobantesPagoElectronicos_RR "+documento.getRucEmisor());
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePagoDB(documento, 
    				documento.getRucEmisor(), rdi.getTipoDocumentoRevertido(), rdi.getSerieNumeroDocRevertido());						
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cpe : results){
					cpe.setIndEstadoCpe(indEstCPE);
					cpe.setUserModi(documento.getUserCrea());
					cpe.setFechaModi(documento.getFechaCrea());
					this.update(cpe);
				}		
    		}
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("modificarEstadoComprobantesPagoElectronicos_RR Exception \n"+errors);
		}
	}	
    
    public void getCompobantePagoResumenDiarioItem_BR(Documento documento, EResumenDocumentoItem rdi) {
    	try {        		
    		log.info("getCompobantePagoResumenDiarioItem_BR "+rdi.getBrDocumentTypeCode()+" - "+rdi.getBrID());
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePago(documento, 
    				documento.getRucEmisor(), rdi.getBrDocumentTypeCode(), rdi.getBrID());	
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results){
	    			if(cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Recha || cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Anula){
	   					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	   					documento.setErrorNumero(Constantes.SUNAT_ERROR_2990);
	   		     		String cpe = "";
	   		   			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
	   		   					cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
	   		   			cpe = cpe+"["+rdi.getBrDocumentTypeCode()+"-"+rdi.getBrID()+"]";
	   		   			documento.setErrorLog(cpe);
	   		   			documento.setLongitudNombre(0);
	   				} 	
	    			return;
	    		} 
    		}
    		if((rdi.getBrDocumentTypeCode().equals(Constantes.SUNAT_BOLETA)) && 
    				(rdi.getBrID().substring(0, 1).equals(Constantes.SUNAT_SERIE_B))){
	    		if(ConsultaSunat.getOlitwsconscpegem(documento.getRucEmisor(), rdi.getBrDocumentTypeCode(), rdi.getBrID()))
	    			return; 
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_2989);
				String cpe = "";
	   			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
	   				cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
	   			cpe = cpe+"["+rdi.getBrDocumentTypeCode()+"-"+rdi.getBrID()+"]";
	   			documento.setErrorLog(cpe);
	    		documento.setLongitudNombre(0);
    		}
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePagoResumenDiarioItem_BR Exception \n"+errors);
		}
    }       
    
	public void grabaTbComprobantesPagoElectronicos_RC(Documento documento, 
			Date fechaEmisionComprobante, EResumenDocumentoItem rdi){	
		//log.info("grabaTbComprobantesPagoElectronicos_RC "+documento.toString()+" | "+ rdi.getDocumentTypeCode()+" - "+rdi.getId());		
		try {
			ComprobantesPagoElectronicos cpe = new ComprobantesPagoElectronicos();
			ComprobantesPagoElectronicosPK cpePK = new ComprobantesPagoElectronicosPK();
			cpePK.setNumRuc(documento.getRucEmisor());
			cpePK.setCodCpe(rdi.getDocumentTypeCode());
			String [] docRefPri = rdi.getId().split("-");
			cpePK.setNumSerieCpe(docRefPri[0]);				
			cpePK.setNumCpe(Long.valueOf(docRefPri[1]));
			cpe.setComprobantesPagoElectronicosPK(cpePK);
			List<ComprobantesPagoElectronicos> results = this.buscarCompobantePagoDB(documento, 
					documento.getRucEmisor(), rdi.getDocumentTypeCode(), rdi.getId());	
			if(results != null && results.size()>0){
				for(ComprobantesPagoElectronicos cp : results){
					//log.info("Status 1) "+rdi.getStatus()+" | ID: "+cpe.getId()+" - "+documento.getId());
					if(rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Adicionar)
						return;
					if(rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Anulado)
						cp.setIndEstadoCpe(Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Anula)));
					if(rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Anulado_Dia)
						cp.setIndEstadoCpe(Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Anula)));
					cp.setMtoImporteCpe(rdi.getTotalVenta());
					cp.setUserModi(documento.getUserCrea());
					cp.setFechaModi(documento.getFechaCrea());				
					cp.setId(documento.getId());					
					this.update(cp);
					return;
				}
			}
			//log.info("Status 2) "+rdi.getStatus()+" | ID: "+cpe.getId()+" - "+documento.getId());
			cpe.setIndEstadoCpe(Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Acept)));
			if(!(rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Adicionar)) 
				cpe.setIndEstadoCpe(Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Anula)));
			cpe.setMtoImporteCpe(rdi.getTotalVenta());
			cpe.setFecEmisionCpe(fechaEmisionComprobante);				
			cpe.setUserCrea(documento.getUserCrea());
			cpe.setFechaCrea(documento.getFechaCrea());
			cpe.setId(documento.getId());
			//cpe.setNumeroCpe(Long.parseLong(cpePK.getNumCpe()));
			this.add(cpe);
			
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabaTbComprobantesPagoElectronicos_RC Exception \n"+errors);
		}
	}
	
    public void getCompobantePagoAnticipoBFE(Documento documento, Long numDocumento, 
    		String tipoDocRefPri, String nroDocRefPri) {
    	//log.info("getCompobantePagoAnticipoBFE "+numDocumento+" - "+tipoDocRefPri+" - "+nroDocRefPri);
    	try {        		    		
    		if(tipoDocRefPri.equals("02"))
    			tipoDocRefPri = Constantes.SUNAT_FACTURA;
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePago(documento, 
    				numDocumento, tipoDocRefPri, nroDocRefPri);	
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results){
	    			log.error(cp.getComprobantesPagoElectronicosPK().getNumRuc()+"  cp.getIndEstadoCpe()  "+cp.getIndEstadoCpe());
	    			if(cp.getIndEstadoCpe()!=Constantes.SUNAT_IndEstCpe_Acept){
	   					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	   					documento.setErrorNumero(Constantes.SUNAT_ERROR_3218);
	   					return;
	   				} 	
	    			return;
	    		}   
    		}
    		if(ConsultaSunat.getOlitwsconscpegem(numDocumento, tipoDocRefPri, nroDocRefPri))
    			return; 
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_3218);
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePagoAnticipoBFE Exception \n"+errors);
		}
    }    
    
    public List<ComprobantesPagoElectronicos> getCompobantePagoItem(Documento documento, 
    		String tipoDocRefPri, String nroDocRefPri) {
    	List<ComprobantesPagoElectronicos> results = new ArrayList<ComprobantesPagoElectronicos>();
    	try {  
    		if(results != null && results.size()>0)
    			results = this.buscarCompobantePagoDB(documento, documento.getRucEmisor(), tipoDocRefPri, nroDocRefPri);	   		
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePagoItem Exception \n"+errors);
		}
		return results;
    }   
    
    public void revisarRespuesta2RCItemSunatList(Documento documento, EResumenDocumentoItem rdi, 
    		String fechaEmision, SunatTokenResponse sunatToken) {
    	try {  
    		String cpeReview = documento.getRucEmisor()+"-"+rdi.getDocumentTypeCode()+"-"+rdi.getId()+" | Status: "+rdi.getStatus();    		   		
    		//log.info("revisarRespuesta2RCItemSunatList: {}",cpe);    		 
    		List<ComprobantesPagoElectronicos> results = this.revisarCompobantePagoGetSunatList(
    				documento, rdi.getDocumentTypeCode(), rdi.getId());
    		String cpe = "";
    		boolean rev = false;
    		boolean bSunatList = false;
    		boolean bCpeSunatList = false;
    		boolean bCpeConsSunat = false;
    		boolean bCpeReference = false;
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results) {       			
	    			bSunatList = true;
	    			//log.info("rdi.getStatus(): "+rdi.getStatus()+"| cp.getIndEstadoCpe(): "+cp.getIndEstadoCpe());
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
	    				log.info("1) {} | DATA: NO-OK ",cpeReview);
	    				Integer oseServerNoOK = Integer.parseInt(documento.getDireccion())+1;
	    				documento.setDireccion(String.valueOf(oseServerNoOK));
	    			} 	    			
	    		}		
    		} 
    		log.info("1) {} | rev: {} | SunatList: {} | bCpeSunatList: {} | bCpeSunatList: {} | bCpeReference:{} ",cpeReview,rev,bSunatList,bCpeSunatList,bCpeSunatList,bCpeReference);
    		if(!bSunatList){
    			if((sunatToken!=null) && (sunatToken.getAccess_token()!=null)) {
	    			this.sunatToken = sunatToken;
		    		String monto = String.valueOf(rdi.getTotalVenta());
 		    		List<ComprobantesPagoElectronicos> resultsCIS =  this.revisarCompobantePago2ConsultaIntegrada(
		    				documento, rdi.getDocumentTypeCode(), rdi.getId(), fechaEmision, monto);
		    		if(results != null && results.size()>0){
			    		for(ComprobantesPagoElectronicos sunatValidar : resultsCIS) { 
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
				    				log.info("2) {} | DATA: NO-OK ",cpeReview);
				    				Integer oseServerNoOK = Integer.parseInt(documento.getDireccion())+1;
				    				documento.setDireccion(String.valueOf(oseServerNoOK));
								}
			        		}
			    		}  
		    		}
    			}
    		}
    		log.info("2) {} | rev: {} | bSunatList: {} | bCpeSunatList: {} | bCpeSunatList: {} | bCpeReference:{} ",cpeReview,rev,bSunatList,bCpeSunatList,bCpeSunatList,bCpeReference);
    		if(rdi.getBrDocumentTypeCode()!=null && rdi.getBrDocumentTypeCode().equals(Constantes.SUNAT_BOLETA)) {
  				if(rdi.getBrID().substring(0,1).equals(Constantes.SUNAT_SERIE_B)) {
  					if(!rev) {
			  			List<ComprobantesPagoElectronicos> resultsRB = this.revisarCompobantePagoGetSunatList(
							documento, rdi.getBrDocumentTypeCode(), rdi.getBrID());
			  			if(!(resultsRB!=null))
			  				bCpeReference = true;
			  			if((resultsRB!=null) && (resultsRB.size()==0))
			  				bCpeReference = true;
			  			if(bCpeReference) {
							rev = true;
							log.info("3) {} | DATA: NO-OK ",cpeReview);
							Integer oseServerNoOK = Integer.parseInt(documento.getDireccion())+1;
		    				documento.setDireccion(String.valueOf(oseServerNoOK));
			  			}
  					}
  				}
    		}    		
    		log.info("3) {} | rev: {} | bSunatList: {} | bCpeSunatList: {} | bCpeSunatList: {} | bCpeReference:{} ",cpeReview,rev,bSunatList,bCpeSunatList,bCpeSunatList,bCpeReference);
    		if((!bCpeSunatList) && (!bCpeConsSunat) && (!bCpeReference)) {
    			log.info("4) {} | DATA: OK {} | {} | {} ",cpeReview, bCpeConsSunat,bCpeSunatList,bCpeReference);
    			documento.setLongitudNombre(documento.getLongitudNombre()+1);
    			documento.setCustomizaVersion(String.valueOf(rdi.getStatus()));
    			return;	   
    		}   		
    		
    		if(rev){
    			if(documento.getErrorLogSunat()!=null && !(documento.getErrorLogSunat().isEmpty()))
    				cpe = documento.getErrorLogSunat()+Constantes.OSE_SPLIT_3;
    			cpe = cpe+"["+rdi.getDocumentTypeCode()+"-"+rdi.getId()+"]";
    			documento.setErrorLogSunat(cpe);   
    			log.info("5) {} | DATA: NO-OK ",cpeReview);
    			return;
    		}  
			documento.setLongitudNombre(documento.getLongitudNombre()+1);
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());	
    		StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			log.error("revisarRespuesta2RCItemSunatList Exception \n"+errors);
		}
    }   

    public void revisarRespuesta2RAItemSunatList(Documento documento, EResumenDocumentoItem rdi) {
    	try {        	
    		String serie = rdi.getSerieDocumentoBaja()+"-"+rdi.getNumeroDocumentoBaja();
    		log.info("documento: {}",documento.toString());
    		//log.info("revisarRespuesta2RAItemSunatList "+rdi.getTipoDocumento()+"-"+serie);
    		String cpe = "";
    		List<ComprobantesPagoElectronicos> results = this.revisarCompobantePagoGetSunatList(documento, 
    				rdi.getTipoDocumento(), serie);  	
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results){
	    			log.info("cp: {}",cp.toString());
	    			if(cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Acept) 
	    				documento.setLongitudNombre(documento.getLongitudNombre()+1);	    			
	    			if((cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Anula) || 
	    				(cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Recha)	){    				
	    				if(documento.getErrorLogSunat()!=null && !(documento.getErrorLogSunat().isEmpty()))
	    					cpe = documento.getErrorLogSunat()+Constantes.OSE_SPLIT_3;
	    				cpe = cpe+"["+rdi.getTipoDocumento()+"-"+serie+"]";
	    				documento.setErrorLogSunat(cpe);
	    				Integer oseServerNoOK = Integer.parseInt(documento.getDireccion())+1;
	    				documento.setDireccion(String.valueOf(oseServerNoOK));	 
	    				documento.setCustomizaVersion(String.valueOf(cp.getIndEstadoCpe()));
	    			} 			
	    		}	
    		}
    		if(documento.getRespuestaSunat().equals(Constantes.SUNAT_ERROR_2105)) {
    			cpe = documento.getErrorLogSunat()+Constantes.OSE_SPLIT_3;
				cpe = cpe+"["+rdi.getTipoDocumento()+"-"+serie+"]";
    		}
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("revisarRespuesta2RAItemSunatList Exception \n"+errors);
		}
    }  
    
    public void revisarRespuesta2RRItemSunatList(Documento documento, EReversionDocumentoItem rdi) {
    	try {       
    		String serie = rdi.getSerieDocumentoRevertido()+"-"+rdi.getCorrelativoDocRevertido();	
    		//log.info("revisarRespuesta2RRItemSunatList "+rdi.getTipoDocumentoRevertido()+"-"+serie);
    		String cpe = "";
    		List<ComprobantesPagoElectronicos> results = this.revisarCompobantePagoGetSunatList(documento, 
    				rdi.getTipoDocumentoRevertido(), serie);  	
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results) {	
	    			if(cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Acept) {
	    				if(documento.getErrorLogSunat()!=null && !(documento.getErrorLogSunat().isEmpty()))
	    					cpe = documento.getErrorLogSunat()+Constantes.OSE_SPLIT_3;
	    				cpe = cpe+"["+rdi.getTipoDocumentoRevertido()+"-"+serie+"]";
	    				documento.setErrorLogSunat(cpe);
	    				documento.setLongitudNombre(documento.getLongitudNombre()+1);
	    			}
	    		}    	
    		}
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("revisarRespuesta2RRItemSunatList Exception \n"+errors);
		}
    }           
    
    public void revisarRespuesta2NCNDItemSunatList(Documento documento, String tipoDocumento, String serie) {
    	try {       	
    		//log.info("revisarRespuesta2NCNDItemSunatList "+tipoDocumento+"-"+serie);
    		String cpe = "";
    		List<ComprobantesPagoElectronicos> results = this.revisarCompobantePagoGetSunatList(documento, 
    				tipoDocumento, serie);  	
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results) {	
	    			if(cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Acept) {
	    				if(documento.getErrorLogSunat()!=null && !(documento.getErrorLogSunat().isEmpty()))
	    					cpe = documento.getErrorLogSunat()+Constantes.OSE_SPLIT_3;
	    				cpe = cpe+"["+tipoDocumento+"-"+serie+"]";
	    				documento.setErrorLogSunat(cpe);
	    				documento.setLongitudNombre(documento.getLongitudNombre()+1);
	    			}
	    		}    	
    		}
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("revisarRespuesta2NCNDItemSunatList Exception \n"+errors);
		}
    }            
    
    private List<ComprobantesPagoElectronicos> buscarCompobantePago(Documento documento, Long numRuc, 
    		String tipoDocRefPri, String nroDocRefPri){
    	log.info("documento {} || {}-{}-{}",documento.toString(),numRuc,tipoDocRefPri,nroDocRefPri);
    	String [] docRefPri = nroDocRefPri.split("-");
    	String sNumRUC = String.valueOf(numRuc);
    	String tipo = tipoDocRefPri;
    	String serie = docRefPri[0];
		int iNumCor = 0;
		if(StringUtil.hasString(docRefPri[1]))
			iNumCor = Integer.parseInt(docRefPri[1]);
		String sNumCor = String.valueOf(iNumCor);    	 		   	
    	List<ComprobantesPagoElectronicos> resulDB = this.buscarCompobantePagoDB(documento, 
    			documento.getRucEmisor(), tipoDocRefPri, nroDocRefPri);  
    	boolean booDB = false;
    	if(resulDB != null && resulDB.size()>0){ 	
	    	for(ComprobantesPagoElectronicos tcpe : resulDB) { 
	    		log.info("tcpe.toString(): {}",tcpe.toString());
	    		if((tcpe.getIndEstadoCpe() != null) 
	    				&& ((tcpe.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Recha)
	    				|| (tcpe.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Anula))){
	    			booDB = true;
	    			continue;
	    		}	
	    		if(documento.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS)
	    				|| documento.getTipoDocumento().equals(Constantes.SUNAT_REVERSION)){
	    			booDB = true;
	    			continue;
	    		}
    			try {
		    		//log.info(documento.getTipoComprobante()+" | tcpe.getIndEstadoCpe(): "+tcpe.getIndEstadoCpe());
		    		if((tcpe.getId()!=null) && (tcpe.getId()>0)) {
		    			log.info("tcpe.getId(); "+tcpe.getId());
		    			Documento tbc = new Documento();
		    			tbc.setId(tcpe.getId());
		    			Documento tbcBill = this.buscarTbComprobanteID(tbc);		    			
		    			if((tbcBill!=null) && (tbcBill.getTipoDocumento()!=null)) { 
		    				log.info("tbcBill: {}",tbcBill.toString());
		    				if(tbcBill.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO)) {
		    					if((tbcBill.getErrorComprobante()!= null) && 
		    							(tbcBill.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0)))){
		    						booDB = true;
		    						log.info("bDB-RC: {}",booDB);
		    						return resulDB;
		    					}
				    		}else {	   	    					    				
				    			if((tbcBill.getRespuestaSunat()!= null) && 
				    					(tbcBill.getRespuestaSunat().trim().equals(Constantes.CONTENT_TRUE))){
				    				booDB = true;
				    				log.info("bDB-SendBill: {}",booDB);
				    				return resulDB;
				    			}	
		    					String fuenteSunatList = mapa.get(TipoJBossOsePropertiesEnum.SUNAT_LIST_CONSULTA.getCodigo());
		    					//log.info("fuenteSunatList: {}",fuenteSunatList);
		    					if(fuenteSunatList.equals(Constantes.CONTENT_NO_APLICA)) {
		    						if((tbcBill.getErrorComprobante()!= null) && 
			    							(tbcBill.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0)))){
			    						booDB = true;
			    						log.info("bDB-SendBill-Testing: {}",booDB);
			    						return resulDB;
			    					}
		    					}
				    		}
		    			}		    			
	    			}
      			} catch (Exception e) {
      				StringWriter errors = new StringWriter();				
      				e.printStackTrace(new PrintWriter(errors));
      				log.error("buscarCompobantePago buscarTbComprobanteID Exception \n"+errors);
      			}
	    	}
    	}
    	
    	List<ComprobantesPagoElectronicos> resulSunatList = this.buscarListGetSunatList(sNumRUC, tipo, serie, Long.valueOf(sNumCor));    	   	
    	if(resulSunatList != null && resulSunatList.size()>0){
	    	if(documento.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS) ||
					documento.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO) ||
					documento.getTipoDocumento().equals(Constantes.SUNAT_REVERSION)||
					documento.getTipoDocumento().equals(Constantes.SUNAT_GUIA_REMISION_REMITENTE)||
					documento.getTipoDocumento().equals(Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA)) { 	
	    		//log.info(" 1) documento.getTipoComprobante(): "+documento.getTipoComprobante()+" | "+resulSunatList.size());
	    		
	    		//if((resulSunatList!=null) && (resulSunatList.size() == 0)) {
		    		if(tipoDocRefPri.equals(Constantes.SUNAT_FACTURA) ||
		    				tipoDocRefPri.equals(Constantes.SUNAT_BOLETA) ||
		    				tipoDocRefPri.equals(Constantes.SUNAT_NOTA_CREDITO) ||
		    				tipoDocRefPri.equals(Constantes.SUNAT_NOTA_DEBITO)) {
		    			//log.info(" 2) documento.getTipoComprobante(): "+documento.getTipoComprobante()+" | "+tipoDocRefPri);
		    			if(ConsultaSunat.getOlitwsconscpegem(documento.getRucEmisor(), tipoDocRefPri, nroDocRefPri)) 
		    				return this.getListTsComprobantesPagoElectronicos(resulSunatList, resulDB);    				 				    				
		    		}
	    		//}
	    		//log.info(" 3) documento.getTipoComprobante(): "+documento.getTipoComprobante()+" | "+d);
	    		if(resulDB != null && resulDB.size()>0){
		    		for(ComprobantesPagoElectronicos t : resulDB) {    	
		    			//log.info("4) "+d+" | "+ t.getFechaCrea()+ " | "+DateUtil.deltaDays(new Date(), t.getFechaCrea()));  			
		    			if(t.getFechaCrea()!= null) {
		    				if(DateUtil.deltaDays(new Date(), t.getFechaCrea()) > 0) 
		    					resulDB = new ArrayList<ComprobantesPagoElectronicos>();
		    			}
		    		}
	    		}
	    	}   
    	}else {
    		if(documento.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO)
    				&& !(booDB))
    			resulDB = null;
    	} 	
    	return this.getListTsComprobantesPagoElectronicos(resulSunatList, resulDB);
    }      
    
	private List<ComprobantesPagoElectronicos> getListTsComprobantesPagoElectronicos(
    		List<ComprobantesPagoElectronicos> resulSunatList, List<ComprobantesPagoElectronicos> resulDB){    	
    	boolean bSunatList = false;
    	boolean bDB = false;
    	if((resulSunatList != null) && (resulSunatList.size() > 0))
    		bSunatList = true;
    	if((resulDB!=null) && (resulDB.size()>0))
    		bDB = true;  
    	log.info("bSunatList: "+bSunatList+" - bDB: "+bDB);
    	if(bSunatList && !bDB)
    		return resulSunatList;   	
    	if(!bSunatList && bDB) {	
    		return resulDB;    		
    	}
    	if(bSunatList && bDB) { 
    		Date fechaEmisionSunatList = resulSunatList.get(0).getFecEmisionCpe();
    		Date fechaEmisionDB = resulDB.get(0).getFecEmisionCpe();
    		//log.info("dias1: "+dias);
    		if(DateUtil.deltaDays(fechaEmisionDB, fechaEmisionSunatList) >= 0) {
    			//log.info("resulSunatList.get(0).getIndEstadoCpe(): "+resulSunatList.get(0).getIndEstadoCpe());
    			Short sia = Constantes.SUNAT_IndEstCpe_Acept;
    			if(resulSunatList.get(0).getIndEstadoCpe().equals(sia)) {
    				//log.info("dias2: "+dias);
    				return resulSunatList;
    			}
    			return resulDB;
    		}
    		return resulSunatList;
    	}   	
    	return new ArrayList<ComprobantesPagoElectronicos>();
    }   	
    
    public boolean getDBCpe2TbComprobanteID(Documento documento, 
    		Long numRuc, String tipoDocumento, String serie_numero, String fechaEmision, String monto) {
    	List<Documento> resultTBC = new ArrayList<Documento>();
    	try {        		
    		String [] docRefPri = serie_numero.split("-");
    		//log.info("getDBCpe2TbComprobanteID: "+numRuc+"-"+ tipoDocumento+"-"+docRefPri[0]+"-"+docRefPri[1]+" | "+fechaEmision+" | "+monto);    		  		
        	String sNumRUC = String.valueOf(numRuc);
        	String tipo = tipoDocumento;
        	String serie = docRefPri[0];
    		int iNumCor = 0;
    		if(StringUtil.hasString(docRefPri[1]))
    			iNumCor = Integer.parseInt(docRefPri[1]);
    		String sNumCor = String.valueOf(iNumCor); 	
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePago(documento, 
    				numRuc, tipoDocumento, serie_numero); 
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results) {
	    			if(cp.getIndEstadoCpe().equals(Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Acept))))
	    				return true;
	    			Long ID = 0L;
	    			//log.info("cp.getId(): "+cp.getId());
	    			if(cp.getId()!=null && cp.getId()>0)
	    				ID = cp.getId();
	    			DocumentoDAOLocal documentoDAOLocal = 
	    				(DocumentoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"DocumentoDAOImpl"+Constantes.OSE_CDEJB_DAO+"DocumentoDAOLocal");
	    			Documento tbc = new Documento();    			
	    			tbc.setId(ID);					  					
	    			resultTBC = documentoDAOLocal.buscarTbComprobanteID(tbc);  
	    			//log.info("resultTBC.size(): "+resultTBC.size());
	    			if(resultTBC.size()==0) {
	    				Documento tbCom = new Documento();
	    				tbCom.setRucEmisor(numRuc);
	    				tbCom.setTipoDocumento(tipoDocumento);
	    				tbCom.setSerie(serie);
	    				tbCom.setNumeroCorrelativo(docRefPri[1]);
	    				List<Documento> tbcList = documentoDAOLocal.buscarTbComprobante4Key(tbCom);		
	    				if(tbcList != null && tbcList.size()>0){
	    					for(Documento tb : tbcList){					
	    						//log.info("tb: "+tb.getId()+"-"+tb.getIdComprobante()+"-"+tb.getErrorNumero());
	    						int iNumCorCpe = Integer.parseInt(tb.getNumeroCorrelativo());
	    						//log.info("docRefPri[1]: "+docRefPri[1]+" | iNumCor: "+iNumCor+" - iNumCorCpe: "+iNumCorCpe);
	    						if(iNumCorCpe == iNumCor) {
	    							resultTBC.add(tb);
	    						}
	    					}
	    				}
	    			}
	    		}	
    		}
    		List<ComprobantesPagoElectronicos> resulSunatList = this.buscarListGetSunatList(sNumRUC, tipo, serie, Long.valueOf(sNumCor));    	    	
    		return this.getTsComprobantesPagoElectronicosState(resulSunatList, resultTBC);
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDBCpe2TbComprobanteID Exception \n"+errors);
		}
    	return false;
    }     
    
	private boolean getTsComprobantesPagoElectronicosState(
    		List<ComprobantesPagoElectronicos> resulSunatList, List<Documento> resultTBC){    	
    	if((resulSunatList!=null) && (resulSunatList.size()>0))
    		return true;
    	if(resultTBC != null && resultTBC.size()>0){
			for(Documento tbc : resultTBC) {
				if(tbc.getRespuestaSunat().trim().equals(Constantes.SUNAT_RESPUESTA_0))
					return true;
			}  	
    	}
    	return false;
    }    
    
    private List<ComprobantesPagoElectronicos> revisarCompobantePagoGetSunatList(Documento documento, 
    		String tipoDocRefPri, String nroDocRefPri){
    	String [] docRefPri = nroDocRefPri.split("-");
    	//log.info("revisarCompobantePagoGetSunatList: "+documento.getRucEmisor()+" - "+ tipoDocRefPri+" - "+docRefPri[0]+" - "+docRefPri[1]);
    	String sNumRUC = String.valueOf(documento.getRucEmisor());
    	String tipo = tipoDocRefPri;
    	String serie = docRefPri[0];
		Long iNumCor = 0l;
		if(StringUtil.hasString(docRefPri[1]))
			iNumCor = Long.valueOf(docRefPri[1]);
		//String sNumCor = String.valueOf(iNumCor);    	 		   	
       	return this.buscarListGetSunatList(sNumRUC, tipo, serie, iNumCor);
    }         
    
    private List<ComprobantesPagoElectronicos> revisarCompobantePago2ConsultaIntegrada(Documento documento, 
    		String tipoDocRefPri, String nroDocRefPri, String fechaEmision, String monto){
    	String [] docRefPri = nroDocRefPri.split("-");
    	//log.info("revisarCompobantePago2ConsultaIntegrada: "+documento.getRucEmisor()+"-"+ tipoDocRefPri+"-"+docRefPri[0]+"-"+docRefPri[1]+" | "+fechaEmision+" | "+monto);
    	String sNumRUC = String.valueOf(documento.getRucEmisor());
    	String tipo = tipoDocRefPri;
    	String serie = docRefPri[0];
		String sNumCor = docRefPri[1];    	 		   	
       	return this.buscarConsultaIntegradaSunat(sNumRUC, tipo, serie, sNumCor, fechaEmision, monto);
    }            

	public String buscarTbComprobantePagoListID(Documento documento, EResumenDocumento eDocumento) {		
		//log.info("buscarTbComprobantePagoListID "+documento.getNombre());
		try {    			
			StringBuilder sListID = new StringBuilder("");
			boolean b = true;
			for(EResumenDocumentoItem rdi : eDocumento.getItems()) {
				String [] docRefPri = rdi.getId().split("-");
      			String serie = docRefPri[0];
      			int iNumCor = 0;
      			if(StringUtil.hasString(docRefPri[1]))
      				iNumCor = Integer.parseInt(docRefPri[1]);
      			String sNumCor = String.valueOf(iNumCor); 
	    		if(b) {
	    			sListID.append("'"+rdi.getDocumentTypeCode()+"-"+serie+"-"+sNumCor+"'");
	    			b=false;
	    			continue;
	    		}
	    		sListID.append(",'"+rdi.getDocumentTypeCode()+"-"+serie+"-"+sNumCor+"'");
	    	}
    		return sListID.toString();
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteIn Exception \n"+errors);
		}
        return null;
	}	
	  
    public void getCompobantePagoResumenDiarioItem(Documento documento, EResumenDocumentoItem rdi, 
    		Date fechaRecep) {
    	try {    
    		log.info("getCompobantePagoResumenDiarioItem "+rdi.getDocumentTypeCode()+"-"+rdi.getId()+" : "+rdi.getStatus());
    		//String fechaEmisionCPE = DateUtil.dateFormat(fechaEmision,"dd/MM/yyyy");
    		//String monto = String.valueOf(rdi.getTotalVenta());
    		List<ComprobantesPagoElectronicos> results = this.buscarCompobantePago(documento, 
    				documento.getRucEmisor(), rdi.getDocumentTypeCode(), rdi.getId());
    		//log.info("results.size(): "+results.size());
    		if(results != null && results.size()>0){
	    		for(ComprobantesPagoElectronicos cp : results){
	    			if(cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Recha || cp.getIndEstadoCpe()==Constantes.SUNAT_IndEstCpe_Anula){
	   					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	   					documento.setErrorNumero(Constantes.SUNAT_ERROR_2987);
	   					String cpe = "";
	   	    			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
	   	    					cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
	   	    			cpe = cpe+"["+rdi.getDocumentTypeCode()+"-"+rdi.getId()+"]";
	   	    			documento.setErrorLog(cpe);
	   	    			documento.setLongitudNombre(0);
	   					return;   					
	   				} 	
	    			if(rdi.getStatus()==Constantes.SUNAT_IndEstCpe_Acept){  	    			    		
	   					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	   					documento.setErrorNumero(Constantes.SUNAT_ERROR_2282);
	   					String cpe = "";
	   	    			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
	   	    				cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
	   	    			cpe = cpe+"["+rdi.getDocumentTypeCode()+"-"+rdi.getId()+"]";
	   	    			documento.setErrorLog(cpe);
	   	    			documento.setLongitudNombre(0);
						return;
	   				}  
	        		if(rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Anulado) {
	        			String [] docRefPri = rdi.getId().split("-");
	        			if(!StringUtil.hasString(docRefPri[0].substring(0, 1))) {
	        				SunatParametroDAOLocal tsParametroDAOLocal = 
	        						(SunatParametroDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatParametroDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatParametroDAOLocal");						
	        				if(tsParametroDAOLocal.getParametroErrorFechas(Constantes.SUNAT_DIAS_7,
	        						fechaRecep, cp.getFecEmisionCpe())) {	
	        					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
	        					documento.setErrorNumero(Constantes.SUNAT_ERROR_2957);			
	        					String cpe = "";
	        	    			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
	        	    					cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
	        	    			cpe = cpe+"["+rdi.getDocumentTypeCode()+"-"+rdi.getId()+"]";
	        	    			documento.setErrorLog(cpe);
	        	    			documento.setLongitudNombre(0);
	        					return;
	    	    			}
	        			}
	        		}  	    			
	    			return;
	    		}   
    		}
    		if((rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Modificar) ||
    				(rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Anulado)) {
    			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_2663);
				String cpe = "";
    			if(documento.getErrorLog()!=null && !(documento.getErrorLog().isEmpty()))
    				cpe = documento.getErrorLog()+Constantes.OSE_SPLIT_3;
    			cpe = cpe+"["+rdi.getDocumentTypeCode()+"-"+rdi.getId()+"]";
    			documento.setErrorLog(cpe);
    			documento.setLongitudNombre(0);
				return;
    		}    
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePagoResumenDiarioItem Exception \n"+errors);
		}
    }                   

    private List<ComprobantesPagoElectronicos> buscarCompobantePagoDB(Documento documento, 
    		Long numRuc, String tipoDocRefPri, String nroDocRefPri) {    	
    	try {
    		String [] docRefPri = nroDocRefPri.split("-");
    		log.info("buscarCompobantePagoDB: "+numRuc+"-"+ tipoDocRefPri+"-"+docRefPri[0]+"-"+docRefPri[1]);    		
    		String consulta = "SELECT d FROM ComprobantesPagoElectronicos d "
				+" where d.comprobantesPagoElectronicosPK.numRuc = :numRuc "
				+" and d.comprobantesPagoElectronicosPK.codCpe = :codCpe "
				+" and d.comprobantesPagoElectronicosPK.numSerieCpe = :numSerieCpe "
				+" and d.comprobantesPagoElectronicosPK.numCpe = :numCpe ";
    		Map<String, Object> parameterValues = new HashMap<String, Object>();
    		parameterValues.put("numRuc", numRuc);
    		parameterValues.put("codCpe", tipoDocRefPri);
    		parameterValues.put("numSerieCpe", docRefPri[0]);
    		long iNumCor = 0;
    		if(StringUtil.hasString(docRefPri[1]))
    			iNumCor = Long.valueOf(docRefPri[1]);   		
    		//String sNumCor = String.valueOf(iNumCor);  
    		parameterValues.put("numCpe", iNumCor);    		
    		List<ComprobantesPagoElectronicos> results = this.query(consulta, parameterValues);    
    		if(results != null && results.size()>0)
    			return results;
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarCompobantePagoDB (5) Exception \n"+errors);
		}
        return null;
    }
    
	public List<ComprobantesPagoElectronicos> buscarTbComprobantePagoIn(Documento documento, String sListID) {		
		//log.info("buscarTbComprobanteIn "+documento.toString()+" | "+sNumCorNew);
		try {    		    		
			String consulta = "SELECT * "
					+ " FROM AXTEROIDOSE.COMPROBANTES_PAGO_ELECTRONICOS d where d.NUM_RUC = "+documento.getRucEmisor()+""
					+ " and d.COD_CPE ||'-'|| d.NUM_SERIE_CPE ||'-'|| d.NUM_CPE in ("+sListID+") ";
			//log.info("consulta: "+consulta);
	    	Map<String, Object> parameterValues = new HashMap<String, Object>();	
			List<ComprobantesPagoElectronicos> results = this.nativeQuery(consulta, parameterValues); 
			if(results != null && results.size()>0)	
				return results;
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteIn Exception \n"+errors);
		}
        return null;
	}	
	
	public Integer countComprobantePagoIDState(Documento documento, int sState) {		
		//log.info("countComprobantePagoIDState "+documento.toString()+" | "+sState);
		Integer countState = 0;
		try {    		    					
			String consulta = "SELECT * FROM AXTEROIDOSE.COMPROBANTES_PAGO_ELECTRONICOS d "
					+ " where d.ID = "+documento.getId()+" AND d.IND_ESTADO_CPE = "+sState+" ";
			//log.info("consulta: "+consulta);
	    	Map<String, Object> parameterValues = new HashMap<String, Object>();	
	    	List<ComprobantesPagoElectronicos>  results = this.nativeQuery(consulta, parameterValues); 
			if(results != null && results.size()>0)	
				countState = results.size();
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("countComprobantePagoIDState Exception \n"+errors);
		}
        return countState;
	}	
	
    public List<ComprobantesPagoElectronicos> buscarListGetSunatList(String ruc, 
    		String tipo, String serie, Long numero ){
    	List<ComprobantesPagoElectronicos> results = new ArrayList <ComprobantesPagoElectronicos>(); 
    	Map<String, Timestamp> mapTime = new HashMap<String, Timestamp>();    	   		
    	try {
    		mapTime.put("StartSunatList", new Timestamp(System.currentTimeMillis()));
    		String fuenteSunatList = mapa.get(TipoJBossOsePropertiesEnum.SUNAT_LIST_CONSULTA.getCodigo());
    		boolean verComprobantePagoList = false;
    		if(fuenteSunatList.equals(Constantes.CONTENT_FALSE)) {
    			mapTime.put("StartSunatListJDBC", new Timestamp(System.currentTimeMillis()));
        		SunatComprobantesPagoElectronicosDAO sunatComprobantesPagoElectronicosPGDAO = new SunatComprobantesPagoElectronicosDAOImpl();
        		//results = sunatComprobantesPagoElectronicosPGDAO.buscarSunatCompobantePagoDB(ruc, tipo, serie, numero);
        		if((results != null) && (results.size() > 0))
        			verComprobantePagoList = true;
        		mapTime.put("EndSunatListJDBC", new Timestamp(System.currentTimeMillis())); 						
    		}
    		if(fuenteSunatList.equals(Constantes.CONTENT_TRUE)) {	
    			mapTime.put("StartSunatListRest", new Timestamp(System.currentTimeMillis())); 
				Optional<ComprobantePago> opt = ListSunatRest.buscaComprobantePagoElectronico(ruc, tipo, serie, numero);							
				if(opt.isPresent()){    			
					ComprobantePago cp = opt.get();
					ComprobantesPagoElectronicos tsComprobantesPagoElectronicos = new ComprobantesPagoElectronicos();
					ComprobantesPagoElectronicosPK tsComprobantesPagoElectronicosPK = new ComprobantesPagoElectronicosPK();
					Long numRuc = Long.parseLong(cp.getRuc());
					tsComprobantesPagoElectronicosPK.setNumRuc(numRuc);
					tsComprobantesPagoElectronicosPK.setCodCpe(cp.getTipo());
					tsComprobantesPagoElectronicosPK.setNumSerieCpe(cp.getSerie());
					Long numCpe = Long.valueOf(cp.getSecuencial());
					tsComprobantesPagoElectronicosPK.setNumCpe(numCpe);   			
					tsComprobantesPagoElectronicos.setComprobantesPagoElectronicosPK(tsComprobantesPagoElectronicosPK);
					Short indEstadoCpe = Short.valueOf(cp.getEstado().toString());
					tsComprobantesPagoElectronicos.setIndEstadoCpe(indEstadoCpe);
					tsComprobantesPagoElectronicos.setFecEmisionCpe(cp.getFechaEmision());
					BigDecimal mtoImporteCpe = BigDecimal.valueOf(cp.getImporte());
					tsComprobantesPagoElectronicos.setMtoImporteCpe(mtoImporteCpe);
					tsComprobantesPagoElectronicos.setCodMonedaCpe(cp.getMoneda());
					Short codMotTraslado = Short.valueOf(cp.getMotivoTraslado().toString());
					tsComprobantesPagoElectronicos.setCodMotTraslado(codMotTraslado);
					Short codModTraslado = Short.valueOf(cp.getModTraslado().toString());
					tsComprobantesPagoElectronicos.setCodModTraslado(codModTraslado);
					Short indTransbordo = Short.valueOf(cp.getIndicadorTransbordo().toString());
					tsComprobantesPagoElectronicos.setIndTransbordo(indTransbordo);				
					tsComprobantesPagoElectronicos.setFecIniTraslado(cp.getFechaInicioTraslado());
					tsComprobantesPagoElectronicos.setIndPercepcion(null); 	
					if(cp.getIndicadorPercepcion()!= null) {
						Short indicadorPercepcion = Short.valueOf(cp.getIndicadorPercepcion().toString());
						tsComprobantesPagoElectronicos.setIndPercepcion(indicadorPercepcion); 	
					}
					tsComprobantesPagoElectronicos.setIndForPpag(null);
					if(cp.getIndicadorFormaPago()!= null) {
						Short indicadorFormaPago = Short.valueOf(cp.getIndicadorFormaPago().toString());
						tsComprobantesPagoElectronicos.setIndForPpag(indicadorFormaPago);
					}
					results.add(tsComprobantesPagoElectronicos);
					verComprobantePagoList = true;
				}			
				mapTime.put("EndOSunatListRest", new Timestamp(System.currentTimeMillis())); 						
    		}	
    		mapTime.put("EndSunatList", new Timestamp(System.currentTimeMillis())); 
			if(verComprobantePagoList)
				DocumentUtil.getStatisticsSunatList(mapTime,results.get(0).toString(), fuenteSunatList);
			else
				DocumentUtil.getStatisticsSunatList(mapTime,"No existe comprobante de pago electronico", fuenteSunatList);			
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarListGetSunatList Exception \n"+errors);
		}
	    return results;
    } 	
    
    public List<ComprobantesPagoElectronicos> buscarSunatListResumenMasiva(String ruc, ComprobantePagoListID comprobantePagoListID){
    	List<ComprobantesPagoElectronicos> results = new ArrayList <ComprobantesPagoElectronicos>(); 
    	try {
    		String oseFilea = mapa.get(TipoJBossOsePropertiesEnum.SUNAT_LIST_CONSULTA.getCodigo());
    		if(oseFilea.equals(Constantes.CONTENT_FALSE))
        		return null;
    		
    		//ComprobantePagoList comprobantePagoList = GetSunatList.busacrComprobantePagoMasiva(ruc, comprobantePagoListID);       
    		ComprobantePagoList comprobantePagoList = new ComprobantePagoList();
    		int comprobantePagoSize = 0;
        	if(comprobantePagoList != null &&  comprobantePagoList.getComprobantePagoList() != null
        			&& comprobantePagoList.getComprobantePagoList().size()>0){    		
        		for(ComprobantePago cp : comprobantePagoList.getComprobantePagoList()) {
					ComprobantesPagoElectronicos tsComprobantesPagoElectronicos = new ComprobantesPagoElectronicos();
					ComprobantesPagoElectronicosPK tsComprobantesPagoElectronicosPK = new ComprobantesPagoElectronicosPK();
					Long numRuc = Long.parseLong(cp.getRuc());
					tsComprobantesPagoElectronicosPK.setNumRuc(numRuc);
					tsComprobantesPagoElectronicosPK.setCodCpe(cp.getTipo());
					tsComprobantesPagoElectronicosPK.setNumSerieCpe(cp.getSerie());
					Long numCpe = Long.valueOf(cp.getSecuencial());
					tsComprobantesPagoElectronicosPK.setNumCpe(numCpe);   			
					tsComprobantesPagoElectronicos.setComprobantesPagoElectronicosPK(tsComprobantesPagoElectronicosPK);
					Short indEstadoCpe = Short.valueOf(cp.getEstado().toString());
					tsComprobantesPagoElectronicos.setIndEstadoCpe(indEstadoCpe);
					tsComprobantesPagoElectronicos.setFecEmisionCpe(cp.getFechaEmision());
					BigDecimal mtoImporteCpe = BigDecimal.valueOf(cp.getImporte());
					tsComprobantesPagoElectronicos.setMtoImporteCpe(mtoImporteCpe);
					tsComprobantesPagoElectronicos.setCodMonedaCpe(cp.getMoneda());
					Short codMotTraslado = Short.valueOf(cp.getMotivoTraslado().toString());
					tsComprobantesPagoElectronicos.setCodMotTraslado(codMotTraslado);
					Short codModTraslado = Short.valueOf(cp.getModTraslado().toString());
					tsComprobantesPagoElectronicos.setCodModTraslado(codModTraslado);
					Short indTransbordo = Short.valueOf(cp.getIndicadorTransbordo().toString());
					tsComprobantesPagoElectronicos.setIndTransbordo(indTransbordo);				
					tsComprobantesPagoElectronicos.setFecIniTraslado(cp.getFechaInicioTraslado());	
//					if(true){
//						log.info("cp.getRuc(): "+cp.getRuc()+" - "+tsComprobantesPagoElectronicos.getTsComprobantesPagoElectronicosPK().getNumRuc());
//						log.info("cp.getTipo(): "+cp.getTipo()+" - "+tsComprobantesPagoElectronicos.getTsComprobantesPagoElectronicosPK().getCodCpe());
//						log.info("cp.getSerie(): "+cp.getSerie()+" - "+tsComprobantesPagoElectronicos.getTsComprobantesPagoElectronicosPK().getNumSerieCpe());
//						log.info("cp.getSecuencial(): "+cp.getSecuencial()+" - "+tsComprobantesPagoElectronicos.getTsComprobantesPagoElectronicosPK().getNumCpe());
//						log.info("cp.getEstado(): "+cp.getEstado()+" - "+tsComprobantesPagoElectronicos.getIndEstadoCpe());
//						log.info("cp.getFechaEmision(): "+cp.getFechaEmision()+" - "+tsComprobantesPagoElectronicos.getFecEmisionCpe());
//						log.info("cp.getImporte(): "+cp.getImporte()+" - "+tsComprobantesPagoElectronicos.getMtoImporteCpe());
//						log.info("cp.getMoneda(): "+cp.getMoneda()+" - "+tsComprobantesPagoElectronicos.getCodMonedaCpe());
//						log.info("cp.getMotivoTraslado(): "+cp.getMotivoTraslado()+" - "+tsComprobantesPagoElectronicos.getCodMotTraslado());
//						log.info("cp.getModTraslado(): "+cp.getModTraslado()+" - "+tsComprobantesPagoElectronicos.getCodModTraslado());
//						log.info("cp.getIndicadorTransbordo(): "+cp.getIndicadorTransbordo()+" - "+tsComprobantesPagoElectronicos.getIndTransbordo());
//						log.info("cp.getFechaInicioTraslado(): "+cp.getFechaInicioTraslado()+" - "+tsComprobantesPagoElectronicos.getFecIniTraslado());
//					}
					results.add(tsComprobantesPagoElectronicos);
        		}
        		comprobantePagoSize = comprobantePagoList.getComprobantePagoList().size();
			}	
        	log.info("List<ComprobantePago>: "+comprobantePagoSize);
			return results;
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarListGetSunatList Exception \n"+errors);
		}
	    return results;
    }   
    
    private List<ComprobantesPagoElectronicos> buscarConsultaIntegradaSunat(String ruc, String tipo, 
    		String serie, String numero, String fechaEmision, String monto){
    	List<ComprobantesPagoElectronicos> results = new ArrayList <ComprobantesPagoElectronicos>(); 
    	try {
    		String oseFilea = mapa.get(TipoJBossOsePropertiesEnum.SUNAT_WS_OLITWSCONSCPEGEM_CONSULTA.getCodigo());
    		if(oseFilea.equals(Constantes.CONTENT_FALSE))
        		return null;	
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
						ComprobantesPagoElectronicos tsComprobantesPagoElectronicos = new ComprobantesPagoElectronicos();
						ComprobantesPagoElectronicosPK tsComprobantesPagoElectronicosPK = new ComprobantesPagoElectronicosPK();
						Long numRuc = Long.parseLong(ruc);
						tsComprobantesPagoElectronicosPK.setNumRuc(numRuc);
						tsComprobantesPagoElectronicosPK.setCodCpe(tipo);
						tsComprobantesPagoElectronicosPK.setNumSerieCpe(serie);
						tsComprobantesPagoElectronicosPK.setNumCpe(Long.valueOf(numero));   			
						tsComprobantesPagoElectronicos.setComprobantesPagoElectronicosPK(tsComprobantesPagoElectronicosPK);
						Short indEstadoCpe = Short.valueOf(sunatValidar.getData().getEstadoCp());
						tsComprobantesPagoElectronicos.setIndEstadoCpe(indEstadoCpe);
						
						tsComprobantesPagoElectronicos.setFecEmisionCpe(DateUtil.parseDate(fechaEmision,"dd/mm/yyyy"));
						BigDecimal mtoImporteCpe = BigDecimal.valueOf(Double.valueOf(monto));
						tsComprobantesPagoElectronicos.setMtoImporteCpe(mtoImporteCpe);
						tsComprobantesPagoElectronicos.setCodMonedaCpe("");
						Short codMotTraslado = Short.valueOf("0");
						tsComprobantesPagoElectronicos.setCodMotTraslado(codMotTraslado);
						Short codModTraslado = Short.valueOf("0");
						tsComprobantesPagoElectronicos.setCodModTraslado(codModTraslado);
						Short indTransbordo = Short.valueOf("0");
						tsComprobantesPagoElectronicos.setIndTransbordo(indTransbordo);				
						tsComprobantesPagoElectronicos.setFecIniTraslado(null);		    			
		    			
		    			if(true){
							log.info("sunatValidar.getSuccess(): "+sunatValidar.getSuccess());	
							log.info("sunatValidar.getMessage(): "+sunatValidar.getMessage());
							log.info("sunatValidar.getErrorCode(): "+sunatValidar.getErrorCode());
							log.info("sunatValidar.getData().getEstadoCp(): "+sunatValidar.getData().getEstadoCp());
							log.info("sunatValidar.getData().getEstadoRuc(): "+sunatValidar.getData().getEstadoRuc());
							log.info("sunatValidar.getData().getCondDomiRuc(): "+sunatValidar.getData().getCondDomiRuc());
							log.info("sunatValidar.getData().getObservaciones(): "+sunatValidar.getData().getObservaciones());
		    			}
		    			log.info("Optional<SunatValidar> opt: "+opt.isPresent());
		    			results.add(tsComprobantesPagoElectronicos);
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
    
    private Documento buscarTbComprobanteID(Documento documento) {
    	//log.info("documento.getId(): "+documento.getId());    
    	try {   		   		
			DocumentoDAOLocal documentoDAOLocal = 
					(DocumentoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"DocumentoDAOImpl"+Constantes.OSE_CDEJB_DAO+"DocumentoDAOLocal");
	    	List<Documento> results = documentoDAOLocal.buscarTbComprobanteID(documento);		    	
	    	if(results != null && results.size()>0){
		    	for(Documento tbc : results) {      
					log.info("documento: "+tbc.toString());  
					if(tbc.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO)) {
						if(tbc.getErrorComprobante()!= null && 
		   					tbc.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0)))
							return tbc;
					}else {					
						if(tbc.getRespuestaSunat()!= null && 
		   					tbc.getRespuestaSunat().trim().equals(Constantes.CONTENT_TRUE))
							return tbc;
    					String fuenteSunatList = mapa.get(TipoJBossOsePropertiesEnum.SUNAT_LIST_CONSULTA.getCodigo());
    					//log.info("fuenteSunatList: {}",fuenteSunatList);
    					if(fuenteSunatList.equals(Constantes.CONTENT_NO_APLICA)) {
    						if((tbc.getErrorComprobante()!= null) && 
	    							(tbc.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0)))){
	    						return tbc;
	    					}
    					}
					}
				} 
	    	}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteID Exception \n"+errors);
		}
    	return new Documento();
    } 
    
	private void updateTbComprobanteErrorComprobante(Documento documento) {
		//log.info("documento.getId(): "+documento.getId());    
		try {   		   		
			DocumentoDAOLocal documentoDAOLocal = 
					(DocumentoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"DocumentoDAOImpl"+Constantes.OSE_CDEJB_DAO+"DocumentoDAOLocal");
	    	documentoDAOLocal.updateTbComprobanteErrorComprobante(documento);		    	    	
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("updateTbComprobanteErrorComprobante Exception \n"+errors);
		}
	}    
}
