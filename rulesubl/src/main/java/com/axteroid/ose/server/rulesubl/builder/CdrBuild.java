package com.axteroid.ose.server.rulesubl.builder;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;

public interface CdrBuild {

	public void generarEDocumentCDR(Documento tbComprobante, EDocumento eDocumento) ;
	public void generarEDocumentCDR_SummaryResumen(Documento tbComprobante,  
			EResumenDocumento eResumenDocumento);
	public void generarEDocumentCDR_SummaryReversion(Documento tbComprobante,  
			EReversionDocumento eReversionDocumento) ;
	public void generarEDocumentCDR_Retencion(Documento tbComprobante,  
			ERetencionDocumento eDocumento);
	public void generarEDocumentCDR_Percepcion(Documento tbComprobante,  
			EPercepcionDocumento eDocumento);
	public void generarEDocumentCDR_Guia(Documento tbComprobante,  
			EGuiaDocumento eDocumento);
}
