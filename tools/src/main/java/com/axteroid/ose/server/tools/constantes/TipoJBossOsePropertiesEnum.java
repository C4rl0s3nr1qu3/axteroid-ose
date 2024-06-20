package com.axteroid.ose.server.tools.constantes;

public enum TipoJBossOsePropertiesEnum {
	AXTEROID_OSE_APICHILE_UPDATESTATUS("axteroid.ose.apiChile.updateStatus","4"),
	AXTEROID_OSE_APICHILE_UPDATESTATUS_URL("axteroid.ose.apiChile.updateStatus.url","4"),
	AXTEROID_OSE_METAINF_FTL("axteroid.ose.meta-inf.ftl","6"),
	AXTEROID_OSE_METAINF_IMG("axteroid.ose.meta-inf.img","6"),
	AXTEROID_OSE_METAINF_JASPER("axteroid.ose.meta-inf.jasper","6"),
	AXTEROID_OSE_METAINF_QR("axteroid.ose.meta-inf.qr","6"),
	AXTEROID_OSE_METAINF_XSD("axteroid.ose.meta-inf.xsd","6"),
	AXTEROID_OSE_METAINF_XSL("axteroid.ose.meta-inf.xsl","6"),	
	AXTEROID_OSE_ENVIO_UBL_PROD_UNO("axteroid.ose.envio.ubl.prod.uno","10"),
	AXTEROID_OSE_ENVIO_UBL_PROD_DOS("axteroid.ose.envio.ubl.prod.dos","10"),		
	DB_MONGODB_OSE_HOST("db.mongodb.ose.host","20"),
	DB_MONGODB_OSE_PORT("db.mongodb.ose.port","20"),
	DB_MONGODB_OSE_DBSCHEMA("db.mongodb.ose.dbschema","20"),
	DB_MONGODB_OSE_USER("db.mongodb.ose.user","20"),
	DB_MONGODB_OSE_PASSWORD("db.mongodb.ose.password","20"),
	DB_POSTGRES_DBOSE_HOST("db.postgres.dbose.old.host","21"),
	DB_POSTGRES_DBOSE_USER("db.postgres.dbose.old.user","21"),
	DB_POSTGRES_DBOSE_PASSWORD("db.postgres.dbose.old.password","21"),	
	S3_REPOSITORY_ZIP("s3.repository.zip","30"),
	S3_REPOSITORY_UBL("s3.repository.ubl","30"),
	S3_REPOSITORY_CDR("s3.repository.cdr","30"),
	S3_REPOSITORY_AR("s3.repository.ar","30"),
	S3_REPOSITORY_BUCKETNAME("s3.repository.bucketname","31"),
	S3_REPOSITORY_LOCATION("s3.repository.location","31"),
	S3_REPOSITORY_ENDPOINTURL("s3.repository.endpointUrl","31"),
	S3_REPOSITORY_APIKEY("s3.repository.apikey","31"),	
	S3_REPOSITORY_SERVICEINSTANCEID("s3.repository.serviceinstanceid","31"),	
	SUNAT_SEND_CDR_PROD("sunat.send.cdr.prod","40"),
	SUNAT_SEND_CDR_PROD_UNO("sunat.send.cdr.prod.uno","40"), 
	SUNAT_SEND_CDR_PROD_DOS("sunat.send.cdr.prod.dos","40"),
	SUNAT_SEND_CDR_PROD_WS("sunat.send.cdr.prod.ws","40"),
	SUNAT_SEND_CDR_BETA("sunat.send.cdr.beta","41"),
	SUNAT_SEND_CDR_BETA_UNO("sunat.send.cdr.beta.uno","41"), 
	SUNAT_SEND_CDR_BETA_DOS("sunat.send.cdr.beta.dos","41"),
	SUNAT_SEND_CDR_BETA_WS("sunat.send.cdr.beta.ws","41"),
	SUNAT_SEND_CDR_CERTIFICATE_RUC("sunat.send.cdr.certificate.ruc","41"),
	SUNAT_SEND_CDR_CERTIFICATE_NAME("sunat.send.cdr.certificate.name","41"), 
	SUNAT_SEND_CDR_CERTIFICATE_STORE_PASSWORD("sunat.send.cdr.certificate.storePassword","41"),
	SUNAT_SEND_CDR_CERTIFICATE_KEY_PASSWORD("sunat.send.cdr.certificate.keyPassword","41"),
	SUNAT_SEND_CDR_CERTIFICATE_PRIVATE_KEY_ALIAS("sunat.send.cdr.certificate.privateKeyAlias","41"),
	SUNAT_SEND_CDR_CERTIFICATE_TYPE("sunat.send.cdr.certificate.type","41"),
	SUNAT_SEND_CDR_CERTIFICATE_SIG_PROP_FILE("sunat.send.cdr.certificate.sigPropFile","41"),
	SUNAT_LIST_CONSULTA("sunat.list.consulta","42"),
	SUNAT_LIST_CONSULTA_URL("sunat.list.consulta.url","42"),
	SUNAT_LIST_DB_POSTGRES_HOST("sunat.list.db.postgres.host","42"),
	SUNAT_LIST_DB_POSTGRES_USER("sunat.list.db.postgres.user","42"),
	SUNAT_LIST_DB_POSTGRES_PASSWORD("sunat.list.db.postgres.password","42"),		
	SUNAT_RES_ENVIO_COMPROBANTE("sunat.rest.envio.comprobante","43"),	
	SUNAT_RES_ENVIO_COMPROBANTE_TOKEN_API("sunat.rest.envio.comprobante.token.api","43"),
	SUNAT_RES_ENVIO_COMPROBANTE_ENVIAR_API("sunat.rest.envio.comprobante.enviar.api","43"),
	SUNAT_RES_ENVIO_COMPROBANTE_CONSULTAR_API("sunat.rest.envio.comprobante.consultar.api","43"),
	SUNAT_WS_OLITWSCONSCPEGEM_CONSULTA("sunat.ws.olitwsconscpegem.consulta","44"),
	SUNAT_WS_OLITWSCONSCPEGEM_CONSULTA_URL("sunat.ws.olitwsconscpegem.consulta.url","44");	
	
	private String codigo;
	private String descripcion;
	
	private TipoJBossOsePropertiesEnum(String codigo, String descripcion){
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
	
	public TipoJBossOsePropertiesEnum[] getValues() {
        return TipoJBossOsePropertiesEnum.values();
    }
}
