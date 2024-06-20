package com.axteroid.ose.server.tools.constantes;

public enum TipoAvatarPropertiesEnum {
	DESCARGA_SUNAT("descargaSunatOSE","1"),
	DESCARGA_SUNAT_HORA("descargaSunatOSE.hora","1"),
	MIGRA_DB2_TM_TS("migraDB2TM2TS","2"),
	MIGRA_DB2_TM_TS_HORAINI("migraDB2TM2TS.horaIni","2"),
	MIGRA_DB2_TM_TS_HORAFIN("migraDB2TM2TS.horaFin","2"),
	
	ACTUALIZAR_ESTADOS_GENERAL("actualizarEstados.general","1"), 
	ACTUALIZAR_ESTADOS_GENERAL_HORAINI("actualizarEstados.general.fecha.ini","1"),
	ACTUALIZAR_ESTADOS_GENERAL_HORAFIN("actualizarEstados.general.fecha.fin","1"),	

	GESTIONAR_JBOSS("gestionarJBoss","1"), 
	GESTIONAR_JBOSS_IP("gestionarJBoss.ip","1"),
	GESTIONAR_JBOSS_USUARIO("gestionarJBoss.usuario","1"),
	GESTIONAR_JBOSS_CLAVE("gestionarJBoss.clave","1"),	
	GESTIONAR_JBOSS_REINICIAR("gestionarJBoss.reiniciar","1"),
	GESTIONAR_JBOSS_REINICIAR_SHELL("gestionarJBoss.reiniciar.shell","1"),
	
	COMUNICA_SUNAT_POSTGRE_THREAD("comunicaSunatPostGres.thread","1"),
	COMUNICA_SUNAT_POSTGRE_THREAD_RUN("comunicaSunatPostGres.thread.run","1"),
	COMUNICA_SUNAT_POSTGRE_THREAD_SLEEP("comunicaSunatPostGres.thread.sleep","1"),
	
	COMUNICA_SUNAT_POSTGRE_30("comunicaSunatPostGres30","4"),
	COMUNICA_SUNAT_POSTGRE_30_URL("comunicaSunatPostGres30.url","4"), 
	COMUNICA_SUNAT_POSTGRE_30_CONTAR("comunicaSunatPostGres30.contar","4"),
	COMUNICA_SUNAT_POSTGRE_30FECHA("comunicaSunatPostGres30FECHA","4"),
	COMUNICA_SUNAT_POSTGRE_30FECHA_URL("comunicaSunatPostGres30FECHA.url","4"), 
	COMUNICA_SUNAT_POSTGRE_30FECHA_FECHA_INI("comunicaSunatPostGres30FECHA.fecha.ini","4"), 	
	COMUNICA_SUNAT_POSTGRE_30FECHA_CONTAR("comunicaSunatPostGres30FECHA.contar","4"),
	COMUNICA_SUNAT_POSTGRE_30_TD("comunicaSunatPostGres30TD","4"),
	COMUNICA_SUNAT_POSTGRE_30_TD_URL("comunicaSunatPostGres30TD.url","4"),
	COMUNICA_SUNAT_POSTGRE_30_TD_CONTAR("comunicaSunatPostGres30TD.contar","4"),
	COMUNICA_SUNAT_POSTGRE_30_TD_DOCUMENTO("comunicaSunatPostGres30TD.documento","4"),
	COMUNICA_SUNAT_POSTGRE_VARIOS("comunicaSunatPostGresVARIOS","7"),
	COMUNICA_SUNAT_POSTGRE_VARIOS_URL("comunicaSunatPostGresVARIOS.url","7"),
	COMUNICA_SUNAT_POSTGRE_VARIOS_ESTADO("comunicaSunatPostGresVARIOS.estado","7"),
	COMUNICA_SUNAT_POSTGRE_VARIOS_RESPUESTASUNAT("comunicaSunatPostGresVARIOS.respuestasunat","7"),	
	COMUNICA_SUNAT_POSTGRE_VARIOS_CONTAR("comunicaSunatPostGresVARIOS.contar","7"),		
	COMUNICA_SUNAT_POSTGRE_VARIOS_LN("comunicaSunatPostGresVARIOSLN","6"),
	COMUNICA_SUNAT_POSTGRE_VARIOS_LN_URL("comunicaSunatPostGresVARIOSLN.url","6"),
	COMUNICA_SUNAT_POSTGRE_VARIOS_LN_ESTADO("comunicaSunatPostGresVARIOSLN.estado","6"),
	COMUNICA_SUNAT_POSTGRE_VARIOS_LN_RESPUESTASUNAT("comunicaSunatPostGresVARIOSLN.respuestasunat","6"),
	COMUNICA_SUNAT_POSTGRE_VARIOS_LN_CONTAR("comunicaSunatPostGresVARIOSLN.contar","6"),		
	COMUNICA_SUNAT_POSTGRE_ESTADO("comunicaSunatPostGresESTADO","15"),
	COMUNICA_SUNAT_POSTGRE_ESTADO_URL("comunicaSunatPostGresESTADO.url","15"),
	COMUNICA_SUNAT_POSTGRE_ESTADO_CONTAR("comunicaSunatPostGresESTADO.contar","15"),	
	COMUNICA_SUNAT_POSTGRE_ESTADO_ESTADO("comunicaSunatPostGresESTADO.estado","15"),	
	COMUNICA_SUNAT_POSTGRE_ESTADO_LN("comunicaSunatPostGresESTADOLN","16"),
	COMUNICA_SUNAT_POSTGRE_ESTADO_LN_URL("comunicaSunatPostGresESTADOLN.url","16"),
	COMUNICA_SUNAT_POSTGRE_ESTADO_LN_CONTAR("comunicaSunatPostGresESTADOLN.contar","16"),	
	COMUNICA_SUNAT_POSTGRE_ESTADO_LN_ESTADO("comunicaSunatPostGresESTADOLN.estado","16"),	
	COMUNICA_SUNAT_POSTGRE_GROUP("comunicaSunatPostGresGROUP","17"),
	COMUNICA_SUNAT_POSTGRE_GROUP_URL("comunicaSunatPostGresGROUP.url","17"),
	COMUNICA_SUNAT_POSTGRE_GROUP_CONTAR("comunicaSunatPostGresGROUP.contar","17"),	
	COMUNICA_SUNAT_POSTGRE_GROUP_ESTADO("comunicaSunatPostGresGROUP.estado","17"),	
	COMUNICA_SUNAT_POSTGRE_GROUP_LN("comunicaSunatPostGresGROUPLN","18"),
	COMUNICA_SUNAT_POSTGRE_GROUP_LN_URL("comunicaSunatPostGresGROUPLN.url","18"),
	COMUNICA_SUNAT_POSTGRE_GROUP_LN_CONTAR("comunicaSunatPostGresGROUPLN.contar","18"),	
	COMUNICA_SUNAT_POSTGRE_GROUP_LN_ESTADO("comunicaSunatPostGresGROUPLN.estado","18"),
	
	ACTUALIZAR_IBM_PG_MONGO_FACTURA("actualizaIBM_PG_MONGO_FACTURA","8"),
	ACTUALIZAR_IBM_PG_MONGO_FACTURA_PROCESO("actualizaIBM_PG_MONGO_FACTURA.proceso","8"),
	ACTUALIZAR_IBM_PG_MONGO_FACTURA_YEAR("actualizaIBM_PG_MONGO_FACTURA.year","8"),
	ACTUALIZAR_IBM_PG_MONGO_FACTURA_MES_INI("actualizaIBM_PG_MONGO_FACTURA.mesINI","8"),
	ACTUALIZAR_IBM_PG_MONGO_FACTURA_MES_FIN("actualizaIBM_PG_MONGO_FACTURA.mesFIN","8"),
	ACTUALIZAR_IBM_PG_MONGO_FACTURA_DIA_INI("actualizaIBM_PG_MONGO_FACTURA.diaINI","8"),
	ACTUALIZAR_IBM_PG_MONGO_FACTURA_DIA_FIN("actualizaIBM_PG_MONGO_FACTURA.diaFIN","8"),	
	ACTUALIZAR_IBM_PG_FACTURA_CPE("actualizaIBM_PG_FACTURA_CPE","9"),
	ACTUALIZAR_IBM_PG_FACTURA_CPE_PROCESO("actualizaIBM_PG_FACTURA_CPE.proceso","9"),
	ACTUALIZAR_IBM_PG_FACTURA_CPE_FECHA_INI("actualizaIBM_PG_FACTURA_CPE.fecha.ini","9"),
	ACTUALIZAR_IBM_PG_FACTURA_CPE_FECHA_FIN("actualizaIBM_PG_FACTURA_CPE.fecha.fin","9"),
	ACTUALIZAR_IBM_PG_FACTURA_CPE_TD("actualizaIBM_PG_FACTURA_CPE.td","9"),
	CLIENTE_ERROR_PFF("cliente_ERROR_PFF","12"),
	CLIENTE_ERROR_PFF_IP("cliente_ERROR_PFF.ip","12"),
	CLIENTE_ERROR_PFF_USUARIO("cliente_ERROR_PFF.usuario","12"),
	CLIENTE_ERROR_PFF_CLAVE("cliente_ERROR_PFF.clave","12"),
	CLIENTE_ERROR_PFF_CONTAR("cliente_ERROR_PFF.contar","12"),
	CLIENTE_ERROR_PFF_URL("cliente_ERROR_PFF.url","12"),
	CLIENTE_ERROR_PFF_ERROR_NUMERO("cliente_ERROR_PFF.error_numero","12"),
	CLIENTE_ERROR_PFF_FECHA_INI_JBOSS("cliente_ERROR_PFF.fecha.ini.jboss","12"),
	CLIENTE_ERROR_PFF_FECHA_FIN_JBOSS("cliente_ERROR_PFF.fecha.fin.jboss","12"),
	CLIENTE_ERROR_PFF_FECHA_INI_DB("cliente_ERROR_PFF.fecha.ini.db","12"),
	CLIENTE_ERROR_PFF_FECHA_FIN_DB("cliente_ERROR_PFF.fecha.fin.db","12"),
	CLIENTE_ERROR_PFF_RUC("cliente_ERROR_PFF.ruc","12"),
	CLIENTE_ERROR_PFF_PROCESO("cliente_ERROR_PFF.proceso","12"),
	DB_POSTGRES_ROBOT_HOST("db.postgres.robot.host","14"),
	DB_POSTGRES_ROBOT_USER("db.postgres.robot.user","14"),
	DB_POSTGRES_ROBOT_PASSWORD("db.postgres.robot.password","14"),
	S3_REPOSITORY_ZIP("s3.repository.zip","15"),
	S3_REPOSITORY_UBL("s3.repository.ubl","15"),
	S3_REPOSITORY_CDR("s3.repository.cdr","15"),
	S3_REPOSITORY_AR("s3.repository.ar","15"),
	S3_REPOSITORY_BUCKETNAME("s3.repository.bucketname","15"),
	S3_REPOSITORY_LOCATION("s3.repository.location","15"),
	S3_REPOSITORY_ENDPOINTURL("s3.repository.endpointUrl","15"),
	S3_REPOSITORY_APIKEY("s3.repository.apikey","15"),	
	S3_REPOSITORY_SERVICEINSTANCEID("s3.repository.serviceinstanceid","15"),
	SUNAT_LIST_CONSULTA("sunat.list.consulta","16"),
	SUNAT_LIST_CONSULTA_URL("sunat.list.consulta.url","16"),
	SUNAT_LIST_DB_POSTGRES_HOST("sunat.list.db.postgres.host","16"),
	SUNAT_LIST_DB_POSTGRES_USER("sunat.list.db.postgres.user","16"),
	SUNAT_LIST_DB_POSTGRES_PASSWORD("sunat.list.db.postgres.password","16"),	
	SUNAT_WS_OLITWSCONSCPEGEM_CONSULTA("ose.consultas.sunat.olitwsconscpegem","17"),
	SUNAT_WS_OLITWSCONSCPEGEM_CONSULTA_URL("ose.consultas.sunat.olitwsconscpegem.url","17"),; 
	
	private String codigo;
	private String descripcion;
	
	private TipoAvatarPropertiesEnum(String codigo, String descripcion){
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public TipoAvatarPropertiesEnum[] getValues() {
        return TipoAvatarPropertiesEnum.values();
    }
}
