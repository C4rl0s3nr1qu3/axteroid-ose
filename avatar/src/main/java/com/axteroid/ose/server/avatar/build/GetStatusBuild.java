package com.axteroid.ose.server.avatar.build;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.xml.soap.SOAPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.axteroid.ose.server.avatar.dao.DocumentoPGDao;
import com.axteroid.ose.server.avatar.dao.SunatDetalleResumenesDao;
import com.axteroid.ose.server.avatar.dao.impl.DocumentoPGDaoImpl;
import com.axteroid.ose.server.avatar.dao.impl.SunatDetalleResumenesDaoImpl;
import com.axteroid.ose.server.avatar.jdbc.RsetPostGresJDBC;
import com.axteroid.ose.server.avatar.model.TmCeDocumento;
import com.axteroid.ose.server.avatar.service.SendOseSeverService;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.DateUtil;

public class GetStatusBuild {
	private static final Logger log = LoggerFactory.getLogger(GetStatusBuild.class);
	Timestamp timestampStart;
    Timestamp timestampEnd;				
	
    public void getStatusOne(String urlSunat, Documento documento, int tipoList, int status) throws SOAPException {    	    	    	
    	List<TmCeDocumento> list = this.getListOSEProduccion_Status(documento, tipoList);
    	int i = 1;
    	int total = list.size();
    	for(TmCeDocumento t : list){     
    		MDC.put("FILE", t.getNombrearchivo());
    		try {
				String td = t.getNombrearchivo().substring(12, 14);
				String dato = String.valueOf(t.getNombrearchivo());
				log.info(i+" of "+total+" Tipo documento: "+td+ " - Filename: "+ dato );
				String ticket = "";
			   	switch(status) {
			   		case 1:
			   			ticket = "SEND_CDR @ "+t.getId().getId();
			   			break;
			   		case 2:
			   			ticket = "GET_CDR @ "+t.getId().getId();
			   			break;
			   		case 3:
			   			ticket = "PORTAL_CDR @ "+t.getId().getId();
			   			break;   
			   		case 4:
			   			ticket = "UPDATE_CPE @ "+t.getId().getId();
			   			break;	
			   	} 	   	
			   	log.info("ticket "+ticket);		
			   	SendOseSeverService sendOseSeverService = new SendOseSeverService();
			   	sendOseSeverService.getSatus_ROBOT(ticket, urlSunat);
			   	i++;
        	} catch (Exception e){
        		i++;
    			StringWriter errors = new StringWriter();				
    			e.printStackTrace(new PrintWriter(errors));
    			log.error("getStatusOne Exception \n"+errors);
            }  	   		 
    	}
    }
    
    public void getStatusThread(String urlSunat, Documento documento, int tipoList, int status,
    		int nRun, long nEspera) throws SOAPException {    	
    	log.info("getStatusThread: ");	
    	List<TmCeDocumento> list = this.getListOSEProduccion_Status(documento, tipoList);  
    	ThreadBuild threadBuild = new ThreadBuild();
    	threadBuild.getStatusThread(urlSunat, list, status, nRun, nEspera);   	
    }

    public void getStatusThread_Hard_(String urlSunat, Documento documento, int tipoList, int status) throws SOAPException {    	
    	List<TmCeDocumento> list = this.getListOSEProduccion_Status(documento, tipoList);  
    	ThreadBuild threadBuild = new ThreadBuild();
    	threadBuild.getStatusThread_Hard(urlSunat, list, status);   	
    }
    
    public List<TmCeDocumento> getListOSEProduccion_Status(Documento documento, int tipoList) {
        //log.info("getListOSEProduccion_Status ...");     
    	RsetPostGresJDBC rsetPostGresJDBC= new RsetPostGresJDBC();
        List<TmCeDocumento> list = rsetPostGresJDBC.getData(documento, tipoList);                 
    	return list;
    }    
       
    public void updateFacturaOseTD(int year, int mes, int dia, int hora, String TD) throws SOAPException {    	
    	List<Documento> list = this.getListOSEProduccionTD_FECHA(year, mes, dia, hora, TD);
    	ThreadBuild threadBuild = new ThreadBuild();
    	threadBuild.updateFacturaOse(list);  
    }    

    public void updateFacturaOseTD_SS(int year, int mes, int dia, int hora, String TD) throws SOAPException {    	
    	List<Documento> list = this.getListOSEProduccionTD_FECHA(year, mes, dia, hora, TD);
    	this.updateFacturaOse(list);    	
    }

    public void updateFacturaOseTD_SSShort(String fecha, String TD) throws SOAPException {    	
    	List<Documento> list = this.getListOSEProduccionTD_FECHAShort(fecha, TD);
    	this.updateFacturaOse(list);    	
    }    
    
//    public void updateFacturaOseTD_OLD(int status, int year, int mes, int dia, int hora, String TD) throws SOAPException {    	
//    	List<Documento> list = this.getListOSEProduccionTD_FECHA_OLD(year, mes, dia, hora, TD);
//    	ThreadBuild threadBuild = new ThreadBuild();
//    	threadBuild.updateFacturaOse(list);    	
//    }
//    
//    public void updateFacturaOseTD_OLD_SS(int year, int mes, int dia, int hora, String TD) throws SOAPException {    	
//    	List<Documento> list = this.getListOSEProduccionTD_FECHA_OLD(year, mes, dia, hora, TD);
//    	this.updateFacturaOse(list);    	
//    }
    
    public void updateFacturaOseTD_SSMIN(int year, int mes, int dia, int hora, String TD, String min) throws SOAPException {    	
    	List<Documento> list = this.getListOSEProduccionTD_FECHAMIN(year, mes, dia, hora, TD, min);
    	this.updateFacturaOse(list);    	
    }
    
    private void updateFacturaOse(List<Documento> list) throws SOAPException { 
    	int total = list.size();
    	int i=0;
    	for(Documento documento : list){   
    		i++;
    		try {
    			log.info(i+" of "+total+" documento: "+documento.getNombre());  
    			FacturaOseBuild facturaOseBuild = new FacturaOseBuild();
    			facturaOseBuild.sendDocumento(documento);   
        	} catch (Exception e){
    			StringWriter errors = new StringWriter();				
    			e.printStackTrace(new PrintWriter(errors));
    			log.error("updateFacturaOse Exception \n"+errors);
            }  	   		
    	}
    }    
    
    List<Documento> getListOSEProduccionAll_FECHA(int year, int mes, int dia, int hora) {
        //log.info("getListOSEProduccionAll_FECHA ...");   
    	RsetPostGresJDBC rsetPostGresJDBC= new RsetPostGresJDBC();
    	Documento tbcConsulta = new Documento();	   	
    	tbcConsulta.setUblVersion(" = "+year+" ");
    	tbcConsulta.setCustomizaVersion(" = "+mes+" ");
    	tbcConsulta.setErrorLogSunat(" = "+dia+" ");
    	tbcConsulta.setAdvertencia(" = "+hora+" ");   
    	List<Documento> list = rsetPostGresJDBC.listComprobantes(tbcConsulta);     	        
        return list;
    }  
    
//    List<Documento> getListOSEProduccionAll_FECHA_OLD(int year, int mes, int dia, int hora) {
//        //log.info("getListOSEProduccionAll_FECHA_OLD ...");   
//    	RsetPostGresJDBC rsetPostGresJDBC= new RsetPostGresJDBC();
//    	Documento tbcConsulta = new Documento();	   	
//    	tbcConsulta.setUblVersion(" = "+year+" ");
//    	tbcConsulta.setCustomizaVersion(" = "+mes+" ");
//    	tbcConsulta.setErrorLogSunat(" = "+dia+" ");
//    	tbcConsulta.setAdvertencia(" = "+hora+" ");   
//    	List<Documento> list = rsetPostGresJDBC.listComprobantesOld(tbcConsulta); 
//    	return list;
//    }  
       
    List<Documento> getListOSEProduccionTD_FECHA(int year, int mes, int dia, int hora, String td) {
        //log.info("getListOSEProduccionTD_FECHA ...");   
    	RsetPostGresJDBC rsetPostGresJDBC= new RsetPostGresJDBC();
    	Documento tbcConsulta = new Documento();	   	
    	tbcConsulta.setUblVersion(" = "+year+" ");
    	tbcConsulta.setCustomizaVersion(" = "+mes+" ");
    	tbcConsulta.setErrorLogSunat(" = "+dia+" ");
    	tbcConsulta.setAdvertencia(" = "+hora+" "); 
    	tbcConsulta.setTipoDocumento(td);
    	List<Documento> list = rsetPostGresJDBC.listComprobantes(tbcConsulta); 
    	return list;
    }  
    
    public List<Documento> getListOSEProduccionTD_FECHAShort(String fecha, String td) { 
    	//log.info("getListOSEProduccionTD_FECHAShort ..."); 
    	RsetPostGresJDBC rsetPostGresJDBC= new RsetPostGresJDBC();
    	Documento tbcConsulta = new Documento();	   	
    	tbcConsulta.setNombre(fecha);
    	tbcConsulta.setTipoDocumento(td);
    	List<Documento> list = rsetPostGresJDBC.listComprobantes(tbcConsulta); 
    	return list;
    }      
    
//    List<Documento> getListOSEProduccionTD_FECHA_OLD(int year, int mes, int dia, int hora, String td) {
//        //log.info("getListOSEProduccionTD_FECHA_OLD ...");   
//    	RsetPostGresJDBC rsetPostGresJDBC= new RsetPostGresJDBC();
//    	Documento tbcConsulta = new Documento();	   	
//    	tbcConsulta.setUblVersion(" = "+year+" ");
//    	tbcConsulta.setCustomizaVersion(" = "+mes+" ");
//    	tbcConsulta.setErrorLogSunat(" = "+dia+" ");
//    	tbcConsulta.setAdvertencia(" = "+hora+" "); 
//    	tbcConsulta.setTipoDocumento(td);
//    	List<Documento> list = rsetPostGresJDBC.listComprobantesOld(tbcConsulta);     	
//    	return list;
//    } 
    
    List<Documento> getListOSEProduccionTD_FECHAMIN(int year, int mes, int dia, int hora, String td, String min) {
        //log.info("getListOSEProduccionTD_FECHAMIN ...");   
    	RsetPostGresJDBC rsetPostGresJDBC= new RsetPostGresJDBC();
    	Documento tbcConsulta = new Documento();	   	
    	tbcConsulta.setUblVersion(" = "+year+" ");
    	tbcConsulta.setCustomizaVersion(" = "+mes+" ");
    	tbcConsulta.setErrorLogSunat(" = "+dia+" ");
    	tbcConsulta.setAdvertencia(" = "+hora+" "); 
    	tbcConsulta.setTipoDocumento(td);
    	tbcConsulta.setUserModi(min);
    	List<Documento> list = rsetPostGresJDBC.listComprobantes(tbcConsulta);  
    	return list;
    }  
    
    public void revisarFacturaOseTDSSShort(String fecha, String TD, 
    		List<String> listDetalleResumen) throws SOAPException {  
    	List<Documento> list = new ArrayList<Documento>();
    	List<Documento> listdocumento = this.getListOSEProduccionTD_FECHAShort(fecha, TD);
    	int i = 0;
    	int k = listdocumento.size();
    	for(Documento tb : listdocumento) {
    		i++;
    		String idComprobante = tb.getIdComprobante();   		
    		String resulIdComprobante = listDetalleResumen.stream().filter(x ->  idComprobante.equals(x)).findAny().orElse("");
    		//log.info(i+"/"+k+") idComprobante; "+idComprobante);
    		if(resulIdComprobante.isEmpty()) {
    			log.info(i+"/"+k+") idComprobante; "+idComprobante);
    			list.add(tb);
    		}
    	}    	
    	this.updateFacturaOse(list);    	
    }   
    
    public List<String> getListDetalleResumenTDSSShort(String fecha, String td) { 
    	//log.info("getListFacturaOseTDSSShort ..."); 
    	try {
    		SunatDetalleResumenesDao tmDetalleResumenesDao= new SunatDetalleResumenesDaoImpl();
    		Documento tbcConsulta = new Documento();	   	
    		tbcConsulta.setNombre(fecha);
    		tbcConsulta.setTipoDocumento(td);
    		List<String> list = tmDetalleResumenesDao.getListDetalleResumenTDSSShort(tbcConsulta);
    		return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	return new ArrayList<String>();
    }  
    
    public void getStatusOneQA(String urlSunat, Documento documento, int tipoList, int status) throws SOAPException {    	    	    	
    	RsetPostGresJDBC rsetPostGresJDBC= new RsetPostGresJDBC();
    	rsetPostGresJDBC.setTipoList(tipoList);
    	List<Documento> list = rsetPostGresJDBC.listComprobantes(documento);
    	int i = 1;
    	int total = list.size();
    	for(Documento t : list){     
    		MDC.put("FILE", t.getNombre());
    		try {
				log.info("{} of {}) Documento: {}",i,total,t.toString() );
				String ticket = "";
			   	switch(status) {
			   		case 1:
			   			ticket = "El documento "+t.getNombre()+" no tiene CDR";
			   			if(t.getEstado().trim().equals(Constantes.SUNAT_CDR_GENERADO)) {
			   				this.updateEstadoRespuestaLogSunatQA(t);
			   				ticket = "SEND_CDR @ "+t.getId();
			   			}
			   			break;
			   		case 2:
			   			ticket = "GET_CDR @ "+t.getId();
			   			break;
			   		case 3:
			   			ticket = "PORTAL_CDR @ "+t.getId();
			   			break;   
			   		case 4:
			   			ticket = "UPDATE_CPE @ "+t.getId();
			   			break;	
			   	} 	   	
			   	log.info("ticket; {}"+ticket);		
			   	i++;
        	} catch (Exception e){
    			StringWriter errors = new StringWriter();				
    			e.printStackTrace(new PrintWriter(errors));
    			log.error("getStatusOneQA Exception \n"+errors);
            }  	   		 
    	}
    }
    
	public void updateEstadoRespuestaLogSunatQA(Documento documento) {
		DocumentoPGDao documentoPGDao = new DocumentoPGDaoImpl();
    	try {
			Documento tb = new Documento();
			tb.setId(documento.getId());			
			tb.setEstado(Constantes.SUNAT_CDR_AUTORIZADO);
			tb.setRespuestaSunat(Constantes.CONTENT_TRUE);
			String respuesta = "OseSever OSE-QA acepto documento "+documento.getNombre();
			tb.setErrorLogSunat(respuesta);
			tb.setFechaRespuestaSunat(DateUtil.getCurrentTimestamp());
			tb.setUserModi(documento.getUserCrea());
			tb.setFechaModi(DateUtil.getCurrentTimestamp());			
			documentoPGDao.updateEstadoRespuestaLogSunatQA(tb); 
			return;
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("updateEstadoRespuestaLogSunatQA \n"+errors);
		}
	}		    
    
}
