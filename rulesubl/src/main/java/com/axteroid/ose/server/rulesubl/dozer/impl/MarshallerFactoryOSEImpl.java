package com.axteroid.ose.server.rulesubl.dozer.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.axteroid.ose.server.rulesubl.dozer.MarshallerFactoryOSE;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.ubl20.gateway.ReplicateDataCmd;
import com.axteroid.ose.server.ubl20.gateway.ReplicateFileCmd;
import com.axteroid.ose.server.ubl20.gateway.ReplicateGroupCmd;
import com.axteroid.ose.server.ubl20.gateway.ReplicateReverseCmd;
import com.axteroid.ose.server.ubl20.gateway.request.ConsultBatchCmd;
import com.axteroid.ose.server.ubl20.gateway.request.MyNamespacePrefixMapper;
import com.axteroid.ose.server.ubl20.gateway.request.SignBatchCmd;
import com.axteroid.ose.server.ubl20.gateway.request.SignOnLineCmd;
import com.axteroid.ose.server.ubl20.gateway.request.SignOnLineDespatchCmd;
import com.axteroid.ose.server.ubl20.gateway.request.SignOnLinePerceptionCmd;
import com.axteroid.ose.server.ubl20.gateway.request.SignOnLineRetentionCmd;
import com.axteroid.ose.server.ubl20.gateway.request.SignOnLineReversionCmd;
import com.axteroid.ose.server.ubl20.gateway.request.SignOnLineSummaryCmd;
import com.axteroid.ose.server.ubl20.gateway.response.EbizResponse;
import com.axteroid.ose.server.ubl20.replicator.xml.ReplicacionParams;
//import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import oasis.names.specification.ubl.schema.xsd.applicationresponse_2.ApplicationResponseType;
import oasis.names.specification.ubl.schema.xsd.creditnote_2.CreditNoteType;
import oasis.names.specification.ubl.schema.xsd.debitnote_2.DebitNoteType;
import oasis.names.specification.ubl.schema.xsd.despatchadvice_2.DespatchAdviceType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;
import sunat.names.specification.ubl.peru.schema.xsd.perception_1.PerceptionType;
import sunat.names.specification.ubl.peru.schema.xsd.retention_1.RetentionType;
import sunat.names.specification.ubl.peru.schema.xsd.summarydocuments_1.SummaryDocumentsType;
import sunat.names.specification.ubl.peru.schema.xsd.voideddocuments_1.VoidedDocumentsType;

public class MarshallerFactoryOSEImpl implements MarshallerFactoryOSE{
	
    JAXBContext context;
    JAXBContext contextDocumento;
    JAXBContext contextInvoice;
    JAXBContext contextCredit;
    JAXBContext contextDebit;
    JAXBContext contextSummary;
    JAXBContext contextVoidded;
    JAXBContext contextGuia;
    JAXBContext contextRetencion;
    JAXBContext contextPercepcion;
    JAXBContext contextApplicationResponse;

    public MarshallerFactoryOSEImpl() {
        try {
            contextInvoice = JAXBContext.newInstance(InvoiceType.class);
            contextCredit = JAXBContext.newInstance(CreditNoteType.class);
            contextDebit = JAXBContext.newInstance(DebitNoteType.class);
            contextSummary = JAXBContext.newInstance(SummaryDocumentsType.class);
            contextVoidded = JAXBContext.newInstance(VoidedDocumentsType.class);
            contextGuia = JAXBContext.newInstance(DespatchAdviceType.class);
            contextRetencion = JAXBContext.newInstance(RetentionType.class);
            contextPercepcion = JAXBContext.newInstance(PerceptionType.class);     
            contextApplicationResponse = JAXBContext.newInstance(ApplicationResponseType.class);  
            
            context = JAXBContext.newInstance(
                    SignBatchCmd.class,
                    ConsultBatchCmd.class,
                    SignOnLineCmd.class,
                    SignOnLineSummaryCmd.class,
                    EbizResponse.class,
                    EDocumento.class,
                    EResumenDocumento.class,
                    EbizResponse.class,
                    ReplicateDataCmd.class,
                    ReplicateFileCmd.class,
                    ReplicacionParams.class,
                    ReplicateGroupCmd.class,
                    ReplicateReverseCmd.class,
                    EGuiaDocumento.class, 
                    SignOnLineDespatchCmd.class,
                    ERetencionDocumento.class,
                    EPercepcionDocumento.class,
                    SignOnLineRetentionCmd.class,
                    SignOnLinePerceptionCmd.class,
                    EReversionDocumento.class,
                    SignOnLineReversionCmd.class
            );
            contextDocumento = JAXBContext.newInstance(
                    EDocumento.class,
                    EResumenDocumento.class,
                    EGuiaDocumento.class,
                    ERetencionDocumento.class,
                    EPercepcionDocumento.class,
                    EReversionDocumento.class
            );
        } catch (Exception e) {
            //log.error("Error en el sistema", e);
        }
    }

    public String marshal(Object object) {
        return marshal(null, object);
    }

    public String marshalDocumento(Object object) {
        try {
            Marshaller marshaller = contextDocumento.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(object, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String marshalByte(Object object) {
        String tm = marshalDocumento(object);
        return tm;
    }

    public String marshal(NamespacePrefixMapper namespacePrefixMapper, Object object) {
        try {
            Marshaller marshaller = context.createMarshaller();
            if (namespacePrefixMapper != null) {
                marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
                        namespacePrefixMapper);
            }
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(object, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String marshalResponse(Object object) {
        try {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            marshaller.setProperty(CharacterEscapeHandler.class.getName(),
//                    new CharacterEscapeHandler() {
//                        public void escape(char[] ac, int i, int j, boolean flag,
//                                           Writer writer) throws IOException {
//                            writer.write(ac, i, j);
//                        }
//                    });
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(object, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Document toDomFile(Object invoiceType, String tipoDocumento) {
        try {
            Marshaller binder = null;
            if (Constantes.SUNAT_BOLETA.equals(tipoDocumento) || 
            		Constantes.SUNAT_FACTURA.equals(tipoDocumento)) {
                binder = contextInvoice.createMarshaller();
            } else if (Constantes.SUNAT_NOTA_CREDITO.equals(tipoDocumento)) {
                binder = contextCredit.createMarshaller();
            } else if (Constantes.SUNAT_NOTA_DEBITO.equals(tipoDocumento)) {
                binder = contextDebit.createMarshaller();
            } else if (Constantes.SUNAT_COMUNICACION_BAJAS.equals(tipoDocumento)) {
                binder = contextVoidded.createMarshaller();
            } else if (Constantes.SUNAT_RESUMEN_DIARIO.equals(tipoDocumento)) {
                binder = contextSummary.createMarshaller();
            } else if (Constantes.SUNAT_GUIA_REMISION_REMITENTE.equals(tipoDocumento) || 
            		Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA.equals(tipoDocumento)) {
            	binder = contextGuia.createMarshaller();            	
            } else if (Constantes.SUNAT_RETENCION.equals(tipoDocumento)) {
            	binder = contextRetencion.createMarshaller();
            } else if (Constantes.SUNAT_PERCEPCION.equals(tipoDocumento)) {
            	binder = contextPercepcion.createMarshaller();
            } else if (Constantes.SUNAT_REVERSION.equals(tipoDocumento)) {	
                binder = contextVoidded.createMarshaller();                
            } else if (Constantes.SUNAT_APPLICATION_RESPONSE.equals(tipoDocumento)) {	
                binder = contextApplicationResponse.createMarshaller();                
            }

            binder.setProperty("com.sun.xml.bind.namespacePrefixMapper",
                    new MyNamespacePrefixMapper());
            binder.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
            binder.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//            binder.setProperty(CharacterEscapeHandler.class.getName(),
//                    new CharacterEscapeHandler() {
//                        public void escape(char[] ac, int i, int j, boolean flag,
//                                           Writer writer) throws IOException {
//                            writer.write(ac, i, j);
//                        }
//                    });
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setCoalescing(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.newDocument();
            //log.info("toDomFile  binder.marshal");
            binder.marshal(invoiceType, document );
            return document;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Document toDomMemory(Object invoiceType, String tipoDocumento) {
        try {
            Marshaller binder = null;
            if (Constantes.SUNAT_BOLETA.equals(tipoDocumento) || Constantes.SUNAT_FACTURA.equals(tipoDocumento)) {
                binder = contextInvoice.createMarshaller();
            } else if (Constantes.SUNAT_NOTA_CREDITO.equals(tipoDocumento)) {
                binder = contextCredit.createMarshaller();
            } else if (Constantes.SUNAT_NOTA_DEBITO.equals(tipoDocumento)) {
                binder = contextDebit.createMarshaller();
            } else if (Constantes.SUNAT_COMUNICACION_BAJAS.equals(tipoDocumento)) {
                binder = contextVoidded.createMarshaller();
            } else if (Constantes.SUNAT_RESUMEN_DIARIO.equals(tipoDocumento)) {
                binder = contextSummary.createMarshaller();
            } else if (Constantes.SUNAT_GUIA_REMISION_REMITENTE.equals(tipoDocumento) || 
            		Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA.equals(tipoDocumento)) {
            	binder = contextGuia.createMarshaller();
            } else if (Constantes.SUNAT_RETENCION.equals(tipoDocumento)) {
            	binder = contextRetencion.createMarshaller();
            } else if (Constantes.SUNAT_PERCEPCION.equals(tipoDocumento)) {
            	binder = contextPercepcion.createMarshaller();
            } else if (Constantes.SUNAT_REVERSION.equals(tipoDocumento)) {
                binder = contextVoidded.createMarshaller();                
            }

            binder.setProperty("com.sun.xml.bind.namespacePrefixMapper",
                    new MyNamespacePrefixMapper());
            binder.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
            binder.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//            binder.setProperty(CharacterEscapeHandler.class.getName(),
//                    new CharacterEscapeHandler() {
//                        public void escape(char[] ac, int i, int j, boolean flag,
//                                           Writer writer) throws IOException {
//                            writer.write(ac, i, j);
//                        }
//                    });
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setCoalescing(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.newDocument();
            binder.marshal(invoiceType, document );
            StringWriter sw=new StringWriter();
            binder.marshal(invoiceType, sw);
            return document;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Object unmarshal(String commandAsXml) throws JAXBException {
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller.unmarshal(new ByteArrayInputStream(commandAsXml.getBytes(Charset.defaultCharset())));
    }



    public Object unmarshalDocumento(String commandAsXml) throws JAXBException {
        Unmarshaller unmarshaller = contextDocumento.createUnmarshaller();
        return unmarshaller.unmarshal(new ByteArrayInputStream(commandAsXml.getBytes()));
    }


    public String fileToByte(File file) {

        FileInputStream fin=null;
        try {
            fin = new FileInputStream(file);
            byte fileContent[] = new byte[(int) file.length()];
            fin.read(fileContent);
            fin.close();
            //return base64Encoder.encode(fileContent);
        } catch (FileNotFoundException e) {
            //log.error("File not found" + e);
        } catch (IOException ioe) {
            //log.error("Exception while reading the file " + ioe);
        }
        return null;
    }

    public Class getClassFrom(EDocumento documento) {
        if (Constantes.SUNAT_FACTURA.equals(documento.getTipoDocumento())) {
            return InvoiceType.class;
        } else if (Constantes.SUNAT_BOLETA.equals(documento.getTipoDocumento())) {
            return InvoiceType.class;
        } else if (Constantes.SUNAT_NOTA_CREDITO.equals(documento.getTipoDocumento())) {
            return CreditNoteType.class;
        } else if (Constantes.SUNAT_NOTA_DEBITO.equals(documento.getTipoDocumento())) {
            return DebitNoteType.class;
        } else if (Constantes.SUNAT_COMUNICACION_BAJAS.equals(documento.getTipoDocumento())) {
            return VoidedDocumentsType.class;
        } else if (Constantes.SUNAT_RESUMEN_DIARIO.equals(documento.getTipoDocumento())) {
            return SummaryDocumentsType.class;
        } else if (Constantes.SUNAT_GUIA_REMISION_REMITENTE.equals(documento.getTipoDocumento()) || 
        		Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA.equals(documento.getTipoDocumento())) {
            return DespatchAdviceType.class;
        } else if (Constantes.SUNAT_RETENCION.equals(documento.getTipoDocumento())) {
        	return RetentionType.class;
        } else if (Constantes.SUNAT_PERCEPCION.equals(documento.getTipoDocumento())) {
        	return PerceptionType.class;
        } else if (Constantes.SUNAT_REVERSION.equals(documento.getTipoDocumento())) {
            return VoidedDocumentsType.class;
        }
        throw new IllegalArgumentException("Tipo de documento incorrecto");
    }
}
