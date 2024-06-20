package com.axteroid.ose.server.repository.builder;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.repository.service.S3Repository;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.FileTypeEnum;

public class S3CrudBuild {
	//private final static Logger log =  Logger.getLogger(S3CrudBuild.class);
	private static final Logger log = LoggerFactory.getLogger(S3CrudBuild.class);
	
//	public String insertZipUBL(TbContent tbContent) {
//		//log.info("insertZipUBL "+tbContent.toString());
//		String datosS3 = new String();				
//		try{
//			String RUC = String.valueOf(tbContent.getRucEmisor());
//			String nombreFile = RUC+"-"+tbContent.getTipoDocumento()+"-"+tbContent.getArchivo()+"-"+tbContent.getNumeroCorrelativo();
//			datosS3 = this.generateSynchroBean(tbContent.getTipoDocumento(), RUC, FileTypeEnum.ZIP.val,
//					nombreFile, tbContent.getContentfile());
//			//log.info("datosS3 "+datosS3);
//		} catch (Exception e) {
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));
//			log.error("insertZipUBL Exception \n"+errors);
//		}
//		return datosS3;
//	}
	
	public String insertUBL(Documento tbComprobante) {
		//log.info("insertUBL "+tbComprobante.toString());
		String datosS3 = new String();				
		try{
			String RUC = String.valueOf(tbComprobante.getRucEmisor());			
			String nombreFile = RUC+Constantes.GUION+tbComprobante.getTipoDocumento()+Constantes.GUION+tbComprobante.getSerie()+Constantes.GUION+tbComprobante.getNumeroCorrelativo();
			datosS3 = this.generateSynchroBean(tbComprobante.getTipoDocumento(), RUC, FileTypeEnum.UBL.val,
					nombreFile, tbComprobante.getUbl());
			//log.info("datosS3 "+datosS3);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertUBL Exception \n"+errors);
		}
		return datosS3;
	}	
	
	public String insertCDR(Documento tbComprobante) {
		//log.info("insertCDR "+tbComprobante.toString());
		String datosS3 = new String();				
		try{
			String RUC = String.valueOf(tbComprobante.getRucEmisor());			
			String nombreFile = Constantes.CDR_PREFI+Constantes.GUION+RUC+Constantes.GUION+tbComprobante.getTipoDocumento()+Constantes.GUION+tbComprobante.getSerie()+Constantes.GUION+tbComprobante.getNumeroCorrelativo();
			datosS3 = this.generateSynchroBean(tbComprobante.getTipoDocumento(), RUC, FileTypeEnum.CDR.val,
					nombreFile, tbComprobante.getCdr());
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertCDR Exception \n"+errors);
		}
		return datosS3;
	}	
	
	
	public String insertAcuseRecibo(Documento tbComprobante) {
		//log.info("insertAcuseRecibo "+tbComprobante.toString());
		String datosS3 = new String();				
		try{
			String RUC = String.valueOf(tbComprobante.getRucEmisor());			
			String nombreFile = Constantes.AR_PREFI+Constantes.GUION
					+RUC+Constantes.GUION
					+tbComprobante.getTipoDocumento()+Constantes.GUION
					+tbComprobante.getSerie()+Constantes.GUION
					+tbComprobante.getNumeroCorrelativo();
			datosS3 = this.generateSynchroBean(tbComprobante.getTipoDocumento(), RUC, FileTypeEnum.SUNAT.val,
					nombreFile, tbComprobante.getMensajeSunat());
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("insertAcuseRecibo Exception \n"+errors);
		}
		return datosS3;
	}		

	public byte[] getS3ByteFile(String locationFile) {
		//log.info("getS3ByteFile "+locationFile);
		byte[] contentFile = new byte[0];		
		try{
			S3Repository s3Repository = new S3Repository();
			contentFile = s3Repository.getFileZip(locationFile);			
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getS3ByteFile Exception \n"+errors);
		}
		log.info("getS3ByteFile locationFile: "+locationFile+" | contentFile.length; "+contentFile.length);
		return contentFile;
	}
	
	private String generateSynchroBean(String documentType, String RUC, String tipoFile, 
			String nombreFile, byte[] byteFile) {
		//log.info("generateSynchroBean "+nombreFile);
		String datosS3 = new String();				
		try{
			if((byteFile!=null) && (byteFile.length>0)) {
				String filePath = documentType.concat("/")
						.concat(RUC).concat("/")
						.concat(tipoFile).concat("/")
						.concat(nombreFile);	
				S3Repository s3Repository = new S3Repository();
				String fileMD5 = s3Repository.setFileZip(filePath, byteFile);	
				datosS3 = filePath +Constantes.OSE_SPLIT+ fileMD5;				
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("generateSynchroBean Exception \n"+errors);
		}
		log.info("generateSynchroBean nombreFile: "+nombreFile+" | datosS3 "+datosS3);
		return datosS3;		
	}	
	
}
