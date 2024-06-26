//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.10 at 03:06:42 PM COT 
//


package sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:qdt="urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2" xmlns:udt="urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;&lt;ccts:ComponentType&gt;ABIE&lt;/ccts:ComponentType&gt;&lt;ccts:DictionaryEntryName&gt;Consolidated Invoice Line Details&lt;/ccts:DictionaryEntryName&gt;&lt;ccts:Definition&gt;Information about a Consolidated Invoice Line.&lt;/ccts:Definition&gt;&lt;ccts:ObjectClass&gt;Consolidated  Invoice Line&lt;/ccts:ObjectClass&gt;
 *
 * 				&lt;/ccts:Component&gt;
 * </pre>
 * <p/>
 * <p/>
 * <p/>
 * <p>Java class for AdditionalInformationType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="AdditionalInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1}AdditionalMonetaryTotal" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1}AdditionalProperty" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalInformationType", propOrder = {
        "additionalMonetaryTotal",
        "additionalProperty",
        "sunatEmbededDespatchAdvice",
        "sunatTransaction",
        "sunatCosts",
        "sunatDeductions"
})
@XmlRootElement(name = "AdditionalInformation")
public class AdditionalInformationType {

    @XmlElement(name = "AdditionalMonetaryTotal")
    protected List<AdditionalMonetaryTotalType> additionalMonetaryTotal;
    @XmlElement(name = "AdditionalProperty")
    protected List<AdditionalPropertyType> additionalProperty;

    @XmlElement(name = "SUNATEmbededDespatchAdvice")
    protected SunatEmbededDespatchAdviceType sunatEmbededDespatchAdvice;

    @XmlElement(name = "SUNATTransaction")
    protected SunatTransactionType sunatTransaction;
    
    @XmlElement(name = "SUNATCosts")
    protected SunatCostsType sunatCosts;
    
    @XmlElement(name = "SUNATDeductions")
    protected SunatDeductionsType sunatDeductions;

    public SunatTransactionType getSunatTransactionType() {
        if (sunatTransaction == null) {
            sunatTransaction = new SunatTransactionType();
        }
        return sunatTransaction;
    }

    public void setSunatTransactionType(SunatTransactionType sunatTransaction) {
        this.sunatTransaction = sunatTransaction;
    }

    public SunatEmbededDespatchAdviceType getSunatEmbededDespatchAdvice() {
        if (sunatEmbededDespatchAdvice == null) {
            sunatEmbededDespatchAdvice = new SunatEmbededDespatchAdviceType();
        }
        return sunatEmbededDespatchAdvice;
    }

    public void setSunatEmbededDespatchAdvice(SunatEmbededDespatchAdviceType sunatEmbededDespatchAdvice) {
        this.sunatEmbededDespatchAdvice = sunatEmbededDespatchAdvice;
    }

    /**
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:qdt="urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2" xmlns:udt="urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;&lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;&lt;ccts:DictionaryEntryName&gt;Consolidated  Invoice Line. Identifier&lt;/ccts:DictionaryEntryName&gt;&lt;ccts:Definition&gt;Identifies the Consolidated  Invoice Line.&lt;/ccts:Definition&gt;&lt;ccts:Cardinality&gt;1&lt;/ccts:Cardinality&gt;&lt;ccts:ObjectClass&gt;Consolidated Invoice Line&lt;/ccts:ObjectClass&gt;&lt;ccts:PropertyTerm&gt;Identifier&lt;/ccts:PropertyTerm&gt;&lt;ccts:RepresentationTerm&gt;Identifier&lt;/ccts:RepresentationTerm&gt;&lt;ccts:DataType&gt;Identifier. Type&lt;/ccts:DataType&gt;
     *
     * 						&lt;/ccts:Component&gt;
     * </pre>
     * <p/>
     * Gets the value of the additionalMonetaryTotal property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the additionalMonetaryTotal property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdditionalMonetaryTotal().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link AdditionalMonetaryTotalType }
     */
    public List<AdditionalMonetaryTotalType> getAdditionalMonetaryTotal() {
        if (additionalMonetaryTotal == null) {
            additionalMonetaryTotal = new ArrayList<AdditionalMonetaryTotalType>();
        }
        return this.additionalMonetaryTotal;
    }

    /**
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:qdt="urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2" xmlns:udt="urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;&lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;&lt;ccts:DictionaryEntryName&gt;Consolidated Invoice Line Type Code. Code&lt;/ccts:DictionaryEntryName&gt;&lt;ccts:Definition&gt;Code specifying the type of the Invoice.&lt;/ccts:Definition&gt;&lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;&lt;ccts:ObjectClass&gt;Consolidated Invoice Line&lt;/ccts:ObjectClass&gt;&lt;ccts:PropertyTerm&gt;Consolidated Invoice Line Type Code&lt;/ccts:PropertyTerm&gt;&lt;ccts:RepresentationTerm&gt;Code&lt;/ccts:RepresentationTerm&gt;&lt;ccts:DataType&gt;Code. Type&lt;/ccts:DataType&gt;
     *
     * 						&lt;/ccts:Component&gt;
     * </pre>
     * <p/>
     * Gets the value of the additionalProperty property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the additionalProperty property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdditionalProperty().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link AdditionalPropertyType }
     */
    public List<AdditionalPropertyType> getAdditionalProperty() {
        if (additionalProperty == null) {
            additionalProperty = new ArrayList<AdditionalPropertyType>();
        }
        return this.additionalProperty;
    }

    //HNA - 30Oct2015: RS185    
    public SunatCostsType getSunatCosts() {
        if (sunatCosts == null) {
            sunatCosts = new SunatCostsType();
        }
        return sunatCosts;
    }

    public void setCosts(SunatCostsType sunatCosts) {
        this.sunatCosts = sunatCosts;
    }

    public SunatDeductionsType getSunatDeductions() {
        if (sunatDeductions == null) {
            sunatDeductions = new SunatDeductionsType();
        }
        return sunatDeductions;
    }

    public void setDeductions(SunatDeductionsType sunatDeductions) {
        this.sunatDeductions = sunatDeductions;
    }    
    

}
