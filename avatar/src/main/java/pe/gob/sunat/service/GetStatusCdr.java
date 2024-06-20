
package pe.gob.sunat.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para getStatusCdr complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="getStatusCdr"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="statusCdr" type="{http://service.sunat.gob.pe/}StatusCdr" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getStatusCdr", propOrder = {
    "statusCdr"
})
public class GetStatusCdr {

    protected StatusCdr statusCdr;

    /**
     * Obtiene el valor de la propiedad statusCdr.
     * 
     * @return
     *     possible object is
     *     {@link StatusCdr }
     *     
     */
    public StatusCdr getStatusCdr() {
        return statusCdr;
    }

    /**
     * Define el valor de la propiedad statusCdr.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusCdr }
     *     
     */
    public void setStatusCdr(StatusCdr value) {
        this.statusCdr = value;
    }

}
