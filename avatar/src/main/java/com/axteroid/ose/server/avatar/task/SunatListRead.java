package com.axteroid.ose.server.avatar.task;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.apirest.list.ListSunatRest;
import com.axteroid.ose.server.avatar.dao.SunatComprobantesPagoElectronicosDAO;
import com.axteroid.ose.server.avatar.dao.impl.SunatComprobantesPagoElectronicosDAOImpl;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatComprobantesPagoElectronicos;
import com.axteroid.ose.server.rulesubl.builder.ParseXML2Document;
import com.axteroid.ose.server.rulesubl.builder.impl.ParseXml2DocumentImpl;
import com.axteroid.ose.server.tools.bean.ComprobantePago;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoAvatarPropertiesEnum;
import com.axteroid.ose.server.tools.constantes.TipoDocumentoSUNATEnum;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumentoItem;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.axteroid.ose.server.tools.util.StringUtil;

public class SunatListRead {
	private static final Logger log = LoggerFactory.getLogger(SunatListRead.class);
	private ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
	    
    public boolean buscarListGetSunatList(String ruc, String tipo, String serie, String numero){
    	try {
    		String oseFilea = DirUtil.getAvatarPropertiesValue(TipoAvatarPropertiesEnum.SUNAT_LIST_CONSULTA.getCodigo());
    		if(oseFilea.equals(Constantes.CONTENT_FALSE))
        		return false;       	
			//log.info("buscarListGetbuscarTscpepreoseDBSunatList: "+ruc+"-"+ tipo+"-"+serie+"-"+numero);  
			SunatComprobantesPagoElectronicosDAO sunatComprobantesPagoElectronicosPGDAO = new SunatComprobantesPagoElectronicosDAOImpl();
			List<SunatComprobantesPagoElectronicos> results = sunatComprobantesPagoElectronicosPGDAO.buscarSunatCompobantePagoDB(ruc, tipo, serie, numero);
			log.info("buscarCompobantePagoDBbuscarTscpepreoseDBSunatList: {}",results.size()>0 ? true : false);
			if(results.size()>0);
				return true;
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarListGetbuscarTscpepreoseDBSunatList Exception \n"+errors);
		}
	    return false;
    } 
 
//    private boolean buscarListGetSunatList(String ruc, String tipo, String serie, String numero, String fechaEmision){
//    	try {
//    		String oseFilea = DirUtil.getAvatarPropertiesValue(TipoAvatarPropertiesEnum.SUNAT_LIST_CONSULTA.getCodigo());
//    		if(oseFilea.equals(ConstantesOse.CONTENT_FALSE))
//        		return false;        	
//			//log.info("buscarListGetSunatList: "+ruc+"-"+ tipo+"-"+serie+"-"+numero);  
//			SunatComprobantesPagoElectronicosDAO sunatComprobantesPagoElectronicosPGDAO = new SunatComprobantesPagoElectronicosDAOImpl();
//			List<SunatComprobantesPagoElectronicos> results = sunatComprobantesPagoElectronicosPGDAO.buscarSunatCompobantePagoDB(ruc, tipo, serie, numero);
//			log.info("buscarListGetSunatList: {}",results.size()>0 ? true : false);
//			if(results.size()>0) {
//				Date fechaEmisionUBL = DateUtil.stringToDateYYYY_MM_DD(fechaEmision);
//				Date fechaEmisionSunatList = new Date(results.get(0).getFecEmisionCpe().getTime());  
//				if(!LogDateUtil.esFechaIgual(fechaEmisionUBL, fechaEmisionSunatList))
//					return false;
//				return true;
//			}
//		} catch (Exception e) {
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));
//			log.error("buscarListGetSunatList Exception \n"+errors);
//		}
//	    return false;
//    } 
    
    public boolean buscarListGetSunatListRest(String ruc, String tipo, String serie, Long numero){
    	try {
    		String oseFilea = DirUtil.getAvatarPropertiesValue(TipoAvatarPropertiesEnum.SUNAT_LIST_CONSULTA.getCodigo());
    		if(oseFilea.equals(Constantes.CONTENT_FALSE))
        		return false;
        	        	   		
			Optional<ComprobantePago> opt = ListSunatRest.buscaComprobantePagoElectronico(ruc, tipo, serie, numero);
			if(opt.isPresent()){    			
				ComprobantePago cp = opt.get();		
				log.info("cp.getRuc(): "+cp.getRuc());
				log.info("cp.getTipo(): "+cp.getTipo());
				log.info("cp.getSerie(): "+cp.getSerie());
				log.info("cp.getSecuencial(): "+cp.getSecuencial());
				log.info("cp.getEstado(): "+cp.getEstado());
				log.info("cp.getFechaEmision(): "+cp.getFechaEmision());
				log.info("cp.getImporte(): "+cp.getImporte());
				log.info("cp.getMoneda(): "+cp.getMoneda());
				log.info("cp.getMotivoTraslado(): "+cp.getMotivoTraslado());
				log.info("cp.getModTraslado(): "+cp.getModTraslado());
				log.info("cp.getIndicadorTransbordo(): "+cp.getIndicadorTransbordo());
				log.info("cp.getFechaInicioTraslado(): "+cp.getFechaInicioTraslado());
//				Date fechaEmisionUBL = DateUtil.stringToDateYYYY_MM_DD(fechaEmision);
//				Date fechaEmisionSunatList = new Date(cp.getFechaEmision().getTime());  
//				if(!LogDateUtil.esFechaIgual(fechaEmisionUBL, fechaEmisionSunatList))
//					return false;
			}
			log.info("Optional<ComprobantePago> opt: "+opt.isPresent());
			return opt.isPresent();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarListSunatConsult Exception \n"+errors);
		}
	    return false;
    }     
    
    public boolean sunatListReview(Documento tbComprobante){
    	try {   
    		if(tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.FACTURA.getCodigo()) ||
    				tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.BOLETA.getCodigo()) ||
    				tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.NOTA_CREDITO.getCodigo()) || 
    				tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.NOTA_DEBITO.getCodigo())  ||
    				tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.GUIA_REMISION.getCodigo()) ||
    				tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.RECIBO_SERV_PUBL.getCodigo()) ||
    				tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.RETENCION.getCodigo()) ||
    				tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.PERCEPCION.getCodigo())) 	{
				String sNumRUC = String.valueOf(tbComprobante.getRucEmisor());
				int iNumCor = 0;
				if(StringUtil.hasString(tbComprobante.getNumeroCorrelativo()))
					iNumCor = Integer.parseInt(tbComprobante.getNumeroCorrelativo());
				String sNumCor = String.valueOf(iNumCor);					
				boolean b = this.buscarListGetSunatList(sNumRUC, 
						tbComprobante.getTipoDocumento(), tbComprobante.getSerie(), sNumCor);
				if(b)  					
					return b;			
    		}
    		if(tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.RESUMEN_DIARIO.getCodigo())) {
    			EResumenDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryResumen(tbComprobante);
    			for(EResumenDocumentoItem rdi : eDocumento.getItems()){	
    				String RUC = String.valueOf(tbComprobante.getRucEmisor());
    				log.info("RC-serie: "+rdi.getId());
    				String [] docRefPri = rdi.getId().split("-");
    				
    				boolean b = this.buscarListGetSunatList(RUC, 
    						rdi.getDocumentTypeCode(), docRefPri[0], docRefPri[1]);
    				if(!b)  					
    					return false;	    				
    			}
    			return true;
    		}   		
    		if(tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.COMUNICACION_BAJAS.getCodigo())) {
    			EResumenDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryResumen(tbComprobante);
    			for(EResumenDocumentoItem rdi : eDocumento.getItems()){	
    				String RUC = String.valueOf(tbComprobante.getRucEmisor());
    				log.info("RA-serie: "+rdi.getSerieDocumentoBaja()+"-"+rdi.getNumeroDocumentoBaja());
    				
    				boolean b = this.buscarListGetSunatList(RUC, 
    						rdi.getTipoDocumento(), rdi.getSerieDocumentoBaja(), rdi.getNumeroDocumentoBaja());
    				if(!b)  					
    					return false;    				   				
    			}
    			return true;
    		}   		
    		if(tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.REVERSION.getCodigo())) {
    			EReversionDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryReversion(tbComprobante);
    			for(EReversionDocumentoItem rdi : eDocumento.getItems()){	
    				String RUC = String.valueOf(tbComprobante.getRucEmisor());
    				log.info("RR-serie: "+rdi.getSerieNumeroDocRevertido());
    				String [] docRefPri = rdi.getSerieNumeroDocRevertido().split("-");
    				
    				boolean b = this.buscarListGetSunatList(RUC, 
    						rdi.getTipoDocumentoRevertido(), docRefPri[0], docRefPri[1]);
    				if(!b) 					
    					return false; 				
    			}
    			return true;
    		}	
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("sunatListReview Exception \n"+errors);
		}
	    return false;
    }
}
