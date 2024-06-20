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

import com.axteroid.ose.server.avatar.bean.TmDetalleResumenes;
import com.axteroid.ose.server.avatar.dao.SunatDetalleResumenesDao;
import com.axteroid.ose.server.avatar.jdbc.DaoPostGresJDBC;
import com.axteroid.ose.server.jpa.model.Documento;

public class SunatDetalleResumenesDaoImpl extends DaoPostGresJDBC implements SunatDetalleResumenesDao{
	private static final Logger log = LoggerFactory.getLogger(SunatDetalleResumenesDaoImpl.class);
	private CallableStatement cstmt;
	private ResultSet rset = null;
	private PreparedStatement pstmt;
	private Statement stmt;
	
	public void insertDetalleResumenes(TmDetalleResumenes tmDetalleResumenes) throws SQLException {
		try {				
			String sql = "SELECT 1 FROM AXTEROIDOSE.TM_DETALLE_RESUMENES WHERE NUM_RUC = "+tmDetalleResumenes.getNumRuc()+
					" AND COD_CPE = '"+tmDetalleResumenes.getCodCpe()+"' AND NUM_SERIE_CPE = '"+tmDetalleResumenes.getNumSerieCpe()+"' "+
					" AND NUM_CPE = '"+tmDetalleResumenes.getNumCpe()+"' AND id_comprobante_rc = '"+tmDetalleResumenes.getIdComprobanteRc()+"' ";
			//log.info("sql (1) "+sql);
			conn = DaoPostGresJDBC.getConnectionOSE();
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			if (!rset.next()) {
				pstmt = conn.prepareStatement(sql);
				sql = "INSERT INTO AXTEROIDOSE.TM_DETALLE_RESUMENES "
						+ " (NUM_RUC, COD_CPE, NUM_SERIE_CPE, NUM_CPE, IND_ESTADO_CPE, FEC_EMISION_CPE, MTO_IMPORTE_CPE, "
						+ " COD_MONEDA_CPE, USER_CREA, FECHA_CREA, id_comprobante_rc, ERROR_COMPROBANTE) "
						+ " VALUES ("+tmDetalleResumenes.getNumRuc()+", '"+tmDetalleResumenes.getCodCpe()+"', '"+tmDetalleResumenes.getNumSerieCpe()+"', "
						+ " '"+tmDetalleResumenes.getNumCpe()+"', "+tmDetalleResumenes.getIndEstadoCpe()+",'"+tmDetalleResumenes.getFecEmisionCpe()+"', "
						+ " "+tmDetalleResumenes.getMtoImporteCpe()+", '"+tmDetalleResumenes.getCodMonedaCpe()+"', '"+tmDetalleResumenes.getUserCrea()+"', "
						+ " '"+tmDetalleResumenes.getFechaCrea()+"', '"+tmDetalleResumenes.getIdComprobanteRc()+"', '"+tmDetalleResumenes.getErrorComprobante()+"')";
				//log.info("sql (2) "+sql);
				//conn = DaoPostGresJDBC.getConnectionOSE(tipBD);
				//conn = DaoPostGresJDBC.abrirConexion(tipBD);
				//conn = super.abrirConn(tipBD);
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
			}else {				
				sql = "UPDATE AXTEROIDOSE.TM_DETALLE_RESUMENES "
						+ " SET IND_ESTADO_CPE = "+tmDetalleResumenes.getIndEstadoCpe()+", FEC_EMISION_CPE = '"+tmDetalleResumenes.getFecEmisionCpe()+"', "
						+ " MTO_IMPORTE_CPE = "+tmDetalleResumenes.getMtoImporteCpe()+", COD_MONEDA_CPE = '"+tmDetalleResumenes.getCodMonedaCpe()+"', "
						+ " USER_CREA = '"+tmDetalleResumenes.getUserCrea()+"', FECHA_CREA = '"+tmDetalleResumenes.getFechaCrea()+"', "
						+ " ERROR_COMPROBANTE = '"+tmDetalleResumenes.getErrorComprobante()+"' WHERE NUM_RUC = "+tmDetalleResumenes.getNumRuc() 
						+ " AND COD_CPE = '"+tmDetalleResumenes.getCodCpe()+"' AND NUM_SERIE_CPE = '"+tmDetalleResumenes.getNumSerieCpe()+"' " 
						+ " AND NUM_CPE = '"+tmDetalleResumenes.getNumCpe()+"' AND id_comprobante_rc = '"+tmDetalleResumenes.getIdComprobanteRc()+"' ";
				//log.info("sql (3) "+sql);
				//conn = DaoPostGresJDBC.getConnectionOSE(tipBD);
				//conn = DaoPostGresJDBC.abrirConexion(tipBD);
				//conn = super.abrirConn(tipBD);
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
			}
		}catch (Exception e) {
			conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertDetalleResumenes Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	
	
	public void insertListaDetalleResumenes(List<TmDetalleResumenes> listTmDetalleResumenes) throws SQLException{					
		for(TmDetalleResumenes tmDetalleResumenes : listTmDetalleResumenes) {
			try {
				String sql = "SELECT 1 FROM AXTEROIDOSE.TM_DETALLE_RESUMENES WHERE NUM_RUC = "+tmDetalleResumenes.getNumRuc()+
						" AND COD_CPE = '"+tmDetalleResumenes.getCodCpe()+"' AND NUM_SERIE_CPE = '"+tmDetalleResumenes.getNumSerieCpe()+"' "+
						" AND NUM_CPE = '"+tmDetalleResumenes.getNumCpe()+"' AND id_comprobante_rc = '"+tmDetalleResumenes.getIdComprobanteRc()+"' ";
				//log.info("sql (1) "+sql);
				conn = DaoPostGresJDBC.getConnectionOSE();
				//conn = DaoPostGresJDBC.abrirConexion(tipBD);
				//conn = abrirConn(tipBD);
				pstmt = conn.prepareStatement(sql);
				rset = pstmt.executeQuery();
				if (!rset.next()) {
					//pstmt = conn.prepareStatement(sql);
					sql = "INSERT INTO AXTEROIDOSE.TM_DETALLE_RESUMENES "
							+ " (NUM_RUC, COD_CPE, NUM_SERIE_CPE, NUM_CPE, IND_ESTADO_CPE, FEC_EMISION_CPE, MTO_IMPORTE_CPE, "
							+ " COD_MONEDA_CPE, USER_CREA, FECHA_CREA, id_comprobante_rc, ERROR_COMPROBANTE) "
							+ " VALUES ("+tmDetalleResumenes.getNumRuc()+", '"+tmDetalleResumenes.getCodCpe()+"', '"+tmDetalleResumenes.getNumSerieCpe()+"', "
							+ " '"+tmDetalleResumenes.getNumCpe()+"', "+tmDetalleResumenes.getIndEstadoCpe()+",'"+tmDetalleResumenes.getFecEmisionCpe()+"', "
							+ " "+tmDetalleResumenes.getMtoImporteCpe()+", '"+tmDetalleResumenes.getCodMonedaCpe()+"', '"+tmDetalleResumenes.getUserCrea()+"', "
							+ " '"+tmDetalleResumenes.getFechaCrea()+"', '"+tmDetalleResumenes.getIdComprobanteRc()+"', '"+tmDetalleResumenes.getErrorComprobante()+"')";
					//log.info("sql (2) "+sql);
					//conn = DaoPostGresJDBC.getConnectionOSE(tipBD);
					//conn = DaoPostGresJDBC.abrirConexion(tipBD);
					//conn = abrirConn(tipBD);
					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();
				}else {				
					sql = "UPDATE AXTEROIDOSE.TM_DETALLE_RESUMENES "
							+ " SET IND_ESTADO_CPE = "+tmDetalleResumenes.getIndEstadoCpe()+", FEC_EMISION_CPE = '"+tmDetalleResumenes.getFecEmisionCpe()+"', "
							+ " MTO_IMPORTE_CPE = "+tmDetalleResumenes.getMtoImporteCpe()+", COD_MONEDA_CPE = '"+tmDetalleResumenes.getCodMonedaCpe()+"', "
							+ " USER_CREA = '"+tmDetalleResumenes.getUserCrea()+"', FECHA_CREA = '"+tmDetalleResumenes.getFechaCrea()+"', "
							+ " ERROR_COMPROBANTE = '"+tmDetalleResumenes.getErrorComprobante()+"' WHERE NUM_RUC = "+tmDetalleResumenes.getNumRuc() 
							+ " AND COD_CPE = '"+tmDetalleResumenes.getCodCpe()+"' AND NUM_SERIE_CPE = '"+tmDetalleResumenes.getNumSerieCpe()+"' " 
							+ " AND NUM_CPE = '"+tmDetalleResumenes.getNumCpe()+"' AND id_comprobante_rc = '"+tmDetalleResumenes.getIdComprobanteRc()+"' ";
					//log.info("sql (3) "+sql);
					//conn = DaoPostGresJDBC.getConnectionOSE(tipBD);
					//conn = DaoPostGresJDBC.abrirConexion(tipBD);
					//conn = abrirConn(tipBD);
					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();
				}				
			}catch (Exception e) {
				conn.rollback();
				StringWriter errors = new StringWriter();				
				e.printStackTrace(new PrintWriter(errors));
				log.error("insertDetalleResumenes Exception \n"+errors);
			} finally {
				cerrarDao();
			}				
		}
	}	
		
	public List<String> getListDetalleResumenTDSSShort(Documento tbComprobante) throws SQLException {
		log.info("getListDetalleResumenTDSSShort: ");
		List<String> listTmDetalleTesumen = new ArrayList<String>();
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();		
			String sql = "SELECT id_comprobante_rc, count(*) FROM AXTEROIDOSE.tm_detalle_resumenes WHERE "
					+ " "+tbComprobante.getNombre()+" group by id_comprobante_rc ";							
			log.info("sql --> "+sql);
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while (rset.next()) 
				listTmDetalleTesumen.add(rset.getString(1));		
			rset.close();
		}catch (Exception e) {
			conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("listComprobantesMensajeSunat Exception \n"+errors);
		} finally {
			cerrarDao();
		}
		return listTmDetalleTesumen;
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
