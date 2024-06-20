package com.axteroid.ose.server.avatar.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.dao.SunatComprobantesPagoElectronicosDAO;
import com.axteroid.ose.server.avatar.jdbc.DaoSunatListPGJdbc;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatComprobantesPagoElectronicos;
import com.axteroid.ose.server.jpa.model.SunatComprobantesPagoElectronicosPK;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.StringUtil;


public class SunatComprobantesPagoElectronicosDAOImpl extends DaoSunatListPGJdbc implements SunatComprobantesPagoElectronicosDAO {
	private static final Logger log = LoggerFactory.getLogger(SunatComprobantesPagoElectronicosDAOImpl.class);
	private ResultSet rset = null;
	private PreparedStatement pstmt;	
	
	public List<SunatComprobantesPagoElectronicos> buscarSunatCompobantePagoDB(Documento tbComprobante, 
    		Long numRuc, String tipoDocRefPri, String nroDocRefPri) {
		List<SunatComprobantesPagoElectronicos> listSunatComprobantesPagoElectronicos = new ArrayList<SunatComprobantesPagoElectronicos>();	
		try {
			String [] docRefPri = nroDocRefPri.split("-");
			log.info("buscarSunatCompobantePagoDB (1): "+numRuc+"-"+ tipoDocRefPri+"-"+docRefPri[0]+"-"+docRefPri[1]);
			String ruc = String.valueOf(numRuc);
			int iNumCor = 0;
			if(StringUtil.hasString(docRefPri[1]))
				iNumCor = Integer.parseInt(docRefPri[1]);   		
			listSunatComprobantesPagoElectronicos = this.buscarSunatcpemainDB(ruc, tipoDocRefPri, docRefPri[0], iNumCor);
			if(listSunatComprobantesPagoElectronicos.size()>0)
				return listSunatComprobantesPagoElectronicos;
			listSunatComprobantesPagoElectronicos = this.buscarSunatcpepreoseDB(ruc, tipoDocRefPri, docRefPri[0], iNumCor);
    	} catch (Exception e) {
    		tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarSunatCompobantePagoDB (1) Exception \n"+errors);
		}
        return listSunatComprobantesPagoElectronicos;		
	}
	
	public List<SunatComprobantesPagoElectronicos> buscarSunatCompobantePagoDB(String ruc, String tipo, String serie, String numero) {
		List<SunatComprobantesPagoElectronicos> listSunatComprobantesPagoElectronicos = new ArrayList<SunatComprobantesPagoElectronicos>();	
		try {
			log.info("buscarSunatCompobantePagoDB (2): "+ruc+"-"+ tipo+"-"+ serie+"-"+numero);
			Integer num = Integer.parseInt(numero);
			listSunatComprobantesPagoElectronicos = this.buscarSunatcpemainDB(ruc, tipo, serie, num);
			if(listSunatComprobantesPagoElectronicos.size()>0) 
				return listSunatComprobantesPagoElectronicos;
			listSunatComprobantesPagoElectronicos = this.buscarSunatcpepreoseDB(ruc, tipo, serie, num);
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarSunatCompobantePagoDB (2) Exception \n"+errors);
		}
        return listSunatComprobantesPagoElectronicos;		
	}
	
    public List<SunatComprobantesPagoElectronicos> buscarSunatcpemainDB(String ruc, String tipo, String serie, Integer num) {    
    	List<SunatComprobantesPagoElectronicos> listSunatComprobantesPagoElectronicos = new ArrayList<SunatComprobantesPagoElectronicos>();
    	try {    		
    		log.info("buscarSunatcpemainDBSunatList: "+ruc+"-"+ tipo+"-"+serie+"-"+num);    		
			String sql = "SELECT num_ruc, cod_cpe, num_serie_cpe, num_cpe, ind_estado_cpe, fec_emision_cpe, " 
					+ " mto_importe_cpe, cod_moneda_cpe, cod_mot_traslado, cod_mod_traslado, ind_transbordo, " 
					+ "fec_ini_traslado, user_crea " 
					+ " FROM AXTEROIDOSE.ts_cpe_main WHERE num_ruc = ? AND cod_cpe = ? "
					+ " AND num_serie_cpe = ? AND num_cpe = ?";
			conn = DaoSunatListPGJdbc.getConnectionSunatList();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ruc);
			pstmt.setString(2, tipo);
			pstmt.setString(3, serie);		
			pstmt.setInt(4, num);
			rset = pstmt.executeQuery();			    		   		
			while (rset.next()) {
				SunatComprobantesPagoElectronicos ts = new SunatComprobantesPagoElectronicos();
				this.getSunatComprobantesPagoElectronicos(ts);
				this.viewSunatComprobantesPagoElectronicos(ts);
				listSunatComprobantesPagoElectronicos.add(ts);
			}					
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("buscarSunatcpemainDB Exception \n"+errors);
		} finally {
			cerrarDao();
		}
        return listSunatComprobantesPagoElectronicos;
    }   	
    
    public List<SunatComprobantesPagoElectronicos> buscarSunatcpepreoseDB(String ruc, 
    		String tipo, String serie, Integer num) throws SQLException {    
    	List<SunatComprobantesPagoElectronicos> listSunatComprobantesPagoElectronicos = new ArrayList<SunatComprobantesPagoElectronicos>();
    	try {    		
    		log.info("buscarSunatcpepreoseDB: "+ruc+"-"+ tipo+"-"+serie+"-"+num);    		
			String sql = "SELECT num_ruc, cod_cpe, num_serie_cpe, num_cpe, ind_estado_cpe, fec_emision_cpe, " 
					+ " mto_importe_cpe, cod_moneda_cpe, cod_mot_traslado, cod_mod_traslado, ind_transbordo, " 
					+ "fec_ini_traslado, user_crea " 
					+ " FROM AXTEROIDOSE.ts_cpe_preose WHERE num_ruc = ? AND cod_cpe = ? "
					+ " AND num_serie_cpe = ? AND num_cpe = ?";
			conn = DaoSunatListPGJdbc.getConnectionSunatList();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ruc);
			pstmt.setString(2, tipo);
			pstmt.setString(3, serie);		
			pstmt.setInt(4, num);
			rset = pstmt.executeQuery();			    		   		
			while (rset.next()) {
				SunatComprobantesPagoElectronicos sunatCpe = new SunatComprobantesPagoElectronicos();
				this.getSunatComprobantesPagoElectronicos(sunatCpe);
				this.viewSunatComprobantesPagoElectronicos(sunatCpe);
				listSunatComprobantesPagoElectronicos.add(sunatCpe);
			}					
		}catch (Exception e) {			
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("buscarSunatcpepreoseDB Exception \n"+errors);
		} finally {
			cerrarDao();
		}
        return listSunatComprobantesPagoElectronicos;
    }       
    
    private void getSunatComprobantesPagoElectronicos(SunatComprobantesPagoElectronicos sunatComprobantesPagoElectronicos) {
    	try {
			SunatComprobantesPagoElectronicosPK sunatComprobantesPagoElectronicosPK = new SunatComprobantesPagoElectronicosPK();
			Long numRuc = Long.parseLong(rset.getString(1));
			sunatComprobantesPagoElectronicosPK.setNumRuc(numRuc);
			sunatComprobantesPagoElectronicosPK.setCodCpe(rset.getString(2));
			sunatComprobantesPagoElectronicosPK.setNumSerieCpe(rset.getString(3));
			Long numCpe = Long.parseLong(rset.getString(4));
			sunatComprobantesPagoElectronicosPK.setNumCpe(numCpe);   			
			sunatComprobantesPagoElectronicos.setSunatComprobantesPagoElectronicosPK(sunatComprobantesPagoElectronicosPK);
			Short indEstadoCpe = Short.valueOf(rset.getString(5));
			sunatComprobantesPagoElectronicos.setIndEstadoCpe(indEstadoCpe);			
			sunatComprobantesPagoElectronicos.setFecEmisionCpe(rset.getDate(6));			
			BigDecimal mtoImporteCpe = BigDecimal.valueOf(rset.getDouble(7));
			sunatComprobantesPagoElectronicos.setMtoImporteCpe(mtoImporteCpe);
			sunatComprobantesPagoElectronicos.setCodMonedaCpe(rset.getString(8));
			String motTraslado = rset.getString(9);	
			Short codMotTraslado = 0;
			if(motTraslado!=null)
				codMotTraslado = Short.valueOf(motTraslado);			
			sunatComprobantesPagoElectronicos.setCodMotTraslado(codMotTraslado);
			String modTraslado = rset.getString(10);			
			Short codModTraslado = 0;
			if(modTraslado!=null)
				codModTraslado = Short.parseShort(modTraslado);
			sunatComprobantesPagoElectronicos.setCodModTraslado(codModTraslado);
			String transbordo = rset.getString(11);			
			Short indTransbordo = 0;
			if(transbordo!=null)
				indTransbordo = Short.valueOf(transbordo);
			sunatComprobantesPagoElectronicos.setIndTransbordo(indTransbordo);				
			sunatComprobantesPagoElectronicos.setFecIniTraslado(rset.getDate(12));	
			sunatComprobantesPagoElectronicos.setUserCrea(rset.getString(13));
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("getTsComprobantesPagoElectronicos Exception \n"+errors);
		}
    }
    
    private void viewSunatComprobantesPagoElectronicos(SunatComprobantesPagoElectronicos sunatComprobantesPagoElectronicos) {
    	log.info("NumRuc: {}",sunatComprobantesPagoElectronicos.getSunatComprobantesPagoElectronicosPK().getNumRuc());
    	log.info("CodCpe: {}",sunatComprobantesPagoElectronicos.getSunatComprobantesPagoElectronicosPK().getCodCpe());
    	log.info("NumSerieCpe: {}",sunatComprobantesPagoElectronicos.getSunatComprobantesPagoElectronicosPK().getNumSerieCpe());
    	log.info("NumCpe: {}",sunatComprobantesPagoElectronicos.getSunatComprobantesPagoElectronicosPK().getNumCpe());
    	log.info("IndEstadoCpe: {}",sunatComprobantesPagoElectronicos.getIndEstadoCpe());
    	log.info("FecEmisionCpe: {}",sunatComprobantesPagoElectronicos.getFecEmisionCpe());
    	log.info("MtoImporteCpe: {}",sunatComprobantesPagoElectronicos.getMtoImporteCpe());
    	log.info("CodMonedaCpe: {}",sunatComprobantesPagoElectronicos.getCodMonedaCpe());
    	log.info("CodMotTraslado: {}",sunatComprobantesPagoElectronicos.getCodMotTraslado());
    	log.info("CodModTraslado: {}",sunatComprobantesPagoElectronicos.getCodModTraslado());
    	log.info("IndTransbordo: {}",sunatComprobantesPagoElectronicos.getIndTransbordo());
    	log.info("FecIniTraslado: {}",sunatComprobantesPagoElectronicos.getFecIniTraslado());
    	log.info("UserCrea: {}",sunatComprobantesPagoElectronicos.getUserCrea());
    }
    
	private void cerrarDao() {
		try {
			//if (cstmt != null)
			//	cstmt.close();
			if (rset != null)
				rset.close();
			if (pstmt != null)
				pstmt.close();
			//if (stmt != null)
			//	stmt.close();
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
