package com.axteroid.ose.server.tools.bean;

import java.util.List;

public class CdrSunatResponse {
	private String responseCode; 
	private String description;	
	private List<String> status;
	
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
	public List<String> getStatus() {
		return status;
	}
	public void setStatus(List<String> status) {
		this.status = status;
	}
	
}
