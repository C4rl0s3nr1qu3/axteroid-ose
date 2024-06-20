package com.axteroid.ose.server.sunat.service;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import com.axteroid.ose.server.tools.util.DirUtil;

public class ClientPasswordCallback implements CallbackHandler {

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		String keyPassword = DirUtil.getCertificateKeyPassword();
		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];	     
		//pc.setPassword(Constantes.OSE_JKS_KeyPassword);
		//pc.setPassword(Constantes.OSE_JKS_KeyPassword_B);
		//pc.setPassword("aXTEROID123$");
		pc.setPassword(keyPassword);
	}

}
