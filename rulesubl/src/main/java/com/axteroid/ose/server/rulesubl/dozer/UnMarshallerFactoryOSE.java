package com.axteroid.ose.server.rulesubl.dozer;

import java.io.File;
import java.util.Iterator;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.edocu.IDocumento;

public interface UnMarshallerFactoryOSE {
	public Iterator<?> unMarshall(String filePath, String tipoDocumento,
			String version, boolean userDefault) throws Exception;

	public Object unmarshal(String commandAsXml) ;

	public Object unmarshal(File fileXml);
	
	public Object unmarshalXml(File fileXml);
	
	public Object unmarshalXml(Documento tbComprobante);

	public Object convertDocumentoToBean(Documento tbComprobante, Object documentoSunat);

	String buscarAdicional(String s, IDocumento documento);
}
