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

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.MaximumNumberNumericType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.MinimumNumberNumericType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.OptionsDescriptionType;


/**
 * 
 *             
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
 *                &lt;ccts:ComponentType&gt;ABIE&lt;/ccts:ComponentType&gt;
 *                &lt;ccts:DictionaryEntryName&gt;Contract Extension. Details&lt;/ccts:DictionaryEntryName&gt;
 *                &lt;ccts:Definition&gt;A class to describe possible extensions to a contract.&lt;/ccts:Definition&gt;
 *                &lt;ccts:ObjectClass&gt;Contract Extension&lt;/ccts:ObjectClass&gt;
 *             &lt;/ccts:Component&gt;
 * </pre>
 * 
 *          
 * 
 * <p>Clase Java para ContractExtensionType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ContractExtensionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}OptionsDescription" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}MinimumNumberNumeric" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}MaximumNumberNumeric" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}OptionValidityPeriod" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}Renewal" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContractExtensionType", propOrder = {
    "optionsDescription",
    "minimumNumberNumeric",
    "maximumNumberNumeric",
    "optionValidityPeriod",
    "renewal"
})
public class ContractExtensionType {

    @XmlElement(name = "OptionsDescription", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected List<OptionsDescriptionType> optionsDescription;
    @XmlElement(name = "MinimumNumberNumeric", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected MinimumNumberNumericType minimumNumberNumeric;
    @XmlElement(name = "MaximumNumberNumeric", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected MaximumNumberNumericType maximumNumberNumeric;
    @XmlElement(name = "OptionValidityPeriod")
    protected PeriodType optionValidityPeriod;
    @XmlElement(name = "Renewal")
    protected List<RenewalType> renewal;

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Contract Extension. Options Description. Text&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;A description for the possible options that can be carried out during the execution of the contract.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..n&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Contract Extension&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Options Description&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Text&lt;/ccts:RepresentationTerm&gt;
     *                      &lt;ccts:DataType&gt;Text. Type&lt;/ccts:DataType&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                Gets the value of the optionsDescription property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the optionsDescription property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOptionsDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OptionsDescriptionType }
     * 
     * 
     */
    public List<OptionsDescriptionType> getOptionsDescription() {
        if (optionsDescription == null) {
            optionsDescription = new ArrayList<OptionsDescriptionType>();
        }
        return this.optionsDescription;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Contract Extension. Minimum_ Number. Numeric&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;The fixed minimum number of contract extensions or renewals.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Contract Extension&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTermQualifier&gt;Minimum&lt;/ccts:PropertyTermQualifier&gt;
     *                      &lt;ccts:PropertyTerm&gt;Number&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Numeric&lt;/ccts:RepresentationTerm&gt;
     *                      &lt;ccts:DataType&gt;Numeric. Type&lt;/ccts:DataType&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                
     * 
     * @return
     *     possible object is
     *     {@link MinimumNumberNumericType }
     *     
     */
    public MinimumNumberNumericType getMinimumNumberNumeric() {
        return minimumNumberNumeric;
    }

    /**
     * Define el valor de la propiedad minimumNumberNumeric.
     * 
     * @param value
     *     allowed object is
     *     {@link MinimumNumberNumericType }
     *     
     */
    public void setMinimumNumberNumeric(MinimumNumberNumericType value) {
        this.minimumNumberNumeric = value;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Contract Extension. Maximum_ Number. Numeric&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;The maximum allowed number of contract extensions.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Contract Extension&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTermQualifier&gt;Maximum&lt;/ccts:PropertyTermQualifier&gt;
     *                      &lt;ccts:PropertyTerm&gt;Number&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Numeric&lt;/ccts:RepresentationTerm&gt;
     *                      &lt;ccts:DataType&gt;Numeric. Type&lt;/ccts:DataType&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                
     * 
     * @return
     *     possible object is
     *     {@link MaximumNumberNumericType }
     *     
     */
    public MaximumNumberNumericType getMaximumNumberNumeric() {
        return maximumNumberNumeric;
    }

    /**
     * Define el valor de la propiedad maximumNumberNumeric.
     * 
     * @param value
     *     allowed object is
     *     {@link MaximumNumberNumericType }
     *     
     */
    public void setMaximumNumberNumeric(MaximumNumberNumericType value) {
        this.maximumNumberNumeric = value;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Contract Extension. Option Validity_ Period. Period&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;The period during which the option for extending the contract is available.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Contract Extension&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTermQualifier&gt;Option Validity&lt;/ccts:PropertyTermQualifier&gt;
     *                      &lt;ccts:PropertyTerm&gt;Period&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:AssociatedObjectClass&gt;Period&lt;/ccts:AssociatedObjectClass&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Period&lt;/ccts:RepresentationTerm&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                
     * 
     * @return
     *     possible object is
     *     {@link PeriodType }
     *     
     */
    public PeriodType getOptionValidityPeriod() {
        return optionValidityPeriod;
    }

    /**
     * Define el valor de la propiedad optionValidityPeriod.
     * 
     * @param value
     *     allowed object is
     *     {@link PeriodType }
     *     
     */
    public void setOptionValidityPeriod(PeriodType value) {
        this.optionValidityPeriod = value;
    }

    /**
     * 
     *                   
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
     *                      &lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;
     *                      &lt;ccts:DictionaryEntryName&gt;Contract Extension. Renewal&lt;/ccts:DictionaryEntryName&gt;
     *                      &lt;ccts:Definition&gt;The period allowed for each contract extension.&lt;/ccts:Definition&gt;
     *                      &lt;ccts:Cardinality&gt;0..n&lt;/ccts:Cardinality&gt;
     *                      &lt;ccts:ObjectClass&gt;Contract Extension&lt;/ccts:ObjectClass&gt;
     *                      &lt;ccts:PropertyTerm&gt;Renewal&lt;/ccts:PropertyTerm&gt;
     *                      &lt;ccts:AssociatedObjectClass&gt;Renewal&lt;/ccts:AssociatedObjectClass&gt;
     *                      &lt;ccts:RepresentationTerm&gt;Renewal&lt;/ccts:RepresentationTerm&gt;
     *                   &lt;/ccts:Component&gt;
     * </pre>
     * 
     *                Gets the value of the renewal property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the renewal property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRenewal().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RenewalType }
     * 
     * 
     */
    public List<RenewalType> getRenewal() {
        if (renewal == null) {
            renewal = new ArrayList<RenewalType>();
        }
        return this.renewal;
    }

}
