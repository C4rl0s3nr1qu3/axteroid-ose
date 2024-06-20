package com.axteroid.ose.server.rulesubl.builder.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.cert.X509Certificate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesubl.builder.UblRulesValidate;
import com.axteroid.ose.server.rulesubl.xmldoc.MessageReceiver;
import com.axteroid.ose.server.rulesubl.xmldoc.UblXmlRead;
import com.axteroid.ose.server.tools.bean.BeanResponse;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoJBossOsePropertiesEnum;
import com.axteroid.ose.server.tools.constantes.TipoParametroEnum;
import com.axteroid.ose.server.tools.exception.MyURIResolver;
import com.axteroid.ose.server.tools.exception.X509KeySelector;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.axteroid.ose.server.tools.util.StringUtil;
import com.axteroid.ose.server.tools.xml.XmlUtil;

import net.sf.saxon.Controller;
import net.sf.saxon.jaxp.TransformerImpl;
import net.sf.saxon.serialize.MessageEmitter;
import net.sf.saxon.trans.XPathException;

public class UblRulesValidateImpl implements UblRulesValidate {
	private static final Logger log = LoggerFactory.getLogger(UblRulesValidateImpl.class);
	private BeanResponse beanResponse = new BeanResponse();
	private String oseMetaInfStorageXSD = DirUtil.getJBossPropertiesValue(
			TipoJBossOsePropertiesEnum.AXTEROID_OSE_METAINF_XSD.getCodigo()).trim(); 	
	
	private String oseMetaInfStorageXSL = DirUtil.getJBossPropertiesValue(
			TipoJBossOsePropertiesEnum.AXTEROID_OSE_METAINF_XSL.getCodigo());
	
	public void service(Documento documento) {
		this.service_prod(documento);			
	}
	
	private void service_prod(Documento documento) {
		//log.info("documento.getNombre() " + documento.getNombre());
		try {
			Map<String, String> parametros = new HashMap<String, String>();	
			parametros.put("processor", "net.sf.saxon.TransformerFactoryImpl");	
			UblXmlRead ublXmlRead = new UblXmlRead();			
			ublXmlRead.getVersionUBL(documento);			
			String ambiente = DirUtil.getAmbiente();
			ReadXsdXsl read_XSD_XSL = new ReadXsdXsl();
			if(ambiente.equals(Constantes.DIR_AMB_PROD))
				read_XSD_XSL.serviceBeta(documento, parametros,"vprod");
			else if(ambiente.equals(Constantes.DIR_AMB_BETA) || ambiente.equals(""))
				read_XSD_XSL.serviceBeta(documento, parametros, "vbeta");			

 			//this.validateWithXSD(parametros.get(TipoParametroEnum.XSD.getCodigo()), documento);  
 			this.validateWithXSDPath(parametros.get(TipoParametroEnum.XSD.getCodigo()), documento);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
				return;

			this.validateSignature(documento);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
				return;
			
			if((documento.getTipoDocumento().equals(Constantes.SUNAT_GUIA_REMISION_REMITENTE)) ||
					(documento.getTipoDocumento().equals(Constantes.SUNAT_GUIA_REMISION_TRANSPORTISTA)))
				return;
				//this.validateWithXSLPath(parametros.get(TipoParametroEnum.XSL.getCodigo()), parametros.get("processor"), documento);
			else
				this.validateWithXSLPath(parametros.get(TipoParametroEnum.XSL.getCodigo()), parametros.get("processor"), documento);
				//this.validateWithXSL(parametros.get(TipoParametroEnum.XSL.getCodigo()), parametros.get("processor"), documento);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
				return;		

			if(beanResponse.getAdvertencias()!=null && !beanResponse.getAdvertencias().isEmpty()) {								
				//log.info(">>> Advertencias "+beanResponse.getAdvertencias());								
				String s = "";		
				String code = "";
				for (String advertencia : beanResponse.getAdvertencias()) {
					s = s+advertencia;	
					String observa1 = advertencia;
					String observa2 = observa1.replaceAll(Constantes.OSE_SPLIT_1, Constantes.OSE_SPLIT);
					String observa3 = observa2.replaceAll(Constantes.OSE_SPLIT_2, Constantes.OSE_SPLIT);
					log.info("3) observaciones: " + observa3);	
					String[] observa = observa3.split(Constantes.OSE_SPLIT);	
					if(!code.isEmpty())
						code= code+Constantes.OSE_SPLIT_3;
					code = code+observa[1].trim();
				}
				String sAdv = s;
				if(s.length()>Constantes.OSE_LOG_LENGTH_MAX)						
					sAdv = s.substring(0, Constantes.OSE_LOG_LENGTH_MAX);
				documento.setAdvertencia(sAdv);		
				documento.setObservaNumero(code);
				//log.info(">>>documento.getObservaNumero: " + documento.getObservaNumero());
			}			
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			if(documento.getErrorNumero()!=null){
				if(documento.getErrorNumero().isEmpty())
					documento.setErrorNumero(Constantes.SUNAT_ERROR_0305);
			}else
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0305);
			documento.setErrorLog(e.toString());
			documento.setErrorLogSunat("");			
		} 
	}
	
	private void validateWithXSD(String xsdPath, Documento documento) { 
		log.info("validateWithXSD "+xsdPath);
		try {			
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(UblRulesValidateImpl.class.getClassLoader().getResource(xsdPath));
			Validator validator = schema.newValidator();
			InputStream source = new ByteArrayInputStream(documento.getUbl());
			validator.validate(new StreamSource(source));
			log.info("1) validateWithXSD: OK ");
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0306);			
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));			
			//log.error("validateWithXSD Exception \n"+errors);  
			log.error("validateWithXSD Exception "+e.getMessage()+"\n"+e.toString()); 
		}
	}

	private void validateWithXSDPath(String xsdPath, Documento documento) { 
		log.info("validateWithXSD: {} ",xsdPath);
		try {			
        	String rutaVALI = oseMetaInfStorageXSD;
	       	 if((oseMetaInfStorageXSD!=null) && (oseMetaInfStorageXSD.equals(Constantes.DIR_RESOURCES))) {
		        	//ClassLoader classLoader = this.getClass().getClassLoader();
	       		ClassLoader classLoader = UblRulesValidateImpl.class.getClassLoader();
		    		String getPath = classLoader.getResource("").getPath();
		    		String getPathReplace = getPath.replace("file: ", "");   
		    		rutaVALI = getPathReplace+"META-INF/xsd";
	       	}
	       	String schemaValidador = (new StringBuilder()).append(rutaVALI).append(xsdPath).toString();;			
	       	log.info("schemaValidador: {} ",schemaValidador);
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(new File(schemaValidador));
			Validator validator = schema.newValidator();
			InputStream source = new ByteArrayInputStream(documento.getUbl());
			validator.validate(new StreamSource(source));
			log.info("1) validateWithXSD: OK ");
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0306);			
			documento.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));			
			//log.error("validateWithXSD Exception \n"+errors);  
			log.error("validateWithXSD Exception "+e.getMessage()+"\n"+e.toString()); 
		}
	}
	
	private void validateWithXSLPath(String xslPath, String xslProcessor, Documento documento){			
		log.info("validateWithXSLPath: {}",xslPath);	
		try {	
			log.info("oseMetaInfStorageXsl: {}",oseMetaInfStorageXSL);
			String xslMetaInfStorageXSL = (new StringBuilder())
					.append(oseMetaInfStorageXSL)
					.append(xslPath).toString();
			log.info("xslMetaInfStorageXSL: {}",xslMetaInfStorageXSL);
			MessageReceiver messageReceiver = new MessageReceiver();		
			StreamSource xslStreamSource = new StreamSource(xslMetaInfStorageXSL);	
			final File archivoXSL = new File(xslMetaInfStorageXSL);			
	    	if(archivoXSL.exists()) {   
	    		xslStreamSource = this.getXslStreamSource(xslMetaInfStorageXSL);
	    		log.info("archivoXSL.getPath(): {}",archivoXSL.getPath());
	    	}else {
	    		ClassLoader classLoader = getClass().getClassLoader();	
				xslStreamSource = new StreamSource(new InputStreamReader(classLoader.getResourceAsStream(xslPath),"UTF-8"),
						classLoader.getResource(xslPath).toURI().toString());
				log.info("archivoXSL.getName(): {}",archivoXSL.getName());
	    	}
	        
			TransformerFactory transformerFactory = TransformerFactory.newInstance(xslProcessor, null);
			Transformer transformer = transformerFactory.newTransformer(xslStreamSource);
			TransformerImpl transformerImpl = (TransformerImpl)transformer;
			final List<Object> lstWarning = new ArrayList<Object>();
	        transformerImpl.getUnderlyingController().setRecoveryPolicy(2);
	        transformerImpl.getUnderlyingController().setMessageEmitter(new MessageEmitter() {
	        	boolean abort;
	            public void startDocument(int properties) throws XPathException{
	                setWriter(new StringWriter());
	                abort = (properties & 0x4000) != 0;
	                super.startDocument(properties);
	            }

	            public void endDocument() throws XPathException {
	                XPathException de = new XPathException(getWriter().toString());
	                de.setErrorCode("XTMM9000");
	                if(abort){
//	        			StringWriter errors = new StringWriter();				
//	        			de.printStackTrace(new PrintWriter(errors));
//	        			log.error("1) transform Exception \n"+errors); 
	                } else {
//	        			StringWriter errors = new StringWriter();				
//	        			de.printStackTrace(new PrintWriter(errors));
//	        			log.error("2) transform Exception \n"+errors); 
	                    lstWarning.add(de);
	                    log.warn((new StringBuilder()).append("Observaciones XML: ").append(documento.getNombre()).append(" - ").append(de).toString());
	                    super.endDocument();
	                    return;
	                }
	            }
	            public void close(){ }
	           
	        });
	        		
			Controller controller = transformerImpl.getUnderlyingController();
			controller.setMessageEmitter(messageReceiver);
	        transformer.setErrorListener(new ErrorListener() {
	            public void warning(TransformerException transformerexception)
	                throws TransformerException{}
	
	            public void fatalError(TransformerException transformerexception)
	                throws TransformerException {}
	
	            public void error(TransformerException transformerexception)
	                throws TransformerException{}	
	        });			
			//transformer.setURIResolver(new MyURIResolver());
			transformer.setParameter("nombreArchivoEnviado", documento.getNombre());
			String igv = "0.18";
			if(documento.getLongitudNombre() == 10)
				igv = "0.10";
			transformer.setParameter("porcentajeIGV", igv);			
			InputStream source = new ByteArrayInputStream(documento.getUbl());	
			StreamSource ublFile = new StreamSource(source);
			transformer.transform(ublFile,new StreamResult(new StringWriter()));			
			log.info("3) validateWithXSLPath: OK ");
			beanResponse.setAdvertencias(messageReceiver.getWarnings());
		} catch (Exception e) {	
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));		
			String[] error = e.getMessage().split(" ");
			log.info(">>> e.getMessage(): " + e.getMessage());
			if(StringUtil.hasString(error[0]))
				documento.setErrorNumero(error[0].trim());	
			else {
				if(documento.getErrorNumero()!=null) {
					if(documento.getErrorNumero().isEmpty())
						documento.setErrorNumero(Constantes.SUNAT_ERROR_0305);
				}else
					documento.setErrorNumero(Constantes.SUNAT_ERROR_0305);		
			}
			documento.setErrorLog(e.toString());
			documento.setErrorLogSunat("");
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));			
			log.error("validateWithXSL Exception \n"+errors); 
			log.error("validateWithXSLPath Exception "+e.getMessage()+"\n"+e.toString()); 
		}
	}

	private void validateWithXSL(String xslPath, String xslProcessor, Documento documento){			
		log.info("validateWithXSL "+xslPath);
		try {			
			MessageReceiver messageReceiver = new MessageReceiver();
			ClassLoader classLoader = getClass().getClassLoader();	
			StreamSource xsltStreamSource = new StreamSource(new InputStreamReader(classLoader.getResourceAsStream(xslPath),"UTF-8"),
					classLoader.getResource(xslPath).toURI().toString());
			TransformerFactory transformerFactory = TransformerFactory.newInstance(xslProcessor, null);
			Transformer transformer = transformerFactory.newTransformer(xsltStreamSource);
			TransformerImpl transformerImpl = (TransformerImpl)transformer;
			final List<Object> lstWarning = new ArrayList<Object>();
	        transformerImpl.getUnderlyingController().setRecoveryPolicy(2);
	        transformerImpl.getUnderlyingController().setMessageEmitter(new MessageEmitter() {
	        	boolean abort;
	            public void startDocument(int properties) throws XPathException{
	                setWriter(new StringWriter());
	                abort = (properties & 0x4000) != 0;
	                super.startDocument(properties);
	            }

	            public void endDocument() throws XPathException {
	                XPathException de = new XPathException(getWriter().toString());
	                de.setErrorCode("XTMM9000");
	                if(abort){
	                    //throw de;
	                    log.warn("mensaje: {} | causa: {}",de.getMessage(), de.getCause());
	                } else {
	                    lstWarning.add(de);
	                    log.warn((new StringBuilder()).append("Observaciones XML: ").append(documento.getNombre()).append(" - ").append(de).toString());
	                    super.endDocument();
	                    return;
	                }
	            }

	            public void close(){ }
	           
	        });
	        
			//Controller controller = ((TransformerImpl) transformer).getUnderlyingController();			
			Controller controller = transformerImpl.getUnderlyingController();
			controller.setMessageEmitter(messageReceiver);
			transformer.setURIResolver(new MyURIResolver());
			transformer.setParameter("nombreArchivoEnviado", documento.getNombre());
			String igv = "0.18";
			if(documento.getLongitudNombre() == 10)
				igv = "0.10";
			transformer.setParameter("porcentajeIGV", igv);
			InputStream source = new ByteArrayInputStream(documento.getUbl());	
			StreamSource ublFile = new StreamSource(source);
			transformer.transform(ublFile,new StreamResult(new StringWriter()));			
			log.info("3) validateWithXSL: OK ");
			beanResponse.setAdvertencias(messageReceiver.getWarnings());
		} catch (Exception e) {	
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));		
			String[] error = e.getMessage().split(" ");
			log.info(">>> e.getMessage(): " + e.getMessage());
			if(StringUtil.hasString(error[0]))
				documento.setErrorNumero(error[0].trim());	
			else {
				if(documento.getErrorNumero()!=null) {
					if(documento.getErrorNumero().isEmpty())
						documento.setErrorNumero(Constantes.SUNAT_ERROR_0305);
				}else
					documento.setErrorNumero(Constantes.SUNAT_ERROR_0305);		
			}
			documento.setErrorLog(e.toString());
			documento.setErrorLogSunat("");
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));			
//			log.error("validateWithXSL Exception \n"+errors); 
			log.error("validateWithXSL Exception "+e.getMessage()+"\n"+e.toString()); 
		}
	}	
	
	private void validateSignature(Documento documento){
		//log.info("validateSignature ");
		try {
			boolean validate = false;
			Document document = XmlUtil.getByteArray2Document(documento.getUbl());
			Node signatureNode = document.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature").item(0);
			if(!(signatureNode!=null)) {
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_2334);
				documento.setErrorLogSunat("");
				return;
			}
			DOMValidateContext domValidateContext = new DOMValidateContext(new X509KeySelector(), signatureNode);
			XMLSignatureFactory xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM");
			XMLSignature xmlSignature = xmlSignatureFactory.unmarshalXMLSignature(domValidateContext);
			validate = xmlSignature.validate(domValidateContext);
			log.info("2) validateSignature : " + !validate);
			if (!validate) {
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_2334);
				documento.setErrorLogSunat("");
			}						
			SignedInfo signedInfo = xmlSignature.getSignedInfo();
			if (signedInfo != null){
				List<Reference> listReference = signedInfo.getReferences();
				for(Reference r : listReference) {
					byte[] encoded = r.getDigestValue(); 
					byte[] valueEnecoded= Base64.getMimeEncoder().encode(encoded);
					String dv = new String(valueEnecoded);		
					documento.setErrorLogSunat(dv);	
				}
			}			
			KeyInfo keyInfo = xmlSignature.getKeyInfo();
			if (keyInfo != null){
				Iterator<XMLStructure> ki = keyInfo.getContent().iterator();
		        while (ki.hasNext()) {
		            XMLStructure info = (XMLStructure) ki.next();
		            if (!(info instanceof X509Data))
		                continue;
		            X509Data x509Data = (X509Data) info;
		            Iterator xi = x509Data.getContent().iterator();
		            while (xi.hasNext()) {
		                Object o = xi.next();
		                if (!(o instanceof X509Certificate))
		                    continue;		      
		                String sn = ((X509Certificate)o).getSerialNumber().toString();	
		                BigInteger bi = new BigInteger(sn);		     		                
		                                
		                String dateNotAfter = DateUtil.dateFormat(((X509Certificate)o).getNotAfter(), "yyyy-MM-dd HH:mm:ss");
		                String dategetNotBefore = DateUtil.dateFormat(((X509Certificate)o).getNotBefore(), "yyyy-MM-dd HH:mm:ss");	
		                
		                String val = (new StringBuilder(String.valueOf(bi.toString(16)))).append(Constantes.OSE_SPLIT).append(documento.getErrorLogSunat()).append(Constantes.OSE_SPLIT).append(dategetNotBefore).append(Constantes.OSE_SPLIT).append(dateNotAfter).toString();                
		                documento.setErrorLogSunat(val);			                
		            }
		        }
			}	
            log.info("2) SerialNumber @ HashValue @ dategetNotBefore @ dateNotAfter ====> "+documento.getErrorLogSunat());
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			if(documento.getErrorNumero()!=null) {
				if(documento.getErrorNumero().isEmpty())
					documento.setErrorNumero(Constantes.SUNAT_ERROR_0305);
			}else
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0305);
			documento.setErrorLog(e.toString());
			documento.setErrorLogSunat("");
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			//log.error("validateSignature Exception \n"+errors);	
			log.error("validateSignature Exception "+e.getMessage()+"\n"+e.toString()); 

		}
	}
	
    private StreamSource getXslStreamSource(String inputXSL){
        File xsltFile = new File(inputXSL);
		StreamSource xlstStreamSource = new StreamSource(xsltFile);
        return xlstStreamSource;
    }	
	
}
