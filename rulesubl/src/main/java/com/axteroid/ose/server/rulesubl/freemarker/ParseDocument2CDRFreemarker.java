package com.axteroid.ose.server.rulesubl.freemarker;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EDocumentoCDR;

public interface ParseDocument2CDRFreemarker {
	public void generarEDocumentCDR(Documento tbComprobante, EDocumentoCDR eDocumentoCDR, 
			EDocumento eDocumento);
}
