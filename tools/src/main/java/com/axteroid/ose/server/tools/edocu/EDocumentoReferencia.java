package com.axteroid.ose.server.tools.edocu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;


/**
 * Date: 13/02/12
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EDocumentoReferencia {

    protected String indicador;

    protected String tipoDocumentoReferencia;

    protected String numeroDocumentoReferencia;

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public String getTipoDocumentoReferencia() {
        return tipoDocumentoReferencia;
    }

    public void setTipoDocumentoReferencia(String tipoDocumentoReferencia) {
        this.tipoDocumentoReferencia = tipoDocumentoReferencia;
    }

    public String getNumeroDocumentoReferencia() {
        return numeroDocumentoReferencia;
    }

    public void setNumeroDocumentoReferencia(String numeroDocumentoReferencia) {
        this.numeroDocumentoReferencia = numeroDocumentoReferencia;
    }
}

