package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.BillingReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToBillingReferenceTypeConverter implements CustomConverter {
    private String parameter;

    /**
     * Invoice/ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/sac:AdditionalInformation/sac:AdditionalMonetaryTotal/cbc:ID (Código de tipo de monto -  Catálogo No. 14)
     * Invoice/ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/sac:AdditionalInformation/sac:AdditionalMonetaryTotal/cbc:PayableAmount (Monto)
     *
     * @param destinationFieldValue
     * @param sourceFieldValue
     * @param destinationClass
     * @param sourceClass
     * @return
     */
    public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {
        String valor = (String) sourceFieldValue;
        if(StringUtils.isBlank(valor)) return null;
        String[] values = StringUtils.split(valor, "|");
        BillingReferenceType documentReferenceType = new BillingReferenceType();


        IDType idType = new IDType();
        if (StringUtils.isBlank(values[1])) {
            idType.setValue("");
        } else {
        idType.setValue(values[1]);
        }

        DocumentReferenceType documentReferenceType1 = new DocumentReferenceType();
        documentReferenceType1.setID(idType);
        if (StringUtils.isNotBlank(values[0])) {
        DocumentTypeCodeType documentTypeCodeType = new DocumentTypeCodeType();
        documentTypeCodeType.setValue(values[0]);
            documentReferenceType1.setDocumentTypeCode(documentTypeCodeType);
        }

        documentReferenceType.setInvoiceDocumentReference(documentReferenceType1);
                return documentReferenceType;
    }


    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
