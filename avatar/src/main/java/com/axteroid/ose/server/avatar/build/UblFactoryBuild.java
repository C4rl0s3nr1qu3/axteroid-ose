package com.axteroid.ose.server.avatar.build;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.dao.DocumentoPGDao;
import com.axteroid.ose.server.avatar.dao.impl.DocumentoPGDaoImpl;
import com.axteroid.ose.server.avatar.jdbc.RsetPostGresJDBC;
import com.axteroid.ose.server.avatar.service.SendOseSeverService;
import com.axteroid.ose.server.avatar.task.UblListCPEReview;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.FileUtil;
import com.axteroid.ose.server.tools.util.LogDateUtil;
import com.axteroid.ose.server.tools.xml.XmlSign;
import com.axteroid.ose.server.ubl21.sign.impl.SignDocumentUblImpl;

public class UblFactoryBuild {
	private static final Logger log = LoggerFactory.getLogger(UblFactoryBuild.class);
	static String username;	
	static String password;	
	static String ipJBoss;	
	static String url;
	Date fechaAnt;
	String dir_UBLSIGN_FIRST;
	String dir_UBLSIGN_CHANGE;
	String dir_UBLSIGN_LAST;
	
    public void repairSummeryUbl(String urlOSE, String ip, String usuario, String clave, String contar, String error_numero, 
    		String estado, int proceso, Date fecJBossInicio, Date fecJBossFin, Date fecDBInicio, Date fecDBFin, String error_log, String ruc) {    	   	
    	username = usuario;
    	password = clave;
    	ipJBoss = ip;
    	url = urlOSE;
    	String dir_UBLSIGN = new StringBuilder(Constantes.DIR_AXTEROID_OSE)
				.append(Constantes.DIR_AVATAR_FILE).toString();
		dir_UBLSIGN_FIRST = new StringBuilder(dir_UBLSIGN)
				.append(Constantes.DIR_AVATAR_FILE_SIGN_FIRST).toString();
		FileUtil.createDirectory(dir_UBLSIGN_FIRST);
		dir_UBLSIGN_CHANGE = new StringBuilder(dir_UBLSIGN)
				.append(Constantes.DIR_AVATAR_FILE_SIGN_CHANGE).toString();
		FileUtil.createDirectory(dir_UBLSIGN_CHANGE);
		dir_UBLSIGN_LAST = new StringBuilder(dir_UBLSIGN)
				.append(Constantes.DIR_AVATAR_FILE_SIGN_LAST).toString();
		FileUtil.createDirectory(dir_UBLSIGN_LAST);
    	int k=0;       	
    	fecDBFin = DateUtil.addDays(fecDBFin, 1);
		Date fecDBAct = fecDBInicio;
		while(LogDateUtil.esFechaMayor(fecDBFin,fecDBAct)) {
			k++;			
			String strFecDBAct = DateUtil.dateToStringYYYY_MM_DD(fecDBAct);
	    	String [] fecDB = strFecDBAct.split("-");
	    	int year = Integer.parseInt(fecDB[0]);
	    	int mes = Integer.parseInt(fecDB[1]);
	    	int dia = Integer.parseInt(fecDB[2]);  
	    	List<Documento> listdocumento = this.getDataRepair(contar, error_numero, estado, year, mes, dia, error_log, ruc);
	    	int total = listdocumento.size(); 
			log.info("Itera: "+k+" - sendOseSeverBDUbl: "+year +"/"+mes+"/"+dia+" - total: "+total);
			fecDBAct = DateUtil.addDays(fecDBAct, 1);
	    	if(total<=0)
	    		continue;
	    	int i = 0;
	    	UblListCPEReview ublListCPEReview = new UblListCPEReview();
	    	for(Documento documento : listdocumento) {
				i++;	
				log.info("Itera: {}.{}.1 | {} | RespuestaSunat: {}",k,i,documento.toString(),documento.getRespuestaSunat());
				if(documento.getEstado().trim().equals("78")) {
//					Long idContent = documento.getId();
//					log.info("Itera: {}.{}.1.1 | {} | RespuestaSunat: {}",k,i,documento.toString(),documento.getRespuestaSunat());
//					DocumentoCrud comprobanteCrud = new DocumentoCrud();
//					comprobanteCrud.updatedocumentoIDContent(documento);
//					log.info("Itera: {}.{}.1.2 | {} | RespuestaSunat: {}",k,i,documento.toString(),documento.getRespuestaSunat());
//					if(idContent == documento.getId()) {
//						this.sendUBLOseSever(documento);
//						continue;
//					}
					this.getSatus_ROBOTOseSever(documento);
					continue;
				}				
				log.info("Itera: {}.{}.2 | {} | RespuestaSunat: {}",k,i,documento.toString(),documento.getRespuestaSunat());				
				if(documento.getRespuestaSunat().trim().equals("9998")) {
					log.info("Itera: {}.{}.2.1 | {} | RespuestaSunat: {}",k,i,documento.toString(),documento.getRespuestaSunat());
					String[] error = documento.getErrorLogSunat().split(";");
					List<String> listSunatError = new ArrayList<String>();
					for(int j=0;j < error.length ;j++) {
						listSunatError.add(error[j]);
					}
					int countErrorCPE = this.repairSunatUBLSign(documento, documento.getRespuestaSunat(), listSunatError, documento.getUserCrea());
					if(countErrorCPE > 0) {
						log.info("Itera: {}.{}.2.2 | {} | RespuestaSunat: {} | countErrorCPE: {}",k,i,documento.toString(),documento.getRespuestaSunat(), countErrorCPE);
						this.updatedocumento(documento);		
						this.sendUBLOseSever(documento);
						continue;
					}
				}
				ublListCPEReview.validateComprobantesReview(documento);
				log.info("Itera: {}.{}.3 | {} | RespuestaSunat: {} | ErrorLogSunat: {}",k,i,documento.toString(),documento.getRespuestaSunat(), documento.getErrorLogSunat());
				if((documento.getErrorLogSunat()!= null) && 
						(!documento.getErrorLogSunat().isEmpty())) {
					String[] error = documento.getErrorLogSunat().split(";");
					List<String> listSunatError = new ArrayList<String>();
					for(int j=0;j < error.length ;j++) {
						listSunatError.add(error[j]);
					}
					int countErrorCPE = this.repairSunatUBLSign(documento, documento.getRespuestaSunat(), listSunatError, documento.getUserCrea());
					if(countErrorCPE > 0) {
						log.info("Itera: {}.{}.3,1 | {} | RespuestaSunat: {} | countErrorCPE: {}",k,i,documento.toString(),documento.getRespuestaSunat(), countErrorCPE);
						this.updatedocumento(documento);		
						this.sendUBLOseSever(documento);
						continue;
					}
				}
				ublListCPEReview.readRespuestaSunat(documento);
				log.info("Itera: {}.{}.4 | {} | RespuestaSunat: {}| ErrorLogSunat: {}",k,i,documento.toString(),documento.getRespuestaSunat(), documento.getErrorLogSunat());
				if((documento.getErrorLogSunat()!= null) && 
						(!documento.getErrorLogSunat().isEmpty())) {				
					String errorLogSunat = documento.getErrorLogSunat();
					List<String> listSunatError = ublListCPEReview.getComprobantesResumenSUNAT(errorLogSunat);
					int countErrorCPE = this.repairSunatUBLSign(documento, documento.getRespuestaSunat(), listSunatError, documento.getUserCrea());
					if(countErrorCPE > 0) {
						log.info("Itera: {}.{}.4.1 | {} | RespuestaSunat: {} | countErrorCPE: {}",k,i,documento.toString(),documento.getRespuestaSunat(), countErrorCPE);
						this.updatedocumento(documento);		
						this.sendUBLOseSever(documento);
						continue;
					}
				}
				log.info("Itera: {}.{}.5 | {} | RespuestaSunat: {}",k,i,documento.toString(),documento.getRespuestaSunat());				
				if(documento.getRespuestaSunat().equals("9998")) {									
						List<String> listSunatError = new ArrayList<String>();
						listSunatError.add("00-00-00");
						int countErrorCPE = this.repairSunatUBLSign(documento, "9998", listSunatError, documento.getUserCrea());
						if(countErrorCPE > 0) {
							log.info("Itera: {}.{}.5.1 | {} | RespuestaSunat: {} | countErrorCPE: {}",k,i,documento.toString(),documento.getRespuestaSunat(), countErrorCPE);
							this.updatedocumento(documento);		
							this.sendUBLOseSever(documento);
							continue;
						}
				}
				log.info("Itera: {}.{}.6 | {} | RespuestaSunat: {}| ErrorLogSunat: {}",k,i,documento.toString(),documento.getRespuestaSunat(), documento.getErrorLogSunat());
				if(documento.getRespuestaSunat().equals("9999")) {
					List<String> listSunatError = new ArrayList<String>();
					listSunatError.add("00-00-00");
					if((documento.getErrorLog()!= null) && 
							(!documento.getErrorLog().isEmpty())) {										
						String errorLogSunat = documento.getErrorLog();
						listSunatError = ublListCPEReview.getComprobantesResumen(errorLogSunat);
					}
					log.info("Itera: {}.{}.6.1 | {} | RespuestaSunat: {} | listSunatError: {}",k,i,documento.toString(),documento.getRespuestaSunat(),listSunatError);
					int countErrorCPE = this.repairSunatUBLSign(documento, "9999", listSunatError, documento.getUserCrea());
					if(countErrorCPE > 0) {
						log.info("Itera: {}.{}.6.2 | {} | RespuestaSunat: {} | countErrorCPE: {}",k,i,documento.toString(),documento.getRespuestaSunat(),countErrorCPE);
						this.updatedocumento(documento);		
						this.sendUBLOseSever(documento);
						continue;
					}						
					
				}
				this.getSatus_ROBOTOseSever(documento);				
			}
		}	
    }  
    
	public List<Documento> getDataRepair(String contar, String error_numero, String estado, 
			int year, int mes, int dia, String error_log, String ruc){		
		log.info(" getDataRepair ... ");
		List<Documento> listdocumento = new ArrayList<Documento>();
		try {
	    	Documento tbcConsulta = new Documento();
	    	tbcConsulta.setLongitudNombre(Integer.valueOf(contar));
	    	//String errSel = "in('"+error_numero+"')";
	    	tbcConsulta.setErrorComprobante(Constantes.CONTENT_TRUE.toCharArray()[0]);
	    	if(!error_numero.equals("in ('9997')"))
	    		tbcConsulta.setRespuestaSunat(error_numero);
	    	String sEstado = "in('"+estado+"')";
	    	tbcConsulta.setEstado(sEstado);	   	
	    	tbcConsulta.setUblVersion(" = "+year+" ");
	    	tbcConsulta.setCustomizaVersion(" = "+mes+" ");
	    	tbcConsulta.setErrorLogSunat(" = "+dia+" ");
	    	//tbcConsulta.setErrorLog(error_log);
	    	//tbcConsulta.setObservaDescripcion(ruc);
	    	//String addUsuario = error_log +" AND USER_CREA "+ruc+" ";	
	    	if(ruc!=null) {
	    		String addUsuario = " USER_CREA "+ruc+" ";	
	    		tbcConsulta.setErrorLog(addUsuario);
	    	}
	    	tbcConsulta.setUserCrea("0");						
			RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
			listdocumento = rsetPostGresJDBC.listComprobantes(tbcConsulta);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDataRepair Exception \n"+errors);		
		}
		return listdocumento;		    
	}
    		
	public int repairSunatUBLSign(Documento documento, String error_numero, 
			List<String> listSunatError, String idCertificadoEmisor){		
		log.info(" repairSunatUBLSign ... ");
		int countErrorCPE = 0;
		try {
			String documentLine = "";
			if((documento.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS)) ||
					(documento.getTipoDocumento().equals(Constantes.SUNAT_REVERSION)))
				documentLine = "sac:VoidedDocumentsLine";
			if(documento.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO))
				documentLine = "sac:SummaryDocumentsLine";
			String rutaFirst = (new StringBuilder(dir_UBLSIGN_FIRST)).append(File.separator).append(documento.getNombre()).toString();
			String rutaChange = (new StringBuilder(dir_UBLSIGN_CHANGE)).append(File.separator).append(documento.getNombre()).toString();
			String rutaLast = (new StringBuilder(dir_UBLSIGN_LAST)).append(File.separator).append(documento.getNombre()).toString();
			XmlSign xmlSign = new XmlSign();
			File fileFirst = new File(rutaFirst);
			FileUtil.writeToFile(fileFirst,documento.getUbl());	
			File fileChange = new File(rutaChange);
			FileUtil.writeToFile(fileChange,documento.getUbl());
			for(String error : listSunatError){
				log.info("error_numero: "+error_numero+" | "+"error: "+error);					
				boolean bCpeUbl = false;
				if(error_numero.equals("9998")) {
					bCpeUbl = true;
					continue;
				}					
				File fileRepairSignDelete = new File(rutaChange);
				byte[] bRepairSignDelete  = FileUtil.toByte(fileRepairSignDelete);
				String[] cpe = error.split("-");
				String documentTypeCode = cpe[0].replace("[", "");
				if(cpe.length != 3)
					continue;
				String id1 = cpe[2].replace("]", "");
				String id = cpe[1]+"-"+id1;
				log.info("error_numero: "+error_numero+" | "+documentTypeCode+"-"+id);
				switch(error_numero){
				case "2282":
					bCpeUbl = xmlSign.updateUBLResumen(bRepairSignDelete, rutaChange, 
							documentTypeCode, id, documentLine);										
					break;
				case "2987" :
					bCpeUbl = xmlSign.updateUBLResumen(bRepairSignDelete, rutaChange, 
							documentTypeCode, id, documentLine);	
					break;
				case "2989" :
					bCpeUbl = xmlSign.updateUBLResumen(bRepairSignDelete, rutaChange, 
							documentTypeCode, id, documentLine);
					break;	
				case "2323":
					bCpeUbl = xmlSign.updateUBLResumen(bRepairSignDelete, rutaChange, 
							documentTypeCode, id, documentLine);
					break;
				case "2324":
					bCpeUbl = xmlSign.updateUBLResumen(bRepairSignDelete, rutaChange, 
							documentTypeCode, id, documentLine);
					break;					
				case "2990" :
					bCpeUbl = xmlSign.updateUBLResumen(bRepairSignDelete, rutaChange, 
							documentTypeCode, id, documentLine);
					break;
				case "2398" :
					bCpeUbl = xmlSign.updateUBLResumen(bRepairSignDelete, rutaChange, 
							documentTypeCode, id, documentLine);
					break;	
				case "2663" :
					bCpeUbl = xmlSign.updateUBLResumen(bRepairSignDelete, rutaChange, 
							documentTypeCode, id, documentLine);	
					break;
				case "2957" :
					bCpeUbl = xmlSign.updateUBLResumen(bRepairSignDelete, rutaChange, 
							documentTypeCode, id, documentLine);
					break;
				case "2223" :
					bCpeUbl = xmlSign.updateUBLResumen(bRepairSignDelete, rutaChange, 
							documentTypeCode, id, documentLine);
					break;
				case "2105" :
					bCpeUbl = xmlSign.updateUBLResumen(bRepairSignDelete, rutaChange, 
							documentTypeCode, id, documentLine);
					break;
				case "0099" :
					bCpeUbl = xmlSign.updateUBLResumen(bRepairSignDelete, rutaChange, 
							documentTypeCode, id, documentLine);
					break;				
				case "9999" :
					bCpeUbl = xmlSign.updateUBLResumen(bRepairSignDelete, rutaChange, 
							documentTypeCode, id, documentLine);
					break;
				}
				if(bCpeUbl) {
					countErrorCPE++;
					log.info(countErrorCPE+") >>>>>>>>>>>  REMOVE: "+documentTypeCode+"-"+id);
				}
			}
			//log.info("countErrorCPE: {} | CDR: ",countErrorCPE, documento.getCdr());
			if((countErrorCPE == 0) && (documento.getCdr() == null))
				countErrorCPE++;
			//log.info("countErrorCPE: {} | CDR: ",countErrorCPE, documento.getCdr().length);
			if(countErrorCPE > 0){
				String sFechaEmision = XmlSign.ObtenerFechaEmisionFromUBL(documento.getUbl());
				Date fechaEmision = DateUtil.stringToDateYYYY_MM_DD(sFechaEmision);
				File fileSignDelete = new File(rutaChange);
				byte[] bSignDelete  = FileUtil.toByte(fileSignDelete); 
				xmlSign.updateUBLDeleteSign(bSignDelete,rutaLast);
				File fileSign = new File(rutaLast);
				byte[] bSign  = FileUtil.toByte(fileSign);
				SignDocumentUblImpl signDocument = new SignDocumentUblImpl();
				byte[] ublSign  = signDocument.signDocumento(bSign, rutaLast, idCertificadoEmisor, fechaEmision);	
				if(ublSign.length > 0)
					documento.setUbl(ublSign);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("repairUBLSign Exception \n"+errors);	
		}
		return countErrorCPE;
	}			
	
	private void updatedocumento(Documento documento) {
		log.info(" updatedocumento: "+documento.getNombre()); 
		String fechaEmision = XmlSign.ObtenerFechaEmisionFromUBL(documento.getUbl());			
		documento.setErrorLog(fechaEmision);		
		documento.setEstado(Constantes.SUNAT_CDR_ERROR_PROCESO);
		documento.setErrorComprobante(Constantes.CONTENT_FALSE.toCharArray()[0]);
		try {		
			DocumentoPGDao documentoPGDao = new DocumentoPGDaoImpl();
			documentoPGDao.updateUblEstErLog(documento);
		} catch (SQLException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updatedocumento SQLException \n"+errors);
		}		
	}
		
    private void sendUBLOseSever(Documento documento) { 
		try {
			log.info(" sendUBLOseSever: "+documento.getNombre()+" | "+documento.getUserCrea());   
			SendOseSeverService sendOseSeverService = new SendOseSeverService();
			String retorno = sendOseSeverService.sendOseSever(url, documento);	
			log.info("retorno: " +  retorno );
			//if(documento.getTipoComprobante().trim().equals(OseConstantes.SUNAT_RESUMEN_DIARIO)) 
			//	Thread.sleep(10000);
			//else
			//	Thread.sleep(1000);
    	} catch (Exception e){
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("sendUBLOseSever Exception \n"+errors);
        }  	   		
    }    	
    
    public void getSatus_ROBOTOseSever(Documento documento) {    	    	    	   		
		try {			
		   	String ticket = "SEND_CDR @ "+documento.getId();  	
		   	log.info("getSatus_ROBOTOseSever ticket "+ticket);		
		   	SendOseSeverService sendOseSeverService = new SendOseSeverService();
		   	sendOseSeverService.getSatus_ROBOT(ticket, url);
		   	
    	} catch (Exception e){
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getSatus_ROBOTOseSever Exception \n"+errors);
        }  	   		
    }
}
