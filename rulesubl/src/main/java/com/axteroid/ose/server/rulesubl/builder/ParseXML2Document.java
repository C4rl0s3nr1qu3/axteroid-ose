package com.axteroid.ose.server.rulesubl.builder;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;

public interface ParseXML2Document {
	public EDocumento parseUBLSendBill(Documento tbComprobante);
	public ERetencionDocumento parseUBLSendBill_Retencion(Documento tbComprobante);
	public EPercepcionDocumento parseUBLSendBill_Percepcion(Documento tbComprobante);
	public EGuiaDocumento parseUBLSendBill_Guia(Documento tbComprobante);
	public EResumenDocumento parseUBLSend_SummaryResumen(Documento tbComprobante);
	public EReversionDocumento parseUBLSend_SummaryReversion(Documento tbComprobante);
	public EDocumento parseUBLPortalOse(Documento tbComprobante);
	public Object parseUBLOse(Documento tbComprobante);
}
