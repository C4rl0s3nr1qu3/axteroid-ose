package com.axteroid.ose.server.avatar.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.dao.InsertList2SunatPGDao;
import com.axteroid.ose.server.avatar.jdbc.DaoPostGresJDBC;
import com.axteroid.ose.server.jpa.model.ComprobantesPagoElectronicos;
import com.axteroid.ose.server.jpa.model.SunatAutorizacionComprobPagoFisico;
import com.axteroid.ose.server.jpa.model.SunatCertificadoEmisor;
import com.axteroid.ose.server.jpa.model.SunatComprobantesPagoElectronicos;
import com.axteroid.ose.server.jpa.model.SunatContribuyente;
import com.axteroid.ose.server.jpa.model.SunatContribuyenteAsociadoEmisor;
import com.axteroid.ose.server.jpa.model.SunatCuadreDiarioDetalle;
import com.axteroid.ose.server.jpa.model.SunatPadronContribuyente;
import com.axteroid.ose.server.jpa.model.SunatParametro;
import com.axteroid.ose.server.jpa.model.SunatParametroPK;
import com.axteroid.ose.server.jpa.model.SunatPlazosExcepcionales;
import com.axteroid.ose.server.tools.bean.Homologa;
import com.axteroid.ose.server.tools.util.DateUtil;

public class InsertList2SunatPGDaoImpl extends DaoPostGresJDBC implements InsertList2SunatPGDao{
	private static final Logger log = LoggerFactory.getLogger(InsertList2SunatPGDaoImpl.class);
	private CallableStatement cstmt;
	private ResultSet rset = null;
	private PreparedStatement pstmt;
	private Statement stmt;

	public void insertSunatContribuyente(SunatContribuyente sunatContribuyente) throws SQLException {
		try {
			conn = DaoPostGresJDBC.getConnectionOSE();
			String sql = "SELECT 1 FROM AXTEROIDOSE.SUNAT_CONTRIBUYENTE WHERE NUM_RUC = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, sunatContribuyente.getNumRuc());
			rset = pstmt.executeQuery();
			if (!rset.next()) {
				sql = "INSERT INTO AXTEROIDOSE.SUNAT_CONTRIBUYENTE "
						+ "(NUM_RUC, IND_ESTADO, IND_CONDICION, USER_CREA, FECHA_CREA) VALUES (?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, sunatContribuyente.getNumRuc());
				pstmt.setString(2, sunatContribuyente.getIndEstado());
				pstmt.setString(3, sunatContribuyente.getIndCondicion());
				pstmt.setString(4, sunatContribuyente.getUserCrea());
				pstmt.setTimestamp(5, new java.sql.Timestamp(sunatContribuyente.getFechaCrea().getTime()));
				pstmt.executeUpdate();
			}
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertSunatContribuyente Exception \n"+errors);			
		} finally {
			cerrarDao();
		}
	}

	public void insertSunatPadronContribuyente(SunatPadronContribuyente sunatPadronContribuyente) throws SQLException {
		try {
			conn = DaoPostGresJDBC.getConnectionOSE();
			String sql = "SELECT 1 FROM AXTEROIDOSE.SUNAT_PADRON_CONTRIBUYENTE WHERE NUM_RUC = ? AND IND_PADRON = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, sunatPadronContribuyente.getSunatPadronContribuyentePK().getNumRuc());
			pstmt.setString(2, sunatPadronContribuyente.getSunatPadronContribuyentePK().getIndPadron());
			rset = pstmt.executeQuery();
			if (!rset.next()) {
				sql = "INSERT INTO AXTEROIDOSE.SUNAT_PADRON_CONTRIBUYENTE "
						+ "(NUM_RUC, IND_PADRON, USER_CREA, FECHA_CREA) VALUES (?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, sunatPadronContribuyente.getSunatPadronContribuyentePK().getNumRuc());
				pstmt.setString(2, sunatPadronContribuyente.getSunatPadronContribuyentePK().getIndPadron());
				pstmt.setString(3, sunatPadronContribuyente.getUserCrea());
				pstmt.setTimestamp(4, new java.sql.Timestamp(sunatPadronContribuyente.getFechaCrea().getTime()));
				pstmt.executeUpdate();
			}
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertSunatPadronContribuyente Exception \n"+errors);	
		} finally {
			cerrarDao();
		}
	}

	public void insertSunatContribuyenteEmisor(SunatContribuyenteAsociadoEmisor sunatContribuyenteAsociadoEmisor)
			throws SQLException {
		try {
			conn = DaoPostGresJDBC.getConnectionOSE();
			String sql = "SELECT 1 FROM AXTEROIDOSE.SUNAT_CONTRIBUYENTE_ASOCIADO_EMISOR "
					+ "WHERE NUM_RUC = ? AND NUM_RUC_ASOCIADO = ? AND IND_TIP_ASOCIACION = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, sunatContribuyenteAsociadoEmisor.getSunatContribuyenteAsociadoEmisorPK().getNumRuc());
			pstmt.setLong(2, sunatContribuyenteAsociadoEmisor.getSunatContribuyenteAsociadoEmisorPK().getNumRucAsociado());
			pstmt.setLong(3, sunatContribuyenteAsociadoEmisor.getSunatContribuyenteAsociadoEmisorPK().getIndTipAsociacion());
			rset = pstmt.executeQuery();
			if (!rset.next()) {
				sql = "INSERT INTO AXTEROIDOSE.SUNAT_CONTRIBUYENTE_ASOCIADO_EMISOR "
						+ "(NUM_RUC, NUM_RUC_ASOCIADO, IND_TIP_ASOCIACION, FEC_INICIO, FEC_FIN, USER_CREA, FECHA_CREA) "
						+ "VALUES (?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, sunatContribuyenteAsociadoEmisor.getSunatContribuyenteAsociadoEmisorPK().getNumRuc());
				pstmt.setLong(2, sunatContribuyenteAsociadoEmisor.getSunatContribuyenteAsociadoEmisorPK().getNumRucAsociado());
				pstmt.setLong(3, sunatContribuyenteAsociadoEmisor.getSunatContribuyenteAsociadoEmisorPK().getIndTipAsociacion());
				pstmt.setDate(4, new java.sql.Date(sunatContribuyenteAsociadoEmisor.getFecInicio().getTime()));
				pstmt.setDate(5, new java.sql.Date(sunatContribuyenteAsociadoEmisor.getFecFin().getTime()));
				pstmt.setString(6, sunatContribuyenteAsociadoEmisor.getUserCrea());
				pstmt.setTimestamp(7, new java.sql.Timestamp(sunatContribuyenteAsociadoEmisor.getFechaCrea().getTime()));
				pstmt.executeUpdate();
			}
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertSunatContribuyenteEmisor Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}

	public void insertSunatCertificadoEmisor(SunatCertificadoEmisor sunatCertificadoEmisor) throws SQLException {
		try {
			conn = DaoPostGresJDBC.getConnectionOSE();
			String sql = "SELECT 1 FROM AXTEROIDOSE.SUNAT_CERTIFICADO_EMISOR WHERE NUM_RUC = ? AND NUM_ID_CA = ? "
					+ "AND NUM_ID_CD = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, sunatCertificadoEmisor.getSunatCertificadoEmisorPK().getNumRuc());
			pstmt.setString(2, sunatCertificadoEmisor.getSunatCertificadoEmisorPK().getNumIdCa());
			pstmt.setString(3, sunatCertificadoEmisor.getSunatCertificadoEmisorPK().getNumIdCd());
			rset = pstmt.executeQuery();
			if (!rset.next()) {
				sql = "INSERT INTO AXTEROIDOSE.SUNAT_CERTIFICADO_EMISOR "
						+ "(NUM_RUC, NUM_ID_CA, NUM_ID_CD, FEC_ALTA, FEC_BAJA, USER_CREA, FECHA_CREA) "
						+ "VALUES (?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, sunatCertificadoEmisor.getSunatCertificadoEmisorPK().getNumRuc());
				pstmt.setString(2, sunatCertificadoEmisor.getSunatCertificadoEmisorPK().getNumIdCa());
				pstmt.setString(3, sunatCertificadoEmisor.getSunatCertificadoEmisorPK().getNumIdCd());
				pstmt.setTimestamp(4, new java.sql.Timestamp(sunatCertificadoEmisor.getFecAlta().getTime()));
				pstmt.setTimestamp(5, new java.sql.Timestamp(sunatCertificadoEmisor.getFecBaja().getTime()));
				pstmt.setString(6, sunatCertificadoEmisor.getUserCrea());
				pstmt.setTimestamp(7, new java.sql.Timestamp(sunatCertificadoEmisor.getFechaCrea().getTime()));
				pstmt.executeUpdate();
			}
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertSunatCertificadoEmisor Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}

	public void insertSunatComprobantePagoElectronico(SunatComprobantesPagoElectronicos sunatComprobantePagoElectronico)
			throws SQLException {
		try {
			conn = DaoPostGresJDBC.getConnectionOSE();
			String sql = "SELECT 1 FROM AXTEROIDOSE.SUNAT_COMPROBANTES_PAGO_ELECTRONICOS WHERE NUM_RUC = ? AND COD_CPE = ? "
					+ "AND NUM_SERIE_CPE = ? AND NUM_CPE = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, sunatComprobantePagoElectronico.getSunatComprobantesPagoElectronicosPK().getNumRuc());
			pstmt.setString(2, sunatComprobantePagoElectronico.getSunatComprobantesPagoElectronicosPK().getCodCpe());
			pstmt.setString(3, sunatComprobantePagoElectronico.getSunatComprobantesPagoElectronicosPK().getNumSerieCpe());
			pstmt.setLong(4, sunatComprobantePagoElectronico.getSunatComprobantesPagoElectronicosPK().getNumCpe());
			rset = pstmt.executeQuery();
			if (!rset.next()) {
				sql = "INSERT INTO AXTEROIDOSE.SUNAT_COMPROBANTES_PAGO_ELECTRONICOS "
						+ "(NUM_RUC, COD_CPE, NUM_SERIE_CPE, NUM_CPE, IND_ESTADO_CPE, FEC_EMISION_CPE, MTO_IMPORTE_CPE, "
						+ "COD_MONEDA_CPE, COD_MOT_TRASLADO, COD_MOD_TRASLADO, IND_TRANSBORDO, FEC_INI_TRASLADO, USER_CREA, "
						+ "FECHA_CREA) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, sunatComprobantePagoElectronico.getSunatComprobantesPagoElectronicosPK().getNumRuc());
				pstmt.setString(2, sunatComprobantePagoElectronico.getSunatComprobantesPagoElectronicosPK().getCodCpe());
				pstmt.setString(3, sunatComprobantePagoElectronico.getSunatComprobantesPagoElectronicosPK().getNumSerieCpe());
				pstmt.setLong(4, sunatComprobantePagoElectronico.getSunatComprobantesPagoElectronicosPK().getNumCpe());
				pstmt.setInt(5, sunatComprobantePagoElectronico.getIndEstadoCpe());
				pstmt.setTimestamp(6, new java.sql.Timestamp(sunatComprobantePagoElectronico.getFecEmisionCpe().getTime()));
				//pstmt.setBigDecimal(7, sunatComprobantePagoElectronico.getMtoImporteCpe());
				pstmt.setBigDecimal(7, sunatComprobantePagoElectronico.getMtoImporteCpe());
				pstmt.setString(8, sunatComprobantePagoElectronico.getCodMonedaCpe());
				pstmt.setObject(9, sunatComprobantePagoElectronico.getCodMotTraslado());
				pstmt.setObject(10, sunatComprobantePagoElectronico.getCodModTraslado());
				pstmt.setObject(11, sunatComprobantePagoElectronico.getIndTransbordo());
				pstmt.setObject(12, sunatComprobantePagoElectronico.getFecIniTraslado());
//				pstmt.setInt(9, sunatComprobantePagoElectronico.getCodMotTraslado());
//				pstmt.setInt(10, sunatComprobantePagoElectronico.getCodModTraslado());
//				pstmt.setInt(11, sunatComprobantePagoElectronico.getIndTransbordo());
//				pstmt.setObject(12, sunatComprobantePagoElectronico.getFecIniTraslado());				
				pstmt.setString(13, sunatComprobantePagoElectronico.getUserCrea());
				pstmt.setTimestamp(14, new java.sql.Timestamp(sunatComprobantePagoElectronico.getFechaCrea().getTime()));
				pstmt.executeUpdate();
			}
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertSunatComprobantePagoElectronico Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}

	public void insertComprobantePagoElectronico(ComprobantesPagoElectronicos comprobantePagoElectronico)
			throws SQLException {
		try {
			conn = DaoPostGresJDBC.getConnectionOSE();
			String sql = "SELECT 1 FROM AXTEROIDOSE.COMPROBANTES_PAGO_ELECTRONICOS WHERE NUM_RUC = ? AND COD_CPE = ? "
					+ "AND NUM_SERIE_CPE = ? AND NUM_CPE = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, comprobantePagoElectronico.getComprobantesPagoElectronicosPK().getNumRuc());
			pstmt.setString(2, comprobantePagoElectronico.getComprobantesPagoElectronicosPK().getCodCpe());
			pstmt.setString(3, comprobantePagoElectronico.getComprobantesPagoElectronicosPK().getNumSerieCpe());
			pstmt.setLong(4, comprobantePagoElectronico.getComprobantesPagoElectronicosPK().getNumCpe());
			rset = pstmt.executeQuery();
			if (!rset.next()) {
				sql = "INSERT INTO AXTEROIDOSE.COMPROBANTES_PAGO_ELECTRONICOS "
						+ "(NUM_RUC, COD_CPE, NUM_SERIE_CPE, NUM_CPE, IND_ESTADO_CPE, FEC_EMISION_CPE, MTO_IMPORTE_CPE, "
						+ "COD_MONEDA_CPE, COD_MOT_TRASLADO, COD_MOD_TRASLADO, IND_TRANSBORDO, FEC_INI_TRASLADO, USER_CREA, "
						+ "FECHA_CREA) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, comprobantePagoElectronico.getComprobantesPagoElectronicosPK().getNumRuc());
				pstmt.setString(2, comprobantePagoElectronico.getComprobantesPagoElectronicosPK().getCodCpe());
				pstmt.setString(3, comprobantePagoElectronico.getComprobantesPagoElectronicosPK().getNumSerieCpe());
				pstmt.setLong(4, comprobantePagoElectronico.getComprobantesPagoElectronicosPK().getNumCpe());
				pstmt.setInt(5, comprobantePagoElectronico.getIndEstadoCpe());
				pstmt.setTimestamp(6, new java.sql.Timestamp(comprobantePagoElectronico.getFecEmisionCpe().getTime()));
				//pstmt.setBigDecimal(7, sunatComprobantePagoElectronico.getMtoImporteCpe());
				pstmt.setBigDecimal(7, comprobantePagoElectronico.getMtoImporteCpe());
				pstmt.setString(8, comprobantePagoElectronico.getCodMonedaCpe());
				pstmt.setObject(9, comprobantePagoElectronico.getCodMotTraslado());
				pstmt.setObject(10, comprobantePagoElectronico.getCodModTraslado());
				pstmt.setObject(11, comprobantePagoElectronico.getIndTransbordo());
				java.sql.Timestamp t = null;
				if(comprobantePagoElectronico.getFecIniTraslado()!=null)
					t = new java.sql.Timestamp(comprobantePagoElectronico.getFecIniTraslado().getTime());
				pstmt.setTimestamp(12, t);
//				pstmt.setInt(9, sunatComprobantePagoElectronico.getCodMotTraslado());
//				pstmt.setInt(10, sunatComprobantePagoElectronico.getCodModTraslado());
//				pstmt.setInt(11, sunatComprobantePagoElectronico.getIndTransbordo());
//				pstmt.setObject(12, sunatComprobantePagoElectronico.getFecIniTraslado());				
				pstmt.setString(13, comprobantePagoElectronico.getUserCrea());
				pstmt.setTimestamp(14, new java.sql.Timestamp(comprobantePagoElectronico.getFechaCrea().getTime()));
				pstmt.executeUpdate();
			}
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertSunatComprobantePagoElectronico Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}
	
	public void insertSunatComprobantePagoFisico(SunatAutorizacionComprobPagoFisico sunatComprobantePagoFisico) throws SQLException {
		try {
			conn = DaoPostGresJDBC.getConnectionOSE();
			String sql = "SELECT 1 FROM AXTEROIDOSE.SUNAT_AUTORIZACION_COMPROB_PAGO_FISICO WHERE NUM_RUC = ? AND COD_CPE = ? "
					+ "AND NUM_SERIE_CPE = ? AND NUM_INI_CPE = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, sunatComprobantePagoFisico.getSunatAutorizacionComprobPagoFisicoPK().getNumRuc());
			pstmt.setString(2, sunatComprobantePagoFisico.getSunatAutorizacionComprobPagoFisicoPK().getCodCpe());
			pstmt.setString(3, sunatComprobantePagoFisico.getSunatAutorizacionComprobPagoFisicoPK().getNumSerieCpe());
			pstmt.setInt(4, sunatComprobantePagoFisico.getSunatAutorizacionComprobPagoFisicoPK().getNumIniCpe());
			rset = pstmt.executeQuery();
			if (!rset.next()) {
				sql = "INSERT INTO AXTEROIDOSE.SUNAT_AUTORIZACION_COMPROB_PAGO_FISICO "
						+ "(NUM_RUC, COD_CPE, NUM_SERIE_CPE, NUM_INI_CPE, NUM_FIN_CPE, USER_CREA, FECHA_CREA) "
						+ "VALUES (?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, sunatComprobantePagoFisico.getSunatAutorizacionComprobPagoFisicoPK().getNumRuc());
				pstmt.setString(2, sunatComprobantePagoFisico.getSunatAutorizacionComprobPagoFisicoPK().getCodCpe());
				pstmt.setString(3, sunatComprobantePagoFisico.getSunatAutorizacionComprobPagoFisicoPK().getNumSerieCpe());
				pstmt.setInt(4, sunatComprobantePagoFisico.getSunatAutorizacionComprobPagoFisicoPK().getNumIniCpe());
				pstmt.setInt(5, sunatComprobantePagoFisico.getNumFinCpe());
				pstmt.setString(6, sunatComprobantePagoFisico.getUserCrea());
				pstmt.setTimestamp(7, new java.sql.Timestamp(sunatComprobantePagoFisico.getFechaCrea().getTime()));
				pstmt.executeUpdate();
			}
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertSunatComprobantePagoFisico Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}

	public void insertSunatParametro(SunatParametro sunatParametro) throws SQLException {
		try {
			conn = DaoPostGresJDBC.getConnectionOSE();
			String sql = "SELECT 1 FROM AXTEROIDOSE.SUNAT_PARAMETRO WHERE COD_PARAMETRO = ? AND COD_ARGUMENTO = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sunatParametro.getSunatParametroPK().getCodParametro());
			pstmt.setString(2, sunatParametro.getSunatParametroPK().getCodArgumento());
			rset = pstmt.executeQuery();
			if (!rset.next()) {
				sql = "INSERT INTO AXTEROIDOSE.SUNAT_PARAMETRO "
						+ "(COD_PARAMETRO, COD_ARGUMENTO, DES_ARGUMENTO, USER_CREA, FECHA_CREA) VALUES (?,?,?,?,?)";
				log.info("sql: {}",sql);
				log.info("COD_PARAMETRO: {}", sunatParametro.getSunatParametroPK().getCodParametro());
				log.info("COD_ARGUMENTO: {}", sunatParametro.getSunatParametroPK().getCodArgumento());
				log.info("DES_ARGUMENTO: {}", sunatParametro.getDesArgumento());
								
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, sunatParametro.getSunatParametroPK().getCodParametro());
				pstmt.setString(2, sunatParametro.getSunatParametroPK().getCodArgumento());
				pstmt.setString(3, sunatParametro.getDesArgumento());
				pstmt.setString(4, sunatParametro.getUserCrea());
				pstmt.setTimestamp(5, new java.sql.Timestamp(sunatParametro.getFechaCrea().getTime()));
				pstmt.executeUpdate();
			}else {
				sql = "UPDATE AXTEROIDOSE.SUNAT_PARAMETRO "
						+ " SET DES_ARGUMENTO = ? , USER_MODI = ? , FECHA_MODI = ? "
						+ " WHERE COD_PARAMETRO = ?  AND COD_ARGUMENTO = ? ";
				log.info("sql: {}",sql);
				log.info("COD_PARAMETRO: {}", sunatParametro.getSunatParametroPK().getCodParametro());
				log.info("COD_ARGUMENTO: {}", sunatParametro.getSunatParametroPK().getCodArgumento());
				log.info("DES_ARGUMENTO: {}", sunatParametro.getDesArgumento());
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, sunatParametro.getDesArgumento());
				pstmt.setString(2, sunatParametro.getUserCrea());
				pstmt.setTimestamp(3, new java.sql.Timestamp(sunatParametro.getFechaCrea().getTime()));
				pstmt.setString(4, sunatParametro.getSunatParametroPK().getCodParametro());
				pstmt.setString(5, sunatParametro.getSunatParametroPK().getCodArgumento());
				pstmt.executeUpdate();
			}
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertSunatParametro Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}

	public List<SunatParametro> findSunatParametrosOrderByCodParametroAsc() throws SQLException {
		log.info("findSunatParametrosOrderByCodParametroAsc ");
		List<SunatParametro> listaParametro = new ArrayList<SunatParametro>();
		try {
			conn = DaoPostGresJDBC.getConnectionOSE();		
			String sql = "SELECT COD_PARAMETRO, COD_ARGUMENTO, DES_ARGUMENTO FROM AXTEROIDOSE.SUNAT_PARAMETRO ORDER BY COD_PARAMETRO";
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				SunatParametroPK parametroPk = new SunatParametroPK(rset.getString(1), rset.getString(2));
				SunatParametro parametro = new SunatParametro(parametroPk);
				parametro.setDesArgumento(rset.getString(3));
				listaParametro.add(parametro);
			}		
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("findParametrosOrderByCodParametroAsc Exception \n"+errors);
		} finally {
			cerrarDao();
		}		
		return listaParametro;
	}
	
	public List<SunatParametro> findSunatParametrosOrderByCodParametroAsc(String cod_Parametro) throws SQLException {
		log.info("findSunatParametrosOrderByCodParametroAsc  cod_Parametro: "+cod_Parametro);
		List<SunatParametro> listaParametro = new ArrayList<SunatParametro>();
		try {
			conn = DaoPostGresJDBC.getConnectionOSE();			
			String sql = "SELECT COD_PARAMETRO, COD_ARGUMENTO, DES_ARGUMENTO FROM AXTEROIDOSE.SUNAT_PARAMETRO "
					+ " where COD_PARAMETRO = '"+cod_Parametro+"' ORDER BY COD_PARAMETRO";
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				SunatParametroPK tsParametroPk = new SunatParametroPK(rset.getString(1), rset.getString(2));
				SunatParametro tsParametro = new SunatParametro(tsParametroPk);
				tsParametro.setDesArgumento(rset.getString(3));
				listaParametro.add(tsParametro);
			}		
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("findParametrosOrderByCodParametroAsc Exception \n"+errors);
		} finally {
			cerrarDao();
		}		
		return listaParametro;
	}	

	public List<SunatParametro> findSunatParametrosOrderByCodParametroAsc(String cod_Parametro, String cod_argumento) throws SQLException {
		log.info("findParametrosOrderByCodParametroAsc  cod_Parametro: "+cod_Parametro+" - cod_argumento: "+cod_argumento);
		List<SunatParametro> listaParametro = new ArrayList<SunatParametro>();
		try {
			conn = DaoPostGresJDBC.getConnectionOSE();			
			String sql = "SELECT COD_PARAMETRO, COD_ARGUMENTO, DES_ARGUMENTO FROM AXTEROIDOSE.SUNAT_PARAMETRO "
					+ " where COD_PARAMETRO = '"+cod_Parametro+"' and cod_argumento = '"+cod_argumento+"' ";
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				SunatParametroPK tsParametroPk = new SunatParametroPK(rset.getString(1), rset.getString(2));
				SunatParametro tsParametro = new SunatParametro(tsParametroPk);
				tsParametro.setDesArgumento(rset.getString(3));
				listaParametro.add(tsParametro);
			}		
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("findParametrosOrderByCodParametroAsc Exception \n"+errors);
		} finally {
			cerrarDao();
		}		
		return listaParametro;
	}	
	
	public void insertSunatPlazosExcepcionales(SunatPlazosExcepcionales sunatPlazosExcepcionales) throws SQLException {
		try {
			conn = DaoPostGresJDBC.getConnectionOSE();
			String sql = "SELECT 1 FROM AXTEROIDOSE.SUNAT_PLAZOS_EXCEPCIONALES WHERE NUM_RUC = ? AND COD_CPE = ? "
					+ "AND FEC_EMISION = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, sunatPlazosExcepcionales.getSunatPlazosExcepcionalesPK().getNumRuc());
			pstmt.setString(2, sunatPlazosExcepcionales.getSunatPlazosExcepcionalesPK().getCodCpe());			
			pstmt.setTimestamp(3, new java.sql.Timestamp(sunatPlazosExcepcionales.getSunatPlazosExcepcionalesPK().getFecEmision().getTime()));
			rset = pstmt.executeQuery();
			if (!rset.next()) {
				sql = "INSERT INTO AXTEROIDOSE.SUNAT_PLAZOS_EXCEPCIONALES "
						+ "(NUM_RUC, COD_CPE, FEC_EMISION, FEC_LIMITE, USER_CREA, FECHA_CREA) "
						+ "VALUES (?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, sunatPlazosExcepcionales.getSunatPlazosExcepcionalesPK().getNumRuc());
				pstmt.setString(2, sunatPlazosExcepcionales.getSunatPlazosExcepcionalesPK().getCodCpe());
				pstmt.setTimestamp(3, new java.sql.Timestamp(sunatPlazosExcepcionales.getSunatPlazosExcepcionalesPK().getFecEmision().getTime()));
				pstmt.setTimestamp(4, new java.sql.Timestamp(sunatPlazosExcepcionales.getFecLimite().getTime()));
				pstmt.setString(5, sunatPlazosExcepcionales.getUserCrea());
				pstmt.setTimestamp(6, new java.sql.Timestamp(sunatPlazosExcepcionales.getFechaCrea().getTime()));
				pstmt.executeUpdate();
			}
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertSunatPlazosExcepcionales Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}

	
	public void insertSunatCuadreDiarioDetalle(String linea) {
		try {
			String[] informacion = linea.split("[|]");
			SunatCuadreDiarioDetalle tsCuadreDiarioDetalle = new SunatCuadreDiarioDetalle();
			tsCuadreDiarioDetalle.setRucEmisor(Long.valueOf(informacion[0]));
			tsCuadreDiarioDetalle.setTipoComprobante(informacion[1]);
			tsCuadreDiarioDetalle.setSerie(informacion[2]);
			tsCuadreDiarioDetalle.setNumeroCorrelativo(informacion[3]);
			tsCuadreDiarioDetalle.setEstadoFinal(informacion[4]);
			String fecha = informacion[5]+" "+informacion[6];
			tsCuadreDiarioDetalle.setFechaRecepcion(DateUtil.parseDate(fecha, "dd/MM/yyyy HH:mm:ss"));
			tsCuadreDiarioDetalle.setHoraRecepcion(informacion[6]);		
			this.insertSunatCuadreDiarioDetalle(tsCuadreDiarioDetalle);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertSunatCuadreDiarioDetalle Exception \n"+errors);
		}
	}
	
	public void insertSunatCuadreDiarioDetalle(SunatCuadreDiarioDetalle sunatCuadreDiarioDetalle) throws SQLException {
		try {				
			String sql = "SELECT 1 FROM AXTEROIDOSE.SUNAT_CRUADRE_DIARIO_DETALLE WHERE RUC_EMISOR = '"+sunatCuadreDiarioDetalle.getRucEmisor()+"' "+
					" AND TIPO_COMPROBANTE = '"+sunatCuadreDiarioDetalle.getTipoComprobante()+"' AND SERIE = '"+sunatCuadreDiarioDetalle.getSerie()+"' "+
					" AND NUMERO_CORRELATIVO = '"+sunatCuadreDiarioDetalle.getNumeroCorrelativo()+"' AND FECHA_RECEPCION = '"+sunatCuadreDiarioDetalle.getFechaRecepcion()+"' ";
			log.info("sql (1) "+sql);
			conn = DaoPostGresJDBC.getConnectionOSE();
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			if (!rset.next()) {
				sql = "INSERT INTO AXTEROIDOSE.SUNAT_CRUADRE_DIARIO_DETALLE "
						+ " (RUC_EMISOR, TIPO_COMPROBANTE, SERIE, NUMERO_CORRELATIVO, ESTADO_FINAL, FECHA_RECEPCION, HORA_RECEPCION) "
						+ " VALUES ('"+sunatCuadreDiarioDetalle.getRucEmisor()+"', '"+sunatCuadreDiarioDetalle.getTipoComprobante()+"', '"+sunatCuadreDiarioDetalle.getSerie()+"', "
						+ " '"+sunatCuadreDiarioDetalle.getNumeroCorrelativo()+"', '"+sunatCuadreDiarioDetalle.getEstadoFinal()+"'," 
						+ " '"+sunatCuadreDiarioDetalle.getFechaRecepcion()+"', '"+sunatCuadreDiarioDetalle.getHoraRecepcion()+"')";
				//log.info("sql (2) "+sql);
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
			}else {				
				sql = "UPDATE AXTEROIDOSE.SUNAT_CRUADRE_DIARIO_DETALLE "
						+ " SET ESTADO_FINAL = '"+sunatCuadreDiarioDetalle.getEstadoFinal()+"' WHERE RUC_EMISOR = '"+sunatCuadreDiarioDetalle.getRucEmisor()+"' "  
						+ " AND TIPO_COMPROBANTE = '"+sunatCuadreDiarioDetalle.getTipoComprobante()+"' AND SERIE = '"+sunatCuadreDiarioDetalle.getSerie()+"' " 
						+ " AND NUMERO_CORRELATIVO = '"+sunatCuadreDiarioDetalle.getNumeroCorrelativo()+"' AND FECHA_RECEPCION = '"+sunatCuadreDiarioDetalle.getFechaRecepcion()+"' ";
				//log.info("sql (3) "+sql);
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
			}
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertSunatCuadreDiarioDetalle Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	

	public void insertHomologa(Homologa homologa) throws SQLException {
		try {				
			String sql = "SELECT 1 FROM AXTEROIDOSE.HOMOLOGACION WHERE comprobante = '"+homologa.getComprobante()+"' ";
			log.info("sql (1) "+sql);
			conn = DaoPostGresJDBC.getConnectionOSE();
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			if (!rset.next()) {
				sql = "INSERT INTO AXTEROIDOSE.HOMOLOGACION "
						+ " (Codigo_Respuesta, Estado_Respuesta, Codigo_Esperado, Estado_Esperado,"
						+ "   A_nivel_de_Codigo, A_nivel_de_Estado, Resultado_Subida, Evaluación_INSI,"
						+ "   E_RazonSocial, E_RUC, E_Usuario, E_Clave, E_RutaXML,"
						+ "   E_WSDL, Estado_value_out, Ticket, E_metodo,"
						+ "   Tipo_de_Documento, Comprobante) "
						+ " VALUES ('"+homologa.getCodigo_Respuesta()+"', '"+homologa.getEstado_Respuesta()+"',"
						+ " '"+homologa.getCodigo_Esperado()+"', '"+homologa.getEstado_Esperado()+"',"
						+ " '"+homologa.getA_nivel_de_Codigo()+"', '"+homologa.getA_nivel_de_Estado()+"'," 						
						+ " '"+homologa.getResultado_Subida()+"', '"+homologa.getEvaluación_INSI()+"'," 						
						+ " '"+homologa.getE_RazonSocial()+"', '"+homologa.getE_RUC()+"'," 						
						+ " '"+homologa.getE_Usuario()+"', '"+homologa.getE_Clave()+"'," 						
						+ " '"+homologa.getE_RutaXML()+"', '"+homologa.getE_WSDL()+"'," 
						+ " '"+homologa.getEstado_value_out()+"', '"+homologa.getTicket()+"'," 						
						+ " '"+homologa.getE_metodo()+"', " 																	
						+ " '"+homologa.getTipo_de_Documento()+"', '"+homologa.getComprobante()+"')";
				//log.info("sql (2) "+sql);
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
			}else {				
				sql = "UPDATE AXTEROIDOSE.HOMOLOGACION "
						+ " SET codigo_Esperado = '"+homologa.getCodigo_Esperado()+"', "
						+ " estado_Esperado = '"+homologa.getEstado_Esperado()+"', "
						+ " Resultado_Subida = '"+homologa.getResultado_Subida()+"', "
						+ " WHERE comprobante = '"+homologa.getComprobante()+"' ";
				//log.info("sql (3) "+sql);
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
			}
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertSunatCuadreDiarioDetalle Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}		
	
	private void cerrarDao() {
		try {
			if (cstmt != null)
				cstmt.close();
			if (rset != null)
				rset.close();
			if (pstmt != null)
				pstmt.close();
			if (stmt != null)
				stmt.close();
			if (conn != null && !conn.isClosed()) {
				conn.close();
				log.info("DisConnected: "+conn.isClosed());
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cerrarDao Exception \n"+errors);
		}
	}

}
