package com.axteroid.ose.server.builder.build;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesejb.rules.OseDBCRUDLocal;
import com.axteroid.ose.server.rulesejb.rules.UBLListValidateLocal;
import com.axteroid.ose.server.rulesejb.rules.impl.UBLListValidateImpl;
import com.axteroid.ose.server.rulesubl.builder.ParseXML2Document;
import com.axteroid.ose.server.rulesubl.builder.impl.ParseXml2DocumentImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.tools.util.DocumentUtil;

public class UpdateCpeBuild {
	//private static final Logger log = Logger.getLogger(UpdateCpeBuild.class);
	private static final Logger log = LoggerFactory.getLogger(UpdateCpeBuild.class);
	
	private Map<String, Timestamp> mapTime = new HashMap<String, Timestamp>();
	private ParseXML2Document parseXML2Document = new ParseXml2DocumentImpl();
	private UBLListValidateLocal ublListValidateLocal = new UBLListValidateImpl();	
	
	public void getBuilding(String id) {
		mapTime.put("Start", new Timestamp(System.currentTimeMillis()));
		try {
			Documento tb = new Documento();
			tb.setId(Long.parseLong(id));
			OseDBCRUDLocal oseDBCRUDLocal = 
					(OseDBCRUDLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"OseDBCRUDImpl"+Constantes.OSE_CDEJB_RULES+"OseDBCRUDLocal");						
			Documento tbComprobante = oseDBCRUDLocal.buscarTbComprobanteXID(tb);							
			if(!(tbComprobante!=null) || !(tbComprobante.getId()!=null))
				return;
			this.buildComprobante(tbComprobante);												
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getBuildingCPE \n"+errors);
		}
	}
	
	public void buildComprobante(Documento tbComprobante) {
		log.info("0) tbComprobante: "+tbComprobante.getNombre());
		switch(tbComprobante.getTipoDocumento()){
			case "01":
				this.getDocument_FBNCND(tbComprobante);
				break;
			case "03":
				this.getDocument_FBNCND(tbComprobante);
				break;
			case "07":
				this.getDocument_FBNCND(tbComprobante);
				break;
			case "08":
				this.getDocument_FBNCND(tbComprobante);
				break;
			case "09":
				this.getDocument_Guia(tbComprobante);
				break;
			case "14":
				this.getDocument_FBNCND(tbComprobante);
				break;
			case "20":
				this.getDocument_Retencion(tbComprobante);
				break;
			case "40":
				this.getDocument_Percepcion(tbComprobante);
				break;
			case "RA":
				this.getDocument_SummaryResumen(tbComprobante);
				break;
			case "RC":
				this.getDocument_SummaryResumen(tbComprobante);
				break;
			case "RR":
				this.getDocument_SummaryReversion(tbComprobante);
				break;
		}
	}
	
	public void getDocument_FBNCND(Documento tbComprobante){	
		mapTime.put("GetUbl", new Timestamp(System.currentTimeMillis()));		
		EDocumento eDocumento = parseXML2Document.parseUBLPortalOse(tbComprobante);	
		mapTime.put("GetUblCPE", new Timestamp(System.currentTimeMillis()));			
		ublListValidateLocal.grabaTbComprobantesPagoElectronicosFBNCND(tbComprobante, eDocumento);
		mapTime.put("End", new Timestamp(System.currentTimeMillis()));		
		DocumentUtil.getStatisticsFactura(mapTime);
	}

	public void getDocument_Guia(Documento tbComprobante){
		mapTime.put("GetUbl", new Timestamp(System.currentTimeMillis()));
		EGuiaDocumento eDocumento = parseXML2Document.parseUBLSendBill_Guia(tbComprobante);	
		mapTime.put("GetUblCPE", new Timestamp(System.currentTimeMillis()));
		ublListValidateLocal.grabaTbComprobantesPagoElectronicosGuia(tbComprobante, eDocumento);
		mapTime.put("End", new Timestamp(System.currentTimeMillis()));
		DocumentUtil.getStatisticsFactura(mapTime);
	}	
	
	public void getDocument_Retencion(Documento tbComprobante){
		mapTime.put("GetUbl", new Timestamp(System.currentTimeMillis()));
		ERetencionDocumento eDocumento = parseXML2Document.parseUBLSendBill_Retencion(tbComprobante);
		mapTime.put("GetUblCPE", new Timestamp(System.currentTimeMillis()));
		ublListValidateLocal.grabaTbComprobantesPagoElectronicosRetencion(tbComprobante, eDocumento);
		mapTime.put("End", new Timestamp(System.currentTimeMillis()));
		DocumentUtil.getStatisticsFactura(mapTime);
	}
	
	public void getDocument_Percepcion(Documento tbComprobante){
		mapTime.put("GetUbl", new Timestamp(System.currentTimeMillis()));
		EPercepcionDocumento eDocumento = parseXML2Document.parseUBLSendBill_Percepcion(tbComprobante);	
		mapTime.put("GetUblCPE", new Timestamp(System.currentTimeMillis()));
		ublListValidateLocal.grabaTbComprobantesPagoElectronicosPercepcion(tbComprobante, eDocumento);
		mapTime.put("End", new Timestamp(System.currentTimeMillis()));		
		DocumentUtil.getStatisticsFactura(mapTime);
	}
	
	public void getDocument_SummaryResumen(Documento tbComprobante){
		mapTime.put("GetUbl", new Timestamp(System.currentTimeMillis()));
		EResumenDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryResumen(tbComprobante);	
		mapTime.put("GetUblCPE", new Timestamp(System.currentTimeMillis()));
		ublListValidateLocal.modificarEstadoComprobantesPagoElectronicos_RA(tbComprobante, eDocumento);		
		ublListValidateLocal.grabaTbComprobantesPagoElectronicos_RC(tbComprobante, eDocumento);	
		mapTime.put("End", new Timestamp(System.currentTimeMillis()));
		DocumentUtil.getStatisticsFactura(mapTime);
	}
	
	public void getDocument_SummaryReversion(Documento tbComprobante){
		mapTime.put("GetUbl", new Timestamp(System.currentTimeMillis()));
		EReversionDocumento eDocumento = parseXML2Document.parseUBLSend_SummaryReversion(tbComprobante);	
		mapTime.put("GetUblCPE", new Timestamp(System.currentTimeMillis()));
		ublListValidateLocal.modificarEstadoComprobantesPagoElectronicos_RR(tbComprobante, eDocumento);
		mapTime.put("End", new Timestamp(System.currentTimeMillis()));
		DocumentUtil.getStatisticsFactura(mapTime);
	}
	
}
