package com.axteroid.ose.server.avatar.task;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoListaSUNATEnum;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.FileUtil;
import com.axteroid.ose.server.tools.util.ZipUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SunatListDownload {
	
	public static Logger log = LoggerFactory.getLogger(SunatListDownload.class);
	TipoListaSUNATEnum aTipoListaSUNAT[];
    String fecha = "";
    String rutaDir = Constantes.DIR_AVATAR;            
    String rutaSunatList = "";
    int l = (aTipoListaSUNAT = TipoListaSUNATEnum.values()).length;
    
    public void actualizarListasSunat(String fecha){
    	this.fecha = fecha;
    	if(this.fecha.isEmpty())
    		fecha = DateUtil.getToday();    
    	rutaSunatList = (new StringBuilder(rutaDir)).append(Constantes.DIR_AVATAR_FILE_LISTADOS).append(fecha).toString();
        if(!FileUtil.tobeDirectory(rutaSunatList))
            FileUtil.createDirectory(rutaSunatList);    	
    	this.listSunatDownload();
    	this.listSunatUnzip();
    	this.listSunatGzip();
    }
      
    public void listSunatDownload(){	
        log.info((new StringBuilder("FTP Descarga iniciada ... ")).append(String.valueOf(new Date())).toString());
        for(int i = 0; i < l; i++) {
        	TipoListaSUNATEnum listaSunat = aTipoListaSUNAT[i];
            if(listaSunat.getTipoLista().intValue() == 1 || listaSunat.getTipoLista().intValue() == 3){
            //if(listaSunat.getTipoLista().intValue() == 4){	
                String rutaOseServerZip = (new StringBuilder(String.valueOf(rutaSunatList))).append(File.separator).append(listaSunat.getDescripcion()).append("_").append(fecha).append(".txt.gz").toString();                
                //String rutaSUNAT = (new StringBuilder(OseConstantes.SUNAT_FTP_PATH_1)).append(listaSunat.getDescripcion()).append("_").append(fecha).append(".txt.gz").toString();
                String rutaSUNAT = (new StringBuilder(Constantes.SUNAT_FTP_PATH_2)).append(listaSunat.getDescripcion()).append("_").append(fecha).append(".txt.gz").toString();              
                log.info("ftp rutaOseServerZip "+rutaOseServerZip);
                //log.info("rutaSUNAT 1 - "+rutaSUNAT);
                if(listaSunat.getTipoLista().intValue() == 1){
                    //rutaSUNAT = (new StringBuilder(OseConstantes.SUNAT_FTP_PATH_RUC_1)).append(listaSunat.getDescripcion()).append("_").append(fecha).append(".txt.gz").toString();
                    rutaSUNAT = (new StringBuilder(Constantes.SUNAT_FTP_PATH_RUC_2)).append(listaSunat.getDescripcion()).append("_").append(fecha).append(".txt.gz").toString();
                }
                //log.info("rutaSUNAT 2 - "+rutaSUNAT);
                if(listaSunat.getTipoLista().intValue() == 4){
                    //rutaSUNAT = (new StringBuilder(OseConstantes.SUNAT_FTP_PATH_RUC_CD_1)).append(listaSunat.getDescripcion()).append("_").append(fecha).append(".txt.gz").toString();
                    rutaSUNAT = (new StringBuilder(Constantes.SUNAT_FTP_PATH_RUC_2)).append(listaSunat.getDescripcion()).append("_").append(fecha).append(".txt.gz").toString();
                }
                if(listaSunat.getTipoLista().intValue() == 5){
                    //rutaSUNAT = (new StringBuilder(OseConstantes.SUNAT_FTP_PATH_RUC_CD_1)).append(listaSunat.getDescripcion()).append("_").append(fecha).append(".txt.gz").toString();
                    rutaSUNAT = (new StringBuilder(Constantes.SUNAT_FTP_PATH_RUC_2)).append(listaSunat.getDescripcion()).append("_").append(".txt.gz").toString();
                }
                log.info("rutaSUNAT 3 - "+rutaSUNAT);
                if(!FileUtil.tobeFile(rutaOseServerZip))
                	DownloadUtil.descargaHTTPS_SSL(rutaSUNAT, rutaOseServerZip);
            }
        }
        log.info((new StringBuilder("FTP Descarga terminada ... ")).append(String.valueOf(new Date())).toString());
    }
    
    public void listSunatUnzip(){	       
        ZipUtil zipUtil = new ZipUtil();
        //TipoListaSUNATEnum aTipoListaSUNAT[];
        //int l = (aTipoListaSUNAT = TipoListaSUNATEnum.values()).length;
        for(int i = 0; i < this.l; i++){
        	TipoListaSUNATEnum listaSunat = aTipoListaSUNAT[i];
            if(listaSunat.getTipoLista().intValue() == 1 || listaSunat.getTipoLista().intValue() == 3){
        	//if(listaSunat.getTipoLista().intValue() == 4){
        		String rutaOseServerZip = (new StringBuilder(String.valueOf(rutaSunatList))).append(File.separator).append(listaSunat.getDescripcion()).append("_").append(fecha).append(".txt.gz").toString();
        		//String rutaOseServerZip = (new StringBuilder(String.valueOf(rutaSunatList))).append(File.separator).append(listaSunat.getDescripcion()).append("_").append(fecha).append(".zip").toString();
        		String rutaOseServerTxt = (new StringBuilder(String.valueOf(rutaSunatList))).append(File.separator).append(listaSunat.getTabla()).append(".txt").toString();
                //log.info("zip rutaOseServerZip "+rutaOseServerZip);
                File archivo = new File(rutaOseServerZip);       		
        		if(!archivo.exists()) {
        			log.info("No existe Archivo: " + rutaOseServerZip);
        			continue;
        		}
        		log.info("rutaOseServerZip: {} | rutaOseServerTxt: {}"+rutaOseServerZip,rutaOseServerTxt);
                if(!FileUtil.tobeFile(rutaOseServerTxt))
                	zipUtil.unZipSave(rutaOseServerZip, rutaOseServerTxt); ;
            }
        }
        log.info((new StringBuilder("FTP Descomprido terminado ... ")).append(String.valueOf(new Date())).toString());
    }

    public void listSunatGzip(){	       
        //ZipUtil zipUtil = new ZipUtil();
        //TipoListaSUNATEnum aTipoListaSUNAT[];
        //int l = (aTipoListaSUNAT = TipoListaSUNATEnum.values()).length;
        for(int i = 0; i < this.l; i++){
        	TipoListaSUNATEnum listaSunat = aTipoListaSUNAT[i];
            if(listaSunat.getTipoLista().intValue() == 1 || listaSunat.getTipoLista().intValue() == 3){
        	//if(listaSunat.getTipoLista().intValue() == 4){
        		String rutaOseServerZip = (new StringBuilder(String.valueOf(rutaSunatList))).append(File.separator).append(listaSunat.getDescripcion()).append("_").append(fecha).append(".txt.gz").toString();
        		//String rutaOseServerZip = (new StringBuilder(String.valueOf(rutaSunatList))).append(File.separator).append(listaSunat.getDescripcion()).append("_").append(fecha).append(".zip").toString();
        		String rutaOseServerTxt = (new StringBuilder(String.valueOf(rutaSunatList))).append(File.separator).append(listaSunat.getTabla()).append(".txt").toString();
                //log.info("zip rutaOseServerZip "+rutaOseServerZip);
        		File archivo = new File(rutaOseServerZip);       		
        		if(!archivo.exists()) {
        			log.info("No existe Archivo: " + rutaOseServerZip);
        			continue;
        		}   
        		try {
		    		Path source = Paths.get(rutaOseServerZip);
		            Path target = Paths.get(rutaOseServerTxt);		            
		    		log.info("rutaOseServerZip: {} | rutaOseServerTxt: {}"+rutaOseServerZip,rutaOseServerTxt);
		            if(!FileUtil.tobeFile(rutaOseServerTxt))		            	
		            	ZipUtil.decompressGzip(source, target); 
		        }catch(IOException ex){
		        	StringWriter errors = new StringWriter();				
					ex.printStackTrace(new PrintWriter(errors));
					log.error("decompressGzip IOException \n"+errors);
					return;
		       	}   
            }
        }
        log.info((new StringBuilder("FTP Descomprido terminado ... ")).append(String.valueOf(new Date())).toString());
    }
    
    public void listSunatAdd(String fecha){	     
    	if(rutaSunatList.isEmpty())
    		rutaSunatList = (new StringBuilder(rutaDir)).append(Constantes.DIR_AVATAR_FILE_LISTADOS).append(fecha).toString();
        for(int k = 0; k < this.l; k++){
        	TipoListaSUNATEnum listaSunat = aTipoListaSUNAT[k];
            if((listaSunat.getTipoLista().intValue() == 0) || 
            	(listaSunat.getTipoLista().intValue() == 1) ||
            	(listaSunat.getTipoLista().intValue() == 3)) {
                String rutaOseServerTxt = (new StringBuilder(String.valueOf(rutaSunatList))).append(File.separator).append(listaSunat.getTabla()).append(".txt").toString();
                log.info("rutaOseServerTxt "+rutaOseServerTxt);
                SunatListCrud sunatListCrud = new SunatListCrud();
                sunatListCrud.cargarListas(listaSunat.getCodigo(),rutaOseServerTxt);
            }
        }
        log.info((new StringBuilder("Add terminada ... ")).append(String.valueOf(new Date())).toString());
    }    

    
    
    private void copyFile(){    	
		try {
			JSch jsch = new JSch();
	    	Session session = jsch.getSession("alignet\\administrator", "172.19.35.48");
	    	session.setPassword("12345678Aa");
	    	session.connect();
	    	ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
	    	sftpChannel.connect();
	    	sftpChannel.put("C:/source/local/path/file.zip", "/gc/OSE_LISTA/file.zip");	    	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
			
	


}
