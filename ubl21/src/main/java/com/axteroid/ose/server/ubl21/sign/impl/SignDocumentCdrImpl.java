package com.axteroid.ose.server.ubl21.sign.impl;

import java.io.*;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.*;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.FileUtil;
import com.axteroid.ose.server.ubl21.sign.SignDocumentCdr;

public class SignDocumentCdrImpl implements SignDocumentCdr{

	private static final Logger log = LoggerFactory.getLogger(SignDocumentCdrImpl.class);
    public SignDocumentCdrImpl(){}

    public void signDocumento(Documento documento){
    	log.info("signDocumento ");
    	String privateKeyAlias = Constantes.OSE_JKS_PrivateKeyAlias;
    	String privateKeyPassword = Constantes.OSE_JKS_KeyPassword;    	
        try{		
        	KeyStore ks = SignDocumentCdrKeyStore.loadStore();
        	X509Certificate cert = (X509Certificate) ks.getCertificate(privateKeyAlias);
        	if(cert==null)
    			throw new IllegalArgumentException("verifique las claves y alias del certificado (EMISOR_PRIVATE_KEY_ALIAS)");

        	PrivateKey keyEntry = (PrivateKey) ks.getKey(privateKeyAlias, privateKeyPassword.toCharArray());	
        	String xmlStr = new String(documento.getCdr());
        	//log.info("xmlStr "+xmlStr);
        	Document doc = this.buildDocumentString(xmlStr);
            Node parentNode = addExtensionContent(doc);
            String idReference = "signatureKG";
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance();
            Reference ref = fac.newReference("", fac.newDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1", null), Collections.singletonList(fac.newTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature", (TransformParameterSpec)null)), null, null);
            SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", (C14NMethodParameterSpec)null), fac.newSignatureMethod("http://www.w3.org/2000/09/xmldsig#rsa-sha1", null), Collections.singletonList(ref));
            KeyInfoFactory kif = fac.getKeyInfoFactory();
            List x509Content = new ArrayList();
            x509Content.add(cert);
            X509Data xd = kif.newX509Data(x509Content);
            KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));
            DOMSignContext dsc = new DOMSignContext(keyEntry, doc.getDocumentElement());
            XMLSignature signature = fac.newXMLSignature(si, ki);
            //log.info("signDocumento 2");
            if(parentNode != null)
                dsc.setParent(parentNode);
            dsc.setDefaultNamespacePrefix("ds");
            signature.sign(dsc);
            Element elementParent = (Element)dsc.getParent();
            if(idReference != null && elementParent.getElementsByTagName("ds:Signature") != null) {
                Element elementSignature = (Element)elementParent.getElementsByTagName("ds:Signature").item(0);
                elementSignature.setAttribute("Id", idReference);
            }
            
            String dirSecFile = Constantes.DIR_AXTEROID_OSE+Constantes.DIR_FILE;
            
            File signatureFile = new File(dirSecFile+"/R-"+documento.getNombre());
    		String signFile = FileUtil.outputDoc2File(doc, signatureFile);
    		//log.info("signDocumento \n"+signFile);
    		byte[] cdr = signFile.getBytes();
    		if(cdr!=null) {
				documento.setCdr(cdr);
				documento.setEstado(Constantes.SUNAT_CDR_GENERADO);
    		}else {
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);
				documento.setErrorLog("Existen problemas para generar el CDR");
			}
    		//FileUtil.writeToFile(signatureFile, cdr);
    		//fis.close();    		
        }
        catch (Exception e) {
        	documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			documento.setErrorLog(errors.toString());
			log.error("Exception \n"+errors);    	
        }
    }

    protected Node addExtensionContent(Document doc)
    {
        NodeList nodeList = doc.getDocumentElement().getElementsByTagNameNS("urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2", "UBLExtensions");
        Node extensions = nodeList.item(0);
        extensions.appendChild(doc.createTextNode("\n\t\t"));
        Node extension = doc.createElement("ext:UBLExtension");
        extension.appendChild(doc.createTextNode("\n\t\t\t"));
        Node content = doc.createElement("ext:ExtensionContent");
        extension.appendChild(content);
        extension.appendChild(doc.createTextNode("\n\t\t"));
        extensions.appendChild(extension);
        extensions.appendChild(doc.createTextNode("\n\t"));
        return content;
    }
    
    private Document buildDocument(InputStream inDocument) throws SignDocumentException{
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inDocument);
            return doc;
        }
        catch(ParserConfigurationException e)
        {
            throw new SignDocumentException(e);
        }
        catch(SAXException e)
        {
            throw new SignDocumentException(e);
        }
        catch(IOException e)
        {
            throw new SignDocumentException(e);
        }
    }
    
    private Document buildDocumentString(String xmlStr) {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
    
    private Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr))); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
    
//    private static byte[] readBytesFromFile(String filePath) {
//        FileInputStream fileInputStream = null;
//        byte[] bytesArray = null;
//        try {        	
//            File file = new File(filePath);
//            if(!file.exists())
//            	file = new File(OseConstantes.OSE_JKS_CERTIFICADO_WIN);
//            if(!file.exists())
//            	file = new File(OseConstantes.OSE_JKS_CERTIFICADO_WIN_D);
//            bytesArray = new byte[(int) file.length()];
//            //read file into bytes[]
//            fileInputStream = new FileInputStream(file);
//            fileInputStream.read(bytesArray);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fileInputStream != null) {
//                try {
//                    fileInputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return bytesArray;
//    }
}
