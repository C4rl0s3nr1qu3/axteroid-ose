package com.axteroid.ose.server.builder.content.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.builder.content.ContentValidate;
import com.axteroid.ose.server.builder.content.ContentValidateSend;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;

public class ContentValidateSendImpl implements ContentValidateSend{
	private static final Logger log = LoggerFactory.getLogger(ContentValidateSendImpl.class);
	private ContentValidate contentValidate = new ContentValidateImpl();
	
	public void validarContentSendBill(Documento documento) {
		//contentValidate = new ContentValidateImpl(OseConstantes.CATALOGO_ERRORES, tbContent);	
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;		

		contentValidate.validarContentFileName(documento);		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;	
		
		contentValidate.validarContentContenido(documento);		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;
						
		contentValidate.validarContentContentFile(documento);		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;		
	}
	
//	public void validarContentSendPack(Documento documento) {		
//		//contentValidate = new ContentValidateImpl(OseConstantes.CATALOGO_ERRORES, tbContent);	
//		//log.info("1) validarContentSendPack: "+tbContent.getFilename());
//		if(tbContent.getErrorContent().equals(ConstantesOse.CONTENT_FALSE.charAt(0)))
//			return;		
//
//		contentValidate.validarContentFileNamePack(tbContent);		
//		if(tbContent.getErrorContent().equals(ConstantesOse.CONTENT_FALSE.charAt(0)))
//			return;			
//		//log.info("2) validarContentSendPack: "+tbContent.getFilename());
//		contentValidate.validarContentContenido(tbContent);		
//		if(tbContent.getErrorContent().equals(ConstantesOse.CONTENT_FALSE.charAt(0)))
//			return;
//		//log.info("3) validarContentSendPack: "+tbContent.getFilename());				
//		contentValidate.validarContentContentFile(tbContent);		
//		if(tbContent.getErrorContent().equals(ConstantesOse.CONTENT_FALSE.charAt(0)))
//			return;
//		//log.info("4) validarContentSendPack: "+tbContent.getFilename());
//	}
	
	public void validarContentSendSummary(Documento documento) {		
		//contentValidate = new ContentValidateImpl(OseConstantes.CATALOGO_ERRORES, tbContent);	
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;		

		contentValidate.validarContentFileNameSummary(documento);		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;			
		
		contentValidate.validarContentContenido(documento);		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;
						
		contentValidate.validarContentContentFile(documento);		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;
		
	}
	
	public void buscarErrorComprobante(Documento documento, String num_error) {
		contentValidate.getCatalogoErrores().buscarErrorComprobante(documento, num_error);
	}
	
	public void buscarError(Documento documento, String num_error) {		
		contentValidate.getCatalogoErrores().buscarError(documento, num_error);
	}
	public void buscarObservaciones(Documento documento) {		
		contentValidate.getCatalogoErrores().buscarObservaciones(documento); 
	}
	
	public void updateContent(Documento documento) {		
		//contentValidate.getCatalogoErrores().updateContent(documento); 
	}
	
//	public void validarContentSend(TbContent tbContent) {		
//		contentValidate = new ContentValidateImpl(OseConstantes.CATALOGO_ERRORES, tbContent);
//	}
	
	public ContentValidate getContentValidate() {
		return contentValidate;
	}
	
	
}
