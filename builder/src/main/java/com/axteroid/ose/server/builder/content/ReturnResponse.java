package com.axteroid.ose.server.builder.content;

import com.axteroid.ose.server.jpa.model.Documento;

public interface ReturnResponse {
	public String buildByteReturnErrorComprobante(Documento documento,  
			ContentValidateSend contentValidateSend);
	public String buildByteReturnErrorArchivoString(Documento documento, 
			ContentValidateSend contentValidateSend);
	public String buildByteReturn(Documento documento);
	public String buildByteReturnTicket(Documento documento);		
	public void grabarDocumento(Documento documento);
	public void updateTbComprobante_Send(Documento documento);
	public void updateComprobante(Documento documento, ContentValidateSend contentValidateSend);
	public void updateComprobante_Send(Documento documento);
	public String getRUCbyUser(Documento documento, String user); 
	public String getEstadobyRUC(Documento documento);
	public String getCLIbyUser(Documento documento, String user);	
	public void getCompobantePago(Documento documento);		
	public void grabarTsAcuseReciboSunat(Documento documento);
	
//	public void updateContent(Documento tbComprobante, ContentValidateSend contentValidateSend);	
//	public void grabarComprobante_SendPack(Documento tbComprobante, ContentValidateSend contentValidateSend);
//	public byte[] buildByteReturnErrorComprobante_Pack(Documento tbComprobante,  
//			ContentValidateSend contentValidateSend);	
	public String buildByteReturnError(Documento documento, ContentValidateSend contentValidateSend);
//	public void grabarContent(TbContent tbContent);
	public String buildByteReturn(Documento documento, ContentValidateSend contentValidateSend);		
//	public String buildByteReturnTicket(TbContent tbContent);			
//	public List<TbContent> buscarTbContentIn(TbContent tbContent);	
//	public void updateContent_Pack(TbContent tbContent);		
	public void updateTbComprobante(Documento documento);	
//	public List<SunatParametro> getParameters(TbContent tbContent);	
	
}
