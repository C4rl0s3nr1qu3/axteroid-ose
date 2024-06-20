package com.axteroid.ose.server.avatar.jdbc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoAvatarPropertiesEnum;
import com.axteroid.ose.server.tools.util.DirUtil;

public class DaoSunatListPGJdbc {
	private static final Logger log = LoggerFactory.getLogger(DaoSunatListPGJdbc.class);
	static protected Connection conn;
	
	public static Connection getConnectionSunatList()  {
		try {
			// Load class into memory
			Class.forName(Constantes.JDBCCLASSNAME_PG);
			Map<String, String> mapRobotProperties = DirUtil.getMapRobotProperties();
			String host = mapRobotProperties.get(TipoAvatarPropertiesEnum.SUNAT_LIST_DB_POSTGRES_HOST.getCodigo());
       		String user = mapRobotProperties.get(TipoAvatarPropertiesEnum.SUNAT_LIST_DB_POSTGRES_USER.getCodigo());     					
       		String password = mapRobotProperties.get(TipoAvatarPropertiesEnum.SUNAT_LIST_DB_POSTGRES_PASSWORD.getCodigo());			
			conn = DriverManager.getConnection(host, user, password);
			log.info("SE CONECTO EXITOSAMENTE SUNATLIST ");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			log.error("getConnectionSunatList Exception \n" + errors);
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
