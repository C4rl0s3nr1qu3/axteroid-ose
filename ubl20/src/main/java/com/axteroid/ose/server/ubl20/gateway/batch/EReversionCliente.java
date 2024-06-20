package com.axteroid.ose.server.ubl20.gateway.batch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * User: HNA
 * Date: 23/12/15
 */


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Reversion", propOrder = {
        "indicador",
        "razonSocialEmisor",
        "numeroRucEmisor",
        "tipoDocumentoEmisor",
        "fechaDocumentoRevertido",
        "serieNumeroReversion",
        "fechaGeneracionReversion",
        "correoEmisor",
        "reversionTipo",
        "inHabilitado",
        "items"
})
@XmlRootElement(name = "Reversion")
public class EReversionCliente {
	
	public String indicador;
	public String razonSocialEmisor;
	public String numeroRucEmisor;
	public String tipoDocumentoEmisor;
	public Date fechaDocumentoRevertido;
	public String serieNumeroReversion;
	public Date fechaGeneracionReversion;

	public String correoEmisor;
    public String reversionTipo;
    public String inHabilitado;

    @XmlElement(name = "ReversionItem", nillable = true)
    List<EReversionItemCliente> items = new ArrayList<EReversionItemCliente>();

	public String getIndicador() {
		return indicador;
	}

	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}

	public String getRazonSocialEmisor() {
		return razonSocialEmisor;
	}

	public void setRazonSocialEmisor(String razonSocialEmisor) {
		this.razonSocialEmisor = razonSocialEmisor;
	}

	public String getNumeroRucEmisor() {
		return numeroRucEmisor;
	}

	public void setNumeroRucEmisor(String numeroRucEmisor) {
		this.numeroRucEmisor = numeroRucEmisor;
	}

	public String getTipoDocumentoEmisor() {
		return tipoDocumentoEmisor;
	}

	public void setTipoDocumentoEmisor(String tipoDocumentoEmisor) {
		this.tipoDocumentoEmisor = tipoDocumentoEmisor;
	}

	public Date getFechaDocumentoRevertido() {
		return fechaDocumentoRevertido;
	}

	public void setFechaDocumentoRevertido(Date fechaDocumentoRevertido) {
		this.fechaDocumentoRevertido = fechaDocumentoRevertido;
	}

	public String getSerieNumeroReversion() {
		return serieNumeroReversion;
	}

	public void setSerieNumeroReversion(String serieNumeroReversion) {
		this.serieNumeroReversion = serieNumeroReversion;
	}

	public Date getFechaGeneracionReversion() {
		return fechaGeneracionReversion;
	}

	public void setFechaGeneracionReversion(Date fechaGeneracionReversion) {
		this.fechaGeneracionReversion = fechaGeneracionReversion;
	}

	public String getCorreoEmisor() {
		return correoEmisor;
	}

	public void setCorreoEmisor(String correoEmisor) {
		this.correoEmisor = correoEmisor;
	}

	public String getReversionTipo() {
		return reversionTipo;
	}

	public void setReversionTipo(String reversionTipo) {
		this.reversionTipo = reversionTipo;
	}

	public String getInHabilitado() {
		return inHabilitado;
	}

	public void setInHabilitado(String inHabilitado) {
		this.inHabilitado = inHabilitado;
	}

	public List<EReversionItemCliente> getItems() {
		return items;
	}

	public void setItems(List<EReversionItemCliente> items) {
		this.items = items;
	}
}
