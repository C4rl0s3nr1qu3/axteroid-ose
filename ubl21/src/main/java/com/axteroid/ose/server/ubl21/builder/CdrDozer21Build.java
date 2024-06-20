package com.axteroid.ose.server.ubl21.builder;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
//import org.glassfish.jaxb.runtime.marshaller.NamespacePrefixMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.edocu.EDocumentoCDR;
import com.axteroid.ose.server.ubl21.gateway.request.NamespacePrefixMapper21;

import oasis.names.specification.ubl.schema.xsd.applicationresponse_2_1.ApplicationResponseType;

public class CdrDozer21Build {
	private static final Logger log = LoggerFactory.getLogger(CdrDozer21Build.class);
	
	public byte[] generaCDR21(EDocumentoCDR eDocumentoCDR) {
		//log.info("generaCDR21 ");
		try {			
			ClassLoader classLoader = getClass().getClassLoader();					
			Mapper mapper = new DozerBeanMapper(Arrays.asList(classLoader.getResource("dozer21/dozer-cdr.xml").toString()));
			ApplicationResponseType applicationResponseType = mapper.map(eDocumentoCDR, ApplicationResponseType.class);

			JAXBContext context = JAXBContext.newInstance(ApplicationResponseType.class);
			Marshaller binder = context.createMarshaller();
	
			// JBOSS 3.4
			binder.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapper21());
			binder.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			binder.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			
			StringWriter sw = new StringWriter();
			binder.marshal(applicationResponseType, sw);
			//log.info("generaCDR21() \n"+sw.toString());								
			return sw.toString().getBytes();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();	
			e.printStackTrace(new PrintWriter(errors));
			log.error("generaCDR21 Exception \n"+errors);
		}
		return null;
	}
	
	public byte[] generaCDR21_Resumen(EDocumentoCDR eDocumentoCDR) {
		//log.info("generaCDR21_Resumen ");
		try {			
			ClassLoader classLoader = getClass().getClassLoader();					
			Mapper mapper = new DozerBeanMapper(Arrays.asList(classLoader.getResource("dozer21/dozer-cdr-resumen.xml").toString()));
			ApplicationResponseType applicationResponseType = mapper.map(eDocumentoCDR, ApplicationResponseType.class);

			JAXBContext context = JAXBContext.newInstance(ApplicationResponseType.class);
			Marshaller binder = context.createMarshaller();
			binder.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapper21());
			binder.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			binder.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//			binder.setProperty(CharacterEscapeHandler.class.getName(), new CharacterEscapeHandler() {
//				public void escape(char[] ac, int i, int j, boolean flag, Writer writer) throws IOException {
//					writer.write(ac, i, j);
//				}
//			});
			
			StringWriter sw = new StringWriter();
			binder.marshal(applicationResponseType, sw);
			//log.info("generaCDR21() \n"+sw.toString());								
			return sw.toString().getBytes();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();	
			e.printStackTrace(new PrintWriter(errors));
			log.error("generaCDR21_Resumen Exception \n"+errors);
		}
		return null;
	}	
	
}
