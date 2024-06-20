package com.axteroid.ose.server.ubl21.gateway.request.sunat.dozer;

import org.apache.commons.lang.StringUtils;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.AttachmentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.ExternalReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.SignatureType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.URIType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_2_1.NameType;


/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToSignTypeConverter extends StringToNameTypeConverter {
    public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {
        String value = (String) sourceFieldValue;

        if (StringUtils.isBlank(value))  return null;

        String[] values = StringUtils.split(value, "|");


        IDType idType = new IDType();
        idType.setValue("IDSignKG");
        IDType idTypeEmisor = new IDType();
        idTypeEmisor.setValue(values[0]);

        URIType uriType = new URIType();
        uriType.setValue("#signatureKG");

        ExternalReferenceType externalReferenceType = new ExternalReferenceType();
        externalReferenceType.setURI(uriType);
        AttachmentType attachmentType = new AttachmentType();
        attachmentType.setExternalReference(externalReferenceType);

        PartyIdentificationType partyIdentificationType = new PartyIdentificationType();
        partyIdentificationType.setID(idTypeEmisor);

        NameType nameType = new NameType();
        nameType.setValue(values[1]);
        PartyNameType partyNameType = new PartyNameType();
        //partyNameType.setName(nameType);

        PartyType partyType = new PartyType();
        partyType.getPartyIdentification().add(partyIdentificationType);
        partyType.getPartyName().add(partyNameType);

        SignatureType signatureType = new SignatureType();
        signatureType.setID(idType);
        signatureType.setSignatoryParty(partyType);
        signatureType.setDigitalSignatureAttachment(attachmentType);

        return signatureType;
    }
}
