package com.axteroid.ose.server.rulesubl.builder.impl;

import java.util.Map;

import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoParametroEnum;

public class ReadXsdXsl {
	private static final Logger log = LoggerFactory.getLogger(ReadXsdXsl.class);
	public void service(Documento documento, Map<String, String> parametros) {
		log.info("TipoDocumento: {} ",documento.getTipoDocumento());
		switch (documento.getTipoDocumento()) {
			case Constantes.SUNAT_FACTURA: 	// FACTURA
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-Invoice-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/ValidaExprRegFactura-2.0.1.xsl");
				break;
			case Constantes.SUNAT_BOLETA: // BOLETA
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-Invoice-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/ValidaExprRegBoleta-2.0.1.xsl");
				break;
			case Constantes.SUNAT_NOTA_CREDITO: 	// NOTA DE CREDIT0
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-CreditNote-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/ValidaExprRegNC-2.0.1.xsl");
				break;
			case Constantes.SUNAT_NOTA_DEBITO:  // NOTA DE DEBITO
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-DebitNote-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/ValidaExprRegND-2.0.1.xsl");
				break;
			case Constantes.SUNAT_GUIA_REMISION_REMITENTE:  // GUIA REMISION REMITENTE
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-DespatchAdvice-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/ValidaExprRegGreRemitente-2.0.1.xsl");
				break;
			case Constantes.SUNAT_RETENCION:  // RETENCIONES
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_0/maindoc/UBLPE-Retention-1.0.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/ValidaExprRegRetencion-1.0.3.xsl");
				break;
			case Constantes.SUNAT_ROL_ADQUIRIENTE_SISTEMAS_PAGO : 	// ROL ADQUIRIENTE
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-Invoice-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/ValidaExprRegAdquiriente-2.0.1.xsl");
				break;	
			case Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA:  // GUIA REMISION TRANSPORTISTA
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-DespatchAdvice-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/ValidaExprRegGreTransportista-2.0.1.xsl");
				break;				
			case Constantes.SUNAT_OPERADOR : 	// OPERADOR
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-Invoice-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/ValidaExprRegOperador-2.0.1.xsl");
				break;	
			case Constantes.SUNAT_PERCEPCION:  // PERCEPCIONES
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_0/maindoc/UBLPE-Perception-1.0.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/ValidaExprRegPercepcion-1.0.2.xsl");
				break;
			case Constantes.SUNAT_ADQUIRIENTE_SISTEMAS_PAGO : 	// ADQUIRIENTE
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-Invoice-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/ValidaExprRegAdquiriente-2.0.1.xsl");
				break;					
			case Constantes.SUNAT_RESUMEN_DIARIO:  // RESUMEN DIARIO
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_0/maindoc/UBLPE-SummaryDocuments-1.0.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/ValidaExprRegSummary-1.1.0.xsl");
				break;
			case Constantes.SUNAT_COMUNICACION_BAJAS:  // COMUNICACION DE BAJA
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_0/maindoc/UBLPE-VoidedDocuments-1.0.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/ValidaExprRegSummaryVoided-1.0.1.xsl");
				break;
			case Constantes.SUNAT_REVERSION:  // RESUMEN DE REVERSIONES
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_0/maindoc/UBLPE-VoidedDocuments-1.0.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/ValidaExprRegOtrosVoided-1.0.1.xsl");
				break;					
			default :
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_1004);
				return;
		}
	}

	public void serviceBeta(Documento documento, Map<String, String> parametros, String version) {
		log.info("version {} | TipoDocumento: {} ",version,documento.getTipoDocumento());
		switch (documento.getTipoDocumento()) {
			case Constantes.SUNAT_FACTURA: 	// FACTURA
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-Invoice-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/"+version+"/ValidaExprRegFactura-2.0.1.xsl");
				break;
			case Constantes.SUNAT_BOLETA: // BOLETA
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-Invoice-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/"+version+"/ValidaExprRegBoleta-2.0.1.xsl");
				break;				
			case Constantes.SUNAT_NOTA_CREDITO: 	// NOTA DE CREDIT0
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-CreditNote-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/"+version+"/ValidaExprRegNC-2.0.1.xsl");
				break;				
			case Constantes.SUNAT_NOTA_DEBITO:  // NOTA DE DEBITO
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-DebitNote-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/"+version+"/ValidaExprRegND-2.0.1.xsl");
				break;
			case Constantes.SUNAT_GUIA_REMISION_REMITENTE:  // GUIA REMISION REMITENTE
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-DespatchAdvice-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/"+version+"/ValidaExprRegGreRemitente-2.0.1.xsl");
				break;
			case Constantes.SUNAT_RETENCION:  // RETENCIONES
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_0/maindoc/UBLPE-Retention-1.0.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/"+version+"/ValidaExprRegRetencion-1.0.3.xsl");
				break;
			case Constantes.SUNAT_ROL_ADQUIRIENTE_SISTEMAS_PAGO : 	// ROL ADQUIRIENTE
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-Invoice-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/"+version+"/ValidaExprRegAdquiriente-2.0.1.xsl");
				break;		
			case Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA:  // GUIA REMISION TRANSPORTISTA
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-DespatchAdvice-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/"+version+"/ValidaExprRegGreTransportista-2.0.1.xsl");
				break;					
			case Constantes.SUNAT_OPERADOR : 	// OPERADOR
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-Invoice-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/"+version+"/ValidaExprRegOperador-2.0.1.xsl");
				break;					
			case Constantes.SUNAT_PERCEPCION:  // PERCEPCIONES
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_0/maindoc/UBLPE-Perception-1.0.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/"+version+"/ValidaExprRegPercepcion-1.0.2.xsl");
				break;
			case Constantes.SUNAT_ADQUIRIENTE_SISTEMAS_PAGO : 	// ADQUIRIENTE
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_1/maindoc/UBL-Invoice-2.1.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/"+version+"/ValidaExprRegAdquiriente-2.0.1.xsl");
				break;					
			case Constantes.SUNAT_RESUMEN_DIARIO:  // RESUMEN DIARIO
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_0/maindoc/UBLPE-SummaryDocuments-1.0.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/"+version+"/ValidaExprRegSummary-1.1.0.xsl");
				break;
			case Constantes.SUNAT_COMUNICACION_BAJAS:  // COMUNICACION DE BAJA
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_0/maindoc/UBLPE-VoidedDocuments-1.0.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/"+version+"/ValidaExprRegSummaryVoided-1.0.1.xsl");
				break;
			case Constantes.SUNAT_REVERSION:  // RESUMEN DE REVERSIONES
				parametros.put(TipoParametroEnum.XSD.getCodigo(), "META-INF/xsd/XSD_UBL_2_0/maindoc/UBLPE-VoidedDocuments-1.0.xsd");
				parametros.put(TipoParametroEnum.XSL.getCodigo(), "META-INF/xsl/"+version+"/ValidaExprRegOtrosVoided-1.0.1.xsl");
				break;					
			default :
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_1004);
				return;
		}
	}
}
