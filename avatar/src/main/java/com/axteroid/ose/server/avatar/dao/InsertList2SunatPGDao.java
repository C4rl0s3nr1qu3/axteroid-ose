package com.axteroid.ose.server.avatar.dao;

import java.sql.SQLException;
import java.util.List;

import com.axteroid.ose.server.jpa.model.ComprobantesPagoElectronicos;
import com.axteroid.ose.server.jpa.model.SunatAutorizacionComprobPagoFisico;
import com.axteroid.ose.server.jpa.model.SunatCertificadoEmisor;
import com.axteroid.ose.server.jpa.model.SunatComprobantesPagoElectronicos;
import com.axteroid.ose.server.jpa.model.SunatContribuyente;
import com.axteroid.ose.server.jpa.model.SunatContribuyenteAsociadoEmisor;
import com.axteroid.ose.server.jpa.model.SunatCuadreDiarioDetalle;
import com.axteroid.ose.server.jpa.model.SunatPadronContribuyente;
import com.axteroid.ose.server.jpa.model.SunatParametro;
import com.axteroid.ose.server.jpa.model.SunatPlazosExcepcionales;
import com.axteroid.ose.server.tools.bean.Homologa;

public interface InsertList2SunatPGDao {
	void insertSunatContribuyente(SunatContribuyente sunatContribuyente) throws SQLException;

	void insertSunatPadronContribuyente(SunatPadronContribuyente sunatPadronContribuyente) throws SQLException;

	void insertSunatContribuyenteEmisor(SunatContribuyenteAsociadoEmisor sunatContribuyenteAsociadoEmisor) throws SQLException;

	void insertSunatCertificadoEmisor(SunatCertificadoEmisor sunatCertificadoEmisor) throws SQLException;

	void insertSunatComprobantePagoElectronico(SunatComprobantesPagoElectronicos sunatComprobantePagoElectronico) throws SQLException;
	void insertComprobantePagoElectronico(ComprobantesPagoElectronicos comprobantePagoElectronico) throws SQLException;

	void insertSunatComprobantePagoFisico(SunatAutorizacionComprobPagoFisico sunatComprobantePagoFisico) throws SQLException;

	void insertSunatParametro(SunatParametro sunatParametro) throws SQLException;

	List<SunatParametro> findSunatParametrosOrderByCodParametroAsc() throws SQLException;
	
	List<SunatParametro> findSunatParametrosOrderByCodParametroAsc( String sunatParametro) throws SQLException;
	
	public List<SunatParametro> findSunatParametrosOrderByCodParametroAsc(String cod_Parametro, String cod_argumento) throws SQLException;
	
	void insertSunatPlazosExcepcionales(SunatPlazosExcepcionales sunaPlazosExcepcionales) throws SQLException;
	
	public void insertSunatCuadreDiarioDetalle(SunatCuadreDiarioDetalle sunaCuadreDiarioDetalle) throws SQLException; 
	public void insertHomologa(Homologa homologa) throws SQLException;
}
