package com.axteroid.ose.server.ubl21.gateway.request;

//import org.glassfish.jaxb.runtime.marshaller.NamespacePrefixMapper;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class NamespacePrefixMapper21 extends NamespacePrefixMapper {

	public static final String PREFIX = "";
    public static final String URI = "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2";
	
    public static final String PREFIX_RESPONSE = "";
    public static final String URI_RESPONSE = "urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2";

    public static final String CAC_PREFIX = "cac";
    public static final String CAC_URI = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2";

    public static final String CBC_PREFIX = "cbc";
    public static final String CBC_URI = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2";

    public static final String EXT_PREFIX = "ext";
    public static final String EXT_URI = "urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2";

    public static final String XSD_PREFIX = "xsd";
    public static final String XSD_URI = "http://www.w3.org/2001/XMLSchema";
    
    public static final String CCTS_PREFIX = "ccts";
    public static final String CCTS_URI = "urn:un:unece:uncefact:documentation:2";
    
    public static final String OSE_PREFIX = "";
    public static final String OSE_URI = "urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2";   

    
    public NamespacePrefixMapper21() {
		super();
	}

	@Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if (URI.equals(namespaceUri)) {
            return PREFIX;
        } else if (URI_RESPONSE.equals(namespaceUri)) {
            return PREFIX_RESPONSE;
        } else if (CAC_URI.equals(namespaceUri)) {
            return CAC_PREFIX;
        } else if (CBC_URI.equals(namespaceUri)) {
            return CBC_PREFIX;
        } else if (EXT_URI.equals(namespaceUri)) {
            return EXT_PREFIX;
        } else if (XSD_URI.equals(namespaceUri)) {
            return XSD_PREFIX;
        } else if (CCTS_URI.equals(namespaceUri)) {
            return CCTS_PREFIX;
        } else if (OSE_URI.equals(namespaceUri)) {
            return OSE_PREFIX;
        }
        return "";
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[]{
                URI,
                URI_RESPONSE,
                CAC_URI,
                CBC_URI,
                EXT_URI,
                XSD_URI,
                CCTS_URI,
                OSE_URI
        };
    }
}
