//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.10 at 03:06:42 PM COT 
//


package sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PaymentType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AmountType;
import un.unece.uncefact.data.specification.unqualifieddatatypesschemamodule._2.IdentifierType;
import un.unece.uncefact.data.specification.unqualifieddatatypesschemamodule._2.TextType;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AdditionalProperty_QNAME = new QName("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "AdditionalProperty");
    private final static QName _EndDocumentNumberID_QNAME = new QName("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "EndDocumentNumberID");
    private final static QName _TotalAmount_QNAME = new QName("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "TotalAmount");
    private final static QName _DocumentNumberID_QNAME = new QName("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "DocumentNumberID");
    private final static QName _BillingPayment_QNAME = new QName("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "BillingPayment");
    private final static QName _DocumentSerialID_QNAME = new QName("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "DocumentSerialID");
    private final static QName _SummaryDocumentsLine_QNAME = new QName("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "SummaryDocumentsLine");
    private final static QName _VoidedDocumentsLine_QNAME = new QName("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "VoidedDocumentsLine");
    private final static QName _StartDocumentNumberID_QNAME = new QName("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "StartDocumentNumberID");
    private final static QName _VoidReasonDescription_QNAME = new QName("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "VoidReasonDescription");
    private final static QName _ReferenceAmount_QNAME = new QName("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "ReferenceAmount");
    private final static QName _AdditionalMonetaryTotal_QNAME = new QName("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "AdditionalMonetaryTotal");
    private final static QName _AdditionalInformation_QNAME = new QName("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "AdditionalInformation");
    private final static QName _PeriodID_QNAME = new QName("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", "PeriodID");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SummaryDocumentsLineType }
     * 
     */
    public SummaryDocumentsLineType createSummaryDocumentsLineType() {
        return new SummaryDocumentsLineType();
    }

    /**
     * Create an instance of {@link AdditionalMonetaryTotalType }
     * 
     */
    public AdditionalMonetaryTotalType createAdditionalMonetaryTotalType() {
        return new AdditionalMonetaryTotalType();
    }

    /**
     * Create an instance of {@link VoidedDocumentsLineType }
     * 
     */
    public VoidedDocumentsLineType createVoidedDocumentsLineType() {
        return new VoidedDocumentsLineType();
    }

    /**
     * Create an instance of {@link AdditionalPropertyType }
     * 
     */
    public AdditionalPropertyType createAdditionalPropertyType() {
        return new AdditionalPropertyType();
    }

    /**
     * Create an instance of {@link AdditionalInformationType }
     * 
     */
    public AdditionalInformationType createAdditionalInformationType() {
        return new AdditionalInformationType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdditionalPropertyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "AdditionalProperty")
    public JAXBElement<AdditionalPropertyType> createAdditionalProperty(AdditionalPropertyType value) {
        return new JAXBElement<AdditionalPropertyType>(_AdditionalProperty_QNAME, AdditionalPropertyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdentifierType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "EndDocumentNumberID")
    public JAXBElement<IdentifierType> createEndDocumentNumberID(IdentifierType value) {
        return new JAXBElement<IdentifierType>(_EndDocumentNumberID_QNAME, IdentifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AmountType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "TotalAmount")
    public JAXBElement<AmountType> createTotalAmount(AmountType value) {
        return new JAXBElement<AmountType>(_TotalAmount_QNAME, AmountType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdentifierType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "DocumentNumberID")
    public JAXBElement<IdentifierType> createDocumentNumberID(IdentifierType value) {
        return new JAXBElement<IdentifierType>(_DocumentNumberID_QNAME, IdentifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaymentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "BillingPayment")
    public JAXBElement<PaymentType> createBillingPayment(PaymentType value) {
        return new JAXBElement<PaymentType>(_BillingPayment_QNAME, PaymentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdentifierType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "DocumentSerialID")
    public JAXBElement<IdentifierType> createDocumentSerialID(IdentifierType value) {
        return new JAXBElement<IdentifierType>(_DocumentSerialID_QNAME, IdentifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SummaryDocumentsLineType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "SummaryDocumentsLine")
    public JAXBElement<SummaryDocumentsLineType> createSummaryDocumentsLine(SummaryDocumentsLineType value) {
        return new JAXBElement<SummaryDocumentsLineType>(_SummaryDocumentsLine_QNAME, SummaryDocumentsLineType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VoidedDocumentsLineType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "VoidedDocumentsLine")
    public JAXBElement<VoidedDocumentsLineType> createVoidedDocumentsLine(VoidedDocumentsLineType value) {
        return new JAXBElement<VoidedDocumentsLineType>(_VoidedDocumentsLine_QNAME, VoidedDocumentsLineType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdentifierType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "StartDocumentNumberID")
    public JAXBElement<IdentifierType> createStartDocumentNumberID(IdentifierType value) {
        return new JAXBElement<IdentifierType>(_StartDocumentNumberID_QNAME, IdentifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TextType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "VoidReasonDescription")
    public JAXBElement<TextType> createVoidReasonDescription(TextType value) {
        return new JAXBElement<TextType>(_VoidReasonDescription_QNAME, TextType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AmountType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "ReferenceAmount")
    public JAXBElement<AmountType> createReferenceAmount(AmountType value) {
        return new JAXBElement<AmountType>(_ReferenceAmount_QNAME, AmountType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdditionalMonetaryTotalType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "AdditionalMonetaryTotal")
    public JAXBElement<AdditionalMonetaryTotalType> createAdditionalMonetaryTotal(AdditionalMonetaryTotalType value) {
        return new JAXBElement<AdditionalMonetaryTotalType>(_AdditionalMonetaryTotal_QNAME, AdditionalMonetaryTotalType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdditionalInformationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "AdditionalInformation")
    public JAXBElement<AdditionalInformationType> createAdditionalInformation(AdditionalInformationType value) {
        return new JAXBElement<AdditionalInformationType>(_AdditionalInformation_QNAME, AdditionalInformationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdentifierType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1", name = "PeriodID")
    public JAXBElement<IdentifierType> createPeriodID(IdentifierType value) {
        return new JAXBElement<IdentifierType>(_PeriodID_QNAME, IdentifierType.class, null, value);
    }

}
