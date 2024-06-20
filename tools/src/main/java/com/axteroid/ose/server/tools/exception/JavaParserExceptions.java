package com.axteroid.ose.server.tools.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaParserExceptions {
	private static final Logger log = LoggerFactory.getLogger(JavaParserExceptions.class);

	public static void getParseException(Object e, String nota) {	  
		StringWriter errors = new StringWriter();
		if (e instanceof Exception)	 
			((Exception) e).printStackTrace(new PrintWriter(errors));
		if (e instanceof IOException)	  
			((IOException) e).printStackTrace(new PrintWriter(errors));
		if (e instanceof NoSuchAlgorithmException)	 
			((NoSuchAlgorithmException) e).printStackTrace(new PrintWriter(errors));
		if (e instanceof KeyStoreException)	 
			((KeyStoreException) e).printStackTrace(new PrintWriter(errors));	  
		if (e instanceof CertificateException)	 
			((CertificateException) e).printStackTrace(new PrintWriter(errors));	
		if (e instanceof FileNotFoundException)	 
			((FileNotFoundException) e).printStackTrace(new PrintWriter(errors));	
		log.info(nota+" Exception \n" + errors);		  
	}		
	
	
	public StringBuilder parserRuntimeException(RuntimeException e){			
		StringBuilder sb = convertirStringBuilderMessage(e.getMessage());
		StackTraceElement[] c = e.getStackTrace();
		return convertirStringBuilderTrace(c,sb);
	}
	
	public StringBuilder parserException(Exception e){		
		StringBuilder sb = convertirStringBuilderMessage(e.getMessage());
		StackTraceElement[] c = e.getStackTrace();
		return convertirStringBuilderTrace(c,sb);
	}
	public StringBuilder parserIOException(IOException e){		
		StringBuilder sb = convertirStringBuilderMessage(e.getMessage());
		StackTraceElement[] c = e.getStackTrace();
		return convertirStringBuilderTrace(c,sb);
	}
	
	public StringBuilder parserMalformedURLException(MalformedURLException e){	
		StringBuilder sb = convertirStringBuilderMessage(e.getMessage());
		StackTraceElement[] c = e.getStackTrace();
		return convertirStringBuilderTrace(c,sb);
	}
	
	public StringBuilder parserInterruptedException(InterruptedException e){	
		StringBuilder sb = convertirStringBuilderMessage(e.getMessage());
		StackTraceElement[] c = e.getStackTrace();
		return convertirStringBuilderTrace(c,sb);
	}
	
	public StringBuilder convertirStringBuilderMessage(String c){
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html>\n<head>\n<style>\n");
		sb.append("table { width: 100% }\n");
		sb.append("th { font:bold 10pt Tahoma; }\n");
		sb.append("td { font:normal 8pt Tahoma; }\n");
		sb.append("h1 {font:normal 11pt Tahoma;}\n");
		sb.append("</style>\n");
		sb.append("</head>\n");
		sb.append("<body>\n");
		sb.append("<table>");		
		sb.append("<tr><th>"+c.toUpperCase()+"</th></tr>");
		return sb;
	}
	
	public StringBuilder convertirStringBuilderTrace(StackTraceElement[] c, StringBuilder sb){
		for (int i=0; i< c.length;i++){			
			sb.append("<tr><td>"+c[i].toString()+"</td></tr>");
		}
		sb.append("</table>\n</body>\n</html>");
		return sb;
	}
}
