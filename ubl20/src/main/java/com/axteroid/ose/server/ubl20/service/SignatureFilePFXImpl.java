package com.axteroid.ose.server.ubl20.service;

import com.axteroid.ose.server.tools.util.FileUtil;
import com.axteroid.ose.server.ubl20.gateway.ClienteContext;

import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: RAC
 * Date: 06/03/12
 */

//@Component
public class SignatureFilePFXImpl implements SignatureFilePFX {
    //private static final Logger log = Logger.getLogger(SignatureFilePFXImpl.class);
    private static final Logger log = LoggerFactory.getLogger(SignatureFilePFXImpl.class);
    private static final String KEYSTORE_TYPE = "PKCS12";

    String providerName;

    XMLSignatureFactory fac;

    SignedInfo si;

    @PostConstruct
    public void init() {
        try {

            providerName = System.getProperty(
                    "jsr105Provider",
                    "org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI"
            );

            fac = XMLSignatureFactory.getInstance(
                    "DOM",
                    (Provider) Class.forName(providerName).newInstance()
            );

            List transforms = null;

            transforms = Collections.singletonList(
                    fac.newTransform(
                            Transform.ENVELOPED,
                            (TransformParameterSpec) null
                    )
            );
            Reference ref = fac.newReference(
                    "",
                    fac.newDigestMethod(DigestMethod.SHA1, null),
                    transforms,
                    null,
                    null
            );
            // Create the SignedInfo
            si = fac.newSignedInfo(
                    fac.newCanonicalizationMethod(
                            CanonicalizationMethod.INCLUSIVE,
                            (C14NMethodParameterSpec) null
                    ),
                    fac.newSignatureMethod(
                            SignatureMethod.RSA_SHA1,
                            null
                    ),
                    Collections.singletonList(ref)
            );
        } catch (Exception e) {
            log.error("Error en el sistema", e);
        }
    }

    public void sign(File signatureFile, Document doc, ClienteContext ebizContext) throws Exception {
    	log.debug("Sign PFX - INICIO ");
        String keyStoreFile = ebizContext.getKeystoreFile();
        String kestorePassword = ebizContext.getKeystorePassword();
        String privateKeyPassword = ebizContext.getPrivateKeystorePassword();
        String privateKeyAlias = ebizContext.getPrivateKeyAlias();

        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            FileInputStream fis=new FileInputStream(keyStoreFile);
            keyStore.load(fis, kestorePassword.toCharArray());
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(privateKeyAlias, privateKeyPassword.toCharArray());
            X509Certificate cert = (X509Certificate) keyStore.getCertificate(privateKeyAlias);
            if (cert == null) {
                throw new IllegalArgumentException("verifique las claves y alias del certificado (EMISOR_PRIVATE_KEY_ALIAS)");
            }
            // Create a KeyValue containing the RSA PublicKey
            KeyInfoFactory keyInfoFactory = fac.getKeyInfoFactory();

            X509Data x509Data = keyInfoFactory.newX509Data(Arrays.asList(cert));
            KeyValue keyValue = keyInfoFactory.newKeyValue(cert.getPublicKey());


            // Create a KeyInfo and add the KeyValue to it
            KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Arrays.asList(x509Data, keyValue));

            XMLSignature xmlSignature = fac.newXMLSignature(si, keyInfo, null, "signatureKG", null);

            Element elemento = doc.getDocumentElement();
            NodeList lista1 = elemento.getChildNodes();
            Node segundoNodo = lista1.item(0);
            Element ublExtensionsType = doc.createElement("ext:UBLExtensions");
            Element extensionContentType = doc.createElement("ext:ExtensionContent");
            Element ublExtensionType = doc.createElement("ext:UBLExtension");
            ublExtensionType.appendChild(extensionContentType);
            ublExtensionsType.appendChild(ublExtensionType);
            if (segundoNodo != null && segundoNodo.getNodeName().startsWith("ext:UBLExtensions")) {
            	log.debug("segundoNodo.appendChild(ublExtensionType);");
                segundoNodo.appendChild(ublExtensionType);
            } else {
            	log.debug("elemento.insertBefore(ublExtensionsType, elemento.getFirstChild());");
                elemento.insertBefore(ublExtensionsType, elemento.getFirstChild());
            }
            // Create a DOMSignContext and specify the RSA PrivateKey and
            // location of the resulting XMLSignature's parent element
            DOMSignContext dsc = new DOMSignContext(
                    privateKey,
                    extensionContentType
            );
            dsc.putNamespacePrefix(XMLSignature.XMLNS, "ds");
            xmlSignature.sign(dsc);
            FileUtil.outputDocToFile(doc, signatureFile);
            fis.close();
            log.debug("Sign PFX - FIN ");
        } catch (IOException e1) {
            log.error("Error en el sistema",e1);
            throw new IllegalArgumentException("verifique las claves y alias del certificado (EMISOR_KEYSTORE_PASSWORD)");
        } catch (UnrecoverableKeyException e0) {
            log.error("Error en el sistema",e0);
            throw new IllegalArgumentException("verifique las claves y alias del certificado (EMISOR_PRIVATE_KEY_PASSWORD)");
        } catch (XMLSignatureException e) {
            log.error("Error en el sistema", e);
            throw new IllegalArgumentException("verifique las claves y alias del certificado");
        } catch (Exception ex) {
            log.error("Error en el sistema",ex);
            throw ex;
        }
    }


}
