//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacioasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.n de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderoasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.n si se vuelve a compilar el esquema de origen. 
// Generado el: 2017.10.03 a las 12:15:38 PM COT 
//


package oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.BusinessClassificationEvidenceIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.BusinessIdentityEvidenceIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.EmployeeQuantityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.OperatingYearsQuantityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.ParticipationPercentType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.PersonalSituationType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.TendererRoleCodeType;


/**
 * 
 *             
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
 *                &lt;ccts:ComponentType&gt;ABIE&lt;/ccts:ComponentType&gt;
 *                &lt;ccts:DictionaryEntryName&gt;Qualifying Party. Details&lt;/ccts:DictionaryEntryName&gt;
 *                &lt;ccts:Definition&gt;A class to describe the distinctive features or characteristics qualifying an economic operator to be a party in a tendering process (e.g., number of employees, number of operating units, type of business, technical and financial capabilities, completed projects).&lt;/ccts:Definition&gt;
 *                &lt;ccts:ObjectClass&gt;Qualifying Party&lt;/ccts:ObjectClass&gt;
 *             &lt;/ccts:Component&gt;
 * </pre>
 * 
 *          
 * 
 * <p>Clase Java para QualifyingPartyType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="QualifyingPartyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}ParticipationPercent" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}PersonalSituation" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}OperatingYearsQuantity" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}EmployeeQuantity" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}BusinessClassificationEvidenceID" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}BusinessIdentityEvidenceID" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}TendererRoleCode" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}BusinessClassificationScheme" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}TechnicalCapability" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}FinancialCapability" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}CompletedTask" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}Declaration" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}Party" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}EconomicOperatorRole" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QualifyingPartyType", propOrder = {
    "participationPercent",
    "personalSituation",
    "operatingYearsQuantity",
    "employeeQuantity",
    "businessClassificationEvidenceID",
    "businessIdentityEvidenceID",
    "tendererRoleCode",
    "businessClassificationScheme",
    "technicalCapability",
    "financialCapability",
    "completedTask",
    "declaration",
    "party",
    "economicOperatorRole"
})
public class QualifyingPartyType {

    @XmlElement(name = "ParticipationPercent", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected ParticipationPercentType participationPercent;
    @XmlElement(name = "PersonalSituation", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected List<PersonalSituationType> personalSituation;
    @XmlElement(name = "OperatingYearsQuantity", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected OperatingYearsQuantityType operatingYearsQuantity;
    @XmlElement(name = "EmployeeQuantity", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected EmployeeQuantityType employeeQuantity;
    @XmlElement(name = "BusinessClassificationEvidenceID", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected BusinessClassificationEvidenceIDType businessClassificationEvidenceID;
    @XmlElement(name = "BusinessIdentityEvidenceID", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected BusinessIdentityEvidenceIDType businessIdentityEvidenceID;
    @XmlElement(name = "TendererRoleCode", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected TendererRoleCodeType tendererRoleCode;
    @XmlElement(name = "BusinessClassificationScheme")
    protected ClassificationSchemeType businessClassificationScheme;
    @XmlElement(name = "TechnicalCapability")
    protected List<CapabilityType> technicalCapability;
    @XmlElement(name = "FinancialCapability")
    protected List<CapabilityType> financialCapability;
    @XmlElement(name = "CompletedTask")
    protected List<CompletedTaskType> completedTask;
    @XmlElement(name = "Declaration")
    protected List<DeclarationType> declaration;
    @XmlElement(name = "Party")
    protected PartyType party;
    @XmlElement(name = "EconomicOperatorRole")
    protected EconomicOperatorRoleType economicOperatorRole;

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Qualifying Party. Participation. Percent&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;The extent to which this party is expected to participate in the tendering process, expressed as a percentage.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Qualifying Party&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Participation&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Percent&lt;/ccts:RepresentationTerm&gt;
     *                      &lt;ccts:DataType&gt;Percent. Type&lt;/ccts:DataType&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                
     * 
     * @return
     *     possible object is
     *     {@link ParticipationPercentType }
     *     
     */
    public ParticipationPercentType getParticipationPercent() {
        return participationPercent;
    }

    /**
     * Define el valor de la propiedad participationPercent.
     * 
     * @param value
     *     allowed object is
     *     {@link ParticipationPercentType }
     *     
     */
    public void setParticipationPercent(ParticipationPercentType value) {
        this.participationPercent = value;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Qualifying Party. Personal Situation. Text&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;Text describing the personal situation of the qualifying party.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..n&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Qualifying Party&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Personal Situation&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Text&lt;/ccts:RepresentationTerm&gt;
     *                      &lt;ccts:DataType&gt;Text. Type&lt;/ccts:DataType&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                Gets the value of the personalSituation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the personalSituation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersonalSituation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PersonalSituationType }
     * 
     * 
     */
    public List<PersonalSituationType> getPersonalSituation() {
        if (personalSituation == null) {
            personalSituation = new ArrayList<PersonalSituationType>();
        }
        return this.personalSituation;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Qualifying Party. Operating Years. Quantity&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;The number of years that this qualifying party has been in operation.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Qualifying Party&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Operating Years&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Quantity&lt;/ccts:RepresentationTerm&gt;
     *                      &lt;ccts:DataType&gt;Quantity. Type&lt;/ccts:DataType&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                
     * 
     * @return
     *     possible object is
     *     {@link OperatingYearsQuantityType }
     *     
     */
    public OperatingYearsQuantityType getOperatingYearsQuantity() {
        return operatingYearsQuantity;
    }

    /**
     * Define el valor de la propiedad operatingYearsQuantity.
     * 
     * @param value
     *     allowed object is
     *     {@link OperatingYearsQuantityType }
     *     
     */
    public void setOperatingYearsQuantity(OperatingYearsQuantityType value) {
        this.operatingYearsQuantity = value;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Qualifying Party. Employee. Quantity&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;The number of people employed by this qualifying party.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Qualifying Party&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Employee&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Quantity&lt;/ccts:RepresentationTerm&gt;
     *                      &lt;ccts:DataType&gt;Quantity. Type&lt;/ccts:DataType&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                
     * 
     * @return
     *     possible object is
     *     {@link EmployeeQuantityType }
     *     
     */
    public EmployeeQuantityType getEmployeeQuantity() {
        return employeeQuantity;
    }

    /**
     * Define el valor de la propiedad employeeQuantity.
     * 
     * @param value
     *     allowed object is
     *     {@link EmployeeQuantityType }
     *     
     */
    public void setEmployeeQuantity(EmployeeQuantityType value) {
        this.employeeQuantity = value;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Qualifying Party. Business Classification Evidence. Identifier&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;An identifier for an item of evidence to support the classification of this qualifying party.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Qualifying Party&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Business Classification Evidence&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Identifier&lt;/ccts:RepresentationTerm&gt;
     *                      &lt;ccts:DataType&gt;Identifier. Type&lt;/ccts:DataType&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                
     * 
     * @return
     *     possible object is
     *     {@link BusinessClassificationEvidenceIDType }
     *     
     */
    public BusinessClassificationEvidenceIDType getBusinessClassificationEvidenceID() {
        return businessClassificationEvidenceID;
    }

    /**
     * Define el valor de la propiedad businessClassificationEvidenceID.
     * 
     * @param value
     *     allowed object is
     *     {@link BusinessClassificationEvidenceIDType }
     *     
     */
    public void setBusinessClassificationEvidenceID(BusinessClassificationEvidenceIDType value) {
        this.businessClassificationEvidenceID = value;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Qualifying Party. Business Identity Evidence. Identifier&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;An identifier for an item of evidence to support the business identity of this qualifying party.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Qualifying Party&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Business Identity Evidence&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Identifier&lt;/ccts:RepresentationTerm&gt;
     *                      &lt;ccts:DataType&gt;Identifier. Type&lt;/ccts:DataType&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                
     * 
     * @return
     *     possible object is
     *     {@link BusinessIdentityEvidenceIDType }
     *     
     */
    public BusinessIdentityEvidenceIDType getBusinessIdentityEvidenceID() {
        return businessIdentityEvidenceID;
    }

    /**
     * Define el valor de la propiedad businessIdentityEvidenceID.
     * 
     * @param value
     *     allowed object is
     *     {@link BusinessIdentityEvidenceIDType }
     *     
     */
    public void setBusinessIdentityEvidenceID(BusinessIdentityEvidenceIDType value) {
        this.businessIdentityEvidenceID = value;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Qualifying Party. Tenderer Role Code. Code&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;A code stating the Tenderer Role.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Qualifying Party&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Tenderer Role Code&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Code&lt;/ccts:RepresentationTerm&gt;
     *                      &lt;ccts:DataType&gt;Code. Type&lt;/ccts:DataType&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                
     * 
     * @return
     *     possible object is
     *     {@link TendererRoleCodeType }
     *     
     */
    public TendererRoleCodeType getTendererRoleCode() {
        return tendererRoleCode;
    }

    /**
     * Define el valor de la propiedad tendererRoleCode.
     * 
     * @param value
     *     allowed object is
     *     {@link TendererRoleCodeType }
     *     
     */
    public void setTendererRoleCode(TendererRoleCodeType value) {
        this.tendererRoleCode = value;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Qualifying Party. Business_ Classification Scheme. Classification Scheme&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;The classification scheme used for the business profile.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Qualifying Party&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTermQualifier&gt;Business&lt;/ccts:PropertyTermQualifier&gt;
     *                      &lt;ccts:PropertyTerm&gt;Classification Scheme&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:AssociatedObjectClass&gt;Classification Scheme&lt;/ccts:AssociatedObjectClass&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Classification Scheme&lt;/ccts:RepresentationTerm&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                
     * 
     * @return
     *     possible object is
     *     {@link ClassificationSchemeType }
     *     
     */
    public ClassificationSchemeType getBusinessClassificationScheme() {
        return businessClassificationScheme;
    }

    /**
     * Define el valor de la propiedad businessClassificationScheme.
     * 
     * @param value
     *     allowed object is
     *     {@link ClassificationSchemeType }
     *     
     */
    public void setBusinessClassificationScheme(ClassificationSchemeType value) {
        this.businessClassificationScheme = value;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Qualifying Party. Technical_ Capability. Capability&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;A technical capability of this qualifying party.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..n&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Qualifying Party&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTermQualifier&gt;Technical&lt;/ccts:PropertyTermQualifier&gt;
     *                      &lt;ccts:PropertyTerm&gt;Capability&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:AssociatedObjectClass&gt;Capability&lt;/ccts:AssociatedObjectClass&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Capability&lt;/ccts:RepresentationTerm&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                Gets the value of the technicalCapability property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the technicalCapability property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTechnicalCapability().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CapabilityType }
     * 
     * 
     */
    public List<CapabilityType> getTechnicalCapability() {
        if (technicalCapability == null) {
            technicalCapability = new ArrayList<CapabilityType>();
        }
        return this.technicalCapability;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Qualifying Party. Financial_ Capability. Capability&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;A financial capability of this qualifying party.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..n&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Qualifying Party&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTermQualifier&gt;Financial&lt;/ccts:PropertyTermQualifier&gt;
     *                      &lt;ccts:PropertyTerm&gt;Capability&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:AssociatedObjectClass&gt;Capability&lt;/ccts:AssociatedObjectClass&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Capability&lt;/ccts:RepresentationTerm&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                Gets the value of the financialCapability property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the financialCapability property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFinancialCapability().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CapabilityType }
     * 
     * 
     */
    public List<CapabilityType> getFinancialCapability() {
        if (financialCapability == null) {
            financialCapability = new ArrayList<CapabilityType>();
        }
        return this.financialCapability;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Qualifying Party. Completed Task&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;A former task completed by this qualifying party.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..n&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Qualifying Party&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Completed Task&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:AssociatedObjectClass&gt;Completed Task&lt;/ccts:AssociatedObjectClass&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Completed Task&lt;/ccts:RepresentationTerm&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                Gets the value of the completedTask property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the completedTask property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCompletedTask().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CompletedTaskType }
     * 
     * 
     */
    public List<CompletedTaskType> getCompletedTask() {
        if (completedTask == null) {
            completedTask = new ArrayList<CompletedTaskType>();
        }
        return this.completedTask;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Qualifying Party. Declaration&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;A declaration by this qualifying party. of certain characteristics or capabilities in fulfilment of requirements specified in a call for tenders.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..n&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Qualifying Party&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Declaration&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:AssociatedObjectClass&gt;Declaration&lt;/ccts:AssociatedObjectClass&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Declaration&lt;/ccts:RepresentationTerm&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                Gets the value of the declaration property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the declaration property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeclaration().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DeclarationType }
     * 
     * 
     */
    public List<DeclarationType> getDeclaration() {
        if (declaration == null) {
            declaration = new ArrayList<DeclarationType>();
        }
        return this.declaration;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Qualifying Party. Party&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;The qualifying party itself.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Qualifying Party&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Party&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:AssociatedObjectClass&gt;Party&lt;/ccts:AssociatedObjectClass&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Party&lt;/ccts:RepresentationTerm&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                
     * 
     * @return
     *     possible object is
     *     {@link PartyType }
     *     
     */
    public PartyType getParty() {
        return party;
    }

    /**
     * Define el valor de la propiedad party.
     * 
     * @param value
     *     allowed object is
     *     {@link PartyType }
     *     
     */
    public void setParty(PartyType value) {
        this.party = value;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Qualifying Party. Economic Operator Role&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;A class to describe the tenderer contracting role.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Qualifying Party&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Economic Operator Role&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:AssociatedObjectClass&gt;Economic Operator Role&lt;/ccts:AssociatedObjectClass&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Economic Operator Role&lt;/ccts:RepresentationTerm&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                
     * 
     * @return
     *     possible object is
     *     {@link EconomicOperatorRoleType }
     *     
     */
    public EconomicOperatorRoleType getEconomicOperatorRole() {
        return economicOperatorRole;
    }

    /**
     * Define el valor de la propiedad economicOperatorRole.
     * 
     * @param value
     *     allowed object is
     *     {@link EconomicOperatorRoleType }
     *     
     */
    public void setEconomicOperatorRole(EconomicOperatorRoleType value) {
        this.economicOperatorRole = value;
    }

}
