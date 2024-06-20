package com.axteroid.ose.server.ubl21.gateway.sunat.dozer21;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.dozer.CustomConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3._2000._09.xmldsig_.CanonicalizationMethodType;
import org.w3._2000._09.xmldsig_.DigestMethodType;
import org.w3._2000._09.xmldsig_.KeyInfoType;
import org.w3._2000._09.xmldsig_.ReferenceType;
import org.w3._2000._09.xmldsig_.SignatureMethodType;
import org.w3._2000._09.xmldsig_.SignatureType;
import org.w3._2000._09.xmldsig_.SignatureValueType;
import org.w3._2000._09.xmldsig_.SignedInfoType;
import org.w3._2000._09.xmldsig_.TransformType;
import org.w3._2000._09.xmldsig_.TransformsType;
import org.w3._2000._09.xmldsig_.X509DataType;
import org.w3c.dom.Document;

import com.axteroid.ose.server.tools.algoritmo.NcCrypt;
import com.axteroid.ose.server.tools.constantes.Constantes;

import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2_1.ExtensionContentType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2_1.UBLExtensionType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2_1.UBLExtensionsType;

public class StringToSignTypeConverter implements CustomConverter {
	//private static final Logger log = Logger.getLogger(StringToSignTypeConverter.class);
	private static final Logger log = LoggerFactory.getLogger(StringToSignTypeConverter.class);
    @SuppressWarnings("rawtypes")
	public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {
        
    	String value = (String) sourceFieldValue;
        if (StringUtils.isBlank(value))  return null;
        String[] values = StringUtils.split(value, "|");        
/*        char [] kestorePassword = OseConstantes.OSE_JKS_KestorePassword.toCharArray();
        String privateKeyPassword = NcCrypt.desencriptarPassword(OseConstantes.OSE_JKS_PrivateKeyPassword);
        String privateKeyAlias = OseConstantes.OSE_JKS_PrivateKeyAlias;*/
        char [] kestorePassword = values[0].toCharArray();
        String privateKeyPassword = NcCrypt.desencriptarPassword(values[1]);
        String privateKeyAlias = values[2];
        
        try {
            KeyStore ks = KeyStore.getInstance(Constantes.OSE_JKS_TYPE);  
            byte[] bVal3 = Base64.getDecoder().decode(values[3]); 
            InputStream fis = new ByteArrayInputStream(bVal3);
         
            ks.load(fis, kestorePassword);            
            X509Certificate cert = (X509Certificate) ks.getCertificate(privateKeyAlias);
            if(cert==null) return null;
                        
            log.info("cert  \n"+cert);
            log.info("cert.toString().getBytes()  \n "+cert.toString().getBytes());
            log.info("cert.getPublicKey() "+cert.getPublicKey());
            log.info("cert.getVersion() "+cert.getVersion());
            log.info("cert.getSigAlgName() "+cert.getSigAlgName()+" - "+String.valueOf(cert.getSigAlgName()));
            log.info("cert.getSignature() "+cert.getSignature());
            log.info("cert.hashCode() "+cert.hashCode());
                       
            PrivateKey privateKey = (PrivateKey) ks.getKey(privateKeyAlias, privateKeyPassword.toCharArray());   
            
            log.info("privateKey.hashCode() "+privateKey.hashCode());
            log.info("privateKey "+privateKey);
            log.info("privateKey.getAlgorithm() "+privateKey.getAlgorithm());
            log.info("privateKey.getFormat() "+privateKey.getFormat());            
            log.info("privateKey.getFormat() "+privateKey);  
            
            DigestMethodType digestMethod = new DigestMethodType();
            digestMethod.setAlgorithm(Constantes.CDR_DIGEST_METHOD);
            
            TransformType transform = new TransformType();
            transform.setAlgorithm(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
            
            TransformsType transforms = new TransformsType();
            //transforms.insertTransformType(transform);
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);                        
            Document doc = dbf.newDocumentBuilder().newDocument();
            XMLSignature xmlSignature = new XMLSignature(doc, "", XMLSignature.ALGO_ID_SIGNATURE_RSA);
            xmlSignature.sign(privateKey);
            KeyInfo k = xmlSignature.getKeyInfo();
            k.getSecretKey();
            k.getX509Certificate();
            
            
            ReferenceType reference = new ReferenceType();
            reference.setURI("");
            reference.setTransforms(transforms);
            reference.setDigestMethod(digestMethod);
            byte [] bDigestValue = ByteBuffer.allocate(4).putInt(privateKey.hashCode()).array();
            reference.setDigestValue(bDigestValue);
            
            SignatureMethodType signatureMethod = new SignatureMethodType();
            signatureMethod.setAlgorithm(XMLSignature.ALGO_ID_SIGNATURE_RSA);
            
            CanonicalizationMethodType canonicalizationMethod = new CanonicalizationMethodType();
            canonicalizationMethod.setAlgorithm(Constantes.CDR_CANONICALIZATION_METHOD);

            SignedInfoType signedInfo = new SignedInfoType();
            signedInfo.setCanonicalizationMethod(canonicalizationMethod);
            signedInfo.setSignatureMethod(signatureMethod);
            //signedInfo.insertReferenceType(reference);
            
            SignatureValueType signatureValue = new SignatureValueType();                 
            signatureValue.setValue(privateKey.getEncoded());            
            
            X509DataType x509Data = new X509DataType();                  
            //x509Data.setX509Certificate(cert.getEncoded());
            //x509Data.setX509Certificate(k.getX509Certificate().getEncoded());
            
            KeyInfoType keyInfo = new KeyInfoType();                        
            //keyInfo.setX509Data(x509Data);
            
            SignatureType signatureType = new SignatureType();
            signatureType.setId(Constantes.CDR_SIGNATURE_ID);
            signatureType.setSignedInfo(signedInfo);
            signatureType.setSignatureValue(signatureValue);
            signatureType.setKeyInfo(keyInfo);
            
            ExtensionContentType extensionContent = new ExtensionContentType();
            //extensionContent.setSignature(signatureType);
            
            UBLExtensionType ublExtension = new UBLExtensionType();
            ublExtension.setExtensionContent(extensionContent);
            
            UBLExtensionsType ublExtensions = new UBLExtensionsType();
            //ublExtensions.insertUBLExtensionType(ublExtension);
            
            return ublExtensions;
            
    	}catch(Exception e) {
    		StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));			
			log.error("Exception \n"+errors);  
			
    	}
        return null;

    }
}
