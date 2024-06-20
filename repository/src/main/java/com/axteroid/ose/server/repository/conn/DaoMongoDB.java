package com.axteroid.ose.server.repository.conn;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.constantes.TipoJBossOsePropertiesEnum;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.MongoSecurityException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import com.mongodb.ServerAddress;

public class DaoMongoDB {
	//private final static Logger log =  Logger.getLogger(DaoMongoDBJDBC.class);
	private static final Logger log = LoggerFactory.getLogger(DaoMongoDB.class);
	protected MongoClient mongoClientOSE;
	protected MongoDatabase mongoDatabaseOSE;	
	
	protected void  conexionMongoDatabaseOSE() {  
		try{
			Map<String, String> mapOseProperties = DirUtil.getMapJBossProperties();
			String DBSCHEMA = mapOseProperties.get(TipoJBossOsePropertiesEnum.DB_MONGODB_OSE_DBSCHEMA.getCodigo());
			mongoDatabaseOSE = mongoClientOSE.getDatabase(DBSCHEMA);
		}catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("conexionMongoDatabaseOSE MongoSecurityException \n"+errors);	
			System.out.println("conexionMongoDatabaseOSE MongoSecurityException \n"+errors);	
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("conexionMongoDatabaseOSE Exception \n"+errors);
			System.out.println("conexionMongoDatabaseOSE Exception \n"+errors);
		}
	}

	protected void conexionMongoClientOSE() {
		try{        
			Map<String, String> mapOseProperties = DirUtil.getMapJBossProperties();
			String mgdbHOST = mapOseProperties.get(TipoJBossOsePropertiesEnum.DB_MONGODB_OSE_HOST.getCodigo());
			//String PORT = mapOseProperties.get(TipoFileOseProperEnum.DB_MONGODB_AXTEROIDOSE_PORT.getCodigo());
			//Integer mgdbPORT = (PORT != null || !PORT.isEmpty()) ? Integer.parseInt(PORT) : 0;
			String mgdbSCHEMA = mapOseProperties.get(TipoJBossOsePropertiesEnum.DB_MONGODB_OSE_DBSCHEMA.getCodigo());
			String mgdbUSER = mapOseProperties.get(TipoJBossOsePropertiesEnum.DB_MONGODB_OSE_USER.getCodigo());
			String mgdbPASSWORD = mapOseProperties.get(TipoJBossOsePropertiesEnum.DB_MONGODB_OSE_PASSWORD.getCodigo());
        	
			String urlMongoDB = "mongodb://"+mgdbUSER+":"+mgdbPASSWORD+"@"+mgdbHOST+"/"+mgdbSCHEMA+"?replicaset=replset&authSource=admin&ssl=true";         

			mongoClientOSE = MongoClients.create(
				    MongoClientSettings.builder().applyConnectionString(new ConnectionString(urlMongoDB))
				    .applyToSocketSettings(builder ->
				        builder.connectTimeout(400, TimeUnit.SECONDS)
				        .readTimeout(200, TimeUnit.SECONDS))
				    .applyToSslSettings(builder ->builder.enabled(true))
				    .build());
			
		}catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("conexionMongoClientOSE MongoSecurityException \n"+errors);		
			//System.out.println("conexionMongoClientOSE MongoSecurityException \n"+errors);	
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("conexionMongoClientOSE Exception \n"+errors);
			//System.out.println("conexionMongoClientOSE Exception \n"+errors);
		}
    }
	
	protected void conexionMongoClientOSESunat(){
		try{
			Map<String, String> mapOseProperties = DirUtil.getMapJBossProperties();
			String mgdbHOST = mapOseProperties.get(TipoJBossOsePropertiesEnum.DB_MONGODB_OSE_HOST.getCodigo());
			String PORT = mapOseProperties.get(TipoJBossOsePropertiesEnum.DB_MONGODB_OSE_PORT.getCodigo());
			Integer mgdbPORT = (PORT != null || !PORT.isEmpty()) ? Integer.parseInt(PORT) : 0;
			String mgdbSCHEMA = mapOseProperties.get(TipoJBossOsePropertiesEnum.DB_MONGODB_OSE_DBSCHEMA.getCodigo());
			String mgdbUSER = mapOseProperties.get(TipoJBossOsePropertiesEnum.DB_MONGODB_OSE_USER.getCodigo());
			String mgdbPASSWORD = mapOseProperties.get(TipoJBossOsePropertiesEnum.DB_MONGODB_OSE_PASSWORD.getCodigo());
			ServerAddress serverAddress = new ServerAddress(mgdbHOST, mgdbPORT);
			MongoCredential credential = MongoCredential.createCredential(mgdbUSER, mgdbSCHEMA, mgdbPASSWORD.toCharArray()); 
			List<MongoCredential> creds = new ArrayList<MongoCredential>();
			creds.add(credential);
			//mongoClientOSE = new MongoClients(serverAddress);
		}catch(MongoSecurityException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("conexionMongoClientOSESunat MongoSecurityException \n"+errors);		
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("conexionMongoClientOSESunat Exception \n"+errors);
		}
    }
			
}
