package com.axteroid.ose.server.avatar.build;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.task.DetalleResumenesCrud;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesubl.builder.ParseXML2Document;
import com.axteroid.ose.server.rulesubl.builder.impl.ParseXml2DocumentImpl;
import com.axteroid.ose.server.tools.constantes.TipoDocumentoSUNATEnum;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.tools.util.DocumentUtil;

public class FacturaOseBuild {
	private static final Logger log = LoggerFactory.getLogger(FacturaOseBuild.class);
	private Map<String, Timestamp> mapTime = new HashMap<String, Timestamp>();
	
	public void sendDocumento(Documento documento) {
		//log.info("0) documento: "+documento.getNombre()+" - tBD: "+tBD);
		mapTime.put("Start", new Timestamp(System.currentTimeMillis()));
		switch(documento.getTipoDocumento()){
			case "01":
				this.getDocument_FBNCND(documento);
				break;
			case "03":
				this.getDocument_FBNCND(documento);
				break;
			case "07":
				this.getDocument_FBNCND(documento);
				break;
			case "08":
				this.getDocument_FBNCND(documento);
				break;
			case "09":
				this.getDocument_Guia(documento);
				break;
			case "14":
				this.getDocument_FBNCND(documento);
				break;
			case "20":
				this.getDocument_Retencion(documento);
				break;
			case "40":
				this.getDocument_Percepcion(documento);
				break;
			case "RA":
				this.getDocument_SummaryResumen(documento);
				break;
			case "RC":
				this.getDocument_SummaryResumen(documento);
				break;
			case "RR":
				this.getDocument_SummaryReversion(documento);
				break;
		}
	}
	
	public void getDocument_FBNCND(Documento documento){	
		mapTime.put("StartBuild", new Timestamp(System.currentTimeMillis()));
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		EDocumento eDocumento = parseXML2Document.parseUBLPortalOse(documento);	
		mapTime.put("ParseDocument", new Timestamp(System.currentTimeMillis()));			
		this.getDocument_CPE_ALL(documento, eDocumento);
		mapTime.put("End", new Timestamp(System.currentTimeMillis()));		
		getStatisticsFactura(mapTime);
	}
	
	public void getDocument_Retencion(Documento documento){
		mapTime.put("StartBuild", new Timestamp(System.currentTimeMillis()));	
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		ERetencionDocumento eDocumento = parseXML2Document.parseUBLSendBill_Retencion(documento);
		mapTime.put("ParseDocument", new Timestamp(System.currentTimeMillis()));
		this.getDocument_CPE_ALL(documento, eDocumento);
		mapTime.put("End", new Timestamp(System.currentTimeMillis()));
		getStatisticsFactura(mapTime);
	}
	
	public void getDocument_Percepcion(Documento documento){
		mapTime.put("StartBuild", new Timestamp(System.currentTimeMillis()));
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		EPercepcionDocumento eDocumento = parseXML2Document.parseUBLSendBill_Percepcion(documento);	
		mapTime.put("ParseDocument", new Timestamp(System.currentTimeMillis()));
		this.getDocument_CPE_ALL(documento, eDocumento);
		mapTime.put("End", new Timestamp(System.currentTimeMillis()));		
		getStatisticsFactura(mapTime);
	}
	
	public void getDocument_Guia(Documento documento){
		mapTime.put("StartBuild", new Timestamp(System.currentTimeMillis()));
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		EGuiaDocumento eDocumento = parseXML2Document.parseUBLSendBill_Guia(documento);	
		mapTime.put("ParseDocument", new Timestamp(System.currentTimeMillis()));
		this.getDocument_CPE_ALL(documento, eDocumento);
		mapTime.put("End", new Timestamp(System.currentTimeMillis()));
		getStatisticsFactura(mapTime);
	}
	
	public void getDocument_SummaryResumen(Documento documento){
		mapTime.put("StartBuild", new Timestamp(System.currentTimeMillis()));
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		EResumenDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryResumen(documento);	
		mapTime.put("ParseDocument", new Timestamp(System.currentTimeMillis()));
		this.getDocument_CPE_ALL(documento, eDocumento);
		mapTime.put("End", new Timestamp(System.currentTimeMillis()));
		getStatisticsFactura(mapTime);
	}
	
	public void getDocument_SummaryReversion(Documento documento){
		mapTime.put("StartBuild", new Timestamp(System.currentTimeMillis()));
		ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
		EReversionDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryReversion(documento);	
		mapTime.put("ParseDocument", new Timestamp(System.currentTimeMillis()));
		this.getDocument_CPE_ALL(documento, eDocumento);
		mapTime.put("End", new Timestamp(System.currentTimeMillis()));
		getStatisticsFactura(mapTime);
	}
	
	public void getDocument_CPE_ALL(Documento documento, Object oDocumento){					
		if(documento.getTipoDocumento().equals(TipoDocumentoSUNATEnum.FACTURA.getCodigo()) ||
				documento.getTipoDocumento().equals(TipoDocumentoSUNATEnum.BOLETA.getCodigo()) ||
				documento.getTipoDocumento().equals(TipoDocumentoSUNATEnum.NOTA_CREDITO.getCodigo()) || 
				documento.getTipoDocumento().equals(TipoDocumentoSUNATEnum.NOTA_DEBITO.getCodigo())  ||
				documento.getTipoDocumento().equals(TipoDocumentoSUNATEnum.GUIA_REMISION.getCodigo()) ||
				documento.getTipoDocumento().equals(TipoDocumentoSUNATEnum.RECIBO_SERV_PUBL.getCodigo()) ||
				documento.getTipoDocumento().equals(TipoDocumentoSUNATEnum.DAE_ROL_ADQUIRIENTE_SISTEMAS_PAGO.getCodigo())  ||
				documento.getTipoDocumento().equals(TipoDocumentoSUNATEnum.DAE_OPERADOR.getCodigo()) ||
				documento.getTipoDocumento().equals(TipoDocumentoSUNATEnum.DAE_ADQUIRIENTE_SISTEMAS_PAGO.getCodigo()) ||
				documento.getTipoDocumento().equals(TipoDocumentoSUNATEnum.RETENCION.getCodigo()) ||
				documento.getTipoDocumento().equals(TipoDocumentoSUNATEnum.PERCEPCION.getCodigo())) 	{
			EDocumento eDocumento = (EDocumento) oDocumento;
			DetalleResumenesCrud detalleResumenesCrud = new DetalleResumenesCrud();	
			detalleResumenesCrud.cargaDetalleDocumento(documento, eDocumento);
		}
		if(documento.getTipoDocumento().equals(TipoDocumentoSUNATEnum.COMUNICACION_BAJAS.getCodigo()) ||
				documento.getTipoDocumento().equals(TipoDocumentoSUNATEnum.RESUMEN_DIARIO.getCodigo())) {
			EResumenDocumento eDocumento = (EResumenDocumento) oDocumento;
			DetalleResumenesCrud detalleResumenesCrud = new DetalleResumenesCrud();		
			detalleResumenesCrud.cargaListaDetalleResumenes_RA_RC(documento, eDocumento);		
		}
		
		if(documento.getTipoDocumento().equals(TipoDocumentoSUNATEnum.REVERSION.getCodigo())) {
			EReversionDocumento eDocumento = (EReversionDocumento) oDocumento;
			DetalleResumenesCrud detalleResumenesCrud = new DetalleResumenesCrud();	
			detalleResumenesCrud.cargaListaDetalleResumenes_RR(documento, eDocumento);
		}				
	}	
	
    public static void getStatisticsFactura(Map<String, Timestamp> mapTime) {
		log.info("********************************************************************");
		log.info(" StartBuild        "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("StartBuild"), mapTime.get("Start")));
		log.info(" ParseDocument     "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("ParseDocument"), mapTime.get("StartBuild")));
		log.info(" CRUDCPE           "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("End"), mapTime.get("ParseDocument")));	
		log.info("********************************************************************");
		log.info(" Total             "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("End"), mapTime.get("Start")));
		log.info("********************************************************************");
	}
}
