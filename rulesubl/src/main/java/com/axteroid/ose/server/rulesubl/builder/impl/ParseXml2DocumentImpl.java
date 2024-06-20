package com.axteroid.ose.server.rulesubl.builder.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesubl.builder.ParseXML2Document;
import com.axteroid.ose.server.rulesubl.dozer.UnMarshallerFactoryOSE;
import com.axteroid.ose.server.rulesubl.dozer.impl.UnMarshallerFactoryOSEImpl;
import com.axteroid.ose.server.rulesubl.xmldoc.InvoiceXmlRead;
import com.axteroid.ose.server.rulesubl.xmldoc.PerceptionXmlRead;
import com.axteroid.ose.server.rulesubl.xmldoc.SummaryXmlRead;
import com.axteroid.ose.server.rulesubl.xmldoc.UblXmlRead;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.tools.xml.XmlUtil;

public class ParseXml2DocumentImpl implements ParseXML2Document{
	private static final Logger log = LoggerFactory.getLogger(ParseXml2DocumentImpl.class);
	public EDocumento parseUBLSendBill(Documento tbComprobante){			
		try {
			log.info("parseUBLSendBill");
			UnMarshallerFactoryOSE unMarshallerFactoryOSE = new UnMarshallerFactoryOSEImpl();
			Object domType = unMarshallerFactoryOSE.unmarshalXml(tbComprobante);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
				return null;
			EDocumento eDocumento = (EDocumento) unMarshallerFactoryOSE.convertDocumentoToBean(tbComprobante, domType);		
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
				return null;	
			Document document = XmlUtil.getByteArray2Document(tbComprobante.getUbl());
			document.getDocumentElement().normalize();
			UblXmlRead ublXmlRead = new UblXmlRead();
			ublXmlRead.getDatosDocuments(document, eDocumento, tbComprobante);
			ublXmlRead.getAccountingSupplierParty(document, eDocumento);
			ublXmlRead.getAccountingCustomerParty(document, eDocumento);
			ublXmlRead.getAdditionalDocumentReference(document, eDocumento);
			InvoiceXmlRead invoiceXmlRead = new InvoiceXmlRead();
			if(eDocumento.getTipoDocumento().equals(Constantes.SUNAT_NOTA_DEBITO)) {
				ublXmlRead.getRequestedMonetaryTotal(document, eDocumento);
				invoiceXmlRead.getInvoiceDocumentsLine(document, eDocumento.getItems(), "cac:DebitNoteLine");
			}else
				ublXmlRead.getLegalMonetaryTotal(document, eDocumento);
			//log.info("eDocumento.getListTypePaymentTerms().size(): {} ",eDocumento.getListTypePaymentTerms().size());
			if((eDocumento.getTipoDocumento()!=null) && 
	        		(eDocumento.getTipoDocumento().equals(Constantes.SUNAT_FACTURA) || 
	        		eDocumento.getTipoDocumento().equals(Constantes.SUNAT_BOLETA) ||
	        		eDocumento.getTipoDocumento().equals(Constantes.SUNAT_RECIBO_SERV_PUBL) ||
	        		eDocumento.getTipoDocumento().trim().equals(Constantes.SUNAT_ROL_ADQUIRIENTE_SISTEMAS_PAGO) ||
	        		eDocumento.getTipoDocumento().trim().equals(Constantes.SUNAT_OPERADOR) ||
	        		eDocumento.getTipoDocumento().trim().equals(Constantes.SUNAT_ADQUIRIENTE_SISTEMAS_PAGO))) {				
				invoiceXmlRead.getInvoiceDocuments(document, eDocumento);
				invoiceXmlRead.getInvoiceDocumentsLine(document, eDocumento.getItems(), "cac:InvoiceLine");
			}
	        if((eDocumento.getTipoDocumento()!=null) && 
	        		(eDocumento.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO)))
	        	invoiceXmlRead.getInvoiceDocumentsLine(document, eDocumento.getItems(), "cac:CreditNoteLine");
			return eDocumento;			
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0305);	
			tbComprobante.setErrorLog(e.toString());
			log.info("parseUBLSendBill Exception --> "+ e.toString());
		}
		return null;
	}
	
	public ERetencionDocumento parseUBLSendBill_Retencion(Documento tbComprobante){			
		try {	
			UnMarshallerFactoryOSE unMarshallerFactoryOSE = new UnMarshallerFactoryOSEImpl();
			Object domType = unMarshallerFactoryOSE.unmarshalXml(tbComprobante);
			ERetencionDocumento eDocumento = (ERetencionDocumento) unMarshallerFactoryOSE.convertDocumentoToBean(tbComprobante, domType);													
			return eDocumento;
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0305);	
			tbComprobante.setErrorLog(e.toString());
			log.info("parseUBLSendBill_Retencion Exception --> "+ e.toString());
		}
		return null;
	}
	
	public EPercepcionDocumento parseUBLSendBill_Percepcion(Documento tbComprobante){			
		try {	
			//log.info("parseUBLSendBill_Percepcion");
			UnMarshallerFactoryOSE unMarshallerFactoryOSE = new UnMarshallerFactoryOSEImpl();
			Object domType = unMarshallerFactoryOSE.unmarshalXml(tbComprobante);
			EPercepcionDocumento eDocumento = (EPercepcionDocumento) unMarshallerFactoryOSE.convertDocumentoToBean(tbComprobante, domType);												
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
				return null;	
			Document document = XmlUtil.getByteArray2Document(tbComprobante.getUbl());
			document.getDocumentElement().normalize();
			PerceptionXmlRead perceptionXmlRead = new PerceptionXmlRead();
			perceptionXmlRead.getPerceptionDocuments(document, eDocumento);
			return eDocumento;
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0305);	
			tbComprobante.setErrorLog(e.toString());
			log.info("parseUBLSendBill_Percepcion Exception --> "+ e.toString());
		}
		return null;
	}
	
	public EGuiaDocumento parseUBLSendBill_Guia(Documento tbComprobante){			
		try {
			UnMarshallerFactoryOSE unMarshallerFactoryOSE = new UnMarshallerFactoryOSEImpl();
			Object domType = unMarshallerFactoryOSE.unmarshalXml(tbComprobante);
			EGuiaDocumento eDocumento = (EGuiaDocumento) unMarshallerFactoryOSE.convertDocumentoToBean(tbComprobante, domType);												
			return eDocumento;
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0305);	
			tbComprobante.setErrorLog(e.toString());
			log.info("parseUBLSendBill_Guia Exception --> "+ e.toString());
		}
		return null;
	}
	
	public EResumenDocumento parseUBLSend_SummaryResumen(Documento tbComprobante){			
		try {	
			UnMarshallerFactoryOSE unMarshallerFactoryOSE = new UnMarshallerFactoryOSEImpl();
			Object domType = unMarshallerFactoryOSE.unmarshalXml(tbComprobante);	
			EResumenDocumento eResumenDocumento = (EResumenDocumento) unMarshallerFactoryOSE.convertDocumentoToBean(tbComprobante, domType);	
			SummaryXmlRead summaryDocumentsXmlRead = new SummaryXmlRead();
			summaryDocumentsXmlRead.getSummaryDocumentsLine(tbComprobante, eResumenDocumento.getItems());	
			return eResumenDocumento;
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0305);	
			tbComprobante.setErrorLog(e.toString());
			log.info("parseUBLSend_SummaryResumen Exception --> "+ e.toString());
		}
		return null;
	}
	
	public EReversionDocumento parseUBLSend_SummaryReversion(Documento tbComprobante){			
		try {	
			UnMarshallerFactoryOSE unMarshallerFactoryOSE = new UnMarshallerFactoryOSEImpl();
			Object domType = unMarshallerFactoryOSE.unmarshalXml(tbComprobante);
			EReversionDocumento eReversionDocumento = (EReversionDocumento) unMarshallerFactoryOSE.convertDocumentoToBean(tbComprobante, domType);												
			return eReversionDocumento;
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0305);	
			tbComprobante.setErrorLog(e.toString());
			log.info("parseUBLSend_SummaryReversion Exception --> "+ e.toString());
		}
		return null;
	
	}
	
	public EDocumento parseUBLPortalOse(Documento tbComprobante){			
		try {
			UnMarshallerFactoryOSE unMarshallerFactoryOSE = new UnMarshallerFactoryOSEImpl();
			Object domType = unMarshallerFactoryOSE.unmarshalXml(tbComprobante);	
			EDocumento eDocumento = (EDocumento) unMarshallerFactoryOSE.convertDocumentoToBean(tbComprobante, domType);	
			
			Document document = XmlUtil.getByteArray2Document(tbComprobante.getUbl());
			document.getDocumentElement().normalize();
			UblXmlRead ublXmlRead = new UblXmlRead();
			ublXmlRead.getDatosDocuments(document, eDocumento, tbComprobante);
			ublXmlRead.getAccountingSupplierParty(document, eDocumento);
			ublXmlRead.getAccountingCustomerParty(document, eDocumento);
			ublXmlRead.getAdditionalDocumentReference(document, eDocumento);
			if(tbComprobante.getUblVersion().equals(Constantes.SUNAT_UBL_21)) {
				InvoiceXmlRead invoiceXmlRead = new InvoiceXmlRead();
				invoiceXmlRead.getInvoiceDocuments(document, eDocumento);
				invoiceXmlRead.getInvoiceDocumentsLine(document, eDocumento.getItems(),"cac:InvoiceLine");
			}			
			return eDocumento;			
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0305);	
			tbComprobante.setErrorLog(e.toString());
			log.info("parseUBLPortalOse Exception --> "+ e.toString());
		}
		return null;
	}	
	
	public Object parseUBLOse(Documento tbComprobante){
		UnMarshallerFactoryOSE unMarshallerFactoryOSE = new UnMarshallerFactoryOSEImpl();
		Object domType = unMarshallerFactoryOSE.unmarshalXml(tbComprobante);	
		return unMarshallerFactoryOSE.convertDocumentoToBean(tbComprobante, domType);	
	}
}
