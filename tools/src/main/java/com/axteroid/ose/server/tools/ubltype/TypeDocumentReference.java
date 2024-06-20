package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class TypeDocumentReference {

    protected String id;
    protected String copyIndicator;
    protected String uuid;
    protected String issueDate;
    protected String documentTypeCode;
    protected String documentType;
    protected List<String> xPath;
    protected String attachment;
    protected String documentStatusCode;
    protected TypeParty issuerParty;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCopyIndicator() {
		return copyIndicator;
	}
	public void setCopyIndicator(String copyIndicator) {
		this.copyIndicator = copyIndicator;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getDocumentTypeCode() {
		return documentTypeCode;
	}
	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public List<String> getxPath() {
		return xPath;
	}
	public void setxPath(List<String> xPath) {
		this.xPath = xPath;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getDocumentStatusCode() {
		return documentStatusCode;
	}
	public void setDocumentStatusCode(String documentStatusCode) {
		this.documentStatusCode = documentStatusCode;
	}
	public TypeParty getIssuerParty() {
		return issuerParty;
	}
	public void setIssuerParty(TypeParty issuerParty) {
		this.issuerParty = issuerParty;
	}
    
}
