package com.axteroid.ose.server.rulesejb.dao;

import javax.ejb.Local;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;

@Local
public interface SunatAutorizacionRangosContingenciaDAOLocal {
	 public void getAutorizacionRangosContingencia_RC(Documento tbComprobante, EResumenDocumentoItem rdi);
	 public void getAutorizacionRangosContingencia_RP(Documento tbComprobante);
	 public void getAutorizacionRangosContingencia(Documento tbComprobante);
}
