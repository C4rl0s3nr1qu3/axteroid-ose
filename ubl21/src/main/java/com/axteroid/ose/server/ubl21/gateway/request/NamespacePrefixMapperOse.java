package com.axteroid.ose.server.ubl21.gateway.request;

//import org.glassfish.jaxb.runtime.marshaller.NamespacePrefixMapper;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class NamespacePrefixMapperOse extends NamespacePrefixMapper {

    public static final String PREFIX = "tns";
    public static final String URI = "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2";

    public static final String CAC_PREFIX = "cac";
    public static final String CAC_URI = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2";

    public static final String CBC_PREFIX = "cbc";
    public static final String CBC_URI = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2";

    public static final String EXT_PREFIX = "ext";
    public static final String EXT_URI = "urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2";

    public static final String QDT_PREFIX = "qdt";
    public static final String QDT_URI = "urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2";

    public static final String SAC_PREFIX = "sac";
    public static final String SAC_URI = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1";

    public static final String DS_PREFIX = "ds";
    public static final String DS_URI = "http://www.w3.org/2000/09/xmldsig#";

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if (URI.equals(namespaceUri)) {
            return PREFIX;
        } else if (CAC_URI.equals(namespaceUri)) {
            return CAC_PREFIX;
        } else if (CBC_URI.equals(namespaceUri)) {
            return CBC_PREFIX;
        } else if (EXT_URI.equals(namespaceUri)) {
            return EXT_PREFIX;
        } else if (QDT_URI.equals(namespaceUri)) {
            return QDT_PREFIX;
        } else if (SAC_URI.equals(namespaceUri)) {
            return SAC_PREFIX;
        } else if (DS_URI.equals(namespaceUri)) {
            return DS_PREFIX;
        }
        return "";
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[]{
                URI,
                CAC_URI,
                CBC_URI,
                EXT_URI,
                QDT_URI,
                SAC_URI,
                DS_URI,
        };
    }
}
