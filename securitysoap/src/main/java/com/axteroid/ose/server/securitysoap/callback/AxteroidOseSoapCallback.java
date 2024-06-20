package com.axteroid.ose.server.securitysoap.callback;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.securitysoap.interceptor.AxteroidOseSoapSecurityInterceptor;

public class AxteroidOseSoapCallback implements CallbackHandler{
	private static final Logger log = LoggerFactory.getLogger(AxteroidOseSoapCallback.class);	
	
	@Override
	public void handle(Callback[] callbacks){
		try {
			String user = "";
			for (Callback callback : callbacks) {
				WSPasswordCallback pc = (WSPasswordCallback) callback;
				user = pc.getIdentifier();
				String pass = this.getPassword(user);
				if (pass != null) {
					pc.setPassword(pass);
					return;
				}
			}
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("handle Exception \n"+errors);
		}
	}
	
	private String getPassword(String user) {
		AxteroidOseSoapSecurityInterceptor securityInterceptorDAO = new AxteroidOseSoapSecurityInterceptor();
		return securityInterceptorDAO.bucarLoginParametro(user);
	}
}
