package com.axteroid.ose.server.tools.edocu;

import java.util.Date;

public interface IDocumento {

    public void setCorreoEmisor(String value);
    public void setCorreoAdquiriente(String value);
    public void setInHabilitado(Integer inHabilitado);
    public String getNumeroDocumentoEmisor();
    public String getTipoDocumentoEmisor();
    public String getIdDocumento();
    public String getTipoDocumento();
    public String getSerieDocumento();
    public Date getFechaDocumento();
    public String getNumeroDocumento();
    public String getEstado();
    public String getHashCode();
    public String getSignatureValue();
    public String getTipoDocumentoAdquiriente();
    public String getNumeroDocumentoAdquiriente();
    public String getRazonSocialAdquiriente();
    public String getRazonSocialEmisor();
    public String getCorreoAdquiriente();
    public String getCorreoEmisor();
    public String getCoTipoEmision();
    public void setCoTipoEmision(String value);
    public boolean isExportacion();
    boolean isNotaDebitoPorPenalidad();
}
