package com.axteroid.ose.server.rulesjdbc.jdbc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoJBossOsePropertiesEnum;
import com.axteroid.ose.server.tools.util.DirUtil;

public class SunatListJDBCDao {
	private static final Logger log = LoggerFactory.getLogger(SunatListJDBCDao.class);
	static protected Connection conn;
	
	public static Connection getConnectionSunatList() {
		try {
			Class.forName(Constantes.JDBCCLASSNAME_PG);
			Map<String, String> mapOseProperties = DirUtil.getMapJBossProperties();				
			String host = mapOseProperties.get(TipoJBossOsePropertiesEnum.SUNAT_LIST_DB_POSTGRES_HOST.getCodigo());
       		String user = mapOseProperties.get(TipoJBossOsePropertiesEnum.SUNAT_LIST_DB_POSTGRES_USER.getCodigo());     					
       		String password = mapOseProperties.get(TipoJBossOsePropertiesEnum.SUNAT_LIST_DB_POSTGRES_PASSWORD.getCodigo());
       		log.info("SUNATLIST: {} | {} | {}",host, user, password);
			conn = DriverManager.getConnection(host, user, password);
			
			log.info("SE CONECTO EXITOSAMENTE SUNATLIST "+user);
								
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			log.error("getConnectionSunatList Exception \n" + errors);
		} 
		return conn;
	}
	
	public static Connection abrirConexion(){
		try {
			if ((conn != null) && !(conn.isClosed())) { 
				log.info("ReStart Connected: "+!conn.isClosed());
			}else {
				conn = SunatListJDBCDao.getConnectionSunatList();
				log.info("New Connected: "+!conn.isClosed());
			}
		} catch (SQLException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			log.error("abrirConexion Exception \n" + errors);
		}
		return conn;
	}
	
	protected Connection abrirConn(){
		try {
			if ((conn != null) && !(conn.isClosed())) { 
				log.info("ReStart Connected: "+!conn.isClosed());
			}else {
				conn = SunatListJDBCDao.getConnectionSunatList();
				log.info("New Connected: "+!conn.isClosed());
			}
		} catch (SQLException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			log.error("abrirConn Exception \n" + errors);
		}
		return conn;
	}
    
	public static void cerrarConexion(){
        try {
			if (conn != null && !conn.isClosed()) {				
				conn.close();
				log.info("Super Connected: "+conn.isClosed());
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cerrarConexion Exception \n"+errors);
		}
	}
			
}
