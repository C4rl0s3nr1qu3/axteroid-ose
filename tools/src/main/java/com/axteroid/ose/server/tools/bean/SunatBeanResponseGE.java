package com.axteroid.ose.server.tools.bean;

import java.util.List;

public class SunatBeanResponseGE {
	private String cod;
	private String msg;
	private String exc;
	private List<Error> errors;
	
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getExc() {
		return exc;
	}
	public void setExc(String exc) {
		this.exc = exc;
	}
	public List<Error> getErrors() {
		return errors;
	}
	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}
	
	@Override
	public String toString() {
		return "SunatBeanResponseGE [cod=" + cod + ", msg=" + msg + ", exc=" + exc + ", errors=" + errors + "]";
	}	
	
}
