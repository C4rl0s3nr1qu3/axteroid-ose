package com.axteroid.ose.server.rulesejb.rules;

import java.util.List;
import javax.ejb.Local;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.Emisor;
import com.axteroid.ose.server.jpa.model.SunatAcuseReciboSunat;
import com.axteroid.ose.server.jpa.model.SunatContribuyenteAsociadoEmisor;
import com.axteroid.ose.server.jpa.model.ComprobantesPagoElectronicos;

@Local
public interface OseDBCRUDLocal {
	public void grabarDocumento(Documento documento);
	public void updateTbComprobante(Documento documento);
	public void updateTbComprobante_Send(Documento documento);
	public void updateTbComprobanteCDR(Documento documento);
	public void updateTbComprobanteCDRID(Documento documento);
	public Documento buscarTbComprobanteXIDComprobante(Documento documento);
	public Documento buscarTbComprobanteXIDComprobanteShort(Documento documento);
	public void getCompobantePago(Documento documento);
	public Documento buscarTbComprobanteXID(Documento documento);
	public List<ComprobantesPagoElectronicos> getCompobantePagoItem(Documento documento, 
			String tipoDocRefPri, String nroDocRefPri);
	public List<Documento> buscarTbComprobanteALL(Documento documento);
	public void grabarTsAcuseReciboSunat(SunatAcuseReciboSunat tsAcuseReciboSunat);
	public Documento buscarTbComprobanteXIDComprobante_GET(Documento documento);
	public Integer countComprobantePagoIDState(Documento documento, int sState);

	public List<Documento> buscarTbComprobanteXContentID_GET(Documento documento);	
	public Emisor buscarEmisor(Documento documento);
	public void getPadronAutorizadoIgv(Documento documento);
	public void getEstadoCompobantePagoElectronicos(Documento documento);
	public List<SunatContribuyenteAsociadoEmisor> getJoinPSEEmisor(Documento documento, 
			int indicador, Long rucPSE);
}
