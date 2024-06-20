package com.axteroid.ose.server.rulesjdbc.dao;

import java.sql.SQLException;

import com.axteroid.ose.server.jpa.model.Documento;

public interface DocumentoPGDao {
	public void updateErrorLog(com.axteroid.ose.server.jpa.model.Documento tbComprobante) throws SQLException;
	public void updateRARCRR() throws SQLException;
	public void updateUblEstErLog(Documento tbComprobante) throws SQLException;
	public void updateUblCdr(Documento tbComprobante) throws SQLException;
	public void updateUblCdrMensajeSunat(Documento tbComprobante) throws SQLException;
	public void updateRespuestaSunat(Documento tbComprobante) throws SQLException;
	public void updateEstadoRespuestaLogSunat(Documento tbComprobante) throws SQLException;
	public void updateRespuestaMensajeLogSunat(Documento tbComprobante) throws SQLException;
	public void updateFileUBLOse(Documento tbComprobante) throws SQLException;
	public void updateFileCDROse(Documento tbComprobante) throws SQLException;
	public void updateFileMensajeSunat(Documento tbComprobante) throws SQLException;
	public void updateError(Documento tbComprobante) throws SQLException;
	public void updateEstado(Documento tbComprobante) throws SQLException;
}
