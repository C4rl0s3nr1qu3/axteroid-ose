package com.axteroid.ose.server.tools.ubltype;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class TypeReference {

    protected String transforms;
    protected String digestMethod;
    protected byte[] digestValue;
    protected String id;
    protected String uri;
    protected String type;
	public String getTransforms() {
		return transforms;
	}
	public void setTransforms(String transforms) {
		this.transforms = transforms;
	}
	public String getDigestMethod() {
		return digestMethod;
	}
	public void setDigestMethod(String digestMethod) {
		this.digestMethod = digestMethod;
	}
	public byte[] getDigestValue() {
		return digestValue;
	}
	public void setDigestValue(byte[] digestValue) {
		this.digestValue = digestValue;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
