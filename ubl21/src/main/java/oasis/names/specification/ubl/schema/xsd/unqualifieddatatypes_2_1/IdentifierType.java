//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacioasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.n de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderoasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.n si se vuelve a compilar el esquema de origen. 
// Generado el: 2017.10.03 a las 12:15:38 PM COT 
//


package oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_2_1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.*;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2_1.ExtensionAgencyIDType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2_1.ExtensionAgencyURIType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2_1.ExtensionURIType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2_1.ExtensionVersionIDType;
import oasis.names.specification.ubl.schema.xsd.signaturebasiccomponents_2_1.ReferencedSignatureIDType;


/**
 * 
 *         
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:UniqueID xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns:ccts-cct="urn:un:unece:uncefact:data:specification:CoreComponentTypeSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;UBLUDT0000011&lt;/ccts:UniqueID&gt;
 * </pre>
 * 
 *         
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:CategoryCode xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns:ccts-cct="urn:un:unece:uncefact:data:specification:CoreComponentTypeSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;UDT&lt;/ccts:CategoryCode&gt;
 * </pre>
 * 
 *         
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:DictionaryEntryName xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns:ccts-cct="urn:un:unece:uncefact:data:specification:CoreComponentTypeSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Identifier. Type&lt;/ccts:DictionaryEntryName&gt;
 * </pre>
 * 
 *         
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:VersionID xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns:ccts-cct="urn:un:unece:uncefact:data:specification:CoreComponentTypeSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;1.0&lt;/ccts:VersionID&gt;
 * </pre>
 * 
 *         
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Definition xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns:ccts-cct="urn:un:unece:uncefact:data:specification:CoreComponentTypeSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;A character string to identify and uniquely distinguish one instance of an object in an identification scheme from all other objects in the same scheme, together with relevant supplementary information.&lt;/ccts:Definition&gt;
 * </pre>
 * 
 *         
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:RepresentationTermName xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns:ccts-cct="urn:un:unece:uncefact:data:specification:CoreComponentTypeSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Identifier&lt;/ccts:RepresentationTermName&gt;
 * </pre>
 * 
 *         
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:PrimitiveType xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns:ccts-cct="urn:un:unece:uncefact:data:specification:CoreComponentTypeSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;string&lt;/ccts:PrimitiveType&gt;
 * </pre>
 * 
 *         
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:UsageRule xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns:ccts-cct="urn:un:unece:uncefact:data:specification:CoreComponentTypeSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Other supplementary components in the CCT are captured as part of the token and name for the schema module containing the identifier list and thus, are not declared as attributes. &lt;/ccts:UsageRule&gt;
 * </pre>
 * 
 *       
 * 
 * <p>Clase Java para IdentifierType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="IdentifierType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;urn:un:unece:uncefact:data:specification:CoreComponentTypeSchemaModule:2&gt;IdentifierType"&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IdentifierType")
@XmlSeeAlso({
    AccountIDType.class,
    AdditionalAccountIDType.class,
    AgencyIDType.class,
    AircraftIDType.class,
    AttributeIDType.class,
    AuctionURIType.class,
    AwardingCriterionIDType.class,
    BarcodeSymbologyIDType.class,
    BrokerAssignedIDType.class,
    BusinessClassificationEvidenceIDType.class,
    BusinessIdentityEvidenceIDType.class,
    BuyerEventIDType.class,
    BuyerProfileURIType.class,
    CV2IDType.class,
    CarrierAssignedIDType.class,
    ChipApplicationIDType.class,
    CompanyIDType.class,
    ConsigneeAssignedIDType.class,
    ConsignorAssignedIDType.class,
    ConsumptionIDType.class,
    ConsumptionReportIDType.class,
    ContractFolderIDType.class,
    ContractedCarrierAssignedIDType.class,
    CustomerAssignedAccountIDType.class,
    CustomizationIDType.class,
    DocumentIDType.class,
    EconomicOperatorRegistryURIType.class,
    EndpointIDType.class,
    ExchangeMarketIDType.class,
    ExtendedIDType.class,
    FreightForwarderAssignedIDType.class,
    HazardClassIDType.class,
    IDType.class,
    IdentificationIDType.class,
    ImmobilizationCertificateIDType.class,
    InformationURIType.class,
    InstructionIDType.class,
    IssueNumberIDType.class,
    IssuerIDType.class,
    JourneyIDType.class,
    LanguageIDType.class,
    LicensePlateIDType.class,
    LineIDType.class,
    LoadingSequenceIDType.class,
    LocationIDType.class,
    LogoReferenceIDType.class,
    LotNumberIDType.class,
    LowerOrangeHazardPlacardIDType.class,
    MarkingIDType.class,
    NationalityIDType.class,
    NetworkIDType.class,
    OntologyURIType.class,
    OpenTenderIDType.class,
    OriginalContractingSystemIDType.class,
    OriginalJobIDType.class,
    ParentDocumentIDType.class,
    ParentDocumentLineReferenceIDType.class,
    ParentDocumentVersionIDType.class,
    PaymentIDType.class,
    PaymentMeansIDType.class,
    PaymentTermsDetailsURIType.class,
    PerformingCarrierAssignedIDType.class,
    PrepaidPaymentReferenceIDType.class,
    PreviousJobIDType.class,
    PreviousVersionIDType.class,
    PrimaryAccountNumberIDType.class,
    ProductTraceIDType.class,
    ProfileExecutionIDType.class,
    ProfileIDType.class,
    RadioCallSignIDType.class,
    RailCarIDType.class,
    ReferenceIDType.class,
    ReferencedConsignmentIDType.class,
    RegistrationIDType.class,
    RegistrationNationalityIDType.class,
    ReleaseIDType.class,
    RequestForQuotationLineIDType.class,
    RequiredCustomsIDType.class,
    RevisedForecastLineIDType.class,
    SalesOrderIDType.class,
    SalesOrderLineIDType.class,
    SchemeURIType.class,
    SecurityIDType.class,
    SellerEventIDType.class,
    SequenceIDType.class,
    SequenceNumberIDType.class,
    SerialIDType.class,
    ShippingOrderIDType.class,
    SignatureIDType.class,
    SpecificationIDType.class,
    SubscriberIDType.class,
    SuccessiveSequenceIDType.class,
    SupplierAssignedAccountIDType.class,
    TenderEnvelopeIDType.class,
    TraceIDType.class,
    TrackingIDType.class,
    TrainIDType.class,
    TransportExecutionPlanReferenceIDType.class,
    TransportationServiceDetailsURIType.class,
    UBLVersionIDType.class,
    URIType.class,
    UUIDType.class,
    UpperOrangeHazardPlacardIDType.class,
    ValidatorIDType.class,
    VariantIDType.class,
    VersionIDType.class,
    VesselIDType.class,
    WebsiteURIType.class,
    ExtensionAgencyIDType.class,
    ExtensionAgencyURIType.class,
    ExtensionURIType.class,
    ExtensionVersionIDType.class,
    ReferencedSignatureIDType.class
})
public class IdentifierType
    extends un.unece.uncefact.data.specification.corecomponenttypeschemamodule._2_1.IdentifierType
{


}
