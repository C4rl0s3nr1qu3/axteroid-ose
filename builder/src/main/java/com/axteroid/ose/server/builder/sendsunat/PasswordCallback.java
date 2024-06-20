package com.axteroid.ose.server.builder.sendsunat;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.wss4j.common.ext.WSPasswordCallback;

import com.axteroid.ose.server.rulesejb.rules.UBLListParametroLocal;
import com.axteroid.ose.server.rulesejb.rules.impl.UBLListParametroImpl;
import com.axteroid.ose.server.tools.algoritmo.NcCrypt;

public class PasswordCallback  implements CallbackHandler{
	@Override
	public void handle(Callback[] callbacks) throws IOException,
	         UnsupportedCallbackException {
		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
	    String user = pc.getIdentifier();	
	    UBLListParametroLocal ublListParametroLocal = new UBLListParametroImpl();
	    String clave = ublListParametroLocal.bucarLoginParametro(user);
	    String password = "";
		if(clave != null && clave.length()>0) {
    		//String password = NcCrypt.desencriptarPassword(clave);
			password = NcCrypt.desencriptarPassword(clave);
		}
		pc.setPassword(password);    
	  }
}
