package com.axteroid.ose.server.builder.content;

import com.axteroid.ose.server.jpa.model.Documento;

public interface ContentValidateSend {
	
	public void validarContentSendBill(Documento documento);
//	public void validarContentSendPack(Documento documento);
	public void validarContentSendSummary(Documento documento);
	public void buscarErrorComprobante(Documento documento, String num_error);
	public void buscarError(Documento documento, String num_error);
	public void buscarObservaciones(Documento documento);
	public void updateContent(Documento documento);
//	public void validarContentSend(Documento documento) ;
	public ContentValidate getContentValidate();
}
