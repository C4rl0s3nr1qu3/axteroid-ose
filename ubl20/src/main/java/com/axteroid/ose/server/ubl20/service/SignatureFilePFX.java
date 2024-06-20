package com.axteroid.ose.server.ubl20.service;

import org.w3c.dom.Document;

import com.axteroid.ose.server.ubl20.gateway.ClienteContext;

import java.io.File;

/**
 * User: RAC
 * Date: 21/03/12
 */
public interface SignatureFilePFX extends SignatureFile{

    public void sign(File signatureFile, Document doc, ClienteContext ebizContext) throws Exception;

}

