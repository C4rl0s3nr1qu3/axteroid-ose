package com.axteroid.ose.server.avatar.dao;

import java.sql.SQLException;

import com.axteroid.ose.server.jpa.model.Documento;

public interface DocumentoPGDao {
	
	public void updateDocumentoAjusteGeneral() throws SQLException;
	public void updateDocumentoEstado30() throws SQLException;
	
	public void updateErrorLog(com.axteroid.ose.server.jpa.model.Documento tbComprobante) throws SQLException;
	public void updateRARCRR() throws SQLException;
	public void updateUblEstErLog(Documento tbComprobante) throws SQLException;
	public void updateUblCdr(Documento tbComprobante) throws SQLException;
	public void updateUblCdrMensajeSunat(Documento tbComprobante) throws SQLException;
	public void updateRespuestaSunat(Documento tbComprobante) throws SQLException;
	public void updateEstadoRespuestaLogSunat(Documento tbComprobante) throws SQLException;
	public void updateRespuestaMensajeLogSunat(Documento tbComprobante) throws SQLException;
	public void updateEstadoRespuestaLogSunatQA(Documento tbComprobante) throws SQLException;
	public void updateFileUBLOseServer(Documento tbComprobante) throws SQLException;
	public void updateFileCDROseServer(Documento tbComprobante) throws SQLException;
	public void updateFileMensajeSunat(Documento tbComprobante) throws SQLException;
	public void updateError(Documento tbComprobante) throws SQLException;
	public void updateEstado(Documento tbComprobante) throws SQLException;
	public void updateIdContent(Documento tbComprobante) throws SQLException;
		
	public void updateTbComprobanteAjusteGeneral() throws SQLException;
	public void updateTbComprobanteAjusteEstado() throws SQLException;
	public void updateTbComprobanteNoRC() throws SQLException;
	public void updateTbComprobanteNoRARCRR() throws SQLException;
	public void updateTbComprobanteRARR() throws SQLException;
	public void updateTbComprobanteRCFirst(String sAyer) throws SQLException;
	public void updateTbComprobanteRCLast(String sAyer) throws SQLException;
	public void updateTbComprobanteResumenErrorEstado() throws SQLException;
	
	public void updateTbComprobanteRARCRR() throws SQLException;
	public void updateTbComprobanteResumenEELast(String sAyer) throws SQLException;
	public void updateTbComprobanteResumenEEFirst(String sAyer) throws SQLException;
	public void updateTbComprobanteResumenEERUC() throws SQLException;
}
