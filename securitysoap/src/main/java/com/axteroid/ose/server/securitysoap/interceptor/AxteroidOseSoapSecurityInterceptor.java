package com.axteroid.ose.server.securitysoap.interceptor;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.rulesejb.rules.UBLListParametroLocal;
import com.axteroid.ose.server.rulesejb.rules.impl.UBLListParametroImpl;
import com.axteroid.ose.server.tools.algoritmo.NcCrypt;
import com.axteroid.ose.server.tools.constantes.Constantes;


public class AxteroidOseSoapSecurityInterceptor {
	private static final Logger log = LoggerFactory.getLogger(AxteroidOseSoapSecurityInterceptor.class);	
	
	public String bucarLoginParametro(String userName){		
		log.info("AxteroidOseServer SOAP: {} | {}",Constantes.OSE_VERSION,userName);
		try {			
			UBLListParametroLocal ublListParametroLocal = new UBLListParametroImpl();
			String clave = ublListParametroLocal.bucarLoginParametro(userName);
			if(clave != null && clave.length()>0) {
    			//String password = NcCrypt.desencriptarPassword(clave);
    			//log.info("bucarLoginParametro "+userName+" - "+password);
    			return NcCrypt.desencriptarPassword(clave);
			}
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarLoginParametro Exception \n {}",errors);
		}
		return "";
	}
}
