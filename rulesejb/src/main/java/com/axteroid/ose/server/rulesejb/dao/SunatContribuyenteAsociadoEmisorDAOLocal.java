package com.axteroid.ose.server.rulesejb.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatContribuyenteAsociadoEmisor;

@Local
public interface SunatContribuyenteAsociadoEmisorDAOLocal {
	public void getContribuyenteAsociado(Documento tbComprobante, Date fechaEmisionComprobante);
	public void getContribuyentePSEEmisor(Documento tbComprobante, Date fechaEmisionComprobante);
	public List<SunatContribuyenteAsociadoEmisor> getJoinPSEEmisor(Documento documento, int indicador, Long rucPSE);
}
