package com.axteroid.ose.server.rulesubl.dozer.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Iterator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesubl.dozer.UnMarshallerFactoryOSE;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.IDocumento;
import com.axteroid.ose.server.ubl20.gateway.ReplicateDataCmd;
import com.axteroid.ose.server.ubl20.gateway.ReplicateFileCmd;
import com.axteroid.ose.server.ubl20.gateway.ReplicateReverseCmd;
import com.axteroid.ose.server.ubl20.gateway.request.ConsultBatchCmd;
import com.axteroid.ose.server.ubl20.gateway.request.SignBatchCmd;
import com.axteroid.ose.server.ubl20.gateway.request.SignOnLineCmd;
import com.axteroid.ose.server.ubl20.gateway.request.SignOnLineDespatchCmd;
import com.axteroid.ose.server.ubl20.gateway.request.SignOnLinePerceptionCmd;
import com.axteroid.ose.server.ubl20.gateway.request.SignOnLineRetentionCmd;
import com.axteroid.ose.server.ubl20.gateway.request.SignOnLineReversionCmd;
import com.axteroid.ose.server.ubl20.gateway.request.SignOnLineSummaryCmd;
import com.axteroid.ose.server.ubl20.gateway.response.EbizResponse;

import oasis.names.specification.ubl.schema.xsd.IDocumentoSunat;
//import oasis.names.specification.ubl.schema.xsd.applicationresponse_21.ApplicationResponseType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.creditnote_2.CreditNoteType;
import oasis.names.specification.ubl.schema.xsd.debitnote_2.DebitNoteType;
import oasis.names.specification.ubl.schema.xsd.despatchadvice_2.DespatchAdviceType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;
import sunat.names.specification.ubl.peru.schema.xsd.perception_1.PerceptionType;
import sunat.names.specification.ubl.peru.schema.xsd.retention_1.RetentionType;
import sunat.names.specification.ubl.peru.schema.xsd.summarydocuments_1.SummaryDocumentsType;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.AdditionalInformationType;
import sunat.names.specification.ubl.peru.schema.xsd.voideddocuments_1.VoidedDocumentsType;

public class UnMarshallerFactoryOSEImpl implements UnMarshallerFactoryOSE{

	private static final Logger log = LoggerFactory.getLogger(UnMarshallerFactoryOSEImpl.class);
	//private static final Logger log = Logger.getLogger(UnMarshallerFactoryOSEImpl.class);
	JAXBContext context;

    public UnMarshallerFactoryOSEImpl() {
        try {
            context = JAXBContext.newInstance(                    
                    CreditNoteType.class,
                    DebitNoteType.class,
                    InvoiceType.class,
                    PerceptionType.class,
                    RetentionType.class,
                    SummaryDocumentsType.class,
                    VoidedDocumentsType.class,
                    AdditionalInformationType.class,
                    InvoiceLineType.class,
                    SignBatchCmd.class,
                    ConsultBatchCmd.class,
                    SignOnLineSummaryCmd.class,
                    SignOnLineCmd.class,
                    EbizResponse.class,
                    ReplicateDataCmd.class,
                    //ApplicationResponseType.class,
                    ReplicateFileCmd.class,
                    ReplicateReverseCmd.class,
                    DespatchAdviceType.class,
                    SignOnLineDespatchCmd.class,
                    SignOnLineRetentionCmd.class,
                    SignOnLinePerceptionCmd.class,
                    SignOnLineReversionCmd.class                                       
            );
            
        } catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("UnMarshallerFactoryOSEImpl Exception \n"+errors);
        }
    }
	
	@Override
	public Iterator<?> unMarshall(String filePath, String tipoDocumento, String version, boolean userDefault)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object unmarshal(String commandAsXml) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object unmarshal(File fileXml) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object unmarshalXml(File fileXml) {
        InputStream inputStream = null;
        Reader reader = null;
        try {
            Unmarshaller unmarshaller = context.createUnmarshaller();    
            return unmarshaller.unmarshal(fileXml);
        } catch (Exception e) {
        	StringWriter errors = new StringWriter();	
			e.printStackTrace(new PrintWriter(errors));
			log.error("unmarshalXml -- Exception \n"+errors);
            throw new RuntimeException("unmarshalXml -- Error al procesar Respuesta", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
            	StringWriter errors = new StringWriter();	
    			e.printStackTrace(new PrintWriter(errors));
    			log.error("unmarshalXml -- IOException \n"+errors);
            }
        }
	}
	
	@Override
	public Object unmarshalXml(Documento tbComprobante) {
        try {
            Unmarshaller unmarshaller = context.createUnmarshaller();    
            return unmarshaller.unmarshal(new ByteArrayInputStream(tbComprobante.getUbl()));
        } catch (Exception e) {
        	tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0305);	
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("unmarshalXml Exception \n"+errors);
        } 
        return null;
	}	

	@Override
	public Object convertDocumentoToBean(Documento tbComprobante, Object documentoSunat) {
		try {
			UnMarshallerSUNAT unMarshallerSUNAT = new UnMarshallerSUNAT();
			if (documentoSunat instanceof SummaryDocumentsType
                || documentoSunat instanceof VoidedDocumentsType) {        	
				return unMarshallerSUNAT.unmarshalResumenSunat(documentoSunat);            
			} else if (documentoSunat instanceof RetentionType) {
				return unMarshallerSUNAT.unmarshalRetencionSunat(documentoSunat);
			} else if (documentoSunat instanceof PerceptionType) {
				return unMarshallerSUNAT.unmarshalPercepcionSunat(documentoSunat);
			}else if(documentoSunat instanceof DespatchAdviceType){
				return unMarshallerSUNAT.unmarshalDespatchAdviceType(documentoSunat);
			}else {
				UnMarshallerOPE unMarshallerOPE = new UnMarshallerOPE();
				return unMarshallerOPE.unmarshalDocumento((IDocumentoSunat) documentoSunat);
			}
		}catch(Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0305);	
			tbComprobante.setErrorLog(e.toString());
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("convertDocumentoToBean Exception \n"+errors);
		}
		return null;
	}
	
	@Override
	public String buscarAdicional(String s, IDocumento documento) {
		return null;
	}
	

	
}
