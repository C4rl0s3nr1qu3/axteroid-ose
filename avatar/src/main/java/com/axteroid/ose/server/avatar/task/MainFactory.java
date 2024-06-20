package com.axteroid.ose.server.avatar.task;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.jdbc.RsetPostGresJDBC;
import com.axteroid.ose.server.jpa.model.Documento;

public class MainFactory {
    static Logger log = LoggerFactory.getLogger(MainFactory.class);    
    private DocumentoCrud comprobanteCRUD = new DocumentoCrud();  
    private SunatCDRRead sunatCDRRead = new SunatCDRRead();   
    private SunatListRead sunatListRead = new SunatListRead();
    
    public void actualizarListasSunat(){
    	SunatListDownload sunatListDownload = new SunatListDownload();
    	sunatListDownload.actualizarListasSunat("");
    }
    
    public void updateCDR_UBL_CDRSUNAT(long idComprobante, String tipo){
    	log.info(" updateCDR_UBL_CDRSUNAT ");
    	switch(tipo){
    	case "UBL":
    		String dirUBL = "D:\\OseServer\\send\\prod\\mesa\\20101914080-01-F003-00049435.xml";    		
    		comprobanteCRUD.upLoadUBLOseServer(idComprobante, dirUBL);
    		break;  
    	case "CDR":	
    		String dirCDR = "D:\\OseServer\\send\\prod\\mesa\\R-20101914080-01-F003-00049587.xml";   	
    		comprobanteCRUD.upLoadCDROseServer(idComprobante, dirCDR);    
    	case "CDR-SUNAT": 
    		String CDR_SUNAT = "D:\\OseServer\\send\\cdr\\SUNAT-R-20347268683-01-F002-0032888.xml";    	
    		comprobanteCRUD.upLoadCDRSUNAT(idComprobante, CDR_SUNAT);
    	}
    }
    
    public void updateMensajeSUNAT(String estado, String contar, String mensaje){
    	log.info("updateMensajeSUNAT: estado: "+estado+" - contar = "+contar);
    	RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
    	List<Documento> listTbComprobante = rsetPostGresJDBC.listComprobantesMensajeSunat(estado, contar, mensaje);
    	for(Documento tb : listTbComprobante){    		
    		log.info("RespuestaSunat_Estado --> "+tb.getNombre()+" - "+tb.getEstado()+" - "+tb.getRespuestaSunat());
    		sunatCDRRead.getRequestSunat(tb);   		    		   		  		
    		if((tb.getMensajeSunat()!=null) && (tb.getMensajeSunat().length > 0)){   			
    			String s = new String(tb.getMensajeSunat());
    			log.info("s \n"+s);   
    			sunatCDRRead.updateRespuestaMensajeLogSunatSunatList(tb); 	
    			continue;
    		}   		
    		if(sunatListRead.sunatListReview(tb)) {
    			tb.setMensajeSunat(new byte[0]);
    			sunatCDRRead.updateFileMensajeSunat(tb);
    			continue;
    		}
    		tb.setRespuestaSunat("9999");
    		sunatCDRRead.updateEstadoRespuestaLogSunat(tb);    		        	      		
    	}
    }  
         
    public void updateMensajeSUNATID(String id){
    	log.info("updateMensajeSUNATID id: "+id);
    	Documento tbc = new Documento();
    	tbc.setDireccion(" = "+id+" ");
    	RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
    	List<Documento> listTbComprobante = rsetPostGresJDBC.listComprobantes(tbc);
    	for(Documento tb : listTbComprobante){    		
    		log.info("RespuestaSunat_Estado --> "+tb.getNombre()+" - "+tb.getEstado()+" - "+tb.getRespuestaSunat());
    		sunatCDRRead.getRequestSunat(tb);   
    		log.info("tb.getMensajeSunat() \n "+tb.getMensajeSunat());
    		if((tb.getMensajeSunat()!=null) && (tb.getMensajeSunat().length > 0)){   			
    			String s = new String(tb.getMensajeSunat());
    			log.info("s \n"+s);   
    			sunatCDRRead.updateRespuestaMensajeLogSunatSunatList(tb); 	
    			continue;
    		}   		
    		if(sunatListRead.sunatListReview(tb)) {
    			tb.setMensajeSunat(new byte[0]);
    			sunatCDRRead.updateFileMensajeSunat(tb);
    			continue;
    		}
    		tb.setRespuestaSunat("9999");
    		sunatCDRRead.updateEstadoRespuestaLogSunat(tb);    		        	      		
    	}
    }      
    
//    public void updateErrorEstadoRespuesta(){
//    	log.info(" updateErrorEstadoRespuesta - DB = "+tipBD+" - ");
//    	ComprobanteCrud comprobanteCRUD = new ComprobanteCrud();
//    	long idComprobante = 2871;
//    	comprobanteCRUD.updateComprobanteContentEstado(idComprobante, tipBD);
//    }
//    
//    public void updateFileContent(){
//    	ComprobanteCrud comprobanteCRUD = new ComprobanteCrud();
//    	
//    	long idComprobante = 2696;
//    	String dirUBLZIP = "D:\\OseServer\\send\\Sunat\\20601162432-01-F006-00000001.zip";
//    	comprobanteCRUD.upLoadUBLZIPOseServer(idComprobante, tipBD, dirUBLZIP);
//    }
       
//    public void migrarTM2TS(int tipBD){
//    	SunatListCrud listSunatCRUD = new SunatListCrud();
//    	//listSunatCRUD.addTM2TS(tipBD);
//    	listSunatCRUD.updateTM2TS(tipBD);
//    }
//    
//    public void readCDRRespuestaSunat() {    	
//    	SunatCDRRead readCDRRespuestaSunat = new SunatCDRRead();   
//    	long idComprobante = 2284;
//    	readCDRRespuestaSunat.readCDRSunat2Comprobante(idComprobante, tipBD);       	
//    }
    
//	public void getDocumentoDirectory(String directoryName){
//		log.info("getDocumentoDirectory ... "+directoryName);
//		SunatListCrud sunatListCrud = new SunatListCrud();
//        File direBase = new File(directoryName);
//        File[] fileBaseList = direBase.listFiles();
//        if(fileBaseList != null){
//            for (File fileBase : fileBaseList) {
//                if (fileBase.isFile()) {
//                    log.info("1) fileBase path "+fileBase.getAbsolutePath());
//                    log.info("2) fileBase name "+fileBase.getName());
//                } else if (fileBase.isDirectory()) {     
//                	log.info("1a) fileBase path "+fileBase.getAbsolutePath());
//                    log.info("2a) fileBase name "+fileBase.getName());
//                    File direCPE = new File(fileBase.getAbsolutePath());
//                    File[] fileCPRList = direCPE.listFiles();
//                    if(fileCPRList != null){
//                    	
//                        for (File fileCPE : fileCPRList) {  
//                        	log.info("3a) fileCPE path "+fileCPE.getAbsolutePath());
//                            log.info("4a) fileCPE name "+fileCPE.getName());
//                            if (fileCPE.isFile()) {                        
//                            	log.info("4) fileBase path "+fileCPE.getAbsolutePath());
//                            	sunatListCrud.cargaListaCPE(tipBD, fileCPE.getAbsolutePath());
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        
//	}	   
}
