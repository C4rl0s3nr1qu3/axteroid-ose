package com.axteroid.ose.server.builder.review;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesejb.rules.UBLListValidateLocal;
import com.axteroid.ose.server.rulesejb.rules.impl.UBLListValidateImpl;
import com.axteroid.ose.server.rulesubl.builder.ParseXML2Document;
import com.axteroid.ose.server.rulesubl.builder.impl.ParseXml2DocumentImpl;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;

public class DBDocumentCpeReview {
	//private static final Logger log = Logger.getLogger(DBDocumentCpeReview.class);
	private static final Logger log = LoggerFactory.getLogger(DBDocumentCpeReview.class);
	private ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
	private UBLListValidateLocal ublListValidateLocal = new UBLListValidateImpl();	
	
	public boolean documentReview(Documento tbComprobante) {
		log.info("documentReview: "+tbComprobante.toString());
		switch(tbComprobante.getTipoDocumento()){
			case "RA":
				return(this.getDocumentSummaryResumen(tbComprobante));
			case "RC":
				return(this.getDocumentSummaryResumen(tbComprobante));
			case "RR":
				return(this.getDocumentSummaryReversion(tbComprobante));
			default:
				return true;
		}
	}	
	
	public boolean getDocumentFBNCND(Documento tbComprobante){	
		boolean error = true;		
		return error;
	}
	
	public boolean getDocumentGuia(Documento tbComprobante){
		boolean error = true;		
		return error;		
	}
	
	public boolean getDocumentRetencion(Documento tbComprobante){
		boolean error = true;		
		return error;		
	}
	
	public boolean getDocumentPercepcion(Documento tbComprobante){
		boolean error = true;		
		return error;		
	}
	
	public boolean getDocumentSummaryResumen(Documento tbComprobante) {		
		EResumenDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryResumen(tbComprobante);			
		return ublListValidateLocal.getDBRARCReview(tbComprobante, eDocumento);
	}
	
	public boolean getDocumentSummaryReversion(Documento tbComprobante){
		EReversionDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryReversion(tbComprobante);
		return ublListValidateLocal.getDBRRReview(tbComprobante, eDocumento);	
	}
	
}
