package com.axteroid.ose.server.rulesejb.rules.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesejb.rules.UBLListValidateDataLocal;
import com.axteroid.ose.server.rulesejb.rules.UBLListValidateLocal;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.tools.ubltype.TypeBillingReference;
import com.axteroid.ose.server.tools.ubltype.TypePaymentTerms;
import com.axteroid.ose.server.tools.ubltype.TypeResponse;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.StringUtil;

public class UBLListValidateImpl implements UBLListValidateLocal{
	private static final Logger log = LoggerFactory.getLogger(UBLListValidateImpl.class);		
	
	public void reglasValidarDatosComprobante(Documento documento, EDocumento eDocumento) {
		log.info("reglasValidarDatosComprobante --> "+documento.getTipoDocumento());
		switch(documento.getTipoDocumento()) {
			case Constantes.SUNAT_FACTURA:
				this.reglasValidarDatosFacturaBoleta(documento, eDocumento);
				break;
			case Constantes.SUNAT_BOLETA:
				this.reglasValidarDatosFacturaBoleta(documento, eDocumento);
				break;						
			case Constantes.SUNAT_NOTA_CREDITO:
				this.reglasValidarDatosNotaCredito(documento, eDocumento);
				break;
			case Constantes.SUNAT_NOTA_DEBITO:
				this.reglasValidarDatosNotaDebito(documento, eDocumento);
				break;	
			case Constantes.SUNAT_RECIBO_SERV_PUBL:
				this.reglasValidarDatosReciboServiciosPublicos(documento, eDocumento);
				break;
			case Constantes.SUNAT_ROL_ADQUIRIENTE_SISTEMAS_PAGO:
				this.reglasValidarDatosDAEAdquiriente(documento, eDocumento);
				break;
			case Constantes.SUNAT_OPERADOR:
				this.reglasValidarDatosDAEOperador(documento, eDocumento);
				break;					
			case Constantes.SUNAT_ADQUIRIENTE_SISTEMAS_PAGO:
				this.reglasValidarDatosDAEAdquiriente(documento, eDocumento);
				break;
		}		
	}
			
	private void reglasValidarDatosGeneral(Documento documento, String numeroDocumentoEmisor, 
			Date fechaEmision, String numIdCd, Date dategetNotBefore, Date dateNotAfter) {
		//log.info("reglasValidarDatosGeneral --> "+tbComprobante.getTipoComprobante());
		try {			
			if(!(documento.getRucEmisor() == Long.parseLong(numeroDocumentoEmisor))) {
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0154);
				return;
			}
			
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");
			
			ublListValidateDataLocal.reglasValidarDatosGeneral(documento, fechaEmision, numIdCd, dategetNotBefore, dateNotAfter);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;		
						
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosGeneral Exception \n"+errors);
		}	
	}	
	
	private void reglasValidarDatosBoleta(Documento documento, EDocumento eDocumento) {
		//log.info("reglasValidarDatosBoleta --> "+documento.getTipoComprobante());
		try {			
			this.reglasValidarDatosGeneral(documento, eDocumento.getNumeroDocumentoEmisor(),
					eDocumento.getFechaEmision(), eDocumento.getNumIdCd(), eDocumento.getDategetNotBefore(), 
					eDocumento.getDateNotAfter() );
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;
						
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");
			ublListValidateDataLocal.reglasValidarDatosBoleta(documento, eDocumento);
			
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;							
			
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosBoleta Exception \n"+errors);
		}		
	}
	
	private void reglasValidarDatosFacturaBoleta(Documento documento, EDocumento eDocumento) {
		//log.info("reglasValidarDatosFacturaBoleta --> "+documento.getTipoComprobante());
		try {
			this.reglasValidarDatosBoleta(documento, eDocumento);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;					
			
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosFacturaBoleta Exception \n"+errors);
		}		
	}
	
	public void reglasValidarDatosNotaDebito(Documento documento, EDocumento eDocumento) {
		//log.info("reglasValidarDatosNotaDebito --> "+documento.getRucEmisor()+" - "+documento.getTipoComprobante());
		try {						
			this.reglasValidarDatosBoleta(documento, eDocumento);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;						
			for(TypeBillingReference tbr : eDocumento.getBillingReference()){
				String tipoDocumento = "";
				if((tbr.getInvoiceDocumentReference()!=null) && 
						(tbr.getInvoiceDocumentReference().getDocumentTypeCode()!=null)) 					
					tipoDocumento = tbr.getInvoiceDocumentReference().getDocumentTypeCode();
				else
					continue;
				if(tipoDocumento.equals(Constantes.SUNAT_FACTURA) || tipoDocumento.equals(Constantes.SUNAT_BOLETA)|| 
						tipoDocumento.equals(Constantes.SUNAT_ROL_ADQUIRIENTE_SISTEMAS_PAGO) || tipoDocumento.equals(Constantes.SUNAT_OPERADOR) || 
						tipoDocumento.equals(Constantes.SUNAT_ADQUIRIENTE_SISTEMAS_PAGO)){										
					UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
						InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");
					
					String codigoTipoNota = "";
					for(TypeResponse discrepancyResponse : eDocumento.getDiscrepancyResponse()) {
						if(discrepancyResponse.getResponseCode()!= null) {
							codigoTipoNota = discrepancyResponse.getResponseCode();
						}				
					}	
					//log.info("reglasValidarDatosNotaDebito --> "+tbComprobante.toString()+" - "+eDocumento.getTipoMoneda());
					ublListValidateDataLocal.reglasValidarDatosNotasDebito(documento, tipoDocumento, 
							tbr.getInvoiceDocumentReference().getId(), eDocumento.getTipoMoneda(), 
							eDocumento.getFechaEmision(), codigoTipoNota);

					if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
							documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
						return;	
				}
			}						
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosNotaDebito Exception \n"+errors);
		}		
	}
	
	public void reglasValidarDatosNotaCredito(Documento documento, EDocumento eDocumento) {
		//log.info("reglasValidarDatosNotaCredito --> "+documento.getRucEmisor()+" - "+documento.getTipoComprobante());
		try {
			this.reglasValidarDatosBoleta(documento,eDocumento);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;		
			if(eDocumento.getListTypeResponse()!=null) {
				if(eDocumento.getListTypeResponse().size()>0){ 
					if(eDocumento.getListTypeResponse().get(0).getResponseCode()!=null) {
						if(eDocumento.getListTypeResponse().get(0).getResponseCode().equals(Constantes.SUNAT_Indicador_10))
							return;			
					}
				}
			}
			
			for(TypeBillingReference tbr : eDocumento.getBillingReference()){	
				String tipoDocumento = "";
				if((tbr.getInvoiceDocumentReference()!=null) && 
						(tbr.getInvoiceDocumentReference().getDocumentTypeCode()!=null))
					tipoDocumento = tbr.getInvoiceDocumentReference().getDocumentTypeCode();
				else
					continue;			
				UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");
				String codigoTipoNota = "";
				for(TypeResponse discrepancyResponse : eDocumento.getDiscrepancyResponse()) {
					if(discrepancyResponse.getResponseCode()!= null) {
						codigoTipoNota = discrepancyResponse.getResponseCode();
					}				
				}	
				List<Date> listDateCuota = new ArrayList<Date>();
				if(tipoDocumento.equals(Constantes.SUNAT_FACTURA)) {
					List<TypePaymentTerms> listTypePaymentTerms = eDocumento.getListTypePaymentTerms();
					if(listTypePaymentTerms != null && listTypePaymentTerms.size()>0) {
						for(TypePaymentTerms tpt : listTypePaymentTerms) {
							if(tpt.getId().equals("FormaPago")){
								if((tpt.getPaymentDueDate()!=null) && 
										!(tpt.getPaymentDueDate().isEmpty())) {
									listDateCuota.add(DateUtil.parseDate(tpt.getPaymentDueDate(), "yyyy-MM-dd"));
								}
							}
						}
					}
				}
				ublListValidateDataLocal.reglasValidarDatosNotasCredito(documento, tipoDocumento, 
						tbr.getInvoiceDocumentReference().getId(), eDocumento.getTipoMoneda(), 
						eDocumento.getFechaEmision(), codigoTipoNota, listDateCuota);
				if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
						documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
					return;						
			}			
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosNotaCredito Exception \n"+errors);
		}		
	}
	
	public void reglasValidarDatosReciboServiciosPublicos(Documento documento, EDocumento eDocumento) {
		//log.info("reglasValidarDatosReciboServiciosPublicos --> "+documento.getTipoComprobante());
		try {
			this.reglasValidarDatosBoleta(documento, eDocumento);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;		
			
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosReciboServiciosPublicos Exception \n"+errors);
		}		
	}
	
	private void reglasValidarDatosDAEAdquiriente(Documento documento, EDocumento eDocumento) {
		//log.info("reglasValidarDatosDAEAdquiriente --> "+documento.getTipoComprobante());
		try {
			this.reglasValidarDatosGeneral(documento, eDocumento.getNumeroDocumentoEmisor(),
					eDocumento.getFechaEmision(), eDocumento.getNumIdCd(), eDocumento.getDategetNotBefore(), 
					eDocumento.getDateNotAfter() );
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;
						
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");
			ublListValidateDataLocal.reglasValidarDatosDAEAdquiriente(documento, eDocumento);
			
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;				
			
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosDAEAdquiriente Exception \n"+errors);
		}		
	}	
	
	private void reglasValidarDatosDAEOperador(Documento documento, EDocumento eDocumento) {
		//log.info("reglasValidarDatosDAEOperador --> "+documento.getTipoComprobante());
		try {
			this.reglasValidarDatosDAEAdquiriente(documento, eDocumento);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;
						
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");
			ublListValidateDataLocal.reglasValidarDatosDAEOperador(documento, eDocumento);
			
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;				
			
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosDAEOperador Exception \n"+errors);
		}		
	}		
	
	public void reglasValidarDatosComprobante_Retencion(Documento documento, ERetencionDocumento eDocumento) {
		//log.info("reglasValidarDatosComprobante_Retencion "+documento.getTipoComprobante());
		try {
			reglasValidarDatosGeneral(documento, eDocumento.getNumeroDocumentoEmisor(), 
					eDocumento.getFechaEmision(), eDocumento.getNumIdCd(), eDocumento.getDategetNotBefore(), 
					eDocumento.getDateNotAfter() );
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;

			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");	
			//log.info("eDocumento.getNumeroDocumentoProveedor() "+eDocumento.getNumeroDocumentoProveedor());
			Long nroDocProv = 0L;			
			if(StringUtil.hasString(eDocumento.getNumeroDocumentoProveedor()))
				nroDocProv = Long.parseLong(eDocumento.getNumeroDocumentoProveedor());			
			ublListValidateDataLocal.reglasValidarDatosRetencion(documento, nroDocProv);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;
			for(ERetencionDocumentoItem rdi : eDocumento.getItems()){
				String tipoDocRefPri = rdi.getTipoDocumentoRelacionado();
				//log.info("rdi.getTipoDocumentoRelacionado() "+rdi.getTipoDocumentoRelacionado());
				if(tipoDocRefPri.equals(Constantes.SUNAT_FACTURA) || tipoDocRefPri.equals(Constantes.SUNAT_NOTA_CREDITO) || 
						tipoDocRefPri.equals(Constantes.SUNAT_NOTA_DEBITO)){
					
					ublListValidateDataLocal.reglasValidarDatosRetencionItem(documento, rdi, nroDocProv);
					if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
							documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
						return;						
				}				
			}			
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosComprobante_Retencion Exception \n"+errors);
		}
	}
	
	public void reglasValidarDatosComprobante_Percepcion(Documento documento, EPercepcionDocumento eDocumento) {
		log.info("reglasValidarDatosComprobante_Percepcion: {}",documento.toString());
		try {
			reglasValidarDatosGeneral(documento, eDocumento.getNumeroDocumentoEmisor(), 
					eDocumento.getFechaEmision(), eDocumento.getNumIdCd(), eDocumento.getDategetNotBefore(), 
					eDocumento.getDateNotAfter() );
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;
			
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
				InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");	
			String tipoDocClie = eDocumento.getTipoDocumentoCliente();				
			Long nroDocClie = 0L;			
			if(StringUtil.hasString(eDocumento.getNumeroDocumentoCliente()))
				nroDocClie = Long.parseLong(eDocumento.getNumeroDocumentoCliente());			
			ublListValidateDataLocal.reglasValidarDatosPercepcion(documento, nroDocClie, tipoDocClie);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;
			String indicadorEmisiónExcepcional = eDocumento.getIndicadorEmisiónExcepcional();
			for(EPercepcionDocumentoItem rdi : eDocumento.getItems()){
				String td = rdi.getTipoDocumentoRelacionado();
				if(td.equals(Constantes.SUNAT_FACTURA) || td.equals(Constantes.SUNAT_BOLETA) ||
						td.equals(Constantes.SUNAT_NOTA_CREDITO) || td.equals(Constantes.SUNAT_NOTA_DEBITO)){
					ublListValidateDataLocal.reglasValidarDatosPercepcionItem(documento, rdi, nroDocClie, tipoDocClie, indicadorEmisiónExcepcional);
					if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
							documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
						return;	
				}
			}
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosComprobante_Percepcion Exception \n"+errors);
		}
	}
	
	public void reglasValidarDatosComprobante_Guia(Documento documento, EGuiaDocumento eDocumento) {
		//log.info("reglasValidarDatosComprobante_Guia "+tbComprobante.getTipoComprobante());
		try {
			reglasValidarDatosGeneral(documento, eDocumento.getNumeroDocumentoEmisor(), 
					eDocumento.getFechaEmisionGuia(), eDocumento.getNumIdCd(), eDocumento.getDategetNotBefore(), 
					eDocumento.getDateNotAfter() );
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;
			
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");	
			ublListValidateDataLocal.reglasValidarDatosGuia(documento, eDocumento);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;			
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatosComprobante_Guia Exception \n"+errors);
		}
	}
	
	public void reglasValidarDatos_SummaryComunicaBajas(Documento documento, EResumenDocumento eDocumento) {
		//log.info("reglasValidarDatos_SummaryResumen "+documento.getTipoComprobante());
		try {
			reglasValidarDatosGeneral(documento, eDocumento.getNumeroDocumentoEmisor(),
					eDocumento.getFechaGeneracionResumen(), eDocumento.getNumIdCd(), eDocumento.getDategetNotBefore(), 
					eDocumento.getDateNotAfter() );

			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;
			
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");	
			ublListValidateDataLocal.reglasValidarDatosComunicaBajas(documento, eDocumento, documento.getFecRecepcion());
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;			
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatos_SummaryResumen Exception \n"+errors);
		}
	}
	
	public void reglasValidarDatos_SummaryResumenDiario(Documento documento, EResumenDocumento eDocumento) {
		//log.info("reglasValidarDatos_SummaryResumenDiario "+documento.getTipoComprobante());
		try {
			this.reglasValidarDatosGeneral(documento, eDocumento.getNumeroDocumentoEmisor(),
					eDocumento.getFechaGeneracionResumen(), eDocumento.getNumIdCd(), eDocumento.getDategetNotBefore(), 
					eDocumento.getDateNotAfter() );
			
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
					return;
			
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");	
			ublListValidateDataLocal.reglasValidarDatosResumenDiario(documento, eDocumento, documento.getFecRecepcion());
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;			
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatos_SummaryResumen Exception \n"+errors);
		}
	}
	
	public void reglasValidarDatos_SummaryReversion(Documento documento, EReversionDocumento eDocumento) {
		//log.info("reglasValidarDatos_SummaryReversion "+documento.getTipoComprobante());
		try {
			reglasValidarDatosGeneral(documento, eDocumento.getNumeroDocumentoEmisor(),
					eDocumento.getFechaEmisionreversion(), eDocumento.getNumIdCd(), eDocumento.getDategetNotBefore(), 
					eDocumento.getDateNotAfter() );
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
					return;
			
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");	
			ublListValidateDataLocal.reglasValidarDatosReversion(documento, eDocumento);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0)))
				return;			
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarDatos_SummaryReversion Exception \n"+errors);
		}
	}
	
	public void grabaTbComprobantesPagoElectronicosFBNCND(Documento documento, EDocumento eDocumento) {
		log.info("grabaTbComprobantesPagoElectronicosFBNCND: {} | moneda: {} ",
				documento.getTipoDocumento(),eDocumento.getTipoMoneda());
		try {
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
				InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");
			BigDecimal amount = new BigDecimal(0);
			if(eDocumento.getTypeLegalMonetaryTotal() != null && eDocumento.getTypeLegalMonetaryTotal().getPayableAmount()!= null)
				amount = eDocumento.getTypeLegalMonetaryTotal().getPayableAmount();			
			Short Ind_percepcion = 0;
			Short ind_for_pag = 0; 
			if((documento.getTipoDocumento().equals(Constantes.SUNAT_FACTURA)) ||
					(documento.getTipoDocumento().equals(Constantes.SUNAT_BOLETA))||
					(documento.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO))){
				List<TypePaymentTerms> listTypePaymentTerms = eDocumento.getListTypePaymentTerms();
				//log.info("size: {}",listTypePaymentTerms.size());
				if(listTypePaymentTerms != null && listTypePaymentTerms.size()>0) {
					for(TypePaymentTerms tpt : listTypePaymentTerms) {
						log.info("ID: {} | PaymentMeansID: {}",tpt.getId(),tpt.getPaymentMeansID());
						if(tpt.getId()!= null) {
							if(tpt.getId().equals("Percepcion"))
								Ind_percepcion = 1;
							if(tpt.getId().equals("FormaPago")){
								if(tpt.getPaymentMeansID().equals("Credito")) 
								ind_for_pag = 1;							
							}
						}
					}
				}
			}
			ublListValidateDataLocal.grabaTbComprobantesPagoElectronicos(documento, 
					eDocumento.getFechaEmision(), amount, Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Acept)), 
					eDocumento.getTipoMoneda(), Ind_percepcion, ind_for_pag);					
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabaTbComprobantesPagoElectronicos Exception \n"+errors);
		}
	}
	
	public void grabaTbComprobantesPagoElectronicosRetencion(Documento documento, ERetencionDocumento eDocumento) {
		//log.info("grabaTbComprobantesPagoElectronicosRetencion "+documento.getTipoComprobante());
		try {
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
				InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");
			BigDecimal amount = new BigDecimal(0);
			
			ublListValidateDataLocal.grabaTbComprobantesPagoElectronicos(documento,	
					eDocumento.getFechaEmision(), amount, Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Acept)), 
					eDocumento.getTipoMonedaTotalRetenido(), null, null);		
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabaTbComprobantesPagoElectronicos Exception \n"+errors);
		}
	}
	
	public void grabaTbComprobantesPagoElectronicosPercepcion(Documento documento, EPercepcionDocumento eDocumento) {
		//log.info("grabaTbComprobantesPagoElectronicosPercepcion "+documento.getTipoComprobante());
		try {
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
				InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");
			BigDecimal amount = new BigDecimal(0);
			ublListValidateDataLocal.grabaTbComprobantesPagoElectronicos(documento, 
					eDocumento.getFechaEmision(), amount, Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Acept)), 
					eDocumento.getTipoMonedaTotalPercibido(), null, null);		
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabaTbComprobantesPagoElectronicos Exception \n"+errors);
		}
	}
	
	public void grabaTbComprobantesPagoElectronicosGuia(Documento documento, EGuiaDocumento eDocumento) {
		//log.info("grabaTbComprobantesPagoElectronicosGuia "+documento.getTipoComprobante());
		try {
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
				InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");
			BigDecimal amount = new BigDecimal(0);
			ublListValidateDataLocal.grabaTbComprobantesPagoElectronicos(documento,	
					eDocumento.getFechaEmisionGuia(), amount, Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Acept)), 
					"", null, null);		
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabaTbComprobantesPagoElectronicos Exception \n"+errors);
		}
	}
	
	public void modificarEstadoComprobantesPagoElectronicos_RA(Documento documento, EResumenDocumento eDocumento) {
		//log.info("modificarEstadoComprobantesPagoElectronicos "+documento.getTipoComprobante());
		try {
			if(documento.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS)) {
				UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");
				ublListValidateDataLocal.modificarEstadoComprobantesPagoElectronicos_RA(documento, 
						eDocumento, Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Anula)));
			}	
			
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabaTbComprobantesPagoElectronicos Exception \n"+errors);
		}
	}
	
	public void modificarEstadoComprobantesPagoElectronicos_RR(Documento documento, EReversionDocumento eDocumento) {
		//log.info("modificarEstadoComprobantesPagoElectronicos "+documento.getTipoComprobante());
		try {
			if(documento.getTipoDocumento().equals(Constantes.SUNAT_REVERSION)) {
				UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");
				ublListValidateDataLocal.modificarEstadoComprobantesPagoElectronicos_RR(documento, 
						eDocumento, Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Anula)));
			}	
			
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabaTbComprobantesPagoElectronicos Exception \n"+errors);
		}
	}
	
	public void grabaTbComprobantesPagoElectronicos_RC(Documento documento, EResumenDocumento eDocumento) {
		//log.info("grabaTbComprobantesPagoElectronicos_RC "+tbComprobante.getTipoComprobante());
		try {
			if(documento.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO)) {
				UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
						InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");
				ublListValidateDataLocal.grabaTbComprobantesPagoElectronicos_RC(documento, eDocumento);				
			}
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabaTbComprobantesPagoElectronicos Exception \n"+errors);
		}
	}

	public void revisarRespuesta2RARCSunatList(Documento documento, EResumenDocumento eDocumento) {
		//log.info("revisarRespuesta2RARCSunatList "+documento.getTipoComprobante());
		try {			
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");				
			ublListValidateDataLocal.revisarRespuesta2RARCSunatList(documento, eDocumento);	
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("revisarRespuesta2RARCSunatList Exception \n"+errors);
		}
	}
	
	public void revisarRespuesta2RRSunatList(Documento documento, EReversionDocumento eDocumento) {
		//log.info("revisarRespuesta2RRSunatList "+documento.getTipoComprobante());
		try {			
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");				
			ublListValidateDataLocal.revisarRespuesta2RRSunatList(documento, eDocumento);	
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("revisarRespuesta2RRSunatList Exception \n"+errors);
		}
	}

	public void revisarRespuesta2NCNDSunatList(Documento documento, EDocumento eDocumento) {
		//log.info("revisarRespuesta2NCNDSunatList "+documento.getTipoComprobante());
		try {			
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");				
			ublListValidateDataLocal.revisarRespuesta2NCNDSunatList(documento, eDocumento);				
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			documento.setErrorLog(e.getMessage());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("revisarRespuesta2RRSunatList Exception \n"+errors);
		}
	}	
	
	public boolean getDBRARCReview(Documento documento, EResumenDocumento eDocumento) {
		log.info("getDBRARCReview "+documento.getTipoDocumento());
		try {			
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");				
			return ublListValidateDataLocal.getDBRARCReview(documento, eDocumento);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDBRARCReview Exception \n"+errors);
		}
		return false;
	}	
	
	public boolean getDBRRReview(Documento documento, EReversionDocumento eDocumento) {
		log.info("getDBRRReview "+documento.getTipoDocumento());
		try {			
			UBLListValidateDataLocal ublListValidateDataLocal = (UBLListValidateDataLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListValidateDataImpl"+Constantes.OSE_CDEJB_RULES+"UBLListValidateDataLocal");				
			return ublListValidateDataLocal.getDBRRReview(documento, eDocumento);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getDBRRReview Exception \n"+errors);
		}
		return false;
	}		
}
