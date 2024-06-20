package com.axteroid.ose.server.avatar.task;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.apirest.sunat.ConsultaIntegradaSunatRest;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.bean.SunatTokenResponse;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumentoItem;
import com.axteroid.ose.server.tools.ubltype.TypeBillingReference;
import com.axteroid.ose.server.tools.util.DateUtil;

public class UBLListDataValidate {
	private static final Logger log = LoggerFactory.getLogger(UBLListDataValidate.class);	
	
    public void revisarRespuesta2RARCSunatList(Documento tbComprobante, EResumenDocumento eDocumento) {
    	log.info("revisarRespuesta2RARCSunatList "+tbComprobante.getTipoDocumento());
    	try {      		
      		DocumentoCrud comprobanteCrud = new DocumentoCrud();
      		tbComprobante.setLongitudNombre(0);
      		tbComprobante.setDireccion("0"); 
      		String fechaEmision = DateUtil.dateFormat(eDocumento.getFechaEmisionComprobante(),"dd/MM/yyyy");
      		SunatTokenResponse sunatToken = ConsultaIntegradaSunatRest.buscaTokenCpe();
      		log.info("getExpires_in "+sunatToken.getExpires_in());
      		for(EResumenDocumentoItem rdi : eDocumento.getItems()) {
      			//sunatToken = ConsultaIntegradaSunat.buscaTokenCpe();
      			//log.info("getExpires_in "+sunatToken.getExpires_in());
				if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO))	
					comprobanteCrud.revisarRespuesta2RCItemSunatList(tbComprobante, rdi, fechaEmision, sunatToken);
      			if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS))
      				comprobanteCrud.revisarRespuesta2RAItemSunatList(tbComprobante, rdi);
      		}      		      		
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("revisarRespuesta2RARCSunatList Exception \n"+errors);
		}
    }
    
    public void revisarRespuesta2RRSunatList(Documento tbComprobante, EReversionDocumento eDocumento) {
    	log.info("revisarRespuesta2RRSunatList "+tbComprobante.getTipoDocumento());
    	try {
//      		TsComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
//					(TsComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(OseConstantes.OSE_doLookup+"TsComprobantesPagoElectronicosDAOImpl"+OseConstantes.OSE_CDEJB_DAO+"TsComprobantesPagoElectronicosDAOLocal");      		
      		DocumentoCrud comprobanteCrud = new DocumentoCrud();
      		tbComprobante.setLongitudNombre(0);
      		for(EReversionDocumentoItem rdi : eDocumento.getItems()) {
      			comprobanteCrud.revisarRespuesta2RRItemSunatList(tbComprobante, rdi);
      		}      		      		
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("revisarRespuesta2RRSunatList Exception \n"+errors);
		}
    }    
 
    public void revisarRespuesta2NCNDSunatList(Documento tbComprobante, EDocumento eDocumento) {
    	log.info("revisarRespuesta2NCNDSunatList "+tbComprobante.getNombre());
    	try {
//      		TsComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
//					(TsComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(OseConstantes.OSE_doLookup+"TsComprobantesPagoElectronicosDAOImpl"+OseConstantes.OSE_CDEJB_DAO+"TsComprobantesPagoElectronicosDAOLocal");      		
      		
      		DocumentoCrud comprobanteCrud = new DocumentoCrud();
      		tbComprobante.setLongitudNombre(0);
			for(TypeBillingReference tbr : eDocumento.getBillingReference()){	
				String tipoDocumento = "";
				if(tbr.getInvoiceDocumentReference().getDocumentTypeCode()!=null) 					
					tipoDocumento = tbr.getInvoiceDocumentReference().getDocumentTypeCode();
				else
					continue;			
				comprobanteCrud.revisarRespuesta2NCNDItemSunatList(tbComprobante, 
						tipoDocumento, tbr.getInvoiceDocumentReference().getId());
			}	     		      		
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("revisarRespuesta2RRSunatList Exception \n"+errors);
		}
    }      
}
