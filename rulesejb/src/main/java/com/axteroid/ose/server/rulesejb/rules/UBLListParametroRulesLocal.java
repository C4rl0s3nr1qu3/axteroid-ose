package com.axteroid.ose.server.rulesejb.rules;

import javax.ejb.Local;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;

@Local
public interface UBLListParametroRulesLocal {
	public void reglasValidarParametrosFacturaBoleta(Documento tbComprobante, EDocumento eDocumento);
	public void reglasValidarParametrosReciboServiciosPublicos(Documento tbComprobante, EDocumento eDocumento);
	public void reglasValidarParametrosNotaCredito(Documento tbComprobante, EDocumento eDocumento);
	public void reglasValidarParametrosNotaDebito(Documento tbComprobante, EDocumento eDocumento);
	public void reglasValidarParametrosDAEAdquiriente(Documento tbComprobante, EDocumento eDocumento);
	public void reglasValidarParametrosResumenDiario(Documento tbComprobante, EResumenDocumento eDocumento);
	public void reglasValidarParametrosRetencion(Documento tbComprobante, ERetencionDocumento eDocumento);
	public void reglasValidarParametrosPercepcion(Documento tbComprobante, EPercepcionDocumento eDocumento);
	public void reglasValidarParametrosGuia(Documento tbComprobante, EGuiaDocumento eDocumento);
	public String bucarParametro(String codParametro,String codArgumento);	
	public String bucarLoginParametro(String codArgumento);
	
}
