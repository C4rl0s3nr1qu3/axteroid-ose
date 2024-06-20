package com.axteroid.ose.server.ubl21.sign.impl;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.axteroid.ose.server.tools.bean.CertificadoEmisor;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.axteroid.ose.server.tools.util.FileUtil;
import com.axteroid.ose.server.ubl21.sign.SignDocumentUbl;

public class SignDocumentUblImpl implements SignDocumentUbl{
	private final static Logger log = LoggerFactory.getLogger(SignDocumentUblImpl.class);
	
    public SignDocumentUblImpl(){}

    public byte[] signDocumento(byte[] bytes, String rutaCPE, String id, Date fechaEmision){    	
    	CertificadoEmisor certificadoEmisor = DirUtil.getCertificadoEmisor(id, fechaEmision);
    	log.info("signDocumento INICIO id: "+id+" | fechaEmision: "+fechaEmision+" | "+certificadoEmisor.toString());  	
    	log.info("getKeyPassword() "+certificadoEmisor.getKeyPassword()+" | getPrivateKeyAlias() "+certificadoEmisor.getPrivateKeyAlias()+" | getKeyStorePassword(); "+certificadoEmisor.getKeyStorePassword()+" | getCerticate(); "+certificadoEmisor.getCertificate());
    	if((certificadoEmisor.getKeyPassword().isEmpty()) ||
    			certificadoEmisor.getPrivateKeyAlias().isEmpty() ||
    			certificadoEmisor.getKeyStorePassword().isEmpty() ||
    			certificadoEmisor.getCertificate().isEmpty())
    		return new byte[0];
    	String privateKeyAlias = certificadoEmisor.getPrivateKeyAlias();
    	String privateKeyPassword = certificadoEmisor.getKeyPassword();
    	
        try{	
        	KeyStore ks = SignDocumentUblKeyStore.loadStore(certificadoEmisor.getKeyStorePassword(),
        			certificadoEmisor.getCertificate());
        	X509Certificate cert = (X509Certificate) ks.getCertificate(privateKeyAlias);
        	if(cert==null)
    			throw new IllegalArgumentException("verifique las claves y alias del certificado (EMISOR_PRIVATE_KEY_ALIAS)");

        	log.info("privateKeyAlias: "+privateKeyAlias+" | privateKeyPassword: "+privateKeyPassword+" | privateKeyPassword.toCharArray(): "+privateKeyPassword.toCharArray());

        	PrivateKey keyEntry = (PrivateKey) ks.getKey(privateKeyAlias, privateKeyPassword.toCharArray());	
        	String xmlStr = new String(bytes);
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
            if(idReference != null && elementParent.getElementsByTagName("ds:Signature") != null){
                Element elementSignature = (Element)elementParent.getElementsByTagName("ds:Signature").item(0);
                elementSignature.setAttribute("Id", idReference);
            }
            
            File signatureFile = new File(rutaCPE);
    		String signFile = FileUtil.outputDoc2File(doc, signatureFile);
    		//log.info("signDocumento \n"+signFile);
    		byte[] ubl = signFile.getBytes();
    		FileUtil.writeToFile(signatureFile, ubl);
    		log.info("signDocumento FIN");
    		return ubl;
        }catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);    	
        }
        return new byte[0];
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
    
    
    
}
