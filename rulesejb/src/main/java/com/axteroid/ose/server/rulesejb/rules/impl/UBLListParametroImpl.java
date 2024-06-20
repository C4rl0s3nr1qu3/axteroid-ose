package com.axteroid.ose.server.rulesejb.rules.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.naming.InitialContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatParametro;
import com.axteroid.ose.server.rulesejb.dao.SunatParametroDAOLocal;
import com.axteroid.ose.server.rulesejb.rules.UBLListParametroLocal;
import com.axteroid.ose.server.rulesejb.rules.UBLListParametroRulesLocal;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;

public class UBLListParametroImpl implements UBLListParametroLocal{
	private static final Logger log = LoggerFactory.getLogger(UBLListParametroImpl.class);

	public void reglasValidarParametrosComprobante(Documento tbComprobante, EDocumento eDocumento) {
		log.info("reglasValidarParametrosComprobante: {}"+tbComprobante.toString());
		switch(tbComprobante.getTipoDocumento()) {
			case Constantes.SUNAT_FACTURA:
				this.reglasValidarParametrosFacturaBoleta(tbComprobante, eDocumento);
				break;
			case Constantes.SUNAT_BOLETA:
				this.reglasValidarParametrosFacturaBoleta(tbComprobante, eDocumento);
				break;		
			case Constantes.SUNAT_NOTA_CREDITO:
				this.reglasValidarParametrosNotaCredito(tbComprobante, eDocumento);
				break;
			case Constantes.SUNAT_NOTA_DEBITO:
				this.reglasValidarParametrosNotaDebito(tbComprobante, eDocumento);
				break;	
			case Constantes.SUNAT_RECIBO_SERV_PUBL:
				this.reglasValidarParametrosReciboServiciosPublicos(tbComprobante, eDocumento);
				break;	
			case Constantes.SUNAT_ROL_ADQUIRIENTE_SISTEMAS_PAGO:
				this.reglasValidarParametrosDAEAdquiriente(tbComprobante, eDocumento);
				break;
			case Constantes.SUNAT_OPERADOR:
				this.reglasValidarParametrosDAEOperador(tbComprobante, eDocumento);
				break;					
			case Constantes.SUNAT_ADQUIRIENTE_SISTEMAS_PAGO:
				this.reglasValidarParametrosDAEAdquiriente(tbComprobante, eDocumento);
				break;				
		}		
	}

	private void reglasValidarParametrosFacturaBoleta(Documento tbComprobante, EDocumento eDocumento) {
		try {			
			UBLListParametroRulesLocal ublListParametroRulesLocal = (UBLListParametroRulesLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListParametroRulesImpl"+Constantes.OSE_CDEJB_RULES+"UBLListParametroRulesLocal");				
		
			ublListParametroRulesLocal.reglasValidarParametrosFacturaBoleta(tbComprobante, eDocumento);	
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarLoginParametro Exception \n"+errors);
		}
	}
	
	private void reglasValidarParametrosReciboServiciosPublicos(Documento tbComprobante, EDocumento eDocumento) {
		try {			
			UBLListParametroRulesLocal ublListParametroRulesLocal = (UBLListParametroRulesLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListParametroRulesImpl"+Constantes.OSE_CDEJB_RULES+"UBLListParametroRulesLocal");				
		
			ublListParametroRulesLocal.reglasValidarParametrosReciboServiciosPublicos(tbComprobante, eDocumento);		
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarLoginParametro Exception \n"+errors);
		}		
	}
	
	private void reglasValidarParametrosNotaCredito(Documento tbComprobante, EDocumento eDocumento) {
		try {			
			UBLListParametroRulesLocal ublListParametroRulesLocal = (UBLListParametroRulesLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListParametroRulesImpl"+Constantes.OSE_CDEJB_RULES+"UBLListParametroRulesLocal");				
		
			ublListParametroRulesLocal.reglasValidarParametrosNotaCredito(tbComprobante, eDocumento);
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarLoginParametro Exception \n"+errors);
		}		
	}	

	private void reglasValidarParametrosNotaDebito(Documento tbComprobante, EDocumento eDocumento) {
		try {			
			UBLListParametroRulesLocal ublListParametroRulesLocal = (UBLListParametroRulesLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListParametroRulesImpl"+Constantes.OSE_CDEJB_RULES+"UBLListParametroRulesLocal");				
		
			ublListParametroRulesLocal.reglasValidarParametrosNotaDebito(tbComprobante, eDocumento);
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarLoginParametro Exception \n"+errors);
		}		
	}

	private void reglasValidarParametrosDAEAdquiriente(Documento tbComprobante, EDocumento eDocumento) {
		try {			
			UBLListParametroRulesLocal ublListParametroRulesLocal = (UBLListParametroRulesLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListParametroRulesImpl"+Constantes.OSE_CDEJB_RULES+"UBLListParametroRulesLocal");				
		
			ublListParametroRulesLocal.reglasValidarParametrosDAEAdquiriente(tbComprobante, eDocumento);
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarLoginParametro Exception \n"+errors);
		}
	}	
	
	private void reglasValidarParametrosDAEOperador(Documento tbComprobante, EDocumento eDocumento) {
//		UBLParametersValidate ublParametersValidate = new UBLParametersValidateImpl();
//		ublParametersValidate.reglasValidarParametrosDAEAdquiriente(tbComprobante, eDocumento);
	}	
	
	public void reglasValidarParametrosResumenDiario(Documento tbComprobante, EResumenDocumento eDocumento) {
		try {			
			UBLListParametroRulesLocal ublListParametroRulesLocal = (UBLListParametroRulesLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListParametroRulesImpl"+Constantes.OSE_CDEJB_RULES+"UBLListParametroRulesLocal");				
		
			ublListParametroRulesLocal.reglasValidarParametrosResumenDiario(tbComprobante, eDocumento);
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarLoginParametro Exception \n"+errors);
		}
	}
	
	public void reglasValidarParametrosRetencion(Documento tbComprobante, ERetencionDocumento eDocumento) {
		try {			
			UBLListParametroRulesLocal ublListParametroRulesLocal = (UBLListParametroRulesLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListParametroRulesImpl"+Constantes.OSE_CDEJB_RULES+"UBLListParametroRulesLocal");				
		
			ublListParametroRulesLocal.reglasValidarParametrosRetencion(tbComprobante, eDocumento);
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarLoginParametro Exception \n"+errors);
		}
	}
	
	public void reglasValidarParametrosPercepcion(Documento tbComprobante, EPercepcionDocumento eDocumento) {
		try {			
			UBLListParametroRulesLocal ublListParametroRulesLocal = (UBLListParametroRulesLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListParametroRulesImpl"+Constantes.OSE_CDEJB_RULES+"UBLListParametroRulesLocal");				
		
			ublListParametroRulesLocal.reglasValidarParametrosPercepcion(tbComprobante,eDocumento);
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarLoginParametro Exception \n"+errors);
		}
	}
	
	public void reglasValidarParametrosGuia(Documento tbComprobante, EGuiaDocumento eDocumento) {
		try {			
			UBLListParametroRulesLocal ublListParametroRulesLocal = (UBLListParametroRulesLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListParametroRulesImpl"+Constantes.OSE_CDEJB_RULES+"UBLListParametroRulesLocal");				

			ublListParametroRulesLocal.reglasValidarParametrosGuia(tbComprobante, eDocumento);
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarLoginParametro Exception \n"+errors);
		}
	}
	
	
	public String bucarParametro(String codParametro, String codArgumento) {
		log.info("bucarParametro: {} - {} ", codParametro,codArgumento);
		String desArgumento = "";
		try {			
			UBLListParametroRulesLocal ublListParametroRulesLocal = (UBLListParametroRulesLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListParametroRulesImpl"+Constantes.OSE_CDEJB_RULES+"UBLListParametroRulesLocal");				
			desArgumento = ublListParametroRulesLocal.bucarParametro(codParametro, codArgumento);				
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarParametro Exception \n"+errors);
		}
		return desArgumento;
	}		
	
	public String bucarLoginParametro(String codArgumento) {
		log.info("bucarLoginParametro "+codArgumento);
		String desArgumento = "";
		try {			
			UBLListParametroRulesLocal ublListParametroRulesLocal = (UBLListParametroRulesLocal) 
					InitialContext.doLookup(Constantes.OSE_doLookup+"UBLListParametroRulesImpl"+Constantes.OSE_CDEJB_RULES+"UBLListParametroRulesLocal");				
			desArgumento = ublListParametroRulesLocal.bucarLoginParametro(codArgumento);				
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarLoginParametro Exception \n"+errors);
		}
		return desArgumento;
	}	
	
	public String buscarParametroCodArgumento(String codParametro, String desArgumento) {
		log.info("buscarParametroCodArgumento: {} - {} ", codParametro,desArgumento);
		String codArgumento = "";
		try {					
			SunatParametroDAOLocal sunatParametroDAOLocal = 
				(SunatParametroDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatParametroDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatParametroDAOLocal");						
			List<SunatParametro> results = sunatParametroDAOLocal.buscarParametroCodArgumento(codParametro,desArgumento);
			if(results != null && results.size()>0)
				codArgumento = results.get(0).getSunatParametroPK().getCodArgumento(); 
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buscarParametroCodArgumento Exception \n"+errors);
		}
		return codArgumento;
	}		
}
