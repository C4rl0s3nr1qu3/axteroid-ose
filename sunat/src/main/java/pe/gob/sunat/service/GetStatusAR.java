
package pe.gob.sunat.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para GetStatusAR complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="getStatusAR"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="rucComprobante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tipoComprobante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;        
 *         &lt;element name="serieComprobante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroComprobante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;      
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getStatusAR", propOrder = {
	"rucComprobante",	
	"tipoComprobante",
	"serieComprobante",
    "numeroComprobante"
})
public class GetStatusAR {  
    protected String rucComprobante;
    protected String tipoComprobante;
    protected String serieComprobante;
    protected String numeroComprobante;

    /**
     * Obtiene el valor de la propiedad numeroComprobante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    /**
     * Define el valor de la propiedad numeroComprobante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroComprobante(String value) {
        this.numeroComprobante = value;
    }

    /**
     * Obtiene el valor de la propiedad rucComprobante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRucComprobante() {
        return rucComprobante;
    }

    /**
     * Define el valor de la propiedad rucComprobante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRucComprobante(String value) {
        this.rucComprobante = value;
    }

    /**
     * Obtiene el valor de la propiedad serieComprobante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerieComprobante() {
        return serieComprobante;
    }

    /**
     * Define el valor de la propiedad serieComprobante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerieComprobante(String value) {
        this.serieComprobante = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoComprobante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoComprobante() {
        return tipoComprobante;
    }

    /**
     * Define el valor de la propiedad tipoComprobante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoComprobante(String value) {
        this.tipoComprobante = value;
    }

}
