package com.axteroid.ose.server.rulesejb.dao.impl;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatCertificadoEmisor;
import com.axteroid.ose.server.rulesejb.dao.SunatCertificadoEmisorDAOLocal;
import com.axteroid.ose.server.rulesejb.emf.impl.DAOComImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.FileUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Local(SunatCertificadoEmisorDAOLocal.class)
public class SunatCertificadoEmisorDAOImpl extends DAOComImpl<SunatCertificadoEmisor> 
		implements SunatCertificadoEmisorDAOLocal {
	private static final Logger log = LoggerFactory.getLogger(SunatCertificadoEmisorDAOImpl.class);
	
    public SunatCertificadoEmisorDAOImpl() {}
    
    private List<SunatCertificadoEmisor> buscarEmisor_NumIdCd(Documento tbComprobante, String numIdCd) {
    	//log.info("buscarEmisor_NumIdCd --> "+numIdCd);
    	try {    		
    		String consulta = "SELECT d FROM SunatCertificadoEmisor d "
				+" where d.sunatCertificadoEmisorPK.numIdCd = :numIdCd "
				+" order by d.fecAlta desc";
    		Map<String, Object> parameterValues = new HashMap<String, Object>();
    		parameterValues.put("numIdCd", numIdCd);	    	
    		List<SunatCertificadoEmisor> results = this.query(consulta, parameterValues);
    		if(results != null && results.size()>0)
    			return results;
    	} catch (Exception e) {
    		tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarEmisor_NumIdCd Exception \n"+errors);
		}
        return null;    	
    }    
    
	public void getCertificadoEmisor_NumIdCd(Documento documento, 
			Date fechaEmisionComprobante, String numIdCd, Date dategetNotBefore, Date dateNotAfter) {
    	try {    		
    		String ruta = (new StringBuilder(Constantes.DIR_AXTEROID_OSE)).toString();
    		if(FileUtil.tobeDirectory(ruta+Constantes.DIR_AMB_BETA))
    			return;
    		
			List<SunatCertificadoEmisor> results = this.buscarEmisor_NumIdCd(documento, numIdCd);	
			if(results != null && results.size()>0) {
				for(SunatCertificadoEmisor sunatCertificadoEmisor : results) {						
					if(documento.getRucEmisor() == sunatCertificadoEmisor.getSunatCertificadoEmisorPK().getNumRuc()
							|| documento.getRucPseEmisor() == sunatCertificadoEmisor.getSunatCertificadoEmisorPK().getNumRuc()) {
						//DateUtil dateUtil = new DateUtil();
						//log.info("fechaEmisionComprobante: "+fechaEmisionComprobante);
						//log.info("sunatCertificadoEmisor.getFecAlta(): "+tsCertificadoEmisor.getFecAlta());
						//log.info("sunatCertificadoEmisor.getFecBaja(): "+tsCertificadoEmisor.getFecBaja());						
						if(sunatCertificadoEmisor.getFecBaja()!= null){
							String anio = DateUtil.getYear(sunatCertificadoEmisor.getFecBaja());
							log.info("anio: "+anio);
							if(anio.equals("0001"))
								return;
						
							long ldias = DateUtil.deltaDays(fechaEmisionComprobante,sunatCertificadoEmisor.getFecBaja());	
							//log.info("ldias: "+ldias);
							if(ldias>0) {
								documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
								documento.setErrorNumero(Constantes.SUNAT_ERROR_2326);
								return;
							}	
						}
							
//						ldias = dateUtil.deltaDays(fechaEmisionComprobante, dateNotAfter);
//						if(ldias>0) {
//							documento.setErrorComprobante(OseConstantes.CONTENT_FALSE.charAt(0));
//							documento.setErrorNumero(OseConstantes.SUNAT_ERROR_2327);
//							return;
//						}
						return;
					}    			
				}			
			}
			//// 
			//documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			//documento.setErrorNumero(Constantes.SUNAT_ERROR_2325);
    	} catch (Exception e) {
    		documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
    		documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
    		documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCertificadoEmisor_NumIdCd Exception \n"+errors);
		}
    }    
}
