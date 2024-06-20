package com.axteroid.ose.server.rulesejb.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatParametro;

@Local
public interface SunatParametroDAOLocal {
	
	public String getParametro(Documento tbComprobante, String codParametro, String codArgumento);
	public boolean getParametroErrorFechas(String dias, Date fechaRecep, Date fechaEmision);
	public boolean getParametroErrorFechaAmplSunat(Date currentdate, Date cbcIssueDate);
	public boolean getParametroNoExiste(Documento tbComprobante, String codParametro, String codArgumento);
	public String getParametroString(Documento tbComprobante, String codParametro, String codArgumento);		
	public List<SunatParametro> listarTsParametro();
	public String bucarParametro(String codParametro, String codArgumento);
	public String bucarLoginParametro(String codArgumento);
	public List<SunatParametro> buscarParametroCodArgumento(String codParametro, String desArgumento);
}
