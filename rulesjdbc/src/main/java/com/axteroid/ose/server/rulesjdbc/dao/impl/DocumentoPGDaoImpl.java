package com.axteroid.ose.server.rulesjdbc.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesjdbc.dao.DocumentoPGDao;
import com.axteroid.ose.server.rulesjdbc.jdbc.PostGresJDBCDao;

public class DocumentoPGDaoImpl extends PostGresJDBCDao implements DocumentoPGDao{
	private static final Logger log = LoggerFactory.getLogger(DocumentoPGDaoImpl.class);
	private CallableStatement cstmt;
	private ResultSet rset = null;
	private PreparedStatement pstmt;
	private Statement stmt;
		
	public void updateErrorLog(com.axteroid.ose.server.jpa.model.Documento tbComprobante) throws SQLException{
		log.info("updateErrorLog: "+tbComprobante.getId()+" : "+tbComprobante.getNombre());
		try {			
			conn = PostGresJDBCDao.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET "+ 
				" ERROR_LOG = '"+tbComprobante.getErrorLog()+"' " +
				" WHERE ID = "+tbComprobante.getId();			
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateErrorLog Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}		
	
	public void updateRARCRR() throws SQLException{
		log.info("updateRARCRR");
		try {			
			conn = PostGresJDBCDao.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '40' "
					+ " WHERE  ESTADO = '30' and  tipo_comprobante in ('RA','RC','RR')";		
			log.info("script --> "+script);
			PreparedStatement pstmt = conn.prepareStatement(script);
//			pstmt.setBytes(1, tbComprobante.getUbl());
//			pstmt.setBytes(2, tbComprobante.getCdr());
//			pstmt.setLong(3, tbComprobante.getId());
			pstmt.executeUpdate();					
		}catch (Exception e) {
			conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateRARCRR Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}		

	public void updateUblEstErLog(Documento tbComprobante) throws SQLException{
		log.info("updateUblEstErLog --> "+tbComprobante.getMensajeSunat());
		try {			
			conn = PostGresJDBCDao.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET UBL = ?, "
					+ " ERROR_COMPROBANTE = ?, ESTADO = ? WHERE ID = ? ";		
			log.info("script --> "+script);
			PreparedStatement pstmt = conn.prepareStatement(script);
			pstmt.setBytes(1, tbComprobante.getUbl());
			pstmt.setString(2, tbComprobante.getErrorComprobante().toString());
			pstmt.setString(3, tbComprobante.getEstado());
			pstmt.setLong(4, tbComprobante.getId());
			pstmt.executeUpdate();					
		}catch (Exception e) {
			conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateUblCdr Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}		
	
	public void updateUblCdr(Documento tbComprobante) throws SQLException{
		log.info("updateUblCdr --> "+tbComprobante.getMensajeSunat());
		try {			
			conn = PostGresJDBCDao.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET UBL = ?, CDR = ? WHERE ID = ? ";		
			log.info("script --> "+script);
			PreparedStatement pstmt = conn.prepareStatement(script);
			pstmt.setBytes(1, tbComprobante.getUbl());
			pstmt.setBytes(2, tbComprobante.getCdr());
			pstmt.setLong(3, tbComprobante.getId());
			pstmt.executeUpdate();					
		}catch (Exception e) {
			conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateUblCdr Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	
	
	public void updateUblCdrMensajeSunat(Documento tbComprobante) throws SQLException{
		log.info("updateUblCdrMensajeSunat --> "+tbComprobante.getEstado());
		try {			
			conn = PostGresJDBCDao.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET UBL = ?, CDR = ?, MENSAJE_SUNAT = ? WHERE ID = ? ";		
			log.info("script --> "+script);
			PreparedStatement pstmt = conn.prepareStatement(script);
			pstmt.setBytes(1, tbComprobante.getUbl());
			pstmt.setBytes(2, tbComprobante.getCdr());
			pstmt.setBytes(3, tbComprobante.getMensajeSunat());
			pstmt.setLong(4, tbComprobante.getId());
			pstmt.executeUpdate();	
		}catch (Exception e) {
			conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateUblCdrMensajeSunat Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}		

	public void updateRespuestaSunat(Documento tbComprobante) throws SQLException{
		log.info("updateRespuestaSunat --> "+tbComprobante.getId());
		try {			
			conn = PostGresJDBCDao.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET " + 
					" ESTADO = '"+tbComprobante.getEstado()+"', " +
					" RESPUESTA_SUNAT = '"+tbComprobante.getRespuestaSunat()+"'  " +
					" WHERE ID = "+tbComprobante.getId();
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateRespuesta Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	
	
	public void updateEstadoRespuestaLogSunat(Documento tbComprobante) throws SQLException{
		log.info("updateRespuestaLogSunat --> "+tbComprobante.getId());
		try {			
			conn = PostGresJDBCDao.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET " + 
					" ESTADO = '"+tbComprobante.getEstado()+"', " +
					" RESPUESTA_SUNAT = '"+tbComprobante.getRespuestaSunat()+"',  " +
					" ERROR_LOG_SUNAT = '"+tbComprobante.getErrorLogSunat()+"'  " +
					" WHERE ID = "+tbComprobante.getId();
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateRespuestaSunat Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	
	
	public void updateRespuestaMensajeLogSunat(Documento tbComprobante) throws SQLException{
		log.info("updateRespuestaMensajeLogSunat --> "+tbComprobante.getId());
		try {			
			conn = PostGresJDBCDao.getConnectionOSE();	
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET ESTADO = ?, "
					+ " RESPUESTA_SUNAT = ?, MENSAJE_SUNAT = ?, ERROR_LOG_SUNAT = ? WHERE ID = ? ";		
			log.info("script --> "+script);
			PreparedStatement pstmt = conn.prepareStatement(script);
			pstmt.setString(1, tbComprobante.getEstado());
			pstmt.setString(2, tbComprobante.getRespuestaSunat());
			pstmt.setBytes(3, tbComprobante.getMensajeSunat());
			pstmt.setString(4, tbComprobante.getErrorLogSunat());
			pstmt.setLong(5, tbComprobante.getId());
			pstmt.executeUpdate();				
		}catch (Exception e) {
			conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateRespuestaSunat Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	

	public void updateFileUBLOse(Documento tbComprobante) throws SQLException{
		log.info("updateFileCDROse --> "+tbComprobante.getId());
		try {			
			conn = PostGresJDBCDao.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET UBL = ? WHERE ID = ? ";		
			log.info("script --> "+script);
			PreparedStatement pstmt = conn.prepareStatement(script);
			pstmt.setBytes(1, tbComprobante.getUbl());
			pstmt.setLong(2, tbComprobante.getId());
			pstmt.executeUpdate();	
		}catch (Exception e) {
			conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateFileCDROse Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	
	public void updateFileCDROse(Documento tbComprobante) throws SQLException{
		log.info("updateFileCDROse --> "+tbComprobante.getId());
		try {			
			conn = PostGresJDBCDao.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET CDR = ? WHERE ID = ? ";		
			log.info("script --> "+script);
			PreparedStatement pstmt = conn.prepareStatement(script);
			pstmt.setBytes(1, tbComprobante.getCdr());
			pstmt.setLong(2, tbComprobante.getId());
			pstmt.executeUpdate();					
		}catch (Exception e) {
			conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateFileCDROse Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	
	
	public void updateFileMensajeSunat(Documento tbComprobante) throws SQLException{
		log.info("updateFileMensajeSunat --> "+tbComprobante.getId());
		try {			
			conn = PostGresJDBCDao.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET MENSAJE_SUNAT = ? WHERE ID = ? ";		
			log.info("script --> "+script);
			PreparedStatement pstmt = conn.prepareStatement(script);
			pstmt.setBytes(1, tbComprobante.getMensajeSunat());
			pstmt.setLong(2, tbComprobante.getId());
			pstmt.executeUpdate();					
		}catch (Exception e) {
			conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateFileMensajeSunat Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	
	
	public void updateError(Documento tbComprobante) throws SQLException{
		log.info("updateError --> "+tbComprobante.getId());
		try {			
			conn = PostGresJDBCDao.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET " + 
					" ERROR_COMPROBANTE = '"+tbComprobante.getErrorComprobante()+"' , " +
					" ERROR_NUMERO = "+tbComprobante.getErrorNumero()+" , " +
					" ERROR_DESCRIPCION = "+tbComprobante.getErrorDescripcion()+" , " +
					" ERROR_LOG = "+tbComprobante.getErrorLog()+" , " +
					" RESPUESTA_SUNAT = "+tbComprobante.getRespuestaSunat()+"  " +
					" WHERE ID = "+tbComprobante.getId();
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateError Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}
	
	public void updateEstado(Documento tbComprobante) throws SQLException{
		log.info("updateEstado --> "+tbComprobante.getId());
		try {			
			conn = PostGresJDBCDao.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET " + 
					" ERROR_COMPROBANTE = '"+tbComprobante.getErrorComprobante()+"', " +
					" ESTADO = '"+tbComprobante.getEstado()+"' " +
					" WHERE ID = "+tbComprobante.getId();
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateEstado Exception \n"+errors);
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
