package com.axteroid.ose.server.ubl20.gateway.request;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * User: RAC
 * Date: 27/02/12
 */
public class MyNamespacePrefixMapper extends NamespacePrefixMapper {


    public static final String PREFIX = "";
    public static final String URI = "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2";

    public static final String URI_RESPONSE = "urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2";

    public static final String PREFIX_CREDIT = "";
    public static final String URI_CREDIT = "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2";

    public static final String PREFIX_DEBIT = "";
    public static final String URI_DEBIT = "urn:oasis:names:specification:ubl:schema:xsd:DebitNote-2";

    public static final String PREFIX_VOIDED = "";
    public static final String URI_VOIDED = "urn:sunat:names:specification:ubl:peru:schema:xsd:VoidedDocuments-1";

    public static final String PREFIX_SUMMARY = "";
    public static final String URI_SUMMARY = "urn:sunat:names:specification:ubl:peru:schema:xsd:SummaryDocuments-1";

    public static final String PREFIX_DESPATCH = "";
    public static final String URI_DESPATCH = "urn:oasis:names:specification:ubl:schema:xsd:DespatchAdvice-2";
    
    public static final String PREFIX_RETENTION = "";
    public static final String URI_RETENTION = "urn:sunat:names:specification:ubl:peru:schema:xsd:Retention-1";
    
    public static final String PREFIX_PERCEPTION = "";
    public static final String URI_PERCEPTION = "urn:sunat:names:specification:ubl:peru:schema:xsd:Perception-1";
    
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
        } else if (URI_CREDIT.equals(namespaceUri)) {
            return PREFIX_CREDIT;
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
        } else if (URI_CREDIT.equals(namespaceUri)) {
            return PREFIX_CREDIT;
        } else if (URI_DEBIT.equals(namespaceUri)) {
            return PREFIX_DEBIT;
        } else if (URI_VOIDED.equals(namespaceUri)) {
            return PREFIX_VOIDED;
        } else if (URI_SUMMARY.equals(namespaceUri)) {
            return PREFIX_SUMMARY;
        } else if (URI_DESPATCH.equals(namespaceUri)) {
        	return PREFIX_DESPATCH;
        } else if (URI_RETENTION.equals(namespaceUri)) {
        	return PREFIX_RETENTION;
        } else if (URI_PERCEPTION.equals(namespaceUri)) {
        	return PREFIX_PERCEPTION;
        }
        return "";
//        throw new IllegalArgumentException("Error uri");
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[]{
                URI,
                URI_CREDIT,
                URI_DEBIT,
                CAC_URI,
                CBC_URI,
                EXT_URI,
                QDT_URI,
                SAC_URI,
                DS_URI,
                URI_SUMMARY,
                URI_VOIDED,
                URI_DESPATCH,
                URI_RETENTION,
                URI_PERCEPTION
        };
    }
}
