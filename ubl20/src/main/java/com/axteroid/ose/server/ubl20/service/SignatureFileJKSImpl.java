package com.axteroid.ose.server.ubl20.service;

import com.axteroid.ose.server.tools.algoritmo.NcCrypt;
import com.axteroid.ose.server.tools.util.FileUtil;
import com.axteroid.ose.server.ubl20.gateway.ClienteContext;

//import org.apache.log4j.Logger;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;

public class SignatureFileJKSImpl implements SignatureFileJKS {
    //private static final Logger log = Logger.getLogger(SignatureFileJKSImpl.class);
    private static final Logger log = LoggerFactory.getLogger(SignatureFileJKSImpl.class);
    private static final String KEYSTORE_TYPE = "JKS";

    @PostConstruct
    public void init() {
        org.apache.xml.security.Init.init();
    }

    public void sign(File signatureFile, Document doc, ClienteContext clienteContext) throws Exception {
//        LOG.debug("sign jks - inicio");
        String keyStoreFile = clienteContext.getKeystoreFile();
        String kestorePassword = clienteContext.getKeystorePassword();
        String privateKeyPassword = NcCrypt.desencriptarPassword("getPrivateKeystorePassword", clienteContext.getPrivateKeystorePassword());
        String privateKeyAlias = clienteContext.getPrivateKeyAlias();
        try {
            KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
            FileInputStream fis=new FileInputStream(keyStoreFile);
            ks.load(fis, kestorePassword.toCharArray());
            X509Certificate cert = (X509Certificate) ks.getCertificate(privateKeyAlias);
            if(cert==null){
                throw new IllegalArgumentException("verifique las claves y alias del certificado (EMISOR_PRIVATE_KEY_ALIAS)");
            }

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
            if (segundoNodo != null && segundoNodo.getNodeName().startsWith("ext:UBLExtensions")) {
                segundoNodo.appendChild(ublExtensionType);
            } else {
                elemento.insertBefore(ublExtensionsType, elemento.getFirstChild());
            }

            Transforms transforms = new Transforms(doc);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);

            xmlSignature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);

            xmlSignature.addKeyInfo(cert);
            xmlSignature.addKeyInfo(cert.getPublicKey());
            xmlSignature.sign(privateKey);
            FileUtil.outputDocToFile(doc, signatureFile);

            fis.close();
        } catch (FileNotFoundException e11) {
            log.error("Error al firnmar ",e11);
            throw new IllegalArgumentException("Certificado no encontrado "+e11.getMessage());
        }catch (IOException e1) {
            log.error("Error al firnmar ",e1);
            throw new IllegalArgumentException("verifique las claves y alias del certificado (EMISOR_KEYSTORE_PASSWORD)");
        }catch (UnrecoverableKeyException e0) {
            log.error("Error al firnmar ",e0);
            throw new IllegalArgumentException("verifique las claves y alias del certificado (EMISOR_PRIVATE_KEY_PASSWORD)");
        }catch (XMLSignatureException e) {
            log.error("Error al firnmar ",e);
            throw new IllegalArgumentException("verifique las claves y alias del certificado");
        }catch (Throwable e) {
            e.printStackTrace();
            log.error("Error al firnmar ",e);

        }

    }


}
