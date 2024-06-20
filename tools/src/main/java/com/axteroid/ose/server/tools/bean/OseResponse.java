package com.axteroid.ose.server.tools.bean;

public class OseResponse {
	private String success;
	private String message;	
	private String errorCode;
	private String ticket;
	private byte[] cdr;
	private byte[] ubl;
	private String data;
	
	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public byte[] getCdr() {
		return cdr;
	}

	public void setCdr(byte[] cdr) {
		this.cdr = cdr;
	}

	public byte[] getUbl() {
		return ubl;
	}

	public void setUbl(byte[] ubl) {
		this.ubl = ubl;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
