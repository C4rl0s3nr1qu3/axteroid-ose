package com.axteroid.ose.server.sunat.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SOAPSigner {

	private static final String ENCODING = "utf-8";
	public static final String sep = System.getProperty("line.separator");

	protected Certificado certificado;
	protected XMLSignatureFactory signatureFactory;
	protected CanonicalizationMethod canMethod;
	protected SignatureMethod signatureMethod;
	protected DigestMethod digestMethod;
	protected KeyInfo keyInfo;

	public SOAPSigner(Certificado certificado) throws Exception {

		try {
			this.certificado = certificado;

			// First, create a DOM XMLSignatureFactory that will be used to
			// generate the XMLSignature and marshal it to DOM.
			String providerName = System.getProperty("jsr105Provider", "org.jcp.xml.dsig.internal.dom.XMLDSigRI");
			signatureFactory = XMLSignatureFactory.getInstance("DOM", (Provider) Class.forName(providerName).newInstance());

			
			List<String> prefixList = new ArrayList<String>(); 
			prefixList.add("soapenv"); 
			C14NMethodParameterSpec c14NMethodParameterSpec = new ExcC14NParameterSpec(prefixList);
			
			/*Creamos parte de la Info de Firma para disminuir el Overhead*/
			canMethod = signatureFactory.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, c14NMethodParameterSpec);
			
			signatureMethod = signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null);

			/*Creamos el DigestMethod*/
			digestMethod = signatureFactory.newDigestMethod(DigestMethod.SHA1, null);

			String tokenReference = 
				"<wsse:SecurityTokenReference xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
				"<wsse:Reference URI=\"#X509-C46279C6030251FE7D1485355223199575\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\"/>" +
				"</wsse:SecurityTokenReference>";
			
			KeyInfoFactory keyFactory = signatureFactory.getKeyInfoFactory();
			DOMStructure domKeyInfo = new DOMStructure(stringToDocument(tokenReference).getDocumentElement());
			keyInfo = keyFactory.newKeyInfo(Collections.singletonList(domKeyInfo));
			
			/*Creamos la informacion de Llaves (Publica y Certificado)*/
//			KeyInfoFactory kif = signatureFactory.getKeyInfoFactory();
//			KeyValue kv1 = kif.newKeyValue(certificado.getClavePublica());
//			X509Data kv2 = kif.newX509Data(Collections.singletonList(certificado.getX509Certificate()));
//
//			List<XMLStructure> list = new LinkedList<XMLStructure>();
////			list.add(kv1);
////			list.add(kv2);
//
//			keyInfo = kif.newKeyInfo(list);
		} catch(Throwable e) {
			e.printStackTrace();
			throw new Exception("Llaves de firma invalidas", e);
		}
		
		//certificado.isCertificadoValido();
	}
	
	/**
	 * Firma Detached
	 * ==============
	 * Firma un nodo del XML y lo incluye en el XML en otro nodo.
	 * 
	 */
	public String firmar(String contenidoXml, String tagAFirmar, String tagContenedor) throws Exception {

		try {
			Document document = stringToDocument(contenidoXml);

			Element eFirma = (Element) ((document.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", tagAFirmar)).item(0));
			Element eContenedor = (Element) ((document.getElementsByTagNameNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", tagContenedor)).item(0));

			Element signature = document.createElement("Signature");
			eContenedor.appendChild(signature);

			String idReferencia = "#" + eFirma.getAttributeNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Id");

			List<String> prefixList = new ArrayList<String>(); 
			C14NMethodParameterSpec c14NMethodParameterSpec = new ExcC14NParameterSpec(prefixList);
			
			List<Transform> transformList = new ArrayList<Transform>() ;
			Transform exc14nTransform = signatureFactory.newTransform(CanonicalizationMethod.EXCLUSIVE, c14NMethodParameterSpec);
	        transformList.add(exc14nTransform) ;
	        
			Reference ref = signatureFactory.newReference(idReferencia, digestMethod, transformList, null, null);

			SignedInfo si = signatureFactory.newSignedInfo(canMethod, signatureMethod, Collections.singletonList(ref));

			XMLSignature signer = signatureFactory.newXMLSignature(si, keyInfo);

			DOMSignContext signContext = new DOMSignContext(certificado.getPrivateKey(), signature);
			signContext.setDefaultNamespacePrefix("ds");
//			signContext.setIdAttributeNS(document,null,"ID");
			signContext.setIdAttributeNS(eFirma,"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd","Id"); // Agregado para resolver error "Cannot resolve element with ID XXXXXX", producido desde jva 1.7.0_5
			
			signer.sign(signContext);

			signature.getParentNode().replaceChild(signature.getFirstChild(), signature);
			
			return "<?xml version=\"1.0\" encoding=\""+ENCODING+"\"?>" + sep + documentToString(document);
		} catch(Throwable e) {
			e.printStackTrace();
			throw new Exception("Error al firmar documento (detached)", e);
		}
		
	}
	
	
	/**
	 * Crea un Document a partir de un String
	 * @param xmlString
	 * @return
	 * @throws Exception
	 */
	private Document stringToDocument(String xmlString) throws Exception {

		ByteArrayInputStream bais = new ByteArrayInputStream(xmlString.getBytes(ENCODING));
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().parse(bais);
		xmlString = null;

		return doc;
	}

	/**
	 * Crear un String a partir de un Document
	 * 
	 * @param nodo
	 * @return
	 * @throws TransformerException
	 * @throws UnsupportedEncodingException
	 */
	private String documentToString(Node nodo) throws Exception, UnsupportedEncodingException {
		/*String resultXML = null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		Properties props = new Properties();
		props.put(OutputKeys.ENCODING, ENCODING);
		props.put(OutputKeys.INDENT, "no");
		props.put(OutputKeys.OMIT_XML_DECLARATION, "yes");
		trans.setOutputProperties(props);
		trans.transform(new DOMSource(nodo), new StreamResult(baos));

		resultXML = new String(baos.toByteArray(), ENCODING);

		return resultXML;*/
		
		String returnValue = null;
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		DOMSerializer serializer = new DOMSerializer();
		
		serializer.setOutputStream(bos);
		serializer.setWhitespaceIgnoring(false);
		serializer.setLineBreaking(false);
		serializer.setIndentation("");
		serializer.setEncoding(ENCODING);
		serializer.setDeclarationOmitting(true);
		
		serializer.serialize(nodo);
		
		bos.flush();

		returnValue = bos.toString();
		
		bos.close();
		return returnValue;
	}
	
	public static void main(String[] args) throws Throwable {
		System.setProperty("file.encoding", "utf-8");
		System.setProperty("sun.io.unicode.encoding", "utf-8");
		
	    String xml = new String(Files.readAllBytes(new File("/home/crosas/Escritorio/soap-new.xml").toPath()));
	   
		Certificado certificado = CertificateLoader.getCertificado("c:/home/axteroid/AXTEROID20610051406.pfx", "0710");
		SOAPSigner firmador = new SOAPSigner(certificado);
		System.out.println(firmador.firmar(xml, "Body", "Security"));
		
		
	}
	
}
