//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.01 at 06:30:20 PM COT 
//


package com.axteroid.ose.server.ubl20.gateway.batch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EDocumentoAnticipo", propOrder = {
        "indicador",
        "tipoDocumentoEmisorAnticipo",
        "numeroDocumentoEmisorAnticipo",
        "tipoDocumentoAnticipo",
        "serieNumeroDocumentoAnticipo",
        "totalPrepagadoAnticipo"
})
public class EDocumentoAnticipoCliente implements Cloneable {

    @XmlElement(required = true)
    protected String indicador;

    private String tipoDocumentoEmisorAnticipo;
    private String numeroDocumentoEmisorAnticipo;

    private String tipoDocumentoAnticipo;
    private String serieNumeroDocumentoAnticipo;
    private BigDecimal totalPrepagadoAnticipo;

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public String getTipoDocumentoEmisorAnticipo() {
        return tipoDocumentoEmisorAnticipo;
    }

    public void setTipoDocumentoEmisorAnticipo(String tipoDocumentoEmisorAnticipo) {
        this.tipoDocumentoEmisorAnticipo = tipoDocumentoEmisorAnticipo;
    }

    public String getNumeroDocumentoEmisorAnticipo() {
        return numeroDocumentoEmisorAnticipo;
    }

    public void setNumeroDocumentoEmisorAnticipo(String numeroDocumentoEmisorAnticipo) {
        this.numeroDocumentoEmisorAnticipo = numeroDocumentoEmisorAnticipo;
    }

    public BigDecimal getTotalPrepagadoAnticipo() {
        return totalPrepagadoAnticipo;
    }

    public void setTotalPrepagadoAnticipo(BigDecimal totalPrepagadoAnticipo) {
        this.totalPrepagadoAnticipo = totalPrepagadoAnticipo;
    }

    public String getTipoDocumentoAnticipo() {
        return tipoDocumentoAnticipo;
    }

    public void setTipoDocumentoAnticipo(String tipoDocumentoAnticipo) {
        this.tipoDocumentoAnticipo = tipoDocumentoAnticipo;
    }

    public String getSerieNumeroDocumentoAnticipo() {
        return serieNumeroDocumentoAnticipo;
    }

    public void setSerieNumeroDocumentoAnticipo(String serieNumeroDocumentoAnticipo) {
        this.serieNumeroDocumentoAnticipo = serieNumeroDocumentoAnticipo;
    }
}
