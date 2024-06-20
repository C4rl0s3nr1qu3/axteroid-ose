package com.axteroid.ose.server.rulesubl.dozer;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EDocumentoCDR;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;


public interface ParseDocument2CDRDozer {
	public void generarEDocumentCDR(Documento tbComprobante, EDocumentoCDR eDocumentoCDR, 
			EDocumento eDocumento);
	public void document2TbComprobante(Documento tbComprobante, EDocumento eDocumento);
	public void generarEDocumentCDR_Retencion(Documento tbComprobante, EDocumentoCDR eDocumentoCDR, 
			ERetencionDocumento eDocumento);
	public void document2TbComprobante_Retencion(Documento tbComprobante, ERetencionDocumento eDocumento);
	public void generarEDocumentCDR_Percepcion(Documento tbComprobante, EDocumentoCDR eDocumentoCDR, 
			EPercepcionDocumento eDocumento);
	public void document2TbComprobante_Percepcion(Documento tbComprobante, EPercepcionDocumento eDocumento);
	public void generarEDocumentCDR_Guia(Documento tbComprobante, EDocumentoCDR eDocumentoCDR, 
			EGuiaDocumento eDocumento);
	public void document2TbComprobante_Guia(Documento tbComprobante, EGuiaDocumento eDocumento);
	public void document2TbComprobante_SummaryResumen(Documento tbComprobante, EResumenDocumento eResumenDocumento);
	public void generarEDocumentCDR_SummaryResumen(Documento tbComprobante, EDocumentoCDR eDocumentoCDR, 
			EResumenDocumento eDocumento);
	public void document2TbComprobante_SummaryReversion(Documento tbComprobante, EReversionDocumento eReversionDocumento);
	public void generarEDocumentCDR_SummaryReversion(Documento tbComprobante, EDocumentoCDR eDocumentoCDR, 
			EReversionDocumento eDocumento);
}
