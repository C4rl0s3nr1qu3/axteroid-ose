package com.axteroid.ose.server.tools.exception;

public class WebserviceConfigurationException extends Exception{
	private static final long serialVersionUID = 1L;
  
	public WebserviceConfigurationException() {}
  

	public WebserviceConfigurationException(String message, Throwable cause){
		super(message, cause);
	}
  
	public WebserviceConfigurationException(String message){
		super(message);
	}
  
	public WebserviceConfigurationException(Throwable cause){
		super(cause);
	}
}
