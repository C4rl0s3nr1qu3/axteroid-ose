
package pe.gob.sunat.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para getStatusResponseAR complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="getStatusResponseAR"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="return" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getStatusResponseAR", propOrder = {
	    "status"
	})
public class GetStatusResponseAR {

	protected StatusResponseAR status;

    /**
     * Obtiene el valor de la propiedad statusAR.
     * 
     * @return
     *     possible object is
     *     {@link StatusResponseAR }
     *     
     */
    public StatusResponseAR getStatus() {
        return status;
    }

    /**
     * Define el valor de la propiedad statusResponseAR.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusResponseAR }
     *     
     */
    public void setStatus(StatusResponseAR value) {
        this.status = value;
    }

}
