package com.axteroid.ose.server.repository.build;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

//import org.apache.log4j.Logger;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EReversionDocumentoItem;
import com.axteroid.ose.server.tools.util.LogDateUtil;

public class BuildDocumentCPEALL {
	//private final static Logger log =  Logger.getLogger(BuildDocumentCPEALL.class);
	private static final Logger log = LoggerFactory.getLogger(BuildDocumentCPEALL.class);
    public Document appendDocumentCPEALL(Documento tbComprobante){    	
    	//log.info("appendDocumentCPE ---> "+tsComprobantesPagoElectronicos.getTsComprobantesPagoElectronicosPK().getNumRuc());
    	Document document = new Document();
    	try {
    		document.append("ID", tbComprobante.getId());
	        document.append("NUM_RUC", tbComprobante.getRucEmisor());			
	        document.append("COD_CPE", tbComprobante.getTipoDocumento());
	        document.append("NUM_SERIE_CPE", tbComprobante.getSerie());        
	        document.append("NUM_CPE", tbComprobante.getNumeroCorrelativo());       
	        document.append("IND_ESTADO_CPE", tbComprobante.getErrorComprobante());               
	        Date emision = LogDateUtil.dateToDateYYYYMMDD(tbComprobante.getFecIniProc());
	        document.append("FEC_EMISION_CPE", emision);
	        document.append("USER_CREA", tbComprobante.getUserCrea());
	        Date fecCrea = LogDateUtil.dateToDateYYYYMMDD(tbComprobante.getFechaCrea());
	        document.append("FECHA_CREA", fecCrea);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("appendDocumentCPEALL Exception ID: "+tbComprobante.getId()+" \n "+errors);
		}        
        return document;
    }
    
    public Document appendDocumentCPEALL_RC_RA(EResumenDocumentoItem rdi, Documento tbComprobante){    	
    	//log.info("appendDocumentCPE ---> "+tsComprobantesPagoElectronicos.getTsComprobantesPagoElectronicosPK().getNumRuc());
    	Document document = new Document();
    	try {
    		document.append("ID", tbComprobante.getId());
	        document.append("NUM_RUC", tbComprobante.getRucEmisor());
			String [] docRefPri = rdi.getId().split("-");
	        document.append("COD_CPE", rdi.getDocumentTypeCode());
	        document.append("NUM_SERIE_CPE", docRefPri[0]);        
	        document.append("NUM_CPE", docRefPri[1]);       
	        document.append("IND_ESTADO_CPE", tbComprobante.getErrorComprobante());               
	        Date emision = LogDateUtil.dateToDateYYYYMMDD(tbComprobante.getFecIniProc());
	        document.append("FEC_EMISION_CPE", emision);
	        document.append("USER_CREA", tbComprobante.getUserCrea());
	        Date fecCrea = LogDateUtil.dateToDateYYYYMMDD(tbComprobante.getFechaCrea());
	        document.append("FECHA_CREA", fecCrea);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("appendDocumentCPEALL_RC_RA Exception ID: "+tbComprobante.getId()+" \n "+errors);
		}        
        return document;
    }
    
    public Document appendDocumentCPEALL_RR(EReversionDocumentoItem rdi, Documento tbComprobante){    	
    	//log.info("appendDocumentCPE ---> "+tsComprobantesPagoElectronicos.getTsComprobantesPagoElectronicosPK().getNumRuc());
    	Document document = new Document();
    	try {
    		document.append("ID", tbComprobante.getId());
	        document.append("NUM_RUC", tbComprobante.getRucEmisor());
			String [] docRefPri = rdi.getSerieNumeroDocRevertido().split("-");
	        document.append("COD_CPE", rdi.getTipoDocumentoRevertido() );
	        document.append("NUM_SERIE_CPE", docRefPri[0]);        
	        document.append("NUM_CPE", docRefPri[1]);       
	        document.append("IND_ESTADO_CPE", tbComprobante.getErrorComprobante());               
	        Date emision = LogDateUtil.dateToDateYYYYMMDD(tbComprobante.getFecIniProc());
	        document.append("FEC_EMISION_CPE", emision);
//	        BigDecimal importe = new BigDecimal(0);
//	        if(tsComprobantesPagoElectronicos.getMtoImporteCpe()!=null)
//	        	importe = tsComprobantesPagoElectronicos.getMtoImporteCpe(); 
//	        document.append("MTO_IMPORTE_CPE", String.valueOf(importe)); 
//	        document.append("COD_MONEDA_CPE", tsComprobantesPagoElectronicos.getCodMonedaCpe());
	        document.append("USER_CREA", tbComprobante.getUserCrea());
	        Date fecCrea = LogDateUtil.dateToDateYYYYMMDD(tbComprobante.getFechaCrea());
	        document.append("FECHA_CREA", fecCrea);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("appendDocumentCPEALL_RR Exception ID: "+tbComprobante.getId()+" \n "+errors);
		}        
        return document;
    }
}
