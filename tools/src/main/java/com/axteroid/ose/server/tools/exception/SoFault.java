package com.axteroid.ose.server.tools.exception;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "", namespace = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"faultcode", "faultstring", "detail"})
public class SoFault {
	
	private String faultcode = "soap:Server";
	private String faultstring;
	private String detail;
	
	public String getFaultcode() {
		return faultcode;
	}
	
	public void setFaultcode(String faultcode) {
		this.faultcode = faultcode;
	}

	public String getFaultstring() {
		return faultstring;
	}
	public void setFaultstring(String faultstring) {
		this.faultstring = faultstring;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = "<message>"+detail+"</message>";
	}

}


