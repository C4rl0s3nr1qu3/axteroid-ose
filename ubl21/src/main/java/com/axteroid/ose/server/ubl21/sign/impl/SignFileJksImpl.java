package com.axteroid.ose.server.ubl21.sign.impl;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.util.FileUtil;
import com.axteroid.ose.server.ubl21.sign.SignFileJks;

import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;


public class SignFileJksImpl implements SignFileJks {
	private static final Logger log = LoggerFactory.getLogger(SignFileJksImpl.class);
    private static final String KEYSTORE_TYPE = "JKS";
    
    public void init() {
        org.apache.xml.security.Init.init();
    }
	
    private Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
    
    public void sign(Documento documento) {
    	init();
    	String xmlStr = new String(documento.getCdr());
    	Document doc = this.convertStringToDocument(xmlStr);
//    	String jks_Certificado = OseConstantes.OSE_JKS_CERTIFICADO_LINUX;
//    	String keyStorePassword = OseConstantes.OSE_JKS_StorePassword;
    	String privateKeyPassword = Constantes.OSE_JKS_KeyPassword;
    	String privateKeyAlias = Constantes.OSE_JKS_PrivateKeyAlias;
 	    
    	try {
//    		KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
//    		byte[] bVal3 = ParseSignatureFileJKSImpl.readBytesFromFile(jks_Certificado);
//    		InputStream fis = new ByteArrayInputStream(bVal3);   		   		
//    		ks.load(fis, keyStorePassword.toCharArray());    	
    		KeyStore ks = SignDocumentCdrKeyStore.loadStore();
    		X509Certificate cert = (X509Certificate) ks.getCertificate(privateKeyAlias);
    		if(cert==null)
    			throw new IllegalArgumentException("verifique las claves y alias del certificado (EMISOR_PRIVATE_KEY_ALIAS)");
    		
    		PrivateKey privateKey = (PrivateKey) ks.getKey(privateKeyAlias, privateKeyPassword.toCharArray());
    		XMLSignature xmlSignature = new XMLSignature(doc, "", XMLSignature.ALGO_ID_SIGNATURE_RSA);
    		xmlSignature.getElement().setAttribute("Id", "signatureKG");

    		Element elemento = doc.getDocumentElement();
    		NodeList lista1 = elemento.getChildNodes();
    		Node segundoNodo = lista1.item(0);

    		Element extensionContentType = doc.createElement("ext:ExtensionContent");
    		extensionContentType.appendChild(xmlSignature.getElement());
    		Element ublExtensionsType = doc.createElement("ext:UBLExtensions");
    		Element ublExtensionType = doc.createElement("ext:UBLExtension");
    		ublExtensionType.appendChild(extensionContentType);
    		ublExtensionsType.appendChild(ublExtensionType);
    		if (segundoNodo != null && segundoNodo.getNodeName().startsWith("ext:UBLExtensions")) 
    			segundoNodo.appendChild(ublExtensionType);
    		else 
    			elemento.insertBefore(ublExtensionsType, elemento.getFirstChild());
    		
    	    NodeList nodeList = doc.getDocumentElement().getElementsByTagNameNS("urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2", "ExtensionContent");
    		
    		Transforms transforms = new Transforms(doc);
    		transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
    		xmlSignature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
    		xmlSignature.addKeyInfo(cert);
    		xmlSignature.addKeyInfo(cert.getPublicKey());
    		xmlSignature.sign(privateKey);
    		
    		File signatureFile = new File(Constantes.DIR_AXTEROID_OSE+Constantes.DIR_FILE+"/R-"+documento.getNombre());
    		String signFile = FileUtil.outputDoc2File(doc, signatureFile);
    		
    		byte[] cdr = signFile.getBytes();
    		if(cdr!=null)
				documento.setCdr(cdr);
			else {
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);
				documento.setErrorLog("Problemaa para generar el CDR2.1");
			}
    		// REVISAR SI ES NECESARIA EJECUTAR ESTE COMANDO
    		FileUtil.writeToFile(signatureFile, cdr);
//    		fis.close();    		
		} catch (Exception e) {
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
			documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);	
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			documento.setErrorLog(errors.toString());
			log.error("Exception \n"+errors);
		}	
    }       
}
