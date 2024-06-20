package com.axteroid.ose.server.repository.service;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.constantes.TipoAvatarPropertiesEnum;
import com.axteroid.ose.server.tools.util.DirUtil;

public class S3Repository {
	//private final static Logger log =  Logger.getLogger(S3Repository.class);
	private static final Logger log = LoggerFactory.getLogger(S3Repository.class);
	static Map<String, String> mapa = DirUtil.getMapJBossProperties();
    static String bucketName = mapa.get(TipoAvatarPropertiesEnum.S3_REPOSITORY_BUCKETNAME.getCodigo());
		
	public byte[] getFileZip(String locationFile) throws Exception {
		//log.info("getFileZip bucketName "+bucketName);		
		byte[] contentFile = new byte[0];
		try {
//			AmazonS3 s3Client = getAmazonS3();
//			final S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, locationFile));		    
//			S3ObjectInputStream s3ObjectInputStream = s3object.getObjectContent();
//			contentFile = IOUtils.toByteArray(s3ObjectInputStream);
			
			
			//String sFile = new String(contentFile);
			//log.info("sFile: "+sFile);
			//log.info("locationFile: "+locationFile);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getFileZip Exception \n"+errors);
		}	
		return contentFile;
	}
	
	public String setFileZip(String locationFile, byte[] contentFile) {
		//log.info("setFileZip "+bucketName);
		//String sFile = new String(contentFile);
		//log.info("sFile: "+sFile);
		//log.info("locationFile: "+locationFile);
		try {	
//			ByteArrayInputStream bin = new ByteArrayInputStream(contentFile);
//			ObjectMetadata meta = new ObjectMetadata();
//			meta.setContentLength(contentFile.length);			
//			AmazonS3 s3Client = getAmazonS3();
//			PutObjectResult res = s3Client.putObject(bucketName, locationFile, bin, meta);	
//			return res.getContentMd5();
			
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("setFileZip Exception \n"+errors);
		}		
		return "";
	}
	
//	private static AmazonS3 getAmazonS3() {
//		String location = mapa.get(TipoFileRobotProperEnum.S3_REPOSITORY_LOCATION.getCodigo());
//		String endpointUrl = mapa.get(TipoFileRobotProperEnum.S3_REPOSITORY_ENDPOINTURL.getCodigo());
//	    String apiKey = mapa.get(TipoFileRobotProperEnum.S3_REPOSITORY_APIKEY.getCodigo());	
//	    String service_instance_id = mapa.get(TipoFileRobotProperEnum.S3_REPOSITORY_SERVICEINSTANCEID.getCodigo());		
//		AWSCredentials credentials = new BasicIBMOAuthCredentials(apiKey, service_instance_id);
//		ClientConfiguration clientConfig = new ClientConfiguration()
//                .withRequestTimeout(5000)
//                .withTcpKeepAlive(true);
//		return AmazonS3ClientBuilder.standard()
//                .withEndpointConfiguration(new EndpointConfiguration(endpointUrl, location))
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .withClientConfiguration(clientConfig)
//                .withPathStyleAccessEnabled(true)
//                .build();	
//	}

}
