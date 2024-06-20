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

import com.axteroid.ose.server.avatar.dao.ComprobantesPagoElectronicosPGDao;
import com.axteroid.ose.server.avatar.jdbc.DaoPostGresJDBC;

public class ComprobantesPagoElectronicosPGDaoImpl extends DaoPostGresJDBC 
	implements ComprobantesPagoElectronicosPGDao{
	private static final Logger log = LoggerFactory.getLogger(ComprobantesPagoElectronicosPGDaoImpl.class);
	private CallableStatement cstmt;
	private ResultSet rset = null;
	private PreparedStatement pstmt;
	private Statement stmt;
	
	public int countComprobantesPagoElectronicosState2(Long id) throws SQLException{					
		int countState2ID = 0;
		try {
			String sql = "SELECT count(*) FROM AXTEROIDOSE.ts_comprobantes_pago_electronicos "
					+ " WHERE ID = "+id+" and ind_estado_cpe = '2' ";
			//log.info("sql (1) "+sql);
			conn = DaoPostGresJDBC.getConnectionOSE();
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				countState2ID = rset.getInt(1);
			}		
			rset.close();
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("countComprobantesPagoElectronicosState2 Exception \n"+errors);
		} finally {
			cerrarDao();
		}				
		return countState2ID;
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
