package com.axteroid.ose.server.rulesubl.builder.impl;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesubl.builder.CdrBuild;
import com.axteroid.ose.server.rulesubl.dozer.ParseDocument2CDRDozer;
import com.axteroid.ose.server.rulesubl.dozer.impl.ParseDocument2CDRDozerImpl;
import com.axteroid.ose.server.rulesubl.xmldoc.CdrOseXmlRead;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EDocumentoCDR;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.tools.exception.X509KeySelector;
import com.axteroid.ose.server.ubl21.sign.impl.SignDocumentCdrImpl;

public class CdrBuildImpl implements CdrBuild{
	private static final Logger log = LoggerFactory.getLogger(CdrBuildImpl.class);
	
	public void generarEDocumentCDR(Documento documento, EDocumento eDocumento) {
		EDocumentoCDR eDocumentoCDR = new EDocumentoCDR();			
		eDocumentoCDR.setFecha_recepcion_comprobante(documento.getFecRecepcion());
		eDocumentoCDR.setHora_recepcion_comprobante(documento.getFecRecepcion());
		eDocumentoCDR.setRuc_emisor_pse(String.valueOf(documento.getRucPseEmisor()));			
		ParseDocument2CDRDozer parseDocument2DataCDR = new ParseDocument2CDRDozerImpl();
		parseDocument2DataCDR.generarEDocumentCDR(documento, eDocumentoCDR, eDocumento);	
		//log.info("generarEDocumentCDR: 1 | "+eDocumento.getNumeroDocumentoAdquiriente());
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;
		CdrOseXmlRead cdrXmlRead = new CdrOseXmlRead();
		if(eDocumento.getNumeroDocumentoAdquiriente()!=null) {
			String[] nodo = eDocumento.getNumeroDocumentoAdquiriente().split("\\t");
			//log.info("generarEDocumentCDR: 2 | "+nodo);
			
			if(nodo.length>1){			
				//log.info("generarEDocumentCDR: 3 | "+eDocumento.getNumeroDocumentoAdquiriente());
				cdrXmlRead.upDateCDR(documento, eDocumento.getNumeroDocumentoAdquiriente(), "CompanyID");						
			}
		}
		//log.info("generarEDocumentCDR: 4 | "+eDocumento.getIssueTime());
		if(eDocumento.getIssueTime()!=null && eDocumento.getIssueTime().length()<12)
			cdrXmlRead.upDateCDR(documento, eDocumento.getIssueTime(), "IssueTime");
		
		SignDocumentCdrImpl signXmlDocument = new SignDocumentCdrImpl();
		signXmlDocument.signDocumento(documento);
		//validateSignature(documento);
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;
	}
	
	public void generarEDocumentCDR_SummaryResumen(Documento documento, EResumenDocumento eResumenDocumento) {
		EDocumentoCDR eDocumentoCDR = new EDocumentoCDR();			
		eDocumentoCDR.setFecha_recepcion_comprobante(documento.getFecRecepcion());
		eDocumentoCDR.setHora_recepcion_comprobante(documento.getFecRecepcion());
		eDocumentoCDR.setRuc_emisor_pse(String.valueOf(documento.getRucPseEmisor()));	
		
		ParseDocument2CDRDozer parseDocument2DataCDR = new ParseDocument2CDRDozerImpl();
		parseDocument2DataCDR.generarEDocumentCDR_SummaryResumen(documento, eDocumentoCDR, eResumenDocumento);	
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;
		
		SignDocumentCdrImpl signXmlDocument = new SignDocumentCdrImpl();
		signXmlDocument.signDocumento(documento);
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;
	}
	
	public void generarEDocumentCDR_SummaryReversion(Documento documento, EReversionDocumento eReversionDocumento) {
		EDocumentoCDR eDocumentoCDR = new EDocumentoCDR();			
		eDocumentoCDR.setFecha_recepcion_comprobante(documento.getFecRecepcion());
		eDocumentoCDR.setHora_recepcion_comprobante(documento.getFecRecepcion());
		eDocumentoCDR.setRuc_emisor_pse(String.valueOf(documento.getRucPseEmisor()));	
		
		ParseDocument2CDRDozer parseDocument2DataCDR = new ParseDocument2CDRDozerImpl();
		parseDocument2DataCDR.generarEDocumentCDR_SummaryReversion(documento, eDocumentoCDR, eReversionDocumento);	
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;

		SignDocumentCdrImpl signXmlDocument = new SignDocumentCdrImpl();
		signXmlDocument.signDocumento(documento);
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;
		
	}
	
	public void generarEDocumentCDR_Retencion(Documento documento, ERetencionDocumento eDocumento) {
		EDocumentoCDR eDocumentoCDR = new EDocumentoCDR();			
		eDocumentoCDR.setFecha_recepcion_comprobante(documento.getFecRecepcion());
		eDocumentoCDR.setHora_recepcion_comprobante(documento.getFecRecepcion());
		eDocumentoCDR.setRuc_emisor_pse(String.valueOf(documento.getRucPseEmisor()));		
		ParseDocument2CDRDozer parseDocument2DataCDR = new ParseDocument2CDRDozerImpl();
		parseDocument2DataCDR.generarEDocumentCDR_Retencion(documento, eDocumentoCDR, eDocumento);	
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;

		SignDocumentCdrImpl signXmlDocument = new SignDocumentCdrImpl();
		signXmlDocument.signDocumento(documento);
		//validateSignature(documento);
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;
	}
	
	public void generarEDocumentCDR_Percepcion(Documento documento, EPercepcionDocumento eDocumento) {
		EDocumentoCDR eDocumentoCDR = new EDocumentoCDR();			
		eDocumentoCDR.setFecha_recepcion_comprobante(documento.getFecRecepcion());
		eDocumentoCDR.setHora_recepcion_comprobante(documento.getFecRecepcion());
		eDocumentoCDR.setRuc_emisor_pse(String.valueOf(documento.getRucPseEmisor()));		
		ParseDocument2CDRDozer parseDocument2DataCDR = new ParseDocument2CDRDozerImpl();
		parseDocument2DataCDR.generarEDocumentCDR_Percepcion(documento, eDocumentoCDR, eDocumento);	
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;

		SignDocumentCdrImpl signXmlDocument = new SignDocumentCdrImpl();
		signXmlDocument.signDocumento(documento);
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;
	}
	
	public void generarEDocumentCDR_Guia(Documento documento, EGuiaDocumento eDocumento) {
		EDocumentoCDR eDocumentoCDR = new EDocumentoCDR();			
		eDocumentoCDR.setFecha_recepcion_comprobante(documento.getFecRecepcion());
		eDocumentoCDR.setHora_recepcion_comprobante(documento.getFecRecepcion());
		eDocumentoCDR.setRuc_emisor_pse(String.valueOf(documento.getRucPseEmisor()));		
		ParseDocument2CDRDozer parseDocument2DataCDR = new ParseDocument2CDRDozerImpl();
		parseDocument2DataCDR.generarEDocumentCDR_Guia(documento, eDocumentoCDR, eDocumento);	
		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;
		if( eDocumento.getIssueTime() != null && eDocumento.getIssueTime().length()<14) {
			CdrOseXmlRead cdrXmlRead = new CdrOseXmlRead();
			cdrXmlRead.upDateCDR(documento, eDocumento.getIssueTime(), "IssueTime");
		}
		
		SignDocumentCdrImpl signXmlDocument = new SignDocumentCdrImpl();
		signXmlDocument.signDocumento(documento);
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;
	}	
	
	private void validateSignature(Documento documento) {
		log.info("validateSignature ");
		try {
			File archivoXML = new File(Constantes.DIR_AXTEROID_OSE+Constantes.DIR_FILE+"/R-"+documento.getNombre());
			boolean validate = false;
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
			Document document = builder.parse(archivoXML);
			Node signatureNode = document.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature").item(0);
			DOMValidateContext domValidateContext = new DOMValidateContext(new X509KeySelector(), signatureNode);
			XMLSignatureFactory xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM");
			XMLSignature xmlSignature = xmlSignatureFactory.unmarshalXMLSignature(domValidateContext);
			validate = xmlSignature.validate(domValidateContext);
			log.info(" >>> Alterado: " + !validate);
			if (!validate) {
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_2334);
				documento.setErrorLogSunat("");
			}			
			
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			if(documento.getErrorNumero()!=null) {
				if(documento.getErrorNumero().isEmpty())
					documento.setErrorNumero(Constantes.SUNAT_ERROR_0305);
			}else
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0305);
			documento.setErrorLog(e.toString());
			documento.setErrorLogSunat("");
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);	
		}
	}

}
