package com.axteroid.ose.server.avatar.dao;

import java.sql.SQLException;
import java.util.List;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatComprobantesPagoElectronicos;

public interface SunatComprobantesPagoElectronicosDAO {	
	public List<SunatComprobantesPagoElectronicos> buscarSunatCompobantePagoDB(Documento tbComprobante, 
    		Long numRuc, String tipoDocRefPri, String nroDocRefPri);
	public List<SunatComprobantesPagoElectronicos> buscarSunatCompobantePagoDB(String ruc, 
			String tipo, String serie, String numero);
	public List<SunatComprobantesPagoElectronicos> buscarSunatcpemainDB(String ruc, 
			String tipo, String serie, Integer num) throws SQLException;
	public List<SunatComprobantesPagoElectronicos> buscarSunatcpepreoseDB(String ruc, 
			String tipo, String serie, Integer num) throws SQLException;
}
