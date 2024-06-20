package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToDespatchDocumentReferenceTypeConverter implements CustomConverter {
    private String parameter;

    /**
     * Invoice/ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/sac:AdditionalInformation/sac:AdditionalMonetaryTotal/cbc:ID (Código de tipo de monto -  Católogo No. 14)
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
        DocumentReferenceType documentReferenceType = new DocumentReferenceType();

        IDType idType = new IDType();
        idType.setValue(values[1]);
        documentReferenceType.setID(idType);
        DocumentTypeCodeType documentTypeCodeType = new DocumentTypeCodeType();
        documentTypeCodeType.setValue(values[0]);
        documentReferenceType.setDocumentTypeCode(documentTypeCodeType);
        return documentReferenceType;
    }


    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
