package com.axteroid.ose.server.rulesejb.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.ComprobantesPagoElectronicos;
import com.axteroid.ose.server.tools.bean.ComprobantePagoListID;
import com.axteroid.ose.server.tools.bean.SunatTokenResponse;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EReversionDocumentoItem;

@Local
public interface ComprobantesPagoElectronicosDAOLocal {
	public void getCompobantePago(Documento documento);
	public void getCompobantePagoNotaDebito(Documento documento, String tipoDocRefPri, 
			String nroDocRefPri, String codMoneda, String codigoTipoNota);
	public void getCompobantePagoNotaCredito(Documento documento, String tipoDocRefPri, 
			String nroDocRefPri, String codMoneda, String codigoTipoNota, List<Date> listDateCuota);
    public void getCompobantePagoNCND(Documento documento, String tipoDocRefPri, 
    		String nroDocRefPri, Date fechaEmision);
    public void getCompobantePagoNCDocRefPri(Documento documento, String tipoDocRefPri, 
    		String nroDocRefPri);
	public void getCompobantePago_RP(Documento documento);
	public void getCompobantePagoPercepcionItem(Documento documento, 
			EPercepcionDocumentoItem rdi, String indicadorEmisiónExcepcional);
	public void getCompobantePagoGuia(Documento documento, String tipoDocRefPri, String nroDocRefPri);
	public void getCompobantePagoComunicaBajasItem(Documento documento, EResumenDocumentoItem rdi,
    		Date fechaEmisionComprobante, Date fechaRecep);
	public void getCompobantePagoReversionItem(Documento documento, EReversionDocumentoItem rdi);
	public void grabaTbComprobantesPagoElectronicos(Documento documento, Date fechaEmisionComprobante, 
			BigDecimal mtoImporteCpe, Short indEstCPE, String moneda, Short Ind_percepcion, Short ind_for_pag);
	public void modificarEstadoComprobantesPagoElectronicos_RA(Documento documento, EResumenDocumentoItem rdi,
			Short indEstCPE);
	public void modificarEstadoComprobantesPagoElectronicos_RR(Documento documento, EReversionDocumentoItem rdi,
			Short indEstCPE);
	public void getCompobantePagoResumenDiarioItem_BR(Documento documento, EResumenDocumentoItem rdi);
	public void grabaTbComprobantesPagoElectronicos_RC(Documento documento, 
			Date fechaEmisionComprobante, EResumenDocumentoItem rdi);
	public void getCompobantePagoAnticipoBFE(Documento documento, Long numDocumento, 
	    	String tipoDocRefPri, String nroDocRefPri) ;
	public List<ComprobantesPagoElectronicos> getCompobantePagoItem(Documento documento, 
    		String tipoDocRefPri, String nroDocRefPri) ;
	public void revisarRespuesta2RCItemSunatList(Documento documento, EResumenDocumentoItem rdi, 
			String fechaEmision, SunatTokenResponse sunatToken);
	public void revisarRespuesta2RAItemSunatList(Documento documento, EResumenDocumentoItem rdi);
	public void revisarRespuesta2RRItemSunatList(Documento documento, EReversionDocumentoItem rdi);
	public void revisarRespuesta2NCNDItemSunatList(Documento documento, String tipoDocumento, String serie);
	public boolean getDBCpe2TbComprobanteID(Documento documento, 
    		Long numRuc, String tipoDocumento, String serie_numero, String fechaEmision, String monto);
    public void getCompobantePagoResumenDiarioItem(Documento documento, EResumenDocumentoItem rdi, 
    		Date fechaRecep) ;
	public String buscarTbComprobantePagoListID(Documento documento, EResumenDocumento eDocumento);
	public List<ComprobantesPagoElectronicos> buscarTbComprobantePagoIn(Documento documento, String sListID);
	public List<ComprobantesPagoElectronicos> buscarSunatListResumenMasiva(String ruc, ComprobantePagoListID comprobantePagoListID);
	public Integer countComprobantePagoIDState(Documento documento, int sState);
    public List<ComprobantesPagoElectronicos> buscarListGetSunatList(String ruc, 
    		String tipo, String serie, Long numero );
}
