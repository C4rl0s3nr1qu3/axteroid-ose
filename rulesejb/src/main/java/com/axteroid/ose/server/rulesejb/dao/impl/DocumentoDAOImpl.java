package com.axteroid.ose.server.rulesejb.dao.impl;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.repository.builder.S3CrudBuild;
import com.axteroid.ose.server.repository.rules.TTOseServerQry;
import com.axteroid.ose.server.repository.rules.impl.TTOseServerQryImpl;
import com.axteroid.ose.server.rulesejb.dao.DocumentoDAOLocal;
import com.axteroid.ose.server.rulesejb.emf.impl.DAOComImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoJBossOsePropertiesEnum;
import com.axteroid.ose.server.tools.util.DirUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Local(DocumentoDAOLocal.class)
public class DocumentoDAOImpl extends DAOComImpl<Documento> implements DocumentoDAOLocal {
	private static final Logger log = LoggerFactory.getLogger(DocumentoDAOImpl.class);
	private byte[] byteUBL = new byte[0];
	private byte[] byteCDR = new byte[0];
	private byte[] byteAR = new byte[0];
	static Map<String, String> mapa = DirUtil.getMapJBossProperties();
	
    public DocumentoDAOImpl() {}

	public void grabaDocumento(Documento documento){	
		//log.info("grabaTbComprobante: "+documento.toString());
		try {	
			log.info("Doc-1) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
					documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
			//List<TbComprobante> results = this.buscarTbComprobanteIn(documento);
			List<Documento> results = this.buscarTbComprobante4Key(documento);
			if(results != null && results.size()>0){
				//int iNumCor = Integer.parseInt(documento.getNumeroCorrelativo());	
				for(Documento doc : results){					
					log.info("Doc-2.1) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", doc.getId(),
							doc.getErrorComprobante(), doc.getEstado(),doc.getErrorNumero(),doc.getUbl().length);
					
					//int iNumCorCpe = Integer.parseInt(tb.getNumeroCorrelativo());
					//log.info("a) documento.getNumeroCorrelativo(): "+documento.getNumeroCorrelativo()+" - "+tb.getNumeroCorrelativo()+" | iNumCor: "+iNumCor+" - iNumCorCpe: "+iNumCorCpe);					
					//if(iNumCorCpe == iNumCor) {
						if(doc.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0))){
							documento.setErrorComprobante(Constantes.CONTENT_TRUE.charAt(0));
							return;
						}
						documento.setId(doc.getId());
						documento.setUserModi(documento.getUserCrea());
						documento.setFechaModi(documento.getFechaCrea());
						documento.setUserCrea(doc.getUserCrea());
						documento.setFechaCrea(doc.getFechaCrea());
						log.info("Doc-2.2) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", doc.getId(),
								doc.getErrorComprobante(), doc.getEstado(),doc.getErrorNumero(),doc.getUbl().length);
						this.lengthMaxgrabaDocumento(documento);
						log.info("Doc-2.3) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", doc.getId(),
								doc.getErrorComprobante(), doc.getEstado(),doc.getErrorNumero(),doc.getUbl().length);
						this.update(documento);
						log.info("Doc-2.4) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", doc.getId(),
								doc.getErrorComprobante(), doc.getEstado(),doc.getErrorNumero(),doc.getUbl().length);
						if(!documento.getEstado().equals(Constantes.SUNAT_CDR_GENERADO))
							return;
						this.getByteFile(documento);
						log.info("Doc-2.5) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", doc.getId(),
								doc.getErrorComprobante(), doc.getEstado(),doc.getErrorNumero(),doc.getUbl().length);
						return;
					//}
				}
			}
			log.info("Doc-3) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
					documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
			
//			documento.setCorrelativo(Long.parseLong(documento.getNumeroCorrelativo()));
			this.lengthMaxgrabaDocumento(documento);
			log.info("Doc-4) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
					documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
			this.add(documento);	
			log.info("Doc-5) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
					documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
			if(!documento.getEstado().equals(Constantes.SUNAT_CDR_GENERADO))
				return;
			this.getByteFile(documento);
			log.info("Doc-6) id: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.getId(),
					documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabaDocumento Exception \n"+errors);
		}
	}
	
	private void lengthMaxgrabaDocumento(Documento documento) {		
		if(documento.getErrorDescripcion()!=null) {
			String eObsv = documento.getErrorDescripcion();
			if(eObsv.length()>Constantes.OSE_LOGDB_LENGTH_MAX)
				eObsv = documento.getErrorDescripcion().substring(0, Constantes.OSE_LOGDB_LENGTH_MAX) ;
			documento.setErrorDescripcion(eObsv);
		}	
		if(documento.getErrorLog()!=null) {
			String eLog = documento.getErrorLog();
			if(eLog.length()>Constantes.OSE_LOGDB_LENGTH_MAX)
				eLog = documento.getErrorLog().substring(0, Constantes.OSE_LOGDB_LENGTH_MAX) ;
			documento.setErrorLog(eLog);
		}
		if(documento.getObservaDescripcion()!=null) {
			String eObsv = documento.getObservaDescripcion();
			if(eObsv.length()>Constantes.OSE_LOGDB_LENGTH_MAX)
				eObsv = documento.getObservaDescripcion().substring(0, Constantes.OSE_LOGDB_LENGTH_MAX) ;
			documento.setObservaDescripcion(eObsv);
		}	
		if(documento.getAdvertencia()!=null) {
			String eObsv = documento.getAdvertencia();
			if(eObsv.length()>Constantes.OSE_LOG_LENGTH_MAX)
				eObsv = documento.getAdvertencia().substring(0, Constantes.OSE_LOG_LENGTH_MAX) ;
			documento.setAdvertencia(eObsv);
		}
		if(documento.getErrorLogSunat()!=null) {
			String eObsv = documento.getErrorLogSunat();
			if(eObsv.length()>Constantes.OSE_LOG_LENGTH_MAX)
				eObsv = documento.getErrorLogSunat().substring(0, Constantes.OSE_LOG_LENGTH_MAX) ;
			documento.setErrorLogSunat(eObsv);
		}
		if(documento.getObservaNumero()!=null) {
			String eObsv = documento.getObservaNumero();
			if(eObsv.length()>Constantes.OSE_OBSDB_LENGTH_MAX)
				eObsv = documento.getObservaNumero().substring(0, Constantes.OSE_OBSDB_LENGTH_MAX) ;
			documento.setObservaNumero(eObsv);
		}
		if(documento.getRespuestaSunat()!=null) {
			String eObsv = documento.getRespuestaSunat();
			if(eObsv.length()>Constantes.OSE_RS_LENGTH_MAX)
				eObsv = documento.getErrorLogSunat().substring(0, Constantes.OSE_RS_LENGTH_MAX) ;
			documento.setRespuestaSunat(eObsv);
		}
		
		//log.info("documento.getEstado(): "+documento.getEstado());
		if((documento.getEstado()!=null) &&
				(documento.getEstado().equals(Constantes.SUNAT_CDR_GENERADO)))
			return;
		//log.info(" TipoFileOseProperEnum.S3_REPOSITORY_UBL.getCodigo(): "+TipoFileOseProperEnum.S3_REPOSITORY_UBL.getCodigo()+ 
		//		" mapa.get(TipoFileOseProperEnum.S3_REPOSITORY_UBL.getCodigo(): "+mapa.get(TipoFileOseProperEnum.S3_REPOSITORY_UBL.getCodigo()));
		S3CrudBuild s3CrudBuild = new S3CrudBuild();
		if((mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_UBL.getCodigo())!=null) &&
				(mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_UBL.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {										
			//log.info("2) TipoFileOseProperEnum.S3_REPOSITORY_UBL.getCodigo(): "+TipoFileOseProperEnum.S3_REPOSITORY_UBL.getCodigo());
			if((documento.getUbl()!=null)  && 
					(documento.getUbl().length>0)){						
				byteUBL = documento.getUbl();
				String mensajeSunat = s3CrudBuild.insertUBL(documento);	
				String[] mensajeArray = mensajeSunat.split(Constantes.OSE_SPLIT);					
				if((mensajeArray.length>1) && (mensajeArray[1] !=null) && 
						!(mensajeArray[1].isEmpty())) {
					byte[] mensaje = mensajeSunat.getBytes();				
					documento.setUbl(mensaje);
				}
			}
			//log.info("documento.getUbl(): "+new String(documento.getUbl()));
		}
		
		if((mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_CDR.getCodigo())!=null) &&
				(mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_CDR.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
			//log.info("TipoFileOseProperEnum.S3_REPOSITORY_CDR.getCodigo(): "+TipoFileOseProperEnum.S3_REPOSITORY_CDR.getCodigo());
			if((documento.getCdr()!=null)  && 
					(documento.getCdr().length>0)){
				byteCDR = documento.getCdr();
				String mensajeSunat = s3CrudBuild.insertCDR(documento);	
				String[] mensajeArray = mensajeSunat.split(Constantes.OSE_SPLIT);					
				if((mensajeArray.length>1) && (mensajeArray[1] !=null) && 
						!(mensajeArray[1].isEmpty())) {
					byte[] mensaje = mensajeSunat.getBytes();
					documento.setCdr(mensaje);
				}
			}
		}
		if((mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_AR.getCodigo())!=null) &&
				(mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_AR.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
			//log.info("TipoFileOseProperEnum.S3_REPOSITORY_AR.getCodigo(): "+TipoFileOseProperEnum.S3_REPOSITORY_AR.getCodigo());
			if((documento.getMensajeSunat()!=null)  && 
					(documento.getMensajeSunat().length>0)){
				byteAR = documento.getMensajeSunat();
				String mensajeSunat = s3CrudBuild.insertAcuseRecibo(documento);	
				String[] mensajeArray = mensajeSunat.split(Constantes.OSE_SPLIT);					
				if((mensajeArray.length>1) && (mensajeArray[1] !=null) && 
						!(mensajeArray[1].isEmpty())) {
					byte[] mensaje = mensajeSunat.getBytes();
					documento.setMensajeSunat(mensaje);
				}
			}		
		}
	}

	public List<Documento> buscarTbComprobante(Documento documento){
		//log.info("buscarTbComprobante "+documento.toString());
		List<Documento> results = new ArrayList<Documento>();		
		try {    		   
			if((documento.getId()!=null) && (documento.getId()>0)) {
				results = this.buscarTbComprobanteID(documento);
				if(results != null && results.size()>0)	
	            	return results; 
			}else {
				results = this.buscarTbComprobante4Key(documento);
				if(results != null && results.size()>0)	
	            	return results; 
			}
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobante Exception \n"+errors);
		}
        return results;		
	}
	
	public List<Documento> buscarTbComprobanteID(Documento documento) {
		log.info("buscarTbComprobanteID "+documento.getId());
		try {    		    		
            List<Documento> results = this.findByColumnName("id", documento.getId());
            if(results != null && results.size()>0)	
            	return this.getListS3ByteFile(results);	   
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteID Exception \n"+errors);
		}
        return null;
	}				

	public List<Documento> buscarTbComprobante4Key(Documento documento) {		
		log.info("buscarTbComprobante4Key "+documento.toString());
		try {    		    		
			String consulta = "SELECT * FROM AXTEROIDOSE.DOCUMENTO d where "
					+ " d.RUC_EMISOR = "+documento.getRucEmisor()+""
					+ " and d.TIPO_DOCUMENTO = '"+documento.getTipoDocumento()+"' "
					+ " and d.SERIE = '"+documento.getSerie()+"' "
					+ " and d.CORRELATIVO = "+documento.getCorrelativo()+" ";
			log.info("consulta: "+consulta);
	    	Map<String, Object> parameterValues = new HashMap<String, Object>();	
			List<Documento> results = this.nativeQuery(consulta, parameterValues); 
			if(results != null && results.size()>0)	
				return this.getListS3ByteFile(results);	
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobante4Key Exception \n"+errors);
		}
        return null;
	}		
	
	public List<Documento> buscarTbComprobanteXIDComprobante(Documento documento) {		
		log.info("buscarTbComprobanteXIDComprobante  "+documento.toString());
		try {    		    		
			String consulta = "SELECT d FROM Documento d where "
					+" d.rucEmisor =:rucEmisor and d.tipoDocumento =:tipoDocumento "
					+ " and d.serie =:serie and d.correlativo =:correlativo";
	    	Map<String, Object> parameterValues = new HashMap<String, Object>();
			parameterValues.put("rucEmisor", documento.getRucEmisor());    
			parameterValues.put("tipoDocumento", documento.getTipoDocumento());
			parameterValues.put("serie", documento.getSerie());
			parameterValues.put("correlativo", documento.getCorrelativo());
			List<Documento> results = this.query(consulta, parameterValues);
			if(results != null && results.size()>0)	  
				return results;
			if(!documento.getErrorNumero().equals(Constantes.SUNAT_ERROR_1033)) {
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0127);	
			}
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteXIDComprobante Exception \n"+errors);
		}
        return null;
	}	
	
	public List<Documento> buscarTbComprobanteXContentID_GET(Documento documento) {
		log.info("buscarTbComprobanteXContentID_GET "+documento.getId());
		//Documento documento = new Documento();
		//documento.setIdContent(tbContent);	
		List<Documento> results = new ArrayList<Documento>();
		List<Documento> resultsMongoDB = new ArrayList<Documento>();
		boolean bEJBFalse = false;
		boolean bEJBTrue = false;
		boolean bMongoDBFalse = false;
		boolean bMongoDBTrue = false;
		try {    		    		
			String consulta = "SELECT d FROM Documento d where d.id =:id ";
	    	Map<String, Object> parameterValues = new HashMap<String, Object>();
	    	parameterValues.put("id", documento.getId());	    	

	    	results = this.query(consulta, parameterValues);
			if(results != null && results.size()>0) {
				bEJBFalse = true;
				if((results.get(0).getCdr()!=null) && 
						(results.get(0).getCdr().length>0))
					bEJBTrue = true;					
			}
			
			if(bEJBTrue)
				return this.getListS3ByteFile(results);	
			
			//resultsMongoDB = this.listComprobantesMongoDB(documento);
			if(resultsMongoDB != null && resultsMongoDB.size()>0) {
				bMongoDBFalse = true;		
				if((resultsMongoDB.get(0).getCdr()!=null) && 
						(resultsMongoDB.get(0).getCdr().length>0))
					bMongoDBTrue = true;
			}
			if(bMongoDBTrue)
				return this.getListS3ByteFile(resultsMongoDB);	
			if(bEJBFalse)
				return this.getListS3ByteFile(results);
			if(bMongoDBFalse)
				return this.getListS3ByteFile(resultsMongoDB);	
			
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0127);
    	} catch (Exception e) {
//    		try {
//    			resultsJDBC = listComprobantesJDBC(documento);
//    			if(resultsJDBC != null && resultsJDBC.size()>0) 
//    				return this.getListS3ByteFile(resultsJDBC);	
//    			if(bEJBFalse)
//    				return this.getListS3ByteFile(results); 
//    		} catch (Exception ex) {
//    			if(bEJBFalse)
//    				return this.getListS3ByteFile(results);    			
//    			tbContent.setErrorContent(OseConstantes.CONTENT_FALSE.charAt(0));
//        		tbContent.setErrorNumero(OseConstantes.SUNAT_ERROR_0127);
//        		tbContent.setErrorLog(e.toString());
//    			StringWriter errors = new StringWriter();				
//    			e.printStackTrace(new PrintWriter(errors));
//    			log.error("buscarTbComprobanteXContentID_GET Exception \n"+errors);
//    		}
    		documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0127);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteXContentID_GET Exception \n"+errors);
		}
        return null;
	}	
		
	public List<Documento> listComprobantesMongoDB(Documento documento) {
		TTOseServerQry ttOseServerQry = new TTOseServerQryImpl();
		List<Documento> results = ttOseServerQry.findTTOseServer(documento);
		if(results != null && results.size()>0)		
			return this.getListS3ByteFile(results); 
		return null;
	}
	
	public List<Documento> buscarTbComprobanteXIDComprobante_GET(Documento documento) {		
		log.info("buscarTbComprobanteXIDComprobante_GET "+documento.toString());
		List<Documento> results = new ArrayList<Documento>();
		List<Documento> resultsJDBC = new ArrayList<Documento>();
		boolean bEJBFalse = false;
		boolean bEJBTrue = false;
		boolean bJDBCFalse = false;
		boolean bJDBCTrue = false;
		try {    		    		
			String consulta = "SELECT d FROM Documento d where "
					+" d.rucEmisor =:rucEmisor and d.tipoDocumento =:tipoDocumento "
					+ " and d.serie =:serie and d.correlativo =:correlativo";
			//String sComprobante = String.valueOf(documento.getRucEmisor())+documento.getTipoComprobante()
			//	+documento.getSerie()+documento.getNumeroCorrelativo();
			//log.info("buscarTbComprobanteXIDComprobante "+sComprobante);
	    	Map<String, Object> parameterValues = new HashMap<String, Object>();
			parameterValues.put("rucEmisor", documento.getRucEmisor());    
			parameterValues.put("tipoDocumento", documento.getTipoDocumento());
			parameterValues.put("serie", documento.getSerie());
			parameterValues.put("correlativo", documento.getCorrelativo());	
			results = this.query(consulta, parameterValues);
			if(results != null && results.size()>0) {	  
				bEJBFalse = true;
				if((results.get(0).getCdr()!=null) && 
						(results.get(0).getCdr().length>0))
					bEJBTrue = true;
			}
			if(bEJBTrue)
				return this.getListS3ByteFile(results);				
			//resultsJDBC = listComprobantesJDBC(documento);
			if(resultsJDBC != null && resultsJDBC.size()>0) {
				bJDBCFalse = true;		
				if((resultsJDBC.get(0).getCdr()!=null) && 
						(resultsJDBC.get(0).getCdr().length>0))
					bJDBCTrue = true;
			}
			if(bJDBCTrue)
				return this.getListS3ByteFile(resultsJDBC);	
			if(bEJBFalse)
				return this.getListS3ByteFile(results);
			if(bJDBCFalse)
				return this.getListS3ByteFile(resultsJDBC);	
			
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0127);	
    	} catch (Exception e) { 
    		documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0127);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteXIDComprobante_GET Exception \n"+errors);
		}		
        return null;
	}
			
	public List<Documento> buscarTbComprobanteXIDComprobanteShort(Documento documento) {		
		log.info("buscarTbComprobanteXIDComprobanteShort "+documento.toString());
		try {    		    		
			String sComprobante = String.valueOf(documento.getRucEmisor())+documento.getTipoDocumento()
				+documento.getSerie()+documento.getNumeroCorrelativo();
			//log.info("buscarTbComprobanteXIDComprobante "+sComprobante);
			String consulta = "SELECT d.ID_COMPROBANTE, d.CDR, d.ERROR_COMPROBANTE, d.ERROR_NUMERO, d.USER_CREA "+
					" FROM AXTEROIDOSE.DOCUMENTO d where d.ID_COMPROBANTE = "+sComprobante;
	    	Map<String, Object> parameterValues = new HashMap<String, Object>();
			//parameterValues.put("idComprobante", sComprobante);    	
			List<Documento> results = this.nativeQuery(consulta, parameterValues);
			if(results != null && results.size()>0)	  
				return this.getListS3ByteFile(results);	
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0127);	
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteXIDComprobanteShort Exception \n"+errors);
		}
        return null;
	}	
		
	public List<Documento> buscarTbComprobanteXID(Documento documento) {		
		log.info("buscarTbComprobanteXID "+documento.getId());
		try {    		    		
			String consulta = "SELECT d FROM Documento d where d.id =:id ";
	    	Map<String, Object> parameterValues = new HashMap<String, Object>();
			parameterValues.put("id", documento.getId());    	
			List<Documento> results = this.query(consulta, parameterValues);
			if(results != null && results.size()>0)	  
				return this.getListS3ByteFile(results);	
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0127);	
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteXIDComprobante Exception \n"+errors);
		}
        return null;
	}
	
	public List<Documento> buscarTbComprobanteALL(Documento documento) {		
		log.info("buscarTbComprobanteXID "+documento.getId());
		try {    		    		
			String consulta = "SELECT d FROM Documento d where d.id <=:id ";
	    	Map<String, Object> parameterValues = new HashMap<String, Object>();
			parameterValues.put("id", documento.getId());    	
			List<Documento> results = this.query(consulta, parameterValues);
			if(results != null && results.size()>0)	  
				return this.getListS3ByteFile(results);	
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0127);	
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteXIDComprobante Exception \n"+errors);
		}
        return null;
	}
	
	public List<Documento> buscarTbComprobanteXVariables(Documento documento) {		
		log.info("buscarTbComprobanteXVariables "+documento.toString());
		try {    		    		
			String consulta = "SELECT d FROM Documento d where d.id =:id ";
	    	Map<String, Object> parameterValues = new HashMap<String, Object>();
			parameterValues.put("id", documento.getId());    	
			List<Documento> results = this.query(consulta, parameterValues);
			if(results != null && results.size()>0)					
				return this.getListS3ByteFile(results);		
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0127);	
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteXIDComprobante Exception \n"+errors);
		}
        return null;
	}		
	
	public void updateTbComprobante(Documento documento){	
		//log.info("updateTbComprobante "+documento.toString());
		try {
//			log.info(" (4AA) EC: "+documento.getErrorComprobante()+" EN: "+documento.getErrorNumero()
//			+" Est: "+documento.getEstado()+" UBL: "+documento.getUbl().length);
			this.lengthMaxgrabaDocumento(documento);
//			log.info(" (4BB) EC: "+documento.getErrorComprobante()+" EN: "+documento.getErrorNumero()
//			+" Est: "+documento.getEstado()+" UBL: "+documento.getUbl().length);
			this.update(documento);
//			log.info(" (4CC) EC: "+documento.getErrorComprobante()+" EN: "+documento.getErrorNumero()
//			+" Est: "+documento.getEstado()+" UBL: "+documento.getUbl().length);
			if(!(documento.getEstado().equals(Constantes.SUNAT_CDR_EN_PROCESO)) || 
					!(documento.getEstado().equals(Constantes.SUNAT_CDR_ERROR_PROCESO)))
				return;
			this.getByteFile(documento);
//			log.info(" (4DD) EC: "+documento.getErrorComprobante()+" EN: "+documento.getErrorNumero()
//			+" Est: "+documento.getEstado()+" UBL: "+documento.getUbl().length);

    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("updateTbComprobante Exception \n"+errors);
		}
	}	
	
	public void updateTbComprobanteErrorComprobante(Documento documento){	
		//log.info("updateTbComprobanteErrorComprobante "+documento.toString());
		try {
			int iNumCor = Integer.parseInt(documento.getNumeroCorrelativo());
			List<Documento> results = this.buscarTbComprobante4Key(documento);
			if(results != null && results.size()>0) {
				for(Documento tb : results){
					int iNumCorCpe = Integer.parseInt(tb.getNumeroCorrelativo());
					//log.info("(1) documento.getNumeroCorrelativo(): "+documento.getNumeroCorrelativo()+" - "+tb.getNumeroCorrelativo()+" | iNumCor: "+iNumCor+" - iNumCorCpe: "+iNumCorCpe);
					if(iNumCorCpe == iNumCor) {
						//log.info("(2) documento.getNumeroCorrelativo(): "+documento.getNumeroCorrelativo()+" - "+tb.getNumeroCorrelativo()+" | iNumCor: "+iNumCor+" - iNumCorCpe: "+iNumCorCpe);						
						tb.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));
						tb.setEstado(Constantes.SUNAT_CDR_EN_PROCESO);
						tb.setUserModi(documento.getUserCrea());
						tb.setFechaModi(new Date());
						this.lengthMaxgrabaDocumento(documento);
						this.update(tb);
						this.getByteFile(documento);
						return;
					}
				}
			}
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("updateTbComprobanteErrorComprobante Exception \n"+errors);
		}
	}	
	
	public void updateTbComprobanteCDR(Documento documento){	
		//log.info("updateTbComprobanteCDR: {}",documento.toString());
		try {			
			//log.info("documento.getRespuestaSunat(): "+documento.getRespuestaSunat());
			//String tcms = new String(documento.getMensajeSunat());
			//log.info("documento.getMensajeSunat(): "+tcms);
			//log.info("documento.getErrorLogSunat(): "+documento.getErrorLogSunat());
			List<Documento> results = this.buscarTbComprobante(documento);	
			if(results != null && results.size()>0) {	  
				for(Documento tb : results){
					if(!(tb.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0))))
						return;
					tb.setEstado(documento.getEstado());
					tb.setRespuestaSunat(documento.getRespuestaSunat());
					tb.setMensajeSunat(documento.getMensajeSunat());
					tb.setErrorLogSunat(documento.getErrorLogSunat());
					tb.setAdvertencia(documento.getAdvertencia());
					tb.setUserModi(documento.getUserModi());
					tb.setFechaModi(documento.getFechaModi());
					this.lengthMaxgrabaDocumento(tb);
					this.update(tb);
					this.getByteFile(documento);
					//log.info("tb.getId() "+tb.getId()+" - "+tb.getIdComprobante());
					//log.info("tb.getRespuestaSunat(): "+tb.getRespuestaSunat());
					//String ms = new String(tb.getMensajeSunat());
					//log.info("tb.getMensajeSunat(): "+ms);
					//log.info("tb.getErrorLogSunat(): "+tb.getErrorLogSunat());
					return;
				}
			}
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabaTbComprobante Exception \n"+errors);
		}
	}
	
	public void updateTbComprobanteCDRID(Documento documento){	
		//log.info("updateTbComprobanteCDRID "+documento.getId()+" - "+documento.getIdComprobante());
		try {			
			//log.info("documento.getRespuestaSunat(): "+documento.getRespuestaSunat()!=null ? documento.getRespuestaSunat() : "");
			//String tcms = documento.getMensajeSunat()!=null ? new String(documento.getMensajeSunat()) : "";
			//log.info("documento.getMensajeSunat(): "+tcms);
			//log.info("documento.getErrorLogSunat(): "+documento.getErrorLogSunat()!=null ? documento.getErrorLogSunat() : "");
			List<Documento> results = this.buscarTbComprobanteID(documento);						
			if(results != null && results.size()>0) {
				for(Documento tb : results){
					if(!(tb.getErrorComprobante().equals(Constantes.CONTENT_TRUE.charAt(0))))
						return;
					tb.setEstado(documento.getEstado());
					tb.setRespuestaSunat(documento.getRespuestaSunat());
					tb.setMensajeSunat(documento.getMensajeSunat());
					tb.setErrorLogSunat(documento.getErrorLogSunat());
					tb.setAdvertencia(documento.getAdvertencia());
					tb.setUserModi(documento.getUserModi());
					tb.setFechaModi(documento.getFechaModi());
					this.lengthMaxgrabaDocumento(tb);
					this.update(tb);
					this.getByteFile(documento);
					//log.info("tb.getId() "+tb.getId()+" - "+tb.getIdComprobante());
					//log.info("tb.getRespuestaSunat(): "+documento.getRespuestaSunat()!=null ? documento.getRespuestaSunat() : "");
					//String ms = documento.getMensajeSunat()!=null ? new String(documento.getMensajeSunat()) : "";
					//log.info("tb.getMensajeSunat(): "+ms);
					//log.info("tb.getErrorLogSunat(): "+tb.getErrorLogSunat());
					return;
				}
			}
		}catch(Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabaTbComprobante Exception \n"+errors);
		}
	}	
	
	private List<Documento> getListS3ByteFile(List<Documento> results) {
		List<Documento> results2 = new ArrayList<Documento>();
		for(Documento tbc : results) {
			this.getS3ByteFile(tbc);
			results2.add(tbc);
		}
		return results2;
	}
	
	private void getS3ByteFile(Documento documento) {
		try {
			log.info("getS3ByteFile documento.getEstado(): "+documento.getEstado());			
			S3CrudBuild s3CrudBuild = new S3CrudBuild();
			if((mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_UBL.getCodigo())!=null) &&
					(mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_UBL.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
				if((documento.getUbl() != null) &&
						(documento.getUbl().length > 0)){
					String sByteFile = new String(documento.getUbl());
					if(sByteFile.length()<=100) {				 
						 String[] val = sByteFile.split(Constantes.OSE_SPLIT);	
						 byte[] contentFile = s3CrudBuild.getS3ByteFile(val[0]);
						 if(contentFile.length > 0)
							 documento.setUbl(contentFile);
					}
				}
			}
			if((mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_CDR.getCodigo())!=null) &&
					(mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_CDR.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {		
				if((documento.getCdr()!= null) &&
						(documento.getCdr().length > 0)){
					String sByteFile = new String(documento.getCdr());
					if(sByteFile.length()<=100) {				 
						 String[] val = sByteFile.split(Constantes.OSE_SPLIT);	
						 byte[] contentFile = s3CrudBuild.getS3ByteFile(val[0]);
						 if(contentFile.length > 0)
							 documento.setCdr(contentFile);
					}
				}
			}
			if((mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_AR.getCodigo())!=null) &&
					(mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_AR.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {
				if((documento.getMensajeSunat()!= null) &&
						(documento.getMensajeSunat().length > 0)){
					String sByteFile = new String(documento.getMensajeSunat());
					if(sByteFile.length()<=100) {				 
						 String[] val = sByteFile.split(Constantes.OSE_SPLIT);	
						 byte[] contentFile = s3CrudBuild.getS3ByteFile(val[0]);
						 if(contentFile.length > 0)
							 documento.setMensajeSunat(contentFile);
					}
				}
			}
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getS3ByteFile Exception \n"+errors);
		}
	}
	
	private void getByteFile(Documento documento) {
		try {
			//log.info("getByteFile documento.getEstado(): "+documento.getEstado());
			if(documento.getEstado().equals(Constantes.SUNAT_CDR_GENERADO))
				return;
			if((mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_UBL.getCodigo())!=null) &&
					(mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_UBL.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {		 		 
				documento.setUbl(byteUBL);
			}
			if((mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_CDR.getCodigo())!=null) &&
					(mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_CDR.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {		 		 
				documento.setCdr(byteCDR);
			}
			if((mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_AR.getCodigo())!=null) &&
					(mapa.get(TipoJBossOsePropertiesEnum.S3_REPOSITORY_AR.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {		 		 
				documento.setMensajeSunat(byteAR);
			}
			
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getByteFile Exception \n"+errors);
		}
	}
	

}
