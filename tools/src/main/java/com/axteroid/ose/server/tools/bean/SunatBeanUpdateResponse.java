package com.axteroid.ose.server.tools.bean;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SunatBeanUpdateResponse {

	@NotNull
	private String issuerCode;
	@NotNull
	private String documentType;
	@NotNull
	private String serial;
	@NotNull
	@Min(1L)
	private Long sequence;
	@NotNull
	private Map<String, Object> sunatMap;
	@NotNull
	@Valid
	private ZipFile sunatCdr;
    @NotNull
    private String status;
    
	private Long sunatTicket;    
	

	
	public Map<String, Object> getSunatMap() {
		return sunatMap;
	}
	public void setSunatMap(Map<String, Object> sunatMap) {
		this.sunatMap = sunatMap;
	}
	public ZipFile getSunatCdr() {
		return sunatCdr;
	}
	public void setSunatCdr(ZipFile sunatCdr) {
		this.sunatCdr = sunatCdr;
	}
	public String getIssuerCode() {
		return issuerCode;
	}
	public void setIssuerCode(String issuerCode) {
		this.issuerCode = issuerCode;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public Long getSequence() {
		return sequence;
	}
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getSunatTicket() {
		return sunatTicket;
	}
	public void setSunatTicket(Long sunatTicket) {
		this.sunatTicket = sunatTicket;
	}
	
	
}
