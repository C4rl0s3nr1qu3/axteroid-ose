package com.axteroid.ose.server.avatar.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.dao.DocumentoPGDao;
import com.axteroid.ose.server.avatar.jdbc.DaoPostGresJDBC;
import com.axteroid.ose.server.jpa.model.Documento;

public class DocumentoPGDaoImpl extends DaoPostGresJDBC implements DocumentoPGDao{
	private static final Logger log = LoggerFactory.getLogger(DocumentoPGDaoImpl.class);
	private CallableStatement cstmt;
	private ResultSet rset = null;
	private PreparedStatement pstmt;
	private Statement stmt;
		

	public void updateDocumentoAjusteGeneral() throws SQLException{
		//log.info("updateDocumentoAjusteGeneral");
		String script = "";
		try {			
			script = "UPDATE AXTEROIDOSE.DOCUMENTO set estado = '30',respuesta_sunat = null "
					+ " where  tipo_documento in ('09','31') and ESTADO in ('60')  and RESPUESTA_SUNAT in ('0306')";		
			log.info("updateDocumentoAjusteGeneral | "+script);
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);	
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateDocumentoAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}
	
	
	public void updateDocumentoEstado30() throws SQLException{
		//log.info("updateDocumentoAjusteGeneral");
		String script = "";
		try {			
			script = "UPDATE AXTEROIDOSE.DOCUMENTO set ERROR_COMPROBANTE = '0', ESTADO = '30' "
					+ " where ESTADO = '10' and ERROR_COMPROBANTE <> '0' "
					+ " and cdr is not null and respuesta_sunat is null and mensaje_sunat is null ";		
			log.info("7) script --> "+script);
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateDocumentoEstado30 Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	
	
	
	public void updateErrorLog(com.axteroid.ose.server.jpa.model.Documento tbComprobante) throws SQLException{
		log.info("updateErrorLog: "+tbComprobante.getId()+" : "+tbComprobante.getNombre());
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET "+ 
				" ERROR_LOG = '"+tbComprobante.getErrorLog()+"' " +
				" WHERE ID = "+tbComprobante.getId();			
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
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
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '40' "
					+ " WHERE  ESTADO = '30' and  tipo_comprobante in ('RA','RC','RR')";		
			log.info("script --> "+script);
			PreparedStatement pstmt = conn.prepareStatement(script);
//			pstmt.setBytes(1, tbComprobante.getUbl());
//			pstmt.setBytes(2, tbComprobante.getCdr());
//			pstmt.setLong(3, tbComprobante.getId());
			pstmt.executeUpdate();					
		}catch (Exception e) {
			//conn.rollback();
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
			conn = DaoPostGresJDBC.getConnectionOSE();
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
			//conn.rollback();
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
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET UBL = ?, CDR = ? WHERE ID = ? ";		
			log.info("script --> "+script);
			PreparedStatement pstmt = conn.prepareStatement(script);
			pstmt.setBytes(1, tbComprobante.getUbl());
			pstmt.setBytes(2, tbComprobante.getCdr());
			pstmt.setLong(3, tbComprobante.getId());
			pstmt.executeUpdate();					
		}catch (Exception e) {
			//conn.rollback();
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
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET UBL = ?, CDR = ?, MENSAJE_SUNAT = ? WHERE ID = ? ";		
			log.info("script --> "+script);
			PreparedStatement pstmt = conn.prepareStatement(script);
			pstmt.setBytes(1, tbComprobante.getUbl());
			pstmt.setBytes(2, tbComprobante.getCdr());
			pstmt.setBytes(3, tbComprobante.getMensajeSunat());
			pstmt.setLong(4, tbComprobante.getId());
			pstmt.executeUpdate();	
		}catch (Exception e) {
			//conn.rollback();
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
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET " + 
					" ESTADO = '"+tbComprobante.getEstado()+"', " +
					" RESPUESTA_SUNAT = '"+tbComprobante.getRespuestaSunat()+"'  " +
					" WHERE ID = "+tbComprobante.getId();
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
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
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET " + 
					" ESTADO = '"+tbComprobante.getEstado()+"', " +
					" RESPUESTA_SUNAT = '"+tbComprobante.getRespuestaSunat()+"',  " +
					" ERROR_LOG_SUNAT = '"+tbComprobante.getErrorLogSunat()+"'  " +
					" WHERE ID = "+tbComprobante.getId();
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateRespuestaSunat Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	

	public void updateEstadoRespuestaLogSunatQA(Documento tbComprobante) throws SQLException{
		log.info("updateEstadoRespuestaLogSunatQA --> "+tbComprobante.getId());
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET " + 
					" ESTADO = '"+tbComprobante.getEstado()+"', " +
					" RESPUESTA_SUNAT = '"+tbComprobante.getRespuestaSunat()+"',  " +
					" ERROR_LOG_SUNAT = '"+tbComprobante.getErrorLogSunat()+"',  " +
					" FECHA_RESPUESTA_SUNAT = '"+tbComprobante.getFechaRespuestaSunat()+"',  " +
					" USER_MODI = '"+tbComprobante.getUserModi()+"',  " +
					" FECHA_MODI = '"+tbComprobante.getFechaModi()+"'  " +
					" WHERE ID = "+tbComprobante.getId();
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateEstadoRespuestaLogSunatQA Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	
	
	public void updateRespuestaMensajeLogSunat(Documento tbComprobante) throws SQLException{
		log.info("updateRespuestaMensajeLogSunat --> "+tbComprobante.getId());
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();	
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
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateRespuestaSunat Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	

	public void updateFileUBLOseServer(Documento tbComprobante) throws SQLException{
		log.info("updateFileCDROseServer --> "+tbComprobante.getId());
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET UBL = ? WHERE ID = ? ";		
			log.info("script --> "+script);
			PreparedStatement pstmt = conn.prepareStatement(script);
			pstmt.setBytes(1, tbComprobante.getUbl());
			pstmt.setLong(2, tbComprobante.getId());
			pstmt.executeUpdate();	
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateFileCDROseServer Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	
	public void updateFileCDROseServer(Documento tbComprobante) throws SQLException{
		log.info("updateFileCDROseServer --> "+tbComprobante.getId());
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET CDR = ? WHERE ID = ? ";		
			log.info("script --> "+script);
			PreparedStatement pstmt = conn.prepareStatement(script);
			pstmt.setBytes(1, tbComprobante.getCdr());
			pstmt.setLong(2, tbComprobante.getId());
			pstmt.executeUpdate();					
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateFileCDROseServer Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	
	
	public void updateFileMensajeSunat(Documento tbComprobante) throws SQLException{
		log.info("updateFileMensajeSunat --> "+tbComprobante.getId());
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET MENSAJE_SUNAT = ? WHERE ID = ? ";		
			log.info("script --> "+script);
			PreparedStatement pstmt = conn.prepareStatement(script);
			pstmt.setBytes(1, tbComprobante.getMensajeSunat());
			pstmt.setLong(2, tbComprobante.getId());
			pstmt.executeUpdate();					
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateFileCDROseServer Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	
	
	public void updateError(Documento tbComprobante) throws SQLException{
		log.info("updateError --> "+tbComprobante.getId());
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
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
			//conn.rollback();
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
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET " + 
					" ERROR_COMPROBANTE = '"+tbComprobante.getErrorComprobante()+"', " +
					" ESTADO = '"+tbComprobante.getEstado()+"' " +
					" WHERE ID = "+tbComprobante.getId();
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateEstado Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	
	
	public void updateIdContent(Documento tbComprobante) throws SQLException{
		log.info("updateIdCpntent --> "+tbComprobante.getId());
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET ID_CONTENT = ? WHERE ID = ? ";		
			log.info("script --> "+script);
			PreparedStatement pstmt = conn.prepareStatement(script);
			pstmt.setLong(1, tbComprobante.getId());
			pstmt.setLong(2, tbComprobante.getId());
			pstmt.executeUpdate();					
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateIdCpntent Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}
	
	public void updateTbComprobanteAjusteGeneral() throws SQLException{
		log.info("updateTbComprobanteAjusteGeneral");
		String script = "";
		try {			
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE set ERROR_COMPROBANTE = '0', ESTADO = '90' "
					+ " where ESTADO = '10' and ERROR_COMPROBANTE <> '0' "
					+ " and cdr is not null and respuesta_sunat = '0' and mensaje_sunat is not null ";		
			log.info("1) script --> "+script);
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);	
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}	
		try {
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE set ERROR_COMPROBANTE = '0', ESTADO = '90' "
					+ " where ESTADO = '10' and ERROR_COMPROBANTE <> '0' "
					+ " and respuesta_sunat = '0' and cdr is not null ";		
			log.info("3) script --> "+script);
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}	
		try {			
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE set ERROR_COMPROBANTE = '0', ESTADO = '60' "
					+ " where ESTADO = '10' and ERROR_COMPROBANTE <> '0' "
					+ " and respuesta_sunat <> '0' and cdr is not null ";		
			log.info("5) script --> "+script);	
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}	
		try {			
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE set ERROR_COMPROBANTE = '0', ESTADO = '30' "
					+ " where ESTADO = '10' and ERROR_COMPROBANTE <> '0' "
					+ " and cdr is not null and respuesta_sunat is null and mensaje_sunat is null ";		
			log.info("7) script --> "+script);
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}	
		try {			
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE set ESTADO = '20', ERROR_COMPROBANTE= '1' "
					+ " where ESTADO in ('30','40','50','60','70','71','72','73','74','75','78','79','80') "
					+ " and ERROR_COMPROBANTE <> '0' and cdr is null ";		
			log.info("9) script --> "+script);
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}	
		try {			
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE set ERROR_COMPROBANTE = '0' "
					+ " where ESTADO in ('30','40','50','60','70','71','72','73','74','75','76','78','79','80') "
					+ " and ERROR_COMPROBANTE <> '0' ";		
			log.info("11) script --> "+script);		
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);	
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}	
		try {			
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '70' "
					+ " WHERE  estado in ('30','40','60','50','80') "
					+ " and respuesta_sunat in ('0100','0109','0113','0130','0132','0133','0151','0155','0200','0306','0402','99','0099','100','132','133','138','158','160','200', "
					+ " '306','402','404','406','500') ";		
			log.info("13) script --> "+script);		
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}	
		try {			
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '70' "
					+ " WHERE  estado in ('40','50','60','80') "
					+ " and respuesta_sunat in ('0001','0002','0003','0004','0005','0006','0007','0008','0009','0010','0011','0012','0013','0014','0015') ";		
			log.info("17) script --> "+script);	
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);	
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}
		try {			
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '78' WHERE  estado in ('60') "
					+ " and respuesta_sunat in ('4000','2017','2760','1080','1032','2874','2762') ";		
			log.info("15) script --> "+script);	
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}	
		try {			
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '78' "
					+ " WHERE  estado in ('60','70','71') "
					+ " and respuesta_sunat in ('0306') and TIPO_COMPROBANTE in ('09')";		
			log.info("15) script --> "+script);	
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}	
		try {			
			script = "UPDATE AXTEROIDOSE.TB_CONTENT set error_content = '1' "
					+ " where error_content = '4' and FECHA_CREA>='2020-12-01 00:00:00' ";		
			log.info("19) script --> "+script);	
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);	
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}	
		try {			
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE set ESTADO = '20', ERROR_COMPROBANTE= '1' "
					+ " where ESTADO = '90' and ERROR_COMPROBANTE <> '0' and cdr is null ";		
			log.info("21) script --> "+script);	
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);	
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}	
		try {			
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE set ERROR_COMPROBANTE = '0' "
					+ " where ESTADO = '90' and ERROR_COMPROBANTE <> '0' ";		
			log.info("23) script --> "+script);		
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}	
		try {			
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE "
					+ " SET  ESTADO = '90', respuesta_sunat = '0', ERROR_NUMERO = '1032' "
					+ " where estado in ('78') AND respuesta_sunat in ('1032') ";		
			log.info("25) script --> "+script);
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);	
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}	
		try {			
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE "
					+ " SET  ESTADO = '90', respuesta_sunat = '0', ERROR_NUMERO = '2223' "
					+ " where estado in ('80') AND respuesta_sunat in ('2223') ";		
			log.info("27) script --> "+script);
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}	
		try {			
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE "
					+ " SET  ESTADO = '90', respuesta_sunat = '0', ERROR_NUMERO = '4000' "
					+ " where estado in ('78') AND respuesta_sunat in ('4000') ";		
			log.info("29) script --> "+script);
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);	
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}	
		try {			
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE "
					+ " SET  ESTADO = '90', respuesta_sunat = '0', ERROR_NUMERO = '2324' "
					+ " where estado in ('80') AND respuesta_sunat in ('2324') ";		
			log.info("31) script --> "+script);		
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);	
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}	
	}		

	public void updateTbComprobanteAjusteEstado() throws SQLException{
		log.info("updateTbComprobanteAjusteEstado");
		String script = "";
		try {					
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '70' "
					+ " WHERE  estado in ('30','40','60','50','80') "
					+ " and respuesta_sunat in ('0100','0109','0113','0130','0132','0133','0151','0155','0200','0306','0402','99','0099','100','132','133','138','158','160','200', "
					+ " '306','402','404','406','500') ";		
			log.info("13) script --> "+script);		
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteEstado Exception \n"+errors);
		} finally {
			cerrarDao();
		}
		try {	
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '70' "
					+ " WHERE  estado in ('40','50','60','80') "
					+ " and respuesta_sunat in ('0001','0002','0003','0004','0005','0006','0007','0008','0009','0010','0011','0012','0013','0014','0015') ";		
			log.info("15) script --> "+script);		
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);	
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteEstado Exception \n"+errors);
		} finally {
			cerrarDao();
		}		
		try {
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '78' WHERE  estado in ('60') "
					+ " and respuesta_sunat in ('4000','2017','2760','1080','1032','2874','2762') ";		
			log.info("16) script --> "+script);	
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteEstado Exception \n"+errors);
		} finally {
			cerrarDao();
		}
		try {			
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '78' "
					+ " WHERE  estado in ('60','70','71') "
					+ " and respuesta_sunat in ('0306') and TIPO_COMPROBANTE in ('09')";		
			log.info("17) script --> "+script);	
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteGeneral Exception \n"+errors);
		} finally {
			cerrarDao();
		}			
		try {	
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE "
					+ " SET  ESTADO = '90', respuesta_sunat = '0', ERROR_NUMERO = '1032' "
					+ " where estado in ('78') AND respuesta_sunat in ('1032') ";		
			log.info("25) script --> "+script);
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);	
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteEstado Exception \n"+errors);
		} finally {
			cerrarDao();
		}
		try {	
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE "
					+ " SET  ESTADO = '90', respuesta_sunat = '0', ERROR_NUMERO = '2223' "
					+ " where estado in ('80') AND respuesta_sunat in ('2223') ";		
			log.info("27) script --> "+script);
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteEstado Exception \n"+errors);
		} finally {
			cerrarDao();
		}
		try {	
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE "
					+ " SET  ESTADO = '90', respuesta_sunat = '0', ERROR_NUMERO = '4000' "
					+ " where estado in ('78') AND respuesta_sunat in ('4000') ";		
			log.info("29) script --> "+script);
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);	
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteEstado Exception \n"+errors);
		} finally {
			cerrarDao();
		}
		try {	
			script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE "
					+ " SET  ESTADO = '90', respuesta_sunat = '0', ERROR_NUMERO = '2324' "
					+ " where estado in ('80') AND respuesta_sunat in ('2324') ";		
			log.info("31) script --> "+script);	
			conn = DaoPostGresJDBC.getConnectionOSE();
			stmt = conn.createStatement();
			stmt.executeUpdate(script);	

		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteAjusteEstado Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}		

	public void updateTbComprobanteNoRC() throws SQLException{
		log.info("updateTbComprobanteNoRC");
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '71' "
					+ " WHERE  estado in ('70') and TIPO_COMPROBANTE not in ('RC') ";
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteNoRC Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}

	public void updateTbComprobanteNoRARCRR() throws SQLException{
		log.info("updateTbComprobanteNoRARCRR");
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '71' "
					+ " WHERE  estado in ('70') and TIPO_COMPROBANTE not in ('RA','RC','RR') ";
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteNoRC Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	
	
	public void updateTbComprobanteRARR() throws SQLException{
		log.info("updateTbComprobanteRARR");
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '72' "
					+ " WHERE  estado in ('70') and TIPO_COMPROBANTE in ('RA','RR') ";
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteNoRC Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}
	
	public void updateTbComprobanteRCFirst(String sAyer) throws SQLException{
		log.info("updateTbComprobanteRCFirst");
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  "
					+ " ESTADO = '74' "//, fecha_respuesta_sunat = '"+sAyer+" 00:00:00' "
					+ " WHERE  estado in ('70','71','72','73')"
					+ " and TIPO_COMPROBANTE in ('RC') and FECHA_CREA >= '"+sAyer+" 00:00:00' ";
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteRCFirst Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}

	public void updateTbComprobanteRCLast(String sAyer) throws SQLException{
		log.info("updateTbComprobanteRCLast");
		try {						
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  "
					+ " ESTADO = '73' "//, fecha_respuesta_sunat = '"+sAyer+" 00:00:00' "
					+ " WHERE  estado in ('70','71','72','74')"
					+ " and TIPO_COMPROBANTE in ('RC') and FECHA_CREA < '"+sAyer+" 00:00:00' ";
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteRCLast Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}
	public void updateTbComprobanteResumenErrorEstado() throws SQLException{
		log.info("updateTbComprobanteResumenErrorEstado");
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '76' WHERE  estado in ('80','71','72','73','74') and "
					+ "respuesta_sunat in ('2987','2989','2282','2323','2324','2990','2751','2398','2663','2750','0000','2105') ";
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteResumenErrorEstado Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}		

	public void updateTbComprobanteRARCRR() throws SQLException{
		log.info("updateTbComprobanteRARCRR");
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '72' "
					+ " WHERE  estado in ('70') and TIPO_COMPROBANTE in ('RA','RC','RR') ";
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteRARCRR Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}
	
	public void updateTbComprobanteResumenEELast(String sAyer) throws SQLException{
		log.info("updateTbComprobanteResumenEELast");
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '75' WHERE  estado in ('80') "
					+ " and respuesta_sunat in ('2987','2989','2282','2323','2324','2990','2751','2398','2663','2750','0000','2105') "
					+ " and FECHA_CREA < '"+sAyer+" 00:00:00' "
					+ " and USER_CREA not in ('NORTFARMA_20399497257' )";
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteResumenEELast Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	

	public void updateTbComprobanteResumenEEFirst(String sAyer) throws SQLException{
		log.info("updateTbComprobanteResumenEEFirst");
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '76' WHERE  estado in ('80') "
					+ " and respuesta_sunat in ('2987','2989','2282','2323','2324','2990','2751','2398','2663','2750','0000','2105') "
					+ " and FECHA_CREA >= '"+sAyer+" 00:00:00' "
					+ " and USER_CREA not in ('NORTFARMA_20399497257' )";
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteResumenEEFirst Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}
	
	public void updateTbComprobanteResumenEERUC() throws SQLException{
		log.info("updateTbComprobanteResumenEERUC");
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '77' WHERE  estado in ('80') "
					+ " and respuesta_sunat in ('2987','2989','2282','2323','2324','2990','2751','2398','2663','2750','0000','2105') "
					+ " and USER_CREA in ('NORTFARMA_20399497257' )";
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteResumenEERUC Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}
	///////////////// NO UTILIZADOS
	
	public void updateTbComprobanteFacturaEstado() throws SQLException{
		log.info("updateTbComprobanteFacturaEstado");
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '71' WHERE  estado in ('70') and TIPO_COMPROBANTE in ('01') ";
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteFacturaEstado Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}		
	
	public void updateTbComprobanteBvNcNdEstado() throws SQLException{
		log.info("updateTbComprobanteBvNcNdEstado");
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '72' WHERE  estado in ('70') and TIPO_COMPROBANTE not in ('01','RA','RC','RR') ";
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteBvNcNdEstado Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}		
	
	public void updateTbComprobanteResumenLastEstado() throws SQLException{
		log.info("updateTbComprobanteResumenLastEstado");
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '73' WHERE  estado in ('70','74') "
					+ "and TIPO_COMPROBANTE in ('RA','RC','RR') and FECHA_CREA < '2021-11-01 00:00:00'  ";
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteResumenLastEstado Exception \n"+errors);
		} finally {
			cerrarDao();
		}
	}	
	
	public void updateTbComprobanteResumenFirstEstado() throws SQLException{
		log.info("updateTbComprobanteResumenFirstEstado");
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String script = "UPDATE AXTEROIDOSE.TB_COMPROBANTE SET  ESTADO = '74' WHERE  estado in ('70','73') "
					+ "and TIPO_COMPROBANTE in ('RA','RC','RR') and FECHA_CREA >= '2021-11-01 00:00:00' ";
			log.info("script --> "+script);
			stmt = conn.createStatement();
			stmt.executeUpdate(script);		
		}catch (Exception e) {
			//conn.rollback();
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateTbComprobanteResumenFirstEstado Exception \n"+errors);
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
