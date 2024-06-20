package com.axteroid.ose.server.rulesejb.rules;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;

@Local
public interface UBLListValidateDataLocal {
	public void reglasValidarDatosGeneral(Documento tbComprobante, 
			Date fechaEmisionComprobante, String numIdCd, Date dategetNotBefore, Date dateNotAfter);
	//public void reglasValidarDatosBoleta(TbComprobante tbComprobante, String tipoDocAdqui, Long documentoAdquiriente);
	public void reglasValidarDatosBoleta(Documento tbComprobante, EDocumento eDocumento);
	public void reglasValidarDatosNotasDebito(Documento tbComprobante, String tipoDocRefPri, 
			String nroDocRefPri, String codMoneda, Date fechaEmision, String codigoTipoNota);
	public void reglasValidarDatosNotasCredito(Documento tbComprobante, String tipoDocRefPri, 
			String nroDocRefPri, String codMoneda, Date fechaEmision, 
			String codigoTipoNota, List<Date> listDateCuota);
	public void reglasValidarDatosDAEAdquiriente(Documento tbComprobante, EDocumento eDocumento);
	public void reglasValidarDatosDAEOperador(Documento tbComprobante, EDocumento eDocumento);
	public void reglasValidarDatosRetencion(Documento tbComprobante, Long numeroDocumentoProveedor);
	public void reglasValidarDatosRetencionItem(Documento tbComprobante, ERetencionDocumentoItem rdi, Long numRUC);
	public void reglasValidarDatosPercepcion(Documento tbComprobante, Long documentoCliente, String tipoDocumentoCliente);
	public void reglasValidarDatosPercepcionItem(Documento tbComprobante, EPercepcionDocumentoItem rdi, 
			Long documentoCliente, String tipoDocumentoCliente, String indicadorEmisiónExcepcional);
	public void reglasValidarDatosGuia(Documento tbComprobante, EGuiaDocumento eDocumento);
	public void reglasValidarDatosComunicaBajas(Documento tbComprobante, EResumenDocumento eDocumento, Date fechaRecep);
	public void reglasValidarDatosResumenDiario(Documento tbComprobante, EResumenDocumento eDocumento, Date fechaRecep);
	public void reglasValidarDatosReversion(Documento tbComprobante, EReversionDocumento eDocumento);
	public void grabaTbComprobantesPagoElectronicos(Documento tbComprobante, Date fechaEmisionComprobante, 
			BigDecimal mtoImporteCpe, Short indEstCPE, String moneda, Short Ind_percepcion, Short ind_for_pag);
	public void modificarEstadoComprobantesPagoElectronicos_RA(Documento tbComprobante, 
    		EResumenDocumento eDocumento, Short indEstCPE);
	public void modificarEstadoComprobantesPagoElectronicos_RR(Documento tbComprobante, 
    		EReversionDocumento eDocumento, Short indEstCPE);
	public void grabaTbComprobantesPagoElectronicos_RC(Documento tbComprobante, EResumenDocumento eDocumento);
	public void revisarRespuesta2RARCSunatList(Documento tbComprobante, EResumenDocumento eDocumento);
	public void revisarRespuesta2RRSunatList(Documento tbComprobante, EReversionDocumento eDocumento);
	public void revisarRespuesta2NCNDSunatList(Documento tbComprobante, EDocumento eDocumento);
	public boolean getDBRARCReview(Documento tbComprobante, EResumenDocumento eDocumento);
	public boolean getDBRRReview(Documento tbComprobante, EReversionDocumento eDocumento);
}
