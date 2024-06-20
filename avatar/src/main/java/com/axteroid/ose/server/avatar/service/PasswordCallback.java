package com.axteroid.ose.server.avatar.service;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.wss4j.common.ext.WSPasswordCallback;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.jdbc.NcCrypt;

public class PasswordCallback  implements CallbackHandler{
	//private static final Logger log = LoggerFactory.getLogger(PasswordCallback.class);

	@Override
	   public void handle(Callback[] callbacks) throws IOException,
	         UnsupportedCallbackException {
	      WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
	      String user = pc.getIdentifier();	      
	      String password = NcCrypt.getWSPasswordProd(user);
	      pc.setPassword(password);
	      
	   }
}
