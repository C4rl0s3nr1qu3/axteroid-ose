package com.axteroid.ose.server.rulesejb.rules.impl;

import com.axteroid.ose.server.apirest.sunat.ConsultaIntegradaSunatRest;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesejb.dao.SunatAutorizacionComprobPagoFisicoDAOLocal;
import com.axteroid.ose.server.rulesejb.dao.SunatAutorizacionRangosContingenciaDAOLocal;
import com.axteroid.ose.server.rulesejb.dao.SunatCertificadoEmisorDAOLocal;
import com.axteroid.ose.server.rulesejb.dao.ComprobantesPagoElectronicosDAOLocal;
import com.axteroid.ose.server.rulesejb.dao.SunatContribuyenteAsociadoEmisorDAOLocal;
import com.axteroid.ose.server.rulesejb.dao.SunatContribuyenteDAOLocal;
import com.axteroid.ose.server.rulesejb.dao.SunatEstablecimientosAnexosDAOLocal;
import com.axteroid.ose.server.rulesejb.dao.SunatPadronContribuyenteDAOLocal;
import com.axteroid.ose.server.rulesejb.rules.UBLListValidateDataLocal;
import com.axteroid.ose.server.tools.bean.SunatTokenResponse;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumentoItem;
import com.axteroid.ose.server.tools.ubltype.TypeAllowanceCharge;
import com.axteroid.ose.server.tools.ubltype.TypeBillingReference;
import com.axteroid.ose.server.tools.ubltype.TypeDocumentReference;
import com.axteroid.ose.server.tools.ubltype.TypeParty;
import com.axteroid.ose.server.tools.ubltype.TypePartyIdentification;
import com.axteroid.ose.server.tools.ubltype.TypePartyLegalEntity;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.StringUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.naming.InitialContext;

import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Local(UBLListValidateDataLocal.class)
public class UBLListValidateDataImpl implements UBLListValidateDataLocal {

    public UBLListValidateDataImpl() {}
    private static final Logger log = LoggerFactory.getLogger(UBLListValidateDataImpl.class);
    
	public void reglasValidarDatosGeneral(Documento tbComprobante,  
			Date fechaEmisionComprobante, String numIdCd, Date dategetNotBefore, Date dateNotAfter) {
		log.info("reglasValidarDatosGeneral --> "+tbComprobante.getTipoDocumento());
		try {					
			SunatContribuyenteAsociadoEmisorDAOLocal tsContribuyenteAsociadoEmisorDAOLocal = 
				(SunatContribuyenteAsociadoEmisorDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatContribuyenteAsociadoEmisorDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatContribuyenteAsociadoEmisorDAOLocal");						
			Date fechaRecepcion = new Date();
			tsContribuyenteAsociadoEmisorDAOLocal.getContribuyenteAsociado(tbComprobante, fechaRecepcion);	
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;
			
			tsContribuyenteAsociadoEmisorDAOLocal.getContribuyentePSEEmisor(tbComprobante, fechaEmisionComprobante);	
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;

			SunatCertificadoEmisorDAOLocal tsCertificadoEmisorDAOLocal = 
					(SunatCertificadoEmisorDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatCertificadoEmisorDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatCertificadoEmisorDAOLocal");
			tsCertificadoEmisorDAOLocal.getCertificadoEmisor_NumIdCd(tbComprobante, fechaEmisionComprobante, 
					numIdCd, dategetNotBefore, dateNotAfter);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;			
			
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);	
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosGeneral Exception \n"+errors);
		}
	}
    
    public void reglasValidarDatosBoleta(Documento tbComprobante, EDocumento eDocumento) {
    	log.info("reglasValidarDatosBoleta --> "+tbComprobante.toString());
		try {									
			ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
			tsComprobantesPagoElectronicosDAOLocal.getCompobantePago(tbComprobante);			
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;			
			
    		if(StringUtil.hasString(tbComprobante.getSerie())) {
    			SunatAutorizacionComprobPagoFisicoDAOLocal tsAutorizacionComprobPagoFisicoDAOLocal = 
					(SunatAutorizacionComprobPagoFisicoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatAutorizacionComprobPagoFisicoDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatAutorizacionComprobPagoFisicoDAOLocal");
    			tsAutorizacionComprobPagoFisicoDAOLocal.getAutorizacionComprobPagoFisico_FB(tbComprobante);			
    			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
    				return;	
    			
    			SunatAutorizacionRangosContingenciaDAOLocal tsAutorizacionRangosContingenciaDAOLocal = 
					(SunatAutorizacionRangosContingenciaDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatAutorizacionRangosContingenciaDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatAutorizacionRangosContingenciaDAOLocal");
    			tsAutorizacionRangosContingenciaDAOLocal.getAutorizacionRangosContingencia(tbComprobante);			
    			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
    				return;				
    		}   				
			
			SunatContribuyenteDAOLocal tsContribuyenteDAOLocal = 
					(SunatContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatContribuyenteDAOLocal");
			tsContribuyenteDAOLocal.getContribuyente(tbComprobante);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;		
						
			SunatEstablecimientosAnexosDAOLocal tsEstablecimientosAnexosDAOLocal = 
					(SunatEstablecimientosAnexosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatEstablecimientosAnexosDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatEstablecimientosAnexosDAOLocal");
			
			String tipoDocumentoModifica = "";
			for(TypeBillingReference typeBillingReference : eDocumento.getBillingReference()) {
				if(typeBillingReference.getInvoiceDocumentReference()!= null) {
					if(typeBillingReference.getInvoiceDocumentReference().getDocumentTypeCode()!= null) {
						tipoDocumentoModifica = typeBillingReference.getInvoiceDocumentReference().getDocumentTypeCode();
					}
				}				
			}
			if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_FACTURA) ||
					tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_BOLETA)) {
				if((eDocumento.getTipoOperacionFactura()!=null) && 
						(eDocumento.getTipoOperacionFactura().equals(Constantes.SUNAT_TIPO_OPERACION_0201))) {
					SunatPadronContribuyenteDAOLocal tsPadronContribuyenteDAOLocal = 
							(SunatPadronContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatPadronContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatPadronContribuyenteDAOLocal");
					tsPadronContribuyenteDAOLocal.getPadronContribuyente_FB(tbComprobante);
					if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
						tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
						return;						
				}
			}
			boolean reasonCode62 = false;
			if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_FACTURA)){
				//log.info(tipoDocumentoModifica);
				if((eDocumento.getListTypeAllowanceCharge()!= null)) { 
					for(TypeAllowanceCharge typeAllowanceCharge : eDocumento.getListTypeAllowanceCharge()) {		
						if((typeAllowanceCharge.getAllowanceChargeReasonCode()!= null) || (!typeAllowanceCharge.getAllowanceChargeReasonCode().isEmpty())) { 		
							if(typeAllowanceCharge.getAllowanceChargeReasonCode().equals(Constantes.SUNAT_Indicador_62))
								reasonCode62 = true;
						}
					}
				}
			}
			if(reasonCode62) {
				SunatPadronContribuyenteDAOLocal tsPadronContribuyenteDAOLocal = 
						(SunatPadronContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatPadronContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatPadronContribuyenteDAOLocal");
				tsPadronContribuyenteDAOLocal.getPadronContribuyenteCustomer(tbComprobante, eDocumento.getNumeroDocumentoAdquiriente()); 
				if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
						tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
					return;	
			}
			if((eDocumento.getAccountingSupplierParty()!= null)) { 				
				if((eDocumento.getAccountingSupplierParty().getParty()!= null)) { 
					for(TypePartyLegalEntity typePartyLegalEntity : eDocumento.getAccountingSupplierParty().getParty().getPartyLegalEntity()) {						
						if((typePartyLegalEntity.getRegistrationAddress()!= null)) { 
							String codEstab = typePartyLegalEntity.getRegistrationAddress().getAddressTypeCode();
							if((codEstab!=null) && (!codEstab.equals(Constantes.SUNAT_Indicador_0000))) {							
								tsEstablecimientosAnexosDAOLocal.getEstablecimientosAnexos(tbComprobante, codEstab, tipoDocumentoModifica);
								if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
										tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
									return;	
							}
						}						
					}
					if(reasonCode62) {
						SunatPadronContribuyenteDAOLocal tsPadronContribuyenteDAOLocal = 
								(SunatPadronContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatPadronContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatPadronContribuyenteDAOLocal");
						tsPadronContribuyenteDAOLocal.getPadronContribuyenteSupplier(tbComprobante, eDocumento.getNumeroDocumentoEmisor()); 
						if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
								tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
							return;
					}
				}
			}
			Long documentoAdquiriente = 0L;			
			if(StringUtil.hasString(eDocumento.getNumeroDocumentoAdquiriente()))
				documentoAdquiriente = Long.parseLong(eDocumento.getNumeroDocumentoAdquiriente());
			
			if(eDocumento.getTipoDocumentoAdquiriente()!= null && eDocumento.getTipoDocumentoAdquiriente().equals(Constantes.SUNAT_TDI_RUC)){
				tsContribuyenteDAOLocal.getBFAdquiriente(tbComprobante, documentoAdquiriente);
				if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
						tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
					return;									
			}
									
			//log.info("eDocumento.getListTypeAdditionalDocumentReference().size() --> "+eDocumento.getListTypeAdditionalDocumentReference().size());			
			for(TypeDocumentReference typeDocumentReference : eDocumento.getListTypeAdditionalDocumentReference()) {
				if(!(typeDocumentReference.getDocumentStatusCode()!= null)) 
					return;
				//log.info("typeDocumentReference.getDocumentStatusCode() --> "+typeDocumentReference.getDocumentStatusCode());
				if(!(typeDocumentReference.getIssuerParty()!= null) ||
						!(typeDocumentReference.getIssuerParty().getPartyIdentification()!= null)) 
					return;
				//log.info("typeDocumentReference.getIssuerParty().getPartyIdentification().size() --> "+typeDocumentReference.getIssuerParty().getPartyIdentification().size());
				for(TypePartyIdentification typePartyIdentification : typeDocumentReference.getIssuerParty().getPartyIdentification()) {
					String numeroDocumentoEmisor = typePartyIdentification.getId();
					Long documentoEmisorAnticipo = 0L;			
					if(StringUtil.hasString(numeroDocumentoEmisor))
						documentoEmisorAnticipo = Long.parseLong(numeroDocumentoEmisor);
					
					tsContribuyenteDAOLocal.getBFEmisorAnticipo(tbComprobante, documentoEmisorAnticipo);
					if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
							tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
						return;
					
					if(tbComprobante.getRucEmisor() == documentoEmisorAnticipo){							
						String serieDocumentoAnticipo = typeDocumentReference.getId().substring(0,1);
						//log.info("typeDocumentReference.getId() --> "+typeDocumentReference.getId()+" -  "+serieDocumentoAnticipo);
						if((serieDocumentoAnticipo.equals(Constantes.SUNAT_SERIE_B) || 
							serieDocumentoAnticipo.equals(Constantes.SUNAT_SERIE_F) ||
							serieDocumentoAnticipo.equals(Constantes.SUNAT_SERIE_E))) {
											
							tsComprobantesPagoElectronicosDAOLocal.getCompobantePagoAnticipoBFE(tbComprobante, 
									documentoEmisorAnticipo, typeDocumentReference.getDocumentTypeCode(),
									typeDocumentReference.getId().substring(0));
							if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
									tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
								return;														
						}
						if(StringUtil.hasString(serieDocumentoAnticipo)){
		    				SunatAutorizacionComprobPagoFisicoDAOLocal tsAutorizacionComprobPagoFisicoDAOLocal = 
		    						(SunatAutorizacionComprobPagoFisicoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatAutorizacionComprobPagoFisicoDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatAutorizacionComprobPagoFisicoDAOLocal");
		    				tsAutorizacionComprobPagoFisicoDAOLocal.getAutorizacionComprobPagoFisico_Anticipo(tbComprobante, 
									documentoEmisorAnticipo, typeDocumentReference.getDocumentTypeCode(),
									typeDocumentReference.getId().substring(0));									
						}
					}
				}								
			}
			//log.info("TipoComprobante() {} | TipoOperacionFactura(): {}",tbComprobante.getTipoComprobante(),eDocumento.getTipoOperacionFactura());
			if((tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_FACTURA)) ||
					(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_BOLETA))){
				if((eDocumento.getTipoOperacionFactura().equals(Constantes.SUNAT_TIPO_OPERACION_2100)) ||
						(eDocumento.getTipoOperacionFactura().equals(Constantes.SUNAT_TIPO_OPERACION_2101)) ||
						(eDocumento.getTipoOperacionFactura().equals(Constantes.SUNAT_TIPO_OPERACION_2102)) ||
						(eDocumento.getTipoOperacionFactura().equals(Constantes.SUNAT_TIPO_OPERACION_2103)) ||
						(eDocumento.getTipoOperacionFactura().equals(Constantes.SUNAT_TIPO_OPERACION_2104)) ||
						(eDocumento.getTipoOperacionFactura().equals(Constantes.SUNAT_TIPO_OPERACION_0112))) {
					SunatPadronContribuyenteDAOLocal tsPadronContribuyenteDAOLocal = 
						(SunatPadronContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatPadronContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatPadronContribuyenteDAOLocal");
					tsPadronContribuyenteDAOLocal.getPadronContribuyente_FBNCND(tbComprobante);			
	    			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
						tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
	    				return;		 
				}
			}
			//log.info("TipoComprobante() {} | TipoOperacionFactura(): {}",tbComprobante.getTipoComprobante(),eDocumento.getTipoOperacionFactura());
			if((tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_FACTURA))){
				if((eDocumento.getTipoOperacionFactura().equals(Constantes.SUNAT_TIPO_OPERACION_2106))) {
					SunatPadronContribuyenteDAOLocal tsPadronContribuyenteDAOLocal = 
						(SunatPadronContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatPadronContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatPadronContribuyenteDAOLocal");
					tsPadronContribuyenteDAOLocal.getPadronContribuyente_F(tbComprobante);			
	    			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
						tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
	    				return;		 
				}
			}
			
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosBoleta Exception \n"+errors);
		}
	}
    
    public void reglasValidarDatosNotasDebito(Documento tbComprobante, String tipoDocRefPri, 
    		String nroDocRefPri, String codMoneda, Date fechaEmision, String codigoTipoNota) {
    	//log.info("reglasValidarDatosNotaDebito --> "+tbComprobante.getTipoComprobante()+" "+nroDocRefPri);
    	try {
    		String serie = nroDocRefPri.substring(0,1);
    		if(serie.equals(Constantes.SUNAT_SERIE_F) || serie.equals(Constantes.SUNAT_SERIE_B) || 
    				serie.equals(Constantes.SUNAT_SERIE_E)) {
    			ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
    			tsComprobantesPagoElectronicosDAOLocal.getCompobantePagoNotaDebito(tbComprobante, 
    					tipoDocRefPri, nroDocRefPri, codMoneda, codigoTipoNota);			
    			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
    				return;	
    			
    			if(!codigoTipoNota.equals(Constantes.SUNAT_Indicador_03)) {
    				tsComprobantesPagoElectronicosDAOLocal.getCompobantePagoNCND(tbComprobante, 
    						tipoDocRefPri, nroDocRefPri, fechaEmision);
        			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
        					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
            				return;	
    			}   			
    		}
    		
    		if(tipoDocRefPri.equals(Constantes.SUNAT_FACTURA) || tipoDocRefPri.equals(Constantes.SUNAT_BOLETA)){
    			if(StringUtil.hasString(serie)) {
    				SunatAutorizacionComprobPagoFisicoDAOLocal tsAutorizacionComprobPagoFisicoDAOLocal = 
    						(SunatAutorizacionComprobPagoFisicoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatAutorizacionComprobPagoFisicoDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatAutorizacionComprobPagoFisicoDAOLocal");
    				tsAutorizacionComprobPagoFisicoDAOLocal.getAutorizacionComprobPagoFisico(tbComprobante, tipoDocRefPri, nroDocRefPri);			
    				if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
    						tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
    					return;		
    			}			   			    			
    		}
			if((tipoDocRefPri.equals(Constantes.SUNAT_Indicador_13)) && (serie.equals(Constantes.SUNAT_SERIE_S))) {
				SunatPadronContribuyenteDAOLocal tsPadronContribuyenteDAOLocal = 
					(SunatPadronContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatPadronContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatPadronContribuyenteDAOLocal");
				tsPadronContribuyenteDAOLocal.getPadronContribuyente_FBNCND(tbComprobante);			
    			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
    				return;		 
			}
			
			
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosNotasDebito Exception \n"+errors);
		}
    }
    
    public void reglasValidarDatosNotasCredito(Documento tbComprobante, String tipoDocRefPri, 
    		String nroDocRefPri, String codMoneda, Date fechaEmision, 
    		String codigoTipoNota, List<Date> listDateCuota) {
    	try {   		
    		log.info("reglasValidarDatosNotasCredito: {} | {}-{}",tbComprobante.toString(), tipoDocRefPri, nroDocRefPri);
    		String serie = "";
    		if((nroDocRefPri != null) && (!nroDocRefPri.isEmpty()))
    			serie = nroDocRefPri.substring(0,1);  
			if((tipoDocRefPri.equals(Constantes.SUNAT_FACTURA)) || (tipoDocRefPri.equals(Constantes.SUNAT_BOLETA)) ||
					(tipoDocRefPri.equals(Constantes.SUNAT_ROL_ADQUIRIENTE_SISTEMAS_PAGO)) || (tipoDocRefPri.equals(Constantes.SUNAT_OPERADOR)) || 
					(tipoDocRefPri.equals(Constantes.SUNAT_ADQUIRIENTE_SISTEMAS_PAGO))){	    		
	    		if(serie.equals(Constantes.SUNAT_SERIE_F) || serie.equals(Constantes.SUNAT_SERIE_B) || 
	    				serie.equals(Constantes.SUNAT_SERIE_E)) {    			
	    			ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
						(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
	    			
	    			tsComprobantesPagoElectronicosDAOLocal.getCompobantePagoNotaCredito(tbComprobante, 
	    					tipoDocRefPri, nroDocRefPri, codMoneda, codigoTipoNota, listDateCuota);			
	    			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
						tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
	    				return;	
	    			
	    			if(!codigoTipoNota.equals(Constantes.SUNAT_Indicador_10)) {
	    				tsComprobantesPagoElectronicosDAOLocal.getCompobantePagoNCND(tbComprobante, 
	    						tipoDocRefPri, nroDocRefPri, fechaEmision);
	        			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
	        					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
	            				return;	
	    			}   
	    			if((tipoDocRefPri.equals(Constantes.SUNAT_FACTURA)) && 
	    					(codigoTipoNota.equals(Constantes.SUNAT_Indicador_13))) {
	    				tsComprobantesPagoElectronicosDAOLocal.getCompobantePagoNCDocRefPri(tbComprobante, 
	    						tipoDocRefPri, nroDocRefPri);
	        			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
	        					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
	            				return;	
	    			}   	    			
	    		}	    		
			}
			if((tipoDocRefPri.equals(Constantes.SUNAT_FACTURA)) || (tipoDocRefPri.equals(Constantes.SUNAT_BOLETA))) {
	    		if(StringUtil.hasString(serie)) {
	    			SunatAutorizacionComprobPagoFisicoDAOLocal tsAutorizacionComprobPagoFisicoDAOLocal = 
						(SunatAutorizacionComprobPagoFisicoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatAutorizacionComprobPagoFisicoDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatAutorizacionComprobPagoFisicoDAOLocal");
	    			tsAutorizacionComprobPagoFisicoDAOLocal.getAutorizacionComprobPagoFisico(tbComprobante, tipoDocRefPri, nroDocRefPri);			
	    			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
						tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
	    				return;		
	    		}   
			}
			if((tipoDocRefPri.equals(Constantes.SUNAT_Indicador_13)) && (serie.equals(Constantes.SUNAT_SERIE_S))) {
				SunatPadronContribuyenteDAOLocal tsPadronContribuyenteDAOLocal = 
					(SunatPadronContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatPadronContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatPadronContribuyenteDAOLocal");
				tsPadronContribuyenteDAOLocal.getPadronContribuyente_FBNCND(tbComprobante);			
    			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
    				return;		 
			}
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosNotasCredito Exception \n"+errors);
		}
    }	
    
    public void reglasValidarDatosDAEAdquiriente(Documento tbComprobante, EDocumento eDocumento) {
    	//log.info("reglasValidarDatosDAEAdquiriente --> "+tbComprobante.getTipoComprobante());
		try {						
			ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
			tsComprobantesPagoElectronicosDAOLocal.getCompobantePago(tbComprobante);			
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;	
			
			SunatContribuyenteDAOLocal tsContribuyenteDAOLocal = 
					(SunatContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatContribuyenteDAOLocal");
			tsContribuyenteDAOLocal.getContribuyente(tbComprobante);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;		
			
			Long documentoAdquiriente = 0L;			
			if(StringUtil.hasString(eDocumento.getNumeroDocumentoAdquiriente()))
				documentoAdquiriente = Long.parseLong(eDocumento.getNumeroDocumentoAdquiriente());
			
			if(eDocumento.getTipoDocumentoAdquiriente()!= null && eDocumento.getTipoDocumentoAdquiriente().equals(Constantes.SUNAT_TDI_RUC)){
				tsContribuyenteDAOLocal.getBFAdquiriente(tbComprobante, documentoAdquiriente);
				if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
						tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
					return;									
			}			
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosBoleta Exception \n"+errors);
		}
	}   
    
    public void reglasValidarDatosDAEOperador(Documento tbComprobante, EDocumento eDocumento) {
    	log.info("reglasValidarDatosDAEOperador --> "+tbComprobante.getTipoDocumento());
		try {									 							
			this.reglasValidarDatosDAEAdquiriente(tbComprobante, eDocumento);	
				
			SunatContribuyenteDAOLocal tsContribuyenteDAOLocal = 
					(SunatContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatContribuyenteDAOLocal");
			
			for(EDocumentoItem item : eDocumento.getItems()){	
				if(item.getOriginatorParty()!=null) {
					TypeParty originatorParty = item.getOriginatorParty();
					for(TypePartyIdentification typePartyIdentification : originatorParty.getPartyIdentification()){	
						if((typePartyIdentification.getSchemeID()!=null) && 
								typePartyIdentification.getSchemeID().equals(Constantes.SUNAT_TDI_RUC)) {
							if(typePartyIdentification.getId()!=null) {							
								tsContribuyenteDAOLocal.getDAEParticipe(tbComprobante, new Long(typePartyIdentification.getId()));						
								if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
										tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
									return;	
							}
						}
					}
				}
			}
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosBoleta Exception \n"+errors);
		}
	}       
    
    public void reglasValidarDatosRetencion(Documento tbComprobante, Long numeroDocumentoProveedor) {
    	try {
			this.reglasValidarDatos_RP(tbComprobante);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;							
    		
			SunatContribuyenteDAOLocal tsContribuyenteDAOLocal = 
					(SunatContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatContribuyenteDAOLocal");
			tsContribuyenteDAOLocal.getRetencionProveedor(tbComprobante, numeroDocumentoProveedor);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;	
			
			SunatPadronContribuyenteDAOLocal tsPadronContribuyenteDAOLocal = 
					(SunatPadronContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatPadronContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatPadronContribuyenteDAOLocal");
			tsPadronContribuyenteDAOLocal.getPadronContribuyente_Proveedor(tbComprobante, numeroDocumentoProveedor);												
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosRetencion Exception \n"+errors);
		}
    }	
    
    public void reglasValidarDatosPercepcion(Documento tbComprobante, Long documentoCliente, String tipoDocumentoCliente) {
    	try {
			reglasValidarDatos_RP(tbComprobante);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;							
			
			//log.info("tipoDocumentoCliente "+tipoDocumentoCliente+" documentoCliente "+ documentoCliente);
			if(tipoDocumentoCliente!=null && tipoDocumentoCliente.equals(Constantes.SUNAT_TDI_RUC) ) {
				SunatContribuyenteDAOLocal tsContribuyenteDAOLocal = 
					(SunatContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatContribuyenteDAOLocal");
				tsContribuyenteDAOLocal.getPercepcionCliente(tbComprobante, documentoCliente);
				if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
					return;	
				SunatPadronContribuyenteDAOLocal tsPadronContribuyenteDAOLocal = 
						(SunatPadronContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatPadronContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatPadronContribuyenteDAOLocal");
				tsPadronContribuyenteDAOLocal.getPadronContribuyente_Cliente(tbComprobante, documentoCliente);
				tsPadronContribuyenteDAOLocal.getPadronContribuyente_Cliente_Percepcion(tbComprobante, documentoCliente);																
			}						
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosPercepcion Exception \n"+errors);
		}
    }
    
    private void reglasValidarDatos_RP(Documento tbComprobante) {
    	try {
    		ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");

			tsComprobantesPagoElectronicosDAOLocal.getCompobantePago_RP(tbComprobante);			
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;	
    		
			if(StringUtil.hasString(tbComprobante.getSerie())) {
				SunatAutorizacionRangosContingenciaDAOLocal sunatAutorizacionRangosContingenciaDAOLocal = 
					(SunatAutorizacionRangosContingenciaDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatAutorizacionRangosContingenciaDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatAutorizacionRangosContingenciaDAOLocal");			
				sunatAutorizacionRangosContingenciaDAOLocal.getAutorizacionRangosContingencia_RP(tbComprobante);				
				
    			SunatAutorizacionComprobPagoFisicoDAOLocal sunatAutorizacionComprobPagoFisicoDAOLocal = 
					(SunatAutorizacionComprobPagoFisicoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatAutorizacionComprobPagoFisicoDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatAutorizacionComprobPagoFisicoDAOLocal");			
    			sunatAutorizacionComprobPagoFisicoDAOLocal.getAutorizacionComprobPagoFisico_RP(tbComprobante);
    		}			
			
			SunatContribuyenteDAOLocal tsContribuyenteDAOLocal = 
					(SunatContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatContribuyenteDAOLocal");
			tsContribuyenteDAOLocal.getContribuyente_RP(tbComprobante);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;		
			
			SunatPadronContribuyenteDAOLocal tsPadronContribuyenteDAOLocal = 
					(SunatPadronContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatPadronContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatPadronContribuyenteDAOLocal");
			tsPadronContribuyenteDAOLocal.getPadronContribuyente_RP(tbComprobante);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;					
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatos_RP Exception \n"+errors);
		}
    }	
    
    public void reglasValidarDatosRetencionItem(Documento tbComprobante, ERetencionDocumentoItem rdi, Long numRUC) {
    	try {
    		String [] docRefPri = rdi.getNumeroDocumentoRelacionado().split("-");    		
    		String serie = docRefPri[0].substring(0,1);    		
    		if(StringUtil.hasString(serie)) {
    			SunatAutorizacionComprobPagoFisicoDAOLocal tsAutorizacionComprobPagoFisicoDAOLocal = 
					(SunatAutorizacionComprobPagoFisicoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatAutorizacionComprobPagoFisicoDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatAutorizacionComprobPagoFisicoDAOLocal");			
    			tsAutorizacionComprobPagoFisicoDAOLocal.getAutorizacionComprobPagoFisico_RP(tbComprobante, 
					rdi.getTipoDocumentoRelacionado(), rdi.getNumeroDocumentoRelacionado(), numRUC);
    		}	
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosRetencionItem Exception \n"+errors);
		}
    }	
    
    public void reglasValidarDatosPercepcionItem(Documento tbComprobante, EPercepcionDocumentoItem rdi,
    		Long documentoCliente, String tipoDocumentoCliente, String indicadorEmisiónExcepcional) {
    	log.info("reglasValidarDatosPercepcionItem: {} | {}-{}",tbComprobante.toString(),
    			rdi.getTipoDocumentoRelacionado(), rdi.getNumeroDocumentoRelacionado());
    	try {
    		String [] docRefPri = rdi.getNumeroDocumentoRelacionado().split("-");    		
    		String serie = docRefPri[0].substring(0,1);
       		ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
    					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
    		//if(docRefPri[0].equals(OseConstantes.SUNAT_SERIE_E001) || docRefPri[0].equals(OseConstantes.SUNAT_SERIE_EB01))
    			tsComprobantesPagoElectronicosDAOLocal.getCompobantePagoPercepcionItem(tbComprobante, rdi, indicadorEmisiónExcepcional);
    		if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;
    		
    		if(StringUtil.hasString(serie) && tipoDocumentoCliente.equals(Constantes.SUNAT_TDI_RUC)) {			
    			SunatAutorizacionComprobPagoFisicoDAOLocal tsAutorizacionComprobPagoFisicoDAOLocal = 
					(SunatAutorizacionComprobPagoFisicoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatAutorizacionComprobPagoFisicoDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatAutorizacionComprobPagoFisicoDAOLocal");
    			tsAutorizacionComprobPagoFisicoDAOLocal.getAutorizacionComprobPagoFisico_RP(tbComprobante, 
					rdi.getTipoDocumentoRelacionado(), rdi.getNumeroDocumentoRelacionado(), documentoCliente);    		
    		}    		
    		
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosPercepcionItem Exception \n"+errors);
		}
    }
    
    public void reglasValidarDatosGuia(Documento tbComprobante, EGuiaDocumento eDocumento) {
    	try {    
      		ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
      		tsComprobantesPagoElectronicosDAOLocal.getCompobantePagoGuia(tbComprobante, eDocumento.getTipoDocumento(), eDocumento.getSerieNumeroGuia());
      		if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;
    		
			SunatContribuyenteDAOLocal tsContribuyenteDAOLocal = 
					(SunatContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatContribuyenteDAOLocal");
			Long documentoRemitente = 0L;			
			if(StringUtil.hasString(eDocumento.getNumeroDocumentoRemitente()))
				documentoRemitente = Long.parseLong(eDocumento.getNumeroDocumentoRemitente());	
			tsContribuyenteDAOLocal.getGuiaRemitente(tbComprobante, documentoRemitente);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;		
			
			Long documentoProveedor = 0L;			
			if(StringUtil.hasString(eDocumento.getNumeroDocumentoEstablecimiento()))
				documentoProveedor = Long.parseLong(eDocumento.getNumeroDocumentoEstablecimiento());	
			if(documentoProveedor>0) {
				tsContribuyenteDAOLocal.getGuiaProveedor(tbComprobante, documentoProveedor);
				if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
						tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
					return;	
			}
    		
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosGuia Exception \n"+errors);
		}
    }
    
    public void reglasValidarDatosComunicaBajas(Documento tbComprobante, EResumenDocumento eDocumento, Date fechaRecep) {
    	try {
			SunatContribuyenteDAOLocal tsContribuyenteDAOLocal = 
					(SunatContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatContribuyenteDAOLocal");
			tsContribuyenteDAOLocal.getContribuyenteComunicaBajas(tbComprobante);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
					tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;		
			
      		ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
      		for(EResumenDocumentoItem rdi : eDocumento.getItems()) 
      			tsComprobantesPagoElectronicosDAOLocal.getCompobantePagoComunicaBajasItem(tbComprobante, rdi, 
      					eDocumento.getFechaEmisionComprobante(), fechaRecep);
      		if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
				tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
      		return;        		
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosResumen Exception \n"+errors);
		}
    }      
    
    public void reglasValidarDatosResumenDiario(Documento tbComprobante, EResumenDocumento eDocumento, Date fechaRecep) {
    	log.info("reglasValidarDatosResumenDiario");
    	try {    		 		
      		ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
      		int i=eDocumento.getItems().size(); 
  			if(i>500) {
  				tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0158);
				return;
  			}     		
      		
      		for(EResumenDocumentoItem rdi : eDocumento.getItems()) {
      			try {
      				tsComprobantesPagoElectronicosDAOLocal.getCompobantePagoResumenDiarioItem(tbComprobante, 
      					rdi, fechaRecep);
      			} catch (Exception e) {
      				StringWriter errors = new StringWriter();				
      				e.printStackTrace(new PrintWriter(errors));
      				log.error("reglasValidarDatosResumenDiario getCompobantePagoResumenDiarioItem Exception \n"+errors);
      			}
      		}     		
      		
  			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
				tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))) {
  				log.info("a) Error: {} | CPE: {} ",tbComprobante.getErrorNumero(),tbComprobante.getErrorLog());
  				return;
  			}
  			
			SunatAutorizacionRangosContingenciaDAOLocal tsAutorizacionRangosContingenciaDAOLocal = 
					(SunatAutorizacionRangosContingenciaDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatAutorizacionRangosContingenciaDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatAutorizacionRangosContingenciaDAOLocal");			  			
      		for(EResumenDocumentoItem rdi : eDocumento.getItems()) {
      			if(rdi.getTipoDocumento()!=null && (rdi.getTipoDocumento().equals(Constantes.SUNAT_BOLETA) ||
      					rdi.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO) || rdi.getTipoDocumento().equals(Constantes.SUNAT_NOTA_DEBITO))) {
      				if(StringUtil.hasString(rdi.getId().substring(0,1))) 
      					tsAutorizacionRangosContingenciaDAOLocal.getAutorizacionRangosContingencia_RC(tbComprobante, rdi);
      			}
      		}	
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
				tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))){
  				log.info("b) Error: {} | CPE: {} ",tbComprobante.getErrorNumero(),tbComprobante.getErrorLog());
  				return;
  			}
			
			SunatAutorizacionComprobPagoFisicoDAOLocal tsAutorizacionComprobPagoFisicoDAOLocal = 
					(SunatAutorizacionComprobPagoFisicoDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatAutorizacionComprobPagoFisicoDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatAutorizacionComprobPagoFisicoDAOLocal");			  			
      		for(EResumenDocumentoItem rdi : eDocumento.getItems()) {
      			if(rdi.getTipoDocumento()!=null && (rdi.getTipoDocumento().equals(Constantes.SUNAT_BOLETA) ||
      					rdi.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO) || rdi.getTipoDocumento().equals(Constantes.SUNAT_NOTA_DEBITO))) {
      				if(StringUtil.hasString(rdi.getId().substring(0,1))) 
      					tsAutorizacionComprobPagoFisicoDAOLocal.getAutorizacionComprobPagoFisico_RC(tbComprobante, rdi);
      			}
      		}	
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
				tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))){
  				log.info("c) Error: {} | CPE: {} ",tbComprobante.getErrorNumero(),tbComprobante.getErrorLog());
  				return;
  			}			
      		
      		for(EResumenDocumentoItem rdi : eDocumento.getItems()) {	
      			if(rdi.getBrDocumentTypeCode()!=null && rdi.getBrDocumentTypeCode().equals(Constantes.SUNAT_BOLETA)) {
      				if(StringUtil.hasString(rdi.getBrID().substring(0,1)))
      	      			tsAutorizacionComprobPagoFisicoDAOLocal.getAutorizacionComprobPagoFisico_RC_BR(tbComprobante, 
      	  					rdi.getBrDocumentTypeCode(), rdi.getBrID(), tbComprobante.getRucEmisor());      				
      			}
      		}
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
				tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))){
  				log.info("d) Error: {} | CPE: {} ",tbComprobante.getErrorNumero(),tbComprobante.getErrorLog());
  				return;
  			} 
			
      		for(EResumenDocumentoItem rdi : eDocumento.getItems()) {	
      			if(rdi.getBrDocumentTypeCode()!=null && rdi.getBrDocumentTypeCode().equals(Constantes.SUNAT_BOLETA)) {
      				if(rdi.getBrID().substring(0,1).equals(Constantes.SUNAT_SERIE_B)) {
      					EResumenDocumentoItem brItem = eDocumento.getItems().stream()
    	      					.filter(x ->  x.getDocumentTypeCode().equals(rdi.getBrDocumentTypeCode())
    	      						&& x.getId().equals(rdi.getBrID()))
    	      					.findAny().orElse(null);
    	      			if(brItem != null)
    	      				continue;
      	      			tsComprobantesPagoElectronicosDAOLocal.getCompobantePagoResumenDiarioItem_BR(tbComprobante, rdi);
      				}
      			}
      		}
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
				tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))){
  				log.info("e) Error: {} | CPE: {} ",tbComprobante.getErrorNumero(),tbComprobante.getErrorLog());
  				return;
  			}
			
      		for(EResumenDocumentoItem rdi : eDocumento.getItems()) {     
      			if(rdi.getAcpAdditionalAccountID() == null)
      				continue;
      			if(rdi.getSunatPerceptionSummaryDocumentReference()==true && 
      					rdi.getAcpAdditionalAccountID().equals(Constantes.SUNAT_TDI_RUC)) {
      				Long documentoCliente = 0L;			
      				if(StringUtil.hasString(rdi.getAcpCustomerAssignedAccountID()))
      					documentoCliente = Long.parseLong(rdi.getAcpCustomerAssignedAccountID());      				
      				SunatContribuyenteDAOLocal tsContribuyenteDAOLocal = 
      						(SunatContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatContribuyenteDAOLocal");
      				tsContribuyenteDAOLocal.getPercepcionCliente(tbComprobante, documentoCliente);
     					
      				SunatPadronContribuyenteDAOLocal tsPadronContribuyenteDAOLocal = 
      						(SunatPadronContribuyenteDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatPadronContribuyenteDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatPadronContribuyenteDAOLocal");     				
      				tsPadronContribuyenteDAOLocal.getPadronContribuyente_RC_Cliente(tbComprobante, documentoCliente);     				
      			}
      		}   
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
				tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))){
  				log.info("f) Error: {} | CPE: {} ",tbComprobante.getErrorNumero(),tbComprobante.getErrorLog());
  				return;
  			}	
      		
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosResumenDiario Exception \n"+errors);
		}
    }    
    
    public void reglasValidarDatosReversion(Documento tbComprobante, EReversionDocumento eDocumento) {
    	try {
      		ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
      		for(EReversionDocumentoItem rdi : eDocumento.getItems())
      			tsComprobantesPagoElectronicosDAOLocal.getCompobantePagoReversionItem(tbComprobante, rdi);
  			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))|| 
				tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
  			return;
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosReversion Exception \n"+errors);
		}
    }    
    
    public void grabaTbComprobantesPagoElectronicos(Documento tbComprobante, Date fechaEmisionComprobante, 
    		BigDecimal mtoImporteCpe, Short indEstCPE, String moneda,Short Ind_percepcion, Short ind_for_pag) {
    	try {
    		ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
				(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
    		
    		tsComprobantesPagoElectronicosDAOLocal.grabaTbComprobantesPagoElectronicos(tbComprobante, 
    				fechaEmisionComprobante, mtoImporteCpe, indEstCPE, moneda, Ind_percepcion, ind_for_pag );
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosReversion Exception \n"+errors);
		}
    }
    
    public void modificarEstadoComprobantesPagoElectronicos_RA(Documento tbComprobante, 
    		EResumenDocumento eDocumento, Short indEstCPE) {
    	try {
    		for(EResumenDocumentoItem rdi : eDocumento.getItems()) {
    			ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
    					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
    			tsComprobantesPagoElectronicosDAOLocal.modificarEstadoComprobantesPagoElectronicos_RA(tbComprobante, 
    					rdi, indEstCPE);
    		}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosReversion Exception \n"+errors);
		}
    }
    
    public void modificarEstadoComprobantesPagoElectronicos_RR(Documento tbComprobante, 
    		EReversionDocumento eDocumento, Short indEstCPE) {
    	try {
    		for(EReversionDocumentoItem rdi : eDocumento.getItems()) {
    			ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
    					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
    			tsComprobantesPagoElectronicosDAOLocal.modificarEstadoComprobantesPagoElectronicos_RR(tbComprobante, 
    					rdi, indEstCPE);
    		}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosReversion Exception \n"+errors);
		}
    }
    
    public void grabaTbComprobantesPagoElectronicos_RC(Documento tbComprobante, EResumenDocumento eDocumento) {
    	try {
    		ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
				(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
    		for(EResumenDocumentoItem rdi : eDocumento.getItems()) {
				tsComprobantesPagoElectronicosDAOLocal.grabaTbComprobantesPagoElectronicos_RC(tbComprobante, 
						eDocumento.getFechaEmisionComprobante(), rdi);
    		}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosReversion Exception \n"+errors);
		}
    }
    
    public void revisarRespuesta2RARCSunatList(Documento tbComprobante, EResumenDocumento eDocumento) {
    	//log.info("revisarRespuesta2RARCSunatList "+tbComprobante.getTipoComprobante());
    	try {
      		ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
      		tbComprobante.setLongitudNombre(0);
      		tbComprobante.setDireccion("0"); 
      		String fechaEmision = DateUtil.dateFormat(eDocumento.getFechaEmisionComprobante(),"dd/MM/yyyy");
      		SunatTokenResponse sunatToken = ConsultaIntegradaSunatRest.buscaTokenCpe();
      		log.info("getExpires_in "+sunatToken.getExpires_in());
      		for(EResumenDocumentoItem rdi : eDocumento.getItems()) {
				if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO))	
      				tsComprobantesPagoElectronicosDAOLocal.revisarRespuesta2RCItemSunatList(tbComprobante, rdi, fechaEmision, sunatToken);
      			if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS))
      				tsComprobantesPagoElectronicosDAOLocal.revisarRespuesta2RAItemSunatList(tbComprobante, rdi);
      		}      		      		
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("revisarRespuesta2RARCSunatList Exception \n"+errors);
		}
    }
    
    public void revisarRespuesta2RRSunatList(Documento tbComprobante, EReversionDocumento eDocumento) {
    	log.info("revisarRespuesta2RRSunatList "+tbComprobante.getTipoDocumento());
    	try {
      		ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");      		
      		tbComprobante.setLongitudNombre(0);
      		for(EReversionDocumentoItem rdi : eDocumento.getItems()) {
      			tsComprobantesPagoElectronicosDAOLocal.revisarRespuesta2RRItemSunatList(tbComprobante, rdi);
      		}      		      		
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("revisarRespuesta2RRSunatList Exception \n"+errors);
		}
    }    
 
    public void revisarRespuesta2NCNDSunatList(Documento tbComprobante, EDocumento eDocumento) {
    	log.info("revisarRespuesta2NCNDSunatList "+tbComprobante.getNombre());
    	try {
      		ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");      		
      		tbComprobante.setLongitudNombre(0);
			for(TypeBillingReference tbr : eDocumento.getBillingReference()){	
				String tipoDocumento = "";
				if(tbr.getInvoiceDocumentReference().getDocumentTypeCode()!=null) 					
					tipoDocumento = tbr.getInvoiceDocumentReference().getDocumentTypeCode();
				else
					continue;			
				tsComprobantesPagoElectronicosDAOLocal.revisarRespuesta2NCNDItemSunatList(tbComprobante, 
						tipoDocumento, tbr.getInvoiceDocumentReference().getId());
			}	     		      		
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("revisarRespuesta2RRSunatList Exception \n"+errors);
		}
    }        
    
    public boolean getDBRARCReview(Documento tbComprobante, EResumenDocumento eDocumento) {
    	log.info("getDBRARCReview "+tbComprobante.getTipoDocumento());
    	try {
      		ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
      		String fechaEmision = DateUtil.dateFormat(eDocumento.getFechaEmisionComprobante(),"dd/MM/yyyy");
      		for(EResumenDocumentoItem rdi : eDocumento.getItems()) {
				if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO)) {	
		    		if(rdi.getStatus()==Constantes.SUNAT_IndEstCpe_Acept)
		    			continue;
		    		String monto = String.valueOf(rdi.getTotalVenta());
		    		if(tsComprobantesPagoElectronicosDAOLocal.getDBCpe2TbComprobanteID(tbComprobante,
		    				tbComprobante.getRucEmisor(), rdi.getDocumentTypeCode(), rdi.getId(), fechaEmision, monto))
		    			return true;
		    		else
		    			return false;
				}
      			if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS)) {
      				String serie = rdi.getSerieDocumentoBaja()+"-"+rdi.getNumeroDocumentoBaja();
      				if(tsComprobantesPagoElectronicosDAOLocal.getDBCpe2TbComprobanteID(tbComprobante, 
      						tbComprobante.getRucEmisor(), rdi.getTipoDocumento(), serie, "", ""))
      					return true;
		    		else
		    			return false;
      			}
      		}      		      		
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDBRARCReview Exception \n"+errors);
		}
    	return true;
    }    
    
    public boolean getDBRRReview(Documento tbComprobante, EReversionDocumento eDocumento) {
    	log.info("getDBRRReview "+tbComprobante.getTipoDocumento());
    	try {
      		ComprobantesPagoElectronicosDAOLocal tsComprobantesPagoElectronicosDAOLocal = 
					(ComprobantesPagoElectronicosDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"ComprobantesPagoElectronicosDAOImpl"+Constantes.OSE_CDEJB_DAO+"ComprobantesPagoElectronicosDAOLocal");
      		for(EReversionDocumentoItem rdi : eDocumento.getItems()) {
      			String serie = rdi.getSerieDocumentoRevertido()+"-"+rdi.getCorrelativoDocRevertido();	
  				if(!tsComprobantesPagoElectronicosDAOLocal.getDBCpe2TbComprobanteID(tbComprobante, 
  						tbComprobante.getRucEmisor(), rdi.getTipoDocumentoRevertido(), serie, "", ""))
  					return false;
      		}      		   		      		
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDBRRReview Exception \n"+errors);
		}
    	return true;
    }       
}
