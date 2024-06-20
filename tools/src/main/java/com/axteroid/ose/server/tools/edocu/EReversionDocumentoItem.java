package com.axteroid.ose.server.tools.edocu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;


@XmlAccessorType(XmlAccessType.FIELD)
public class EReversionDocumentoItem {

    /**
     * parametro
     * sac:VoidedDocumentsLine/cbc:DocumentTypeCode
     */
	public String tipoDocumentoRevertido;
    /**
     * parametro
     * sac:VoidedDocumentsLine/sac:DocumentSerialID
     */
	public String serieDocumentoRevertido;
    /**
     * parametro
     * sac:VoidedDocumentsLine/sac:DocumentNumberID
     */
	public String correlativoDocRevertido;
    /**
     * parametro
     * sac:VoidedDocumentsLine/sac:VoidReasonDescription
     */
	public String motivoReversion;
    /**
     * parametro
     * sac:VoidedDocumentsLine/cbc:LineID
     */
	@NotBlank(message = "7047")
    public String numeroOrdenItem;
	
    //@Transient
    //private String serieNumeroDocRevertido;	
	
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
	
    public String getIdDocumento() {
        return serieDocumentoRevertido + "-" + correlativoDocRevertido;
    }
    
    public String getSerieNumeroDocRevertido() {
    	return serieDocumentoRevertido + "-" + correlativoDocRevertido;
	}
	public void setSerieNumeroDocRevertido(String serieNumeroDocRevertido) {
        if (StringUtils.isBlank(serieNumeroDocRevertido)) return;
        String[] values = StringUtils.split(serieNumeroDocRevertido, "-");
        serieDocumentoRevertido = values[0];
        correlativoDocRevertido = values[1];
	}
}
