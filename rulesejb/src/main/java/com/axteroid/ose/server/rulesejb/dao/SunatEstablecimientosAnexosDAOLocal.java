package com.axteroid.ose.server.rulesejb.dao;

import javax.ejb.Local;

import com.axteroid.ose.server.jpa.model.Documento;

@Local
public interface SunatEstablecimientosAnexosDAOLocal {
	public void getEstablecimientosAnexos(Documento tbComprobante, String codEstab,
			String tipoDocumentoMdifica);
}
