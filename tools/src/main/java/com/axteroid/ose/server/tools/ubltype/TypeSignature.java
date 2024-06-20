package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class TypeSignature {

    protected TypeSignedInfo signedInfo;
    protected TypeSignatureValue signatureValue;
    protected TypeKeyInfo keyInfo;
    protected List<String> object;
    protected String id;
    
	public TypeSignedInfo getSignedInfo() {
		return signedInfo;
	}
	public void setSignedInfo(TypeSignedInfo signedInfo) {
		this.signedInfo = signedInfo;
	}
	public TypeSignatureValue getSignatureValue() {
		return signatureValue;
	}
	public void setSignatureValue(TypeSignatureValue signatureValue) {
		this.signatureValue = signatureValue;
	}
	public TypeKeyInfo getKeyInfo() {
		return keyInfo;
	}
	public void setKeyInfo(TypeKeyInfo keyInfo) {
		this.keyInfo = keyInfo;
	}
	public List<String> getObject() {
		return object;
	}
	public void setObject(List<String> object) {
		this.object = object;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
