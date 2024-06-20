package com.axteroid.ose.server.repository.rules;

import java.sql.Timestamp;
import java.util.Map;

import org.bson.Document;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;

public interface OseResponseCRUDLocal {
	public void grabarOseResponse(Document document);
	public void grabarTsStress(Documento tbComprobante, Map<String, Timestamp> mapTime);
	public void getDocument_FBNCND(Documento tbComprobante, EDocumento eDocumento, Document document);
	public void getDocument_Retencion(Documento tbComprobante, ERetencionDocumento eDocumento, Document document);
	public void getDocument_Percepcion(Documento tbComprobante, EPercepcionDocumento eDocumento, Document document);
	public void getDocument_Guia(Documento tbComprobante, EGuiaDocumento eDocumento, Document document);
	public void getDocument_SummaryResumen(Documento tbComprobante, EResumenDocumento eDocumento, Document document);
	public void getDocument_SummaryReversion(Documento tbComprobante, EReversionDocumento eDocumento, Document document);
}
