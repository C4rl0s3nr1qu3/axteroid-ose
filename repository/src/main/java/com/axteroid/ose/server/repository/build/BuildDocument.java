package com.axteroid.ose.server.repository.build;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Date;

//import org.apache.log4j.Logger;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoDocumentoSUNATEnum;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.axteroid.ose.server.tools.util.FileUtil;
import com.axteroid.ose.server.tools.util.LogDateUtil;

public class BuildDocument {
	//private final static Logger log =  Logger.getLogger(BuildDocument.class);
	private static final Logger log = LoggerFactory.getLogger(BuildDocument.class);
    public void appendDocument_FBNCND(Documento tbComprobante, EDocumento eDocumento, Document document){
    	//log.info("appendDocument_FBNCND ---> "+tbComprobante.getNombre());
    	try {
	        document.append("ID", tbComprobante.getId());
	        document.append("ID_COMPROBANTE", tbComprobante.getIdComprobante());
	        document.append("RUC_EMISOR", tbComprobante.getRucEmisor());
	        document.append("TIPO_COMPROBANTE", tbComprobante.getTipoDocumento());
	        document.append("ESTADO", tbComprobante.getEstado().trim());
	        document.append("SERIE", tbComprobante.getSerie());
	        document.append("NUMERO_CORRELATIVO", tbComprobante.getNumeroCorrelativo());
	        
	        document.append("FECHA_EMISION", eDocumento.getFechaEmision());
	        document.append("TIPO_DOCUMENTO_EMISOR", eDocumento.getTipoDocumentoEmisor());
	        document.append("RAZON_SOCIAL_EMISOR", eDocumento.getRazonSocialEmisor());
	        document.append("DIRECCION_EMISOR", eDocumento.getDireccionEmisor());
	        
	        document.append("NUMERO_DOCUMENTO_CLIENTE", eDocumento.getNumeroDocumentoAdquiriente());
	        document.append("TIPO_DOCUMENTO_CLIENTE", eDocumento.getTipoDocumentoAdquiriente());
	        document.append("RAZON_SOCIAL_CLIENTE", eDocumento.getRazonSocialAdquiriente());    
	        document.append("DIRECCION_CLIENTE", eDocumento.getDireccionAdquiriente());
	        
	        document.append("TIPO_MONEDA", eDocumento.getTipoMoneda());
	        document.append("FECHA_VENCIMIENTO", eDocumento.getFechaVencimiento());
	        
	        BigDecimal opGravada = new BigDecimal(0);
	        if(eDocumento.getTotalOPGravada()!=null)
	        	opGravada = eDocumento.getTotalOPGravada();            	
	        document.append("OP_GRAVADA", String.valueOf(opGravada));
	        
	        BigDecimal igv = new BigDecimal(0);
	        if(eDocumento.getTotalIgv()!=null)
	        	igv = eDocumento.getTotalIgv();              
	        document.append("IGV", String.valueOf(igv));
	        
	        BigDecimal opInafecta = new BigDecimal(0);
	        if(eDocumento.getTotalOPInafecta()!=null)
	        	opInafecta = eDocumento.getTotalOPInafecta();              
	        document.append("OP_INAFECTA", String.valueOf(opInafecta));
	        
	        BigDecimal opExonerada = new BigDecimal(0);
	        if(eDocumento.getTotalOPExonerada()!=null)
	        	opExonerada = eDocumento.getTotalOPExonerada();              
	        document.append("OP_EXONERADA", String.valueOf(opExonerada));
	        
	        BigDecimal opExportacion = new BigDecimal(0);
	        if(eDocumento.getTotalOPExportacion()!=null)
	        	opExportacion = eDocumento.getTotalOPExportacion();  
	        document.append("OP_EXPORTACION", String.valueOf(opExportacion));  
	        
	        BigDecimal isc = new BigDecimal(0);
	        if(eDocumento.getTotalIsc()!=null)
	        	isc = eDocumento.getTotalIsc();              
	        document.append("ISC", String.valueOf(isc));      
	        
	        BigDecimal descuentoGlobalesAfectos = new BigDecimal(0);
	        document.append("DESCUENTOS_GLOBALES_AFECTOS", String.valueOf(descuentoGlobalesAfectos));
	        BigDecimal cargosGlobalesAfectos = new BigDecimal(0);
	        document.append("CARGOS_GLOBALES_AFECTOS", String.valueOf(cargosGlobalesAfectos));
	        BigDecimal descuentoGlobalesNoAfectos = new BigDecimal(0);
	        
	        BigDecimal total = new BigDecimal(0);
	        if(eDocumento.getTypeLegalMonetaryTotal()!= null && 
	        		eDocumento.getTypeLegalMonetaryTotal() != null &&
	        		eDocumento.getTypeLegalMonetaryTotal().getPayableAmount() != null)
	        	total = eDocumento.getTypeLegalMonetaryTotal().getPayableAmount();
	        if(tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.NOTA_DEBITO.getCodigo()) && 
	        		eDocumento.getTypeRequestedMonetaryTotal()!= null &&
	        		eDocumento.getTypeRequestedMonetaryTotal().getPayableAmount() != null )
	        	total = eDocumento.getTypeRequestedMonetaryTotal().getPayableAmount();
	        document.append("IMPORTE_TOTAL", String.valueOf(total));  
	        
	        document.append("DESCUENTOS_GLOBALES_NO_AFECTOS", String.valueOf(descuentoGlobalesNoAfectos));
	        BigDecimal cargosGlobalesNoAfectos = new BigDecimal(0);
	        document.append("CARGOS_GLOBALES_NO_AFECTOS", String.valueOf(cargosGlobalesNoAfectos));              
	        
	        document.append("PDF", null);  
	        document.append("UBL", null);
	        document.append("CDR", null); 
//	        String ruta = (new StringBuilder(DirUtil.getRutaDirectory())).toString();
//	        if(FileUtil.tobeDirectory(ruta+ConstantesOse.DIR_AMB_PROD_PG)) {
//		        document.append("UBL", tbComprobante.getUbl());
//		        document.append("CDR", tbComprobante.getCdr());    
//	        }  
	                
	        document.append("ESTADO_OSE", tbComprobante.getEstado().trim());        
	        
	        StringBuilder sbtc = new StringBuilder().append(tbComprobante.getTipoDocumento());
	        if(tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.NOTA_CREDITO.getCodigo())
	        	|| tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.NOTA_DEBITO.getCodigo())) {   
	        	if(eDocumento.getBillingReference()!=null && eDocumento.getBillingReference().size() > 0 &&
	        		eDocumento.getBillingReference().get(0).getInvoiceDocumentReference() != null && 
	        		eDocumento.getBillingReference().get(0).getInvoiceDocumentReference().getDocumentTypeCode()!= null){
	        		if(eDocumento.getBillingReference().get(0).getInvoiceDocumentReference().getDocumentTypeCode().equals(TipoDocumentoSUNATEnum.FACTURA.getCodigo()))
	        			sbtc.append(TipoDocumentoSUNATEnum.FACTURA.getCodigo());
	        		if(eDocumento.getBillingReference().get(0).getInvoiceDocumentReference().getDocumentTypeCode().equals(TipoDocumentoSUNATEnum.BOLETA.getCodigo()))
	        			sbtc.append(TipoDocumentoSUNATEnum.BOLETA.getCodigo());     
	        	}
	        }
	        document.append("TIPO_COMPROBANTE_OSE", sbtc.toString());        
	        document.append("UBL_VERSION", tbComprobante.getUblVersion());
	        document.append("USER_CREA", tbComprobante.getUserCrea());
	        Date fecCrea = LogDateUtil.dateToDateYYYYMMDD(tbComprobante.getFechaCrea());
	        document.append("FECHA_CREA", fecCrea);	        
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("appendDocument_FBNCND Exception ID: "+tbComprobante.getId()+" \n "+errors);
		}
    }

    public void appendDocument_Retencion(Documento tbComprobante, ERetencionDocumento eDocumento, Document document){
    	//log.info("appendDocument_Retencion ---> "+tbComprobante.getNombre());
    	try {
	        document.append("ID", tbComprobante.getId());
	        document.append("ID_COMPROBANTE", tbComprobante.getIdComprobante());
	        document.append("RUC_EMISOR", tbComprobante.getRucEmisor());
	        document.append("TIPO_COMPROBANTE", tbComprobante.getTipoDocumento());
	        document.append("ESTADO", tbComprobante.getEstado());
	        document.append("SERIE", tbComprobante.getSerie());
	        document.append("NUMERO_CORRELATIVO", tbComprobante.getNumeroCorrelativo());        
	        document.append("FECHA_EMISION", eDocumento.getFechaEmision());
	        
	        document.append("TIPO_DOCUMENTO_EMISOR", eDocumento.getTipoDocumentoEmisor());
	        document.append("RAZON_SOCIAL_EMISOR", eDocumento.getRazonSocialEmisor());
	        document.append("DIRECCION_EMISOR", eDocumento.getDireccionEmisor());
	        
	        document.append("NUMERO_DOCUMENTO_CLIENTE", eDocumento.getNumeroDocumentoAdquiriente());
	        document.append("TIPO_DOCUMENTO_CLIENTE", eDocumento.getTipoDocumentoAdquiriente());
	        document.append("RAZON_SOCIAL_CLIENTE", eDocumento.getRazonSocialAdquiriente());    
	        document.append("DIRECCION_CLIENTE", eDocumento.getDepartamentoProveedor());
	        
	        document.append("TIPO_MONEDA", eDocumento.getTipoMonedaTotalRetenido());
	        document.append("FECHA_VENCIMIENTO", "");
	        
	        BigDecimal opGravada = new BigDecimal(0);         	
	        document.append("OP_GRAVADA", String.valueOf(opGravada));
	        
	        BigDecimal igv = new BigDecimal(0);           
	        document.append("IGV", String.valueOf(igv));
	        
	        BigDecimal opInafecta = new BigDecimal(0);             
	        document.append("OP_INAFECTA", String.valueOf(opInafecta));
	        
	        BigDecimal opExonerada = new BigDecimal(0);            
	        document.append("OP_EXONERADA", String.valueOf(opExonerada));
	        
	        BigDecimal opExportacion = new BigDecimal(0);
	        document.append("OP_EXPORTACION", String.valueOf(opExportacion));  
	        
	        BigDecimal isc = new BigDecimal(0);            
	        document.append("ISC", String.valueOf(isc)); 
	        
	        BigDecimal descuentoGlobalesAfectos = new BigDecimal(0);
	        document.append("DESCUENTOS_GLOBALES_AFECTOS", String.valueOf(descuentoGlobalesAfectos));
	        BigDecimal cargosGlobalesAfectos = new BigDecimal(0);
	        document.append("CARGOS_GLOBALES_AFECTOS", String.valueOf(cargosGlobalesAfectos));
	        
	        BigDecimal total = new BigDecimal(0);
	        if(eDocumento.getImporteTotalPagado()!= null)
	        	total = eDocumento.getImporteTotalPagado();
	        document.append("IMPORTE_TOTAL", String.valueOf(total)); 
	        
	        BigDecimal descuentoGlobalesNoAfectos = new BigDecimal(0);        
	        document.append("DESCUENTOS_GLOBALES_NO_AFECTOS", String.valueOf(descuentoGlobalesNoAfectos));
	        BigDecimal cargosGlobalesNoAfectos = new BigDecimal(0);
	        document.append("CARGOS_GLOBALES_NO_AFECTOS", String.valueOf(cargosGlobalesNoAfectos));              
	        
	        document.append("PDF", null);  
	        document.append("UBL", null);
	        document.append("CDR", null); 
//	        String ruta = (new StringBuilder(DirUtil.getRutaDirectory())).toString();
//	        if(FileUtil.tobeDirectory(ruta+ConstantesOse.DIR_AMB_PROD_PG)) {
//		        document.append("UBL", tbComprobante.getUbl());
//		        document.append("CDR", tbComprobante.getCdr());    
//	        }    
	        
	        document.append("ESTADO_OSE", tbComprobante.getEstado().trim());  
	        document.append("TIPO_COMPROBANTE_OSE", tbComprobante.getTipoDocumento());        
	        document.append("UBL_VERSION", tbComprobante.getUblVersion()); 
	        document.append("USER_CREA", tbComprobante.getUserCrea());
	        Date fecCrea = LogDateUtil.dateToDateYYYYMMDD(tbComprobante.getFechaCrea());
	        document.append("FECHA_CREA", fecCrea);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("appendDocument_Retencion Exception ID: "+tbComprobante.getId()+" \n "+errors);
		}
    }    
    
    public void appendDocument_Percepcion(Documento tbComprobante, EPercepcionDocumento eDocumento, Document document){
    	//log.info("appendDocument_Percepcion ---> "+tbComprobante.getNombre());
    	try {
	        document.append("ID", tbComprobante.getId());
	        document.append("ID_COMPROBANTE", tbComprobante.getIdComprobante());
	        document.append("RUC_EMISOR", tbComprobante.getRucEmisor());
	        document.append("TIPO_COMPROBANTE", tbComprobante.getTipoDocumento());
	        document.append("ESTADO", tbComprobante.getEstado());
	        document.append("SERIE", tbComprobante.getSerie());
	        document.append("NUMERO_CORRELATIVO", tbComprobante.getNumeroCorrelativo());
	        
	        document.append("FECHA_EMISION", eDocumento.getFechaEmision());
	        document.append("TIPO_DOCUMENTO_EMISOR", eDocumento.getTipoDocumentoEmisor());
	        document.append("RAZON_SOCIAL_EMISOR", eDocumento.getRazonSocialEmisor());
	        document.append("DIRECCION_EMISOR", eDocumento.getDireccionEmisor());
	        
	        document.append("NUMERO_DOCUMENTO_CLIENTE", eDocumento.getNumeroDocumentoAdquiriente());
	        document.append("TIPO_DOCUMENTO_CLIENTE", eDocumento.getTipoDocumentoAdquiriente());
	        document.append("RAZON_SOCIAL_CLIENTE", eDocumento.getRazonSocialAdquiriente());    
	        document.append("DIRECCION_CLIENTE", eDocumento.getDireccionCliente());
	        
	        document.append("TIPO_MONEDA", eDocumento.getTipoMonedaTotalPercibido());
	        document.append("FECHA_VENCIMIENTO", "");
	        
	        BigDecimal opGravada = new BigDecimal(0);         	
	        document.append("OP_GRAVADA", String.valueOf(opGravada));
	        
	        BigDecimal igv = new BigDecimal(0);           
	        document.append("IGV", String.valueOf(igv));
	        
	        BigDecimal opInafecta = new BigDecimal(0);            
	        document.append("OP_INAFECTA", String.valueOf(opInafecta));
	        
	        BigDecimal opExonerada = new BigDecimal(0);           
	        document.append("OP_EXONERADA", String.valueOf(opExonerada));
	        
	        BigDecimal opExportacion = new BigDecimal(0);
	        document.append("OP_EXPORTACION", String.valueOf(opExportacion)); 
	        
	        BigDecimal isc = new BigDecimal(0);             
	        document.append("ISC", String.valueOf(isc));   
	        
	        BigDecimal descuentoGlobalesAfectos = new BigDecimal(0);
	        document.append("DESCUENTOS_GLOBALES_AFECTOS", String.valueOf(descuentoGlobalesAfectos));
	        BigDecimal cargosGlobalesAfectos = new BigDecimal(0);
	        document.append("CARGOS_GLOBALES_AFECTOS", String.valueOf(cargosGlobalesAfectos));
	        
	        BigDecimal total = new BigDecimal(0);
	        if(eDocumento.getImporteTotalCobrado()!= null)
	        	total = eDocumento.getImporteTotalCobrado();
	        document.append("IMPORTE_TOTAL", String.valueOf(total));         
	        
	        BigDecimal descuentoGlobalesNoAfectos = new BigDecimal(0);
	        document.append("DESCUENTOS_GLOBALES_NO_AFECTOS", String.valueOf(descuentoGlobalesNoAfectos));
	        BigDecimal cargosGlobalesNoAfectos = new BigDecimal(0);
	        document.append("CARGOS_GLOBALES_NO_AFECTOS", String.valueOf(cargosGlobalesNoAfectos));              
	        
	        document.append("PDF", null);  
	        document.append("UBL", null);
	        document.append("CDR", null); 
//	        String ruta = (new StringBuilder(DirUtil.getRutaDirectory())).toString();
//	        if(FileUtil.tobeDirectory(ruta+ConstantesOse.DIR_AMB_PROD_PG)) {
//		        document.append("UBL", tbComprobante.getUbl());
//		        document.append("CDR", tbComprobante.getCdr());    
//	        }     
	        
	        document.append("ESTADO_OSE", tbComprobante.getEstado().trim());  
	        document.append("TIPO_COMPROBANTE_OSE", tbComprobante.getTipoDocumento());        
	        document.append("UBL_VERSION", tbComprobante.getUblVersion()); 
	        document.append("USER_CREA", tbComprobante.getUserCrea());
	        Date fecCrea = LogDateUtil.dateToDateYYYYMMDD(tbComprobante.getFechaCrea());
	        document.append("FECHA_CREA", fecCrea);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("appendDocument_Percepcion Exception ID: "+tbComprobante.getId()+" \n "+errors);
		}
    }        
    
    public void appendDocument_Guia(Documento tbComprobante, EGuiaDocumento eDocumento, Document document){
    	//log.info("appendDocument_Guia ---> "+tbComprobante.getNombre());
    	try {
	        document.append("ID", tbComprobante.getId());
	        document.append("ID_COMPROBANTE", tbComprobante.getIdComprobante());
	        document.append("RUC_EMISOR", tbComprobante.getRucEmisor());
	        document.append("TIPO_COMPROBANTE", tbComprobante.getTipoDocumento());
	        document.append("ESTADO", tbComprobante.getEstado());
	        document.append("SERIE", tbComprobante.getSerie());
	        document.append("NUMERO_CORRELATIVO", tbComprobante.getNumeroCorrelativo());
	        
	        document.append("FECHA_EMISION", eDocumento.getFechaEmisionGuia());
	        document.append("TIPO_DOCUMENTO_EMISOR", eDocumento.getTipoDocumentoEmisor());
	        document.append("RAZON_SOCIAL_EMISOR", eDocumento.getRazonSocialEmisor());
	        document.append("DIRECCION_EMISOR", eDocumento.getDireccionPtoPartida());
	        
	        document.append("NUMERO_DOCUMENTO_CLIENTE", eDocumento.getNumeroDocumentoAdquiriente());
	        document.append("TIPO_DOCUMENTO_CLIENTE", eDocumento.getTipoDocumentoAdquiriente());
	        document.append("RAZON_SOCIAL_CLIENTE", eDocumento.getRazonSocialAdquiriente());    
	        document.append("DIRECCION_CLIENTE", eDocumento.getDireccionPtoLLegada());
	        
	        document.append("TIPO_MONEDA", "");
	        document.append("FECHA_VENCIMIENTO", "");
	        
	        BigDecimal opGravada = new BigDecimal(0);      	
	        document.append("OP_GRAVADA", String.valueOf(opGravada));
	        
	        BigDecimal igv = new BigDecimal(0);            
	        document.append("IGV", String.valueOf(igv));
	        
	        BigDecimal opInafecta = new BigDecimal(0);            
	        document.append("OP_INAFECTA", String.valueOf(opInafecta));
	        
	        BigDecimal opExonerada = new BigDecimal(0);            
	        document.append("OP_EXONERADA", String.valueOf(opExonerada));
	        
	        BigDecimal opExportacion = new BigDecimal(0);
	        document.append("OP_EXPORTACION", String.valueOf(opExportacion)); 
	        
	        BigDecimal isc = new BigDecimal(0);           
	        document.append("ISC", String.valueOf(isc));
	        
	        BigDecimal descuentoGlobalesAfectos = new BigDecimal(0);
	        document.append("DESCUENTOS_GLOBALES_AFECTOS", String.valueOf(descuentoGlobalesAfectos));
	        BigDecimal cargosGlobalesAfectos = new BigDecimal(0);
	        document.append("CARGOS_GLOBALES_AFECTOS", String.valueOf(cargosGlobalesAfectos));        
	        BigDecimal total = new BigDecimal(0);
	        document.append("IMPORTE_TOTAL", String.valueOf(total));                  
	        BigDecimal descuentoGlobalesNoAfectos = new BigDecimal(0);
	        document.append("DESCUENTOS_GLOBALES_NO_AFECTOS", String.valueOf(descuentoGlobalesNoAfectos));
	        BigDecimal cargosGlobalesNoAfectos = new BigDecimal(0);
	        document.append("CARGOS_GLOBALES_NO_AFECTOS", String.valueOf(cargosGlobalesNoAfectos));             
	        
	        document.append("PDF", null);  
	        document.append("UBL", null);
	        document.append("CDR", null); 
//	        String ruta = (new StringBuilder(DirUtil.getRutaDirectory())).toString();
//	        if(FileUtil.tobeDirectory(ruta+ConstantesOse.DIR_AMB_PROD_PG)) {
//		        document.append("UBL", tbComprobante.getUbl());
//		        document.append("CDR", tbComprobante.getCdr());    
//	        }  
	        
	        document.append("ESTADO_OSE", tbComprobante.getEstado().trim());  
	        document.append("TIPO_COMPROBANTE_OSE", tbComprobante.getTipoDocumento());        
	        document.append("UBL_VERSION", tbComprobante.getUblVersion()); 
	        document.append("USER_CREA", tbComprobante.getUserCrea());
	        Date fecCrea = LogDateUtil.dateToDateYYYYMMDD(tbComprobante.getFechaCrea());
	        document.append("FECHA_CREA", fecCrea);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("appendDocument_Guia Exception ID: "+tbComprobante.getId()+" \n "+errors);
		}
    }    

    public void appendDocument_SummaryResumen(Documento tbComprobante, EResumenDocumento eDocumento, Document document){
    	//log.info("appendDocument_SummaryResumen ---> "+tbComprobante.getNombre());
    	try {
	        document.append("ID", tbComprobante.getId());
	        document.append("ID_COMPROBANTE", tbComprobante.getIdComprobante());
	        document.append("RUC_EMISOR", tbComprobante.getRucEmisor());
	        document.append("TIPO_COMPROBANTE", tbComprobante.getTipoDocumento());
	        document.append("ESTADO", tbComprobante.getEstado());
	        document.append("SERIE", tbComprobante.getSerie());
	        document.append("NUMERO_CORRELATIVO", tbComprobante.getNumeroCorrelativo());
	        
	        document.append("FECHA_EMISION", eDocumento.getFechaEmisionComprobante());
	        document.append("TIPO_DOCUMENTO_EMISOR", eDocumento.getTipoDocumentoEmisor());
	        document.append("RAZON_SOCIAL_EMISOR", eDocumento.getRazonSocialEmisor());
	        document.append("DIRECCION_EMISOR", "");
	        
	        document.append("NUMERO_DOCUMENTO_CLIENTE", "");
	        document.append("TIPO_DOCUMENTO_CLIENTE", "");
	        document.append("RAZON_SOCIAL_CLIENTE", "");    
	        document.append("DIRECCION_CLIENTE", "");
	        
	        document.append("TIPO_MONEDA", "");
	        document.append("FECHA_VENCIMIENTO", "");
	        
	        BigDecimal opGravada = new BigDecimal(0);       	
	        document.append("OP_GRAVADA", String.valueOf(opGravada));
	        
	        BigDecimal igv = new BigDecimal(0);            
	        document.append("IGV", String.valueOf(igv));
	        
	        BigDecimal opInafecta = new BigDecimal(0);            
	        document.append("OP_INAFECTA", String.valueOf(opInafecta));
	        
	        BigDecimal opExonerada = new BigDecimal(0);             
	        document.append("OP_EXONERADA", String.valueOf(opExonerada));
	        
	        BigDecimal opExportacion = new BigDecimal(0);
	        document.append("OP_EXPORTACION", String.valueOf(opExportacion));  
	        
	        BigDecimal isc = new BigDecimal(0);           
	        document.append("ISC", String.valueOf(isc));      
	        
	        BigDecimal descuentoGlobalesAfectos = new BigDecimal(0);
	        document.append("DESCUENTOS_GLOBALES_AFECTOS", String.valueOf(descuentoGlobalesAfectos));
	        BigDecimal cargosGlobalesAfectos = new BigDecimal(0);
	        document.append("CARGOS_GLOBALES_AFECTOS", String.valueOf(cargosGlobalesAfectos));
	        
	        BigDecimal total = new BigDecimal(0);
	        document.append("IMPORTE_TOTAL", String.valueOf(total));           
	        
	        BigDecimal descuentoGlobalesNoAfectos = new BigDecimal(0);
	        document.append("DESCUENTOS_GLOBALES_NO_AFECTOS", String.valueOf(descuentoGlobalesNoAfectos));
	        BigDecimal cargosGlobalesNoAfectos = new BigDecimal(0);
	        document.append("CARGOS_GLOBALES_NO_AFECTOS", String.valueOf(cargosGlobalesNoAfectos));              
	        
	        document.append("PDF", null);  
	        document.append("UBL", null);
	        document.append("CDR", null); 
//	        String ruta = (new StringBuilder(DirUtil.getRutaDirectory())).toString();
//	        if(FileUtil.tobeDirectory(ruta+ConstantesOse.DIR_AMB_PROD_PG)) {
//		        document.append("UBL", tbComprobante.getUbl());
//		        document.append("CDR", tbComprobante.getCdr());    
//	        }   
	        
	        document.append("ESTADO_OSE", tbComprobante.getEstado().trim());  
	        document.append("TIPO_COMPROBANTE_OSE", tbComprobante.getTipoDocumento());        
	        document.append("UBL_VERSION", tbComprobante.getUblVersion()); 
	        document.append("USER_CREA", tbComprobante.getUserCrea());
	        Date fecCrea = LogDateUtil.dateToDateYYYYMMDD(tbComprobante.getFechaCrea());
	        document.append("FECHA_CREA", fecCrea);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("appendDocument_SummaryResumen Exception ID: "+tbComprobante.getId()+" \n "+errors);
		}
    }    
    
    public void appendDocument_SummaryReversion(Documento tbComprobante, EReversionDocumento eDocumento, Document document){
    	//log.info("appendDocument_SummaryReversion ---> "+tbComprobante.getNombre());
    	try {
	        document.append("ID", tbComprobante.getId());
	        document.append("ID_COMPROBANTE", tbComprobante.getIdComprobante());
	        document.append("RUC_EMISOR", tbComprobante.getRucEmisor());
	        document.append("TIPO_COMPROBANTE", tbComprobante.getTipoDocumento());
	        document.append("ESTADO", tbComprobante.getEstado());
	        document.append("SERIE", tbComprobante.getSerie());
	        document.append("NUMERO_CORRELATIVO", tbComprobante.getNumeroCorrelativo());
	        
	        document.append("FECHA_EMISION", eDocumento.getFechaEmisionreversion());
	        document.append("TIPO_DOCUMENTO_EMISOR", eDocumento.getTipoDocumentoEmisor());
	        document.append("RAZON_SOCIAL_EMISOR", eDocumento.getRazonSocialEmisor());
	        document.append("DIRECCION_EMISOR", "");
	        
	        document.append("NUMERO_DOCUMENTO_CLIENTE", "");
	        document.append("TIPO_DOCUMENTO_CLIENTE", "");
	        document.append("RAZON_SOCIAL_CLIENTE", "");    
	        document.append("DIRECCION_CLIENTE", "");
	        
	        document.append("TIPO_MONEDA", "");
	        document.append("FECHA_VENCIMIENTO", "");
	        
	        BigDecimal opGravada = new BigDecimal(0);         	
	        document.append("OP_GRAVADA", String.valueOf(opGravada));
	        
	        BigDecimal igv = new BigDecimal(0);           
	        document.append("IGV", String.valueOf(igv));
	        
	        BigDecimal opInafecta = new BigDecimal(0);            
	        document.append("OP_INAFECTA", String.valueOf(opInafecta));
	        
	        BigDecimal opExonerada = new BigDecimal(0);           
	        document.append("OP_EXONERADA", String.valueOf(opExonerada));
	        
	        BigDecimal opExportacion = new BigDecimal(0);
	        document.append("OP_EXPORTACION", String.valueOf(opExportacion));  
	        
	        BigDecimal isc = new BigDecimal(0);            
	        document.append("ISC", String.valueOf(isc));      
	        
	        BigDecimal descuentoGlobalesAfectos = new BigDecimal(0);
	        document.append("DESCUENTOS_GLOBALES_AFECTOS", String.valueOf(descuentoGlobalesAfectos));
	        BigDecimal cargosGlobalesAfectos = new BigDecimal(0);
	        document.append("CARGOS_GLOBALES_AFECTOS", String.valueOf(cargosGlobalesAfectos));
	        
	        BigDecimal total = new BigDecimal(0);
	        document.append("IMPORTE_TOTAL", String.valueOf(total));          
	        
	        BigDecimal descuentoGlobalesNoAfectos = new BigDecimal(0);        
	        document.append("DESCUENTOS_GLOBALES_NO_AFECTOS", String.valueOf(descuentoGlobalesNoAfectos));
	        BigDecimal cargosGlobalesNoAfectos = new BigDecimal(0);
	        document.append("CARGOS_GLOBALES_NO_AFECTOS", String.valueOf(cargosGlobalesNoAfectos));              
	        
	        document.append("PDF", null);  
	        document.append("UBL", null);
	        document.append("CDR", null); 
//	        String ruta = (new StringBuilder(DirUtil.getRutaDirectory())).toString();
//	        if(FileUtil.tobeDirectory(ruta+ConstantesOse.DIR_AMB_PROD_PG)) {
//		        document.append("UBL", tbComprobante.getUbl());
//		        document.append("CDR", tbComprobante.getCdr());    
//	        }  
	        
	        document.append("ESTADO_OSE", tbComprobante.getEstado().trim());  
	        document.append("TIPO_COMPROBANTE_OSE", tbComprobante.getTipoDocumento());        
	        document.append("UBL_VERSION", tbComprobante.getUblVersion()); 
	        document.append("USER_CREA", tbComprobante.getUserCrea());
	        Date fecCrea = LogDateUtil.dateToDateYYYYMMDD(tbComprobante.getFechaCrea());
	        document.append("FECHA_CREA", fecCrea);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("appendDocument_SummaryReversion Exception ID: "+tbComprobante.getId()+" \n "+errors);
		}
    }
    
}
