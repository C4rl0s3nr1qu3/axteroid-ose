package com.axteroid.ose.server.rulesubl.builder.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.cert.Extension;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;

//import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.x509.X509AttributeIdentifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.exception.X509KeySelector;
import com.axteroid.ose.server.tools.xml.XmlUtil;

public class ReadCertificate {
	private static final Logger log = LoggerFactory.getLogger(ReadCertificate.class);

	public void getCertificateData(Documento documento) {
		//log.info("getCertificateData ");
		try {
			boolean validate = false;
			String xmlRecords = new String(documento.getUbl());
			Document document = XmlUtil.getString2Document(xmlRecords);  
			Node signatureNode = document.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature").item(0);
			DOMValidateContext domValidateContext = new DOMValidateContext(new X509KeySelector(), signatureNode);
			XMLSignatureFactory xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM");
			XMLSignature xmlSignature = xmlSignatureFactory.unmarshalXMLSignature(domValidateContext);					
			SignedInfo signedInfo = xmlSignature.getSignedInfo();
			if (signedInfo != null){
				List<Reference> listReference = signedInfo.getReferences();
				for(Reference r : listReference) {
					byte[] encoded = r.getDigestValue(); 
					byte[] valueEnecoded= Base64.getMimeEncoder().encode(encoded);
					String dv = new String(valueEnecoded);		
					documento.setErrorLogSunat(dv);	
					log.info("DigestValue ---> "+dv );
				}
			}			
			KeyInfo keyInfo = xmlSignature.getKeyInfo();
			if (keyInfo != null){
				Iterator ki = keyInfo.getContent().iterator();
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
		                String val = sn+Constantes.OSE_SPLIT+documento.getErrorLogSunat();
		                documento.setErrorLogSunat(val);		
		                log.info("SerialNumber ---> "+sn );
		                BigInteger bi = new BigInteger(sn);
		                log.info(bi.toString(16));
		                //this.validarCertificate(o);
		            }
		        }
			}	
			//this.validarCertificate(archivoXML, tbComprobante);
			log.info("tbComprobante.getErrorLogSunat(): "+documento.getErrorLogSunat());
		} catch (Exception e) {
			documento.setErrorLog(e.toString());
			documento.setErrorLogSunat("");
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCertificateData Exception \n"+errors);	
		}	
	}
	
	public void validarCertificate(Object o) {
		try {
            log.info("1) *********************************************************** "); 

            log.info("X509Certificate.getBasicConstraints: "+((X509Certificate)o).getBasicConstraints());
            log.info("X509Certificate.getExtendedKeyUsage: "+((X509Certificate)o).getExtendedKeyUsage()); // NULL
            log.info("X509Certificate.getIssuerAlternativeNames: "+((X509Certificate)o).getIssuerAlternativeNames()); // NULL		                
            log.info("X509Certificate.getIssuerDN: "+((X509Certificate)o).getIssuerDN());
            log.info("X509Certificate.getIssuerDN.getName(): "+((X509Certificate)o).getIssuerDN().getName());
            log.info("X509Certificate.getIssuerUniqueID: "+((X509Certificate)o).getIssuerUniqueID()); // NULL
            log.info("X509Certificate.getIssuerX500Principal: "+((X509Certificate)o).getIssuerX500Principal());
            log.info("X509Certificate.getSerialNumber: "+((X509Certificate)o).getSerialNumber().byteValue());
            log.info("X509Certificate.getSerialNumber: "+((X509Certificate)o).getSerialNumber());
            log.info("X509Certificate.getSigAlgName: "+((X509Certificate)o).getSigAlgName());
            log.info("X509Certificate.getSigAlgOID: "+((X509Certificate)o).getSigAlgOID());
            log.info("X509Certificate.getSigAlgParams: "+((X509Certificate)o).getSigAlgParams());// NULL
            log.info("X509Certificate.getSignature: "+((X509Certificate)o).getSignature());
            log.info("X509Certificate.getSubjectAlternativeNames: "+((X509Certificate)o).getSubjectAlternativeNames());	// NULL	                
            log.info("X509Certificate.getSubjectDN: "+((X509Certificate)o).getSubjectDN()); // Denin
            log.info("X509Certificate.getSubjectUniqueID: "+((X509Certificate)o).getSubjectUniqueID()); // NULL	
            log.info("X509Certificate.getSubjectX500Principal: "+((X509Certificate)o).getSubjectX500Principal()); 	
            log.info("X509Certificate.getVersion: "+((X509Certificate)o).getVersion());            
            log.info("X509Certificate.getCriticalExtensionOIDs: "+((X509Certificate)o).getCriticalExtensionOIDs());
            log.info("X509Certificate.getNonCriticalExtensionOIDs: "+((X509Certificate)o).getNonCriticalExtensionOIDs());		                
            log.info("X509Certificate.getPublicKey: "+((X509Certificate)o).getPublicKey());		                
            log.info("X509Certificate.getType: "+((X509Certificate)o).getType());	
            log.info("X509Certificate.getType: "+((X509Certificate)o).getNotAfter());
            log.info("2) *********************************************************** "); 
            
            this.getBase64_Encoder(((X509Certificate)o).getSignature(), "getSignature");
            
            if (((X509Certificate)o).getExtendedKeyUsage() != null){
            	for(String str : ((X509Certificate)o).getExtendedKeyUsage()) {
            		log.info("getExtendedKeyUsage ---> " + str);	
            	}
            }
            
            List<String> listExtensionValue = new ArrayList<String>();
            listExtensionValue.add("2.5.29.14");
            listExtensionValue.add("2.5.29.15");
            listExtensionValue.add("2.5.29.16");
            listExtensionValue.add("2.5.29.17");
            listExtensionValue.add("2.5.29.18");
            listExtensionValue.add("2.5.29.19");
            listExtensionValue.add("2.5.29.30");
            listExtensionValue.add("2.5.29.33");
            listExtensionValue.add("2.5.29.35");
            listExtensionValue.add("2.5.29.36");
            
            for(String str : listExtensionValue)
            	this.getExtensionValue(o, str);
            
            log.info("3) *********************************************************** ");
            
            for(String str : ((X509Certificate)o).getCriticalExtensionOIDs())            	            	
            	this.getExtensionValue(o, str);
                        
            for(String str : ((X509Certificate)o).getNonCriticalExtensionOIDs())            			                	
            	this.getExtensionValue(o, str);
                                    
            log.info("4) *********************************************************** "); 
            		                
            byte[] tbs  = ((X509Certificate)o).getTBSCertificate();		                
            
            StringBuffer buf = new StringBuffer();
            TBSCertificateStructure tbscs = TBSCertificateStructure.getInstance(ASN1Sequence.fromByteArray(tbs));
            log.info("tbscs.getVersion: "+tbscs.getVersion());
            log.info("tbscs.getSubjectPublicKeyInfo: "+tbscs.getSubjectPublicKeyInfo());
            log.info("tbscs.getSubjectPublicKeyInfo.getAlgorithm: "+tbscs.getSubjectPublicKeyInfo().getAlgorithm());
            log.info("tbscs.getIssuer: "+tbscs.getIssuer());
            log.info("tbscs.getSerialNumber.getValue: "+tbscs.getSerialNumber().getValue());
            
            ASN1Sequence ans = (ASN1Sequence) ASN1Sequence.fromByteArray(tbs);
            if (ans instanceof ASN1Primitive){
            	log.info("ASN1Primitive ---> ");
            }else if (ans instanceof ASN1Object){
            	log.info("ASN1Object ---> ");	
            }else if (ans instanceof ASN1Encodable){
            	log.info("ASN1Encodable ---> ");
            }else if (ans instanceof ASN1String){
                ASN1String asn1 = (ASN1String)ans;
                String decoded = asn1.getString();
                log.info("ASN1String ---> " + decoded); 
/*            }else if (ans instanceof X500Name){
            	log.info("ASN1Encodable ---> ");*/
            }else if (ans instanceof X509AttributeIdentifiers){
            	log.info("X509AttributeIdentifiers ---> ");
            }else if (ans instanceof Extension){
            	log.info("Extension ---> ");            
            }
            this.getASN1Primitive(tbscs.getEncoded(), "tbscs.getEncoded()");	
            this.getBase64_Encoder(tbscs.getEncoded(), "tbscs.getEncoded()");                       
            this.getBase64_Encoder(tbs, "X509Certificate");
            //String c1 = new String(tbs);
            //log.info("X509Certificate.getTBSCertificate: "+c1 );
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);	
		}	
	}
	
	private void getExtensionValue(Object o, String str) throws IOException {
    	byte[] id = ((X509Certificate)o).getExtensionValue(str);    	
    	if (id != null){
    		String sID = new String(id);
        	log.info("ExtensionOIDs  --> " + str+ " ---> "+sID);
            this.getASN1Primitive(id, str);	    
            this.getBase64_Encoder(id, str);
            //ASN1Primitive extensionValue = JcaX509ExtensionUtils.parseExtensionValue(id);
            //log.info("extensionValue ---> " + extensionValue.toString());
        }
	}
	
	private ASN1Primitive getASN1Primitive(byte[] data) throws IOException{		
		if (data != null){
			ByteArrayInputStream inStream = new ByteArrayInputStream(data);
			ASN1InputStream asnInputStream = new ASN1InputStream(inStream);			
			return asnInputStream.readObject();
		}
		return null;
	}
	
	private String getASN1Primitive(byte[] data, String etiqueta) throws IOException{
		String decoded = null;
		if (data != null){
	        ASN1Primitive derObject = getASN1Primitive(data);
	        if (derObject instanceof DEROctetString){
	            DEROctetString derOctetString = (DEROctetString) derObject;
	            derObject = getASN1Primitive(derOctetString.getOctets());
	            if (derObject instanceof ASN1String){
	                ASN1String s = (ASN1String)derObject;
	                decoded = s.getString();	                
	                log.info(etiqueta+" ASN1String: "+decoded);
	            }
	        }
	    }
		return decoded;
	}
	
	
	private void getBase64_Encoder(byte[] b, String etiqueta) {
        String encodedCertString = new String(Base64.getEncoder().encode(b));		                
        log.info(etiqueta+" Base64.getEncoder: "+encodedCertString);
	}
	
	//private void validarCertificate(File archivoXML,TbComprobante tbComprobante) {
	private void validarCertificate(Documento documento) {	
		boolean validate = false;
		try {						       
			String xmlRecords = new String(documento.getUbl());
			Document document = XmlUtil.getString2Document(xmlRecords);  
			Node signatureNode = document.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature").item(0);
			log.info("signatureNode: " + signatureNode.getTextContent());			
			DOMValidateContext domValidateContext = new DOMValidateContext(new X509KeySelector(), signatureNode);
			XMLSignatureFactory xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM");
			XMLSignature xmlSignature = xmlSignatureFactory.unmarshalXMLSignature(domValidateContext);
			validate = xmlSignature.validate(domValidateContext);
			//log.info(">>> Alterado: " + !validate);			
			log.info("xmlSignature ... "+xmlSignature.toString());
			
			NodeList nl_0 = signatureNode.getChildNodes();
			for(int a=0; a < nl_0.getLength(); a++){
				Node node_0 = nl_0.item(a);				
				log.info(a+" node: " +node_0.getNodeName());		
				NodeList nl_1 = node_0.getChildNodes();
				for(int b=0; b < nl_1.getLength(); b++){
					Node node_1 = nl_1.item(b);				
					log.info(b+" node_1 : " +node_1.getNodeName());
					NodeList nl_2 = node_1.getChildNodes();
					for(int c=0; c < nl_2.getLength(); c++){
						Node node_2 = nl_2.item(c);				
						log.info(b+" getNodeName : " +node_2.getNodeName());
						log.info(b+" getNodeType : " +node_2.getNodeType());
						log.info(b+" getTextContent : " +node_2.getTextContent());
						log.info(b+" getNodeValue : " +node_2.getNodeValue());
						log.info(b+" getLocalName : " +node_2.getLocalName());
					}
				}
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);	
		}		
	}
	
	private void getRegexSummaryDocumentsLineType_1(File archivoXML,Documento documento) {
	//private void getRegexSummaryDocumentsLineType(TbComprobante tbComprobante) {
		try {
			FileInputStream fis = new FileInputStream(archivoXML);		
			ByteArrayOutputStream bout = new ByteArrayOutputStream();		
			byte[] buffer = new byte[500];
			int leido = -1;		
			while((leido=fis.read(buffer))!=-1) {
				bout.write(buffer,0,leido);
			}
			fis.close();
			bout.close();		
			String cad= new String(bout.toByteArray());		
			Pattern patron = Pattern.compile("X509Certificate>(.*?)<\\/");		
			Matcher m = patron.matcher(cad);	
			if(m.find()) {
				System.out.println(m.group(1));
			}
		
			//CertificateFactory fac = CertificateFactory.getInstance("X509");
			//fac.generateCertificate(new ByteArrayInputStream());
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);	
		}
	}
}
