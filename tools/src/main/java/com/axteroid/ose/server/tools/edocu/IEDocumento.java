package com.axteroid.ose.server.tools.edocu;

/**
 * User: RAC
 * Date: 07/03/12
 */
public interface IEDocumento {
    void populateItem();
    void setCoTipoEmision(String value);
    String getCoTipoEmision();
    String getNumeroDocumentoEmisor();
}
