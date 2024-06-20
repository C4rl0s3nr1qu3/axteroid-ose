package com.axteroid.ose.server.ubl20.gateway.response;

//import org.hibernate.validator.constraints.NotBlank;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
//import java.util.List;

/**
 * Created by RAZANERO on 21/08/14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "item", propOrder = {
        "numeroOrdenItem",
        "codigoProducto",
        "adicionales"
})
public class DocumentItemResult {

    private String numeroOrdenItem;
    private String codigoProducto;
    @XmlElement(name = "adicionales", nillable = true)
    protected AdicionalGroup adicionales;

    public String getNumeroOrdenItem() {
        return numeroOrdenItem;
    }

    public void setNumeroOrdenItem(String numeroOrdenItem) {
        this.numeroOrdenItem = numeroOrdenItem;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public AdicionalGroup getAdicionales() {
        return adicionales;
    }

    public void setAdicionales(AdicionalGroup adicionales) {
        this.adicionales = adicionales;
    }
}
