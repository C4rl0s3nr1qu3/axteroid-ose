package com.axteroid.ose.server.tools.bean;

public class Error {
	private String codError;
	private String desError;
	
	public String getCodError() {
		return codError;
	}
	public void setCodError(String codError) {
		this.codError = codError;
	}
	public String getDesError() {
		return desError;
	}
	public void setDesError(String desError) {
		this.desError = desError;
	}
	@Override
	public String toString() {
		return "Error [codError=" + codError + ", desError=" + desError + "]";
	}
}
