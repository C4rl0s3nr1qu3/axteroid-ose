
package pe.gob.sunat.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para getStatusAR complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="getStatusCdr"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="statusAR" type="{http://service.sunat.gob.pe/}StatusCdr" minOccurs="0"/&gt;
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
    "getStatusAR"
})
public class StatusAR {

    protected GetStatusAR statusAR;

    /**
     * Obtiene el valor de la propiedad statusCdr.
     * 
     * @return
     *     possible object is
     *     {@link StatusAR }
     *     
     */
    public GetStatusAR getStatusAR() {
        return statusAR;
    }

    /**
     * Define el valor de la propiedad statusAR.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusAR }
     *     
     */
    public void setStatusAR(GetStatusAR value) {
        this.statusAR = value;
    }

}
