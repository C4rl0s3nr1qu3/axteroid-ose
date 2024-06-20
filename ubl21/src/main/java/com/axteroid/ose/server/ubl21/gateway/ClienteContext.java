package com.axteroid.ose.server.ubl21.gateway;

import java.io.File;
import java.util.List;

public interface ClienteContext {
	
    boolean isUseDefaultNCND();
    
    String getKeystoreFile();

    File getKeystoreFileReal();

    String getDir();

    String getUnidadEquivalente(String unidad) ;

    String getKeystorePassword();

    String getPrivateKeystorePassword();

    String getPrivateKeyAlias();

    String getUsuarioSol();

    String getClaveSol();

    String getNombreLogo();

    String getRutaLogo();

    String getNumeroResolucion();

    String getUsuarioEbiz();

    String isAnularRechazoSunat();

    String isUnidadEquivalente();

    List<String> getUnidadesEquivalentes();

    Integer getInFirma();
    
}
