package oasis.names.specification.ubl.schema.xsd;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.UBLExtensionsType;

import java.util.List;

/**
 * Created by RAZANERO on 24/06/14.
 */
public interface IDocumentoSunat {

    public UBLExtensionsType getUBLExtensions();

    public List<TaxTotalType> getTaxTotal();

    public List<? extends IDocumentoItemSunat> getLineItem();

    public List<DocumentReferenceType> getAdditionalDocumentReference() ;

    public List<DocumentReferenceType> getDespatchDocumentReference() ;
}
