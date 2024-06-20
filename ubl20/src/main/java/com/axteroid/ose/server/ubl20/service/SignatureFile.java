package com.axteroid.ose.server.ubl20.service;

import org.w3c.dom.Document;

import com.axteroid.ose.server.ubl20.gateway.ClienteContext;

import java.io.File;

/**
 * User: RAC
 * Date: 21/03/12
 */
public interface SignatureFile {

    public void sign(File signFile, Document doc, ClienteContext ebizContext) throws Exception;

}

