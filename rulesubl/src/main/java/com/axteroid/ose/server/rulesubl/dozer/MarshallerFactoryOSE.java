package com.axteroid.ose.server.rulesubl.dozer;

import javax.xml.bind.JAXBException;

import org.w3c.dom.Document;

import com.axteroid.ose.server.tools.edocu.EDocumento;

public interface MarshallerFactoryOSE {

	Document toDomFile(Object invoiceType,String tipoDocumento);

    Document toDomMemory(Object invoiceType,String tipoDocumento);

    public Object unmarshal(String commandAsXml)  throws JAXBException;

    public Object unmarshalDocumento(String commandAsXml) throws JAXBException;

    public String marshal(Object object);

    public String marshalResponse(Object object);

    public String marshalByte(Object object);
    
    public Class getClassFrom(EDocumento documento);
}
