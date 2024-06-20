package com.axteroid.ose.server.ubl21.sign;

import java.util.Date;

public interface SignDocumentUbl{

	public byte[] signDocumento(byte[] bytes, String rutaCPE, String id, Date fechaEmision) ;
}
