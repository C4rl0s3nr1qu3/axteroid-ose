package com.axteroid.ose.server.avatar.jdbc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoAvatarPropertiesEnum;
import com.axteroid.ose.server.tools.util.DirUtil;

public class DaoPostGresJDBC {
	private static final Logger log = LoggerFactory.getLogger(DaoPostGresJDBC.class);
	static protected Connection conn; 
	
	public static Connection getConnectionOSE() {
		try {
			Class.forName(Constantes.JDBCCLASSNAME_PG);
			Map<String, String> mapRobotProperties = DirUtil.getMapRobotProperties();				
			String host = mapRobotProperties.get(TipoAvatarPropertiesEnum.DB_POSTGRES_ROBOT_HOST.getCodigo());
       		String user = mapRobotProperties.get(TipoAvatarPropertiesEnum.DB_POSTGRES_ROBOT_USER.getCodigo());     					
       		String password = mapRobotProperties.get(TipoAvatarPropertiesEnum.DB_POSTGRES_ROBOT_PASSWORD.getCodigo());
			//log.info("host "+host);
			//log.info("password "+password);
			//log.info("SE CONECTO EXITOSAMENTE IBM "+user);			
       		conn = DriverManager.getConnection(host, user, password);
			//log.info("host "+host);
			//log.info("password "+password);
			//log.info("SE CONECTO EXITOSAMENTE IBM "+user);
			
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			log.error("getConnectionOSE Exception \n" + errors);
		} 
		return conn;
	}
	
	public static Connection abrirConexion(){
		try {
			if ((conn != null) && !(conn.isClosed())) { 
				log.info("ReStart Connected: "+!conn.isClosed());
			}else {
				conn = DaoPostGresJDBC.getConnectionOSE();
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
				conn = DaoPostGresJDBC.getConnectionOSE();
				log.info("New Connected: "+!conn.isClosed());
			}
		} catch (SQLException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			log.error("abrirConexion Exception \n" + errors);
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
