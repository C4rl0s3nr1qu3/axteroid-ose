package com.axteroid.ose.server.avatar.task;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.apirest.sunat.ConsultaSunat;
import com.axteroid.ose.server.avatar.dao.DocumentoPGDao;
import com.axteroid.ose.server.avatar.dao.impl.DocumentoPGDaoImpl;
import com.axteroid.ose.server.avatar.jdbc.RsetPostGresJDBC;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoAvatarPropertiesEnum;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.axteroid.ose.server.tools.xml.UblResponseSunat;

public class SunatCDRRead {
	private static final Logger log = LoggerFactory.getLogger(SunatCDRRead.class);
	private DocumentoPGDao tbComprobantePGDao = new DocumentoPGDaoImpl();
	private SunatListRead sunatListRead = new SunatListRead();
	private UblResponseSunat xmlReader = new UblResponseSunat(); 
	
	public void readCDRSunat2Comprobante(long idComprobante){		
		log.info("readCDRSunat2Comprobante --> "+idComprobante);
		try {
			Documento tbComprobante = new Documento();
			tbComprobante.setId(idComprobante);		
			RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
			rsetPostGresJDBC.listComprobantes(tbComprobante);		
			this.updateRespuestaSunat(tbComprobante);					
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("readCDRSunat2Comprobante Exception \n"+errors);
		}
	}
	
	public void updateRespuestaSunat(Documento tbComprobante) {
		log.info("updateRespuestaSunat --> "+tbComprobante.getId() );
		try {
			if(!(tbComprobante.getMensajeSunat()!=null)) 
				return;
			UblResponseSunat xmlReader = new UblResponseSunat();
			String respuestaSunat = xmlReader.readResponseCodeCDR(tbComprobante.getMensajeSunat());
			if(!(respuestaSunat!=null))
				return;
			tbComprobante.setEstado(Constantes.SUNAT_CDR_AUTORIZADO);
			if(!respuestaSunat.equals("0"))
				tbComprobante.setEstado(Constantes.SUNAT_CDR_ERROR_SENDSUMMARY);
			tbComprobante.setRespuestaSunat(respuestaSunat);			
			tbComprobante.setErrorLogSunat(xmlReader.readResponseCodeCDRNote(tbComprobante.getMensajeSunat()));			
			tbComprobantePGDao.updateRespuestaSunat(tbComprobante);			
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateRespuestaSunat Exception \n"+errors);
		}
	}
	
	public void updateEstadoRespuestaLogSunat(Documento tbComprobante) {
		log.info("updateEstadoRespuestaLogSunat --> "+tbComprobante.getId() );
		try {
			if((tbComprobante.getMensajeSunat()!=null)) {		
				String respuestaSunat = xmlReader.readResponseCodeCDR(tbComprobante.getMensajeSunat());
				tbComprobante.setRespuestaSunat(respuestaSunat);
				tbComprobante.setEstado(Constantes.SUNAT_CDR_AUTORIZADO);										
				String description = xmlReader.readResponseCodeCDRDescription(tbComprobante.getMensajeSunat());
				String description2 = description.replace("'", "");
				log.info("description --> "+description);
				tbComprobante.setErrorLogSunat(description2);	
			}
//			if(!tbComprobante.getRespuestaSunat().equals("0"))
//				tbComprobante.setEstado(ConstantesOse.SUNAT_CDR_SIN_RETORNO);
			tbComprobantePGDao.updateEstadoRespuestaLogSunat(tbComprobante);			
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateRespuestaLogSunat Exception \n"+errors);
		}
	}	

	public void updateRespuestaMensajeLogSunatSunatList(Documento tbComprobante) {
		log.info("updateRespuestaMensajeLogSunatSunatList --> "+tbComprobante.getId() );
		try {
			if(!(tbComprobante.getMensajeSunat()!=null)) 
				return;			
			log.info("tbComprobante.getMensajeSunat().length --> "+tbComprobante.getMensajeSunat().length );
			String respuestaSunat = xmlReader.readResponseCodeCDR(tbComprobante.getMensajeSunat());
			if(!(respuestaSunat!=null))
				return;
			tbComprobante.setRespuestaSunat(respuestaSunat);
			tbComprobante.setEstado(Constantes.SUNAT_CDR_AUTORIZADO);
			if(!respuestaSunat.equals("0")) {
				if(sunatListRead.sunatListReview(tbComprobante)) 
					tbComprobante.setRespuestaSunat("0");
//				else
//	    			tbComprobante.setEstado(ConstantesOse.SUNAT_CDR_SIN_RETORNO);				
			}	
			String description = xmlReader.readResponseCodeCDRDescription(tbComprobante.getMensajeSunat());
			String description2 = description.replace("'", "");
			log.info("description --> "+description);
			tbComprobante.setErrorLogSunat(description2);
			if(!respuestaSunat.equals("0")) 
				tbComprobante.setErrorLogSunat("Error "+respuestaSunat+": "+description2);			
			tbComprobantePGDao.updateRespuestaMensajeLogSunat(tbComprobante);			
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateRespuestaMensajeLogSunatSunatList Exception \n"+errors);
		}
	}		

	public void updateFileMensajeSunat(Documento tbComprobante) {
		log.info("updateFileMensajeSunat --> "+tbComprobante.getId() );
		try {
			if(!(tbComprobante.getMensajeSunat()!=null)) 
				return;						
			tbComprobantePGDao.updateFileMensajeSunat(tbComprobante);			
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("updateFileMensajeSunat Exception \n"+errors);
		}
	}	
	
    public void getRequestSunat(Documento tbComprobante) {
		String sunatFiles = DirUtil.getAvatarPropertiesValue(TipoAvatarPropertiesEnum.SUNAT_WS_OLITWSCONSCPEGEM_CONSULTA.getCodigo());
		if(sunatFiles.equals(Constantes.CONTENT_FALSE))
    		return ;
		log.info("getConsultaSunat --> "+tbComprobante.getRucEmisor()+" - "+ tbComprobante.getTipoDocumento()+" - "+tbComprobante.getSerie()+" - "+tbComprobante.getNumeroCorrelativo());
		String ruc = String.valueOf(tbComprobante.getRucEmisor());			
		Map<String, String> getStatus = ConsultaSunat.lookupCDR(ruc,tbComprobante.getTipoDocumento(),tbComprobante.getSerie(),tbComprobante.getNumeroCorrelativo());			
		if((getStatus.get("responseCode")!= null) && (getStatus.get("responseCode").equals(Constantes.SUNAT_RESPUESTA_0))){
			tbComprobante.setRespuestaSunat(Constantes.SUNAT_RESPUESTA_0);
			String base64CDR = getStatus.get("content");
			byte[] cdr = base64CDR.getBytes();
			byte[] zip = Base64.decodeBase64(cdr);
			byte[] xml = UblResponseSunat.extractXMLFromCDR(zip);
			tbComprobante.setMensajeSunat(xml);
			tbComprobante.setEstado(Constantes.SUNAT_CDR_AUTORIZADO);
		}else {
			if(getStatus.get("responseCode")!= null)
				tbComprobante.setRespuestaSunat(getStatus.get("responseCode"));
			if(getStatus.get("content")!= null) {
				String base64CDR = getStatus.get("content");
				byte[] cdr = base64CDR.getBytes();
				byte[] zip = Base64.decodeBase64(cdr);
				byte[] xml = UblResponseSunat.extractXMLFromCDR(zip);
				tbComprobante.setMensajeSunat(xml);
			}
		}
		if(getStatus.get("description")!= null)
			tbComprobante.setErrorLogSunat(getStatus.get("description"));
		
		for(Entry<String,String> entry : getStatus.entrySet()) {
			log.info("key: "+entry.getKey()+" value: "+entry.getValue());
		}
	}	
     
}
