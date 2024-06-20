package com.axteroid.ose.server.rulesejb.dao;

import javax.ejb.Local;

import com.axteroid.ose.server.jpa.model.Documento;

@Local
public interface SunatContribuyenteDAOLocal {
	 public void getContribuyente(Documento tbComprobante);
	 public void getBFAdquiriente(Documento tbComprobante, Long documentoAdquiriente);
	 public void getBFEmisorAnticipo(Documento tbComprobante, Long documentoEmisorAnticipo);
	 public void getContribuyente_RP(Documento tbComprobante);
	 public void getRetencionProveedor(Documento tbComprobante, Long documentoProveedor);
	 public void getPercepcionCliente(Documento tbComprobante, Long documentoCliente);
	 public void getGuiaRemitente(Documento tbComprobante, Long documentoRemitente);
	 public void getGuiaProveedor(Documento tbComprobante, Long documentoProveedor);
	 public void getDAEParticipe(Documento tbComprobante, Long rucParticipe);
	 public void getContribuyenteComunicaBajas(Documento tbComprobante);
}
