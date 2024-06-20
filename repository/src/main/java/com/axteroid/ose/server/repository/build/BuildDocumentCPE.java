package com.axteroid.ose.server.repository.build;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Date;

//import org.apache.log4j.Logger;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.ComprobantesPagoElectronicos;
import com.axteroid.ose.server.jpa.model.SunatComprobantesPagoElectronicos;
import com.axteroid.ose.server.tools.util.LogDateUtil;

public class BuildDocumentCPE {
	//private final static Logger log =  Logger.getLogger(BuildDocumentCPE.class);
	private static final Logger log = LoggerFactory.getLogger(BuildDocumentCPE.class);
    public Document appendDocumentCPE(SunatComprobantesPagoElectronicos sunatComprobantesPagoElectronicos){    	
    	//log.info("appendDocumentCPE ---> "+tsComprobantesPagoElectronicos.getTsComprobantesPagoElectronicosPK().getNumRuc());
    	Document document = new Document();
    	try {
	        document.append("NUM_RUC", sunatComprobantesPagoElectronicos.getSunatComprobantesPagoElectronicosPK().getNumRuc());
	        document.append("COD_CPE", sunatComprobantesPagoElectronicos.getSunatComprobantesPagoElectronicosPK().getCodCpe());
	        document.append("NUM_SERIE_CPE", sunatComprobantesPagoElectronicos.getSunatComprobantesPagoElectronicosPK().getNumSerieCpe());        
	        document.append("NUM_CPE", sunatComprobantesPagoElectronicos.getSunatComprobantesPagoElectronicosPK().getNumCpe());       
	        document.append("IND_ESTADO_CPE", sunatComprobantesPagoElectronicos.getIndEstadoCpe());               
	        Date emision = LogDateUtil.dateToDateYYYYMMDD(sunatComprobantesPagoElectronicos.getFecEmisionCpe());
	        document.append("FEC_EMISION_CPE", emision);
	        BigDecimal importe = new BigDecimal(0);
	        if(sunatComprobantesPagoElectronicos.getMtoImporteCpe()!=null)
	        	importe = sunatComprobantesPagoElectronicos.getMtoImporteCpe(); 
	        document.append("MTO_IMPORTE_CPE", String.valueOf(importe)); 
	        document.append("COD_MONEDA_CPE", sunatComprobantesPagoElectronicos.getCodMonedaCpe());
	        Date fecCrea = LogDateUtil.dateToDateYYYYMMDD(sunatComprobantesPagoElectronicos.getFechaCrea());
	        document.append("FECHA_CREA", fecCrea);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("appendDocumentCPE Exception \n "+errors);
		}        
        return document;
    }
}
