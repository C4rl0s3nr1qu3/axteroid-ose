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

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.CompanyLegalFormCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.CompanyLegalFormType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.DescriptionType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.EmployeeQuantityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.OperatingYearsQuantityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.PersonalSituationType;


/**
 * 
 *             
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
 *                &lt;ccts:ComponentType&gt;ABIE&lt;/ccts:ComponentType&gt;
 *                &lt;ccts:DictionaryEntryName&gt;Tenderer Qualification Request. Details&lt;/ccts:DictionaryEntryName&gt;
 *                &lt;ccts:Definition&gt;The evaluation that the Contracting Authority party requests to fulfill to the tenderers.&lt;/ccts:Definition&gt;
 *                &lt;ccts:ObjectClass&gt;Tenderer Qualification Request&lt;/ccts:ObjectClass&gt;
 *             &lt;/ccts:Component&gt;
 * </pre>
 * 
 *          
 * 
 * <p>Clase Java para TendererQualificationRequestType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TendererQualificationRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}CompanyLegalFormCode" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}CompanyLegalForm" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}PersonalSituation" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}OperatingYearsQuantity" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}EmployeeQuantity" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}Description" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}RequiredBusinessClassificationScheme" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}TechnicalEvaluationCriterion" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}FinancialEvaluationCriterion" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}SpecificTendererRequirement" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}EconomicOperatorRole" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TendererQualificationRequestType", propOrder = {
    "companyLegalFormCode",
    "companyLegalForm",
    "personalSituation",
    "operatingYearsQuantity",
    "employeeQuantity",
    "description",
    "requiredBusinessClassificationScheme",
    "technicalEvaluationCriterion",
    "financialEvaluationCriterion",
    "specificTendererRequirement",
    "economicOperatorRole"
})
public class TendererQualificationRequestType {

    @XmlElement(name = "CompanyLegalFormCode", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected CompanyLegalFormCodeType companyLegalFormCode;
    @XmlElement(name = "CompanyLegalForm", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected CompanyLegalFormType companyLegalForm;
    @XmlElement(name = "PersonalSituation", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected List<PersonalSituationType> personalSituation;
    @XmlElement(name = "OperatingYearsQuantity", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected OperatingYearsQuantityType operatingYearsQuantity;
    @XmlElement(name = "EmployeeQuantity", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected EmployeeQuantityType employeeQuantity;
    @XmlElement(name = "Description", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected List<DescriptionType> description;
    @XmlElement(name = "RequiredBusinessClassificationScheme")
    protected List<ClassificationSchemeType> requiredBusinessClassificationScheme;
    @XmlElement(name = "TechnicalEvaluationCriterion")
    protected List<EvaluationCriterionType> technicalEvaluationCriterion;
    @XmlElement(name = "FinancialEvaluationCriterion")
    protected List<EvaluationCriterionType> financialEvaluationCriterion;
    @XmlElement(name = "SpecificTendererRequirement")
    protected List<TendererRequirementType> specificTendererRequirement;
    @XmlElement(name = "EconomicOperatorRole")
    protected List<EconomicOperatorRoleType> economicOperatorRole;

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Tenderer Qualification Request. Company Legal Form Code. Code&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;The legal status requested for potential tenderers, expressed as a code.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Tenderer Qualification Request&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Company Legal Form Code&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Code&lt;/ccts:RepresentationTerm&gt;
     *                      &lt;ccts:DataType&gt;Code. Type&lt;/ccts:DataType&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                
     * 
     * @return
     *     possible object is
     *     {@link CompanyLegalFormCodeType }
     *     
     */
    public CompanyLegalFormCodeType getCompanyLegalFormCode() {
        return companyLegalFormCode;
    }

    /**
     * Define el valor de la propiedad companyLegalFormCode.
     * 
     * @param value
     *     allowed object is
     *     {@link CompanyLegalFormCodeType }
     *     
     */
    public void setCompanyLegalFormCode(CompanyLegalFormCodeType value) {
        this.companyLegalFormCode = value;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Tenderer Qualification Request. Company Legal Form. Text&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;The legal status requested for potential tenderers, expressed as text&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Tenderer Qualification Request&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Company Legal Form&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Text&lt;/ccts:RepresentationTerm&gt;
     *                      &lt;ccts:DataType&gt;Text. Type&lt;/ccts:DataType&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                
     * 
     * @return
     *     possible object is
     *     {@link CompanyLegalFormType }
     *     
     */
    public CompanyLegalFormType getCompanyLegalForm() {
        return companyLegalForm;
    }

    /**
     * Define el valor de la propiedad companyLegalForm.
     * 
     * @param value
     *     allowed object is
     *     {@link CompanyLegalFormType }
     *     
     */
    public void setCompanyLegalForm(CompanyLegalFormType value) {
        this.companyLegalForm = value;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Tenderer Qualification Request. Personal Situation. Text&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;Text describing the personal situation of the economic operators in this tendering process.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..n&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Tenderer Qualification Request&lt;/ccts:ObjectClass&gt;
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
     *                      &lt;ccts:DictionaryEntryName&gt;Tenderer Qualification Request. Operating Years. Quantity&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;Textual description of the legal form required for potential tenderers.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Tenderer Qualification Request&lt;/ccts:ObjectClass&gt;
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
     *                      &lt;ccts:DictionaryEntryName&gt;Tenderer Qualification Request. Employee. Quantity&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;Textual description of the legal form required for potential tenderers.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Tenderer Qualification Request&lt;/ccts:ObjectClass&gt;
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
     *                      &lt;ccts:DictionaryEntryName&gt;Tenderer Qualification Request. Description. Text&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;Text describing the evaluation requirements for this tenderer.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..n&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Tenderer Qualification Request&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Description&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Text&lt;/ccts:RepresentationTerm&gt;
     *                      &lt;ccts:DataType&gt;Text. Type&lt;/ccts:DataType&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                Gets the value of the description property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the description property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DescriptionType }
     * 
     * 
     */
    public List<DescriptionType> getDescription() {
        if (description == null) {
            description = new ArrayList<DescriptionType>();
        }
        return this.description;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Tenderer Qualification Request. Required Business_ Classification Scheme. Classification Scheme&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;A classification scheme for the business profile.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..n&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Tenderer Qualification Request&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTermQualifier&gt;Required Business&lt;/ccts:PropertyTermQualifier&gt;
     *                      &lt;ccts:PropertyTerm&gt;Classification Scheme&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:AssociatedObjectClass&gt;Classification Scheme&lt;/ccts:AssociatedObjectClass&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Classification Scheme&lt;/ccts:RepresentationTerm&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                Gets the value of the requiredBusinessClassificationScheme property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the requiredBusinessClassificationScheme property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRequiredBusinessClassificationScheme().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClassificationSchemeType }
     * 
     * 
     */
    public List<ClassificationSchemeType> getRequiredBusinessClassificationScheme() {
        if (requiredBusinessClassificationScheme == null) {
            requiredBusinessClassificationScheme = new ArrayList<ClassificationSchemeType>();
        }
        return this.requiredBusinessClassificationScheme;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Tenderer Qualification Request. Technical_ Evaluation Criterion. Evaluation Criterion&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;A technical evaluation criterion required for an economic operator in a tendering process.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..n&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Tenderer Qualification Request&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTermQualifier&gt;Technical&lt;/ccts:PropertyTermQualifier&gt;
     *                      &lt;ccts:PropertyTerm&gt;Evaluation Criterion&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:AssociatedObjectClass&gt;Evaluation Criterion&lt;/ccts:AssociatedObjectClass&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Evaluation Criterion&lt;/ccts:RepresentationTerm&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                Gets the value of the technicalEvaluationCriterion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the technicalEvaluationCriterion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTechnicalEvaluationCriterion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EvaluationCriterionType }
     * 
     * 
     */
    public List<EvaluationCriterionType> getTechnicalEvaluationCriterion() {
        if (technicalEvaluationCriterion == null) {
            technicalEvaluationCriterion = new ArrayList<EvaluationCriterionType>();
        }
        return this.technicalEvaluationCriterion;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Tenderer Qualification Request. Financial_ Evaluation Criterion. Evaluation Criterion&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;A financial evaluation criterion required for an economic operator in a tendering process.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..n&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Tenderer Qualification Request&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTermQualifier&gt;Financial&lt;/ccts:PropertyTermQualifier&gt;
     *                      &lt;ccts:PropertyTerm&gt;Evaluation Criterion&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:AssociatedObjectClass&gt;Evaluation Criterion&lt;/ccts:AssociatedObjectClass&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Evaluation Criterion&lt;/ccts:RepresentationTerm&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                Gets the value of the financialEvaluationCriterion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the financialEvaluationCriterion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFinancialEvaluationCriterion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EvaluationCriterionType }
     * 
     * 
     */
    public List<EvaluationCriterionType> getFinancialEvaluationCriterion() {
        if (financialEvaluationCriterion == null) {
            financialEvaluationCriterion = new ArrayList<EvaluationCriterionType>();
        }
        return this.financialEvaluationCriterion;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Tenderer Qualification Request. Specific_ Tenderer Requirement. Tenderer Requirement&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;A requirement to be met by a tenderer.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..n&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Tenderer Qualification Request&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTermQualifier&gt;Specific&lt;/ccts:PropertyTermQualifier&gt;
     *                      &lt;ccts:PropertyTerm&gt;Tenderer Requirement&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:AssociatedObjectClass&gt;Tenderer Requirement&lt;/ccts:AssociatedObjectClass&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Tenderer Requirement&lt;/ccts:RepresentationTerm&gt;
     *                      &lt;ccts:Examples&gt;Preregistration in a Business Registry&lt;/ccts:Examples&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                Gets the value of the specificTendererRequirement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the specificTendererRequirement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpecificTendererRequirement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TendererRequirementType }
     * 
     * 
     */
    public List<TendererRequirementType> getSpecificTendererRequirement() {
        if (specificTendererRequirement == null) {
            specificTendererRequirement = new ArrayList<TendererRequirementType>();
        }
        return this.specificTendererRequirement;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Tenderer Qualification Request. Economic Operator Role&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;A class to describe the tenderer contracting role.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..n&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Tenderer Qualification Request&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Economic Operator Role&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:AssociatedObjectClass&gt;Economic Operator Role&lt;/ccts:AssociatedObjectClass&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Economic Operator Role&lt;/ccts:RepresentationTerm&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                Gets the value of the economicOperatorRole property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the economicOperatorRole property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEconomicOperatorRole().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EconomicOperatorRoleType }
     * 
     * 
     */
    public List<EconomicOperatorRoleType> getEconomicOperatorRole() {
        if (economicOperatorRole == null) {
            economicOperatorRole = new ArrayList<EconomicOperatorRoleType>();
        }
        return this.economicOperatorRole;
    }

}
