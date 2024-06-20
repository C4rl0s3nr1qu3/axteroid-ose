package com.axteroid.ose.server.ubl21.sign.impl;

public class SignDocumentException extends Exception
{

    public SignDocumentException()
    {
        super("No se pudo crear el documento a firmar");
    }

    public SignDocumentException(Throwable e)
    {
        super("No se pudo crear el documento a firmar", e);
    }

    private static final long serialVersionUID = 1L;
}
