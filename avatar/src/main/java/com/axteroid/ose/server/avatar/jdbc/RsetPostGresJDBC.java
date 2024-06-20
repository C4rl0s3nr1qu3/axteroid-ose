package com.axteroid.ose.server.avatar.jdbc;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.bean.GroupBean;
import com.axteroid.ose.server.avatar.model.TmCeDocumento;
import com.axteroid.ose.server.avatar.model.TmCeDocumentoPK;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.DocumentUtil;
import com.axteroid.ose.server.tools.util.FileUtil;
import com.axteroid.ose.server.tools.util.ZipUtil;

public class RsetPostGresJDBC extends DaoPostGresJDBC{
	private static final Logger log = LoggerFactory.getLogger(RsetPostGresJDBC.class); 
	private CallableStatement cstmt;
	private ResultSet rset = null;
	private PreparedStatement pstmt;
	private Statement stmt;
	private int tipoList = 0;    
	public RsetPostGresJDBC() {
    	super();
    }
	
	public List<TmCeDocumento>  getData(Documento tbComprobante, int tipoList){
		log.info("Execute Documento getData ... ");
		this.tipoList = tipoList;
		List<TmCeDocumento> list = new ArrayList<TmCeDocumento>();
		try {
			List<Documento> listTbComprobante = new ArrayList<Documento>();
		    switch(tipoList){	
				case 100 :
					listTbComprobante = this.listComprobantes(tbComprobante);
					break;	
				case 110 :
					//listTbComprobante = this.listComprobantesOld(tbComprobante);
					break;	
				case 200 :
					listTbComprobante = this.listComprobantes(tbComprobante);
					break;						
		    }
			int i = 0;		
			for(Documento tb : listTbComprobante) {		
				try{
					TmCeDocumento tmCeDocumento = new TmCeDocumento();
					TmCeDocumentoPK tmCeDocumentoPK = new TmCeDocumentoPK();
					tmCeDocumentoPK.setId(tb.getId());
					tmCeDocumentoPK.setIdemisor(String.valueOf(tb.getRucEmisor()));
					tmCeDocumentoPK.setIdorigen(tb.getDireccion());
					tmCeDocumento.setId(tmCeDocumentoPK);
					tmCeDocumento.setIdticket(tb.getId());
					String nameFile = tb.getNombre().substring(0, tb.getNombre().length()-4);					        
			        String nameZip =(new StringBuilder(nameFile)).append(".").append("zip").toString();
			        tmCeDocumento.setNombrearchivo(nameZip);
			        if(tipoList != 200){
			        	List<File> listFile = new ArrayList<File>();
			        	File fileNombreUBL = new File(tb.getNombre());
			        	File fileUBL = FileUtil.fromBytes(fileNombreUBL, tb.getUbl());					        
			        	listFile.add(fileUBL);
			        	byte[] bytesZip = ZipUtil.zipFiles2Byte(listFile);
			        	tmCeDocumento.setArchivoenviado(bytesZip);
			        	if (fileUBL.exists())
			        		fileUBL.delete();
			        }					
					log.info(i +" "+tmCeDocumento.getNombrearchivo());
					list.add(tmCeDocumento);
				}catch (Exception e) {
					StringWriter errors = new StringWriter();				
					e.printStackTrace(new PrintWriter(errors));
					log.info("getData Exception \n"+errors);
				}				
				i++;
			}		
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("getData Exception \n"+errors);
		}
		return list;
	}	

	public List<Documento> listComprobantes(Documento tbComprobante) {
		log.info("listComprobantes ");
		List<Documento> listTbComprobante = new ArrayList<Documento>();
		boolean b = true;
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String sql = "";
			sql = "SELECT ID, ID_COMPROBANTE, RUC_EMISOR, "  
					+" TIPO_DOCUMENTO, SERIE, NUMERO_CORRELATIVO, NOMBRE, "  
//					+" LONGITUD_NOMBRE, DIRECCION, UBL, UBL_VERSION, "  
//					+" CUSTOMIZA_VERSION, CDR, ESTADO, ERROR_COMPROBANTE, "  
//					+" ERROR_NUMERO, ERROR_DESCRIPCION, ERROR_LOG, OBSERVA_NUMERO, "  
//					+" OBSERVA_DESCRIPCION, ADVERTENCIA, FECHA_ENVIO_SUNAT, RESPUESTA_SUNAT, "  
//					+" MENSAJE_SUNAT, FECHA_RESPUESTA_SUNAT, ERROR_LOG_SUNAT, FEC_INI_PROC, FEC_FIN_PROC, USER_CREA "  
					+" UBL_VERSION, ESTADO, ERROR_COMPROBANTE, user_crea, fecha_crea, "
					+" ERROR_LOG, ERROR_LOG_SUNAT, MENSAJE_SUNAT, RESPUESTA_SUNAT"			
					+" FROM AXTEROIDOSE.DOCUMENTO WHERE ";
			if(tipoList != 200){
				sql = "SELECT ID, ID_COMPROBANTE, RUC_EMISOR, "  
						+" TIPO_DOCUMENTO, SERIE, NUMERO_CORRELATIVO, NOMBRE,"  
						+" UBL_VERSION, ESTADO, ERROR_COMPROBANTE, user_crea, fecha_crea, "
						+" ERROR_LOG, ERROR_LOG_SUNAT, MENSAJE_SUNAT, RESPUESTA_SUNAT, UBL"							
						+" FROM AXTEROIDOSE.DOCUMENTO WHERE ";				
			}						
			StringBuilder ruta = new StringBuilder(sql);			
			if((tbComprobante.getDireccion()!= null) && 
					(!tbComprobante.getDireccion().isEmpty())) {
				if(b) {
					ruta.append(" ID "+tbComprobante.getDireccion()+" ");	
					b = false;
				}					
			}	
			if((tbComprobante.getObservaDescripcion()!= null) && 
					(!tbComprobante.getObservaDescripcion().isEmpty())){				
				if(b) {
					ruta.append(" RUC_EMISOR "+tbComprobante.getObservaDescripcion()+" ");
					b = false;
				}else
					ruta.append(" and RUC_EMISOR "+tbComprobante.getObservaDescripcion()+" ");
			}			
			if((tbComprobante.getTipoDocumento()!= null) && 
					(!tbComprobante.getTipoDocumento().isEmpty())) {
				if(b) {
					ruta.append(" TIPO_DOCUMENTO "+tbComprobante.getTipoDocumento()+" ");
					b = false;
				}else
					ruta.append(" and TIPO_DOCUMENTO "+tbComprobante.getTipoDocumento()+" ");
			}
			if((tbComprobante.getEstado()!= null) && 
					(!tbComprobante.getEstado().isEmpty())) {
				if(b) {
					ruta.append(" ESTADO "+tbComprobante.getEstado()+" ");
					b = false;
				}else
					ruta.append(" and ESTADO "+tbComprobante.getEstado()+" ");
			}			
			if((tbComprobante.getErrorComprobante()!= null)){
				if(b) {
					ruta.append(" ERROR_COMPROBANTE = '"+tbComprobante.getErrorComprobante()+"' ");
					b = false;
				}else
					ruta.append(" and ERROR_COMPROBANTE = '"+tbComprobante.getErrorComprobante()+"' ");
			}
			if((tbComprobante.getErrorNumero()!= null) && 
					(!tbComprobante.getErrorNumero().isEmpty())){
				if(b) {
					ruta.append(" ERROR_NUMERO "+tbComprobante.getErrorNumero()+" ");
					b = false;
				}else
					ruta.append(" and ERROR_NUMERO "+tbComprobante.getErrorNumero()+" ");
			}
			if((tbComprobante.getRespuestaSunat()!= null) && 
					(!tbComprobante.getRespuestaSunat().isEmpty())){
				if(b) {
					ruta.append(" RESPUESTA_SUNAT "+tbComprobante.getRespuestaSunat()+" ");
					b = false;
				}else
					ruta.append(" and RESPUESTA_SUNAT "+tbComprobante.getRespuestaSunat()+" ");
			}
			if((tbComprobante.getErrorLog()!= null) && 
					(!tbComprobante.getErrorLog().isEmpty())){
				if(b) {
					ruta.append(" "+tbComprobante.getErrorLog()+" ");
					b = false;
				}else
					ruta.append(" and "+tbComprobante.getErrorLog()+" ");
			}			
			if((tbComprobante.getNombre()!= null) && 
					(!tbComprobante.getNombre().isEmpty())) {
				if(b) {
					ruta.append(" "+tbComprobante.getNombre()+" ");
					b = false;
				}else
					ruta.append(" and "+tbComprobante.getNombre()+" ");
			}		
			if((tbComprobante.getUserCrea()!= null) && (!tbComprobante.getUserCrea().isEmpty())){
				if(tbComprobante.getUserCrea().equals("0")) {
					if((tbComprobante.getUblVersion()!= null) && 
							(!tbComprobante.getUblVersion().isEmpty())) {
						if(b) {
							ruta.append(" extract(year from FECHA_CREA::timestamp) "+ tbComprobante.getUblVersion() +" ");
							b = false;
						}else
							ruta.append(" and extract(year from FECHA_CREA::timestamp) "+ tbComprobante.getUblVersion() +" ");
					}
					if((tbComprobante.getCustomizaVersion()!= null) && 
							(!tbComprobante.getCustomizaVersion().isEmpty())) {
						if(b) {
							ruta.append(" extract(month from FECHA_CREA::timestamp) "+ tbComprobante.getCustomizaVersion() +" ");
							b = false;
						}else
							ruta.append(" and extract(month from FECHA_CREA::timestamp) "+ tbComprobante.getCustomizaVersion() +" ");
					}
					if((tbComprobante.getErrorLogSunat()!= null) && 
							(!tbComprobante.getErrorLogSunat().isEmpty())) {
						if(b) {
							ruta.append(" extract(day from FECHA_CREA::timestamp) "+ tbComprobante.getErrorLogSunat() +" ");
							b = false;
						}else
							ruta.append(" and extract(day from FECHA_CREA::timestamp) "+ tbComprobante.getErrorLogSunat() +" ");
					}	
				}
				if(tbComprobante.getUserCrea().equals("1")) {	
					if((tbComprobante.getUblVersion()!= null) && 
							(!tbComprobante.getUblVersion().isEmpty())) {
						if(b) {
							ruta.append(" extract(year from error_log::timestamp) "+ tbComprobante.getUblVersion() +" ");
							b = false;
						}else
							ruta.append(" and extract(year from error_log::timestamp) "+ tbComprobante.getUblVersion() +" ");
					}
					if((tbComprobante.getCustomizaVersion()!= null) && 
							(!tbComprobante.getCustomizaVersion().isEmpty())) {
						if(b) {
							ruta.append(" extract(month from error_log::timestamp) "+ tbComprobante.getCustomizaVersion() +" ");
							b = false;
						}else
							ruta.append(" and extract(month from error_log::timestamp) "+ tbComprobante.getCustomizaVersion() +" ");
					}
					if((tbComprobante.getErrorLogSunat()!= null) && 
							(!tbComprobante.getErrorLogSunat().isEmpty())) {
						if(b) {
							ruta.append(" extract(day from error_log::timestamp) "+ tbComprobante.getErrorLogSunat() +" ");
							b = false;
						}else
							ruta.append(" and extract(day from error_log::timestamp) "+ tbComprobante.getErrorLogSunat() +" ");
					}						
				}
				if(tbComprobante.getUserCrea().equals("2")) {
					Date fecha3D = DateUtil.substractDaysToDate(new Date(),3);
					String sFecha3D = DateUtil.dateFormat(fecha3D, "yyyy-MM-dd");
					if(b) {
						ruta.append(" FECHA_CREA>='"+sFecha3D+" 00:00:00' ");
						b = false;
					}else
						ruta.append(" and FECHA_CREA>='"+sFecha3D+" 00:00:00' ");					
				}
			}
					
			if((tbComprobante.getAdvertencia()!= null) && 
					(!tbComprobante.getAdvertencia().isEmpty())) {
				if(b) {
					ruta.append(" extract(hours from FECHA_CREA::timestamp) "+ tbComprobante.getAdvertencia() +" ");
					b = false;
				}else
					ruta.append(" extract(hours from FECHA_CREA::timestamp) "+ tbComprobante.getAdvertencia() +" ");
			}	
			if((tbComprobante.getUserModi()!= null) && 
					(!tbComprobante.getUserModi().isEmpty())) {
				if(b) {
					ruta.append(" extract(MINUTE from FECHA_CREA::timestamp) "+ tbComprobante.getUserModi() +" ");
					b = false;
				}else
					ruta.append(" extract(MINUTE from FECHA_CREA::timestamp) "+ tbComprobante.getUserModi() +" ");
			}			
			if((tbComprobante.getLongitudNombre()!= null) && 
					(tbComprobante.getLongitudNombre()>0))	
				ruta.append(" ORDER BY ID FETCH FIRST "+tbComprobante.getLongitudNombre()+" ROW ONLY ");								
			log.info("ruta --> "+ruta.toString()); 
			pstmt = conn.prepareStatement(ruta.toString());
			rset = pstmt.executeQuery();
			while (rset.next()) {
				Documento tb = new Documento();
				this.getTbComprobante(tb);
				listTbComprobante.add(tb);
			}			
			rset.close();
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("listComprobantes Exception \n"+errors);
		} finally {
			cerrarDao();
		}
		return listTbComprobante;
	}	
	

//	public List<Documento> listComprobantesOld(Documento tbComprobante) {
//		log.info("listComprobantes ");
//		List<Documento> listTbComprobante = new ArrayList<Documento>();
//		boolean b = true;
//		try {			
//			conn = DaoPostGresJDBC.getConnectionOSE();
//			String sql = "SELECT ID, ID_COMPROBANTE, RUC_EMISOR, " + 
//					" TIPO_DOCUMENTO, SERIE, NUMERO_CORRELATIVO, NOMBRE, " + 
////					" LONGITUD_NOMBRE, DIRECCION, UBL, UBL_VERSION, " + 
////					" CUSTOMIZA_VERSION, CDR, ESTADO, ERROR_COMPROBANTE, " + 
////					" ERROR_NUMERO, ERROR_DESCRIPCION, ERROR_LOG, OBSERVA_NUMERO, " + 
////					" OBSERVA_DESCRIPCION, ADVERTENCIA, FECHA_ENVIO_SUNAT, RESPUESTA_SUNAT, " + 
////					" MENSAJE_SUNAT, FECHA_RESPUESTA_SUNAT, ERROR_LOG_SUNAT, FEC_INI_PROC, FEC_FIN_PROC, USER_CREA " + 
//					" UBL, UBL_VERSION, ESTADO, ERROR_COMPROBANTE, "+				
//					" user_crea, fecha_crea "+			
//					" FROM AXTEROIDOSE.DOCUMENTO_OLD WHERE ";
//			StringBuilder ruta = new StringBuilder(sql);			
//			if((tbComprobante.getDireccion()!= null) && 
//					(!tbComprobante.getDireccion().isEmpty())) {
//				if(b) {
//					ruta.append(" ID "+tbComprobante.getDireccion()+" ");	
//					b = false;
//				}					
//			}
//			if((tbComprobante.getTipoDocumento()!= null) && 
//					(!tbComprobante.getTipoDocumento().isEmpty())) {
//				if(b) {
//					ruta.append(" TIPO_COMPROBANTE "+tbComprobante.getTipoDocumento()+" ");
//					b = false;
//				}else
//					ruta.append(" and TIPO_COMPROBANTE "+tbComprobante.getTipoDocumento()+" ");
//			}
//			if((tbComprobante.getEstado()!= null) && 
//					(!tbComprobante.getEstado().isEmpty())) {
//				if(b) {
//					ruta.append(" ESTADO "+tbComprobante.getEstado()+" ");
//					b = false;
//				}else
//					ruta.append(" and ESTADO "+tbComprobante.getEstado()+" ");
//			}			
//			if((tbComprobante.getErrorComprobante()!= null)){
//				if(b) {
//					ruta.append(" ERROR_COMPROBANTE = '"+tbComprobante.getErrorComprobante()+"' ");
//					b = false;
//				}else
//					ruta.append(" and ERROR_COMPROBANTE = '"+tbComprobante.getErrorComprobante()+"' ");
//			}
//			if((tbComprobante.getErrorNumero()!= null) && 
//					(!tbComprobante.getErrorNumero().isEmpty())){
//				if(b) {
//					ruta.append(" ERROR_NUMERO "+tbComprobante.getErrorNumero()+" ");
//					b = false;
//				}else
//					ruta.append(" and ERROR_NUMERO "+tbComprobante.getErrorNumero()+" ");
//			}
//			if((tbComprobante.getRespuestaSunat()!= null) && 
//					(!tbComprobante.getRespuestaSunat().isEmpty())){
//				if(b) {
//					ruta.append(" RESPUESTA_SUNAT "+tbComprobante.getRespuestaSunat()+" ");
//					b = false;
//				}else
//					ruta.append(" and RESPUESTA_SUNAT "+tbComprobante.getRespuestaSunat()+" ");
//			}
//			if((tbComprobante.getObservaDescripcion()!= null) && 
//					(!tbComprobante.getObservaDescripcion().isEmpty())){
//				if(b) {
//					ruta.append(" RUC_EMISOR "+tbComprobante.getObservaDescripcion()+" ");
//					b = false;
//				}else
//					ruta.append(" and RUC_EMISOR "+tbComprobante.getObservaDescripcion()+" ");
//			}
//			if((tbComprobante.getErrorLog()!= null) && 
//					(!tbComprobante.getErrorLog().isEmpty())){
//				if(b) {
//					ruta.append(" "+tbComprobante.getErrorLog()+" ");
//					b = false;
//				}else
//					ruta.append(" and "+tbComprobante.getErrorLog()+" ");
//			}			
//			if((tbComprobante.getUserModi()!= null) && (!tbComprobante.getUserModi().isEmpty())){
//				if(tbComprobante.getUserModi().equals("0")) {
//					if((tbComprobante.getUblVersion()!= null) && 
//							(!tbComprobante.getUblVersion().isEmpty())) {
//						if(b) {
//							ruta.append(" extract(year from FECHA_CREA::timestamp) "+ tbComprobante.getUblVersion() +" ");
//							b = false;
//						}else
//							ruta.append(" and extract(year from FECHA_CREA::timestamp) "+ tbComprobante.getUblVersion() +" ");
//					}
//					if((tbComprobante.getCustomizaVersion()!= null) && 
//							(!tbComprobante.getCustomizaVersion().isEmpty())) {
//						if(b) {
//							ruta.append(" extract(month from FECHA_CREA::timestamp) "+ tbComprobante.getCustomizaVersion() +" ");
//							b = false;
//						}else
//							ruta.append(" and extract(month from FECHA_CREA::timestamp) "+ tbComprobante.getCustomizaVersion() +" ");
//					}
//					if((tbComprobante.getErrorLogSunat()!= null) && 
//							(!tbComprobante.getErrorLogSunat().isEmpty())) {
//						if(b) {
//							ruta.append(" extract(day from FECHA_CREA::timestamp) "+ tbComprobante.getErrorLogSunat() +" ");
//							b = false;
//						}else
//							ruta.append(" and extract(day from FECHA_CREA::timestamp) "+ tbComprobante.getErrorLogSunat() +" ");
//					}	
//					
//					
//				}
//				if(tbComprobante.getUserModi().equals("1")) {	
//					if((tbComprobante.getUblVersion()!= null) && 
//							(!tbComprobante.getUblVersion().isEmpty())) {
//						if(b) {
//							ruta.append(" extract(year from error_log::timestamp) "+ tbComprobante.getUblVersion() +" ");
//							b = false;
//						}else
//							ruta.append(" and extract(year from error_log::timestamp) "+ tbComprobante.getUblVersion() +" ");
//					}
//					if((tbComprobante.getCustomizaVersion()!= null) && 
//							(!tbComprobante.getCustomizaVersion().isEmpty())) {
//						if(b) {
//							ruta.append(" extract(month from error_log::timestamp) "+ tbComprobante.getCustomizaVersion() +" ");
//							b = false;
//						}else
//							ruta.append(" and extract(month from error_log::timestamp) "+ tbComprobante.getCustomizaVersion() +" ");
//					}
//					if((tbComprobante.getErrorLogSunat()!= null) && 
//							(!tbComprobante.getErrorLogSunat().isEmpty())) {
//						if(b) {
//							ruta.append(" extract(day from error_log::timestamp) "+ tbComprobante.getErrorLogSunat() +" ");
//							b = false;
//						}else
//							ruta.append(" and extract(day from error_log::timestamp) "+ tbComprobante.getErrorLogSunat() +" ");
//					}						
//				}
//			}		
//			
//			if((tbComprobante.getLongitudNombre()!= null) && 
//					(tbComprobante.getLongitudNombre()>0))	
//				ruta.append(" ORDER BY ID FETCH FIRST "+tbComprobante.getLongitudNombre()+" ROW ONLY ");								
//			log.info("ruta --> "+ruta.toString()); 
//			pstmt = conn.prepareStatement(ruta.toString());
//			rset = pstmt.executeQuery();
//			while (rset.next()) {
//				Documento tb = new Documento();
//				this.getTbComprobante(tb);
//				listTbComprobante.add(tb);
//			}			
//			rset.close();
//		}catch (Exception e) {
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));
//			log.info("listComprobantesOld Exception \n"+errors);
//		} finally {
//			cerrarDao();
//		}
//		return listTbComprobante;
//	}	
		
	public String listComprobantes_Parametro(String parametro, String val) {
		//log.info("parametro: "+parametro);
		String descripcion="";
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String sql = "SELECT * FROM AXTEROIDOSE.SUNAT_PARAMETRO where cod_parametro='"+parametro+"' and COD_ARGUMENTO='"+val+"'";							
			log.info("sql --> "+sql);
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				descripcion = rset.getString(3);
			}	
			log.info("parametro: "+parametro+" - val: "+val+" - "+descripcion);
			rset.close();
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("listComprobantes_Parametro Exception \n"+errors);
		} finally {
			cerrarDao();
		}
		return descripcion;
	}									
			
	public List<Documento> listComprobantesMensajeSunat(String estado, String contar, String mensaje) {
		log.info("listComprobantesMensajeSunat: "+estado +"-"+ contar );
		List<Documento> listTbComprobante = new ArrayList<Documento>();
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();			
			String sql = "SELECT ID, ID_COMPROBANTE, RUC_EMISOR, " + 
					" TIPO_DOCUMENTO, SERIE, NUMERO_CORRELATIVO, NOMBRE, " + 
					" UBL, UBL_VERSION, ESTADO, ERROR_COMPROBANTE, "+					
					" user_crea, fecha_crea "+
					" FROM AXTEROIDOSE.DOCUMENTO WHERE "+
					" ESTADO = '"+estado+"' and mensaje_sunat "+mensaje+" "+
					" ORDER BY ID FETCH FIRST "+contar+" ROW ONLY ";							
			log.info("sql --> "+sql);
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				Documento tb = new Documento();
				this.getTbComprobante(tb);
				listTbComprobante.add(tb);
			}			
			rset.close();
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("listComprobantesMensajeSunat Exception \n"+errors);
		} finally {
			cerrarDao();
		}
		return listTbComprobante;
	}	
	
	public int countComprobantesEstadoErrorNumerContarDiaSinFe(String error_numero, String estado, int year) {
		//log.info("listComprobantes_Estado_ErrorNumero_Contar_Dia_RUC --> "+tbComprobante.getEstado()+" - "+tbComprobante.getErrorNumero());
		int resultado = 0;
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String sql = "SELECT count(*) as CPE FROM AXTEROIDOSE.DOCUMENTO WHERE " + 
					" ESTADO = '"+estado+"' and ERROR_NUMERO "+error_numero+" "+
					" and (error_log is null or LENGTH(error_log) != 10) " + 
					" and extract(year from error_log::date) = "+year+" ";							
			log.info("sql --> "+sql);
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				resultado = rset.getInt(1);
			}			
			rset.close();
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("countComprobantesEstadoErrorNumerContarDiaSinFe Exception \n"+errors);
		} finally {
			cerrarDao();
		}
		return resultado;
	}	
	
	public List<GroupBean> countGrupoEstado() {
		log.info("countGrupoEstado ");
		List<GroupBean> listGroupBean = new ArrayList<GroupBean>();
		try {						
			conn = DaoPostGresJDBC.getConnectionOSE();
			String sql = "SELECT ESTADO, count(*) as CPE FROM AXTEROIDOSE.DOCUMENTO " + 
					" where ESTADO not in ('','10','20','30','40','90') group by ESTADO order by ESTADO ";							
			log.info("sql --> "+sql);
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				//log.info(rset.getString(1)+" | "+rset.getInt(2));
				GroupBean groupBean = new GroupBean();
				groupBean.setEstado(rset.getString(1)); 
				groupBean.setIntCount(rset.getInt(2)); 
				listGroupBean.add(groupBean);
			}			
			rset.close();
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("countGrupoEstado Exception \n"+errors);
		} finally {
			cerrarDao();
		}
		return listGroupBean;
	}		
	
	public List<GroupBean> groupRespuestaSunat(String estado) {
		log.info("groupRespuestaSunat: "+estado);
		List<GroupBean> listGroupBean = new ArrayList<GroupBean>();
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String sql = " SELECT ESTADO, RESPUESTA_SUNAT, count(*) as CPE " + 
					" FROM AXTEROIDOSE.DOCUMENTO  where ERROR_COMPROBANTE = '0' and ESTADO "+estado+" " + 
					" group by ESTADO, RESPUESTA_SUNAT order by ESTADO, RESPUESTA_SUNAT ; ";							
			log.info("sql --> "+sql);
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				GroupBean groupBean = new GroupBean();
				groupBean.setEstado(rset.getString(1)); 
				groupBean.setError(rset.getString(2)); 
				groupBean.setIntCount(rset.getInt(3)); 
				listGroupBean.add(groupBean);
			}			
			rset.close();
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("groupRespuestaSunat Exception \n"+errors);
		} finally {
			cerrarDao();
		}
		return listGroupBean;
	}	
	
	public List<GroupBean> groupRespuestaSunatLN(String estado) {
		log.info("groupRespuestaSunat: "+estado);
		List<GroupBean> listGroupBean = new ArrayList<GroupBean>();
		try {			
			conn = DaoPostGresJDBC.getConnectionOSE();
			String sql = " SELECT ESTADO, RESPUESTA_SUNAT, count(*) as CPE " + 
					" FROM AXTEROIDOSE.DOCUMENTO  where ERROR_COMPROBANTE = '0' and ESTADO "+estado+" " + 
					" and longitud_nombre is null "+
					" group by ESTADO, RESPUESTA_SUNAT order by ESTADO, RESPUESTA_SUNAT ; ";							
			log.info("sql --> "+sql);
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				GroupBean groupBean = new GroupBean();
				groupBean.setEstado(rset.getString(1)); 
				groupBean.setError(rset.getString(2)); 
				groupBean.setIntCount(rset.getInt(3)); 
				groupBean.setReplay(false);
				listGroupBean.add(groupBean);
			}			
			rset.close();
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("groupRespuestaSunatLN Exception \n"+errors);
		} finally {
			cerrarDao();
		}
		return listGroupBean;
	}	
	
	public List<Documento> selectComprobante_PG_UBL() 
			throws SQLException{
		log.info("selectComprobante_PG_UBL --> ");
		List<Documento> listTbComprobante = new ArrayList<Documento>();
		try {
			conn = DaoPostGresJDBC.getConnectionOSE();
			String sql = "SELECT ID, UBL, CDR, MENSAJE_SUNAT, estado " + 
					" FROM AXTEROIDOSE.DOCUMENTO WHERE ubl is null "+ 
					" ORDER BY ID FETCH FIRST 1 ROW ONLY ";
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				Documento tb = new Documento();
				this.getTbComprobante_Blob(tb);
				listTbComprobante.add(tb);
			}
			rset.close();
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("selectComprobante_PG_UBL Exception \n"+errors);
		} finally {
			cerrarDao();
		}
		return listTbComprobante;
	}	
	
	private void getTbComprobante(Documento tbComprobante) {
		try {
			tbComprobante.setId(rset.getLong(1));
			tbComprobante.setIdComprobante(rset.getString(2));
			tbComprobante.setRucEmisor(rset.getLong(3));
			tbComprobante.setTipoDocumento(rset.getString(4));
			tbComprobante.setSerie(rset.getString(5));
			tbComprobante.setNumeroCorrelativo(rset.getString(6));
			tbComprobante.setNombre(rset.getString(7));
			tbComprobante.setUblVersion(rset.getString(8));
			tbComprobante.setEstado(rset.getString(9));
			tbComprobante.setErrorComprobante(rset.getString(10).charAt(0));		
			tbComprobante.setUserCrea(rset.getString(11));	
			tbComprobante.setFechaCrea(rset.getDate(12));
			tbComprobante.setErrorLog(rset.getString(13));
			tbComprobante.setErrorLogSunat(rset.getString(14));	
			tbComprobante.setMensajeSunat(rset.getBytes(15));
			tbComprobante.setRespuestaSunat(rset.getString(16));
			if(tipoList != 200)
				tbComprobante.setUbl(rset.getBytes(17));							
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cerrarDao Exception \n"+errors);
		}	
	}	
	
	private void getTbComprobante_Blob(Documento tbComprobante) {		
		try {
			log.info("getTbComprobante_Blob --> "+rset.getLong(1));
			tbComprobante.setId(rset.getLong(1));
			tbComprobante.setUbl(rset.getBytes(2));				
			tbComprobante.setCdr(rset.getBytes(3));
			tbComprobante.setMensajeSunat(rset.getBytes(4));
			tbComprobante.setEstado(rset.getString(5));
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cerrarDao Exception \n"+errors);
		}	
	}	
					
	private void cerrarDao() {
		try {
			if (cstmt != null)
				cstmt.close();
			if (rset != null)
				rset.close();
			if (pstmt != null)
				pstmt.close();
			if (stmt != null)
				stmt.close();
			if (conn != null && !conn.isClosed()) {
				conn.close();
				log.info("DisConnected: "+conn.isClosed());
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cerrarDao Exception \n"+errors);
		}
	}

	public void setTipoList(int tipoList) {
		this.tipoList = tipoList;
	}	

}
