package com.axteroid.ose.server.ubl20.gateway.batch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * User: RAC
 * Date: 14/02/12
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReversionItem", propOrder = {
        "indicador",
        "tipoDocumentoRevertido",
        "serieDocumentoRevertido",
        "correlativoDocRevertido",
        "motivoReversion",
        "numeroOrdenItem"
})        
public class EReversionItemCliente {

	public String indicador;
	public String tipoDocumentoRevertido;
	public String serieDocumentoRevertido;
	public String correlativoDocRevertido;
	public String motivoReversion;
    public String numeroOrdenItem;
    
	public String getIndicador() {
		return indicador;
	}
	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}
	public String getTipoDocumentoRevertido() {
		return tipoDocumentoRevertido;
	}
	public void setTipoDocumentoRevertido(String tipoDocumentoRevertido) {
		this.tipoDocumentoRevertido = tipoDocumentoRevertido;
	}
	public String getSerieDocumentoRevertido() {
		return serieDocumentoRevertido;
	}
	public void setSerieDocumentoRevertido(String serieDocumentoRevertido) {
		this.serieDocumentoRevertido = serieDocumentoRevertido;
	}
	public String getCorrelativoDocRevertido() {
		return correlativoDocRevertido;
	}
	public void setCorrelativoDocRevertido(String correlativoDocRevertido) {
		this.correlativoDocRevertido = correlativoDocRevertido;
	}
	public String getMotivoReversion() {
		return motivoReversion;
	}
	public void setMotivoReversion(String motivoReversion) {
		this.motivoReversion = motivoReversion;
	}
	public String getNumeroOrdenItem() {
		return numeroOrdenItem;
	}
	public void setNumeroOrdenItem(String numeroOrdenItem) {
		this.numeroOrdenItem = numeroOrdenItem;
	}    
}
