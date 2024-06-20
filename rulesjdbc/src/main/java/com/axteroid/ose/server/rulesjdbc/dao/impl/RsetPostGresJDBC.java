package com.axteroid.ose.server.rulesjdbc.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesjdbc.jdbc.PostGresJDBCDao;

public class RsetPostGresJDBC extends PostGresJDBCDao{
	private static final Logger log = LoggerFactory.getLogger(RsetPostGresJDBC.class);
	private CallableStatement cstmt;
	private ResultSet rset = null;
	private PreparedStatement pstmt;
	private Statement stmt;
	
	public List<Documento> listComprobantesJDBC(Documento tbComprobante) {
		//log.info("listComprobantesJDBC "+tbComprobante.toString());
		List<Documento> listTbComprobante = new ArrayList<Documento>();
		boolean a = false;
		boolean b = true;
		int getStatus = 0;
		int getStatusCDR = 0;		
		try {			
			conn = PostGresJDBCDao.getConnectionOSE();
			String sql = "SELECT ID, ID_COMPROBANTE, RUC_EMISOR, " + 
					" TIPO_COMPROBANTE, SERIE, NUMERO_CORRELATIVO, NOMBRE, " + 
					" LONGITUD_NOMBRE, DIRECCION, UBL, UBL_VERSION, " + 
					" CUSTOMIZA_VERSION, CDR, ESTADO, ERROR_COMPROBANTE, " + 
					" ERROR_NUMERO, ERROR_DESCRIPCION, ERROR_LOG, OBSERVA_NUMERO, " + 
					" OBSERVA_DESCRIPCION, ADVERTENCIA, FECHA_ENVIO_SUNAT, RESPUESTA_SUNAT, " + 
					" MENSAJE_SUNAT, FECHA_RESPUESTA_SUNAT, ERROR_LOG_SUNAT, FEC_INI_PROC, FEC_FIN_PROC, USER_CREA " + 	
					" FROM AXTEROIDOSE.TB_COMPROBANTE WHERE ";
			StringBuilder ruta = new StringBuilder(sql);			
			if((tbComprobante.getId()!= null)
					&& (tbComprobante.getId()!= null)) {
				getStatus++;
				if(b) {
					ruta.append(" ID = "+tbComprobante.getId()+" ");	
					b = false;
				}					
			}	
			if(tbComprobante.getRucEmisor()> 0){	
				getStatusCDR++;
				if(b) {
					ruta.append(" RUC_EMISOR = "+tbComprobante.getRucEmisor()+" ");
					b = false;
				}else
					ruta.append(" and RUC_EMISOR = "+tbComprobante.getRucEmisor()+" ");
			}			
			if((tbComprobante.getTipoDocumento()!= null) && 
					(!tbComprobante.getTipoDocumento().isEmpty())) {
				getStatusCDR++;
				if(b) {
					ruta.append(" TIPO_COMPROBANTE = '"+tbComprobante.getTipoDocumento()+"' ");
					b = false;
				}else
					ruta.append(" and TIPO_COMPROBANTE = '"+tbComprobante.getTipoDocumento()+"' ");
			}
			if((tbComprobante.getSerie()!= null) && 
					(!tbComprobante.getSerie().isEmpty())) {
				getStatusCDR++;
				if(b) {
					ruta.append(" SERIE = '"+tbComprobante.getSerie()+"' ");
					b = false;
				}else
					ruta.append(" and SERIE = '"+tbComprobante.getSerie()+"' ");
			}			
			if((tbComprobante.getNumeroCorrelativo()!= null) && 
					(!tbComprobante.getNumeroCorrelativo().isEmpty())){
				getStatusCDR++;
				if(b) {
					ruta.append(" NUMERO_CORRELATIVO = '"+tbComprobante.getNumeroCorrelativo()+"' ");
					b = false;
				}else
					ruta.append(" and NUMERO_CORRELATIVO = '"+tbComprobante.getNumeroCorrelativo()+"' ");
			}					
			if(getStatus==1)
				a = true;
			if(getStatusCDR==4)
				a = true;
			if(a) {
				log.info("script --> "+ruta.toString()); 
				pstmt = conn.prepareStatement(ruta.toString());
				rset = pstmt.executeQuery();
				while (rset.next()) {
					Documento tb = new Documento();
					this.getTbComprobante(tb);
					//log.info("tb --> "+tb.toString()); 
					listTbComprobante.add(tb);
				}			
			}
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("listComprobantes Exception \n"+errors);
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
			tbComprobante.setLongitudNombre(rset.getInt(8));				
			tbComprobante.setDireccion(rset.getString(9));
			tbComprobante.setUbl(rset.getBytes(10));				
			tbComprobante.setUblVersion(rset.getString(11));
			tbComprobante.setCustomizaVersion(rset.getString(12));
			tbComprobante.setCdr(rset.getBytes(13));
			tbComprobante.setEstado(rset.getString(14));
			tbComprobante.setErrorComprobante(rset.getString(15).charAt(0));
			tbComprobante.setErrorNumero(rset.getString(16));
			tbComprobante.setErrorDescripcion(rset.getString(17));
			tbComprobante.setErrorLog(rset.getString(18));
			tbComprobante.setObservaNumero(rset.getString(19));
			tbComprobante.setObservaDescripcion(rset.getString(20));
			tbComprobante.setAdvertencia(rset.getString(21));	
			tbComprobante.setFechaEnvioSunat(rset.getDate(22));
			tbComprobante.setRespuestaSunat(rset.getString(23));
			tbComprobante.setMensajeSunat(rset.getBytes(24));
			tbComprobante.setFechaRespuestaSunat(rset.getDate(25));
			tbComprobante.setErrorLogSunat(rset.getString(26));
			tbComprobante.setFecIniProc(rset.getDate(27));
			tbComprobante.setFecFinProc(rset.getDate(28));	
			tbComprobante.setUserCrea(rset.getString(29));
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

}
