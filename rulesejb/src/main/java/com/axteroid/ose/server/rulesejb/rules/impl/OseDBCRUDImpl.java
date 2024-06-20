package com.axteroid.ose.server.rulesejb.rules.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.Emisor;
import com.axteroid.ose.server.jpa.model.SunatAcuseReciboSunat;
import com.axteroid.ose.server.jpa.model.SunatContribuyenteAsociadoEmisor;
import com.axteroid.ose.server.jpa.model.ComprobantesPagoElectronicos;
import com.axteroid.ose.server.rulesejb.dao.DocumentoDAOLocal;
import com.axteroid.ose.server.rulesejb.dao.EmisorDAOLocal;
import com.axteroid.ose.server.rulesejb.dao.SunatAcuseReciboSunatDAOLocal;
import com.axteroid.ose.server.rulesejb.dao.SunatContribuyenteAsociadoEmisorDAOLocal;
import com.axteroid.ose.server.rulesejb.dao.SunatPadronVigenciaDAOLocal;
import com.axteroid.ose.server.rulesejb.dao.ComprobantesPagoElectronicosDAOLocal;
import com.axteroid.ose.server.rulesejb.rules.OseDBCRUDLocal;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.xml.UblCpe;

@Stateless
@Local(OseDBCRUDLocal.class)
public class OseDBCRUDImpl implements OseDBCRUDLocal{
	private static final Logger log = LoggerFactory.getLogger(OseDBCRUDImpl.class);
		
	public void grabarDocumento(Documento documento){
		log.info("grabarDocumento "+documento.getNombre());
		try {
			DocumentoDAOLocal documentoDAOLocal = 
					(DocumentoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"DocumentoDAOImpl"+Constantes.OSE_CDEJB_DAO+"DocumentoDAOLocal");
			documento.setFecFinProc(DateUtil.getCurrentTimestamp());				
			documento.setFechaCrea(DateUtil.getCurrentTimestamp());
			documentoDAOLocal.grabaDocumento(documento);				
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabarComprobante Exception \n"+errors);
		}
	}
	
	public void updateTbComprobante(Documento documento){
		//log.info("updateTbComprobante ");
		try {
			DocumentoDAOLocal documentoDAOLocal = 
					(DocumentoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"DocumentoDAOImpl"+Constantes.OSE_CDEJB_DAO+"DocumentoDAOLocal");
			documento.setFecFinProc(DateUtil.getCurrentTimestamp());
			documento.setUserModi(documento.getUserCrea());		
			documento.setFechaModi(DateUtil.getCurrentTimestamp());
			documentoDAOLocal.updateTbComprobante(documento);				
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("updateTbComprobante Exception \n"+errors);
		}		
	}
	
	public void updateTbComprobante_Send(Documento documento){
		//log.info("updateTbComprobante_Send ");
		//log.info("documento.getUserCrea(): "+documento.getUserCrea());
		try {
			DocumentoDAOLocal documentoDAOLocal = 
					(DocumentoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"DocumentoDAOImpl"+Constantes.OSE_CDEJB_DAO+"DocumentoDAOLocal");
			documento.setFecFinProc(DateUtil.getCurrentTimestamp());
			documento.setUserModi(documento.getUserCrea());		
			documento.setFechaModi(DateUtil.getCurrentTimestamp());
			documentoDAOLocal.updateTbComprobante(documento);				
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("updateTbComprobante_Send Exception \n"+errors);
		}		
	}
	
	public void updateTbComprobanteCDR(Documento documento){
		//log.info("updateTbComprobanteCDR ");
		try {
			DocumentoDAOLocal documentoDAOLocal = 
					(DocumentoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"DocumentoDAOImpl"+Constantes.OSE_CDEJB_DAO+"DocumentoDAOLocal");
			documento.setUserModi(documento.getUserCrea());		
			documento.setFechaModi(DateUtil.getCurrentTimestamp());
			documentoDAOLocal.updateTbComprobanteCDR(documento);				
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("updateTbComprobanteCDR Exception \n"+errors);
		}		
	}	
	
	public void updateTbComprobanteCDRID(Documento documento){
		//log.info("updateTbComprobanteCDRID ");
		try {
			DocumentoDAOLocal documentoDAOLocal = 
					(DocumentoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"DocumentoDAOImpl"+Constantes.OSE_CDEJB_DAO+"DocumentoDAOLocal");
			documento.setUserModi(documento.getUserCrea());		
			documento.setFechaModi(DateUtil.getCurrentTimestamp());
			documentoDAOLocal.updateTbComprobanteCDRID(documento);				
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("updateTbComprobanteCDR Exception \n"+errors);
		}		
	}		
	
	public Documento buscarTbComprobanteXIDComprobante_GET(Documento documento){
		//log.info("buscarTbComprobanteXIDComprobante_GET "+documento.toString());
		try {
			DocumentoDAOLocal documentoDAOLocal = 
					(DocumentoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"DocumentoDAOImpl"+Constantes.OSE_CDEJB_DAO+"DocumentoDAOLocal");
			List<Documento> listTBC = documentoDAOLocal.buscarTbComprobanteXIDComprobante_GET(documento);	
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return null;	
//			log.info("getNumeroCorrelativo "+listTBC.get(0).getNumeroCorrelativo());
//			log.info("getCdr "+listTBC.get(0).getCdr());
			documento.setErrorComprobante(Constantes.CONTENT_TRUE.charAt(0));
			return listTBC.get(0);						
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);		
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteXIDComprobante_GET Exception \n "+errors);
		}		
		return null;
	}	
	
	public Documento buscarTbComprobanteXIDComprobante(Documento documento){
		//log.info("buscarTbComprobanteXIDComprobante "+documento.toString());
		try {
			DocumentoDAOLocal documentoDAOLocal = 
					(DocumentoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"DocumentoDAOImpl"+Constantes.OSE_CDEJB_DAO+"DocumentoDAOLocal");
			List<Documento> results = documentoDAOLocal.buscarTbComprobanteXIDComprobante(documento);	
			if(results != null && results.size()>0)	{ 					
				if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
						documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return null;	
//			log.info("getNumeroCorrelativo "+listTBC.get(0).getNumeroCorrelativo());
//			log.info("getCdr "+listTBC.get(0).getCdr());
				documento.setErrorComprobante(Constantes.CONTENT_TRUE.charAt(0));
				return results.get(0);
			}
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);		
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteXIDComprobante Exception \n "+errors);
		}		
		return null;
	}	
	
	public Documento buscarTbComprobanteXIDComprobanteShort(Documento documento){
		//log.info("buscarTbComprobanteXIDComprobanteShort "+documento.toString());
		try {
			DocumentoDAOLocal documentoDAOLocal = 
					(DocumentoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"DocumentoDAOImpl"+Constantes.OSE_CDEJB_DAO+"DocumentoDAOLocal");
			List<Documento> listTBC = documentoDAOLocal.buscarTbComprobanteXIDComprobanteShort(documento);	
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return null;	
			//log.info("getNumeroCorrelativo "+listTBC.get(0).getNumeroCorrelativo());
			//log.info("getCdr "+listTBC.get(0).getCdr());
			documento.setErrorComprobante(Constantes.CONTENT_TRUE.charAt(0));
			return listTBC.get(0);						
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);		
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteXIDComprobante Exception \n "+errors);
		}		
		return null;
	}	
	

    
	public void getCompobantePago(Documento documento) {
		//log.info("getCompobantePago ");
    	try {   		
			ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
			tsComprobantesPagoElectronicosDAOLocal.getCompobantePago(documento);				
			if(documento.getErrorComprobante()!= null && (documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))))
				return;	
		} catch (Exception e) {
			//documento.setErrorComprobante(OseConstantes.CONTENT_ERROR_DB.charAt(0));
			//documento.setErrorNumero(OseConstantes.SUNAT_ERROR_0138);
			//documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePago Exception \n"+errors);
		}
		
	}
	
	public Documento buscarTbComprobanteXID(Documento documento){
		//log.info("buscarTbComprobanteXID "+documento.getId());
		try {
			DocumentoDAOLocal documentoDAOLocal = 
					(DocumentoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"DocumentoDAOImpl"+Constantes.OSE_CDEJB_DAO+"DocumentoDAOLocal");
			List<Documento> listTBC = documentoDAOLocal.buscarTbComprobanteXID(documento);					
			if(listTBC != null && listTBC.size()>0)	
				return listTBC.get(0);													
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);		
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteXID Exception \n "+errors);
		}		
		return null;
	}	
	
	public List<Documento> buscarTbComprobanteALL(Documento documento){
		//log.info("buscarTbComprobanteALL "+documento.getId());
		try {
			DocumentoDAOLocal documentoDAOLocal = 
					(DocumentoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"DocumentoDAOImpl"+Constantes.OSE_CDEJB_DAO+"DocumentoDAOLocal");
			List<Documento> listTBC = documentoDAOLocal.buscarTbComprobanteXID(documento);					
			return listTBC;													
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);		
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteALL Exception \n "+errors);
		}		
		return null;
	}	
	
	public List<ComprobantesPagoElectronicos> getCompobantePagoItem(Documento documento, 
			String tipoDocRefPri, String nroDocRefPri) {
		//log.info("getCompobantePagoItem "+documento.getId());
    	try {
			ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");			
			return tsComprobantesPagoElectronicosDAOLocal.getCompobantePagoItem(documento, tipoDocRefPri, nroDocRefPri);				
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCompobantePagoItem Exception \n"+errors);
		}
    	return null;
	}
	
	public List<Documento> buscarTbComprobanteLike(Documento documento){
		//log.info("buscarTbComprobanteLike "+documento.getId());
		try {
			DocumentoDAOLocal documentoDAOLocal = 
					(DocumentoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"DocumentoDAOImpl"+Constantes.OSE_CDEJB_DAO+"DocumentoDAOLocal");
			List<Documento> listTBC = documentoDAOLocal.buscarTbComprobanteXID(documento);					
			return listTBC;													
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);		
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteALL Exception \n "+errors);
		}		
		return null;
	}		
	
	public void grabarTsAcuseReciboSunat(SunatAcuseReciboSunat tsAcuseReciboSunat) {
		//log.info("grabarTsAcuseReciboSunat: "+tsAcuseReciboSunat.toString());
		try {
			SunatAcuseReciboSunatDAOLocal tsAcuseReciboSunatDAOLocal = 
					(SunatAcuseReciboSunatDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatAcuseReciboSunatDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatAcuseReciboSunatDAOLocal");
			tsAcuseReciboSunatDAOLocal.grabaTsAcuseReciboSunat(tsAcuseReciboSunat);																	
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabarTsAcuseReciboSunat Exception \n "+errors);
		}		
	}	
	
	public Integer countComprobantePagoIDState(Documento documento, int sState) {
		//log.info("countComprobantePagoIDState "+documento.toString()+" | "+sState);
		Integer countState = 0;
		try {
			ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
			countState = tsComprobantesPagoElectronicosDAOLocal.countComprobantePagoIDState(documento, sState);																	
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("countComprobantePagoIDState Exception \n "+errors);
		}		
		return countState;
	}		
	
	public List<Documento> buscarTbComprobanteXContentID_GET(Documento documento){
		log.info("buscarTbComprobanteXContentID_GET ");
		try {
			DocumentoDAOLocal documentoDAOLocal = 
					(DocumentoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"DocumentoDAOImpl"+Constantes.OSE_CDEJB_DAO+"DocumentoDAOLocal");
			List<Documento> listTBC = documentoDAOLocal.buscarTbComprobanteXContentID_GET(documento);	
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return null;			 
			return listTBC;				
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);	
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarTbComprobanteXContentID_GET Exception \n "+errors);
		}		
		return null;
	}	
	
	public Emisor buscarEmisor(Documento documento){
		//log.info("buscarEmisor "+documento.toString());
		try {
			EmisorDAOLocal emisorDAOLocal = 
					(EmisorDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"EmisorDAOImpl"+Constantes.OSE_CDEJB_DAO+"EmisorDAOLocal");
			Emisor emisor = emisorDAOLocal.buscarEmisor(documento);	
			if(emisor != null)
				return emisor;	
			return null;						
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);		
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarEmisor Exception \n "+errors);
		}		
		return null;
	}	
	
	public void getPadronAutorizadoIgv(Documento documento) {
		//log.info("getPadronAutorizadoIgv: {} ",documento.toString());
		try {
			Date emisiondate = new Date();
			if((documento.getTipoDocumento().trim().equals(Constantes.SUNAT_FACTURA)) ||
					(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_BOLETA))||
					(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_NOTA_CREDITO))||
					(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_NOTA_DEBITO))) 
				emisiondate = UblCpe.readIssueDateFromUbl(documento.getUbl());
			if(documento.getTipoDocumento().trim().equals(Constantes.SUNAT_FACTURA))
					emisiondate = UblCpe.readReferenceDateFromUbl(documento.getUbl());
			documento.setFecFinProc(emisiondate);
			SunatPadronVigenciaDAOLocal sunatPadronVigenciaDAOLocal = 
					(SunatPadronVigenciaDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatPadronVigenciaDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatPadronVigenciaDAOLocal");
			sunatPadronVigenciaDAOLocal.getPadronAutorizadoIgv(documento);	
			log.info("getPadronAutorizadoIgv: {} | IGV: {}",documento.toString(),documento.getLongitudNombre());
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getPadronAutorizadoIgv Exception \n "+errors);
		}		
	}	
	
    public void getEstadoCompobantePagoElectronicos(Documento documento) {
    	//log.info("getEstadoCompobantePagoElectronicos: "+documento.getId());    
    	try {   		   		
			ComprobantesPagoElectronicosDAOLocal comprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");			
			if((documento.getTipoDocumento().equals(Constantes.SUNAT_FACTURA)) || 
					(documento.getTipoDocumento().equals(Constantes.SUNAT_BOLETA)) ||
					(documento.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO)) ||
					(documento.getTipoDocumento().equals(Constantes.SUNAT_NOTA_DEBITO))||
					(documento.getTipoDocumento().equals(Constantes.SUNAT_ROL_ADQUIRIENTE_SISTEMAS_PAGO)) ||
					(documento.getTipoDocumento().equals(Constantes.SUNAT_OPERADOR)) ||
					(documento.getTipoDocumento().equals(Constantes.SUNAT_ADQUIRIENTE_SISTEMAS_PAGO)))
				comprobantesPagoElectronicosDAOLocal.getCompobantePago(documento);		    	
			if((documento.getTipoDocumento().equals(Constantes.SUNAT_RETENCION)) || 
					(documento.getTipoDocumento().equals(Constantes.SUNAT_PERCEPCION)))
				comprobantesPagoElectronicosDAOLocal.getCompobantePago_RP(documento);	
			log.info("getEstadoCompobantePagoElectronicos) documento: {} | ErrorComprobante(): {} | estado: {} | errornumero: {} | ubl: {}", documento.toString(),
					documento.getErrorComprobante(), documento.getEstado(),documento.getErrorNumero(),documento.getUbl().length);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getEstadoCompobantePagoElectronicos Exception \n"+errors);
		}
    } 	
    
	public List<SunatContribuyenteAsociadoEmisor> getJoinPSEEmisor(Documento documento, 
			int indicador, Long rucPSE){
		//log.info("getJoinPSEEmisor: {} | {} | {} ",documento.toString(), indicador, rucPSE);
		try {
			SunatContribuyenteAsociadoEmisorDAOLocal sunatContribuyenteAsociadoEmisorDAOLocal = 
					(SunatContribuyenteAsociadoEmisorDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"EmisorDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatContribuyenteAsociadoEmisorDAOLocal");
			List<SunatContribuyenteAsociadoEmisor> results = sunatContribuyenteAsociadoEmisorDAOLocal.getJoinPSEEmisor(documento, indicador, rucPSE);	
			if(results != null && results.size()>0)
				return results;						
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);		
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarEmisor Exception \n "+errors);
		}		
		return null;
	}   
}
