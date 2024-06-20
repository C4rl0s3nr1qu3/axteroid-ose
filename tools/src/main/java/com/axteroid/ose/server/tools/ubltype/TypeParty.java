package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class TypeParty {
    protected String markCareIndicator;
    protected String markAttentionIndicator;
    protected String websiteURI;
    protected String logoReferenceID;
    protected String endpointID;
    protected List<TypePartyIdentification> partyIdentification;
    protected List<String> partyName;
    protected String language;
    protected TypeAddress postalAddress;
    protected String physicalLocation;
    protected List<String> partyTaxScheme;
    protected List<TypePartyLegalEntity> partyLegalEntity;
    protected String contact;
    protected String person;
    protected String agentParty;
	public String getMarkCareIndicator() {
		return markCareIndicator;
	}
	public void setMarkCareIndicator(String markCareIndicator) {
		this.markCareIndicator = markCareIndicator;
	}
	public String getMarkAttentionIndicator() {
		return markAttentionIndicator;
	}
	public void setMarkAttentionIndicator(String markAttentionIndicator) {
		this.markAttentionIndicator = markAttentionIndicator;
	}
	public String getWebsiteURI() {
		return websiteURI;
	}
	public void setWebsiteURI(String websiteURI) {
		this.websiteURI = websiteURI;
	}
	public String getLogoReferenceID() {
		return logoReferenceID;
	}
	public void setLogoReferenceID(String logoReferenceID) {
		this.logoReferenceID = logoReferenceID;
	}
	public String getEndpointID() {
		return endpointID;
	}
	public void setEndpointID(String endpointID) {
		this.endpointID = endpointID;
	}
	public List<String> getPartyName() {
		return partyName;
	}
	public void setPartyName(List<String> partyName) {
		this.partyName = partyName;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public TypeAddress getPostalAddress() {
		return postalAddress;
	}
	public void setPostalAddress(TypeAddress postalAddress) {
		this.postalAddress = postalAddress;
	}
	public String getPhysicalLocation() {
		return physicalLocation;
	}
	public void setPhysicalLocation(String physicalLocation) {
		this.physicalLocation = physicalLocation;
	}
	public List<String> getPartyTaxScheme() {
		return partyTaxScheme;
	}
	public void setPartyTaxScheme(List<String> partyTaxScheme) {
		this.partyTaxScheme = partyTaxScheme;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getAgentParty() {
		return agentParty;
	}
	public void setAgentParty(String agentParty) {
		this.agentParty = agentParty;
	}
	public List<TypePartyLegalEntity> getPartyLegalEntity() {
		return partyLegalEntity;
	}
	public void setPartyLegalEntity(List<TypePartyLegalEntity> partyLegalEntity) {
		this.partyLegalEntity = partyLegalEntity;
	}
	public List<TypePartyIdentification> getPartyIdentification() {
		return partyIdentification;
	}
	public void setPartyIdentification(List<TypePartyIdentification> partyIdentification) {
		this.partyIdentification = partyIdentification;
	}
    
}
