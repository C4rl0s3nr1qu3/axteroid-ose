//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacioasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.n de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderoasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.n si se vuelve a compilar el esquema de origen. 
// Generado el: 2017.10.03 a las 12:15:38 PM COT 
//


package oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2_1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.NameType;


/**
 * 
 *         A single extension for private use.
 *       
 * 
 * <p>Clase Java para UBLExtensionType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="UBLExtensionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}ID" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}Name" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2}ExtensionAgencyID" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2}ExtensionAgencyName" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2}ExtensionVersionID" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2}ExtensionAgencyURI" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2}ExtensionURI" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2}ExtensionReasonCode" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2}ExtensionReason" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2}ExtensionContent"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UBLExtensionType", propOrder = {
    "id",
    "name",
    "extensionAgencyID",
    "extensionAgencyName",
    "extensionVersionID",
    "extensionAgencyURI",
    "extensionURI",
    "extensionReasonCode",
    "extensionReason",
    "extensionContent"
})
public class UBLExtensionType {

    @XmlElement(name = "ID", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected IDType id;
    @XmlElement(name = "Name", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected NameType name;
    @XmlElement(name = "ExtensionAgencyID")
    protected ExtensionAgencyIDType extensionAgencyID;
    @XmlElement(name = "ExtensionAgencyName")
    protected ExtensionAgencyNameType extensionAgencyName;
    @XmlElement(name = "ExtensionVersionID")
    protected ExtensionVersionIDType extensionVersionID;
    @XmlElement(name = "ExtensionAgencyURI")
    protected ExtensionAgencyURIType extensionAgencyURI;
    @XmlElement(name = "ExtensionURI")
    protected ExtensionURIType extensionURI;
    @XmlElement(name = "ExtensionReasonCode")
    protected ExtensionReasonCodeType extensionReasonCode;
    @XmlElement(name = "ExtensionReason")
    protected ExtensionReasonType extensionReason;
    @XmlElement(name = "ExtensionContent", required = true)
    protected ExtensionContentType extensionContent;

    /**
     * 
     *             An identifier for the Extension assigned by the creator of the extension.
     *           
     * 
     * @return
     *     possible object is
     *     {@link IDType }
     *     
     */
    public IDType getID() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     * @param value
     *     allowed object is
     *     {@link IDType }
     *     
     */
    public void setID(IDType value) {
        this.id = value;
    }

    /**
     * 
     *             A name for the Extension assigned by the creator of the extension.
     *           
     * 
     * @return
     *     possible object is
     *     {@link NameType }
     *     
     */
    public NameType getName() {
        return name;
    }

    /**
     * Define el valor de la propiedad name.
     * 
     * @param value
     *     allowed object is
     *     {@link NameType }
     *     
     */
    public void setName(NameType value) {
        this.name = value;
    }

    /**
     * 
     *             An agency that maintains one or more Extensions.
     *           
     * 
     * @return
     *     possible object is
     *     {@link ExtensionAgencyIDType }
     *     
     */
    public ExtensionAgencyIDType getExtensionAgencyID() {
        return extensionAgencyID;
    }

    /**
     * Define el valor de la propiedad extensionAgencyID.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionAgencyIDType }
     *     
     */
    public void setExtensionAgencyID(ExtensionAgencyIDType value) {
        this.extensionAgencyID = value;
    }

    /**
     * 
     *             The name of the agency that maintains the Extension.
     *           
     * 
     * @return
     *     possible object is
     *     {@link ExtensionAgencyNameType }
     *     
     */
    public ExtensionAgencyNameType getExtensionAgencyName() {
        return extensionAgencyName;
    }

    /**
     * Define el valor de la propiedad extensionAgencyName.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionAgencyNameType }
     *     
     */
    public void setExtensionAgencyName(ExtensionAgencyNameType value) {
        this.extensionAgencyName = value;
    }

    /**
     * 
     *             The version of the Extension.
     *           
     * 
     * @return
     *     possible object is
     *     {@link ExtensionVersionIDType }
     *     
     */
    public ExtensionVersionIDType getExtensionVersionID() {
        return extensionVersionID;
    }

    /**
     * Define el valor de la propiedad extensionVersionID.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionVersionIDType }
     *     
     */
    public void setExtensionVersionID(ExtensionVersionIDType value) {
        this.extensionVersionID = value;
    }

    /**
     * 
     *             A URI for the Agency that maintains the Extension.
     *           
     * 
     * @return
     *     possible object is
     *     {@link ExtensionAgencyURIType }
     *     
     */
    public ExtensionAgencyURIType getExtensionAgencyURI() {
        return extensionAgencyURI;
    }

    /**
     * Define el valor de la propiedad extensionAgencyURI.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionAgencyURIType }
     *     
     */
    public void setExtensionAgencyURI(ExtensionAgencyURIType value) {
        this.extensionAgencyURI = value;
    }

    /**
     * 
     *             A URI for the Extension.
     *           
     * 
     * @return
     *     possible object is
     *     {@link ExtensionURIType }
     *     
     */
    public ExtensionURIType getExtensionURI() {
        return extensionURI;
    }

    /**
     * Define el valor de la propiedad extensionURI.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionURIType }
     *     
     */
    public void setExtensionURI(ExtensionURIType value) {
        this.extensionURI = value;
    }

    /**
     * 
     *             A code for reason the Extension is being included.
     *           
     * 
     * @return
     *     possible object is
     *     {@link ExtensionReasonCodeType }
     *     
     */
    public ExtensionReasonCodeType getExtensionReasonCode() {
        return extensionReasonCode;
    }

    /**
     * Define el valor de la propiedad extensionReasonCode.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionReasonCodeType }
     *     
     */
    public void setExtensionReasonCode(ExtensionReasonCodeType value) {
        this.extensionReasonCode = value;
    }

    /**
     * 
     *             A description of the reason for the Extension.
     *           
     * 
     * @return
     *     possible object is
     *     {@link ExtensionReasonType }
     *     
     */
    public ExtensionReasonType getExtensionReason() {
        return extensionReason;
    }

    /**
     * Define el valor de la propiedad extensionReason.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionReasonType }
     *     
     */
    public void setExtensionReason(ExtensionReasonType value) {
        this.extensionReason = value;
    }

    /**
     * 
     *             The definition of the extension content.
     *           
     * 
     * @return
     *     possible object is
     *     {@link ExtensionContentType }
     *     
     */
    public ExtensionContentType getExtensionContent() {
        return extensionContent;
    }

    /**
     * Define el valor de la propiedad extensionContent.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionContentType }
     *     
     */
    public void setExtensionContent(ExtensionContentType value) {
        this.extensionContent = value;
    }

}
