package com.axteroid.ose.server.tools.constantes;

public interface Constantes {
	 
    // Data base Server POSTGRESQL
	static final String OSE_VERSION = "v.230327.230401-230821";
	static final String AVATAR_VERSION = "v.230327.230401-230821";
    static final String JDBCCLASSNAME_PG="org.postgresql.Driver";       
  
    // CONSULTAS SUNAT 
    static final String OSE_CONSULTAS_SUNAT_CONSULTA_INTEGRADA_TOCKEN = "https://api-seguridad.sunat.gob.pe/v1/clientesextranet/e66be698-d865-43b8-a933-48f37165a2ee/oauth2/token/"; 
    static final String OSE_CONSULTAS_SUNAT_CONSULTA_INTEGRADA_VALIDEZ = "https://api.sunat.gob.pe/v1/contribuyente/contribuyentes/"; 
    static final String OSE_CONSULTAS_SUNAT_CONSULTA_INTEGRADA = "http://172.19.35.157:8099/clienteCISUNAT/clienteConsultaIntegradaSUNAT/consultaValidezComprobante/"; 
    
	//  FTP SUNAT Servidor Produccion
    static final String SUNAT_SERVIDOR_PRODUCCION_FTP_1 = "https://e-descargaose1.sunat.gob.pe/ose/";    
    static final String SUNAT_FTP_PATH_1 = Constantes.SUNAT_SERVIDOR_PRODUCCION_FTP_1+"public/";
	static final String SUNAT_FTP_PATH_RUC_1 = Constantes.SUNAT_SERVIDOR_PRODUCCION_FTP_1+Constantes.CDR_RUC_OSE+"/";
	static final String SUNAT_FTP_PATH_RUC_CD_1 = Constantes.SUNAT_SERVIDOR_PRODUCCION_FTP_1+Constantes.CDR_RUC_OSE+"/cuadre/";
	static final String SUNAT_SERVIDOR_PRODUCCION_FTP_2 = "https://e-descargaose2.sunat.gob.pe/ose/";
	static final String SUNAT_FTP_PATH_2 = Constantes.SUNAT_SERVIDOR_PRODUCCION_FTP_2+"public/";
	static final String SUNAT_FTP_PATH_RUC_2 = Constantes.SUNAT_SERVIDOR_PRODUCCION_FTP_2+Constantes.CDR_RUC_OSE+"/";
	static final String SUNAT_FTP_PATH_RUC_CD_2 = Constantes.SUNAT_SERVIDOR_PRODUCCION_FTP_2+Constantes.CDR_RUC_OSE+"/cuadre/";
	    
    // VARIABLES	
	final String OSE_FTP = "F";
	final int SUNAT_FTP_PORT_SECURE = 22;	
	static int ReadXMLEventReader = 1;
	static int ReadXMLStreamReader = 2;			
	static int BUFFER_CONTENTFILE = 1024;
		
	static final String OSE_doLookup = "global/ol-ti-itcpe/";
	static final String OSE_CDEJB_RULES = "!com.axteroid.ose.server.rulesejb.rules.";
	static final String OSE_CDEJB_DAO = "!com.axteroid.ose.server.rulesejb.dao.";
	
	static final String OSE_FILE_XML = ".xml";
	static final String OSE_FILE_ZIP = ".zip";
	static final String OSE_FILE_TXT = ".txt";
	static final String CDR_PREFI = "R";
	static final String AR_PREFI = "AR";
	static final String GUION = "-";	

	static final String OSE_ZIP = "ZIP";
	static final String OSE_LT = "LT";
	static final String OSE_SPLIT = " @ ";
	static final String OSE_SPLIT_1 = ":";
	static final String OSE_SPLIT_2 = "-";
	static final String OSE_SPLIT_3 = ";";
	static final String OSE_SPLIT_4 = "[";
	static final int OSE_LOG_LENGTH_MAX = 495;
	static final int OSE_OBS_LENGTH_MAX = 12;
	static final int OSE_LOGDB_LENGTH_MAX = 245;
	static final int OSE_OBSDB_LENGTH_MAX = 45;
	static final int OSE_RS_LENGTH_MAX = 4;
	static final int OSE_TIMEOUT = 5000;	
	static final String OSE_NcCrypt = "7C91DE760FFEDCCDVVWZYYZ"; 
	// CERTIFICADO
//	static final String OSE_JKS_CERTIFICADO_2019 = "osecert_2019.jks";
//	static final String OSE_JKS_CERTIFICADO_2020 = "osecert_2020.jks";
//	static final String OSE_JKS_CERTIFICADO_2021 = "osecert_2021.jks";
//	static final String OSE_JKS_CERTIFICADO_2022 = "osecert_2022.jks";
//	static final String OSE_JKS_StorePassword = "B1lz1nk52019*";
//	static final String OSE_JKS_KeyPassword = "B1lz1nk52019*";	
//	static final String OSE_JKS_PrivateKeyAlias = "osecert";
//	static final String OSE_JKS_TYPE = "JKS";
	
//	static String CDR_RUC_OSE_B = "20610051406";
//	static final String OSE_JKS_CERTIFICADO_B = "cert.pfx";
//	static final String OSE_JKS_StorePassword_B = "Bizlinks$$2022$";
//	static final String OSE_JKS_KeyPassword_B = "Bizlinks$$2022$";	
//	static final String OSE_JKS_PrivateKeyAlias_B = "javier fernando draxl garcia rosell ruc:20478005017 (ac camerfirma perú certificados - 2016)";
	
	static final String OSE_JKS_CERTIFICADO = "AXTEROID20610051406.pfx";
	static final String OSE_JKS_StorePassword = "0710";
	static final String OSE_JKS_KeyPassword = "0710";	
	static final String OSE_JKS_PrivateKeyAlias = "te-daf44019-f245-4ab7-a5c4-39d9e579c0bf";
	static final String OSE_JKS_TYPE = "PKCS12";
	static final String FILE_SUNAT_SECURITY_PROPER = "sunatsecurity.properties";
	
	// CDR
	// RUC QUE FIRMA CDR
	static String CDR_RUC_OSE = "20610051406"; 
	static String CDR_SUNAT_PE = "PE:SUNAT";
	static String CDR_SUNAT_C6 = "urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo6";
	static String CDR_SUNAT_CR = "urn:pe:gob:sunat:cpe:see:gem:codigos:codigoretorno";
	static String CDR_RUC_VF = "|6|"+CDR_SUNAT_PE+"|"+CDR_SUNAT_C6;
	static String CDR_DIGEST_METHOD = "http://www.w3.org/2000/09/xmldsig#sha1";
	static String CDR_TRANSFORM = "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
	static String CDR_SIGNATURE_METHOD = "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
	static String CDR_CANONICALIZATION_METHOD = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
	static String CDR_SIGNATURE_ID = "signatureKG";
	
	// PROPERTIES JBOSS
	static final String FILE_JBOSS_PROPER = "jboss-ose.properties";
		
	// DIR OSE
	static final String DIR_RESOURCES = "META-INF";
	//static final String DIR_ROOT_OSE_ = "/opt/axteroid-ose";
	static final String DIR_AXTEROID_OSE = "/opt/axteroid-ose";
	static final String DIR_LOG = "/log";
	//static final String DIR_LISTADOS = "/listados/";
	static final String DIR_TMP = "tmp";	
	// DIR - AMBIENTE	
	static final String DIR_AMB_PROD = "/prod/";
	static final String DIR_AMB_BETA = "/beta/";
	static final String DIR_AMB_ALFA = "";
	// DIR SEC
	static final String DIR_FILE = "/sec/file";
	static final String DIR_JKS = "/sec/jks/";
	static final String DIR_CONFIG = "/sec/config/";
	  
	//DIR AVATAR
	static final String AVATAR_USER = "AXTEROIDOSE_AVATAR";	
	static final String DIR_AVATAR = "/opt/axteroid-avatar";		
	static final String DIR_AVATAR_LOG = "/log";
	static final String DIR_AVATAR_FILE = "/file";
	static final String DIR_AVATAR_FILE_LISTADOS = "/file/listados/";
	static final String DIR_AVATAR_FILE_SIGN_FIRST = "/file/sign/first";
	static final String DIR_AVATAR_FILE_SIGN_CHANGE = "/file/sign/change";
	static final String DIR_AVATAR_FILE_SIGN_LAST = "/file/sign/last";
	static final String DIR_AVATAR_CONFIG = "/sec/config/";
	static final String DIR_AVATAR_CONFIG_JKSUBL = "/sec/jksUbl/";	
	static final String DIR_AVATAR_CDR = "/send/cdr";	
	static final String DIR_AVATAR_HOMOLOGA = "/send/homologa";
	static final String DIR_AVATAR_UBL = "/send/ubl";
	//static final String DIR_AVATAR_LISTADOS = "/send/file/listados/";
	static final String YAML_CERTICADO_EMISOR = "certificadoEmisor.yaml";
	static final String FILE_AVATAR_PROPER = "axteroidAvatar.properties";
	static final String FILE_CERTICADO_EMISOR = "certificadoEmisor.properties";
	
	// CAMBIAR A ENUM
	// OSE ESTADO	
	static String CONTENT_TRUE = "0";
	static String CONTENT_FALSE = "1";
	static String CONTENT_UBL_RESPUESTA = "AX-OSE remite el UBL";
	static String CONTENT_CDR_RESPUESTA = "AX-OSE remite el CDR";
	static String CONTENT_NO_APLICA = "2";
	static String CONTENT_REPETIDO = "3";
	static String CONTENT_ERROR_DB = "4";
	static String CONTENT_QA = "5";
	static String CONTENT_PROCESO = "98";
	
	// UBL ESTADO
	public static final String SUNAT_UBL_POR_CARGAR = "01";	
	public static final String SUNAT_UBL_CON_ERRORES = "02";
	public static final String SUNAT_UBL_POR_GENERAR = "03";
	public static final String SUNAT_UBL_POR_FIRMAR = "04";
	public static final String SUNAT_UBL_POR_PARSEAR = "05";
	public static final String SUNAT_UBL_POR_EVALUAR = "06";	
	public static final String SUNAT_UBL_GENERADO = "07";
	public static final String SUNAT_UBL_ENVIADO = "08";
	
	// CDR ESTADO
	static String SUNAT_CDR_EN_PROCESO = "10";
	static String SUNAT_CDR_ERROR_PROCESO = "20";	
	static String SUNAT_CDR_GENERADO = "30";
	static String SUNAT_CDR_DETENIDO = "40";
	static String SUNAT_CDR_SERVICIO_INHABILITADO = "50";
	static String SUNAT_CDR_ERROR_SENDBILL = "60";
	static String SUNAT_CDR_ERROR_AVATAR = "70";
	static String SUNAT_CDR_ERROR_SENDSUMMARY = "80";
	static String SUNAT_CDR_AUTORIZADO = "90";
	
	// SUNAT TIPO DOCUMENTO
	static String SUNAT_FACTURA = "01";
	static String SUNAT_BOLETA = "03";
	static String SUNAT_LIQUIDACION_COMPRA = "04";
	static String SUNAT_NOTA_CREDITO = "07";
	static String SUNAT_NOTA_DEBITO = "08";
	static String SUNAT_GUIA_REMISION_REMITENTE = "09";
	static String SUNAT_TICKET = "12"; 
	static String SUNAT_DOCU_CONTROL_SBS = "13";
	static String SUNAT_RECIBO_SERV_PUBL = "14";
	static String SUNAT_BOLETO_ETPIP = "16";
	static String SUNAT_DOCU_AFP = "18";
	static String SUNAT_RETENCION = "20";
	static String SUNAT_ROL_ADQUIRIENTE_SISTEMAS_PAGO = "30";
	static String SUNAT_GUIA_REMISION_TRANSPORTISTA = "31";
	static String SUNAT_OPERADOR = "34";
	static String SUNAT_PARTICIPE = "35";
	static String SUNAT_RECIBO_DISTRIBUCION_GAS_NATURAL = "36";
	static String SUNAT_PERCEPCION = "40";	
	static String SUNAT_PERCEPCION_VI = "41";
	static String SUNAT_ADQUIRIENTE_SISTEMAS_PAGO = "42";
	static String SUNAT_PAGO_SAE = "56";
	static String SUNAT_GUIA_REMISION_REMITENTE_CO = "71";
	static String SUNAT_GUIA_REMISION_TRANSPORTISTA_CO = "72";
	static String SUNAT_GENERAL = "GE";
	static String SUNAT_REVERSION = "RR";    
	static String SUNAT_RESUMEN_DIARIO = "RC";
	static String SUNAT_COMUNICACION_BAJAS = "RA";
	static String SUNAT_APPLICATION_RESPONSE = "CDR"; 	
	static String SUNAT_NOTA_DEBITO_PENALIDAD = "03";	
	static String SUNAT_PACK = "LT";
	// FIN

	// SUNAT
	static Long SUNAT_RUC = 20131312955l;
	static String SUNAT_BAJA = "02";
	static String SUNAT_UBL_20 = "2.0";
	static String SUNAT_UBL_21 = "2.1";
	static String SUNAT_CUSTOMIZA_10 = "1.0";
	static String SUNAT_CUZTOMIZA_20 = "2.0";	
	static String SUNAT_FECHA_IGV = "20110301";
	static String SUNAT_TDI_RUC = "6";
	
	// Ind Tipo Asociacion
	static int SUNAT_IndTipAsoc_EMI =0;
	static int SUNAT_IndTipAsoc_PSE =1;
	static int SUNAT_IndTipAsoc_OSE =2;
	
	// Ind Estado CPE
	static String SUNAT_ESTADOCP_NO_EXISTE ="0";
	static String SUNAT_ESTADOCP_ACEPTADO ="1";
	static String SUNAT_ESTADOCP_ANULADO ="2";
	static String SUNAT_ESTADOCP_AUTORIZADO ="3";
	static String SUNAT_ESTADOCP_NO_AUTORIZADO ="4";	
	static int SUNAT_IndEstCpe_Acept =1;
	static int SUNAT_IndEstCpe_Recha =0;
	static int SUNAT_IndEstCpe_Anula =2;
	static int SUNAT_IndEstCpe_SD =9;
	static int SUNAT_CodigoOperacion_Adicionar =1;
	static int SUNAT_CodigoOperacion_Modificar =2;
	static int SUNAT_CodigoOperacion_Anulado =3;
	static int SUNAT_CodigoOperacion_Anulado_Dia =4;
	
	static String SUNAT_Indicador_0000 ="0000";
	static String SUNAT_Indicador_00 ="00";
	static String SUNAT_Indicador_01 ="01";
	static String SUNAT_Indicador_02 ="02";
	static String SUNAT_Indicador_03 ="03";
	static String SUNAT_Indicador_04 ="04";
	static String SUNAT_Indicador_05 ="05";
	static String SUNAT_Indicador_10 ="10";
	static String SUNAT_Indicador_11 ="11";
	static String SUNAT_Indicador_12 ="12";
	static String SUNAT_Indicador_13 ="13";
	static String SUNAT_Indicador_14 ="14";
	static String SUNAT_Indicador_62 ="62";
	// SERIE
	static String SUNAT_SERIE_B = "B";
	static String SUNAT_SERIE_F = "F";
	static String SUNAT_SERIE_E = "E";
	static String SUNAT_SERIE_S = "S";
	static String SUNAT_SERIE_E001 = "E001";
	static String SUNAT_SERIE_EB01 = "EB01";
	
	// DIAS
	static String SUNAT_DIAS_5 = "5";
	static String SUNAT_DIAS_7 = "7";
	
	//CODIGOS
	static String SUNAT_CODIGO_ = "-";
	static String SUNAT_CODIGO_TRIBUTO_IGV = "1000";
	static String SUNAT_CODIGO_TRIBUTO_ISC = "2000";
	static String SUNAT_CODIGO_OP_GRAVADA = "1001";
	static String SUNAT_CODIGO_OTROS_CODIGO = "9999";
	
	String PERCEPCIONES = "2001";
	String RETENCIONES = "2002";
	String DETRACCIONES = "2003";
	String BONIFICACIONES = "2004";
	String TIPO_TOTAL_DESCUENTOS = "2005";
	String TIPO_TRIBUTO_IGV_CODIGO = "1000";
	String TIPO_TRIBUTO_IGV_DESC = "IGV IMPUESTO GENERAL A LAS VENTAS";
	String TIPO_TRIBUTO_ISC_CODIGO = "2000";
	String TIPO_TRIBUTO_ISC_DESC = "ISC IMPUESTO SELECTIVO AL CONSUMO";
	String TIPO_TRIBUTO_OTROS_CODIGO = "9999";
	String TIPO_TRIBUTO_OTROS_DESC = "OTROS CONCEPTOS DE PAGO";
	String TIPO_TRIBUTO_IGV_CODIGO_INTERNACIONAL = "VAT";
	String TIPO_TRIBUTO_ISC_CODIGO_INTERNACIONAL = "EXC";
	String TIPO_TRIBUTO_OTROS_CODIGO_INTERNACIONAL = "OTH";
	String TIPO_TRIBUTO_IGV_NOMBRE = "IGV";
	String TIPO_TRIBUTO_ISC_NOMBRE = "ISC";
	String TIPO_TRIBUTO_OTROS_NOMBRE = "OTROS";
	String TIPO_OPERACIONES_GRAVADAS = "1001";
	String TIPO_OPERACIONES_NO_GRAVADAS = "1002";
	String TIPO_OPERACIONES_EXONERADAS = "1003";
	String TIPO_OPERACIONES_GRATUITAS = "1004";
	String TIPO_SUBTOTAL_VENTA = "1005";
	String TIPO_OPERACIONES_FIS = "3001";
	public static int MAX_BLOCK_DESCRIPCION = 250;
	
	//TIPO OPERACION
	static String SUNAT_TIPO_OPERACION_2100 = "2100";
	static String SUNAT_TIPO_OPERACION_2101 = "2101";
	static String SUNAT_TIPO_OPERACION_2102 = "2102";
	static String SUNAT_TIPO_OPERACION_2103 = "2103";
	static String SUNAT_TIPO_OPERACION_2104 = "2104";
	static String SUNAT_TIPO_OPERACION_2106 = "2106";
	static String SUNAT_TIPO_OPERACION_0112 = "0112";
	static String SUNAT_TIPO_OPERACION_0201 = "0201";
	
	// PARAMETROS
	static String SUNAT_PARAMETRO_C03 = "C03";
	static String SUNAT_PARAMETRO_001 = "001";
	static String SUNAT_PARAMETRO_002 = "002";
	static String SUNAT_PARAMETRO_003 = "003";
	static String SUNAT_PARAMETRO_004 = "004";
	static String SUNAT_PARAMETRO_005 = "005";
	static String SUNAT_PARAMETRO_006 = "006";
	static String SUNAT_PARAMETRO_007 = "007";
	static String SUNAT_PARAMETRO_008 = "008";
	static String SUNAT_PARAMETRO_009 = "009";
	static String SUNAT_PARAMETRO_010 = "010";
	static String SUNAT_PARAMETRO_011 = "011";
	static String SUNAT_PARAMETRO_012 = "012";
	static String SUNAT_PARAMETRO_013 = "013";
	static String SUNAT_PARAMETRO_014 = "014";
	static String SUNAT_PARAMETRO_015 = "015";
	static String SUNAT_PARAMETRO_016 = "016";
	static String SUNAT_PARAMETRO_017 = "017";
	static String SUNAT_PARAMETRO_018 = "018";
	static String SUNAT_PARAMETRO_019 = "019";
	static String SUNAT_PARAMETRO_020 = "020";
	static String SUNAT_PARAMETRO_021 = "021";
	static String SUNAT_PARAMETRO_022 = "022";
	static String SUNAT_PARAMETRO_023 = "023";
	static String SUNAT_PARAMETRO_025 = "025";
	static String SUNAT_PARAMETRO_CODIGO_RETORNO = "ERR";

    // RESPUESTA SUNAT	
	static String SUNAT_RESPUESTA_0 = "0";
	static String SUNAT_RESPUESTA_98 = "98";
	static String SUNAT_RESPUESTA_99 = "99";
	static String SUNAT_RESPUESTA_Continue = "100";
	static String SUNAT_RESPUESTA_OK = "200";
	static String SUNAT_RESPUESTA_Bad_Request = "400";
	static String SUNAT_RESPUESTA_Payment_Required = "401";
	static String SUNAT_RESPUESTA_NO_AUTORIZADO = "402";
	static String SUNAT_RESPUESTA_NO_FOUND = "404";
	static String SUNAT_RESPUESTA_Not_Acceptable = "406";
	static String SUNAT_RESPUESTA_REQUEST_TIMEOUT = "408";
	static String SUNAT_RESPUESTA_SERVICIO_INHABILITADO = "500";
	static String SUNAT_RESPUESTA_SUMMARY = "SUMM";
	static String SUNAT_RESPUESTA_0005 = "0005";
	static String SUNAT_RESPUESTA_0011 = "0011";
	static String SUNAT_RESPUESTA_0098 = "0098";
	static String SUNAT_RESPUESTA_0099 = "0099";
	static String SUNAT_RESPUESTA_SD = "";
	
	static String SUNAT_ERROR_D = "MENSAJE DE INCONSITENCIAS";
	static String SUNAT_ERROR_ROBOT = "TAREA TERMINADA ";
	
	static String SUNAT_ERROR_133 = "133";
	static String SUNAT_ERROR_158 = "158";
	static String SUNAT_ERROR_306 = "306";
	static String SUNAT_ERROR_0000 = "0000";
	static String SUNAT_ERROR_0000_D = "Error no existe en catalogo de SUNAT, revisar el codigo";
	static String SUNAT_ERROR_0000_CD = "Respuesta al envio de SUNAT";
	static String SUNAT_ERROR_98_C = "98";
	static String SUNAT_ERROR_98_D = "SUNAT no remite el CDR";
	static String SUNAT_ERROR_0100_C = "0100";
	static String SUNAT_ERROR_0100_D = "El sistema no puede responder su solicitud. Intente nuevamente o comuníquese con su Administrador";
	static String SUNAT_ERROR_0102_C = "0102";
	static String SUNAT_ERROR_0102_D = "Usuario o contraseña incorrectos";
	static String SUNAT_ERROR_0105 = "0105";
	static String SUNAT_ERROR_0106 = "0106";
	static String SUNAT_ERROR_0109 = "0109";
	static String SUNAT_ERROR_0113 = "0113";
	static String SUNAT_ERROR_0125 = "0125";
	static String SUNAT_ERROR_0126 = "0126";
	static String SUNAT_ERROR_0127 = "0127";
	static String SUNAT_ERROR_0130 = "0130";
	static String SUNAT_ERROR_0132 = "0132";
	static String SUNAT_ERROR_0137 = "0137";
	static String SUNAT_ERROR_0137_D = "El sistema no puede responder su solicitud. (Se obtuvo una respuesta nula)";
	static String SUNAT_ERROR_0138 = "0138";
	static String SUNAT_ERROR_0138_D = "El sistema no puede responder su solicitud. (Error en Base de Datos)";
	static String SUNAT_ERROR_0151 = "0151";
	static String SUNAT_ERROR_0155 = "0155";
	static String SUNAT_ERROR_0156 = "0156";
	static String SUNAT_ERROR_0158 = "0158";
	static String SUNAT_ERROR_0161 = "0161";
	static String SUNAT_ERROR_0200 = "0200";
	static String SUNAT_ERROR_0305 = "0305";
	static String SUNAT_ERROR_0306 = "0306";	
	static String SUNAT_ERROR_0402 = "0402";
	// GENERAL
	static String SUNAT_ERROR_0111 = "0111";
	static String SUNAT_ERROR_0154 = "0154";
	static String SUNAT_ERROR_1004 = "1004";
	static String SUNAT_ERROR_2074 = "2074";
	static String SUNAT_ERROR_2325 = "2325";
	static String SUNAT_ERROR_2326 = "2326";
	static String SUNAT_ERROR_2327 = "2327";
	static String SUNAT_ERROR_2329 = "2329";
	static String SUNAT_ERROR_2873 = "2873";
	static String SUNAT_OBSERV_2873 = "2873";
	static String SUNAT_ERROR_2874 = "2874";
	static String SUNAT_OBSERV_2874 = "2874";
	static String SUNAT_ERROR_2936 = "2936";
	static String SUNAT_OBSERV_4199 = "4199";
	static String SUNAT_ERROR_3239 = "3239";
	//FACTURA - BOLETA	
	static String SUNAT_ERROR_1032 = "1032";
	static String SUNAT_ERROR_1033 = "1033";
	static String SUNAT_ERROR_1035 = "1035";
	static String SUNAT_ERROR_1079 = "1079";
	static String SUNAT_ERROR_1083 = "1083";
	static String SUNAT_ERROR_2108 = "2108";
	static String SUNAT_ERROR_2104 = "2104";
	static String SUNAT_ERROR_2010 = "2010";
	static String SUNAT_ERROR_2011 = "2011";	
	static String SUNAT_ERROR_2410 = "2410";
	static String SUNAT_ERROR_2040 = "2040";
	static String SUNAT_ERROR_2036 = "2036";
	static String SUNAT_ERROR_2041 = "2041";
	static String SUNAT_ERROR_2045 = "2045";	
	static String SUNAT_ERROR_2051 = "2051";
	static String SUNAT_ERROR_2057 = "2057";
	static String SUNAT_ERROR_2529 = "2529";
	static String SUNAT_ERROR_2602 = "2602";	
	static String SUNAT_ERROR_2798 = "2798";
	static String SUNAT_ERROR_2957 = "2957";
	static String SUNAT_ERROR_2958 = "2958";
	static String SUNAT_ERROR_3002 = "3002";
	static String SUNAT_ERROR_3097 = "3097";
	static String SUNAT_ERROR_3202 = "3202";
	static String SUNAT_ERROR_3218 = "3218";
	static String SUNAT_ERROR_3219 = "3219";
	static String SUNAT_ERROR_3235 = "3235";
	static String SUNAT_ERROR_3281 = "3281";
	static String SUNAT_ERROR_3262 = "3262";
	static String SUNAT_ERROR_3269 = "3269";
	
	static String SUNAT_OBSERV_2016 = "2016";
	static String SUNAT_OBSERV_2377 = "2377";
	static String SUNAT_OBSERV_2950 = "2950";
	static String SUNAT_OBSERV_3219 = "3219";
	static String SUNAT_OBSERV_4001 = "4001";
	static String SUNAT_OBSERV_4013 = "4013";
	static String SUNAT_OBSERV_4014 = "4014";
	static String SUNAT_OBSERV_4019 = "4019";
	static String SUNAT_OBSERV_4042 = "4042";
	static String SUNAT_OBSERV_4093 = "4093";
	static String SUNAT_OBSERV_4200 = "4200";
	static String SUNAT_OBSERV_4280 = "4280";
	static String SUNAT_OBSERV_4292 = "4292";
	static String SUNAT_OBSERV_4332 = "4332";
	static String SUNAT_OBSERV_4441 = "4441";
	// FACTURA	
	static String SUNAT_OBSERV_4043 = "4043";
	static String SUNAT_OBSERV_4149 = "4149";
	static String SUNAT_OBSERV_4176 = "4176";
	static String SUNAT_OBSERV_4181 = "4181";
	// NOTA DEBITO
	static String SUNAT_ERROR_2209 = "2209";
	static String SUNAT_ERROR_2207 = "2207";
	static String SUNAT_ERROR_2208 = "2208";
	static String SUNAT_ERROR_2197 = "2197";
	// NOTA CREDITO	
	static String SUNAT_ERROR_2119 = "2119";
	static String SUNAT_ERROR_2120 = "2120";
	static String SUNAT_ERROR_2121 = "2121";
	static String SUNAT_ERROR_2145 = "2145";
	static String SUNAT_ERROR_2172 = "2172";
	static String SUNAT_ERROR_3209 = "3209";
	static String SUNAT_ERROR_3260 = "3260";
	static String SUNAT_ERROR_3321 = "3321";
	// NOTA CREDITO Y DEBITO	
	static String SUNAT_ERROR_1080 = "1080";
	static String SUNAT_ERROR_2404 = "2404";	
	static String SUNAT_ERROR_2400 = "2400";	
	static String SUNAT_ERROR_2194 = "2194";
	static String SUNAT_ERROR_2199 = "2199";	
	static String SUNAT_ERROR_2183 = "2183";
	static String SUNAT_ERROR_2885 = "2885";
	static String SUNAT_OBSERV_2404 = "2404";
	static String SUNAT_OBSERV_2988 = "2988";
	static String SUNAT_OBSERV_4368 = "4368";
	static String SUNAT_OBSERV_4204 = "4204";
	// DAE	
	static String SUNAT_OBSERV_4231 = "4231";
	static String SUNAT_OBSERV_4351 = "4351";
	static String SUNAT_OBSERV_4352 = "4352";
	// RETENCION
	static String SUNAT_ERROR_1049 = "1049";
	static String SUNAT_ERROR_2621 = "2621";
	static String SUNAT_OBSERV_4091 = "4091";
	// PERCEPCION
	static String SUNAT_ERROR_2605 = "2605";
	static String SUNAT_OBSERV_4086 = "4086";
	static String SUNAT_OBSERV_4089 = "4089";
	static String SUNAT_OBSERV_4090 = "4090";
	// RETENCION Y PERCEPCION
	static String SUNAT_ERROR_2600 = "2600";
	static String SUNAT_ERROR_2609 = "2609";
	static String SUNAT_ERROR_2610 = "2610";
	static String SUNAT_ERROR_2617 = "2617";
	static String SUNAT_OBSERV_2917 = "2917";
	static String SUNAT_ERROR_3312 = "3312";
	static String SUNAT_ERROR_3325 = "3325";
	static String SUNAT_ERROR_3328 = "3328";
	static String SUNAT_ERROR_3329 = "3329";
	static String SUNAT_OBSERV_4087 = "4087";
	static String SUNAT_OBSERV_4088 = "4088";
	static String SUNAT_ERROR_4285 = "4285";	
	// GUIA
	static String SUNAT_ERROR_1001 = "1001";
	static String SUNAT_ERROR_1063 = "1063";
	static String SUNAT_ERROR_1085 = "1085";
	static String SUNAT_ERROR_2755 = "2755";
	static String SUNAT_ERROR_2760 = "2760";
	static String SUNAT_ERROR_2773 = "2773";
	static String SUNAT_ERROR_4000 = "4000";
	static String SUNAT_ERROR_4050 = "4050";
	static String SUNAT_ERROR_4051 = "4051";
	static String SUNAT_ERROR_4052 = "4052";
	static String SUNAT_ERROR_4200 = "4200";
	// RESUMEN DIARIO
	static String SUNAT_ERROR_2220 = "2220";
	static String SUNAT_ERROR_2223 = "2223";
	static String SUNAT_ERROR_2236 = "2236";
	static String SUNAT_ERROR_2256 = "2256";
	static String SUNAT_ERROR_2268 = "2268";
	static String SUNAT_ERROR_2663 = "2663";	
	static String SUNAT_ERROR_2988 = "2988";
	static String SUNAT_ERROR_2989 = "2989";
	static String SUNAT_ERROR_2990 = "2990";
	static String SUNAT_ERROR_2917 = "2917";
	static String SUNAT_ERROR_2987 = "2987";
	static String SUNAT_OBSERV_3207 = "3207";
	static String SUNAT_ERROR_3207 = "3207";
	static String SUNAT_OBSERV_4285 = "4285";
	// RESUMEN BAJA
	static String SUNAT_ERROR_2105 = "2105";
	static String SUNAT_ERROR_2323 = "2323";
	static String SUNAT_ERROR_2324 = "2324";
	static String SUNAT_ERROR_2375 = "2375";
	static String SUNAT_ERROR_2398 = "2398";
	static String SUNAT_ERROR_2927 = "2927";
	// REVERSION
	static String SUNAT_ERROR_2282 = "2282";
	static String SUNAT_ERROR_2750 = "2750";
	static String SUNAT_ERROR_2751 = "2751";
	// RECIBO SERVICIOS PUBLICOS
	static String SUNAT_ERROR_4231 = "4231";	
	static String SUNAT_ERROR_2334 = "2334";
}
