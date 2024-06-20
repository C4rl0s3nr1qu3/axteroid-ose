//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacioasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.n de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderoasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.n si se vuelve a compilar el esquema de origen. 
// Generado el: 2017.10.03 a las 12:15:38 PM COT 
//


package org.etsi.uri._01903.v1_3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Clase Java para UnsignedPropertiesType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="UnsignedPropertiesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="UnsignedSignatureProperties" type="{http://uri.etsi.org/01903/v1.3.2#}UnsignedSignaturePropertiesType" minOccurs="0"/&gt;
 *         &lt;element name="UnsignedDataObjectProperties" type="{http://uri.etsi.org/01903/v1.3.2#}UnsignedDataObjectPropertiesType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UnsignedPropertiesType", propOrder = {
    "unsignedSignatureProperties",
    "unsignedDataObjectProperties"
})
public class UnsignedPropertiesType {

    @XmlElement(name = "UnsignedSignatureProperties")
    protected UnsignedSignaturePropertiesType unsignedSignatureProperties;
    @XmlElement(name = "UnsignedDataObjectProperties")
    protected UnsignedDataObjectPropertiesType unsignedDataObjectProperties;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Obtiene el valor de la propiedad unsignedSignatureProperties.
     * 
     * @return
     *     possible object is
     *     {@link UnsignedSignaturePropertiesType }
     *     
     */
    public UnsignedSignaturePropertiesType getUnsignedSignatureProperties() {
        return unsignedSignatureProperties;
    }

    /**
     * Define el valor de la propiedad unsignedSignatureProperties.
     * 
     * @param value
     *     allowed object is
     *     {@link UnsignedSignaturePropertiesType }
     *     
     */
    public void setUnsignedSignatureProperties(UnsignedSignaturePropertiesType value) {
        this.unsignedSignatureProperties = value;
    }

    /**
     * Obtiene el valor de la propiedad unsignedDataObjectProperties.
     * 
     * @return
     *     possible object is
     *     {@link UnsignedDataObjectPropertiesType }
     *     
     */
    public UnsignedDataObjectPropertiesType getUnsignedDataObjectProperties() {
        return unsignedDataObjectProperties;
    }

    /**
     * Define el valor de la propiedad unsignedDataObjectProperties.
     * 
     * @param value
     *     allowed object is
     *     {@link UnsignedDataObjectPropertiesType }
     *     
     */
    public void setUnsignedDataObjectProperties(UnsignedDataObjectPropertiesType value) {
        this.unsignedDataObjectProperties = value;
    }

    /**
     * Obtiene el valor de la propiedad id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
