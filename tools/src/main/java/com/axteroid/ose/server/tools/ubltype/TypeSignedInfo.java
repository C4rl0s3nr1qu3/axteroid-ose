package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class TypeSignedInfo {

    protected String canonicalizationMethod;
    protected String signatureMethod;
    protected List<TypeReference> listTypeReference;
    protected String id;
    
	public String getCanonicalizationMethod() {
		return canonicalizationMethod;
	}
	public void setCanonicalizationMethod(String canonicalizationMethod) {
		this.canonicalizationMethod = canonicalizationMethod;
	}
	public String getSignatureMethod() {
		return signatureMethod;
	}
	public void setSignatureMethod(String signatureMethod) {
		this.signatureMethod = signatureMethod;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<TypeReference> getListTypeReference() {
		return listTypeReference;
	}
	public void setListTypeReference(List<TypeReference> listTypeReference) {
		this.listTypeReference = listTypeReference;
	}
}
