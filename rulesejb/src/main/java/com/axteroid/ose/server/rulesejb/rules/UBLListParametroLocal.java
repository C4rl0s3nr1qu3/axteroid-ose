package com.axteroid.ose.server.rulesejb.rules;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;

public interface UBLListParametroLocal {
	public void reglasValidarParametrosComprobante(Documento tbComprobante, EDocumento eDocumento);
	public void reglasValidarParametrosResumenDiario(Documento tbComprobante, EResumenDocumento eDocumento);
	public void reglasValidarParametrosRetencion(Documento tbComprobante, ERetencionDocumento eDocumento);
	public void reglasValidarParametrosPercepcion(Documento tbComprobante, EPercepcionDocumento eDocumento);
	public void reglasValidarParametrosGuia(Documento tbComprobante, EGuiaDocumento eDocumento);
	public String bucarParametro(String codParametro, String codArgumento);
	public String bucarLoginParametro(String codArgumento) ;
	public String buscarParametroCodArgumento(String codParametro, String desArgumento);
}
