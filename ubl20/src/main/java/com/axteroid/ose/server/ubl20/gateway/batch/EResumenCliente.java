package com.axteroid.ose.server.ubl20.gateway.batch;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: RAC
 * Date: 14/02/12
 */


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Resumen", propOrder = {
        "indicador",
        "tipoDocumentoEmisor",
        "numeroDocumentoEmisor",
        "resumenId",
        "fechaEmisionComprobante",
        "fechaGeneracionResumen",
        "razonSocialEmisor",
        "correoEmisor",
        "resumenTipo",
        "inHabilitado",
        "items"
})
@XmlRootElement(name = "Resumen")
public class EResumenCliente {

    /**
     * parametro
     */
    public String indicador;
    public String numeroDocumentoEmisor;

    /**
     * parametro
     */
    public String tipoDocumentoEmisor;

    /**
     * parametro
     * cbc:ID
     */
    private String resumenId;

    /**
     * parametro
     * cbc:ReferenceDate
     */

    private Date fechaEmisionComprobante;

    /**
     * parametro
     * IssueDate
     */

    private Date fechaGeneracionResumen;

    /**
     * parametro
     * cac:AccountingSupplierParty/cac:Party/cac:PartyLegalEntity/cbc:RegistrationName
     */
    public String razonSocialEmisor;

    public String correoEmisor;

    public String resumenTipo;

    public String inHabilitado;

    @XmlElement(name = "ResumenItem", nillable = true)
    List<EResumenItemCliente> items = new ArrayList<EResumenItemCliente>();


    public EResumenCliente() {
    }



    public EResumenCliente(String tipoDocumentoEmisor, String numeroDocumentoEmisor) {
        this.tipoDocumentoEmisor = tipoDocumentoEmisor;
        this.numeroDocumentoEmisor = numeroDocumentoEmisor;
    }

    public String getNumeroDocumentoEmisor() {
        return numeroDocumentoEmisor;
    }

    public void setNumeroDocumentoEmisor(String numeroDocumentoEmisor) {
        this.numeroDocumentoEmisor = numeroDocumentoEmisor;
    }

    public String getTipoDocumentoEmisor() {
        return tipoDocumentoEmisor;
    }

    public void setTipoDocumentoEmisor(String tipoDocumentoEmisor) {
        this.tipoDocumentoEmisor = tipoDocumentoEmisor;
    }

    public String getResumenId() {
        return resumenId;
    }

    public void setResumenId(String resumenId) {
        this.resumenId = resumenId;
    }

    public Date getFechaEmisionComprobante() {
        return fechaEmisionComprobante;
    }

    public void setFechaEmisionComprobante(Date fechaEmisionComprobante) {
        this.fechaEmisionComprobante = fechaEmisionComprobante;
    }

    public Date getFechaGeneracionResumen() {
        return fechaGeneracionResumen;
    }

    public void setFechaGeneracionResumen(Date fechaGeneracionResumen) {
        this.fechaGeneracionResumen = fechaGeneracionResumen;
    }

    public String getRazonSocialEmisor() {
        return razonSocialEmisor;
    }

    public void setRazonSocialEmisor(String razonSocialEmisor) {
        this.razonSocialEmisor = razonSocialEmisor;
    }

    public String getCorreoEmisor() {
        return correoEmisor;
    }

    public void setCorreoEmisor(String correoEmisor) {
        this.correoEmisor = correoEmisor;
    }

    public String getResumenTipo() {
        return resumenTipo;
    }

    public void setResumenTipo(String resumenTipo) {
        this.resumenTipo = resumenTipo;
    }


    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public List<EResumenItemCliente> getItems() {
        return items;
    }

    public void setItems(List<EResumenItemCliente> items) {
        this.items = items;
    }

    public String getInHabilitado() {
        return inHabilitado;
    }

    public void setInHabilitado(String inHabilitado) {
        this.inHabilitado = inHabilitado;
    }
}
