package com.axteroid.ose.server.rulesejb.dao;

import javax.ejb.Local;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;

@Local
public interface SunatAutorizacionComprobPagoFisicoDAOLocal {
	 public void getAutorizacionComprobPagoFisico(Documento tbComprobante);
	 public void getAutorizacionComprobPagoFisico_FB(Documento tbComprobante);
	 public void getAutorizacionComprobPagoFisico(Documento tbComprobante, String tipoDocRefPri, 
			 String nroDocRefPri);
	 public void getAutorizacionComprobPagoFisico_RP(Documento tbComprobante, String tipoDocRefPri, 
			 String nroDocRefPri, Long numRUC);
	 public void getAutorizacionComprobPagoFisico_RC(Documento tbComprobante, EResumenDocumentoItem rdi);
	 public void getAutorizacionComprobPagoFisico_RP(Documento tbComprobante);
	 public void getAutorizacionComprobPagoFisico_RC_BR(Documento tbComprobante, String tipoDocRefPri, 
    		 String nroDocRefPri, Long numRUC);
	 public void getAutorizacionComprobPagoFisico_Anticipo(Documento tbComprobante, Long numDocumento, 
	     		String tipoDocRefPri, String nroDocRefPri);
}
