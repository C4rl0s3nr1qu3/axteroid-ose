package com.axteroid.ose.server.rulesejb.dao;

import java.util.List;

import javax.ejb.Local;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesejb.emf.DAOCom;

@Local
public interface DocumentoDAOLocal extends DAOCom<Documento>{
	public void grabaDocumento(Documento documento);
	public void updateTbComprobante(Documento documento);
	public void updateTbComprobanteErrorComprobante(Documento documento);
	public List<Documento> buscarTbComprobanteID(Documento documento);
//	public List<Documento> buscarTbComprobanteXContent(TbContent tbContent);
//	public List<Documento> buscarTbComprobanteXContentID(TbContent tbContent);
	public List<Documento> buscarTbComprobanteXIDComprobante(Documento documento);
	public List<Documento> buscarTbComprobanteXIDComprobanteShort(Documento documento);
	public List<Documento> buscarTbComprobanteXID(Documento documento);
	public void updateTbComprobanteCDR(Documento documento);
	public void updateTbComprobanteCDRID(Documento documento);
	public List<Documento> buscarTbComprobanteXVariables(Documento documento);
	public List<Documento> buscarTbComprobanteALL(Documento documento);
	//public List<TbComprobante> buscarTbComprobanteIn(Documento documento);
	public List<Documento> buscarTbComprobante4Key(Documento documento);
	public List<Documento> buscarTbComprobanteXContentID_GET(Documento documento);
	public List<Documento> buscarTbComprobanteXIDComprobante_GET(Documento documento);
}
