package com.axteroid.ose.server.avatar.dao;

import java.sql.SQLException;
import java.util.List;

import com.axteroid.ose.server.avatar.bean.TmDetalleResumenes;
import com.axteroid.ose.server.jpa.model.Documento;

public interface SunatDetalleResumenesDao {
	public void insertDetalleResumenes(TmDetalleResumenes tmDetalleResumenes) throws SQLException;
	public void insertListaDetalleResumenes(List<TmDetalleResumenes> listTmDetalleResumenes) throws SQLException;
	public List<String> getListDetalleResumenTDSSShort(Documento tbComprobante) throws SQLException;
}
