package com.axteroid.ose.server.builder.content;

import com.axteroid.ose.server.builder.content.impl.CatalogoErrores;
import com.axteroid.ose.server.jpa.model.Documento;

public interface ContentValidate {
	
	public void validarContentContenido(Documento documento);
	public void validarContentFileName(Documento documento);
	public void validarContentContentFile(Documento documento);
	public CatalogoErrores getCatalogoErrores();
	public void setCatalogoErrores(CatalogoErrores catalogoErrores);	
//	public void validarContentFileNamePack(Documento documento);
	public void validarContentFileNameSummary(Documento documento);

}
