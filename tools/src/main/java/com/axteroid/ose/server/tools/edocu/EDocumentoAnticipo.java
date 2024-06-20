package com.axteroid.ose.server.tools.edocu;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.math.BigDecimal;

/**
 * Date: 13/02/12
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EDocumentoAnticipo {

    protected String indicador="A";

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

    public BigDecimal getTotalPrepagadoAnticipo() {
        return totalPrepagadoAnticipo;
    }

    public void setTotalPrepagadoAnticipo(BigDecimal totalPrepagadoAnticipo) {
        this.totalPrepagadoAnticipo = totalPrepagadoAnticipo;
    }
}

