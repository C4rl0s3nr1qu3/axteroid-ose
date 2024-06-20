package com.axteroid.ose.server.tools.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.constantes.Constantes;

public class DocumentUtil {
	//private static final Logger log = Logger.getLogger(DocumentUtil.class);
	private final static Logger log = LoggerFactory.getLogger(DocumentUtil.class);
	   public static String compareTwoTimeStamps(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime) {        
	        long milliseconds = currentTime.getTime() - oldTime.getTime();
	        int seconds = (int) milliseconds / 1000;     
	        long diffDays = milliseconds / (24 * 60 * 60 * 1000);
	        int hours = seconds / 3600;
	        int minutes = (seconds % 3600) / 60;
	        int seconds2 = (seconds % 3600) % 60;                               
	        long diffMilli = milliseconds-(seconds * 1000);        
	        return "total ms: "+milliseconds+" -> Dias: "+diffDays+" Hor: "+hours+" Min: "+minutes+" Sec: "+seconds2+" Milesimas: "+ diffMilli;
	    }
	    
	   public static String compareTwoTimeStamps_SEG(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime) {   
		   
	        long milliseconds = currentTime.getTime() - oldTime.getTime();
	        int seconds = (int) milliseconds / 1000;     
	        //long diffDays = milliseconds / (24 * 60 * 60 * 1000);
	        //int hours = seconds / 3600;
	        //int minutes = (seconds % 3600) / 60;
	        //int seconds2 = (seconds % 3600) % 60;                               
	        long diffMilli = milliseconds-(seconds * 1000);        
	        return "total ms: "+milliseconds+" -> Sec: "+seconds+" Milesimas: "+ diffMilli;
	    }
	   
//	    public static void zipFiles(List<File> files, String nameZip){
//	        
//	        FileOutputStream fos = null;
//	        ZipOutputStream zipOut = null;
//	        FileInputStream fis = null;
//	        try {
//	            fos = new FileOutputStream(nameZip);
//	            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
//	            for(File f : files){
//	                File input = f;
//	                fis = new FileInputStream(input);
//	                ZipEntry ze = new ZipEntry(input.getName());
//	                //log.info("Zipping the file: "+input.getName());
//	                zipOut.putNextEntry(ze);
//	                byte[] tmp = new byte[4*1024];
//	                int size = 0;
//	                while((size = fis.read(tmp)) != -1){
//	                    zipOut.write(tmp, 0, size);
//	                }
//	                zipOut.flush();
//	                fis.close();
//	            }
//	            zipOut.close();
//	            //log.info("Done... Zipped the files...");
//	        } catch (FileNotFoundException e) {
//	            e.printStackTrace();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        } finally{
//	            try{
//	                if(fos != null) fos.close();
//	            } catch(Exception e){
//	            	e.printStackTrace();
//	            }
//	        }
//	    }
	    
//	    public static byte[] zipFiles2Byte(List<File> files){
//	    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
//	        ZipOutputStream zipOut = null;
//	        FileInputStream fis = null;
//	        try {
//	        	zipOut = new ZipOutputStream(bos);
//	            for(File f : files){
//	                File input = f;
//	                //log.info("Zipping the file: "+input.getName());
//	                fis = new FileInputStream(input);
//	                ZipEntry ze = new ZipEntry(input.getName());
//	                zipOut.putNextEntry(ze);
//	                byte[] tmp = new byte[4*1024];
//	                int size = 0;
//	                while((size = fis.read(tmp)) != -1){
//	                    zipOut.write(tmp, 0, size);
//	                }
//	                zipOut.flush();
//	                fis.close();
//	            }
//	            zipOut.close();	            
//	            //log.info("Done... Zipped the files...");
//	        } catch (FileNotFoundException e) {
//				StringWriter errors = new StringWriter();				
//				e.printStackTrace(new PrintWriter(errors));
//				log.info("zipFiles2Byte Exception \n"+errors);
//	        } catch (IOException e) {
//				StringWriter errors = new StringWriter();				
//				e.printStackTrace(new PrintWriter(errors));
//				log.info("zipFiles2Byte Exception \n"+errors);
//	        } finally{
//	            try{
//	                if(bos.size()>0) 
//	                	return bos.toByteArray();
//	            } catch(Exception e){
//					StringWriter errors = new StringWriter();				
//					e.printStackTrace(new PrintWriter(errors));
//					log.info("zipFiles2Byte Exception \n"+errors);
//	            }
//	        }
//	        return null;
//	    }
	    
//	    public static void zipString(List<String> files, String nameZip){	        
//	        FileOutputStream fos = null;
//	        ZipOutputStream zipOut = null;
//	        FileInputStream fis = null;
//	        try {
//	            fos = new FileOutputStream(nameZip);
//	            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
//	            for(String filePath:files){
//	                File input = new File(filePath);
//	                fis = new FileInputStream(input);
//	                ZipEntry ze = new ZipEntry(input.getName());
//	                //log.info("Zipping the file: "+input.getName());
//	                zipOut.putNextEntry(ze);
//	                byte[] tmp = new byte[4*1024];
//	                int size = 0;
//	                while((size = fis.read(tmp)) != -1){
//	                    zipOut.write(tmp, 0, size);
//	                }
//	                zipOut.flush();
//	                fis.close();
//	            }
//	            zipOut.close();
//	            //log.info("Done... Zipped the files...");
//	        } catch (FileNotFoundException e) {
//	            // TODO Auto-generated catch block
//	            e.printStackTrace();
//	        } catch (IOException e) {
//	            // TODO Auto-generated catch block
//	            e.printStackTrace();
//	        } finally{
//	            try{
//	                if(fos != null) fos.close();
//	            } catch(Exception ex){
//	                 
//	            }
//	        }
//	    }
	    
//		public static byte[] descomprimirArchivoMejorado(byte[] entrada) {
//			try {
//				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//				ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(entrada));
//				ZipEntry zipEntry = null;
//				byte data[] = new byte[10240];
//				int count = 0;			
//				while ((zipEntry = zipInputStream.getNextEntry()) != null) {
//					if (!zipEntry.isDirectory()) {
//						while ((count = zipInputStream.read(data, 0, 10240)) != -1) {
//							byteArrayOutputStream.write(data, 0, count);
//						}
//					}
//				}
//				byte[] bytesArraySalida = new byte[byteArrayOutputStream.toByteArray().length];
//				bytesArraySalida = byteArrayOutputStream.toByteArray();
//				zipInputStream.close();
//				byteArrayOutputStream.close();
//				return bytesArraySalida;
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
//		}
	    
	    
	    public static void string2File(String dirFile, String strContent) {
	        BufferedWriter bufferedWriter = null;
	        try {            
	            File myFile = new File(dirFile);
	            // check if file exist, otherwise create the file before writing
	            if (!myFile.exists()) {
	                myFile.createNewFile();
	            }
	            Writer writer = new FileWriter(myFile);
	            bufferedWriter = new BufferedWriter(writer);
	            bufferedWriter.write(strContent);
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally{
	            try{
	                if(bufferedWriter != null) bufferedWriter.close();
	            } catch(Exception ex){
	                 
	            }
	        }
	    }
	    
	    public static void getStatistics(Map<String, Timestamp> mapTime) {
			log.info("********************************************************************");
			log.info(" Security          "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("Security"), mapTime.get("Start")));
			log.info(" RulesUbl          "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("RulesUbl"), mapTime.get("Security")));
			log.info(" GetDatUbl         "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("GetDatUbl"), mapTime.get("RulesUbl")));
			log.info(" RulesParameters   "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("RulesParameters"), mapTime.get("GetDatUbl")));
			log.info(" RulesList         "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("RulesList"), mapTime.get("RulesParameters")));
			log.info(" BuildCDR          "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("BuildCDR"), mapTime.get("RulesList")));
			log.info(" RecordCDR         "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("RecordCDR"), mapTime.get("BuildCDR")));	
			log.info(" RecordDocument    "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("RecordDocument"), mapTime.get("RecordCDR")));	
			log.info(" SendSUNAT         "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("End"), mapTime.get("RecordDocument")));	
			log.info("********************************************************************");
			log.info(" Total             "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("End"), mapTime.get("Start")));
			log.info("********************************************************************");
		}
	    
//	    public static void getStatisticsMongo(Map<String, Timestamp> mapTime) {
//			log.info("********************************************************************");
//			log.info(" StartBuild        "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("StartBuild"), mapTime.get("Start")));
//			log.info(" ParseDocument     "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("ParseDocument"), mapTime.get("StartBuild")));
//			log.info(" BuildDocument     "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("BuildDocument"), mapTime.get("ParseDocument")));
//			log.info(" CRUDDocument      "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("CRUDDocument"), mapTime.get("BuildDocument")));
//			log.info(" CRUDCPE           "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("End"), mapTime.get("CRUDDocument")));	
//			log.info("********************************************************************");
//			log.info(" Total             "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("End"), mapTime.get("Start")));
//			log.info("********************************************************************");
//		}
	    
	    public static void getStatisticsFactura(Map<String, Timestamp> mapTime) {
			log.info("********************************************************************");
			log.info(" GetUbl              "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("GetUbl"), mapTime.get("Start")));
			log.info(" GetUblCPE           "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("GetUblCPE"), mapTime.get("GetUbl")));
			log.info(" RecordCPE           "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("End"), mapTime.get("GetUblCPE")));	
			log.info("********************************************************************");
			log.info(" Total             "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("End"), mapTime.get("Start")));
			log.info("********************************************************************");
		}
	    
	    public static void getStatisticsSunatListRest(Map<String, Timestamp> mapTime) {
			//log.info("********************************************************************");
			log.info(" Total SunatListRest     "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("EndSunatListRest"), mapTime.get("StartSunatListRest")));
			//log.info("********************************************************************");
		}	    

	    public static void getStatisticsSunatListJDBC(Map<String, Timestamp> mapTime) {
			//log.info("********************************************************************");
			log.info(" Total SunatListJDBC     "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("EndSunatListJDBC"), mapTime.get("StartSunatListJDBC")));
			//log.info("********************************************************************");
		}	 

	    public static void getStatisticsSunatList(Map<String, Timestamp> mapTime) {
			//log.info("********************************************************************");
			log.info(" Total SunatList     "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("EndSunatList"), mapTime.get("StartSunatList")));
			//log.info("********************************************************************");
		}	 
	    public static void getStatisticsSunatList(Map<String, Timestamp> mapTime, String resultado, String fuenteSunatList) {
	    	log.info("********************************************************************");
	    	log.info(" Respuesta SunatList: {} ",resultado);
	    	if(fuenteSunatList.equals(Constantes.CONTENT_TRUE))
	    		getStatisticsSunatListRest(mapTime);	    	
	    	else if(fuenteSunatList.equals(Constantes.CONTENT_FALSE))
	    		getStatisticsSunatListJDBC(mapTime);
	    	else
	    		getStatisticsSunatList(mapTime);		
	    	log.info("********************************************************************");
		}
	    
	    public static void getStatisticsGetStatus(Map<String, Timestamp> mapTime) {
			log.info("********************************************************************");
			log.info(" Total GetStatus     "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("EndGetStatus"), mapTime.get("StartGetStatus")));
			log.info("********************************************************************");
		}	  
	    
	    public static void getStatisticsGetStatusCDR(Map<String, Timestamp> mapTime) {
			log.info("********************************************************************");
			log.info(" Total GetStatusCDR  "+DocumentUtil.compareTwoTimeStamps_SEG(mapTime.get("EndGetStatusCDR"), mapTime.get("StartGetStatusCDR")));
			log.info("********************************************************************");
		}	  
	    	   
}
