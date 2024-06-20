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

import com.axteroid.ose.server.avatar.dao.SunatParametroPGDao;
import com.axteroid.ose.server.avatar.jdbc.DaoPostGresJDBC;

public class SunatParametroPGDaoImpl extends DaoPostGresJDBC implements SunatParametroPGDao{
	private static final Logger log = LoggerFactory.getLogger(SunatParametroPGDaoImpl.class);
	private CallableStatement cstmt;
	private ResultSet rset = null;
	private PreparedStatement pstmt;
	private Statement stmt;
	
	public void getSunatParametro(String codParametro, String codArgumento) {					
		try {			
			String sql = "SELECT * FROM AXTEROIDOSE.SUNAT_PARAMETRO "
					+ " WHERE COD_PARAMETRO = '"+codParametro+"' and COD_ARGUMENTO = '"+codArgumento+"' ";
			if(codArgumento.isBlank()) {
				sql = "SELECT * FROM AXTEROIDOSE.SUNAT_PARAMETRO "
						+ " WHERE COD_PARAMETRO = '"+codParametro+"' ";
			}
			log.info("sql "+sql);
			conn = DaoPostGresJDBC.getConnectionOSE();
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				log.info("{} | {} | {} ",rset.getString(1),rset.getString(2),rset.getString(3));
			}		
			rset.close();
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getSunatParametro Exception \n"+errors);
		} finally {
			cerrarDao();
		}						
	}

	public void getSunatParametro(String codParametro) {					
		try {
			String sql = "SELECT * FROM AXTEROIDOSE.SUNAT_PARAMETRO "
					+ " WHERE COD_PARAMETRO = '"+codParametro+"' ";
			log.info("sql "+sql);
			conn = DaoPostGresJDBC.getConnectionOSE();
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				log.info("{} | {} | {} ",rset.getString(1),rset.getString(2),rset.getString(3));
			}		
			rset.close();
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getSunatParametro Exception \n"+errors);
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
