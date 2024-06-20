package com.axteroid.ose.server.tools.bean;

public class PopulationData {
	private Long id;
    private String issuer;
    private String documentType;
    private String serial;
    private Long correlative;       
	private String collection;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
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
	public Long getCorrelative() {
		return correlative;
	}
	public void setCorrelative(Long correlative) {
		this.correlative = correlative;
	}
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
		
}
