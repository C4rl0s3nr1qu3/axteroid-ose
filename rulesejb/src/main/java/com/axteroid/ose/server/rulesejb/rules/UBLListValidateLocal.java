package com.axteroid.ose.server.rulesejb.rules;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;

public interface UBLListValidateLocal {
	public void reglasValidarDatosComprobante(Documento tbComprobante, EDocumento eDocumento);
	public void reglasValidarDatosNotaDebito(Documento tbComprobante, EDocumento eDocumento);
	public void reglasValidarDatosNotaCredito(Documento tbComprobante, EDocumento eDocumento);
	public void reglasValidarDatosComprobante_Retencion(Documento tbComprobante, ERetencionDocumento eDocumento);
	public void reglasValidarDatosComprobante_Percepcion(Documento tbComprobante, EPercepcionDocumento eDocumento);
	public void reglasValidarDatosComprobante_Guia(Documento tbComprobante, EGuiaDocumento eDocumento);
	public void reglasValidarDatos_SummaryComunicaBajas(Documento tbComprobante, EResumenDocumento eResumenDocumento);
	public void reglasValidarDatos_SummaryResumenDiario(Documento tbComprobante, EResumenDocumento eDocumento);
	public void reglasValidarDatos_SummaryReversion(Documento tbComprobante, EReversionDocumento eReversionDocumento);
	public void grabaTbComprobantesPagoElectronicosFBNCND(Documento tbComprobante, EDocumento eDocumento);
	public void grabaTbComprobantesPagoElectronicosRetencion(Documento tbComprobante, ERetencionDocumento eDocumento);
	public void grabaTbComprobantesPagoElectronicosPercepcion(Documento tbComprobante, EPercepcionDocumento eDocumento);
	public void grabaTbComprobantesPagoElectronicosGuia(Documento tbComprobante, EGuiaDocumento eDocumento);
	public void modificarEstadoComprobantesPagoElectronicos_RA(Documento tbComprobante, EResumenDocumento eDocumento);
	public void modificarEstadoComprobantesPagoElectronicos_RR(Documento tbComprobante, EReversionDocumento eDocumento);
	public void grabaTbComprobantesPagoElectronicos_RC(Documento tbComprobante, EResumenDocumento eDocumento);
	public void revisarRespuesta2RARCSunatList(Documento tbComprobante, EResumenDocumento eDocumento);
	public void revisarRespuesta2RRSunatList(Documento tbComprobante, EReversionDocumento eDocumento);
	public void revisarRespuesta2NCNDSunatList(Documento tbComprobante, EDocumento eDocumento);
	public boolean getDBRARCReview(Documento tbComprobante, EResumenDocumento eDocumento);
	public boolean getDBRRReview(Documento tbComprobante, EReversionDocumento eDocumento);
}
