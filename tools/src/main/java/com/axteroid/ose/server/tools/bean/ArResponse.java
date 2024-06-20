package com.axteroid.ose.server.tools.bean;

import java.util.List;

public class ArResponse {
	private String responseCode; 
	private String description;	
	private List<String> note;
	
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getNote() {
		return note;
	}
	public void setNote(List<String> note) {
		this.note = note;
	}
	
}
