package com.axteroid.ose.server.avatar.worker;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.bean.GroupBean;
import com.axteroid.ose.server.avatar.build.GetStatusBuild;
import com.axteroid.ose.server.avatar.jdbc.RsetPostGresJDBC;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoAvatarPropertiesEnum;
import com.axteroid.ose.server.tools.util.DirUtil;

public class EnviarSunatWorker {
	
    static Logger log = LoggerFactory.getLogger(EnviarSunatWorker.class);
    private String sThread = "1"; 
    private String nRun = "0"; 
    private String nEspera = "0";
    public void worker(){    					
    	Map<String, String> mapa = DirUtil.getMapRobotProperties();
    	if((mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_THREAD.getCodigo())!=null) &&
				(mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_THREAD.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
    		this.sThread = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_THREAD.getCodigo());
    		this.nRun = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_THREAD_RUN.getCodigo());
    		this.nEspera = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_THREAD_SLEEP.getCodigo());
    	}    	   	
		if((mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30.getCodigo())!=null) &&
				(mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
			log.info("COMUNICA_SUNAT_BIZPOSTGRE_30: " + mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30.getCodigo()));
			String urlSunat = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30_URL.getCodigo());
			String contar = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30_CONTAR.getCodigo());
			this.comunicaSunatBIZPostGres_30(urlSunat, contar);
		}	
		if((mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30_TD.getCodigo()) != null) &&
				(mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30_TD.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
			log.info("COMUNICA_SUNAT_BIZPOSTGRE_30TD: " + mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30_TD.getCodigo()));
			String urlSunat = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30_TD_URL.getCodigo());
			String contar = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30_TD_CONTAR.getCodigo());
			String documentos = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30_TD_DOCUMENTO.getCodigo());
			this.comunicaSunatBIZPostGres_30TD(urlSunat, contar, documentos);
		}			
		if((mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30FECHA.getCodigo())!=null) &&
				(mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30FECHA.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
			log.info("COMUNICA_SUNAT_BIZPOSTGRE_30Fecha: " + mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30FECHA.getCodigo()));
			String urlSunat = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30FECHA_URL.getCodigo());
			String fechaIniDB = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30FECHA_FECHA_INI.getCodigo());
			String contar = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30FECHA_CONTAR.getCodigo());
			this.comunicaSunatBIZPostGres_30Fecha(urlSunat, contar, fechaIniDB);
		}			
		if((mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30.getCodigo())!=null) &&
				(mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30.getCodigo()).trim().equals(Constantes.CONTENT_QA))) {
			log.info("COMUNICA_SUNAT_BIZPOSTGRE_30QA: " + mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30.getCodigo()));
			String urlSunat = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30_URL.getCodigo());
			String contar = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30_CONTAR.getCodigo());
			this.comunicaSunatBIZPostGres_30QA(urlSunat, contar);
		}		
		if((mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30_TD.getCodigo()) != null) &&
				(mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30_TD.getCodigo()).trim().equals(Constantes.CONTENT_QA))) {
			log.info("COMUNICA_SUNAT_BIZPOSTGRE_30TDQA: " + mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30_TD.getCodigo()));
			String urlSunat = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30_TD_URL.getCodigo());
			String contar = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30_TD_CONTAR.getCodigo());
			String documentos = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30_TD_DOCUMENTO.getCodigo());
			this.comunicaSunatBIZPostGres_30TDQA(urlSunat, contar, documentos);
		}	
		if((mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30FECHA.getCodigo())!=null) &&
				(mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30FECHA.getCodigo()).trim().equals(Constantes.CONTENT_QA))) {
			log.info("COMUNICA_SUNAT_BIZPOSTGRE_30FechaQA: " + mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30FECHA.getCodigo()));
			String urlSunat = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30FECHA_URL.getCodigo());
			String fechaIniDB = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30FECHA_FECHA_INI.getCodigo());
			String contar = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_30FECHA_CONTAR.getCodigo());
			this.comunicaSunatBIZPostGres_30FechaQA(urlSunat, contar, fechaIniDB);
		}		

		if((mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_VARIOS.getCodigo()) != null) &&
				(mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_VARIOS.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
			log.info("COMUNICA_SUNAT_BIZPOSTGRE_VARIOS: " + mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_VARIOS.getCodigo()));
			String urlSunat = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_VARIOS_URL.getCodigo());
			String estado = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_VARIOS_ESTADO.getCodigo());
			String respuestaSunat = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_VARIOS_RESPUESTASUNAT.getCodigo());
			String contar = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_VARIOS_CONTAR.getCodigo());
			this.comunicaSunatBIZPostGres_VARIOS(urlSunat, estado, respuestaSunat, contar);
		}		
		if((mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_VARIOS_LN.getCodigo()) != null) &&
				(mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_VARIOS_LN.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
			log.info("COMUNICA_SUNAT_BIZPOSTGRE_VARIOS_LN: " + mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_VARIOS_LN.getCodigo()));
			String urlSunat = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_VARIOS_LN_URL.getCodigo());
			String estado = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_VARIOS_LN_ESTADO.getCodigo());
			String respuestaSunat = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_VARIOS_LN_RESPUESTASUNAT.getCodigo());
			String contar = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_VARIOS_LN_CONTAR.getCodigo());
			this.comunicaSunatBIZPostGres_VARIOS_LN(urlSunat, estado, respuestaSunat, contar);
		}			
		if((mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_ESTADO.getCodigo()) != null) &&
				(mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_ESTADO.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
			log.info("COMUNICA_SUNAT_BIZPOSTGRE_ESTADO: " + mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_ESTADO.getCodigo()));
			String urlSunat = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_ESTADO_URL.getCodigo());
			String contar = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_ESTADO_CONTAR.getCodigo());
			//int year = Integer.parseInt(mapa.get(TipoFileRobotProperEnum.COMUNICA_SUNAT_POSTGRE_ESTADO_YEAR.getCodigo()));
			String estado = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_ESTADO_ESTADO.getCodigo());
			this.comunicaSunatBIZPostGres_ESTADO(urlSunat, contar, estado);
		}		
		if((mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_ESTADO_LN.getCodigo()) != null) &&
				(mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_ESTADO_LN.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
			log.info("COMUNICA_SUNAT_BIZPOSTGRE_ESTADO_LN: " + mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_ESTADO_LN.getCodigo()));
			String urlSunat = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_ESTADO_LN_URL.getCodigo());
			String contar = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_ESTADO_LN_CONTAR.getCodigo());
			//int year = Integer.parseInt(mapa.get(TipoFileRobotProperEnum.COMUNICA_SUNAT_POSTGRE_ESTADO_LN_YEAR.getCodigo()));
			String estado = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_ESTADO_LN_ESTADO.getCodigo());
			this.comunicaSunatBIZPostGres_ESTADO_LN(urlSunat, contar, estado);
		}		
		if((mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_GROUP.getCodigo()) != null) &&
				(mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_GROUP.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
			log.info("COMUNICA_SUNAT_BIZPOSTGRE_GROUP: " + mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_GROUP.getCodigo()));
			String urlSunat = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_GROUP_URL.getCodigo());
			String contar = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_GROUP_CONTAR.getCodigo());
			//int year = Integer.parseInt(mapa.get(TipoFileRobotProperEnum.COMUNICA_SUNAT_POSTGRE_GROUP_YEAR.getCodigo()));
			String estado = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_GROUP_ESTADO.getCodigo());
			this.comunicaSunatBIZPostGres_GROUP(urlSunat, contar, estado);
		}		
		if((mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_GROUP_LN.getCodigo()) != null) &&
				(mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_GROUP_LN.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
			log.info("COMUNICA_SUNAT_BIZPOSTGRE_GROUP_LN: " + mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_GROUP_LN.getCodigo()));
			String urlSunat = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_GROUP_LN_URL.getCodigo());
			String contar = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_GROUP_LN_CONTAR.getCodigo());
			//int year = Integer.parseInt(mapa.get(TipoFileRobotProperEnum.COMUNICA_SUNAT_POSTGRE_GROUP_LN_YEAR.getCodigo()));
			String estado = mapa.get(TipoAvatarPropertiesEnum.COMUNICA_SUNAT_POSTGRE_GROUP_LN_ESTADO.getCodigo());
			this.comunicaSunatBIZPostGres_GROUP_LN(urlSunat, contar, estado);
		}				
    }   
    
    private void comunicaSunatBIZPostGres_30(String urlSunat, String contar){
		Documento tbComprobante = new Documento();	
		tbComprobante.setEstado("= '30' ");
		tbComprobante.setRespuestaSunat(" IS NULL");	
		tbComprobante.setLongitudNombre(Integer.parseInt(contar));
		int status = 1 ; // SEND
		this.send2Sunat(urlSunat, tbComprobante, status);
    }    
    
    private void comunicaSunatBIZPostGres_30TD(String urlSunat, String contar, String documentos){
		Documento tbComprobante = new Documento();	
		tbComprobante.setEstado("= '30' ");
		tbComprobante.setRespuestaSunat(" IS NULL");	
		tbComprobante.setTipoDocumento(documentos);
		tbComprobante.setLongitudNombre(Integer.parseInt(contar));
		int status = 1 ; // SEND
		this.send2Sunat(urlSunat, tbComprobante, status);
    } 
    
    private void comunicaSunatBIZPostGres_30Fecha(String urlSunat, String contar, String fecha){
		Documento tbComprobante = new Documento();	
		tbComprobante.setEstado("= '30' ");
		tbComprobante.setRespuestaSunat(" IS NULL");	
		tbComprobante.setLongitudNombre(Integer.parseInt(contar));
		tbComprobante.setNombre(" fecha_crea >= '"+fecha+"' ");
		int status = 1 ; // SEND
		this.send2Sunat(urlSunat, tbComprobante, status);
    }     
    
    private void comunicaSunatBIZPostGres_30QA(String urlSunat, String contar){
		Documento tbComprobante = new Documento();	
		tbComprobante.setEstado("= '30' ");
		tbComprobante.setRespuestaSunat(" IS NULL");	
		tbComprobante.setLongitudNombre(Integer.parseInt(contar));
		//tbComprobante.setNombre(" fecha_crea >= ");
		int status = 1 ; // SEND
		this.send2SunatQA(urlSunat, tbComprobante, status);
    } 
    
    private void comunicaSunatBIZPostGres_30TDQA(String urlSunat, String contar, String documentos){
		Documento tbComprobante = new Documento();	
		tbComprobante.setEstado("= '30' ");
		tbComprobante.setRespuestaSunat(" IS NULL");	
		tbComprobante.setTipoDocumento(documentos);
		tbComprobante.setLongitudNombre(Integer.parseInt(contar));
		int status = 1 ; // SEND
		this.send2SunatQA(urlSunat, tbComprobante, status);
    } 
    
    private void comunicaSunatBIZPostGres_30FechaQA(String urlSunat, String contar, String fecha){
		Documento tbComprobante = new Documento();	
		tbComprobante.setEstado("= '30' ");
		tbComprobante.setRespuestaSunat(" IS NULL");	
		tbComprobante.setLongitudNombre(Integer.parseInt(contar));
		tbComprobante.setNombre(" fecha_crea >= '"+fecha+"' ");
		int status = 1 ; // SEND
		this.send2SunatQA(urlSunat, tbComprobante, status);
    }   
    
    private void comunicaSunatBIZPostGres_VARIOS(String urlSunat, String estado, String respuestaSunat, String contar){
    	Documento tbComprobante = new Documento();	
		tbComprobante.setEstado(estado);
		tbComprobante.setRespuestaSunat(respuestaSunat);	
		tbComprobante.setLongitudNombre(Integer.parseInt(contar));
		int status = 1 ; // SEND
		this.send2Sunat(urlSunat, tbComprobante, status);						
    } 
    
    private void comunicaSunatBIZPostGres_VARIOS_LN(String urlSunat, String estado, String respuestaSunat, String contar){
		Documento tbComprobante = new Documento();	
		tbComprobante.setEstado(estado);
		tbComprobante.setRespuestaSunat(respuestaSunat);	
		tbComprobante.setLongitudNombre(Integer.parseInt(contar));
		tbComprobante.setNombre(" longitud_nombre is null ");
		int status = 1 ; // SEND		
		this.send2Sunat(urlSunat, tbComprobante, status);									
    }       
    
    private void comunicaSunatBIZPostGres_ESTADO(String urlSunat, String contar, String estado){			   		
		int status = 1 ; // SEND	
		Documento tbComprobante = new Documento();
		tbComprobante.setEstado(estado);
		tbComprobante.setLongitudNombre(Integer.parseInt(contar));
		this.send2Sunat(urlSunat, tbComprobante, status);
    } 
    
    private void comunicaSunatBIZPostGres_ESTADO_LN(String urlSunat, String contar, String estado){			   		
		int status = 1 ; // SEND
		Documento tbComprobante = new Documento();
		tbComprobante.setEstado(estado+" and longitud_nombre is null ");
		tbComprobante.setLongitudNombre(Integer.parseInt(contar));
		this.send2Sunat(urlSunat, tbComprobante, status);
    }    
    
    private void comunicaSunatBIZPostGres_GROUP(String urlSunat, String contar, String estado){			   		
		int status = 1 ; // SEND	
		RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
		List<GroupBean> listGroupBean = rsetPostGresJDBC.groupRespuestaSunat(estado);
		this.forGroupBean(urlSunat, contar, listGroupBean, status);
    }       
    
    private void comunicaSunatBIZPostGres_GROUP_LN(String urlSunat, String contar, String estado){    	
		int status = 1 ; // SEND
		RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
		List<GroupBean> listGroupBean = rsetPostGresJDBC.groupRespuestaSunatLN(estado);
		this.forGroupBean(urlSunat, contar, listGroupBean, status);
    }         
    
    private void forGroupBean(String urlSunat, String contar, List<GroupBean> listGroupBean, int status) {	
		for(GroupBean gb : listGroupBean) {
			Documento tbComprobante = new Documento();
			tbComprobante.setEstado(" ='"+gb.getEstado().trim()+"' ");
			//tbComprobante.setRespuestaSunat(" ='' ");
			if(!gb.isReplay())
				tbComprobante.setNombre(" longitud_nombre is null ");
			tbComprobante.setLongitudNombre(Integer.parseInt(contar));
			if((gb.getError()!=null) && !(gb.getError().isEmpty()))
				tbComprobante.setRespuestaSunat(" ='"+gb.getError().trim()+"' ");
			//tbComprobante.setErrorLog(String.valueOf(year));
			this.send2Sunat(urlSunat, tbComprobante, status);
		}
    }

    private void send2Sunat(String urlSunat, Documento tbComprobante, int status) {
    	try {   	     		
    		int tipoList = 200;  
    		GetStatusBuild getStatusBuild = new GetStatusBuild();
			if(sThread.equals(Constantes.CONTENT_TRUE)) {
				getStatusBuild.getStatusThread(urlSunat, tbComprobante, tipoList, status, 
						Integer.parseInt(nRun), Long.parseLong(nEspera));	
			}else 
				getStatusBuild.getStatusOne(urlSunat, tbComprobante, tipoList, status);
	    } catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("send2Sunat Exception \n"+errors);
		} 
    }
    
    private void send2SunatQA(String urlSunat, Documento tbComprobante, int status) {
    	try {   	  
    		int tipoList = 200;  
    		GetStatusBuild getStatusBuild = new GetStatusBuild();
//    		if(sThread.equals(OseConstantes.CONTENT_TRUE)) {
//				getStatusBuild.getStatusThread(urlSunat, tbComprobante, tipoList, status, 
//						Integer.parseInt(nRun), Long.parseLong(nEspera));	
//			}else  
			getStatusBuild.getStatusOneQA(urlSunat, tbComprobante, tipoList, status);
	    } catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("send2Sunat Exception \n"+errors);
		} 
    }
    
//    private void updateRARCRR() {
//    	TbComprobantePGDao tbComprobantePGDao = new TbComprobantePGDaoImpl();
//    	try {
//			tbComprobantePGDao.updateRARCRR();
//		} catch (Exception e) {
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));
//			log.error("updateRARCRR Exception \n"+errors);
//		}
//    }
    
}
