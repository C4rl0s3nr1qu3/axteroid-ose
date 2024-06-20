package com.axteroid.ose.server.tools.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumeExceptionRest extends Exception {
	private static final Logger log = LoggerFactory.getLogger(ConsumeExceptionRest.class);
	private static final long serialVersionUID = -3385592278769635286L;

	public ConsumeExceptionRest() {
		super();		
	}

	public ConsumeExceptionRest(String message) {
		super(message);		
	}

	public ConsumeExceptionRest(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ConsumeExceptionRest(String message, Throwable cause) {
		super(message, cause);
	}

	public ConsumeExceptionRest(Throwable cause) {
		super(cause);
	}

	public ConsumeExceptionRest(String message, Exception e){
		super(e.getMessage(), e.getCause());
	  	StringWriter errors = new StringWriter();				
		e.printStackTrace(new PrintWriter(errors));
		log.error(message+" \n"+errors);    
	}
}
